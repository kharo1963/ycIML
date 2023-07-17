package com.example.bootIML.jcodec.api.transcode;

import static com.example.bootIML.jcodec.common.Format.*;
import static com.example.bootIML.jcodec.common.io.NIOUtils.writableFileChannel;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.example.bootIML.jcodec.codecs.h264.H264Encoder;
import com.example.bootIML.jcodec.codecs.h264.encode.CQPRateControl;
import com.example.bootIML.jcodec.common.AudioCodecMeta;
import com.example.bootIML.jcodec.common.AudioEncoder;
import com.example.bootIML.jcodec.common.AudioFormat;
import com.example.bootIML.jcodec.common.Codec;
import com.example.bootIML.jcodec.common.Format;
import com.example.bootIML.jcodec.common.Muxer;
import com.example.bootIML.jcodec.common.MuxerTrack;
import com.example.bootIML.jcodec.common.VideoCodecMeta;
import com.example.bootIML.jcodec.common.VideoEncoder;
import com.example.bootIML.jcodec.common.VideoEncoder.EncodedFrame;
import com.example.bootIML.jcodec.common.io.IOUtils;
import com.example.bootIML.jcodec.common.io.NIOUtils;
import com.example.bootIML.jcodec.common.io.SeekableByteChannel;
import com.example.bootIML.jcodec.common.logging.Logger;
import com.example.bootIML.jcodec.common.model.AudioBuffer;
import com.example.bootIML.jcodec.common.model.ColorSpace;
import com.example.bootIML.jcodec.common.model.Packet;
import com.example.bootIML.jcodec.common.model.Packet.FrameType;
import com.example.bootIML.jcodec.common.model.Picture;
import com.example.bootIML.jcodec.common.model.Rational;
import com.example.bootIML.jcodec.common.model.Size;
import com.example.bootIML.jcodec.common.model.Unit;
import com.example.bootIML.jcodec.containers.mp4.muxer.CodecMP4MuxerTrack;
import com.example.bootIML.jcodec.containers.mp4.muxer.MP4Muxer;
import com.example.bootIML.jcodec.containers.mp4.muxer.MP4MuxerTrack;

/**
 * The sink that consumes the uncompressed frames and stores them into a
 * compressed file.
 *
 * @author Stanislav Vitvitskiy
 *
 * 2023.06.17 Oleg Kharchenko
 * Saved everything necessary to work with the MOV format and the H.264 codec
 */
public class SinkImpl implements Sink, PacketSink {
    private String destName;
    private SeekableByteChannel destStream;
    private Muxer muxer;
    private MuxerTrack videoOutputTrack;
    private MuxerTrack audioOutputTrack;
    private boolean framesOutput;
    private Codec outputVideoCodec;
    private Codec outputAudioCodec;
    private Format outputFormat;
    // ThreadLocal instances are typically private static fields in classes that
    // wish to associate state with a thread
    // FIXME: potential memory leak: non-static ThreadLocal
    private final ThreadLocal<ByteBuffer> bufferStore;

    private AudioEncoder audioEncoder;
    private VideoEncoder videoEncoder;
    private String profile;
    private boolean interlaced;
    private ByteBuffer videoCodecPrivate;
    private ByteBuffer audioCodecPrivate;
    private Map<String, String> codecOpts;

    @Override
    public void outputVideoPacket(Packet packet, VideoCodecMeta codecMeta) throws IOException {
        if (!outputFormat.isVideo())
            return;
        if (videoOutputTrack == null) {
            videoOutputTrack = muxer.addVideoTrack(outputVideoCodec, codecMeta);
            if (videoOutputTrack instanceof MP4MuxerTrack) {
                ((MP4MuxerTrack) videoOutputTrack).setTgtChunkDuration(Rational.R(3, 1), Unit.SEC);
            }
            if (videoOutputTrack instanceof CodecMP4MuxerTrack)
                ((CodecMP4MuxerTrack) videoOutputTrack).setCodecPrivateOpaque(videoCodecPrivate);
        }
        videoOutputTrack.addFrame(packet);
        framesOutput = true;
    }

    @Override
    public void outputAudioPacket(Packet audioPkt, AudioCodecMeta audioCodecMeta) throws IOException {
        if (!outputFormat.isAudio())
            return;
        if (audioOutputTrack == null) {
            audioOutputTrack = muxer.addAudioTrack(outputAudioCodec, audioCodecMeta);
            if (audioOutputTrack instanceof MP4MuxerTrack) {
                ((MP4MuxerTrack) audioOutputTrack).setTgtChunkDuration(Rational.R(3, 1), Unit.SEC);
            }
            if (audioOutputTrack instanceof CodecMP4MuxerTrack)
                ((CodecMP4MuxerTrack) audioOutputTrack).setCodecPrivateOpaque(audioCodecPrivate);
        }
        audioOutputTrack.addFrame(audioPkt);
        framesOutput = true;
    }

    public void initMuxer() throws IOException {
        if (destStream == null && outputFormat != IMG)
            destStream = writableFileChannel(destName);
        if (MOV == outputFormat) {
            muxer = MP4Muxer.createMP4MuxerToChannel(destStream);
        }
        else {
            throw new RuntimeException("The output format " + outputFormat + " is not supported.");
        }
    }

    public void finish() throws IOException {
        if (videoEncoder != null)
            videoEncoder.finish();
        if (framesOutput) {
            muxer.finish();
        } else {
            Logger.warn("No frames output.");
        }
        if (destStream != null) {
            IOUtils.closeQuietly(destStream);
        }
    }

    public SinkImpl(String destName, Format outputFormat, Codec outputVideoCodec, Codec outputAudioCodec) {
        if (destName == null && outputFormat == IMG)
            throw new IllegalArgumentException("A destination file should be specified for the image muxer.");
        this.destName = destName;
        this.outputFormat = outputFormat;
        this.outputVideoCodec = outputVideoCodec;
        this.outputAudioCodec = outputAudioCodec;
        this.outputFormat = outputFormat;
        bufferStore = new ThreadLocal<ByteBuffer>();
    }

    @Override
    public void setVideoCodecPrivate(ByteBuffer videoCodecPrivate) {
        this.videoCodecPrivate = videoCodecPrivate;
        if (videoOutputTrack instanceof CodecMP4MuxerTrack)
            ((CodecMP4MuxerTrack) videoOutputTrack).setCodecPrivateOpaque(videoCodecPrivate);
    }

    @Override
    public void setAudioCodecPrivate(ByteBuffer audioCodecPrivate) {
        this.audioCodecPrivate = audioCodecPrivate;
        if (audioOutputTrack instanceof CodecMP4MuxerTrack)
            ((CodecMP4MuxerTrack) audioOutputTrack).setCodecPrivateOpaque(audioCodecPrivate);
    }

    public static SinkImpl createWithStream(SeekableByteChannel destStream, Format outputFormat, Codec outputVideoCodec,
                                            Codec outputAudioCodec) {
        SinkImpl result = new SinkImpl(null, outputFormat, outputVideoCodec, outputAudioCodec);
        result.destStream = destStream;
        return result;
    }

    @Override
    public void init(boolean videoCopy, boolean audioCopy) throws IOException {
        initMuxer();
        if (outputFormat.isVideo() && outputVideoCodec != null && !videoCopy) {
            if (Codec.H264 == outputVideoCodec) {
                videoEncoder = createH264Encoder();
            }
            else {
                throw new RuntimeException("Could not find encoder for the codec: " + outputVideoCodec);
            }
        }
        initVideoEncoder();
    }

    private H264Encoder createH264Encoder() {
        Map<String, String> opts = getCodecOpts();
        if ("cqp".equals(opts.get("rc"))) {
            int qp = 20;
            if (opts.containsKey("qp"))
                qp = Integer.parseInt(opts.get("qp"));
            return new H264Encoder(new CQPRateControl(qp));
        } else
            return H264Encoder.createH264Encoder();
    }

    private void initVideoEncoder() {
        for (Entry<String, String> entry : getCodecOpts().entrySet()) {
            setEncoderOption(entry.getKey(), entry.getValue());
        }
    }

    private Map<String, String> getCodecOpts() {
        Map<String, String> map = new HashMap<String, String>();
        if (outputVideoCodec == null)
            return map;
        if (codecOpts == null)
            return map;
        String opts = codecOpts.get(outputVideoCodec.name().toLowerCase());
        if (opts == null)
            return map;
        for (String entry : opts.split(",")) {
            String[] split = entry.split(":");
            map.put(split[0], split[1]);
        }
        return map;
    }

    private String setter(String key) {
        if (key.length() == 0 || key == null)
            return "";
        char[] chars = key.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return "set" + new String(chars);
    }

    @SuppressWarnings("AndroidJdkLibsChecker")
    private void setEncoderOption(String key, String value) {
        Method[] methods = videoEncoder.getClass().getMethods();
        for (Method method : methods) {
            if (setter(key).equals(method.getName())) {
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 1) {
                    Object val = parseVal(parameters[0].getType(), value);
                    if (val != null) {
                        try {
                            method.invoke(videoEncoder, val);
                        } catch (Exception e) {
                            Logger.error("Couldn't set codec option " + key + ", " + e.getClass().getName() + "["
                                    + e.getMessage() + "]");
                        }
                    }
                }
            }
        }
    }

    private Object parseVal(Class<?> type, String value) {
        if (type == String.class)
            return value;
        else if (type == boolean.class)
            return Boolean.parseBoolean(value);
        else if (type == int.class)
            return Integer.parseInt(value);
        else if (type == double.class)
            return Double.parseDouble(value);
        else
            Logger.error("Unsupported codec argument type: " + type.getName());
        return null;
    }

    protected EncodedFrame encodeVideo(Picture frame, ByteBuffer _out) {
        if (!outputFormat.isVideo())
            return null;

        return videoEncoder.encodeFrame(frame, _out);
    }

    private AudioEncoder createAudioEncoder(Codec codec, AudioFormat format) {
        if (codec != Codec.PCM) {
            throw new RuntimeException("Only PCM audio encoding (RAW audio) is supported.");
        }
        return new RawAudioEncoder();
    }

    private static class RawAudioEncoder implements AudioEncoder {
        @Override
        public ByteBuffer encode(ByteBuffer audioPkt, ByteBuffer buf) {
            return audioPkt;
        }
    }

    protected ByteBuffer encodeAudio(AudioBuffer audioBuffer) {
        if (audioEncoder == null) {
            AudioFormat format = audioBuffer.getFormat();
            audioEncoder = createAudioEncoder(outputAudioCodec, format);
        }

        return audioEncoder.encode(audioBuffer.getData(), null);
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setInterlaced(Boolean interlaced) {
        this.interlaced = interlaced;
    }

    @Override
    public void outputVideoFrame(VideoFrameWithPacket videoFrame) throws IOException {
        if (!outputFormat.isVideo() || outputVideoCodec == null)
            return;
        Packet outputVideoPacket;
        ByteBuffer buffer = bufferStore.get();
        int bufferSize = videoEncoder.estimateBufferSize(videoFrame.getFrame().getPicture());
        if (buffer == null || bufferSize < buffer.capacity()) {
            buffer = ByteBuffer.allocate(bufferSize);
            bufferStore.set(buffer);
        }
        buffer.clear();
        Picture frame = videoFrame.getFrame().getPicture();
        EncodedFrame enc = encodeVideo(frame, buffer);
        outputVideoPacket = Packet.createPacketWithData(videoFrame.getPacket(), NIOUtils.clone(enc.getData()));
        outputVideoPacket.setFrameType(enc.isKeyFrame() ? FrameType.KEY : FrameType.INTER);
        outputVideoPacket(outputVideoPacket, com.example.bootIML.jcodec.common.VideoCodecMeta
                .createSimpleVideoCodecMeta(new Size(frame.getWidth(), frame.getHeight()), frame.getColor()));
    }

    @Override
    public void outputAudioFrame(AudioFrameWithPacket audioFrame) throws IOException {
        if (!outputFormat.isAudio() || outputAudioCodec == null)
            return;
        outputAudioPacket(Packet.createPacketWithData(audioFrame.getPacket(), encodeAudio(audioFrame.getAudio())),
                com.example.bootIML.jcodec.common.AudioCodecMeta.fromAudioFormat(audioFrame.getAudio().getFormat()));
    }

    @Override
    public ColorSpace getInputColor() {
        if (videoEncoder == null)
            throw new IllegalStateException(
                    "Video encoder has not been initialized, init() must be called before using this class.");
        ColorSpace[] colorSpaces = videoEncoder.getSupportedColorSpaces();
        return colorSpaces == null ? null : colorSpaces[0];
    }

    @Override
    public void setOption(Options option, Object value) {
        if (option == Options.PROFILE)
            profile = (String) value;
        else if (option == Options.INTERLACED)
            interlaced = (Boolean) value;
    }

    @Override
    public boolean isVideo() {
        return outputFormat.isVideo();
    }

    @Override
    public boolean isAudio() {
        return outputFormat.isAudio();
    }

    @Override
    public boolean isInitialised() {
        return muxer != null;
    }

    @Override
    public void setCodecOpts(Map<String, String> codecOpts) {
        this.codecOpts = codecOpts;
    }
}


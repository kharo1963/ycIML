package com.example.bootIML.jcodec.api;

import static com.example.bootIML.jcodec.common.Codec.H264;
import static com.example.bootIML.jcodec.common.Format.MOV;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.example.bootIML.jcodec.api.transcode.PixelStore;
import com.example.bootIML.jcodec.api.transcode.PixelStore.LoanerPicture;
import com.example.bootIML.jcodec.api.transcode.PixelStoreImpl;
import com.example.bootIML.jcodec.api.transcode.Sink;
import com.example.bootIML.jcodec.api.transcode.SinkImpl;
import com.example.bootIML.jcodec.api.transcode.VideoFrameWithPacket;
import com.example.bootIML.jcodec.common.Codec;
import com.example.bootIML.jcodec.common.Format;
import com.example.bootIML.jcodec.common.io.NIOUtils;
import com.example.bootIML.jcodec.common.io.SeekableByteChannel;
import com.example.bootIML.jcodec.common.model.ColorSpace;
import com.example.bootIML.jcodec.common.model.Packet;
import com.example.bootIML.jcodec.common.model.Packet.FrameType;
import com.example.bootIML.jcodec.common.model.Picture;
import com.example.bootIML.jcodec.common.model.Rational;
import com.example.bootIML.jcodec.scale.ColorUtil;
import com.example.bootIML.jcodec.scale.Transform;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * Encodes a sequence of images as a video.
 *
 * @author The JCodec project
 */
public class SequenceEncoder {

    private Transform transform;
    private int frameNo;
    private int timestamp;
    private Rational fps;
    private Sink sink;
    private PixelStore pixelStore;

    public static SequenceEncoder createSequenceEncoder(File out, int fps) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(fps, 1), MOV, H264, null);
    }

    public static SequenceEncoder create25Fps(File out) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(25, 1), MOV, H264, null);
    }

    public static SequenceEncoder create30Fps(File out) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(30, 1), MOV, H264, null);
    }

    public static SequenceEncoder create2997Fps(File out) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(30000, 1001), MOV, H264, null);
    }

    public static SequenceEncoder create24Fps(File out) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(24, 1), MOV, H264, null);
    }

    public static SequenceEncoder createWithFps(SeekableByteChannel out, Rational fps) throws IOException {
        return new SequenceEncoder(out, fps, MOV, H264, null);
    }

    public SequenceEncoder(SeekableByteChannel out, Rational fps, Format outputFormat, Codec outputVideoCodec,
                           Codec outputAudioCodec) throws IOException {
        this.fps = fps;

        sink = SinkImpl.createWithStream(out, outputFormat, outputVideoCodec, outputAudioCodec);

        pixelStore = new PixelStoreImpl();
    }

    /**
     * Allows passing configuration to the codec. Must be called before the first
     * frame is encoded.
     *
     * @param opts
     */
    public void configureCodec(Map<String, String> opts) {
        if (sink.isInitialised()) {
            throw new RuntimeException(
                    "Sink was already used to encode frames, we cannot allow any codec configuration changes");
        }
        sink.setCodecOpts(opts);
    }

    /**
     * Encodes a frame into a movie.
     *
     * @param pic
     * @throws IOException
     */
    public void encodeNativeFrame(Picture pic) throws IOException {
        if (!sink.isInitialised()) {
            sink.init(false, false);
            if (sink.getInputColor() != null)
                transform = ColorUtil.getTransform(ColorSpace.RGB, sink.getInputColor());
        }
        ColorSpace sinkColor = sink.getInputColor();
        ColorSpace picColor = pic.getColor();
        if (picColor != ColorSpace.RGB && picColor != sinkColor)
            throw new IllegalArgumentException("The input images is expected in RGB color.");

        LoanerPicture toEncode;
        if (sinkColor != null && picColor != sinkColor) {
            toEncode = pixelStore.getPicture(pic.getWidth(), pic.getHeight(), sinkColor);
            transform.transform(pic, toEncode.getPicture());
        } else {
            toEncode = new LoanerPicture(pic, 0);
        }

        Packet pkt = Packet.createPacket(null, timestamp, fps.getNum(), fps.getDen(), frameNo, FrameType.KEY, null);
        sink.outputVideoFrame(new VideoFrameWithPacket(pkt, toEncode));

        if (sinkColor != null)
            pixelStore.putBack(toEncode);

        timestamp += fps.getDen();
        frameNo++;
    }

    public void finish() throws IOException {
        sink.finish();
    }
}

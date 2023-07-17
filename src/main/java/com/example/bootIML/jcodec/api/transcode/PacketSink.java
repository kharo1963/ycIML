package com.example.bootIML.jcodec.api.transcode;

import java.io.IOException;

import com.example.bootIML.jcodec.common.AudioCodecMeta;
import com.example.bootIML.jcodec.common.VideoCodecMeta;
import com.example.bootIML.jcodec.common.model.Packet;


/**
 * The sink that consumes the uncompressed frames and stores them into a
 * compressed file.
 *
 * @author Stanislav Vitvitskiy
 */
public interface PacketSink {

    void outputVideoPacket(Packet videoPacket, VideoCodecMeta videoCodecMeta) throws IOException;

    void outputAudioPacket(Packet audioPacket, AudioCodecMeta audioCodecMeta) throws IOException;

}


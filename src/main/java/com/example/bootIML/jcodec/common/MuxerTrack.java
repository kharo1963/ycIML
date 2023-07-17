package com.example.bootIML.jcodec.common;

import java.io.IOException;

import com.example.bootIML.jcodec.common.model.Packet;

/**
 * Interface for muxer track that many muxers implement.
 *
 * @author Stanislav Vitvitskiy
 */
public interface MuxerTrack {

    void addFrame(Packet outPacket) throws IOException;
}

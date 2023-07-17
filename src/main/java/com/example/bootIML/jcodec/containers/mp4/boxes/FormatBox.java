package com.example.bootIML.jcodec.containers.mp4.boxes;

import static com.example.bootIML.jcodec.common.JCodecUtil2.asciiString;

import com.example.bootIML.jcodec.common.io.NIOUtils;

import java.nio.ByteBuffer;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 *
 */
public class FormatBox extends Box {
    private String fmt;

    public FormatBox(Header header) {
        super(header);
    }

    public static String fourcc() {
        return "frma";
    }

    public static FormatBox createFormatBox(String fmt) {
        FormatBox frma = new FormatBox(new Header(fourcc()));
        frma.fmt = fmt;
        return frma;
    }

    public void parse(ByteBuffer input) {
        this.fmt = NIOUtils.readString(input, 4);
    }

    protected void doWrite(ByteBuffer out) {
        out.put(asciiString(fmt));
    }

    @Override
    public int estimateSize() {
        return asciiString(fmt).length + 8;
    }
}

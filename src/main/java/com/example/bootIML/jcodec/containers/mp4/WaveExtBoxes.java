package com.example.bootIML.jcodec.containers.mp4;

import com.example.bootIML.jcodec.containers.mp4.boxes.EndianBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.FormatBox;

public class WaveExtBoxes extends Boxes {
    public WaveExtBoxes() {
        mappings.put(FormatBox.fourcc(), FormatBox.class);
        mappings.put(EndianBox.fourcc(), EndianBox.class);
        //            mappings.put(EsdsBox.fourcc(), EsdsBox.class);
    }
}


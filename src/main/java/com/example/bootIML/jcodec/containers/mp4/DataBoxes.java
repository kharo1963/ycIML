package com.example.bootIML.jcodec.containers.mp4;

import com.example.bootIML.jcodec.containers.mp4.boxes.AliasBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.UrlBox;

public class DataBoxes extends Boxes {
    public DataBoxes() {
        mappings.put(UrlBox.fourcc(), UrlBox.class);
        mappings.put(AliasBox.fourcc(), AliasBox.class);
        mappings.put("cios", AliasBox.class);
    }
}


package com.example.bootIML.jcodec.containers.mp4;

import com.example.bootIML.jcodec.containers.mp4.boxes.Box;
import com.example.bootIML.jcodec.containers.mp4.boxes.ChannelBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.WaveExtension;

public class AudioBoxes extends Boxes {

    public AudioBoxes() {
        super();
        mappings.put(WaveExtension.fourcc(), WaveExtension.class);
        mappings.put(ChannelBox.fourcc(), ChannelBox.class);
        mappings.put("esds", Box.LeafBox.class);
    }
}


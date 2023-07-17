package com.example.bootIML.jcodec.containers.mp4;

import com.example.bootIML.jcodec.codecs.h264.mp4.AvcCBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.CleanApertureExtension;
import com.example.bootIML.jcodec.containers.mp4.boxes.ColorExtension;
import com.example.bootIML.jcodec.containers.mp4.boxes.FielExtension;
import com.example.bootIML.jcodec.containers.mp4.boxes.GamaExtension;
import com.example.bootIML.jcodec.containers.mp4.boxes.PixelAspectExt;

public class VideoBoxes extends Boxes {
    public VideoBoxes() {
        mappings.put(PixelAspectExt.fourcc(), PixelAspectExt.class);
        mappings.put(AvcCBox.fourcc(), AvcCBox.class);
        mappings.put(ColorExtension.fourcc(), ColorExtension.class);
        mappings.put(GamaExtension.fourcc(), GamaExtension.class);
        mappings.put(CleanApertureExtension.fourcc(), CleanApertureExtension.class);
        mappings.put(FielExtension.fourcc(), FielExtension.class);
    }
}

package com.example.bootIML.jcodec.containers.mp4;

import com.example.bootIML.jcodec.containers.mp4.boxes.Box;
import com.example.bootIML.jcodec.containers.mp4.boxes.Header;

public interface IBoxFactory {

    Box newBox(Header header);
}

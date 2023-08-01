package com.example.bootIML.interpretator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceProgram {

    private int currentPos = 0;
    char getNextChar() {
        return StatD.sourceText[currentPos++];
    }

    void unGetChar () {
        --currentPos;
    }

}

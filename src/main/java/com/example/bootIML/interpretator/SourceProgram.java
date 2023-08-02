package com.example.bootIML.interpretator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class SourceProgram {

    public static char[]  sourceText;
    public static ArrayList filFiles;
    private int currentPos = 0;

    public SourceProgram (char[]  sourceText){
        this.sourceText = sourceText;
    }
    char getNextChar() {
        return StatD.sourceText[currentPos++];
    }

    void unGetChar () {
        --currentPos;
    }

}

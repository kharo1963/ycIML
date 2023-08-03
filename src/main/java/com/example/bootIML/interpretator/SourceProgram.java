package com.example.bootIML.interpretator;

import com.example.bootIML.service.GraphicsService;
import com.example.bootIML.service.ImlParamServiceImpl;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class SourceProgram {

    public static ImlParamServiceImpl imlParamServiceImpl;
    public static GraphicsService graphicsService;
    public ArrayList<String> restArg;
    public ArrayList<Ident> TID;
    public byte[] fileContent;
    public ArrayList resultList;
    public String resultText = "";
    private char[] sourceText;
    private int currentPos = 0;

    public SourceProgram (char[]  sourceText,
                          ImlParamServiceImpl imlParamServiceImpl,
                          GraphicsService graphicsService){
        this.sourceText = sourceText;
        this.imlParamServiceImpl = imlParamServiceImpl;
        this.graphicsService = graphicsService;
    }

    char getNextChar() {
        return sourceText[currentPos++];
    }

    void unGetChar () {
        --currentPos;
    }

}

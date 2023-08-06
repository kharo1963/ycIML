package com.example.bootIML.interpretator;

import com.example.bootIML.service.ExecuterService;
import com.example.bootIML.service.GraphicsService;
import com.example.bootIML.service.ImlParamServiceImpl;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class SourceProgram {

    public ExecuterService executerService;
    public ArrayList<String> restArg;
    public ArrayList<Ident> TID;
    public byte[] fileContent;
    public ArrayList resultList;
    public String resultText = "";
    private char[] sourceText;
    private int currentPos = 0;

    public SourceProgram (char[]  sourceText, ExecuterService executerService) {
        this.sourceText = sourceText;
        this.executerService = executerService;
    }

    char getNextChar() {
        return sourceText[currentPos++];
    }

    void unGetChar () {
        --currentPos;
    }

}

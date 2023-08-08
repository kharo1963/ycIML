package com.example.bootIML.interpretator;

import com.example.bootIML.service.ExecuterService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

@Getter
@Setter
public class SourceProgram {

    public ArrayList<String> restArg = new ArrayList<>();
    public ArrayList<Ident> TID = new ArrayList<>();
    public ArrayList resultList = new ArrayList();
    public String resultText = "";
    private char[] sourceText;
    private int currentPos = 0;
    public byte[] fileContent;
    int currentLexVal;
    Lex currentLex;
    TypeOfLex currentLexType;
    Deque<Integer> stackInteger = new ArrayDeque<>();
    Deque<TypeOfLex> stackTypeOfLex = new ArrayDeque<>();
    public ArrayList<Lex> poliz = new ArrayList<>();

    public SourceProgram (char[]  sourceText) {
        this.sourceText = sourceText;
    }

    char getNextChar() {
        return sourceText[currentPos++];
    }

    void unGetChar () {
        --currentPos;
    }

}

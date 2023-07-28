package com.example.bootIML.interpretator;

public class Lex {
    TypeOfLex typeOfLex;
    int valueOfLex;
    Lex () {
    	typeOfLex = TypeOfLex.LEX_NULL;
    	valueOfLex = 0;
    }
    Lex(TypeOfLex typeOfLex) {
    	this.typeOfLex = typeOfLex;
    	this.valueOfLex = 0;
    }   
    Lex(TypeOfLex typeOfLex, int valueOfLex) {
    	this.typeOfLex = typeOfLex;
    	this.valueOfLex = valueOfLex;
    }      
    TypeOfLex getTypeOfLex() {
        return typeOfLex;
    }
    int getValueOfLex() {
        return valueOfLex;
    }
 }

package com.example.bootIML.interpretator;

public class Lex {
    TypeOfLex t_lex;
    int           v_lex;
    public
    Lex () {
    	t_lex = TypeOfLex.LEX_NULL;
    	v_lex = 0;
    }
    Lex(TypeOfLex t) {
    	t_lex = t;
    	v_lex = 0;
    }   
    Lex(TypeOfLex t, int v) {
    	t_lex = t;
    	v_lex = v;
    }      
    TypeOfLex get_type() {
        return t_lex;
    }
    int get_value() {
        return v_lex;
    }
 }

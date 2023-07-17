package com.example.bootIML.interpretator;

public class Ident {
    String      name;
    Boolean     declare;
    TypeOfLex type;
    Boolean     assign;
    int         value;
    public
    Ident() {
        declare = false;
        assign  = false;
    }
    Ident(String n) {
        name    = n;
        declare = false;
        assign  = false;
    }
    String get_name() {
        return name;
    }
    Boolean get_declare() {
        return declare;
    }
    void put_declare() {
        declare = true;
    }
    TypeOfLex get_type() {
        return type;
    }
    void put_type(TypeOfLex t) {
        type = t;
    }
    Boolean get_assign() {
        return assign;
    }
    void put_assign() {
        assign = true;
    }
    int  get_value() {
        return value;
    }
    void put_value(int v) {
        value = v;
    }
}

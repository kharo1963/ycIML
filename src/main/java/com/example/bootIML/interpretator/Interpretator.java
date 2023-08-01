package com.example.bootIML.interpretator;

public class Interpretator {
    Parser pars;
    Executer E = new Executer();

    public Interpretator() {
        pars = new Parser();
    }

    public void interpretation() {
        pars.analyze();
        E.Execute(pars.poliz);
    }
}

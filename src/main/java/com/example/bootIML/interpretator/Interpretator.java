package com.example.bootIML.interpretator;

public class Interpretator {
    Parser pars;
    Executer E = new Executer();

    public Interpretator(String program) {
        pars = new Parser(program);
    }

    public void interpretation() {
        pars.analyze();
        E.Execute(pars.poliz);
    }
}

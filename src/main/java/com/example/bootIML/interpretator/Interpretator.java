package com.example.bootIML.interpretator;

public class Interpretator {
    Parser parser;
    Executer executer;

    public Interpretator(SourceProgram sourceProgram) {
        parser = new Parser(sourceProgram);
        executer = new Executer(sourceProgram);
    }

    public void interpretation() {
        parser.analyze();
        executer.Execute(parser.sourceProgram.poliz);
    }
}

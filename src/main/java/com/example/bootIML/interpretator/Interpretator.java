package com.example.bootIML.interpretator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Interpretator {
    private final Parser parser;
    private final Executer executer;

    public void interpretation(SourceProgram sourceProgram) {
        parser.analyze(sourceProgram);
        executer.Execute(sourceProgram);
    }
}

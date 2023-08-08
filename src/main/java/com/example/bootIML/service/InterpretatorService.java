package com.example.bootIML.service;

import com.example.bootIML.interpretator.Interpretator;
import com.example.bootIML.interpretator.SourceProgram;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterpretatorService {
    private final Interpretator interpretator;

    public SourceProgram invokeInterpretator (String sourceText){
        log.info("Start invokeInterpretator");
        SourceProgram sourceProgram = new SourceProgram(sourceText.toCharArray());
        interpretator.interpretation(sourceProgram);
        for (Object line : sourceProgram.resultList) {
            sourceProgram.resultText += line + System.lineSeparator();
        }
        return sourceProgram;
    }

    public String addSample() {
        String sourceText = "";
        log.info("addSample");
        Path path = Paths.get("m-ext-gcd.txt");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                sourceText += line + System.lineSeparator();            }
        } catch (IOException e) {
            e.printStackTrace();
            sourceText = "program var x,y : int ;  begin x := y := 1 ; write (x); write (y) end @";
        }
        return sourceText;
    }
}

package com.example.bootIML.service;

import com.example.bootIML.interpretator.Interpretator;
import com.example.bootIML.interpretator.SourceProgram;
import com.example.bootIML.interpretator.StatD;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterpretatorService {
    private final ImlParamServiceImpl  imlParamServiceImpl;
    private final GraphicsService graphicsService;

    public SourceProgram invokeInterpretator (String sourceText){
        SourceProgram sourceProgram;
        log.info("Start invokeInterpretator");
        sourceProgram = new SourceProgram(sourceText.toCharArray(), imlParamServiceImpl, graphicsService);
        sourceProgram.TID = new ArrayList<>();
        StatD.restArg = new ArrayList<>();
        sourceProgram.filFiles = new ArrayList();
        Interpretator interpretator = new Interpretator(sourceProgram);
        interpretator.interpretation();
        for (Object line : sourceProgram.filFiles) {
            sourceProgram.resultText += line + System.lineSeparator();
        }
        return sourceProgram;
    }

    public String addSample() {
        String sourceText = "";
        log.info("addSample");
        Path path = Paths.get("ext-gcd.txt");
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

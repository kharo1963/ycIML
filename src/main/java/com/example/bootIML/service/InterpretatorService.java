package com.example.bootIML.service;

import com.example.bootIML.interpretator.Interpretator;
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

    public String invokeInterpretator (String sourceText){
        String resultText = "";
        StatD.TID = new ArrayList<>();
        StatD.restArg = new ArrayList<>();
        ArrayFilFiles.filFiles = new ArrayList();
        log.info("Start invokeInterpretator");
        try {
            StatD.sourceText = sourceText.toCharArray();
            Interpretator I = new Interpretator();
            I.interpretation();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        for (Object line : ArrayFilFiles.filFiles) {
            resultText += line + System.lineSeparator();
        }
        return resultText;
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

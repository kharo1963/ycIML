package com.example.bootIML.service;

import com.example.bootIML.interpretator.Interpretator;
import com.example.bootIML.interpretator.StatD;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
}

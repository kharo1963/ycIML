package com.example.bootIML.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import com.example.bootIML.service.InterpretatorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bootIML.interpretator.StatD;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InterpretatorController {

    private final InterpretatorService interpretatorService;

    @GetMapping("/")
    public String handleUploadForm(Model model) {
        return "uploadForm";
    }

    @PostMapping("/")
    public String handleFileUpload(Model model,
                                   HttpServletRequest request,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("sourceText") String sourceText,
                                   RedirectAttributes redirectAttributes) {
        if (!file.isEmpty()) {
            sourceText = fileToString (file);
        }
        String resultText = interpretatorService.invokeInterpretator(sourceText);
        redirectAttributes.addFlashAttribute("resultText", resultText);
        redirectAttributes.addFlashAttribute("sourceText", sourceText);
        log.info("resultText.indexOf(spinCube)" + resultText.indexOf("spinCube"));
        if (resultText.indexOf("spinCube") >= 0) {
            String resultFile64  = Base64.getEncoder().encodeToString(StatD.fileContent);
            String resultVideo64 = "data:video/mp4;base64," + resultFile64;
            model.addAttribute("resultVideo64", resultVideo64);
            model.addAttribute("videoOperator", "Результат выполнения оператора spinCube");
            return "mp4Form";
        }
        String redirectURL = transformRedirectUrl (request, "/");
        return redirectURL;
    }

    @PostMapping("/addSample")
    public String handleAddSample(HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {

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
        redirectAttributes.addFlashAttribute("sourceText", sourceText);

        String redirectURL = transformRedirectUrl (request, "/");
        return redirectURL;

    }


    private String transformRedirectUrl (HttpServletRequest request, String requestURI) {

        if (request.getServerName().startsWith("localhost")) {
            return "redirect:/";
        }

        String redirectURL = //request.getScheme() +
                             "https://"
                           + request.getServerName() + requestURI //request.getRequestURI()
                           + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        log.info("redirectURL " + redirectURL);
        return "redirect:" + redirectURL;
    }
    private String fileToString  (MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException ("Failed to store empty file " + file.getOriginalFilename());
            }
            log.info(file.getInputStream().toString());
            return new String(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to getBytes from file " + file.getOriginalFilename(), e);
        }
    }

}
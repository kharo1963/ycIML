package com.example.bootIML.controller;

import java.io.IOException;
import java.util.Base64;

import com.example.bootIML.interpretator.SourceProgram;
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
            sourceText = fileToString(file);
        }
        SourceProgram sourceProgram = interpretatorService.invokeInterpretator(sourceText);
        log.info("resultText.indexOf(spinCube)" + sourceProgram.resultText.indexOf("spinCube"));
        if (sourceProgram.resultText.indexOf("spinCube") >= 0) {
            String resultFile64  = Base64.getEncoder().encodeToString(sourceProgram.fileContent);
            String resultVideo64 = "data:video/mp4;base64," + resultFile64;
            model.addAttribute("resultVideo64", resultVideo64);
            model.addAttribute("videoOperator", "Результат выполнения оператора spinCube");
            return "mp4Form";
        }
        redirectAttributes.addFlashAttribute("resultText", sourceProgram.resultText);
        redirectAttributes.addFlashAttribute("sourceText", sourceText);
            String redirectURL = transformRedirectUrl (request, "/");
        return redirectURL;
    }

    @PostMapping("/addSample")
    public String handleAddSample(HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {

        String sourceText = interpretatorService.addSample();;
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

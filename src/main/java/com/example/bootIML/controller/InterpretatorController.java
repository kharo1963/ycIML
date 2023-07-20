package com.example.bootIML.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bootIML.service.StorageFileNotFoundException;
import com.example.bootIML.service.StorageService;
import com.example.bootIML.service.ArrayFilFiles;
import com.example.bootIML.interpretator.StatD;
import com.example.bootIML.interpretator.Interpretator;

@Controller
public class InterpretatorController {

    private final StorageService storageService;

    @Autowired
    public InterpretatorController(StorageService storageService) {

        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        return "uploadForm";
    }

    @PostMapping("/addSample")
    public String handleAddSample(HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {

        String sourceText = "";

        System.out.println("addSample");

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

    @SneakyThrows
    @PostMapping("/")
    public String handleFileUpload(Model model,
                                   HttpServletRequest request,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("sourceText") String sourceText,
                                   RedirectAttributes redirectAttributes) {

        String srcCode;
        String resultText = "";
        Path path;

        StatD.TID = new ArrayList<>();
        StatD.restArg = new ArrayList<>();
        ArrayFilFiles.filFiles = new ArrayList();

        if (file.isEmpty()) {
            srcCode = System.getProperty("java.io.tmpdir") + File.separator + "tmpEdit.txt";
            path = Paths.get(srcCode);
            try {
                Files.writeString(path, sourceText, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            sourceText = "";
            srcCode = storageService.store(file);
            path = Paths.get(srcCode);
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                for (String line : lines) {
                    sourceText += line + System.lineSeparator();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println("Start");
            System.out.println(srcCode);
            Interpretator I = new Interpretator(srcCode);
            I.interpretation();
            //redirectAttributes.addFlashAttribute("message", "Вы успешно интерпретировали " + file.getOriginalFilename() + "!");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        for (Object line : ArrayFilFiles.filFiles) {
            resultText += line + System.lineSeparator();
        }

        redirectAttributes.addFlashAttribute("resultText", resultText);
        redirectAttributes.addFlashAttribute("sourceText", sourceText);

        System.out.println("resultText.indexOf(spinCube)" + resultText.indexOf("spinCube"));

        if (resultText.indexOf("spinCube") >= 0) {

            byte[] fileContent = FileUtils.readFileToByteArray(new File(System.getProperty("java.io.tmpdir") + File.separator + "spincubevideo.mp4"));
            String resultFile64 = Base64.getEncoder().encodeToString(fileContent);

            String resultVideo64 = "data:video/mp4;base64," + resultFile64;
            model.addAttribute("resultVideo64", resultVideo64);
            model.addAttribute("videoOperator", "Результат выполнения оператора spinCube");

            return "mp4Form";
        }

        String redirectURL = transformRedirectUrl (request, "/");
        return redirectURL;

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    private String transformRedirectUrl (HttpServletRequest request, String requestURI) {

        if (request.getServerName().startsWith("localhost")) {
            return "redirect:/";
        }

        String redirectURL = //request.getScheme() +
                             "https://"
                           + request.getServerName() + requestURI //request.getRequestURI()
                           + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        System.out.println("redirectURL");
        System.out.println(redirectURL);
        return "redirect:" + redirectURL;
    }

}
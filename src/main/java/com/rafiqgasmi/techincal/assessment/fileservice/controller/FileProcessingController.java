package com.rafiqgasmi.techincal.assessment.fileservice.controller;

import com.rafiqgasmi.techincal.assessment.fileservice.service.FileProcessingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileProcessingController {


    private final FileProcessingService fileProcessingService;


    public FileProcessingController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @PostMapping("/processFile")
    public ResponseEntity<String> processFile(@RequestParam("file") MultipartFile file) throws Exception {

        // fail fast
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            String outcomeFile = fileProcessingService.processFile(file);
            return ResponseEntity.ok().body(outcomeFile);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("failed to process the file, please check the input file or refer to API Documentation");
        }
    }

}
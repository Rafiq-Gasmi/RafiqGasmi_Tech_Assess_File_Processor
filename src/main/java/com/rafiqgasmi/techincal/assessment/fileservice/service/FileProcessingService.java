package com.rafiqgasmi.techincal.assessment.fileservice.service;

// FileProcessingService.java

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafiqgasmi.techincal.assessment.fileservice.exception.Exceptions.EmptyFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileProcessingService {

    private static final Logger log = LoggerFactory.getLogger(FileProcessingService.class);

    @Autowired
    private ObjectMapper objectMapper;

    public String processFile(MultipartFile file) throws Exception {
        List<Map<String, String>> outcomeList = processLines(file);

        if (outcomeList.isEmpty()) {
            throw new EmptyFileException("Empty file");
        }

        writeOutcomeToFile(outcomeList, "OutcomeFile.json");
        return objectMapper.writeValueAsString(outcomeList);
    }

    private List<Map<String, String>> processLines(MultipartFile file) throws Exception {
        List<Map<String, String>> outcomeList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 7) {
                    log.error("Invalid file format");
                    throw new IllegalArgumentException("Invalid file format");
                }

                for (String part : parts) {
                    if (part.trim().isEmpty()) {
                        log.error("Empty field in file");
                        throw new IllegalArgumentException("Empty field in file");
                    }
                }

                outcomeList.add(createOutcomeMap(parts));
            }
        }

        return outcomeList;
    }

    private Map<String, String> createOutcomeMap(String[] parts) {
        Map<String, String> outcome = new LinkedHashMap<>();
        outcome.put("Name", parts[2]);
        outcome.put("Transport", parts[4]);
        outcome.put("Top Speed", parts[6]);
        return outcome;
    }

    private void writeOutcomeToFile(List<Map<String, String>> outcomeList, String fileName) throws IOException {
        objectMapper.writeValue(new File(fileName), outcomeList);
    }
}
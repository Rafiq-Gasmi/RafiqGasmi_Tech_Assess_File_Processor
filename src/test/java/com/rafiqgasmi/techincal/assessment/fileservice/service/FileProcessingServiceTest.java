package com.rafiqgasmi.techincal.assessment.fileservice.service;

import com.rafiqgasmi.techincal.assessment.fileservice.exception.Exceptions;
import org.junit.jupiter.api.Test;

import static com.rafiqgasmi.techincal.assessment.fileservice.exception.Exceptions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafiqgasmi.techincal.assessment.fileservice.service.FileProcessingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class FileProcessingServiceTest {

    @InjectMocks
    private FileProcessingService fileProcessingService;
    @Spy
    private ObjectMapper objectMapper;

    @Test
    public void processFileTest() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("file", "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1".getBytes());
        String result = fileProcessingService.processFile(multipartFile);

        assertEquals("[{\"Name\":\"John Smith\",\"Transport\":\"Rides A Bike\",\"Top Speed\":\"12.1\"}]", result);
    }


    @Test
    public void processFileTest_emptyFile() {
        MultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
        assertThrows(EmptyFileException.class, () -> {
            fileProcessingService.processFile(emptyFile);
        });
    }

    @Test
    public void processFileTest_throwsException() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
        assertThrows(IllegalArgumentException.class, () -> {
            fileProcessingService.processFile(file);
        });
    }
}
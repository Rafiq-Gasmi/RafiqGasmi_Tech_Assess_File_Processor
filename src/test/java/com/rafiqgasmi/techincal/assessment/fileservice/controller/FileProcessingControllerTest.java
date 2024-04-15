package com.rafiqgasmi.techincal.assessment.fileservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafiqgasmi.techincal.assessment.fileservice.exception.Exceptions;
import com.rafiqgasmi.techincal.assessment.fileservice.service.FileProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;


@SpringBootTest
@AutoConfigureMockMvc
public class FileProcessingControllerTest {

    private FileProcessingController fileProcessingController;
    private FileProcessingService fileProcessingService = Mockito.mock(FileProcessingService.class);
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessFile() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MockMultipartFile file = new MockMultipartFile("file", "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n".getBytes());
        String expectedOutcome = "[{\"Name\":\"John Smith\",\"Transport\":\"Rides A Bike\",\"Top Speed\":\"12.1\"}]";
        doReturn(expectedOutcome).when(fileProcessingService).processFile(any());
        assertThat(mockingDetails(fileProcessingService).isMock()).isTrue();


        MvcResult result = mockMvc.perform(multipart("/processFile").file(file))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(expectedOutcome, result.getResponse().getContentAsString());
    }

    @Test
    public void testProcessFileWithEmptyFileThrowsException() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "".getBytes());

        mockMvc.perform(multipart("/processFile").file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testProcessFileWhenFileProcessingServiceThrowsIllegalArgumentExceptionDueToInvalidFileFormat() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
        when(fileProcessingService.processFile(eq(file))).thenThrow(new IllegalArgumentException("Invalid file format"));

        mockMvc.perform(multipart("/processFile").file(file))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testProcessFileWhenFileProcessingServiceThrowsIllegalArgumentExceptionDueToEmptyFieldInFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "| | | | | | | |".getBytes());
        when(fileProcessingService.processFile(eq(file))).thenThrow(new IllegalArgumentException("Invalid file format"));

        MockMvc mockMvc
                = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/processFile").file(file))
                .andExpect(status().isBadRequest());

    }
}
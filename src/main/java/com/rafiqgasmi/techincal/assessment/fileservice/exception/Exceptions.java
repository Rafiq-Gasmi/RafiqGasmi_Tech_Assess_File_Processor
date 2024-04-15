package com.rafiqgasmi.techincal.assessment.fileservice.exception;

public class Exceptions {

    public static class EmptyFileException extends RuntimeException {
        public EmptyFileException(String message) {
            super(message);
        }
    }

    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

}

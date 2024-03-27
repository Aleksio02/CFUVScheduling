package ru.cfuv.cfuvscheduling.commons.exception;

public class IncorrectRequestDataException extends RuntimeException {
    public IncorrectRequestDataException(String message) {
        super(message);
    }
}

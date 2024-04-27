package ru.cfuv.cfuvscheduling.commons.exception;

public class ServerUnavailableException extends RuntimeException {

    public ServerUnavailableException(String message) {
        super(message);
    }
}

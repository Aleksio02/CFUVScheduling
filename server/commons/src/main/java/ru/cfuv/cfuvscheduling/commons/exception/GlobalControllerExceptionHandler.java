package ru.cfuv.cfuvscheduling.commons.exception;

import javax.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ExceptionHandler(IncorrectRequestDataException.class)
    public ResponseEntity<?> handleIncorrectRequestDataException(IncorrectRequestDataException exception, WebRequest request) {
        return ResponseEntity.status(400).body(exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException exception, WebRequest request) {
        return ResponseEntity.status(401).body(exception.getMessage());
    }

    @ExceptionHandler(AccessForbiddenException.class)
    public ResponseEntity<?> handleAccessForbiddenException(AccessForbiddenException exception, WebRequest request) {
        return ResponseEntity.status(403).body(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }
}

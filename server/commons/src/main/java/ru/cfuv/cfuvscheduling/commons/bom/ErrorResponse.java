package ru.cfuv.cfuvscheduling.commons.bom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends AbstractResponseEntity {

    private String message;

    public ErrorResponse generateResponse(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }
}

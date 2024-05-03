package ru.cfuv.cfuvscheduling.auth.bdd;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class AuthTestContext {

    @Getter(AccessLevel.NONE)
    private static AuthTestContext INSTANCE;

    private Object requestObject;
    private Class requestClass;

    private Object responseObject;
    private Class responseClass;

    private Object exceptionObject;
    private Class exceptionClass;

    private AuthTestContext() {}

    public static AuthTestContext getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AuthTestContext();
        }
        return INSTANCE;
    }

    public void setRequestObject(Object requestObject, Class requestClass) {
        this.requestObject = requestObject;
        this.requestClass = requestClass;
    }

    public void setResponseObject(Object responseObject, Class responseClass) {
        this.responseObject = responseObject;
        this.responseClass = responseClass;
    }

    public void setExceptionObject(Object exceptionObject, Class exceptionClass) {
        this.exceptionObject = exceptionObject;
        this.exceptionClass = exceptionClass;
    }
}

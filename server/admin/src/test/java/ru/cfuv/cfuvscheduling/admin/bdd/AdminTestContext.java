package ru.cfuv.cfuvscheduling.admin.bdd;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class AdminTestContext {

    @Getter(AccessLevel.NONE)
    private static AdminTestContext INSTANCE;

    private Object requestObject;
    private Class requestClass;

    private Object responseObject;
    private Class responseClass;

    private Object exceptionObject;
    private Class exceptionClass;

    private AdminTestContext() {}

    public static AdminTestContext getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AdminTestContext();
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

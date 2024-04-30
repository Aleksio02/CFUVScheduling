package ru.cfuv.cfuvscheduling.auth.bdd.then;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.java.en.Then;
import ru.cfuv.cfuvscheduling.auth.bdd.AuthTestContext;
import ru.cfuv.cfuvscheduling.auth.bom.AccountResponse;

public class UserAuthorizationThen {

    private final AuthTestContext testContext = AuthTestContext.getINSTANCE();

    @Then("request should be processed successfully")
    public void requestShouldBeProcessedSuccessfully() {
        Object responseObject = testContext.getResponseObject();
        Class responseClass = testContext.getResponseClass();

        assertNotNull(responseObject);
        assertEquals(responseClass, AccountResponse.class);
    }

    @Then("request won't be processed due to {}")
    public void requestWontBeProcessedDueTo(String exceptionClass) {
        Class exceptionResponseClass = testContext.getExceptionClass();
        assertEquals(exceptionClass, exceptionResponseClass.getSimpleName());
    }
}

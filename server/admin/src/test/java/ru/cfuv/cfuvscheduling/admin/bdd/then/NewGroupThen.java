package ru.cfuv.cfuvscheduling.admin.bdd.then;

import io.cucumber.java.en.Then;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestContext;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NewGroupThen {

    private final AdminTestContext testContext = AdminTestContext.getINSTANCE();

    @Then("a group named {} has been added to the database")
    public void groupWillBeAdded(String name) {
        Object responceObject = testContext.getResponseObject();
        Class responceClass = testContext.getResponseClass();

        assertNotNull(responceObject);
        assertEquals(responceClass, GroupsBom.class);
    }

    @Then("the group will not be added due to an exception {}")
    public void groupWontAdded(String exceptionClass) {
        Class responceExceptionClass = testContext.getExceptionClass();

        assertEquals(exceptionClass, responceExceptionClass.getSimpleName());
    }

}

package ru.cfuv.cfuvscheduling.auth.bdd.when;

import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.auth.bdd.AuthTestContext;
import ru.cfuv.cfuvscheduling.auth.bom.AccountForm;
import ru.cfuv.cfuvscheduling.auth.bom.AccountResponse;
import ru.cfuv.cfuvscheduling.auth.conntroller.AuthController;

public class UserAuthorizationWhen {

    @Autowired
    private AuthController authController;

    private final AuthTestContext testContext = AuthTestContext.getINSTANCE();

    @When("server got authorization request with username {} and password {}")
    public void serverGotAuthorizationRequestWithUsernameAndPassword(String username, String password) {
        try {
            AccountForm accountForm = new AccountForm();
            accountForm.setUsername(username);
            accountForm.setPassword(password);
            AccountResponse response = authController.authenticateUser(accountForm);
            testContext.setResponseObject(response, AccountResponse.class);
        } catch (Exception e) {
            testContext.setExceptionObject(e, e.getClass());
        }
    }

    @When("server got authorization request with username {} and empty password")
    public void serverGotAuthorizationRequestWithUsernameAndEmptyPassword(String username) {
        serverGotAuthorizationRequestWithUsernameAndPassword(username, null);
    }

    @When("server got authorization request with empty username and password {}")
    public void serverGotAuthorizationRequestWithEmptyUsernameAndPassword(String password) {
        serverGotAuthorizationRequestWithUsernameAndPassword(null, password);
    }

    @When("server got registration request with username {} and password {}")
    public void serverGotRegistrationRequestWithUsernameAndPassword(String username, String password) {
        try {
            AccountForm accountForm = new AccountForm();
            accountForm.setUsername(username);
            accountForm.setPassword(password);
            AccountResponse response = authController.registerUser(accountForm);
            testContext.setResponseObject(response, AccountResponse.class);
        } catch (Exception e) {
            testContext.setExceptionObject(e, e.getClass());
        }
    }

    @When("server got registration request with empty username and password {}")
    public void serverGotRegistrationRequestWithEmptyUsernameAndPassword(String password) {
        serverGotRegistrationRequestWithUsernameAndPassword(null, password);
    }

    @When("server got registration request with username {} and empty password")
    public void serverGotRegistrationRequestWithUsernameAndEmptyPassword(String username) {
        serverGotRegistrationRequestWithUsernameAndPassword(username, null);
    }
}

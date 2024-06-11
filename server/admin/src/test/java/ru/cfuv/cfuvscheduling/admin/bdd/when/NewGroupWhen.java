package ru.cfuv.cfuvscheduling.admin.bdd.when;

import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestContext;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestUtils;
import ru.cfuv.cfuvscheduling.admin.connector.AuthConnector;
import ru.cfuv.cfuvscheduling.admin.controller.GroupsController;
import ru.cfuv.cfuvscheduling.admin.controller.UserValidator;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.converter.GroupsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

import static org.mockito.Mockito.*;

public class NewGroupWhen {

    @Autowired
    private GroupsController groupsController;

    @Autowired
    private AuthConnector authConnector;

    private final AdminTestContext testContext = AdminTestContext.getINSTANCE();

    @When("server got a request to add a group named {}")
    public void serverGotARequestToAddAGroupNamed(String name) {
        UserBom admin = new AdminTestUtils().createDummyUserWithRole(UserRoles.ADMIN);
        when(authConnector.getCurrentUser(anyString())).thenReturn(admin);

        try {
            GroupsBom responceGroupsBom = groupsController.group("token", (GroupsBom) testContext.getRequestObject());
            testContext.setResponseObject(responceGroupsBom, GroupsBom.class);
        } catch (Exception e) {
            testContext.setExceptionObject(e, e.getClass());
        }
    }

}

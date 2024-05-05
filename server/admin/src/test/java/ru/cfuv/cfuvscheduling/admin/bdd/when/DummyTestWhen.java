package ru.cfuv.cfuvscheduling.admin.bdd.when;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestContext;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestUtils;
import ru.cfuv.cfuvscheduling.admin.connector.AuthConnector;
import ru.cfuv.cfuvscheduling.admin.controller.GroupsController;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

public class DummyTestWhen {

    @Autowired
    private GroupsController groupsController;

    @Autowired
    private AuthConnector authConnector;

    private final AdminTestContext adminTestContext = AdminTestContext.getINSTANCE();

    @When("delete group request for current group incoming from admin")
    public void deleteGroupWithIdRequestIncoming() {
//        Clear exception obj and class (could be in new method in future)
        adminTestContext.setExceptionObject(null, null);
        UserBom dummyAdmin = new AdminTestUtils().createDummyUserWithRole(UserRoles.ADMIN);
        when(authConnector.getCurrentUser(anyString())).thenReturn(dummyAdmin);
        try {
            groupsController.deleteGroup("some token", ((GroupsDto) adminTestContext.getRequestObject()).getId());
        } catch (Exception e) {
            adminTestContext.setExceptionObject(e, e.getClass());
        }
    }
}

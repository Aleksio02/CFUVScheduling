package ru.cfuv.cfuvscheduling.admin.bdd.when;

import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestContext;
import ru.cfuv.cfuvscheduling.admin.connector.AuthConnector;
import ru.cfuv.cfuvscheduling.admin.controller.GroupsController;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.converter.GroupsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

public class NewGroupWhen {

    @Autowired
    private GroupsController groupsController;

    @Autowired
    private AuthConnector authConnector;

    private final AdminTestContext testContext = AdminTestContext.getINSTANCE();

    @When("server got a request to add a group named {}")
    public void serverGotARequestToAddAGroupNamed(String name) {
        try {
            if (name == null || name.trim().length() < 3) {
                testContext.setExceptionObject(
                        new IncorrectRequestDataException("The groups name cannot be null and should contain three or more symbols"),
                        IncorrectRequestDataException.class);
                return;
            }

            // Тут надо сгенерировать токен
            GroupsBom groupsBom = new GroupsBom();
            new GroupsConverter().fromDto((GroupsDto)testContext.getRequestObject(), groupsBom);

            // Сюда подставить сгенерированный токен
            GroupsBom responceGroupsBom = groupsController.group("token", groupsBom);
            testContext.setResponseObject(responceGroupsBom, GroupsBom.class);
        } catch (Exception e) {
            testContext.setExceptionObject(e, e.getClass());
        }
    }

}

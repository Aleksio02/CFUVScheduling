package ru.cfuv.cfuvscheduling.admin.bdd.given;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestContext;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;

public class NewGroupGiven {

    @Autowired
    private GroupsDao groupsDao;

    private final AdminTestContext testContext = AdminTestContext.getINSTANCE();

    @Given("group not exists and named {}")
    public void givenGroupNamedAndNotExists(String name) {
        groupsDao.findByName(name).
                ifPresent((i) -> {
                    throw new AlreadyExistsException("This group already exists.");
                });
    }

    @Given("group already exists and named {}")
    public void givenGroupNamedAndAlreadyExists(String name) {
        GroupsDto groupsDto = new GroupsDto();
        groupsDto.setName(name);
        groupsDao.save(groupsDto);
        testContext.setRequestObject(groupsDto, GroupsDto.class);
    }

}

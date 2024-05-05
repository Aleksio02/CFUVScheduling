package ru.cfuv.cfuvscheduling.admin.bdd.given;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestContext;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

public class DummyTestGiven {

    private final AdminTestContext adminTestContext = AdminTestContext.getINSTANCE();

    @Autowired
    private GroupsDao groupsDao;

    @Given("group named {}")
    public void givenGroupNamed(String name) {
        GroupsDto groupsDto = new GroupsDto();
        groupsDto.setName(name);
        groupsDao.save(groupsDto);
        adminTestContext.setRequestObject(groupsDto, GroupsDto.class);
    }
}

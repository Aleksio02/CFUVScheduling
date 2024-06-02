package ru.cfuv.cfuvscheduling.admin.bdd.given;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

public class NewGroupGiven {

    @Autowired
    private GroupsDao groupsDao;

    @Given("group named {}")
    public void givenGroupNamed(String name) {
        GroupsDto groupsDto = new GroupsDto();
        groupsDto.setName(name);
        groupsDao.save(groupsDto);
    }

}

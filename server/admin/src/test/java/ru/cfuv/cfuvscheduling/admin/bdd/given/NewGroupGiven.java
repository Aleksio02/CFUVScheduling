package ru.cfuv.cfuvscheduling.admin.bdd.given;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestContext;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.converter.GroupsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;

import javax.persistence.EntityNotFoundException;

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

        GroupsBom group = new GroupsBom();
        group.setName(name);
        group.setId(99999);

        testContext.setRequestObject(group, GroupsBom.class);
    }

    @Given("group name is null and group not exists")
    public void givenGroupNamedAndNotExists() {
        GroupsBom group = new GroupsBom();
        group.setId(99999);

        testContext.setRequestObject(group, GroupsBom.class);
    }

    @Given("group already exists and named {}")
    public void givenGroupNamedAndAlreadyExists(String name) {
        GroupsDto groupsDto = new GroupsDto();
        groupsDto.setName(name);
        groupsDao.save(groupsDto);

        GroupsDto findedGroupsDto = groupsDao.findByName(name).
                orElseThrow(() -> new EntityNotFoundException("Entity not found! Test CS-NewGroup-04"));

        GroupsBom groupsBom = new GroupsBom();
        new GroupsConverter().fromDto(findedGroupsDto, groupsBom);

        testContext.setRequestObject(groupsBom, GroupsBom.class);
    }

}

package ru.cfuv.cfuvscheduling.admin.bdd.then;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.admin.bdd.AdminTestContext;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

public class DummyTestThen {

    private final AdminTestContext adminTestContext = AdminTestContext.getINSTANCE();

    @Autowired
    private GroupsDao groupsDao;

    @Then("group with name {} won't exist")
    public void groupWithIdWontExist(String name) {
        assertEquals(((GroupsDto)adminTestContext.getRequestObject()).getName(), name);
        assertNull(groupsDao.findByName(name).orElse(null));
        assertNull(adminTestContext.getExceptionClass());
    }
}

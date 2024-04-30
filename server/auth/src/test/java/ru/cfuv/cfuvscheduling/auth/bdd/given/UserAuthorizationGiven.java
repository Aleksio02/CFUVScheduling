package ru.cfuv.cfuvscheduling.auth.bdd.given;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cfuv.cfuvscheduling.auth.dao.UserRolesDao;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.dao.UserDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

public class UserAuthorizationGiven {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRolesDao userRolesDao;

    @Given("user {} with password {} is exist")
    public void givenUserWithPassword(String username, String password) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setRoleId(userRolesDao.findByName(UserRoles.USER.name()).get());
        userDao.save(userDto);
    }
}

package ru.cfuv.cfuvscheduling.admin.bdd;

import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;

public class AdminTestUtils {

    public UserBom createDummyUserWithRole(UserRoles role) {
        UserBom userBom = new UserBom();
        userBom.setId(1);
        userBom.setUsername("dummy_user");
        userBom.setRoleId(1);
        userBom.setRole(role.name());
        return userBom;
    }
}

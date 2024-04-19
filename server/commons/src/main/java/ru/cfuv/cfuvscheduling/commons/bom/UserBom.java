package ru.cfuv.cfuvscheduling.commons.bom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBom {
    private String username;
    private String role;

    public boolean hasAdminRole() {
        return role.equals(UserRoles.ADMIN.name());
    }

    public boolean isTeacher() {
        return role.equals(UserRoles.TEACHER.name());
    }
}
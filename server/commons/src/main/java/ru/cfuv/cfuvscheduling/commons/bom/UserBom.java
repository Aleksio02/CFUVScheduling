package ru.cfuv.cfuvscheduling.commons.bom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBom {

    private Integer id;
    private String username;
    private Integer roleId;
    private String role;

    public boolean hasAdminRole() {
        return role.equals(UserRoles.ADMIN.name());
    }

    public boolean isTeacher() {
        return role.equals(UserRoles.TEACHER.name());
    }
}
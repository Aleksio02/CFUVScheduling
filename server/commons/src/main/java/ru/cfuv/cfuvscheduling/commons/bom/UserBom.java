package ru.cfuv.cfuvscheduling.commons.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBom {

    private Integer id;
    private String username;
    private Integer roleId;
    private String role;

    @JsonIgnore
    public boolean hasAdminRole() {
        return role.equals(UserRoles.ADMIN.name());
    }

    @JsonIgnore
    public boolean isTeacher() {
        return role.equals(UserRoles.TEACHER.name());
    }
}
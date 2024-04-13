package ru.cfuv.cfuvscheduling.auth.converter;

import ru.cfuv.cfuvscheduling.auth.bom.AccountForm;
import ru.cfuv.cfuvscheduling.auth.dao.UserRolesDao;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefUserRolesDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

public class UserConverter {
    public void fromDto(UserDto sourсe, UserBom destination) {
        destination.setUsername(sourсe.getUsername());
        destination.setRole(sourсe.getRoleId().getName());
    }

    public void fromRequestToDto(AccountForm source, UserDto destination) {
        destination.setId(null);
        destination.setUsername(source.getUsername());
        destination.setPassword(source.getPassword());
    }
}

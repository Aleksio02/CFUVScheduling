package ru.cfuv.cfuvscheduling.auth.converter;

import ru.cfuv.cfuvscheduling.auth.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

public class UserConverter {
    public void fromDto(UserDto sourсe, UserBom destination) {
        destination.setUsername(sourсe.getUsername());
        destination.setRole(sourсe.getRoleId().getName());
    }

}

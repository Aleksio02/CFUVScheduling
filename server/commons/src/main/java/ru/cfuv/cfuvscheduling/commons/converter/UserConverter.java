package ru.cfuv.cfuvscheduling.commons.converter;

import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefUserRolesDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

public class UserConverter {

    public void fromDto(UserDto source, UserBom destination) {
        destination.setId(source.getId());
        destination.setUsername(source.getUsername());
        destination.setFirstName(source.getFirstName());
        destination.setSecondName(source.getSecondName());
        destination.setLastName(source.getLastName());

        if (source.getRoleId() != null) {
            destination.setRoleId(source.getRoleId().getId());
            destination.setRole(source.getRoleId().getName());
        } else {
            destination.setRoleId(null);
            destination.setRole(null);
        }
    }

    public void toDto(UserBom source, UserDto destination) {
        destination.setId(source.getId());
        destination.setUsername(source.getUsername());
        destination.setFirstName(source.getFirstName());
        destination.setSecondName(source.getSecondName());
        destination.setLastName(source.getLastName());

        if (source.getRoleId() != null) {
            RefUserRolesDto refUserRolesDto = new RefUserRolesDto();
            refUserRolesDto.setId(source.getRoleId());
            refUserRolesDto.setName(source.getRole());
            destination.setRoleId(refUserRolesDto);
        } else {
            destination.setRoleId(null);
        }
    }
}
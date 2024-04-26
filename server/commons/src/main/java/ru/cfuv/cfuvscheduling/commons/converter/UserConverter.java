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
        destination.setRoleId(source.getRoleId().getId());
        destination.setRole(source.getRoleId().getName());
    }

    public void toDto(UserBom source, UserDto destination) {
        destination.setId(null);
        destination.setUsername(source.getUsername());
        destination.setFirstName(source.getFirstName());
        destination.setSecondName(source.getSecondName());
        destination.setLastName(source.getLastName());

        RefUserRolesDto refUserRolesDto = new RefUserRolesDto();
        refUserRolesDto.setId(source.getRoleId());
        refUserRolesDto.setName(source.getRole());
        destination.setRoleId(refUserRolesDto);
    }
}

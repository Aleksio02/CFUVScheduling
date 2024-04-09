package ru.cfuv.cfuvscheduling.admin.converter;

import ru.cfuv.cfuvscheduling.admin.bom.AddNewGroupBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

public class AddNewGroupConverter {

    public void fromDto(GroupsDto source, AddNewGroupBom destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

    public void toDto(AddNewGroupBom source, GroupsDto destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }
}

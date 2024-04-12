package ru.cfuv.cfuvscheduling.admin.converter;

import ru.cfuv.cfuvscheduling.admin.bom.GroupBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

public class GroupConverter {

    public void fromDto(GroupsDto source, GroupBom destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

    public void toDto(GroupBom source, GroupsDto destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }
}

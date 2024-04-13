package ru.cfuv.cfuvscheduling.commons.converter;

import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

public class GroupsConverter {

    public void fromDto(GroupsDto source, GroupsBom destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

    public void toDto(GroupsBom source, GroupsDto destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }
}

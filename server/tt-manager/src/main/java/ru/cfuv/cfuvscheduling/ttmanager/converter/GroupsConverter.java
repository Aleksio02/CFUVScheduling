package ru.cfuv.cfuvscheduling.ttmanager.converter;

import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.ttmanager.bom.GroupsBom;

public class GroupsConverter {

    public void fromDTO(GroupsDto source, GroupsBom destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

}

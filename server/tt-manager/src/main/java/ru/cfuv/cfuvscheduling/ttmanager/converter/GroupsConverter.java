package ru.cfuv.cfuvscheduling.ttmanager.converter;

import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

public class GroupsConverter {

    public void fromDTO(GroupsDto source, GroupsBom destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

}

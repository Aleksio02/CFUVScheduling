package ru.cfuv.cfuvscheduling.admin.converter;

import ru.cfuv.cfuvscheduling.admin.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;

public class RefClassDurationsConverter {

    public void fromDto(RefClassDurationsDto source, RefClassDurationsBom destination) {
        destination.setNumber(source.getNumber());
        destination.setStartTime(source.getStartTime());
        destination.setEndTime(source.getEndTime());
    }

}

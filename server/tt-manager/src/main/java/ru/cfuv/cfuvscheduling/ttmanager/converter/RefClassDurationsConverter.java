package ru.cfuv.cfuvscheduling.ttmanager.converter;

import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.ttmanager.bom.RefClassDurationsBom;

public class RefClassDurationsConverter {

    public void fromDto(RefClassDurationsDto source, RefClassDurationsBom destination) {
        destination.setNumber(source.getNumber());
        destination.setStartTime(source.getStartTime());
        destination.setEndTime(source.getEndTime());
    }

}

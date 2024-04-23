package ru.cfuv.cfuvscheduling.commons.converter;

import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;

public class RefClassDurationsConverter {

    public void fromDto(RefClassDurationsDto source, RefClassDurationsBom destination) {
        destination.setNumber(source.getNumber());
        destination.setStartTime(source.getStartTime());
        destination.setEndTime(source.getEndTime());
    }

}

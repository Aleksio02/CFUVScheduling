package ru.cfuv.cfuvscheduling.commons.converter;

import ru.cfuv.cfuvscheduling.commons.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;

public class RefClassTypeConverter {

    public void fromDto(RefClassTypeDto source, RefClassTypeBom destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

    public void toDto(RefClassTypeBom source, RefClassTypeDto destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }
}

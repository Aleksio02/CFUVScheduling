package ru.cfuv.cfuvscheduling.admin.converter;

import ru.cfuv.cfuvscheduling.admin.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;

public class RefClassTypeConverter {

    public void fromDto(RefClassTypeDto source, RefClassTypeBom destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

}

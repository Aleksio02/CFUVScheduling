package ru.cfuv.cfuvscheduling.admin.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.admin.dao.RefClassDurationsDao;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassDurationsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;

@Service
public class RefClassDurationsService {

    @Autowired
    private RefClassDurationsDao refClassDurationsDao;

    public List<RefClassDurationsBom> findAll() {
        List<RefClassDurationsDto> dtos = refClassDurationsDao.findAll();
        List<RefClassDurationsBom> boms = new ArrayList<RefClassDurationsBom>();

        for (int i = 0; i < dtos.size(); i++) {
            RefClassDurationsBom bom = new RefClassDurationsBom();
            new RefClassDurationsConverter().fromDto(dtos.get(i), bom);
            boms.add(bom);
        }

        return boms;
    }

}

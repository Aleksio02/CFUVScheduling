package ru.cfuv.cfuvscheduling.ttmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.ttmanager.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.ttmanager.converter.RefClassDurationsConverter;
import ru.cfuv.cfuvscheduling.ttmanager.dao.RefClassDurationsDao;

import java.util.ArrayList;
import java.util.List;

@Service
public class RefClassDurationsService {

    @Autowired
    private RefClassDurationsDao refClassDurationsDao;

    public List<RefClassDurationsBom> findAll() {
        List<RefClassDurationsDto> dtos = refClassDurationsDao.findAll();
        List<RefClassDurationsBom> boms = new ArrayList<RefClassDurationsBom>();

        for (int  i = 0; i < dtos.size(); i++) {
            RefClassDurationsBom bom = new RefClassDurationsBom();
            new RefClassDurationsConverter().fromDto(dtos.get(i), bom);
            boms.add(bom);
        }

        return boms;
    }

}

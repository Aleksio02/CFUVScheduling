package ru.cfuv.cfuvscheduling.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.admin.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.admin.converter.RefClassTypeConverter;
import ru.cfuv.cfuvscheduling.commons.dao.RefClassTypeDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;

import java.util.*;

@Service
public class RefClassTypeService {

    @Autowired
    private RefClassTypeDao refClassTypeDao;

    public List<RefClassTypeBom> findAll() {
        List<RefClassTypeDto> dtos = refClassTypeDao.findAll();
        List<RefClassTypeBom> boms = new ArrayList<RefClassTypeBom>();

        for (int i = 0; i < dtos.size(); i++) {
            RefClassTypeBom bom = new RefClassTypeBom();
            new RefClassTypeConverter().fromDto(dtos.get(i), bom);
            boms.add(bom);
        }

        return boms;
    }

}

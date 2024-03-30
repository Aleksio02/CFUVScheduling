package ru.cfuv.cfuvscheduling.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.dao.RefClassTypeDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;

import java.util.List;

@Service
public class RefClassTypeService {

    @Autowired
    private RefClassTypeDao refClassTypeDao;

    public List<RefClassTypeDto> findAll() {
        return refClassTypeDao.findAll();
    }

}

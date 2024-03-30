package ru.cfuv.cfuvscheduling.ttmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.ttmanager.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.ttmanager.converter.GroupsConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupsService {

    @Autowired
    private GroupsDao groupsDao;

    public List<GroupsBom> findAll() {
        List<GroupsDto> dtos = groupsDao.findAll();
        List<GroupsBom> boms = new ArrayList<GroupsBom>();

        for (int i = 0; i < dtos.size(); i++) {
            GroupsBom bom = new GroupsBom();
            new GroupsConverter().fromDTO(dtos.get(i), bom);
            boms.add(bom);
        }

        return boms;
    }

}

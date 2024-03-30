package ru.cfuv.cfuvscheduling.ttmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

import java.util.List;

@Service
public class GroupsService {

    @Autowired
    private GroupsDao groupsDao;

    public List<GroupsDto> findAll() {
        return groupsDao.findAll();
    }

}

package ru.cfuv.cfuvscheduling.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.converter.GroupsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;

@Service
public class GroupService {

    @Autowired
    private GroupsDao groupsDao;

    public GroupsBom addNewGroup(GroupsBom group) {
        try {
            group.setId(null);
            GroupsDto groupDto = new GroupsDto();
            GroupsConverter converter = new GroupsConverter();
            converter.toDto(group, groupDto);
            groupsDao.save(groupDto);
            converter.fromDto(groupDto, group);
            return group;
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Group with name %s already exist".formatted(group.getName()));
        }
    }
}
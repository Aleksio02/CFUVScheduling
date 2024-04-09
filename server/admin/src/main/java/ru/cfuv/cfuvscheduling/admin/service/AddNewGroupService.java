package ru.cfuv.cfuvscheduling.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.admin.bom.AddNewGroupBom;
import ru.cfuv.cfuvscheduling.admin.converter.AddNewGroupConverter;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;

@Service
public class AddNewGroupService {

    @Autowired
    private GroupsDao groupsDao;

    public AddNewGroupBom addNewGroup(AddNewGroupBom group) {
        try {
            group.setId(null);
            GroupsDto groupDto = new GroupsDto();
            AddNewGroupConverter converter = new AddNewGroupConverter();
            converter.toDto(group, groupDto);
            groupsDao.save(groupDto);
            converter.fromDto(groupDto, group);
            return group;
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Group with name %s already exist".formatted(group.getName()));
        }
    }
}
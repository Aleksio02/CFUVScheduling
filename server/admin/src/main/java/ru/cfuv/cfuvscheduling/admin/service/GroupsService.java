package ru.cfuv.cfuvscheduling.admin.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.converter.GroupsConverter;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupsService {

    @Autowired
    private GroupsDao groupsDao;

    public GroupsBom addNewGroup(GroupsBom group) {
        try {
            if (group.getName() == null || group.getName().trim().length() < 3) {
                throw new IncorrectRequestDataException("The groups name cannot be null and should contain three or more symbols");
            }
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

    public List<GroupsBom> findAll() {
        List<GroupsDto> dtos = groupsDao.findAll();
        List<GroupsBom> boms = new ArrayList<GroupsBom>();

        for (int i = 0; i < dtos.size(); i++) {
            GroupsBom bom = new GroupsBom();
            new GroupsConverter().fromDto(dtos.get(i), bom);
            boms.add(bom);
        }

        return boms;
    }

    public void updateGroupName(GroupsBom groupsBom) {
        if (groupsBom.getName() == null || groupsBom.getName().trim().length() < 3) {
            throw new IncorrectRequestDataException("The groups name cannot be null and should contain three or more symbols");
        }
        if (groupsBom.getId() == null) {
            throw new IncorrectRequestDataException("Groups id cannot be null");
        }
        try {
            GroupsDto existingGroup = groupsDao.findById(groupsBom.getId())
                    .orElseThrow(() -> new DataIntegrityViolationException ("Group with id " + groupsBom.getId() + " not found"));
            String newName = groupsBom.getName();
            existingGroup.setName(newName);
            groupsDao.save(existingGroup);
            GroupsBom updatedGroup = new GroupsBom();
            new GroupsConverter().fromDto(existingGroup, updatedGroup);
        } catch ( AlreadyExistException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}

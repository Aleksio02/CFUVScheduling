package ru.cfuv.cfuvscheduling.admin.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupsServiceTest {

    @InjectMocks
    private GroupsService groupsService;

    @Mock
    private GroupsDao groupsDao;


    @Test
    public void testAddNewGroup_Success() {
        GroupsBom groupToAdd = new GroupsBom();
        groupToAdd.setId(11);
        groupToAdd.setName("231");

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        when(groupsDao.save(any(GroupsDto.class))).thenReturn(groupDto);

        GroupsBom addedGroup = groupsService.addNewGroup(groupToAdd);

        assertEquals(groupToAdd.getId(), addedGroup.getId());
        assertEquals(groupToAdd.getName(), addedGroup.getName());
    }

    @Test
    public void testAddNewGroup_NullName() {
        GroupsBom groupToAdd = new GroupsBom();

        IncorrectRequestDataException exception = assertThrows(IncorrectRequestDataException.class, () -> groupsService.addNewGroup(groupToAdd));
        assertEquals("The groups name cannot be null and should contain three or more symbols", exception.getMessage());
    }

    @Test
    public void testAddNewGroup_ShortName() {
        GroupsBom groupToAdd = new GroupsBom();
        groupToAdd.setName("23");

        IncorrectRequestDataException exception = assertThrows(IncorrectRequestDataException.class, () -> groupsService.addNewGroup(groupToAdd));
        assertEquals("The groups name cannot be null and should contain three or more symbols", exception.getMessage());
    }

    @Test
    public void testAddNewGroup_AlreadyExists() {
        GroupsBom groupToAdd = new GroupsBom();
        groupToAdd.setName("231");

        when(groupsDao.save(any(GroupsDto.class))).thenThrow(DataIntegrityViolationException.class);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> groupsService.addNewGroup(groupToAdd));
        assertEquals("Group with name 231 already exist", exception.getMessage());
    }

    @Test
    public void testFindAll_Success() {
        GroupsDto dto1 = new GroupsDto();
        dto1.setId(11);
        dto1.setName("231");

        GroupsDto dto2 = new GroupsDto();
        dto2.setId(12);
        dto2.setName("232");

        List<GroupsDto> dtos = new ArrayList<>();
        dtos.add(dto1);
        dtos.add(dto2);

        when(groupsDao.findAll()).thenReturn(dtos);

        List<GroupsBom> result = groupsService.findAll();

        assertEquals(2, result.size());
        assertEquals("231", result.get(0).getName());
        assertEquals("232", result.get(1).getName());
    }

    @Test
    public void testUpdateGroupName_Success() {
        GroupsBom groupToUpdate = new GroupsBom();
        groupToUpdate.setId(11);
        groupToUpdate.setName("231");

        GroupsDto existingGroup = new GroupsDto();
        existingGroup.setId(10);
        existingGroup.setName("221");

        when(groupsDao.findById(11)).thenReturn(Optional.of(existingGroup));
        when(groupsDao.save(existingGroup)).thenReturn(existingGroup);

        groupsService.updateGroupName(groupToUpdate);

        assertEquals("231", existingGroup.getName());
    }

    @Test
    public void testUpdateGroupName_NullName() {
        GroupsBom groupToUpdate = new GroupsBom();
        groupToUpdate.setId(14);

        IncorrectRequestDataException exception = assertThrows(IncorrectRequestDataException.class, () -> groupsService.updateGroupName(groupToUpdate));
        assertEquals("The groups name cannot be null and should contain three or more symbols", exception.getMessage());
    }

    @Test
    public void testUpdateGroupName_ShortName() {
        GroupsBom groupToUpdate = new GroupsBom();
        groupToUpdate.setId(14);
        groupToUpdate.setName("23");

        IncorrectRequestDataException exception = assertThrows(IncorrectRequestDataException.class, () -> groupsService.updateGroupName(groupToUpdate));
        assertEquals("The groups name cannot be null and should contain three or more symbols", exception.getMessage());
    }

    @Test
    public void testUpdateGroupName_NullId() {
        GroupsBom groupToUpdate = new GroupsBom();
        groupToUpdate.setName("NewGroup");

        IncorrectRequestDataException exception = assertThrows(IncorrectRequestDataException.class, () -> groupsService.updateGroupName(groupToUpdate));
        assertEquals("Groups id cannot be null", exception.getMessage());
    }

    @Test
    public void testUpdateGroupName_GroupNotFound() {
        GroupsBom groupToUpdate = new GroupsBom();
        groupToUpdate.setId(14);
        groupToUpdate.setName("234");

        when(groupsDao.findById(14)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> groupsService.updateGroupName(groupToUpdate));
        assertEquals("Group with id 14 not found", exception.getMessage());
    }

    @Test
    public void testUpdateGroupName_AlreadyExists() {
        GroupsBom groupToUpdate = new GroupsBom();
        groupToUpdate.setId(11);
        groupToUpdate.setName("231");

        GroupsDto existingGroupDto = new GroupsDto();
        existingGroupDto.setId(11);
        existingGroupDto.setName("231");

        when(groupsDao.findById(11)).thenReturn(java.util.Optional.of(existingGroupDto));
        when(groupsDao.save(any(GroupsDto.class))).thenThrow(DataIntegrityViolationException.class);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> groupsService.updateGroupName(groupToUpdate));
        assertEquals("This name already exists", exception.getMessage());
    }

    @Test
    public void testDeleteGroup_Success() {
        GroupsBom groupToDelete = new GroupsBom();
        groupToDelete.setId(11);
        groupToDelete.setName("231");

        when(groupsDao.existsById(groupToDelete.getId())).thenReturn(true);

        groupsService.deleteGroup(groupToDelete.getId());

        verify(groupsDao, times(1)).deleteById(groupToDelete.getId());
    }

    @Test
    public void testDeleteGroup_GroupNotFound() {
        GroupsBom groupToDelete = new GroupsBom();
        groupToDelete.setId(14);
        groupToDelete.setName("234");

        when(groupsDao.existsById(groupToDelete.getId())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> groupsService.deleteGroup(groupToDelete.getId()));
        assertEquals("Group with ID 14 not found", exception.getMessage());

        verify(groupsDao, never()).deleteById(groupToDelete.getId());
    }

}


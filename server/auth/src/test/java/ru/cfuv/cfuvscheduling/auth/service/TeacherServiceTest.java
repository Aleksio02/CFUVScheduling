package ru.cfuv.cfuvscheduling.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cfuv.cfuvscheduling.auth.dao.UserRolesDao;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.dao.UserDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefUserRolesDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private UserDao userDao;

    @Mock
    private UserRolesDao userRolesDao;

    @Test
    public void testFindAllTeachers_Success() {
        UserDto user1 = new UserDto();
        UserDto user2 = new UserDto();

        RefUserRolesDto userRole = new RefUserRolesDto();
        userRole.setId(1);
        userRole.setName(UserRoles.TEACHER.name());

        user1.setRoleId(userRole);
        user2.setRoleId(userRole);

        when(userRolesDao.findByName(UserRoles.TEACHER.name())).thenReturn(Optional.of(userRole));
        when(userDao.findAllByRoleId(any())).thenReturn(List.of(user1, user2));

        List<UserBom> result = teacherService.findAllTeachers();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGiveTeacherRoleToUser_Success() {
        Integer userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);

        RefUserRolesDto userRole = new RefUserRolesDto();
        userRole.setId(1);
        userRole.setName(UserRoles.TEACHER.name());

        when(userDao.findById(userId)).thenReturn(Optional.of(userDto));
        when(userRolesDao.findByName(UserRoles.TEACHER.name())).thenReturn(Optional.of(userRole));
        when(userDao.save(any(UserDto.class))).thenReturn(new UserDto());

        teacherService.giveTeacherRoleToUser(userId);

        verify(userDao, times(1)).findById(userId);
        verify(userRolesDao, times(1)).findByName(UserRoles.TEACHER.name());
        verify(userDao, times(1)).save(userDto);
    }

    @Test
    public void testRemoveTeacherRoleFromUser_Success() {
        Integer userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);

        RefUserRolesDto userRole = new RefUserRolesDto();
        userRole.setId(2);
        userRole.setName(UserRoles.USER.name());

        when(userDao.findById(userId)).thenReturn(Optional.of(userDto));
        when(userRolesDao.findByName(UserRoles.USER.name())).thenReturn(Optional.of(userRole));
        when(userDao.save(any(UserDto.class))).thenReturn(new UserDto());

        teacherService.removeTeacherRoleFromUser(userId);

        verify(userDao, times(1)).findById(userId);
        verify(userRolesDao, times(1)).findByName(UserRoles.USER.name());
        verify(userDao, times(1)).save(userDto);
    }
}
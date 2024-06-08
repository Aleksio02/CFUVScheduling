package ru.cfuv.cfuvscheduling.ttmanager.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cfuv.cfuvscheduling.commons.bom.*;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.RefClassTypeDao;
import ru.cfuv.cfuvscheduling.commons.dao.UserDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.*;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;
import ru.cfuv.cfuvscheduling.ttmanager.bom.ClassBom;
import ru.cfuv.cfuvscheduling.ttmanager.dao.ClassDao;
import ru.cfuv.cfuvscheduling.ttmanager.dao.dto.ClassDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

    @InjectMocks
    private ClassService classService;

    @Mock
    private ClassDao classDao;

    @Mock
    private UserDao userDao;

    @Mock
    private GroupsDao groupsDao;

    @Mock
    private RefClassTypeDao classTypeDao;

    @Test
    public void testAddCommentToClass_Success() {
        UserBom currentUser = new UserBom();
        currentUser.setId(1);
        currentUser.setUsername("Смирнова С.И.");

        Integer classId = 11;
        String comment = "Пара в 402В";

        UserDto currentUserDto = new UserDto();
        currentUserDto.setId(1);
        currentUserDto.setUsername("Смирнова С.И.");

        ClassDto classDto = new ClassDto();
        classDto.setId(11);
        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        teacherDto.setRoleId(userRolesDto);
        classDto.setUserId(teacherDto);

        when(userDao.findByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUserDto));
        when(classDao.findById(classId)).thenReturn(Optional.of(classDto));

        classService.addCommentToClass(currentUser, classId, comment);
        verify(classDao).save(any(ClassDto.class));
        assertEquals(comment, classDto.getComment());
    }

    @Test
    public void testAddCommentToClass_AccessForbiddenException() {
        UserBom currentUser = new UserBom();
        currentUser.setId(2);
        currentUser.setUsername("Горская И.Ю.");

        Integer classId = 11;
        String comment = "Пара в 402В";

        UserDto currentUserDto = new UserDto();
        currentUserDto.setId(2);
        currentUserDto.setUsername("Горская И.Ю.");

        ClassDto classDto = new ClassDto();
        classDto.setId(11);
        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        teacherDto.setRoleId(userRolesDto);
        classDto.setUserId(teacherDto);

        when(userDao.findByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUserDto));
        when(classDao.findById(classId)).thenReturn(Optional.of(classDto));

        assertThrows(AccessForbiddenException.class, () -> classService.addCommentToClass(currentUser, classId, comment));
    }

    @Test
    public void testFindClassesForGroup_Success() {
        LocalDate startDate = LocalDate.of(2022, 5, 18);
        LocalDate endDate = LocalDate.of(2022, 5, 25);

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        teacherDto.setRoleId(userRolesDto);

        ClassDto class1 = new ClassDto();
        class1.setId(11);
        class1.setSubjectName("Математика");
        class1.setClassroom("211");

        RefClassDurationsDto durationDto1 = new RefClassDurationsDto();
        durationDto1.setNumber(1);

        RefClassTypeDto classTypeDto1 = new RefClassTypeDto();
        classTypeDto1.setId(1);

        UserDto teacherDto1 = new UserDto();
        teacherDto1.setId(1);
        teacherDto1.setRoleId(userRolesDto);

        LocalDate classDateDto1 = LocalDate.of(2022, 5, 20);

        class1.setDate(classDateDto1);
        class1.setTypeId(classTypeDto1);
        class1.setClassNumber(durationDto1);
        class1.setUserId(teacherDto1);
        class1.setGroupId(groupDto);


        ClassDto class2 = new ClassDto();
        class2.setId(12);
        class2.setSubjectName("Математика");
        class2.setClassroom("211");

        RefClassDurationsDto durationDto2 = new RefClassDurationsDto();
        durationDto2.setNumber(4);

        RefClassTypeDto classTypeDto2 = new RefClassTypeDto();
        classTypeDto2.setId(2);

        LocalDate classDateDto2 = LocalDate.of(2022, 5, 20);

        class2.setDate(classDateDto2);
        class2.setTypeId(classTypeDto2);
        class2.setClassNumber(durationDto2);
        class2.setUserId(teacherDto1);
        class2.setGroupId(groupDto);

        when(groupsDao.findByName(any())).thenReturn(Optional.of(groupDto));
        when(classDao.findAllByGroupIdAndDateBetween(groupDto, startDate, endDate)).thenReturn(List.of(class1, class2));

        List<ClassBom> result = classService.findClassesForGroup(groupDto.getName(), startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testCreateConsultation_Success() {
        ClassBom classBom = new ClassBom();
        classBom.setId(11);
        classBom.setSubjectName("Математика");
        classBom.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classBom.setClassDate(classDate);
        classBom.setDuration(duration);
        classBom.setTeacher(teacher);
        classBom.setGroup(group);

        UserBom currentUser = new UserBom();
        currentUser.setId(1);
        currentUser.setUsername("Смирнова С.И.");

        UserDto currentUserDto = new UserDto();
        currentUserDto.setId(1);
        currentUserDto.setUsername("Смирнова С.И.");
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        currentUserDto.setRoleId(userRolesDto);

        RefClassTypeDto refClassTypeDto = new RefClassTypeDto();
        refClassTypeDto.setId(1);
        refClassTypeDto.setName("CONSULTATION_TYPE_NAME");

        when(userDao.findByUsername(any())).thenReturn(Optional.of(currentUserDto));
        when(classTypeDao.findByName(any())).thenReturn(Optional.of(refClassTypeDto));
        when(classDao.save(any(ClassDto.class))).thenReturn(new ClassDto());

        ClassBom result = classService.createConsultation(classBom, currentUser);

        assertNotNull(result);
        verify(classDao).save(any(ClassDto.class));
    }

    @Test
    public void testCreateConsultation_IncorrectRequestDataException() {
        ClassBom classBom = new ClassBom();
        UserBom currentUser = new UserBom();

        assertThrows(IncorrectRequestDataException.class, () -> classService.createConsultation(classBom, currentUser));
    }

    @Test
    public void testCreateConsultation_AlreadyExistsException() {
        ClassBom classBom = new ClassBom();
        classBom.setId(11);
        classBom.setSubjectName("Математика");
        classBom.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classBom.setClassDate(classDate);
        classBom.setDuration(duration);
        classBom.setTeacher(teacher);
        classBom.setGroup(group);

        UserBom currentUser = new UserBom();
        currentUser.setId(1);
        currentUser.setUsername("Смирнова С.И.");

        UserDto currentUserDto = new UserDto();
        currentUserDto.setId(1);
        currentUserDto.setUsername("Смирнова С.И.");
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        currentUserDto.setRoleId(userRolesDto);

        RefClassTypeDto refClassTypeDto = new RefClassTypeDto();
        refClassTypeDto.setId(1);
        refClassTypeDto.setName("CONSULTATION_TYPE_NAME");

        when(userDao.findByUsername(any())).thenReturn(Optional.of(currentUserDto));
        when(classTypeDao.findByName(any())).thenReturn(Optional.of(refClassTypeDto));
        when(classDao.save(any(ClassDto.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AlreadyExistsException.class, () -> classService.createConsultation(classBom, currentUser));
    }

    @Test
    public void testAddClassByAdmin_Success() {
        ClassBom classToAdd = new ClassBom();
        classToAdd.setId(11);
        classToAdd.setSubjectName("Математика");
        classToAdd.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToAdd.setClassDate(classDate);
        classToAdd.setClassType(classType);
        classToAdd.setDuration(duration);
        classToAdd.setTeacher(teacher);
        classToAdd.setGroup(group);

        ClassDto classDto = new ClassDto();
        classDto.setId(11);
        classDto.setSubjectName("Математика");
        classDto.setClassroom("211");

        RefClassDurationsDto durationDto = new RefClassDurationsDto();
        durationDto.setNumber(1);

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        RefClassTypeDto classTypeDto = new RefClassTypeDto();
        classTypeDto.setId(1);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        teacherDto.setRoleId(userRolesDto);

        LocalDate classDateDto = LocalDate.of(2022, 12, 31);

        classDto.setDate(classDateDto);
        classDto.setTypeId(classTypeDto);
        classDto.setClassNumber(durationDto);
        classDto.setUserId(teacherDto);
        classDto.setGroupId(groupDto);

        when(userDao.findById(any())).thenReturn(Optional.of(teacherDto));
        when(classDao.save(any(ClassDto.class))).thenReturn(classDto);

        ClassBom addedClassByAdmin = classService.addClassByAdmin(classToAdd);

        assertEquals(classToAdd.getId(), addedClassByAdmin.getId());
    }

    @Test
    public void testAddClassByAdmin_IncorrectRequestDataException() {
        ClassBom classToAdd = new ClassBom();

        assertThrows(IncorrectRequestDataException.class, () -> classService.addClassByAdmin(classToAdd));
    }

    @Test
    public void testAddClassByAdmin_AlreadyExistsException() {
        ClassBom classToAdd = new ClassBom();
        classToAdd.setId(11);
        classToAdd.setSubjectName("Математика");
        classToAdd.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToAdd.setClassDate(classDate);
        classToAdd.setClassType(classType);
        classToAdd.setDuration(duration);
        classToAdd.setTeacher(teacher);
        classToAdd.setGroup(group);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        teacherDto.setRoleId(userRolesDto);

        when(userDao.findById(any())).thenReturn(Optional.of(teacherDto));
        when(classDao.save(any(ClassDto.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AlreadyExistsException.class, () -> classService.addClassByAdmin(classToAdd));
    }

    @Test
    public void testAddClassByAdmin_UserIsNotTeacher() {
        ClassBom classToAdd = new ClassBom();
        classToAdd.setId(11);
        classToAdd.setSubjectName("Математика");
        classToAdd.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToAdd.setClassDate(classDate);
        classToAdd.setClassType(classType);
        classToAdd.setDuration(duration);
        classToAdd.setTeacher(teacher);
        classToAdd.setGroup(group);

        ClassDto classDto = new ClassDto();
        classDto.setId(11);
        classDto.setSubjectName("Математика");
        classDto.setClassroom("211");

        RefClassDurationsDto durationDto = new RefClassDurationsDto();
        durationDto.setNumber(1);

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        RefClassTypeDto classTypeDto = new RefClassTypeDto();
        classTypeDto.setId(1);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.USER.name());
        teacherDto.setRoleId(userRolesDto);

        LocalDate classDateDto = LocalDate.of(2022, 12, 31);

        classDto.setDate(classDateDto);
        classDto.setTypeId(classTypeDto);
        classDto.setClassNumber(durationDto);
        classDto.setUserId(teacherDto);
        classDto.setGroupId(groupDto);

        when(userDao.findById(any())).thenReturn(Optional.of(teacherDto));

        assertThrows(IncorrectRequestDataException.class, () -> classService.addClassByAdmin(classToAdd));
    }

    @Test
    public void testChangeClassByAdmin_Success() {
        ClassBom classToChange = new ClassBom();
        classToChange.setId(11);
        classToChange.setSubjectName("Математика");
        classToChange.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToChange.setComment("Турум турум");

        classToChange.setClassDate(classDate);
        classToChange.setClassType(classType);
        classToChange.setDuration(duration);
        classToChange.setTeacher(teacher);
        classToChange.setGroup(group);

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("Смирнова С.И.");
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        userDto.setRoleId(userRolesDto);
        userDto.setRoleId(userRolesDto);

        ClassDto existsClassDto = new ClassDto();
        existsClassDto.setId(11);
        existsClassDto.setSubjectName("Математика");
        existsClassDto.setClassroom("211");

        RefClassDurationsDto durationDto = new RefClassDurationsDto();
        durationDto.setNumber(1);

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        RefClassTypeDto classTypeDto = new RefClassTypeDto();
        classTypeDto.setId(1);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        teacherDto.setRoleId(userRolesDto);

        LocalDate classDateDto = LocalDate.of(2022, 12, 31);

        existsClassDto.setComment("Турум турум");

        existsClassDto.setDate(classDateDto);
        existsClassDto.setTypeId(classTypeDto);
        existsClassDto.setClassNumber(durationDto);
        existsClassDto.setUserId(teacherDto);
        existsClassDto.setGroupId(groupDto);

        when(userDao.findById(classToChange.getTeacher().getId())).thenReturn(Optional.of(userDto));
        when(classDao.findById(classToChange.getId())).thenReturn(Optional.of(existsClassDto));
        when(classDao.save(any(ClassDto.class))).thenReturn(existsClassDto);

        classService.changeClassByAdmin(classToChange);

        verify(classDao).save(any(ClassDto.class));
    }

    @Test
    public void testChangeClassByAdmin_IncorrectRequestDataException() {
        ClassBom classToChange = new ClassBom();

        assertThrows(IncorrectRequestDataException.class, () -> classService.changeClassByAdmin(classToChange));
    }

    @Test
    public void testChangeClassByAdmin_UserNotFound() {
        ClassBom classToChange = new ClassBom();
        classToChange.setId(11);
        classToChange.setSubjectName("Математика");
        classToChange.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToChange.setComment("Турум турум");

        classToChange.setClassDate(classDate);
        classToChange.setClassType(classType);
        classToChange.setDuration(duration);
        classToChange.setTeacher(teacher);
        classToChange.setGroup(group);

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("Смирнова С.И.");
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        userDto.setRoleId(userRolesDto);
        userDto.setRoleId(userRolesDto);

        ClassDto existsClassDto = new ClassDto();
        existsClassDto.setId(11);
        existsClassDto.setSubjectName("Математика");
        existsClassDto.setClassroom("211");

        RefClassDurationsDto durationDto = new RefClassDurationsDto();
        durationDto.setNumber(1);

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        RefClassTypeDto classTypeDto = new RefClassTypeDto();
        classTypeDto.setId(1);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        teacherDto.setRoleId(userRolesDto);

        LocalDate classDateDto = LocalDate.of(2022, 12, 31);

        existsClassDto.setComment("Турум турум");

        existsClassDto.setDate(classDateDto);
        existsClassDto.setTypeId(classTypeDto);
        existsClassDto.setClassNumber(durationDto);
        existsClassDto.setUserId(teacherDto);
        existsClassDto.setGroupId(groupDto);

        when(userDao.findById(classToChange.getTeacher().getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> classService.changeClassByAdmin(classToChange));
        verify(userDao).findById(classToChange.getTeacher().getId());
        verify(classDao, never()).save(any(ClassDto.class));
    }

    @Test
    public void testChangeClassByAdmin_NewUserIsNotTeacher() {
        ClassBom classToChange = new ClassBom();
        classToChange.setId(11);
        classToChange.setSubjectName("Математика");
        classToChange.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToChange.setComment("Турум турум");

        classToChange.setClassDate(classDate);
        classToChange.setClassType(classType);
        classToChange.setDuration(duration);
        classToChange.setTeacher(teacher);
        classToChange.setGroup(group);

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("Ищенко А.И.");
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.USER.name());
        userDto.setRoleId(userRolesDto);
        userDto.setRoleId(userRolesDto);

        ClassDto existsClassDto = new ClassDto();
        existsClassDto.setId(11);
        existsClassDto.setSubjectName("Математика");
        existsClassDto.setClassroom("211");

        RefClassDurationsDto durationDto = new RefClassDurationsDto();
        durationDto.setNumber(1);

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        RefClassTypeDto classTypeDto = new RefClassTypeDto();
        classTypeDto.setId(1);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        teacherDto.setRoleId(userRolesDto);

        LocalDate classDateDto = LocalDate.of(2022, 12, 31);

        existsClassDto.setComment("Турум турум");

        existsClassDto.setDate(classDateDto);
        existsClassDto.setTypeId(classTypeDto);
        existsClassDto.setClassNumber(durationDto);
        existsClassDto.setUserId(teacherDto);
        existsClassDto.setGroupId(groupDto);

        when(userDao.findById(classToChange.getTeacher().getId())).thenReturn(Optional.of(userDto));

        assertThrows(IncorrectRequestDataException.class, () -> classService.changeClassByAdmin(classToChange));
        verify(userDao).findById(classToChange.getTeacher().getId());
        verify(classDao, never()).save(any(ClassDto.class));
    }

    @Test
    public void testChangeClassByAdmin_ClassNotFound() {
        ClassBom classToChange = new ClassBom();
        classToChange.setId(11);
        classToChange.setSubjectName("Математика");
        classToChange.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToChange.setComment("Турум турум");

        classToChange.setClassDate(classDate);
        classToChange.setClassType(classType);
        classToChange.setDuration(duration);
        classToChange.setTeacher(teacher);
        classToChange.setGroup(group);

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("Смирнова С.И.");
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        userDto.setRoleId(userRolesDto);
        userDto.setRoleId(userRolesDto);

        ClassDto existsClassDto = new ClassDto();
        existsClassDto.setId(11);
        existsClassDto.setSubjectName("Математика");
        existsClassDto.setClassroom("211");

        RefClassDurationsDto durationDto = new RefClassDurationsDto();
        durationDto.setNumber(1);

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        RefClassTypeDto classTypeDto = new RefClassTypeDto();
        classTypeDto.setId(1);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        teacherDto.setRoleId(userRolesDto);

        LocalDate classDateDto = LocalDate.of(2022, 12, 31);

        existsClassDto.setComment("Турум турум");

        existsClassDto.setDate(classDateDto);
        existsClassDto.setTypeId(classTypeDto);
        existsClassDto.setClassNumber(durationDto);
        existsClassDto.setUserId(teacherDto);
        existsClassDto.setGroupId(groupDto);

        when(userDao.findById(classToChange.getTeacher().getId())).thenReturn(Optional.of(userDto));
        when(classDao.findById(classToChange.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> classService.changeClassByAdmin(classToChange));
        verify(userDao).findById(classToChange.getTeacher().getId());
        verify(classDao).findById(classToChange.getId());
        verify(classDao, never()).save(any(ClassDto.class));
    }


    @Test
    public void testChangeClassByAdmin_AlreadyExistsException() {
        ClassBom classToChange = new ClassBom();
        classToChange.setId(11);
        classToChange.setSubjectName("Математика");
        classToChange.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToChange.setComment("Турум турум");

        classToChange.setClassDate(classDate);
        classToChange.setClassType(classType);
        classToChange.setDuration(duration);
        classToChange.setTeacher(teacher);
        classToChange.setGroup(group);

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("Смирнова С.И.");
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        userDto.setRoleId(userRolesDto);
        userDto.setRoleId(userRolesDto);

        ClassDto existsClassDto = new ClassDto();
        existsClassDto.setId(11);
        existsClassDto.setSubjectName("Математика");
        existsClassDto.setClassroom("211");

        RefClassDurationsDto durationDto = new RefClassDurationsDto();
        durationDto.setNumber(1);

        GroupsDto groupDto = new GroupsDto();
        groupDto.setId(11);
        groupDto.setName("231");

        RefClassTypeDto classTypeDto = new RefClassTypeDto();
        classTypeDto.setId(1);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        teacherDto.setRoleId(userRolesDto);

        LocalDate classDateDto = LocalDate.of(2022, 12, 31);

        existsClassDto.setComment("Турум турум");

        existsClassDto.setDate(classDateDto);
        existsClassDto.setTypeId(classTypeDto);
        existsClassDto.setClassNumber(durationDto);
        existsClassDto.setUserId(teacherDto);
        existsClassDto.setGroupId(groupDto);

        when(userDao.findById(classToChange.getTeacher().getId())).thenReturn(Optional.of(userDto));
        when(classDao.findById(classToChange.getId())).thenReturn(Optional.of(existsClassDto));
        when(classDao.save(any(ClassDto.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AlreadyExistsException.class, () -> classService.changeClassByAdmin(classToChange));
        verify(userDao).findById(classToChange.getTeacher().getId());
        verify(classDao).findById(classToChange.getId());
        verify(classDao).save(any(ClassDto.class));
    }

    @Test
    public void testDeleteClassByAdmin_Success() {
        ClassBom classToDelete = new ClassBom();
        classToDelete.setId(11);
        classToDelete.setSubjectName("Математика");
        classToDelete.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToDelete.setClassDate(classDate);
        classToDelete.setClassType(classType);
        classToDelete.setDuration(duration);
        classToDelete.setTeacher(teacher);
        classToDelete.setGroup(group);

        when(classDao.existsById(classToDelete.getId())).thenReturn(true);

        classService.deleteClassByAdmin(classToDelete.getId());

        verify(classDao, times(1)).deleteById(classToDelete.getId());
    }

    @Test
    public void testDeleteClassByAdmin_ClassNotFound() {
        ClassBom classToDelete = new ClassBom();
        classToDelete.setId(11);
        classToDelete.setSubjectName("Математика");
        classToDelete.setClassroom("211");

        RefClassDurationsBom duration = new RefClassDurationsBom();
        duration.setNumber(1);

        GroupsBom group = new GroupsBom();
        group.setId(11);
        group.setName("231");

        RefClassTypeBom classType = new RefClassTypeBom();
        classType.setId(1);

        UserBom teacher = new UserBom();
        teacher.setId(1);

        LocalDate classDate = LocalDate.of(2022, 12, 31);

        classToDelete.setClassDate(classDate);
        classToDelete.setClassType(classType);
        classToDelete.setDuration(duration);
        classToDelete.setTeacher(teacher);
        classToDelete.setGroup(group);

        when(classDao.existsById(classToDelete.getId())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> classService.deleteClassByAdmin(classToDelete.getId()));

        verify(classDao, never()).deleteById(classToDelete.getId());
    }

    @Test
    public void testFindClassesForTeacher_Success() {
        LocalDate startDate = LocalDate.of(2022, 5, 18);
        LocalDate endDate = LocalDate.of(2022, 5, 25);

        UserDto teacherDto = new UserDto();
        teacherDto.setId(1);
        RefUserRolesDto userRolesDto = new RefUserRolesDto();
        userRolesDto.setId(1);
        userRolesDto.setName(UserRoles.TEACHER.name());
        teacherDto.setRoleId(userRolesDto);

        ClassDto class1 = new ClassDto();
        class1.setId(11);
        class1.setSubjectName("Математика");
        class1.setClassroom("211");

        RefClassDurationsDto durationDto1 = new RefClassDurationsDto();
        durationDto1.setNumber(1);

        GroupsDto groupDto1 = new GroupsDto();
        groupDto1.setId(11);
        groupDto1.setName("231");

        RefClassTypeDto classTypeDto1 = new RefClassTypeDto();
        classTypeDto1.setId(1);

        UserDto teacherDto1 = new UserDto();
        teacherDto1.setId(1);
        teacherDto1.setRoleId(userRolesDto);

        LocalDate classDateDto1 = LocalDate.of(2022, 5, 20);

        class1.setDate(classDateDto1);
        class1.setTypeId(classTypeDto1);
        class1.setClassNumber(durationDto1);
        class1.setUserId(teacherDto1);
        class1.setGroupId(groupDto1);

        ClassDto class2 = new ClassDto();
        class2.setId(12);
        class2.setSubjectName("Математика");
        class2.setClassroom("211");

        RefClassDurationsDto durationDto2 = new RefClassDurationsDto();
        durationDto2.setNumber(2);

        GroupsDto groupDto2 = new GroupsDto();
        groupDto2.setId(12);
        groupDto2.setName("232");

        RefClassTypeDto classTypeDto2 = new RefClassTypeDto();
        classTypeDto2.setId(1);

        LocalDate classDateDto2 = LocalDate.of(2022, 5, 20);

        class2.setDate(classDateDto2);
        class2.setTypeId(classTypeDto2);
        class2.setClassNumber(durationDto2);
        class2.setUserId(teacherDto1);
        class2.setGroupId(groupDto2);

        when(userDao.findById(any())).thenReturn(Optional.of(teacherDto));
        when(classDao.findAllByUserIdAndDateBetween(teacherDto, startDate, endDate)).thenReturn(List.of(class1, class2));

        List<ClassBom> result = classService.findClassesForTeacher(teacherDto.getId(), startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
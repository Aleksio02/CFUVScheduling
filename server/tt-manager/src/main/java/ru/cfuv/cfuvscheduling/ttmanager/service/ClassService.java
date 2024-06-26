package ru.cfuv.cfuvscheduling.ttmanager.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.converter.GroupsConverter;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassDurationsConverter;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassTypeConverter;
import ru.cfuv.cfuvscheduling.commons.converter.UserConverter;
import ru.cfuv.cfuvscheduling.commons.dao.GroupsDao;
import ru.cfuv.cfuvscheduling.commons.dao.RefClassTypeDao;
import ru.cfuv.cfuvscheduling.commons.dao.UserDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;
import ru.cfuv.cfuvscheduling.ttmanager.bom.ClassBom;
import ru.cfuv.cfuvscheduling.ttmanager.converter.ClassConverter;
import ru.cfuv.cfuvscheduling.ttmanager.dao.ClassDao;
import ru.cfuv.cfuvscheduling.ttmanager.dao.dto.ClassDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {

    private final String CONSULTATION_TYPE_NAME = "consultation";

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private RefClassTypeDao classTypeDao;

    @Autowired
    private ClassDao classDao;


    public void addCommentToClass(UserBom currentUser, Integer id, String comment) {
        UserDto currentUserDto = userDao.findByUsername(currentUser.getUsername()).get();
        ClassDto classDto = classDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Class with this id doesn't exist."));
        if (currentUserDto.getId().equals(classDto.getUserId().getId())) {
            classDto.setComment(comment);
            classDao.save(classDto);
            return;
        }
        throw new AccessForbiddenException("You can't add comment to this class");
    }

    public List<ClassBom> findClassesForGroup(String groupName, LocalDate startDate, LocalDate endDate) {
        GroupsDto foundGroup = groupsDao.findByName(groupName)
                .orElseThrow(() -> new EntityNotFoundException("Group with given name isn't exist"));
        return classDao.findAllByGroupIdAndDateBetween(foundGroup, startDate, endDate).stream().map(i -> {
            ClassBom classBom = new ClassBom();
            new ClassConverter().fromDto(i, classBom);
            return classBom;
        }).collect(Collectors.toList());
    }

    public ClassBom createConsultation(ClassBom classBom, UserBom currentUser) {
        if (classBom.getSubjectName() == null || classBom.getClassroom() == null ||
                classBom.getDuration() == null || classBom.getDuration().getNumber() == null ||
                classBom.getGroup() == null || classBom.getGroup().getId() == null || classBom.getClassDate() == null) {
            throw new IncorrectRequestDataException("Object fields can't be null");
        }

        UserDto currentUserDto = userDao.findByUsername(currentUser.getUsername()).get();
        RefClassTypeDto refClassTypeDto = classTypeDao.findByName(CONSULTATION_TYPE_NAME).get();
        ClassDto classDto = new ClassDto();
        new ClassConverter().toDto(classBom, classDto);
        classDto.setId(null);
        classDto.setUserId(currentUserDto);
        classDto.setTypeId(refClassTypeDto);
        try {
            classDao.save(classDto);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("You can't create consultation in this day and this place with given group");
        }
        new ClassConverter().fromDto(classDto, classBom);
        return classBom;
    }

    public ClassBom addClassByAdmin(ClassBom classBom) {
        if (classBom.getSubjectName() == null ||
                classBom.getClassroom() == null ||
                classBom.getDuration() == null ||
                classBom.getDuration().getNumber() == null ||
                classBom.getGroup() == null ||
                classBom.getGroup().getId() == null ||
                classBom.getClassType() == null ||
                classBom.getClassType().getId() == null ||
                classBom.getTeacher() == null ||
                classBom.getTeacher().getId() == null ||
                classBom.getClassDate() == null
        ) {
            throw new IncorrectRequestDataException("Object fields can't be null");
        }

        try {
            classBom.setId(null);
            ClassDto classDto = new ClassDto();
            ClassConverter converter = new ClassConverter();
            converter.toDto(classBom, classDto);
            UserDto receivedUser = userDao.findById(classDto.getUserId().getId()).get();
            if (!receivedUser.getRoleId().getName().equals(UserRoles.TEACHER.name())) {
                throw new IncorrectRequestDataException("User is not a teacher");
            }
            classDao.save(classDto);
            converter.fromDto(classDto, classBom);
            return classBom;
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("You can't create class in this day and this place with given group");
        }
    }

    public void changeClassByAdmin(ClassBom classBom) {
        if (classBom.getId() == null
                || classBom.getSubjectName() == null
                || classBom.getClassroom() == null
                || classBom.getDuration() == null
                || classBom.getDuration().getNumber() == null
                || classBom.getComment() == null
                || classBom.getGroup() == null
                || classBom.getGroup().getId() == null
                || classBom.getClassType() == null
                || classBom.getClassType().getId() == null
                || classBom.getTeacher() == null
                || classBom.getTeacher().getId() == null
                || classBom.getClassDate() == null) {
            throw new IncorrectRequestDataException("Obj fields can't be null");
        }
        try {
            UserDto newUser = userDao.findById(classBom.getTeacher().getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            if (!newUser.getRoleId().getName().equals(UserRoles.TEACHER.name())) {
                throw new IncorrectRequestDataException("New user is not a teacher");
            }
            ClassDto existsClassDto = classDao.findById(classBom.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Class with this ID was not found"));
            ClassBom existClassBom = new ClassBom();
            new ClassConverter().fromDto(existsClassDto, existClassBom);

            existClassBom.setSubjectName(classBom.getSubjectName());
            existClassBom.setClassroom(classBom.getClassroom());
            existClassBom.getDuration().setNumber(classBom.getDuration().getNumber());
            existClassBom.setComment(classBom.getComment());
            existClassBom.getGroup().setId(classBom.getGroup().getId());
            existClassBom.getClassType().setId(classBom.getClassType().getId());
            existClassBom.getTeacher().setId(classBom.getTeacher().getId());
            existClassBom.setClassDate(classBom.getClassDate());

            new ClassConverter().toDto(existClassBom, existsClassDto);
            classDao.save(existsClassDto);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("You can't change class in this day and this place with given group");
        }
    }

    public void deleteClassByAdmin(Integer classId) {
        if (!classDao.existsById(classId)) {
            throw new EntityNotFoundException("Class with ID %d not found".formatted(classId));
        }
        classDao.deleteById(classId);
    }

    public List<ClassBom> findClassesForTeacher(Integer userId, LocalDate startDate, LocalDate endDate) {
        UserDto foundTeacher = userDao.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with given id isn't exist"));
        return classDao.findAllByUserIdAndDateBetween(foundTeacher, startDate, endDate).stream().map(i -> {
            ClassBom classBom = new ClassBom();
            new ClassConverter().fromDto(i, classBom);
            return classBom;
        }).collect(Collectors.toList());
    }
}

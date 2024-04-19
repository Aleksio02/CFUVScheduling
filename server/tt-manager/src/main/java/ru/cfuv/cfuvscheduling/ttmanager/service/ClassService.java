package ru.cfuv.cfuvscheduling.ttmanager.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.dao.UserDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;
import ru.cfuv.cfuvscheduling.ttmanager.dao.ClassDao;
import ru.cfuv.cfuvscheduling.ttmanager.dao.dto.ClassDto;

@Service
public class ClassService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ClassDao classDao;

    public void addCommentToClass(UserBom currentUser, Integer id, String comment) {
        UserDto currentUserDto = userDao.findByUsername(currentUser.getUsername()).get();
        ClassDto classDto = classDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Class with this id doesn't exist."));
        if (currentUserDto.getId().equals(classDto.getUserId().getId())) {
            classDto.setComment(comment);
            classDao.save(classDto);
            return;
        }
        throw new AccessForbiddenException("You can't add comment to this class");
    }

}

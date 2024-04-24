package ru.cfuv.cfuvscheduling.auth.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.auth.dao.UserRolesDao;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.converter.UserConverter;
import ru.cfuv.cfuvscheduling.commons.dao.UserDao;

@Service
public class TeacherService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRolesDao userRolesDao;

    public List<UserBom> findAllTeachers() {
        return userDao.findAllByRoleId(userRolesDao.findByName(UserRoles.TEACHER.name()).get()).stream().map(i -> {
            UserBom teacher = new UserBom();
            new UserConverter().fromDto(i, teacher);
            return teacher;
        }).collect(Collectors.toList());
    }
}

package ru.cfuv.cfuvscheduling.auth.conntroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.auth.service.TeacherService;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;

import java.util.List;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/findAllTeachers")
    List<UserBom> findAllTeachers(@RequestHeader(name = "Authorization", required = false) String token) {
        return teacherService.findAllTeachers();
    }

    @PostMapping("/changeUserRoleToTeacher/{userId}")
    public void changeUserRoleToTeacher(@RequestHeader(name = "Authorization", required = false) String token, @PathVariable Integer userId) {
        if (!authService.getCurrentUser(token).hasAdminRole()) {
            throw new AccessForbiddenException("You don't have access to this action!");
        }
        teacherService.changeUserRoleToTeacher(userId);
    }
}


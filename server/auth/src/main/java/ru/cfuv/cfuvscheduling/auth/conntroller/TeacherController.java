package ru.cfuv.cfuvscheduling.auth.conntroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;
import ru.cfuv.cfuvscheduling.auth.service.TeacherService;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;

import java.util.List;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private AuthService authService;

    @GetMapping("/findAllTeachers")
    List<UserBom> findAllTeachers(@RequestHeader(name = "Authorization", required = false) String token) {
        return teacherService.findAllTeachers();
    }

    @PostMapping("/giveTeacherRoleToUser/{userId}")
    public void giveTeacherRoleToUser(@RequestHeader(name = "Authorization", required = false) String token, @PathVariable Integer userId) {
        if (!authService.getCurrentUser(token).hasAdminRole()) {
            throw new AccessForbiddenException("You don't have access to this action!");
        }
        teacherService.giveTeacherRoleToUser(userId);
    }
}


package ru.cfuv.cfuvscheduling.auth.conntroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;
import ru.cfuv.cfuvscheduling.auth.service.TeacherService;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/findAllTeachers")
    List<UserBom> findAllTeachers(@RequestHeader(name = "Authorization", required = false) String token) {
        if (!authService.getCurrentUser(token).hasAdminRole()) {
            throw new AccessForbiddenException("You don't have access to this action!");
        }
        return teacherService.findAllTeachers();
    }

    @PostMapping("/{userId}/role")
    public void addTeacherRole(@PathVariable Integer userId) {
        teacherService.changeUserRoleToTeacher(userId);
    }
}

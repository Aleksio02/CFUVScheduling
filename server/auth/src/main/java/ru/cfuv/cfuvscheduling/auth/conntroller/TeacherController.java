package ru.cfuv.cfuvscheduling.auth.conntroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;
import ru.cfuv.cfuvscheduling.auth.service.TeacherService;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;

@RestController
@RequestMapping("/admin/teacher")
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
}

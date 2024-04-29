package ru.cfuv.cfuvscheduling.auth.conntroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;
import ru.cfuv.cfuvscheduling.auth.service.TeacherService;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;

import java.util.List;

@RestController
@RequestMapping("/admin/teacher")
public class TeacherController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/findAllTeachers")
    List<UserBom> findAllTeachers(@RequestHeader(name = "Authorization", required = false) String token) {
        return teacherService.findAllTeachers();
    }
}

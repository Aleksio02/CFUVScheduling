package ru.cfuv.cfuvscheduling.ttmanager.controller;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.ttmanager.service.ClassService;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private ClassService classService;

    @GetMapping("/{id}/addComment")
    public void addCommentToClass(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestParam @Nullable String comment) {
        UserBom currentUser = userValidator.validateUserAsTeacher(token);
        classService.addCommentToClass(currentUser, id, comment);
    }
}

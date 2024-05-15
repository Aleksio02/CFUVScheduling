package ru.cfuv.cfuvscheduling.ttmanager.controller;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.ttmanager.bom.ClassBom;
import ru.cfuv.cfuvscheduling.ttmanager.service.ClassService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tt-manager/class")
public class ClassController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private ClassService classService;

    @PostMapping("/{id}/addComment")
    public void addCommentToClass(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable Integer id,
            @RequestParam @Nullable String comment
    ) {
        UserBom currentUser = userValidator.validateUserAsTeacher(token);
        classService.addCommentToClass(currentUser, id, comment);
    }

    @GetMapping("/findClassesForGroup")
    public List<ClassBom> findClassesForGroup(
            @RequestParam String groupName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return classService.findClassesForGroup(groupName, startDate, endDate);
    }

    @PostMapping("/createConsultation")
    public ClassBom createConsultation(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestBody ClassBom classBom
    ) {
        UserBom currentUser = userValidator.validateUserAsTeacher(token);
        return classService.createConsultation(classBom, currentUser);
    }

    @PostMapping("/addClassByAdmin")
    public ClassBom addClassByAdmin(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestBody ClassBom classBom
    ) {
        userValidator.validateUserAsAdmin(token);
        return classService.addClassByAdmin(classBom);
    }

    @PostMapping("/changeClassByAdmin")
    public void changeClassByAdmin(
            @RequestHeader(name = "Aruthorization", required = false) String token,
            @RequestBody ClassBom classBom
    ) {
        userValidator.validateUserAsAdmin(token);
        classService.changeClassByAdmin(classBom);
    }

    @DeleteMapping("/deleteClassByAdmin/{id}")
    public void deleteClassByAdmin(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable Integer id
    ) {
        userValidator.validateUserAsAdmin(token);
        classService.deleteClassByAdmin(id);
    }

    @GetMapping("/findClassesForTeacher")
    public List<ClassBom> findClassesForTeacher(
            @RequestParam Integer userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return classService.findClassesForTeacher(userId, startDate, endDate);
    }
}

package ru.cfuv.cfuvscheduling.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.admin.service.RefClassDurationsService;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;

import java.util.List;

@RestController
@RequestMapping("/admin/refClassDurations")
public class RefClassDurationsController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private RefClassDurationsService refClassDurationsService;

    @GetMapping("/getListOfClassNumbers")
    public List<RefClassDurationsBom> getListOfClassNumbers() {
        return refClassDurationsService.findAll();
    }

    @PostMapping("/addClassDuration")
    public RefClassDurationsBom addClassDuration(@RequestBody RefClassDurationsBom classDuration, @RequestHeader(name = "Authorization", required = false) String token) {
        userValidator.validateUserAsAdmin(token);
        return refClassDurationsService.addClassDuration(classDuration);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClassDuration(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable Integer id
    ) {
        userValidator.validateUserAsAdmin(token);
        refClassDurationsService.deleteClassDuration(id);
    }
}

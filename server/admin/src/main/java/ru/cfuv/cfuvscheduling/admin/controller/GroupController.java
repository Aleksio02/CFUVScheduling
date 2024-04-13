package ru.cfuv.cfuvscheduling.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.admin.service.GroupService;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;

@RestController
@RequestMapping("/admin/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserValidator userValidator;
    @PostMapping("/addNewGroup")
    public GroupsBom group(@RequestHeader("Authorization") String token, @RequestBody GroupsBom group) {
        userValidator.validateUserAsAdmin(token);
        return groupService.addNewGroup(group);
    }
}
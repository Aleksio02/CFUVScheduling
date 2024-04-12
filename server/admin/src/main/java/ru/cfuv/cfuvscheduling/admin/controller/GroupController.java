package ru.cfuv.cfuvscheduling.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.admin.bom.GroupBom;
import ru.cfuv.cfuvscheduling.admin.service.GroupService;

@RestController
@RequestMapping("/admin/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserValidator userValidator;
    @PostMapping("/addNewGroup")
    public GroupBom group(@RequestHeader("Authorization") String token, @RequestBody GroupBom group) {
        userValidator.validateUserAsAdmin(token);
        return groupService.addNewGroup(group);
    }
}
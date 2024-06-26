package ru.cfuv.cfuvscheduling.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.admin.service.GroupsService;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;

import java.util.List;

@RestController
@RequestMapping("/admin/group")
public class GroupsController {

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private UserValidator userValidator;

    @PostMapping("/addNewGroup")
    public GroupsBom group(@RequestHeader(name = "Authorization", required = false) String token, @RequestBody GroupsBom group) {
        userValidator.validateUserAsAdmin(token);
        return groupsService.addNewGroup(group);
    }

    @GetMapping("/findAll")
    public List<GroupsBom> findAllGroups() {
        return groupsService.findAll();
    }

    @PostMapping("/renameGroup")
    public void updateGroupName(@RequestHeader("Authorization") String jwtToken, @RequestBody GroupsBom groupsBom) {
        userValidator.validateUserAsAdmin(jwtToken);
        groupsService.updateGroupName(groupsBom);
    }

    @DeleteMapping("/deleteGroup/{id}")
    public void deleteGroup(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable Integer id
    ) {
        userValidator.validateUserAsAdmin(token);
        groupsService.deleteGroup(id);
    }
}


package ru.cfuv.cfuvscheduling.ttmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.ttmanager.service.GroupsService;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupsController {

    @Autowired
    private GroupsService groupsService;

    @GetMapping("/findAll")
    public List<GroupsDto> findAllGroups() {
        return groupsService.findAll();
    }
}

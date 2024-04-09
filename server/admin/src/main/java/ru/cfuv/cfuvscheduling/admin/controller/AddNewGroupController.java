package ru.cfuv.cfuvscheduling.admin.controller;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.admin.bom.AddNewGroupBom;
import ru.cfuv.cfuvscheduling.admin.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.admin.connector.AuthConnector;
import ru.cfuv.cfuvscheduling.admin.service.AddNewGroupService;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;
import ru.cfuv.cfuvscheduling.commons.exception.UnauthorizedException;

import java.util.List;

@RestController
@RequestMapping("/admin/group")
public class AddNewGroupController {

    @Autowired
    private AddNewGroupService addNewGroupService;

    @Autowired
    private AuthConnector authConnector;

    @PostMapping("/addNewGroup")
    public AddNewGroupBom addNewGroup(@RequestHeader("Authorization") String token, @RequestBody AddNewGroupBom group) {
        validateUserAsAdmin(token);
        return addNewGroupService.addNewGroup(group);
    }

    private void validateUserAsAdmin(String token) {
        String exceptionMessage = "You don't have access to this action";
        try {
            if (!authConnector.getCurrentUser(token).hasAdminRole()) {
                throw new AccessForbiddenException(exceptionMessage);
            }
        } catch (FeignException e) {
            throw new UnauthorizedException(exceptionMessage);
        }
    }

}

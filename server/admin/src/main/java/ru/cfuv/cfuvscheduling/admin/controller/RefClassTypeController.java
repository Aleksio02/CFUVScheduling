package ru.cfuv.cfuvscheduling.admin.controller;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.admin.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.admin.connector.AuthConnector;
import ru.cfuv.cfuvscheduling.admin.service.RefClassTypeService;

import java.util.List;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;
import ru.cfuv.cfuvscheduling.commons.exception.UnauthorizedException;

@RestController
@RequestMapping("/admin/classType")
public class RefClassTypeController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private RefClassTypeService refClassTypeService;

    @GetMapping("/findAll")
    public List<RefClassTypeBom> findAll() {
        return refClassTypeService.findAll();
    }

    @PostMapping("/create")
    public RefClassTypeBom createClassType(@RequestHeader(name = "Authorization",required = false) String token, @RequestBody RefClassTypeBom type) {
        userValidator.validateUserAsAdmin(token);
        return refClassTypeService.createClassType(type);
    }

    @PostMapping("/rename")
    public RefClassTypeBom renameClassType(@RequestHeader(name = "Authorization", required = false) String token, @RequestBody RefClassTypeBom type) {
        userValidator.validateUserAsAdmin(token);
        return refClassTypeService.renameClassType(type);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClassType(@RequestHeader(name = "Authorization", required = false) String token, @PathVariable Integer id) {
        userValidator.validateUserAsAdmin(token);
        refClassTypeService.deleteClassType(id);
    }
}

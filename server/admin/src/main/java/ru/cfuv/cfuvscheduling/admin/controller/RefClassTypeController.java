package ru.cfuv.cfuvscheduling.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.admin.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.admin.service.RefClassTypeService;

import java.util.List;

@RestController
@RequestMapping("/refClassType")
public class RefClassTypeController {

    @Autowired
    private RefClassTypeService refClassTypeService;

    @GetMapping("/findAll")
    public List<RefClassTypeBom> findAll() {
        return refClassTypeService.findAll();
    }

}

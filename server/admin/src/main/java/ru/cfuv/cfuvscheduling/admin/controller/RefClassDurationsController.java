package ru.cfuv.cfuvscheduling.admin.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.admin.service.RefClassDurationsService;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;

@RestController
@RequestMapping("/refClassDurations")
public class RefClassDurationsController {

    @Autowired
    private RefClassDurationsService refClassDurationsService;

    @GetMapping("/getListOfClassNumbers")
    public List<RefClassDurationsBom> getListOfClassNumbers() {
        return refClassDurationsService.findAll();
    }

}

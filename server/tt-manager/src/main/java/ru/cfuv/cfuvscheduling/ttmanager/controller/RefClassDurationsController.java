package ru.cfuv.cfuvscheduling.ttmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.ttmanager.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.ttmanager.service.RefClassDurationsService;

import java.util.List;

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

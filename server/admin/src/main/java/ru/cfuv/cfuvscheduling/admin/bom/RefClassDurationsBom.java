package ru.cfuv.cfuvscheduling.admin.bom;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class RefClassDurationsBom {
    private Integer number;
    private Date startTime;
    private Date endTime;
}
package ru.cfuv.cfuvscheduling.commons.bom;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefClassDurationsBom {

    private Integer number;
    private Date startTime;
    private Date endTime;
}

package ru.cfuv.cfuvscheduling.commons.bom;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class RefClassDurationsBom {
    private Integer number;
    private LocalTime startTime;
    private LocalTime endTime;
}

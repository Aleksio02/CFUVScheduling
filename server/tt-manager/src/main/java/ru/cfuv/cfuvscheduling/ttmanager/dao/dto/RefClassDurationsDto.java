package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "ref_class_duration")

public class RefClassDurationsDto {
    @Id
    @GeneratedValue
    private Integer number;
    private Date startTime;
    private Date endTime;

}

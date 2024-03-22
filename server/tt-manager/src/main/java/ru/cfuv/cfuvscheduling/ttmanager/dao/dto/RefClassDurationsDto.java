package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"start_time", "end_time"})}, schema = "public", name = "ref_class_duration")
public class RefClassDurationsDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer number;
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    @Column(name = "end_time", nullable = false)
    private Date endTime;
}

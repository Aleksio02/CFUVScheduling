package ru.cfuv.cfuvscheduling.commons.dao.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"start_time", "end_time"})}, schema = "public", name = "ref_class_duration")
public class RefClassDurationsDto {

    @Id
    private Integer number;
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}

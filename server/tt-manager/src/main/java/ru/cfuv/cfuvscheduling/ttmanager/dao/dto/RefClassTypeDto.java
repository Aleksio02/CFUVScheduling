package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;

import jakarta.persistence.*;


@Entity
@Table(schema = "public", name = "ref_class_type")
public class RefClassTypeDto {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

}
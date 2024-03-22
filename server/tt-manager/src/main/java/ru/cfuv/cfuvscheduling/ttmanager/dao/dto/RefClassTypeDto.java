package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;


@Entity
@Table(schema = "public", name = "ref_class_type")
public class RefClassTypeDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;

}
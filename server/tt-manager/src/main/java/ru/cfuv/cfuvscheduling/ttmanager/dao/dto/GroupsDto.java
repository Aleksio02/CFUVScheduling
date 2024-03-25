package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;

import jakarta.persistence.*;

@Entity
@Table(schema="public", name = "groups")
public class GroupsDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name", nullable = false, unique = true)
    private String name;
}

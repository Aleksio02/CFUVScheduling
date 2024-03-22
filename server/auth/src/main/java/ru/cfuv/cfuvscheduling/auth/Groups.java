package ru.cfuv.cfuvscheduling.auth;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(schema="public", name = "groups")
public class Groups {

    @Id
    @GeneratedValue
    private int id;
    private String name;
}

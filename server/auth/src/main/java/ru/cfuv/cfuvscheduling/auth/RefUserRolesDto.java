package ru.cfuv.cfuvscheduling.auth;

import jakarta.persistence.*;

@Entity
@Table(schema="public", name = "ref_user_roles")
public class RefUserRolesDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name", nullable = false, unique = true)
    private String name;

}

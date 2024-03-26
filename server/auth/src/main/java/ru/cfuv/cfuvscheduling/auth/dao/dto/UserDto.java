package ru.cfuv.cfuvscheduling.auth.dao.dto;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "users")
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RefUserRolesDto roleId;
}
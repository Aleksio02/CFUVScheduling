package ru.cfuv.cfuvscheduling.admin.dao.dto;

import jakarta.persistence.*;


@Entity
@Table(schema = "public", name = "users")
public class UserDto {
    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String password;

}
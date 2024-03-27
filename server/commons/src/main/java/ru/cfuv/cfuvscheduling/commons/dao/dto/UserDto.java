package ru.cfuv.cfuvscheduling.commons.dao.dto;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "users")
public class UserDto extends AbstractEntityDto {

    private String username;
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RefUserRolesDto roleId;
}
package ru.cfuv.cfuvscheduling.auth;

// Найти как сделать этот класс сущностью справочника "ref_user_roles" в БД
// Таблица должна сама создаваться и заполняться при изменении этого класса (изменение ???)

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(schema="public", name = "ref_user_roles")
public class RefUserRoles {

    @Id
    @GeneratedValue
    private int id;
    private String name;

}

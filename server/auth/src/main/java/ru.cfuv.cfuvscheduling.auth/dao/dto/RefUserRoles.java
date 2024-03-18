package ru.cfuv.cfuvscheduling.auth.dao.dto;

// Найти как сделать этот класс сущностью справочника "ref_user_roles" в БД
// Таблица должна сама создаваться и заполняться при изменении этого класса (изменение ???)

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "ref_user_roles")
public class RefUserRoles {

    @Id
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

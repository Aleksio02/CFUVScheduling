package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "ref_class_type")
public class RefClassTypeDto {

    @Id
    private Integer id;
    private String name;

}

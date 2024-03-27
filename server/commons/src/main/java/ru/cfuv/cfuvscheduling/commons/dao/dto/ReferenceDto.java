package ru.cfuv.cfuvscheduling.commons.dao.dto;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class ReferenceDto extends AbstractEntityDto{
    @Column(nullable = false, unique = true)
    private String name;
}

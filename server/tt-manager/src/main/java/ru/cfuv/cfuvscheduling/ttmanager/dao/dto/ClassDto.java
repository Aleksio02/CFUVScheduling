package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import ru.cfuv.cfuvscheduling.commons.dao.dto.AbstractEntityDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

@Getter
@Setter
@Entity
@Table(
        name = "classes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"classroom", "class_number", "date"}),
                @UniqueConstraint(columnNames = {"group_id", "class_number", "date"}),
                @UniqueConstraint(columnNames = {"user_id", "class_number", "date"})
        }
)
public class ClassDto extends AbstractEntityDto {
    @Column(name = "subject_name")
    private String subjectName;

    private String classroom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_number", referencedColumnName = "number")
    private RefClassDurationsDto classNumber;

    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private GroupsDto groupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private RefClassTypeDto typeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserDto userId;

    @Temporal(TemporalType.DATE)
    private LocalDate date;
}
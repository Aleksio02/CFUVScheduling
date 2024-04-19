package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.cfuv.cfuvscheduling.commons.dao.dto.*;

import java.sql.Date;

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

    @ManyToOne
    @JoinColumn(name = "class_number", referencedColumnName = "number")
    private RefClassDurationsDto classNumber;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private GroupsDto groupId;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private RefClassTypeDto typeId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserDto userId;

    @Temporal(TemporalType.DATE)
    private Date date;
}
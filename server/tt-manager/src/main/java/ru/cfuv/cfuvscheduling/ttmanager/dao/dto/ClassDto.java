package ru.cfuv.cfuvscheduling.ttmanager.dao.dto;

import jakarta.persistence.*;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

import java.sql.Date;

@Entity
@Table(
        name = "classes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"classroom", "class_number", "date"}),
                @UniqueConstraint(columnNames = {"group_id", "class_number", "date"}),
                @UniqueConstraint(columnNames = {"user_id", "class_number", "date"})
        }
)
public class ClassDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
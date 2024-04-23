package ru.cfuv.cfuvscheduling.ttmanager.bom;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;

@Getter
@Setter
public class ClassBom {

    private Integer id;
    private String subjectName;
    private String classroom;
    private RefClassDurationsBom duration;
    private String comment;
    private GroupsBom group;
    private RefClassTypeBom classType;
    private UserBom teacher;
    private LocalDate classDate;
}

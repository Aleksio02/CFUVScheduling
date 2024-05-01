package ru.cfuv.cfuvscheduling.ttmanager.converter;


import ru.cfuv.cfuvscheduling.commons.bom.GroupsBom;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassDurationsBom;
import ru.cfuv.cfuvscheduling.commons.bom.RefClassTypeBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.converter.GroupsConverter;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassDurationsConverter;
import ru.cfuv.cfuvscheduling.commons.converter.RefClassTypeConverter;
import ru.cfuv.cfuvscheduling.commons.converter.UserConverter;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassTypeDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;
import ru.cfuv.cfuvscheduling.ttmanager.bom.ClassBom;
import ru.cfuv.cfuvscheduling.ttmanager.dao.dto.ClassDto;

public class ClassConverter {

    public void fromDto(ClassDto source, ClassBom destination) {
        destination.setId(source.getId());
        destination.setSubjectName(source.getSubjectName());
        destination.setClassroom(source.getClassroom());
        destination.setComment(source.getComment());
        destination.setClassDate(source.getDate());

        RefClassDurationsBom refClassDurationsBom = new RefClassDurationsBom();
        new RefClassDurationsConverter().fromDto(source.getClassNumber(), refClassDurationsBom);
        destination.setDuration(refClassDurationsBom);

        GroupsBom groupsBom = new GroupsBom();
        new GroupsConverter().fromDto(source.getGroupId(), groupsBom);
        destination.setGroup(groupsBom);

        RefClassTypeBom refClassTypeBom = new RefClassTypeBom();
        new RefClassTypeConverter().fromDto(source.getTypeId(), refClassTypeBom);
        destination.setClassType(refClassTypeBom);

        UserBom teacher = new UserBom();
        new UserConverter().fromDto(source.getUserId(), teacher);
        destination.setTeacher(teacher);
    }

    public void toDto(ClassBom source, ClassDto destination) {
        destination.setId(source.getId());
        destination.setSubjectName(source.getSubjectName());
        destination.setClassroom(source.getClassroom());
        destination.setComment(source.getComment());
        destination.setDate(source.getClassDate());

        RefClassDurationsDto refClassDurationsDto = new RefClassDurationsDto();
        new RefClassDurationsConverter().toDto(source.getDuration(), refClassDurationsDto);
        destination.setClassNumber(refClassDurationsDto);

        GroupsDto groupsDto = new GroupsDto();
        new GroupsConverter().toDto(source.getGroup(), groupsDto);
        destination.setGroupId(groupsDto);

        if (source.getClassType() != null) {
            RefClassTypeDto refClassTypeDto = new RefClassTypeDto();
            new RefClassTypeConverter().toDto(source.getClassType(), refClassTypeDto);
            destination.setTypeId(refClassTypeDto);
        }

        if (source.getTeacher() != null) {
            UserDto teacher = new UserDto();
            new UserConverter().toDto(source.getTeacher(), teacher);
            destination.setUserId(teacher);
        }
    }
}

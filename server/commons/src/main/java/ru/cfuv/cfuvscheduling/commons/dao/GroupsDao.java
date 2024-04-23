package ru.cfuv.cfuvscheduling.commons.dao;

import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;

@Repository
public interface GroupsDao extends ReferenceDao<GroupsDto, Integer> {
    }

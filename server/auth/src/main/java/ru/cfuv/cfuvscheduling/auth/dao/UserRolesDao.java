package ru.cfuv.cfuvscheduling.auth.dao;

import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.commons.dao.ReferenceDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefUserRolesDto;

@Repository
public interface UserRolesDao extends ReferenceDao<RefUserRolesDto, Integer> {
}

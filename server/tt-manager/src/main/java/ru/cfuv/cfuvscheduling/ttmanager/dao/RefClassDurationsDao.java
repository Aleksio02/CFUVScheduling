package ru.cfuv.cfuvscheduling.ttmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;

@Repository
public interface RefClassDurationsDao extends JpaRepository<RefClassDurationsDto, Integer> {
}

package ru.cfuv.cfuvscheduling.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefClassDurationsDto;

import java.time.LocalTime;

@Repository
public interface RefClassDurationsDao extends JpaRepository<RefClassDurationsDto, Integer> {

    boolean existsByStartTimeAndEndTime(LocalTime time1, LocalTime time2);
}

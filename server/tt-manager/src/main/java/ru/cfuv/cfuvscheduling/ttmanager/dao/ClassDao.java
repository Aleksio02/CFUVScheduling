package ru.cfuv.cfuvscheduling.ttmanager.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.ttmanager.dao.dto.ClassDto;

@Repository
public interface ClassDao extends JpaRepository<ClassDto, Integer> {

    List<ClassDto> findAllByGroupIdAndDateBetween(GroupsDto groupId, LocalDate startDate, LocalDate endDate);
}

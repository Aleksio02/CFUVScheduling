package ru.cfuv.cfuvscheduling.ttmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.commons.dao.dto.GroupsDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;
import ru.cfuv.cfuvscheduling.ttmanager.dao.dto.ClassDto;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassDao extends JpaRepository<ClassDto, Integer> {

    List<ClassDto> findAllByGroupIdAndDateBetween(GroupsDto groupId, LocalDate startDate, LocalDate endDate);

    List<ClassDto> findAllByUserIdAndDateBetween(UserDto userId, LocalDate startDate, LocalDate endDate);
}

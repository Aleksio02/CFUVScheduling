package ru.cfuv.cfuvscheduling.ttmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.ttmanager.dao.dto.ClassDto;

@Repository
public interface ClassDao extends JpaRepository<ClassDto, Integer> {
}

package ru.cfuv.cfuvscheduling.commons.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserDto, Integer> {
    Optional<UserDto> findByUsername(String username);
}


package ru.cfuv.cfuvscheduling.commons.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefUserRolesDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserDto, Integer> {

    Optional<UserDto> findByUsername(String username);

    List<UserDto> findAllByRoleId(RefUserRolesDto roleId);

    Optional<UserDto> findByUsernameAndPassword(String username, String password);

}


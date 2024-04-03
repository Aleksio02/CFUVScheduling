package ru.cfuv.cfuvscheduling.auth.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.auth.bom.UserBom;
import ru.cfuv.cfuvscheduling.auth.converter.UserConverter;
import ru.cfuv.cfuvscheduling.auth.jwt.JwtUtils;
import ru.cfuv.cfuvscheduling.auth.dao.UserDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;

@Service
public class AuthService {

    @Autowired
    private UserDao userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public String authenticateUser(String username) {
        // Поиск пользователя в репозитории по имени
        UserDto user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Генерация JWT токена
        String token = jwtUtils.generateToken(user.getUsername());
        return token;
    }

    public UserBom getCurrentUser(String token) {
        try {
            String username = jwtUtils.parseJwt(token);
            UserDto user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            UserBom userBom = new UserBom();
            new UserConverter().fromDto(user, userBom);
            return userBom;
        } catch (Exception e) {
            throw new IncorrectRequestDataException("Error occured in parsing JWT");
        }
    }

}
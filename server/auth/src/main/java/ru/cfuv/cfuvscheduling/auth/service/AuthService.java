package ru.cfuv.cfuvscheduling.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cfuv.cfuvscheduling.auth.bom.AccountForm;
import ru.cfuv.cfuvscheduling.auth.bom.AccountResponse;
import ru.cfuv.cfuvscheduling.auth.dao.UserRolesDao;
import ru.cfuv.cfuvscheduling.auth.hash.PasswordEncoder;
import ru.cfuv.cfuvscheduling.auth.jwt.JwtUtils;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.converter.UserConverter;
import ru.cfuv.cfuvscheduling.commons.dao.UserDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefUserRolesDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;
import ru.cfuv.cfuvscheduling.commons.exception.UnauthorizedException;

import javax.persistence.EntityNotFoundException;

@Service
public class AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRolesDao userRolesDao;

    @Autowired
    private JwtUtils jwtUtils;

    public AccountResponse authenticateUser(AccountForm accountForm) {
        if (accountForm.getUsername() == null || accountForm.getPassword() == null) {
            throw new IncorrectRequestDataException("Username or password cannot be null");
        }

        UserDto foundUser = userDao.findByUsername(accountForm.getUsername()).orElse(null);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (foundUser == null || !passwordEncoder.matches(accountForm.getPassword(), foundUser.getPassword())) {
            throw new UnauthorizedException("Incorrect username or password");
        }

        UserConverter converter = new UserConverter();
        UserBom userBom = new UserBom();
        converter.fromDto(foundUser, userBom);

        String token = jwtUtils.generateToken(accountForm.getUsername());
        return new AccountResponse(token, userBom);
    }

    public UserBom getCurrentUser(String token) {
        String jwtCheckStage = "parsing";
        try {
            String username = jwtUtils.parseJwt(token);
            jwtCheckStage = "validating";
            UserDto user = userDao.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            UserBom userBom = new UserBom();
            new UserConverter().fromDto(user, userBom);
            return userBom;
        } catch (Exception e) {
            throw new IncorrectRequestDataException("Error occured in %s JWT".formatted(jwtCheckStage));
        }
    }

    public AccountResponse registration(AccountForm userForm) throws AlreadyExistsException {
        if (userForm.getUsername() == null || userForm.getPassword() == null) {
            throw new IncorrectRequestDataException("Object fields can't be null");
        }

        userDao.findByUsername(userForm.getUsername())
                .ifPresent(i -> {
                    throw new AlreadyExistsException("This username already taken");
                });

        RefUserRolesDto userRole = userRolesDao.findByName(UserRoles.USER.name()).get();

        String hashedPassword = PasswordEncoder.encode(userForm.getPassword());

        UserDto userDto = new UserDto();
        userDto.setUsername(userForm.getUsername());
        userDto.setPassword(hashedPassword);
        userDto.setRoleId(userRole);
        userDao.save(userDto);

        String token = new JwtUtils().generateToken(userForm.getUsername());

        UserBom userBom = new UserBom();
        new UserConverter().fromDto(userDto, userBom);

        return new AccountResponse(token, userBom);
    }

}
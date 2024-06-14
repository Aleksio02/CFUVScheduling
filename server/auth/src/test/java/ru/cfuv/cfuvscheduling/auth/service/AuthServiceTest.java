package ru.cfuv.cfuvscheduling.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.cfuv.cfuvscheduling.auth.bom.AccountForm;
import ru.cfuv.cfuvscheduling.auth.bom.AccountResponse;
import ru.cfuv.cfuvscheduling.auth.dao.UserRolesDao;
import ru.cfuv.cfuvscheduling.auth.jwt.JwtUtils;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.bom.UserRoles;
import ru.cfuv.cfuvscheduling.commons.dao.UserDao;
import ru.cfuv.cfuvscheduling.commons.dao.dto.RefUserRolesDto;
import ru.cfuv.cfuvscheduling.commons.dao.dto.UserDto;
import ru.cfuv.cfuvscheduling.commons.exception.AlreadyExistsException;
import ru.cfuv.cfuvscheduling.commons.exception.IncorrectRequestDataException;
import ru.cfuv.cfuvscheduling.commons.exception.UnauthorizedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserRolesDao userRolesDao;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateUser_Success() {
        String username = "testuser";
        String password = "password";
        AccountForm accountForm = new AccountForm();
        accountForm.setUsername(username);
        accountForm.setPassword(password);

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(new BCryptPasswordEncoder().encode(password));

        when(userDao.findByUsername(username)).thenReturn(Optional.of(userDto));
        when(jwtUtils.generateToken(username)).thenReturn("token");

        AccountResponse response = authService.authenticateUser(accountForm);

        assertEquals("token", response.getToken());
        assertEquals(username, response.getUser().getUsername());
    }

    @Test
    public void testAuthenticateUser_Failure() {
        String username = "testuser";
        String password = "wrongpassword";
        AccountForm accountForm = new AccountForm();
        accountForm.setUsername(username);
        accountForm.setPassword(password);

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(new BCryptPasswordEncoder().encode("correctpassword"));

        when(userDao.findByUsername(username)).thenReturn(Optional.of(userDto));

        assertThrows(UnauthorizedException.class, () -> authService.authenticateUser(accountForm));
    }

    @Test
    public void testGetCurrentUser_Success() {
        String token = "token";
        String username = "testuser";
        UserDto userDto = new UserDto();
        userDto.setUsername(username);

        when(jwtUtils.parseJwt(token)).thenReturn(username);
        when(userDao.findByUsername(username)).thenReturn(Optional.of(userDto));

        UserBom userBom = authService.getCurrentUser(token);

        assertEquals(username, userBom.getUsername());
    }

    @Test
    public void testGetCurrentUser_Failure() {
        String token = "invalidtoken";

        when(jwtUtils.parseJwt(token)).thenThrow(new IncorrectRequestDataException("Error occured in parsing JWT"));

        assertThrows(IncorrectRequestDataException.class, () -> authService.getCurrentUser(token));
    }

    @Test
    public void testGetCurrentUser_ValidToken() {
        String token = "validToken";
        String username = "testuser";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);

        when(jwtUtils.parseJwt(token)).thenReturn(username);
        when(userDao.findByUsername(username)).thenReturn(Optional.of(userDto));

        UserBom userBom = authService.getCurrentUser(token);

        assertEquals(username, userBom.getUsername());
    }

    @Test
    public void testGetCurrentUser_InvalidToken() {
        String invalidToken = "invalidToken";

        when(jwtUtils.parseJwt(invalidToken)).thenThrow(new IncorrectRequestDataException("Invalid JWT"));

        assertThrows(IncorrectRequestDataException.class, () -> authService.getCurrentUser(invalidToken));
    }

    @Test
    public void testRegistration_Success() {
        String username = "newuser_" + System.currentTimeMillis();
        String password = "password";

        RefUserRolesDto userRole = new RefUserRolesDto();
        userRole.setId(1);
        userRole.setName("USER");
        when(userRolesDao.findByName(UserRoles.USER.name())).thenReturn(Optional.of(userRole));

        when(userDao.findByUsername(username)).thenReturn(Optional.empty());

        when(userDao.save(any(UserDto.class))).thenAnswer(invocation -> {
            UserDto savedUser = invocation.getArgument(0);
            savedUser.setId(1);
            return savedUser;
        });

        String expectedToken = new JwtUtils().generateToken(username);
        when(jwtUtils.generateToken(username)).thenReturn(expectedToken);

        AccountForm registrationForm = new AccountForm();
        registrationForm.setUsername(username);
        registrationForm.setPassword(password);
        AccountResponse response = authService.registration(registrationForm);

        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
        assertEquals(username, response.getUser().getUsername());
    }

    @Test
    public void testRegistration_Failure_UserExists() {
        String username = "existinguser";
        String password = "password";
        AccountForm accountForm = new AccountForm();
        accountForm.setUsername(username);
        accountForm.setPassword(password);

        UserDto userDto = new UserDto();
        userDto.setUsername(username);

        when(userDao.findByUsername(username)).thenReturn(Optional.of(userDto));

        assertThrows(AlreadyExistsException.class, () -> authService.registration(accountForm));
    }

    @Test
    public void testRegistration_AlreadyExists() {
        String existingUsername = "existinguser";
        String password = "password";
        AccountForm accountForm = new AccountForm();
        accountForm.setUsername(existingUsername);
        accountForm.setPassword(password);

        UserDto existingUserDto = new UserDto();
        existingUserDto.setUsername(existingUsername);

        when(userDao.findByUsername(existingUsername)).thenReturn(Optional.of(existingUserDto));

        assertThrows(AlreadyExistsException.class, () -> authService.registration(accountForm));
    }

    @Test
    public void testRegistration_NullFields() {
        AccountForm accountForm = new AccountForm();

        assertThrows(IncorrectRequestDataException.class, () -> authService.registration(accountForm));
    }

}

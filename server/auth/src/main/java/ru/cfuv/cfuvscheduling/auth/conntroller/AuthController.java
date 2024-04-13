package ru.cfuv.cfuvscheduling.auth.conntroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.auth.bom.AccountForm;
import ru.cfuv.cfuvscheduling.auth.bom.AccountResponse;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/getCurrentUser")
    public UserBom getCurrentUser(@RequestHeader("Authorization") String token) {
        return authService.getCurrentUser(token);
    }

    @PostMapping("/registerUser")
    public AccountResponse registration(@RequestBody AccountForm userForm) {
        return authService.registration(userForm);
    }
}

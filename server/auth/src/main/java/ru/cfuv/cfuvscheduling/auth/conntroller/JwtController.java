package ru.cfuv.cfuvscheduling.auth.conntroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.auth.bom.AccountForm;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/jwt")
public class JwtController {

    @Autowired
    private AuthService authService;

    @PostMapping("/generate")
    public String generateJwt(@RequestBody String username) {
        AccountForm accountForm = new AccountForm();
        accountForm.setUsername(username);
        String token = authService.authenticateUser(accountForm);
        return token;
    }

    @GetMapping("/parse")
    public UserBom parseJwt(@RequestHeader(name = "Authorization", required = false) String token) {
        return authService.getCurrentUser(token);
    }
}
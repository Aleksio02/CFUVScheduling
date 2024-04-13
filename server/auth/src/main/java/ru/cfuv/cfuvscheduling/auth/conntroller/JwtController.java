package ru.cfuv.cfuvscheduling.auth.conntroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    @Autowired
    private AuthService authService;

    @PostMapping("/generate")
    public String generateJwt(@RequestBody String username) {
        return authService.authenticateUser(username);
    }

    @GetMapping("/parse")
    public UserBom parseJwt(@RequestHeader("Authorization") String token) {
        return authService.getCurrentUser(token);
    }
}
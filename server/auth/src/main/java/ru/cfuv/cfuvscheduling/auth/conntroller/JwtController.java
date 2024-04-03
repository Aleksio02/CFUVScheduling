package ru.cfuv.cfuvscheduling.auth.conntroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cfuv.cfuvscheduling.auth.bom.UserBom;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;

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
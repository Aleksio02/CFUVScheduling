package ru.cfuv.cfuvscheduling.admin.connector;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;

@FeignClient(name = "auth-connector", url = "${connector.auth.url}")
public interface AuthConnector {

    @GetMapping("/getCurrentUser")
    UserBom getCurrentUser(@RequestHeader(name = "Authorization") String jwtToken);

}

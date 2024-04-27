package ru.cfuv.cfuvscheduling.admin.controller;

import feign.FeignException;
import feign.FeignException.InternalServerError;
import feign.RetryableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cfuv.cfuvscheduling.admin.connector.AuthConnector;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;
import ru.cfuv.cfuvscheduling.commons.exception.ServerUnavailableException;
import ru.cfuv.cfuvscheduling.commons.exception.UnauthorizedException;

@Component
public class UserValidator {

    @Autowired
    private AuthConnector authConnector;

    public void validateUserAsAdmin(String token) {
        String exceptionMessage = "You don't have access to this action";
        try {
            if (!authConnector.getCurrentUser(token).hasAdminRole()) {
                throw new AccessForbiddenException(exceptionMessage);
            }
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                throw new ServerUnavailableException("Server can't receive response from authorization service");
            }
            throw new UnauthorizedException(exceptionMessage);
        }
    }
}

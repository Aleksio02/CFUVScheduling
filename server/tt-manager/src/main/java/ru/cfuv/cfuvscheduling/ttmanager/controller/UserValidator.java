package ru.cfuv.cfuvscheduling.ttmanager.controller;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;
import ru.cfuv.cfuvscheduling.commons.exception.AccessForbiddenException;
import ru.cfuv.cfuvscheduling.commons.exception.UnauthorizedException;
import ru.cfuv.cfuvscheduling.ttmanager.connector.AuthConnector;

@Component
public class UserValidator {

    @Autowired
    private AuthConnector authConnector;

    public UserBom validateUserAsTeacher(String token) {
        String exceptionMessage = "You don't have access to this action";
        try {
            UserBom currentUser = authConnector.getCurrentUser(token);
            if (!currentUser.isTeacher()) {
                throw new AccessForbiddenException(exceptionMessage);
            }
            return currentUser;
        } catch (FeignException e) {
            throw new UnauthorizedException(exceptionMessage);
        }
    }
}
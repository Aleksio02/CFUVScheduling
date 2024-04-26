package ru.cfuv.cfuvscheduling.auth.bom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.cfuv.cfuvscheduling.commons.bom.AbstractResponseEntity;
import ru.cfuv.cfuvscheduling.commons.bom.UserBom;

@Getter
@Setter
@AllArgsConstructor
public class AccountResponse extends AbstractResponseEntity {
    private String token;
    private UserBom user;
}

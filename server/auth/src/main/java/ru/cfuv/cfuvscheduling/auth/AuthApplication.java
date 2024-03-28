package ru.cfuv.cfuvscheduling.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan({"ru.cfuv.cfuvscheduling.commons.dao.dto", "ru.cfuv.cfuvscheduling.auth.dao.dto"})
@EnableJpaRepositories("ru.cfuv.cfuvscheduling")
@ComponentScan("ru.cfuv.cfuvscheduling")
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}

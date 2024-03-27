package ru.cfuv.cfuvscheduling.ttmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("ru.cfuv.cfuvscheduling.commons.dao.dto")
@SpringBootApplication
public class TtManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtManagerApplication.class, args);
    }

}

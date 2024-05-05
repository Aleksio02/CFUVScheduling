package ru.cfuv.cfuvscheduling.admin.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.cfuv.cfuvscheduling.admin.connector.AuthConnector;
import ru.cfuv.cfuvscheduling.admin.controller.GroupsController;
import ru.cfuv.cfuvscheduling.admin.service.GroupsService;

@CucumberOptions(features = "src/test/resources/bdd/features", glue = "ru.cfuv.cfuvscheduling.admin.bdd",
    tags = "not @Bug")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(Cucumber.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class CucumberIntegrationTest {

    @InjectMocks
    private GroupsController groupsController;

    @SpyBean
    private GroupsService groupsService;

    @MockBean
    private AuthConnector authConnector;

}

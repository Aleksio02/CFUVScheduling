package ru.cfuv.cfuvscheduling.auth.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.cfuv.cfuvscheduling.auth.conntroller.AuthController;
import ru.cfuv.cfuvscheduling.auth.service.AuthService;

@CucumberOptions(features = "src/test/resources/bdd/features", glue = "ru.cfuv.cfuvscheduling.auth.bdd",
    tags = "not @Bug")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(Cucumber.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class CucumberIntegrationTest {

    @InjectMocks
    private AuthController authController;

    @SpyBean
    private AuthService authService;

}

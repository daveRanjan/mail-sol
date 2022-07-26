package me.dev.solution;

import lombok.extern.slf4j.Slf4j;
import me.dev.solution.model.MailType;
import me.dev.solution.model.UserData;
import me.dev.solution.service.MailService;
import me.dev.solution.service.impl.SimpleJavaMailServiceImpl;
import me.dev.solution.util.CsvDataLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.util.Assert;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.SendFailedException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class SimpleMailServiceTest {

    MailService mailService;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    String emailFrom;
    @Value("${spring.mail.password}")
    String password;
    @Value("${spring.mail.host}")
    String host;

    @BeforeEach
    public void before() {
        mailService = new SimpleJavaMailServiceImpl(templateEngine, emailFrom, password, host);
    }

    @Test
    public void Should_SendEmail_When_InputIsCorrect() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Mitul!");
        attributes.put("eventName", "ComicCon");
        attributes.put("eventStartTime", "2022-07-19 09:00 AM");
        attributes.put("eventLink", "https://www.comicconindia.com/");
        attributes.put("sign", "DevT");
        boolean result = mailService.send("dtiwari@yopmail.com", "Welcome To ComicCon!!", attributes, MailType.SCHEDULED_INVITATION_EMAIL);

        Assert.isTrue(result, "Should_SendEmail_When_InputIsCorrect worked fine.");
    }


    @Test
    public void Should_FailToSendEmail_When_ToEmailIsInValid() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Mitul!");
        attributes.put("eventName", "ComicCon");
        attributes.put("eventStartTime", "2022-07-19 09:00 AM");
        attributes.put("eventLink", "https://www.comicconindia.com/");
        attributes.put("sign", "DevT");

        SendFailedException ex = Assertions.assertThrows(SendFailedException.class,
                () -> mailService.send("dtiwari@yopmail", "Welcome To ComicCon!!", attributes, MailType.SCHEDULED_INVITATION_EMAIL),
                "Expected send() to throw exception, but it didn't");

        Assert.hasText(ex.getMessage(), "Invalid Addresses");
    }

    @Test
    public void Should_TestFor_MultipleInputs() throws URISyntaxException, IOException {
        List<UserData> userDataList = CsvDataLoader.loadUserData();
        Map<String, Boolean> status = new HashMap<>();
        for (UserData userData : userDataList) {
            try {
                mailService.send(userData.getEmail(), userData.getSubject(), userData.getAttributes(), MailType.SCHEDULED_INVITATION_EMAIL);
                status.put(userData.getEmail(), true);
            }catch (Exception ex){
                status.put(userData.getEmail(), false);
            }

            Assert.isTrue(status.get("dtiwari@yopmail.com"), "Email not send to dtiwari@yopmail.com");
            Assert.isTrue(!status.get("mm@yopmail"), "Email sent to mm@yopmail, which should had not sent");
        }
    }


}

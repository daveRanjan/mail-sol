package me.dev.solution;

import me.dev.solution.model.MailType;
import me.dev.solution.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MailSolutionApplication implements CommandLineRunner {


    @Qualifier("simpleJavaMailService")
    @Autowired
    MailService simpleJavaMailService;

    public static void main(String[] args) {
        SpringApplication.run(MailSolutionApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Mitul!");
        attributes.put("eventName", "ComicCon");
        attributes.put("eventStartTime", "2022-07-19 09:00 AM");
        attributes.put("eventLink", "https://www.comicconindia.com/");
        attributes.put("sign", "DevT");
        simpleJavaMailService.send("dtiwari@yopmail.com", "Welcome To ComicCon!!", attributes, MailType.SCHEDULED_INVITATION_EMAIL);
    }
}

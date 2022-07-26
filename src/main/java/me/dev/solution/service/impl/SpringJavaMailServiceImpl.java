package me.dev.solution.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.dev.solution.model.MailType;
import me.dev.solution.service.MailService;
import me.dev.solution.util.TemplateUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service("springJavaMailService")
public class SpringJavaMailServiceImpl implements MailService {

    private JavaMailSender emailSender;
    private SpringTemplateEngine templateEngine;
    String emailFrom;

    public SpringJavaMailServiceImpl(JavaMailSender emailSender, SpringTemplateEngine templateEngine, @Value("${spring.mail.username}")
            String emailFrom) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.emailFrom = emailFrom;
    }

    @SneakyThrows
    @Override
    public boolean send(String to, String subject, Map<String, Object> attributes, MailType mailType) {
        log.info("send -- Sending email to : {}", to);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        String html = TemplateUtil.getMailTemplate(mailType, attributes, templateEngine);
        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject(subject);
        helper.setFrom(emailFrom);
        try {
            emailSender.send(message);
            log.info("send -- Email sent successfully to :" + to);
            return true;
        } catch (Exception ex) {
            log.error("send -- Error while send email to {}. Message : {}", to, ex);
            throw ex;
        }
    }


}

package me.dev.solution.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.dev.solution.model.MailType;
import me.dev.solution.service.MailService;
import me.dev.solution.util.TemplateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Service("simpleJavaMailService")
@Slf4j
public class SimpleJavaMailServiceImpl implements MailService {

    private SpringTemplateEngine templateEngine;
    private String emailFrom;
    private String password;
    private String host;

    public SimpleJavaMailServiceImpl(SpringTemplateEngine templateEngine, @Value("${spring.mail.username}") String emailFrom,
                                     @Value("${spring.mail.password}") String password, @Value("${spring.mail.host}") String host) {
        this.templateEngine = templateEngine;
        this.emailFrom = emailFrom;
        this.password = password;
        this.host = host;
    }

    @SneakyThrows
    @Override
    public boolean send(String to, String subject, Map<String, Object> attributes, MailType mailType) {
        try {
            MimeMessage msg = new MimeMessage(getSession(emailFrom, password, host));
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(emailFrom, subject));
            msg.setReplyTo(InternetAddress.parse(emailFrom, false));
            msg.setSubject(subject, "UTF-8");
            msg.setContent(TemplateUtil.getMailTemplate(mailType, attributes, templateEngine), "text/html; charset=utf-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            Transport.send(msg);

            log.info("Email Sent Successfully!!");
            return true;
        } catch (Exception e) {
            log.error("Error while sending email to {}, Exception: {}", to, e);
            throw e;
        }
    }

    private Session getSession(String fromEmail, String password, String host) {

        log.info("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", host); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        return session;
    }
}

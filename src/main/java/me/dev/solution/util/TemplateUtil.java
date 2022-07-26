package me.dev.solution.util;

import me.dev.solution.model.MailType;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;

public class TemplateUtil {
    public static String getMailTemplate(MailType mailType, Map<String, Object> attributes, SpringTemplateEngine templateEngine) {
        Context context = new Context();
        context.setVariables(attributes);

        switch (mailType) {
            case SCHEDULED_INVITATION_EMAIL:
                return templateEngine.process("invitation_email_template.html", context);
        }
        return "";
    }
}

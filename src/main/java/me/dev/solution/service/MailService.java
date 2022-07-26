package me.dev.solution.service;

import lombok.SneakyThrows;
import me.dev.solution.model.MailType;

import java.util.Map;

public interface MailService {
    @SneakyThrows
    boolean send(String to, String subject, Map<String, Object> attributes, MailType mailType);
}

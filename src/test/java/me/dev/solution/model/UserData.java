package me.dev.solution.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class UserData {
    private String email;
    private String subject;
    private Map<String, Object> attributes = new HashMap<>();
}

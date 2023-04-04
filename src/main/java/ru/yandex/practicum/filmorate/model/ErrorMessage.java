package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorMessage {
    private final Map<String, String> messages = new HashMap<>();

    public ErrorMessage() {
    }

    public ErrorMessage(String k, String v) {
        messages.put(k, v);
    }

    public void setMessage(String k, String v) {
        messages.put(k, v);
    }
}

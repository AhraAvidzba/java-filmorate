package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

@Getter
class ErrorMessage {
    private final String message;
    public ErrorMessage(String message) {
        this.message = message;
    }
}

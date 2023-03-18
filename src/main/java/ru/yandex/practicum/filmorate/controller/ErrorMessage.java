package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

@Getter
class ErrorMessage {
    String message;
    ErrorMessage(String message) {
        this.message = message;
    }
}

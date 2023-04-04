package ru.yandex.practicum.filmorate.exceptions;

public class ContentAlreadyExistException extends RuntimeException {
    public ContentAlreadyExistException(String message) {
        super(message);
    }
}


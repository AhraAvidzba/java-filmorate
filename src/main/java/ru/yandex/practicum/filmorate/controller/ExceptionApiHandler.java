package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;

//@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(ContentAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> handleException(ContentAlreadyExistException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ContentNotFountException.class)
    public ResponseEntity<ErrorMessage> handleException(ContentNotFountException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

}

@Getter
class ErrorMessage {
    String message;
    ErrorMessage(String message) {
        this.message = message;
    }
}
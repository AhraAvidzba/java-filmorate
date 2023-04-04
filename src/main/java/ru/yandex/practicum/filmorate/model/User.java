package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private final Set<Long> friendsList = new LinkedHashSet<>();
    private Long id;
    @EqualsAndHashCode.Exclude
    @Past(message = "Дата должна быть в прошлом")
    private LocalDate birthday;
    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Не должно быть пустым")
    private String login;
    @EqualsAndHashCode.Exclude
    @Email(message = "Должно содержать валидный email")
    private String email;
    @EqualsAndHashCode.Exclude
    private String name;
}

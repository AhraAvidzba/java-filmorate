package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
@Validated
public class User {
    private Integer id;
    @EqualsAndHashCode.Exclude
    @Past
    private LocalDate birthday;
    @EqualsAndHashCode.Exclude
    @NotBlank
    private String login;
    @EqualsAndHashCode.Exclude
    @Email
    private String email;
    @EqualsAndHashCode.Exclude
    private String name;
}

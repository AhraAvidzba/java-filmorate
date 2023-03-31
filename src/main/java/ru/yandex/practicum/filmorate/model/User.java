package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;
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

    private final Set<User> friendsList = Collections.emptySet();
}

package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.enums.Status;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class User {
    private Map<Long, Status> friendsList = new HashMap<>();
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

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("birthday", birthday);
        values.put("login", login);
        values.put("email", email);
        values.put("name", name);
        return values;
    }
}

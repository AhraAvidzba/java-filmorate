package ru.yandex.practicum.filmorate.model.enums;

public enum Genres {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик"),
    CARTOON("Мультфильм"),

    FICTION("Фантастика"),
    ADVENTURE("Приключения");

    private final String name;

    Genres(String name) {
        this.name = name;
    }

    public String getTranslate() {
        return name;
    }
}

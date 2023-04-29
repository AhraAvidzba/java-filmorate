package ru.yandex.practicum.filmorate.model.enums;

public enum Genres {
    COMEDY("КОМЕДИЯ"),
    HORROR("УЖАСЫ"),
    DOCUMENTARY("ДОКУМЕНТАЛЬНЫЙ"),
    FICTION("ФАНТАСТИКА"),
    MUSICAL("МЮЗИКЛ"),
    WESTERN("ВЕСТЕРН"),
    THRILLER("ТРИЛЛЕР"),
    ACTION("БОЕВИК"),
    HISTORICAL("ИСТОРИЯ"),
    ADVENTURE("ПРИКЛЮЧЕНИЯ"),
    DETECTIVE("ДЕТЕКТИВ"),
    DRAMA("ДРАМА"),
    MELODRAMA("МЕЛОДРАМА");

    private final String name;

    Genres(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

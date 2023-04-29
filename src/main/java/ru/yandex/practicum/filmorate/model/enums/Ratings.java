package ru.yandex.practicum.filmorate.model.enums;

import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;

public enum Ratings {
    G("G"), PG("PG"), PG13("PG-13"), R("R"), NC17("NC-17");
    private final String name;

    Ratings(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Ratings getRatingById(int id) {
        switch (id) {
            case 1:
                return G;
            case 2:
                return PG;
            case 3:
                return PG13;
            case 4:
                return R;
            case 5:
                return NC17;
            default:
                throw new ContentNotFountException("Невозможно найти значение рейтинга с указанным " +
                        "идентификатором");
        }
    }

    public static Integer getIdByRating(Ratings rating) {
        switch (rating) {
            case G:
                return 1;
            case PG:
                return 2;
            case PG13:
                return 3;
            case R:
                return 4;
            case NC17:
                return 5;
            default:
                throw new ContentNotFountException("Невозможно найти значение идентификатора по указанному " +
                        "рейтингу");
        }
    }
}

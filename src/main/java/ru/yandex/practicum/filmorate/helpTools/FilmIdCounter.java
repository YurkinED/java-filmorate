package ru.yandex.practicum.filmorate.helpTools;

import lombok.Data;

@Data
public class FilmIdCounter {
    private int id;

    public int incrementIdCounter() {
        return ++id;
    }
    public int decrementIdCounter() {
        return --id;
    }
}

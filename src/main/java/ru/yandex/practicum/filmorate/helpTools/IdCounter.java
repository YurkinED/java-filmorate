package ru.yandex.practicum.filmorate.helpTools;

import lombok.Data;

@Data
public class IdCounter {
    private int id;

    public int incrementIdCounter() {
        return ++id;
    }
}


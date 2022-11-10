package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Mpa extends BaseEntity {
    private String name;

    public Mpa(long id, String name) {
        super(id);
        this.name = name;
    }
}

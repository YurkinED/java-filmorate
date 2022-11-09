package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Genre extends BaseEntity {
    private String name;

    public Genre(long id, String name) {
        super(id);
        this.name = name;
    }
}

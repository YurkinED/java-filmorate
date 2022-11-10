package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Director extends BaseEntity {

    @NotBlank
    private String name;

    public Director(long id, String name) {
        super(id);
        this.name = name;
    }
}
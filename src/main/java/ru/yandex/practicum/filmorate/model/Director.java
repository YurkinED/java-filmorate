package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Director extends BaseEntity {

    @NotBlank
    private String name;

    public Director(int id, String name) {
        super(id);
        this.name = name;
    }
}
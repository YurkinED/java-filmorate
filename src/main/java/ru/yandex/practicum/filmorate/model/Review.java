package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ValidationGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class Review {

    @Null(groups = ValidationGroup.OnCreate.class)
    @NotNull(groups = ValidationGroup.OnUpdate.class)
    private Integer reviewId;

    @NotBlank
    private String content;

    @NotNull
    @JsonProperty("isPositive")
    private Boolean isPositive;

    private Integer useful;

    @NotNull(groups = ValidationGroup.OnCreate.class)
    private Integer userId;

    @NotNull(groups = ValidationGroup.OnCreate.class)
    private Integer filmId;
}

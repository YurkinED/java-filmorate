package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review {
    private Integer reviewId;
    private String content;
    @JsonProperty("isPositive")
    private Boolean isPositive;
    private Integer useful;
    private Integer userId;
    private Integer filmId;
}

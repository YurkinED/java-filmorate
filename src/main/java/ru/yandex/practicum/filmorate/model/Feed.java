package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {
    private int eventId;
    private int userId;
    private int entityId;
    private String eventType;
    private String operation;
    private Long timestamp;
}

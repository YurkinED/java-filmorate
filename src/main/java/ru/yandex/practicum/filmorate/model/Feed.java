package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
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

    /*public Feed(int eventId, int userId, int entityId, String eventType, String operation) {
        this.eventId = eventId;
        this.userId = userId;
        this.entityId = entityId;
        this.eventType = eventType;
        this.operation = operation;
        timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
    }*/

}

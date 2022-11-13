package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public Collection<Director> findAllDirectors() {
        log.debug("Получен запрос Get /directors. Получить все режиссеров.");
        return directorService.findAllDirectors();
    }

    @GetMapping("/{directorId}")
    public Director getDirectorById(@PathVariable int directorId) {
        log.debug("Получен запрос Get /director/{}. Найти режиссера по directorId {}.", directorId, directorId);
        return directorService.findDirectorById(directorId).orElseThrow(
                () -> new InvalidIdException("К сожалению, режиссера с id " + directorId + " нет."));
    }

    @PostMapping
    public Director createDirector(@RequestBody @Valid Director director) {
        log.debug("Получен запрос Post /directors. Создать режиссера {}", director);
        return directorService.createDirector(director);
    }

    @PutMapping
    public Director updateDirector(@RequestBody @Valid Director director) {
        log.debug("Получен запрос Put /directors. Изменить режиссера {}", director);
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{directorId}")
    public void removeDirector(@PathVariable int directorId) {
        log.debug("Получен запрос Delete /director/{}. Удалить режиссера по directorId {}.", directorId, directorId);
        directorService.removeDirector(directorId);
    }
}

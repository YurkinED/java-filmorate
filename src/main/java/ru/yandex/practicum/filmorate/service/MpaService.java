package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaDbStorage) {
        this.mpaStorage = mpaDbStorage;
    }

    public Collection<Mpa> findAllMpa() {
        return mpaStorage.findAllMpa();
    }

    public Optional<Mpa> findMpaById(int mpaId) {
        return mpaStorage.findMpaById(mpaId);
    }
}

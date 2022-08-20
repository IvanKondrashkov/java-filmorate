package ru.yandex.praktikum.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.dao.mpa.MpaStorage;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.model.Mpa;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa findById(Long id) {
        final Mpa mpa = mpaStorage.findById(id);

        if (Objects.isNull(mpa)) {
            throw new NotFoundException(String.format("Mpa with id=%d not found!", id));
        }
        return mpa;
    }

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public Mpa save(Mpa mpa) {
        return mpaStorage.save(mpa);
    }

    public Mpa update(Mpa mpa) {
        mpa = mpaStorage.update(mpa);

        if (Objects.isNull(mpa)) {
            throw new NotFoundException("Mpa with not found!");
        }
        return mpa;
    }

    public void deleteById(Long id) {
        final Mpa mpa = mpaStorage.findById(id);

        if (Objects.isNull(mpa)) {
            throw new NotFoundException(String.format("Mpa with id=%d not found!", id));
        }
        mpaStorage.deleteById(id);
    }
}

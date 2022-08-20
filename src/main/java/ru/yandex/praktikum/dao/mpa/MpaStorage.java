package ru.yandex.praktikum.dao.mpa;

import java.util.List;
import ru.yandex.praktikum.model.Mpa;

public interface MpaStorage {
    Mpa findById(Long id);
    List<Mpa> findAll();
    Mpa save(Mpa mpa);
    Mpa update(Mpa mpa);
    void deleteById(Long id);
}

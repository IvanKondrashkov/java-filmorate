package ru.yandex.praktikum.model;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Mpa {
    private final Long id;
    private final String name;
}

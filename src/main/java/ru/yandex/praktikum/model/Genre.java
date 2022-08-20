package ru.yandex.praktikum.model;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre {
    private final Long id;
    private final String name;
}

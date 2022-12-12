package ru.yandex.practicum.filmorate.model;

import lombok.*;


import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder//для тестирования
public class Film {
	private Integer id;
	@NonNull
	private final String name;
	private String description;
	@NonNull
	private final LocalDate releaseDate;
	@NonNull
	private final Duration duration;
}

package ru.yandex.practicum.filmorate.model;

import lombok.*;


import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
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
	@Positive
	private final int duration;
}

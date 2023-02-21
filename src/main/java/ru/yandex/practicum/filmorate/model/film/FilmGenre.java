package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Positive;

@Data
@Builder
public class FilmGenre {
	@Positive
	int id;
	@EqualsAndHashCode.Exclude
	String name;
}
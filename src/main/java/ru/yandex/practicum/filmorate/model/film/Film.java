package ru.yandex.practicum.filmorate.model.film;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder//для тестирования
public class Film {
	@EqualsAndHashCode.Exclude
	private Integer id;
	@NotBlank
	@NonNull
	private final String name;
	private String description;
	@NonNull
	private final LocalDate releaseDate;
	@Positive
	@NonNull
	private final int duration;
	@EqualsAndHashCode.Exclude
	private Set<Integer> likes;
	@EqualsAndHashCode.Exclude
	private Set<FilmGenres> genres;
	@EqualsAndHashCode.Exclude
	private MPARatings MPARating;
}

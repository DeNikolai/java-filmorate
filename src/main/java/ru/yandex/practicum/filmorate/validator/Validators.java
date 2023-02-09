package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.List;

public class Validators {
	/*
	Утилитарный класс с валидаторами.
	 */
	private static final int FILM_DESCRIPTION_MAX_SIZE = 200;
	private static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 27);
	public static final Validator<Film> filmValidator =  new Validator<>(List.of(
			film -> film.getDescription().length() <= FILM_DESCRIPTION_MAX_SIZE,
			film -> film.getReleaseDate().isAfter(FIRST_FILM_RELEASE_DATE)
	));

	public static final Validator<User> userValidator = new Validator<>(List.of(
			user -> user.getBirthday().isBefore(LocalDate.now())
	));
}

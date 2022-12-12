package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class Validators {
	/*
	Утилитарный класс с валидаторами.
	 */
	private static final int DESCRIPTION_MAX_SIZE = 200;
	private static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 27);
	public static final Validator<Film> filmValidator =  new Validator<>(List.of(
			film -> !film.getName().isEmpty(),
			film -> film.getDescription().length() <= DESCRIPTION_MAX_SIZE,
			film -> film.getReleaseDate().isAfter(FIRST_FILM_RELEASE_DATE),
			film -> film.getDuration().toSeconds() > 0
	));

	public static final Validator<User> userValidator = new Validator<>(List.of(
			user -> user.getEmail().contains("@"),
			user -> !user.getEmail().split("@")[0].isEmpty(),
			user -> !user.getEmail().split("@")[1].split("\\.")[0].isEmpty(),
			user -> user.getEmail().split("@").length <=2,
			user -> user.getEmail().contains("."),
			user -> user.getEmail().split("\\.").length > 1,
			user -> !user.getLogin().isEmpty() && !user.getLogin().contains(" "),
			user -> user.getBirthday().isBefore(LocalDate.now())
	));
}

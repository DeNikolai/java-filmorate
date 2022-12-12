package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

class ValidatorsTest {
	/*
	Тестирование валидации фильмов.
	 */
	@Test
	void validFilmValidationIsTrue() {
		Film film = Film.builder()
				.name("filmName")
				.description("filmDescription")
				.releaseDate(LocalDate.of(1895, 12, 28))
				.duration(Duration.ofSeconds(1))
				.build();
		Assertions.assertTrue(Validators.filmValidator.isValid(film));
	}
	@Test
	void filmWithWrongDateValidationIsFalse() {
		Film film = Film.builder()
				.name("filmName")
				.description("filmDescription")
				.releaseDate(LocalDate.of(1895, 12, 27))
				.duration(Duration.ofSeconds(1))
				.build();
		Assertions.assertFalse(Validators.filmValidator.isValid(film));
	}
	@Test
	void filmWithoutNameValidationIsFalse() {
		Film film = Film.builder()
				.name("")
				.description("filmDescription")
				.releaseDate(LocalDate.of(1895, 12, 28))
				.duration(Duration.ofSeconds(1))
				.build();
		Assertions.assertFalse(Validators.filmValidator.isValid(film));
	}
	@Test
	void filmWithInvalidDescriptionValidationIsFalse() {
		Film film = Film.builder()
				.name("filmName")
				.description("filmDescriptionfilmDescriptionfilmDescriptionfilmDescription" +
						"filmDescriptionfilmDescriptionfilmDescriptionfilmDescriptionfilmDescription" +
						"filmDescriptionfilmDescriptionfilmDescriptionfilmDescriptionfilmDescription")
				.releaseDate(LocalDate.of(1895, 12, 28))
				.duration(Duration.ofSeconds(1))
				.build();
		Assertions.assertFalse(Validators.filmValidator.isValid(film));
	}
	@Test
	void filmWithNegativeDurationValidationIsFalse() {
		Film film = Film.builder()
				.name("filmName")
				.description("filmDescription")
				.releaseDate(LocalDate.of(1895, 12, 28))
				.duration(Duration.ofSeconds(-1))
				.build();
		Assertions.assertFalse(Validators.filmValidator.isValid(film));
	}
	@Test
	void filmWithZeroDurationValidationIsFalse() {
		Film film = Film.builder()
				.name("filmName")
				.description("filmDescription")
				.releaseDate(LocalDate.of(1895, 12, 28))
				.duration(Duration.ofSeconds(0))
				.build();
		Assertions.assertFalse(Validators.filmValidator.isValid(film));
	}
	@Test
	void emptyFilmValidationIsFalse() {
		try {
			Film film = Film.builder().build();
		} catch (Exception exception) {
			Assertions.assertEquals(exception.getMessage(), "name is marked non-null but is null");
		}
	}
	/*
	Тестирование валидации пользователей.
	 */
	@Test
	void validUserValidationIsTrue() {
		User user = User.builder()
				.email("email@email.email")
				.login("login")
				.name("name")
				.birthday(LocalDate.of(2000, 10, 10))
				.build();
		Assertions.assertTrue(Validators.userValidator.isValid(user));
	}
	@Test
	void userWithInvalidEmailValidationIsFalse() {
		User user = User.builder()
				.email("@email.email")
				.login("login")
				.name("name")
				.birthday(LocalDate.of(2000, 10, 10))
				.build();
		Assertions.assertFalse(Validators.userValidator.isValid(user));

		user = User.builder()
				.email("email@.email")
				.login("login")
				.name("name")
				.birthday(LocalDate.of(2000, 10, 10))
				.build();
		Assertions.assertFalse(Validators.userValidator.isValid(user));

		user = User.builder()
				.email("email@email.")
				.login("login")
				.name("name")
				.birthday(LocalDate.of(2000, 10, 10))
				.build();
		Assertions.assertFalse(Validators.userValidator.isValid(user));

		user = User.builder()
				.email("em@il@email.email")
				.login("login")
				.name("name")
				.birthday(LocalDate.of(2000, 10, 10))
				.build();
		Assertions.assertFalse(Validators.userValidator.isValid(user));
	}
	@Test
	void invalidUsersBirthdayValidationIsFalse() {
		User user = User.builder()
				.email("email@email.email")
				.login("login")
				.name("name")
				.birthday(LocalDate.of(3000, 10, 10))
				.build();
		Assertions.assertFalse(Validators.userValidator.isValid(user));
	}
	@Test
	void invalidUsersLoginValidationIsFalse() {
		User user = User.builder()
				.email("email@email.email")
				.login("    ")
				.name("name")
				.birthday(LocalDate.of(2000, 10, 10))
				.build();
		Assertions.assertFalse(Validators.userValidator.isValid(user));
	}
	@Test
	void emptyUserValidationIsFalse() {
		try {
			User user = User.builder().build();
		} catch (Exception exception) {
			Assertions.assertEquals(exception.getMessage(), "email is marked non-null but is null");
		}
	}
}
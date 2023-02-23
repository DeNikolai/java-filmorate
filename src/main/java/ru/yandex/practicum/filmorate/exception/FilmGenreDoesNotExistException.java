package ru.yandex.practicum.filmorate.exception;

public class FilmGenreDoesNotExistException extends RuntimeException {
	public FilmGenreDoesNotExistException(String message) {
		super(message);
	}
}

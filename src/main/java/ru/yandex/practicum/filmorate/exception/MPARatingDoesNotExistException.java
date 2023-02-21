package ru.yandex.practicum.filmorate.exception;

public class MPARatingDoesNotExistException extends RuntimeException {

	public MPARatingDoesNotExistException(String message) {
		super(message);
	}
}

package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyAddedException extends RuntimeException{
	public UserAlreadyAddedException(String message) {
		super(message);
	}
}

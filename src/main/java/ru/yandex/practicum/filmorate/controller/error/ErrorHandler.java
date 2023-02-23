package ru.yandex.practicum.filmorate.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleValidationFailed(final ValidationException e) {
		return Map.of("Validation error", e.getMessage());
	}

	@ExceptionHandler({FilmAlreadyAddedException.class, UserAlreadyAddedException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleAlreadyAdded(RuntimeException e) {
		return Map.of("Already added", e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> handleBadId(final IDException e) {
		return Map.of("Not found", e.getMessage());
	}

	@ExceptionHandler({FilmDoesNotExistException.class,
			UserDoesNotExistException.class,
			FilmGenreDoesNotExistException.class,
			MPARatingDoesNotExistException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> handleNotFound(RuntimeException e) {
		return Map.of("Not found", e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleUsersAlreadyFriendsOrAlreadyNot(FriendsException e) {
		return Map.of("Bad request", e.getMessage());
	}
}

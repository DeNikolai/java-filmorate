package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validators;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
	/*
	Контроллер со следующими эндпоинтами:
	1. Создание пользователя;
	2. Обновление пользователя;
	3. Получение списка всех пользователей.
	Контроллер хранит данные о пользователях в себе.
	 */
	private final HashMap<Integer, User> users = new HashMap<>();
	int idCounter = 0;

	@PostMapping
	public User postUser(@RequestBody @Valid User user) {
		log.debug("Request to add user: {}.", user);
		if(!Validators.userValidator.isValid(user)) {
			log.debug("Validation failed.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation failed.");
		}
		if (user.getName() == null)
			user.setName(user.getLogin());
		idCounter++;
		user.setId(idCounter);
		log.debug("Assigning an ID: {} to user.", idCounter);
		users.put(user.getId(), user);
		log.debug("User added.");
		return user;
	}

	@PutMapping
	public User putUser(@RequestBody @Valid User user) {
		log.debug("Request to update user: {}.", user);
		if(!Validators.userValidator.isValid(user)) {
			log.debug("Validation failed.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation failed.");
		}
		if(user.getId() == null || !users.containsKey(user.getId())) {
			log.debug("Invalid user ID.");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user ID.");
		}
		users.replace(user.getId(), user);
		log.debug("User updated.");
		return user;
	}

	@GetMapping
	public List<User> getFilms() {
		return new ArrayList<User>(users.values());
	}
}

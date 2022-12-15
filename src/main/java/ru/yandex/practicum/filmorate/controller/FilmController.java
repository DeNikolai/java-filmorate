package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validators;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
	/*
	Контроллер со следующими эндпоинтами:
	1. Добавление фильма;
	2. Обновление фильма;
	3. Получение всех фильмов.
	Контроллер хранит данные о фильмах в себе.
	 */
	private final HashMap<Integer, Film> films = new HashMap<>();
	private int idCounter = 0;

	@PostMapping
	public Film postFilm(@RequestBody @Valid Film film) {
		log.debug("Request to add film: {}.", film);
		if (!Validators.filmValidator.isValid(film)) {
			log.debug("Validation failed.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation failed.");
		}
		idCounter++;
		film.setId(idCounter);
		log.debug("Assigning an ID: {} to film.", idCounter);
		films.put(film.getId(), film);
		log.debug("Film added.");
		return film;
	}

	@PutMapping
	public Film putFilm(@RequestBody @Valid Film film){
		log.debug("Request to update film: {}.", film);
		if (!Validators.filmValidator.isValid(film)) {
			log.debug("Validation failed.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation failed.");
		}
		if (film.getId() == null || !films.containsKey(film.getId())) {
			log.debug("Invalid film ID.");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid film ID.");
		}
		films.replace(film.getId(), film);
		log.debug("Film updated.");
		return film;
	}

	@GetMapping
	public List<Film> getFilms() {
		return new ArrayList<Film>(films.values());
	}
}

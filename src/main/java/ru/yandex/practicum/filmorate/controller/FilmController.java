package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.IDException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Validators;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

	private final FilmService filmService;
	private static final int DEFAULT_COUNT = 10;

	@Autowired
	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@PostMapping
	public Film postFilm(@RequestBody @Valid Film film) {
		validateFilm(film);
		setLikesSet(film);
		return filmService.addFilm(film);
	}

	@GetMapping("/{id}")
	public Film getFilmById(@PathVariable int id) {
		filmIdCheck(id);
		return filmService.getFilmById(id);
	}

	@PutMapping
	public Film putFilm(@RequestBody @Valid Film film){
		if (film.getId() == null) {
			log.debug("Film ID in null.");
			throw new IDException("Film ID in null.");
		}
		filmIdCheck(film.getId());
		validateFilm(film);
		setLikesSet(film);
		return filmService.updateFilm(film);
	}

	@GetMapping
	public List<Film> getAllFilms() {
		return filmService.getAllFilms();
	}

	@DeleteMapping("/{id}")
	public Film deleteFilm(@PathVariable int id) {
		filmIdCheck(id);
		return filmService.deleteFilm(id);
	}

	@PutMapping("/{filmId}/like/{userId}")
	public Film addLike(@PathVariable int filmId, @PathVariable int userId) {
		filmIdCheck(filmId);
		userIdCheck(userId);
		return filmService.addLike(userId, filmId);
	}

	@DeleteMapping("/{filmId}/like/{userId}")
	public Film removeLike(@PathVariable int filmId, @PathVariable int userId) {
		filmIdCheck(filmId);
		userIdCheck(userId);
		return filmService.removeLike(userId, filmId);
	}

	@GetMapping("/popular")
	public Object[] getPopularFilms(@RequestParam Optional<String> count) {
		if (count.isEmpty())
			return filmService.getPopularFilms(DEFAULT_COUNT);
		else
			return filmService.getPopularFilms(Integer.parseInt(count.get()));
	}

	@GetMapping("/{id}/likes")
	public Set<Integer> getAllLikesById(@PathVariable int id) {
		filmIdCheck(id);
		return filmService.getAllLikesById(id);
	}

	private void validateFilm(Film film) {
		if (!Validators.filmValidator.isValid(film)) {
			log.debug("Film validation failed.");
			throw new ValidationException("Film validation failed.");
		}
	}

	private void setLikesSet(Film film) {
		if (film.getLikes() == null)
			film.setLikes(new HashSet<>());
	}

	private void filmIdCheck(Integer id) {
		if (id <= 0) {
			log.debug("Wrong film ID: " + id + "<= 0.");
			throw new IDException("Wrong film ID: " + id + "<= 0.");
		}
	}

	private void userIdCheck(int id) {
		if (id <= 0) {
			log.debug("Wrong user ID: " + id + "<= 0.");
			throw new IDException("Wrong user ID: " + id + "<= 0.");
		}
	}
}

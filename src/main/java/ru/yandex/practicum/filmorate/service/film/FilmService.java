package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;


@Service
@Slf4j
public class FilmService implements FilmServiceInterface {
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;

	@Autowired
	public FilmService(@Qualifier("FilmStorageDAO") FilmStorage filmStorage,
					   @Qualifier("UserStorageDAO") UserStorage userStorage) {
		this.filmStorage = filmStorage;
		this.userStorage = userStorage;
	}

	@Override
	public Film addFilm(Film film) {
		log.debug("Request to add film: {}.", film);
		if (filmStorage.contains(film)) {
			log.debug("Film already added.");
			throw new FilmAlreadyAddedException("Film already added.");
		}
		filmStorage.addFilm(film);
		log.debug("Film added.");
		return film;
	}

	@Override
	public Film getFilmById(int id) {
		log.debug("Request to get film by ID: {}.", id);
		return getFilmByIdAndExistCheck(id);
	}

	@Override
	public Film updateFilm(Film film) {
		log.debug("Request to update film: {}.", film);
		getFilmByIdAndExistCheck(film.getId());
		filmStorage.updateFilm(film);
		log.debug("Film updated.");
		return film;
	}

	@Override
	public List<Film> getAllFilms() {
		return new ArrayList<>(filmStorage.getFilms());
	}

	@Override
	public Film deleteFilm(int id) {
		log.debug("Request to delete film with ID: {}.", id);
		Film film = getFilmByIdAndExistCheck(id);
		filmStorage.deleteFilm(id);
		log.debug("Film deleted.");
		return film;
	}

	@Override
	public Film addLike(int userId, int filmId) {
		log.debug("Request to add like by user with id = {} to film with id = {}." , userId, filmId);
		userExistCheck(userId);
		Film film = getFilmByIdAndExistCheck(filmId);
		film.getLikes().add(userId);
		filmStorage.updateFilm(film);
		log.debug("User with id = {} add like to film with id = {}!", userId, filmId);
		return film;
	}

	@Override
	public Film removeLike(int userId, int filmId) {
		log.debug("Request remove add like by user with id = {} to film with id = {}." , userId, filmId);
		userExistCheck(userId);
		Film film = getFilmByIdAndExistCheck(filmId);
		film.getLikes().remove(userId);
		filmStorage.updateFilm(film);
		log.debug("User {} remove like to film {}!", userId, filmId);
		return film;
	}

	@Override
	public Object[] getPopularFilms(int count) {
		log.debug("Request to get first {} most popular films.", count);
		/*
		Первый элемент в стриме после сортировки с самым большим количеством лайков.
		 */
		return filmStorage.getFilms().stream()
				.sorted((Film f1, Film f2) -> f2.getLikes().size() - f1.getLikes().size())
				.limit(count)
				.toArray();
	}

	@Override
	public Set<Integer> getAllLikesById(int id) {
		log.debug("Request to get all likes by ID: {}.", id);
		Film film = getFilmByIdAndExistCheck(id);
		return film.getLikes();
	}

	private Film getFilmByIdAndExistCheck(int id) {
		Optional<Film> film = filmStorage.getFilmById(id);
		if (film.isEmpty()) {
			log.debug("Film with ID {} does not exist", id);
			throw new FilmDoesNotExistException("Film with ID " + id + " does not exist.");
		}
		return film.get();
	}

	private void userExistCheck(int id) {
		Optional<User> user = userStorage.getUserById(id);
		if (user.isEmpty()) {
			log.debug("User with id = {} does not exist.", id);
			throw new UserDoesNotExistException(String.format("User with id = %s does not exist.", id));
		}
	}
}

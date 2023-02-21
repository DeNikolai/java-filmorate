package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;

public interface FilmStorage {

	Collection<Film> getFilms();

	Optional<Film> getFilmById(int id);

	void addFilm(Film film);

	void updateFilm(Film film);

	void deleteFilm(int id);

	boolean contains(Film film);
}

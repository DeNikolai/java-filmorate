package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

	private final HashMap<Integer, Film> films = new HashMap<>();

	@Override
	public HashMap<Integer, Film> getFilms() {
		return films;
	}

	@Override
	public Optional<Film> getFilmById(int id) {
		return Optional.ofNullable(films.get(id));
	}

	@Override
	public void addFilm(Film film) {
		films.put(film.getId(), film);
	}

	@Override
	public void updateFilm(Film film){
		films.replace(film.getId(), film);
	}

	@Override
	public void deleteFilm(int id) {
		films.remove(id);
	}

	@Override
	public boolean contains(Film film) {
		return films.containsValue(film);
	}
}

package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class InMemoryFilmStorage implements FilmStorage {

	private int idCounter = 0;

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
		idCounter++;
		film.setId(idCounter);
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

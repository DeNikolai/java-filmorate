package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Set;

public interface FilmService {

	public Film addFilm(Film film);

	public Film getFilmById(int id);

	public Film updateFilm(Film film);

	public List<Film> getAllFilms();

	public Film deleteFilm(int id);

	public Film addLike(int userId, int filmId);

	public Film removeLike(int userId, int filmId);

	public Object[] getPopularFilms(int count);

	public Set<Integer> getAllLikesById(int id);


}

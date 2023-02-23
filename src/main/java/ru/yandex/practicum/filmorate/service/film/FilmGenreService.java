package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.film.FilmGenre;

import java.util.List;

public interface FilmGenreService {

	public List<FilmGenre> getAllFilmGenres();

	public FilmGenre getFilmGenreById(int id);
}

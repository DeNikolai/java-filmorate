package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.film.FilmGenre;

import java.util.List;
import java.util.Optional;

public interface FilmGenreDAO {

	List<FilmGenre> getAllFilmGenres();

	Optional<FilmGenre> getFilmGenreByGenreId(int genreId);
}

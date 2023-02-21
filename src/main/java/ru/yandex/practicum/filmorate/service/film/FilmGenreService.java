package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmGenreDAO;
import ru.yandex.practicum.filmorate.dao.film.FilmGenreDAOImpl;
import ru.yandex.practicum.filmorate.exception.FilmGenreDoesNotExistException;
import ru.yandex.practicum.filmorate.model.film.FilmGenre;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmGenreService {

	private final FilmGenreDAO filmGenreDAO;

	@Autowired
	public FilmGenreService(FilmGenreDAOImpl filmGenreDAOImpl) {
		this.filmGenreDAO = filmGenreDAOImpl;
	}

	public List<FilmGenre> getAllFilmGenres() {
		return filmGenreDAO.getAllFilmGenres();
	}

	public FilmGenre getFilmGenreById(int id) {
		Optional<FilmGenre> filmGenre = filmGenreDAO.getFilmGenreByGenreId(id);
		if (filmGenre.isPresent())
			return filmGenre.get();
		else
			throw new FilmGenreDoesNotExistException("");
	}
}

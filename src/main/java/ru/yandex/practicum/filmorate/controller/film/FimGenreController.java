package ru.yandex.practicum.filmorate.controller.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.FilmGenre;
import ru.yandex.practicum.filmorate.service.film.FilmGenreService;

import java.util.List;

@RestController
public class FimGenreController {

	private final FilmGenreService fgs;

	@Autowired
	public FimGenreController(FilmGenreService fgs) {
		this.fgs = fgs;
	}

	@GetMapping("/genres")
	public List<FilmGenre> getAllFilmGenres() {
		return fgs.getAllFilmGenres();
	}

	@GetMapping("/genres/{id}")
	public FilmGenre getFilmGenreById(@PathVariable int id) {
		return fgs.getFilmGenreById(id);
	}
}

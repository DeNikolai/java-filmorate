package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmGenreDAO;
import ru.yandex.practicum.filmorate.model.film.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FilmGenreDAOImpl implements FilmGenreDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public FilmGenreDAOImpl (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<FilmGenre> getAllFilmGenres() {
		log.debug("Request to find all genres from DB.");

		String sql = "SELECT * " +
				"FROM genres;";

		return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmGenre(rs));
	}

	@Override
	public Optional<FilmGenre> getFilmGenreByGenreId(int genreId) {
		log.debug("Request to find genre with id = {} from DB.", genreId);

		String sql = "SELECT * " +
				"FROM genres " +
				"WHERE id = ?;";
		List<FilmGenre> filmGenres = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmGenre(rs), genreId);
		if (filmGenres.isEmpty())
			return Optional.empty();
		else
			return Optional.of(filmGenres.iterator().next());
	}

	private FilmGenre makeFilmGenre(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");

		return FilmGenre.builder()
				.id(id)
				.name(name)
				.build();
	}
}

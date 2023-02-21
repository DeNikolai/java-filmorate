package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmGenre;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
@Qualifier("FilmStorageDAO")
@Slf4j
public class FilmStorageDAO implements FilmStorage {

	private final JdbcTemplate jdbcTemplate;

	private final FilmGenreDAO filmGenreDAO;
	private final MPARatingDAO mpaRatingDAO;

	@Autowired
	public FilmStorageDAO (JdbcTemplate jdbcTemplate,
						   FilmGenreDAOImpl filmGenreDAOImpl,
						   MPARatingDAOImpl mpaRatingDAOImpl) {
		this.jdbcTemplate = jdbcTemplate;
		this.filmGenreDAO = filmGenreDAOImpl;
		this.mpaRatingDAO = mpaRatingDAOImpl;
	}

	@Override
	public Collection<Film> getFilms() {
		log.debug("Request to get all films from DB.");

		String sql = "SELECT * " +
				"FROM films;";
		return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
	}

	private Film makeFilm(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String description = rs.getString("description");
		LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
		int duration = rs.getInt("duration");
		int mpaId = rs.getInt("mpa_rating_id");

		MPARating rating = getMPARatingByRatingId(mpaId);
		Set<FilmGenre> genres = findGenresByFilmId(id);
		Set<Integer> likes = findLikesById(id);

		return Film.builder()
				.id(id)
				.name(name)
				.description(description)
				.releaseDate(releaseDate)
				.duration(duration)
				.likes(likes)
				.mpa(rating)
				.genres(genres)
				.build();
	}

	private Set<Integer> findLikesById(int id) {
		log.debug("Request to find likes by id from DB.");

		Set<Integer> likes = new HashSet<>();
		String sql = "SELECT user_id " +
				"FROM likes " +
				"WHERE film_id = ?;";
		SqlRowSet likesRows = jdbcTemplate.queryForRowSet(sql, id);
		while (likesRows.next()) {
			likes.add(likesRows.getInt("user_id"));
		}
		return likes;
	}

	private Set<FilmGenre> findGenresByFilmId(int id) {
		log.debug("Request to find genres by film id from DB.");

		Set<FilmGenre> filmGenres = new HashSet<>();

		Collection<Integer> genreIds = new HashSet<>();

		String sql = "SELECT genre_id " +
				"FROM film_genres " +
				"WHERE film_id = ?";
		SqlRowSet genreIdRows = jdbcTemplate.queryForRowSet(sql, id);
		while (genreIdRows.next()) {
			genreIds.add(genreIdRows.getInt("genre_id"));
		}

		for (int genreId : genreIds) {
			filmGenres.add(filmGenreDAO.getFilmGenreByGenreId(genreId).orElse(null));
		}
		return filmGenres;
	}

	@Override
	public Optional<Film> getFilmById(int id) {
		log.debug("Request to get film by id from DB.");

		String sql = "SELECT * " +
				"FROM films " +
				"WHERE id = ?;";
		Collection<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
		if (films.isEmpty())
			return Optional.empty();
		else
			return Optional.of(films.iterator().next());
	}

	@Override
	public void addFilm(Film film) {
		log.debug("Request to add film in DB.");

		String sql = "INSERT INTO films (name, description, release_date, duration, mpa_rating_id) " +
				"VALUES (?, ?, ?, ?, ?);";
		jdbcTemplate.update(sql,
				film.getName(),
				film.getDescription(),
				film.getReleaseDate(),
				film.getDuration(),
				film.getMpa().getId());


		Integer id = findFilmId(film);
		film.setId(id);
		MPARating rating = getMPARatingByRatingId(film.getMpa().getId());

		addGenres(film);
		Set<FilmGenre> genres = findGenresByFilmId(film.getId());
		film.setGenres(genres);
		film.setMpa(rating);

		addLikes(film);
	}

	private void addLikes(Film film) {
		log.debug("Request to add likes to DB.");

		String sql = "INSERT INTO likes (user_id, film_id) " +
				"VALUES (?, ?);";

		Set<Integer> likes = film.getLikes();
		int filmId = film.getId();
		for (int userId : likes) {
			jdbcTemplate.update(sql, userId, filmId);
		}
	}

	private void addGenres(Film film) {
		log.debug("Request to add genres to DB.");

		String sql = "INSERT INTO film_genres (film_id, genre_id) " +
				"VALUES (?, ?);";

		Set<FilmGenre> genres = film.getGenres();
		int filmId = film.getId();
		for (FilmGenre fg : genres) {
			jdbcTemplate.update(sql, filmId, fg.getId());
		}
	}

	private MPARating getMPARatingByRatingId(int id) {
		return mpaRatingDAO.getMPARatingByRatingId(id).orElse(null);
	}

	private int findFilmId(Film film) {
		log.debug("Request to find film id from DB.");

		String sql = "SELECT id " +
				"FROM films " +
				"WHERE name = ? AND description = ?;";
		SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, film.getName(), film.getDescription());
		filmRows.next();
		return filmRows.getInt("id");
	}

	@Override
	public void updateFilm(Film film) {
		log.debug("Request to update film from DB.");

		String sql = "UPDATE films " +
				"SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?" +
				"WHERE id = ?";
		jdbcTemplate.update(sql,
				film.getName(),
				film.getDescription(),
				film.getReleaseDate(),
				film.getDuration(),
				film.getMpa().getId(),
				film.getId());

		removeLikes(film);
		removeGenres(film);
		addLikes(film);
		addGenres(film);
	}

	private void removeLikes(Film film) {
		log.debug("Request to remove likes from DB.");
		jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?;", film.getId());
	}
	private void removeGenres(Film film) {
		log.debug("Request o delete genres from DB.");
		jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
	}

	@Override
	public void deleteFilm(int id) {
		log.debug("Request to remove film from DB.");
		jdbcTemplate.update("DELETE FROM films WHERE id = ?;", id);
	}

	@Override
	public boolean contains(Film film) {
		log.debug("Request to contains film in DB.");

		String sql = "SELECT * " +
				"FROM films " +
				"WHERE name = ? AND description = ?;";
		SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, film.getName(), film.getDescription());
		return filmRows.next();
	}
}

package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MPARatingDAOImpl implements MPARatingDAO {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public MPARatingDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<MPARating> getAllMPARatings() {
		log.debug("Request to find all MPA ratings from DB.");

		String sql = "SELECT * " +
				"FROM mpa_ratings;";

		return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPARating(rs));
	}

	@Override
	public Optional<MPARating> getMPARatingByRatingId(int id) {
		log.debug("Request to get MPA rating with id = {} rom DB.", id);

		String sql = "SELECT * " +
				"FROM mpa_ratings " +
				"WHERE id = ?;";
		List<MPARating> MPARatings = jdbcTemplate.query(sql, (rs, rowNum) -> makeMPARating(rs), id);
		if (MPARatings.isEmpty())
			return Optional.empty();
		else
			return Optional.of(MPARatings.iterator().next());
	}

	private MPARating makeMPARating(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");

		return MPARating.builder()
				.id(id)
				.name(name)
				.build();
	}
}

package ru.yandex.practicum.filmorate.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("UserStorageDAO")
@Slf4j
public class UserStorageDAO implements UserStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserStorageDAO (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<User> getUsers() {
		log.debug("Request to get all users from DB.");

		String sql = "SELECT * " +
				"FROM users " +
				"ORDER BY id;";
		return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
	}

	private User makeUser(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String email = rs.getString("email");
		String login = rs.getString("login");
		String name = rs.getString("name");
		LocalDate birthday = rs.getDate("birthday").toLocalDate();

		return User.builder()
				.id(id)
				.email(email)
				.login(login)
				.name(name)
				.birthday(birthday)
				.friends(findFriendsByUserId(id))
				.build();
	}

	private HashMap<Integer, Boolean> findFriendsByUserId(int userId) {
		log.debug("Request to find users friends from DB.");

		HashMap<Integer, Boolean> friends = new HashMap<>();

		String sql = "SELECT friend_id, status " +
				"FROM friendships " +
				"WHERE user_id = ?;";
		SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(sql, userId);

		while (friendsRows.next()) {
			int friend_id = friendsRows.getInt("friend_id");
			boolean status = friendsRows.getBoolean("status");
			friends.put(friend_id, status);
		}

		return friends;
	}

	@Override
	public Optional<User> getUserById(int userId) {
		log.debug("Request to get user with id = {} from DB.", userId);

		String sql = "SELECT * " +
				"FROM users " +
				"WHERE id = ?;";

		Collection<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
		if (users.isEmpty())
			return Optional.empty();
		else
			return Optional.of(users.iterator().next());
	}

	@Override
	public void addUser(User user) {
		log.debug("Request to add user to DB.");

		String sql = "INSERT INTO users (email, login, name, birthday) " +
				"VALUES(?, ?, ?, ?);";
		jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
		user.setId(findUserId(user));
		addUserFriends(user);
	}

	private int findUserId(User user) {
		log.debug("Request to find user id from DB.");

		String sql = "SELECT id " +
				"FROM users " +
				"WHERE email = ?;";
		SqlRowSet userRow = jdbcTemplate.queryForRowSet(sql, user.getEmail());
		userRow.next();
		return userRow.getInt("id");
	}

	private void addUserFriends(User user) {
		log.debug("Request to add users friends to DB");
		HashMap<Integer, Boolean> friends = user.getFriends();
		if (friends.isEmpty())
			return;
		for (Integer friend_id : friends.keySet()) {
			String sql = "INSERT INTO PUBLIC.FRIENDSHIPS " +
					"(USER_ID, FRIEND_ID, STATUS) " +
					"VALUES(?, ?, ?);";
			jdbcTemplate.update(sql, user.getId(), friend_id, friends.get(friend_id));
		}
	}

	@Override
	public void updateUser(User user) {
		log.debug("Request to update user with id = {} in DB.", user.getId());

		String sql = "UPDATE users " +
				"SET email = ?, login = ?, name = ?, birthday = ? " +
				"WHERE id = ?; ";
		jdbcTemplate.update(sql,
				user.getEmail(),
				user.getLogin(),
				user.getName(),
				user.getBirthday(),
				user.getId());

		removeAllUserFriends(user);
		addUserFriends(user);
	}

	private void removeAllUserFriends(User user) {
		log.debug("Request to delete all user friends from DB.");
		String sql = "DELETE FROM friendships " +
				"WHERE user_id = ?";
		jdbcTemplate.update(sql, user.getId());
	}

	@Override
	public void deleteUser(int id) {
		log.debug("Request to delete user with id = {} from DB.", id);

		String sql = "DELETE FROM users " +
				"WHERE id = ?;";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public boolean contains(User user) {
		log.debug("Request to contains user in DB.");

		String sql = "SELECT id " +
				"FROM users " +
				"WHERE email = ?;";
		SqlRowSet userRow = jdbcTemplate.queryForRowSet(sql, user.getEmail());
		return userRow.next();
	}
}

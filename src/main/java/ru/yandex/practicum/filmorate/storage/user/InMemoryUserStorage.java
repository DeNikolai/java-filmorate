package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

	private final HashMap<Integer, User> users = new HashMap<>();

	@Override
	public List<User> getUsers() {
		return new ArrayList<>(users.values());
	}

	@Override
	public Optional<User> getUserById(int id) {
		return Optional.ofNullable(users.get(id));
	}

	@Override
	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	@Override
	public void updateUser(User user) {
		users.replace(user.getId(), user);
	}

	@Override
	public void deleteUser(int id) {
		users.remove(id);
	}

	@Override
	public boolean contains(User user) {
		return users.containsValue(user);
	}
}

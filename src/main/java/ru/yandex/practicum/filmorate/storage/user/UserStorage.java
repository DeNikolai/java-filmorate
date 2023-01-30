package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

	List<User> getUsers();

	Optional<User> getUserById(int userId);

	void addUser(User user);

	void updateUser(User user);

	void deleteUser(int id);

	boolean contains(User user);
}

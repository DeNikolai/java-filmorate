package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserServiceInterface {

	public User addUser(User user);

	public User getUserById(int id);

	public User updateUser(User user);

	public List<User> getAllUsers();

	public User deleteUserById(int id);

	public void addFriendByUsersIds(int userId, int friendId);

	public void removeFriendByUsersIds(int userId, int friendId);

	public List<User> getMutualFriendsListByUsersIds(int userId, int otherUserId);

	public List<User> getAllFriendsByUserId(int id);
}

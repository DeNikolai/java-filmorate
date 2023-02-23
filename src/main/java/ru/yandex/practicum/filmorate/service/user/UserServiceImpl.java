package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	private final UserStorage storage;

	@Autowired
	public UserServiceImpl(@Qualifier("UserStorageDAO") UserStorage userStorage) {
		this.storage = userStorage;
	}

	@Override
	public User addUser(User user) {
		log.debug("Request to add user: {}.", user);
		if (storage.contains(user)) {
			log.debug("User already added.");
			throw new UserAlreadyAddedException("User already added.");
		}
		storage.addUser(user);
		log.debug("User added.");
		return user;
	}

	@Override
	public User getUserById(int id) {
		log.debug("Request to get user by ID: {}.", id);
		return getUserByIdAndExistCheck(id);
	}

	@Override
	public User updateUser(User user)  {
		log.debug("Request to update user: {}.", user);
		getUserByIdAndExistCheck(user.getId());
		storage.updateUser(user);
		log.debug("User updated.");
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		return storage.getUsers();
	}

	@Override
	public User deleteUserById(int id) {
		log.debug("Request to delete user with ID: {}.", id);
		User user = getUserByIdAndExistCheck(id);
		deleteUserFromAllFriendsSets(user);
		storage.deleteUser(id);
		log.debug("User with ID {} delete", id);
		return user;
	}

	@Override
	public void addFriendByUsersIds(int userId, int friendId) {
		log.debug("Request to become friends by ID: {} {}.", userId, friendId);
		User user1 = getUserByIdAndExistCheck(userId);
		User user2 = getUserByIdAndExistCheck(friendId);
		if (user1.getFriends().containsKey(friendId)) {
			log.debug("User with ID = {} and user with ID = {} are already friends.", userId, friendId);
			throw new FriendsException(
					String.format("User with ID = %s and user with ID = %s are already friends.", userId, friendId)
			);
		}
		user1.getFriends().put(friendId, true);
		storage.updateUser(user1);
		log.debug("User with ID = {} are friend with user with ID = {}!", userId, friendId);
		/*
		Дружба теперь односторонняя. На всякий оставил старый код, вдруг чо.
		user2.getFriends().put(userId, true);
		storage.updateUser(user2);
		log.debug("Users with IDs: {} {} are friends!", userId, friendId);
		 */
	}

	@Override
	public void removeFriendByUsersIds(int userId, int friendId) {
		log.debug("Request to end friendship by IDs: {} {}.", userId, friendId);
		User user1 = getUserByIdAndExistCheck(userId);
		User user2 = getUserByIdAndExistCheck(friendId);
		if (!user1.getFriends().containsKey(friendId)) {
			log.debug("User with ID = {} and user with ID = {} are not friends.", userId, friendId);
			throw new FriendsException(
					String.format("User with ID = %s and user with ID = %s are not friends.", userId, friendId)
			);
		}
		user1.getFriends().remove(friendId);
		storage.updateUser(user1);
		user2.getFriends().remove(friendId);
		storage.updateUser(user2);
		log.debug("Friendship between users with IDs: {} {} is over!", userId, friendId);
	}

	@Override
	public List<User> getMutualFriendsListByUsersIds(int userId, int otherUserId) {
		log.debug("Request to get mutual friends by IDs: {} {}.", userId, otherUserId);
		User user1 = getUserByIdAndExistCheck(userId);
		User user2 = getUserByIdAndExistCheck(otherUserId);
		Set<Integer> user1Friends = user1.getFriends().keySet();
		Set<Integer> user2Friends = user2.getFriends().keySet();

		Set<Integer> mutualFriendsIDs = user1Friends.stream()
				.filter(user2Friends::contains)
				.collect(Collectors.toSet());

		List<User> mutualFriends = new ArrayList<>();
		for (Integer id : mutualFriendsIDs) {
			Optional<User> user = storage.getUserById(id);
			user.ifPresent(mutualFriends::add);
		}
		return mutualFriends;
	}

	@Override
	public List<User> getAllFriendsByUserId(int id) {
		log.debug("Request to get all friends by ID: {}", id);
		User user = getUserByIdAndExistCheck(id);

		List<User> friends = new ArrayList<>();
		for (int friendId : user.getFriends().keySet()) {
			Optional<User> friend = storage.getUserById(friendId);
			friend.ifPresent(friends::add);
		}
		return friends;
	}

	private User getUserByIdAndExistCheck(int id) {
		Optional<User> user = storage.getUserById(id);
		if (user.isEmpty()) {
			log.debug("User with id = {} does not exist.", id);
			throw new UserDoesNotExistException(String.format("User with id = %s does not exist.", id));
		}
		return user.get();
	}

	private void deleteUserFromAllFriendsSets(User user) {
		Set<Integer> userFriends = user.getFriends().keySet();
		for (Integer id : userFriends) {
			Optional<User> friend = storage.getUserById(id);
			friend.ifPresent(value -> value.getFriends().remove(user.getId()));
		}
	}
}

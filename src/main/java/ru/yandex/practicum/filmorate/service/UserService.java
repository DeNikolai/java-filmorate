package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

	private int idCounter = 0;
	private final UserStorage storage;

	@Autowired
	public UserService(InMemoryUserStorage inMemoryUserStorage) {
		this.storage = inMemoryUserStorage;
	}

	public User addUser(User user) {
		log.debug("Request to add user: {}.", user);
		if(storage.contains(user)) {
			log.debug("User already added.");
			throw new UserAlreadyAddedException("User already added.");
		}
		idCounter++;
		user.setId(idCounter);
		storage.addUser(user);
		log.debug("User added.");
		return user;
	}

	public User getUserById(int id) {
		log.debug("Request to get user by ID: {}.", id);
		return getUserByIdAndExistCheck(id);
	}

	public User updateUser(User user)  {
		log.debug("Request to update user: {}.", user);
		getUserByIdAndExistCheck(user.getId());
		storage.updateUser(user);
		log.debug("User updated.");
		return user;
	}

	public List<User> getAllUsers() {
		return storage.getUsers();
	}

	public User deleteUserById(int id) {
		log.debug("Request to delete user with ID: {}.", id);
		User user = getUserByIdAndExistCheck(id);
		deleteUserFromAllFriendsSets(user);
		storage.deleteUser(id);
		log.debug("User with ID {} delete", id);
		return user;
	}

	public void addFriendByUsersIds(int user1Id, int user2Id) {
		log.debug("Request to become friends by ID: {} {}.", user1Id, user2Id);
		User user1 = getUserByIdAndExistCheck(user1Id);
		User user2 = getUserByIdAndExistCheck(user2Id);
		if (user1.getFriends().contains(user2Id)) {
			log.debug("User with ID = {} and user with ID = {} are already friends.", user1Id, user2Id);
			throw new FriendsException(
					String.format("User with ID = %s and user with ID = %s are already friends.", user1Id, user2Id)
			);
		}
		user1.getFriends().add(user2Id);
		storage.updateUser(user1);
		user2.getFriends().add(user1Id);
		storage.updateUser(user2);
		log.debug("Users with IDs: {} {} are friends!", user1Id, user2Id);
	}

	public void removeFriendByUsersIds(int user1Id, int user2Id) {
		log.debug("Request to end friendship by IDs: {} {}.", user1Id, user2Id);
		User user1 = getUserByIdAndExistCheck(user1Id);
		User user2 = getUserByIdAndExistCheck(user2Id);
		if (!user1.getFriends().contains(user2Id)) {
			log.debug("User with ID = {} and user with ID = {} are not friends.", user1Id, user2Id);
			throw new FriendsException(
					String.format("User with ID = %s and user with ID = %s are not friends.", user1Id, user2Id)
			);
		}
		user1.getFriends().remove(user2Id);
		storage.updateUser(user1);
		user2.getFriends().remove(user2Id);
		storage.updateUser(user2);
		log.debug("Friendship between users with IDs: {} {} is over!", user1Id, user2Id);
	}

	public List<User> getMutualFriendsListByUsersIds(int user1Id, int user2Id) {
		log.debug("Request to get mutual friends by IDs: {} {}.", user1Id, user2Id);
		User user1 = getUserByIdAndExistCheck(user1Id);
		User user2 = getUserByIdAndExistCheck(user2Id);
		Set<Integer> user1Friends = user1.getFriends();
		Set<Integer> user2Friends = user2.getFriends();

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

	public List<User> getAllFriendsByUserId(int id) {
		log.debug("Request to get all friends by ID: {}", id);
		User user = getUserByIdAndExistCheck(id);

		List<User> friends = new ArrayList<>();
		for (int friendId : user.getFriends()) {
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
		Set<Integer> userFriends = user.getFriends();
		for (Integer id : userFriends) {
			Optional<User> friend = storage.getUserById(id);
			friend.ifPresent(value -> value.getFriends().remove(user.getId()));
		}
	}
}

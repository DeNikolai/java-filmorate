package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.IDException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Validators;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public User postUser(@RequestBody @Valid User user) {
		validateUser(user);
		setUserNameAndFriendsSet(user);
		return userService.addUser(user);
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable int id) {
		userIdCheck(id);
		return userService.getUserById(id);
	}

	@PutMapping
	public User putUser(@RequestBody @Valid User user) {
		if (user.getId() == null) {
			log.debug("User ID is null.");
			throw new IDException("User ID is null.");
		}
		userIdCheck(user.getId());
		validateUser(user);
		setUserNameAndFriendsSet(user);
		return userService.updateUser(user);
	}

	@GetMapping
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@DeleteMapping("/{id}")
	public User deleteUser(@PathVariable int id) {
		userIdCheck(id);
		return userService.deleteUserById(id);
	}

	@PutMapping("/{id}/friends/{friendId}")
	public void addFriendByUsersIds(@PathVariable int id, @PathVariable int friendId) {
		if (id == friendId) {
			log.debug("Request to add friend by users IDs with the same id.");
			throw new IDException("Request to add friend by users IDs with the same id.");
		}
		userIdCheck(id);
		userIdCheck(friendId);
		userService.addFriendByUsersIds(id, friendId);
	}

	@DeleteMapping("/{id}/friends/{friendId}")
	public void removeFriendByUsersIds(@PathVariable int id, @PathVariable int friendId) {
		if (id == friendId) {
			log.debug("Request to remove friend by users IDs with the same id.");
			throw new IDException("Request to remove friend by users IDs with the same id.");
		}
		userIdCheck(id);
		userIdCheck(friendId);
		userService.removeFriendByUsersIds(id, friendId);
	}

	@GetMapping("/{id}/friends/common/{otherId}")
	public List<User> getMutualFriendsListByUsersIds(@PathVariable int id, @PathVariable int otherId) {
		if (id == otherId) {
			log.debug("Request to get mutual friends by users IDs with the same id.");
			throw new IDException("Request to get mutual friends by users IDs with the same id.");
		}
		userIdCheck(id);
		userIdCheck(otherId);
		return userService.getMutualFriendsListByUsersIds(id, otherId);
	}

	@GetMapping("/{id}/friends")
	public List<User> getAllFriendsById(@PathVariable int id) {
		userIdCheck(id);
		return userService.getAllFriendsByUserId(id);
	}

	private void validateUser(User user) {
		if(!Validators.userValidator.isValid(user)) {
			log.debug("Validation failed.");
			throw new ValidationException("Validation failed.");
		}
	}

	private void setUserNameAndFriendsSet(User user) {
		if(user.getFriends() == null)
			user.setFriends(new HashSet<>());
		if (user.getName() == null || user.getName().isBlank())
			user.setName(user.getLogin());
	}

	private void userIdCheck(int id) {
		if (id <= 0) {
			log.debug("Wrong user ID: " + id + "<= 0.");
			throw new IDException("Wrong user ID: " + id + "<= 0.");
		}
	}
}

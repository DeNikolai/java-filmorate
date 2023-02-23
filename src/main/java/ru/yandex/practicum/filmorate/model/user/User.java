package ru.yandex.practicum.filmorate.model.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

@Data
@Builder
public class User {
	@EqualsAndHashCode.Exclude
	private Integer id;
	@NonNull
	@Email
	private final String email;
	@NonNull
	@NotBlank
	private final String login;
	@EqualsAndHashCode.Exclude
	private String name;
	@NonNull
	@EqualsAndHashCode.Exclude
	private final LocalDate birthday;
	@EqualsAndHashCode.Exclude
	private HashMap<Integer, Boolean> friends;
}

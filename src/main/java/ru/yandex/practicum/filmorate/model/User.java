package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@Builder//для тестирования
public class User {
	private Integer id;
	@Email
	@NonNull
	private final String email;
	@NonNull
	private final String login;
	private String name;
	@NonNull
	private final LocalDate birthday;
}

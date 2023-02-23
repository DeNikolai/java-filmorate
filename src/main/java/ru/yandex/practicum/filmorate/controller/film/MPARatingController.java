package ru.yandex.practicum.filmorate.controller.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.service.film.MPARatingService;
import ru.yandex.practicum.filmorate.service.film.MPARatingServiceImpl;

import java.util.List;

@RestController
public class MPARatingController {

	private final MPARatingService mrs;

	@Autowired
	public MPARatingController(MPARatingServiceImpl mrs) {
		this.mrs = mrs;
	}

	@GetMapping("/mpa")
	public List<MPARating> getAllMPARatings() {
		return mrs.getAllMPARatings();
	}

	@GetMapping("/mpa/{id}")
	public MPARating getMPARatingByRatingId(@PathVariable int id) {
		return mrs.getMPARatingByRatingId(id);
	}
}

package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.film.MPARating;

import java.util.List;

public interface MPARatingService {

	List<MPARating> getAllMPARatings();

	public MPARating getMPARatingByRatingId(int id);
}

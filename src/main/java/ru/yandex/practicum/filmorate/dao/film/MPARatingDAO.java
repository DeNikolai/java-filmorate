package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.film.MPARating;

import java.util.List;
import java.util.Optional;

public interface MPARatingDAO {

	List<MPARating> getAllMPARatings();

	Optional<MPARating> getMPARatingByRatingId(int id);
}

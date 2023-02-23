package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.MPARatingDAO;
import ru.yandex.practicum.filmorate.dao.film.MPARatingDAOImpl;
import ru.yandex.practicum.filmorate.exception.MPARatingDoesNotExistException;
import ru.yandex.practicum.filmorate.model.film.MPARating;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MPARatingServiceImpl implements MPARatingService {

	private final MPARatingDAO mpaRatingDAO;

	@Autowired
	public MPARatingServiceImpl(MPARatingDAOImpl mpaRatingDAOImpl) {
		this.mpaRatingDAO = mpaRatingDAOImpl;
	}

	@Override
	public List<MPARating> getAllMPARatings() {
		return mpaRatingDAO.getAllMPARatings();
	}

	@Override
	public MPARating getMPARatingByRatingId(int id) {
		Optional<MPARating> mpaRating = mpaRatingDAO.getMPARatingByRatingId(id);
		if (mpaRating.isPresent())
			return mpaRating.get();
		else
			throw new MPARatingDoesNotExistException("");
	}
}

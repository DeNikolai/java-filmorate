package ru.yandex.practicum.filmorate.validator;

import java.util.List;
import java.util.function.Predicate;

public class Validator <T> {
	private final List<Predicate<T>> predicateList;

	public Validator(List<Predicate<T>> predicateList) {
		this.predicateList = predicateList;
	}

	public boolean isValid(T t) {
		boolean isValid = true;
		for(Predicate<T> p: predicateList) {
			isValid = isValid && p.test(t);
		}
		return isValid;
	}
}

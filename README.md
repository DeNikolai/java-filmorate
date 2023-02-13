# java-filmorate
Template repository for Filmorate project.
![ER-model](Filmorate%20ER-model.png)


Примеры запросов: 
1. Получение имен фильмов, которые понравились друзьям пользователя с id = 1
  
  ```
  SELECT name
  FROM film
  WHERE film_id IN (
                  /*
                  Подзапрос возвращает id фильмов, которые понравились друзьям пользователя
                  */
                  SELECT film_id
                  FROM like
                  WHERE user_id IN (
                    /* 
                    Подзапрос возвращает id друзей пользователя
                    */
                    SELECT friend_id
                    FROM friendship
                    WHERE user_id = 1
                  )
 );
```

2. Получение жанров 5 самых популярных фильмов

```
  SELECT DISTINCT g.name
  FROM genre AS g
  WHERE film_id IN (
    /*
    Подзапрос возвращает id 5 самых популярных фильмов
    */
    SELECT film_id
    FROM like
    GROUP BY film_id
    ORDER BY COUNT (user_id) DESC
    LIMIT 5
  );
  ```

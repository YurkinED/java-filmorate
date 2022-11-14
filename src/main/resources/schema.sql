DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS films_genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS reviews_likes CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS films_directors CASCADE;
DROP TABLE IF EXISTS feeds CASCADE;



CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id   int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name varchar(40)
);

COMMENT ON TABLE mpa IS 'Список рейтингов';
COMMENT ON COLUMN mpa.mpa_name IS 'Имя рейтинга';

CREATE TABLE IF NOT EXISTS films
(
    film_id      int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name    varchar(40) NOT NULL,
    description  varchar(200),
    release_date DATE,
    duration     int,
    mpa_id       int REFERENCES mpa (mpa_id)
);

COMMENT ON TABLE films IS 'Список фильмов';
COMMENT ON COLUMN films.film_name IS 'Название фильма';
COMMENT ON COLUMN films.description IS 'Описание содержимого фильма';
COMMENT ON COLUMN films.release_date IS 'Дата релиза';
COMMENT ON COLUMN films.duration IS 'Продолжительность фильма в минутах';
COMMENT ON COLUMN films.mpa_id IS 'ИД рейтинга';


CREATE TABLE IF NOT EXISTS genres
(
    genre_id   int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(40)
);

COMMENT ON TABLE genres IS 'Жанры фильмов';
COMMENT ON COLUMN genres.genre_name IS 'Имя жанра';

CREATE TABLE IF NOT EXISTS films_genres
(
    film_id  int
        references films
            on delete cascade,
    genre_id INTEGER
        references genres
            on delete cascade,
    PRIMARY KEY (film_id, genre_id)
);

COMMENT ON TABLE films_genres IS 'Связь фильма с жанрами';

CREATE TABLE IF NOT EXISTS users
(
    user_id  int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    varchar(40),
    login    varchar(40) NOT NULL,
    name     varchar(40),
    birthday DATE
);

COMMENT ON TABLE users IS 'Список пользователей';
COMMENT ON COLUMN users.email IS 'Почта пользователя';
COMMENT ON COLUMN users.login IS 'Логин пользователя';
COMMENT ON COLUMN users.name IS 'Имя пользователя';
COMMENT ON COLUMN users.birthday IS 'Дата рождения пользователя';

CREATE TABLE IF NOT EXISTS likes
(
    film_id int
        references films
            on delete cascade,
    user_id INTEGER
        references users
            on delete cascade,
    PRIMARY KEY (film_id, user_id)
);

COMMENT ON TABLE likes IS 'Лайки фильма пользователями';

CREATE TABLE IF NOT EXISTS friends
(
    first_user_id  int
        references users
            on delete cascade,
    second_user_id INTEGER
        references users
            on delete cascade,
    PRIMARY KEY (first_user_id, second_user_id)
);

COMMENT ON TABLE friends IS 'Список друзей';
COMMENT ON COLUMN friends.first_user_id IS 'ИД пользователя подавшего заявку';
COMMENT ON COLUMN friends.second_user_id IS 'ИД пользователя получившего заявку';

CREATE TABLE IF NOT EXISTS reviews
(
    review_id  int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content    text,
    isPositive boolean,
    useful     int default 0,
    user_id    int REFERENCES users (user_id) ON DELETE CASCADE,
    film_id    int REFERENCES films (film_id) ON DELETE CASCADE,
    constraint  UIX_reviews unique (user_id, film_id)
);

COMMENT ON TABLE reviews IS 'Список отзывов';
COMMENT ON COLUMN reviews.content IS 'Текст отзыва';
COMMENT ON COLUMN reviews.isPositive IS 'Является ли отзыв положительным';
COMMENT ON COLUMN reviews.useful IS 'Полезност отзыва';



CREATE TABLE IF NOT EXISTS reviews_likes
(
    review_id int REFERENCES reviews (review_id) ON DELETE CASCADE,
    user_id   int REFERENCES users (user_id) ON DELETE CASCADE,
    useful    int NOT NULL,
    PRIMARY KEY (review_id, user_id)
);

COMMENT ON TABLE reviews_likes IS 'Список лайков на отзыв';
COMMENT ON COLUMN reviews_likes.useful IS 'Является ли лайком или дизлайком';

CREATE TABLE IF NOT EXISTS directors
(
    director_id   int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name varchar(60)
);

COMMENT ON TABLE directors IS 'Список режиссёров';
COMMENT ON COLUMN directors.director_name IS 'Имя режиссёра';

CREATE TABLE IF NOT EXISTS films_directors
(
    film_id     int REFERENCES films (film_id) ON DELETE CASCADE,
    director_id int REFERENCES directors (director_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, director_id)
);

COMMENT ON TABLE films_directors IS 'Связь фильмов с режиссёрами';

CREATE TABLE IF NOT EXISTS feeds
(
    feed_id       int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id       INT REFERENCES users (user_id) ON DELETE CASCADE,
    event_type    ENUM ('LIKE', 'REVIEW','FRIEND') not null,
    operation     ENUM ('REMOVE', 'ADD','UPDATE')  not null,
    entity_id     INTEGER                          not null,
    creation_time LONG                             not null
);


COMMENT ON TABLE feeds IS 'Список событий';
COMMENT ON COLUMN feeds.user_id IS 'Ид пользователя';
COMMENT ON COLUMN feeds.event_type IS 'Тип события';
COMMENT ON COLUMN feeds.operation IS 'Вил действия';
COMMENT ON COLUMN feeds.entity_id IS 'ИД объекта';
COMMENT ON COLUMN feeds.creation_time IS 'Время создания';


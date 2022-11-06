DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS films_genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS reviews_likes CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS films_directors CASCADE;

CREATE TABLE mpa
(
    mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name varchar(40)
);

CREATE TABLE films
(
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name varchar(40) NOT NULL,
    description varchar(200),
    release_date DATE,
    duration int,
    mpa_id int REFERENCES mpa(mpa_id)
);

CREATE TABLE genres
(
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(40)
);

create table films_genres
(
    film_id  INTEGER
        references films
            on delete cascade,
    genre_id INTEGER
        references genres
            on delete cascade
);

CREATE TABLE users
(
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(40),
    login varchar(40) NOT NULL,
    name varchar(40),
    birthday DATE
);

create table likes
(
    film_id INTEGER
        references films
            on delete cascade,
    user_id INTEGER
        references users
            on delete cascade
);

create table friends
(
    first_user_id  INTEGER
        references users
            on delete cascade,
    second_user_id INTEGER
        references users
            on delete cascade
);

CREATE TABLE reviews
(
    review_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content text,
    isPositive boolean,
    useful int default 0,
    user_id int REFERENCES users(user_id) ON DELETE CASCADE,
    film_id int REFERENCES films(film_id)  ON DELETE CASCADE
);

CREATE TABLE reviews_likes
(
    review_id int REFERENCES reviews(review_id) ON DELETE CASCADE,
    user_id int REFERENCES users(user_id) ON DELETE CASCADE,
    isLike boolean NOT NULL
);

CREATE TABLE directors
(
    director_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name varchar(60)
);

CREATE TABLE films_directors
(
    film_id int REFERENCES films(film_id) ON DELETE CASCADE,
    director_id int REFERENCES directors(director_id) ON DELETE CASCADE
);



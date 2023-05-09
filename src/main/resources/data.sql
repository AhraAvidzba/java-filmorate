--ЗАПОЛНЕНИЕ ТАБЛИЦЫ GENRE
MERGE INTO GENRE (GENRE_ID, NAME) KEY (GENRE_ID) VALUES (1, 'Комедия');
MERGE INTO GENRE (GENRE_ID, NAME) KEY (GENRE_ID) VALUES (2, 'Драма');
MERGE INTO GENRE (GENRE_ID, NAME) KEY (GENRE_ID) VALUES (3, 'Мультфильм');
MERGE INTO GENRE (GENRE_ID, NAME) KEY (GENRE_ID) VALUES (4, 'Триллер');
MERGE INTO GENRE (GENRE_ID, NAME) KEY (GENRE_ID) VALUES (5, 'Документальный');
MERGE INTO GENRE (GENRE_ID, NAME) KEY (GENRE_ID) VALUES (6, 'Боевик');

--ЗАПОЛНЕНИЕ ТАБЛИЦЫ RATING
MERGE INTO RATING (RATING_ID, NAME) KEY (RATING_ID) VALUES (1, 'G');
MERGE INTO RATING (RATING_ID, NAME) KEY (RATING_ID) VALUES (2, 'PG');
MERGE INTO RATING (RATING_ID, NAME) KEY (RATING_ID) VALUES (3, 'PG-13');
MERGE INTO RATING (RATING_ID, NAME) KEY (RATING_ID) VALUES (4, 'R');
MERGE INTO RATING (RATING_ID, NAME) KEY (RATING_ID) VALUES (5, 'NC-17');



-- --ЗАПОЛНЕНИЕ ТАБЛИЦЫ FILM
-- MERGE INTO FILM (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) KEY (FILM_ID) VALUES (1, 'Аватар',
--                                                                                                    'Действие фильма происходит в 2154 году, когда человечество добывает ценный минерал анобтаниум на Пандоре',
--                                                                                                    '2009-12-10', 162,
--                                                                                                    3);
-- MERGE INTO FILM (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) KEY (FILM_ID) VALUES (2, 'Мстители: Финал',
--                                                                                                    'американский супергеройский фильм',
--                                                                                                    '2019-04-22', 181,
--                                                                                                    3);
-- MERGE INTO FILM (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) KEY (FILM_ID) VALUES (3, 'Титаник',
--                                                                                                    'американская эпическая романтическая драма и фильм-катастрофа',
--                                                                                                    '1997-11-01', 195,
--                                                                                                    3);

-- --ЗАПОЛНЕНИЕ ТАБЛИЦЫ USER
-- MERGE INTO _USER (USER_ID, BIRTHDAY, LOGIN, EMAIL, NAME) KEY (USER_ID) VALUES (1, '1992-08-30', 'akhra', 'akhraa1@yandex.ru', 'Ахра');
-- MERGE INTO _USER (USER_ID, BIRTHDAY, LOGIN, EMAIL, NAME) KEY (USER_ID) VALUES (2, '1991-01-17', 'julia', 'julia@gmail.com', 'Юлия');
-- MERGE INTO _USER (USER_ID, BIRTHDAY, LOGIN, EMAIL, NAME) KEY (USER_ID) VALUES (3, '1994-04-17', 'anri', 'anri@yandex.ru', 'Анри');
--
-- --ЗАПОЛНЕНИЕ ТАБЛИЦЫ FILM_GENRE
-- MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (1, 3);
-- MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (1, 4);
-- MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (2, 3);
-- MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (2, 4);
-- MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (3, 6);
-- MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (3, 5);
-- MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (3, 2);
-- MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (3, 4);
--
-- --ЗАПОЛНЕНИЕ ТАБЛИЦЫ FRIENDSHIP
-- MERGE INTO FRIENDSHIP KEY (USER_ID, FRIEND_ID) VALUES (1, 2, 'CONFIRMED');
-- MERGE INTO FRIENDSHIP KEY (USER_ID, FRIEND_ID) VALUES (3, 2, 'UNCONFIRMED');
-- MERGE INTO FRIENDSHIP KEY (USER_ID, FRIEND_ID) VALUES (1, 3, 'CONFIRMED');
--
-- --ЗАПОЛНЕНИЕ ТАБЛИЦЫ LIKES
-- MERGE INTO LIKES KEY (FILM_ID, USER_ID) VALUES (1, 1);
-- MERGE INTO LIKES KEY (FILM_ID, USER_ID) VALUES (1, 2);
-- MERGE INTO LIKES KEY (FILM_ID, USER_ID) VALUES (1, 3);
-- MERGE INTO LIKES KEY (FILM_ID, USER_ID) VALUES (3, 1);
-- MERGE INTO LIKES KEY (FILM_ID, USER_ID) VALUES (3, 2);
-- //MERGE INTO LIKES KEY (FILM_ID, USER_ID) VALUES (2, 3);
--
--
--
--

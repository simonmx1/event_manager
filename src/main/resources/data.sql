INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, ROLE, CREATE_DATE) VALUES('true', 'Admin', 'Istrator', '$2a$10$nU8E73kNkO28YwFyx2Xwee21e8KS9DhV7UCKArOa2TyLpocRho/KG', 'admin', 'admin@admin.com', 'ADMIN', '2016-01-01 00:00:00');
INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, ROLE, CREATE_DATE) VALUES('true', 'Matthias', 'Komar', '$2a$10$nU8E73kNkO28YwFyx2Xwee21e8KS9DhV7UCKArOa2TyLpocRho/KG', 'user1', 'admin2@admin.com', 'USER', '2016-01-01 00:00:00');
INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, ROLE, CREATE_DATE) VALUES('true', 'Max', 'Mustermann', '$2a$10$nU8E73kNkO28YwFyx2Xwee21e8KS9DhV7UCKArOa2TyLpocRho/KG', 'user2', 'user@nichtadmin.com', 'LOCATION_MANAGER', '2016-01-01 00:00:00');
INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, ROLE, CREATE_DATE) VALUES('true', 'Elvis', 'The King', '$2a$10$nU8E73kNkO28YwFyx2Xwee21e8KS9DhV7UCKArOa2TyLpocRho/KG', 'elvis', 'elvis@elvis.com', 'USER', '2016-01-01 00:00:00');

INSERT INTO TAG (TEXT, CREATE_DATE) VALUES('Chinese', '2016-01-01 00:00:00');
INSERT INTO TAG (TEXT, CREATE_DATE) VALUES('Fast Food', '2016-01-01 00:00:00');
INSERT INTO TAG (TEXT, CREATE_DATE) VALUES('Pizza', '2016-01-01 00:00:00');
INSERT INTO TAG (TEXT, CREATE_DATE) VALUES('Delivery', '2016-01-01 00:00:00');

INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'Pizzeria Mario', 'mariuspizza.it', 'goo.gl/maps/j4QqpPUHtUoqqyEp9', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'McDonalds', 'mcdonalds.at', 'goo.gl/maps/tJEtHefB6FYXJkAa9', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'Burger King', 'burgerking.de', 'goo.gl/maps/CPPrigxSo9Tg8hfF7', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'KFC', 'kfc.de', 'goo.gl/maps/fQkQcnWPH2s9MiLt7', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'Meggima', 'meggima.eu', 'goo.gl/maps/Fspndr3WTdEJJyMH6', '2016-01-01 00:00:00');

INSERT INTO LOCATION_TAGS(LOCATION_ID, TAGS_TEXT) VALUES(1, 'Chinese');
INSERT INTO LOCATION_TAGS(LOCATION_ID, TAGS_TEXT) VALUES(1, 'Fast Food');
INSERT INTO LOCATION_TAGS(LOCATION_ID, TAGS_TEXT) VALUES(2, 'Pizza');
INSERT INTO LOCATION_TAGS(LOCATION_ID, TAGS_TEXT) VALUES(2, 'Delivery');
INSERT INTO LOCATION_TAGS(LOCATION_ID, TAGS_TEXT) VALUES(3, 'Chinese');
INSERT INTO LOCATION_TAGS(LOCATION_ID, TAGS_TEXT) VALUES(4, 'Chinese');
INSERT INTO LOCATION_TAGS(LOCATION_ID, TAGS_TEXT) VALUES(4, 'Pizza');
INSERT INTO LOCATION_TAGS(LOCATION_ID, TAGS_TEXT) VALUES(5, 'Chinese');

INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '13:00:00','19:00:00', 0,1,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '13:00:00','19:00:00', 1,1,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','13:00:00', 4,1,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '17:00:00','23:00:00', 4,1,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','23:00:00', 5,1,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','23:59:59', 6,1,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '00:00:00','23:59:59', 0,2,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '00:00:00','23:59:59', 1,2,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '00:00:00','23:59:59', 2,2,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '00:00:00','23:59:59', 3,2,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '00:00:00','23:59:59', 4,2,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '00:00:00','23:59:59', 5,2,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '05:00:00','23:59:59', 6,2,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 0,3,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 1,3,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 2,3,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 3,3,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 4,3,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','23:59:59', 5,3,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '05:00:00','23:59:59', 6,3,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 0,4,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 1,4,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 2,4,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 3,4,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 4,4,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','23:59:59', 5,4,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '05:00:00','23:59:59', 6,4,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 2,5,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 3,5,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '10:00:00','22:00:00', 4,5,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '09:00:00','22:00:00', 5,5,'2016-01-01 00:00:00');
INSERT INTO OPENING_TIME(START, END, WEEKDAY, LOCATION_ID, CREATE_DATE) VALUES ( '09:00:00','22:00:00', 6,5,'2016-01-01 00:00:00');

INSERT INTO EVENT(NAME, CREATE_DATE, POLL_END_DATE, CREATOR_IS_PREFERRED, IS_EVALUATED) VALUES('Mecces Party', '2016-01-01 00:00:00', '2022-01-20 00:00:00', false, false);
INSERT INTO EVENT(NAME, CREATE_DATE, POLL_END_DATE, CREATOR_IS_PREFERRED, IS_EVALUATED) VALUES('Corona Party', '2016-01-01 00:00:00', '2022-01-20 00:00:00', false, false);
INSERT INTO EVENT(NAME, CREATE_DATE, POLL_END_DATE, CREATOR_IS_PREFERRED, IS_EVALUATED) VALUES('KFC Party', '2016-01-01 00:00:00', '2022-01-20 00:00:00', false, false);

INSERT INTO TIMESLOT(START, END, CREATE_DATE) VALUES('2023-01-01 20:15:00', '2023-01-01 21:15:00', '2016-01-01 00:00:00');
INSERT INTO TIMESLOT(START, END, CREATE_DATE) VALUES('2023-01-02 20:15:00', '2023-01-02 21:15:00', '2016-01-01 00:00:00');
INSERT INTO TIMESLOT(START, END, CREATE_DATE) VALUES('2023-01-03 20:15:00', '2023-01-03 21:15:00', '2016-01-01 00:00:00');


INSERT INTO EVENT(NAME, CREATE_DATE, POLL_END_DATE, CREATOR_USERNAME, CREATOR_IS_PREFERRED, IS_EVALUATED) VALUES('TEST EVENT', '2022-01-15 00:00:00', '2022-01-15 12:00:00', 'user1', true, false);
INSERT INTO EVENT_PARTICIPANTS(EVENT_ID, PARTICIPANTS_USERNAME) VALUES(4, 'user1');
INSERT INTO EVENT_PARTICIPANTS(EVENT_ID, PARTICIPANTS_USERNAME) VALUES(4, 'elvis');
INSERT INTO POLL(CREATE_DATE, EVENT_ID, USER_USERNAME) VALUES('2022-01-15 00:00:00', 4, 'user1');
INSERT INTO POLL(CREATE_DATE, EVENT_ID, USER_USERNAME) VALUES('2022-01-15 00:00:00', 4, 'elvis');
INSERT INTO POLL_LOCATIONS(POINTS, LOCATION_ID, POLL_ID) VALUES(3, 1, 1);
INSERT INTO POLL_LOCATIONS(POINTS, LOCATION_ID, POLL_ID) VALUES(2, 2, 1);
INSERT INTO POLL_LOCATIONS(POINTS, LOCATION_ID, POLL_ID) VALUES(1, 3, 1);
INSERT INTO POLL_LOCATIONS(POINTS, LOCATION_ID, POLL_ID) VALUES(1, 1, 2);
INSERT INTO POLL_LOCATIONS(POINTS, LOCATION_ID, POLL_ID) VALUES(3, 2, 2);
INSERT INTO POLL_LOCATIONS(POINTS, LOCATION_ID, POLL_ID) VALUES(2, 3, 2);

INSERT INTO POLL_TIMESLOTS(POINTS, TIMESLOT_ID, POLL_ID) VALUES(1, 1, 1);
INSERT INTO POLL_TIMESLOTS(POINTS, TIMESLOT_ID, POLL_ID) VALUES(2, 2, 1);
INSERT INTO POLL_TIMESLOTS(POINTS, TIMESLOT_ID, POLL_ID) VALUES(0, 3, 1);
INSERT INTO POLL_TIMESLOTS(POINTS, TIMESLOT_ID, POLL_ID) VALUES(2, 1, 2);
INSERT INTO POLL_TIMESLOTS(POINTS, TIMESLOT_ID, POLL_ID) VALUES(1, 2, 2);
INSERT INTO POLL_TIMESLOTS(POINTS, TIMESLOT_ID, POLL_ID) VALUES(3, 3, 2);

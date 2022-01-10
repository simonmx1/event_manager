INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, ROLE, CREATE_DATE) VALUES('true', 'Admin', 'Istrator', '$2a$10$nU8E73kNkO28YwFyx2Xwee21e8KS9DhV7UCKArOa2TyLpocRho/KG', 'admin', 'admin@admin.com', 'ADMIN', '2016-01-01 00:00:00');
INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, ROLE, CREATE_DATE) VALUES('true', 'Matthias', 'Komar', '$2a$10$nU8E73kNkO28YwFyx2Xwee21e8KS9DhV7UCKArOa2TyLpocRho/KG', 'matthias', 'admin2@admin.com', 'USER', '2016-01-01 00:00:00');
INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, ROLE, CREATE_DATE) VALUES('true', 'Max', 'Mustermann', '$2a$10$nU8E73kNkO28YwFyx2Xwee21e8KS9DhV7UCKArOa2TyLpocRho/KG', 'user2', 'user@nichtadmin.com', 'LOCATION_MANAGER', '2016-01-01 00:00:00');
INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, EMAIL, ROLE, CREATE_DATE) VALUES('true', 'Elvis', 'The King', '$2a$10$nU8E73kNkO28YwFyx2Xwee21e8KS9DhV7UCKArOa2TyLpocRho/KG', 'elvis', 'elvis@elvis.com', 'USER', '2016-01-01 00:00:00');
INSERT INTO TAG (TEXT, CREATE_DATE) VALUES('CHINESE', '2016-01-01 00:00:00');
INSERT INTO TAG (TEXT, CREATE_DATE) VALUES('FAST_FOOD', '2016-01-01 00:00:00');
INSERT INTO TAG (TEXT, CREATE_DATE) VALUES('PIZZA', '2016-01-01 00:00:00');
INSERT INTO TAG (TEXT, CREATE_DATE) VALUES('DELIVERY', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'Pizzeria Mario', 'mariospizza.it', 'goo.gl/maps/j4QqpPUHtUoqqyEp9', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'McDonalds', 'mcdonalds.at', 'goo.gl/maps/tJEtHefB6FYXJkAa9', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'Burger King', 'burgerking.de', 'goo.gl/maps/CPPrigxSo9Tg8hfF7', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'KFC', 'kfc.de', 'goo.gl/maps/fQkQcnWPH2s9MiLt7', '2016-01-01 00:00:00');
INSERT INTO LOCATION (ENABLED, NAME, MENU, GEOLOCATION, CREATE_DATE) VALUES(TRUE, 'Meggima', 'meggima.eu', 'goo.gl/maps/Fspndr3WTdEJJyMH6', '2016-01-01 00:00:00');
INSERT INTO LOCATION_TAG(LOCATION_LOCATION_ID, TAGS_TEXT) VALUES(1, 'CHINESE');
INSERT INTO LOCATION_TAG(LOCATION_LOCATION_ID, TAGS_TEXT) VALUES(1, 'FAST_FOOD');
INSERT INTO LOCATION_TAG(LOCATION_LOCATION_ID, TAGS_TEXT) VALUES(2, 'PIZZA');
INSERT INTO LOCATION_TAG(LOCATION_LOCATION_ID, TAGS_TEXT) VALUES(2, 'DELIVERY');
INSERT INTO LOCATION_TAG(LOCATION_LOCATION_ID, TAGS_TEXT) VALUES(3, 'CHINESE');
INSERT INTO LOCATION_TAG(LOCATION_LOCATION_ID, TAGS_TEXT) VALUES(4, 'CHINESE');
INSERT INTO LOCATION_TAG(LOCATION_LOCATION_ID, TAGS_TEXT) VALUES(4, 'PIZZA');
INSERT INTO LOCATION_TAG(LOCATION_LOCATION_ID, TAGS_TEXT) VALUES(5, 'CHINESE');
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
CREATE KEYSPACE image_repository WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE image_repository;

CREATE TABLE image_repository.users
(
    user_id  text PRIMARY KEY,
    password text
);

CREATE TABLE image_repository.image_by_user
(
    user_id           text,
    image_id          text,
    image_is_public   boolean,
    image_title       text,
    image_description text,
    image_location    text,
    image_upload_date date,
    PRIMARY KEY (user_id, image_id)
);
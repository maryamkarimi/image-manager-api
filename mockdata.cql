USE image_repository;

INSERT INTO image_repository.users (user_id, password)
VALUES ('yams', '$2a$10$XAKoeONDveqwmeQGQ7CtmeMqMwdGet2ZyFenJbh7LJ4Eywxai1n4u'); -- yams_password

INSERT INTO image_repository.image_by_user (user_id,
                                            image_id,
                                            image_is_public,
                                            image_title,
                                            image_description,
                                            image_location,
                                            image_upload_date)
VALUES ('yams',
        'some_url',
        true,
        'yams_photo',
        'this is a beautiful photo',
        'London',
        todate(now()));

CREATE TABLE image_repository.image_by_title
(
    user_id           text,
    image_url         text,
    image_title       text,
    image_description text,
    image_location    text,
    image_upload_date date,
    image_is_public   boolean,
    PRIMARY KEY (image_title, image_url, user_id, image_is_public)
);
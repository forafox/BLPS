CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    fullName TEXT,
    password TEXT        NOT NULL,
    role     TEXT        NOT NULL
);
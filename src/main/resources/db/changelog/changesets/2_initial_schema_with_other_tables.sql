DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    name     TEXT NOT NULL,
    surname  TEXT NOT NULL,
    email    TEXT,
    phone    TEXT,
    role     TEXT NOT NULL
);


CREATE TABLE accommodations
(
    id          SERIAL PRIMARY KEY,
    country     TEXT    NOT NULL,
    city        TEXT    NOT NULL,
    address     TEXT    NOT NULL,
    price       INTEGER NOT NULL,
    description TEXT    NOT NULL,
    owner_id    INTEGER REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE bookings
(
    id               SERIAL PRIMARY KEY,
    arrival_day      DATE    NOT NULL,
    departure_day    DATE    NOT NULL,
    guest_count      INTEGER NOT NULL,
    price            INTEGER NOT NULL,
    quest_id         INTEGER REFERENCES users (id) ON DELETE CASCADE,
    accommodation_id INTEGER REFERENCES accommodations (id) ON DELETE CASCADE
);


CREATE TABLE accommodation_raitings
(
    id                 SERIAL PRIMARY KEY,
    overall_impression INTEGER NOT NULL,
    putiry             INTEGER NOT NULL,
    accuracy           INTEGER NOT NULL,
    arrival            INTEGER NOT NULL,
    communication      INTEGER NOT NULL,
    location           INTEGER NOT NULL,
    price_quality      INTEGER NOT NULL,
    conveniences       INTEGER NOT NULL,
    feedback           TEXT    NOT NULL,
    day                DATE    NOT NULL,
    relevance          BOOL    NOT NULL,
    accommodation_id   INTEGER REFERENCES accommodations (id) ON DELETE CASCADE,
    booking_id         INTEGER REFERENCES bookings (id) ON DELETE CASCADE
);


CREATE TABLE guest_ratings
(
    id         SERIAL PRIMARY KEY,
    rating     INTEGER NOT NULL,
    feedback   TEXT    NOT NULL,
    day        DATE    NOT NULL,
    relevance  BOOL    NOT NULL,
    quest_id   INTEGER REFERENCES users (id) ON DELETE CASCADE,
    booking_id INTEGER REFERENCES bookings (id) ON DELETE CASCADE
);
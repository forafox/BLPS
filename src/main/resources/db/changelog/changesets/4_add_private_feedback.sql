CREATE TABLE private_feedback
(
    id                 SERIAL PRIMARY KEY,
    feedback           TEXT    NOT NULL,
    day                DATE    NOT NULL,
    owner_id           INTEGER REFERENCES users (id) ON DELETE CASCADE,
    booking_id         INTEGER REFERENCES bookings (id) ON DELETE CASCADE
);
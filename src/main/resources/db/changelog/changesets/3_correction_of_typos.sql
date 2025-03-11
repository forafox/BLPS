ALTER TABLE accommodation_raitings RENAME COLUMN putiry TO purity;
ALTER TABLE accommodation_raitings RENAME TO accommodation_ratings;

ALTER TABLE bookings RENAME COLUMN quest_id TO guest_id;
ALTER TABLE guest_ratings RENAME COLUMN quest_id TO guest_id;

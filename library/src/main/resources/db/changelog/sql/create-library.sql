CREATE TABLE library (
    id serial PRIMARY KEY NOT NULL,
    book_id INTEGER NOT NULL,
    borrow_time TIMESTAMP,
    return_time TIMESTAMP
);
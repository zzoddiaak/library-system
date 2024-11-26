CREATE TABLE books (
    id serial PRIMARY KEY NOT NULL,
    isbn BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    description TEXT,
    author VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users(
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar,
    email varchar UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS requests(
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description varchar(200),
    requester_id integer REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items(
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar UNIQUE NOT NULL,
    description varchar(200) UNIQUE NOT NULL,
    is_available boolean UNIQUE NOT NULL,
    owner_id integer REFERENCES users (id),
    request_id integer REFERENCES requests (id)
);

create type request_status as enum ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS bookings(
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id integer REFERENCES items (id),
    booker_id integer REFERENCES users (id),
    status request_status
);

CREATE TABLE IF NOT EXISTS comments(
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text varchar(200),
    item_id integer REFERENCES items (id),
    author_id integer REFERENCES users (id)
);
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilation_event CASCADE;
DROP TABLE IF EXISTS rates CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name           VARCHAR(120) NOT NULL,
    email               VARCHAR(120) NOT NULL UNIQUE,
    user_rate           DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    category_name       VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events
(
    event_id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_title         VARCHAR(120) NOT NULL UNIQUE,
    event_annotation    VARCHAR(2000) NOT NULL,
    event_description   VARCHAR(7000) NOT NULL,
    category_id         BIGINT NOT NULL REFERENCES categories (category_id),
    event_date          TIMESTAMP NOT NULL,
    initiator_id        BIGINT NOT NULL REFERENCES users (user_id),
    location_lat        FLOAT NOT NULL,
    location_lon        FLOAT NOT NULL,
    is_paid             BOOLEAN NOT NULL,
    participant_limit   BIGINT NOT NULL,
    created_on          TIMESTAMP NOT NULL,
    published_on        TIMESTAMP,
    request_moderation  BOOLEAN DEFAULT TRUE,
    event_views         BIGINT DEFAULT 0,
    confirmed_requests  BIGINT DEFAULT 0,
    event_state         VARCHAR(10) NOT NULL,
    event_rate          DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    requester_id        BIGINT NOT NULL REFERENCES users (user_id) ,
    event_id            BIGINT NOT NULL REFERENCES events (event_id),
    created             TIMESTAMP NOT NULL,
    request_status      VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned              BOOLEAN NOT NULL,
    compilation_title   VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilation_event
(
    compilation_id      BIGINT NOT NULL REFERENCES compilations (compilation_id) ON DELETE CASCADE,
    event_id            BIGINT NOT NULL REFERENCES events (event_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rates
(
    rate_id             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id             BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    event_id            BIGINT NOT NULL REFERENCES events (event_id) ON DELETE CASCADE,
    event_rate          INT NOT NULL,

    CONSTRAINT valid_rate_not_more CHECK (event_rate <= 5),
    CONSTRAINT valid_rate_not_less CHECK (event_rate >= 1)
);
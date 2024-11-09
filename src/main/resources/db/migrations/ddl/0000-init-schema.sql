create schema ubiquiti;

CREATE TABLE ubiquiti.user
(
    id         bigserial PRIMARY KEY,
    username   varchar NOT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP
);
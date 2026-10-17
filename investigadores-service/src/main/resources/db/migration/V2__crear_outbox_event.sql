CREATE TABLE outbox_event (
    id        BIGSERIAL PRIMARY KEY,
    tipo      VARCHAR(100) NOT NULL,
    payload   TEXT NOT NULL,
    publicado BOOLEAN NOT NULL DEFAULT FALSE
);
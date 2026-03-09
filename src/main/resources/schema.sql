CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ  NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes        BYTEA        NOT NULL
);

CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ         NOT NULL,
    updated_at TIMESTAMPTZ,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(60)         NOT NULL,
    profile_id UUID UNIQUE,
    FOREIGN KEY (profile_id) REFERENCES binary_contents (id)
        ON DELETE SET NULL
);

CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ,
    user_id        UUID        NOT NULL,
    last_active_at TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TYPE channel_type AS ENUM ('PUBLIC', 'PRIVATE');

CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        channel_type NOT NULL
);

CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ,
    user_id      UUID        NOT NULL UNIQUE,
    channel_id   UUID        NOT NULL UNIQUE,
    last_read_at TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels (id)
        ON DELETE CASCADE
);



CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    content    TEXT,
    channel_id UUID        NOT NULL,
    author_id  UUID,
    FOREIGN KEY (channel_id) REFERENCES channels (id)
        ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users (id)
        ON DELETE SET NULL
);

CREATE TABLE message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages (id)
        ON DELETE CASCADE,
    FOREIGN KEY (attachment_id) REFERENCES binary_contents (id)
        ON DELETE CASCADE
);

DELETE
FROM binary_contents;
DELETE
FROM channels;
DELETE
FROM message_attachments;
DELETE
FROM messages;
DELETE
FROM read_statuses;
DELETE
FROM user_statuses;
DELETE
FROM users;

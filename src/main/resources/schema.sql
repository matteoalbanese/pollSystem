


CREATE TABLE IF NOT EXISTS user_account(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS poll(
    id SERIAL PRIMARY KEY,
    status VARCHAR(255),
    question VARCHAR(255),
    expiration_date VARCHAR(255),
    owner_id BIGINT,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES user_account(id)
);


CREATE TABLE IF NOT EXISTS poll_option(
    id SERIAL PRIMARY KEY,
    poll_id BIGINT,
    CONSTRAINT fk_poll FOREIGN KEY (poll_id) REFERENCES poll(id),
    option_name VARCHAR(255),
    created_at VARCHAR(255),
    winner BOOLEAN DEFAULT FALSE,
    percentage DOUBLE PRECISION


    );

CREATE TABLE IF NOT EXISTS vote(

    id SERIAL PRIMARY KEY,
    user_id BIGINT,
    option_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_account(id),
    CONSTRAINT fk_option FOREIGN KEY (option_id) REFERENCES poll_option(id),
    votedAt VARCHAR(255)
);



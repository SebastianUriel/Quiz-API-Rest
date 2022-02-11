CREATE DATABASE db_quiz;
USE db_quiz;

CREATE TABLE quiz (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(25) NOT NULL,
    description VARCHAR(225) NOT NULL,
    create_at DATETIME NOT NULL,
    last_update DATETIME NOT NULL,
    available BOOLEAN NOT NULL DEFAULT 1
);

CREATE TABLE question (
	quiz_id BIGINT NOT NULL,
    number BIGINT NOT NULL,
	description VARCHAR(255) NOT NULL,
    type VARCHAR(30) ,
    available BOOLEAN NOT NULL DEFAULT 1,
    CHECK (type = 'OPEN' OR type = 'MULTIPLE_OPTION' OR type = 'ONE_OPTION'),
    PRIMARY KEY (quiz_id, number),
    CONSTRAINT fk_quiz_id FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

CREATE TABLE qn_option (
	quiz_id BIGINT NOT NULL,
    number_question BIGINT NOT NULL,
    number BIGINT NOT NULL,
	description VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT 1,
    PRIMARY KEY (quiz_id, number_question, number),
    CONSTRAINT fk_quiz_question FOREIGN KEY (quiz_id, number_question) REFERENCES question(quiz_id, number)
);
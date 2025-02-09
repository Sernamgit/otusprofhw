CREATE TABLE users (
id bigserial PRIMARY KEY,
login VARCHAR(255),
password VARCHAR(255),
nickname VARCHAR(255)
);

CREATE TABLE testTable2 (id bigserial PRIMARY KEY, testField VARCHAR(255));
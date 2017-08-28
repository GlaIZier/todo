-- docker pull postgres:9.6
-- docker run --rm -p 5432:5432 -v $PWD/postgresql:/docker-entrypoint-initdb.d --name postgres postgres:9.6


-- # Step 1 Creating database --------------------------

-- -----------------------------------------------------
-- Database
-- -----------------------------------------------------
DROP DATABASE IF EXISTS tododb;
DROP ROLE IF EXISTS todoer;

CREATE ROLE todoer WITH PASSWORD 'password' LOGIN;

CREATE DATABASE tododb
  WITH OWNER todoer
  ENCODING UTF8
  LC_CTYPE 'en_US.utf8'
  LC_COLLATE 'en_US.utf8';

-- -----------------------------------------------------
-- Connect to created database as created user
-- -----------------------------------------------------
\connect tododb todoer

-- -----------------------------------------------------
-- Schema
-- -----------------------------------------------------
CREATE SCHEMA todo;
ALTER ROLE todoer SET search_path TO "$user", public, todo; -- need to reconnect
SET search_path TO "$user", public, todo; -- for current session

CREATE TABLE IF NOT EXISTS todo.User (
  login     text PRIMARY KEY NOT NULL,
  password   text NOT NULL
);

CREATE TABLE todo.Role (
  role text PRIMARY KEY NOT NULL
);

CREATE TABLE todo.Authorization (
  login text NOT NULL,
  role  text NOT NULL,
  PRIMARY KEY (login, role),
  CONSTRAINT fk_authorization_user
    FOREIGN KEY (login)
    REFERENCES todo.User (login)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_authorization_role
    FOREIGN KEY (role)
    REFERENCES todo.Role (role)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE todo.Task (
  id serial PRIMARY KEY,
  login text NOT NULL,
  todo text NOT NULL,
  CONSTRAINT fk_task_user
    FOREIGN KEY (login)
    REFERENCES todo.User (login)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

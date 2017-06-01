CREATE TABLE User (
  Login    VARCHAR(50) PRIMARY KEY,
  Password VARCHAR(30)
);

CREATE TABLE Role (
  Role VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Authorization (
  Id    INTEGER IDENTITY PRIMARY KEY,
  Login VARCHAR(50),
  Role  VARCHAR(50)
);

ALTER TABLE Authorization
  ADD FOREIGN KEY (Login) REFERENCES user (Login)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE Authorization
  ADD FOREIGN KEY (Role) REFERENCES role (Role)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

CREATE TABLE Task (
  Login VARCHAR(50),
  Id    INTEGER IDENTITY,
  Todo  VARCHAR(255),
  PRIMARY KEY (Login, Id)
);

ALTER TABLE Task
  ADD FOREIGN KEY (Login) REFERENCES user (Login)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
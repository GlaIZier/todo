CREATE SCHEMA todo AUTHORIZATION DBA;

CREATE TABLE todo.User (
  login    VARCHAR(50) PRIMARY KEY NOT NULL,
  password VARCHAR(100)            NOT NULL
);

CREATE TABLE todo.Role (
  role VARCHAR(50) PRIMARY KEY NOT NULL
);

CREATE TABLE todo.Authorization (
  login VARCHAR(50) NOT NULL,
  role  VARCHAR(50) NOT NULL,
  PRIMARY KEY (login, role)
);

ALTER TABLE todo.Authorization
  ADD FOREIGN KEY (login) REFERENCES todo.User (login)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE todo.Authorization
  ADD FOREIGN KEY (role) REFERENCES todo.Role (role)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

CREATE TABLE todo.Task (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY ( START WITH 1,
  INCREMENT BY 1
),
  login VARCHAR (50
) NOT NULL,
  todo VARCHAR (255
) NOT NULL,
PRIMARY KEY (id
)
);

ALTER TABLE todo.Task
  ADD FOREIGN KEY (login) REFERENCES todo.User (login)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
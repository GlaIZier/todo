INSERT INTO user (login, password)
VALUES
  ('u', 'p'),
  ('a', 'p');

INSERT INTO role (role)
VALUES
  ('USER'),
  ('ADMIN');

INSERT INTO authorization (login, role)
VALUES
  ('u', 'USER'),
  ('a', 'USER'),
  ('a', 'ADMIN');

INSERT INTO task (login, todo)
VALUES
  ('u', 'todo1'),
  ('u', 'todo2'),
  ('a', 'todo1');

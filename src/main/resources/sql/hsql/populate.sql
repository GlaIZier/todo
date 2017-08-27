INSERT INTO User (login, password)
VALUES
  ('u', 'p'),
  ('a', 'p');

INSERT INTO Role (role)
VALUES
  ('ROLE_USER'),
  ('ROLE_ADMIN');

INSERT INTO Authorization (login, role)
VALUES
  ('u', 'ROLE_USER'),
  ('a', 'ROLE_USER'),
  ('a', 'ROLE_ADMIN');

INSERT INTO Task (login, todo)
VALUES
  ('u', 'todo1'),
  ('u', 'todo2'),
  ('a', 'todo1');

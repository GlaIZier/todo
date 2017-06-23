INSERT INTO User (login, password)
VALUES
  ('u', 'p'),
  ('a', 'p');

INSERT INTO Role (role)
VALUES
  ('USER'),
  ('ADMIN');

INSERT INTO Authorization (login, role)
VALUES
  ('u', 'USER'),
  ('a', 'USER'),
  ('a', 'ADMIN');

INSERT INTO Task (login, todo)
VALUES
  ('u', 'todo1'),
  ('u', 'todo2'),
  ('a', 'todo1');

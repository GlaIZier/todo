INSERT INTO User (Login, Password)
VALUES
  ('u', 'p'),
  ('a', 'p');

INSERT INTO Role (Role)
VALUES
  ('USER'),
  ('ADMIN');

INSERT INTO Authorization (Login, Role)
VALUES
  ('u', 'USER'),
  ('a', 'USER'),
  ('a', 'ADMIN');

INSERT INTO Task (Login, Todo)
VALUES
  ('u', 'todo1'),
  ('u', 'todo2'),
  ('a', 'todo1');

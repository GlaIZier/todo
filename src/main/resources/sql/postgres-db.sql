-- # Step 1 Creating database --------------------------

-- -----------------------------------------------------
-- Database powerliftersdb and role to control
-- -----------------------------------------------------
DROP DATABASE IF EXISTS tododb;
DROP ROLE IF EXISTS todo;

CREATE ROLE todo WITH PASSWORD 'password' LOGIN;

CREATE DATABASE tododb
  WITH OWNER todo
  ENCODING UTF8
  LC_CTYPE 'en_US.utf8'
  LC_COLLATE 'en_US.utf8';

-- -----------------------------------------------------
-- Connect to created database as created user
-- -----------------------------------------------------
\connect todo todo

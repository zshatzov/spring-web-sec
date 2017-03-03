-- SECURITY: Users and Roles mapping
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
   "username" varchar(20) NOT NULL,
   "password" varchar(20) NOT NULL,
   enabled boolean NOT NULL DEFAULT FALSE,
   primary key("username")
);

create table user_roles (
  user_role_id SERIAL PRIMARY KEY,
  "username" varchar(20) NOT NULL,
  "role" varchar(20) NOT NULL,
  UNIQUE ("username",role),
  FOREIGN KEY ("username") REFERENCES users ("username")
);

INSERT INTO users(username,password,enabled) VALUES ('jack','jack', true);
INSERT INTO users(username,password,enabled) VALUES ('peter','peter', true);
 
INSERT INTO user_roles (username, role) VALUES ('jack', 'ROLE_USER');
INSERT INTO user_roles (username, role) VALUES ('jack', 'ROLE_ADMIN');
INSERT INTO user_roles (username, role) VALUES ('peter', 'ROLE_USER');
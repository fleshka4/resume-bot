CREATE TABLE hh_tokens (
  hh_tokens_id serial PRIMARY KEY,
  access_token varchar,
  expires_in integer,
  refresh_token varchar,
  user_id integer UNIQUE
);

CREATE TABLE users (
  tg_uid integer PRIMARY KEY
);

CREATE TABLE resumes (
  resume_id serial PRIMARY KEY,
  resume_data text,
  pdf_path text,
  link text,
  title varchar unique,
  user_id integer,
  template_id integer
);

comment on column resumes.hh_link is 'link to hh resume';

CREATE TABLE templates (
  template_id serial PRIMARY KEY,
  image_path text,
  source_path text
);

ALTER TABLE hh_tokens ADD FOREIGN KEY (user_id) REFERENCES users (tg_uid);

ALTER TABLE resumes ADD FOREIGN KEY (user_id) REFERENCES users (tg_uid);

ALTER TABLE resumes ADD FOREIGN KEY (template_id) REFERENCES templates (template_id);

DROP TABLE event_entity IF EXISTS ;
CREATE TABLE event_entity (
  id bigint primary key,
  details varchar(255),
  end_time time,
  event_date date,
  start_time time,
  title varchar(255)
);
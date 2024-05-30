create table images (
    id serial primary key,
    name varchar(255),
    content BLOB
);

alter table posts add column image_id bigint;
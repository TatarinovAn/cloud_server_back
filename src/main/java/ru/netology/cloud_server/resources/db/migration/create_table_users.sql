create table users
(
    id       serial primary key,
    username varchar not null,
    password varchar not null
);

insert into users (id, username, password)
values (1, 'Anton', 'Password123');

insert into users (id, username, password)
values (2, 'Maria12', 'Agent');
create table files
(
    id        serial IDENTITY primary key not null,
    date      timestamp not null,
    file_name varchar   not null,
    file_size long      not null,
    file      bytea     not null,
    id_users  integer references users (id)
        unique (file_name, id_users)
);
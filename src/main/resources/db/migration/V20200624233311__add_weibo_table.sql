create table weibo
(
    id           varchar(36) not null primary key,
    json_content json        not null
);
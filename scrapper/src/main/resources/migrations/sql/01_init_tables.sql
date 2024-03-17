create table if not exists link
(
    id              bigserial primary key,
    url             text unique not null,
    last_check_at   timestamp with time zone not null default now(),
    updated_at      timestamp with time zone not null
);

create table if not exists chat
(
    id              bigint primary key,
    created_at      timestamp with time zone not null default now()
);

create table if not exists subscription
(
    link_id         bigint references link ON DELETE CASCADE,
    chat_id         bigint references chat ON DELETE CASCADE,
    created_at      timestamp with time zone not null default now(),
    primary key (link_id, chat_id)
);

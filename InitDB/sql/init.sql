create schema if not exists aggregator;

create table if not exists aggregator.role
(
    id   bigserial not null
        constraint role_pk
            primary key,
    role varchar   not null
);

alter table aggregator.role
    owner to pd_aggregator;

create unique index if not exists role_id_uindex
    on aggregator.role (id);

create unique index if not exists role_role_uindex
    on aggregator.role (role);


create table if not exists aggregator.users
(
    id          bigserial            not null
        constraint users_pk
            primary key,
    first_name  varchar              not null,
    second_name varchar              not null,
    email       varchar              not null,
    password    varchar              not null,
    role_id     bigint               not null
        constraint users_role_id_fk
            references aggregator.role,
    active      boolean default true not null,
    login       varchar              not null
);

alter table aggregator.users
    owner to pd_aggregator;

create unique index if not exists users_id_uindex
    on aggregator.users (id);


create table if not exists aggregator.company
(
    id          bigserial not null
        constraint company_pk
            primary key,
    name        varchar   not null,
    city        varchar   not null,
    description varchar
);

alter table aggregator.company
    owner to pd_aggregator;

create unique index if not exists company_id_uindex
    on aggregator.company (id);


create table if not exists aggregator.address
(
    id         bigserial not null
        constraint address_pk
            primary key,
    city       varchar   not null,
    address    varchar   not null,
    company_id bigint
        constraint address_company_id_fk
            references aggregator.company
);

alter table aggregator.address
    owner to pd_aggregator;

create unique index if not exists address_id_uindex
    on aggregator.address (id);


create table if not exists aggregator.user_role
(
    user_id bigint not null
        constraint user_role_user_id_fk
            references aggregator.users,
    role_id bigint not null
        constraint user_role_pk
            primary key
        constraint user_role_role_id_fk
            references aggregator.role
);

alter table aggregator.user_role
    owner to pd_aggregator;


create table if not exists aggregator.config
(
    id               bigserial not null
        constraint config_pk
            primary key,
    scanner_page_num integer   not null
);

alter table aggregator.config
    owner to pd_aggregator;

create unique index config_id_uindex
    on aggregator.config (id);

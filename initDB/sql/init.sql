create schema if not exists aggregator;

create table if not exists aggregator.roles
(
    id    bigserial not null
        constraint roles_pk
            primary key,
    roles varchar   not null
);

alter table aggregator.roles
    owner to pd_aggregator;

create unique index if not exists roles_id_uindex
    on aggregator.roles (id);

create unique index if not exists roles_roles_uindex
    on aggregator.roles (roles);


create table if not exists aggregator.users
(
    id          bigserial            not null
        constraint users_pk
            primary key,
    first_name  varchar              not null,
    second_name varchar              not null,
    email       varchar              not null,
    password    varchar              not null,
    roles_id    bigint               not null
        constraint users_roles_id_fk
            references aggregator.roles,
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
    description varchar,
    active      boolean default true not null
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

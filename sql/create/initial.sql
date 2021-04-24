create schema if not exists aggregator;

create table aggregator.company
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

create unique index company_id_uindex
    on aggregator.company (id);

create table aggregator.address
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

create unique index address_id_uindex
    on aggregator.address (id);

create table aggregator.role
(
    id   bigserial not null
        constraint role_pk
            primary key,
    role varchar   not null
);

alter table aggregator.role
    owner to pd_aggregator;

create unique index role_id_uindex
    on aggregator.role (id);

create unique index role_role_uindex
    on aggregator.role (role);

create table aggregator.users
(
    id          bigserial not null
        constraint users_pk
            primary key,
    name        varchar   not null,
    second_name varchar   not null,
    email       varchar   not null,
    password    varchar   not null,
    role_id     bigint
        constraint users_role_id_fk
            references aggregator.role
);

alter table aggregator.users
    owner to pd_aggregator;

create unique index users_email_uindex
    on aggregator.users (email);

create unique index users_id_uindex
    on aggregator.users (id);


create table aggregator.user_role
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

insert into aggregator.user_role (user_id, role_id) VALUES (1, 1);
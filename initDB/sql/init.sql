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

create table aggregator.scanner_configs
(
    id      bigint                not null
        constraint scanner_configs_pk
            primary key,
    name    varchar(48)           not null,
    url     varchar(100)          not null,
    user_id bigint                not null
        constraint scanner_configs_user_id_fk
            references aggregator.users,
    active  boolean default false not null
);

comment on table aggregator.scanner_configs is 'Configuration of scanner';

comment on column aggregator.scanner_configs.id is 'Index of scanner onfig';

comment on column aggregator.scanner_configs.name is 'Name of scanner';

comment on column aggregator.scanner_configs.url is 'Basic path to search page';

comment on column aggregator.scanner_configs.active is 'The status of scanner config';

alter table aggregator.scanner_configs
    owner to pd_aggregator;

create table aggregator.scanner_steps
(
    id                 bigint                not null
        constraint scanner_steps_pk
            primary key,
    tag                varchar(48)           not null,
    type               varchar(48)           not null,
    action             varchar(48)           not null,
    active             boolean default false not null,
    scanner_configs_id bigint                not null
        constraint scanner_steps_scanner_configs_id_fk
            references aggregator.scanner_configs
);

comment on table aggregator.scanner_steps is 'Steps of queue of tags to find out the data';

comment on column aggregator.scanner_steps.id is 'Index of step';

comment on column aggregator.scanner_steps.tag is 'The name of tag';

comment on column aggregator.scanner_steps.type is 'The type of tag';

comment on column aggregator.scanner_steps.action is 'The action of tag';

comment on column aggregator.scanner_steps.active is 'Status of step';

alter table aggregator.scanner_steps
    owner to pd_aggregator;



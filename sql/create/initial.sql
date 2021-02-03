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


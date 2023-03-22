create sequence jpa_sequence start with 100 increment by 1;
create sequence postcard_seq start with 100 increment by 50;
create sequence postcard_image_seq start with 100 increment by 50;

create table postcard
(
    id          bigint primary key,
    vendor_code varchar(255),
    name        varchar(255) not null,
    author      varchar(255),
    brand       varchar(255),
    description varchar(255),
    image       varchar(255),
    category    varchar(255),
    tags        varchar(255),
    create_date timestamp with time zone
);

create table postcard_image
(
    id bigint primary key,
    name varchar(255),
    file bytea not null
);

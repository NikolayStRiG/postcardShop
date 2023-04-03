create sequence jpa_sequence start with 100 increment by 1;
create sequence product_seq start with 100 increment by 50;
create sequence product_image_seq start with 100 increment by 50;

create table product
(
    id          bigint primary key,
    vendor_code varchar(255),
    name        varchar(255) not null,
    type        varchar(50) not null,
    author      varchar(255),
    brand       varchar(255),
    description varchar(255),
    image       varchar(255),
    category    varchar(255),
    tags        varchar(255),
    create_date timestamp with time zone,
    price int
);

create table product_image
(
    id bigint primary key,
    name varchar(255),
    file bytea not null
);

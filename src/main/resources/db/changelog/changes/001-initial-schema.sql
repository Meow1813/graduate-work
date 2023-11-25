create table if not exists users (
    id bigserial not null primary key,
    username varchar(255) not null,
    password varchar(255) not null,
    first_name varchar(255),
    last_name varchar(255),
    phone varchar(255),
    role varchar(255),
    image varchar(255)
);

create table if not exists ads(
    pk bigserial not null primary key,
    description text,
    image varchar(255),
    price int not null,
    title varchar(255),
    author_id bigint
);

create table if not exists comments(
    pk serial not null primary key,
    text varchar(255),
    created_at timestamp,
    ad_id bigint,
    author_id bigint
);

alter table ads
    add constraint ads_user_fk foreign key(author_id) references users(id);

alter table comments
    add constraint comments_ads_fk foreign key(ad_id) references ads(pk);

alter table comments
    add constraint comments_users_fk foreign key(author_id) references users(id);

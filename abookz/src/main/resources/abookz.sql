create table if not exists book
(
    item_page     int                                                                                                                                                                                                                                     not null,
    pub_date      date                                                                                                                                                                                                                                    null,
    book_id       bigint auto_increment
        primary key,
    isbn          bigint                                                                                                                                                                                                                                  null,
    isbn13        bigint                                                                                                                                                                                                                                  null,
    author        varchar(255)                                                                                                                                                                                                                            null,
    cover         varchar(255)                                                                                                                                                                                                                            null,
    description   varchar(255)                                                                                                                                                                                                                            null,
    link          varchar(255)                                                                                                                                                                                                                            null,
    publisher     varchar(255)                                                                                                                                                                                                                            null,
    title         varchar(255)                                                                                                                                                                                                                            null,
    category_name enum ('HUMANITIES', 'SOCIAL_SCIENCE', 'HISTORY_CULTURE', 'TRAVEL', 'YOUTH', 'FOREIGN_LANGUAGE', 'HEALTH_RELIGION', 'SCIENCE_ENGINEERING', 'COMPUTER', 'CERTIFICATION', 'NOVEL', 'MANAGEMENT_ECONOMY', 'SELF_IMPROVEMENT', 'LITERATURE') null
);

create table if not exists member
(
    mem_id   bigint auto_increment
        primary key,
    reg_date datetime(6)                                      null,
    email    varchar(255)                                     null,
    login_id varchar(255)                                     null,
    nickname varchar(255)                                     null,
    password varchar(255)                                     null,
    profile  varchar(255)                                     null,
    role     enum ('Role_Admin', 'Role_Manager', 'Role_User') null
);

create table if not exists bookshelf
(
    add_date     datetime(6)                                        null,
    book_id      bigint                                             null,
    bookshelf_id bigint auto_increment
        primary key,
    end_date     datetime(6)                                        null,
    mem_id       bigint                                             null,
    start_date   datetime(6)                                        null,
    tag          enum ('READ', 'WANT_TO_READ', 'CURRENTLY_READING') null,
    constraint FKg9fjuh3yp4mdxxp6dga60gorv
        foreign key (book_id) references book (book_id),
    constraint FKqbx54c9rstfd0hjiw96dnl702
        foreign key (mem_id) references member (mem_id)
);

create table if not exists memo
(
    page         int          not null,
    book_id      bigint       null,
    created_date datetime(6)  null,
    mem_id       bigint       null,
    memo_id      bigint auto_increment
        primary key,
    note         varchar(255) null,
    quotes       varchar(255) null,
    constraint FK5khqn2sk6xa9wbvcl0ayhlnk6
        foreign key (book_id) references book (book_id),
    constraint FKejk89ku44jw6hj42bsyi11qft
        foreign key (mem_id) references member (mem_id)
);

create table if not exists review
(
    grade             int          not null,
    is_spoiler_active bit          null,
    book_id           bigint       null,
    created_date      datetime(6)  null,
    mem_id            bigint       null,
    review_id         bigint auto_increment
        primary key,
    content           varchar(255) null,
    constraint FK3sxttditmpbkx7f2ynmm2ugp4
        foreign key (mem_id) references member (mem_id),
    constraint FK70yrt09r4r54tcgkrwbeqenbs
        foreign key (book_id) references book (book_id)
);

create table if not exists comment
(
    comment_id   bigint auto_increment
        primary key,
    created_date datetime(6)  null,
    mem_id       bigint       null,
    review_id    bigint       null,
    comment      varchar(255) null,
    constraint FKikbl30677l3k4j937v9vqwobj
        foreign key (mem_id) references member (mem_id),
    constraint FKnf4ni761w29tmtgdxymmgvg8r
        foreign key (review_id) references review (review_id)
);

create table if not exists review_like
(
    like_id   bigint auto_increment
        primary key,
    mem_id    bigint null,
    review_id bigint null,
    constraint FK68am9vk1s1e8n1v873meqkk0k
        foreign key (review_id) references review (review_id),
    constraint FK9rb4u4xeglglq7eft2tyi7wpd
        foreign key (mem_id) references member (mem_id)
);


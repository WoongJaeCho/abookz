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
    constraint BOOKSHELF_IBFK_1
        foreign key (mem_id) references member (mem_id),
    constraint BOOKSHELF_IBFK_2
        foreign key (book_id) references book (book_id)
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
    constraint MEMO_IBFK_1
        foreign key (mem_id) references member (mem_id),
    constraint MEMO_IBFK_2
        foreign key (book_id) references book (book_id)
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
    constraint REVIEW_IBFK_1
        foreign key (mem_id) references member (mem_id),
    constraint REVIEW_IBFK_2
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
    constraint COMMENT_IBFK_1
        foreign key (mem_id) references member (mem_id),
    constraint COMMENT_IBFK_2
        foreign key (review_id) references review (review_id)
);

create table if not exists review_like
(
    like_id   bigint auto_increment
        primary key,
    mem_id    bigint null,
    review_id bigint null,
    constraint LIKE_IBFK_1
        foreign key (mem_id) references member (mem_id),
    constraint LIKE_IBFK_2
        foreign key (review_id) references review (review_id)
);
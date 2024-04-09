create table book
(
    book_id       bigint auto_increment
        primary key,
    isbn13        bigint                                                                                                                                                                                                                                  null,
    aladin_grade  bigint                                                                                                                                                                                                                                  null,
    author        varchar(255)                                                                                                                                                                                                                            null,
    category_name enum ('HUMANITIES', 'SOCIAL_SCIENCE', 'HISTORY_CULTURE', 'TRAVEL', 'YOUTH', 'FOREIGN_LANGUAGE', 'HEALTH_RELIGION', 'SCIENCE_ENGINEERING', 'COMPUTER', 'CERTIFICATION', 'NOVEL', 'MANAGEMENT_ECONOMY', 'SELF_IMPROVEMENT', 'LITERATURE') null,
    cover         varchar(255)                                                                                                                                                                                                                            null,
    description   varchar(255)                                                                                                                                                                                                                            null,
    item_page     int                                                                                                                                                                                                                                     not null,
    link          varchar(255)                                                                                                                                                                                                                            null,
    price         int                                                                                                                                                                                                                                     not null,
    pub_date      date                                                                                                                                                                                                                                    null,
    publisher     varchar(255)                                                                                                                                                                                                                            null,
    size_depth    int                                                                                                                                                                                                                                     not null,
    title         varchar(255)                                                                                                                                                                                                                            null,
    weight        int                                                                                                                                                                                                                                     not null
);

create table member
(
    mem_id      bigint auto_increment
        primary key,
    email       varchar(255)                                     null,
    login_id    varchar(255)                                     null,
    name        varchar(255)                                     null,
    password    varchar(255)                                     null,
    profile     varchar(255)                                     null,
    provider    varchar(255)                                     null,
    provider_id varchar(255)                                     null,
    reg_date    date                                             null,
    role        enum ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER') null
);

create table bookshelf
(
    bookshelf_id     bigint auto_increment
        primary key,
    add_date         datetime(6)                                        null,
    book_shelf_grade int                                                not null,
    current_page     int                                                not null,
    daily_page       int                                                not null,
    end_date         datetime(6)                                        null,
    start_date       datetime(6)                                        null,
    tag              enum ('READ', 'WANT_TO_READ', 'CURRENTLY_READING') null,
    target_date      datetime(6)                                        null,
    book_id          bigint                                             null,
    mem_id           bigint                                             null,
    constraint BOOKSHELF_IBFK_1
        foreign key (mem_id) references member (mem_id),
    constraint BOOKSHELF_IBFK_2
        foreign key (book_id) references book (book_id)
);

create table memo
(
    memo_id      bigint auto_increment
        primary key,
    created_date datetime(6)  null,
    note         varchar(255) null,
    page         int          not null,
    quotes       varchar(255) null,
    bookshelf_id bigint       null,
    constraint MEMO_IBFK_1
        foreign key (bookshelf_id) references bookshelf (bookshelf_id)
);

create table review
(
    review_id         bigint auto_increment
        primary key,
    content           varchar(255) null,
    created_date      datetime(6)  null,
    is_spoiler_active bit          null,
    review_grade      int          not null,
    book_id           bigint       null,
    mem_id            bigint       null,
    constraint REVIEW_IBFK_1
        foreign key (mem_id) references member (mem_id),
    constraint REVIEW_IBFK_2
        foreign key (book_id) references book (book_id)
);

create table comment
(
    comment_id   bigint auto_increment
        primary key,
    comment      varchar(255) null,
    created_date datetime(6)  null,
    mem_id       bigint       null,
    review_id    bigint       null,
    constraint COMMENT_IBFK_1
        foreign key (mem_id) references member (mem_id),
    constraint COMMENT_IBFK_2
        foreign key (review_id) references review (review_id)
);

create table review_like
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

create table slide_card
(
    id              bigint auto_increment
        primary key,
    isbn13          bigint       null,
    add_date        date         null,
    idx             int          not null,
    slide_file_name varchar(255) null
);


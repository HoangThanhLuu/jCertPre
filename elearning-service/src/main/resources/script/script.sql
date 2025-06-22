drop database if exists `course_app`;
create database if not exists `course_app`;
use `course_app`;

drop table if exists `enrollments`;
drop table if exists `results_detail`;
drop table if exists `results`;
drop table if exists `questions`;
drop table if exists `quizzes`;
drop table if exists `media`;
drop table if exists `lessons`;
drop table if exists `courses`;
drop table if exists `levels`;
drop table if exists `users`;


create table if not exists `users`
(
    `user_id`    int auto_increment,
    `email`      varchar(255) unique not null,
    `password`   varchar(255)        not null,
    `first_name` varchar(255),
    `last_name`  varchar(255),
    `gender`     enum ('male', 'female'),
    `phone`      varchar(20),
    `address`    text,
    `dob`        date,
    `avatar`     longtext,
    `role`       enum ('admin', 'student', 'teacher')   default 'student',
    `status`     enum ('active', 'inactive', 'block') default 'active',
    `created_at` timestamp                   default current_timestamp,
    `updated_at` timestamp                   default current_timestamp on update current_timestamp,
    primary key (`user_id`)
    );


create table if not exists `courses`
(
    `course_id`          int auto_increment,
    `name`               varchar(255) unique not null,
    `description`        text,
    `level_id`           int,
    `thumbnail`          longtext,
    `status`             enum ('active', 'inactive') default 'active',
    `start_date`         date,
    `end_date`           date,
    `number_of_students` int                                    default 0,
    `created_at`         timestamp                              default current_timestamp,
    `updated_at`         timestamp                              default current_timestamp on update current_timestamp,
    primary key (`course_id`),
    foreign key (`level_id`) references `levels` (`level_id`)
    );

create table if not exists `lessons`
(
    `lesson_id`   int auto_increment,
    `course_id`   int,
    `title`       varchar(255) not null,
    `description` text,
    `sequence`    int,
    `status`      enum ('active', 'inactive') default 'active',
    `created_at`  timestamp                   default current_timestamp,
    `updated_at`  timestamp                   default current_timestamp on update current_timestamp,
    primary key (`lesson_id`),
    foreign key (`course_id`) references `courses` (`course_id`)
    );

create table if not exists `media`
(
    `media_id`   int auto_increment,
    `lesson_id`  int,
    `title`      varchar(255),
    `url`        longtext,
    `type`       varchar(255),
    `status`     enum ('active', 'inactive') default 'active',
    `created_at` timestamp                   default current_timestamp,
    `updated_at` timestamp                   default current_timestamp on update current_timestamp,
    primary key (`media_id`),
    foreign key (`lesson_id`) references `lessons` (`lesson_id`)
    );


create table if not exists `quizzes`
(
    `quiz_id`             int auto_increment,
    `lesson_id`           int,
    `title`               varchar(255) not null,
    `description`         text,
    `number_of_questions` int,
    `status`              enum ('active', 'inactive') default 'active',
    `created_at`          timestamp                   default current_timestamp,
    `updated_at`          timestamp                   default current_timestamp on update current_timestamp,
    primary key (`quiz_id`),
    foreign key (`lesson_id`) references `lessons` (`lesson_id`)
    );


create table if not exists `questions`
(
    `question_id` int auto_increment,
    `quiz_id`     int,
    `no`          int,
    `content`     text,
    `option_a`    text,
    `option_b`    text,
    `option_c`    text,
    `option_d`    text,
    `answer`      enum ('a', 'b', 'c', 'd'),
    `status`      enum ('active', 'inactive') default 'active',
    `created_at`  timestamp                   default current_timestamp,
    `updated_at`  timestamp                   default current_timestamp on update current_timestamp,
    primary key (`question_id`),
    foreign key (`quiz_id`) references `quizzes` (`quiz_id`)
    );

create table if not exists `time_table`
(
    `time_table_id` int auto_increment,
    `user_id`       int,
    `course_id`     int,
    `status`        enum ('pending', 'rejected', 'approved', 'deleted') default 'pending',
    `created_at`    timestamp                                           default current_timestamp,
    `updated_at`    timestamp                                           default current_timestamp on update current_timestamp,
    primary key (`time_table_id`),
    foreign key (`user_id`) references `users` (`user_id`),
    foreign key (`course_id`) references `courses` (`course_id`)
    );
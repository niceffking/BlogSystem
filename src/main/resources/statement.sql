drop database if exists blogsystemspring;
create database blogsystemspring character set utf8mb4;

use blogsystemspring;
drop table if exists userinfo;
create table userinfo(
    id int(11) primary key auto_increment not null,
    password varchar(32) not null,
    username varchar(100) not null ,
    photo varchar(500) default '',
    createtime timestamp null default current_timestamp,
    updatetime timestamp null default current_timestamp,
    state int(11) default '1'
);

drop table if exists articleinfo;
create table articleinfo(
    id int(11) primary key auto_increment not null,
    title varchar(50) not null ,
    content varchar(15000) not null,

    createtime timestamp null default current_timestamp,
    updatetime timestamp null default current_timestamp,

    uid int(11),
    readcount int(11) default 0,
    state int(11) default '1'
);


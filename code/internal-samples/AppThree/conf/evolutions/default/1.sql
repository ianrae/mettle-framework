# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table role_model (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_role_model primary key (id))
;

create table ticket_model (
  id                        bigint not null,
  constraint pk_ticket_model primary key (id))
;

create table user_model (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_user_model primary key (id))
;

create sequence role_model_seq;

create sequence ticket_model_seq;

create sequence user_model_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists role_model;

drop table if exists ticket_model;

drop table if exists user_model;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists role_model_seq;

drop sequence if exists ticket_model_seq;

drop sequence if exists user_model_seq;


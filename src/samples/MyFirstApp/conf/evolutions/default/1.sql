# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table task_model (
  id                        bigint,
  label                     varchar(255),
  enabled                   boolean)
;

create table user_model (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_user_model primary key (id))
;

create sequence user_model_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists task_model;

drop table if exists user_model;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_model_seq;


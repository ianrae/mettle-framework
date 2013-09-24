# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table task_model (
  id                        bigint not null,
  label                     varchar(255),
  constraint pk_task_model primary key (id))
;

create sequence task_model_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists task_model;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists task_model_seq;


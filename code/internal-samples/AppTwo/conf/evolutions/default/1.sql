# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company_model (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_company_model primary key (id))
;

create table flight (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_flight primary key (id))
;

create table user_model (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_user_model primary key (id))
;

create sequence company_model_seq;

create sequence flight_seq;

create sequence user_model_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists company_model;

drop table if exists flight;

drop table if exists user_model;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists company_model_seq;

drop sequence if exists flight_seq;

drop sequence if exists user_model_seq;


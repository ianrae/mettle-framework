# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table flight (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_flight primary key (id))
;

create sequence flight_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists flight;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists flight_seq;


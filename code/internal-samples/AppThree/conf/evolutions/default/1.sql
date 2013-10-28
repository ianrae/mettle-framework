# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table auth_role_model (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_auth_role_model primary key (id))
;

create table auth_rule_model (
  id                        bigint not null,
  user_id                   bigint,
  role_id                   bigint,
  ticket_id                 bigint,
  constraint pk_auth_rule_model primary key (id))
;

create table auth_ticket_model (
  id                        bigint not null,
  constraint pk_auth_ticket_model primary key (id))
;

create table auth_user_model (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_auth_user_model primary key (id))
;

create table ticket_model (
  id                        bigint not null,
  constraint pk_ticket_model primary key (id))
;

create sequence auth_role_model_seq;

create sequence auth_rule_model_seq;

create sequence auth_ticket_model_seq;

create sequence auth_user_model_seq;

create sequence ticket_model_seq;

alter table auth_rule_model add constraint fk_auth_rule_model_user_1 foreign key (user_id) references auth_user_model (id) on delete restrict on update restrict;
create index ix_auth_rule_model_user_1 on auth_rule_model (user_id);
alter table auth_rule_model add constraint fk_auth_rule_model_role_2 foreign key (role_id) references auth_role_model (id) on delete restrict on update restrict;
create index ix_auth_rule_model_role_2 on auth_rule_model (role_id);
alter table auth_rule_model add constraint fk_auth_rule_model_ticket_3 foreign key (ticket_id) references auth_ticket_model (id) on delete restrict on update restrict;
create index ix_auth_rule_model_ticket_3 on auth_rule_model (ticket_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists auth_role_model;

drop table if exists auth_rule_model;

drop table if exists auth_ticket_model;

drop table if exists auth_user_model;

drop table if exists ticket_model;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists auth_role_model_seq;

drop sequence if exists auth_rule_model_seq;

drop sequence if exists auth_ticket_model_seq;

drop sequence if exists auth_user_model_seq;

drop sequence if exists ticket_model_seq;


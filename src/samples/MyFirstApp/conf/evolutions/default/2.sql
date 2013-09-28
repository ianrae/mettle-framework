# --- Sample dataset

# --- !Ups

insert into phone_model (id,name) values (  1,'Apple Inc.');
insert into phone_model (id,name) values (  2,'Samsung');
insert into phone_model (id,name) values (  3,'Nokia');
insert into phone_model (id,name) values (  4,'Blackberry');

# --- !Downs

delete from phone_model;
delete from user_model;

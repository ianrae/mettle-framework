# --- Sample dataset

# --- !Ups

insert into phone_model (id,name) values (  1,'Apple Inc.');
insert into phone_model (id,name) values (  2,'Samsung');
insert into phone_model (id,name) values (  3,'Nokia');
insert into phone_model (id,name) values (  4,'Blackberry');

insert into user_model  (id,name,phone_id) values (  1,'MacBook Pro 15.4 inch',1);
insert into user_model  (id,name,phone_id) values (  2,'apple',2);
insert into user_model  (id,name,phone_id) values (  3,'banana',null);

# --- !Downs

delete from phone_model;
delete from user_model;

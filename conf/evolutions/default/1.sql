# --- !Ups
create table "members" (
  "id" int auto_increment primary key,
  "name" varchar(255) not null,
  "mail" varchar(255) not null,
  "password" varchar(255) not null
);
insert into "members" values (default, 'takashi', 'takashi@ogino', 'mmm-kkk');
insert into "members" values (default, 'nishioka', 'nishioka@tsuyoshi', 'lll-nnn');
insert into "members" values (default, 'chizuru', 'chizuru@mizuhara', 'kano-kari');

# --- !Downs
drop table "members"
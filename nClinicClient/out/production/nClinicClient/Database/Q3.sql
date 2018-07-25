create database if not exists clinic;

use clinic;

drop table if exists visitCount;

CREATE TABLE `visitCount` (
  `num` int(11) default null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into visitCount values(0);

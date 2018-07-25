create database if not exists clinic;

use clinic;

drop table if exists patiantInfo;

CREATE TABLE `patiantInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `age` varchar(7) DEFAULT null,
  `sex` varchar(4) DEFAULT NULL,
  `address` varchar(10) DEFAULT NULL,
  `connect` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

insert into patiantInfo values(null,'basel','18.10','ذكر','الوسطى','001');
insert into patiantInfo values(null,'test1','1','ذكر','غزة','002');
insert into patiantInfo values(null,'test2','2','انثى','رفح','003');
insert into patiantInfo values(null,'test3','3','انثى','خانيونس','004');
insert into patiantInfo values(null,'new1','1','ذكر','الوسطى','1');
insert into patiantInfo values(null,'new2','2','ذكر','غزة','2');
insert into patiantInfo values(null,'new3','3','انثى','رفح','3');
insert into patiantInfo values(null,'new4','4','انثى','خانيونس','4');
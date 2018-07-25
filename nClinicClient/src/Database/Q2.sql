create database if not exists clinic;

use clinic;

drop table if exists visitInfo;

CREATE TABLE `visitInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patiantID` int(11) default null,
  `patiant_name` varchar(64) default null,
  `reserve_date` DATE,
  `reserve_time` TIME,
  `attend_date` DATE,
  `attend_time` TIME,
  `visit_type` varchar(10) DEFAULT null,
  `payment_value` int(5) DEFAULT NULL,
  `payment_date` DATE,
  `attend` varchar(10) default null,
  `attend_type` varchar(15) default null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

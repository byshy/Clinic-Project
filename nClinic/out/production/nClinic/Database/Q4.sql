create database if not exists clinic;

use clinic;

drop table if exists medicalInfo;

CREATE TABLE `medicalInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patiantID` varchar(11) default null,
  `cheif_comp` text DEFAULT NULL,
  `other_comp` text DEFAULT null,
  `pih` text DEFAULT NULL,
  `pmh` text DEFAULT NULL,
  `psh` text DEFAULT NULL,
  `clinical_exam` text DEFAULT NULL,
  `dd` text DEFAULT NULL,
  `ct` text DEFAULT NULL,
  `mri` text DEFAULT NULL,
  `other_invests` text DEFAULT NULL,
  `plan` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
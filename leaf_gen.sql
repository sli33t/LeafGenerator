/*
SQLyog Ultimate - MySQL GUI v8.2 
MySQL - 8.0.20 : Database - leaf_gen
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`leaf_gen` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

/*Table structure for table `leaf_gen` */

DROP TABLE IF EXISTS `leaf_gen`;

CREATE TABLE `leaf_gen` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `max_id` bigint NOT NULL DEFAULT '0' COMMENT '最大ID',
  `step` int NOT NULL DEFAULT '0' COMMENT '每次递增数量',
  `gen_type` smallint DEFAULT '0' COMMENT '0-ID,1-NO',
  `business_type` smallint DEFAULT NULL COMMENT '业务类型',
  `prefix` varchar(4) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前缀',
  `unit_id` int DEFAULT NULL COMMENT '客户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `leaf_gen` */

insert  into `leaf_gen`(`id`,`max_id`,`step`,`gen_type`,`business_type`,`prefix`,`unit_id`) values (1,301001,1000,0,1,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

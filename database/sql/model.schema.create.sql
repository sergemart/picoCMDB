CREATE DATABASE IF NOT EXISTS `picocmdb-model` DEFAULT CHARACTER SET utf8
;
USE `picocmdb-model`
;

SET FOREIGN_KEY_CHECKS=0 
;

DROP TABLE IF EXISTS `managed_area` CASCADE
;

DROP TABLE IF EXISTS `role` CASCADE
;

CREATE TABLE `managed_area`
(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`name` NVARCHAR(50) NOT NULL,
	`description` NVARCHAR(200) 	 NULL,
	CONSTRAINT `PK_marea` PRIMARY KEY (`id` ASC)
)

;

CREATE TABLE `role`
(
	`id` NVARCHAR(50) NOT NULL,
	`description` NVARCHAR(200) 	 NULL,
	`is_system` BIT(1) NOT NULL,
	CONSTRAINT `PK_role` PRIMARY KEY (`id` ASC)
)

;

ALTER TABLE `managed_area` 
 ADD INDEX `UQ_marea_name` (`name` ASC)
;

SET FOREIGN_KEY_CHECKS=1 
;

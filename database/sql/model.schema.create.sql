CREATE DATABASE IF NOT EXISTS `picocmdb-model` DEFAULT CHARACTER SET utf8
;
USE `picocmdb-model`
;

SET FOREIGN_KEY_CHECKS=0 
;

DROP TABLE IF EXISTS `ci_marea_link` CASCADE
;

DROP TABLE IF EXISTS `ci_marea_link_type` CASCADE
;

DROP TABLE IF EXISTS `configuration_item` CASCADE
;

DROP TABLE IF EXISTS `configuration_item_type` CASCADE
;

DROP TABLE IF EXISTS `managed_area` CASCADE
;

DROP TABLE IF EXISTS `role` CASCADE
;

CREATE TABLE `ci_marea_link`
(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`ci_id` BIGINT NOT NULL,
	`marea_id` BIGINT NOT NULL,
	`link_type` NVARCHAR(50) NOT NULL,
	CONSTRAINT `PK_cml` PRIMARY KEY (`id` ASC)
)

;

CREATE TABLE `ci_marea_link_type`
(
	`id` NVARCHAR(50) NOT NULL,
	`description` NVARCHAR(200) 	 NULL,
	CONSTRAINT `PK_cit` PRIMARY KEY (`id` ASC)
)

;

CREATE TABLE `configuration_item`
(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`name` NVARCHAR(50) NOT NULL,
	`ci_type_id` NVARCHAR(50) NOT NULL,
	`description` NVARCHAR(200) 	 NULL,
	CONSTRAINT `PK_ci` PRIMARY KEY (`id` ASC)
)

;

CREATE TABLE `configuration_item_type`
(
	`id` NVARCHAR(50) NOT NULL,
	`description` NVARCHAR(200) 	 NULL,
	CONSTRAINT `PK_cit` PRIMARY KEY (`id` ASC)
)

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

ALTER TABLE `ci_marea_link` 
 ADD INDEX `IXFK_cml_ci` (`ci_id` ASC)
;

ALTER TABLE `ci_marea_link` 
 ADD INDEX `IXFK_cml_link_type` (`link_type` ASC)
;

ALTER TABLE `ci_marea_link` 
 ADD INDEX `IXFK_cml_marea` (`marea_id` ASC)
;

ALTER TABLE `ci_marea_link` 
 ADD INDEX `UQ_cml` (`ci_id` ASC, `marea_id` ASC, `link_type` ASC)
;

ALTER TABLE `configuration_item` 
 ADD INDEX `IXFK_ci_ci_type` (`ci_type_id` ASC)
;

ALTER TABLE `configuration_item` 
 ADD INDEX `UQ_ci_name_ci_type` (`name` ASC, `ci_type_id` ASC)
;

ALTER TABLE `managed_area` 
 ADD INDEX `UQ_marea_name` (`name` ASC)
;

ALTER TABLE `ci_marea_link` 
 ADD CONSTRAINT `FK_cml_ci`
	FOREIGN KEY (`ci_id`) REFERENCES `configuration_item` (`id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `ci_marea_link` 
 ADD CONSTRAINT `FK_cml_link_type`
	FOREIGN KEY (`link_type`) REFERENCES `ci_marea_link_type` (`id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `ci_marea_link` 
 ADD CONSTRAINT `FK_cml_marea`
	FOREIGN KEY (`marea_id`) REFERENCES `managed_area` (`id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `configuration_item` 
 ADD CONSTRAINT `FK_ci_ci_type`
	FOREIGN KEY (`ci_type_id`) REFERENCES `configuration_item_type` (`id`) ON DELETE Restrict ON UPDATE Restrict
;

SET FOREIGN_KEY_CHECKS=1 
;

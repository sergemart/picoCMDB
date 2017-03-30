CREATE DATABASE IF NOT EXISTS `picocmdb-model` DEFAULT CHARACTER SET utf8
;
USE `picocmdb-model`
;

SET FOREIGN_KEY_CHECKS=0 
;

DROP TABLE IF EXISTS `ci_ci_link` CASCADE
;

DROP TABLE IF EXISTS `ci_ci_link_type` CASCADE
;

DROP TABLE IF EXISTS `ci_marea_link` CASCADE
;

DROP TABLE IF EXISTS `configuration_item` CASCADE
;

DROP TABLE IF EXISTS `configuration_item_type` CASCADE
;

DROP TABLE IF EXISTS `managed_area` CASCADE
;

DROP TABLE IF EXISTS `role` CASCADE
;

CREATE TABLE `ci_ci_link`
(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`parent_id` BIGINT NOT NULL,
	`child_id` BIGINT NOT NULL,
	`link_type_id` NVARCHAR(50) NOT NULL,
	CONSTRAINT `PK_ccl` PRIMARY KEY (`id` ASC)
)

;

CREATE TABLE `ci_ci_link_type`
(
	`id` NVARCHAR(50) NOT NULL,
	`description` NVARCHAR(200) 	 NULL,
	CONSTRAINT `PK_cclt` PRIMARY KEY (`id` ASC)
)

;

CREATE TABLE `ci_marea_link`
(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`ci_id` BIGINT NOT NULL,
	`marea_id` BIGINT NOT NULL,
	CONSTRAINT `PK_cml` PRIMARY KEY (`id` ASC)
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

ALTER TABLE `ci_ci_link` 
 ADD CONSTRAINT `UQ_ccl` UNIQUE (`parent_id` ASC, `child_id` ASC, `link_type_id` ASC)
;

ALTER TABLE `ci_ci_link` 
 ADD INDEX `IXFK_ccl_cclt` (`link_type_id` ASC)
;

ALTER TABLE `ci_ci_link` 
 ADD INDEX `IXFK_ccl_ci_child` (`child_id` ASC)
;

ALTER TABLE `ci_ci_link` 
 ADD INDEX `IXFK_ccl_ci_parent` (`parent_id` ASC)
;

ALTER TABLE `ci_marea_link` 
 ADD CONSTRAINT `UQ_cml` UNIQUE (`ci_id` ASC, `marea_id` ASC)
;

ALTER TABLE `ci_marea_link` 
 ADD INDEX `IXFK_cml_ci` (`ci_id` ASC)
;

ALTER TABLE `ci_marea_link` 
 ADD INDEX `IXFK_cml_marea` (`marea_id` ASC)
;

ALTER TABLE `configuration_item` 
 ADD CONSTRAINT `UQ_ci_name_ci_type` UNIQUE (`name` ASC, `ci_type_id` ASC)
;

ALTER TABLE `configuration_item` 
 ADD INDEX `IXFK_ci_ci_type` (`ci_type_id` ASC)
;

ALTER TABLE `managed_area` 
 ADD CONSTRAINT `UQ_marea_name` UNIQUE (`name` ASC)
;

ALTER TABLE `ci_ci_link` 
 ADD CONSTRAINT `FK_ccl_cclt`
	FOREIGN KEY (`link_type_id`) REFERENCES `ci_ci_link_type` (`id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `ci_ci_link` 
 ADD CONSTRAINT `FK_ccl_ci_child`
	FOREIGN KEY (`child_id`) REFERENCES `configuration_item` (`id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `ci_ci_link` 
 ADD CONSTRAINT `FK_ccl_ci_parent`
	FOREIGN KEY (`parent_id`) REFERENCES `configuration_item` (`id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `ci_marea_link` 
 ADD CONSTRAINT `FK_cml_ci`
	FOREIGN KEY (`ci_id`) REFERENCES `configuration_item` (`id`) ON DELETE Restrict ON UPDATE Restrict
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

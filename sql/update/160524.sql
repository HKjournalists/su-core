#后端数据库修改脚本
ALTER TABLE `enterprise_registe`
	ADD COLUMN `serviceNo` VARCHAR(100) NULL DEFAULT NULL COMMENT '餐饮服务许许可证代码' AFTER `organizationNo`;
ALTER TABLE `enterprise_registe`
	ADD COLUMN `passNo` VARCHAR(100) NULL DEFAULT NULL COMMENT '食品流通许可证' AFTER `servicerNo`;
ALTER TABLE `enterprise_registe`
	ADD COLUMN `productNo` VARCHAR(100) NULL DEFAULT NULL COMMENT '生产许可证号' AFTER `passNo`;
ALTER TABLE `t_test_resource`
	CHANGE COLUMN `origin` `origin` VARCHAR(100) NULL DEFAULT NULL AFTER `UPLOAD_DATE`;

#新增人员信息表	
drop table if exists member;

/*==============================================================*/
/* Table: member                                                */
/*==============================================================*/
create table member
(
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`orgId` BIGINT(20) NULL DEFAULT NULL COMMENT '企业id',
	`description` VARCHAR(2000) NULL DEFAULT NULL COMMENT '个人简介' COLLATE 'utf8_unicode_ci',
	`name` VARCHAR(200) NULL DEFAULT NULL COMMENT '姓名' COLLATE 'utf8_unicode_ci',
	`position` VARCHAR(200) NULL DEFAULT NULL COMMENT '岗位' COLLATE 'utf8_unicode_ci',
	`address` VARCHAR(200) NULL DEFAULT NULL COMMENT '住址' COLLATE 'utf8_unicode_ci',
	`email` VARCHAR(200) NULL DEFAULT NULL COMMENT '邮箱' COLLATE 'utf8_unicode_ci',
	`tel` VARCHAR(200) NULL DEFAULT NULL COMMENT '联系电话' COLLATE 'utf8_unicode_ci',
	`identificationNo` VARCHAR(200) NULL DEFAULT NULL COMMENT '身份证号' COLLATE 'utf8_unicode_ci',
	`status` VARCHAR(10) NULL DEFAULT NULL COMMENT '状态 0 - 带提交 1-已提交' COLLATE 'utf8_unicode_ci',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_unicode_ci'
;

alter table member comment '人员信息表';
#新增人员证件照表
CREATE TABLE `t_hd_member_to_resource` (
	`RESOURCE_ID` BIGINT(20) NOT NULL COMMENT '资源ID',
	`MEMBER_ID` BIGINT(20) NOT NULL,
	`origin` VARCHAR(10) NULL DEFAULT NULL,
	`RECORD_INSERT_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`RESOURCE_ID`, `MEMBER_ID`),
	INDEX `FK_resourceId_to_resource_iid` (`RESOURCE_ID`),
	INDEX `MEMBER_ID` (`MEMBER_ID`),
	CONSTRAINT `FK_hd_member_to_resource_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`),
	CONSTRAINT `FK_t_hd_member_to_resource_member` FOREIGN KEY (`MEMBER_ID`) REFERENCES `member` (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
#新增人员健康证表
CREATE TABLE `t_hth_member_to_resource` (
	`RESOURCE_ID` BIGINT(20) NOT NULL COMMENT '资源ID',
	`MEMBER_ID` BIGINT(20) NOT NULL,
	`origin` VARCHAR(10) NULL DEFAULT NULL,
	`RECORD_INSERT_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`RESOURCE_ID`, `MEMBER_ID`),
	INDEX `FK_resourceId_to_resource_iid` (`RESOURCE_ID`),
	INDEX `MEMBER_ID` (`MEMBER_ID`),
	CONSTRAINT `FK_hth_member_to_resource_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`),
	CONSTRAINT `FK_t_hth_member_to_resource_member` FOREIGN KEY (`MEMBER_ID`) REFERENCES `member` (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
#新增人员从业资格证照表
CREATE TABLE `t_qc_member_to_resource` (
	`RESOURCE_ID` BIGINT(20) NOT NULL COMMENT '资源ID',
	`MEMBER_ID` BIGINT(20) NOT NULL,
	`origin` VARCHAR(10) NULL DEFAULT NULL,
	`RECORD_INSERT_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`RESOURCE_ID`, `MEMBER_ID`),
	INDEX `FK_resourceId_to_resource_iid` (`RESOURCE_ID`),
	INDEX `MEMBER_ID` (`MEMBER_ID`),
	CONSTRAINT `FK_qc_member_to_resource_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`),
	CONSTRAINT `FK_t_qc_member_to_resource_member` FOREIGN KEY (`MEMBER_ID`) REFERENCES `member` (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
#新增人员荣誉证书照表
CREATE TABLE `t_hn_member_to_resource` (
	`RESOURCE_ID` BIGINT(20) NOT NULL COMMENT '资源ID',
	`MEMBER_ID` BIGINT(20) NOT NULL,
	`origin` VARCHAR(10) NULL DEFAULT NULL,
	`RECORD_INSERT_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`RESOURCE_ID`, `MEMBER_ID`),
	INDEX `FK_resourceId_to_resource_iid` (`RESOURCE_ID`),
	INDEX `MEMBER_ID` (`MEMBER_ID`),
	CONSTRAINT `FK_hn_member_to_resource_id` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_test_resource` (`RESOURCE_ID`),
	CONSTRAINT `FK_t_hn_member_to_resource_member` FOREIGN KEY (`MEMBER_ID`) REFERENCES `member` (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
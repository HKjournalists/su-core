ALTER TABLE `member`
MODIFY COLUMN `description`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人简介' AFTER `orgId`,
MODIFY COLUMN `name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名' AFTER `description`,
MODIFY COLUMN `identificationNo`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号码' AFTER `name`,
MODIFY COLUMN `position`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位' AFTER `identificationNo`,
MODIFY COLUMN `address`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '户籍地址' AFTER `position`,
MODIFY COLUMN `email`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱' AFTER `address`,
CHANGE COLUMN `tel` `mobilePhone`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话' AFTER `email`,
MODIFY COLUMN `status`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 0 - 带提交 1-已提交' AFTER `mobilePhone`,
ADD COLUMN `sex`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别' AFTER `name`,
ADD COLUMN `credentialsType`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型' AFTER `sex`,
ADD COLUMN `nation`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '民族' AFTER `identificationNo`,
ADD COLUMN `appointUnit`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任免单位' AFTER `address`,
ADD COLUMN `personType`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人员类型' AFTER `appointUnit`,
ADD COLUMN `workType`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工种' AFTER `personType`,
ADD COLUMN `healthCertificateNo`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '健康证编号' AFTER `workType`,
ADD COLUMN `issueUnit`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发证单位' AFTER `healthCertificateNo`,
ADD COLUMN `tel`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定电话' AFTER `email`,
ADD COLUMN `origin`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源  1：录入  2：监管' AFTER `mobilePhone`;


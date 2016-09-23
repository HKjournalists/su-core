ALTER TABLE `test_result`
ADD COLUMN `limsSampleId`  bigint(20) NULL DEFAULT NULL COMMENT 'LIMS样品ID' AFTER `test_lab_id`,
ADD COLUMN `backLimsURL`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退回LIMS接口路径' AFTER `end_date`,
ADD COLUMN `limsIdentification`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'lims报告唯一标识' AFTER `limsSampleId`;

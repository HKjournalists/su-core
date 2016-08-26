
/**
 * 删除字段
 */
ALTER TABLE `deal_to_problem` DROP COLUMN deal_id;  

ALTER TABLE `deal_to_problem` ADD COLUMN `jgname` VARCHAR(255) DEFAULT NULL COMMENT '监管机构名称' AFTER `business_id`;

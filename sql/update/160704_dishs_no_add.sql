/**
 * 菜品管理信息,信息字段,备注信息和是否显示（默认值为1，表示显示，0表示否）
 */
ALTER TABLE `dishs_no` ADD COLUMN `remark` VARCHAR(1000) DEFAULT NULL COMMENT '备注' AFTER `qiyeId`;
ALTER TABLE `dishs_no` ADD COLUMN `show_flag` VARCHAR(2) DEFAULT 1 COMMENT '是否显示' AFTER `qiyeId`;

/**
 * （采购信息）新增台账界面需要加一个供应商证照上传功能【营业执照、食品流通许可证（非必填）】
 */
ALTER TABLE `ledger_prepackno` ADD COLUMN `DIS_RESOURCE_ID` BIGINT(20) DEFAULT NULL COMMENT '食品流通许可证' AFTER `qiyeId`;
ALTER TABLE `ledger_prepackno` ADD COLUMN `QS_RESOURCE_ID` BIGINT(20) DEFAULT NULL COMMENT '生存许可证号' AFTER `qiyeId`;
ALTER TABLE `ledger_prepackno` ADD COLUMN `LIC_RESOURCE_ID` BIGINT(20) DEFAULT NULL COMMENT '营养执照图片ID' AFTER `qiyeId`;
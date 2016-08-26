ALTER TABLE `product`
ADD COLUMN `nutri_status`  varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '营养指标计算状态:0-未计算，1-技算成功，2-计算失败' AFTER `package_flag`;
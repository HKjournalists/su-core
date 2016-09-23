ALTER TABLE `tz_product_trail`
MODIFY COLUMN `product_batch`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品批次' AFTER `bar_code`;

ALTER TABLE `tz_stock_info`
MODIFY COLUMN `product_batch`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品批次' AFTER `product_num`;

ALTER TABLE `tz_business_account_info_out`
MODIFY COLUMN `product_batch`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品批次' AFTER `product_price`;

ALTER TABLE `tz_business_account_info`
MODIFY COLUMN `product_batch`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品批次' AFTER `product_price`;

ALTER TABLE `tz_business_account_info_out`
MODIFY COLUMN `production_date`  varchar(24) NULL DEFAULT NULL COMMENT '产品生产日期' AFTER `product_batch`,
MODIFY COLUMN `over_date`  varchar(24) NULL DEFAULT NULL COMMENT '产品过期日期' AFTER `production_date`;

ALTER TABLE `tz_stock_info`
MODIFY COLUMN `date`  varchar(24) NULL DEFAULT NULL COMMENT '入库日期' AFTER `product_format`;

ALTER TABLE `tz_product_trail`
MODIFY COLUMN `production_date`  varchar(24) NULL DEFAULT NULL COMMENT '产品生产日期' AFTER `product_batch`,
MODIFY COLUMN `over_date`  varchar(24) NULL DEFAULT NULL COMMENT '产品过期日期' AFTER `production_date`,
MODIFY COLUMN `date`  varchar(24) NULL DEFAULT NULL COMMENT '台账日期' AFTER `over_date`;

ALTER TABLE `tz_business_relation`
MODIFY COLUMN `create_time`  varchar(24) NULL DEFAULT NULL COMMENT '创建时间' AFTER `relation_type`;

ALTER TABLE `tz_business_account_out`
MODIFY COLUMN `create_time`  varchar(24) NULL DEFAULT NULL COMMENT '创建时间' AFTER `in_business_id`,
MODIFY COLUMN `out_date`  varchar(24) NULL DEFAULT NULL COMMENT '退货日期' AFTER `in_status`,
MODIFY COLUMN `in_date`  varchar(24) NULL DEFAULT NULL COMMENT '收货日期' AFTER `out_date`;

ALTER TABLE `tz_business_account_info_out`
MODIFY COLUMN `production_date`  varchar(24) NULL DEFAULT NULL COMMENT '产品生产日期' AFTER `product_batch`,
MODIFY COLUMN `over_date`  varchar(24) NULL DEFAULT NULL COMMENT '产品过期日期' AFTER `production_date`;

ALTER TABLE `tz_business_account_info`
MODIFY COLUMN `production_date`  varchar(24) NULL DEFAULT NULL COMMENT '产品生产日期' AFTER `product_batch`,
MODIFY COLUMN `over_date`  varchar(24) NULL DEFAULT NULL COMMENT '产品过期日期' AFTER `production_date`;

ALTER TABLE `tz_business_account`
MODIFY COLUMN `out_date`  varchar(24) NULL DEFAULT NULL COMMENT '发货日期' AFTER `in_status`,
MODIFY COLUMN `in_date`  varchar(24) NULL DEFAULT NULL COMMENT '收货日期' AFTER `out_date`;

ALTER TABLE `tz_business_account`
ADD COLUMN `origin_id`  bigint(20) NULL AFTER `origin`;







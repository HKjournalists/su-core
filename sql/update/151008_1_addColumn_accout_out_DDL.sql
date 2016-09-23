ALTER TABLE `tz_business_account_out`
ADD COLUMN `account_id`  bigint(20) NULL DEFAULT NULL COMMENT '用于追溯是那张收货单产生的退货信息' AFTER `in_date`;

ALTER TABLE `tz_business_account_info`
ADD COLUMN `real_num`  int(11) NULL DEFAULT NULL COMMENT '收货商实际收货的数量' AFTER `product_num`;






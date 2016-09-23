ALTER TABLE `tz_stock_info`
ADD COLUMN `intake`  tinyint(4) NULL DEFAULT 0 COMMENT '0表示其他方式入库，1表示通过商品入库添加的' AFTER `qs_no`;


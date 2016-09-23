/*
销毁记录中添加处理人及处理地点
*/
ALTER TABLE `product_recycle_destroy_record`
ADD COLUMN `deal_address`  varchar(255) NULL AFTER `process_time`,
ADD COLUMN `deal_person`  varchar(255) NULL AFTER `deal_address`;
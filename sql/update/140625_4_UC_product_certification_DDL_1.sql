ALTER TABLE `product_certification`
MODIFY COLUMN `enddate`  datetime NULL DEFAULT NULL COMMENT '截止有效时间' AFTER `document_url`;
ALTER TABLE `product`
MODIFY COLUMN `characteristic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '产品特色' AFTER `feature`;
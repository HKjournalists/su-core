ALTER TABLE `product`
ADD COLUMN `is_agriculture_product`  bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是弄产品' AFTER `is_special_product`;
ALTER TABLE `product`
ADD COLUMN `provinceID`  varchar(40) NOT NULL COMMENT '省份ID' AFTER `is_agriculture_product`,
ADD COLUMN `cityID`  varchar(50) NOT NULL COMMENT '城市ID' AFTER `provinceID`,
ADD COLUMN `areaID`  varchar(50) NOT NULL COMMENT '地区ID' AFTER `cityID`;

ALTER TABLE `product`
MODIFY COLUMN `provinceID`  varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '省份ID' AFTER `is_agriculture_product`,
MODIFY COLUMN `cityID`  varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '城市ID' AFTER `provinceID`,
MODIFY COLUMN `areaID`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地区ID' AFTER `cityID`;


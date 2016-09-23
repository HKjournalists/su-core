ALTER TABLE `product`
ADD COLUMN `product_certification`  tinyint UNSIGNED NOT NULL COMMENT '0：未通过认证、1：通过食安认证、2：通过联盟认证' AFTER `areaID`;


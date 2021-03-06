DROP TABLE IF EXISTS `product_recommend_url`;
CREATE TABLE `product_recommend_url`(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `url_name` VARCHAR(100)  DEFAULT NULL COMMENT 'url名称',
  `pro_id` INT(11)  DEFAULT NULL COMMENT '产品ID',
  `status` VARCHAR(1) DEFAULT NULL COMMENT '状态：0表示供应商，1表示其他',
  `pro_url` VARCHAR(1000)  DEFAULT NULL COMMENT '填写的URL',
  `identify` VARCHAR(100)  DEFAULT NULL COMMENT '企业标识',
  PRIMARY KEY (`id`)
)ENGINE=INNODB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `product_to_regularity` (
`product_id`  bigint(20) NOT NULL COMMENT '产品主键',
`regularity_id`  bigint(20) NOT NULL COMMENT '执行标准主键',
PRIMARY KEY (`product_id`, `regularity_id`),
CONSTRAINT `product_to_regularity_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `product_to_regularity_ibfk_2` FOREIGN KEY (`regularity_id`) REFERENCES `product_category_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
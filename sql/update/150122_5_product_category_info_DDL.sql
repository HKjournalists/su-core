CREATE TABLE `product_category_info` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT  ,
`name`  varchar(255) NOT NULL COMMENT '若category_flag为0则是执行标准，为1则是三级分类' ,
`display`  varchar(100) NULL COMMENT '三级分类别名' ,
`category_id`  bigint(20) NOT NULL COMMENT '所属二级分类id' ,
`category_flag`  bit(1) NOT NULL  COMMENT '为0则是执行标准，为1则是三级分类' ,
`addition`  bit(1) NOT NULL DEFAULT b'1' COMMENT '为0则是默认添加的，为1则是人为添加的' ,
PRIMARY KEY (`id`),
FOREIGN KEY (`category_id`) REFERENCES `product_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
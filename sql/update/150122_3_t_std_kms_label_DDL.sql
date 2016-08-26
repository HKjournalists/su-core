CREATE TABLE `t_std_kms_label` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`kms_label_id`  bigint(20) NOT NULL COMMENT 'kms提供的标签ID' ,
`label_name`  varchar(300) NOT NULL COMMENT '标签名称' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `resource_of_prolicinfo_qs_applicant_claim` (
`id`  bigint UNSIGNED NOT NULL AUTO_INCREMENT ,
`file_name`  varchar(100) NOT NULL DEFAULT '' COMMENT '文件展示名称' ,
`resource_name`  varchar(100) NOT NULL DEFAULT '' COMMENT '文件实际名称' ,
`url`  varchar(200) NOT NULL DEFAULT '' COMMENT '文件web路径' ,
`resource_type_id`  tinyint NOT NULL COMMENT '文件类型' ,
`upload_time`  datetime NOT NULL COMMENT '上传时间' ,
`upload_user_name`  varchar(30) NOT NULL DEFAULT '' COMMENT '上传人' ,
`qs_id_applicant_claim_back`  bigint NOT NULL ,
`del`  tinyint NOT NULL DEFAULT 0 COMMENT '0代表未删除  1代表已经删除' ,
PRIMARY KEY (`id`)
);
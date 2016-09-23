CREATE TABLE `mengniu_product_import_log` (
`id`  int UNSIGNED NOT NULL AUTO_INCREMENT ,
`product_id`  int UNSIGNED NOT NULL COMMENT '关联产品ID' ,
`test_result_id`  int UNSIGNED NOT NULL COMMENT '关联报告ID' ,
`create_time`  timestamp NOT NULL COMMENT '创建时间' ,
`start_date`  date NOT NULL COMMENT '同步开始日期' ,
`end_date`  date NOT NULL COMMENT '同步结束日期' ,
PRIMARY KEY (`id`)
)
;


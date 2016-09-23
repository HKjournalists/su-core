/*
销毁记录中添加添加备注刷处理原因数据
*/
ALTER TABLE `product_recycle_destroy_record`
ADD COLUMN `remark`  varchar(255) NULL AFTER `operation_user`;
ADD COLUMN `format`  varchar(255) NULL AFTER `product_code`;
update product_recycle_destroy_record set problem_describe="国家强制召回" where problem_describe="compulsory_recall";
update product_recycle_destroy_record set problem_describe="产品临期" where problem_describe="advent";
update product_recycle_destroy_record set problem_describe="其他" where problem_describe="other";
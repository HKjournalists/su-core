DROP TABLE IF EXISTS product_business_license;
CREATE TABLE product_business_license(
   id bigint(20) NOT NULL AUTO_INCREMENT,
   product_id bigint(20) NOT NULL COMMENT '产品ID',
   business_id bigint(20) NOT NULL COMMENT '企业ID',
   business_name varchar(200) NOT NULL COMMENT '企业名称',
   RESOURCE_ID bigint(20)  NULL COMMENT '营业执照图片ID',
   QS_RESOURCE_ID bigint(20)  NULL COMMENT '生产许可证图片ID',
   DIS_RESOURCE_ID bigint(20)  NULL COMMENT '食品流通许可证图片ID',
   create_date DATETIME,
   PRIMARY KEY(id)
) ;
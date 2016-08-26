ALTER TABLE `business_unit`
ADD COLUMN `about`  varchar(1024) NULL COMMENT '企业简介' AFTER `sign_flag`,
ADD COLUMN `website`  varchar(200) NULL COMMENT '企业官网' AFTER `about`;
ALTER TABLE `business_unit`
ADD COLUMN `hot_flag`  char(1) NULL DEFAULT '0' COMMENT '热门企业推荐标识 0:不需要推荐 1:需要推荐' AFTER `orgnization`;
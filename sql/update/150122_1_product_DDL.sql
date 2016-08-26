ALTER TABLE `product`
ADD COLUMN `package_flag`  char(1) DEFAULT '0' COMMENT '产品包装标志：0：预包装，1：散装、2：无条码' AFTER `UNIT_ID`;
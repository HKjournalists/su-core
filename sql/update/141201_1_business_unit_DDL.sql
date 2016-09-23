ALTER TABLE `business_unit`
ADD COLUMN `sign_flag`  bit(1) NULL DEFAULT b'0' COMMENT '签名标志' AFTER `office_id`;
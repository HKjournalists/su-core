ALTER TABLE `business_market`
ADD COLUMN `publish_flag`  bit(1) NOT NULL DEFAULT b'0' AFTER `note`;
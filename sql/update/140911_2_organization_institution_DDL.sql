ALTER TABLE `organizing_institution`
ADD COLUMN `other_address`  varchar(300) NULL DEFAULT NULL COMMENT '地址别名' AFTER `org_address`;
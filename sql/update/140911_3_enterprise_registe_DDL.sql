ALTER TABLE `enterprise_registe`
ADD COLUMN `otherAddress`  varchar(300) NULL DEFAULT NULL COMMENT '地址别名' AFTER `enterptiteAddress`;
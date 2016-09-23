ALTER TABLE `enterprise_registe`
ADD COLUMN `licenseNo`  varchar(100) NULL COMMENT '营业执照号' AFTER `enterpriteDate`,
ADD COLUMN `organizationNo`  varchar(100) NULL COMMENT '组织机构代码' AFTER `licenseNo`;

ALTER TABLE `businessunit_to_prolicinfo`
ADD COLUMN `local`  tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '0：qs号不是该公司的qs号 1：qs号是该公司的qs号' AFTER `qs_no`,
ADD COLUMN `can_use`  tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '1: 除我之外，其他企业均不可使用 2: 所有企业均可使用 4: 只有制定企业可以使用' AFTER `local`,
ADD COLUMN `can_edit`  tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '1: 除我之外，均不可编辑 2: 所有企业均可编辑 4: 只有指定企业可编辑' AFTER `can_use`;

ALTER TABLE `businessunit_to_prolicinfo`
ADD COLUMN `del`  tinyint NOT NULL DEFAULT 0 COMMENT '0 代表未删除，1 代表已删除' AFTER `can_edit`;
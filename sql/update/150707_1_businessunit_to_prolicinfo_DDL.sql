ALTER TABLE `businessunit_to_prolicinfo`
MODIFY COLUMN `local`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0：qs号不是该公司的qs号 1：qs号是该公司的qs号' AFTER `qs_id`,
MODIFY COLUMN `can_use`  tinyint(3) UNSIGNED NOT NULL DEFAULT 2 COMMENT '1: 除我之外，其他企业均不可使用 2: 所有企业均可使用 4: 只有制定企业可以使用' AFTER `local`,
MODIFY COLUMN `can_edit`  tinyint(3) UNSIGNED NOT NULL DEFAULT 2 COMMENT '1: 除我之外，均不可编辑 2: 所有企业均可编辑 4: 只有指定企业可编辑' AFTER `can_use`;
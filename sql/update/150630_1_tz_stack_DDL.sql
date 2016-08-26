ALTER TABLE `tz_stock`
ADD COLUMN `report_status`  tinyint(4) NULL DEFAULT '1' COMMENT '0 代表 没有报告;1 代表有报告' AFTER `qs_no`;




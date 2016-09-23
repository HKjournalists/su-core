ALTER TABLE tz_business_account_info_out
ADD COLUMN problem_describe VARCHAR(100) NULL COMMENT '问题描述' AFTER origin;
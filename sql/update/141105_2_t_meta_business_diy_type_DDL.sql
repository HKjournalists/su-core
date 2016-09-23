ALTER TABLE `t_meta_business_diy_type`
ADD COLUMN `type`  int(11) NOT NULL AFTER `organization`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`business_id`, `organization`, `type`);

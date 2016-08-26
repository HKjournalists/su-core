
ALTER TABLE `business_unit`

  ADD COLUMN  `org_code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '''组织机构代码''',
  ADD COLUMN  `person_in_charge` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '法定代表人(负责人)',
   ADD COLUMN `id_card_no` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '身份证号',
  ADD COLUMN  `reg_fund` float DEFAULT NULL COMMENT '注册资金，单位万元',
  ADD COLUMN  `economic_nature` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '经济性质',
  ADD COLUMN  `business_scope` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '经营范围',
  ADD COLUMN  `postal_code` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮政编码',
  ADD COLUMN  `telephone` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系电话',
  ADD COLUMN  `mobile` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '手机',
   ADD COLUMN `tester` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '检验员(信息员)/手机',
   ADD COLUMN `area` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '所属区域',

  ADD COLUMN  `license_unit` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营业执照发证单位',
   ADD COLUMN `license_begin` datetime DEFAULT NULL COMMENT '营业执照发证日期',
  ADD COLUMN  `license_end` datetime DEFAULT NULL COMMENT '营业执照有效期至',
   ADD COLUMN `export_hygiene_no` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '出口食品卫生注册证号',
  ADD COLUMN  `export_hygiene_range` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '出口食品卫生注册证登记范围',
   ADD COLUMN `export_hygiene_start` datetime DEFAULT NULL COMMENT '出口食品卫生注册证发证日期',
   ADD COLUMN `export_hygiene_end` datetime DEFAULT NULL COMMENT '出口食品卫生注册证有效期至',
   ADD COLUMN `iso9001_cert` int(1) DEFAULT NULL COMMENT '是否是ISO9001认证',
  ADD COLUMN  `iso9001_unit` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ISO9001发证单位',
  ADD COLUMN  `iso9001_start` datetime DEFAULT NULL COMMENT 'ISO9001获证日期',
  ADD COLUMN  `iso9001_end` datetime DEFAULT NULL COMMENT 'ISO9001有效期至',
  ADD COLUMN  `iso14000_cert` int(1) DEFAULT NULL COMMENT '是否是ISO14000认证',
  ADD COLUMN  `iso14000_unit` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ISO14000发证单位',
  ADD COLUMN  `iso14000_start` datetime DEFAULT NULL COMMENT 'ISO14000获证日期',
  ADD COLUMN  `iso14000_end` datetime DEFAULT NULL COMMENT 'ISO14000有效期至',
  ADD COLUMN  `haccp_cert` int(1) DEFAULT NULL COMMENT '是否是HACCP认证',
  ADD COLUMN  `haccp_unit` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'HACCP发证单位',
  ADD COLUMN  `haccp_start` datetime DEFAULT NULL COMMENT 'HACCP获证日期',
   ADD COLUMN `haccp_end` datetime DEFAULT NULL COMMENT 'HACCP有效期至',
   ADD COLUMN `other_cert` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '其他认证名称',
  ADD COLUMN  `other_unit` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '其他认证发证单位',
  ADD COLUMN  `enterprise_scale` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '企业规模',
  ADD COLUMN  `total_population` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '企业总人数',
  ADD COLUMN  `tech_population` int(11) DEFAULT NULL,
  ADD COLUMN  `floor_space` float DEFAULT NULL COMMENT '占地面积，单位万平方米',
  ADD COLUMN  `structure_area` float DEFAULT NULL COMMENT '建筑面积，单位万平方米',
  ADD COLUMN  `fixed_assets` float DEFAULT NULL COMMENT '固定资产(现值)，单位万元',
  ADD COLUMN  `circulating_fund` float DEFAULT NULL COMMENT '流动资金，单位万元',
  ADD COLUMN  `annual_total_output_value` float DEFAULT NULL COMMENT '年总产值，单位万元',
  ADD COLUMN  `annual_sales` float DEFAULT NULL COMMENT '年销售额，单位万元',
  ADD COLUMN  `annual_tax` float DEFAULT NULL COMMENT '年缴税额，单位万元',
  ADD COLUMN  `annual_profit` float DEFAULT NULL COMMENT '年利润，单位万元',
  ADD COLUMN  `verify_status` int(1) DEFAULT NULL COMMENT '审核状态，1-新建 2-通过 3-退回',
  ADD COLUMN  `fax` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '传真',
  ADD COLUMN  `set_up_date` datetime DEFAULT NULL COMMENT '成立日期',
  ADD COLUMN  `operating_period` datetime DEFAULT NULL COMMENT '经营期限';

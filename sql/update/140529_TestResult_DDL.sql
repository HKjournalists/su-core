ALTER TABLE `test_result`
MODIFY COLUMN `comment`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注',
MODIFY COLUMN `pass`  tinyint(1) NULL DEFAULT NULL COMMENT '是否合格',
MODIFY COLUMN `test_date`  datetime NULL DEFAULT NULL COMMENT '检测时间',
MODIFY COLUMN `brand_id`  bigint(20) NULL DEFAULT NULL COMMENT '商标' ,
MODIFY COLUMN `testee_id`  bigint(20) NULL DEFAULT NULL COMMENT '被检测人' ,
MODIFY COLUMN `sample_id`  bigint(20) NULL DEFAULT NULL COMMENT '产品实例',
MODIFY COLUMN `sample_quantity`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '抽样数量',
MODIFY COLUMN `sampling_location`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '抽样地点' ,
MODIFY COLUMN `sampling_date`  datetime NULL DEFAULT NULL COMMENT '抽样时间',
MODIFY COLUMN `test_type`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '检测类型',
MODIFY COLUMN `equipment`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '主要仪器' ,
MODIFY COLUMN `standard`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '检测标准',
MODIFY COLUMN `result`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '结果描述',
MODIFY COLUMN `approve_by`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '批准人',
MODIFY COLUMN `audit_by`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '审核人',
MODIFY COLUMN `key_tester`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '主检人',
MODIFY COLUMN `pdf_report`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'pdf路径',
MODIFY COLUMN `publish_flag`  bit(1) NOT NULL DEFAULT b'0' COMMENT '发布到portal标志',
MODIFY COLUMN `service_order`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '报告编号',
MODIFY COLUMN `sample_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '样品编号',
MODIFY COLUMN `publishDate`  datetime NULL DEFAULT NULL COMMENT '发布到portal日期',
MODIFY COLUMN `receiveDate`  datetime NULL DEFAULT NULL COMMENT '接收日期',
MODIFY COLUMN `edition`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '报告来至哪个版本lims标志' ,
MODIFY COLUMN `dbflag`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '区分lims2.0和工商数据' ,
MODIFY COLUMN `organization`  bigint(20) NULL DEFAULT NULL COMMENT '报告所属公司的组织机构ID';



ALTER TABLE `test_result`
DROP COLUMN `tester_id`,
DROP COLUMN `tester_type`,
DROP COLUMN `fda_test_plan_id`,
DROP COLUMN `test_lab_id`;
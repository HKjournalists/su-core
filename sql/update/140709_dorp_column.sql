ALTER TABLE `business_brand` DROP COLUMN `identity`,DROP COLUMN `symbol`,DROP COLUMN `trademark`,DROP COLUMN `cobrand`;

ALTER TABLE `business_brand`
MODIFY COLUMN `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id' FIRST ,
MODIFY COLUMN `name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '品牌名称' AFTER `id`,
MODIFY COLUMN `logo`  varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '品牌图片路径' AFTER `name`,
MODIFY COLUMN `registration_date`  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '注册日期' AFTER `logo`,
MODIFY COLUMN `business_unit_id`  bigint(20) NULL DEFAULT NULL COMMENT '所属企业id' AFTER `registration_date`,
MODIFY COLUMN `organization`  bigint(20) NULL DEFAULT NULL COMMENT '组织机构id' AFTER `business_unit_id`,
MODIFY COLUMN `last_modify_time`  datetime NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `organization`;

ALTER TABLE `business_unit` DROP FOREIGN KEY `fk_business_unit_business_category`;

ALTER TABLE `business_unit` DROP FOREIGN KEY `fk_business_unit_business_type`;


ALTER TABLE `business_unit`
DROP COLUMN `address2`,
DROP COLUMN `logo`,
DROP COLUMN `website`,
DROP COLUMN `category`,
DROP COLUMN `id_card_no`,
DROP COLUMN `reg_fund`,
DROP COLUMN `economic_nature`,
DROP COLUMN `business_scope`,
DROP COLUMN `postal_code`,
DROP COLUMN `tester`,
DROP COLUMN `area`,
DROP COLUMN `license_unit`,
DROP COLUMN `license_begin`,
DROP COLUMN `license_end`,
DROP COLUMN `export_hygiene_no`,
DROP COLUMN `export_hygiene_range`,
DROP COLUMN `export_hygiene_start`,
DROP COLUMN `export_hygiene_end`,
DROP COLUMN `iso9001_cert`,
DROP COLUMN `iso9001_unit`,
DROP COLUMN `iso9001_start`,
DROP COLUMN `iso9001_end`,
DROP COLUMN `iso14000_cert`,
DROP COLUMN `iso14000_unit`,
DROP COLUMN `iso14000_start`,
DROP COLUMN `iso14000_end`,
DROP COLUMN `haccp_cert`,
DROP COLUMN `haccp_unit`,
DROP COLUMN `haccp_start`,
DROP COLUMN `haccp_end`,
DROP COLUMN `other_cert`,
DROP COLUMN `other_unit`,
DROP COLUMN `enterprise_scale`,
DROP COLUMN `total_population`,
DROP COLUMN `tech_population`,
DROP COLUMN `floor_space`,
DROP COLUMN `structure_area`,
DROP COLUMN `fixed_assets`,
DROP COLUMN `circulating_fund`,
DROP COLUMN `annual_total_output_value`,
DROP COLUMN `annual_sales`,
DROP COLUMN `annual_tax`,
DROP COLUMN `annual_profit`,
DROP COLUMN `verify_status`,
DROP COLUMN `set_up_date`,
DROP COLUMN `operating_period`;

ALTER TABLE `business_unit`
MODIFY COLUMN `name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '企业名称' AFTER `id`,
MODIFY COLUMN `address`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '地址' AFTER `name`,
MODIFY COLUMN `type`  bigint(20) NULL DEFAULT NULL COMMENT '类型（生产，流通，餐饮）' AFTER `address`,
MODIFY COLUMN `license_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '营业执照号' AFTER `type`,
MODIFY COLUMN `contact`  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '联系人' AFTER `license_no`,
MODIFY COLUMN `email`  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '邮箱' AFTER `contact`,
MODIFY COLUMN `distribution_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '流通许可证' AFTER `email`,
MODIFY COLUMN `region`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '地区' AFTER `distribution_no`,
MODIFY COLUMN `sampleLocal`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '抽样场所' AFTER `region`,
MODIFY COLUMN `administrativeLevel`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '行政区域' AFTER `sampleLocal`,
MODIFY COLUMN `note`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注' AFTER `administrativeLevel`,
MODIFY COLUMN `qs_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '生产许可证' AFTER `note`,
MODIFY COLUMN `parentOrgnization`  bigint(20) NULL DEFAULT NULL COMMENT '父企业组织机构id' AFTER `fax`,
MODIFY COLUMN `orgnization`  bigint(20) NULL DEFAULT 0 COMMENT '组织机构ID' AFTER `parentOrgnization`;

DROP TABLE business_type;
DROP TABLE break_laws;

ALTER TABLE `product`
DROP COLUMN `product_license_id`,
DROP COLUMN `regularity_cat`,
DROP COLUMN `trademark`,
DROP COLUMN `factory_inspection`,
DROP COLUMN `sale_area`,
DROP COLUMN `is_export`,
DROP COLUMN `license_no`,
DROP COLUMN `license_unit`,
DROP COLUMN `license_start`,
DROP COLUMN `license_end`,
DROP COLUMN `license_status`,
DROP COLUMN `verify_status`,
DROP COLUMN `manu_status`,
MODIFY COLUMN `expiration`  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '保质期' AFTER `expiration_date`;

ALTER TABLE `product`
MODIFY COLUMN `name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品名称' AFTER `id`,
MODIFY COLUMN `status`  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '状态' AFTER `name`,
MODIFY COLUMN `format`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '规格' AFTER `status`,
MODIFY COLUMN `regularity`  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '执行标准' AFTER `format`,
MODIFY COLUMN `barcode`  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '条形码' AFTER `regularity`,
MODIFY COLUMN `note`  text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '备注' AFTER `barcode`,
MODIFY COLUMN `business_brand_id`  bigint(20) NULL DEFAULT NULL COMMENT '所属品牌id' AFTER `note`,
MODIFY COLUMN `ICB_category`  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工商产品类别' AFTER `ingredient`,
MODIFY COLUMN `feature`  text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '特征' AFTER `category`,
MODIFY COLUMN `characteristic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '特色' AFTER `feature`,
MODIFY COLUMN `expiration_date`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品保质天数' AFTER `characteristic`,
MODIFY COLUMN `organization`  bigint(20) NULL DEFAULT NULL COMMENT '组织机构id' AFTER `expiration`,
MODIFY COLUMN `unit`  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '单位' AFTER `business_unit_id`,
MODIFY COLUMN `last_modify_time`  datetime NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `unit`;





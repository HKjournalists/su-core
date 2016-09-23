
DROP TABLE IF EXISTS `t_bus_sales_case`;
CREATE TABLE `t_bus_sales_case` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL ,
`organization`  bigint(20) NOT NULL ,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`name`  varchar(100) NOT NULL COMMENT '销售案例名称，不允许为空' ,
`description`  varchar(200) NULL COMMENT '销售情况简介' ,
`sort`  int(5) NULL DEFAULT -1 COMMENT '资料排序 序号 默认为-1未参加排序' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建人名称' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新人名称' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`),
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_bus_sales_branch`;
CREATE TABLE `t_bus_sales_branch` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL ,
`organization`  bigint(20) NOT NULL ,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空。' ,
`name`  varchar(100) NOT NULL COMMENT '销售网点名称，不允许为空' ,
`contact`  varchar(50) NULL COMMENT '联系人' ,
`person_in_charge`  varchar(50) NULL COMMENT '负责人' ,
`telephone`  varchar(20) NULL COMMENT '联系电话' ,
`addr_province`  varchar(20) NULL COMMENT '地址(省)' ,
`addr_city`  varchar(20) NULL COMMENT '地址(市)' ,
`addr_counties`  varchar(20) NULL COMMENT '地址（县）' ,
`addr_street`  varchar(200) NULL COMMENT '街道地址' ,
`point_lng`  varchar(50) NULL COMMENT '销售网点所在地图坐标的经度' ,
`point_lat`  varchar(50) NULL COMMENT '销售网点所在地图坐标的纬度' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建人名称' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新人名称' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`),
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_bus_contract`;
CREATE TABLE `t_bus_contract` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL ,
`organization`  bigint(20) NOT NULL ,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`name`  varchar(100) NOT NULL COMMENT '合同名称，不允许为空' ,
`remark`  varchar(200) NULL COMMENT '备注' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建人名称' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新人名称' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`),
INDEX `index_org` (`organization`) USING BTREE ,
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_bus_electronic_data`;
CREATE TABLE `t_bus_electronic_data` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`business_id` bigint(20) NOT NULL ,
`organization` bigint(20) NOT NULL ,
`guid` varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`name` varchar(100) NULL COMMENT '资料名称' ,
`create_time` datetime NULL COMMENT '创建时间' ,
`create_user` varchar(30) NULL COMMENT '创建人名称' ,
`update_time` datetime NULL COMMENT '更新时间' ,
`update_user` varchar(30) NULL COMMENT '更新人名称' ,
`del_status` int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`),
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_bus_promotion_case`;
CREATE TABLE `t_bus_promotion_case` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL ,
`organization`  bigint(20) NOT NULL ,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`name`  varchar(100) NOT NULL COMMENT '活动名称，不允许为空' ,
`start_time`  datetime NULL COMMENT '活动开始日期' ,
`end_time`  datetime NULL COMMENT '活动结束日期' ,
`area`  varchar(200) NULL COMMENT '惠及地区' ,
`introduction`  varchar(200) NULL COMMENT '活动简介' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建人名称' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新人名称' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`),
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_bus_photos_albums`;
CREATE TABLE `t_bus_photos_albums` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL COMMENT '企业id (外键),不允许为空。' ,
`organization`  bigint(20) NOT NULL COMMENT '企业组织机构' ,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`name`  varchar(100) NULL COMMENT '相册名称' ,
`description`  varchar(1000) NULL COMMENT '相册描述' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建者' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新者' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`) ,
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_bus_recommend_buy`;
CREATE TABLE `t_bus_recommend_buy` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL COMMENT '企业id (外键),不允许为空。' ,
`organization`  bigint(20) NOT NULL COMMENT '企业组织机构' ,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`name`  varchar(100) NULL COMMENT '购买方式' ,
`way`  varchar(100) NULL COMMENT '购买途径' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建者' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新者' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`) ,
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_bus_email_log`;
CREATE TABLE `t_bus_email_log` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL ,
`organization`  bigint(20) NOT NULL ,
`title`  varchar(100) NULL COMMENT '邮件标题' ,
`content`  varchar(500) NULL COMMENT '内容' ,
`addressee`  varchar(300) NULL COMMENT '收件人' ,
`annex_name`  varchar(100) NULL COMMENT '附件名称' ,
`electronic_data_id`  bigint(20) NULL COMMENT '电子资料id' ,
`contarts_ids`  varchar(100) NULL COMMENT '电子合同id集合' ,
`send_time`  datetime NULL COMMENT '发送时间' ,
`send_user`  varchar(30) NULL COMMENT '邮件发送者' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`),
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_sales_resource`;
CREATE TABLE `t_sales_resource` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`file_name`  varchar(100) NULL COMMENT '文件名' ,
`resource_type_id`  bigint(20) NULL COMMENT '资源类型' ,
`url`  varchar(300) NULL COMMENT '资源的url' ,
`sort`  int(10) DEFAULT -1 COMMENT '排序序号' ,
`cover`  int(5) DEFAULT 0 COMMENT '图片是否为封面的标记，默认为0,1表示封面' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建人' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新人' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`) ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_bus_sales_info`;
CREATE TABLE `t_bus_sales_info` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL ,
`organization`  bigint(20) NOT NULL ,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`pub_ptotos_name`  varchar(100) NULL COMMENT '宣传图片名称' ,
`pub_ptotos_url`  varchar(300) NULL COMMENT '宣传图片路径' ,
`qrcode_img_name`  varchar(100) NULL COMMENT '企业二维码图片名称' ,
`qrcode_img_url`  varchar(300) NULL COMMENT '企业二维码图片路径' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建者' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新者' ,
`del_status`  int(5) NULL DEFAULT 0 COMMENT '假删除状态，默认未0,1表示删除' ,
PRIMARY KEY (`id`) ,
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_sales_sort_field`;
CREATE TABLE `t_sales_sort_field` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`parent_id`  bigint(20) NOT NULL COMMENT '父id' ,
`name`  varchar(100) NOT NULL COMMENT '字段名称' ,
`display`  varchar(100) NULL COMMENT '字段的中文名称' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_sales_data_sort`;
CREATE TABLE `t_sales_data_sort` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT,
`business_id`  bigint(20) NOT NULL ,
`organization`  bigint(20) NOT NULL ,
`guid`  varchar(50) NOT NULL COMMENT '全局id，需创建索引，不能为空' ,
`sort_field_id`  bigint(20) NULL COMMENT '排序字段类型id' ,
`object_id`  bigint(20) NULL COMMENT '排序对象的id' ,
`no`  varchar(100) NULL COMMENT '排序对象编号' ,
`sort`  int(10) NULL DEFAULT -1 COMMENT '排序序号' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`create_user`  varchar(30) NULL COMMENT '创建者' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`update_user`  varchar(30) NULL COMMENT '更新者' ,
PRIMARY KEY (`id`) ,
INDEX `index_bus` (`business_id`) USING BTREE ,
INDEX `index_org` (`organization`) USING BTREE ,
INDEX `index_guid` (`guid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `certification`
ADD COLUMN `std_status`  int(5) NULL DEFAULT 0 COMMENT '标准认证的状态，0是标准认证，1是用户自己添加的认证' AFTER `name`;

ALTER TABLE `certification`
MODIFY COLUMN `name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '认证名称' AFTER `message`;

-- 在t_sales_sort_field表中添加基础数据
INSERT INTO t_sales_sort_field(id,parent_id,name,display) VALUES(1,7,'organization_license','组织机构证照');
INSERT INTO t_sales_sort_field(id,parent_id,name,display) VALUES(2,7,'business_license','营业执照证照');
INSERT INTO t_sales_sort_field(id,parent_id,name,display) VALUES(3,7,'tax_license','税务登记证照');
INSERT INTO t_sales_sort_field(id,parent_id,name,display) VALUES(4,4,'producer_qs','生产许可证照');
INSERT INTO t_sales_sort_field(id,parent_id,name,display) VALUES(5,5,'honor_certification','荣誉认证证照');
INSERT INTO t_sales_sort_field(id,parent_id,name,display) VALUES(6,6,'product_album','产品相册集');
INSERT INTO t_sales_sort_field(id,parent_id,name,display) VALUES(7,7,'sanzheng','三证');






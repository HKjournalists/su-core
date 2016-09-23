ALTER TABLE `product`
ADD COLUMN `net_content`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '净含量' AFTER `format`,
ADD COLUMN `product_type`  int(10) NULL DEFAULT 1 COMMENT '产品类型 1：国产食品  2：进口食品 4：国内分装食品' AFTER `risk_failure`;


-- ----------------------------
-- Table structure for `country`
-- ----------------------------
DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '国家名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of country
-- ----------------------------
INSERT INTO `country` VALUES ('1', '美国');
INSERT INTO `country` VALUES ('2', '法国');
INSERT INTO `country` VALUES ('3', '保加利亚');
INSERT INTO `country` VALUES ('4', '斯洛文尼亚\r\n斯洛文尼亚');
INSERT INTO `country` VALUES ('5', '克罗地亚');
INSERT INTO `country` VALUES ('6', '波黑');
INSERT INTO `country` VALUES ('7', '黑山共和国');
INSERT INTO `country` VALUES ('8', '德国');
INSERT INTO `country` VALUES ('9', '日本');
INSERT INTO `country` VALUES ('10', '俄罗斯');
INSERT INTO `country` VALUES ('11', '吉尔吉斯共和国');
INSERT INTO `country` VALUES ('12', '台湾');
INSERT INTO `country` VALUES ('13', '爱沙尼亚');
INSERT INTO `country` VALUES ('14', '拉脱维亚');
INSERT INTO `country` VALUES ('15', '阿塞拜疆');
INSERT INTO `country` VALUES ('16', '立陶宛');
INSERT INTO `country` VALUES ('17', '乌兹别克斯坦');
INSERT INTO `country` VALUES ('18', '斯里兰卡');
INSERT INTO `country` VALUES ('19', '菲律宾');
INSERT INTO `country` VALUES ('20', '白俄罗斯');
INSERT INTO `country` VALUES ('21', '乌克兰');
INSERT INTO `country` VALUES ('22', '摩尔多瓦');
INSERT INTO `country` VALUES ('23', '亚美尼亚');
INSERT INTO `country` VALUES ('24', '格鲁吉亚');
INSERT INTO `country` VALUES ('25', '哈萨克斯坦');
INSERT INTO `country` VALUES ('26', '塔吉克斯坦');
INSERT INTO `country` VALUES ('27', '香港');
INSERT INTO `country` VALUES ('28', '英国');
INSERT INTO `country` VALUES ('29', '希腊');
INSERT INTO `country` VALUES ('30', '黎巴嫩');
INSERT INTO `country` VALUES ('31', '塞浦路斯');
INSERT INTO `country` VALUES ('32', '阿尔巴尼亚');
INSERT INTO `country` VALUES ('33', '马其顿');
INSERT INTO `country` VALUES ('34', '马耳他');
INSERT INTO `country` VALUES ('35', '爱尔兰');
INSERT INTO `country` VALUES ('36', '比利时');
INSERT INTO `country` VALUES ('37', '葡萄牙');
INSERT INTO `country` VALUES ('38', '冰岛');
INSERT INTO `country` VALUES ('39', '丹麦');
INSERT INTO `country` VALUES ('40', '波兰');
INSERT INTO `country` VALUES ('41', '罗马尼亚');
INSERT INTO `country` VALUES ('42', '匈牙利');
INSERT INTO `country` VALUES ('43', '南非');
INSERT INTO `country` VALUES ('44', '加纳');
INSERT INTO `country` VALUES ('45', '塞内加尔');
INSERT INTO `country` VALUES ('46', '巴林');
INSERT INTO `country` VALUES ('47', '毛里求斯');
INSERT INTO `country` VALUES ('48', '摩洛哥');
INSERT INTO `country` VALUES ('49', '阿尔及利亚');
INSERT INTO `country` VALUES ('50', '尼日利亚');
INSERT INTO `country` VALUES ('51', '肯尼亚');
INSERT INTO `country` VALUES ('52', '象牙海岸');
INSERT INTO `country` VALUES ('53', '突尼斯');
INSERT INTO `country` VALUES ('54', '坦桑尼亚');
INSERT INTO `country` VALUES ('55', '叙利亚');
INSERT INTO `country` VALUES ('56', '埃及');
INSERT INTO `country` VALUES ('57', '文莱');
INSERT INTO `country` VALUES ('58', '利比亚');
INSERT INTO `country` VALUES ('59', '约旦');
INSERT INTO `country` VALUES ('60', '伊朗');
INSERT INTO `country` VALUES ('61', '科威特');
INSERT INTO `country` VALUES ('62', '沙特');
INSERT INTO `country` VALUES ('63', '阿联酋');
INSERT INTO `country` VALUES ('64', '芬兰');
INSERT INTO `country` VALUES ('65', '中国');
INSERT INTO `country` VALUES ('66', '挪威');
INSERT INTO `country` VALUES ('67', '以色列');
INSERT INTO `country` VALUES ('68', '瑞典');
INSERT INTO `country` VALUES ('69', '危地马拉');
INSERT INTO `country` VALUES ('70', '萨尔瓦多');
INSERT INTO `country` VALUES ('71', '洪都拉斯');
INSERT INTO `country` VALUES ('72', '尼加拉瓜');
INSERT INTO `country` VALUES ('73', '哥斯达黎加');
INSERT INTO `country` VALUES ('74', '巴拿马');
INSERT INTO `country` VALUES ('75', '多米尼加');
INSERT INTO `country` VALUES ('76', '墨西哥');
INSERT INTO `country` VALUES ('77', '加拿大');
INSERT INTO `country` VALUES ('78', '委内瑞拉');
INSERT INTO `country` VALUES ('79', '瑞士');
INSERT INTO `country` VALUES ('80', '哥伦比亚');
INSERT INTO `country` VALUES ('81', '乌拉圭');
INSERT INTO `country` VALUES ('82', '秘鲁');
INSERT INTO `country` VALUES ('83', '玻利维亚');
INSERT INTO `country` VALUES ('84', '阿根廷');
INSERT INTO `country` VALUES ('85', '智利');
INSERT INTO `country` VALUES ('86', '巴拉圭');
INSERT INTO `country` VALUES ('87', '厄瓜多尔');
INSERT INTO `country` VALUES ('88', '巴西');
INSERT INTO `country` VALUES ('89', '斯洛伐克');
INSERT INTO `country` VALUES ('90', '捷克');
INSERT INTO `country` VALUES ('91', '南斯拉夫');
INSERT INTO `country` VALUES ('92', '蒙古');
INSERT INTO `country` VALUES ('93', '朝鲜');
INSERT INTO `country` VALUES ('94', '土耳其');
INSERT INTO `country` VALUES ('95', '荷兰');
INSERT INTO `country` VALUES ('96', '韩国');
INSERT INTO `country` VALUES ('97', '柬埔寨');
INSERT INTO `country` VALUES ('98', '泰国');
INSERT INTO `country` VALUES ('99', '新加坡');
INSERT INTO `country` VALUES ('100', '印度');
INSERT INTO `country` VALUES ('101', '越南');
INSERT INTO `country` VALUES ('102', '巴基斯坦');
INSERT INTO `country` VALUES ('103', '印度尼西亚');
INSERT INTO `country` VALUES ('104', '奥地利');
INSERT INTO `country` VALUES ('105', '澳大利亚');
INSERT INTO `country` VALUES ('106', '新西兰');
INSERT INTO `country` VALUES ('107', 'GS1总部');
INSERT INTO `country` VALUES ('108', 'GS1总部（产品电子代码）');
INSERT INTO `country` VALUES ('109', 'GS1总部（缩短码）');
INSERT INTO `country` VALUES ('110', '马来西亚');
INSERT INTO `country` VALUES ('111', '中国澳门特别行政区');
INSERT INTO `country` VALUES ('112', '店内码');
INSERT INTO `country` VALUES ('113', '意大利');
INSERT INTO `country` VALUES ('114', '西班牙');
INSERT INTO `country` VALUES ('115', '古巴');



-- ----------------------------
-- Table structure for `country_to_barcode`
-- ----------------------------
DROP TABLE IF EXISTS `country_to_barcode`;
CREATE TABLE `country_to_barcode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country_id` bigint(20) DEFAULT NULL COMMENT '国家表id',
  `bar3Int_start` int(10) DEFAULT NULL COMMENT '条码前3位开始区间 整数类型',
  `bar3Int_end` int(10) DEFAULT NULL COMMENT '条码前3位结束区间 整数类型',
  `bar3_start` varchar(10) DEFAULT NULL COMMENT '条码前3位开始区间 字符类型',
  `bar3_end` varchar(10) DEFAULT NULL COMMENT '条码前3位结束区间 字符类型',
  `createDate` datetime DEFAULT '2015-05-22 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of country_to_barcode
-- ----------------------------
INSERT INTO `country_to_barcode` VALUES ('1', '1', '0', '19', '000', '019', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('2', '1', '30', '39', '030', '039', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('3', '1', '60', '139', '060', '039', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('4', '112', '20', '29', '020', '029', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('5', '112', '40', '49', '040', '049', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('6', '112', '200', '299', '200', '299', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('7', '2', '300', '379', '300', '379', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('8', '3', '380', '380', '380', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('9', '4', '383', '383', '383', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('10', '5', '385', '385', '385', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('11', '6', '387', '387', '387', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('12', '7', '389', '389', '389', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('13', '8', '400', '440', '400', '440', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('14', '9', '450', '459', '450', '459', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('15', '9', '490', '499', '490', '499', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('16', '10', '460', '469', '460', '469', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('17', '11', '470', '470', '470', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('18', '12', '471', '471', '471', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('19', '13', '474', '474', '474', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('20', '14', '475', '475', '475', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('21', '15', '476', '476', '476', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('22', '16', '477', '477', '477', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('23', '17', '478', '478', '478', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('24', '18', '479', '479', '479', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('25', '19', '480', '480', '480', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('26', '20', '481', '481', '481', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('27', '21', '482', '482', '482', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('28', '22', '484', '484', '484', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('29', '23', '485', '485', '485', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('30', '24', '486', '486', '486', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('31', '25', '487', '487', '487', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('32', '26', '488', '488', '488', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('33', '27', '489', '489', '489', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('34', '28', '500', '509', '500', '509', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('35', '29', '520', '521', '520', '521', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('36', '30', '528', '528', '528', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('37', '31', '529', '529', '529', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('38', '32', '530', '530', '530', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('39', '33', '531', '531', '531', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('40', '34', '535', '535', '535', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('41', '35', '539', '539', '539', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('42', '36', '540', '549', '540', '549', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('43', '37', '560', '560', '560', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('44', '38', '569', '569', '569', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('45', '39', '570', '579', '570', '579', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('46', '40', '590', '590', '590', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('47', '41', '594', '594', '594', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('48', '42', '599', '599', '599', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('49', '43', '600', '601', '600', '601', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('50', '44', '603', '603', '603', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('51', '45', '604', '604', '604', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('52', '46', '608', '608', '608', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('53', '47', '609', '609', '609', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('54', '48', '611', '611', '611', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('55', '49', '613', '613', '613', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('56', '50', '615', '615', '615', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('57', '51', '616', '616', '616', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('58', '52', '618', '618', '618', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('59', '53', '619', '619', '619', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('60', '54', '620', '620', '620', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('61', '55', '621', '621', '621', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('62', '56', '622', '622', '622', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('63', '57', '623', '623', '623', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('64', '58', '624', '624', '624', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('65', '59', '625', '625', '625', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('66', '60', '626', '626', '626', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('67', '61', '627', '627', '627', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('68', '62', '628', '628', '628', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('69', '63', '629', '629', '629', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('70', '64', '640', '649', '640', '649', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('71', '65', '690', '699', '690', '699', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('72', '66', '700', '709', '700', '709', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('73', '67', '729', '729', '729', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('74', '68', '730', '739', '730', '739', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('75', '69', '740', '740', '740', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('76', '70', '741', '741', '741', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('77', '71', '742', '742', '742', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('78', '72', '743', '743', '743', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('79', '73', '744', '744', '744', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('80', '74', '745', '745', '745', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('81', '75', '746', '746', '746', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('82', '76', '750', '750', '750', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('83', '77', '754', '755', '754', '755', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('84', '78', '759', '759', '759', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('85', '79', '760', '769', '760', '769', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('86', '80', '770', '771', '770', '771', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('87', '81', '773', '773', '773', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('88', '82', '775', '775', '775', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('89', '83', '777', '777', '777', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('90', '84', '778', '779', '778', '779', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('91', '85', '780', '780', '780', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('92', '86', '784', '784', '784', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('93', '87', '786', '786', '786', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('94', '88', '789', '790', '789', '790', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('95', '113', '800', '839', '800', '839', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('96', '114', '840', '849', '840', '849', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('97', '115', '850', '850', '850', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('98', '89', '858', '858', '858', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('99', '90', '859', '859', '859', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('100', '91', '860', '860', '860', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('101', '92', '865', '865', '865', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('102', '93', '867', '867', '867', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('103', '94', '868', '869', '868', '869', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('104', '95', '870', '879', '870', '879', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('105', '96', '880', '880', '880', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('106', '97', '884', '884', '884', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('107', '98', '885', '885', '885', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('108', '99', '888', '888', '888', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('109', '100', '890', '890', '890', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('110', '101', '893', '893', '893', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('111', '102', '896', '896', '896', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('112', '103', '899', '899', '899', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('113', '104', '900', '919', '900', '919', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('114', '105', '930', '939', '930', '939', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('115', '106', '940', '949', '940', '949', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('116', '107', '950', '950', '950', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('117', '108', '951', '951', '951', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('118', '109', '960', '969', '960', '969', '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('119', '110', '955', '955', '955', null, '2015-05-22 00:00:00');
INSERT INTO `country_to_barcode` VALUES ('120', '111', '958', '958', '958', null, '2015-05-22 00:00:00');


DROP TABLE IF EXISTS `imported_product`;
CREATE TABLE `imported_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `country_id` bigint(20) DEFAULT NULL COMMENT '原产地ID',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `lastModifyUser` varchar(100) DEFAULT NULL COMMENT '最后更新者',
  `lastModifyDate` datetime DEFAULT NULL COMMENT '最后更新时间',
  `del` bit(1) DEFAULT b'0' COMMENT '删除标志 0：未删除  1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `imported_product_domestic_agents`;
CREATE TABLE `imported_product_domestic_agents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imp_pro_id` bigint(20) DEFAULT NULL COMMENT '进口产品表(imported_product)ID',
  `agent_name` varchar(255) DEFAULT NULL COMMENT '代理商名称',
  `agent_address` varchar(255) DEFAULT NULL COMMENT '代理商地址',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `lastModifyUser` varchar(100) DEFAULT NULL COMMENT '最后更新者',
  `lastModifyDate` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `imported_product_label_to_resource`;
CREATE TABLE `imported_product_label_to_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  `imp_pro_id` bigint(20) NOT NULL COMMENT '进口食品表ID',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`RESOURCE_ID`,`imp_pro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `test_result_imported_product`;
CREATE TABLE `test_result_imported_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_result_id` bigint(20) DEFAULT NULL COMMENT '检测结果ID',
  `sanitary_cert_no` varchar(100) DEFAULT NULL COMMENT '卫生证书编号',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `lastModifyUser` varchar(100) DEFAULT NULL COMMENT '最后更新者',
  `lastModifyDate` datetime DEFAULT NULL COMMENT '最后更新时间',
  `del` bit(1) DEFAULT b'0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sanitary_cert_to_resource`;
CREATE TABLE `sanitary_cert_to_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源id',
  `test_result_imp_pro_id` bigint(20) NOT NULL COMMENT '进口食品检测结果表ID',
  PRIMARY KEY (`RESOURCE_ID`,`test_result_imp_pro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
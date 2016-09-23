/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : fsn_cloud

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2013-12-10 15:38:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `business_brand`
-- ----------------------------
DROP TABLE IF EXISTS `business_brand`;
CREATE TABLE `business_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `identity` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `symbol` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `trademark` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cobrand` bit(1) DEFAULT NULL,
  `registration_date` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `business_unit_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`),
  UNIQUE KEY `idx_identity` (`identity`),
  KEY `fk_business_brand_business_unit` (`business_unit_id`),
  CONSTRAINT `fk_business_brand_business_unit` FOREIGN KEY (`business_unit_id`) REFERENCES `business_unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10093 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_brand
-- ----------------------------
INSERT INTO `business_brand` VALUES ('10', 'NESCAFE', 'NESCAFE', null, null, null, '', null, '10');
INSERT INTO `business_brand` VALUES ('11', 'Nestle', 'Nestle', null, null, null, '', null, '11');
INSERT INTO `business_brand` VALUES ('12', 'Nestle\'', 'Nestle\'', null, null, null, '', null, '12');
INSERT INTO `business_brand` VALUES ('13', 'PabjtBLUERIbbon', 'PabjtBLUERIbbon', null, null, null, '', null, '13');
INSERT INTO `business_brand` VALUES ('14', 'PEPSI', 'PEPSI', null, null, null, '', null, '14');
INSERT INTO `business_brand` VALUES ('15', 'PEPSI百事可乐', 'PEPSI百事可乐', null, null, null, '', null, '15');
INSERT INTO `business_brand` VALUES ('16', 'Rich Blend', 'Rich Blend', null, null, null, '', null, '16');
INSERT INTO `business_brand` VALUES ('17', 'Sprite', 'Sprite', null, null, null, '', null, '17');
INSERT INTO `business_brand` VALUES ('18', 'SSS', 'SSS', null, null, null, '', null, '18');
INSERT INTO `business_brand` VALUES ('19', 'Tropicana', 'Tropicana', null, null, null, '', null, '19');
INSERT INTO `business_brand` VALUES ('20', 'Tropicana纯果乐', 'Tropicana纯果乐', null, null, null, '', null, '20');
INSERT INTO `business_brand` VALUES ('21', 'XLX', 'XLX', null, null, null, '', null, '21');
INSERT INTO `business_brand` VALUES ('22', '一万响', '一万响', null, null, null, '', null, '22');
INSERT INTO `business_brand` VALUES ('23', '一壶春', '一壶春', null, null, null, '', null, '23');
INSERT INTO `business_brand` VALUES ('24', '一桶江湖', '一桶江湖', null, null, null, '', null, '24');
INSERT INTO `business_brand` VALUES ('25', '一米阳光', '一米阳光', null, null, null, '', null, '25');
INSERT INTO `business_brand` VALUES ('26', '七叔公', '七叔公', null, null, null, '', null, '26');
INSERT INTO `business_brand` VALUES ('27', '七味', '七味', null, null, null, '', null, '27');
INSERT INTO `business_brand` VALUES ('28', '七喜', '七喜', null, null, null, '', null, '28');
INSERT INTO `business_brand` VALUES ('29', '七箭', '七箭', null, null, null, '', null, '29');
INSERT INTO `business_brand` VALUES ('30', '万家欢', '万家欢', null, null, null, '', null, '30');
INSERT INTO `business_brand` VALUES ('31', '万林', '万林', null, null, null, '', null, '31');
INSERT INTO `business_brand` VALUES ('32', '万顺豪', '万顺豪', null, null, null, '', null, '32');
INSERT INTO `business_brand` VALUES ('33', '三代行', '三代行', null, null, null, '', null, '33');
INSERT INTO `business_brand` VALUES ('34', '三全', '三全', null, null, null, '', null, '34');
INSERT INTO `business_brand` VALUES ('35', '三全凌', '三全凌', null, null, null, '', null, '35');
INSERT INTO `business_brand` VALUES ('36', '世纪福', '世纪福', null, null, null, '', null, '36');
INSERT INTO `business_brand` VALUES ('37', '东明思', '东明思', null, null, null, '', null, '37');
INSERT INTO `business_brand` VALUES ('38', '中华山', '中华山', null, null, null, '', null, '38');
INSERT INTO `business_brand` VALUES ('39', '中国茗茶', '中国茗茶', null, null, null, '', null, '39');
INSERT INTO `business_brand` VALUES ('40', '中沃', '中沃', null, null, null, '', null, '40');
INSERT INTO `business_brand` VALUES ('41', '中绿', '中绿', null, null, null, '', null, '41');
INSERT INTO `business_brand` VALUES ('42', '乐源', '乐源', null, null, null, '', null, '42');
INSERT INTO `business_brand` VALUES ('43', '乐满家', '乐满家', null, null, null, '', null, '43');
INSERT INTO `business_brand` VALUES ('44', '乖媳妇', '乖媳妇', null, null, null, '', null, '44');
INSERT INTO `business_brand` VALUES ('45', '九洞天', '九洞天', null, null, null, '', null, '45');
INSERT INTO `business_brand` VALUES ('46', '九福泉', '九福泉', null, null, null, '', null, '46');
INSERT INTO `business_brand` VALUES ('47', '九龙泉', '九龙泉', null, null, null, '', null, '47');
INSERT INTO `business_brand` VALUES ('48', '乡太佬', '乡太佬', null, null, null, '', null, '48');
INSERT INTO `business_brand` VALUES ('49', '乡妹子', '乡妹子', null, null, null, '', null, '49');
INSERT INTO `business_brand` VALUES ('50', '乡巴佬', '乡巴佬', null, null, null, '', null, '50');
INSERT INTO `business_brand` VALUES ('51', '乡音', '乡音', null, null, null, '', null, '51');
INSERT INTO `business_brand` VALUES ('52', '二酉', '二酉', null, null, null, '', null, '52');
INSERT INTO `business_brand` VALUES ('53', '云井', '云井', null, null, null, '', null, '53');
INSERT INTO `business_brand` VALUES ('54', '云仰', '云仰', null, null, null, '', null, '54');
INSERT INTO `business_brand` VALUES ('55', '云鹤食品', '云鹤食品', null, null, null, '', null, '55');
INSERT INTO `business_brand` VALUES ('56', '五芳斋', '五芳斋', null, null, null, '', null, '56');
INSERT INTO `business_brand` VALUES ('57', '仡佬山', '仡佬山', null, null, null, '', null, '57');
INSERT INTO `business_brand` VALUES ('58', '仰阿莎', '仰阿莎', null, null, null, '', null, '58');
INSERT INTO `business_brand` VALUES ('59', '伊利', '伊利', null, null, null, '', null, '59');
INSERT INTO `business_brand` VALUES ('60', '优乐美', '优乐美', null, null, null, '', null, '60');
INSERT INTO `business_brand` VALUES ('61', '余六妹', '余六妹', null, null, null, '', null, '61');
INSERT INTO `business_brand` VALUES ('62', '佰豆集', '佰豆集', null, null, null, '', null, '62');
INSERT INTO `business_brand` VALUES ('63', '佳香旺', '佳香旺', null, null, null, '', null, '63');
INSERT INTO `business_brand` VALUES ('64', '佳香肚', '佳香肚', null, null, null, '', null, '64');
INSERT INTO `business_brand` VALUES ('65', '假日', '假日', null, null, null, '', null, '65');
INSERT INTO `business_brand` VALUES ('66', '健力宝', '健力宝', null, null, null, '', null, '66');
INSERT INTO `business_brand` VALUES ('67', '健娃', '健娃', null, null, null, '', null, '67');
INSERT INTO `business_brand` VALUES ('68', '億农', '億农', null, null, null, '', null, '68');
INSERT INTO `business_brand` VALUES ('69', '億龍', '億龍', null, null, null, '', null, '69');
INSERT INTO `business_brand` VALUES ('70', '六六红', '六六红', null, null, null, '', null, '70');
INSERT INTO `business_brand` VALUES ('71', '六广河', '六广河', null, null, null, '', null, '71');
INSERT INTO `business_brand` VALUES ('72', '农夫山泉', '农夫山泉', null, null, null, '', null, '72');
INSERT INTO `business_brand` VALUES ('73', '农夫果园', '农夫果园', null, null, null, '', null, '73');
INSERT INTO `business_brand` VALUES ('74', '冰极零', '冰极零', null, null, null, '', null, '74');
INSERT INTO `business_brand` VALUES ('75', '冰泉', '冰泉', null, null, null, '', null, '75');
INSERT INTO `business_brand` VALUES ('76', '冰点', '冰点', null, null, null, '', null, '76');
INSERT INTO `business_brand` VALUES ('77', '凉泉', '凉泉', null, null, null, '', null, '77');
INSERT INTO `business_brand` VALUES ('78', '凤凰山', '凤凰山', null, null, null, '', null, '78');
INSERT INTO `business_brand` VALUES ('79', '凯维', '凯维', null, null, null, '', null, '79');
INSERT INTO `business_brand` VALUES ('80', '刘胡子', '刘胡子', null, null, null, '', null, '80');
INSERT INTO `business_brand` VALUES ('81', '利兴达', '利兴达', null, null, null, '', null, '81');
INSERT INTO `business_brand` VALUES ('82', '北极熊', '北极熊', null, null, null, '', null, '82');
INSERT INTO `business_brand` VALUES ('83', '千古韵', '千古韵', null, null, null, '', null, '83');
INSERT INTO `business_brand` VALUES ('84', '华雄', '华雄', null, null, null, '', null, '84');
INSERT INTO `business_brand` VALUES ('85', '南国', '南国', null, null, null, '', null, '85');
INSERT INTO `business_brand` VALUES ('86', '南岛河', '南岛河', null, null, null, '', null, '86');
INSERT INTO `business_brand` VALUES ('87', '南方', '南方', null, null, null, '', null, '87');
INSERT INTO `business_brand` VALUES ('88', '卡夫', '卡夫', null, null, null, '', null, '88');
INSERT INTO `business_brand` VALUES ('89', '友伦', '友伦', null, null, null, '', null, '89');
INSERT INTO `business_brand` VALUES ('90', '双佳龙', '双佳龙', null, null, null, '', null, '90');
INSERT INTO `business_brand` VALUES ('91', '双汇', '双汇', null, null, null, '', null, '91');
INSERT INTO `business_brand` VALUES ('92', '可口可乐', '可口可乐', null, null, null, '', null, '92');
INSERT INTO `business_brand` VALUES ('93', '吐香', '吐香', null, null, null, '', null, '93');
INSERT INTO `business_brand` VALUES ('94', '周君记', '周君记', null, null, null, '', null, '94');
INSERT INTO `business_brand` VALUES ('95', '呷比特', '呷比特', null, null, null, '', null, '95');
INSERT INTO `business_brand` VALUES ('96', '和兴隆', '和兴隆', null, null, null, '', null, '96');
INSERT INTO `business_brand` VALUES ('97', '和路雪', '和路雪', null, null, null, '', null, '97');
INSERT INTO `business_brand` VALUES ('98', '咖啡伴侣', '咖啡伴侣', null, null, null, '', null, '98');
INSERT INTO `business_brand` VALUES ('99', '品馨', '品馨', null, null, null, '', null, '99');
INSERT INTO `business_brand` VALUES ('100', '哇哈哈', '哇哈哈', null, null, null, '', null, '100');
INSERT INTO `business_brand` VALUES ('101', '喜之郎', '喜之郎', null, null, null, '', null, '101');
INSERT INTO `business_brand` VALUES ('102', '喜鹭', '喜鹭', null, null, null, '', null, '102');
INSERT INTO `business_brand` VALUES ('103', '嘉港', '嘉港', null, null, null, '', null, '103');
INSERT INTO `business_brand` VALUES ('104', '图形商标', '图形商标', null, null, null, '', null, '104');
INSERT INTO `business_brand` VALUES ('105', '图标见瓶身', '图标见瓶身', null, null, null, '', null, '105');
INSERT INTO `business_brand` VALUES ('106', '圣罗黑啤', '圣罗黑啤', null, null, null, '', null, '106');
INSERT INTO `business_brand` VALUES ('107', '坤阳', '坤阳', null, null, null, '', null, '107');
INSERT INTO `business_brand` VALUES ('108', '壶一春', '壶一春', null, null, null, '', null, '108');
INSERT INTO `business_brand` VALUES ('109', '夏君樂', '夏君樂', null, null, null, '', null, '109');
INSERT INTO `business_brand` VALUES ('110', '夜郎村', '夜郎村', null, null, null, '', null, '110');
INSERT INTO `business_brand` VALUES ('111', '大地迎春', '大地迎春', null, null, null, '', null, '111');
INSERT INTO `business_brand` VALUES ('112', '大板水', '大板水', null, null, null, '', null, '112');
INSERT INTO `business_brand` VALUES ('113', '大桥道', '大桥道', null, null, null, '', null, '113');
INSERT INTO `business_brand` VALUES ('114', '天城香', '天城香', null, null, null, '', null, '114');
INSERT INTO `business_brand` VALUES ('115', '天强', '天强', null, null, null, '', null, '115');
INSERT INTO `business_brand` VALUES ('116', '天淇乐', '天淇乐', null, null, null, '', null, '116');
INSERT INTO `business_brand` VALUES ('117', '天源', '天源', null, null, null, '', null, '117');
INSERT INTO `business_brand` VALUES ('118', '天源翔', '天源翔', null, null, null, '', null, '118');
INSERT INTO `business_brand` VALUES ('119', '天瀑', '天瀑', null, null, null, '', null, '119');
INSERT INTO `business_brand` VALUES ('120', '天车', '天车', null, null, null, '', null, '120');
INSERT INTO `business_brand` VALUES ('121', '奇香源', '奇香源', null, null, null, '', null, '121');
INSERT INTO `business_brand` VALUES ('122', '好+1', '好+1', null, null, null, '', null, '122');
INSERT INTO `business_brand` VALUES ('123', '好人家', '好人家', null, null, null, '', null, '123');
INSERT INTO `business_brand` VALUES ('124', '好利来', '好利来', null, null, null, '', null, '124');
INSERT INTO `business_brand` VALUES ('125', '好友', '好友', null, null, null, '', null, '125');
INSERT INTO `business_brand` VALUES ('126', '好呷鬼', '好呷鬼', null, null, null, '', null, '126');
INSERT INTO `business_brand` VALUES ('127', '好茗翠柳', '好茗翠柳', null, null, null, '', null, '127');
INSERT INTO `business_brand` VALUES ('128', '妙恋', '妙恋', null, null, null, '', null, '128');
INSERT INTO `business_brand` VALUES ('129', '娃哈哈', '娃哈哈', null, null, null, '', null, '129');
INSERT INTO `business_brand` VALUES ('130', '安亲', '安亲', null, null, null, '', null, '130');
INSERT INTO `business_brand` VALUES ('131', '宏興隆', '宏興隆', null, null, null, '', null, '131');
INSERT INTO `business_brand` VALUES ('132', '宝立', '宝立', null, null, null, '', null, '132');
INSERT INTO `business_brand` VALUES ('133', '小数点', '小数点', null, null, null, '', null, '133');
INSERT INTO `business_brand` VALUES ('134', '小飞龙', '小飞龙', null, null, null, '', null, '134');
INSERT INTO `business_brand` VALUES ('135', '山城', '山城', null, null, null, '', null, '135');
INSERT INTO `business_brand` VALUES ('136', '山尔', '山尔', null, null, null, '', null, '136');
INSERT INTO `business_brand` VALUES ('137', '山珍', '山珍', null, null, null, '', null, '137');
INSERT INTO `business_brand` VALUES ('138', '峒河', '峒河', null, null, null, '', null, '138');
INSERT INTO `business_brand` VALUES ('139', '布乡', '布乡', null, null, null, '', null, '139');
INSERT INTO `business_brand` VALUES ('140', '希之源', '希之源', null, null, null, '', null, '140');
INSERT INTO `business_brand` VALUES ('141', '年润', '年润', null, null, null, '', null, '141');
INSERT INTO `business_brand` VALUES ('142', '康乐滋', '康乐滋', null, null, null, '', null, '142');
INSERT INTO `business_brand` VALUES ('143', '康利', '康利', null, null, null, '', null, '143');
INSERT INTO `business_brand` VALUES ('144', '康师傅', '康师傅', null, null, null, '', null, '144');
INSERT INTO `business_brand` VALUES ('145', '康師傅', '康師傅', null, null, null, '', null, '145');
INSERT INTO `business_brand` VALUES ('146', '康源', '康源', null, null, null, '', null, '146');
INSERT INTO `business_brand` VALUES ('147', '康赞', '康赞', null, null, null, '', null, '147');
INSERT INTO `business_brand` VALUES ('148', '康辉', '康辉', null, null, null, '', null, '148');
INSERT INTO `business_brand` VALUES ('149', '强人', '强人', null, null, null, '', null, '149');
INSERT INTO `business_brand` VALUES ('150', '德毅', '德毅', null, null, null, '', null, '150');
INSERT INTO `business_brand` VALUES ('151', '心静如水', '心静如水', null, null, null, '', null, '151');
INSERT INTO `business_brand` VALUES ('152', '快乐王子', '快乐王子', null, null, null, '', null, '152');
INSERT INTO `business_brand` VALUES ('153', '快活林', '快活林', null, null, null, '', null, '153');
INSERT INTO `business_brand` VALUES ('154', '思念', '思念', null, null, null, '', null, '154');
INSERT INTO `business_brand` VALUES ('155', '恒的', '恒的', null, null, null, '', null, '155');
INSERT INTO `business_brand` VALUES ('156', '想我香', '想我香', null, null, null, '', null, '156');
INSERT INTO `business_brand` VALUES ('157', '我滴', '我滴', null, null, null, '', null, '157');
INSERT INTO `business_brand` VALUES ('158', '扬百利', '扬百利', null, null, null, '', null, '158');
INSERT INTO `business_brand` VALUES ('159', '挞扒洞', '挞扒洞', null, null, null, '', null, '159');
INSERT INTO `business_brand` VALUES ('160', '振旺', '振旺', null, null, null, '', null, '160');
INSERT INTO `business_brand` VALUES ('161', '捷品', '捷品', null, null, null, '', null, '161');
INSERT INTO `business_brand` VALUES ('162', '新桥', '新桥', null, null, null, '', null, '162');
INSERT INTO `business_brand` VALUES ('163', '无意', '无意', null, null, null, '', null, '163');
INSERT INTO `business_brand` VALUES ('164', '无穷', '无穷', null, null, null, '', null, '164');
INSERT INTO `business_brand` VALUES ('165', '旺口福', '旺口福', null, null, null, '', null, '165');
INSERT INTO `business_brand` VALUES ('166', '旺旺', '旺旺', null, null, null, '', null, '166');
INSERT INTO `business_brand` VALUES ('167', '普天', '普天', null, null, null, '', null, '167');
INSERT INTO `business_brand` VALUES ('168', '普江', '普江', null, null, null, '', null, '168');
INSERT INTO `business_brand` VALUES ('169', '景丽华', '景丽华', null, null, null, '', null, '169');
INSERT INTO `business_brand` VALUES ('170', '景威', '景威', null, null, null, '', null, '170');
INSERT INTO `business_brand` VALUES ('171', '智仁', '智仁', null, null, null, '', null, '171');
INSERT INTO `business_brand` VALUES ('172', '有友', '有友', null, null, null, '', null, '172');
INSERT INTO `business_brand` VALUES ('173', '朱家山', '朱家山', null, null, null, '', null, '173');
INSERT INTO `business_brand` VALUES ('174', '村姑', '村姑', null, null, null, '', null, '174');
INSERT INTO `business_brand` VALUES ('175', '杜威盛', '杜威盛', null, null, null, '', null, '175');
INSERT INTO `business_brand` VALUES ('176', '杜家庄', '杜家庄', null, null, null, '', null, '176');
INSERT INTO `business_brand` VALUES ('177', '杜鹃山泉', '杜鹃山泉', null, null, null, '', null, '177');
INSERT INTO `business_brand` VALUES ('178', '杨协成', '杨协成', null, null, null, '', null, '178');
INSERT INTO `business_brand` VALUES ('179', '杨梅山泉', '杨梅山泉', null, null, null, '', null, '179');
INSERT INTO `business_brand` VALUES ('180', '杨百利', '杨百利', null, null, null, '', null, '180');
INSERT INTO `business_brand` VALUES ('181', '松竹音', '松竹音', null, null, null, '', null, '181');
INSERT INTO `business_brand` VALUES ('182', '果王', '果王', null, null, null, '', null, '182');
INSERT INTO `business_brand` VALUES ('183', '果粒多', '果粒多', null, null, null, '', null, '183');
INSERT INTO `business_brand` VALUES ('184', '柯想', '柯想', null, null, null, '', null, '184');
INSERT INTO `business_brand` VALUES ('185', '栗子园', '栗子园', null, null, null, '', null, '185');
INSERT INTO `business_brand` VALUES ('186', '格蕾美', '格蕾美', null, null, null, '', null, '186');
INSERT INTO `business_brand` VALUES ('187', '桴焉茶业', '桴焉茶业', null, null, null, '', null, '187');
INSERT INTO `business_brand` VALUES ('188', '梦龙', '梦龙', null, null, null, '', null, '188');
INSERT INTO `business_brand` VALUES ('189', '梵净', '梵净', null, null, null, '', null, '189');
INSERT INTO `business_brand` VALUES ('190', '梵净山', '梵净山', null, null, null, '', null, '190');
INSERT INTO `business_brand` VALUES ('191', '梵绿', '梵绿', null, null, null, '', null, '191');
INSERT INTO `business_brand` VALUES ('192', '椰星', '椰星', null, null, null, '', null, '192');
INSERT INTO `business_brand` VALUES ('193', '欣龍', '欣龍', null, null, null, '', null, '193');
INSERT INTO `business_brand` VALUES ('194', '步步为赢', '步步为赢', null, null, null, '', null, '194');
INSERT INTO `business_brand` VALUES ('195', '毛克翕', '毛克翕', null, null, null, '', null, '195');
INSERT INTO `business_brand` VALUES ('196', '毛哥', '毛哥', null, null, null, '', null, '196');
INSERT INTO `business_brand` VALUES ('197', '水溶C100', '水溶C100', null, null, null, '', null, '197');
INSERT INTO `business_brand` VALUES ('198', '水金豆', '水金豆', null, null, null, '', null, '198');
INSERT INTO `business_brand` VALUES ('199', '永健', '永健', null, null, null, '', null, '199');
INSERT INTO `business_brand` VALUES ('200', '汇源', '汇源', null, null, null, '', null, '200');
INSERT INTO `business_brand` VALUES ('201', '沙地茶园', '沙地茶园', null, null, null, '', null, '201');
INSERT INTO `business_brand` VALUES ('202', '河江', '河江', null, null, null, '', null, '202');
INSERT INTO `business_brand` VALUES ('203', '泓泰', '泓泰', null, null, null, '', null, '203');
INSERT INTO `business_brand` VALUES ('204', '泡王', '泡王', null, null, null, '', null, '204');
INSERT INTO `business_brand` VALUES ('205', '泸源', '泸源', null, null, null, '', null, '205');
INSERT INTO `business_brand` VALUES ('206', '洞桥', '洞桥', null, null, null, '', null, '206');
INSERT INTO `business_brand` VALUES ('207', '浩然山泉', '浩然山泉', null, null, null, '', null, '207');
INSERT INTO `business_brand` VALUES ('208', '海天', '海天', null, null, null, '', null, '208');
INSERT INTO `business_brand` VALUES ('209', '润田', '润田', null, null, null, '', null, '209');
INSERT INTO `business_brand` VALUES ('210', '清友园', '清友园', null, null, null, '', null, '210');
INSERT INTO `business_brand` VALUES ('211', '清合园', '清合园', null, null, null, '', null, '211');
INSERT INTO `business_brand` VALUES ('212', '清味园', '清味园', null, null, null, '', null, '212');
INSERT INTO `business_brand` VALUES ('213', '清水芸香', '清水芸香', null, null, null, '', null, '213');
INSERT INTO `business_brand` VALUES ('214', '温莎威尔', '温莎威尔', null, null, null, '', null, '214');
INSERT INTO `business_brand` VALUES ('215', '湄江', '湄江', null, null, null, '', null, '215');
INSERT INTO `business_brand` VALUES ('216', '滨都', '滨都', null, null, null, '', null, '216');
INSERT INTO `business_brand` VALUES ('217', '滴水岩', '滴水岩', null, null, null, '', null, '217');
INSERT INTO `business_brand` VALUES ('218', '漓泉', '漓泉', null, null, null, '', null, '218');
INSERT INTO `business_brand` VALUES ('219', '潘祥记', '潘祥记', null, null, null, '', null, '219');
INSERT INTO `business_brand` VALUES ('220', '潮汕好味来', '潮汕好味来', null, null, null, '', null, '220');
INSERT INTO `business_brand` VALUES ('221', '澳地澳', '澳地澳', null, null, null, '', null, '221');
INSERT INTO `business_brand` VALUES ('222', '激浪', '激浪', null, null, null, '', null, '222');
INSERT INTO `business_brand` VALUES ('223', '熊大妈', '熊大妈', null, null, null, '', null, '223');
INSERT INTO `business_brand` VALUES ('224', '燕京', '燕京', null, null, null, '', null, '224');
INSERT INTO `business_brand` VALUES ('225', '爱美琪', '爱美琪', null, null, null, '', null, '225');
INSERT INTO `business_brand` VALUES ('226', '玉龙', '玉龙', null, null, null, '', null, '226');
INSERT INTO `business_brand` VALUES ('227', '珍洲绿', '珍洲绿', null, null, null, '', null, '227');
INSERT INTO `business_brand` VALUES ('228', '瑞丽江', '瑞丽江', null, null, null, '', null, '228');
INSERT INTO `business_brand` VALUES ('229', '瑞博', '瑞博', null, null, null, '', null, '229');
INSERT INTO `business_brand` VALUES ('230', '瓯南龙翔', '瓯南龙翔', null, null, null, '', null, '230');
INSERT INTO `business_brand` VALUES ('231', '甘露康', '甘露康', null, null, null, '', null, '231');
INSERT INTO `business_brand` VALUES ('232', '甜甜雪', '甜甜雪', null, null, null, '', null, '232');
INSERT INTO `business_brand` VALUES ('233', '甲比特', '甲比特', null, null, null, '', null, '233');
INSERT INTO `business_brand` VALUES ('234', '白沙清泉', '白沙清泉', null, null, null, '', null, '234');
INSERT INTO `business_brand` VALUES ('235', '百万宝贝', '百万宝贝', null, null, null, '', null, '235');
INSERT INTO `business_brand` VALUES ('236', '百事', '百事', null, null, null, '', null, '236');
INSERT INTO `business_brand` VALUES ('237', '百事可乐', '百事可乐', null, null, null, '', null, '237');
INSERT INTO `business_brand` VALUES ('238', '百森', '百森', null, null, null, '', null, '238');
INSERT INTO `business_brand` VALUES ('239', '百灵鸟', '百灵鸟', null, null, null, '', null, '239');
INSERT INTO `business_brand` VALUES ('240', '百花林泉', '百花林泉', null, null, null, '', null, '240');
INSERT INTO `business_brand` VALUES ('241', '百里锦江', '百里锦江', null, null, null, '', null, '241');
INSERT INTO `business_brand` VALUES ('242', '益和源', '益和源', null, null, null, '', null, '242');
INSERT INTO `business_brand` VALUES ('243', '盘龍山', '盘龍山', null, null, null, '', null, '243');
INSERT INTO `business_brand` VALUES ('244', '真田', '真田', null, null, null, '', null, '244');
INSERT INTO `business_brand` VALUES ('245', '石乳', '石乳', null, null, null, '', null, '245');
INSERT INTO `business_brand` VALUES ('246', '祐康', '祐康', null, null, null, '', null, '246');
INSERT INTO `business_brand` VALUES ('247', '神仙乐', '神仙乐', null, null, null, '', null, '247');
INSERT INTO `business_brand` VALUES ('248', '立顿', '立顿', null, null, null, '', null, '248');
INSERT INTO `business_brand` VALUES ('249', '立香香', '立香香', null, null, null, '', null, '249');
INSERT INTO `business_brand` VALUES ('250', '红坊人', '红坊人', null, null, null, '', null, '250');
INSERT INTO `business_brand` VALUES ('251', '红湖', '红湖', null, null, null, '', null, '251');
INSERT INTO `business_brand` VALUES ('252', '红钻', '红钻', null, null, null, '', null, '252');
INSERT INTO `business_brand` VALUES ('253', '纯果乐', '纯果乐', null, null, null, '', null, '253');
INSERT INTO `business_brand` VALUES ('254', '统一', '统一', null, null, null, '', null, '254');
INSERT INTO `business_brand` VALUES ('255', '维维', '维维', null, null, null, '', null, '255');
INSERT INTO `business_brand` VALUES ('256', '绿叶牌', '绿叶牌', null, null, null, '', null, '256');
INSERT INTO `business_brand` VALUES ('257', '绿地缘', '绿地缘', null, null, null, '', null, '257');
INSERT INTO `business_brand` VALUES ('258', '绿杰', '绿杰', null, null, null, '', null, '258');
INSERT INTO `business_brand` VALUES ('259', '羊艾', '羊艾', null, null, null, '', null, '259');
INSERT INTO `business_brand` VALUES ('260', '美乐', '美乐', null, null, null, '', null, '260');
INSERT INTO `business_brand` VALUES ('261', '美优特', '美优特', null, null, null, '', null, '261');
INSERT INTO `business_brand` VALUES ('262', '美伦', '美伦', null, null, null, '', null, '262');
INSERT INTO `business_brand` VALUES ('263', '美咪', '美咪', null, null, null, '', null, '263');
INSERT INTO `business_brand` VALUES ('264', '美好', '美好', null, null, null, '', null, '264');
INSERT INTO `business_brand` VALUES ('265', '美年达', '美年达', null, null, null, '', null, '265');
INSERT INTO `business_brand` VALUES ('266', '美怡乐', '美怡乐', null, null, null, '', null, '266');
INSERT INTO `business_brand` VALUES ('267', '美汁源', '美汁源', null, null, null, '', null, '267');
INSERT INTO `business_brand` VALUES ('268', '翠绿', '翠绿', null, null, null, '', null, '268');
INSERT INTO `business_brand` VALUES ('269', '老家人', '老家人', null, null, null, '', null, '269');
INSERT INTO `business_brand` VALUES ('270', '老泉水', '老泉水', null, null, null, '', null, '270');
INSERT INTO `business_brand` VALUES ('271', '胖四娘', '胖四娘', null, null, null, '', null, '271');
INSERT INTO `business_brand` VALUES ('272', '艾尔', '艾尔', null, null, null, '', null, '272');
INSERT INTO `business_brand` VALUES ('273', '芬达', '芬达', null, null, null, '', null, '273');
INSERT INTO `business_brand` VALUES ('274', '苗乡情', '苗乡情', null, null, null, '', null, '274');
INSERT INTO `business_brand` VALUES ('275', '苗苗纯', '苗苗纯', null, null, null, '', null, '275');
INSERT INTO `business_brand` VALUES ('276', '茅台', '茅台', null, null, null, '', null, '276');
INSERT INTO `business_brand` VALUES ('277', '茗中鹤', '茗中鹤', null, null, null, '', null, '277');
INSERT INTO `business_brand` VALUES ('278', '莎辣咪', '莎辣咪', null, null, null, '', null, '278');
INSERT INTO `business_brand` VALUES ('279', '蒙牛', '蒙牛', null, null, null, '', null, '279');
INSERT INTO `business_brand` VALUES ('280', '蓝带', '蓝带', null, null, null, '', null, '280');
INSERT INTO `business_brand` VALUES ('281', '蓝狮', '蓝狮', null, null, null, '', null, '281');
INSERT INTO `business_brand` VALUES ('282', '蓝色激情', '蓝色激情', null, null, null, '', null, '282');
INSERT INTO `business_brand` VALUES ('283', '蓝色经典', '蓝色经典', null, null, null, '', null, '283');
INSERT INTO `business_brand` VALUES ('284', '蓝贝', '蓝贝', null, null, null, '', null, '284');
INSERT INTO `business_brand` VALUES ('285', '蓝贝啤酒', '蓝贝啤酒', null, null, null, '', null, '285');
INSERT INTO `business_brand` VALUES ('286', '蓝鼎', '蓝鼎', null, null, null, '', null, '286');
INSERT INTO `business_brand` VALUES ('287', '蘑菇石', '蘑菇石', null, null, null, '', null, '287');
INSERT INTO `business_brand` VALUES ('288', '蜡笔小新', '蜡笔小新', null, null, null, '', null, '288');
INSERT INTO `business_brand` VALUES ('289', '西麦', '西麦', null, null, null, '', null, '289');
INSERT INTO `business_brand` VALUES ('290', '见图标', '见图标', null, null, null, '', null, '290');
INSERT INTO `business_brand` VALUES ('291', '观音缘', '观音缘', null, null, null, '', null, '291');
INSERT INTO `business_brand` VALUES ('292', '贵州龍', '贵州龍', null, null, null, '', null, '292');
INSERT INTO `business_brand` VALUES ('293', '贵州龙', '贵州龙', null, null, null, '', null, '293');
INSERT INTO `business_brand` VALUES ('294', '贵醇源', '贵醇源', null, null, null, '', null, '294');
INSERT INTO `business_brand` VALUES ('295', '贵隆茶', '贵隆茶', null, null, null, '', null, '295');
INSERT INTO `business_brand` VALUES ('296', '辣博士', '辣博士', null, null, null, '', null, '296');
INSERT INTO `business_brand` VALUES ('297', '辣来主义', '辣来主义', null, null, null, '', null, '297');
INSERT INTO `business_brand` VALUES ('298', '达利园', '达利园', null, null, null, '', null, '298');
INSERT INTO `business_brand` VALUES ('299', '选中你', '选中你', null, null, null, '', null, '299');
INSERT INTO `business_brand` VALUES ('300', '逍遥派', '逍遥派', null, null, null, '', null, '300');
INSERT INTO `business_brand` VALUES ('301', '都匀毛峰', '都匀毛峰', null, null, null, '', null, '301');
INSERT INTO `business_brand` VALUES ('302', '醇品', '醇品', null, null, null, '', null, '302');
INSERT INTO `business_brand` VALUES ('303', '醇都', '醇都', null, null, null, '', null, '303');
INSERT INTO `business_brand` VALUES ('304', '醒目', '醒目', null, null, null, '', null, '304');
INSERT INTO `business_brand` VALUES ('305', '重都', '重都', null, null, null, '', null, '305');
INSERT INTO `business_brand` VALUES ('306', '野人谷', '野人谷', null, null, null, '', null, '306');
INSERT INTO `business_brand` VALUES ('307', '野春菇', '野春菇', null, null, null, '', null, '307');
INSERT INTO `business_brand` VALUES ('308', '金威', '金威', null, null, null, '', null, '308');
INSERT INTO `business_brand` VALUES ('309', '金威啤酒', '金威啤酒', null, null, null, '', null, '309');
INSERT INTO `business_brand` VALUES ('310', '金崂', '金崂', null, null, null, '', null, '310');
INSERT INTO `business_brand` VALUES ('311', '金星', '金星', null, null, null, '', null, '311');
INSERT INTO `business_brand` VALUES ('312', '金果源', '金果源', null, null, null, '', null, '312');
INSERT INTO `business_brand` VALUES ('313', '金爱丽', '金爱丽', null, null, null, '', null, '313');
INSERT INTO `business_brand` VALUES ('314', '金禾', '金禾', null, null, null, '', null, '314');
INSERT INTO `business_brand` VALUES ('315', '鑫丰', '鑫丰', null, null, null, '', null, '315');
INSERT INTO `business_brand` VALUES ('316', '鑫球', '鑫球', null, null, null, '', null, '316');
INSERT INTO `business_brand` VALUES ('317', '铜巩山', '铜巩山', null, null, null, '', null, '317');
INSERT INTO `business_brand` VALUES ('318', '银溪', '银溪', null, null, null, '', null, '318');
INSERT INTO `business_brand` VALUES ('319', '银鹭', '银鹭', null, null, null, '', null, '319');
INSERT INTO `business_brand` VALUES ('320', '陈老根', '陈老根', null, null, null, '', null, '320');
INSERT INTO `business_brand` VALUES ('321', '陶华碧', '陶华碧', null, null, null, '', null, '321');
INSERT INTO `business_brand` VALUES ('322', '雀巢', '雀巢', null, null, null, '', null, '322');
INSERT INTO `business_brand` VALUES ('323', '雀巢咖啡', '雀巢咖啡', null, null, null, '', null, '323');
INSERT INTO `business_brand` VALUES ('324', '雀巢果维', '雀巢果维', null, null, null, '', null, '324');
INSERT INTO `business_brand` VALUES ('325', '雅士利', '雅士利', null, null, null, '', null, '325');
INSERT INTO `business_brand` VALUES ('326', '雅点', '雅点', null, null, null, '', null, '326');
INSERT INTO `business_brand` VALUES ('327', '雅芙', '雅芙', null, null, null, '', null, '327');
INSERT INTO `business_brand` VALUES ('328', '雪利', '雪利', null, null, null, '', null, '328');
INSERT INTO `business_brand` VALUES ('329', '雪碧', '雪碧', null, null, null, '', null, '329');
INSERT INTO `business_brand` VALUES ('330', '雪花', '雪花', null, null, null, '', null, '330');
INSERT INTO `business_brand` VALUES ('331', '霸豪丰', '霸豪丰', null, null, null, '', null, '331');
INSERT INTO `business_brand` VALUES ('332', '霸龙', '霸龙', null, null, null, '', null, '332');
INSERT INTO `business_brand` VALUES ('333', '青岛', '青岛', null, null, null, '', null, '333');
INSERT INTO `business_brand` VALUES ('334', '青岛啤酒', '青岛啤酒', null, null, null, '', null, '334');
INSERT INTO `business_brand` VALUES ('335', '青苹果', '青苹果', null, null, null, '', null, '335');
INSERT INTO `business_brand` VALUES ('336', '非常', '非常', null, null, null, '', null, '336');
INSERT INTO `business_brand` VALUES ('337', '风靡·情', '风靡·情', null, null, null, '', null, '337');
INSERT INTO `business_brand` VALUES ('338', '飞龙峡', '飞龙峡', null, null, null, '', null, '338');
INSERT INTO `business_brand` VALUES ('339', '飞龙雨', '飞龙雨', null, null, null, '', null, '339');
INSERT INTO `business_brand` VALUES ('340', '香仔', '香仔', null, null, null, '', null, '340');
INSERT INTO `business_brand` VALUES ('341', '香勃禄', '香勃禄', null, null, null, '', null, '341');
INSERT INTO `business_brand` VALUES ('342', '香味佬', '香味佬', null, null, null, '', null, '342');
INSERT INTO `business_brand` VALUES ('343', '香如许', '香如许', null, null, null, '', null, '343');
INSERT INTO `business_brand` VALUES ('344', '香必啃', '香必啃', null, null, null, '', null, '344');
INSERT INTO `business_brand` VALUES ('345', '香滋乡味', '香滋乡味', null, null, null, '', null, '345');
INSERT INTO `business_brand` VALUES ('346', '香滋香味', '香滋香味', null, null, null, '', null, '346');
INSERT INTO `business_brand` VALUES ('347', '香约', '香约', null, null, null, '', null, '347');
INSERT INTO `business_brand` VALUES ('348', '香飘飘', '香飘飘', null, null, null, '', null, '348');
INSERT INTO `business_brand` VALUES ('349', '馨泰', '馨泰', null, null, null, '', null, '349');
INSERT INTO `business_brand` VALUES ('350', '高乐高', '高乐高', null, null, null, '', null, '350');
INSERT INTO `business_brand` VALUES ('351', '高原', '高原', null, null, null, '', null, '351');
INSERT INTO `business_brand` VALUES ('352', '高樂高', '高樂高', null, null, null, '', null, '352');
INSERT INTO `business_brand` VALUES ('353', '鲜八里', '鲜八里', null, null, null, '', null, '353');
INSERT INTO `business_brand` VALUES ('354', '鲜果粒', '鲜果粒', null, null, null, '', null, '354');
INSERT INTO `business_brand` VALUES ('355', '黑牛', '黑牛', null, null, null, '', null, '355');
INSERT INTO `business_brand` VALUES ('356', '黔中缘', '黔中缘', null, null, null, '', null, '356');
INSERT INTO `business_brand` VALUES ('357', '黔匀', '黔匀', null, null, null, '', null, '357');
INSERT INTO `business_brand` VALUES ('358', '黔北泉', '黔北泉', null, null, null, '', null, '358');
INSERT INTO `business_brand` VALUES ('359', '黔山秀水', '黔山秀水', null, null, null, '', null, '359');
INSERT INTO `business_brand` VALUES ('360', '黔萃园', '黔萃园', null, null, null, '', null, '360');
INSERT INTO `business_brand` VALUES ('361', '龍雲', '龍雲', null, null, null, '', null, '361');
INSERT INTO `business_brand` VALUES ('362', '龍鳯', '龍鳯', null, null, null, '', null, '362');
INSERT INTO `business_brand` VALUES ('363', '龙凤', '龙凤', null, null, null, '', null, '363');
INSERT INTO `business_brand` VALUES ('364', '龙旺', '龙旺', null, null, null, '', null, '364');
INSERT INTO `business_brand` VALUES ('365', '龙马江', '龙马江', null, null, null, '', null, '365');
INSERT INTO `business_brand` VALUES ('366', '﹡ZC6702769 SL﹡', '﹡ZC6702769 SL﹡', null, null, null, '', null, '366');
INSERT INTO `business_brand` VALUES ('367', '山花', '山花', null, null, null, '', null, '371');
INSERT INTO `business_brand` VALUES ('368', '老干爹', '老干爹', null, null, null, '', null, '379');
INSERT INTO `business_brand` VALUES ('369', '国台', '国台', null, null, null, '', null, '380');
INSERT INTO `business_brand` VALUES ('370', '兰馨', '兰馨', null, null, null, '', null, '381');
INSERT INTO `business_brand` VALUES ('371', '味莼园', '味莼园', null, null, null, '', null, '382');
INSERT INTO `business_brand` VALUES ('372', '黔五福', '黔五福', null, null, null, '', null, '378');
INSERT INTO `business_brand` VALUES ('373', '芙蓉食府', '芙蓉食府', null, null, null, null, null, '383');
INSERT INTO `business_brand` VALUES ('374', '何标', '何标', null, null, null, null, null, '384');
INSERT INTO `business_brand` VALUES ('375', '乾苑', '乾苑', null, null, null, null, null, '386');
INSERT INTO `business_brand` VALUES ('10086', '六合福思', '六合福思', null, null, null, '', null, '10086');
INSERT INTO `business_brand` VALUES ('10090', '雅培', null, null, null, null, '', null, '10094');
INSERT INTO `business_brand` VALUES ('10091', '惠氏', null, null, null, null, '', null, '10095');
INSERT INTO `business_brand` VALUES ('10092', '合生元', null, null, null, null, '', null, '10099');

-- ----------------------------
-- Table structure for `business_category`
-- ----------------------------
DROP TABLE IF EXISTS `business_category`;
CREATE TABLE `business_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_category
-- ----------------------------

-- ----------------------------
-- Table structure for `business_type`
-- ----------------------------
DROP TABLE IF EXISTS `business_type`;
CREATE TABLE `business_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_type
-- ----------------------------
INSERT INTO `business_type` VALUES ('1', '10001', '制造企业');
INSERT INTO `business_type` VALUES ('2', '10002', '研发企业\r\n');

-- ----------------------------
-- Table structure for `business_unit`
-- ----------------------------
DROP TABLE IF EXISTS `business_unit`;
CREATE TABLE `business_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address2` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `website` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` bigint(20) DEFAULT NULL,
  `category` bigint(20) DEFAULT NULL,
  `license_no` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contact` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `note` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qs_no` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`),
  KEY `fk_business_unit_business_category` (`category`),
  KEY `fk_business_unit_business_type` (`type`),
  CONSTRAINT `fk_business_unit_business_category` FOREIGN KEY (`category`) REFERENCES `product_category` (`id`),
  CONSTRAINT `fk_business_unit_business_type` FOREIGN KEY (`type`) REFERENCES `business_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10101 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_unit
-- ----------------------------
INSERT INTO `business_unit` VALUES ('10', 'NESCAFE', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('11', 'Nestle', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('12', 'Nestle\'', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('13', 'PabjtBLUERIbbon', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('14', 'PEPSI', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('15', 'PEPSI百事可乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('16', 'Rich Blend', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('17', 'Sprite', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('18', 'SSS', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('19', 'Tropicana', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('20', 'Tropicana纯果乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('21', 'XLX', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('22', '一万响', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('23', '一壶春', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('24', '一桶江湖', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('25', '一米阳光', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('26', '七叔公', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('27', '七味', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('28', '七喜', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('29', '七箭', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('30', '万家欢', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('31', '万林', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('32', '万顺豪', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('33', '三代行', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('34', '三全', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('35', '三全凌', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('36', '世纪福', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('37', '东明思', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('38', '中华山', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('39', '中国茗茶', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('40', '中沃', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('41', '中绿', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('42', '乐源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('43', '乐满家', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('44', '乖媳妇', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('45', '九洞天', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('46', '九福泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('47', '九龙泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('48', '乡太佬', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('49', '乡妹子', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('50', '乡巴佬', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('51', '乡音', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('52', '二酉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('53', '云井', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('54', '云仰', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('55', '云鹤食品', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('56', '五芳斋', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('57', '仡佬山', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('58', '仰阿莎', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('59', '伊利', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('60', '优乐美', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('61', '余六妹', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('62', '佰豆集', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('63', '佳香旺', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('64', '佳香肚', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('65', '假日', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('66', '健力宝', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('67', '健娃', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('68', '億农', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('69', '億龍', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('70', '六六红', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('71', '六广河', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('72', '农夫山泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('73', '农夫果园', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('74', '冰极零', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('75', '冰泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('76', '冰点', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('77', '凉泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('78', '凤凰山', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('79', '凯维', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('80', '刘胡子', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('81', '利兴达', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('82', '北极熊', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('83', '千古韵', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('84', '华雄', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('85', '南国', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('86', '南岛河', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('87', '南方', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('88', '卡夫', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('89', '友伦', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('90', '双佳龙', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('91', '双汇', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('92', '可口可乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('93', '吐香', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('94', '周君记', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('95', '呷比特', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('96', '和兴隆', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('97', '和路雪', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('98', '咖啡伴侣', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('99', '品馨', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('100', '哇哈哈', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('101', '喜之郎', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('102', '喜鹭', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('103', '嘉港', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('104', '图形商标', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('105', '图标见瓶身', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('106', '圣罗黑啤', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('107', '坤阳', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('108', '壶一春', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('109', '夏君樂', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('110', '夜郎村', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('111', '大地迎春', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('112', '大板水', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('113', '大桥道', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('114', '天城香', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('115', '天强', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('116', '天淇乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('117', '天源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('118', '天源翔', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('119', '天瀑', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('120', '天车', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('121', '奇香源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('122', '好+1', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('123', '好人家', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('124', '好利来', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('125', '好友', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('126', '好呷鬼', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('127', '好茗翠柳', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('128', '妙恋', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('129', '娃哈哈', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('130', '安亲', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('131', '宏興隆', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('132', '宝立', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('133', '小数点', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('134', '小飞龙', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('135', '山城', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('136', '山尔', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('137', '山珍', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('138', '峒河', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('139', '布乡', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('140', '希之源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('141', '年润', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('142', '康乐滋', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('143', '康利', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('144', '康师傅', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('145', '康師傅', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('146', '康源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('147', '康赞', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('148', '康辉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('149', '强人', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('150', '德毅', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('151', '心静如水', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('152', '快乐王子', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('153', '快活林', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('154', '思念', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('155', '恒的', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('156', '想我香', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('157', '我滴', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('158', '扬百利', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('159', '挞扒洞', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('160', '振旺', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('161', '捷品', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('162', '新桥', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('163', '无意', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('164', '无穷', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('165', '旺口福', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('166', '旺旺', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('167', '普天', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('168', '普江', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('169', '景丽华', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('170', '景威', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('171', '智仁', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('172', '有友', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('173', '朱家山', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('174', '村姑', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('175', '杜威盛', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('176', '杜家庄', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('177', '杜鹃山泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('178', '杨协成', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('179', '杨梅山泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('180', '杨百利', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('181', '松竹音', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('182', '果王', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('183', '果粒多', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('184', '柯想', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('185', '栗子园', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('186', '格蕾美', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('187', '桴焉茶业', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('188', '梦龙', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('189', '梵净', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('190', '梵净山', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('191', '梵绿', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('192', '椰星', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('193', '欣龍', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('194', '步步为赢', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('195', '毛克翕', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('196', '毛哥', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('197', '水溶C100', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('198', '水金豆', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('199', '永健', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('200', '汇源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('201', '沙地茶园', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('202', '河江', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('203', '泓泰', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('204', '泡王', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('205', '泸源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('206', '洞桥', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('207', '浩然山泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('208', '海天', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('209', '润田', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('210', '清友园', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('211', '清合园', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('212', '清味园', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('213', '清水芸香', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('214', '温莎威尔', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('215', '湄江', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('216', '滨都', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('217', '滴水岩', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('218', '漓泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('219', '潘祥记', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('220', '潮汕好味来', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('221', '澳地澳', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('222', '激浪', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('223', '熊大妈', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('224', '燕京', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('225', '爱美琪', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('226', '玉龙', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('227', '珍洲绿', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('228', '瑞丽江', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('229', '瑞博', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('230', '瓯南龙翔', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('231', '甘露康', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('232', '甜甜雪', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('233', '甲比特', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('234', '白沙清泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('235', '百万宝贝', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('236', 'A-百事', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('237', '百事可乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('238', '百森', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('239', '百灵鸟', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('240', '百花林泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('241', '百里锦江', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('242', '益和源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('243', '盘龍山', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('244', '真田', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('245', '石乳', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('246', '祐康', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('247', '神仙乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('248', '立顿', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('249', '立香香', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('250', '红坊人', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('251', '红湖', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('252', '红钻', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('253', '纯果乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('254', '统一', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('255', '维维', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('256', '绿叶牌', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('257', '绿地缘', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('258', '绿杰', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('259', '羊艾', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('260', '美乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('261', '美优特', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('262', '美伦', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('263', '美咪', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('264', '美好', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('265', '美年达', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('266', '美怡乐', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('267', '美汁源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('268', '翠绿', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('269', '老家人', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('270', '老泉水', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('271', '胖四娘', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('272', '艾尔', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('273', '芬达', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('274', '苗乡情', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('275', '苗苗纯', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('276', '茅台', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('277', '茗中鹤', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('278', '莎辣咪', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('279', 'A-蒙牛', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('280', '蓝带', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('281', '蓝狮', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('282', '蓝色激情', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('283', '蓝色经典', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('284', '蓝贝', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('285', '蓝贝啤酒', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('286', '蓝鼎', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('287', '蘑菇石', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('288', '蜡笔小新', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('289', '西麦', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('290', '见图标', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('291', '观音缘', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('292', '贵州龍', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('293', '贵州龙', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('294', '贵醇源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('295', '贵隆茶', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('296', '辣博士', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('297', '辣来主义', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('298', 'A-达利园', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('299', '选中你', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('300', '逍遥派', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('301', '都匀毛峰', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('302', '醇品', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('303', '醇都', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('304', '醒目', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('305', '重都', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('306', '野人谷', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('307', '野春菇', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('308', '金威', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('309', '金威啤酒', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('310', '金崂', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('311', '金星', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('312', '金果源', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('313', '金爱丽', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('314', '金禾', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('315', '鑫丰', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('316', '鑫球', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('317', '铜巩山', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('318', '银溪', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('319', '银鹭', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('320', '陈老根', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('321', '陶华碧', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('322', '雀巢', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('323', '雀巢咖啡', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('324', '雀巢果维', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('325', '雅士利', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('326', '雅点', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('327', '雅芙', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('328', '雪利', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('329', '雪碧', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('330', '雪花', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('331', '霸豪丰', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('332', '霸龙', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('333', '青岛', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('334', '青岛啤酒', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('335', '青苹果', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('336', '非常', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('337', '风靡·情', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('338', '飞龙峡', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('339', '飞龙雨', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('340', '香仔', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('341', '香勃禄', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('342', '香味佬', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('343', '香如许', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('344', '香必啃', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('345', '香滋乡味', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('346', '香滋香味', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('347', '香约', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('348', '香飘飘', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('349', '馨泰', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('350', '高乐高', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('351', '高原', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('352', '高樂高', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('353', '鲜八里', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('354', '鲜果粒', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('355', '黑牛', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('356', '黔中缘', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('357', '黔匀', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('358', '黔北泉', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('359', '黔山秀水', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('360', '黔萃园', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('361', '龍雲', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('362', '龍鳯', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('363', '龙凤', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('364', '龙旺', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('365', '龙马江', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('366', '﹡ZC6702769 SL﹡', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('371', 'A-贵州三联', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('373', '重庆百事公司', '中国重庆市渝北区', null, null, null, '1', null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('374', '云南达利食品有限公司', '玉溪市高新技术产业开发区九龙工业园区', null, null, null, '1', null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('375', '内蒙古蒙牛乳业（集团）股份有限公司', '内蒙古呼和浩特市和林格尔盛乐经济园区', null, null, null, '1', null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('376', '雀巢（中国）有限公司', '中国北京市朝阳区望京街8号', null, null, 'http://www.nestle.com.cn/', '1', null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('377', '贵阳三联乳业有限公司', '贵阳市新添寨乌当奶牛场 ', null, null, 'http://www.gyslry.com/', '1', null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('378', '贵州五福坊食品有限公司', '贵州省贵阳市新添大道114号', null, null, 'http://www.gzwff.com/index.aspx', null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('379', '贵州老干爹食品有限公司', '贵阳国家高新技术产业开发区顺海工业小区', null, null, 'http://www.laogandie.com/', null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('380', '贵州国台酒业有限公司', '贵州仁怀市茅台镇国酒社区', null, null, 'http://www.guotaiworld.com/', null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('381', '贵州湄潭兰馨茶业有限公司', '贵州省湄潭县协育绿色食品工业园区', null, null, 'http://www.lanxintea.com/', null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('382', '贵阳味莼园食品（集团）有限公司', '贵阳市如意巷28号1单元附5号', null, null, 'http://www.gywcy.com/', null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('383', '贵阳金阳湘水芙蓉食府', '贵阳花溪区田园路771号', null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('384', '红果何标粮油经营部', '', null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('385', '红果华夏中学', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('386', '六盘水市钟山区乾苑品味坊', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('10086', '六合福思科技有限公司', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('10087', '六合福思测试12312', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('10088', 'LHFS测试企业2', '西溪路525', null, null, null, null, null, null, null, null, null);
INSERT INTO `business_unit` VALUES ('10093', '四川蒙牛集团有限', '四川省成都XX区', null, null, null, null, null, '58459666584', null, null, '1548752649');
INSERT INTO `business_unit` VALUES ('10094', '新西兰', '新西兰(雅培)', null, null, null, null, null, '440108400005602', null, null, 'QS440105011779');
INSERT INTO `business_unit` VALUES ('10095', '新加坡（惠氏）', '新加坡', null, null, null, null, null, '320000400003325', null, null, 'QS320005020002');
INSERT INTO `business_unit` VALUES ('10098', '爱尔兰（惠氏）', '爱尔兰', null, null, null, null, null, '320000400003325', null, null, 'QS320005020002');
INSERT INTO `business_unit` VALUES ('10099', '广州市合生元生物制品有限公司', '广州经济技术开发区东区联广路187号生产车间2-3楼', null, null, null, null, null, '440108400008919', null, null, 'QS44010601827');

-- ----------------------------
-- Table structure for `business_unit_test`
-- ----------------------------
DROP TABLE IF EXISTS `business_unit_test`;
CREATE TABLE `business_unit_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_unit_id` bigint(20) DEFAULT NULL,
  `test_cycle_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_business_unit_test_business_unit` (`business_unit_id`),
  KEY `fk_business_unit_test_test_cycle` (`test_cycle_id`),
  CONSTRAINT `fk_business_unit_test_business_unit` FOREIGN KEY (`business_unit_id`) REFERENCES `business_unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of business_unit_test
-- ----------------------------

-- ----------------------------
-- Table structure for `certification`
-- ----------------------------
DROP TABLE IF EXISTS `certification`;
CREATE TABLE `certification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imgurl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '认证的图片url',
  `message` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '认证信息简介',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '认证名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of certification
-- ----------------------------
INSERT INTO `certification` VALUES ('1', 'http://211.151.134.74:8080/portal/img/certification/128/3C.jpg', null, '3C认证');
INSERT INTO `certification` VALUES ('2', 'http://211.151.134.74:8080/portal/img/certification/128/3environment.png', null, 'Ⅲ型环境标志');
INSERT INTO `certification` VALUES ('3', 'http://211.151.134.74:8080/portal/img/certification/128/CE.jpg', null, 'CE');
INSERT INTO `certification` VALUES ('4', 'http://211.151.134.74:8080/portal/img/certification/128/FCC.jpg', null, 'FCC');
INSERT INTO `certification` VALUES ('5', 'http://211.151.134.74:8080/portal/img/certification/128/ISO90012000.jpg', null, 'ISO9001 2000');
INSERT INTO `certification` VALUES ('6', 'http://211.151.134.74:8080/portal/img/certification/128/ISO9001.png', null, 'ISO9001');
INSERT INTO `certification` VALUES ('7', 'http://211.151.134.74:8080/portal/img/certification/128/ISO14000.png', null, 'ISO14000体系认证');
INSERT INTO `certification` VALUES ('8', 'http://211.151.134.74:8080/portal/img/certification/128/ISO22000.png', null, 'ISO22000食品安全管理体系');
INSERT INTO `certification` VALUES ('9', 'http://211.151.134.74:8080/portal/img/certification/128/baojianshipin.jpg', null, '保健食品');
INSERT INTO `certification` VALUES ('10', 'http://211.151.134.74:8080/portal/img/certification/128/guojiamianjianchanpin.jpg', null, '国家免检产品');
INSERT INTO `certification` VALUES ('11', 'http://211.151.134.74:8080/portal/img/certification/128/huaxiarenzheng.jpg', null, '华夏认证');
INSERT INTO `certification` VALUES ('12', 'http://211.151.134.74:8080/portal/img/certification/128/lvseshipin.jpg', null, '绿色食品');
INSERT INTO `certification` VALUES ('13', 'http://211.151.134.74:8080/portal/img/certification/128/QS.jpg', null, '生产许可');
INSERT INTO `certification` VALUES ('14', 'http://211.151.134.74:8080/portal/img/certification/128/sudongshipin.jpg', null, '速冻食品');
INSERT INTO `certification` VALUES ('15', 'http://211.151.134.74:8080/portal/img/certification/128/wugonghainongchanpin.png', null, '无公害农产品');
INSERT INTO `certification` VALUES ('16', 'http://211.151.134.74:8080/portal/img/certification/128/wugonghainongchanpinjiu.png', null, '无公害农产品 旧');
INSERT INTO `certification` VALUES ('17', 'http://211.151.134.74:8080/portal/img/certification/128/xunhuanzaishengbiaozhi.jpg', null, '循环再生标志');
INSERT INTO `certification` VALUES ('18', 'http://211.151.134.74:8080/portal/img/certification/128/youjishipin.jpg', null, '有机食品');
INSERT INTO `certification` VALUES ('19', 'http://211.151.134.74:8080/portal/img/certification/128/zhongguohuanjingbiaozhi.png', null, '中国环境标志');
INSERT INTO `certification` VALUES ('20', 'http://211.151.134.74:8080/portal/img/certification/128/zhongguohuanjingguanlitixirenzhengjigouguojiarenzheng.png', null, '中国环境管理体系认证机构国家认可');
INSERT INTO `certification` VALUES ('21', 'http://211.151.134.74:8080/portal/img/certification/128/zhongguomingpai.jpg', null, '中国名牌');
INSERT INTO `certification` VALUES ('22', 'http://211.151.134.74:8080/portal/img/certification/128/zhonghuarenmingongheguoyuanchandiyuchanpin.jpg', null, '中华人民共和国原产地域产品');
INSERT INTO `certification` VALUES ('23', 'http://211.151.134.74:8080/portal/img/certification/128/unknow.jpg', null, 'unknow');
INSERT INTO `certification` VALUES ('24', 'http://211.151.134.74:8080/portal/img/certification/128/ISO9001CQC.jpg', null, 'ISO9001 2008');
INSERT INTO `certification` VALUES ('25', 'http://211.151.134.74:8080/portal/img/certification/128/ISO14001SGS.jpg', null, 'ISO14001 2004环境管理体系');
INSERT INTO `certification` VALUES ('26', 'http://211.151.134.74:8080/portal/img/certification/128/OHSAS18001SGS.jpg', null, 'OHSAS18001 2007');
INSERT INTO `certification` VALUES ('27', null, null, '国家标准化良好行为AAA级企业认证');
INSERT INTO `certification` VALUES ('28', 'http://211.151.134.74:8080/portal/img/certification/128/ISO9001CQC.jpg', null, 'ISO9001 2008质量管理体系');
INSERT INTO `certification` VALUES ('29', null, null, 'ISO 22000 2005食品安全管理体系认证');
INSERT INTO `certification` VALUES ('30', null, null, '安全生产标准化三级企业认证');
INSERT INTO `certification` VALUES ('31', 'http://211.151.134.74:8080/portal/img/certification/128/ISO10012.jpg', null, 'ISO10012');
INSERT INTO `certification` VALUES ('32', 'http://211.151.134.74:8080/portal/img/certification/128/OHSAS18001SGS.jpg', null, 'ISO18001');

-- ----------------------------
-- Table structure for `fda`
-- ----------------------------
DROP TABLE IF EXISTS `fda`;
CREATE TABLE `fda` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_fda_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_fda_code` (`code`),
  KEY `fk_parent_fda` (`parent_fda_id`),
  CONSTRAINT `fk_parent_fda` FOREIGN KEY (`parent_fda_id`) REFERENCES `fda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda
-- ----------------------------
INSERT INTO `fda` VALUES ('1', '贵阳', null, null);
INSERT INTO `fda` VALUES ('3', 'fsn_cloud', null, null);

-- ----------------------------
-- Table structure for `fda_dependency`
-- ----------------------------
DROP TABLE IF EXISTS `fda_dependency`;
CREATE TABLE `fda_dependency` (
  `FDA_id` bigint(20) NOT NULL,
  `DEPENDENCY_FDA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`FDA_id`,`DEPENDENCY_FDA_ID`),
  KEY `FK__fda_dependency` (`DEPENDENCY_FDA_ID`),
  CONSTRAINT `FK_ssd__fda_dependency` FOREIGN KEY (`FDA_id`) REFERENCES `fda` (`id`),
  CONSTRAINT `FK__fda_dependency` FOREIGN KEY (`DEPENDENCY_FDA_ID`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_dependency
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_product_group`
-- ----------------------------
DROP TABLE IF EXISTS `fda_product_group`;
CREATE TABLE `fda_product_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_product_group
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_statement`
-- ----------------------------
DROP TABLE IF EXISTS `fda_statement`;
CREATE TABLE `fda_statement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `publish_date` datetime DEFAULT NULL,
  `content` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `product_instance_id` bigint(20) DEFAULT NULL,
  `fda_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fda_statement_product_instance` (`product_instance_id`),
  KEY `fk_fda_statement_fda` (`fda_id`),
  CONSTRAINT `fk_fda_statement_fda` FOREIGN KEY (`fda_id`) REFERENCES `fda` (`id`),
  CONSTRAINT `fk_fda_statement_product_instance` FOREIGN KEY (`product_instance_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_statement
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_test`
-- ----------------------------
DROP TABLE IF EXISTS `fda_test`;
CREATE TABLE `fda_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fda_id` bigint(20) DEFAULT NULL,
  `test_cycle_id` bigint(20) DEFAULT NULL,
  `comment` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fda_test_fda` (`fda_id`),
  KEY `fk_fda_test_test_cycle` (`test_cycle_id`),
  CONSTRAINT `fk_fda_test_fda` FOREIGN KEY (`fda_id`) REFERENCES `fda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_test
-- ----------------------------

-- ----------------------------
-- Table structure for `fda_test_plan`
-- ----------------------------
DROP TABLE IF EXISTS `fda_test_plan`;
CREATE TABLE `fda_test_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `fda_id` bigint(20) DEFAULT NULL,
  `plan_no` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sampling_district` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sampling_quantity` int(11) DEFAULT NULL,
  `quarter` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `testee_id` bigint(20) DEFAULT NULL,
  `producer_id` bigint(20) DEFAULT NULL,
  `sample_id` bigint(20) DEFAULT NULL,
  `product_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `plan_no` (`plan_no`),
  KEY `fk_fda_test_plan_fda` (`fda_id`),
  KEY `fk_fda_test_plan_business_unit_testee` (`testee_id`),
  KEY `fk_fda_test_plan_business_unit_producer` (`producer_id`),
  KEY `fk_fda_test_plan_product_instnace` (`sample_id`),
  KEY `fk_fda_test_plan_product_group` (`product_group_id`),
  CONSTRAINT `fk_fda_test_plan_business_unit_producer` FOREIGN KEY (`producer_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `fk_fda_test_plan_business_unit_testee` FOREIGN KEY (`testee_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `fk_fda_test_plan_fda` FOREIGN KEY (`fda_id`) REFERENCES `fda` (`id`),
  CONSTRAINT `fk_fda_test_plan_product_group` FOREIGN KEY (`product_group_id`) REFERENCES `fda_product_group` (`id`),
  CONSTRAINT `fk_fda_test_plan_product_instnace` FOREIGN KEY (`sample_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fda_test_plan
-- ----------------------------
INSERT INTO `fda_test_plan` VALUES ('1', 'Pass', '2013-08-29', '2013-09-12', '1', 'GZ20131004', null, null, null, null, null, null, null, null);
INSERT INTO `fda_test_plan` VALUES ('8', null, null, null, '3', null, null, null, null, null, null, null, null, null);
INSERT INTO `fda_test_plan` VALUES ('9', null, null, null, '3', null, null, null, null, null, null, null, null, null);
INSERT INTO `fda_test_plan` VALUES ('10', null, null, null, '3', null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `foodcat`
-- ----------------------------
DROP TABLE IF EXISTS `foodcat`;
CREATE TABLE `foodcat` (
  `FdCatID` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `FdUpCatID` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FdCatName` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `FdCatMth` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `FdTopCatID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FdTopCatName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FdUpCatName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`FdCatID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of foodcat
-- ----------------------------
INSERT INTO `foodcat` VALUES ('01', '', '乳及乳制品', 'GB2760-2011', '01', '乳及乳制品', '');
INSERT INTO `foodcat` VALUES ('0101', '01', '巴氏杀菌乳、灭菌乳和调制乳', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010101', '0101', '巴氏杀菌乳', 'GB2760-2011', '01', '乳及乳制品', '巴氏杀菌乳、灭菌乳和调制乳');
INSERT INTO `foodcat` VALUES ('010102', '0101', '灭菌乳', 'GB2760-2011', '01', '乳及乳制品', '巴氏杀菌乳、灭菌乳和调制乳');
INSERT INTO `foodcat` VALUES ('010103', '0101', '调制乳', 'GB2760-2011', '01', '乳及乳制品', '巴氏杀菌乳、灭菌乳和调制乳');
INSERT INTO `foodcat` VALUES ('0102', '01', '发酵乳和风味发酵乳', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010201', '0102', '发酵乳', 'GB2760-2011', '01', '乳及乳制品', '发酵乳和风味发酵乳');
INSERT INTO `foodcat` VALUES ('010202', '0102', '风味发酵乳', 'GB2760-2011', '01', '乳及乳制品', '发酵乳和风味发酵乳');
INSERT INTO `foodcat` VALUES ('0103', '01', '乳粉和奶油粉及其调制产品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010301', '0103', '乳粉和奶油粉', 'GB2760-2011', '01', '乳及乳制品', '乳粉和奶油粉及其调制产品');
INSERT INTO `foodcat` VALUES ('010302', '0103', '调制乳粉和调制奶油粉', 'GB2760-2011', '01', '乳及乳制品', '乳粉和奶油粉及其调制产品');
INSERT INTO `foodcat` VALUES ('0104', '01', '炼乳及其调制产品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010401', '0104', '淡炼乳', 'GB2760-2011', '01', '乳及乳制品', '炼乳及其调制产品');
INSERT INTO `foodcat` VALUES ('010402', '0104', '调制炼乳', 'GB2760-2011', '01', '乳及乳制品', '炼乳及其调制产品');
INSERT INTO `foodcat` VALUES ('0105', '01', '稀奶油及其类似品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010501', '0105', '稀奶油', 'GB2760-2011', '01', '乳及乳制品', '稀奶油及其类似品');
INSERT INTO `foodcat` VALUES ('010502', '0105', '凝固稀奶油', 'GB2760-2011', '01', '乳及乳制品', '稀奶油及其类似品');
INSERT INTO `foodcat` VALUES ('010503', '0105', '调味稀奶油', 'GB2760-2011', '01', '乳及乳制品', '稀奶油及其类似品');
INSERT INTO `foodcat` VALUES ('010504', '0105', '稀奶油类似品', 'GB2760-2011', '01', '乳及乳制品', '稀奶油及其类似品');
INSERT INTO `foodcat` VALUES ('0106', '01', '干酪', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('010601', '0106', '非熟化干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('010602', '0106', '熟化干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('010603', '0106', '乳清干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('010604', '0106', '再制干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('01060401', '010604', '普通再制干酪', 'GB2760-2011', '01', '乳及乳制品', '再制干酪');
INSERT INTO `foodcat` VALUES ('01060402', '010604', '调味再制干酪', 'GB2760-2011', '01', '乳及乳制品', '再制干酪');
INSERT INTO `foodcat` VALUES ('010605', '0106', '干酪类似品', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('010606', '0106', '乳清蛋白干酪', 'GB2760-2011', '01', '乳及乳制品', '干酪');
INSERT INTO `foodcat` VALUES ('0107', '01', '以乳为主要配料的即食风味甜点或其预制产品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('0108', '01', '其他乳制品', 'GB2760-2011', '01', '乳及乳制品', '乳及乳制品');
INSERT INTO `foodcat` VALUES ('02', '', '脂肪，油和乳化脂肪制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '');
INSERT INTO `foodcat` VALUES ('0201', '02', '基本不含水的脂肪和油', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('020101', '0201', '植物油脂', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '基本不含水的脂肪和油');
INSERT INTO `foodcat` VALUES ('02010101', '020101', '植物油', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '植物油脂');
INSERT INTO `foodcat` VALUES ('02010102', '020101', '氢化植物油', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '植物油脂');
INSERT INTO `foodcat` VALUES ('020102', '0201', '动物油脂', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '基本不含水的脂肪和油');
INSERT INTO `foodcat` VALUES ('020103', '0201', '无水黄油，无水乳脂', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '基本不含水的脂肪和油');
INSERT INTO `foodcat` VALUES ('0202', '02', '水油状脂肪乳化制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('020201', '0202', '脂肪含量80%以上的乳化制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '水油状脂肪乳化制品');
INSERT INTO `foodcat` VALUES ('02020101', '020201', '黄油和浓缩黄油', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '水油状脂肪乳化制品');
INSERT INTO `foodcat` VALUES ('02020102', '020201', '人造黄油及其类似制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '水油状脂肪乳化制品');
INSERT INTO `foodcat` VALUES ('020202', '0202', '脂肪含量80%以下的乳化制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '水油状脂肪乳化制品');
INSERT INTO `foodcat` VALUES ('0203', '02', '其他脂肪乳化制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('0204', '02', '脂肪类甜品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('0205', '02', '其他油脂或油脂制品', 'GB2760-2011', '02', '脂肪，油和乳化脂肪制品', '脂肪，油和乳化脂肪制品');
INSERT INTO `foodcat` VALUES ('03', '', '冷冻饮品', 'GB2760-2011', '03', '冷冻饮品', '');
INSERT INTO `foodcat` VALUES ('0301', '03', '冰淇淋类、雪糕类', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('0302', '03', '____', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('0303', '03', '风味冰、冰棍类', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('0304', '03', '食用冰', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('0305', '03', '其他冷冻饮品', 'GB2760-2011', '03', '冷冻饮品', '冷冻饮品');
INSERT INTO `foodcat` VALUES ('04', '', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '');
INSERT INTO `foodcat` VALUES ('0401', '04', '水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040101', '0401', '新鲜水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果');
INSERT INTO `foodcat` VALUES ('04010101', '040101', '未经加工的鲜果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜水果');
INSERT INTO `foodcat` VALUES ('04010102', '040101', '经表面处理的鲜水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜水果');
INSERT INTO `foodcat` VALUES ('04010103', '040101', '去皮或预切的鲜水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜水果');
INSERT INTO `foodcat` VALUES ('040102', '0401', '加工水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果');
INSERT INTO `foodcat` VALUES ('04010201', '040102', '冷冻水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010202', '040102', '水果干类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010203', '040102', '醋、油或盐渍水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010204', '040102', '水果罐头', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010205', '040102', '果酱', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010206', '040102', '果泥', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010207', '040102', '其他果酱', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010208', '040102', '蜜饯凉果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('0401020801', '04010208', '蜜饯类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020802', '04010208', '凉果类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020803', '04010208', '果脯类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020804', '04010208', '话化类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020805', '04010208', '果丹（饼）类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('0401020806', '04010208', '果糕类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蜜饯凉果');
INSERT INTO `foodcat` VALUES ('04010209', '040102', '装饰性果蔬', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010210', '040102', '水果甜品，包括果味液体甜品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010211', '040102', '发酵的水果制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010212', '040102', '煮熟的或油炸的水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('04010213', '040102', '其他加工水果', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工水果');
INSERT INTO `foodcat` VALUES ('0402', '04', '蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040201', '0402', '新鲜蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蔬菜');
INSERT INTO `foodcat` VALUES ('04020101', '040201', '未经加工鲜蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜蔬菜');
INSERT INTO `foodcat` VALUES ('04020102', '040201', '经表面处理的新鲜蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜蔬菜');
INSERT INTO `foodcat` VALUES ('04020103', '040201', '去皮、切块或切丝的蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜蔬菜');
INSERT INTO `foodcat` VALUES ('04020104', '040201', '豆芽菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜蔬菜');
INSERT INTO `foodcat` VALUES ('040202', '0402', '加工蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蔬菜');
INSERT INTO `foodcat` VALUES ('04020201', '040202', '冷冻蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020202', '040202', '干制蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020203', '040202', '腌渍的蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020204', '040202', '蔬菜罐头', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020205', '040202', '蔬菜泥，番茄沙司除外', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020206', '040202', '发酵蔬菜制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020207', '040202', '经水煮或油炸的蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('04020208', '040202', '其他加工蔬菜', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工蔬菜');
INSERT INTO `foodcat` VALUES ('040203', '0402', '根茎类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蔬菜');
INSERT INTO `foodcat` VALUES ('040204', '0402', '叶菜类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '蔬菜');
INSERT INTO `foodcat` VALUES ('0403', '04', '食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040301', '0403', '新鲜食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030101', '040301', '未经加工鲜食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030102', '040301', '经表面处理的鲜食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030103', '040301', '去皮、切块或切丝的食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '新鲜食用菌和藻类');
INSERT INTO `foodcat` VALUES ('040302', '0403', '加工食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030201', '040302', '冷冻食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030202', '040302', '干制食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030203', '040302', '腌渍的食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030204', '040302', '食用菌和藻类罐头', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030205', '040302', '经水煮或油炸的藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('04030206', '040302', '其他加工食用菌和藻类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工食用菌和藻类');
INSERT INTO `foodcat` VALUES ('0404', '04', '豆类制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040401', '0404', '非发酵豆制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆类制品');
INSERT INTO `foodcat` VALUES ('04040101', '040401', '豆腐类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040102', '040401', '豆干类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040103', '040401', '豆干再制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('0404010301', '04040103', '炸制半干豆腐', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆干再制品');
INSERT INTO `foodcat` VALUES ('0404010302', '04040103', '卤制半干豆腐', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆干再制品');
INSERT INTO `foodcat` VALUES ('0404010303', '04040103', '熏制半干豆腐', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆干再制品');
INSERT INTO `foodcat` VALUES ('0404010304', '04040103', '其他半干豆腐', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆干再制品');
INSERT INTO `foodcat` VALUES ('04040104', '040401', '腐竹类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040105', '040401', '新型豆制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040106', '040401', '熟制豆类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '非发酵豆制品');
INSERT INTO `foodcat` VALUES ('040402', '0404', '发酵豆制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆类制品');
INSERT INTO `foodcat` VALUES ('04040201', '040402', '腐乳类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '发酵豆制品');
INSERT INTO `foodcat` VALUES ('04040202', '040402', '豆豉及其制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '发酵豆制品');
INSERT INTO `foodcat` VALUES ('040403', '0404', '其他豆制品', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '豆类制品');
INSERT INTO `foodcat` VALUES ('0405', '04', '坚果和籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等');
INSERT INTO `foodcat` VALUES ('040501', '0405', '新鲜坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '坚果和籽类');
INSERT INTO `foodcat` VALUES ('040502', '0405', '加工坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '坚果和籽类');
INSERT INTO `foodcat` VALUES ('04050201', '040502', '熟制坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('0405020101', '04050201', '带壳熟制坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '熟制坚果与籽类');
INSERT INTO `foodcat` VALUES ('0405020102', '04050201', '脱壳熟制坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '熟制坚果与籽类');
INSERT INTO `foodcat` VALUES ('04050202', '040502', '包衣的坚果和籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('04050203', '040502', '坚果与籽类罐头', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('04050204', '040502', '坚果与籽类的泥（酱）', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('04050205', '040502', '其他加工的坚果与籽类', 'GB2760-2011', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '加工坚果与籽类');
INSERT INTO `foodcat` VALUES ('05', '', '可可制品、巧克力和巧克力制品以及糖果', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '');
INSERT INTO `foodcat` VALUES ('0501', '05', '可可制品、巧克力和巧克力制品', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品以及糖果');
INSERT INTO `foodcat` VALUES ('050101', '0501', '可可制品', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品');
INSERT INTO `foodcat` VALUES ('050102', '0501', '巧克力和巧克力制品、其他可可制品', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品');
INSERT INTO `foodcat` VALUES ('050103', '0501', '代可可脂巧克力及使用可可脂代用品的巧克力类似产品', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品');
INSERT INTO `foodcat` VALUES ('0502', '05', '糖果', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品以及糖果');
INSERT INTO `foodcat` VALUES ('050201', '0502', '胶基糖果', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '糖果');
INSERT INTO `foodcat` VALUES ('050202', '0502', '除胶基糖果以外的其他糖果', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '糖果');
INSERT INTO `foodcat` VALUES ('0503', '05', '糖果和巧克力制品包衣', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品以及糖果');
INSERT INTO `foodcat` VALUES ('0504', '05', '装饰糖果、顶饰', 'GB2760-2011', '05', '可可制品、巧克力和巧克力制品以及糖果', '可可制品、巧克力和巧克力制品以及糖果');
INSERT INTO `foodcat` VALUES ('06', '', '粮食和粮食制品', 'GB2760-2011', '06', '粮食和粮食制品', '');
INSERT INTO `foodcat` VALUES ('0601', '06', '原粮', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0602', '06', '大米及其制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('060201', '0602', '大米', 'GB2760-2011', '06', '粮食和粮食制品', '大米及其制品');
INSERT INTO `foodcat` VALUES ('060202', '0602', '大米制品', 'GB2760-2011', '06', '粮食和粮食制品', '大米及其制品');
INSERT INTO `foodcat` VALUES ('060203', '0602', '米粉', 'GB2760-2011', '06', '粮食和粮食制品', '大米及其制品');
INSERT INTO `foodcat` VALUES ('060204', '0602', '米粉制品', 'GB2760-2011', '06', '粮食和粮食制品', '大米及其制品');
INSERT INTO `foodcat` VALUES ('0603', '06', '小麦粉及其制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('060301', '0603', '小麦粉', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉及其制品');
INSERT INTO `foodcat` VALUES ('06030101', '060301', '通用小麦粉', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉');
INSERT INTO `foodcat` VALUES ('06030102', '060301', '专用小麦粉', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉');
INSERT INTO `foodcat` VALUES ('060302', '0603', '小麦粉制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉及其制品');
INSERT INTO `foodcat` VALUES ('06030201', '060302', '生湿面制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('06030202', '060302', '生干面制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('06030203', '060302', '发酵面制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('06030204', '060302', '面糊、裹粉、煎炸粉', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('06030205', '060302', '油炸面制品', 'GB2760-2011', '06', '粮食和粮食制品', '小麦粉制品');
INSERT INTO `foodcat` VALUES ('0604', '06', '杂粮粉及其制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('060401', '0604', '杂粮粉', 'GB2760-2011', '06', '粮食和粮食制品', '杂粮粉及其制品');
INSERT INTO `foodcat` VALUES ('060402', '0604', '杂粮制品', 'GB2760-2011', '06', '粮食和粮食制品', '杂粮粉及其制品');
INSERT INTO `foodcat` VALUES ('06040201', '060402', '八宝粥罐头', 'GB2760-2011', '06', '粮食和粮食制品', '杂粮制品');
INSERT INTO `foodcat` VALUES ('06040202', '060402', '其他杂粮制品', 'GB2760-2011', '06', '粮食和粮食制品', '杂粮制品');
INSERT INTO `foodcat` VALUES ('0605', '06', '淀粉及淀粉类制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('060501', '0605', '食用淀粉', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉及淀粉类制品');
INSERT INTO `foodcat` VALUES ('060502', '0605', '淀粉制品', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉及淀粉类制品');
INSERT INTO `foodcat` VALUES ('06050201', '060502', '粉丝、粉条', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉制品');
INSERT INTO `foodcat` VALUES ('06050202', '060502', '虾味片', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉制品');
INSERT INTO `foodcat` VALUES ('06050203', '060502', '藕粉', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉制品');
INSERT INTO `foodcat` VALUES ('06050204', '060502', '粉圆', 'GB2760-2011', '06', '粮食和粮食制品', '淀粉制品');
INSERT INTO `foodcat` VALUES ('0606', '06', '即食谷物', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0607', '06', '方便米面制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0608', '06', '冷冻米面制品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0609', '06', '谷类和淀粉类甜品', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('0610', '06', '粮食制品馅料', 'GB2760-2011', '06', '粮食和粮食制品', '粮食和粮食制品');
INSERT INTO `foodcat` VALUES ('07', '', '焙烤食品', 'GB2760-2011', '07', '焙烤食品', '');
INSERT INTO `foodcat` VALUES ('0701', '07', '面包', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('0702', '07', '糕点', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('070201', '0702', '中式糕点', 'GB2760-2011', '07', '焙烤食品', '糕点');
INSERT INTO `foodcat` VALUES ('070202', '0702', '西式糕点', 'GB2760-2011', '07', '焙烤食品', '糕点');
INSERT INTO `foodcat` VALUES ('070203', '0702', '月饼', 'GB2760-2011', '07', '焙烤食品', '糕点');
INSERT INTO `foodcat` VALUES ('070204', '0702', '糕点上彩装', 'GB2760-2011', '07', '焙烤食品', '糕点');
INSERT INTO `foodcat` VALUES ('0703', '07', '饼干', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('070301', '0703', '夹心及装饰类饼干', 'GB2760-2011', '07', '焙烤食品', '饼干');
INSERT INTO `foodcat` VALUES ('070302', '0703', '威化饼干', 'GB2760-2011', '07', '焙烤食品', '饼干');
INSERT INTO `foodcat` VALUES ('070303', '0703', '蛋卷', 'GB2760-2011', '07', '焙烤食品', '饼干');
INSERT INTO `foodcat` VALUES ('070304', '0703', '其他饼干', 'GB2760-2011', '07', '焙烤食品', '饼干');
INSERT INTO `foodcat` VALUES ('0704', '07', '焙烤食品馅料及表面用挂浆', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('0705', '07', '其他焙烤食品', 'GB2760-2011', '07', '焙烤食品', '焙烤食品');
INSERT INTO `foodcat` VALUES ('08', '', '肉及肉制品', 'GB2760-2011', '08', '肉及肉制品', '');
INSERT INTO `foodcat` VALUES ('0801', '08', '生、鲜肉', 'GB2760-2011', '08', '肉及肉制品', '肉及肉制品');
INSERT INTO `foodcat` VALUES ('080101', '0801', '生鲜肉', 'GB2760-2011', '08', '肉及肉制品', '生、鲜肉');
INSERT INTO `foodcat` VALUES ('080102', '0801', '冷却肉', 'GB2760-2011', '08', '肉及肉制品', '生、鲜肉');
INSERT INTO `foodcat` VALUES ('080103', '0801', '冻肉', 'GB2760-2011', '08', '肉及肉制品', '生、鲜肉');
INSERT INTO `foodcat` VALUES ('0802', '08', '预制肉制品', 'GB2760-2011', '08', '肉及肉制品', '肉及肉制品');
INSERT INTO `foodcat` VALUES ('080201', '0802', '调理肉制品', 'GB2760-2011', '08', '肉及肉制品', '预制肉制品');
INSERT INTO `foodcat` VALUES ('080202', '0802', '腌腊肉制品类', 'GB2760-2011', '08', '肉及肉制品', '预制肉制品');
INSERT INTO `foodcat` VALUES ('0803', '08', '熟肉制品', 'GB2760-2011', '08', '肉及肉制品', '肉及肉制品');
INSERT INTO `foodcat` VALUES ('080301', '0803', '酱卤肉制品类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('08030101', '080301', '白煮肉类', 'GB2760-2011', '08', '肉及肉制品', '酱卤肉制品类');
INSERT INTO `foodcat` VALUES ('08030102', '080301', '酱卤肉类', 'GB2760-2011', '08', '肉及肉制品', '酱卤肉制品类');
INSERT INTO `foodcat` VALUES ('08030103', '080301', '糟肉类', 'GB2760-2011', '08', '肉及肉制品', '酱卤肉制品类');
INSERT INTO `foodcat` VALUES ('080302', '0803', '熏、烧、烤肉类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080303', '0803', '油炸肉类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080304', '0803', '西式火腿类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080305', '0803', '肉灌肠类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080306', '0803', '发酵肉制品类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080307', '0803', '熟肉干制品', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('08030701', '080307', '肉松类', 'GB2760-2011', '08', '肉及肉制品', '熟肉干制品');
INSERT INTO `foodcat` VALUES ('08030702', '080307', '肉干类', 'GB2760-2011', '08', '肉及肉制品', '熟肉干制品');
INSERT INTO `foodcat` VALUES ('08030703', '080307', '肉脯类', 'GB2760-2011', '08', '肉及肉制品', '熟肉干制品');
INSERT INTO `foodcat` VALUES ('080308', '0803', '肉罐头类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080309', '0803', '可食用动物肠衣类', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('080310', '0803', '其他肉及肉制品', 'GB2760-2011', '08', '肉及肉制品', '熟肉制品');
INSERT INTO `foodcat` VALUES ('09', '', '水产及其制品', 'GB2760-2011', '09', '水产及其制品', '');
INSERT INTO `foodcat` VALUES ('0901', '09', '鲜水产', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('090101', '0901', '鲐鱼', 'GB2760-2011', '09', '水产及其制品', '鲜水产');
INSERT INTO `foodcat` VALUES ('090102', '0901', '其他', 'GB2760-2011', '09', '水产及其制品', '鲜水产');
INSERT INTO `foodcat` VALUES ('0902', '09', '冷冻水产品及其制品', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('090201', '0902', '冷冻制品', 'GB2760-2011', '09', '水产及其制品', '冷冻水产品及其制品');
INSERT INTO `foodcat` VALUES ('090202', '0902', '冷冻挂桨制品', 'GB2760-2011', '09', '水产及其制品', '冷冻水产品及其制品');
INSERT INTO `foodcat` VALUES ('090203', '0902', '冷冻鱼糜制品', 'GB2760-2011', '09', '水产及其制品', '冷冻水产品及其制品');
INSERT INTO `foodcat` VALUES ('0903', '09', '预制水产品', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('090301', '0903', '醋渍或肉冻状水产品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090302', '0903', '腌制水产品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090303', '0903', '鱼子制品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090304', '0903', '风干、烘干、压干等水产品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090305', '0903', '水发水产品(鱿鱼、海参等)', '', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('090306', '0903', '其他预制水产品', 'GB2760-2011', '09', '水产及其制品', '预制水产品');
INSERT INTO `foodcat` VALUES ('0904', '09', '熟制水产品', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('090401', '0904', '熟干水产品', 'GB2760-2011', '09', '水产及其制品', '熟制水产品');
INSERT INTO `foodcat` VALUES ('090402', '0904', '经烹调或油炸的水产品', 'GB2760-2011', '09', '水产及其制品', '熟制水产品');
INSERT INTO `foodcat` VALUES ('090403', '0904', '熏、烤水产品', 'GB2760-2011', '09', '水产及其制品', '熟制水产品');
INSERT INTO `foodcat` VALUES ('090404', '0904', '发酵水产品', 'GB2760-2011', '09', '水产及其制品', '熟制水产品');
INSERT INTO `foodcat` VALUES ('0905', '09', '水产品罐头', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('0906', '09', '其他水产品及其制品', 'GB2760-2011', '09', '水产及其制品', '水产及其制品');
INSERT INTO `foodcat` VALUES ('10', '', '蛋及蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '');
INSERT INTO `foodcat` VALUES ('1001', '10', '鲜蛋', 'GB2760-2011', '10', '蛋及蛋制品', '蛋及蛋制品');
INSERT INTO `foodcat` VALUES ('1002', '10', '再制蛋', 'GB2760-2011', '10', '蛋及蛋制品', '蛋及蛋制品');
INSERT INTO `foodcat` VALUES ('100201', '1002', '卤蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('100202', '1002', '糟蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('100203', '1002', '皮蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('100204', '1002', '咸蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('100205', '1002', '其他再制蛋', 'GB2760-2011', '10', '蛋及蛋制品', '再制蛋');
INSERT INTO `foodcat` VALUES ('1003', '10', '蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋及蛋制品');
INSERT INTO `foodcat` VALUES ('100301', '1003', '脱水蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋制品');
INSERT INTO `foodcat` VALUES ('100302', '1003', '热凝固蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋制品');
INSERT INTO `foodcat` VALUES ('100303', '1003', '冷冻蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋制品');
INSERT INTO `foodcat` VALUES ('100304', '1003', '液体蛋', 'GB2760-2011', '10', '蛋及蛋制品', '蛋制品');
INSERT INTO `foodcat` VALUES ('1004', '10', '其他蛋制品', 'GB2760-2011', '10', '蛋及蛋制品', '蛋及蛋制品');
INSERT INTO `foodcat` VALUES ('11', '', '甜味料，包括蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '');
INSERT INTO `foodcat` VALUES ('1101', '11', '食糖', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('110101', '1101', '白糖及白糖制品', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '食糖');
INSERT INTO `foodcat` VALUES ('110102', '1101', '其他糖和糖浆', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '食糖');
INSERT INTO `foodcat` VALUES ('1102', '11', '淀粉糖', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('1103', '11', '蜂蜜及花粉', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('110301', '1103', '蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜及花粉');
INSERT INTO `foodcat` VALUES ('11030101', '110301', '桉树蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030102', '110301', '柑橘蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030103', '110301', '紫苜蓿蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030104', '110301', '荔枝蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030105', '110301', '野桂花蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030106', '110301', '龙眼蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030107', '110301', '鹅掌柴蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030108', '110301', '一级蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030109', '110301', '二级蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('11030110', '110301', '其他蜂蜜', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜');
INSERT INTO `foodcat` VALUES ('110302', '1103', '花粉', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '蜂蜜及花粉');
INSERT INTO `foodcat` VALUES ('1104', '11', '餐桌甜味料', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('1105', '11', '调味糖浆', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('110501', '1105', '水果调味糖浆', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '调味糖浆');
INSERT INTO `foodcat` VALUES ('110502', '1105', '其他调味糖浆', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '调味糖浆');
INSERT INTO `foodcat` VALUES ('1106', '11', '其他甜味料', 'GB2760-2011', '11', '甜味料，包括蜂蜜', '甜味料，包括蜂蜜');
INSERT INTO `foodcat` VALUES ('12', '', '调味品', 'GB2760-2011', '12', '调味品', '');
INSERT INTO `foodcat` VALUES ('1201', '12', '盐及代盐制品', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1202', '12', '鲜味剂和助鲜剂', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1203', '12', '醋', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('120301', '1203', '酿造食醋', 'GB2760-2011', '12', '调味品', '醋');
INSERT INTO `foodcat` VALUES ('120302', '1203', '配制食醋', 'GB2760-2011', '12', '调味品', '醋');
INSERT INTO `foodcat` VALUES ('1204', '12', '酱油', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('120401', '1204', '酿造酱油', 'GB2760-2011', '12', '调味品', '酱油');
INSERT INTO `foodcat` VALUES ('120402', '1204', '配制酱油', 'GB2760-2011', '12', '调味品', '酱油');
INSERT INTO `foodcat` VALUES ('1205', '12', '酱及酱制品', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('120501', '1205', '酿造酱', 'GB2760-2011', '12', '调味品', '酱及酱制品');
INSERT INTO `foodcat` VALUES ('120502', '1205', '配制酱', 'GB2760-2011', '12', '调味品', '酱及酱制品');
INSERT INTO `foodcat` VALUES ('1206', '12', '_____', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1207', '12', '料酒及制品', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1208', '12', '______', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('1209', '12', '香辛料类', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('120901', '1209', '香辛料及粉', 'GB2760-2011', '12', '调味品', '香辛料类');
INSERT INTO `foodcat` VALUES ('120902', '1209', '香辛料油', 'GB2760-2011', '12', '调味品', '香辛料类');
INSERT INTO `foodcat` VALUES ('120903', '1209', '香辛料酱', 'GB2760-2011', '12', '调味品', '香辛料类');
INSERT INTO `foodcat` VALUES ('120904', '1209', '其他香辛料加工品', 'GB2760-2011', '12', '调味品', '香辛料类');
INSERT INTO `foodcat` VALUES ('1210', '12', '复合调味料', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('121001', '1210', '固体复合调味料', 'GB2760-2011', '12', '调味品', '复合调味料');
INSERT INTO `foodcat` VALUES ('12100101', '121001', '固体汤料', 'GB2760-2011', '12', '调味品', '固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100102', '121001', '鸡精、鸡粉', 'GB2760-2011', '12', '调味品', '固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100103', '121001', '其他固体复合调味料', 'GB2760-2011', '12', '调味品', '固体复合调味料');
INSERT INTO `foodcat` VALUES ('121002', '1210', '半固体复合调味料', 'GB2760-2011', '12', '调味品', '复合调味料');
INSERT INTO `foodcat` VALUES ('12100201', '121002', '蛋黄酱、沙拉酱', 'GB2760-2011', '12', '调味品', '半固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100202', '121002', '以动物性原料为基料的调味酱', 'GB2760-2011', '12', '调味品', '半固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100203', '121002', '以蔬菜为基料的调味酱', 'GB2760-2011', '12', '调味品', '半固体复合调味料');
INSERT INTO `foodcat` VALUES ('12100204', '121002', '其他', 'GB2760-2011', '12', '调味品', '半固体复合调味料');
INSERT INTO `foodcat` VALUES ('121003', '1210', '液体复合调味料', 'GB2760-2011', '12', '调味品', '复合调味料');
INSERT INTO `foodcat` VALUES ('12100301', '121003', '浓缩汤', 'GB2760-2011', '12', '调味品', '液体复合调味料');
INSERT INTO `foodcat` VALUES ('12100302', '121003', '肉汤、骨汤', 'GB2760-2011', '12', '调味品', '液体复合调味料');
INSERT INTO `foodcat` VALUES ('12100303', '121003', '调味清汁', 'GB2760-2011', '12', '调味品', '液体复合调味料');
INSERT INTO `foodcat` VALUES ('12100304', '121003', '蚝油、虾油、鱼露等', 'GB2760-2011', '12', '调味品', '液体复合调味料');
INSERT INTO `foodcat` VALUES ('1211', '12', '其他调味料', 'GB2760-2011', '12', '调味品', '调味品');
INSERT INTO `foodcat` VALUES ('13', '', '特殊膳食用食品', 'GB2760-2011', '13', '特殊膳食用食品', '');
INSERT INTO `foodcat` VALUES ('1301', '13', '婴幼儿配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('130101', '1301', '婴儿配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿配方食品');
INSERT INTO `foodcat` VALUES ('130102', '1301', '较大婴儿和幼儿配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿配方食品');
INSERT INTO `foodcat` VALUES ('130103', '1301', '特殊医学用途婴儿配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿配方食品');
INSERT INTO `foodcat` VALUES ('1302', '13', '婴幼儿辅助食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('130201', '1302', '婴幼儿谷类辅助食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿辅助食品');
INSERT INTO `foodcat` VALUES ('130202', '1302', '婴幼儿罐装辅助食品', 'GB2760-2011', '13', '特殊膳食用食品', '婴幼儿辅助食品');
INSERT INTO `foodcat` VALUES ('1303', '13', '特殊医学用途配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('1304', '13', '低能量配方食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('1305', '13', '其他特殊膳食用食品', 'GB2760-2011', '13', '特殊膳食用食品', '特殊膳食用食品');
INSERT INTO `foodcat` VALUES ('14', '', '饮料类', 'GB2760-2011', '14', '饮料类', '');
INSERT INTO `foodcat` VALUES ('1401', '14', '包装饮用水类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140101', '1401', '饮用天然矿泉水', 'GB2760-2011', '14', '饮料类', '1401');
INSERT INTO `foodcat` VALUES ('140102', '1401', '饮用纯净水', 'GB2760-2011', '14', '饮料类', '1401');
INSERT INTO `foodcat` VALUES ('140103', '1401', '其他饮用水', 'GB2760-2011', '14', '饮料类', '1401');
INSERT INTO `foodcat` VALUES ('1402', '14', '果蔬汁类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140201', '1402', '果蔬汁（浆）', 'GB2760-2011', '14', '饮料类', '果蔬汁类');
INSERT INTO `foodcat` VALUES ('140202', '1402', '浓缩果蔬汁（浆）', 'GB2760-2011', '14', '饮料类', '果蔬汁类');
INSERT INTO `foodcat` VALUES ('140203', '1402', '果蔬汁（肉）', 'GB2760-2011', '14', '饮料类', '果蔬汁类');
INSERT INTO `foodcat` VALUES ('1403', '14', '蛋白饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140301', '1403', '含乳饮料', 'GB2760-2011', '14', '饮料类', '蛋白饮料类');
INSERT INTO `foodcat` VALUES ('14030101', '140301', '发酵型含乳饮料', 'GB2760-2011', '14', '饮料类', '含乳饮料');
INSERT INTO `foodcat` VALUES ('14030102', '140301', '配制型含乳饮料', 'GB2760-2011', '14', '饮料类', '含乳饮料');
INSERT INTO `foodcat` VALUES ('14030103', '140301', '乳酸菌饮料', 'GB2760-2011', '14', '饮料类', '含乳饮料');
INSERT INTO `foodcat` VALUES ('140302', '1403', '植物蛋白饮料', 'GB2760-2011', '14', '饮料类', '蛋白饮料类');
INSERT INTO `foodcat` VALUES ('140303', '1403', '复合蛋白饮料', 'GB2760-2011', '14', '饮料类', '蛋白饮料类');
INSERT INTO `foodcat` VALUES ('1404', '14', '水基调味饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140401', '1404', '碳酸饮料', 'GB2760-2011', '14', '饮料类', '水基调味饮料类');
INSERT INTO `foodcat` VALUES ('14040101', '140401', '可乐型碳酸饮料', 'GB2760-2011', '14', '饮料类', '碳酸饮料');
INSERT INTO `foodcat` VALUES ('14040102', '140401', '其他型碳酸饮料', 'GB2760-2011', '14', '饮料类', '碳酸饮料');
INSERT INTO `foodcat` VALUES ('140402', '1404', '非碳酸饮料', 'GB2760-2011', '14', '饮料类', '水基调味饮料类');
INSERT INTO `foodcat` VALUES ('14040201', '140402', '特殊用途饮料', 'GB2760-2011', '14', '饮料类', '非碳酸饮料');
INSERT INTO `foodcat` VALUES ('14040202', '140402', '风味饮料', 'GB2760-2011', '14', '饮料类', '非碳酸饮料');
INSERT INTO `foodcat` VALUES ('1405', '14', '茶、咖啡、植物饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140501', '1405', '茶饮料类', 'GB2760-2011', '14', '饮料类', '茶、咖啡、植物饮料类');
INSERT INTO `foodcat` VALUES ('140502', '1405', '咖啡饮料类', 'GB2760-2011', '14', '饮料类', '茶、咖啡、植物饮料类');
INSERT INTO `foodcat` VALUES ('140503', '1405', '植物饮料类', 'GB2760-2011', '14', '饮料类', '茶、咖啡、植物饮料类');
INSERT INTO `foodcat` VALUES ('1406', '14', '固体饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('140601', '1406', '果香型固体饮料', 'GB2760-2011', '14', '饮料类', '固体饮料类');
INSERT INTO `foodcat` VALUES ('140602', '1406', '蛋白型固体饮料', 'GB2760-2011', '14', '饮料类', '固体饮料类');
INSERT INTO `foodcat` VALUES ('140603', '1406', '速溶咖啡', 'GB2760-2011', '14', '饮料类', '固体饮料类');
INSERT INTO `foodcat` VALUES ('140604', '1406', '其他固体饮料', 'GB2760-2011', '14', '饮料类', '固体饮料类');
INSERT INTO `foodcat` VALUES ('1407', '14', '生活饮用水', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('1408', '14', '其他饮料类', 'GB2760-2011', '14', '饮料类', '饮料类');
INSERT INTO `foodcat` VALUES ('15', '', '酒类', 'GB2760-2011', '15', '酒类', '');
INSERT INTO `foodcat` VALUES ('1501', '15', '蒸馏酒', 'GB2760-2011', '15', '酒类', '酒类');
INSERT INTO `foodcat` VALUES ('150101', '1501', '白酒', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150102', '1501', '调香蒸馏酒', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150103', '1501', '白兰地', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150104', '1501', '威士忌', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150105', '1501', '伏特加', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150106', '1501', '朗姆酒', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('150107', '1501', '其他蒸馏酒', 'GB2760-2011', '15', '酒类', '蒸馏酒');
INSERT INTO `foodcat` VALUES ('1502', '15', '配制酒', 'GB2760-2011', '15', '酒类', '酒类');
INSERT INTO `foodcat` VALUES ('1503', '15', '发酵酒', 'GB2760-2011', '15', '酒类', '酒类');
INSERT INTO `foodcat` VALUES ('150301', '1503', '葡萄酒', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('15030101', '150301', '无汽葡萄酒', 'GB2760-2011', '15', '酒类', '葡萄酒');
INSERT INTO `foodcat` VALUES ('15030102', '150301', '起泡和半起泡葡萄酒', 'GB2760-2011', '15', '酒类', '葡萄酒');
INSERT INTO `foodcat` VALUES ('15030103', '150301', '调香葡萄酒', 'GB2760-2011', '15', '酒类', '葡萄酒');
INSERT INTO `foodcat` VALUES ('15030104', '150301', '特种葡萄酒', 'GB2760-2011', '15', '酒类', '葡萄酒');
INSERT INTO `foodcat` VALUES ('150302', '1503', '黄酒', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('150303', '1503', '果酒', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('150304', '1503', '蜂蜜酒', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('150305', '1503', '啤酒和麦芽饮料', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('150306', '1503', '其他发酵酒类', 'GB2760-2011', '15', '酒类', '发酵酒');
INSERT INTO `foodcat` VALUES ('16', '', '其他类', 'GB2760-2011', '16', '其他类', '');
INSERT INTO `foodcat` VALUES ('1601', '16', '果冻', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1602', '16', '茶叶、咖啡', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1603', '16', '胶原蛋白肠衣', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1604', '16', '酵母及酵母类制品', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('160401', '1604', '干酵母', 'GB2760-2011', '16', '其他类', '酵母及酵母类制品');
INSERT INTO `foodcat` VALUES ('160402', '1604', '其他酵母及酵母类制品', 'GB2760-2011', '16', '其他类', '酵母及酵母类制品');
INSERT INTO `foodcat` VALUES ('1605', '16', '________', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1606', '16', '膨化食品', 'GB2760-2011', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1607', '16', '小吃', '', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('160701', '1607', '味精', 'GB2760-2011', '16', '其他类', '其他');
INSERT INTO `foodcat` VALUES ('16070102', '160701', '增鲜味精', 'GB2760-2011', '16', '其他类', '味精');
INSERT INTO `foodcat` VALUES ('16070103', '160701', '加盐味精', 'GB2760-2011', '16', '其他类', '味精');
INSERT INTO `foodcat` VALUES ('16070104', '160701', '其他味精', 'GB2760-2011', '16', '其他类', '味精');
INSERT INTO `foodcat` VALUES ('160702', '1607', '自来水', 'GB2760-2011', '16', '其他类', '其他');
INSERT INTO `foodcat` VALUES ('160703', '1607', '其他', 'GB2760-2011', '16', '其他类', '其他');
INSERT INTO `foodcat` VALUES ('1608', '16', '油炸食品', '', '16', '其他类', '其他类');
INSERT INTO `foodcat` VALUES ('1609', '16', '其他', 'GB2760-2011', '16', '其他类', '其他类');

-- ----------------------------
-- Table structure for `fsn_comment`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_comment`;
CREATE TABLE `fsn_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `star` int(1) NOT NULL DEFAULT '0',
  `content` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `method` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `method_detail` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `product_detail` varchar(50) CHARACTER SET utf8 DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fsn_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `fsn_display`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display`;
CREATE TABLE `fsn_display` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usrType` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户类型：匿名用户、cookie用户，实名用户',
  `uriId` bigint(20) DEFAULT NULL COMMENT 'URI编号',
  `container` int(3) DEFAULT NULL COMMENT '显示槽位',
  `active` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='根据用户类型在页面显示对应的信息，包含推荐栏、广告栏、楼';

-- ----------------------------
-- Records of fsn_display
-- ----------------------------
INSERT INTO `fsn_display` VALUES ('1', 'default', '1', '1', '1');
INSERT INTO `fsn_display` VALUES ('2', 'default', '2', '2', '1');
INSERT INTO `fsn_display` VALUES ('3', 'default', '3', '3', '1');
INSERT INTO `fsn_display` VALUES ('4', 'default', '4', '4', '1');
INSERT INTO `fsn_display` VALUES ('5', 'default', '5', '5', '1');
INSERT INTO `fsn_display` VALUES ('6', 'default', '6', '6', '1');
INSERT INTO `fsn_display` VALUES ('7', 'default', '7', '7', '1');
INSERT INTO `fsn_display` VALUES ('8', 'default', '8', '8', '1');
INSERT INTO `fsn_display` VALUES ('9', 'default', '9', '9', '1');
INSERT INTO `fsn_display` VALUES ('10', 'default', '10', '10', '1');
INSERT INTO `fsn_display` VALUES ('11', 'default', '11', '1', '1');
INSERT INTO `fsn_display` VALUES ('12', 'default', '12', '3', '1');
INSERT INTO `fsn_display` VALUES ('13', 'default', '13', '4', '1');
INSERT INTO `fsn_display` VALUES ('14', 'default', '14', '5', '1');
INSERT INTO `fsn_display` VALUES ('15', 'default', '15', '6', '1');
INSERT INTO `fsn_display` VALUES ('16', 'default', '16', '1', '1');
INSERT INTO `fsn_display` VALUES ('17', 'default', '17', '1', '1');
INSERT INTO `fsn_display` VALUES ('18', 'default', '18', '2', '1');
INSERT INTO `fsn_display` VALUES ('19', 'default', '19', '3', '1');
INSERT INTO `fsn_display` VALUES ('20', 'default', '20', '4', '1');
INSERT INTO `fsn_display` VALUES ('21', 'default', '21', '5', '1');
INSERT INTO `fsn_display` VALUES ('22', 'default', '22', '6', '1');
INSERT INTO `fsn_display` VALUES ('23', 'default', '23', '7', '1');
INSERT INTO `fsn_display` VALUES ('24', 'default', '24', '8', '1');
INSERT INTO `fsn_display` VALUES ('25', 'default', '25', '9', '1');
INSERT INTO `fsn_display` VALUES ('26', 'default', '26', '10', '1');
INSERT INTO `fsn_display` VALUES ('27', 'default', '27', '2', '1');
INSERT INTO `fsn_display` VALUES ('28', 'default', '28', '1', '1');
INSERT INTO `fsn_display` VALUES ('29', 'default', '29', '2', '1');
INSERT INTO `fsn_display` VALUES ('30', 'default', '30', '3', '1');
INSERT INTO `fsn_display` VALUES ('31', 'default', '31', '4', '1');
INSERT INTO `fsn_display` VALUES ('32', 'default', '32', '5', '1');
INSERT INTO `fsn_display` VALUES ('33', 'default', '33', '6', '1');
INSERT INTO `fsn_display` VALUES ('34', 'default', '34', '7', '1');
INSERT INTO `fsn_display` VALUES ('35', 'default', '35', '8', '1');
INSERT INTO `fsn_display` VALUES ('36', 'default', '36', '4', '1');
INSERT INTO `fsn_display` VALUES ('37', 'default', '37', '1', '1');
INSERT INTO `fsn_display` VALUES ('38', 'default', '38', '2', '1');
INSERT INTO `fsn_display` VALUES ('39', 'default', '39', '3', '1');
INSERT INTO `fsn_display` VALUES ('40', 'default', '40', '4', '1');
INSERT INTO `fsn_display` VALUES ('41', 'default', '41', '5', '1');
INSERT INTO `fsn_display` VALUES ('42', 'default', '42', '6', '1');
INSERT INTO `fsn_display` VALUES ('43', 'default', '43', '7', '1');
INSERT INTO `fsn_display` VALUES ('44', 'default', '44', '8', '1');
INSERT INTO `fsn_display` VALUES ('45', 'default', '45', '3', '1');
INSERT INTO `fsn_display` VALUES ('46', 'default', '46', '1', '1');
INSERT INTO `fsn_display` VALUES ('47', 'default', '47', '2', '1');
INSERT INTO `fsn_display` VALUES ('48', 'default', '48', '3', '1');
INSERT INTO `fsn_display` VALUES ('49', 'default', '49', '4', '1');
INSERT INTO `fsn_display` VALUES ('50', 'default', '50', '5', '1');
INSERT INTO `fsn_display` VALUES ('51', 'default', '51', '6', '1');
INSERT INTO `fsn_display` VALUES ('52', 'default', '52', '7', '1');
INSERT INTO `fsn_display` VALUES ('53', 'default', '53', '8', '1');
INSERT INTO `fsn_display` VALUES ('54', 'default', '54', '2', '1');
INSERT INTO `fsn_display` VALUES ('55', 'default', '55', '9', '1');
INSERT INTO `fsn_display` VALUES ('56', 'default', '56', '9', '1');
INSERT INTO `fsn_display` VALUES ('57', 'default', '57', '10', '1');
INSERT INTO `fsn_display` VALUES ('58', 'default', '58', '10', '1');

-- ----------------------------
-- Table structure for `fsn_display_ad`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_ad`;
CREATE TABLE `fsn_display_ad` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ad_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '广告图片路径',
  `ad_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '广告图片点击跳转链接',
  `col_code` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '所属栏目code，没有则为通用广告',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='显示的广告信息';

-- ----------------------------
-- Records of fsn_display_ad
-- ----------------------------
INSERT INTO `fsn_display_ad` VALUES ('1', '/fsn/resource/img/test_ad.jpg', '#', null);

-- ----------------------------
-- Table structure for `fsn_display_col`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_col`;
CREATE TABLE `fsn_display_col` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `col_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '栏目名称',
  `col_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '栏目编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fsn_display_col
-- ----------------------------

-- ----------------------------
-- Table structure for `fsn_display_prod`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_prod`;
CREATE TABLE `fsn_display_prod` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `prod_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `prod_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品名称',
  `prod_desc` text COLLATE utf8_unicode_ci COMMENT '产品描述',
  `prod_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品图片资源路径，一份',
  `prod_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品链接',
  `col_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='楼的显示信息';

-- ----------------------------
-- Records of fsn_display_prod
-- ----------------------------
INSERT INTO `fsn_display_prod` VALUES ('0', '1', '雀巢咖啡', '雀巢公司陆续在中国的天津、青岛和东莞等地建起了自己的工厂，1999年秋天，雀巢成功地收购了太太乐集团80%的股份。之后，雀巢又于2001年与中国第二大鸡精生产厂家豪吉公司合作，成立了合资四川豪吉食品有限公司。\r\n雀巢在市场上咄咄逼人的态势，让它的竞争对手感到巨大的压力，国内一个乳制品企业的管理人员将雀巢形容为一个食品领域的“巨人”，随时都可以在相关的竞争领域发起攻势。', '/fsn/resource/img/nescafe1-3.jpg', '/fsn/ui/product_detail?proid=1', null);

-- ----------------------------
-- Table structure for `fsn_display_re`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_re`;
CREATE TABLE `fsn_display_re` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `re_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '推荐信息图片资源路径',
  `re_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '推荐跳转链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='推荐栏的显示信息';

-- ----------------------------
-- Records of fsn_display_re
-- ----------------------------

-- ----------------------------
-- Table structure for `fsn_display_uri`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_display_uri`;
CREATE TABLE `fsn_display_uri` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '资源类型 1-产品 2-广告 3-栏目 4-推荐 5-新闻',
  `content` text COLLATE utf8_unicode_ci COMMENT '资源内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='显示资源存储表';

-- ----------------------------
-- Records of fsn_display_uri
-- ----------------------------
INSERT INTO `fsn_display_uri` VALUES ('1', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>216</prodId><prodName>纯牛奶利乐</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniuchunniunailile/mncnnll.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0001</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('2', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>217</prodId><prodName>低脂高钙牛奶利乐砖</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniudizhigaogainiunaililezhuan/mndzggnnllz.jpg</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0001</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('3', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>218</prodId><prodName>低乳糖牛奶</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniuxinyangdaoditangniunai/mnxyddtnn.jpg</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0001</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('4', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>219</prodId><prodName>优益乳</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniuyouyiru/mnyyr.jpg</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0001</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('5', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>220</prodId><prodName>优菌乳</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/mengniuyijunru/mnyjr.jpg</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0001</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('6', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>221</prodId><prodName>联杯菠萝</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/liangbeiboluo/lianbeiboluo.jpg</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0001</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('7', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>222</prodId><prodName>1953黑加仑</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/1953heijialun/1953heijialun.jpg</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0001</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('8', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>223</prodId><prodName>壶装酸奶</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/huzhuangshuannai/huzhuangshuannai.jpg</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0001</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('11', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:0</adPic><adUrl>adUrl:0</adUrl><colCode>0001</colCode><active>1</active><container>0</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('12', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:2</adPic><adUrl>adUrl:2</adUrl><colCode></colCode><active>1</active><container>2</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('13', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:3</adPic><adUrl>adUrl:3</adUrl><colCode></colCode><active>1</active><container>3</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('14', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:4</adPic><adUrl>adUrl:4</adUrl><colCode></colCode><active>1</active><container>4</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('15', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:5</adPic><adUrl>adUrl:5</adUrl><colCode></colCode><active>1</active><container>5</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('16', '3', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>牛奶</colName><colCode>0001</colCode><active>1</active><container>1</container></col>');
INSERT INTO `fsn_display_uri` VALUES ('17', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/qwfu1.jpg</rePic><reUrl>/fsn-core/ui/portal/product-detail?proid=163</reUrl><active>1</active><container>0</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('18', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/lgd1.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>1</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('19', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/lxc1.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>2</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('20', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/visual1.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>3</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('21', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/slry.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>4</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('22', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/gzgt.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>5</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('23', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/lgd2.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>6</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('24', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/visual2.jpg</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>7</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('25', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/qwfu2.jpg</rePic><reUrl>/fsn-core/ui/portal/product-detail?proid=165</reUrl><active>1</active><container>8</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('26', '4', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><re><rePic>http://211.151.134.74:8080/portal/img/recommendAd/lxc2.png</rePic><reUrl>/fsn-core/ui/portal/error404</reUrl><active>1</active><container>9</container></re>');
INSERT INTO `fsn_display_uri` VALUES ('27', '2', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ad><adPic>adPic:1</adPic><adUrl>adUrl:1</adUrl><colCode></colCode><active>1</active><container>1</container></ad>');
INSERT INTO `fsn_display_uri` VALUES ('28', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian1.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0002</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('29', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian2.jpg</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0002</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('30', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian3.jpg</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0002</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('31', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian4.jpg</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0002</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('32', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>238</prodId><prodName>都匀毛尖</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian5.jpg</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0002</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('33', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>168</prodId><prodName>贵州辣子鸡</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/guizhoulaziji/guizhoulaziji1.jpg</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0002</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('34', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>169</prodId><prodName>蔬菜猪肉干（醇香味）</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/shucaizhurouganchunxiangwei/shucaizhurougan(chunxiangwei)1.jpg</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0002</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('35', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>170</prodId><prodName>精制猪肉脯（醇香味）</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/jingzhizhuroupuchunxiangwei/jingzhizhuroupu(chunxiangwei)1.jpg</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0002</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('36', '3', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>茶</colName><colCode>0002</colCode><active>1</active><container>4</container></col>');
INSERT INTO `fsn_display_uri` VALUES ('37', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>225</prodId><prodName>红油腐乳</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/hongyoufuru/hongyoufuru.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0003</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('38', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>226</prodId><prodName>黄豆酱油</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/huangdoujiangyou500ml/huangdoujiangyou500ml.jpg</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0003</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('39', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>227</prodId><prodName>生抽王</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/shengchouwang/shengchouwang.jpg</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0003</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('40', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>228</prodId><prodName>蒜汁酱油</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/suanzhijiangyou/suanzhijiangyou.jpg</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0003</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('41', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>229</prodId><prodName>特鲜酱油</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/texianjiangyou/texianjiangyou.jpg</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0003</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('42', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>230</prodId><prodName>特制酱油</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/tezhijiangyou/tezhijiangyou.jpg</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0003</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('43', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>231</prodId><prodName>铁强化营养酱油</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/tieqianghuayinyangjiangyou/tieqianghuayinyangjiangyou.jpg</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0003</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('44', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>232</prodId><prodName>鲜味酱油</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/xianweijiangyou/xianweijiangyou.jpg</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0003</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('45', '3', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>调味</colName><colCode>0003</colCode><active>1</active><container>3</container></col>');
INSERT INTO `fsn_display_uri` VALUES ('46', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>163</prodId><prodName>贵州腊肠</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/guizhoulachang/guizhoulachang2.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0004</colCode><active>1</active><container>0</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('47', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>164</prodId><prodName>广味香肠</prodName><prodDesc>prodDesc:1</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/guangweixiangchang/guangweixiangchang1.jpg</prodPic><prodUrl>prodUrl:1</prodUrl><colCode>0004</colCode><active>1</active><container>1</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('48', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>165</prodId><prodName>老腊肉</prodName><prodDesc>prodDesc:2</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/laolarou/laolarou1.jpg</prodPic><prodUrl>prodUrl:2</prodUrl><colCode>0004</colCode><active>1</active><container>2</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('49', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>166</prodId><prodName>猪肉小米鲊（甜味）</prodName><prodDesc>prodDesc:3</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/zhurouxiaomizhatianwei/zhurouxiaomizha(tianwei)1.jpg</prodPic><prodUrl>prodUrl:3</prodUrl><colCode>0004</colCode><active>1</active><container>3</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('50', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>167</prodId><prodName>八宝小米鲊（甜味）</prodName><prodDesc>prodDesc:4</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/babaoxiaomizhatianwei/babaoxiaomizha(tianwei)1.jpg</prodPic><prodUrl>prodUrl:4</prodUrl><colCode>0004</colCode><active>1</active><container>4</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('51', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>168</prodId><prodName>贵州辣子鸡</prodName><prodDesc>prodDesc:5</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/guizhoulaziji/guizhoulaziji1.jpg</prodPic><prodUrl>prodUrl:5</prodUrl><colCode>0004</colCode><active>1</active><container>5</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('52', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>169</prodId><prodName>蔬菜猪肉干（醇香味）</prodName><prodDesc>prodDesc:6</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/shucaizhourouganchunxiangwei/shucaizhourougan(chunxiangwei)1.jpg</prodPic><prodUrl>prodUrl:6</prodUrl><colCode>0004</colCode><active>1</active><container>6</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('53', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>170</prodId><prodName>精制猪肉脯（醇香味）</prodName><prodDesc>prodDesc:7</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/jingzhizhuroupuchunxiangwei/jingzhizhuroupu(chunxiangwei)1.jpg</prodPic><prodUrl>prodUrl:7</prodUrl><colCode>0004</colCode><active>1</active><container>7</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('54', '3', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><col><colName>肉</colName><colCode>0004</colCode><active>1</active><container>2</container></col>');
INSERT INTO `fsn_display_uri` VALUES ('55', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>224</prodId><prodName>黄牛奶</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/huangniunai/huangniunai.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0001</colCode><active>1</active><container>9</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('56', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>233</prodId><prodName>香辣臭腐乳</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/xianglachoufuru/xianglachoufuru.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0003</colCode><active>1</active><container>9</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('57', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>234</prodId><prodName>香辣腐乳</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/xianglafuru/xianglafuru.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0003</colCode><active>1</active><container>10</container></prod>');
INSERT INTO `fsn_display_uri` VALUES ('58', '1', '<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><prod><prodId>159</prodId><prodName>维生素AD奶</prodName><prodDesc>prodDesc:0</prodDesc><prodPic>http://211.151.134.74:8080/portal/img/product/weishengsuADgainai/weishengsuADgainai1.jpg</prodPic><prodUrl>resource/img/portal/qwf.png</prodUrl><colCode>0001</colCode><active>1</active><container>10</container></prod>');

-- ----------------------------
-- Table structure for `fsn_user`
-- ----------------------------
DROP TABLE IF EXISTS `fsn_user`;
CREATE TABLE `fsn_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usr_name` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '登录名',
  `usr_pwd` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '登录密码',
  `usr_type` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT 'default' COMMENT '用户类型：匿名用户、cookie用户，实名用户',
  `usr_email` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '电子邮件',
  `usr_gender` int(1) DEFAULT NULL COMMENT '0-男 1-女',
  `usr_birth` date DEFAULT NULL COMMENT '出生年',
  `usr_birthplace` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '出生地',
  `usr_occupation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '职业',
  `usr_income` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '收入',
  `usr_sta` int(1) NOT NULL DEFAULT '0' COMMENT '用户状态: 0-未激活,1-激活,2-高级用户审核被退回',
  `usr_key` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '验证码（账号激活码）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of fsn_user
-- ----------------------------

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  `sender_type` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `receiver_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for `nutri_rpt`
-- ----------------------------
DROP TABLE IF EXISTS `nutri_rpt`;
CREATE TABLE `nutri_rpt` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `product_id` int(11) DEFAULT '0' COMMENT 'product ID',
  `nutri_id` int(11) DEFAULT NULL COMMENT '营养名称',
  `unit` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营养值单位',
  `value` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '每个计算单位中的营养值',
  `per` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '计算单位如每100ml,每份',
  `nrv` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营养所占百分比',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='营养报告';

-- ----------------------------
-- Records of nutri_rpt
-- ----------------------------
INSERT INTO `nutri_rpt` VALUES ('1', '1', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('2', '1', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('3', '1', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('4', '1', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('5', '1', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('6', '2', '1', '大卡', '69', '每100克', '0.69');
INSERT INTO `nutri_rpt` VALUES ('7', '2', '3', '克', '3.80', '每100克', '4.8');
INSERT INTO `nutri_rpt` VALUES ('8', '2', '2', '克', '3.30', '每100克', '3.2');
INSERT INTO `nutri_rpt` VALUES ('9', '2', '4', '克', '5.00', '每100克', '5.3');
INSERT INTO `nutri_rpt` VALUES ('10', '2', '6', '克', '1.00', '每100克', '1.4');
INSERT INTO `nutri_rpt` VALUES ('11', '163', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('12', '163', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('13', '163', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('14', '163', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('15', '163', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('16', '164', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('17', '164', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('18', '164', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('19', '164', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('20', '164', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('22', '165', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('23', '165', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('24', '165', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('25', '165', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('26', '165', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('27', '166', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('28', '166', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('29', '166', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('30', '166', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('31', '166', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('32', '167', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('33', '167', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('34', '167', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('35', '167', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('36', '167', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('37', '168', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('38', '168', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('39', '168', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('40', '168', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('41', '168', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('42', '169', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('43', '169', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('44', '169', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('45', '169', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('46', '169', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('47', '170', '1', '千焦', '253', '每份13克', '2.53');
INSERT INTO `nutri_rpt` VALUES ('48', '170', '2', '克', '0.5', '每份13克', '0.625');
INSERT INTO `nutri_rpt` VALUES ('49', '170', '3', '克', '2.3', '每份13克', '4.6');
INSERT INTO `nutri_rpt` VALUES ('50', '170', '4', '克', '9.4', '每份13克', '2.35');
INSERT INTO `nutri_rpt` VALUES ('51', '170', '5', '毫克', '40', '每份13克', '6.67');
INSERT INTO `nutri_rpt` VALUES ('52', '240', '1', '千焦', '290', '每100 ml', '3');
INSERT INTO `nutri_rpt` VALUES ('53', '240', '2', '克', '8.1', '每100 ml', '14');
INSERT INTO `nutri_rpt` VALUES ('54', '240', '3', '克', '0.0', '每100 ml', '0');
INSERT INTO `nutri_rpt` VALUES ('55', '240', '4', '克', '9.0', '每100 ml', '3');
INSERT INTO `nutri_rpt` VALUES ('56', '240', '5', '毫克', '6500', '每100 ml', '325');
INSERT INTO `nutri_rpt` VALUES ('57', '239', '1', '千焦', '310', '每100 ml', '4');
INSERT INTO `nutri_rpt` VALUES ('58', '239', '2', '克', '3.4', '每100 ml', '6');
INSERT INTO `nutri_rpt` VALUES ('59', '239', '3', '克', '4.1', '每100 ml', '7');
INSERT INTO `nutri_rpt` VALUES ('60', '239', '4', '克', '5.0', '每100 ml', '2');
INSERT INTO `nutri_rpt` VALUES ('61', '239', '5', '毫克', '110', '每100 ml', '14');
INSERT INTO `nutri_rpt` VALUES ('62', '197', '1', '千焦', '2680', '每100克', '32');
INSERT INTO `nutri_rpt` VALUES ('63', '197', '2', '克', '6.8', '每100克', '11');
INSERT INTO `nutri_rpt` VALUES ('64', '197', '3', '克', '55.0', '每100克', '92');
INSERT INTO `nutri_rpt` VALUES ('65', '197', '4', '克', '31.8', '每100克', '11');
INSERT INTO `nutri_rpt` VALUES ('66', '197', '5', '毫克', '440', '每100克', '22');
INSERT INTO `nutri_rpt` VALUES ('67', '203', '1', '千焦', '688', '每100克', '8');
INSERT INTO `nutri_rpt` VALUES ('68', '203', '2', '克', '0.6', '每100克', '1');
INSERT INTO `nutri_rpt` VALUES ('69', '203', '3', '克', '11.0', '每100克', '18');
INSERT INTO `nutri_rpt` VALUES ('70', '203', '4', '克', '21.0', '每100克', '7');
INSERT INTO `nutri_rpt` VALUES ('71', '203', '5', '毫克', '680', '每100克', '34');

-- ----------------------------
-- Table structure for `nutri_std`
-- ----------------------------
DROP TABLE IF EXISTS `nutri_std`;
CREATE TABLE `nutri_std` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营养名称',
  `daily_intake` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '一天摄入量',
  `unit` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='营养标准表';

-- ----------------------------
-- Records of nutri_std
-- ----------------------------
INSERT INTO `nutri_std` VALUES ('1', '能量', '10000', '千焦');
INSERT INTO `nutri_std` VALUES ('2', '蛋白质', '80', '克');
INSERT INTO `nutri_std` VALUES ('3', '脂肪', '50', '克');
INSERT INTO `nutri_std` VALUES ('4', '碳水化合物', '400', '克');
INSERT INTO `nutri_std` VALUES ('5', '钠', '600', '毫克');
INSERT INTO `nutri_std` VALUES ('6', '纤维素', '100', '克');

-- ----------------------------
-- Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `format` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `regularity` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `barcode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8_unicode_ci,
  `business_brand_id` bigint(20) DEFAULT NULL,
  `fda_product_group` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `producer_id` bigint(20) DEFAULT NULL,
  `qscore_self` float(3,2) NOT NULL DEFAULT '5.00' COMMENT '自检评分',
  `qscore_censor` float(3,2) NOT NULL DEFAULT '5.00' COMMENT '送检评分',
  `qscore_sample` float(3,2) NOT NULL DEFAULT '5.00' COMMENT '抽检评分',
  `imgurl` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品图片url路径',
  `des` text COLLATE utf8_unicode_ci COMMENT '产品描述',
  `cstm` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '适宜人群',
  `ingredient` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '主要成分',
  `category` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `feature` text COLLATE utf8_unicode_ci,
  `characteristic` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '产品特色',
  `expiration_date` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '产品保质期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_barcode` (`barcode`),
  KEY `fk_product_business_brand` (`business_brand_id`),
  KEY `fk_product_fda_product_group` (`fda_product_group`),
  KEY `idx_name` (`name`),
  KEY `fk_product_business_unit` (`producer_id`),
  CONSTRAINT `fk_product_business_brand` FOREIGN KEY (`business_brand_id`) REFERENCES `business_brand` (`id`),
  CONSTRAINT `fk_product_business_unit` FOREIGN KEY (`producer_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `fk_product_fda_product_group` FOREIGN KEY (`fda_product_group`) REFERENCES `fda_product_group` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=10099 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('1', '雀巢咖啡', '固态', '42条X13克', 'Q/QC0001S', '6917878033662', null, '10', null, '376', '5.00', '3.00', '4.00', 'http://211.151.134.74:8080/portal/img/product/nescafe/nescafe1-1.jpg|http://211.151.134.74:8080/portal/img/product/nescafe/nescafe1-2.jpg|http://211.151.134.74:8080/portal/img/product/nescafe/nescafe1-3.jpg', '雀巢公司陆续在中国的天津、青岛和东莞等地建起了自己的工厂，1999年秋天，雀巢成功地收购了太太乐集团80%的股份。之后，雀巢又于2001年与中国第二大鸡精生产厂家豪吉公司合作，成立了合资四川豪吉食品有限公司。\r\n雀巢在市场上咄咄逼人的态势，让它的竞争对手感到巨大的压力，国内一个乳制品企业的管理人员将雀巢形容为一个食品领域的“巨人”，随时都可以在相关的竞争领域发起攻势。', '所有人群', '植脂末[葡萄糖浆、食用氢化植物油、稳定剂（340ii、331iii、452i）、酪蛋白（含牛奶蛋白）、乳化剂、（471、472e）、食用香精、抗结剂（551）]、 白砂糖、速溶咖啡 、食品添加剂[（酸度调节剂（501i）、阿斯甜（含苯丙氨酸）]、食用香精。', '010101', '规格1--参数2', '高蛋白', null);
INSERT INTO `product` VALUES ('2', '雀巢咖啡', '固体', '143g/盒', 'Q/QC 0004S', '6917898001982', null, '10', null, '376', '3.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/nescafe/nescafe2-1.jpg|http://211.151.134.74:8080/portal/img/product/nescafe/nescafe2-2.jpg|http://211.151.134.74:8080/portal/img/product/nescafe/nescafe2-3.jpg', '雀巢公司陆续在中国的天津、青岛和东莞等地建起了自己的工厂，1999年秋天，雀巢成功地收购了太太乐集团80%的股份。之后，雀巢又于2001年与中国第二大鸡精生产厂家豪吉公司合作，成立了合资四川豪吉食品有限公司。\r\n雀巢在市场上咄咄逼人的态势，让它的竞争对手感到巨大的压力，国内一个乳制品企业的管理人员将雀巢形容为一个食品领域的“巨人”，随时都可以在相关的竞争领域发起攻势。', '成年人', '植脂末[葡萄糖浆、食用氢化植物油、稳定剂（340ii、331iii、452i）、酪蛋白（含牛奶蛋白）、乳化剂、（471、472e）、食用香精、抗结剂（551）]、 白砂糖、速溶咖啡 、食品添加剂[（酸度调节剂（501i）、阿斯甜（含苯丙氨酸）]、食用香精。', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('3', '咖啡伴侣植脂末', '粉末状', '100克/瓶', 'Q/QC 0002S', '6903473021067', null, '11', null, '11', '5.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('4', '醇品雀巢咖啡', '颗粒', '50g/瓶', 'Q/QC0001S', '6903473100014', null, '16', null, '16', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('5', '纯果乐', '液体', '420毫升/瓶', 'Q/BSYL0018S', '6934024516107', null, '19', null, '19', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('6', '冰鲜啤酒', '液态', '486ml/瓶', 'GB4927', '6934067290309', null, '29', null, '29', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('7', 'e.脆点雪糕', '', '65克/支', 'SB/T10015（组合型）', '6945512520421', null, '33', null, '33', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('8', '泡椒牛肉粽', '固体', '520g/袋', 'SB/T 10412', '无', null, '34', null, '34', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('9', '果粒橙汁饮料', '液体', '2.5L/瓶', 'GB/T21731', '6927573109005', null, '40', null, '40', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('10', '果粒橙', '液体', '450ml/瓶', 'Q/LYS0001S', '6934004601335', null, '42', null, '42', '3.00', '3.00', '3.00', null, null, null, null, '010101', '规格1--参数2', '高蛋白', null);
INSERT INTO `product` VALUES ('11', '乐满家椰果爽饮品', '液体', '500g/瓶', 'Q/ZJHLJ 0004S', '6924254676160', null, '43', null, '43', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('12', '乐满家粒粒橙饮品', '液体', '500g/瓶', 'Q/ZJHLJ 0004S', '6924254676122', null, '43', null, '43', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('13', '乐满家菠萝爽饮品', '液体', '500g/瓶', 'Q/ZJHLJ 0004S', '6924254676153', null, '43', null, '43', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('14', '板栗鲜肉粽', '固体', '300克/袋', 'GB19855-2005', '6953476510112', null, '56', null, '56', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('15', '栗子鲜肉粽', '固体', '280克/袋', 'SB/T10377', '6907619663432', null, '56', null, '56', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('16', '细沙枣泥粽', '固体', '280克/袋', 'SB/T10377', '6907619663371', null, '56', null, '56', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('17', '蛋黄鲜肉粽', '固体', '280克/袋', 'SB/T10377', '6907619663388', null, '56', null, '56', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('18', '迷你豆沙粽', '固体', '200克/袋', 'SB/T10377', '6907619547367', null, '56', null, '56', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('19', '迷你鲜肉粽', '固体', '200克/袋', 'SB/T10377', '69076195474667', null, '56', null, '56', '3.00', '3.00', '3.00', null, null, null, null, '010101', '规格1:参数1', '高蛋白', null);
INSERT INTO `product` VALUES ('20', '鲜肉粽', '固体', '280克/袋', 'SB/T10377', '6907619663364', null, '56', null, '56', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('21', '鲜肉粽', '固体', '300克/袋', 'GB19855-2005', '6953476510068', null, '56', null, '56', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('22', '伊利牧场果酱层趣雪糕', '', '80g/袋', 'SB/T 10015(组合型）', '6907992820736', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('23', '冰工厂', '固体', '75克/支', '伊利', '6907992817422', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('24', '冰工厂冰片蜜桃棒冰', '', '80克', 'SB/T10016（组合型）', '6907992818696', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('25', '冰工厂缤纷果园棒冰', '', '80克/袋', 'SB/T10016（组合型）', '6907992819181', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('26', '四个圈雪糕', '', '75克/袋', 'SB/T10015(组合型）', '6907992819396', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('27', '小布丁雪糕', '', '48克/袋', 'SB/T41148(清型）', '6907992811611', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('28', '小雪生雪糕', '固体', '65g/袋', 'SB/T10015', '6907992816296', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('29', '巧乐兹雪糕', '', '75克/支', 'SB/T10015(组合型）', '6907992820835', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('30', '玉米香雪糕', '', '65克', 'SB/T10015', '6907992815909', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('31', '红枣牛奶味雪糕', '', '70克', 'SB/T10015', '6907992819150', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('32', '绿色情怡雪糕', '', '71克/支', 'Q/NYLC00075(清型）', '00000000000000', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('33', '雪糕', '固体', '80g/袋', 'SB/T10015', '6907992818375', null, '59', null, '59', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('34', '优乐美奶茶', '粉末状', '22克/袋', 'Q/XZL0002S', '6926475204115', null, '60', null, '60', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('35', '优乐美奶茶麦香味', '粉末状', '22克/袋', 'Q/XZL0002S', '6926475204283', null, '60', null, '60', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('36', '优乐美奶茶（麦香味）', '固态', '80克/瓶', 'Q/XZL0002S', '6926475203156', null, '60', null, '60', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('37', '珍珠奶茶', '固态', '70g/盒', 'Q/XZL0002S', '6926475203712', null, '60', null, '60', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('38', '草莓味珍珠奶茶', '粉末状', '70g/盒', 'Q/XZL0002S', '6926475203729', null, '60', null, '60', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('39', '牛奶蜜豆雪糕', '', '75克', 'SB/T10015', '6907992816036', null, '62', null, '62', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('40', '纯生啤酒', '透明液体', '588ml/瓶', 'GB4927', '6941880800325', null, '69', null, '69', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('41', '30%混合果蔬', '液体', '500ml/瓶', 'Q/NFS 0003S', '6921168532001', null, '73', null, '73', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('42', '30%混合果蔬', '液体', '500ml/瓶', 'Q/NFS0003S', '6921168532049', null, '73', null, '73', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('43', '混合果蔬', '液体', '500ml/瓶', 'Q/NFS0003S', '6921168532025', null, '73', null, '73', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('44', '速冻豆腐花', '固体', '192g/包', 'GB/T23782', '6901432306583', null, '75', null, '75', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('45', '速溶女人豆浆', '固体', '246克/袋', 'Q/BQSY01', '6901432533606', null, '75', null, '75', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('46', '速溶豆浆晶', '固体', '160g/袋', 'Q/BQSY01', '6901432737929', null, '75', null, '75', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('47', '中老年黑芝麻糊', '固体', '600克/袋', 'G/HWL0001S', '6901333110722', null, '87', null, '87', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('48', '柠檬味菓珍', '固体', '400克/袋', 'Q/KAFUGT9', '6904724022420', null, '88', null, '88', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('49', '芒果菓珍', '固态', '400g/袋', 'Q/KAFUGT9', '6904724022468', null, '88', null, '88', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('50', '菓珍速溶固体饮料', '固态', '400克/袋', 'Q/KAFUGT9', '6904724022444', null, '88', null, '88', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('51', '菓珍速溶固体饮料（阳光甜橙味）', '固态', '400克/袋', 'Q/KAFUGT0009S', '6904724022406', null, '88', null, '88', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('52', '可爱多', '固体', '67克/支', '和路雪', '6909493400103', null, '97', null, '97', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('53', '苹果口味果冻冰棍', '固体', '60g/支', 'SB/T10016', '6909493800057', null, '97', null, '97', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('54', '金澳纯生风味啤酒', '无色透明液体', '330ml/瓶', 'GB4927', '6901035603348', null, '103', null, '103', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('55', '啤酒', '液体', '500ml/瓶', 'GB4927', '6920807200059', null, '104', null, '104', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('56', '扬百利', '液体', '450ml/瓶', 'Q/ZJYM2', '6940870920012', null, '104', null, '104', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('57', '茅台啤酒', '液体', '518ml/瓶', 'GB4927', '6916065700042', null, '104', null, '104', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('58', '纯生态啤酒', '无色透明液体', '580ml/瓶', 'GB4927-2008(一级）', '6930098220315', null, '105', null, '105', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('59', '冰块', '固体', '90克/袋', 'SB/T10327', '6940777000015', null, '109', null, '109', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('60', '绿色恋情', '固体', '77克/支', 'SB/T10016', '6925147001038', null, '113', null, '113', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('61', '娃哈哈桂园莲子', '半固体', '360克/瓶', 'QB/T2221', '6902083880781', null, '129', null, '129', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('62', '木糖醇营养八宝粥', '固体', '360克/罐', 'Q/WHJ0881', '0000000000000', null, '129', null, '129', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('63', '橙汁饮品', '液体', '500ml/瓶', 'GB/T21731', '6902083883041', null, '129', null, '129', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('64', '葡萄汁饮品', '液体', '500ml/瓶', 'Q/WHJ0283S', '6902083884369', null, '129', null, '129', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('65', '蜂蜜冰糖雪梨', '液体', '450ml/瓶', 'Q/WHJ0931S', '6902083893170', null, '129', null, '129', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('66', '蜂蜜金桔柠檬复合果汁饮料', '液体', '450ml/瓶', 'Q/WHJ 0932S', '6902083893187', null, '129', null, '129', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('67', '金晶菊花晶', '固体', '380克/袋', '', '6941889300475', null, '130', null, '130', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('68', '栗子鲜肉粽', '固体', '320克/袋', 'SB/T10377-2004', '6933949062768', null, '131', null, '131', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('69', '蛋黄莲蓉粽', '固体', '100克/袋', 'SB/T10377-2004', '6933949021659', null, '131', null, '131', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('70', '鲜肉粽', '固体', '100克/袋', 'SB/T10377-2004', '6933949021758', null, '131', null, '131', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('71', '青岛青牌酒', '液态', '600ml/瓶', 'GB4927', '6932769800041', null, '137', null, '137', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('72', '橙汁饮品', '液体', '450ml/瓶', 'GB/T 21731', '6922456812096', null, '145', null, '145', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('73', '白葡萄汁饮品', '液体', '450ml/瓶', 'Q/14A0239S', '6922456808181', null, '145', null, '145', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('74', '燕麦八宝粥', '半固体', '320g/瓶', 'Q/FJKH002', '6905337013270', null, '148', null, '148', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('75', '经典猪肉粽', '固体', '1kg/袋', 'SB/T10412', '6921665710094', null, '154', null, '154', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('76', '醇香花生粽', '固体', '500g/袋', 'SB/T10412', '6921665709777', null, '154', null, '154', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('77', '我滴橙果粒饮料', '液态', '500g/瓶', 'Q/ZBG0036S', '6900157527785', null, '157', null, '157', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('78', '果粒多葡萄汁饮料', '液态', '350ml/瓶', 'Q/NJFW0001S', '6920658284284', null, '166', null, '166', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('79', '杨梅果汁饮料', '液体', '450ml/瓶', 'Q/ZJYM2', 'YLGSJ2012070001', null, '180', null, '180', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('80', '枇杷饮品', '液体', '500ml/瓶', 'Q/JJB 0002S', '6943871200770', null, '186', null, '186', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('81', '西柚味复合果汁饮料', '液体', '445ml/瓶', 'Q/NFS 0008S', '6921168500970', null, '197', null, '197', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('82', '水晶葡萄饮料', '液体', '500ml/瓶', 'Q/MAAD0001S', '6923555215597', null, '200', null, '200', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('83', '汇源纯果汁', '液体', '1升', 'Q/MAAD0001S', '6923555210462', null, '200', null, '200', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('84', '橙粒苹果饮料', '液体', '600g/瓶', 'Q/KAD04-2009', '6945032921968', null, '221', null, '221', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('85', '芒果多芒果汁饮料', '液体', '500ml/瓶', 'Q/KAD 0001 S-2010', '6945032920558', null, '221', null, '221', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('86', '燕京啤酒', '液体', '595ml/瓶', 'GB4927', '6903102430086', null, '224', null, '224', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('87', '燕京啤酒', '液态', '330ml/瓶', 'GB4927', '6903102430161', null, '224', null, '224', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('88', '小奶糕（牛奶味雪糕）', '', '40g', 'SB/T10015', '6950179720138', null, '225', null, '225', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('89', '益生元高钙菊花晶', '固体', '460g/瓶', 'Q/JRB0001S-2010', '6940131000685', null, '229', null, '229', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('90', '红舌头', '固体', '60克/袋', 'SB/T10016', '6907720813146', null, '232', null, '232', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('91', '纯果乐果缤纷蜜桃樱桃味', '液态', '450毫升/瓶', 'Q/BSYL0013S', '6934024515902', null, '236', null, '373', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('92', '纯果乐鲜果粒', '液态', '420毫升/瓶', 'GB/T21731', '6934024515605', null, '236', null, '373', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('93', '芒果珍果汁饮料', '液态', '500ml/瓶', 'Q/JYH0015S', '6926452306092', null, '242', null, '242', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('94', '酸角汁果汁饮料', '液态', '500ml/瓶', 'Q/JYY0008S', '6926452306283', null, '242', null, '242', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('95', '雪梨汁', '液态', '500ml/瓶', 'Q/JYH0001S', '6926452306085', null, '242', null, '242', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('96', '莆田八宝粥', '半固体', '360g/瓶', 'QB/T2221', '6923284168058', null, '244', null, '244', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('97', '统一金橘柠檬水果饮料', '液体', '500ml/瓶', 'Q/TYK 0003S', '6925303724900', null, '254', null, '254', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('98', '统一鲜橙多', '液体', '450ml/瓶', 'GB/T21731', '6925303721039', null, '254', null, '254', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('99', '鲜橙多', '液体', '2L/瓶', 'GB/T21731', '6925303721244', null, '254', null, '254', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('100', '甜豆浆粉', '固体', '500克/袋', 'GB/T18738', '6904432302210', null, '255', null, '255', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('101', '维他型豆奶粉', '固体', '500克/袋', 'GB/T18738', '6904432800372', null, '255', null, '255', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('102', '绿杰七个苹果发酵型苹果醋饮料', '液体', '280ml/瓶', 'Q/YLJ 0002S-2010', '6926356030963', null, '258', null, '258', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('103', '浪漫星期天', '固体', '60克/袋', 'SB/T10015', '6920612642693', null, '262', null, '262', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('104', 'C粒•柠檬', '液体', '420ml/瓶', 'Q/SBDG0006S', '6920927182697', null, '267', null, '267', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('105', '美汁源C粒·柠檬', '液体', '420ml/瓶', 'Q/SBDG0006S', '6920927182679', null, '267', null, '267', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('106', '美汁源果粒橙', '液体', '420ml/瓶', 'Q/SBDG0001S', '6920927182631', null, '267', null, '267', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('107', '超纯啤酒', '液态', '330ml/瓶', 'GB4927', '6934598902191', null, '270', null, '270', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('108', '冰+', '固体', '85g/支', 'SB/T10016', '6923644275051', null, '279', null, '279', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('109', '巧脆兹雪糕', '', '78克/袋', 'SB/T10015', '6923044274054', null, '279', null, '279', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('110', '布丁雪糕', '', '45g/袋', 'SB/T10015', '6923644275174', null, '279', null, '279', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('111', '绿色心情', '固体', '71克/支', 'Q/NMRY0044S', '6923644266257', null, '279', null, '279', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('112', '老冰棍', '固体', '88克/支', 'SB/T10016', '6923644268688', null, '279', null, '279', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('113', '随变旋顶杯', '', '120克', 'SB/T10015', '6923644272746', null, '279', null, '279', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('114', '蓝色经典啤酒', '液态', '486ml/瓶', 'GB4927', '6934067290040', null, '283', null, '283', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('115', '纯爽啤酒', '液体', '330ml/瓶', 'GB4927', '6925452500752', null, '284', null, '284', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('116', '蓝贝啤酒（纯爽）', '液体', '500ml/听', 'GB4927', '6925452501001', null, '285', null, '285', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('117', '幽沫奶味茶', '固态', '80g/盒', 'Q/JLBX006(F)', '6921101210928', null, '288', null, '288', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('118', '桂园莲子八宝粥', '半固体', '360克/瓶', 'QB/T2221', '6911988011985', null, '298', null, '298', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('119', '红枣桂园八宝粥', '-', '360克/瓶', 'QB/T2221', '6911988013965', null, '298', null, '298', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('120', '红枣桂圆八宝粥罐头', '粘稠固体', '360g/罐', 'QB/T2221', '6926892565080', null, '298', null, '298', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('121', '雀巢咖啡', '固体', '500g/罐', 'Q/QC001', '6903473015103', null, '302', null, '302', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('122', '金威特爽啤酒', '液体', '500ml/瓶', 'GB4927', '6942481699066', null, '309', null, '309', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('123', '中老年加钙豆奶粉', '固体', '500克/袋', 'GB/T18738', '6923118623210', null, '314', null, '314', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('124', '营养维他豆奶粉', '固体', '500g/袋', 'GB/T18738', '6923118623203', null, '314', null, '314', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('125', '好粥道八宝粥罐头', '半固体', '280克/瓶', 'Q/XMYL0006S', '6926892568081', null, '319', null, '319', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('126', '好粥道莲子玉米粥', '半固体', '280g/瓶', 'Q/XMYL0006S', '6926892527088', null, '319', null, '319', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('127', '椰奶燕麦粥罐头', '粘稠固体', '280g/罐', 'Q/XMYL0006S', '6926892568089', null, '319', null, '319', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('128', '奶香乐', '固体', '63g/袋', 'SB/T10015', '6918551810600', null, '322', null, '322', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('129', '花心筒', '固体', '68g/支', 'Q/CYQCZ223', '6918551810839', null, '322', null, '322', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('130', '卡布奇诺咖啡', '固体', '65克/盒', 'Q/PAAV0001S', '6917878017891', null, '323', null, '323', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('131', '即溶咖啡（原味1+2）', '固态', '624克/盒', 'Q/QC0004S', '6917878002446', null, '323', null, '323', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('132', '白咖啡', '固态', '145克/盒', 'Q/PAAV0001S', '6917878036564', null, '323', null, '323', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('133', '雀巢咖啡原味', '粉末状', '169克/盒', 'Q/QC0004S', '6917878007441', null, '323', null, '323', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('134', '柠檬味固体饮料', '固态', '500g/袋', 'Q/OAEF7', '6917878037653', null, '324', null, '324', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('135', '柠檬味固体饮料', '粉末状', '500克/袋', 'Q/OAEF7', '6917878007731', null, '324', null, '324', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('136', '橙味固体饮料', '固体', '500克/袋', 'Q/OAEF7', '6917878003337', null, '324', null, '324', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('137', '橙味固体饮料', '固态', '400g/袋', 'Q/PAAV0010S', '6917878037578', null, '324', null, '324', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('138', '芒果味固体饮料', '粉末状', '500克/袋', 'Q/OAEF7', '6917878014876', null, '324', null, '324', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('139', '雀巢果维', '固态', '500克/袋', 'Q/OAEF7', '6917878004709', null, '324', null, '324', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('140', '雀巢果维固体饮料', '固体', '500克/袋', 'Q/OAEF7', '6917878018201', null, '324', null, '324', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('141', '黑加仑味固体饮料', '粉末状', '125克/袋', 'Q/OAEF7', '6917878022758', null, '324', null, '324', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('142', '啤酒', '透明液体', '580ml/瓶', 'GB4927(优级）', '6920252600282', null, '330', null, '330', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('143', '啤酒（勇闯天涯）', '液体', '330ml/听', 'GB4927', '6949352200017', null, '330', null, '330', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('145', '相约奶茶', '固体', '72克/罐', 'Q/DHD0009S', '6926858908050', null, '347', null, '347', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('146', '即溶冲调奶茶', '固态', '64克/盒', 'Q/XPP0001S', '6938888888615', null, '348', null, '348', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('147', '奶茶', '固体', '80g/瓶', 'Q/XPP0001S', '6938888888837', null, '348', null, '348', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('148', '高原啤酒', '液体', '518ml/瓶', 'GB4927', '6916065900046', 'AAA444', '351', null, '351', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('154', '美年达', '液态', '600毫升/瓶', 'GB/T10792', '6915766000390', '橙味汽水', '236', null, '373', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('155', '达利园派', '固体', '1只/包', 'QS530424010384', '6911988005373', null, '298', null, '374', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('156', '蒙牛酸酸乳', '液态', '250ML/盒', null, '6923644264895', null, '279', null, '375', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('157', '草莓酸奶', '液态', '200克/袋', null, '6927770901273', null, '367', null, '377', '3.00', '3.00', '3.00', null, null, null, null, '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('158', '280g辣三丁油辣椒', '半固体', '280g/瓶', 'GB/T20293', '6921377400085', null, '368', null, '379', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/lasandingyoulajiao/lasandingyoulajiao1.jpg|http://211.151.134.74:8080/portal/img/product/lasandingyoulajiao/lasandingyoulajiao2.jpg', '选用高原猪肉丁，优质花生，豆腐丁而得名，采用秘传手艺精心制作，由于三种主料经油炸后酥脆爽口，嚼碎后在口中炸开，特别收消费者的喜爱。', '嗜辣', '植物油、花生、黄豆、辣椒、猪肉、芝麻、食盐、姜香辛料、食品添加剂（谷氨酸钠）', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('159', '维生素AD奶', '液体', '243mL/袋', null, null, null, '367', null, '377', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/weishengsuADgainai/weishengsuADgainai1.jpg', '含钙量远远高于一般牛奶，非常适合青少年、妇女、中年人以及其它缺钙的消费者饮用。由于它特别强化了促进钙的吸收的VD3因子，因此更容易被人体吸收。所以饮用海子高钙奶的效果一般会好于服用其它药物。与普通纯鲜奶相比，加钙纯鲜奶不仅强化了奶中可溶性生物活性钙，还增加了促进钙吸收的维生素D的含量。\r\n加钙纯鲜奶多为巴氏杀菌法处理过的袋装奶和盒装奶，可以更多地保留牛奶中的营养成分，最容易被人体消化吸收。', '小孩、老年人及缺钙人群', '牛奶', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('160', '国台1979', '液体', '500mL/瓶', 'DB52/526-2007', '6927322001888', null, '369', null, '380', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/guotai1979/guotai19791.jpg|http://211.151.134.74:8080/portal/img/product/guotai1979/guotai19792.jpg', '尊贵的不只是美酒，更是对岁月的历练。国台1979源自茅台镇中国酱酒核心产区，出自酱香名师手笔，珍贵非凡。深红色窑变釉瓶体，辅以传统瓷器烤花工艺，器华于外，酒珍于内。品酌间，可感岁月之隽永，可品风云之激荡。', '成人', '高粱、小麦、水', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('161', '特选1号兰馨雀舌', '固体', '60g/盒', 'DB52/478-2005', '6941315000986', null, '370', null, '381', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/texuan1haolanqinqueshe/texuan1hao1.jpg|http://211.151.134.74:8080/portal/img/product/texuan1haolanqinqueshe/texuan1hao2.jp', '俗称“极品”，公司标准称谓为“特选一号兰馨雀舌”，是兰馨雀舌系列中的稀有产品，产自兰馨公司自建的或组织专家认定授牌的“兰馨一号茶基地”（如：湄潭贡茶原产地的圣心山，湄潭高原春雪公司群峰有机茶基地）。此类基地面积小，每亩限产雀舌鲜叶10KG，产特选兰馨雀舌2KG，因此十分珍贵。', '高要求群体', '明前茶叶', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('162', '红油臭腐乳', '半固体', '260g/瓶', 'Q/WCY0003S', null, null, '371', null, '382', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/hongyouchoudoufuru/hongyouchoudoufuru1.jpg|http://211.151.134.74:8080/portal/img/product/hongyouchoudoufuru/hongyouchoudoufuru2.jpg', ' 红油腐乳是贵州的地方风味产品，产品是全自然的发酵过程，从外到里松软一致，富含蛋白质，口感一流。', '3岁以上', '大豆、水、大豆油、食盐、辣椒、花椒、白酒', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('163', '贵州腊肠', '固体', '400g/袋', 'Q/WFF0302', '6928082800031', null, '372', null, '378', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/guizhoulachang/guizhoulachang1.jpg|http://211.151.134.74:8080/portal/img/product/guizhoulachang/guizhoulachang2.jpg', '　　黔五福贵州腊肠系选用贵州高原优质猪肉原料，经腌制、烘烤等传统加工工艺制作而成。该产品风味独特、腊香浓郁、爽口不腻、不加淀粉等低质辅料、营养卫生，可烧、烤、炒、蒸、煮后食用，口味自然纯正，但建议不采用微波炉来烤制食用，是居家食用、宴请宾客、馈赠亲友之佳品。', '嗜辣', '猪肉、白砂糖、白酒、食盐、味精、花椒、食品添加剂（亚硝酸钠）', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('164', '广味香肠', '固体', '400g/袋', null, '6928082800018', null, '372', null, '378', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/guangweixiangchang/guangweixiangchang1.jpg|http://211.151.134.74:8080/portal/img/product/guangweixiangchang/guangweixiangchang2.jpg', '将优质猪肉按肥瘦一定比例合理搭配，再经腌制、电加热烘烤等的工序加工而成。香甜为主味，不加淀粉等低质辅料。成品瘦肉呈红色、枣红色、脂肪呈乳白色、色泽分明，外表有光泽。  \r\n该品滋味鲜美，香甜可口，回味悠长，肉香味浓，肉质柔软嫩滑，是不可多得的美味佳肴，可烧、烤、炒、蒸、煮后食用，口味纯正，但建议不采用微波炉来烤制食用。该产品口感独特，长期以来深受消费者的喜爱， 是我公司销量第一的产品。', '3岁以上人群，蒸熟后食用', '猪肉、肠衣、白砂糖、食盐、白酒、味精，食品添加剂（双乙酸钠、D-异抗坏血酸钠、亚硝酸钠）', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('165', '老腊肉', '固体', '400g/袋', null, '6928082800049', null, '372', null, '378', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/laolarou/laolarou1.jpg|http://211.151.134.74:8080/portal/img/product/laolarou/laolarou2.jpg', '        老腊肉是将猪带皮后腿肉，按照合理的肥瘦搭配比例,根据规定的尺寸成型。经腌制，烘烤后的一种肉制品，以贵州民间传统的腊肉制作方法为基础，结合现代腌腊肉制品制作工艺、技术，在保持传统风味的基础上，减少烟熏的时间和强度，运用科学的方法将烟熏腊肉的烟熏危害降到最低。       \r\n        熏好的腊肉，表里一致，蒸熟切成片，透明发亮，色泽鲜艳，黄里透红，吃起来味道醇香，肥不腻口，瘦不塞牙，不仅风味独特，营养丰富，而且具有开胃、去寒、消食等功能。  \r\n        “贵州腊肉”是贵州各族人民从猎耕到机种，从原始到现代历经千百年的实践而研制出来的传统美食佳品。也是他们智慧的结晶。早已芳名远扬，被世人视为送礼、待客的佳品。', '3岁以上人群，蒸熟后食用', '猪肉、食盐、白酒、花椒，食品添加剂（双乙酸钠、D-异抗坏血酸钠、亚硝酸钠）', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('166', '猪肉小米鲊（甜味）', '固体', '400g/袋', null, '6928082800124', null, '372', null, '378', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/zhurouxiaomizhatianwei/zhurouxiaomizha(tianwei)1.jpg|http://211.151.134.74:8080/portal/img/product/zhurouxiaomizhatianwei/zhurouxiaomizha(tianwei)2.jpg', '猪肉小米鲊是精选贵州高原特有的糯小米为主料，辅以肥瘦相宜的新鲜猪肉、白砂糖，通过适当的蒸煮，在蒸煮熟化时猪肉与小米充分混合，猪肉中具有的脂肪芳香味道可以充分渗透到小米中去，使猪肉鲜香和小米的芳香溶为一体，产品风味独特，营养丰富，甜爽细腻，糯香可口，回味悠长。', '3岁以上', '小米、猪肉、白砂糖', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('167', '八宝小米鲊（甜味', '固体', '400g/袋', null, '6928082800131', null, '372', null, '378', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/babaoxiaomizhatianwei/babaoxiaomizha(tianwei)1.jpg|http://211.151.134.74:8080/portal/img/product/babaoxiaomizhatianwei/babaoxiaomizha(tianwei)2.jpg', '        秋冬时节，由于气候干燥，非常容易上火，所以这个季节适合吃一些粗粮和多纤维的食物。\r\n        本产品属纯天然珍品，口感绵甜，口味悠长，风味独特，色彩鲜黄透亮，颗粒圆润饱满，是一种营养价值很高的食品。\r\n黔五福小米鲊是以贵州高原生态环境下独有的贵州金黄糯小米为主要原料,配以核桃，花生，葵花子，红枣等，以贵州少数民族特有的糯竹蒸笼闷蒸而成,是贵州的\"十大名菜之一\"，是贵州特色饮食文化的典型代表。\r\n        从撕口初撕开外袋,直接将内袋放入沸水中，蒸或煮至产品软化,取出内袋装盘即可享用。', '3岁以上', '小米、高原植物油、核桃、花生、薏仁、莲子、百合、红枣', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('168', '贵州辣子鸡', '固体', '300g/袋', null, '6928082800865', null, '372', null, '378', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/guizhoulaziji/guizhoulaziji1.jpg|http://211.151.134.74/img/product/guizhoulaziji/guizhoulaziji2.jpg|http://211.151.134.74/img/product/guizhoulaziji/guizhoulaziji3.jpg', '        贵州一大怪“辣椒当作菜”。俗话说“四川人不怕辣,湖南人辣不怕,贵州人怕不辣”,贵州人吃辣椒的确令人瞠目结舌。 \r\n        辣子鸡可算是一道极有贵州特色的菜，是贵州的传统家常名菜，每逢佳节家家户户都会烧上一锅，全家人一起享用。那种香香的辣，滋味悠长而绵软，少去了川菜般的火爆，而添了一份贵州风情的自然与醇厚。这辣子鸡的辣不似寻常辣味，是辣中有香，辣而不过，让人吃一口还想吃第二口，直到满头大汗，欲罢不能。吃剩下的鸡肉下面条，足可余香三日。此菜色鲜红，辣香爽口，肉嫩离骨，皮糯鲜香，糍粑辣椒的特有味道更使其增色不少，实乃下饭佐酒的佳肴。', '嗜辣', '鸡、天然香料（辣椒、姜、大蒜等）', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('169', '蔬菜猪肉干（醇香味）', '固体', '180g/袋', null, '6928082801541', null, '372', null, '378', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/shucaizhurouganchunxiangwei/shucaizhurougan(chunxiangwei)1.jpg|http://211.151.134.74:8080/portal/img/product/shucaizhurouganchunxiangwei/shucaizhurougan(chunxiangwei)2.jpg', '采用优质猪肉为原料，在生产过程中加入纯天然新鲜蔬菜（汁），采用预煮、切丁、炒制、烘烤成熟等工艺反复调味、分层次入味加工而成，呈丝与颗粒混合状。  \r\n优质猪肉与新鲜蔬菜的完美结合，能补充蛋白质、维生素和膳食纤维，好吃不上火。既有肉香，又有蔬菜的香味，香气浓郁、回味悠长、营养丰富。  \r\n醇香味：咸鲜味为主，带微甜。', '成人', '猪肉、蜂蜜、大豆油、黄酒、酱油、辣椒、花椒、白糖、食盐、茴香', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('170', '精制猪肉脯（醇香味）', '固体', '200g/袋', null, '6928082800988', null, '372', null, '378', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/jingzhizhuroupuchunxiangwei/jingzhizhuroupu(chunxiangwei)1.jpg|http://211.151.134.74:8080/portal/img/product/jingzhizhuroupuchunxiangwei/jingzhizhuroupu(chunxiangwei)2.jpg', '        精选猪瘦肉为原料，将肉绞制到相当细后，用高级鱼露作为调味基础，再加以新鲜鸡蛋加工而成，品呈片状。该品味道鲜美、营养丰富，有独特可口的烧烤风味。机械化的自动包装，安全、卫生。（鱼露：一种以鱼、虾为原料，加入食盐发酵而成的调味酱汁，含有17种氨基酸，其中8种是人体所必需的。蛋白质含量也很丰富。）  \r\n        精制猪肉脯醇香味：口感香甜，猪肉纤维非常明显，独特的烧烤风味，回味悠长，口齿流香，主要体现猪肉的自然清香。', '成人', '猪瘦肉、白砂糖、鱼露、鸡蛋、食盐、香辛料', '010101', null, '高蛋白', null);
INSERT INTO `product` VALUES ('171', '红国台酒', '液体', '500ml/瓶', null, '6927322001277', null, '369', null, '380', '3.00', '3.00', '3.00', '', null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('172', '国台酒（五年精品）', '液体', '500ml/瓶', null, '6927322001284', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('173', '国台酒（15年陈酿）', '液体', '500ml/瓶', null, '6927322001291', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('174', '红国台', '液体', '125ml/瓶', null, '6927322001338', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--125ml', '高蛋白', null);
INSERT INTO `product` VALUES ('175', '国台酒（高档单支礼盒）', '液体', '500ml/瓶', null, '6927322001352', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--50%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('176', '国台酒（高档双支礼盒）', '液体', '500ml/瓶', null, '6927322001369', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--50%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('177', '国台46°酒（五年精品）', '液体', '500ml/瓶', null, '6927322001734', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--46%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('178', '国台46°酒（十五年珍藏陈酿）', '液体', '500ml/瓶', null, '6927322001741', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--46%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('179', '国台十五年珍藏', '液体', '600ml/瓶', null, '6927322001864', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--600ml', '高蛋白', null);
INSERT INTO `product` VALUES ('180', '国台十年珍藏', '液体', '500ml/瓶', null, '6927322001871', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('181', '国台1999', '液体', '500ml/瓶', null, '6927322001895', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('182', '国台2009', '液体', '500ml/瓶', null, '6927322001901', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('183', '红国台（新包装）', '液体', '500ml/瓶', null, '6927322002519', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('184', '金国台', '液体', '500ml/瓶', null, '6927322002526', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('185', '银国台', '液体', '500ml/瓶', null, '6927322002533', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('186', '红国台（新包装）', '液体', '500ml/瓶', null, '6927322002540', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--46%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('187', '金国台', '液体', '500ml/瓶', null, '6927322002557', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--46%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('188', '银国台', '液体', '500ml/瓶', null, '6927322002564', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--46%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('189', '红国台（新包装）', '液体', '300ml/瓶', null, '6927322002571', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--46%vol,净含量--300ml', '高蛋白', null);
INSERT INTO `product` VALUES ('190', '金国台', '液体', '300ml/瓶', null, '6927322002588', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--46%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('191', '银国台', '液体', '300ml/瓶', null, '6927322002595', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--46%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('192', '相邀酒 (金酱) ', '液体', '500ml/瓶', null, '6927322006012', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('193', '相邀酒 (银酱)', '液体', '500ml/瓶', null, '6927322006029', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('194', '相邀酒 (五星) ', '液体', '500ml/瓶', null, '6927322006036', null, '369', null, '380', '3.00', '3.00', '3.00', null, null, null, null, null, '酒精度--53%vol,净含量--500ml', '高蛋白', null);
INSERT INTO `product` VALUES ('195', '280g油辣椒', '半固体', '280g/瓶', null, '6921377400146', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('196', '200g油辣椒', '半固体', '200g/瓶', null, '6921377400016', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('197', '辣三丁油辣椒', '半固体', '280g/瓶', null, '6921377400030', null, '368', null, '379', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/lasandingyoulajiao/lasandingyoulajiao1.jpg|http://211.151.134.74:8080/portal/img/product/lasandingyoulajiao/lasandingyoulajiao2.jpg', '辣三丁油辣椒以贵州高原地区出产的优质辣椒、菜子油、花生等为主要原料，沉淀贵州百年辣椒种植及制作精华，延续贵阳民间传统制作“三丁”名炒工艺，采用传统工艺和现代技术相结合的方法，用公司独创炒制设备制作而成。该品为油质带立方颗粒酱状辣椒调味品，呈油亮深红色，富含蛋白质、维生素、铁、磷等多种人体必需营养及矿物质，香脆可口、辣味适度。同时，该产品还具有开胃健脾、提热驱寒、去湿除风等保健功效。', '普通消费者', '菜籽油、黄豆、豌豆、花生、辣椒、食盐、味精。', '120402', null, '高蛋白', null);
INSERT INTO `product` VALUES ('198', '280g风味豆豉油辣椒', '半固体', '280g/瓶', null, '6921377400115', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('199', '210g风味豆豉油辣椒', '半固体', '210g/瓶', null, '6921377400092', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('200', '280g香辣脆油辣椒', '半固体', '280g/瓶', null, '6921377400160', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('201', '210g香辣脆油辣椒', '半固体', '210g/瓶', null, '6921377400153', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('202', '220g油爆剁辣椒', '半固体', '220g/瓶', null, '6921377400290', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('203', '苗家酸汤', '半固体', '160g/袋', null, '6921377400566', null, '368', null, '379', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/miaojiasuantang/miaojiasuantang1.jpg|http://211.151.134.74:8080/portal/img/product/miaojiasuantang/miaojiasuantang2.jpg', '酸汤采用贵州高原黔东南苗族同胞数百年传统工艺，结合现代烹调技术精制而成。该品为油质酱状辣椒调味品，呈鲜红色，富含多种维生素，产品酸辣可口、辣味适度，具有开胃健脾、提热驱寒之功效。', '普遍消费者', '酸汤、菜籽油、味精、食盐、大蒜、姜', '120402', null, '高蛋白', '365');
INSERT INTO `product` VALUES ('204', '210g牛肉豆豉油辣椒', '半固体', '210g/瓶', null, '6921377400023', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('205', '210g肉丝豆豉油辣椒', '半固体', '210g/瓶', null, '6921377400078', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('206', '280g鸡辣椒', '半固体', '280g/瓶', null, '6921377400122', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('207', '220g鸡腿菇油辣椒', '半固体', '220g/瓶', null, '6921377400207', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('208', '300g油爆剁辣椒', '半固体', '300g/瓶', null, '6921377400108', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('209', '240g红油腐乳', '半固体', '240g/瓶', null, '6921377400054', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('210', '240g苗家腐乳', '半固体', '240g/瓶', null, '6921377400788', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('211', '230g风味水豆鼓', '半固体', '230g/瓶', null, '6921377400061', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('212', '60g香辣菜', '半固体', '60g/袋', null, '6921377400825', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('213', '220g牛肉香菇油辣椒', '半固体', '220g/瓶', null, '6921377400832', null, '368', null, '379', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('214', '特仑苏纯牛奶', '液体', '250ml*12盒', null, '6923644266066', null, '279', null, '375', '3.00', '3.00', '3.00', null, null, '', null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('215', '特仑苏低脂', '液体', '250ml*12盒', null, '6923644269579', null, '279', null, '375', '3.00', '3.00', '3.00', null, null, null, null, null, null, '高蛋白', null);
INSERT INTO `product` VALUES ('216', '纯牛奶利乐', '液体', '250ml*24盒', 'GB 25190', null, null, '279', null, '375', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/mengniuchunniunailile/mncnnll.jpg', '优质营养：牛奶中的蛋白质、矿物质等营养成分，易被人体吸收。     \r\n品味纯正：蒙牛纯牛奶纯正无添加，带给您自然纯的好口味。     \r\n优享生活：滴滴好品质，让蒙牛牛奶伴您健康生活每一天。', '所有人群', '生牛乳', null, null, '', null);
INSERT INTO `product` VALUES ('217', '低脂高钙牛奶利乐砖', '液体', '250ml*20盒', 'GB 25191', null, null, '279', null, '375', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/mengniudizhigaogainiunaililezhuan/mndzggnnllz.jpg', '蒙牛低脂高钙牛奶，脂肪含量比普通牛奶低50%以上，特别添加水解蛋黄粉和乳矿物盐，给予骨骼更多呵护，让身体时刻充满活力。', '所有人群（婴幼儿除外）', '生牛乳、乳矿物盐、水解蛋黄粉、食品添加剂（微晶纤维素、单硬脂酸甘油酯、羧甲基纤维素钠、卡拉胶、三聚磷酸钠、六偏磷酸钠、焦磷酸钠）、乳矿物盐（添加量：100mg/100g）、水解蛋黄粉（添加量：3mg/100g）', null, null, '', null);
INSERT INTO `product` VALUES ('218', '低乳糖牛奶', '液体', '250ml*12盒', 'Q/NMRY 0030S', null, null, '279', null, '375', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/mengniuxinyangdaoditangniunai/mnxyddtnn.jpg', '采用EHTTM酶水解技术，微化营养易吸收；乳糖水解易吸收，消除乳糖不耐受；益生元(低聚木糖)，让营养吸收更全面；零蔗糖，口味自然,甜香浓溢。', '都市中肠胃消化吸收不好的人', '鲜牛奶、食品添加剂（蔗糖脂肪酸酯、单硬脂酸甘油酯、乳糖酶、三聚磷酸钠、卡拉胶）、低聚木糖、食用香精（牛奶香精）、低聚木糖（添加量:49mg/100g）', null, null, '', null);
INSERT INTO `product` VALUES ('219', '优益乳', '液体', '458ml*12盒', 'GB/T 21732', null, null, '279', null, '375', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/mengniuyouyiru/mnyyr.jpg', '特别添加享誉盛名的活性乳酸菌。有助于分解和消除肠道有害菌，维护人体健康。', '酒店消费者', '水、生牛乳、白砂糖、食品添加剂【羧甲基纤维素钠、果胶、柠檬酸、山梨酸钾、安赛蜜、阿斯巴甜（含苯丙氨酸）】、保加利亚乳杆菌、嗜热链球菌、嗜酸乳杆菌、乳双歧杆菌、食用香精', null, null, '', null);
INSERT INTO `product` VALUES ('220', '优菌乳', '液体', '458ml*12盒', 'GB/T 21732', null, null, '279', null, '375', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/mengniuyijunru/mnyjr.jpg', '特别添加享誉盛名的活性乳酸菌。有助于分解和消除肠道有害菌，维护人体健康。', '酒店消费者', '水、生牛乳、低聚果糖（添加量：5g/kg）、食品添加剂【木糖醇（添加量：5g/kg）、羧甲基纤维素钠、果胶、柠檬酸、山梨酸钾、安赛蜜、阿斯巴甜（含苯丙氨酸）】、保加利亚乳杆菌、嗜热链球菌、食用香精', null, null, '', null);
INSERT INTO `product` VALUES ('221', '联杯菠萝', '液体', '100g/盒', null, null, null, '367', null, '377', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/liangbeiboluo/lianbeiboluo.jpg', '新鲜牛奶中加入天然果汁，补充人体所需各种维生素，新鲜水果口味配合香浓牛奶，口味更浓、更鲜、更自然。', '高端人群', null, null, null, '', null);
INSERT INTO `product` VALUES ('222', '1953黑加仑', '液体', '200g/袋', null, null, null, '367', null, '377', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/1953heijialun/1953heijialun.jpg', '山花1953鲜牛奶精选优质牛奶，采用闪蒸技术，脱掉牛奶中的部分水分，使口感更浓、更香、更淳，特别添加乳糖酶，缓解“乳糖不耐症”；山花1953酸牛奶特别添加益生菌，富含VB1、VB2、VB6、VB12等多种维生素，有利于钙、磷等元素的吸收。1953鲜牛奶和酸牛奶，全家营养好保障。', '中高端人群', null, null, null, '', null);
INSERT INTO `product` VALUES ('223', '壶装酸奶', '液体', '1.2L/瓶', null, null, null, '367', null, '377', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/huzhuangshuannai/huzhuangshuannai.jpg', '100％鲜纯，鲜香宜人；纯鲜奶经巴氏消毒；能很好的保存牛奶的营养价值，避免营养流失；经济实惠。', '缺钙人群', null, null, null, '', null);
INSERT INTO `product` VALUES ('224', '黄牛奶', '液体', '243ml/盒', null, null, null, '367', null, '377', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/huangniunai/huangniunai.jpg', '特别添加活性乳酸菌，除保留鲜牛奶的全部营养成分外，使多种维生素更易被人体吸收。不易发生腹胀，气多或腹泻现象，特别适合对乳糖消化不良的人群。特浓酸奶是“肠道清道夫”，是益生菌的丰富源泉。调节改善肠道微环境。1.2升大壶装，更实惠，能满足一家人一天的健康营养需求。', '肠胃消化吸收不好的人', null, null, null, '', null);
INSERT INTO `product` VALUES ('225', '红油腐乳', '半固体', '260g/瓶', null, null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/hongyoufuru/hongyoufuru.jpg', '红油腐乳是贵州的地方风味产品，产品是全自然的发酵过程，从外到里松软一致，富含蛋白质，口感一流。', '3岁以上', '大豆、水、大豆油、食盐、辣椒、花椒、白酒', null, null, '', null);
INSERT INTO `product` VALUES ('226', '黄豆酱油', '液体', '500ml/瓶', null, null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/huangdoujiangyou500ml/huangdoujiangyou500ml.jpg', null, '3岁以上', null, '120401', null, '', null);
INSERT INTO `product` VALUES ('227', '生抽王', '液体', '500ml/瓶', null, null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/shengchouwang/shengchouwang.jpg', '酱油是我国传统的调味品。味莼园生产的系列酱油选用优质原料，秉承传统工艺，采用先进技术天然发酵。可直接点蘸、佐餐、凉拌、炒菜等，可为菜肴增滋加味，添香增色。', '3岁以上', '水、脱脂大豆、麦麸、食用盐、白酒、谷氨酸钠（味精）、5\'-呈味核苷酸二钠、苯甲酸钠、安赛蜜、氨基酸态氮：≥0.70g/100mL', null, null, '', null);
INSERT INTO `product` VALUES ('228', '蒜汁酱油', '液体', '500ml/瓶', null, null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/suanzhijiangyou/suanzhijiangyou.jpg', null, '3岁以上', null, '120401', null, '', null);
INSERT INTO `product` VALUES ('229', '特鲜酱油1', '液体', '500ml/瓶', '', '69545662122212', null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/texianjiangyou/texianjiangyou.jpg', null, '3岁以上', null, '120402', null, '', null);
INSERT INTO `product` VALUES ('230', '特制酱油', '液体', '500ml/瓶', '', null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/tezhijiangyou/tezhijiangyou.jpg', null, '3岁以上', null, '120402', null, '', null);
INSERT INTO `product` VALUES ('231', '铁强化营养酱油', '液体', '500ml/瓶', null, null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/tieqianghuayinyangjiangyou/tieqianghuayinyangjiangyou.jpg', null, '3岁以上', null, '120401', null, '', null);
INSERT INTO `product` VALUES ('232', '鲜味酱油', '液体', '500ml/瓶', null, null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/xianweijiangyou/xianweijiangyou.jpg', null, '3岁以上', null, '120402', null, '', null);
INSERT INTO `product` VALUES ('233', '香辣臭腐乳', '半固体', '260g/瓶', null, null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/xianglachoufuru/xianglachoufuru.jpg', null, '重口味', null, null, null, '', null);
INSERT INTO `product` VALUES ('234', '香辣腐乳', '半固体', '260g/瓶', null, null, null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/xianglafuru/xianglafuru.jpg', null, '重口味', null, null, null, '', null);
INSERT INTO `product` VALUES ('235', '腊肉', '固态', '250g/份', null, null, null, '373', null, '383', '5.00', '5.00', '5.00', null, null, null, null, null, null, '', null);
INSERT INTO `product` VALUES ('236', '菜籽油', '液态', '250ml/瓶', null, null, null, '374', null, '384', '5.00', '5.00', '5.00', null, null, null, null, null, null, '', null);
INSERT INTO `product` VALUES ('237', '莲渣闹', '固态', '250g/袋', null, null, null, '375', null, '386', '5.00', '5.00', '5.00', null, null, null, null, null, null, '', null);
INSERT INTO `product` VALUES ('238', '都匀毛尖', '固态', '51-75g/盒', null, null, null, '10086', null, '382', '3.00', '3.00', '3.00', 'http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian1.jpg|http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian2.jpg|http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian3.jpg|http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian4.jpg|http://211.151.134.74:8080/portal/img/product/duyunmaojian/duyunmaojian5.jpg', null, '所有', null, '010101', null, '怡气养神', null);
INSERT INTO `product` VALUES ('239', '特仑苏有机纯牛奶', '液态', '250ml/盒', null, '6923644270957', null, '279', null, '375', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/menniuyoujinai/menniuyoujinai.jpg', '特仑苏有机奶源自专属有机牧场，集纳天地精华，秉承了特仑苏高品质血统，每100克牛奶中蛋白质含量≥3.3克，特仑苏有机奶每年必须经过农业部中绿华夏有机食品认证中心的严格认证，荣获第六届中国国际有机食品博览会乳品唯一金奖。', '3岁以上', '有机生牛乳', '010101', null, '蛋白质含量≥3.3克（每100克牛奶）', '180');
INSERT INTO `product` VALUES ('240', '特鲜酱油', '液体', '438ml/袋', ' ', '6921971200715', null, '371', null, '382', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/texianjiangyou/texianjiangyou438ml.png', '味莼园创建于1534年。产品选用优质脱脂大豆为原料，采用固稀发酵工艺精制而成。酱香浓郁、滋味鲜美、口感醇厚、令佳肴味道更加丰厚，是您选择的理想调味佳品。氨基酸态氮：≥0.70g/100mL', '3岁以上', '水、脱脂大豆、麦麸、食盐、谷氨酸钠、苯甲酸钠、安赛蜜、5\'-呈味核苷酸二钠', '120401', null, '氨基酸态氮：≥0.70g/100mL', '365');
INSERT INTO `product` VALUES ('10086', '六合福思测试产品1', null, null, null, '1008610086001', null, '10086', null, null, '3.00', '3.00', '3.00', null, '', null, null, '010101', '规格1--参数1,规格2--参数1,规格3--参数1', '高蛋白', null);
INSERT INTO `product` VALUES ('10087', '六合福思测试产品2', null, null, null, null, null, '10086', null, null, '3.00', '3.00', '3.00', null, null, null, null, '010101', '规格1--参数2,规格2--参数2,规格3--参数2', '高蛋白', null);
INSERT INTO `product` VALUES ('10088', '六合福思测试产品3', null, null, null, null, null, '10086', null, null, '3.00', '3.00', '3.00', null, null, null, null, '010101', '规格1--参数1,规格2--参数1,规格3--参数1', '高蛋白', null);
INSERT INTO `product` VALUES ('10089', '六合福思测试产品4', null, null, null, null, null, '10086', null, null, '3.00', '3.00', '3.00', null, null, null, null, '010101', '规格1--参数1,规格2--参数1,规格3--参数1', '高蛋白', null);
INSERT INTO `product` VALUES ('10094', '雅培金装喜康力较大婴儿配方奶粉', null, '900g', '无', '6932904708789', null, '10090', null, '10094', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/6932904708789/6932904708789-0.jpg', null, null, null, '010101', null, '', '720');
INSERT INTO `product` VALUES ('10095', '惠氏S-26金装学儿乐', null, '900g', '无', '6949756340111', null, '10091', null, '10095', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/6949756340111/6949756340111-0.jpg', null, null, null, '010101', null, '', '720');
INSERT INTO `product` VALUES ('10096', '惠氏S-26金装爱儿加', null, '400g', '无', '300087180016', null, '10091', null, '10098', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/300087180016/300087180016-0.jpg|http://211.151.134.74:8080/portal/img/product/300087180016/300087180016-1.jpg', null, null, null, '010101', null, '', '720');
INSERT INTO `product` VALUES ('10097', '合生元呵护婴儿配方奶粉', null, '900g', '无', '3760179010084', null, '10092', null, '10099', '5.00', '5.00', '5.00', 'http://211.151.134.74:8080/portal/img/product/3760179010084/3760179010084-0.jpg', null, null, null, '010101', null, '', '1095');
INSERT INTO `product` VALUES ('10098', '合生元金装较大婴儿配方奶粉', null, '900G', '无', '3760170400013', null, '10092', null, '10099', '5.00', '5.00', '5.00', null, null, null, null, '010101', null, '', '1095');

-- ----------------------------
-- Table structure for `product_auth`
-- ----------------------------
DROP TABLE IF EXISTS `product_auth`;
CREATE TABLE `product_auth` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `auth_id` int(11) DEFAULT NULL COMMENT '认证的ID，关联到auth表',
  `auth_imgurl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `auth_message` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='产品认证信息表';

-- ----------------------------
-- Records of product_auth
-- ----------------------------

-- ----------------------------
-- Table structure for `product_category`
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `display` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imgUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=401 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of product_category
-- ----------------------------
INSERT INTO `product_category` VALUES ('7', '01', '乳及乳制品', '乳及乳制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav01.png');
INSERT INTO `product_category` VALUES ('8', '0101', '巴氏杀菌乳、灭菌乳和调制乳', '巴氏杀菌乳类', null);
INSERT INTO `product_category` VALUES ('9', '010101', '巴氏杀菌乳', '巴氏杀菌乳', null);
INSERT INTO `product_category` VALUES ('10', '010102', '灭菌乳', '灭菌乳', null);
INSERT INTO `product_category` VALUES ('11', '010103', '调制乳', '调制乳', null);
INSERT INTO `product_category` VALUES ('12', '0102', '发酵乳和风味发酵乳', '风味发酵乳类', null);
INSERT INTO `product_category` VALUES ('13', '010201', '发酵乳', '发酵乳', null);
INSERT INTO `product_category` VALUES ('14', '010202', '风味发酵乳', '风味发酵乳', null);
INSERT INTO `product_category` VALUES ('15', '0103', '乳粉和奶油粉及其调制产品', '乳粉和奶油粉类', null);
INSERT INTO `product_category` VALUES ('16', '010301', '乳粉和奶油粉', '乳粉和奶油粉', null);
INSERT INTO `product_category` VALUES ('17', '010302', '调制乳粉和调制奶油粉', '调制乳粉和调制奶油粉', null);
INSERT INTO `product_category` VALUES ('18', '0104', '炼乳及其调制产品', '炼乳类', null);
INSERT INTO `product_category` VALUES ('19', '010401', '淡炼乳', '淡炼乳', null);
INSERT INTO `product_category` VALUES ('20', '010402', '调制炼乳', '调制炼乳', null);
INSERT INTO `product_category` VALUES ('21', '0105', '稀奶油及其类似品', '稀奶油类', null);
INSERT INTO `product_category` VALUES ('22', '010501', '稀奶油', '稀奶油', null);
INSERT INTO `product_category` VALUES ('23', '010502', '凝固稀奶油', '凝固稀奶油', null);
INSERT INTO `product_category` VALUES ('24', '010503', '调味稀奶油', '调味稀奶油', null);
INSERT INTO `product_category` VALUES ('25', '010504', '稀奶油类似品', '稀奶油类似品', null);
INSERT INTO `product_category` VALUES ('26', '0106', '干酪', '干酪', null);
INSERT INTO `product_category` VALUES ('27', '010601', '非熟化干酪', '非熟化干酪', null);
INSERT INTO `product_category` VALUES ('28', '010602', '熟化干酪', '熟化干酪', null);
INSERT INTO `product_category` VALUES ('29', '010603', '乳清干酪', '乳清干酪', null);
INSERT INTO `product_category` VALUES ('30', '010604', '再制干酪', '再制干酪', null);
INSERT INTO `product_category` VALUES ('33', '010605', '干酪类似品', '类干酪', null);
INSERT INTO `product_category` VALUES ('34', '010606', '乳清蛋白干酪', '乳清蛋白干酪', null);
INSERT INTO `product_category` VALUES ('35', '0107', '以乳为主要配料的即食风味甜点或其预制产品', '即食风味甜点', null);
INSERT INTO `product_category` VALUES ('36', '0108', '其他乳制品', '其他乳制品', null);
INSERT INTO `product_category` VALUES ('37', '02', '脂肪，油和乳化脂肪制品', '脂肪及油类', 'http://211.151.134.74:8080/portal/img/leftNav/nav02.png');
INSERT INTO `product_category` VALUES ('38', '0201', '基本不含水的脂肪和油', '基本无水脂肪和油', null);
INSERT INTO `product_category` VALUES ('39', '020101', '植物油脂', '植物油脂', null);
INSERT INTO `product_category` VALUES ('42', '020102', '动物油脂', '动物油脂', null);
INSERT INTO `product_category` VALUES ('43', '020103', '无水黄油，无水乳脂', '无水黄油，无水乳脂', null);
INSERT INTO `product_category` VALUES ('44', '0202', '水油状脂肪乳化制品', '水油状脂肪乳化制品', null);
INSERT INTO `product_category` VALUES ('45', '020201', '脂肪含量80%以上的乳化制品', '脂肪含量80%以上的乳化制品', null);
INSERT INTO `product_category` VALUES ('48', '020202', '脂肪含量80%以下的乳化制品', '脂肪含量80%以下的乳化制品', null);
INSERT INTO `product_category` VALUES ('49', '0203', '其他脂肪乳化制品', '其他脂肪乳化制品', null);
INSERT INTO `product_category` VALUES ('50', '0204', '脂肪类甜品', '脂肪类甜品', null);
INSERT INTO `product_category` VALUES ('51', '0205', '其他油脂或油脂制品', '其他油脂或油脂制品', null);
INSERT INTO `product_category` VALUES ('52', '03', '冷冻饮品', '冷冻饮品', 'http://211.151.134.74:8080/portal/img/leftNav/nav03.png');
INSERT INTO `product_category` VALUES ('53', '0301', '冰淇淋类、雪糕类', '冰淇淋类、雪糕类', null);
INSERT INTO `product_category` VALUES ('55', '0303', '风味冰、冰棍类', '风味冰、冰棍类', null);
INSERT INTO `product_category` VALUES ('56', '0304', '食用冰', '食用冰', null);
INSERT INTO `product_category` VALUES ('57', '0305', '其他冷冻饮品', '其他冷冻饮品', null);
INSERT INTO `product_category` VALUES ('58', '04', '水果、蔬菜、豆类、食用菌、藻类、坚果以及籽类等', '果蔬豆菌藻坚果类', 'http://211.151.134.74:8080/portal/img/leftNav/nav04.png');
INSERT INTO `product_category` VALUES ('59', '0401', '水果', '水果', null);
INSERT INTO `product_category` VALUES ('60', '040101', '新鲜水果', '新鲜水果', null);
INSERT INTO `product_category` VALUES ('64', '040102', '加工水果', '加工水果', null);
INSERT INTO `product_category` VALUES ('84', '0402', '蔬菜', '蔬菜', null);
INSERT INTO `product_category` VALUES ('85', '040201', '新鲜蔬菜', '新鲜蔬菜', null);
INSERT INTO `product_category` VALUES ('90', '040202', '加工蔬菜', '加工蔬菜', null);
INSERT INTO `product_category` VALUES ('91', '040203', '根茎类', '根茎类', null);
INSERT INTO `product_category` VALUES ('92', '040204', '叶菜类', '叶菜类', null);
INSERT INTO `product_category` VALUES ('101', '0403', '食用菌和藻类', '食用菌类', null);
INSERT INTO `product_category` VALUES ('102', '040301', '新鲜食用菌和藻类', '新鲜食用菌和藻类', null);
INSERT INTO `product_category` VALUES ('106', '040302', '加工食用菌和藻类', '加工食用菌和藻类', null);
INSERT INTO `product_category` VALUES ('113', '0404', '豆类制品', '豆类制品', null);
INSERT INTO `product_category` VALUES ('114', '040401', '非发酵豆制品', '非发酵豆制品', null);
INSERT INTO `product_category` VALUES ('125', '040402', '发酵豆制品', '发酵豆制品', null);
INSERT INTO `product_category` VALUES ('128', '040403', '其他豆制品', '其他豆制品', null);
INSERT INTO `product_category` VALUES ('129', '0405', '坚果和籽类', '坚果和籽类', null);
INSERT INTO `product_category` VALUES ('130', '040501', '新鲜坚果与籽类', '新鲜坚果与籽类', null);
INSERT INTO `product_category` VALUES ('131', '040502', '加工坚果与籽类', '加工坚果与籽类', null);
INSERT INTO `product_category` VALUES ('139', '05', '可可制品、巧克力和巧克力制品以及糖果', '巧克力和糖果类', 'http://211.151.134.74:8080/portal/img/leftNav/nav05.png');
INSERT INTO `product_category` VALUES ('140', '0501', '巧克力', '巧克力', null);
INSERT INTO `product_category` VALUES ('141', '050101', '可可制品', '可可制品', null);
INSERT INTO `product_category` VALUES ('142', '050102', '巧克力和巧克力制品、其他可可制品', '巧克力类制品', null);
INSERT INTO `product_category` VALUES ('143', '050103', '代可可脂巧克力及使用可可脂代用品的巧克力类似产品', '代可可脂巧克力类', null);
INSERT INTO `product_category` VALUES ('144', '0502', '糖果', '糖果', null);
INSERT INTO `product_category` VALUES ('145', '050201', '胶基糖果', '口香糖类', null);
INSERT INTO `product_category` VALUES ('146', '050202', '除胶基糖果以外的其他糖果', '硬质夹心糖类', null);
INSERT INTO `product_category` VALUES ('147', '0503', '糖果和巧克力制品包衣', '糖果和巧克力制品包衣', null);
INSERT INTO `product_category` VALUES ('148', '0504', '装饰糖果、顶饰', '装饰糖果类', null);
INSERT INTO `product_category` VALUES ('149', '06', '粮食和粮食制品', '粮食和粮食制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav06.png');
INSERT INTO `product_category` VALUES ('150', '0601', '原粮', '原粮', null);
INSERT INTO `product_category` VALUES ('151', '0602', '大米及其制品', '大米及其制品', null);
INSERT INTO `product_category` VALUES ('152', '060201', '大米', '大米', null);
INSERT INTO `product_category` VALUES ('153', '060202', '大米制品', '大米制品', null);
INSERT INTO `product_category` VALUES ('154', '060203', '米粉', '米粉', null);
INSERT INTO `product_category` VALUES ('155', '060204', '米粉制品', '米粉制品', null);
INSERT INTO `product_category` VALUES ('156', '0603', '小麦粉及其制品', '小麦粉及其制品', null);
INSERT INTO `product_category` VALUES ('157', '060301', '小麦粉', '小麦粉', null);
INSERT INTO `product_category` VALUES ('160', '060302', '小麦粉制品', '小麦粉制品', null);
INSERT INTO `product_category` VALUES ('166', '0604', '杂粮粉及其制品', '杂粮粉及其制品', null);
INSERT INTO `product_category` VALUES ('167', '060401', '杂粮粉', '杂粮粉', null);
INSERT INTO `product_category` VALUES ('168', '060402', '杂粮制品', '杂粮制品', null);
INSERT INTO `product_category` VALUES ('171', '0605', '淀粉及淀粉类制品', '淀粉及淀粉类制品', null);
INSERT INTO `product_category` VALUES ('172', '060501', '食用淀粉', '食用淀粉', null);
INSERT INTO `product_category` VALUES ('173', '060502', '淀粉制品', '淀粉制品', null);
INSERT INTO `product_category` VALUES ('178', '0606', '即食谷物', '即食谷物', null);
INSERT INTO `product_category` VALUES ('179', '0607', '方便米面制品', '方便米面制品', null);
INSERT INTO `product_category` VALUES ('180', '0608', '冷冻米面制品', '冷冻米面制品', null);
INSERT INTO `product_category` VALUES ('181', '0609', '谷类和淀粉类甜品', '谷类和淀粉类甜品', null);
INSERT INTO `product_category` VALUES ('182', '0610', '粮食制品馅料', '粮食制品馅料', null);
INSERT INTO `product_category` VALUES ('183', '07', '焙烤食品', '焙烤食品', 'http://211.151.134.74:8080/portal/img/leftNav/nav07.png');
INSERT INTO `product_category` VALUES ('184', '0701', '面包', '面包', null);
INSERT INTO `product_category` VALUES ('185', '0702', '糕点', '糕点', null);
INSERT INTO `product_category` VALUES ('186', '070201', '中式糕点', '中式糕点', null);
INSERT INTO `product_category` VALUES ('187', '070202', '西式糕点', '西式糕点', null);
INSERT INTO `product_category` VALUES ('188', '070203', '月饼', '月饼', null);
INSERT INTO `product_category` VALUES ('189', '070204', '糕点上彩装', '糕点彩装', null);
INSERT INTO `product_category` VALUES ('190', '0703', '饼干', '饼干', null);
INSERT INTO `product_category` VALUES ('191', '070301', '夹心及装饰类饼干', '夹心及装饰类饼干', null);
INSERT INTO `product_category` VALUES ('192', '070302', '威化饼干', '威化饼干', null);
INSERT INTO `product_category` VALUES ('193', '070303', '蛋卷', '蛋卷', null);
INSERT INTO `product_category` VALUES ('194', '070304', '其他饼干', '其他饼干', null);
INSERT INTO `product_category` VALUES ('195', '0704', '焙烤食品馅料及表面用挂浆', '焙烤食品馅料及表面用挂浆', null);
INSERT INTO `product_category` VALUES ('196', '0705', '其他焙烤食品', '其他焙烤食品', null);
INSERT INTO `product_category` VALUES ('197', '08', '肉及肉制品', '肉及肉制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav08.png');
INSERT INTO `product_category` VALUES ('198', '0801', '生、鲜肉', '生、鲜肉', null);
INSERT INTO `product_category` VALUES ('199', '080101', '生鲜肉', '生鲜肉', null);
INSERT INTO `product_category` VALUES ('200', '080102', '冷却肉', '冷却肉', null);
INSERT INTO `product_category` VALUES ('201', '080103', '冻肉', '冻肉', null);
INSERT INTO `product_category` VALUES ('202', '0802', '预制肉制品', '预制肉制品', null);
INSERT INTO `product_category` VALUES ('203', '080201', '调理肉制品', '调理肉制品', null);
INSERT INTO `product_category` VALUES ('204', '080202', '腌腊肉制品类', '腌腊肉制品类', null);
INSERT INTO `product_category` VALUES ('205', '0803', '熟肉制品', '熟肉制品', null);
INSERT INTO `product_category` VALUES ('206', '080301', '酱卤肉制品类', '酱卤肉制品类', null);
INSERT INTO `product_category` VALUES ('210', '080302', '熏、烧、烤肉类', '熏、烧、烤肉类', null);
INSERT INTO `product_category` VALUES ('211', '080303', '油炸肉类', '油炸肉类', null);
INSERT INTO `product_category` VALUES ('212', '080304', '西式火腿类', '西式火腿类', null);
INSERT INTO `product_category` VALUES ('213', '080305', '肉灌肠类', '肉灌肠类', null);
INSERT INTO `product_category` VALUES ('214', '080306', '发酵肉制品类', '发酵肉制品类', null);
INSERT INTO `product_category` VALUES ('215', '080307', '熟肉干制品', '熟肉干制品', null);
INSERT INTO `product_category` VALUES ('219', '080308', '肉罐头类', '肉罐头类', null);
INSERT INTO `product_category` VALUES ('220', '080309', '可食用动物肠衣类', '可食用动物肠衣类', null);
INSERT INTO `product_category` VALUES ('221', '080310', '其他肉及肉制品', '其他肉及肉制品', null);
INSERT INTO `product_category` VALUES ('222', '09', '水产及其制品', '水产及其制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav09.png');
INSERT INTO `product_category` VALUES ('223', '0901', '鲜水产', '鲜水产', null);
INSERT INTO `product_category` VALUES ('224', '090101', '鲐鱼', '鲐鱼', null);
INSERT INTO `product_category` VALUES ('225', '090102', '其他', '其他', null);
INSERT INTO `product_category` VALUES ('226', '0902', '冷冻水产品及其制品', '冷冻水产品及其制品', null);
INSERT INTO `product_category` VALUES ('227', '090201', '冷冻制品', '冷冻制品', null);
INSERT INTO `product_category` VALUES ('228', '090202', '冷冻挂桨制品', '冷冻挂桨制品', null);
INSERT INTO `product_category` VALUES ('229', '090203', '冷冻鱼糜制品', '冷冻鱼糜制品', null);
INSERT INTO `product_category` VALUES ('230', '0903', '预制水产品', '预制水产品', null);
INSERT INTO `product_category` VALUES ('231', '090301', '醋渍或肉冻状水产品', '醋渍水产品', null);
INSERT INTO `product_category` VALUES ('232', '090302', '腌制水产品', '腌制水产品', null);
INSERT INTO `product_category` VALUES ('233', '090303', '鱼子制品', '鱼子制品', null);
INSERT INTO `product_category` VALUES ('234', '090304', '风干、烘干、压干等水产品', '干制水产品', null);
INSERT INTO `product_category` VALUES ('235', '090305', '水发水产品(鱿鱼、海参等)', '水发水产品', null);
INSERT INTO `product_category` VALUES ('236', '090306', '其他预制水产品', '其他预制水产品', null);
INSERT INTO `product_category` VALUES ('237', '0904', '熟制水产品', '熟制水产品', null);
INSERT INTO `product_category` VALUES ('238', '090401', '熟干水产品', '熟干水产品', null);
INSERT INTO `product_category` VALUES ('239', '090402', '经烹调或油炸的水产品', '烹制水产品', null);
INSERT INTO `product_category` VALUES ('240', '090403', '熏、烤水产品', '熏、烤水产品', null);
INSERT INTO `product_category` VALUES ('241', '090404', '发酵水产品', '发酵水产品', null);
INSERT INTO `product_category` VALUES ('242', '0905', '水产品罐头', '水产品罐头', null);
INSERT INTO `product_category` VALUES ('243', '0906', '其他水产品及其制品', '其他水产品及其制品', null);
INSERT INTO `product_category` VALUES ('244', '10', '蛋及蛋制品', '蛋及蛋制品', 'http://211.151.134.74:8080/portal/img/leftNav/nav10.png');
INSERT INTO `product_category` VALUES ('245', '1001', '鲜蛋', '鲜蛋', null);
INSERT INTO `product_category` VALUES ('246', '1002', '再制蛋', '再制蛋', null);
INSERT INTO `product_category` VALUES ('247', '100201', '卤蛋', '卤蛋', null);
INSERT INTO `product_category` VALUES ('248', '100202', '糟蛋', '糟蛋', null);
INSERT INTO `product_category` VALUES ('249', '100203', '皮蛋', '皮蛋', null);
INSERT INTO `product_category` VALUES ('250', '100204', '咸蛋', '咸蛋', null);
INSERT INTO `product_category` VALUES ('251', '100205', '其他再制蛋', '其他再制蛋', null);
INSERT INTO `product_category` VALUES ('252', '1003', '蛋制品', '蛋制品', null);
INSERT INTO `product_category` VALUES ('253', '100301', '脱水蛋制品', '脱水蛋制品', null);
INSERT INTO `product_category` VALUES ('254', '100302', '热凝固蛋制品', '热凝固蛋制品', null);
INSERT INTO `product_category` VALUES ('255', '100303', '冷冻蛋制品', '冷冻蛋制品', null);
INSERT INTO `product_category` VALUES ('256', '100304', '液体蛋', '液体蛋', null);
INSERT INTO `product_category` VALUES ('257', '1004', '其他蛋制品', '其他蛋制品', null);
INSERT INTO `product_category` VALUES ('258', '11', '甜味料，包括蜂蜜', '甜味料', 'http://211.151.134.74:8080/portal/img/leftNav/nav11.png');
INSERT INTO `product_category` VALUES ('259', '1101', '食糖', '食糖', null);
INSERT INTO `product_category` VALUES ('260', '110101', '白糖及白糖制品', '白糖及白糖制品', null);
INSERT INTO `product_category` VALUES ('261', '110102', '其他糖和糖浆', '其他糖和糖浆', null);
INSERT INTO `product_category` VALUES ('262', '1102', '淀粉糖', '淀粉糖', null);
INSERT INTO `product_category` VALUES ('263', '1103', '蜂蜜及花粉', '蜂蜜及花粉', null);
INSERT INTO `product_category` VALUES ('264', '110301', '蜂蜜', '蜂蜜', null);
INSERT INTO `product_category` VALUES ('275', '110302', '花粉', '花粉', null);
INSERT INTO `product_category` VALUES ('276', '1104', '餐桌甜味料', '餐桌甜味料', null);
INSERT INTO `product_category` VALUES ('277', '1105', '调味糖浆', '调味糖浆', null);
INSERT INTO `product_category` VALUES ('278', '110501', '水果调味糖浆', '水果调味糖浆', null);
INSERT INTO `product_category` VALUES ('279', '110502', '其他调味糖浆', '其他调味糖浆', null);
INSERT INTO `product_category` VALUES ('280', '1106', '其他甜味料', '其他甜味料', null);
INSERT INTO `product_category` VALUES ('281', '12', '调味品', '调味品', 'http://211.151.134.74:8080/portal/img/leftNav/nav12.png');
INSERT INTO `product_category` VALUES ('282', '1201', '盐及代盐制品', '盐及代盐制品', null);
INSERT INTO `product_category` VALUES ('283', '1202', '鲜味剂和助鲜剂', '鲜味剂和助鲜剂', null);
INSERT INTO `product_category` VALUES ('284', '1203', '醋', '醋', null);
INSERT INTO `product_category` VALUES ('285', '120301', '酿造食醋', '酿造食醋', null);
INSERT INTO `product_category` VALUES ('286', '120302', '配制食醋', '配制食醋', null);
INSERT INTO `product_category` VALUES ('287', '1204', '酱油', '酱油', null);
INSERT INTO `product_category` VALUES ('288', '120401', '酿造酱油', '酿造酱油', null);
INSERT INTO `product_category` VALUES ('289', '120402', '配制酱油', '配制酱油', null);
INSERT INTO `product_category` VALUES ('290', '1205', '酱及酱制品', '酱及酱制品', null);
INSERT INTO `product_category` VALUES ('291', '120501', '酿造酱', '酿造酱', null);
INSERT INTO `product_category` VALUES ('292', '120502', '配制酱', '配制酱', null);
INSERT INTO `product_category` VALUES ('294', '1207', '料酒及制品', '料酒及制品', null);
INSERT INTO `product_category` VALUES ('296', '1209', '香辛料类', '香辛料类', null);
INSERT INTO `product_category` VALUES ('297', '120901', '香辛料及粉', '香辛料及粉', null);
INSERT INTO `product_category` VALUES ('298', '120902', '香辛料油', '香辛料油', null);
INSERT INTO `product_category` VALUES ('299', '120903', '香辛料酱', '香辛料酱', null);
INSERT INTO `product_category` VALUES ('300', '120904', '其他香辛料加工品', '其他香辛料加工品', null);
INSERT INTO `product_category` VALUES ('301', '1210', '复合调味料', '复合调味料', null);
INSERT INTO `product_category` VALUES ('302', '121001', '固体复合调味料', '固体复合调味料', null);
INSERT INTO `product_category` VALUES ('306', '121002', '半固体复合调味料', '半固体复合调味料', null);
INSERT INTO `product_category` VALUES ('311', '121003', '液体复合调味料', '液体复合调味料', null);
INSERT INTO `product_category` VALUES ('316', '1211', '其他调味料', '其他调味料', null);
INSERT INTO `product_category` VALUES ('317', '13', '特殊膳食用食品', '特殊膳食用食品', 'http://211.151.134.74:8080/portal/img/leftNav/nav13.png');
INSERT INTO `product_category` VALUES ('318', '1301', '婴幼儿配方食品', '婴幼儿配方食品', null);
INSERT INTO `product_category` VALUES ('319', '130101', '婴儿配方食品', '婴儿配方食品', null);
INSERT INTO `product_category` VALUES ('320', '130102', '较大婴儿和幼儿配方食品', '婴幼儿配方食品', null);
INSERT INTO `product_category` VALUES ('321', '130103', '特殊医学用途婴儿配方食品', '特殊医学用途类', null);
INSERT INTO `product_category` VALUES ('322', '1302', '婴幼儿辅助食品', '婴幼儿辅助食品', null);
INSERT INTO `product_category` VALUES ('323', '130201', '婴幼儿谷类辅助食品', '婴幼儿谷类辅助食品', null);
INSERT INTO `product_category` VALUES ('324', '130202', '婴幼儿罐装辅助食品', '婴幼儿罐装辅助食品', null);
INSERT INTO `product_category` VALUES ('325', '1303', '特殊医学用途配方食品', '特殊医学用途配方食品', null);
INSERT INTO `product_category` VALUES ('326', '1304', '低能量配方食品', '低能量配方食品', null);
INSERT INTO `product_category` VALUES ('327', '1305', '其他特殊膳食用食品', '其他食品', null);
INSERT INTO `product_category` VALUES ('328', '14', '饮料类', '饮料类', 'http://211.151.134.74:8080/portal/img/leftNav/nav14.png');
INSERT INTO `product_category` VALUES ('329', '1401', '包装饮用水类', '包装饮用水类', null);
INSERT INTO `product_category` VALUES ('330', '140101', '饮用天然矿泉水', '饮用天然矿泉水', null);
INSERT INTO `product_category` VALUES ('331', '140102', '饮用纯净水', '饮用纯净水', null);
INSERT INTO `product_category` VALUES ('332', '140103', '其他饮用水', '其他饮用水', null);
INSERT INTO `product_category` VALUES ('333', '1402', '果蔬汁类', '果蔬汁类', null);
INSERT INTO `product_category` VALUES ('334', '140201', '果蔬汁（浆）', '果蔬汁', null);
INSERT INTO `product_category` VALUES ('335', '140202', '浓缩果蔬汁（浆）', '浓缩果蔬汁', null);
INSERT INTO `product_category` VALUES ('336', '140203', '果蔬汁（肉）', '果蔬汁', null);
INSERT INTO `product_category` VALUES ('337', '1403', '蛋白饮料类', '蛋白饮料类', null);
INSERT INTO `product_category` VALUES ('338', '140301', '含乳饮料', '含乳饮料', null);
INSERT INTO `product_category` VALUES ('342', '140302', '植物蛋白饮料', '植物蛋白饮料', null);
INSERT INTO `product_category` VALUES ('343', '140303', '复合蛋白饮料', '复合蛋白饮料', null);
INSERT INTO `product_category` VALUES ('344', '1404', '水基调味饮料类', '水基调味饮料类', null);
INSERT INTO `product_category` VALUES ('345', '140401', '碳酸饮料', '碳酸饮料', null);
INSERT INTO `product_category` VALUES ('348', '140402', '非碳酸饮料', '非碳酸饮料', null);
INSERT INTO `product_category` VALUES ('351', '1405', '茶、咖啡、植物饮料类', '茶、咖啡、植物饮料类', null);
INSERT INTO `product_category` VALUES ('352', '140501', '茶饮料类', '茶饮料类', null);
INSERT INTO `product_category` VALUES ('353', '140502', '咖啡饮料类', '咖啡饮料类', null);
INSERT INTO `product_category` VALUES ('354', '140503', '植物饮料类', '植物饮料类', null);
INSERT INTO `product_category` VALUES ('355', '1406', '固体饮料类', '固体饮料类', null);
INSERT INTO `product_category` VALUES ('356', '140601', '果香型固体饮料', '果香型固体饮料', null);
INSERT INTO `product_category` VALUES ('357', '140602', '蛋白型固体饮料', '蛋白型固体饮料', null);
INSERT INTO `product_category` VALUES ('358', '140603', '速溶咖啡', '速溶咖啡', null);
INSERT INTO `product_category` VALUES ('359', '140604', '其他固体饮料', '其他固体饮料', null);
INSERT INTO `product_category` VALUES ('360', '1407', '生活饮用水', '生活饮用水', null);
INSERT INTO `product_category` VALUES ('361', '1408', '其他饮料类', '其他饮料类', null);
INSERT INTO `product_category` VALUES ('362', '15', '酒类', '酒类', 'http://211.151.134.74:8080/portal/img/leftNav/nav15.png');
INSERT INTO `product_category` VALUES ('363', '1501', '蒸馏酒', '蒸馏酒', null);
INSERT INTO `product_category` VALUES ('364', '150101', '白酒', '白酒', null);
INSERT INTO `product_category` VALUES ('365', '150102', '调香蒸馏酒', '调香蒸馏酒', null);
INSERT INTO `product_category` VALUES ('366', '150103', '白兰地', '白兰地', null);
INSERT INTO `product_category` VALUES ('367', '150104', '威士忌', '威士忌', null);
INSERT INTO `product_category` VALUES ('368', '150105', '伏特加', '伏特加', null);
INSERT INTO `product_category` VALUES ('369', '150106', '朗姆酒', '朗姆酒', null);
INSERT INTO `product_category` VALUES ('370', '150107', '其他蒸馏酒', '其他蒸馏酒', null);
INSERT INTO `product_category` VALUES ('371', '1502', '配制酒', '配制酒', null);
INSERT INTO `product_category` VALUES ('372', '1503', '发酵酒', '发酵酒', null);
INSERT INTO `product_category` VALUES ('373', '150301', '葡萄酒', '葡萄酒', null);
INSERT INTO `product_category` VALUES ('378', '150302', '黄酒', '黄酒', null);
INSERT INTO `product_category` VALUES ('379', '150303', '果酒', '果酒', null);
INSERT INTO `product_category` VALUES ('380', '150304', '蜂蜜酒', '蜂蜜酒', null);
INSERT INTO `product_category` VALUES ('381', '150305', '啤酒和麦芽饮料', '啤酒和麦芽饮料', null);
INSERT INTO `product_category` VALUES ('382', '150306', '其他发酵酒类', '其他发酵酒类', null);
INSERT INTO `product_category` VALUES ('383', '16', '其他类', '其他类', 'http://211.151.134.74:8080/portal/img/leftNav/nav16.png');
INSERT INTO `product_category` VALUES ('384', '1601', '果冻', '果冻', null);
INSERT INTO `product_category` VALUES ('385', '1602', '茶叶、咖啡', '茶叶、咖啡', null);
INSERT INTO `product_category` VALUES ('386', '1603', '胶原蛋白肠衣', '胶原蛋白肠衣', null);
INSERT INTO `product_category` VALUES ('387', '1604', '酵母及酵母类制品', '酵母及其制品', null);
INSERT INTO `product_category` VALUES ('388', '160401', '干酵母', '干酵母', null);
INSERT INTO `product_category` VALUES ('389', '160402', '其他酵母及酵母类制品', '其他酵母及其制品', null);
INSERT INTO `product_category` VALUES ('391', '1606', '膨化食品', '膨化食品', null);
INSERT INTO `product_category` VALUES ('392', '1607', '小吃', '小吃', null);
INSERT INTO `product_category` VALUES ('393', '160701', '味精', '味精', null);
INSERT INTO `product_category` VALUES ('397', '160702', '自来水', '自来水', null);
INSERT INTO `product_category` VALUES ('398', '160703', '其他', '其他', null);
INSERT INTO `product_category` VALUES ('399', '1608', '油炸食品', '油炸食品', null);
INSERT INTO `product_category` VALUES ('400', '1609', '其他', '其他', null);

-- ----------------------------
-- Table structure for `product_certification`
-- ----------------------------
DROP TABLE IF EXISTS `product_certification`;
CREATE TABLE `product_certification` (
  `Id` bigint(11) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品ID',
  `cert_id` bigint(20) DEFAULT NULL COMMENT '认证的ID，关联到certification',
  `document_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_product_cert_certification` (`cert_id`),
  CONSTRAINT `fk_product_cert_cert` FOREIGN KEY (`cert_id`) REFERENCES `certification` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='产品认证信息表';

-- ----------------------------
-- Records of product_certification
-- ----------------------------
INSERT INTO `product_certification` VALUES ('1', '163', '1', null);
INSERT INTO `product_certification` VALUES ('2', '163', '6', null);
INSERT INTO `product_certification` VALUES ('3', '163', '13', null);
INSERT INTO `product_certification` VALUES ('4', '164', '1', null);
INSERT INTO `product_certification` VALUES ('5', '164', '2', null);
INSERT INTO `product_certification` VALUES ('6', '164', '3', null);
INSERT INTO `product_certification` VALUES ('7', '165', '4', null);
INSERT INTO `product_certification` VALUES ('8', '165', '5', null);
INSERT INTO `product_certification` VALUES ('9', '165', '6', null);
INSERT INTO `product_certification` VALUES ('10', '166', '7', null);
INSERT INTO `product_certification` VALUES ('11', '166', '8', null);
INSERT INTO `product_certification` VALUES ('12', '167', '9', null);
INSERT INTO `product_certification` VALUES ('13', '167', '10', null);
INSERT INTO `product_certification` VALUES ('14', '168', '11', null);
INSERT INTO `product_certification` VALUES ('15', '168', '12', null);
INSERT INTO `product_certification` VALUES ('16', '168', '13', null);
INSERT INTO `product_certification` VALUES ('17', '168', '14', null);
INSERT INTO `product_certification` VALUES ('18', '169', '15', null);
INSERT INTO `product_certification` VALUES ('19', '169', '16', null);
INSERT INTO `product_certification` VALUES ('20', '170', '17', null);
INSERT INTO `product_certification` VALUES ('21', '170', '19', null);
INSERT INTO `product_certification` VALUES ('22', '170', '20', null);
INSERT INTO `product_certification` VALUES ('23', '214', '24', null);
INSERT INTO `product_certification` VALUES ('24', '214', '25', null);
INSERT INTO `product_certification` VALUES ('25', '214', '26', null);
INSERT INTO `product_certification` VALUES ('26', '215', '24', null);
INSERT INTO `product_certification` VALUES ('27', '215', '25', null);
INSERT INTO `product_certification` VALUES ('28', '215', '26', null);
INSERT INTO `product_certification` VALUES ('29', '163', '25', null);
INSERT INTO `product_certification` VALUES ('30', '163', '27', null);
INSERT INTO `product_certification` VALUES ('31', '163', '28', null);
INSERT INTO `product_certification` VALUES ('32', '163', '29', null);
INSERT INTO `product_certification` VALUES ('33', '163', '30', null);
INSERT INTO `product_certification` VALUES ('34', '164', '25', null);
INSERT INTO `product_certification` VALUES ('36', '164', '28', null);
INSERT INTO `product_certification` VALUES ('39', '165', '25', null);
INSERT INTO `product_certification` VALUES ('40', '166', '27', null);
INSERT INTO `product_certification` VALUES ('41', '166', '28', null);
INSERT INTO `product_certification` VALUES ('42', '166', '29', null);
INSERT INTO `product_certification` VALUES ('43', '166', '30', null);
INSERT INTO `product_certification` VALUES ('44', '167', '25', null);
INSERT INTO `product_certification` VALUES ('45', '167', '27', null);
INSERT INTO `product_certification` VALUES ('46', '167', '28', null);
INSERT INTO `product_certification` VALUES ('47', '167', '29', null);
INSERT INTO `product_certification` VALUES ('48', '167', '30', null);
INSERT INTO `product_certification` VALUES ('49', '168', '25', null);
INSERT INTO `product_certification` VALUES ('50', '168', '27', null);
INSERT INTO `product_certification` VALUES ('51', '168', '28', null);
INSERT INTO `product_certification` VALUES ('52', '168', '29', null);
INSERT INTO `product_certification` VALUES ('53', '168', '30', null);
INSERT INTO `product_certification` VALUES ('54', '169', '25', null);
INSERT INTO `product_certification` VALUES ('55', '169', '27', null);
INSERT INTO `product_certification` VALUES ('56', '169', '28', null);
INSERT INTO `product_certification` VALUES ('57', '169', '29', null);
INSERT INTO `product_certification` VALUES ('58', '169', '30', null);
INSERT INTO `product_certification` VALUES ('59', '170', '25', null);
INSERT INTO `product_certification` VALUES ('60', '170', '27', null);
INSERT INTO `product_certification` VALUES ('61', '170', '28', null);
INSERT INTO `product_certification` VALUES ('62', '170', '29', null);
INSERT INTO `product_certification` VALUES ('63', '170', '30', null);
INSERT INTO `product_certification` VALUES ('64', '240', '13', 'http://211.151.134.74:8080/portal/img/certification/document/weichunyuan/texianjiangyouQS.jpg');
INSERT INTO `product_certification` VALUES ('65', '240', '28', 'http://211.151.134.74:8080/portal/img/certification/document/weichunyuan/ISO90012008.jpg');
INSERT INTO `product_certification` VALUES ('66', '240', '31', 'http://211.151.134.74:8080/portal/img/certification/document/weichunyuan/ISO10012.jpg');
INSERT INTO `product_certification` VALUES ('67', '239', '13', 'http://211.151.134.74:8080/portal/img/certification/document/mengniu/telunsuyoujinaiQS.jpg');
INSERT INTO `product_certification` VALUES ('68', '239', '25', 'http://211.151.134.74:8080/portal/img/certification/document/mengniu/ISO14001.jpg');
INSERT INTO `product_certification` VALUES ('69', '239', '28', 'http://211.151.134.74:8080/portal/img/certification/document/mengniu/ISO90012008.jpg');
INSERT INTO `product_certification` VALUES ('70', '239', '32', 'http://211.151.134.74:8080/portal/img/certification/document/mengniu/ISO18001.jpg');
INSERT INTO `product_certification` VALUES ('71', '197', '13', 'http://211.151.134.74:8080/portal/img/certification/document/laogandie/tiaoweiliaoQS.jpg');
INSERT INTO `product_certification` VALUES ('72', '197', '28', 'http://211.151.134.74:8080/portal/img/certification/document/laogandie/ISO90012008.jpg');
INSERT INTO `product_certification` VALUES ('73', '197', '31', 'http://211.151.134.74:8080/portal/img/certification/document/laogandie/ISO10012.jpg');
INSERT INTO `product_certification` VALUES ('74', '197', '8', 'http://211.151.134.74:8080/portal/img/certification/document/laogandie/HACCP.jpg');
INSERT INTO `product_certification` VALUES ('75', '203', '13', 'http://211.151.134.74:8080/portal/img/certification/document/laogandie/tiaoweiliaoQS.jpg');
INSERT INTO `product_certification` VALUES ('76', '203', '28', 'http://211.151.134.74:8080/portal/img/certification/document/laogandie/ISO90012008.jpg');
INSERT INTO `product_certification` VALUES ('77', '203', '8', 'http://211.151.134.74:8080/portal/img/certification/document/laogandie/HACCP.jpg');
INSERT INTO `product_certification` VALUES ('78', '203', '31', 'http://211.151.134.74:8080/portal/img/certification/document/laogandie/ISO10012.jpg');

-- ----------------------------
-- Table structure for `product_instance`
-- ----------------------------
DROP TABLE IF EXISTS `product_instance`;
CREATE TABLE `product_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batch_serial_no` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `serial` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `production_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `original_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_instance_product_instance` (`original_id`),
  KEY `fk_product_instance_product` (`product_id`),
  KEY `idx_batch_serial_no_serial_product_id` (`batch_serial_no`,`serial`,`product_id`) USING BTREE,
  CONSTRAINT `fk_product_instance_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_product_instance_product_instance` FOREIGN KEY (`original_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=312 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of product_instance
-- ----------------------------
INSERT INTO `product_instance` VALUES ('2', '20120119', 'GTQTC2012060002', '2012-11-13 17:20:48', '2013-08-13 17:20:48', '81', null);
INSERT INTO `product_instance` VALUES ('151', '20120304', 'YLGTC2012060009', '2012-01-20 17:21:47', '2012-07-20 17:21:47', '1', null);
INSERT INTO `product_instance` VALUES ('242', '155651', '', null, null, '1', null);
INSERT INTO `product_instance` VALUES ('243', '20131008', 'YSSYLA201310001', null, null, '235', null);
INSERT INTO `product_instance` VALUES ('244', '20130905', 'YSYZYB201310001', null, null, '236', null);
INSERT INTO `product_instance` VALUES ('245', '20130923', 'YDZFFB201310005', null, null, '237', null);
INSERT INTO `product_instance` VALUES ('246', '20131129', '', '2013-11-29 00:00:00', null, '229', null);
INSERT INTO `product_instance` VALUES ('247', null, null, '2013-11-06 00:00:00', '2014-11-06 00:00:00', '240', null);
INSERT INTO `product_instance` VALUES ('248', null, null, '2013-11-02 00:00:00', '2013-04-02 00:00:00', '239', null);
INSERT INTO `product_instance` VALUES ('249', '2013-11-14', '', '2013-11-14 00:00:00', null, '240', null);
INSERT INTO `product_instance` VALUES ('250', '2013-11-02', '', '2013-11-02 00:00:00', null, '239', null);
INSERT INTO `product_instance` VALUES ('251', '2013-11-03', '', '2013-11-03 00:00:00', null, '239', null);
INSERT INTO `product_instance` VALUES ('252', '2013-12-02', '', '2013-12-02 00:00:00', null, '1', null);
INSERT INTO `product_instance` VALUES ('253', '2013-11-02', '', '2013-11-02 00:00:00', null, '1', null);
INSERT INTO `product_instance` VALUES ('254', '2013-11-7', '', '2013-11-07 00:00:00', null, '1', null);
INSERT INTO `product_instance` VALUES ('255', '2013/9/9', '', '2013-09-09 00:00:00', null, '240', null);
INSERT INTO `product_instance` VALUES ('256', '2013/11/21', '', '2013-11-21 00:00:00', null, '240', null);
INSERT INTO `product_instance` VALUES ('257', '2013/8/20', '', '2013-08-20 00:00:00', null, '240', null);
INSERT INTO `product_instance` VALUES ('259', '2013-11-6', '', '2013-11-06 00:00:00', null, '240', null);
INSERT INTO `product_instance` VALUES ('260', '2013-11-7', '', '2013-11-07 00:00:00', null, '240', null);
INSERT INTO `product_instance` VALUES ('261', '2013-11-01', '', '2013-11-01 00:00:00', null, '1', null);
INSERT INTO `product_instance` VALUES ('268', '2013-11-20', '', '2013-11-20 00:00:00', null, '1', null);
INSERT INTO `product_instance` VALUES ('269', '2013-11-30', '', '2013-11-30 00:00:00', null, '1', null);
INSERT INTO `product_instance` VALUES ('272', '2013-12-30', '', '2013-12-30 00:00:00', null, '1', null);
INSERT INTO `product_instance` VALUES ('273', '2013-12-03', '', '2013-12-03 00:00:00', null, '239', null);
INSERT INTO `product_instance` VALUES ('299', '2013-11-02', '', '2013-11-02 00:00:00', null, '239', null);
INSERT INTO `product_instance` VALUES ('300', '2013-11-06', '', '2013-11-06 00:00:00', null, '240', null);
INSERT INTO `product_instance` VALUES ('301', '2013-11-03', '', '2013-11-03 00:00:00', null, '239', null);
INSERT INTO `product_instance` VALUES ('302', '2013-11-07', '', '2013-11-07 00:00:00', null, '240', null);
INSERT INTO `product_instance` VALUES ('303', '2013-11-25', '', '2013-11-25 00:00:00', null, '1', null);
INSERT INTO `product_instance` VALUES ('307', '2013-05-27', '', '2013-05-27 00:00:00', null, '10094', null);
INSERT INTO `product_instance` VALUES ('308', '2012-04-23', '', '2012-04-23 00:00:00', null, '10095', null);
INSERT INTO `product_instance` VALUES ('309', '2013-02-09', '', '2013-02-09 00:00:00', null, '10096', null);
INSERT INTO `product_instance` VALUES ('310', '2013-01-24', '', '2013-01-24 00:00:00', null, '10097', null);
INSERT INTO `product_instance` VALUES ('311', '2013-05-26', '', '2013-05-26 00:00:00', null, '10098', null);

-- ----------------------------
-- Table structure for `product_poll`
-- ----------------------------
DROP TABLE IF EXISTS `product_poll`;
CREATE TABLE `product_poll` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_instance_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `like_` tinyint(4) DEFAULT NULL,
  `rate` int(11) DEFAULT NULL,
  `poll_date` datetime DEFAULT NULL,
  `comment` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_poll_product_instance` (`product_instance_id`),
  CONSTRAINT `fk_product_poll_product_instance` FOREIGN KEY (`product_instance_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16701 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of product_poll
-- ----------------------------

-- ----------------------------
-- Table structure for `testa`
-- ----------------------------
DROP TABLE IF EXISTS `testa`;
CREATE TABLE `testa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `item` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rank` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of testa
-- ----------------------------
INSERT INTO `testa` VALUES ('1', 'user1', 'item1', '3');
INSERT INTO `testa` VALUES ('2', 'user1', 'item2', '3');
INSERT INTO `testa` VALUES ('3', 'user1', 'item3', '3');
INSERT INTO `testa` VALUES ('4', 'user2', 'item1', '1');
INSERT INTO `testa` VALUES ('5', 'user2', 'item2', '1');
INSERT INTO `testa` VALUES ('6', 'user2', 'item3', '1');
INSERT INTO `testa` VALUES ('7', 'user3', 'item1', '1');
INSERT INTO `testa` VALUES ('8', 'user3', 'item2', '4');

-- ----------------------------
-- Table structure for `testb`
-- ----------------------------
DROP TABLE IF EXISTS `testb`;
CREATE TABLE `testb` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `usera` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ranka` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userb` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rankb` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `count` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of testb
-- ----------------------------

-- ----------------------------
-- Table structure for `test_case`
-- ----------------------------
DROP TABLE IF EXISTS `test_case`;
CREATE TABLE `test_case` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_cycle_id` bigint(20) DEFAULT NULL,
  `product_instance_id` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `business_brand_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_case_product_instance` (`product_instance_id`),
  KEY `fk_test_case_test_cycle` (`test_cycle_id`),
  KEY `fk_test_case_business_brand` (`business_brand_id`),
  CONSTRAINT `fk_test_case_business_brand` FOREIGN KEY (`business_brand_id`) REFERENCES `business_brand` (`id`),
  CONSTRAINT `fk_test_case_product_instance` FOREIGN KEY (`product_instance_id`) REFERENCES `product_instance` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_case
-- ----------------------------

-- ----------------------------
-- Table structure for `test_cycle`
-- ----------------------------
DROP TABLE IF EXISTS `test_cycle`;
CREATE TABLE `test_cycle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_cycle
-- ----------------------------

-- ----------------------------
-- Table structure for `test_lab`
-- ----------------------------
DROP TABLE IF EXISTS `test_lab`;
CREATE TABLE `test_lab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_lab_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_parent_lab` (`parent_lab_id`),
  CONSTRAINT `fk_parent_lab` FOREIGN KEY (`parent_lab_id`) REFERENCES `test_lab` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_lab
-- ----------------------------
INSERT INTO `test_lab` VALUES ('3', null, null);
INSERT INTO `test_lab` VALUES ('5', '贵阳', null);
INSERT INTO `test_lab` VALUES ('38', '贵州贵阳', '3');

-- ----------------------------
-- Table structure for `test_lab_test`
-- ----------------------------
DROP TABLE IF EXISTS `test_lab_test`;
CREATE TABLE `test_lab_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_lab_id` bigint(20) DEFAULT NULL,
  `test_cycle_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_lab_test_test_lab` (`test_lab_id`),
  KEY `fk_test_lab_test_test_cycle` (`test_cycle_id`),
  CONSTRAINT `fk_test_lab_test_test_lab` FOREIGN KEY (`test_lab_id`) REFERENCES `test_lab` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_lab_test
-- ----------------------------

-- ----------------------------
-- Table structure for `test_property`
-- ----------------------------
DROP TABLE IF EXISTS `test_property`;
CREATE TABLE `test_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_result_id` bigint(20) DEFAULT NULL,
  `category` bigint(20) DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `result` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tech_indicator` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `assessment` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `standard` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_property_test_result` (`test_result_id`),
  KEY `fk_test_property_test_property_category` (`category`),
  CONSTRAINT `fk_test_property_test_property_category` FOREIGN KEY (`category`) REFERENCES `test_property_category` (`id`),
  CONSTRAINT `fk_test_property_test_result` FOREIGN KEY (`test_result_id`) REFERENCES `test_result` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=947 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_property
-- ----------------------------
INSERT INTO `test_property` VALUES ('209', '36', null, '糖精钠', '3423', 'mg/kg', '<5000', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('210', '36', null, '氯化钾', '456546', 'mg/kg', '<5000', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('243', '36', null, '罂粟壳(粉)', '未检出', '', '不得检出', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('551', '169', null, '可溶性无盐固形物', '1', 'j', '', 'k', 'GB 18186-2000 酿造酱油酱油_特级_可溶性无盐固形物');
INSERT INTO `test_property` VALUES ('555', '173', null, '可溶性无盐固形物', '1', 'gd', '', 'f', 'GB 18186-2000 酿造酱油酱油_特级_可溶性无盐固形物');
INSERT INTO `test_property` VALUES ('556', '174', null, '苯甲酸', '未检出', 'g/kg', '不得检出', '合格', 'GB/T 23495-2009');
INSERT INTO `test_property` VALUES ('557', '174', null, '山梨酸', '未检出', 'g/kg', '不得检出', '合格', 'GB/T23495-2009');
INSERT INTO `test_property` VALUES ('558', '174', null, '日落黄', '未检出', 'g/kg', '不得检出', '合格', 'GB/T5009.35-2003');
INSERT INTO `test_property` VALUES ('559', '174', null, '柠檬黄', '未检出', 'g/kg', '不得检出', '合格', 'GB/T5009.35-2003');
INSERT INTO `test_property` VALUES ('560', '174', null, '胭脂红', '未检出', 'g/kg', '不得检出', '合格', 'GB/T5009.35-2003');
INSERT INTO `test_property` VALUES ('561', '174', null, '诱惑红', '未检出', 'g/kg', '不得检出', '合格', 'GB/T5009.141-2003');
INSERT INTO `test_property` VALUES ('562', '174', null, '氯霉素', '未检出', 'μ g/kg', '不得检出', '合格', 'GB/T22338-2008');
INSERT INTO `test_property` VALUES ('563', '174', null, '亚硝酸盐', '1.6', 'mg/kg', '≤30', '合格', 'GB5009.33-2010');
INSERT INTO `test_property` VALUES ('564', '175', null, '铅', '0.022', 'mg/kg', '≤0.1', '合格', 'GB5009.12-2010');
INSERT INTO `test_property` VALUES ('565', '175', null, '黄曲霉素B1', '0.31', 'μg/kg', '≤10', '合格', 'GB/T5009.22-2003');
INSERT INTO `test_property` VALUES ('566', '175', null, '苯并（a）比', '未检出', 'μg/kg', '≤10', '合格', 'GB/T5009.27-2003');
INSERT INTO `test_property` VALUES ('567', '175', null, '过氧化值', '0.11', 'g/100g', '≤0.25', '合格', 'GB/T5009.37-2003');
INSERT INTO `test_property` VALUES ('568', '175', null, '总砷', '未检出', 'mg/kg', '≤0.1', '合格', 'GB/T5009.11-2003');
INSERT INTO `test_property` VALUES ('569', '175', null, '酸价', '1.1', 'mg/g', '≤3', '合格', 'GB/T5009.37-2003');
INSERT INTO `test_property` VALUES ('570', '176', null, '吊白块(甲醛次硫酸氢钠)', '未检出', 'μg/g', '不得检出', '合格', 'GB/T21126-2007');
INSERT INTO `test_property` VALUES ('571', '176', null, '菌落总数', '90000', 'cfu/g', '≤100000', '合格', 'GB 4789.2-2010');
INSERT INTO `test_property` VALUES ('572', '176', null, '金黄色葡萄球菌', '未检出/25g', '', '不得检出', '合格', 'GB 4789.10-2010');
INSERT INTO `test_property` VALUES ('573', '176', null, '志贺氏菌', '未检出/25g', '', '不得检出', '合格', 'GB 4789.5-2012');
INSERT INTO `test_property` VALUES ('574', '176', null, '沙门氏菌', '未检出/25g', '', '不得检出', '合格', 'GB 4789.4-2010');
INSERT INTO `test_property` VALUES ('575', '176', null, '柠檬黄', '未检出', 'g/kg', '不得检出', '合格', 'GB/T5009.35-2003');
INSERT INTO `test_property` VALUES ('576', '176', null, '日落黄', '未检出', 'g/kg', '不得检出', '合格', 'GB/T5009.35-2003');
INSERT INTO `test_property` VALUES ('577', '176', null, '山梨酸', '未检出', 'g/kg', '不得检出', '合格', 'GB/T23495-2009');
INSERT INTO `test_property` VALUES ('578', '176', null, '苯甲酸', '未检出', 'g/kg', '不得检出', '合格', 'GB/T23495-2009');
INSERT INTO `test_property` VALUES ('579', '176', null, '大肠杆菌', 'MPN/100g', '≤150', '≤150', '不合格', 'GB/T4789.3-2003');
INSERT INTO `test_property` VALUES ('580', '177', null, '沙门氏菌', '24', 'f', '', '合格', 'GB 25191-2010 调制乳');
INSERT INTO `test_property` VALUES ('581', '178', null, '无盐固形物', '14.05 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('582', '178', null, '食    盐', '16 ', 'g/100mL', '15～17', '合  格', '');
INSERT INTO `test_property` VALUES ('583', '178', null, '氨基酸态氮', '0.74 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('584', '178', null, '空瓶细菌数', '50', 'cfu/mL', '≤50', '合  格', '');
INSERT INTO `test_property` VALUES ('585', '178', null, '内 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('586', '178', null, '大肠菌群', '<30', 'MPN/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('587', '178', null, '菌落总数', '10', 'cfu/mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('588', '178', null, '净 含 量', '502', 'mL', '500', '合  格', '');
INSERT INTO `test_property` VALUES ('589', '178', null, '感    官', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('590', '178', null, '全    氮', '1.30 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('591', '178', null, '铵    盐', '20 ', '%', '≤氨基氮的30%', '合  格', '');
INSERT INTO `test_property` VALUES ('592', '178', null, '外 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('593', '178', null, '总    酸', '1.82 ', 'g/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('594', '179', null, '大肠菌群(CFU/mL)', '<1', '', '', '合格', '<1');
INSERT INTO `test_property` VALUES ('595', '179', null, '脂肪(g/100g)', '4.29', '', '', '合格', '≥3.10');
INSERT INTO `test_property` VALUES ('596', '179', null, '总砷(mg/kg))', '未检出(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('597', '179', null, '酸度(°T)', '14.50', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('598', '179', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('599', '179', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('600', '179', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('601', '179', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('602', '179', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('603', '179', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('604', '179', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('605', '179', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('606', '179', null, '铬(mg/kg)', '0.019', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('607', '179', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('608', '179', null, '铅(mg/kg)', '未检出(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('609', '179', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('610', '179', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('611', '179', null, '总汞(mg/kg)', '未检出(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('612', '179', null, '非脂乳固体(g/100g)', '9.41', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('613', '180', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('614', '180', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('615', '180', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('616', '180', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('617', '180', null, '总砷(mg/kg))', '未检出(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('618', '180', null, '总汞(mg/kg)', '未检出(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('619', '180', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('620', '180', null, '酸度(°T)', '16.5', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('621', '180', null, '大肠菌群(CFU/mL)', '<2', '', '', '不合格', '<1');
INSERT INTO `test_property` VALUES ('622', '180', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('623', '180', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('624', '180', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('625', '180', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('626', '180', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('627', '180', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('628', '180', null, '铬(mg/kg)', '0.029', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('629', '180', null, '非脂乳固体(g/100g)', '9.51', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('630', '180', null, '脂肪(g/100g)', '2.29', '', '', '不合格', '≥3.10');
INSERT INTO `test_property` VALUES ('631', '180', null, '铅(mg/kg)', '未检出(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('632', '181', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('633', '181', null, '脂肪(g/100g)', '4.29', '', '', '合格', '≥3.10');
INSERT INTO `test_property` VALUES ('634', '181', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('635', '181', null, '总汞(mg/kg)', '未检出(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('636', '181', null, '总砷(mg/kg))', '未检出(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('637', '181', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('638', '181', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('639', '181', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('640', '181', null, '酸度(°T)', '14.50', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('641', '181', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('642', '181', null, '大肠菌群(CFU/mL)', '<1', '', '', '合格', '<1');
INSERT INTO `test_property` VALUES ('643', '181', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('644', '181', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('645', '181', null, '铬(mg/kg)', '0.019', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('646', '181', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('647', '181', null, '非脂乳固体(g/100g)', '9.41', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('648', '181', null, '铅(mg/kg)', '未检出(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('649', '181', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('650', '181', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('651', '182', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('652', '182', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('653', '182', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('654', '182', null, '大肠菌群(CFU/mL)', '<1', '', '', '合格', '<1');
INSERT INTO `test_property` VALUES ('655', '182', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('656', '182', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('657', '182', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('658', '182', null, '铅(mg/kg)', '未检出(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('659', '182', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('660', '182', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('661', '182', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('662', '182', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('663', '182', null, '脂肪(g/100g)', '4.29', '', '', '合格', '≥3.10');
INSERT INTO `test_property` VALUES ('664', '182', null, '总汞(mg/kg)', '未检出(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('665', '182', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('666', '182', null, '总砷(mg/kg))', '未检出(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('667', '182', null, '酸度(°T)', '14.50', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('668', '182', null, '铬(mg/kg)', '0.019', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('669', '182', null, '非脂乳固体(g/100g)', '9.41', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('670', '183', null, '菌落总数', '20', 'cfu/mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('671', '183', null, '大肠菌群', '<30', 'MPN/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('672', '183', null, '感    官', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('673', '183', null, '无盐固形物', '11.05 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('674', '183', null, '外 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('675', '183', null, '氨基酸态氮', '0.58 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('676', '183', null, '铵    盐', '22 ', '%', '≤氨基氮的30%', '合  格', '');
INSERT INTO `test_property` VALUES ('677', '183', null, '总    酸', '1.60 ', 'g/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('678', '183', null, '食    盐', '16 ', 'g/100mL', '15～17', '合  格', '');
INSERT INTO `test_property` VALUES ('679', '183', null, '内 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('680', '183', null, '净 含 量', '439', 'mL', '438', '合  格', '');
INSERT INTO `test_property` VALUES ('681', '183', null, '全    氮', '1.03 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('682', '184', null, '菌落总数', '60', 'cfu/mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('683', '184', null, '内 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('684', '184', null, '食    盐', '16 ', 'g/100mL', '15～17', '合  格', '');
INSERT INTO `test_property` VALUES ('685', '184', null, '全    氮', '1.35 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('686', '184', null, '无盐固形物', '14.89 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('687', '184', null, '净 含 量', '502', 'mL', '500', '合  格', '');
INSERT INTO `test_property` VALUES ('688', '184', null, '外 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('689', '184', null, '总    酸', '2.03 ', 'g/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('690', '184', null, '空瓶细菌数', '30', 'cfu/mL', '≤50', '合  格', '');
INSERT INTO `test_property` VALUES ('691', '184', null, '铵    盐', '20 ', '%', '≤氨基氮的30%', '合  格', '');
INSERT INTO `test_property` VALUES ('692', '184', null, '氨基酸态氮', '0.75 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('693', '184', null, '大肠菌群', '<30', 'MPN/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('694', '184', null, '感    官', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('695', '185', null, '无盐固形物', '14.64 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('696', '185', null, '铵    盐', '21 ', '%', '≤氨基氮的30%', '合  格', '');
INSERT INTO `test_property` VALUES ('697', '185', null, '感    官', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('698', '185', null, '大肠菌群', '<30', 'MPN/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('699', '185', null, '内 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('700', '185', null, '氨基酸态氮', '0.76 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('701', '185', null, '空瓶细菌数', '10', 'cfu/mL', '≤50', '合  格', '');
INSERT INTO `test_property` VALUES ('702', '185', null, '净 含 量', '502', 'mL', '500', '合  格', '');
INSERT INTO `test_property` VALUES ('703', '185', null, '菌落总数', '40', 'cfu/mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('704', '185', null, '外 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('705', '185', null, '食    盐', '15 ', 'g/100mL', '15～17', '合  格', '');
INSERT INTO `test_property` VALUES ('706', '185', null, '总    酸', '1.76 ', 'g/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('707', '185', null, '全    氮', '1.38 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('708', '186', null, '大肠菌群', '<30', 'MPN/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('709', '186', null, '空瓶细菌数', '10', 'cfu/mL', '≤50', '合  格', '');
INSERT INTO `test_property` VALUES ('710', '186', null, '外 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('711', '186', null, '菌落总数', '10', 'cfu/mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('712', '186', null, '感    官', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('713', '186', null, '全    氮', '1.32 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('714', '186', null, '无盐固形物', '15.08 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('715', '186', null, '铵    盐', '21 ', '%', '≤氨基氮的30%', '合  格', '');
INSERT INTO `test_property` VALUES ('716', '186', null, '食    盐', '16 ', 'g/100mL', '15～17', '合  格', '');
INSERT INTO `test_property` VALUES ('717', '186', null, '内 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('718', '186', null, '净 含 量', '502', 'mL', '500', '合  格', '');
INSERT INTO `test_property` VALUES ('719', '186', null, '总    酸', '1.97 ', 'g/100mL', '≤', '合  格', '');
INSERT INTO `test_property` VALUES ('720', '186', null, '氨基酸态氮', '0.71 ', 'g/100mL', '≥', '合  格', '');
INSERT INTO `test_property` VALUES ('721', '187', null, '内 包 装', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('722', '187', null, '外 包 装', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('723', '187', null, '总    酸', '1.96 ', 'g/100mL', '≤2.50', '合  格', '');
INSERT INTO `test_property` VALUES ('724', '187', null, '菌落总数', '90', 'cfu/mL', '≤30000', '合  格', '');
INSERT INTO `test_property` VALUES ('725', '187', null, '大肠菌群', '25', 'MPN/100mL', '≤30', '合  格', '');
INSERT INTO `test_property` VALUES ('726', '187', null, '空瓶细菌数', '12', 'cfu/mL', '≤50', '合  格', '');
INSERT INTO `test_property` VALUES ('727', '187', null, '食    盐', '16 ', 'g/100mL', '15～17', '合  格', '');
INSERT INTO `test_property` VALUES ('728', '187', null, '无盐固形物', '12.00 ', 'g/100mL', '≥13.00', '不合格', '');
INSERT INTO `test_property` VALUES ('729', '187', null, '氨基酸态氮', '0.76 ', 'g/100mL', '≥0.70', '合  格', '');
INSERT INTO `test_property` VALUES ('730', '187', null, '感    官', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('731', '187', null, '铵    盐', '26 ', '%', '≤氨基氮的30%', '合  格', '');
INSERT INTO `test_property` VALUES ('732', '187', null, '净 含 量', '501', 'mL', '500', '合  格', '');
INSERT INTO `test_property` VALUES ('733', '187', null, '全    氮', '1.36 ', 'g/100mL', '≥1.03', '合  格', '');
INSERT INTO `test_property` VALUES ('734', '188', null, '食    盐', '15 ', 'g/100mL', '15～17', '合  格', '');
INSERT INTO `test_property` VALUES ('735', '188', null, '外 包 装', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('736', '188', null, '铵    盐', '26 ', '%', '≤氨基氮的30%', '合  格', '');
INSERT INTO `test_property` VALUES ('737', '188', null, '空瓶细菌数', '12', 'cfu/mL', '≤50', '合  格', '');
INSERT INTO `test_property` VALUES ('738', '188', null, '内 包 装', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('739', '188', null, '大肠菌群', '27', 'MPN/100mL', '≤3030', '合  格', '');
INSERT INTO `test_property` VALUES ('740', '188', null, '全    氮', '1.34 ', 'g/100mL', '≥1.03', '合  格', '');
INSERT INTO `test_property` VALUES ('741', '188', null, '氨基酸态氮', '0.78 ', 'g/100mL', '≥0.70', '合  格', '');
INSERT INTO `test_property` VALUES ('742', '188', null, '净 含 量', '501', 'mL', '500', '合  格', '');
INSERT INTO `test_property` VALUES ('743', '188', null, '菌落总数', '86', 'cfu/mL', '≤3000030000', '合  格', '');
INSERT INTO `test_property` VALUES ('744', '188', null, '无盐固形物', '14.00 ', 'g/100mL', '≥13.00', '合  格', '');
INSERT INTO `test_property` VALUES ('745', '188', null, '感    官', '正  常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('746', '188', null, '总    酸', '1.98 ', 'g/100mL', '≤2.502.5', '合  格', '');
INSERT INTO `test_property` VALUES ('747', '189', null, '铬(mg/kg)', '0.019', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('748', '189', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('749', '189', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('750', '189', null, '大肠菌群(CFU/mL)', '<1', '', '', '合格', '<1');
INSERT INTO `test_property` VALUES ('751', '189', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('752', '189', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('753', '189', null, '酸度(°T)', '14.50', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('754', '189', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('755', '189', null, '脂肪(g/100g)', '4.29', '', '', '合格', '≥3.10');
INSERT INTO `test_property` VALUES ('756', '189', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('757', '189', null, '非脂乳固体(g/100g)', '9.41', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('758', '189', null, '总砷(mg/kg))', '未检出(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('759', '189', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('760', '189', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('761', '189', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('762', '189', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('763', '189', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('764', '189', null, '总汞(mg/kg)', '未检出(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('765', '189', null, '铅(mg/kg)', '未检出(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('766', '190', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('767', '190', null, '总汞(mg/kg)', '未检出(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('768', '190', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('769', '190', null, '总砷(mg/kg))', '未检出(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('770', '190', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('771', '190', null, '脂肪(g/100g)', '4.29', '', '', '合格', '≥3.10');
INSERT INTO `test_property` VALUES ('772', '190', null, '铅(mg/kg)', '未检出(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('773', '190', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('774', '190', null, '铬(mg/kg)', '0.019', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('775', '190', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('776', '190', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('777', '190', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('778', '190', null, '酸度(°T)', '14.50', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('779', '190', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('780', '190', null, '大肠菌群(CFU/mL)', '<1', '', '', '合格', '<1');
INSERT INTO `test_property` VALUES ('781', '190', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('782', '190', null, '非脂乳固体(g/100g)', '9.41', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('783', '190', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('784', '190', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('785', '191', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('786', '191', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('787', '191', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('788', '191', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('789', '191', null, '非脂乳固体(g/100g)', '9.41', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('790', '191', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('791', '191', null, '大肠菌群(CFU/mL)', '<1', '', '', '合格', '<1');
INSERT INTO `test_property` VALUES ('792', '191', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('793', '191', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('794', '191', null, '铬(mg/kg)', '0.019', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('795', '191', null, '脂肪(g/100g)', '4.29', '', '', '合格', '≥3.10');
INSERT INTO `test_property` VALUES ('796', '191', null, '总砷(mg/kg))', '(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('797', '191', null, '铅(mg/kg)', '(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('798', '191', null, '酸度(°T)', '14.50', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('799', '191', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('800', '191', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('801', '191', null, '总汞(mg/kg)', '(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('802', '191', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('803', '191', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('804', '192', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('805', '192', null, '铅(mg/kg)', '(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('806', '192', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('807', '192', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('808', '192', null, '脂肪(g/100g)', '4.29', '', '', '合格', '≥3.10');
INSERT INTO `test_property` VALUES ('809', '192', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('810', '192', null, '总砷(mg/kg))', '(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('811', '192', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('812', '192', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('813', '192', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('814', '192', null, '非脂乳固体(g/100g)', '9.41', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('815', '192', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('816', '192', null, '总汞(mg/kg)', '(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('817', '192', null, '铬(mg/kg)', '0.019', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('818', '192', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('819', '192', null, '酸度(°T)', '14.50', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('820', '192', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('821', '192', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('822', '192', null, '大肠菌群(CFU/mL)', '<1', '', '', '合格', '<1');
INSERT INTO `test_property` VALUES ('823', '193', null, '大肠菌群(CFU/mL)', '<1', '', '', '合格', '<1');
INSERT INTO `test_property` VALUES ('824', '193', null, '沙门氏菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('825', '193', null, '铅(mg/kg)', '(<0.0050)', '', '', '合格', '≤0.05');
INSERT INTO `test_property` VALUES ('826', '193', null, '总砷(mg/kg))', '(<0.010)', '', '', '合格', '≤0.1');
INSERT INTO `test_property` VALUES ('827', '193', null, '蛋白质(g/100g)', '3.45', '', '', '合格', '≥2.90');
INSERT INTO `test_property` VALUES ('828', '193', null, '铬(mg/kg)', '0.019', '', '', '合格', '≤0.3');
INSERT INTO `test_property` VALUES ('829', '193', null, '脂肪(g/100g)', '4.29', '', '', '合格', '≥3.10');
INSERT INTO `test_property` VALUES ('830', '193', null, '黄曲霉毒素M1(μg/kg)', '0.08959', '', '', '合格', '≤0.5');
INSERT INTO `test_property` VALUES ('831', '193', null, '非脂乳固体(g/100g)', '9.41', '', '', '合格', '≥8.10');
INSERT INTO `test_property` VALUES ('832', '193', null, '金黄色葡萄球菌', '未检出', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('833', '193', null, '组织状态', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('834', '193', null, '三聚氰胺(mg/kg)', '未检出(<2)', '', '', '合格', '不得检出');
INSERT INTO `test_property` VALUES ('835', '193', null, '滋味、气味', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('836', '193', null, '菌落总数(CFU/ml)', '<1', '', '', '合格', '≤10');
INSERT INTO `test_property` VALUES ('837', '193', null, '总汞(mg/kg)', '(<0.000150)', '', '', '合格', '≤0.01');
INSERT INTO `test_property` VALUES ('838', '193', null, '净含量(ml)', '符合', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('839', '193', null, '色泽', ' 符合 ', '', '', '合格', '符合');
INSERT INTO `test_property` VALUES ('840', '193', null, '抗生素', '-', '', '', '合格', '阴性');
INSERT INTO `test_property` VALUES ('841', '193', null, '酸度(°T)', '14.50', '', '', '合格', '12.0-18.0');
INSERT INTO `test_property` VALUES ('842', '194', null, '亚硝酸盐', '未检出', 'mg/kg', '', '合格', 'GB 2762-2012 食品安全国家标准 食品中污染物限量');
INSERT INTO `test_property` VALUES ('843', '194', null, '志贺氏菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('844', '194', null, '酵母', '＜1', 'cfu/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('845', '194', null, '沙门氏菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('846', '194', null, '总汞', '未检出', 'mg/kg', '', '合格', 'GB 2762-2012 食品安全国家标准 食品中污染物限量');
INSERT INTO `test_property` VALUES ('847', '194', null, '霉菌', '＜1', 'cfu/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('848', '194', null, '金黄色葡萄球菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('849', '194', null, '菌落总数', '＜1', 'CFU/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('850', '194', null, '总砷（以As计）', '未检出', 'mg/kg', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('851', '194', null, '大肠菌群', '＜3', 'MPN/100mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('852', '195', null, '氨基酸态氮（以氮计）', '0.72', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('853', '195', null, '总酸（以乳酸计）', '1.80', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('854', '195', null, '可溶性无盐固形物', '14.6', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('855', '195', null, '全氮（以氮计）', '1.38', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('856', '196', null, '亚硝酸盐', '未检出', 'mg/kg', '', '合格', 'GB 2762-2012 食品安全国家标准 食品中污染物限量');
INSERT INTO `test_property` VALUES ('857', '196', null, '志贺氏菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('858', '196', null, '酵母', '＜1.2', 'cfu/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('859', '196', null, '沙门氏菌', '未检出/24g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('860', '196', null, '总汞', '未检出', 'mg/kg', '', '合格', 'GB 2762-2012 食品安全国家标准 食品中污染物限量');
INSERT INTO `test_property` VALUES ('861', '196', null, '霉菌', '＜2', 'cfu/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('862', '196', null, '金黄色葡萄球菌', '未检出/24g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('863', '196', null, '菌落总数', '＜1.3', 'CFU/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('864', '196', null, '总砷（以As计）', '未检出', 'mg/kg', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('865', '196', null, '大肠菌群', '＜2', 'MPN/100mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('866', '197', null, '氨基酸态氮（以氮计）', '0.74', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('867', '197', null, '总酸（以乳酸计）', '1.82', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('868', '197', null, '可溶性无盐固形物', '14.7', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('869', '197', null, '全氮（以氮计）', '1.48', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('870', '198', null, '氨基酸态氮（以氮计）', '0.75', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('871', '198', null, '可溶性无盐固形物', '15.3', 'g/100ml', '', '不合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('872', '198', null, '总砷（以As计）', '1.75', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('873', '198', null, '总酸（以乳酸计）', '1.72', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('874', '198', null, '全氮（以氮计）', '1.35', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('875', '199', null, '可溶性无盐固形物', '14.8', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('876', '199', null, '总酸（以乳酸计）', '0.78', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('877', '199', null, '总砷（以As计）', '1.74', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('878', '199', null, '全氮（以氮计）', '1.75', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('879', '199', null, '氨基酸态氮（以氮计）', '1.34', 'g/100ml', '', '合格', 'GB 18186-2000 酿造酱油');
INSERT INTO `test_property` VALUES ('880', '200', null, '总汞', '未检出', 'mg/kg', '', '合格', 'GB 2762-2012 食品安全国家标准 食品中污染物限量');
INSERT INTO `test_property` VALUES ('881', '200', null, '霉菌', '＜1', 'CFU/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('882', '200', null, '总砷（以As计）', '未检出', 'mg/kg', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('883', '200', null, '金黄色葡萄球菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('884', '200', null, '沙门氏菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('885', '200', null, '亚硝酸盐', '未检出', 'mg/kg', '', '合格', 'GB 2762-2012 食品安全国家标准 食品中污染物限量');
INSERT INTO `test_property` VALUES ('886', '200', null, '菌落总数', '＜1', 'CFU/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('887', '200', null, '志贺氏菌', '检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('888', '200', null, '大肠菌群', '＜3', 'MPN/100mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('889', '201', null, '酵母', '＜1', 'CFU/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('890', '201', null, '总汞', '未检出', 'mg/kg', '', '合格', 'GB 2762-2012 食品安全国家标准 食品中污染物限量');
INSERT INTO `test_property` VALUES ('891', '201', null, '总砷（以As计）', '未检出', 'mg/kg', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('892', '201', null, '金黄色葡萄球菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('893', '201', null, '大肠菌群', '＜3', 'MPN/100mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('894', '201', null, '霉菌', '＜1', 'CFU/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('895', '201', null, '菌落总数', '＜1', 'CFU/mL', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('896', '201', null, '沙门氏菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('897', '201', null, '志贺氏菌', '未检出/25g', '—', '', '合格', 'GB 11673-2003 含乳饮料卫生标准');
INSERT INTO `test_property` VALUES ('898', '201', null, '亚硝酸盐', '未检出', 'mg/kg', '', '合格', 'GB 2762-2012 食品安全国家标准 食品中污染物限量');
INSERT INTO `test_property` VALUES ('899', '202', null, '总    酸', '4.68 ', 'g/100mL', '≥4.50 ', '合  格', '');
INSERT INTO `test_property` VALUES ('900', '202', null, '不挥发酸', '1.88 ', 'g/100mL', '≥0.50 ', '合  格', '');
INSERT INTO `test_property` VALUES ('901', '202', null, '大肠菌群', '<3', 'MPN/100mL', '≤3 ', '合  格', '');
INSERT INTO `test_property` VALUES ('902', '202', null, '无盐固形物', '11.27 ', 'g/100mL', '≥1.00 ', '合  格', '');
INSERT INTO `test_property` VALUES ('903', '202', null, '内 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('904', '202', null, '净 含 量', '502', 'mL', '500', '合  格', '');
INSERT INTO `test_property` VALUES ('905', '202', null, '空瓶细菌数', '10', 'cfu/mL', '≤50', '合  格', '');
INSERT INTO `test_property` VALUES ('906', '202', null, '外 包 装', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('907', '202', null, '菌落总数', '10', 'cfu/mL', '≤10000 ', '合  格', '');
INSERT INTO `test_property` VALUES ('908', '202', null, '感    官', '正常', '', '', '合  格', '');
INSERT INTO `test_property` VALUES ('912', '206', null, '请见检测报告复印件', '', '', '', '', '');
INSERT INTO `test_property` VALUES ('913', '207', null, '请见检测报告复印件', '', '', '', '', '');
INSERT INTO `test_property` VALUES ('914', '208', null, '氯', '553.5', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('915', '208', null, '锌', '7.96', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('916', '208', null, '硒', '5.44', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('917', '208', null, '镁', '53.25', 'mg/100g', '', '合格', 'Q/PMC0004S-2012');
INSERT INTO `test_property` VALUES ('918', '208', null, '碘', '87.79', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('919', '208', null, '锰', '863.5', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('920', '208', null, '磷', '464.5', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('921', '208', null, '钠', '220.1', 'mg/100', '', '合格', '');
INSERT INTO `test_property` VALUES ('922', '208', null, '铁', '6.39', 'mg/100g', '', '合格', 'Q/PMC0004S-2012');
INSERT INTO `test_property` VALUES ('923', '208', null, '钾', '1120.8', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('924', '209', null, '请见检测报告附件', '', '', '', '', '');
INSERT INTO `test_property` VALUES ('925', '210', null, '请见检测报告附件', '', '', '', '', '');
INSERT INTO `test_property` VALUES ('926', '211', null, '钾', '1120.8', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('927', '211', null, '钠', '220.1', 'mg/100', '', '合格', '');
INSERT INTO `test_property` VALUES ('928', '211', null, '锰', '863.5', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('929', '211', null, '镁', '53.25', 'mg/100g', '', '合格', 'Q/PMC0004S-2012');
INSERT INTO `test_property` VALUES ('930', '211', null, '硒', '5.44', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('931', '211', null, '磷', '464.5', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('932', '211', null, '氯', '553.5', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('933', '211', null, '锌', '7.96', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('934', '211', null, '碘', '87.79', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('935', '211', null, '铁', '6.39', 'mg/100g', '', '合格', 'Q/PMC0004S-2012');
INSERT INTO `test_property` VALUES ('936', '212', null, '钠', '220.1', 'mg/100', '', '合格', '');
INSERT INTO `test_property` VALUES ('937', '212', null, '硒', '5.44', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('938', '212', null, '钾', '1120.8', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('939', '212', null, '铁', '6.39', 'mg/100g', '', '合格', 'Q/PMC0004S-2012');
INSERT INTO `test_property` VALUES ('940', '212', null, '磷', '464.5', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('941', '212', null, '氯', '553.5', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('942', '212', null, '碘', '87.79', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('943', '212', null, '锰', '863.5', 'μg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('944', '212', null, '锌', '7.96', 'mg/100g', '', '合格', '');
INSERT INTO `test_property` VALUES ('945', '212', null, '镁', '53.25', 'mg/100g', '', '合格', 'Q/PMC0004S-2012');
INSERT INTO `test_property` VALUES ('946', '213', null, '请见检测报告附件', '', '', '', '', '');

-- ----------------------------
-- Table structure for `test_property_category`
-- ----------------------------
DROP TABLE IF EXISTS `test_property_category`;
CREATE TABLE `test_property_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_property_category
-- ----------------------------
INSERT INTO `test_property_category` VALUES ('1', '1001', '营养成份');
INSERT INTO `test_property_category` VALUES ('2', '1002', '微量元素');

-- ----------------------------
-- Table structure for `test_result`
-- ----------------------------
DROP TABLE IF EXISTS `test_result`;
CREATE TABLE `test_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pass` tinyint(1) DEFAULT NULL,
  `test_date` datetime DEFAULT NULL,
  `tester_id` bigint(20) DEFAULT NULL,
  `tester_type` int(11) DEFAULT NULL,
  `fda_test_plan_id` bigint(20) DEFAULT NULL,
  `brand_id` bigint(20) DEFAULT NULL,
  `testee_id` bigint(20) DEFAULT NULL,
  `sample_id` bigint(20) DEFAULT NULL,
  `sample_quantity` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sampling_location` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sampling_date` datetime DEFAULT NULL,
  `test_type` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `equipment` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `standard` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `result` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `approve_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `audit_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `key_tester` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pdf_report` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `test_lab_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_result_fda_test_plan` (`fda_test_plan_id`),
  KEY `fk_test_result_business_unit` (`testee_id`),
  KEY `fk_test_result_product_instance` (`sample_id`),
  KEY `fk_test_result_business_brand` (`brand_id`),
  KEY `fk_test_result_test_lab` (`test_lab_id`),
  CONSTRAINT `fk_test_result_business_brand` FOREIGN KEY (`brand_id`) REFERENCES `business_brand` (`id`),
  CONSTRAINT `fk_test_result_business_unit` FOREIGN KEY (`testee_id`) REFERENCES `business_unit` (`id`),
  CONSTRAINT `fk_test_result_fda_test_plan` FOREIGN KEY (`fda_test_plan_id`) REFERENCES `fda_test_plan` (`id`),
  CONSTRAINT `fk_test_result_product_instance` FOREIGN KEY (`sample_id`) REFERENCES `product_instance` (`id`),
  CONSTRAINT `fk_test_result_test_lab` FOREIGN KEY (`test_lab_id`) REFERENCES `test_lab` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of test_result
-- ----------------------------
INSERT INTO `test_result` VALUES ('36', 'Pass', '1', '2013-08-29 15:48:24', null, '3', '1', '350', '371', '2', '1', '杭州六合福思科技有限公司', '2013-09-22 18:35:22', '企业自检', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'testerb', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('169', '备注 ', '0', '2013-11-22 12:33:56', null, '4', null, '10', '10', '151', '抽样量 ', '抽样地点 ', '2013-11-22 12:33:56', '企业送检', '备注 ', '判定依据 ', '送检要求 ', '批准人 ', '审核人 ', '主检人 ', '/publish/FZ20137013/FZ20137013-1.pdf', '38');
INSERT INTO `test_result` VALUES ('173', '备注 	', '0', '2013-11-22 17:33:35', null, '4', null, '10', '10', '242', '抽样量 	', '抽样地点 	', '2013-11-14 16:00:00', '企业送检', '主要仪器 ', '判定依据 	', '检测结论 	', '批准人 	', '审核人 	', '主检人 ', '/publish/FZ20137009/FZ20137009-1.pdf', '38');
INSERT INTO `test_result` VALUES ('174', '抽样编号：YSSYLA201310001  GYYLZP-54', null, '2013-11-15 17:33:35', null, '4', '8', '373', '383', '243', '3份', '贵阳市金阳新区睿力上城第A1、A2、A3栋2层3号', '2013-10-09 00:00:00', '委托检验', '高效液相色谱', '《动物性食品中兽药最高残留限量》（农业部235号公告） 《食品安全国家标准  食品添加剂使用标准》（GB 2760-2011）', '所检指标符合《动物性食品中兽药最高残留限量》（农业部235号公告）；《食品安全国家标准  食品添加剂使用标准》（GB 2760-\r\n2011）标准要求。', null, null, null, null, null);
INSERT INTO `test_result` VALUES ('175', '抽样编号：YSYZYB201310001 LPSSYY-01', null, null, null, '4', '9', '374', '385', '244', '3瓶', '第二食堂厨房', '2013-09-10 00:00:00', '委托检验', '原子吸收光谱仪', '《食用植物油卫生标准》（GB 2716-2005）', '所检指标符合《食用植物油卫生标准》（GB 2716-2005）标准要\r\n求。', null, null, null, null, null);
INSERT INTO `test_result` VALUES ('176', '抽样编号：YDZFFB201310005LPS-DZP-28', null, null, null, '4', '10', '375', '386', '245', '3袋', '钟山区德坞西宁村一组', '2013-09-23 00:00:00', '委托检验', '高效液相色谱仪', '《食品中可能违法添加的非食用物质和易滥用的食品添加剂品种名单（第一批）》（食品整治办〔2008〕3号）、《食品安全国家标准食品添加剂使用标准》（GB 2760-2011）、《非发酵性豆制品及面筋卫生标准》（GB 2711-2003 ）', '所检指标中大肠菌群含量不符合《非发酵性豆制品及面筋卫生标准》（GB 2711-2003 ） 标准要求，该批次产品质量不合格。', null, null, null, null, null);
INSERT INTO `test_result` VALUES ('177', '备注 ', '0', '2013-11-30 11:29:56', null, '4', null, '371', '382', '246', '抽样量 ', '抽样地点 ', '2013-11-30 12:59:37', '企业送检', '主要仪器 ', '判定依据 ', '检测结论 ', '批准人 ', '样品状态 ', '主检人 ', '/publish/FZ20137505/FZ20137505-1.pdf', '38');
INSERT INTO `test_result` VALUES ('178', '', '0', '2013-12-02 09:33:29', null, '2', null, '371', '382', '249', '', '', '2013-12-02 13:09:55', '企业自检', '', 'GB18186一级', '符合GB18186一级标准', '喻爱难', '徐立秀', '邹利 陈敏 龙春蓉 罗晓青', '/publish/zh2013-11-T-14/zh2013-11-T-14-1.pdf', null);
INSERT INTO `test_result` VALUES ('179', '', '0', '2013-12-02 15:20:08', null, '2', null, '279', '279', '250', '', '', '2013-12-02 15:20:09', '企业自检', '', '', '', '包凤琳', '张丽凤', '', '/publish/MN2013110202/MN2013110202-1.pdf', null);
INSERT INTO `test_result` VALUES ('180', '', '0', '2013-12-02 15:20:31', null, '2', null, '279', '279', '251', '', '', '2013-12-02 15:20:31', '企业自检', '', '', '', '包凤琳', '张丽凤', '', '/publish/MN2013110301/MN2013110301-1.pdf', null);
INSERT INTO `test_result` VALUES ('181', '', '0', '2013-12-02 15:20:39', null, '2', null, '10', '10', '252', '', '', '2013-12-02 15:20:39', '企业自检', '', '', '', '包凤琳', '张丽凤', '', '/publish/MN2013-11-01-07/MN2013-11-01-07-1.pdf', null);
INSERT INTO `test_result` VALUES ('182', '', '0', '2013-12-02 15:20:47', null, '2', null, '10', '10', '253', '', '', '2013-12-02 15:20:48', '企业自检', '', '', '', '包凤琳', '张丽凤', '', '/publish/MN2013110102/MN2013110102-1.pdf', null);
INSERT INTO `test_result` VALUES ('183', '', '0', '2013-12-02 15:21:27', null, '2', null, '10', '10', '254', '2袋', '', '2013-12-02 15:21:27', '企业自检', '', 'GB18186二级', '符合GB18186二级标准', '喻爱难', '徐立秀', '邹利 陈敏 龙春蓉 罗晓青', '/publish/zh2013-11-J-15/zh2013-11-J-15-1.pdf', null);
INSERT INTO `test_result` VALUES ('184', '', '0', '2013-12-02 15:21:31', null, '2', null, '371', '382', '255', '2瓶', '', '2013-12-02 15:21:31', '企业自检', '', 'GB18186一级', '符合GB18186一级标准', '喻爱难', '徐立秀', '罗晓青 龙春蓉 陈艳 陈敏 ', '/publish/zh2013-09-T-25/zh2013-09-T-25-1.pdf', null);
INSERT INTO `test_result` VALUES ('185', '', '0', '2013-12-02 15:21:36', null, '2', null, '371', '382', '256', '2瓶', '', '2013-12-02 15:21:36', '企业自检', '', 'GB18186一级', '符合GB18186一级标准', '喻爱难', '徐立秀', '邹利 陈敏 龙春蓉 罗晓青', '/publish/zh2013-11-T-67/zh2013-11-T-67-1.pdf', null);
INSERT INTO `test_result` VALUES ('186', '', '0', '2013-12-02 15:21:40', null, '2', null, '371', '382', '257', '2瓶', '', '2013-12-02 15:21:40', '企业自检', '', 'GB18186一级', '符合GB18186一级标准', '喻爱难', '徐立秀', '陈艳 罗晓青 陈敏 邹利', '/publish/zh2013-08-T-52/zh2013-08-T-52-1.pdf', null);
INSERT INTO `test_result` VALUES ('187', '', '0', '2013-12-02 15:50:46', null, '2', null, '371', '382', '259', '2瓶', '', '2013-12-02 15:50:46', '企业自检', '', 'GB18186一级', '符合GB18186一级标准', '喻爱难', '徐立秀', '陈敏 徐立秀 罗晓青', '/publish/zh2013-11-T-16/zh2013-11-T-16-1.pdf', null);
INSERT INTO `test_result` VALUES ('188', '', '0', '2013-12-02 15:50:52', null, '2', null, '371', '382', '260', '2瓶', '包装 11-73', '2013-12-02 15:50:52', '企业自检', '', 'GB18186一级', '符合GB18186一级标准', '喻爱难', '徐立秀', '陈敏 徐立秀 罗晓青', '/publish/zh2013-11-T-17/zh2013-11-T-17-1.pdf', null);
INSERT INTO `test_result` VALUES ('189', '', '0', '2013-12-02 16:05:17', null, '2', null, '10', '10', '261', '', '', '2013-12-02 16:05:21', '企业自检', '', '', '', '包凤琳', '张丽凤', '', '/publish/MN2013-11-01-13/MN2013-11-01-13-1.pdf', null);
INSERT INTO `test_result` VALUES ('190', '', '0', '2013-12-02 16:19:17', null, '2', null, '10', '10', '268', '', '', '2013-12-02 16:19:21', '企业自检', '', '', '', '包凤琳', '张丽凤', '', '/publish/MN2013-11-01-15/MN2013-11-01-15-1.pdf', null);
INSERT INTO `test_result` VALUES ('191', '', '0', '2013-12-02 16:56:33', null, '2', null, '10', '10', '269', '', '', '2013-12-02 16:56:33', '企业自检', '', '', '', '高杰', '张丽凤', '', '/publish/MN2013-11-30-01/MN2013-11-30-01-1.pdf', null);
INSERT INTO `test_result` VALUES ('192', '', '0', '2013-12-02 22:09:06', null, '2', null, '10', '10', '272', '', '', '2013-12-02 22:09:06', '企业自检', '', '', '', '高杰', '张丽凤', '', '/publish/MN2013-12-30-01/MN2013-12-30-01-1.pdf', null);
INSERT INTO `test_result` VALUES ('193', '', '0', '2013-12-03 10:09:33', null, '2', null, '279', '279', '273', '', '', '2013-12-03 10:09:33', '企业自检', '', '', '', '高杰', '张丽凤', '', '/publish/MN2013-12-03-01/MN2013-12-03-01-1.pdf', null);
INSERT INTO `test_result` VALUES ('194', '合格', '0', '2013-11-02 16:36:40', null, '4', null, '279', '279', '299', '18盒', '蒙牛', '2013-11-02 16:00:00', '企业送检', '分光光度计 原子荧光光谱仪', '《含乳饮料卫生标准》（GB 11673-2003）', '所检指标符合《食品安全国家标准   食品中真菌毒素限量》（GB 2761-2011）、《含乳饮料卫生标准》（GB 11673-2003）、《食品中污染物限量》（GB 2762-2005）标准要求', '安沙', '谢峰', '李荣华', '/publish/FZ20137508/FZ20137508-1.pdf', '38');
INSERT INTO `test_result` VALUES ('195', '合格', '0', '2013-11-08 16:36:44', null, '4', null, '371', '382', '300', '10', '味醇原', '2013-11-06 16:00:00', '企业送检', '分光光度计 ', 'GB 18186', '符合GB 18186标准', '安沙', '谢峰', '李荣华', '/publish/FZ20137508/FZ20137508-2.pdf', '38');
INSERT INTO `test_result` VALUES ('196', '符合要求', '0', '2013-11-03 16:36:40', null, '4', null, '279', '279', '301', '10盒', '蒙牛', '2013-11-03 00:00:00', '企业送检', '原子荧光光谱仪', '《含乳饮料卫生标准》（GB 11673-2003）', '所检指标符合《食品安全国家标准   食品中真菌毒素限量》（GB 2761-2011）、《含乳饮料卫生标准》（GB 11673-2003）、《食品中污染物限量》（GB 2762-2005）标准要求', '高杰', '李荣华', '李荣华', '/publish/FZ20137509/FZ20137509-1.pdf', '38');
INSERT INTO `test_result` VALUES ('197', '合格', '0', '2013-11-07 16:36:44', null, '4', null, '371', '382', '302', '10', '味醇园', '2013-11-07 00:00:00', '企业送检', '分光光度计，原子荧光光谱仪', 'GB 18186', '符合GB 18186标准的要求', '谢峰', '高杰', '李荣华', '/publish/FZ20137509/FZ20137509-2.pdf', '38');
INSERT INTO `test_result` VALUES ('198', '合格', '0', '2013-11-07 17:59:35', null, '3', null, '371', '382', '302', '10', '味醇园', '2013-11-07 16:00:00', '政府抽检', '原子荧光光谱仪', 'GB 18186', '符合GB 18186标准', '谢峰', '李荣华', '安沙', '/publish/GZ20131203/FZ201312031.pdf', null);
INSERT INTO `test_result` VALUES ('199', '符合GB 18186标准', '0', '2013-11-08 17:59:36', null, '3', null, '371', '382', '300', '15', '味醇园', '2013-11-06 16:00:00', '政府抽检', '原子荧光光谱仪', 'GB 18186', '合格 符合GB 18186标准', '谢峰', '安沙', '李荣华', '/publish/GZ20131203/FZ20131203.pdf', null);
INSERT INTO `test_result` VALUES ('200', '合格 符合《含乳饮料卫生标准》（GB 11673-2003）标准', '0', '2013-11-02 19:29:14', null, '3', null, '279', '279', '250', '10', '蒙牛', '2013-11-02 16:00:00', '政府抽检', '原子荧光光谱仪', '《含乳饮料卫生标准》（GB 11673-2003） 《食品中污染物限量》（GB 2762-2005） 《食品安全国家标准   食品中真菌毒素限量》（GB 2761-2011）', '  所检指标符合《食品安全国家标准   食品中真菌毒素限量》（GB 2761-2011）、《含乳饮料卫生标准》（GB 11673-2003）标准要求', '谢峰', '李荣华', '安沙', '/publish/GZMN1206/GZMN1206-2.pdf', null);
INSERT INTO `test_result` VALUES ('201', '合格', '0', '2013-11-03 19:29:15', null, '3', null, '279', '279', '301', '11', '蒙牛', '2013-11-03 16:00:00', '政府抽检', '原子荧光光谱仪', '《含乳饮料卫生标准》（GB 11673-2003） 《食品中污染物限量》（GB 2762-2005） 《食品安全国家标准   食品中真菌毒素限量》（GB 2761-2011）', '  所检指标符合《食品安全国家标准   食品中真菌毒素限量》（GB 2761-2011）、《含乳饮料卫生标准》（GB 11673-2003）标准要求', '安沙', '谢峰', '李荣华', '/publish/GZMN1206/GZMN1206-1.pdf', null);
INSERT INTO `test_result` VALUES ('202', '', '0', '2013-12-07 14:43:25', null, '2', null, '10', '10', '303', '2瓶', '包装 10-135', '2013-12-07 14:43:26', '企业自检', '', 'GB18187 QJ01.05.', '符合GB18187.QJ01.05标准', '喻爱难', '徐立秀', '邹利 陈敏 龙春蓉 罗晓青', '/publish/zh2013-11-C-38/zh2013-11-C-38-1.pdf', null);
INSERT INTO `test_result` VALUES ('206', '', '0', '2013-12-08 13:44:14', null, '4', null, '10090', '10094', '307', '', '上海', '2013-08-12 08:00:00', '企业送检', '', '', '1', '', '', '', '/publish/310700113185712/310700113185712-1-report.jpg', '38');
INSERT INTO `test_result` VALUES ('207', '', '0', '2013-12-08 13:44:27', null, '4', null, '10090', '10094', '307', '', '上海', '2013-08-12 08:00:00', '企业送检', '', '', '1', '', '', '', '/publish/310700113185712/310700113185712-1-report.jpg', '38');
INSERT INTO `test_result` VALUES ('208', '', '0', '2013-12-08 13:44:22', null, '4', null, '10091', '10095', '308', '', '中国', '2012-05-08 08:00:00', '企业送检', '', '', '1', '', '', '', '/publish/F000005996/F000005996-1-report.jpg', '38');
INSERT INTO `test_result` VALUES ('209', '', '0', '2013-12-08 14:17:48', null, '4', null, '10091', '10095', '309', '', '盐田', '2013-04-08 08:00:00', '企业送检', '', '', '1', '', '', '', '/publish/470600113016799/470600113016799-1-report.jpg', '38');
INSERT INTO `test_result` VALUES ('210', '', '0', '2013-12-08 14:17:53', null, '4', null, '10092', '10099', '310', '', '广州', '2013-05-14 08:00:00', '企业送检', '', '', '1', '', '', '', '/publish/440100113008827-1/440100113008827-1-1-report.jpg', '38');
INSERT INTO `test_result` VALUES ('211', '', '0', '2013-12-08 14:19:16', null, '4', null, '10091', '10095', '308', '', '中国', '2012-05-08 08:00:00', '企业送检', '', '', '1', '', '', '', '/publish/F000005996/F000005996-1-report.jpg', '38');
INSERT INTO `test_result` VALUES ('212', '', '0', '2013-12-08 14:19:41', null, '4', null, '10091', '10095', '308', '', '中国', '2012-05-08 08:00:00', '企业送检', '', '', '1', '', '', '', '/publish/F000005996/F000005996-1-report.jpg', '38');
INSERT INTO `test_result` VALUES ('213', '', '0', '2013-12-08 14:50:23', null, '4', null, '10092', '10099', '311', '', '广州', '2013-08-08 08:00:00', '企业送检', '', '', '1', '', '', '', '/publish/440100113017326-1/440100113017326-1-1-report.jpg', '38');

-- ----------------------------
-- Table structure for `t_batch_sample`
-- ----------------------------
DROP TABLE IF EXISTS `t_batch_sample`;
CREATE TABLE `t_batch_sample` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` text COLLATE utf8_unicode_ci NOT NULL,
  `ABSTRACT_SAMPLE_ID` int(10) unsigned NOT NULL,
  `SHEET_ID` int(10) unsigned NOT NULL,
  `DATA_COVERAGE_ID` int(10) unsigned NOT NULL,
  `PRODUCT_ID` int(10) unsigned NOT NULL,
  `BACKUP_STORAGE_PLACE_ID` int(10) unsigned NOT NULL,
  `DISTRICT_ID` int(10) unsigned NOT NULL,
  `VALUE_UNIT_ID` int(10) unsigned NOT NULL,
  `PROVIDER_ID` int(10) unsigned NOT NULL,
  `ICBUREAU_PRODUCT_ID` int(10) unsigned NOT NULL,
  `VOLUME_FOR_TEST` text COLLATE utf8_unicode_ci NOT NULL,
  `VOLUME_FOR_BACKUP` text COLLATE utf8_unicode_ci NOT NULL,
  `CLAIMED_BRAND` text COLLATE utf8_unicode_ci NOT NULL,
  `LEVEL` text COLLATE utf8_unicode_ci NOT NULL,
  `TYPE` text COLLATE utf8_unicode_ci NOT NULL,
  `FORMAT` text COLLATE utf8_unicode_ci NOT NULL,
  `REGULARITY` text COLLATE utf8_unicode_ci NOT NULL,
  `BATCH_SERIAL_NO` text COLLATE utf8_unicode_ci NOT NULL,
  `SERIAL` text COLLATE utf8_unicode_ci NOT NULL,
  `STATUS` text COLLATE utf8_unicode_ci NOT NULL,
  `PRICE` text COLLATE utf8_unicode_ci NOT NULL,
  `INVENTORY_VOLUME` text COLLATE utf8_unicode_ci NOT NULL,
  `PURCHASE_VOLUME` text COLLATE utf8_unicode_ci NOT NULL,
  `BARCODE` text COLLATE utf8_unicode_ci NOT NULL,
  `NOTES` text COLLATE utf8_unicode_ci NOT NULL,
  `SAMPLE_METHOD_ID` int(10) unsigned DEFAULT NULL,
  `PRODUCTION_DATE` text COLLATE utf8_unicode_ci NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `DATA` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ABSTRACT_SAMPLE_ID` (`ABSTRACT_SAMPLE_ID`),
  KEY `SHEET_ID` (`SHEET_ID`),
  KEY `DATA_COVERAGE_ID` (`DATA_COVERAGE_ID`),
  KEY `PRODUCT_ID` (`PRODUCT_ID`),
  KEY `BACKUP_STORAGE_PLACE_ID` (`BACKUP_STORAGE_PLACE_ID`),
  KEY `DISTRICT_ID` (`DISTRICT_ID`),
  KEY `VALUE_UNIT_ID` (`VALUE_UNIT_ID`),
  KEY `PROVIDER_ID` (`PROVIDER_ID`),
  KEY `ICBUREAU_PRODUCT_ID` (`ICBUREAU_PRODUCT_ID`),
  KEY `SAMPLE_METHOD_ID` (`SAMPLE_METHOD_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1418 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_batch_sample
-- ----------------------------
INSERT INTO `t_batch_sample` VALUES ('3', '九洞天观音泉饮用天然泉水', '4', '3', '7', '532', '4', '10926', '237', '2', '292', '1', '1', '九洞天', '', '', '18.9L/桶', 'DB/T434-2007', '2012年5月3日', 'LYSHF2012050001', '液态', '12', '30桶', '200桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('5', '松竹音饮用泉水', '6', '5', '10', '1526', '2', '9567', '237', '4', '292', '1', '1', '松竹音', '', '', '18.9L/桶', 'DB52/434', '201205041', 'LYSHE2012060001', '无色透明液体', '8.0元/桶', '30桶', '300桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('6', '饮用泉水', '7', '6', '10', '1526', '2', '9567', '237', '4', '292', '1', '1', '小飞龙', '', '', '18.9L/桶', 'GB 17323', '20120503', 'LYSHE2012060002', '无色透明液体', '10元/桶', '30桶', '100桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('7', '饮用天然泉水', '8', '7', '10', '532', '2', '9567', '237', '4', '292', '1', '1', '飞龙雨', '', '', '18.9L/桶', 'DB52/434', '20120503', 'LYSHE2012060003', '无色透明液体', '12.0元/桶', '200桶', '500桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('8', '饮用泉水', '9', '8', '10', '1526', '2', '9567', '237', '4', '292', '1', '1', '小飞龙', '', '', '18.9桶/L', 'GB17323', '20120504', 'LYSHE2012060004', '无色透明液体', '10.0元/桶', '300桶', '400桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('9', '饮用天然泉水', '10', '9', '10', '1526', '2', '9567', '237', '5', '292', '1', '1', '奇香源', '', '', '18.9L/桶', 'DB52/434', '20120504 03 097', 'LYSHE2012060005', '无色透明液体', '8.0元/桶', '30桶', '100桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('10', '饮用天然泉水', '11', '10', '10', '532', '2', '9567', '237', '5', '292', '1', '1', '贵醇源', '', '', '18.9L/桶', 'DB52/434', '20120505', 'LYSHE12060006', '无色透明液体', '10.0', '30桶', '100桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('11', '饮用天然泉水', '12', '11', '10', '1526', '2', '9567', '237', '6', '292', '1', '1', '8000年', '', '', '18.9L/桶', 'DB52/434', '2012年04月30日', 'LYSHE12060007', '无色透明液体', '9.0', '20桶', '200桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('12', '饮用天然泉水', '13', '12', '10', '1526', '2', '9567', '237', '5', '292', '1', '1', '贵醇源', '', '', '18.9L/桶', 'DB52/434', '20120503', 'LYSH12060008', '无色透明液体', '10.0元/桶', '60桶', '200桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('13', '饮用天然泉水', '14', '13', '10', '1526', '2', '9567', '237', '7', '292', '1', '1', '杜鹃山泉', '', '', '18.9L/桶', 'DB52/434', '12/0505', 'LYSH12060009', '无色透明液体', '9.0元/桶', '60桶', '100桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('14', '饮用天然泉水', '15', '14', '10', '1526', '2', '9567', '237', '5', '292', '1', '1', '贵醇源', '', '', '18.9L/桶', 'DB 52/434', '201205050398', 'LYSHE2012060010', '无色透明液体', '10.0元/瓶', '100桶', '100桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('15', '鲜橙多', '16', '15', '8', '288', '4', '7363', '217', '8', '298', '2', '4', '甜甜雪', '', '', '280ml/瓶', 'GB/T21733', '2012.04.18', 'LYLYD2012060001', '液体', '0.5', '200件', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('16', '小可乐', '17', '16', '8', '288', '4', '7363', '217', '8', '298', '1', '1', '甜甜雪', '', '', '280ml/瓶', 'GB/T21733', '2012.4.20', 'LYLYD2012060002', '液体', '0.5', '30件', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('17', '饮用山泉水', '18', '17', '8', '1526', '4', '7363', '237', '14', '292', '1', '1', '翠绿', '', '', '18.9L/桶', 'DB43/434', '20120502', 'LYSHD2012060001', '无色液体', '10', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('18', '雪碧', '19', '18', '8', '288', '4', '7363', '217', '10', '299', '4', '2', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '2012.1.6', 'LYLYD2012060003', '液体', '3.0', '2件', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('19', '百事可乐', '20', '19', '8', '288', '4', '7363', '217', '11', '299', '4', '2', '百事', '', '', '500毫升/瓶', 'GB/T10792', '2012.03.29C', 'LYLYD2012060004', '液体', '3.0', '3件', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('20', '非常可乐', '21', '20', '8', '288', '4', '7363', '217', '12', '298', '2', '4', '娃哈哈', '', '', '500ml/瓶', 'GB/T10792', '2012.01.31CD', 'LYLYD2012060005', '液体', '3.0', '10件', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('21', '饮用泉水', '22', '21', '8', '1526', '1', '7363', '237', '13', '292', '1', '1', '小飞龙', '', '', '18.9L/桶', 'DN52/434', '20120427', 'LYSHD2012060002', '无色液体', '9.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('22', '冰爽柠檬味汽水', '23', '22', '8', '288', '1', '7363', '217', '11', '299', '4', '2', '百事', '', '', '500毫升/瓶', 'GB/T10792', '2011.10.20A', 'LYLYD2012060006', '液体', '2.8', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('23', '饮用纯净水', '24', '23', '8', '532', '4', '7363', '237', '14', '293', '1', '1', '天源翔', '', '', '18.9L/桶', 'GB17323', '20120501', 'LYSHD2012060003', '无色液体', '6', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('24', '乡妹子麻辣鸡腿', '25', '24', '10', '158', '4', '9567', '141', '15', '212', '2', '1', '乡妹子', '', '', '88克/包', 'GB/T23586-2009', '2012年03月13日', 'SSQTE2012050001', '暗红色固体', '8.0元/包', '9包', '15包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('25', '乡音普洱茶（生沱）', '26', '25', '10', '308', '1', '9567', '141', '16', '685', '1', '', '乡音', '', '', '200克/包', 'DB53/103', '2008年1月18日', 'NCCYE201205001', '颗粒', '21元/包', '6包', '15包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('26', '饮用天然泉水', '27', '26', '7', '532', '4', '10926', '237', '17', '292', '1', '1', '观音缘', '', '', '18.9L/桶', 'DB52/434-2007', '2012年5月8日', 'LYSHF2012050002', '液态', '10', '60桶', '120桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('27', '饮用天然泉水', '28', '27', '8', '532', '4', '7363', '237', '18', '292', '1', '1', '苗苗纯', '', '', '18.9L/桶', 'DB52/434-2007', '2012年5月5日', 'LYSHD2012060004', '无色液体', '7', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('28', '香酥猪蹄', '29', '28', '10', '1260', '2', '9567', '141', '19', '212', '2', '1', '香味佬', '', '', '130克/包', 'GB/T23586-2009', '2012年03月03日', 'SSQTE2012050002', '暗红色固体', '6.8元/包', '4包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('29', '九洞天观音泉饮用天然泉水', '30', '29', '7', '532', '4', '10926', '237', '221', '292', '1', '1', '九洞天', '', '', '18.9L/桶', 'DB52/434-2007', '2012年5月7日', 'LYSHF2012050003', '液态', '10', '260桶', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('30', '饮用泉水', '31', '30', '8', '1526', '4', '7363', '237', '18', '292', '1', '1', '蘑菇石', '', '', '18.9L/桶', 'DB52/434', '2012年5月2日', 'LYSHD2012060005', '无色液体', '7.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('31', '百花林泉优质山泉水', '32', '31', '7', '532', '4', '10926', '237', '21', '292', '1', '1', '百花林泉', '', '', '18.9L/桶', 'DB52/434', '2012年04月30日', 'LYSHF2012050004', '液态', '10', '80桶', '80桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('32', '泡椒凤爪（辐照食品）', '33', '32', '10', '1261', '4', '9567', '141', '22', '218', '2', '1', '永健', '', '', '90克/包', 'DBS50/004', '2012/03/18BM', 'SSQTE2012050003', '白色固体', '4.6元/包', '6包', '15包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('33', '山山山泉水', '34', '33', '7', '1526', '4', '10926', '237', '23', '292', '1', '1', 'SSS', '', '', '18.9L/桶', 'DB52/434', '2012年4月30日', 'LYSHF2012050005', '液态', '17', '', '200桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('34', '碧螺春', '35', '34', '10', '308', '1', '9567', '141', '24', '685', '1', '', '银溪', '特级', '', '200克/包', 'GB/T14456.2', '2012年03月19日', 'NCCYE2012050002', '颗粒', '12元/包', '10包', '15包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('35', '雪碧冰爽柠檬味汽水', '36', '35', '8', '288', '4', '7363', '217', '10', '299', '4', '2', 'Sprite', '', '', '500毫升/瓶', 'GB/T10792', '20120131', 'LYLYD2012060007', '液体', '3.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('36', '金沙饮用天然泉水', '37', '36', '7', '1526', '4', '10926', '237', '222', '292', '1', '1', '', '', '', '18.9L/桶', 'DB52/434', '2012年5月5日', 'LYSHF2012050006', '液态', '36', '100桶', '200桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('37', '饮用天然泉水', '38', '37', '8', '1526', '4', '7363', '237', '26', '292', '1', '1', '梵净', '', '', '18.9L/桶', 'DB52/434', '2012.5.6', 'LYSHD2012060006', '无色液体', '7', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('38', '美年达橙味汽水', '39', '38', '8', '288', '4', '7363', '217', '10', '299', '4', '2', 'PEPSI', '', '', '330毫升/瓶', 'GB/T10792', '20110829D', 'LYLYD2012060008', '液体', '2.5', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('39', '康源天然饮用泉水', '40', '39', '7', '532', '4', '10926', '237', '27', '292', '1', '1', '康源', '', '', '18.9L', 'GB17323-1998', '012年5月08', 'LYSHF2012050007', '液态', '9', '50桶', '300桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('40', '盐焗脆翅', '41', '40', '10', '1261', '4', '9567', '141', '28', '212', '2', '1', '永健', '', '', '115克/包', 'GB/T23586-2009', '2012/02/05', 'SSQTE20120004', '固体', '7.80元/包', '4包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('41', '饮用天然泉水', '42', '41', '7', '1526', '4', '10926', '237', '29', '292', '1', '1', '水金豆', '', '', '18.9L/桶', 'DB52/434', '2012年5月8日', 'LYSHF2012050008', '液态', '10', '500桶', '500桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('42', '耿马银钩绿茶', '43', '42', '10', '308', '1', '9567', '141', '30', '685', '2', '', '', '特级', '', '180克/包', 'GB/T14456.2-2008', '2011年12月7日', 'NCCYE2012050003', '颗粒', '12.8元/包', '8包', '15包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('43', '野山椒凤爪', '44', '43', '7', '1261', '1', '10926', '141', '31', '231', '3', '2', '泡王', '', '', '50g/包', 'GB/T23586-2009', '2012年4月15日', 'SSQTF2012050002', '固态', '1.00', '1件', '5件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('44', '东明思饮用天然泉水', '45', '44', '7', '1526', '4', '10926', '237', '32', '292', '1', '1', '东明思', '', '', '18.9L/桶', 'DB52/434', '2012年5月3', 'LYSHF2012050009', '液态', '12', '310桶', '410桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('45', '香辣猪蹄', '46', '45', '7', '1260', '4', '10926', '145', '33', '212', '3', '2', '好呷鬼', '', '', '135克/袋', 'Q/ABRB001', '2012.0301', 'SSQTF2012050003', '固态', '6.8', '8袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('46', '饮用天然山泉水', '47', '46', '7', '532', '4', '10926', '237', '34', '292', '1', '1', '野人谷', '', '', '18.9L/桶', 'DB52/434', '201年5月3日', 'LYSHF2012050010', '液态', '35', '300桶', '300桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('47', '冰爽柠檬味汽水', '48', '47', '8', '288', '4', '7363', '217', '11', '299', '4', '2', 'PEPSI', '', '', '500毫升/瓶', 'GB/T10792', '20110906B', 'LYLYD2012060009', '液体', '2.8', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('48', '香酥猪蹄', '49', '48', '7', '1260', '4', '10926', '145', '35', '212', '3', '2', '清合园', '', '', '140克/袋', 'GB/T23586', '2012/03/14', 'SSQTF2012050004', '固态', '9.50', '198包', '24包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('49', '可口可乐汽水', '50', '49', '8', '288', '4', '7363', '217', '10', '299', '4', '2', 'Coke', '', '', '500毫升/瓶', 'GB/T10792', '20111130LS1', 'LYLYD2012060010', '液体', '3.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('50', '都匀毛尖', '51', '50', '9', '308', '4', '18578', '193', '36', '675', '200', '0', '黔匀', '', '', '200克', 'DB52/433-2001', '', 'NCCYJ2012060001', '叶片状', '160', '50斤', '100斤', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('51', '美年达葡萄味汽水', '52', '51', '8', '288', '4', '7363', '217', '11', '299', '4', '2', 'PEPSI', '', '', '500毫升/瓶', 'GB/T10792', '20120229', 'LYLYD2012060011', '液体', '3.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('52', '香酥猪蹄', '53', '52', '7', '1260', '4', '10926', '145', '37', '212', '3', '2', '清味园', '', '', '140克/袋', 'GB/T23586-2009', '2012/04/08', 'SSQTF2012050005', '固态', '7.8', '20袋', '50袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('53', '谷雨级毛尖', '54', '53', '9', '308', '4', '18578', '193', '36', '675', '200', '0', '', '', '', '散装', '', '2012.3.8', 'NCCYJ2012060002', '叶片状', '180', '20斤', '50斤', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('54', '美年达橙味汽水', '55', '54', '8', '288', '4', '7363', '217', '11', '299', '4', '2', 'PEPSI', '', '', '500ml/瓶', 'GB/T10792', '20120225B', 'LYLYD2012060012', '液体', '3.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('55', '脆皮鸡腿', '56', '55', '7', '1261', '4', '10926', '145', '37', '212', '3', '2', '杜威盛', '', '', '140克/袋', 'GB12586', '2012/04/08', 'SSQTF2012050006', '固态', '7.50', '25包', '50包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('56', '香酥鸡腿', '57', '56', '7', '1261', '4', '10926', '145', '28', '212', '3', '2', '永健', '', '', '100克/袋', 'GB/T23586', '2012/03/12', 'SSQTF2012050007', '固态', '9.5', '3袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('57', '贵定雪芽茶', '58', '57', '9', '308', '4', '18578', '193', '36', '675', '200', '0', '', '', '', '散装', '', '2012.3.12', 'NCCYJ2012060003', '叶片状', '450', '30斤', '50斤', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('58', '排抓', '59', '58', '7', '1261', '4', '10926', '145', '15', '212', '3', '2', '乡妹子', '', '', '135克/袋', 'GB/T23586-2009', '2012年02月05日', 'SSQTF2012050008', '固态', '6.5', '26袋', '1240袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('59', '香酥猪蹄', '60', '59', '7', '1260', '4', '10926', '141', '19', '212', '3', '2', '香味佬', '', '', '13克/包', 'GB/T23586-2009', '2012年03月03', 'SSQTF2012050009', '固态', '6.5', '5包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('60', '都匀毛尖茶（一级）', '61', '60', '9', '308', '4', '18578', '193', '36', '675', '200', '0', '', '', '', '散装', '', '2012.4.6', 'NCCYJ2012060004', '叶片状', '580', '20斤', '40斤', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('61', '香辣猪蹄', '62', '61', '7', '1260', '4', '10926', '145', '38', '212', '3', '2', '香滋香味', '', '', '140g/袋', 'GB/T23586', '2012年04月03日', 'SSQTF2012050010', '固态', '6.50', '15袋', '5袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('62', '清明茶', '63', '62', '9', '308', '4', '18578', '193', '36', '675', '200', '0', '', '', '', '散装', '', '2012.4.9', 'NCCYJ2012060005', '叶片状', '300', '20斤', '40斤', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('63', '马蹄爽', '64', '63', '4', '288', '1', '3', '252', '39', '299', '2', '1', '杨协成', '', '', '300毫升/听', 'Q/GZYXC6', '20110118H', 'LYLYA2012060001', '液态', '3.5', '30件', '50件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('64', '饮用天然泉水', '65', '64', '12', '1526', '2', '1768', '237', '32', '292', '1', '1', '东明思', '', '', '18.9L/桶', 'DB52/434', '2012年5月6日', 'LYSHB2012060001', '液态', '12元', '5', '10', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('65', '精品爪王', '66', '65', '12', '1261', '4', '1768', '145', '40', '218', '3', '2', '佳香肚', '', '', '50g/袋', 'GB/T23586-2009', '2012.3.21', 'SSQTB2012060001', '固态', '1.5', '32', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('66', '陡箐山泉', '67', '66', '12', '1526', '2', '1768', '237', '32', '292', '1', '1', '东明思', '', '', '18.9L/桶', 'DB52/434', '20120505', 'LYSHB2012060002', '液态', '10元', '2', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('67', '小腿王', '68', '67', '12', '1261', '4', '1768', '145', '41', '218', '3', '2', '康赞', '', '', '40g/袋', 'GB/T23586', '2012.2.26', 'SSQTB2012060002', '固态', '1.5', '28', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('68', '美年达苹果汽水', '69', '68', '4', '288', '4', '3', '217', '42', '299', '2', '1', '美年达', '', '', '500毫升/瓶', 'Q/GZYXC6', '20120227B', 'LYLYA2012060002', '液态', '2.7', '2件（24瓶/件）', '3件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('69', '青山绿水绿茶', '70', '69', '10', '308', '1', '9567', '141', '43', '685', '2', '', '', '特级', '', '100克/包', 'GB/T14456.2-2008', '2012年02月8日', 'NCCYE2012050004', '固体', '12元/包', '6包', '15包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('70', '碧螺春', '71', '70', '12', '308', '1', '1768', '145', '44', '676', '1', '', '南岛河', '一级', '', '180克/袋', 'GB/T14456-1943', '2012.01.02', 'NCCYB2012060001', '固体', '12', '2', '5', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('71', '香酥猪蹄', '72', '71', '12', '158', '4', '1768', '145', '220', '212', '3', '2', '万家欢', '', '', '150g/袋', 'Q/WJH0014S', '2012.3.13', 'SSQTB2012060003', '固态', '10.5', '106', '120', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('73', '可口可乐', '74', '73', '7', '288', '4', '10926', '217', '47', '299', '4', '2', 'Coke', '', '', '500毫升/瓶', 'GB/T10792', '20120229', 'LYLYF2012050001', '液态', '2.9', '9瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('74', '雪碧', '75', '74', '4', '288', '4', '3', '217', '48', '299', '2', '1', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120229', 'LYLYA2012060003', '液态', '3.0', '30件', '50件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('75', '香酥猪蹄', '76', '75', '12', '1260', '4', '1768', '145', '49', '212', '3', '2', '香味佬', '', '', '130g/袋', 'GB/T23586-2009', '2012.3.3', 'SSQTB2012060004', '固态', '7.8', '105', '170', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('76', '百事可乐', '77', '76', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'Aape', '', '', '500毫升/瓶', 'GB/T10792', '20120414', 'LYLYF2012050002', '', '2.9', '24瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('77', '香卤鸡爪', '78', '77', '12', '1261', '4', '1768', '145', '3', '218', '3', '2', '永健', '', '', '120g/袋；', 'DB50/T322', '20120321', 'SSQTB2012060005', '固态', '7.8', '67', '150', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('78', '高山雲雾茶', '79', '78', '10', '308', '1', '9567', '141', '50', '685', '3', '', '老家人', '一级', '', '80克/包', 'GB/T14456.2-2008', '2012年03月18日', 'NCCYE2012050005', '颗粒', '3元/包', '20包', '30包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('79', '冰点水', '80', '79', '12', '1525', '4', '1768', '217', '51', '293', '1', '1', '冰点', '', '', '1.5L/瓶', 'GB17323', '2012.3.11', 'LYSHB2012060003', '液态', '0.8元/瓶', '121', '480', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('80', '7喜', '81', '80', '4', '288', '4', '3', '217', '11', '299', '2', '1', '百事', '', '', '500毫升/瓶', 'GB/T10792', '2012.02.23', 'LYLYA2012060004', '液态', '2.7', '5件', '8件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('81', '盐鸡焗腿', '82', '81', '12', '1261', '4', '1768', '145', '3', '212', '3', '2', '永健', '', '', '100g/袋', 'GB/T23586-2009', '2012.2.21', 'SSQTB2012060006', '固态', '7.8', '67', '150', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('82', '零度可口可乐汽水', '83', '82', '4', '288', '4', '3', '217', '10', '299', '2', '1', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120303', 'LYLYA2012060005', '液态', '2.9', '5件', '8件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('83', '雪碧清爽柠檬味汽水', '84', '83', '7', '288', '4', '10926', '217', '48', '299', '4', '2', 'Sprite', '', '', '500ml/瓶', 'GB/T10792', '20120225', 'LYLYF2012050003', '', '2.4', '3件', '31件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('84', '北极熊', '85', '84', '12', '1525', '4', '1768', '217', '52', '293', '1', '2', '北极熊', '', '', '550mL/瓶', 'GB17323', '2011.10.4', 'LYSHB2012060004', '液态', '1.5元/瓶', '5289', '6720', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('85', '双汇王中王', '86', '85', '12', '1265', '4', '1768', '239', '53', '228', '3', '2', '双汇', '', '', '50g/支', 'GB/T20712', '20120204', 'SSQTB2012060007', '固态', '1.5', '97', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('86', '醒目菠萝味汽水', '87', '86', '9', '288', '1', '18578', '217', '10', '299', '4', '2', '醒目', '', '', '500ml/瓶', 'GB/T10792', '20120304', 'LYLYJ2012060001', '液体', '2.6元', '120瓶', '360瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('87', '百事可乐', '88', '87', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'BY*ABATHINGAPE', '', '', '500ml/瓶', 'GB/T10792', '20120218C', 'LYLYF2012050004', '', '2.4', '29件', '31件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('88', '百事可乐汽水', '89', '88', '4', '288', '4', '3', '217', '11', '299', '2', '1', '百事', '', '', '500毫升/瓶', 'GB/T10792', '20120414B', 'LYLYA2012060006', '液态', '2.7', '3件', '5件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('89', '贵隆绿茶', '90', '89', '10', '308', '1', '9567', '141', '54', '685', '1', '', '见图标', '', '', '250克/包', 'GB/T14456-1993', '2011年12月', 'NCCYE2012050006', '固体', '10元/包', '3包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('90', '娃哈哈', '91', '90', '12', '1525', '4', '1768', '217', '55', '293', '1', '2', '娃哈哈', '', '', '596mL/瓶', 'GB17323', '2011.12.1', 'LYSHB2012060005', '液态', '2.5元/瓶', '1733', '4560', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('91', '康师傅', '92', '91', '12', '1526', '4', '1768', '217', '56', '294', '1', '2', '康师傅', '', '', '550mL/瓶', 'Q/14A0240S', '2012.3.24', 'LYSHB2012060006', '液态', '1.5元/瓶', '5401', '13176', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('92', '乡巴佬鸡翅王', '93', '92', '12', '1261', '4', '1768', '145', '57', '218', '3', '2', '乡巴佬', '', '', '75g/袋', 'GB/T23586', '20120117', 'SSQTB2012060008', '固态', '7.8', '5', '10', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('93', '激浪', '94', '93', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'MountamDew', '', '', '330毫升/瓶', 'GB/T10792', '20111125D', 'LYLYF2012050005', '液态', '2.5', '16瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('94', '云雾绿茶', '95', '94', '10', '308', '1', '9567', '141', '58', '685', '1', '', '普天', '', '', '250克/包', 'GB9697-88', '2012年3月', 'NCCYE2012050007', '固体', '20元/包', '7包', '15包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('95', '可口可乐汽水', '96', '95', '4', '288', '4', '3', '217', '59', '299', '2', '1', '可口可乐', '', '', '300ml/瓶', 'GB/T10792', '20120403', 'LYLYA2012060007', '液态', '2.0', '41瓶', '72瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('96', '美年达葡萄味汽水', '97', '96', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'MiRiNDA', '', '', '500毫升/瓶', 'GB/T10792', '20120228', 'LYLYF2012050006', '液态', '3.0', '72瓶', '420瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('97', '饮用天然山泉水', '98', '97', '12', '1526', '4', '1768', '237', '60', '291', '1', '1', '铜巩山', '', '', '18.9L', 'DB52/434', '20120501', 'LYSHB2012060007', '液态', '8元/桶', '221', '350', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('98', '糯米香茶', '99', '98', '10', '308', '1', '9567', '141', '61', '685', '3', '', '见图标', '一级', '', '80克/包', 'GB/T14456-93', '2012年2月', 'NCCYE2012050008', '固体', '3元/包', '20包', '30包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('99', '香辣腿', '100', '99', '12', '1261', '4', '1768', '145', '62', '218', '3', '2', '香仔', '', '', '110g/袋', 'GB/T23586-2009', '20120101', 'SSQTB2012060009', '固态', '6.9', '11', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('100', '云雾绿茶', '101', '100', '10', '308', '1', '9567', '141', '63', '685', '1', '', '普江', '', '', '250克/包', 'GB/T14456', '2012年3月', 'NCCYE201205009', '固体', '10元/包', '10包', '20包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('101', '饮用天然泉水', '102', '101', '12', '1526', '4', '1768', '237', '64', '292', '1', '1', '飞龙雨', '', '', '18.9L/桶', 'DB52/434', '20120501', 'LYSHB2012060008', '液态', '12元/桶', '100', '300', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('102', '美年达葡萄味汽水', '103', '102', '4', '288', '4', '3', '252', '11', '299', '2', '1', '美年达', '', '', '330毫升/听', 'GB/T10792', '20120224', 'LYLYA2012060008', '液态', '2.0', '66罐', '148罐', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('103', '香酥猪蹄', '104', '103', '12', '1260', '4', '1768', '145', '65', '212', '3', '2', '潮汕好味来', '', '', '150g/袋', 'GB/T23586', 'HWL2011/12/02', 'SSQTB2012060010', '固态', '7.5', '10', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('104', '雪碧', '105', '104', '9', '288', '1', '18578', '252', '10', '299', '4', '2', '雪碧', '', '', '330ml/听', 'GB/T10792', '20120118', 'LYLYJ2012060002', '液体', '2.5元', '20听', '24听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('105', '盐焗鸡腿', '106', '105', '7', '1261', '4', '10926', '141', '3', '212', '2', '1', '永健', '', '', '100克/包', 'GB/T23586-2009', '2012/03/18', 'SSQTF2012050001', '', '10.9', '9包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('106', '百事可乐', '107', '106', '4', '288', '4', '3', '252', '11', '299', '2', '1', '百事', '', '', '330毫升/听', 'GB/T10792', '2012.02.23', 'LYLYA2012060009', '液态', '2.0', '38听', '48听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('107', '清明绿茶', '108', '107', '10', '308', '1', '9567', '141', '66', '685', '1', '', '', '', '', '250克/包', 'GB9697-88', '2012年3月', 'NCCYE2012050010', '固体', '8元/包', '5包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('108', '饮用纯净水', '109', '108', '12', '532', '4', '1768', '237', '224', '293', '1', '1', '盘龍山', '', '', '18.9L', '', '2012年5月1日', 'LYSHB2012060009', '液态', '8元/桶', '40', '96', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('109', '百事可乐', '110', '109', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'Aape', '', '', '500毫升/瓶', 'GB/T10792', '20120316C', 'LYLYF2012050007', '液态', '3.0', '150瓶', '360瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('110', '雪碧清爽柠檬', '111', '110', '4', '288', '4', '3', '252', '10', '299', '2', '1', '可口可乐', '', '', '330毫升/听', 'GB/T10792', '20120412', 'LYLYA2012060010', '液态', '2.3', '43听', '48听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('111', '露珠茶', '112', '111', '12', '308', '1', '1768', '145', '68', '685', '1', '', '和兴隆', '特级', '', '200g/袋', 'GB/T14456.2-2008', '', 'NCCYB2012060002', '固态', '25.0', '3', '5', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('112', '饮用天然泉水', '113', '112', '12', '1526', '4', '1768', '237', '64', '292', '1', '1', '飞龙雨', '', '', '18.9L/桶', 'DB52/434', '20120428', 'LYSHB2012060010', '液态', '12元/桶', '250', '450', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('113', '芬达葡萄味汽水', '114', '113', '4', '288', '4', '3', '252', '10', '299', '2', '1', '芬达', '', '', '330毫升/听', 'GB/T10792', '20110918CS', 'LYLYA2012060011', '液态', '2.10', '43听', '48听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('115', '菠萝味汽水', '116', '115', '4', '288', '4', '3', '252', '10', '299', '2', '1', '醒目', '', '', '330毫升/听', 'GB/T10792', '20120115', 'LYLYA2012060012', '液态', '2.10', '14听', '24听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('117', '雪碧', '118', '117', '4', '288', '1', '3', '217', '59', '299', '2', '1', '雪碧', '', '', '300毫升/瓶', 'GB/T10792', '20120317', 'LYLYA2012060013', '液态', '2.0', '11瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('118', '乡妹子香酥鸡腿', '119', '118', '9', '158', '1', '18578', '145', '15', '212', '3', '2', '乡妹子', '', '', '88克/袋', 'GB/T23586-2009', '2012年04月14日', 'SSQTJ2012060001', '固态', '7.80', '20袋', '50袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('119', '雪碧', '120', '119', '4', '288', '4', '3', '252', '10', '299', '2', '1', '可口可乐', '', '', '330毫升/听', 'GB/T10792', '20111029', 'LYLYA2012060014', '液态', '2.0', '11听', '24听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('120', '百事可乐', '121', '120', '4', '288', '4', '3', '252', '11', '299', '2', '1', '百事', '', '', '330毫升/听', 'GB/T10792', '20120223', 'LYLYA2012060015', '液态', '2.0', '14听', '24听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('121', '可口可乐汽水', '122', '121', '4', '288', '4', '3', '217', '59', '299', '2', '1', '可口可乐', '', '', '330毫升/瓶', 'GB/T10792', '20120321', 'LYLYA2012060016', '液态', '2.0', '10瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('122', '可口可乐', '123', '122', '4', '288', '1', '3', '217', '10', '299', '2', '1', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120302', 'LYLYA2012060017', '液态', '2.0', '24瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('123', '碧螺春茶', '124', '123', '12', '308', '1', '1768', '145', '71', '685', '1', '0', '龙马江', '特级', '', '180g/袋', 'GB/T14456.2-2008', '20120101', 'NCCYB2012060003', '固态', '28.0', '26', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('124', '百事可乐', '125', '124', '4', '288', '4', '3', '217', '11', '299', '2', '1', '百事', '', '', '500毫升/瓶', 'GB/T10792', '20120228', 'LYLYA2012060018', '液态', '3.0', '24瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('125', '雪碧', '126', '125', '12', '288', '4', '1768', '217', '47', '299', '4', '2', '可口可乐', '', '', '500mL/瓶', 'GB/T10792', '201203021656HBA07', 'LYLYB2012060001', '液态', '3元/瓶', '17', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('127', '可口可乐汽水', '128', '127', '12', '288', '4', '1768', '217', '10', '299', '4', '2', '可口可乐', '', '', '500mL/瓶', 'GB/T10792', '20120128102408081', 'LYLYB2012060002', '液态', '3元/瓶', '21', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('128', '百事可乐', '129', '128', '12', '288', '4', '1768', '217', '11', '299', '4', '2', 'PEPSI', '', '', '500mL/瓶', 'GB/T10792', '20111219C', 'LYLYB2012060003', '液态', '3元/瓶', '23', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('129', '早春茶', '130', '129', '12', '308', '1', '1768', '145', '73', '685', '1', '0', '清水芸香', '特级', '', '200g/袋', 'DB52/T447-2003', '20110401', 'NCCYB2012060004', '固态', '16.0', '15', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('130', '百事可乐', '131', '130', '10', '288', '1', '9567', '217', '11', '301', '2', '1', '图标见瓶身', '', '', '500毫升/瓶', 'GB/T10792', '20120227C', 'LYLYE2012050001', '深褐色液体', '3.00元/瓶', '15瓶', '75瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('131', '非常柠檬', '132', '131', '12', '288', '4', '1768', '217', '74', '299', '4', '2', '娃哈哈', '', '', '500mL/瓶', 'GB/T10792', '201202237203BL', 'LYLYB2012060004', '液态', '3元/瓶', '23', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('132', '非常可乐', '133', '132', '12', '288', '4', '1768', '217', '74', '299', '4', '2', '娃哈哈', '', '', '500mL/瓶', 'GB/T10792', '201203207114BL', 'LYLYB2012060005', '液态', '3元/瓶', '26', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('133', '飘香碧螺春茶', '134', '133', '12', '308', '1', '1768', '145', '71', '685', '1', '0', '龙马江', '特级', '', '200g/袋', 'GB/T14456.2-2008', '2011.12.01', 'NCCYB2012060005', '固态', '12.0', '20', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('134', '百事可乐', '135', '134', '10', '288', '4', '9567', '217', '11', '301', '2', '1', '', '', '', '330毫升/瓶', 'GB/T10792', '20120130', 'LYLYE2012050002', '深褐色液体', '2.50元/瓶', '60瓶', '120瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('135', '雪碧', '136', '135', '12', '288', '4', '1768', '217', '10', '299', '3', '1', '雪碧', '', '', '2L/瓶', 'GB/T10792', '20111217', 'LYLYB2012060006', '液态', '3元/瓶', '6', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('136', '普洱春毛尖', '137', '136', '12', '308', '1', '1768', '145', '71', '685', '2', '', '龙马江', '特级', '', '100g/袋', 'GB/T14456.2-2008', '20120201', 'NCCYB2012060006', '固态1', '10.0', '20', '40', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('137', '非常可乐', '138', '137', '12', '288', '4', '1768', '217', '74', '299', '4', '2', '娃哈哈', '', '', '500mL/瓶', 'GB/T10792', '20120320', 'LYLYB2012060007', '液态', '2.7元/瓶', '21', '264', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('138', '飘香碧螺春茶', '139', '138', '12', '308', '1', '1768', '145', '71', '685', '1', '0', '龙马江', '特级', '', '200g/袋', 'GB/T14456.2-2008', '2011.12.01', 'NCCYB2012060007', '固态', '18.0', '6', '10', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('139', '美年达（橙味）', '140', '139', '12', '288', '4', '1768', '252', '11', '299', '4', '2', '美年达', '', '', '330mL/听', 'GB/T10792', '20110629', 'LYLYB2012060008', '液态', '2.5元/听', '0', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('140', '雪碧', '141', '140', '4', '288', '4', '3', '217', '48', '299', '2', '1', '雪碧', '', '', '500毫升/瓶', 'GB/T10792', '20120329', 'LYLYA2012060019', '液态', '2.8', '12听', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('141', '百事可乐', '142', '141', '12', '288', '4', '1768', '252', '11', '299', '4', '2', '百事', '', '', '330mL/听', 'GB/T10792', '20120211', 'LYLYB2012060009', '液态', '2.5元/听', '0', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('142', '普洱碧螺春', '143', '142', '12', '308', '1', '1768', '145', '75', '685', '1', '0', '红坊人', '特级', '', '200g/袋', 'GB/T14456.2-1993', '2011/08/15', 'NCCYB2012060008', '固态', '16.5', '3', '10', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('143', '雪碧', '144', '143', '12', '288', '4', '1768', '252', '10', '299', '4', '2', '雪碧', '', '', '330mL/听', 'GB/T10792', '20110815', 'LYLYB2012060010', '液态', '2.5元/听', '0', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('144', '露（耿马）珠', '145', '144', '12', '308', '1', '1768', '145', '75', '685', '1', '0', '红坊人', '特级', '', '200g/袋', 'GB/T14456.2-2008', '2011/08/03/', 'NCCYB2012060009', '固态', '21.0', '5', '15', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('145', '绿茶王', '146', '145', '12', '308', '1', '1768', '145', '75', '685', '1', '0', '红坊人', '特级', '', '200g/袋', 'GB/T14456.2-2008', '2012/2/13日', 'NCCYB2012060010', '固态', '18.5', '24', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('146', '7喜汽水', '147', '146', '12', '288', '4', '1768', '217', '11', '299', '4', '2', '七喜', '', '', '500mL/瓶', 'GB/T10792', '20120225A', 'LYLYB2012060011', '液态', '3元/瓶', '29', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('147', '盐脆焗翅', '148', '147', '9', '1261', '4', '18578', '145', '28', '212', '3', '2', '永健', '', '', '115克/袋', 'GB/T23586-2009', '2012/03/29', 'SSQTJ2012060002', '固态', '8元', '40袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('148', '雪碧汽水', '149', '148', '12', '288', '4', '1768', '217', '10', '299', '4', '2', '雪碧', '', '', '500mL/瓶', 'GB/T10792', '20120301', 'LYLYB2012060012', '液态', '3元/瓶', '131', '168', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('149', '乡妹子粽叶鸡排', '150', '149', '9', '1261', '4', '18578', '145', '15', '212', '3', '2', '乡妹子', '', '', '120克/袋', 'GB/T23586', '2012年03月10日', 'SSQTJ2012060003', '固态', '6元', '24袋', '12袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('150', '乡妹子长生爪', '151', '150', '9', '1261', '4', '18578', '145', '15', '212', '3', '2', '乡妹子', '', '', '80克/袋', 'GB/T23586-2009', '2012年03月18日', 'SSQTJ2012060004', '固态', '4.50', '20袋', '100袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('152', '乡妹子香辣鸭腿', '153', '152', '9', '1261', '4', '18578', '145', '15', '212', '3', '2', '乡妹子', '', '', '85克/袋', 'GB/T23586-2009', '2012年04月10日', 'SSQTJ2012060005', '固态', '4.5', '12袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('153', '可口可乐汽水', '154', '153', '10', '288', '4', '9567', '217', '10', '301', '2', '1', '图标见瓶身', '', '', '330毫升/瓶', 'GB/T10792', '20120117163238CS3', 'LYLYE2012050003', '深褐色液体', '2.50元/瓶', '30瓶', '120瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('154', '芬达橙味汽水', '155', '154', '12', '288', '4', '1768', '252', '10', '299', '4', '2', '芬达', '', '', '330mL/听', 'GB/T10792', '20120204', 'LYLYB2012060013', '液态', '2.4元/听', '20', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('155', '百事可乐', '156', '155', '9', '288', '4', '18578', '252', '11', '299', '4', '2', '百事可乐', '', '', '330毫升/听', 'GB/T10792', '20120222', 'LYLYJ2012060003', '液体', '2.5元', '30听', '48听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('156', '黑胡椒牛肉', '157', '156', '4', '1269', '4', '3', '141', '76', '225', '2', '1', '美好', '特级', '', '200克/包', 'GB/T 20711', '20120403', 'SSQTA2012060001', '固态', '17.6', '4件', '2件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('157', '雪碧清爽柠檬汽水', '158', '157', '9', '288', '4', '18578', '217', '48', '299', '4', '2', '雪碧', '', '', '500ml/瓶', 'GB/T10792', '20120525', 'LYLYJ2012060004', '液体', '3.0元', '10瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('158', '百事可乐汽水', '159', '158', '12', '288', '4', '1768', '252', '42', '299', '4', '2', '百事可乐', '', '', '330mL/听', 'GB/T10792', '20111216D', 'LYLYB2012060014', '液态', '2.4元/听', '20', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('159', '盐焗鸡排', '160', '159', '9', '1261', '4', '18578', '145', '77', '212', '3', '2', '香必啃', '', '', '110克/袋', 'GB/T23586-2009', '2010 01 10', 'SSQTJ2012060006', '固态', '6.00', '20袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('160', '雪碧汽水', '161', '160', '12', '288', '4', '1768', '252', '10', '299', '4', '2', '雪碧', '', '', '330mL/听', 'GB/T10792', '20111216CS', 'LYLYB2012060015', '液态', '2.4元/听', '20', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('161', '美好熏煮香肠', '162', '161', '4', '1266', '4', '3', '141', '78', '225', '3', '2', '美好', '普通级', '', '100克/包', 'GB/T 10279', '2012/03/29', 'SSQTA2012060002', '固态', '2.5', '2件', '1件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('162', '芬达葡萄味汽水', '163', '162', '10', '288', '4', '9567', '217', '10', '301', '2', '1', '芬达', '', '', '500毫升/瓶', 'GB/T10792', '20120215', 'LYLYE2012050004', '紫红色液体', '3.00元/瓶', '28瓶', '75瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('163', '美年达葡萄味汽水', '164', '163', '12', '288', '4', '1768', '217', '11', '299', '4', '2', '美年达', '', '', '500mL/瓶', 'GB/T10792', '20120228B', 'LYLYB2012060016', '液态', '2.9元/瓶', '21', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('164', '酱牛肉', '165', '164', '4', '1260', '4', '3', '141', '76', '225', '2', '2', '美好', '特级', '', '200克/包', 'GB/T 20711', '20120405', 'SSQTA2012060003', '固体', '17.6', '1件', '2件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('165', '可口可乐汽水', '166', '165', '9', '288', '4', '18578', '252', '10', '299', '4', '2', '可口可乐', '', '', '330毫升/听', 'GB/T10792', '20111021', 'LYLYJ2012060005', '液体', '2.5元', '15听', '24听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('166', '香酥猪蹄', '167', '166', '9', '1260', '4', '18578', '145', '79', '212', '3', '2', '香味佬', '', '', '130克/袋', 'GB/T23586-2009', '2012年03月03', 'SSQTJ2012060007', '固态', '6.00', '10袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('167', '可口可乐汽水', '168', '167', '10', '288', '4', '9567', '217', '47', '301', '2', '1', '图标见瓶身', '', '', '500毫升/瓶', 'GB/T10792', '201202291845HBA07', 'LYLYE2012050005', '深棕色液体', '2.80元/瓶', '45瓶', '75瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('168', '雪碧清爽柠檬汽水', '169', '168', '9', '288', '4', '18578', '252', '47', '299', '4', '2', '雪碧', '', '', '330ml/听', 'GB/T10792', '20120228', 'LYLYJ2012060006', '液体', '2.5元', '15听', '24听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('169', '茶叶', '170', '169', '9', '308', '4', '18578', '145', '80', '675', '1', '0', '', '', '', '', '', '', 'NCCYJ2012060006', '叶片状', '140', '6袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('170', '双汇王中王', '171', '170', '4', '1265', '4', '3', '239', '81', '225', '3', '3', '双汇', '优级', '', '105克/支', 'GB/T 20712', '20120305', 'SSQTA2012060004', '固态', '3.5', '1件', '2件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('171', '可口可乐汽水', '172', '171', '9', '288', '4', '18578', '217', '10', '299', '4', '2', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120309', 'LYLYJ2012060007', '液体', '2.6元', '24瓶', '120瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('172', '百事可乐可乐型汽水', '173', '172', '12', '288', '4', '1768', '217', '82', '299', '4', '2', '百事可乐', '', '', '500mL/瓶', 'GB/T10792', '20120317B', 'LYLYB2012060017', '液态', '3.5元/瓶', '11', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('173', '五香猪蹄', '174', '173', '9', '1260', '4', '18578', '145', '83', '212', '3', '2', '鲜八里', '', '', '125克/袋', 'GB/T23586-2009', '2011.10.18A1', 'SSQTJ2012060008', '固态', '10.50', '100袋', '200袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('174', '泡凤爪（山椒味）', '175', '174', '4', '1261', '4', '3', '141', '84', '244', '3', '2', '有友', '', '', '90克/包', 'DB50/294', '2012/04/22', 'SSQTA2012060005', '固体', '6.3', '5包', '5包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('175', '美年达橙味汽水', '176', '175', '9', '288', '4', '18578', '252', '11', '299', '4', '2', '美年达', '', '', '330ml/听', 'GB/T10792', '20120210', 'LYLYJ2012060008', '液体', '3.0元', '21听', '24听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('176', '雪碧汽水', '177', '176', '12', '288', '4', '1768', '217', '10', '299', '4', '2', '雪碧', '', '', '500mL/瓶', 'GB/T10792', '20120301233030CS1', 'LYLYB2012060018', '液态', '3元/瓶', '37', '96', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('177', '芬达橙味汽水', '178', '177', '9', '288', '4', '18578', '252', '10', '299', '4', '2', '芬达', '', '', '330毫升/听', 'GB/T10792', '20111030', 'LYLYJ2012060009', '液体', '2.2元', '100听', '200听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('178', '盐焗鸡腿', '179', '178', '4', '1261', '4', '3', '141', '85', '244', '3', '2', '逍遥派', '', '', '100克/包', 'Q/HZW0001S-2009', '20120410', 'SSQTA2012060006', '固态', '9.6', '4包', '9包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('179', '可口可乐汽水', '180', '179', '9', '288', '4', '18578', '217', '10', '299', '4', '2', '可口可乐', '', '', '300ml/瓶', 'GB/T10792', '20120409', 'LYLYJ2012060010', '液体', '1.8元', '80瓶', '200瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('180', '茶叶', '181', '180', '9', '308', '4', '18578', '145', '86', '675', '1', '', '', '', '', '散装', '', '', 'NCCYJ2012060007', '叶片状', '140', '40袋', '100袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('181', '非常可乐碳酸饮料', '182', '181', '12', '288', '4', '1768', '217', '74', '299', '4', '2', '娃哈哈', '', '', '500mL/瓶', 'GB/T10792', '', 'LYLYB2012060019', '液态', '2.5元/瓶', '24', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('182', '盐焗鸡腿', '183', '182', '4', '1261', '1', '3', '141', '3', '244', '3', '2', '永健', '', '', '100克/包', 'GB/T 23586-2009', '2012.03.28', 'SSQTA2012060007', '固态', '10.9', '5包', '5包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('183', '健力宝', '184', '183', '12', '288', '4', '1768', '217', '87', '300', '4', '2', '健力宝', '', '', '560mL/瓶', 'GB15266', '20120309YH1A', 'LYLYB2012060020', '液态', '3.5元/瓶', '7', '15', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('184', '早春绿茶', '185', '184', '9', '308', '4', '18578', '145', '88', '675', '2', '0', '龍雲', '特级', '', '100克', 'GB/T14438-12', '2012年03月12日', 'NCCYJ2012060008', '叶片状', '5.0', '5袋', '7袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('185', '百事可乐', '186', '185', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'Aape', '', '', '500ml/瓶', 'GB/T10792', '20120224B', 'LYLYF2012050008', '液态', '3.0', '24瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('186', '白毫银针', '187', '186', '9', '308', '4', '18578', '145', '89', '675', '1', '0', '大地迎春', '特级', '', '50克/袋', 'GB/T14456-1993', '2012年01月03日', 'NCCYJ2012060009', '叶片状', '8', '7袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('187', '毛尖茶', '188', '187', '9', '308', '4', '18578', '145', '90', '675', '1', '0', '云仰', '特级', '', '250克', '', '2011年11月18日', 'NCCYJ2012060010', '叶片状', '25', '7袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('188', '雪碧', '189', '188', '7', '288', '4', '10926', '217', '47', '299', '4', '2', 'Sprite', '', '', '500ml/瓶', 'GB/T10792', '20120304', 'LYLYF2012050009', '液态', '3', '24瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('189', '百事可乐', '190', '189', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'Aape', '', '', '500毫升/瓶', 'GB/T10792', '20120323', 'LYLYF2012050010', '液态', '3.0', '24瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('190', '美年达葡萄味汽水', '191', '190', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'MiRiNDA', '', '', '500ml/瓶', 'GB/T10792', '20120228', 'LYLYF2012050011', '液态', '3.5', '20件', '48件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('191', '非常可乐', '192', '191', '7', '288', '4', '10926', '217', '91', '299', '4', '2', '哇哈哈', '', '', '600毫升/瓶', 'GB/T10792', '20111203', 'LYLYF2012050012', '液态', '3', '100件', '300件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('192', '美年达葡萄味汽水', '193', '192', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'MiRiNDA', '', '', '500毫升/瓶', 'GB/T10792', '20110923', 'LYLYF2012050013', '液态', '3', '24瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('193', '7喜冰爽柠檬味汽水', '194', '193', '7', '288', '4', '10926', '217', '11', '299', '4', '2', '7喜', '', '', '500毫升/瓶', 'GB/T10792', '20120224', 'LYLYF2012050014', '液态', '3', '10瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('194', '雪碧清爽柠檬味汽水', '195', '194', '7', '288', '4', '10926', '217', '10', '299', '4', '2', 'Sprite', '', '', '500毫升/瓶', 'GB/T10792', '20111030', 'LYLYF2012050015', '液态', '3', '24瓶', '72瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('195', '雪碧柠檬汽水', '196', '195', '7', '288', '4', '10926', '217', '10', '299', '4', '2', 'Sprite', '', '', '330毫升/瓶', 'GB/T10792', '20120114', 'LYLYF2012050016', '液态', '2.5', '46', '72', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('196', '可口可乐饮料', '197', '196', '7', '288', '4', '10926', '252', '10', '299', '4', '2', '可口可乐', '', '', '330毫升/听', 'GB/T10792', '20110906', 'LYLYF2012050017', '液态', '2.5', '30听', '30听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('197', '百事可乐', '198', '197', '7', '288', '4', '10926', '217', '11', '299', '4', '2', 'Aape', '', '', '500毫升/瓶', 'GB/T10792', '20120222', 'LYLYF2012050018', '液态', '3.0', '10瓶', '20瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('198', '雪碧', '199', '198', '7', '288', '4', '10926', '217', '48', '299', '4', '2', 'Sprite', '', '', '500毫升/瓶', 'GB/T10792', '20120315', 'LYLYF2012050019', '液态', '3.0', '10瓶', '20瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('199', '雪碧', '200', '199', '7', '288', '4', '10926', '217', '47', '299', '4', '2', 'Sprite', '', '', '330ml/瓶', 'GB/T10792', '20120306', 'LYLYF2012050020', '液态', '2.50', '48瓶', '72瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('200', '桶装水', '201', '200', '9', '532', '4', '18578', '237', '92', '292', '1', '1', '', '', '', '18.9L', 'DB52/434', '2012.5.4', 'LYSHJ2012060001', '液体', '6.0', '30桶', '100桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('201', '茉莉花茶', '202', '201', '7', '308', '1', '10926', '141', '93', '682', '1', '', '醇都', '', '', '250克/包', 'GB18665-2002', '2012年4月9日', 'NCCYF2012050001', '固体', '19.9', '2包', '6包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('202', '朱家山泉水', '203', '202', '9', '1526', '4', '18578', '237', '94', '292', '1', '1', '朱家山', '', '', '18.9L', 'DB52/434', '2012.5.4', 'LYSHJ2012060002', '液体', '6元', '50桶', '100桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('203', '九洞天仙茗绿茶', '204', '203', '7', '308', '1', '10926', '141', '95', '676', '1', '', '九洞天', '', '', '250g/包', 'GB/T14456', '2011年09月1日', 'NCCYF2012050002', '固体', '48.0', '3包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('204', '茉莉花茶', '205', '204', '7', '308', '1', '10926', '145', '96', '682', '1', '', '贵隆茶', '', '', '250克/袋', 'Q/GLJ001-2001', '2012年1月2日', 'NCCYF2012050003', '固体', '19.8', '7袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('205', '清池茶', '206', '205', '7', '308', '1', '10926', '179', '97', '676', '1', '', '沙地茶园', '一级', '', '250g/盒', '', '2011年3月20日', 'NCCYF2012050004', '固体', '50', '15盒', '20盒', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('206', '馨泰茶叶', '207', '206', '7', '308', '1', '10926', '141', '98', '676', '1', '', '馨泰', '特级', '', '250克/包', 'GB19598-2004', '2012年3月4日', 'NCCYF2012050005', '固体', '32', '19包', '32包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('207', '朱家山', '208', '207', '9', '1526', '4', '18578', '237', '94', '292', '1', '', '朱家山', '', '', '18.9L', 'DB52/434', '2012.5.5', 'LYSHJ2012060003', '液体', '6元', '50桶', '100桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('208', '盐焗鸡翅', '209', '208', '9', '1261', '4', '18578', '145', '99', '212', '3', '2', '无穷', '', '', '65克/袋', 'GB/T23586', '2012 04 08', 'SSQTJ2012060009', '固态', '9.5', '100袋', '150袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('209', '雪碧清爽柠檬味汽水', '210', '209', '10', '288', '4', '9567', '217', '10', '301', '2', '1', '雪碧', '', '', '330毫升/瓶', 'GB/T10792', '20120121024927CS3', 'LYLYE2012050006', '无色透明液体', '2.5元/罐', '45罐', '120罐', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('210', '盐焗鸡腿', '211', '210', '9', '1261', '4', '18578', '145', '28', '212', '3', '2', '永健', '', '', '100克/袋', 'GB/T23586-2009', '2012/03/28', 'SSQTJ2012060010', '固态', '10.5', '100袋', '150袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('211', '凤凰山天然冰泉', '212', '211', '9', '1526', '1', '18578', '237', '100', '292', '1', '1', '凤凰山', '', '', '18.9L', 'DB52/434', '2012.05.03', 'LYSHJ2012060004', '液体', '8.0', '3桶', '210桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('212', '耿马露珠', '213', '212', '7', '308', '1', '10926', '141', '101', '676', '1', '', '龙马江', '特级', '', '200克/包', 'GB/T14456.2-2008', '2011.03.01', 'NCCYF2012050006', '固体', '16.0', '9包', '20包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('213', '绿茶', '214', '213', '7', '308', '1', '10926', '145', '102', '676', '1', '', '湄江', '', '', '250克/袋', 'Q/MC02', '2012年2月9日', 'NCCYF2012050007', '固体', '20', '14袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('214', '醒目菠萝味汽水', '215', '214', '10', '288', '4', '9567', '217', '10', '301', '2', '1', '醒目', '', '', '500毫升/瓶', 'GB/T10792', '20120304105303CS1', 'LYLYE201250007', '黄色液体', '3.0元/瓶', '35瓶', '75瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('215', '芬达苹果味汽水', '216', '215', '10', '288', '4', '9567', '217', '10', '301', '2', '1', '芬达', '', '', '500毫升/瓶', 'GB/T10792', '20120305180724CS1', 'LYLYE2012050008', '无色透明液体', '3.0元/瓶', '40瓶', '75瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('216', '湄潭绿茶', '217', '216', '7', '308', '1', '10926', '141', '103', '676', '12', '', '千古韵', '特级', '', '100克/包', 'GB/T20359', '2012年03月9', 'NCCYF2012050008', '固体', '18', '20包', '50包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('217', '茉莉白豪', '218', '217', '7', '308', '1', '10926', '141', '104', '682', '2', '', '坤阳', '特级', '', '100克/包', 'GB18957-2007', '2012年03月9', 'NCCYF2012050009', '固体', '10', '20包', '50包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('218', '饮用天然山泉水', '219', '218', '11', '1526', '4', '14940', '237', '46', '292', '1', '1', '康利', '', '', '18.9L/桶', 'DB52/434', '2012年5月6', 'LYSHH2012060001', '液态', '12.0', '100桶', '120桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('219', '湄潭绿茶', '220', '219', '7', '308', '1', '10926', '145', '105', '676', '1', '', '天城香', '特级', '', '200g/袋', 'DB52/T447-2003', '2012.0408', 'NCCYF2012050010', '固体', '35', '5包', '5包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('220', '（雪碧）清爽柠檬味汽水', '221', '220', '8', '288', '4', '7363', '169', '10', '299', '2', '1', '可口可乐', '', '', '330ml/罐', 'GB/T10792', '20111206CS', 'LYLYD2012060013', '液体', '2.5', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('221', '美年达青苹果味汽水', '222', '221', '10', '288', '4', '9567', '217', '11', '301', '2', '1', '美年达', '', '', '500毫升/瓶', 'GB/T10792', '20120227B', 'LYLYE2012050009', '无色透明液体', '3.00元/瓶', '13瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('222', '可乐型汽水', '223', '222', '8', '288', '4', '7363', '217', '11', '299', '2', '1', '百事可乐', '', '', '1.25升/瓶', 'GB/T10792', '20120102B', 'LYLYD2012060014', '液体', '6.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('223', '橙味汽水', '224', '223', '8', '288', '4', '7363', '217', '11', '299', '2', '1', '美年达', '', '', '500毫升/瓶', 'GB/T10792', '20120320B', 'LYLYD2012060015', '液体', '3.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('224', '柠檬味可乐型汽水', '225', '224', '8', '288', '4', '7363', '217', '11', '299', '2', '1', '百事', '', '', '500毫升/瓶', 'GB/T10792', '20120322B`', 'LYLYD2012060016', '液体', '3.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('225', '原生态硒水', '226', '225', '11', '1526', '4', '14940', '237', '69', '294', '1', '1', '一桶江湖', '', '', '18.9L/桶', 'DB52/434', '201247', 'LYSHH2012060002', '液态', '8.00', '80桶', '150桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('226', '(小可乐）低热量型碳酸饮料', '227', '226', '8', '288', '4', '7363', '217', '11', '299', '5', '1', '甜甜雪', '', '', '250ml/瓶', 'GB/T21733', '20120502', 'LYLYD2012060017', '液体', '0.5', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('227', '可乐型汽水', '228', '227', '8', '288', '4', '7363', '217', '8', '299', '2', '1', '百事可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120414B', 'LYLYD2012060018', '液体', '2.2', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('228', '中华山饮用泉水', '229', '228', '11', '1526', '4', '14940', '237', '70', '291', '1', '1', '中华山', '', '', '18.9L/桶', 'DB52/434', '2012年04月24', 'LYSHH2012060003', '液态', '10.0', '200桶', '500桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('229', '非常可乐（碳酸饮料）', '230', '229', '8', '288', '4', '7363', '217', '107', '299', '2', '1', '娃哈哈', '', '', '1.25升/瓶', 'GB/T10792', '20120102FL', 'LYLYD2012060019', '液体', '5.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('230', '非常可乐碳酸饮料', '231', '230', '8', '288', '4', '7363', '217', '107', '299', '2', '1', '娃哈哈', '', '', '500ml/瓶', 'GB/T10792', '20120313FL', 'LYLYD2012060020', '液体', '3.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('231', '饮用天然水', '232', '231', '11', '1526', '4', '14940', '237', '108', '292', '1', '1', '心静如水', '', '', '18.9L/桶', 'DB52/434', '2012年5月06', 'LYSHH2012060004', '液态', '8.0', '500桶', '130桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('232', '梵净山藤茶', '233', '232', '8', '308', '1', '7363', '145', '109', '674', '2', '', '夜郎村', '一级', '', '100克', 'Q/JMYS01-2010', '2012年01月10日', 'NCCYD2012060001', '固态', '48.0元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('233', '阿幼朵饮用天然泉水', '234', '233', '11', '1526', '4', '14940', '237', '110', '291', '1', '1', '村姑', '', '', '18.9L/桶', 'DB52/434', '2012.05.06', 'LYSHH2012060005', '液态', '10.0', '60桶', '200桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('234', '仰阿莎饮用泉水', '235', '234', '11', '1526', '4', '14940', '237', '111', '291', '1', '1', '仰阿莎', '', '', '18.9L/桶', 'DB52/434', '2012.05.05', 'LYSHH2012060006', '液态', '10.00', '50桶', '300桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('235', '饮用纯净水', '236', '235', '11', '1525', '4', '14940', '237', '112', '293', '1', '1', '泓泰', '', '', '18.9L/桶', 'GB17323', '2012年5月07日', 'LYSHH2012060007', '液态', '8.00', '100桶', '120桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('236', '石阡苔茶', '237', '236', '8', '308', '1', '7363', '179', '113', '675', '7', '', '中国茗茶', '特级', '', '150g/盒', 'DB52/532', '2012.3', 'NCCYD2012060002', '固态', '158.0/盒', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('237', '饮用天然泉水', '238', '237', '11', '1526', '4', '14940', '237', '13', '292', '1', '1', '飞龙雨', '', '', '19.7L/桶', 'DB52/434-2007', '2012年5月5日', 'LYSHH2012060008', '液态', '10.00', '4000桶', '1000桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('238', '北极熊饮用纯净水', '239', '238', '11', '1525', '4', '14940', '237', '114', '293', '1', '1', '北极熊', '', '', '18.9L/桶', 'GB17323', '2012年5月6', 'LYSHH2012060009', '液态', '12.00', '6桶', '400桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('239', '梵净山翠峰', '240', '239', '8', '308', '1', '7363', '20', '115', '675', '200', '', '', '', '', '散装', '', '', 'NCCYD2012060003', '固态', '280元/斤', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('240', '雷公山峡谷泉', '241', '240', '11', '1526', '4', '14940', '237', '116', '291', '1', '1', '云井', '', '', '18.9L±0.5', 'DB52/434', '2012年05月07日', 'LYSHH2012060010', '液态', '10.00', '60桶', '150桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('241', '苦丁茶', '242', '241', '8', '308', '1', '7363', '20', '117', '675', '200', '', '', '', '', '散装', '散装', '', 'NCCYD2012060004', '固态', '320元/斤', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('242', '梵净山毛峰茶', '243', '242', '8', '308', '1', '7363', '145', '117', '675', '1', '', '梵绿', '特级', '', '250克/袋', 'DB52/470', '2012.02', 'NCCYD2012060005', '固态', '88.0元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('243', '7喜冰爽柠檬味汽水', '244', '243', '10', '288', '4', '9567', '217', '11', '301', '2', '1', '', '', '', '500毫升/瓶', 'GB/T10792', '20120225A', 'LYLYE2012050010', '绿色透明有液体', '3.0元/瓶', '12瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('244', '雪碧清爽柠檬味汽水', '245', '244', '10', '288', '1', '9567', '217', '10', '301', '4', '2', '雪碧', '', '', '330ml/瓶', 'GB/T10792', '20111205CS', 'LYLYE2012050011', '无色透明液体', '3.0元/瓶', '5瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('245', '百事可乐可乐型汽水', '246', '245', '10', '288', '4', '9567', '217', '11', '301', '4', '2', '', '', '', '330ml/瓶', 'GB/T10792', '20120223D', 'LYLYE2012050012', '无色透明液体', '3.0元/瓶', '15瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('246', '可口可乐汽水', '247', '246', '10', '288', '4', '9567', '217', '47', '301', '4', '2', '图标见瓶身', '', '', '500毫升/瓶', 'GB/T10792', '201202291633HBA07', 'LYLYE2012050013', '深褐色液体', '3元/瓶', '10瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('247', '非常可乐碳酸饮料', '248', '247', '10', '288', '4', '9567', '217', '12', '301', '4', '2', '娃哈哈', '', '', '500ml/瓶', 'GB/T10792', '201204172222CD', 'LYLYE2012050014', '红褐色液体', '3元/瓶', '24瓶', '60瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('248', '柠檬味碳酸饮料', '249', '248', '10', '288', '4', '9567', '217', '107', '301', '4', '2', '娃哈哈', '', '', '500ml/瓶', 'GB/T10792', '201203092101FL', 'LYLYE2012050015', '红褐色液体', '3元/瓶', '36瓶', '60瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('249', '可口可乐汽水', '250', '249', '10', '288', '4', '9567', '217', '10', '301', '4', '2', '图标见瓶身', '', '', '500毫升/瓶', 'GB/T10792', '20120201172833CS1', 'LYLYE2012050016', '红褐色液体', '3元/瓶', '20瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('250', '百事可乐可乐型汽水', '251', '250', '10', '288', '4', '9567', '217', '11', '301', '4', '2', '图标见瓶身', '', '', '500毫升/瓶', 'GB/T10792', '20120222B', 'LYLYE2012050017', '红褐色液体', '3.0元/瓶', '14瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('251', '雪碧清爽柠檬味汽水', '252', '251', '10', '288', '4', '9567', '217', '47', '301', '4', '2', '雪碧', '', '', '500毫升/瓶', 'GB/T10792', '201203031446HBA07', 'LYLYE2012050018', '红褐色液体', '3元/瓶', '19瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('252', '非常可乐碳酸饮料', '253', '252', '10', '288', '4', '9567', '217', '107', '301', '4', '2', '娃哈哈', '', '', '500ml/瓶', 'GB/T10792', '201203132100FL', 'LYLYE2012050019', '红褐色液体', '3元/瓶', '5瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('253', '可口可乐汽水', '254', '253', '10', '288', '4', '9567', '217', '10', '301', '4', '2', '图标见瓶身', '', '', '330毫升/瓶', 'GB/T10792', '20110914CS', 'LYLYE2012050020', '红褐色液体', '2.5元/瓶', '8瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('254', '云外享香卤鸡爪', '255', '254', '10', '1261', '4', '9567', '141', '118', '218', '2', '1', '', '', '', '100克/包', 'GB/T23586', '2012年03月08日', 'SSQTE2012050005', '深红色固体', '3.7元/包', '6包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('255', '精品爪王', '256', '255', '10', '1261', '4', '9567', '145', '40', '218', '2', '1', '佳香旺', '', '', '50克/袋', 'GB/T23586-2009', '2012.3.21', 'SSQTE2012050006', '深红色固体', '1.5元/袋', '150袋', '300袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('256', '莎辣咪单爪', '257', '256', '10', '1261', '4', '9567', '145', '119', '218', '2', '1', '莎辣咪', '', '', '16克/袋', 'GB/T23586', '2012.3.25', 'SSQTE2012050007', '橘黄色固体', '0.5元/袋', '50袋', '300袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('257', '香鸡翅', '258', '257', '10', '1261', '4', '9567', '145', '37', '218', '2', '1', '清味园', '', '', '100克/袋', 'GB/T23586-2009', '2012/02/29', 'SSQTE2012050008', '固体', '6.0元/袋', '20袋', '50袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('258', '野山椒凤爪', '259', '258', '10', '1261', '4', '9567', '145', '120', '218', '2', '1', '', '', '', '70克/袋', 'GB/T23586-2009', '2012年3月05日', 'SSQTE2012050009', '固体', '3元/袋', '20袋', '50袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('259', '香菇鸭翅', '260', '259', '10', '158', '4', '9567', '145', '121', '218', '2', '1', '', '', '', '16克/袋', 'GB/T23586-2009', '2012.3.21', 'SSQTE2012050010', '黑色固体', '0.5元/袋', '30袋', '100袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('260', '美年达橙味汽水', '261', '260', '11', '288', '4', '16699', '169', '11', '299', '4', '2', '美年达', '', '', '330ml/罐', 'GB/T10792', '20110629D', 'LYLYH2012060001', '液态', '2.2', '12', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('261', '非常可乐碳酸饮料', '262', '261', '11', '288', '4', '16699', '217', '122', '299', '4', '2', '非常', '', '', '600ml/瓶', 'GB/T10792', '20120305', 'LYLYH2012060002', '液态', '3.0', '30', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('262', '卤鸡翅', '263', '262', '11', '1261', '4', '14940', '145', '123', '212', '2', '1', '友伦', '', '', '100克', 'Q/68459110', '2012/02/29B', 'SSQTH2012060001', '固体', '6.20', '9袋', '9袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('263', '都匀毛尖', '264', '263', '8', '308', '1', '7363', '145', '124', '675', '2', '', '千古韵', '特级', '', '100克/袋', 'GB/T20359', '2012年4月10日', 'NCCYD2012060006', '固体', '12.0元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('264', '湄潭绿茶', '265', '264', '8', '308', '1', '7363', '145', '124', '675', '1', '', '千古韵', '特级', '', '250克/袋', 'GB/T20359', '2012年4月10日', 'NCCYD2012060007', '固体', '15.0元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('265', '7喜冰爽柠檬味汽水', '266', '265', '11', '288', '4', '16699', '217', '11', '299', '4', '2', '百事', '', '', '500ml/瓶', 'GB/T10792', '20120225A', 'LYLYH2012060003', '液态', '3.0', '216', '240', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('266', '泡凤爪', '267', '266', '11', '1261', '4', '14940', '145', '123', '212', '3', '2', '友伦', '', '', '70克', 'Q/68459110-3.1', '2012/02/21B', 'SSQTH2012060002', '固体', '3.20', '12袋', '12袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('267', '老卤香香卤猪蹄', '268', '267', '11', '1260', '4', '14940', '145', '125', '212', '2', '1', '友伦', '', '', '150克', 'Q/68459110-3.2', '2012/02/10B', 'SSQTH2012060003', '固体', '7.90', '6袋', '6袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('268', '百事可乐', '269', '268', '11', '288', '2', '14940', '217', '11', '299', '4', '2', '百事可乐', '', '', '500ml/瓶', 'GB/T10792', '20120212B', 'LYLYH2012060004', '液态', '3.0', '150', '250', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('269', '百事可乐', '270', '269', '11', '288', '4', '14940', '217', '11', '299', '4', '2', 'PEPSI百事可乐', '', '', '500ml/瓶', 'GB/T10792', '20111201B', 'LYLYH2012060005', '液态', '3.0', '14', '120', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('270', '凤凰山泉水', '271', '270', '9', '1526', '4', '18578', '237', '100', '292', '1', '1', '凤凰山', '', '', '18.9L', 'DB52/434', '2012.05.07', 'LYSHJ2012060005', '液体', '8.0', '30桶', '32桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('271', '香酥猪蹄', '272', '271', '11', '1260', '4', '14940', '145', '37', '212', '2', '1', '清味园', '', '', '140克', 'GB/T23586-2009', '2012∕02∕14', 'SSQTH2012060004', '固体', '7.80', '16袋', '16袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('272', '椒盐鸡腿', '273', '272', '11', '1261', '4', '14940', '145', '126', '212', '2', '1', '金爱丽', '', '', '90克', 'DB43/160.2-2009', '2012∕03∕12', 'SSQTH2012060005', '固体', '9.80', '93袋', '50袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('273', '云雾绿茶', '274', '273', '8', '308', '1', '7363', '145', '127', '675', '1', '', '梵净山', '高级', '', '500克/袋', 'DB52/T442.3-2010', '2012年5月02日', 'NCCYD2012060008', '固体', '36.0元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('274', '黔山秀水', '275', '274', '9', '1526', '4', '18578', '237', '128', '292', '1', '1', '黔山秀水', '', '', '18.9L', 'DB52/434', '2012/05/6日', 'LYSHJ2012060006', '液体', '12', '300桶', '30桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('275', '铁观音', '276', '275', '8', '308', '1', '7363', '20', '129', '675', '200', '', '', '', '', '散装', '', '', 'NCCYD2012060009', '', '', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('276', '雪碧柠檬汽水', '277', '276', '9', '288', '4', '18578', '217', '10', '299', '4', '2', '雪碧', '', '', '500毫升/瓶', 'GB/T10792', '20111203', 'LYLYJ2012060011', '液体', '2.8元', '24瓶', '120瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('277', '铁观音', '278', '277', '8', '308', '1', '7363', '20', '129', '675', '200', '', '', '', '', '散装', '', '', 'NCCYD2012060010', '固体', '120.0元/斤', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('278', '百事可乐汽水', '279', '278', '9', '288', '4', '18578', '217', '11', '299', '4', '2', '百事可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120412', 'LYLYJ2012060012', '液体', '3.00元', '50瓶', '240瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('279', '东山泉饮用纯净水', '280', '279', '9', '1525', '4', '18578', '237', '130', '293', '1', '1', '﹡ZC6702769 SL﹡', '', '', '18.9L', 'GB17323', '2012 5 6', 'LYSHJ2012060007', '液体', '7', '50桶', '250桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('280', '激浪柠檬味可乐型汽水', '281', '280', '9', '288', '4', '18578', '217', '11', '299', '4', '2', '激浪', '', '', '500毫升/瓶', 'GB/T10792', '20120322', 'LYLYJ2012060013', '液体', '3.00元', '12瓶', '60瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('281', '山椒凤爪', '282', '281', '11', '1261', '4', '14940', '145', '131', '212', '3', '2', '友伦', '', '', '70克', 'Q/FGHS01-2011', '2012/03/21C', 'SSQTH2012060006', '固体', '3.20', '47袋', '47袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('282', '七喜冰爽柠檬味汽水', '283', '282', '9', '288', '4', '18578', '252', '11', '299', '4', '2', '百事可乐', '', '', '330ml/听', 'GB/T10792', '20120224', 'LYLYJ2012060014', '液体', '2.5元', '25听', '60听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('283', '飞龙雨饮用天然泉水', '284', '283', '9', '1526', '4', '18578', '237', '132', '292', '1', '1', '飞龙雨', '', '', '19.7L', 'DB52/434-2007', '2012年5月3日', 'LYSHJ2012060008', '液体', '10', '103桶', '140桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('284', '野山椒泡凤爪', '285', '284', '11', '1261', '4', '14940', '145', '123', '212', '2', '1', '友伦', '', '', '180克', 'Q/68459110-3.1', '2012/03/02B', 'SSQTH2012060007', '固体', '8.90', '21袋', '21袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('285', '可口可乐汽水', '286', '285', '9', '288', '4', '18578', '252', '10', '299', '4', '2', '可口可乐', '', '', '330ml/听', 'GB/T10792', '20111213', 'LYLYJ2012060015', '液体', '2.3元', '48听', '96听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('286', '可口可乐汽水', '287', '286', '9', '288', '4', '18578', '217', '48', '299', '4', '2', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120206', 'LYLYJ2012060016', '液体', '2.8元', '96瓶', '240瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('287', '呷比特香辣猪蹄', '288', '287', '11', '1260', '4', '14940', '145', '133', '212', '2', '1', '呷比特', '', '', '130克', 'DB43/160.2-2009', '2012∕02∕02', 'SSQTH2012060008', '固体', '9.00', '8袋', '8袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('288', '雪碧清爽柠檬味汽水', '289', '288', '11', '288', '4', '18303', '217', '10', '299', '4', '2', '雪碧', '', '500ml/瓶', '', 'GB/T10792', '20120325', 'LYLYH2012060006', '液态', '2.5', '200件', '500件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('289', '醒目荔枝味汽水', '290', '289', '9', '288', '4', '18578', '217', '10', '299', '4', '2', '醒目', '', '', '500毫升/瓶', 'GB/T10792', '20111127', 'LYLYJ2012060017', '液体', '3.00元', '24瓶', '96瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('290', '山椒凤爪', '291', '290', '11', '1261', '4', '14940', '145', '131', '212', '6', '4', '友伦', '', '', '28克', 'Q/FGHS01-2011', '2012/03/20C', 'SSQTH2012060009', '固体', '1.50', '334袋', '200袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('291', '美年达青苹果味汽水', '292', '291', '9', '288', '4', '18578', '252', '11', '299', '4', '2', '美年达', '', '', '330ml/听', 'GB/T10792', '20120225', 'LYLYJ2012060018', '液体', '2.5元', '90听', '240听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('292', '金爱丽野山椒土鸡爪', '293', '292', '11', '1261', '4', '14940', '145', '126', '212', '2', '1', '金爱丽', '', '', '100g/袋', 'DB43/160.2', '2012.03.10', 'SSQTH2012060010', '固体', '6.20', '20袋', '40袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('293', '沁碧泉饮用天然泉水', '294', '293', '9', '532', '4', '18578', '237', '134', '292', '1', '1', '', '', '', '18.9L', 'DB52/434-2007', '20120507', 'LYSHJ2012060009', '液体', '7元', '110桶', '150桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('294', '极度', '295', '294', '9', '288', '4', '18578', '252', '82', '299', '4', '2', '百事可乐', '', '', '330毫升/听', 'GB/T10792', '20120410', 'LYLYJ2012060019', '液体', '2.0元', '20听', '48听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('295', '盐焗鸡腿', '296', '295', '8', '1261', '4', '7363', '145', '135', '244', '3', '2', '陈老根', '', '', '110g/包', 'DB43/160.2', '2012/03/02', 'SSQTD2012060001', '固体', '7.0元', '', '15', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('296', '芬达葡萄汽水', '297', '296', '9', '288', '4', '18578', '252', '10', '299', '4', '2', '芬达', '', '', '330毫升/听', 'GB/T10792', '20110801', 'LYLYJ2012060020', '液体', '2.3元', '15听', '48听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('297', '纯露饮用天然泉水', '298', '297', '9', '1526', '4', '18578', '237', '136', '292', '1', '1', '宝立', '', '', '18.9L', 'DB52/434', '2012/05/07', 'LYSHJ2012060010', '液体', '9', '300桶', '500桶', '', '无条形码', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('298', '香辣火鸡腿', '299', '298', '8', '1261', '4', '7363', '145', '137', '244', '3', '2', '霸豪丰', '', '', '75克/袋', 'GB/T23586-2009', '2012年02月29日', 'SSQTD2012060002', '固体', '7.00元/袋', '一件', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('299', '乡巴佬麻辣猪蹄', '300', '299', '8', '158', '4', '7363', '145', '139', '212', '3', '2', '万顺豪', '', '', '130克', 'GB/T23586', '2012年2月13日', 'SSQTD2012060003', '固体', '6.00元/袋', '一件', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('300', '原味盐焗鸡腿', '301', '300', '8', '158', '4', '7363', '141', '139', '245', '3', '2', '旺口福', '', '', '80克/包', 'Q/RWF1', '20120305', 'SSQTD2012060004', '固体', '6元/包', '', '一件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('301', '乡太佬猪蹄', '302', '301', '8', '1260', '4', '7363', '145', '140', '212', '3', '2', '乡太佬', '', '', '120克', 'GB/T23586-2009', '20120207', 'SSQTD2012060005', '固体', '5.50元/袋', '一件', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('302', '盐焗鸡排', '303', '302', '8', '1261', '4', '7363', '141', '141', '245', '3', '2', '杜家庄', '', '', '100克/包', 'GB/T23586', '2012/02/05', 'SSQTD2012060006', '固体', '6.8元/包', '20包', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('303', '酱板鸭', '304', '303', '8', '1261', '4', '7363', '141', '142', '245', '3', '2', '步步为赢', '', '', '100g', 'DB43/160.2', '2012.02.16', 'SSQTD2012060007', '固体', '5元/包', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('304', '盐焗鸡翅', '305', '304', '8', '1261', '4', '7363', '141', '141', '245', '3', '2', '杜家庄', '', '', '65g/包', 'GB/T23586', '2012/04/03', 'SSQTD2012060008', '固体', '7.5元/包', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('305', '盐焗鸡腿', '306', '305', '8', '1261', '4', '7363', '141', '99', '245', '4', '2', '无穷', '', '', '80g', 'GB/T23586', '2012031812T', 'SSQTD2012060009', '固体', '7.9元/包', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('306', '麻辣猪蹄', '307', '306', '8', '1260', '4', '7363', '141', '143', '218', '4', '2', '振旺', '', '', '130g', 'GB/T23586', '20120206', 'SSQTD2012060010', '固体', '7.5元/包', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('307', '可口可乐', '308', '307', '4', '288', '4', '3', '252', '47', '299', '2', '1', '可口可乐', '', '', '330毫升/听', 'GB/T 10792', '20120304', 'LYLYA2012060020', '液态', '2.5', '12听', '24听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('308', '卤翅尖', '309', '308', '8', '1261', '4', '7363', '141', '144', '245', '4', '2', '七味', '', '', '50克', 'DB43/160.2', '2012/03/16c', 'SSQTD2012060011', '固体', '3.5元/包', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('309', '香酥猪蹄', '310', '309', '8', '1260', '4', '7363', '141', '144', '218', '4', '2', '柯想', '', '', '140克', 'GB/T23586-2009', '2012/04/03', 'SSQTD2012060012', '固体', '7.9元/包', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('310', '香辣风味香肠', '311', '310', '4', '1266', '4', '3', '153', '146', '231', '4', '2', '双汇', '普通级', '', '85g/个', 'Q/NAAA0003S', '20120219', 'SSQTA2012060008', '固体', '2.0', '6件', '7件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('311', '香辣猪蹄', '312', '311', '8', '158', '4', '7363', '145', '147', '218', '2', '1', '想我香', '', '', '140克/袋', 'GB/T23586-2009', '2012/02/23', 'SSQTD2012060013', '固体', '7元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('312', '土鸡爪', '313', '312', '4', '1261', '4', '3', '141', '148', '231', '2', '1', '恒的', '', '', '100g/包', 'DB50/294', '2012/02/17', 'SSQTA2012060009', '固态', '4.3', '17包', '20包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('313', '盐焗肉鸡爪', '314', '313', '8', '1261', '4', '7363', '145', '141', '245', '2', '1', '杜家庄', '酱卤制品', '', '50克/袋', 'GB/T23586', '2012/03/25', 'SSQTD2012060014', '固体', '4.0元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('314', '凤尾鱼', '315', '314', '4', '158', '1', '3', '141', '149', '231', '2', '1', '辣来主义', '', '', '65g/包', 'Q/YPY0002S', '2011.12.25', 'SSQTA2012060010', '固态', '4.8', '5包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('315', '酱辣腿', '316', '315', '8', '1261', '4', '7363', '145', '57', '245', '2', '1', '乡巴佬', '', '', '90g/袋', 'GB/T23586', '2012/01/03', 'SSQTD2012060015', '固体', '6.5元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('316', '香辣鸡肉串', '317', '316', '8', '1261', '4', '7363', '145', '57', '245', '3', '1', '辣博士', '', '', '60克/袋', 'DB43/160', '2012.03.02W', 'SSQTD2012060016', '固体', '5.0元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('317', '盐焗鸭腿', '318', '317', '8', '158', '4', '7363', '145', '151', '245', '4', '1', '双佳龙', '', '', '75克/袋', 'DB43/160.2-2009', '2012/02/06', 'SSQTD2012060017', '固体', '2.7元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('318', '长寿山泉', '319', '318', '8', '1526', '4', '7363', '237', '152', '292', '1', '1', '挞扒洞', '', '', '18.9L/桶', 'DB52/434-2007', '2012年5月4日', 'LYSHD2012060007', '无色液体', '7.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('319', '烤香羊肉味肉串', '320', '319', '8', '1260', '1', '7363', '145', '153', '245', '4', '1', '二酉', '', '', '50克/袋', 'DB43/160.2-2009', '2012/04/21', 'SSQTD2012060018', '固体', '2.8元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('320', '天然泉水', '321', '320', '8', '1526', '4', '7363', '237', '152', '292', '1', '1', '百里锦江', '', '', '18.9L/桶', 'DB52/434', '2012年5月6日', 'LYSHD2012060008', '无色液体', '10.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('321', '饮用天然泉水', '322', '321', '8', '532', '4', '7363', '237', '155', '292', '1', '1', '九龙泉', '', '', '18.9L/桶', 'DB52/434', '2012年5月7日', 'LYSHD2012060009', '无色液体', '7.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('322', '香酥猪蹄', '323', '322', '8', '1260', '4', '7363', '145', '156', '218', '2', '1', '瓯南龙翔', '', '', '140克/袋', 'GB/T23586-2009', '2012年4月8', 'SSQTD2012060019', '固体', '5.50元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('323', '饮用天然泉水', '324', '323', '8', '1526', '4', '7363', '237', '155', '292', '1', '1', '九福泉', '', '', '18.9L/桶', 'DB52/434', '2012年5月7日', 'LYSHD2012060010', '无色液体', '6.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('324', '麻辣猪蹄', '325', '324', '8', '1260', '4', '7363', '145', '138', '218', '2', '1', '万顺豪', '', '', '130克/袋', 'GB/T23586', '2012年5月01', 'SSQTD2012060020', '固体', '5.5元/袋', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('325', '百事可乐', '326', '325', '11', '288', '4', '18130', '217', '11', '299', '1', '1', '百事可乐', '', '', '2升/瓶', 'GB/T10792', '20120131B', 'LYLYH2012060007', '液态', '8.0', '3', '3', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('326', '可口可乐汽水', '327', '326', '11', '288', '4', '18129', '217', '10', '299', '4', '2', '可口可乐', '', '', '500ml/瓶', 'GB/T10792', '20120110', 'LYLYH2012060008', '液态', '3.0', '15', '2', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('327', '雪峰云雾绿茶', '328', '327', '11', '308', '1', '14940', '141', '158', '676', '4', '0', '茗中鹤', '特级', '', '50g/包', 'GB/T14456.1-2008', '2011∕12∕26', 'NCCYH2012060001', '固体', '5.0', '8包', '20包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('328', '舞水绿茶', '329', '328', '11', '308', '1', '14940', '141', '158', '676', '2', '0', '茗中鹤', '一级', '', '100克/包', 'GB/T14456.1-2008', '2011∕12∕26', 'NCCYH2012060002', '固体', '6.8', '8包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('329', '都匀手工毛尖', '330', '329', '11', '308', '1', '14940', '141', '159', '676', '2', '', '世纪福', '一级', '', '100克/包', 'GB/T14456', '2012年4月1日', 'NCCYH2012060003', '固体', '15', '9包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('330', '苦丁茶', '331', '330', '11', '308', '1', '14940', '145', '160', '676', '1', '', '一壶春', '特级', '', '200克/袋', '', '2012年3月3日', 'NCCYH2012060004', '固体', '10', '3袋', '5袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('331', '云雾绿茶', '332', '331', '11', '308', '1', '14940', '145', '161', '676', '1', '', '毛克翕', '一级', '', '250克/袋', 'Q/LMC04', '2011.10.31', 'NCCYH2012060005', '固体', '22', '24袋', '56袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('332', '云雾绿茶', '333', '332', '11', '308', '1', '14940', '145', '162', '676', '3', '0', '德毅', '一级', '', '200克/袋', 'Q/LDL04-2007', '2011年5月8日', 'NCCYH2012060006', '固体', '12.5', '8袋', '12袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('333', '大龙清明茶', '334', '333', '11', '308', '1', '14940', '145', '162', '676', '2', '', '德毅', '特级', '', '100克/袋', 'Q/LDL03-2006', '2012年4月8日', 'NCCYH2012060007', '固体', '25', '33袋', '50袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('334', '苗岭翠绿茶', '335', '334', '11', '308', '1', '14940', '145', '163', '676', '1', '', '苗乡情', '特级', '', '200克/袋', 'DB52/T447', '2011年9月18日', 'NCCYH2012060008', '固体', '35', '5袋', '6袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('335', '云雾绿茶', '336', '335', '11', '308', '1', '14940', '141', '164', '676', '1', '', '鑫球', '一级', '', '200克/包', 'DB52/T442', '2012年4月9日', 'NCCYH2012060009', '固体', '15', '9包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('336', '碧螺春茶', '337', '336', '11', '308', '1', '14940', '141', '165', '676', '1', '', '好茗翠柳', '一级', '', '250克/包', 'DB52/T447', '2011年04月3', 'NCCYH2012060010', '固体', '22', '4袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('337', '百事可乐', '338', '337', '11', '288', '4', '14940', '169', '11', '299', '4', '2', '百事', '', '', '330ml/罐', 'GB/T10792', '20120222D', 'LYLYH2012060009', '液态', '2.5', '24', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('338', '激浪柠檬味可乐型汽水', '339', '338', '11', '288', '4', '14940', '217', '11', '299', '4', '2', 'PEPSI', '', '500ml/瓶', '', 'GB/T10792', '20111202B', 'LYLYH2012060010', '液态', '3.0', '10', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('339', '葡萄味汽水', '340', '339', '11', '288', '4', '14940', '217', '11', '299', '4', '2', '美年达', '', '', '500ml/瓶', 'GB/T10792', '20111222C', 'LYLYH2012060011', '液态', '3.0', '18', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('340', '汽水', '341', '340', '11', '288', '4', '14940', '217', '59', '299', '4', '2', '可口可乐', '', '', '300ml/瓶', 'GB/T10792', '20120320', 'LYLYH2012060012', '液态', '2.0', '19', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('341', '百事可乐', '342', '341', '11', '288', '4', '14940', '169', '11', '299', '4', '2', '百事可乐', '', '', '330ml/罐', 'GB/T10792', '20120212D', 'LYLYH2012060013', '液态', '2.5', '2件', '3件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('342', '雪碧', '343', '342', '11', '288', '4', '14940', '169', '47', '299', '4', '2', '雪碧', '', '', '330ml/罐', 'GB/T10792', '20120227', 'LYLYH2012060014', '液态', '2.5', '2件', '3件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('343', '可口可乐', '344', '343', '11', '288', '4', '14940', '217', '10', '299', '4', '2', '可口可乐', '', '', '500ml/瓶', 'GB/T10792', '20120314', 'LYLYH2012060015', '液态', '2.8', '10', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('344', '百事可乐', '345', '344', '11', '288', '4', '14940', '217', '11', '299', '4', '2', 'BY*A BATHING APE', '', '', '500ml/瓶', 'GB/T10792', '20120323C', 'LYLYH2012060016', '液态', '3.0', '42', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('345', '雪碧', '346', '345', '11', '288', '4', '14940', '217', '10', '299', '4', '2', '雪碧', '', '', '500ml/瓶', '', '20120325', 'LYLYH2012060017', '液态', '3.0', '18', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('346', '芬达橙味汽水', '347', '346', '11', '288', '4', '14940', '217', '10', '299', '3', '2', '芬达', '', '', '500ml/瓶', 'GB/T10792', '20120323', 'LYLYH2012060018', '液态', '2.8', '65', '65', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('347', '青苹果味汽水', '348', '347', '11', '288', '4', '14940', '169', '11', '299', '3', '2', '美年达', '', '', '330ml/罐', 'GB/T10792', '20110630D', 'LYLYH2012060019', '液态', '2.2', '24', '57', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('348', '7喜冰爽柠檬味汽水', '349', '348', '11', '288', '4', '14940', '169', '11', '299', '4', '2', '百事', '', '', '330ml/罐', 'GB/T10792', '20111124D', 'LYLYH2012060020', '液态', '2.5', '8组', '80组', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('349', '茶', '350', '349', '5', '308', '1', '3052', '193', '166', '676', '200', '0', '', '', '', '散装', 'GB/T14456', '2012年04月01日', 'CHAYC201205-0001', '固态', '80元', '20千克', '25千克', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('350', '桴焉茶', '351', '350', '5', '308', '1', '3052', '193', '167', '676', '200', '0', '桴焉茶业', '', '', '散装', 'DB52/T442.1-2003', '2012.02.02', 'CHAYC201205-0002', '固态', '80元', '32千克', '32千克', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('351', '珍州绿茶', '352', '351', '5', '308', '1', '3052', '145', '168', '676', '2', '0', '珍洲绿', '', '', '125克/袋', 'GB/T14456', '2012年03月13日', 'CHAYC201205-0003', '固态', '48元/袋', '50千克', '50千克', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('352', '有机茶', '353', '352', '5', '308', '1', '3052', '145', '169', '676', '1', '0', '吐香', '特级', '', '200克/袋', 'GB/T14456-93', '2012年03月28日', 'CHAYC201205-0004', '固态', '40元/袋', '250千克', '250千克', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('353', '百事可乐', '354', '353', '5', '288', '4', '3052', '217', '11', '301', '4', '2', 'PEPSI', '', '', '500毫升/瓶', 'GB/T10792', '20111110', '赤水LYLYC2012050001', '液体', '3元/瓶', '13瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('354', '雪碧', '355', '354', '5', '288', '4', '3052', '217', '10', '301', '4', '2', '雪碧', '', '', '500ml/瓶', 'GB/T10792', '20120116', '赤水LYLYC2012050002', '液体', '3元/瓶', '12瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('356', '雪碧', '357', '356', '5', '288', '4', '3052', '217', '48', '301', '4', '2', '雪碧', '', '', '330毫升/瓶', 'GB/T10792', '20120223', '赤水LYLYC2012050003', '液体', '2元/瓶', '12瓶', '50瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('357', '森旺茶', '358', '357', '5', '308', '1', '3052', '193', '171', '676', '200', '0', '', '', '', '散装', '', '2012.4.28', 'CHAYC201205-0005', '固态', '25元', '20千克', '20千克', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('358', '可口可乐', '359', '358', '5', '288', '4', '3052', '217', '48', '301', '4', '2', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120206', '赤水LYLYC2012050004', '液体', '3元/瓶', '15瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('360', '绿茶', '361', '360', '5', '308', '1', '3052', '145', '173', '676', '2', '0', '仡佬山', '', '', '100g/袋', 'GB/T14456.1-2008', '12年04月24日', 'CHAYC2012050006', '固态', '12元/袋', '5000袋', '5000袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('361', '可口可乐汽水', '362', '361', '5', '288', '4', '3052', '217', '48', '301', '4', '2', '可口可乐', '', '', '瓶装', '330毫升/瓶', '20120218', '赤水LYLYC2012050005', '液体', '2.00元/瓶', '15瓶', '50瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('362', '百事可乐', '363', '362', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '百事', '', '', '500ml/瓶', 'GB/T10792', '20120330', 'LYLYC201205006', '液体', '2.9元/瓶', '10瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('363', '饮用纯净水', '364', '363', '5', '1525', '4', '3052', '237', '52', '293', '2', '1', '北极熊', '', '', '18.9L/桶', 'GB17323', '2012年5月4', 'LYSHC201205003', '液体', '15.00元', '29', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('364', '黄山毛峰', '365', '364', '5', '308', '1', '3052', '145', '174', '676', '2', '0', '绿地缘', '一级', '', '100g/袋', 'GB/T14456.1', '2012年02月01日', 'CHAYC2012050007', '固态', '22元/袋', '4袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('365', '雪碧汽水', '366', '365', '5', '288', '4', '3052', '217', '10', '301', '4', '2', '雪碧', '', '', '500ml/瓶', 'GB/T10792', '20120325', 'LYLYC201205007', '液体', '3元/瓶', '15瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('366', '茉莉花茶', '367', '366', '5', '308', '1', '3052', '145', '175', '682', '2', '0', '图形商标', '甲级', '', '100g/袋', 'Q/XGT0011S', '2012/01/02', 'CHAYC2012050008', '固态', '15元/袋', '5袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('367', '冰爽汽水', '368', '367', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '', '', '', '500ml/瓶', 'GB/T10792', '20120225', 'LYLYC2012050008', '液体', '3元/瓶', '23瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('368', '饮用天然泉水', '369', '368', '5', '532', '4', '3052', '237', '170', '292', '2', '1', '黔北泉', '', '', '18.9L/桶', 'DB52/434', '20120508', 'LYSHC201205004', '液体', '7.00元', '10', '26', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('369', '青苹果味汽水', '370', '369', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '美年达', '', '', '500ml/瓶', 'GB/T10792', '20110908', 'LYLYC2012050009', '液体', '2.9元/瓶', '93', '93', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('370', '风冈锌硒绿茶', '371', '370', '5', '308', '1', '3052', '179', '176', '676', '1', '0', '野春菇', '特级', '', '200克/盒', 'DB52/489-2005', '2012年03月01日', 'CHAYC2012050009', '固态', '90元/盒', '50盒', '60盒', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('371', '可口可乐汽水', '372', '371', '5', '288', '4', '3052', '217', '10', '301', '4', '2', '可口可乐', '', '', '500ml/瓶', 'GB/T10792', '20120311', 'LYLYC2012050010', '液体', '2.8元/瓶', '20瓶', '111瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('372', '重都香茗茉莉花茶', '373', '372', '5', '308', '1', '3052', '145', '177', '682', '1', '0', '重都', '特级', '', '200克/袋', 'DB52/20', '20120101', 'CHAYC2012050010', '固态', '35元/袋', '9袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('373', '青苹果味汽水', '374', '373', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '美年达', '', '', '500毫升/瓶', 'GB/T10792', '20111025', 'LYLYC201205-0011', '液体', '3.00元/瓶', '36瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('374', '香卤鸡翅', '375', '374', '5', '158', '4', '3052', '145', '178', '212', '3', '2', '一米阳光', '', '', '90克/袋', 'DB50/T322', '2012/02/25', '赤水SSQTC2012050001', '固态', '7.5元/袋', '13袋', '25袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('375', '卤香鸡翅', '376', '375', '5', '1261', '4', '3052', '145', '35', '212', '3', '3', '清友园', '', '', '100克/袋', 'GB/T23586', '2012/02/06', '赤水SSQTC2012050002', '固态', '6.9元/袋', '14袋', '25袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('376', '橙味汽水', '377', '376', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '美年达', '', '', '500毫升/瓶', 'GB/T10792', '20111118', 'LYLYC201205-0012', '液体', '3.00元/瓶', '36瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('377', '可口可乐', '378', '377', '5', '288', '4', '3052', '217', '47', '301', '4', '2', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120301', 'LYLYC201205-0013', '液体', '3.00元/瓶', '36瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('378', '卤香鸡翅', '379', '378', '5', '1261', '4', '3052', '145', '84', '212', '3', '2', '有友', '', '', '12克/袋', 'DB50/T322', '2012\\04\\15', '赤水SSQTC2012050003', '固态', '10.5元/袋', '12袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('379', '香酥鸡爪', '380', '379', '5', '1261', '4', '3052', '145', '179', '212', '3', '2', '风靡·情', '', '', '135克/袋', 'GB/T23586', '2012.2.16', '赤水SSQTC2012050004', '固态', '5.5元/袋', '12袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('380', '雪碧', '381', '380', '5', '288', '4', '3052', '217', '10', '301', '4', '2', '雪碧', '', '', '500毫升/瓶', 'GB/T10792', '20120116', 'LYLYC201205-0014', '液体', '2.9元/瓶', '12瓶', '12瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('381', '泡凤爪', '382', '381', '5', '1261', '4', '3052', '145', '84', '212', '3', '2', '有友', '', '', '180克/袋', 'DB50/294', '2012/04/07', '赤水SSQTC201205-0005', '固态', '9.9元/袋', '12袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('382', '饮用天然泉水', '383', '382', '5', '1526', '4', '3052', '237', '180', '292', '2', '1', '欣龍', '', '', '18.9L/桶', 'DB52/434', '2012年5月7日', 'LYSHC201205005', '液体', '7.00元', '3', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('383', '香辣鸡排', '384', '383', '5', '1261', '4', '3052', '145', '141', '212', '3', '2', '立香香', '', '', '88克/袋', 'Q/RMKF1', '2011/12/01', 'SSQTC2012050006', '固态', '6.5元/袋', '12袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('384', '冰爽柠檬味汽水', '385', '384', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '', '', '', '500毫升/瓶', 'GB/T10792', '20120208', 'LYLYC201205-0015', '液体', '2.9元/瓶', '12瓶', '12瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('385', '酱香鹅翅', '386', '385', '5', '1261', '4', '3052', '145', '181', '212', '3', '2', '乖媳妇', '', '', '120克/袋', 'Q/CZQ', '2012/2/24', 'SSQTC2012050007', '固态', '7.4元/袋', '15袋', '15袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('386', '青苹果味汽水', '387', '386', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '美年达', '', '', '500毫升/瓶', 'GB/T10792', '20110908', 'LYLYC2012050016', '液体', '3元/瓶', '10瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('387', '香辣猪拱嘴', '388', '387', '5', '1260', '4', '3052', '145', '182', '212', '3', '3', '七叔公', '', '', '240克/袋', 'GB/T23586', '2012/2/3', 'SSQTC2012050008', '固态', '15.8元/袋', '9袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('388', '香卤凤翅', '389', '388', '5', '1261', '4', '3052', '145', '3', '212', '3', '2', '永健', '', '', '120克/袋', 'DB50/T322', '2012/02/11', 'SSQTC2012050009', '固态', '7.8元/袋', '10袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('389', '百事可乐', '390', '389', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '百事可乐', '', '', '500毫升/瓶', 'GB/T10792', '20120225', 'LYLYC2012050017', '液体', '2.8元/瓶', '34瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('390', '饮用天然泉水', '391', '390', '5', '1526', '4', '3052', '237', '172', '292', '2', '1', '大板水', '', '', '18.9L/桶', 'DB52/434', '2012年05月08日', 'LYSHC2012050006', '液体', '6.00元', '43', '163', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('391', '香辣猪蹄', '392', '391', '5', '1260', '4', '3052', '145', '133', '212', '3', '2', '甲比特', '', '', '130克/袋', 'DB43/160.2-2009', '2012/04/09', 'SSQTC2012050010', '固态', '6.5元/袋', '14袋', '14袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('392', '清爽柠檬味汽水', '393', '392', '5', '288', '4', '3052', '217', '10', '301', '4', '2', '雪碧', '', '', '500毫升/瓶', 'GB/T10792', '20120228', 'LYLYC2012050018', '液体', '3.00元/瓶', '24瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('393', '清爽柠檬味汽水', '394', '393', '5', '288', '4', '3052', '217', '10', '301', '4', '2', '雪碧', '', '', '500毫升/瓶', 'GB/T10792', '20120119', 'LYLYC2012050019', '液体', '3.00元/瓶', '12瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('394', '饮用天然泉水', '395', '394', '5', '1526', '4', '3052', '237', '183', '292', '1', '1', '白沙清泉', '', '', '18.9L/桶', 'DB52/434', '2012年5月8日', 'LYSHC2012050007', '液态', '5元/桶', '20桶', '30桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('395', '美年达橙味', '396', '395', '5', '288', '4', '3052', '217', '11', '301', '4', '2', '美年达', '', '', '330毫升/瓶', 'GB/T10792', '20111023', 'LYLYC2012050020', '液体', '2.5元/瓶', '24瓶', '72瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('396', '饮用天然泉水', '397', '396', '5', '1526', '4', '3052', '237', '100', '292', '1', '1', '凤凰山', '', '', '18.9L/桶', 'DB52/434', '2012.05.08', 'LYSHC2012050008', '液态', '6元/桶', '50桶', '120桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('397', '饮用天然泉水', '398', '397', '5', '1526', '4', '3052', '237', '185', '292', '1', '1', '浩然山泉', '', '', '18.9L/桶', 'DB52/434', '2012年5月8日', 'LYSHC2012050009', '液态', '6元/桶', '16桶', '40桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('398', '饮用天然泉水', '399', '398', '5', '1526', '4', '3052', '237', '186', '292', '1', '1', '冰极零', '', '', '18.9L/桶', 'DB52/434', '2012年05月07日', 'LYSHC2012050010', '液态', '6元/桶', '30桶', '56桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('399', '清味园香酥猪蹄', '400', '399', '6', '1260', '4', '5321', '145', '37', '225', '2', '1', '清味园', '', '', '140克/袋', 'GB/T23586-2009', '2012/02/01', 'SSQTG2012060001', '待检', '6.5', '5袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('400', '桶装水', '401', '400', '5', '1526', '4', '3052', '237', '170', '292', '2', '1', '甘露康', '', '', '18.9L/桶', 'GB8537', '2012.05.04', 'LYSHC201205001', '液态', '10元/桶', '9桶', '9桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('401', '桶装饮用天然泉水', '402', '401', '5', '1526', '4', '3052', '237', '172', '292', '2', '2', '大板水', '', '', '18.9L/桶', 'DB52/434', '2012年05月08日', 'LYSHC201205002', '液态', '8元/桶', '35桶', '42桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('402', '麻辣鸡腿', '403', '402', '6', '1261', '4', '5321', '145', '179', '201', '2', '1', '香勃禄', '', '', '95克/袋', 'GB/T23586', '2012.2.17', 'SSQTG2012060002', '待检', '3.8', '5袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('403', '桶装矿物质水', '404', '403', '4', '1526', '2', '3', '237', '187', '294', '1', '1', '娃哈哈', '', '', '18.9升/桶', 'Q/WHJ0273', '20120503', 'LYSHA2012060001', '液态', '16元/桶', '50桶', '100桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('404', '香卤凤翅', '405', '404', '6', '1261', '4', '5321', '145', '3', '212', '2', '1', '永健', '', '', '120克/袋', 'DB50/322', '2012/02/11', 'SSQTG2012060003', '待检', '7.5', '5袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('405', '桶装纯净水', '406', '405', '4', '1525', '2', '3', '237', '187', '293', '1', '1', '娃哈哈', '', '', '18.9升/桶', 'GB 17323', '20120504', 'LYSHA2012060002', '液体', '14.0', '50桶', '60桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('406', '美优特桶装水', '407', '406', '4', '532', '2', '3', '237', '188', '293', '1', '1', '美优特', '', '', '18.9升/桶', 'GB 17323', '20120507', 'LYSHA2012060003', '液体', '10.0', '60桶', '100桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('407', '长生腿', '408', '407', '6', '1260', '4', '5321', '145', '189', '240', '2', '1', '泸源', '', '', '35克/袋', 'GB/T23586-2009', '2012.2.2', 'SSQTG2012060004', '待检', '1.5', '5袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('408', '香辣猪蹄', '409', '408', '6', '1260', '4', '5321', '145', '38', '194', '2', '1', '香滋乡味', '', '', '140克/袋', 'GB/T23586', '2012年02月19日', 'SSQTG2012060005', '待检', '6.5', '5袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('409', '饮用矿泉水', '410', '409', '4', '1526', '1', '3', '237', '190', '294', '1', '1', '假日', '', '', '18.9升/桶', 'Q/TH 0001S-2011', '2012年5月2日', 'LYSHA2012060004', '液态', '9.0', '2桶', '10桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('410', '盐焗脆翅', '411', '410', '6', '1261', '4', '5321', '145', '3', '194', '2', '1', '永健', '', '', '115克/袋', 'GB/T23586-2009', '2012/02/02', 'SSQTG2012060006', '待检', '7.5', '8袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('411', '泡椒凤爪', '412', '411', '6', '1261', '4', '5321', '145', '3', '194', '3', '2', '永健', '', '', '80克/袋', 'DBS50/004', 'JB2012/02/12', 'SSQTG2012060007', '待检', '5.0', '8袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('412', '饮用纯净水', '413', '412', '4', '1525', '2', '3', '237', '191', '293', '1', '1', '霸龙', '', '', '18.9升/桶', 'GB 17323-1998', '20120507', 'LYSHA2012060005', '液体', '10.0', '4桶', '100桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('413', '泡凤爪', '414', '413', '6', '1261', '4', '5321', '145', '192', '194', '3', '2', '有友', '', '', '100克/袋', 'DB50/294', 'A102012/03/26', 'SSQTG2012060008', '待检', '5.0', '6袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('414', '北极熊饮用纯净水', '415', '414', '4', '1525', '2', '3', '237', '193', '293', '1', '1', '北极熊', '', '', '18.9升/桶', 'GB 17323', '20120421', 'LYSHA2012060006', '液体', '10.0', '12桶', '20桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('415', '香酥猪蹄', '416', '415', '6', '1260', '4', '5321', '145', '145', '194', '3', '1', '柯想', '', '', '140克/袋', 'GB/T23586-2009', '2012/02/10', 'SSQTG2012060009', '待检', '6.0', '10袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('416', '盐焗鸡腿', '417', '416', '6', '1261', '4', '5321', '145', '194', '194', '3', '2', '陈老根', '', '', '110克/袋', 'DB43/160.2', '2012/02/10', 'SSQTG2012060010', '待检', '5.5', '10袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('417', '天瀑饮用天然泉水', '418', '417', '6', '1526', '4', '5321', '237', '195', '292', '1', '1', '天瀑', '', '', '18.9L/桶', 'DB52/434', '20120508', 'LYSHG2012060001', '待检', '7', '30桶', '40桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('418', '杨梅山泉', '419', '418', '4', '1526', '1', '3', '217', '196', '292', '1', '1', '杨梅山泉', '', '', '7.5升/瓶', 'DB52/434', '20120506', 'LYSHA2012060007', '液态', '3.0', '3瓶', '3瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('419', '天然饮用泉水', '420', '419', '6', '1526', '1', '5321', '237', '197', '292', '1', '1', '滴水岩', '', '', '18.9L/桶', 'DB52/434', '20120506', 'LYSHG2012060002', '待检', '8', '20桶', '40桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('420', '饮用山泉水', '421', '420', '6', '1526', '4', '5321', '237', '198', '292', '1', '1', '黔中缘', '', '', '18.9L/桶', 'DB52/434', '20120506', 'LYSHG2012060003', '待检', '10', '20桶', '60桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('421', '天然泉水', '422', '421', '6', '1526', '4', '5321', '237', '199', '292', '1', '1', '天源', '', '', '18.9L/桶', 'DB52/434-2007', '20120502', 'LYSHG2012060004', '待检', '8.0', '20桶', '40桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('422', '黔山秀水饮用天然矿泉水', '423', '422', '4', '1526', '1', '3', '237', '128', '292', '1', '1', '黔山秀水', '', '', '18.9L', 'GB 8537-2008', '2012年/05月/08日', 'LYSHA2012060008', '液态', '13.0', '30桶', '40桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('423', '饮用泉水', '424', '423', '6', '1526', '4', '5321', '237', '200', '292', '1', '1', '凉泉', '', '', '18.9L/桶', 'DB52/434', '20120508', 'LYSHG2012060005', '待检', '12', '10桶', '40桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('424', '天然饮用泉水', '425', '424', '6', '1526', '4', '5321', '237', '201', '292', '1', '1', '青苹果', '', '', '18.9L/桶', 'DB52/434', '20120508', 'LYSHG2012060006', '待检', '8', '10桶', '40桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('425', '百灵鸟纯净水', '426', '425', '6', '532', '4', '5321', '237', '202', '293', '1', '1', '百灵鸟', '', '', '18.9升/桶', 'GB17323', '2012.5.2', 'LYSHG2012060007', '待检', '10', '20桶', '50桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('426', '纯净水', '427', '426', '6', '532', '4', '5321', '237', '202', '293', '1', '1', '百灵鸟', '', '', '18.9升', 'GB17323', '2012年5月8日', 'LYSHG2012060008', '待检', '10', '16桶', '80桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('427', '北极熊纯净水', '428', '427', '6', '1525', '4', '5321', '237', '114', '293', '1', '1', '北极熊', '', '', '18.9/L/桶', 'GB17323', '20120504:4362C', 'LYSHG2012060009', '待检', '15.0', '20桶', '50桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('428', '天瀑饮用天然泉水', '429', '428', '6', '1526', '4', '5321', '237', '195', '292', '1', '1', '', '', '', '18.9L/桶', 'DB52/434', '20120508', 'LYSHG2012060010', '待检', '8.0', '20桶', '50桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('429', '茶叶', '430', '429', '6', '308', '3', '5321', '145', '203', '675', '1', '1', '神仙乐', '一级', '', '250克/袋', 'ZBBB5002-88', '2012年5月', 'NCCYG2012060001', '待检', '15.0', '10袋', '40袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('430', '雲雾绿茶', '431', '430', '6', '308', '3', '5321', '145', '204', '675', '2', '1', '红湖', '特级', '', '250g/袋', 'GB/T14456-93', '2012年1月7日', 'NCCYG2012060002', '待检', '15.0', '10袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('431', '茶叶', '432', '431', '6', '308', '3', '5321', '141', '205', '675', '2', '1', '都匀毛峰', '', '', '散装', '', '2012年', 'NCCYG2012060003', '待检', '70', '17斤', '20斤', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('432', '苦丁茶', '433', '432', '6', '308', '3', '5321', '145', '206', '675', '2', '1', '河江', 'A级', '', '200克/袋', 'B52/454-2004', '2011年8月23日', 'NCCYG2012060004', '待检', '7', '50袋', '100袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('433', '碧螺春', '434', '433', '6', '308', '3', '5321', '145', '207', '675', '2', '1', '天强', '一级', '', '250g/袋', 'Q/FQCY17-2001', '2011年8月01日', 'NCCYG2012060005', '待检', '15.0', '20袋', '50袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('434', '铁观音', '435', '434', '6', '308', '2', '5321', '141', '208', '675', '1', '1', '品馨', '一级', '', '250克/包', 'GB19898-2004', '2011年12月2日', 'NCCYG2012060006', '待检', '50.0', '2包', '5包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('435', '玉龙饮用矿泉水', '436', '435', '4', '1526', '2', '3', '237', '209', '291', '1', '1', '玉龙', '', '', '10L', 'GB17323', '2012.05.08', 'LYSHA2012060009', '液态', '8.0', '2桶', '5桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('436', '凯维饮用泉水', '437', '436', '4', '532', '2', '3', '237', '223', '292', '1', '1', '凯维', '', '', '18.9L', 'DB52/434-2007', '2012.05.06', 'LYSHA2012060010', '液态', '9.0', '11桶', '50桶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('437', '手工毛尖', '438', '437', '6', '308', '2', '5321', '141', '159', '675', '1', '1', '世纪福', '一级', '', '200克/包', 'GB/T14456', '2011年9月26日', 'NCCYG2012060007', '待检', '45.0', '4包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('438', '碧螺春', '439', '438', '6', '308', '2', '5321', '141', '211', '675', '1', '1', '大地迎春', '特级', '', '250克/包', 'GB/T14456-1993', '2011年09月26日', 'NCCYG2012060008', '待检', '40', '5包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('440', '早春绿茶', '441', '440', '6', '308', '2', '5321', '141', '211', '675', '2', '1', '大地迎春', '', '', '100克/包', 'Q/HDDYC13-2006', '2011年2月4日', 'NCCYG2012060009', '待检', '18.0', '7包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('441', '都匀毛尖', '442', '441', '6', '308', '2', '5321', '141', '124', '675', '2', '1', '千古韵', '特级', '', '100克/包', 'GB/Y20359', '2012年01月03日', 'NCCYG2012060010', '待检', '18', '6包', '10包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('446', '雪碧', '447', '446', '6', '288', '4', '5321', '252', '47', '298', '2', '1', '雪碧', '', '', '330毫升/听', 'GB/T10792', '20120315', 'LYLYG2012060001', '待检', '2', '10听', '20听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('447', '百事可乐', '448', '447', '6', '288', '4', '5321', '252', '11', '298', '2', '1', '百事可乐', '', '', '330ml/听', 'GB/T10792', '20111215D', 'LYLYG2012060002', '待检', '2.0', '5听', '20听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('448', '可口可乐', '449', '448', '6', '288', '4', '5321', '252', '47', '298', '2', '1', '可口可乐', '', '', '330ml/听', 'GB/T10792', '20120304', 'LYLYG2012060003', '待检', '2.0', '10听', '20听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('449', '雪碧', '450', '449', '6', '288', '4', '5321', '217', '10', '298', '2', '1', '雪碧', '', '', '500毫升/瓶', 'GB/T10792', '20120327', 'LYLYG2012060004', '待检', '2.5', '10瓶', '20瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('450', '可口可乐', '451', '450', '6', '288', '4', '5321', '217', '10', '298', '2', '1', '可口可乐', '', '', '500ml/瓶', 'GB/T10792', '20120213', 'LYLYG2012060005', '待检', '2.5', '10瓶', '20瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('451', '茶叶', '452', '451', '4', '308', '1', '3', '141', '212', '676', '1', '', '黔萃园', '特级', '', '250克/包', 'DB52/T447', '20110504', 'NCCYA2012050001', '固体', '28.0', '20包', '40包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('452', '美年达橙味', '453', '452', '6', '288', '4', '5321', '169', '11', '298', '4', '2', '百事', '', '', '330毫升/罐', 'GB/T10792', '20120210D', 'LYLYG2012060006', '待检', '3.0', '20罐', '50罐', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('453', '可口可乐', '454', '453', '6', '288', '4', '5321', '217', '10', '298', '4', '2', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20111012CS1', 'LYLYG2012060007', '待检', '3.3', '20瓶', '50瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('454', '羊艾绿茶', '455', '454', '4', '308', '1', '3', '141', '213', '676', '1', '', '羊艾', '羊艾绿茶（一级）', '', '250克/包', 'Q/GZYA0004S-2010', '2012年3月2日', 'NCCYA2012050002', '固体', '17.8', '20包', '40包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('455', '7喜', '456', '455', '6', '288', '4', '5321', '217', '11', '298', '4', '2', '7喜', '', '', '500毫升/瓶', 'GB/T10792', '20110906B', 'LYLYG2012060008', '待检', '3.0', '20瓶', '50瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('456', '羊艾高绿茶', '457', '456', '4', '308', '1', '3', '145', '213', '676', '1', '', '羊艾', '高绿茶', '', '250g/袋', 'Q/GYCH01', '20120102', 'NCCYA2012050003', '固体', '36.0', '20包', '40包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('457', '百事可乐', '458', '457', '6', '288', '4', '5321', '169', '11', '298', '4', '2', '百事', '', '', '300ml/罐', 'GB/T10792', '20111022D', 'LYLYG2012060009', '待检', '3.0', '20罐', '50罐', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('458', '湄潭绿茶', '459', '458', '4', '308', '1', '3', '141', '214', '676', '1', '', '千古韵', '特级', '', '100克/包', 'DB52/T442.1', '20120302', 'NCCYA2012050004', '固态', '9.6', '20包', '40包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('459', '百事可乐', '460', '459', '6', '288', '4', '5321', '217', '11', '298', '1', '1', '百事', '', '', '500ml/瓶', 'GB/T10792', '20120228C', 'LYLYG2012060010', '待检', '3.0', '20瓶', '50瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('460', '茉莉花茶', '461', '460', '4', '308', '1', '3', '141', '215', '682', '1', '', '新桥', '毛尖', '', '250克/包', 'Q/XQ01-1995', '2012年2月9日', 'NCCYA2012050005', '固体', '8.0', '20包', '40包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('461', '雪碧', '462', '461', '6', '288', '4', '5321', '217', '59', '298', '4', '2', '雪碧', '', '', '300ml/瓶', 'GB/T10792', '180GXA02', 'LYLYG2012060011', '待检', '2.0', '16瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('462', '碧螺春', '463', '462', '4', '308', '1', '3', '141', '216', '676', '1', '', '天强', '一级', '', '250克/包', 'GB/T 14456.1-2008', '20120308', 'NCCYA2012050006', '固体', '8.0', '20包', '40包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('463', '石乳毛尖茉莉花茶', '464', '463', '4', '308', '1', '3', '141', '217', '682', '1', '', '石乳', '毛尖', '', '100克/包', 'Q/NCX01', '20110701', 'NCCYA2012050007', '固体', '6.0', '10包', '50包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('464', '可口可乐', '465', '464', '6', '288', '4', '5321', '217', '10', '298', '2', '1', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '20111205', 'LYLYG2012060012', '待检', '3.0', '10瓶', '15瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('465', '石乳银毫花茶', '466', '465', '4', '308', '1', '3', '141', '217', '682', '2', '', '石乳', '银毫', '', '100克/包', 'Q/NCX01', '20110701', 'NCCYA2012050008', '固体', '7.0', '15包', '50包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('466', '雪碧', '467', '466', '6', '288', '4', '5321', '217', '48', '298', '2', '1', '雪碧', '', '', '500毫升/瓶', 'GB/T10792', '20120216J', 'LYLYG2012060013', '待检', '3.0', '20瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('467', '毛尖茶都匀特产', '468', '467', '4', '308', '1', '3', '145', '218', '676', '1', '', '壶一春', '特级', '', '200克/袋', '', '20120102', 'NCCYA2012050009', '固体', '26.5', '3包', '5包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('468', '百事可乐', '469', '468', '6', '288', '3', '5321', '217', '11', '298', '2', '1', '百事', '', '', '500毫升/瓶', 'GB/10792', '20120419A', 'LYLYG2012060014', '待检', '3.0', '40瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('469', '苦丁茶', '470', '469', '4', '308', '1', '3', '145', '218', '676', '1', '', '壶一春', '特级', '', '200克/袋', '', '2011年7月16日', 'NCCYA2012050010', '固体', '7.5', '3包', '5包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('470', '雪碧', '471', '470', '6', '288', '4', '5321', '169', '47', '298', '4', '2', '雪碧', '', '', '330毫升/罐', 'GB/T10792', '190HBA02', 'LYLYG2012060015', '待检', '2.5', '15瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('471', '清爽柠檬水汽水', '472', '471', '6', '288', '4', '5321', '217', '48', '298', '4', '2', '雪碧', '', '', '500ml/瓶', 'GB/T10792', '20120225', 'LYLYG2012060016', '待检', '3.0', '10瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('472', '可口可乐', '473', '472', '6', '288', '4', '5321', '217', '42', '298', '4', '2', '百事', '', '', '500ml/瓶', 'GB/T10792', '20120329', 'LYLYG2012060017', '待检', '3.0', '4瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('473', '百事可乐', '474', '473', '6', '288', '4', '5321', '217', '11', '298', '4', '2', '百事', '', '', '500毫升/瓶', 'GB/T10792', '20120317C', 'LYLYG2012060018', '待检', '3.0', '11瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('474', '美年达葡萄味汽水', '475', '474', '6', '288', '4', '5321', '217', '11', '298', '4', '2', '百事', '', '', '500毫升/瓶', 'GB/T10792', '20120218 B', 'LYLYG2012060019', '待检', '3.0', '14瓶', '24瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('475', '可口可乐汽水', '476', '475', '6', '288', '4', '5321', '217', '219', '298', '4', '2', '可口可乐', '', '', '500毫升/瓶', 'GB/T10792', '004042CS1', 'LYLYG2012060020', '待检', '3.0', '40瓶', '48瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('476', '黑糯花生粽', '497', '476', '10', '1528', '1', '9567', '145', '225', '401', '3', '2', '三全', '无', '', '520g/袋', 'Q/ZSQ0007S', '20120223合格A881', 'MMQTE2012050001', '冷冻固体', '12.9元/袋', '6袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('477', '泡椒牛肉粽', '498', '477', '10', '1527', '4', '9567', '145', '226', '401', '3', '2', '三全', '', '', '520g/袋', 'SB/T 10412', '20120428合格H20', 'MMQTE2012050002', '固体', '12.9元/袋', '6袋', '20袋', '无', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('478', '板栗粽', '499', '478', '10', '135', '4', '9567', '145', '225', '401', '3', '2', '三全', '', '', '520g/袋', 'SB/T10412', '20120321合格A801', 'MMQTE2012050003', '固体', '15.8元/袋', '30袋', '200袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('479', '蜜枣棕', '500', '479', '10', '1528', '4', '9567', '145', '227', '401', '3', '2', '三全', '无', '', '520g/袋', 'SB/T 10412', '20120312合格D01', 'MMQTE2012050004', '冷冻固体', '15.8元/袋', '30袋', '200袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('480', '叉烧粽', '501', '480', '10', '1527', '4', '9567', '145', '226', '401', '3', '2', '三全', '', '', '520g/袋', 'SB/T 10412', '20120507合格H20', 'MMQTE2012050005', '冷冻固体', '15.8元/袋', '30袋', '200袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('481', '贞丰布乡粽', '502', '481', '10', '1527', '4', '9567', '145', '228', '401', '5', '3', '布乡', '', '', '200克/袋', '', '2012年5月20日', 'MMQTE2012050006', '真空固体', '5.0元/袋', '320袋', '340袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('482', '粽子', '503', '482', '10', '1527', '4', '9567', '145', '229', '401', '6', '2', '', '', '', '散装', '无', '2012年5月28日', 'MMQTE2012050007', '固体', '6.0元/袋', '30袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('483', '杂子板栗粽', '504', '483', '10', '1527', '1', '9567', '145', '230', '401', '3', '2', '胖四娘', '', '', '180g/袋', 'SB/T10377', '2012年05月27日', 'MMQTE2012050008', '固体', '5.5元/袋', '130袋', '150袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('484', '贞丰布香粽', '505', '484', '10', '1527', '4', '9567', '145', '231', '401', '3', '2', '布乡', '', '', '450克/袋', '无', '2012年5月29日', 'MMQTE2012050009', '固体', '10元/袋', '20袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('485', '杂子板栗粽', '506', '485', '10', '1527', '4', '9567', '145', '230', '401', '3', '2', '胖四娘', '', '', '180g/袋', 'SB/T10377', '2012年05月27日', 'MMQTE2012050010', '固体', '5.0元/袋', '150袋', '800袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('486', '舒氏粽子', '507', '486', '10', '1527', '4', '9567', '153', '232', '401', '8', '3', '', '', '', '散装', '', '20120601', 'MMQTE2012050011', '固体', '5元/袋', '', '300个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('487', '刘三妹粽粑', '508', '487', '10', '1527', '4', '9567', '153', '233', '401', '8', '3', '', '', '', '散装', '无', '2012年6月01日', 'MMQTE2012050012', '固体', '5元/个', '', '300个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('488', '真空板栗猪肉粽', '509', '488', '8', '1529', '4', '7363', '145', '234', '401', '3', '2', '三全凌', '合格', '', '300g/袋', 'Q/ZSQ0007S', '20120302A19', 'MMQTD2012060001', '固体', '14.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('489', '八宝粽', '510', '489', '8', '1528', '1', '7363', '145', '235', '52', '3', '2', '三全', '合格', '', '520克/袋', 'SB/T10412', '20120320D01', 'MMQTD2012060002', '固体', '11.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('490', '速冻板栗猪肉粽', '511', '490', '8', '1527', '1', '7363', '179', '226', '401', '3', '2', '三全凌', '合格', '', '400g/盒', 'Q/ZSQ0007S', '20120302H071', 'MMQTD2012060003', '固体', '15.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('491', '叉烧粽', '512', '491', '8', '1527', '4', '7363', '145', '226', '401', '3', '2', '三全', '合格', '', '520克/袋', 'SB/T10412', '20120512H20', 'MMQTD2012060004', '固体', '', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('492', '金丝蜜枣棕', '513', '492', '8', '1528', '4', '7363', '145', '225', '401', '3', '2', '三全', '合格', '', '300克/袋', '', '20120302A18', 'MMQTD2012060005', '固体', '11.0', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('493', '咸粽板栗肉粽', '514', '493', '8', '1529', '1', '7363', '145', '236', '401', '3', '2', '思念', '', '', '500g/袋', 'SB/T10412', '20120404 60S', 'MMQTD2012060006', '固体', '16.8', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('494', '香米香肠粽', '515', '494', '7', '1527', '1', '10926', '145', '237', '52', '4', '2', '贵州龍', '', '', '200克/袋', 'SB/T10377', '2012年04月23日', 'MMQTF2012050001', '固态', '12包', '15包', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('495', '香米红枣粽', '516', '495', '7', '1529', '4', '10926', '145', '237', '52', '4', '2', '贵州龍', '', '', '200克/袋', 'SB/T10377', '2012年04月23日', 'MMQTF2012050002', '固态', '12', '15包', '20包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('496', '香米老腊肉粽', '517', '496', '7', '1527', '4', '10926', '145', '237', '52', '4', '2', '贵州龍', '', '', '200克/袋', 'SB/T10377', '2012年04月23日', 'MMQTF2012050003', '固态', '12', '15包', '20包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('497', '粽粑', '518', '497', '10', '1527', '4', '9567', '153', '238', '401', '8', '3', '熊大妈', '', '', '200g/个', 'Q/XDM01-2006', '2012年06月01日', 'MMQTE2012050013', '固体', '5.0元/个', '无', '300个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('498', '香米五彩咸肉粽', '519', '498', '7', '1527', '4', '10926', '145', '237', '52', '4', '2', '贵州龍', '', '', '200克/袋', 'SB/T10377', '2012年04月25日', 'MMQTF2012050004', '固态', '12', '15包', '20包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('499', '香米红豆沙粽', '520', '499', '7', '1529', '4', '10926', '145', '237', '52', '4', '', '贵州龍', '', '', '200克/袋', 'SB/T10377', '2012年04月23日', 'MMQTF2012050005', '固态', '12', '15包', '20包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('500', '熊家粽粑', '521', '500', '10', '1527', '4', '9567', '153', '239', '401', '8', '2', '', '', '', '散装', 'Q/XJZB01-2006', '2012年06月01日', 'MMQTE2012050014', '固体', '5.0元/个', '无', '200个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('501', '紫米老腊肉粽', '522', '501', '7', '1527', '4', '10926', '145', '237', '52', '4', '2', '贵州龍', '', '', '200克/袋', 'SB/T10377', '2012年04月23日', 'MMQTF2012050006', '固态', '12', '1袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('502', '香米红豆沙粽', '523', '502', '7', '135', '4', '10926', '145', '237', '52', '4', '2', '贵州龍', '', '', '200克/袋', 'SB/T10377', '2012年04月23日', 'MMQTF2012050007', '固态', '12', '4袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('503', '津乾粽', '524', '503', '7', '1527', '4', '10926', '145', '240', '52', '4', '2', '', '', '', '240g/袋', 'SB/T10377', '2012年05月28日', 'MMQTF2012050010', '固态', '4.5', '350个', '400个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('504', '津乾粽', '525', '504', '7', '1528', '4', '10926', '145', '240', '52', '4', '2', '', '', '', '200g/袋', 'SB/T10377', '2012年05月28日', 'MMQTF2012050011', '固态', '3.5', '300个', '400个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('505', '蜜枣棕', '526', '505', '7', '1529', '4', '10926', '145', '235', '52', '4', '2', '三全', '', '', '520克/袋', 'SB/T10412', '20120328', 'MMQTF2012050012', '固态', '15.9', '30个', '50个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('506', '金丝蜜枣棕', '527', '506', '11', '1528', '4', '14940', '145', '225', '52', '5', '2', '三全', '', '', '300g/袋', 'SB/T 10377', '20120302', 'MMQTH2012060001', '固态', '15.9', '23袋', '25袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('507', '板栗猪肉真空龙舟粽', '528', '507', '11', '1529', '4', '14940', '145', '225', '52', '5', '2', '三全凌', '', '', '300g/袋', 'Q/ZSQ 0007S', '20120307', 'MMQTH2012060002', '固态', '15.9', '13袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('508', '蛋黄猪肉真空龙舟粽', '529', '508', '11', '1527', '4', '14940', '145', '225', '52', '5', '2', '三全凌', '', '', '300g/袋', 'Q/ZSQ 0007S', '20120303', 'MMQTH2012060003', '固态', '15.9', '18袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('509', '猪肉真空龙舟粽', '530', '509', '11', '135', '4', '14940', '145', '225', '52', '5', '2', '三全凌', '', '', '300g/袋', 'Q/ZSQ 0007S', '20120303', 'MMQTH2012060004', '固态', '15.9', '19袋', '25袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('510', '绿豆猪肉粽', '531', '510', '11', '1527', '4', '14940', '145', '225', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T10412', '20120427', 'MMQTH2012060005', '固态', '15.9', '20袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('511', '板栗粽', '532', '511', '11', '1528', '4', '14940', '145', '225', '52', '3', '2', '三全凌', '', '', '520g/袋', 'Q/ZSQ 0007S', '20120301', 'MMQTH2012060006', '固态', '15.9', '20袋', '25袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('512', '猪肉粽', '533', '512', '11', '1527', '4', '14940', '145', '225', '52', '3', '2', '三全凌', '', '', '520g/袋', 'Q/ZSQ 0007S', '20120225', 'MMQTH2012060007', '固态', '15.9', '99袋', '100袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('513', '猪肉粽', '534', '513', '11', '1527', '4', '14940', '145', '225', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T 10412', '20120328', 'MMQTH2012060008', '固态', '15.9', '20袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('514', '余记粽粑', '535', '514', '10', '135', '4', '9567', '153', '241', '401', '8', '3', '余六妹', '', '', '200克/个', 'SB/T10377-2004', '2012年6月1日', 'MMQTE2012050015', '固体', '5.0元/个', '无', '300个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('515', '菠萝棕', '536', '515', '11', '1529', '4', '14940', '145', '225', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T 10412', '20120323', 'MMQTH2012060009', '固态', '15.9', '99袋', '100袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('516', '熊家粽粑', '537', '516', '10', '1527', '4', '9567', '153', '239', '401', '8', '3', '', '', '', '散装', 'Q/XJZB01-2006', '2012年5月28日', 'MMQTE2012050016', '固体', '5元/个', '无', '200个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('517', '泡椒牛肉粽', '538', '517', '11', '1527', '4', '14940', '145', '225', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T 10412', '20120428', 'MMQTH2012060010', '固态', '15.9', '99袋', '100袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('518', '粽粑', '539', '518', '10', '1527', '4', '9567', '153', '238', '401', '8', '3', '熊大妈', '', '', '200g/个', 'D/XDM01-2006', '2012年05月28日', 'MMQTE2012050017', '固体', '5.0元/袋', '无', '300个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('519', '刘三妹粽粑', '540', '519', '10', '1527', '4', '9567', '153', '233', '401', '8', '3', '', '', '', '散装', '无', '2012年5月28日', 'MMQTE2012050018', '固体', '5.0元/个', '无', '200个', '无', '', '1', '2012.05.28', '0', '');
INSERT INTO `t_batch_sample` VALUES ('520', '杂子板栗粽', '541', '520', '10', '1527', '4', '9567', '153', '230', '401', '8', '3', '胖四娘', '', '', '100g/个', 'SB/T10377', '2012年05月27日', 'MMQTE2012050019', '固体', '5.0元/个', '无', '1500个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('521', '詹氏粽子', '542', '521', '10', '1527', '4', '9567', '153', '242', '401', '8', '3', '', '', '', '散装', 'GB19855', '2012年05月26日', 'MMQTE2012050020', '固体', '5.0元/个', '', '200个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('527', '鲜香猪肉粽', '548', '527', '12', '1527', '4', '1768', '145', '244', '52', '4', '2', '思念', '', '', '480g/袋', 'SB/T10412', '2012050705', 'MMQTB2012060001', '固态', '21.8元/袋', '10', '16', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('528', '红枣细沙粽', '549', '528', '12', '1529', '4', '1768', '145', '226', '52', '5', '2', '三全凌', '', '', '520g/袋', 'Q/ZSQ0007S', '20120312H202', 'MMQTB2012060002', '固态', '19.8元/袋', '73', '80', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('529', '香米引子粽', '550', '529', '12', '1529', '4', '1768', '145', '245', '52', '10', '5', '贵州龙', '', '', '200g/袋', 'SB/T10377', '2012年04月23日', 'MMQTB2012060003', '固态', '12元/袋', '4', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('530', '三全凌龙舟粽', '551', '530', '12', '1529', '4', '1768', '145', '225', '52', '2', '1', '三全凌', '', '', '750g/袋', 'Q/ZSQ0007S', '20120214A19', 'MMQTB2012060004', '固态', '18.6', '23', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('531', '三全龙舟粽', '552', '531', '12', '1527', '4', '1768', '145', '226', '52', '2', '1', '三全', '', '', '520g/袋', 'SB/T10412', '20120427H22', 'MMQTB2012060005', '固态', '', '', '', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('532', '思念香棕', '553', '532', '12', '1527', '4', '1768', '145', '236', '52', '2', '1', '思念', '', '', '500g/袋', 'SB/T10412', '2012042861S', 'MMQTB2012060006', '固态', '16.9', '260', '300', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('533', '金丝双枣粽', '554', '533', '12', '1529', '4', '1768', '145', '244', '52', '2', '1', '思念', '', '', '480g/袋', 'SB/T10412', '2012050405', 'MMQTB2012060007', '固态', '19.9', '45', '48', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('534', '速冻金丝蜜枣粽', '555', '534', '12', '1529', '4', '1768', '179', '226', '52', '2', '1', '三全凌', '', '', '400g/盒', 'Q/ZSQ0007S', '20120214H082', 'MMQTB2012060008', '固态', '13.9', '22', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('535', '速冻猪肉粽', '556', '535', '12', '1527', '4', '1768', '179', '226', '52', '6', '3', '三全凌', '', '', '400g/盒', 'Q/ZSQ0007S', '20120314H07', 'MMQTB2012060009', '固态', '16.8元/盒', '7', '16', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('536', '真空板栗猪肉粽', '557', '536', '12', '1529', '4', '1768', '145', '225', '52', '5', '2', '三全凌', '', '', '300g/袋', 'Q/ZSQ0007S', '20120308A191', 'MMQTB2012060010', '固态', '15.8元/袋', '23', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('537', '如意八宝粽', '558', '537', '12', '1529', '4', '1768', '145', '236', '52', '5', '2', '思念', '', '', '500g/袋', 'SB/T10412', '2012051261S', 'MMQTB2012060011', '固态', '19.8元/袋', '53', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('538', '香软紫糯粽', '559', '538', '12', '1529', '4', '1768', '145', '244', '52', '2', '1', '思念', '', '', '500g/袋', '', '2012030161A', 'MMQTB2012060012', '固态', '16.9元/袋', '8', '16', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('539', '醇香花生粽', '560', '539', '12', '1528', '4', '1768', '145', '236', '52', '5', '2', '思念', '', '', '500g/袋', 'SB/T10412', '2012040960S', 'MMQTB2012060013', '固态', '16.9元/袋', '8', '16', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('540', '板栗肉粽', '561', '540', '12', '1529', '4', '1768', '145', '236', '52', '5', '2', '思念', '', '', '500g/袋', 'SB/T10412', '2012040460S', 'MMQTB2012060014', '固态', '16.9元/袋', '8', '16', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('541', '泡椒牛肉粽', '562', '541', '12', '1527', '4', '1768', '145', '226', '52', '5', '2', '三全', '', '', '520g/袋', 'SB/T10412', '20120428', 'MMQTB2012060015', '固态', '15.9', '88', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('542', '蜜枣粽', '563', '542', '12', '1529', '4', '1768', '145', '243', '52', '5', '3', '三全', '', '', '520g/袋', 'SB/T10412', '20120416', 'MMQTB2012060016', '固态', '15.9元/袋', '87', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('543', '黑糯花生粽', '564', '543', '12', '1528', '4', '1768', '145', '225', '52', '4', '2', '三全凌', '', '', '520g/袋', 'Q/ZSQ0007S', '20120224', 'MMQTB2012060017', '固态', '17', '8', '12', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('544', '板栗肉粽', '565', '544', '12', '1529', '4', '1768', '145', '236', '52', '4', '2', '思念', '', '', '500g/袋', 'SB/T10412', '2012032860S', 'MMQTB2012060018', '固态', '16', '12', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('545', '龙舟粽黑糯花生粽', '566', '545', '12', '135', '4', '1768', '141', '225', '52', '4', '2', '三全', '', '', '520克/包', 'Q/ZSQ0007S', '20120223', 'MMQTB2012030019', '固态', '17元/包', '90包', '140包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('546', '龙舟紫薯蜜枣粽', '567', '546', '12', '135', '4', '1768', '141', '226', '52', '4', '2', '三全', '', '', '520克/包', 'SB/T10412', '20120504', 'MMQTB2012060020', '固态', '17元/包', '90包', '140包', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('548', '香米红豆沙粽', '569', '548', '4', '1529', '4', '3', '145', '245', '57', '4', '2', '贵州龙', '', '', '200克/袋', 'SB/T 10377', '20120423', 'MMQTA2012060001', '固体', '12', '9', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('549', '香米灰灰粽', '570', '549', '4', '1527', '4', '3', '145', '245', '57', '4', '2', '贵州龙', '', '', '200克/袋', 'SB/T 10377', '20120524', 'MMQTA2012060002', '固体', '12', '54', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('550', '蛋黄鲜肉粽子', '571', '550', '4', '1527', '4', '3', '145', '246', '57', '4', '2', '好利来', '', '', '100克/袋', 'Q/ SHLL0003S', '20120430', 'MMQTA2012060003', '固体', '18', '15', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('551', '枣蓉粽子', '572', '551', '4', '1529', '4', '3', '145', '246', '57', '4', '2', '好利来', '', '', '200克/袋', 'Q/SHLL 0003S', '20120427', 'MMQTA2012060004', '固体', '16', '15', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('552', '八宝如意粽', '573', '552', '4', '1527', '4', '3', '145', '247', '57', '4', '2', '潘祥记', '', '', '500克/袋', 'SB/T 10377-2004', '20120524', 'MMQTA2012060005', '固体', '23.8', '28', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('553', '台湾风味豆沙粽', '574', '553', '4', '1529', '4', '3', '145', '248', '57', '4', '2', '龙凤', '', '', '270克/袋', 'SB/T 10377', '20120402DC', 'MMQTA2012060006', '固体', '16.8', '54', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('554', '金丝蜜枣粽', '575', '554', '4', '1529', '4', '3', '145', '225', '57', '4', '2', '三全', '', '', '300克/袋', 'SB/T 10377', '20120302A18', 'MMQTA2012060007', '固体', '12.8', '20', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('555', '台湾风味栗子肉粽', '576', '555', '4', '1529', '4', '3', '145', '248', '57', '4', '2', '龙凤', '', '', '270克/袋', 'SB/T 10377', '20120423DC', 'MMQTA2012060008', '固体', '16.8', '53', '68', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('556', '真空板栗猪肉粽', '577', '556', '4', '1529', '4', '3', '145', '234', '57', '4', '2', '三全', '', '', '300g/袋', 'Q/2SQ0007S', '20120307', 'MMQTA2012060009', '固体', '12.8', '25', '40', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('557', '香橙豆沙粽', '578', '557', '4', '1529', '4', '3', '145', '244', '57', '4', '2', '思念', '', '', '600g/袋', 'SB/T 10377', '20120428', 'MMQTA2012060010', '固体', '24.8', '80', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('558', '香粽', '579', '558', '4', '1529', '4', '3', '179', '245', '57', '1', '1', '贵州龙', '', '', '810g/盒', 'SB /T 10377', '2012/05/15', 'MMQTA2012060011', '固体', '168', '10', '10', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('559', '鲜肉粽', '580', '559', '4', '1527', '4', '3', '145', '249', '57', '4', '1', '五芳斋', '', '', '140g/袋', 'SB/T 10377', '20120416', 'MMQTA2012060012', '固体', '12.8', '17', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('560', '细沙枣泥粽', '581', '560', '4', '1529', '4', '3', '145', '249', '57', '4', '1', '五芳斋', '', '', '280g/袋', 'SB/T 10377', '2012/04/21', 'MMQTA2012060013', '固体', '12.8', '20', '23', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('561', '迷你粽', '582', '561', '4', '1529', '4', '3', '145', '250', '57', '4', '1', '年润', '', '', '200g/袋', 'SB/T 10377', '20120516', 'MMQTA2012060014', '固体', '9.5', '20', '25', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('562', '蛋黄鲜肉粽', '583', '562', '4', '1527', '4', '3', '145', '249', '57', '4', '1', '五芳斋', '', '', '280g/袋', 'SB/T 10377', '2012/04/07', 'MMQTA2012060015', '固体', '18.8', '20', '28', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('563', '组合迷你粽', '584', '563', '4', '1529', '4', '3', '145', '250', '57', '4', '1', '年润', '', '', '200g/袋', 'SB/T 10377', '20120516', 'MMQTA2012060016', '固体', '11.9', '19', '21', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('564', '板栗肉粽', '585', '564', '4', '1529', '4', '3', '145', '236', '57', '4', '1', '思念', '', '', '500g/袋', 'SB/T 10412', '20120404', 'MMQTA2012060017', '固体', '18', '20', '25', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('565', '香润豆沙粽', '586', '565', '4', '1529', '4', '3', '145', '236', '57', '4', '1', '思念', '', '', '750g/袋', 'SB/T 10412', '20120506', 'MMQTA2012060018', '固体', '22', '19', '24', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('566', '三全蜜枣棕', '587', '566', '4', '1529', '4', '3', '145', '235', '57', '2', '1', '三全', '', '', '520g/袋', 'SB/T 10412', '20120416', 'MMQTA2012060019', '固体', '15.9', '20', '23', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('567', '三全猪肉粽', '588', '567', '4', '1527', '4', '3', '145', '226', '57', '1', '1', '三全', '', '', '750g/袋', 'SB/T 10412', '20120310', 'MMQTA2012060020', '固体', '19.9', '25', '28', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('568', '板栗猪肉真空龙舟粽', '589', '568', '11', '1529', '4', '14940', '145', '225', '52', '5', '2', '三全', '', '', '300g/袋', 'SB/T10377', '20120308', 'MMQTH2012060011', '固体', '15.90', '21袋', '21袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('569', '八宝粽', '590', '569', '11', '1528', '4', '14940', '145', '243', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T10412', '20120419', 'MMQTH2012060012', '固体', '15.9', '20袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('570', '紫薯蜜枣棕', '591', '570', '11', '1528', '4', '14940', '145', '226', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T10412', '20120514', 'MMQTH2012060013', '固体', '15.9', '40袋', '84袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('571', '黑糯花生粽', '592', '571', '11', '1528', '4', '14940', '145', '225', '52', '3', '2', '三全', '', '', '520g/袋', 'Q/ZSQ0007S', '20120304', 'MMQTH2012060014', '固体', '15.9', '40袋', '84袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('572', '豆沙粽', '593', '572', '11', '1528', '4', '14940', '145', '243', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T10412', '20120310', 'MMQTH2012060015', '固体', '15.9', '40袋', '84袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('573', '叉烧粽', '594', '573', '11', '1527', '4', '14940', '145', '226', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T10412', '20120513', 'MMQTH2012060016', '固体', '15.9', '20袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('574', '猪肉真空龙舟粽', '595', '574', '11', '1527', '4', '14940', '145', '234', '52', '5', '2', '三全凌', '', '', '300g/袋', 'Q/ZSQ0007S', '20120304', 'MMQTH2012060017', '固体', '15.9', '18袋', '18袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('575', '蜜枣棕', '596', '575', '11', '1528', '4', '14940', '145', '243', '52', '3', '2', '三全', '', '', '520g/袋', 'SB/T10412', '20120312', 'MMQTH2012060018', '固体', '15.90', '18袋', '18袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('576', '金丝蜜枣棕', '597', '576', '11', '1528', '4', '14940', '145', '225', '52', '5', '2', '三全', '', '', '300g/袋', 'SB/T10377', '20120301', 'MMQTH2012060019', '固体', '15.90', '23袋', '23袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('577', '猪肉粽', '598', '577', '11', '1527', '4', '14940', '145', '226', '52', '3', '2', '三全凌', '', '', '520g/袋', 'Q/ZSQ0007S', '20120227', 'MMQTH2012060020', '固体', '15.80', '10袋', '50袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('578', '龙舟粽紫糯人宝', '599', '578', '9', '1529', '4', '18578', '145', '225', '52', '4', '2', '三全', '-', '', '520g/袋', 'SB/T10412', '20120321合格A821', 'MMQTJ2012060001', '固态', '18.6', '10', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('579', '速冻猪肉粽', '600', '579', '9', '1527', '4', '18578', '179', '226', '52', '5', '2', '三全凌', '-', '', '400g/盒', 'Q/ZSQ0007S', '20120309H07', 'MMQTJ2012060002', '固态', '15.9', '15', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('580', '板栗猪肉粽', '601', '580', '9', '1529', '4', '18578', '145', '226', '52', '4', '2', '三全', '-', '', '520g/袋', 'SB/T10412', '20120508合格/H22', 'MMQTJ2012060003', '固态', '16.9', '20', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('581', '速冻金丝蜜枣粽', '602', '581', '9', '1529', '4', '18578', '145', '226', '52', '4', '2', '三全凌', '-', '', '400g/袋', 'Q/ZSQ0007S', '20120213合格H081', 'MMQTJ2012060004', '固态', '16.6', '20', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('582', '猪肉粽', '603', '582', '9', '1527', '4', '18578', '145', '226', '52', '4', '2', '三全凌', '-', '', '520g/袋', 'Q/ZSQ0007S', '20120223H082', 'MMQTJ2012060005', '固态', '16.6', '10', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('583', '细沙枣泥粽', '604', '583', '9', '1529', '4', '18578', '145', '249', '52', '5', '2', '五芳斋', '-', '', '280g/袋', 'SB/T10377', '2012/05/14/B7', 'MMQTJ2012060006', '固态', '15.8', '60', '80', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('584', '五谷粽', '605', '584', '9', '1529', '4', '18578', '145', '249', '52', '5', '2', '五芳斋', '-', '', '280g/袋', 'SB/T10377', '2012/05/04B5', 'MMQTJ2012060007', '固态', '15.8', '50', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('585', '豆沙粽', '606', '585', '9', '1529', '4', '18578', '145', '249', '52', '5', '1', '五芳斋', '-', '', '600g/袋', 'SB/T10377', '2012/05/04/B7', 'MMQTJ2012060008', '固态', '29.8', '60', '70', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('586', '板栗肉粽', '607', '586', '9', '1529', '4', '18578', '145', '236', '52', '5', '2', '思念', '-', '', '500g/袋', 'SB/T10412', '2012040460S', 'MMQTJ2012060009', '固态', '14.5', '40', '50', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('587', '香粽（经典猪肉粽）', '608', '587', '9', '1527', '4', '18578', '145', '236', '52', '5', '2', '思念', '-', '', '500g/袋', 'SB/T10412', '2012031960S', 'MMQTJ2012060010', '固态', '14.5', '50', '70', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('588', '速冻鲜肉粽', '609', '588', '9', '1527', '4', '18578', '145', '249', '52', '5', '2', '五芳斋', '-', '', '500g/袋', 'SB/T10412', '2012/05/13/B8', 'MMQTJ2012060011', '固态', '19.8', '70', '80', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('589', '板栗粽', '610', '589', '5', '1528', '4', '3052', '145', '225', '52', '5', '1', '三全', '', '', '520g/袋', 'SB/T10412', '20120505', 'MMQTC2012050001', '固态', '16.8元/袋', '95袋', '160袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('590', '猪肉粽', '611', '590', '5', '1527', '4', '3052', '145', '225', '52', '5', '1', '三全', '', '', '520g/袋', 'Q/ZSQ0007S', '20120314', 'MMQTC2012050002', '固态', '16.8元/袋', '84袋', '140袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('591', '速冻豆沙粽', '612', '591', '9', '1529', '4', '18578', '145', '249', '52', '5', '2', '五芳斋', '-', '', '500g/袋', 'SB/T10412', '2012/05/13/B8', 'MMQTJ2012060012', '固态', '19.8', '60', '80', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('592', '叉烧粽', '613', '592', '5', '1527', '4', '3052', '145', '225', '52', '5', '1', '三全', '', '', '520g/袋', 'SB/T10412', '20120511', 'MMQTC2012050003', '固态', '16.8元/袋', '84袋', '140袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('593', '经典鲜肉粽', '614', '593', '9', '1527', '4', '18578', '145', '251', '52', '5', '2', '五芳斋', '-', '', '200g/袋', 'SB/T10377', '2012/04/08/A/11', 'MMQTJ2012060013', '固态', '29.8', '50', '70', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('594', '八宝粽', '615', '594', '5', '1528', '4', '3052', '145', '225', '52', '5', '1', '三全', '', '', '520g/袋', 'SB/T10142', '20120301', 'MMQTC2012050004', '固态', '16.8元/袋', '95袋', '140袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('595', '米字号香粽（猪肉粽）', '616', '595', '9', '1527', '4', '18578', '145', '252', '52', '5', '1', '云鹤食品', '-', '', '750g/袋', 'SBT10377', '2012 04 15 02B', 'MMQTJ2012060014', '固态', '15.8', '4件', '4件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('596', '猪肉粽', '617', '596', '5', '1527', '4', '3052', '145', '253', '52', '10', '2', '', '', '', '散装', '', '2012.05.27', 'MMQTC2012050005', '固态', '5.00元/个', '7个', '60个', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('597', '米字号香粽豆沙粽', '618', '597', '9', '1529', '4', '18578', '145', '252', '52', '5', '2', '云鹤食品', '-', '', '500g/袋', 'SB/T10377', '2012040802B', 'MMQTJ2012060015', '固态', '12.5', '4件', '5件', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('598', '米字号香粽蜜枣粽', '619', '598', '9', '1529', '4', '18578', '145', '254', '52', '6', '2', '云鹤食品', '-', '', '500g/袋', 'SB/T10377', '2012051301A', 'MMQTJ2012060016', '固态', '12.5', '4件', '4件·', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('599', '经典猪肉粽', '620', '599', '9', '1527', '4', '18578', '145', '236', '52', '5', '2', '思念', '-', '', '500g/袋', 'SB/T10412', '2012031960S', 'MMQTJ2012060017', '固态', '16.8', '80', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('600', '栗子鲜肉粽', '621', '600', '9', '1529', '4', '18578', '145', '249', '52', '5', '2', '五芳斋', '-', '', '280g/袋', 'SB/T10377', '2012/05/14/B6', 'MMQTJ2012060018', '固态', '22.8', '15', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('601', '龙舟粽', '622', '601', '5', '1527', '4', '3052', '145', '225', '52', '5', '1', '三全', '', '', '300g/袋', 'Q/ZSQ0007S', '20120302', 'MMQTC2012050006', '固态', '15.8元/袋', '94袋', '100袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('602', '叉烧粽', '623', '602', '5', '1527', '4', '3052', '145', '248', '52', '5', '1', '龍鳯', '', '', '540g/袋', 'SB/T10377', '20120504', 'MMQTC2012050007', '固态', '28.5元/袋', '55袋', '60袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('603', '猪肉粽', '624', '603', '5', '1527', '4', '3052', '145', '252', '52', '5', '1', '图形商标', '', '', '500g/袋', 'SB/T10377', '20120420', 'MMQTC2012050008', '固态', '13.8元/袋', '52袋', '59袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('604', '豆沙粽', '625', '604', '5', '1528', '4', '3052', '145', '249', '52', '5', '1', '五芳斋', '', '', '600g/袋', 'SB/T10377', '2012/05/04', 'MMQTC2012050009', '固态', '28.8元/袋', '17袋', '20袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('605', '迷你白水棕', '626', '605', '5', '135', '4', '3052', '145', '249', '52', '7', '1', '五芳斋', '', '', '200g/袋', 'SN/T10377', '2012.04.16', 'MMQTC2012050010', '固态', '7.8元/袋', '27袋', '30袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('606', '板栗肉粽', '627', '606', '9', '1529', '4', '18578', '145', '236', '52', '5', '2', '思念', '-', '', '500g/袋', 'SB/T10412', '2012040460S', 'MMQTJ2012060019', '固态', '16.8', '80', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('607', '迷你鲜肉粽', '628', '607', '9', '1527', '4', '18578', '145', '249', '52', '5', '2', '五芳斋', '-', '', '200g/袋', 'SB/T10377', '2012/05/08/B5', 'MMQTJ2012060020', '固态', '19.8', '20', '35', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('608', '紫米红豆沙粽', '629', '608', '6', '1528', '4', '5321', '145', '245', '52', '2', '1', '贵州龙', '', '', '200克/袋', 'SB/T10377', '2012年05月13日', 'MMQTG2012060001', '待检', '12.0', '100', '150', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('609', '板栗粽', '630', '609', '6', '1528', '4', '5321', '145', '236', '52', '2', '1', '思念', '', '', '500g/袋', 'SB/T10412', '20120309615', 'MMQTG2012060002', '待检', '15.9', '50', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('610', '秘制叉烧粽', '631', '610', '6', '1527', '4', '5321', '145', '236', '52', '2', '1', '思念', '', '', '500克/袋', 'SB/T10412', '2012030660S', 'MMQTG2012060003', '待检', '15.9', '100', '150', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('611', '龙舟粽', '632', '611', '6', '1528', '4', '5321', '145', '235', '52', '2', '1', '三全凌', '', '', '520克/袋', 'SB/T10412', '20120214合格D01', 'MMQTG2012060004', '待检', '15.9', '100', '150', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('612', '蜜枣粽', '633', '612', '6', '1528', '4', '5321', '145', '226', '52', '2', '1', '三全凌', '', '', '400g/袋', 'Q/ZSQ0007S', '20120212合格H071', 'MMQTG2012060005', '待检', '15.9', '50', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('613', '板栗粽', '634', '613', '6', '1528', '4', '5321', '179', '226', '52', '2', '1', '三全凌', '', '', '400克/盒', 'Q/ZSQ0075', '20120303合格H071', 'MMQTG2012060006', '待检', '15.9', '50', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('614', '速冻紫糯八宝粽', '635', '614', '6', '1528', '4', '5321', '179', '226', '52', '2', '1', '三全凌', '', '', '400g/盒', 'Q/ZSQ0007S', '20120313合格H07', 'MMQTG2012060007', '待检', '15.9', '50', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('615', '咸味粽', '636', '615', '6', '1528', '4', '5321', '145', '245', '52', '2', '1', '贵州龙', '', '', '200克/袋', 'SB/T10377', '2012年05月22日', 'MMQTG2012060008', '待检', '15.9', '50', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('616', '龙舟粽', '637', '616', '6', '135', '4', '5321', '145', '225', '52', '3', '1', '三全凌', '', '', '300g/袋', 'Q/ZSQ00075', '20120302合格A181', 'MMQTG2012060009', '待检', '16.8', '15', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('617', '香润豆沙粽', '638', '617', '6', '1528', '4', '5321', '145', '236', '52', '3', '1', '思念', '', '', '1000g/袋', 'SB/T10412', '20120305S1', 'MMQTG2012060010', '待检', '23.9', '15', '36', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('618', '黑米花生粽', '639', '618', '6', '1528', '4', '5321', '145', '252', '52', '3', '1', '云鹤食品', '', '', '500g/袋', 'SB/T10377', '2012 041802B', 'MMQTG2012060011', '待检', '13.8', '15', '38', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('619', '速冻紫糯八宝粽', '640', '619', '6', '1528', '4', '5321', '145', '226', '52', '3', '1', '三全凌', '', '', '400g/袋', 'Q/ZSQ00075', '', 'MMQTG2012060012', '待检', '15.9', '5', '16', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('620', '猪肉粽', '641', '620', '6', '1527', '4', '5321', '145', '252', '52', '3', '1', '云鹤食品', '', '', '500g/袋', 'SB/T10377', '2012042002B', 'MMQTG2012060013', '固态', '12.9', '50', '133', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('621', '桂花红豆八宝粽', '642', '621', '6', '1528', '4', '5321', '145', '235', '52', '3', '1', '三全凌', '', '', '520克/袋', 'SB/T10412', '20120419合格D01', 'MMQTG2012060014', '固态', '14.9', '50', '120', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('622', '豆沙粽', '643', '622', '6', '1528', '4', '5321', '145', '235', '52', '2', '1', '三全凌', '', '', '500克/袋', 'SB/T10412', '20120224合格D01', 'MMQTG2012060015', '固态', '15.9', '50', '80', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('623', '醇香花生粽', '644', '623', '6', '1528', '4', '5321', '145', '236', '52', '2', '1', '思念', '', '', '500g/袋', 'SB/T10412', '2012030531', 'MMQTG2012060016', '固态', '15.9', '50', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('624', '叉烧粽', '645', '624', '6', '135', '4', '5321', '145', '226', '52', '2', '1', '三全凌', '', '', '520克/袋', 'SB/T10412', '20120512H20', 'MMQTG2012060017', '固态', '15.9', '50', '100', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('625', '桂花红豆粽', '646', '625', '6', '1528', '4', '5321', '145', '225', '52', '3', '1', '三全凌', '', '', '520克/袋', 'Q/ZSQ0007S', '', 'MMQTG2012060018', '固态', '19.5', '10', '25', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('626', '猪肉粽', '647', '626', '6', '135', '4', '5321', '145', '226', '52', '3', '1', '三全凌', '', '', '520g/袋', 'Q/ZSQ0007S', '20120224合格H071', 'MMQTG2012060019', '固态', '19.5', '10', '25', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('627', '蜜枣粽', '648', '627', '6', '1528', '4', '5321', '145', '235', '52', '3', '1', '三全凌', '', '', '520克/袋', 'SB/T10412', '20120420合格D01', 'MMQTG2012060020', '固态', '19.5', '10', '26', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('629', '果粒橙', '650', '629', '4', '1491', '2', '3', '217', '255', '327', '2', '1', '美汁源', '', '', '420ml', 'Q/SBDG0001S', '20120203', 'YLGSA2012070001', '液体', '3.00', '5', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('630', '爽粒. 红葡萄', '651', '630', '4', '1491', '4', '3', '217', '256', '306', '2', '1', '美汁源', '', '', '420ml', 'Q/SBDG0006S', '20120312', 'YLGSA2012070002', '液体', '3.00', '10', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('631', '一榨鲜', '652', '631', '4', '1491', '4', '3', '217', '257', '306', '2', '1', '希之源', '', '', '428ml', 'Q/EMH015', '20120412', 'YLGSA2012070003', '液体', '4.00', '6', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('632', '水溶C100', '653', '632', '4', '1491', '4', '3', '217', '258', '306', '2', '1', '农夫山泉', '', '', '445ml', 'Q/NFS0008S', '20120229', 'YLGSA2012070004', '液体', '4.00', '10', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('635', '绿杰苹果醋饮料', '656', '635', '4', '1491', '4', '3', '217', '260', '310', '2', '1', '绿杰', '', '', '260ml', 'Q/YLJ0002S-2010', '20120225', 'YLGSA2012070005', '液体', '4.00', '10', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('638', '果粒橙', '659', '638', '4', '1491', '4', '3', '217', '270', '310', '2', '1', '乐源', '', '', '450ml', 'Q/LYS0001S', '20120211A', 'YLGSA2012070006', '液体', '3.00', '3', '5', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('641', '桂圆莲子八宝粥', '662', '641', '8', '219', '4', '7363', '217', '263', '393', '5', '3', '银鹭', '', '', '360g/瓶', 'QB/T2221', '20120101', 'GTQTD2012070001', '粘稠液', '3.5', '30', '36', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('642', 'C每粒果粒橙', '663', '642', '4', '1491', '4', '3', '217', '264', '310', '2', '1', '汇源', '', '', '420ml', 'GB/T21731', 'Z20120327', 'YLGSA2012070007', '液体', '3.50', '12', '36', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('643', '木糖醇营养八宝粥', '664', '643', '8', '219', '4', '7363', '217', '265', '393', '5', '3', '娃哈哈', '', '', '360g/瓶', 'Q/WHJ0881', '20120330CG', 'GTQTD2012070002', '粘稠液', '3.5', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('644', '巧乐兹雪糕', '665', '644', '12', '1532', '4', '1768', '239', '266', '428', '7', '3', '伊利', '', '', '80g/支', 'SB/T10015组合型', '2012042810071231V', 'YLQTB2012070001', '固体', '3.0', '9支', '30支', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('645', '100%橙汁', '666', '645', '4', '1491', '4', '3', '179', '267', '310', '2', '1', '汇源', '', '', '1升', 'GB/T21731', 'B20120316', 'YLGSA2012070008', '液体', '14.80', '24', '36', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('646', '好香绿豆雪糕', '667', '646', '12', '1532', '4', '1768', '239', '266', '428', '7', '3', '佰豆集', '', '', '75g/支', 'SB/T10015组合型', '2011061710052211V', 'YLQTB2012070002', '固体', '2.0', '23支', '40支', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('648', '好粥道黑米粥', '669', '648', '8', '219', '4', '7363', '217', '263', '393', '5', '3', '银鹭', '', '', '280g/瓶', 'Q/XMYL0006S', '20120228XM', 'GTQTD2012070003', '粘稠液', '3.5', '135', '180', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('649', '伊利牧场', '670', '649', '12', '1532', '4', '1768', '179', '266', '428', '7', '3', '伊利', '', '', '75g/盒', 'SB/T10015组合型', '2012022310022121V', 'YLQTB2012070003', '固体', '3.0', '122', '150', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('650', '苦咖啡雪糕', '671', '650', '12', '1532', '4', '1768', '239', '266', '428', '7', '3', '伊利', '', '', '70g/支', 'SB/T10015组合型', '2012050310051211V', 'YLQTB2012070004', '固体', '3.0', '19', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('651', '30%混合果汁', '672', '651', '4', '1491', '4', '3', '217', '261', '310', '2', '1', '农夫果园', '', '', '500ml', 'Q/NFS00035S', '201204181104G6', 'YLGSA2012070009', '液体', '3.90', '24', '100', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('652', '香雪儿雪糕', '673', '652', '12', '1532', '4', '1768', '179', '266', '428', '7', '3', '伊利', '', '', '145g/盒', 'SB/T10015组合型', '2012042610011021V', 'YLQTB2012070005', '固体', '4.7', '5', '16', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('653', '桂圆莲子营养八宝粥', '674', '653', '8', '219', '4', '7363', '217', '268', '393', '5', '3', '娃哈哈', '', '', '360g/瓶', 'QB/T2221', '20120304', 'GTQTD2012070004', '粘稠液', '3.5', '12', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('654', '冰工厂冰片蜜桃', '675', '654', '12', '1534', '4', '1768', '239', '266', '428', '7', '3', '伊利', '', '', '80g/支', 'SB/T10016组合型', '2012050310132491V', 'YLQTB2012070006', '固体', '1.5', '27', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('655', '小布丁', '676', '655', '12', '1532', '4', '1768', '239', '266', '428', '7', '3', '伊利', '', '', '48g/支', 'SB/T10015清型', '2012050310132491V', 'YLQTB2012070007', '固体', '0.6', '', '1200', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('656', '好粥道薏仁红豆粥', '677', '656', '8', '219', '4', '7363', '217', '263', '393', '5', '3', '银鹭', '', '', '280g/瓶', 'Q/XMYL0006S', '20120112G718XM', 'GTQTD2012070005', '粘稠液', '3.5', '13', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('657', 'C粒柠檬', '678', '657', '4', '1491', '4', '3', '217', '270', '310', '2', '1', '美汁源', '', '', '420ml', 'Q/SBDG0006S', '20120427', 'YLGSA2012070010', '液体', '3.20', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('659', '蛋黄莲蓉粽', '680', '659', '8', '1529', '4', '7363', '145', '271', '3', '8', '2', '宏興隆', '', '', '100克/袋', 'SB/T10377-2004', '2012-5-20', 'MMQTD2012060007', '固体', '4.0', '', '', '6933949021659', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('661', '蓝莓酸奶口味棒冰', '682', '661', '12', '1532', '4', '1768', '239', '272', '428', '7', '3', '蒙牛', '', '', '75g/支', 'SB/T10015', '20120510231181W', 'YLQTB2012070008', '固体', '2.0', '9', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('662', '柠爽葡醉美味', '683', '662', '4', '1491', '4', '3', '217', '270', '310', '2', '2', '纯果乐', '', '', '450毫升', 'Q/BSYL0013S', '20120313CD216750', 'YLGSA2012070011', '液体', '3.80', '7', '36', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('663', '热带美味', '684', '663', '4', '1491', '4', '3', '217', '270', '310', '2', '2', '纯果乐', '', '', '450毫升', 'Q/BSYL0013S', '20120213DC21', 'YLGSA2012070012', '液体', '3.50', '8', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('664', '芒果口味雪泥', '685', '664', '12', '1534', '4', '1768', '239', '272', '428', '7', '3', '蒙牛', '', '', '75g/支', 'SB/T10014', '20120510122111W', 'YLQTB2012070009', '固体', '2.0', '10', '36', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('666', '新鲜阳光椰果粒', '687', '666', '4', '1491', '4', '3', '217', '273', '310', '2', '2', '快活林', '', '', '500ml', 'Q/KHL0007S', '20120418', 'YLGSA2012070013', '液体', '3.00', '10', '15', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('667', '橙汁饮品', '688', '667', '4', '1491', '4', '3', '217', '187', '310', '2', '2', '娃哈哈', '', '', '500ml', 'GB/T21731', '20120921', 'YLGSA2012070014', '液体', '2.50', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('668', '新鲜阳光粒粒橙', '689', '668', '4', '1491', '4', '3', '217', '273', '310', '2', '2', '快活林', '', '', '500ml', 'Q/JKHL010', '20120407', 'YLGSA2012070015', '液体', '3.00', '11', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('669', '绿色风情', '690', '669', '12', '1534', '4', '1768', '239', '274', '428', '7', '3', '雪利', '', '', '70g/支', 'SB/T10016', '20120131B', 'YLQTB2012070010', '固体', '1.5', '9', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('670', '三色雪糕', '691', '670', '12', '1532', '4', '1768', '179', '272', '428', '7', '3', '蒙牛', '', '', '110g/盒', 'SB/T10015', '20120415111041W', 'YLQTB2012070011', '固体', '3.5', '5', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('671', '30%混合果蔬', '692', '671', '10', '1491', '4', '9567', '217', '261', '309', '2', '1', '农夫果园', '', '', '500ml/瓶', 'Q/NFS 0003S', '20120325', 'YLGSE2012060001', '液体', '4.0', '5', '15', '6921168532001', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('672', '绿色心情', '693', '672', '12', '1532', '4', '1768', '239', '272', '428', '7', '3', '蒙牛', '', '', '65g/支', 'SB/T10015', '2012022515231W', 'YLQTB2012070012', '固体', '3.0', '10', '20', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('673', '柠爽葡醉美味', '694', '673', '4', '1491', '4', '3', '217', '270', '310', '2', '1', '纯果乐', '', '', '450毫升', 'Q/BSYL13S', '20111208', 'YLGSA2012070016', '液体', '3.50', '9', '45', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('674', '随变旋顶杯', '695', '674', '12', '1532', '4', '1768', '179', '272', '428', '7', '3', '蒙牛', '', '', '120g/盒', 'SB/T10015', '20120504', 'YLQTB2012070013', '固体', '5', '2', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('675', '芒果多芒果汁饮料', '696', '675', '10', '1491', '4', '9567', '217', '275', '307', '2', '1', '澳地澳', '', '', '500ml/瓶', 'Q/KAD 0001 S-2010', '2012/04/02', 'YLGSE2012060002', '液体', '2.50', '12', '30', '6945032920558', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('676', '蜂蜜金桔柠檬复合果汁饮料', '697', '676', '4', '1491', '4', '3', '217', '276', '310', '2', '1', '娃哈哈', '', '', '500ml', 'Q/WHJ0932S', '20120328', 'YLGSA2012070017', '液体', '3.50', '9', '15', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('677', '西柚味复合果汁饮料', '698', '677', '10', '1491', '4', '9567', '217', '277', '310', '2', '1', '水溶C100', '', '', '445ml/瓶', 'Q/NFS 0008S', '20120221', 'YLGSE2012060003', '液体', '4.5', '7', '15', '6921168500970', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('678', '橙粒混合水果饮料', '699', '678', '4', '1491', '4', '3', '217', '278', '310', '2', '1', '选中你', '', '', '500ml', 'Q/XWT07', '20120315', 'YLGSA2012070018', '液体', '3.00', '11', '15', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('679', '果粒橙', '700', '679', '4', '1491', '4', '3', '217', '255', '310', '2', '1', '美汁源', '', '', '420ml', 'Q/SBDG0001S', '20111221', 'YLGSA2012070019', '液体', '3.00', '60', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('680', '牛奶提子', '701', '680', '12', '1532', '4', '1768', '239', '279', '428', '7', '3', '三代行', '', '', '45g/支', 'SB/T10014', '20120410', 'YLQTB2012070014', '固体', '0.35', '13', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('681', '蜂蜜金桔柠檬复合果汁饮料', '702', '681', '10', '1491', '4', '9567', '217', '122', '310', '2', '1', '娃哈哈', '', '', '450ml/瓶', 'Q/WHJ 0932S', '20111202', 'YLGSE2012060004', '液体', '3.0', '24', '30', '6902083893187', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('682', '绿豆心情', '703', '682', '12', '1532', '4', '1768', '145', '280', '428', '7', '3', '甜甜雪', '', '', '60g/袋', 'SB/T10016', '2012052011', 'YLQTB2012070015', '固体', '0.35', '', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('683', '统一鲜橙多（橙汁饮料）', '704', '683', '10', '1491', '4', '9567', '217', '259', '307', '2', '1', '统一', '', '', '450ml/瓶', 'GB/T21731', '20120312', 'YLGSE2012060005', '液体', '3.0', '6', '15', '6925303721039', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('684', '芒果雪泥', '705', '684', '12', '1534', '4', '1768', '239', '281', '428', '7', '3', '鑫丰', '', '', '70g/支', 'SB/T10016', '20120329', 'YLQTB2012070016', '固体', '1.0', '', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('685', '巧乐兹四个圈雪糕', '706', '685', '12', '1532', '4', '1768', '239', '266', '428', '7', '3', '伊利', '', '', '75g/支', 'SB/T10015', '20120514', 'YLQTB2012070017', '固体', '1.6', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('686', '30%果蔬汁饮料', '707', '686', '4', '1491', '4', '3', '217', '261', '310', '2', '1', '农夫果园', '', '', '500ml', 'Q/NFS0003S', '20111022', 'YLGSA2012070020', '液体', '3.00', '2', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('687', '脆皮鲨', '708', '687', '12', '1534', '4', '1768', '239', '282', '428', '7', '3', '雀巢', '', '', '58g/支', 'SB/T10015', '20120428.1V', 'YLQTB2012070018', '固体', '1.6', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('688', '芒果饮品', '709', '688', '10', '1491', '4', '9567', '217', '283', '310', '2', '1', '格蕾美', '', '', '500ml/瓶', 'Q/JJB 0002S', '2012/04/03', 'YLGSE2012060006', '液体', '4.5', '28', '50', '6943871200770', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('689', '老冰棍', '710', '689', '12', '1532', '4', '1768', '239', '272', '428', '7', '3', '蒙牛', '', '', '88g/支', 'SB/T10016', '20120517163282W', 'YLQTB2012070019', '固体', '1.0', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('690', '蓝莓恋', '711', '690', '12', '1534', '4', '1768', '239', '284', '428', '7', '3', '洞桥', '', '', '70g/支', 'SB/T10327', '20120513', 'YLQTB2012070020', '固体', '0.5', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('692', '枇杷饮品', '713', '692', '10', '1491', '4', '9567', '217', '283', '310', '2', '1', '格蕾美', '', '', '500ml/瓶', 'Q/JJB 0002S', '2012/04/04', 'YLGSE2012060007', '液体', '4.5', '41', '50', '6943871200770', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('695', '橙汁饮品', '716', '695', '10', '1491', '4', '9567', '217', '285', '307', '2', '1', '康師傅', '', '', '450ml/瓶', 'GB/T 21731', '20120225', 'YLGSE2012060008', '液体', '2.5', '39', '50', '6922456812096', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('699', '绿杰七个苹果发酵型苹果醋饮料', '720', '699', '10', '1491', '4', '9567', '217', '260', '307', '2', '1', '绿杰', '', '', '280ml/瓶', 'Q/YLJ 0002S-2010', '2012/03/09', 'YLGSE2012060009', '液体', '6', '34', '50', '6926356030963', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('700', '草莓红豆雪糕', '721', '700', '7', '1532', '4', '10926', '239', '266', '428', '7', '3', '伊利', '', '', '70克/支', 'SB/T10015', '20120308', 'LYQTF2012060001', '', '2.4', '60支', '80支', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('701', '白葡萄汁饮品', '722', '701', '10', '1491', '4', '9567', '217', '285', '307', '2', '1', '康師傅', '', '', '450ml/瓶', 'Q/14A0239S', '20110918', 'YLGSE2012060010', '液体', '2.5', '23', '50', '6922456808181', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('702', '巧乐兹雪糕', '723', '702', '7', '1532', '4', '10926', '239', '266', '428', '7', '3', '伊利', '', '', '75克/支', 'SB/T10015', '20120522', 'LYQTF2012060002', '', '3.5', '80支', '80支', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('703', '八宝粥', '724', '703', '12', '219', '4', '1768', '252', '286', '377', '7', '3', '哇哈哈', '', '', '360g/听', 'Q/WHJ0881', '2011.10.30', 'GTQTB2012070001', '半固体', '3.9', '40', '200', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('704', '乐满家菠萝爽饮品', '725', '704', '10', '1491', '4', '9567', '217', '287', '310', '3', '2', '乐满家', '合格品', '', '500g/瓶', 'Q/ZJHLJ 0004S', '2012/03/20', 'YLGSE2012060011', '液体', '3', '15', '75', '6924254676153', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('705', '八宝粥', '726', '705', '12', '219', '4', '1768', '252', '263', '377', '7', '3', '银鹭', '', '', '360g/听', 'Q/XMYL0011S', '2012.01.17', 'GTQTB2012070002', '半固体', '3.5', '40', '200', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('706', '乐满家椰果爽饮品', '727', '706', '10', '1491', '4', '9567', '217', '287', '310', '3', '2', '乐满家', '合格品', '', '500g/瓶', 'Q/ZJHLJ 0004S', '2012/03/20', 'YLGSE2012060012', '液体', '3', '15', '75', '6924254676160', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('707', '巧乐兹巧脆棒', '728', '707', '7', '1532', '4', '10926', '239', '266', '428', '7', '3', '伊利', '', '', '80克/支', 'SB/T10015', '20120529', 'LYQTF2012060003', '', '3.5', '50支', '60支', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('708', '乐满家粒粒橙饮品', '729', '708', '10', '1491', '4', '9567', '217', '287', '310', '3', '2', '乐满家', '合格品', '', '500g/瓶', 'Q/ZJHLJ 0004S', '2012/03/20', 'YLGSE2012060013', '液体', '3', '15', '75', '6924254676122', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('709', '八宝粥', '730', '709', '12', '219', '4', '1768', '252', '263', '377', '7', '3', '银鹭', '', '', '360g/听', 'QB/T2221', '2012.03.11', 'GTQTB2012070003', '半固体', '3.5', '60', '200', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('710', '牛奶蜜豆雪糕', '731', '710', '7', '1532', '4', '10926', '239', '266', '428', '7', '3', '伊利', '', '', '75克/支', 'SB/T10015', '20120310', 'LYQTF2012060004', '', '2.5', '40', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('711', '美汁源果粒橙', '732', '711', '10', '1491', '4', '9567', '217', '255', '310', '3', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0001S', '20120327', 'YLGSE2012060014', '液体', '3.5', '15', '75', '6920927182631', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('712', '大布丁', '733', '712', '7', '1532', '4', '10926', '239', '266', '428', '7', '3', '伊利', '', '', '75克/支', 'SB/T10015', '20120310', 'LYQTF2012060005', '', '2.0', '50', '120', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('713', '西瓜口味冰棒', '734', '713', '7', '1532', '2', '10926', '239', '272', '430', '7', '3', '蒙牛', '', '', '85克/支', 'SB/T10016', '20120228', 'LYQTF2012060006', '', '2.0', '40', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('714', '美汁源C粒·柠檬', '735', '714', '10', '1491', '4', '9567', '217', '270', '310', '3', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0006S', '20120322', 'YLGSE2012060015', '液体', '3.5', '15', '75', '6920927182679', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('715', '黑米糕', '736', '715', '7', '1532', '4', '10926', '239', '272', '428', '7', '3', '蒙牛', '', '', '78克/支', 'SB/T10015', '20120510', 'LYQTF2012060007', '', '2.5', '30', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('716', '冰爽青梅', '737', '716', '7', '1534', '4', '10926', '239', '266', '428', '7', '3', '伊利', '', '', '80克/支', 'SB/T10016', '20120517', 'LYQTF2012060008', '', '2.0', '30', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('717', '橙汁饮品', '738', '717', '10', '1491', '4', '9567', '217', '285', '310', '2', '1', '康師傅', '', '', '450ml/瓶', 'GB/T21731', '20120210', 'YLGSE2012060016', '液体', '2.5', '15', '45', '6922456812096', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('718', '八宝粥', '739', '718', '12', '219', '4', '1768', '252', '263', '377', '7', '3', '银鹭', '', '', '360g/听', 'QB/T2221', '2012.02.25', 'GTQTB2012070004', '半固体', '8.0', '60', '100', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('719', '老冰棍冰棒', '740', '719', '7', '1532', '4', '10926', '239', '272', '430', '7', '3', '蒙牛', '', '', '88克/支', 'SB/T10016', '20120406', 'LYQTF2012060009', '', '2.0', '40', '100', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('720', '统一金橘柠檬水果饮料', '741', '720', '10', '1491', '4', '9567', '217', '259', '310', '2', '1', '统一', '', '', '500ml/瓶', 'Q/TYK 0003S', '20120228', 'YLGSE2012060017', '液体', '2.5', '15', '30', '6925303724900', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('721', '好粥道', '742', '721', '12', '219', '4', '1768', '252', '263', '377', '7', '3', '银鹭', '', '', '280g/听', 'Q/XMYL0006S', '22012020422：:2XM', 'GTQTB2012070005', '半固体', '4.0', '60', '100', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('722', '葡萄汁饮品', '743', '722', '10', '1491', '4', '9567', '217', '12', '310', '2', '1', '娃哈哈', '', '', '500ml/瓶', 'Q/WHJ0283S', '20120324', 'YLGSE2012060018', '液体', '2.5', '20', '40', '6902083884369', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('723', '绿色情怡雪糕', '744', '723', '7', '1533', '4', '10926', '239', '266', '428', '7', '3', '伊利', '', '', '71克/支', 'SB/T10016', '20120109', 'LYQTF2012060010', '', '2.0', '50', '130', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('724', '红枣桂圆八宝粥', '745', '724', '12', '219', '4', '1768', '252', '288', '377', '7', '3', '达利园', '', '', '360g/听', 'QB/T2221', '20120208', 'GTQTB2012070006', '半固体', '4.0', '10', '12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('725', '桂圆莲子', '746', '725', '12', '219', '4', '1768', '252', '289', '377', '7', '3', '哇哈哈', '', '', '360g/听', 'QB/T2221', '20120205211112CD', 'GTQTB2012070007', '半固体', '3.8', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('726', '果粒橙', '747', '726', '10', '1491', '4', '9567', '217', '262', '310', '2', '1', '乐源', '', '', '450ml/瓶', 'Q/LYS0001S', '20120303', 'YLGSE2012060019', '液体', '3.5', '15', '30', '6934004601335', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('727', '酸爱纯脆', '748', '727', '7', '1534', '4', '10926', '239', '272', '430', '7', '3', '蒙牛', '', '', '56克/支', 'SB/T10015', '2012.4.28', 'LYQTF2012060011', '', '1.1', '40', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('728', '银耳莲子八宝粥', '749', '728', '12', '219', '4', '1768', '252', '290', '377', '7', '3', '百森', '', '', '360g/听', 'QB/T2221', '2011/11/04SH', 'GTQTB2012070008', '半固体', '3.8', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('729', '统一鲜橙多（橙汁饮料）', '750', '729', '10', '1491', '4', '9567', '217', '259', '310', '2', '1', '统一', '', '', '450ml/瓶', 'GB/T 21731', '20120312', 'YLGSE2012060020', '液体', '3', '30', '60', '6925303721039', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('730', '新桂圆八宝粥', '751', '730', '12', '219', '4', '1768', '252', '290', '377', '7', '3', '椰星', '', '', '320g/听', 'Q/SPS0002S', '2011/11/26SH', 'GTQTB2012070009', '半固体', '3.8', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('731', '好粥道', '752', '731', '12', '219', '4', '1768', '252', '263', '377', '7', '3', '银鹭', '', '', '280g/听', 'Q/XMYL0006S', '20120115XM', 'GTQTB2012070010', '半固体', '4.0', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('732', '香芋脆皮雪糕', '753', '732', '7', '1532', '4', '10926', '239', '272', '428', '7', '3', '蒙牛', '', '', '75克/支', 'SB/T10015', '2012.5.1', 'LYQTF2012060012', '', '1.5', '40', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('733', '杨梅果汁饮料', '754', '733', '9', '1491', '2', '18578', '217', '291', '307', '3', '2', '杨百利', '', '', '450ml/瓶', 'Q/ZJYM2', '2012/04/10F1', 'YLGSJ2012070001', '液体', '3.91元', '5瓶', '30瓶', 'YLGSJ2012070001', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('734', 'C粒•柠檬', '755', '734', '9', '1491', '4', '18578', '217', '270', '307', '3', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0006S', '2012042705:51CD2', 'YLGSJ2012070002', '液体', '3.5元', '6瓶', '30瓶', '6920927182697', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('735', '冰糖雪梨', '756', '735', '9', '1491', '4', '18578', '217', '259', '307', '3', '2', '统一', '', '', '500毫升/瓶', 'Q/TYK0001S', '20120408km1', 'YLGSJ2012070003', '液体', '3元', '20瓶', '45瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('736', '果粒橙', '757', '736', '9', '1491', '4', '18578', '217', '255', '307', '3', '2', '美汁源', '', '', '', 'Q/SBDG0001S', '20120408CDP', 'YLGSJ2012070004', '液体', '3.5元', '1件', '5件', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('737', '葡萄汁饮品', '758', '737', '9', '1491', '4', '18578', '217', '255', '307', '3', '2', '娃哈哈', '', '', '500毫升/瓶', '', '20120319', 'YLGSJ2012070005', '液体', '2.5元', '1件', '1件', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('738', '冰糖雪梨', '759', '738', '9', '1491', '4', '18578', '217', '285', '307', '3', '2', '康师傅', '', '', '450ml/瓶', '', '20120319', 'YLGSJ2012070005', '液体', '2.5元', '12瓶', '24瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('739', '30%混合果蔬', '760', '739', '9', '1491', '4', '18578', '217', '261', '309', '3', '2', '农夫果园', '', '', '500ml/瓶', 'Q/NFS00035', '201205060324G6', 'YLGSJ2012070007', '液体', '3.5元', '20瓶', '24瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('740', '橙汁饮品', '761', '740', '9', '1491', '4', '18578', '217', '261', '307', '3', '2', '康师傅', '', '', '450ml/瓶', 'GB/T21731', '2012031813:38CB7', 'YLGSJ2012070008', '液体', '2.5元', '15瓶', '24瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('741', '酸枣饮品', '762', '741', '9', '1491', '4', '18578', '217', '285', '307', '3', '2', '康师傅', '', '', '450ml/瓶', 'Q/14A0239S', '2012022917:31CB6', 'YLGSJ2012070009', '液体', '2.5元', '17瓶', '24瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('743', '好粥道黑米粥', '764', '743', '4', '219', '4', '3', '252', '263', '377', '3', '1', '银鹭', '', '', '280g', 'Q/XMYL0006S', '20120129', 'GTQTA2012070001', '半固体', '3.50', '5', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('744', '好粥道莲子玉米粥', '765', '744', '4', '219', '4', '3', '252', '263', '377', '3', '1', '银鹭', '', '', '280g', 'Q/XMYL0006S', '20110913', 'GTQTA2012070002', '半固体', '3.50', '7', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('745', '好粥道椰奶燕麦粥', '766', '745', '4', '219', '4', '3', '252', '263', '377', '3', '1', '银鹭', '', '', '280g', 'Q/XMYL0006S', '20120113', 'GTQTA2012070003', '半固体', '3.50', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('746', '桂圆八宝粥', '767', '746', '4', '219', '4', '3', '252', '292', '377', '3', '1', '银鹭', '', '', '360g', 'QB/T2221', '20120101', 'GTQTA2012070004', '半固体', '3.50', '5', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('747', '桂圆莲子营养八宝粥', '768', '747', '4', '219', '4', '3', '252', '268', '377', '3', '1', '娃哈哈', '', '', '360克', 'QB/T2221', '20120410', 'GTQTA2012070005', '半固体', '', '8', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('748', '果粒橙', '769', '748', '9', '1491', '4', '18578', '217', '293', '307', '3', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0001S', '2012032711:25CDP1', 'YLGSJ2012070010', '固体', '3.5元', '18瓶', '24瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('749', '黑米粥', '770', '749', '4', '219', '4', '3', '252', '292', '377', '2', '1', '银鹭', '', '', '280g', 'Q/XMYL0006S', '201204151014', 'GTQTA2012070006', '半固体', '4.20', '5', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('750', '红枣桂圆八宝粥', '771', '750', '4', '219', '4', '3', '252', '288', '377', '4', '1', '达利园', '', '', '360g', 'GB/T2221', '20111203J', 'GTQTA2012070007', '半固体', '3.90', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('751', '好粥道薏仁红豆粥', '772', '751', '4', '219', '4', '3', '252', '263', '377', '4', '1', '银鹭', '', '', '280g', 'Q/XMYL0006S', '20120310', 'GTQTA2012070008', '半固体', '4.20', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('752', '桂圆莲子八宝粥', '773', '752', '4', '219', '4', '3', '252', '288', '377', '4', '1', '达利园', '', '', '360g', 'QB/T2221', '20120101J', 'GTQTA2012070009', '半固体', '3.90', '24', '36', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('753', '桂圆莲子八宝粥', '774', '753', '4', '219', '4', '3', '252', '263', '377', '4', '1', '银鹭', '', '', '360g', 'QB/T2221', '20120102', 'GTQTA2012070010', '半固体', '3.20', '24', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('754', '好粥道黑米粥', '775', '754', '4', '219', '4', '3', '252', '294', '377', '4', '1', '银鹭', '', '', '280g', 'Q/XMYL0006S', '20120509', 'GTQTA2012070011', '半固体', '4.00', '50', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('755', '好粥道薏仁红豆粥', '776', '755', '4', '219', '4', '3', '252', '294', '377', '4', '1', '银鹭', '', '', '280g', 'Q/XMYL0006S', '20120303', 'GTQTA2012070012', '半固体', '4.00', '24', '36', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('756', '桂圆椰果八宝粥', '777', '756', '4', '219', '4', '3', '252', '292', '377', '4', '1', '银鹭', '', '', '360g', 'QB/T2221', '20120306', 'GTQTA2012070013', '半固体', '3.50', '96', '240', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('757', '桂圆莲子营养八宝粥', '778', '757', '4', '219', '4', '3', '252', '268', '377', '4', '1', '娃哈哈', '', '', '360克', 'QB/T2221', '20120411', 'GTQTA2012070014', '半固体', '4.00', '48', '84', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('758', '好粥道莲子玉米粥', '779', '758', '4', '219', '4', '3', '252', '263', '377', '4', '1', '银鹭', '', '', '280克', 'Q/XMYL0006S', '20120109', 'GTQTA2012070015', '半固体', '4.00', '30', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('759', '桂圆椰果八宝粥', '780', '759', '4', '219', '4', '3', '252', '263', '377', '4', '1', '银鹭', '', '', '360g', 'QB/T2221', '20111225', 'GTQTA2012070016', '半固体', '4.20', '36', '44', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('760', '桂圆莲子八宝粥', '781', '760', '4', '219', '4', '3', '252', '268', '377', '4', '1', '娃哈哈', '', '', '360克', 'QB/T2221', '20120504', 'GTQTA2012070017', '半固体', '4.20', '72', '77', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('761', '木糖醇八宝粥', '782', '761', '4', '219', '4', '3', '252', '265', '377', '4', '1', '娃哈哈', '', '', '360克', 'Q/WHJ0881', '20120307', 'GTQTA2012070018', '半固体', '4.20', '28', '36', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('762', '醇豆浆甜豆浆粉', '783', '762', '6', '1501', '4', '5321', '145', '295', '319', '2', '1', '维维', '', '', '500克/袋', 'GB/T18738', '201203200604H', 'YLGTG2012070001', '固体', '16.5', '5', '15', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('763', '粗粮生活营养八宝粥', '784', '763', '4', '219', '4', '3', '252', '265', '377', '4', '1', '娃哈哈', '', '', '360克', 'QMHJ0831', '20111107', 'GTQTA2012070019', '半固体', '4.20', '27', '48', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('764', '好粥道薏仁红豆粥', '785', '764', '4', '219', '4', '3', '252', '263', '377', '4', '1', '银鹭', '', '', '280克', 'Q/XMYL0006S', '20120206', 'GTQTA2012070020', '半固体', '4.20', '32', '33', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('765', '豆奶粉', '786', '765', '6', '1501', '2', '5321', '145', '296', '319', '2', '1', '雅士利', '', '', '580克/袋', 'Q/YSL0050S', '20120223B', 'YLGTG2012070002', '固体', '16.5', '8', '12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('766', '好粥道莲子玉米粥', '787', '766', '8', '219', '4', '7363', '169', '263', '393', '7', '1', '银鹭', '', '', '280g/罐', 'Q/XMYL0006S', '20120108XM', 'GTQTD2012070006', '粘稠液', '4.0', '6*12', '8*12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('767', '娃哈哈葡萄汁饮品', '788', '767', '9', '1491', '4', '18578', '217', '297', '307', '3', '2', '娃哈哈', '', '', '500ml', 'Q/WHJ0283S', '20120129431727HC', 'YLGSJ2012070011', '液体', '2.5元', '12瓶', '24瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('768', '菠萝啤', '789', '768', '4', '1292', '4', '3', '252', '298', '348', '2', '1', '蓝带', '', '', '330ml', 'Q/LBYL0005S', '20120414', 'JYPJA2012070001', '液体', '2.50', '15', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('769', '桂圆莲子八宝粥', '790', '769', '8', '219', '4', '7363', '217', '288', '393', '7', '1', '达利园', '', '', '360g/瓶', 'QB/T2221', '20110708J', 'GTQTD2012070007', '粘稠液', '3.5', '6*12', '8*12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('770', '雪花啤酒', '791', '770', '4', '1292', '4', '3', '252', '299', '348', '2', '1', '雪花', '一级', '', '330ml', 'GB4927', '20110826', 'JYPJA2012070002', '液体', '2.50', '5', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('771', '桂圆莲子八宝粥', '792', '771', '8', '219', '4', '7363', '217', '290', '393', '7', '1', '椰星', '', '', '320g/瓶', 'Q/SPS0002S', '2011/9/17', 'GTQTD2012070008', '粘稠液', '4.5', '3*12', '5*12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('772', '雪花啤酒', '793', '772', '4', '1292', '4', '3', '252', '299', '348', '2', '1', '雪花', '一级', '', '330ml', 'GB4927', '20120302 6.6', 'JYPJA2012070003', '液体', '2.90', '5', '120', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('773', '啤酒', '794', '773', '12', '1292', '4', '1768', '217', '307', '348', '2', '1', 'CENTVRYSACUTE', '', '', '330ml/瓶', 'GB4927', '20120305', 'JYPJB2012070001', '液体', '30', '440', '480', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('774', '蓝带啤酒', '795', '774', '4', '1292', '4', '3', '252', '301', '348', '2', '1', 'PabjtBLUERIbbon', '', '', '330ml', 'GB4927', '20111229', 'JYPJA2012070004', '液体', '3.70', '12', '48', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('775', '啤酒', '796', '775', '12', '1292', '4', '1768', '217', '300', '348', '2', '1', '青岛啤酒', '', '', '330ml/瓶', 'GB4927', '20120208', 'JYPJB2012070002', '液体', '30', '560', '1200', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('776', '蓝狮啤酒', '797', '776', '4', '1292', '4', '3', '252', '303', '348', '2', '1', '蓝狮', '优级', '', '330ml', 'GB4927', '20111019', 'JYPJA2012070005', '液体', '3.00', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('777', '啤酒', '798', '777', '12', '1292', '4', '1768', '217', '302', '348', '2', '1', '青岛啤酒', '', '', '330ml/瓶', 'GB4927', '20120423', 'JYPJB2012070003', '液体', '30', '200', '480', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('778', '啤酒', '799', '778', '12', '1292', '4', '1768', '217', '304', '348', '2', '1', '青岛啤酒', '', '', '330ml/瓶', 'GB4927', '20120208', 'JYPJB2012070004', '液体', '30', '200', '480', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('779', '燕京啤酒', '800', '779', '4', '1317', '4', '3', '217', '305', '348', '2', '1', '燕京', '', '', '595ml', 'GB4927', '20120116', 'JYPJA2012070006', '液体', '4.00', '12', '120', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('780', '山城啤酒', '801', '780', '12', '1292', '4', '1768', '217', '310', '348', '2', '1', '山城', '优级', '', '520ml/瓶', 'GB4927', '20120513 3*14:26', 'JYPJB2012070005', '液体', '2.55', '360', '450', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('781', '金星啤酒', '802', '781', '4', '1292', '4', '3', '217', '307', '348', '2', '1', '金星', '', '', '575ml', 'GB4927', '20120302', 'JYPJA2012070007', '液体', '3.00', '144', '240', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('782', '金星啤酒', '803', '782', '4', '1292', '4', '3', '217', '307', '348', '2', '1', '金星', '', '', '560ml', 'GB4927', '20120228', 'JYPJA2012070008', '液体', '2.50', '120', '240', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('783', '雪花啤酒', '804', '783', '4', '1292', '4', '3', '217', '308', '348', '2', '1', '雪花', '', '', '580ml', 'GB4927', '20111212', 'JYPJA2012070009', '液体', '3.50', '96', '240', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('784', '超纯啤酒', '805', '784', '12', '1292', '4', '1768', '252', '309', '348', '3', '2', '蓝鼎', '', '', '330ml/瓶', 'GB4927', '20120330 19:22 3G', 'JYPJB2012070006', '液体', '2.5', '3', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('785', '精品鲜啤', '806', '785', '4', '1317', '4', '3', '217', '305', '348', '2', '1', '燕京', '', '', '500ml', 'GB4927', '20120426', 'JYPJA2012070010', '液体', '4.00', '48', '96', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('786', '蓝带啤酒', '807', '786', '12', '1292', '4', '1768', '217', '301', '348', '2', '1', '蓝带', '优级', '', '330ml/瓶', 'GB4927', '20120101', 'JYPJB2012070007', '液体', '4.5', '257', '480', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('787', '纯生啤酒', '808', '787', '12', '1292', '2', '1768', '252', '306', '348', '2', '1', '金崂', '一级', '', '330ml/听', 'GB4927', '20120/04/28 3G', 'JYPJB2012070008', '液体', '2.8', '4', '72', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('788', '超纯啤酒', '809', '788', '12', '1292', '4', '1768', '252', '298', '348', '2', '1', '蓝狮', '优级', '', '330ml/听', 'GB4927', 'H20120207', 'JYPJB2012070009', '液体', '2.0', '418', '720', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('789', '青岛啤酒', '810', '789', '12', '1292', '4', '1768', '252', '311', '348', '2', '1', '青岛', '优级', '', '330ml/听', 'GB4927', '2012/05/01', 'JYPJB2012070010', '液体', '5.0', '154', '336', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('791', '啤酒', '812', '791', '7', '1292', '4', '10926', '217', '312', '349', '2', '1', '雪花', '优级', '', '518ml/瓶', 'GB4927', '120319', 'JYPJF2012060001', '液态', '2.50', '36', '225', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('792', '高乐高巧克力味', '813', '792', '12', '1501', '4', '1768', '179', '313', '316', '2', '1', '高乐高', '', '', '350g/盒', 'Q/14A0481S', '2011-12-19  09:56:48 AM', 'YLGTB2012070001', '固态', '30', '7', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('793', '金星新一代啤酒', '814', '793', '7', '1292', '4', '10926', '217', '307', '349', '2', '1', '金星', '优级', '', '560ml/瓶', 'GB4927', '120608', 'JYPJF2012060002', '液态', '3.00', '1200', '1200', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('794', '巧乐兹雪糕', '815', '794', '4', '1532', '4', '3', '239', '314', '426', '2', '1', '伊利', '', '', '75克', 'SB/T10015', '20120512V', 'LYQTA2012070001', '固体', '2.00', '5', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('795', '雪花啤酒', '816', '795', '7', '1292', '4', '10926', '217', '308', '349', '2', '1', '雪花', '', '', '580ml/瓶', 'GB4927', '120331', 'JYPJF2012060003', '液态', '3.00', '7800', '9000', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('796', '奶茶', '817', '796', '12', '1502', '4', '1768', '179', '315', '316', '5', '2', '优乐美', '', '', '80g/盒', 'Q/XZL0002S', '20120429阳28404284 08:49', 'YLGTB2012070002', '固态', '3', '223', '294', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('797', '巧愿杯', '818', '797', '4', '1532', '4', '3', '239', '266', '426', '2', '1', '伊利', '', '', '90克', 'SB/T10015', '20120405', 'LYQTA2012070002', '固体', '5.00', '5', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('798', '奶砖雪糕', '819', '798', '4', '1532', '4', '3', '179', '266', '426', '2', '1', '伊利', '', '', '75克', 'SB/T10015', '20120220', 'LYQTA2012070003', '固体', '3.00', '5', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('799', '冰糖雪梨汁', '1214', '799', '12', '1491', '4', '1768', '217', '259', '309', '3', '2', '统一', '', '', '500ml/瓶', 'Q/TYK0001S', '20120408', 'YLGSB2012070001', '液态', '2.6', '15', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('800', '雪花啤酒', '821', '800', '7', '1292', '4', '10926', '217', '308', '349', '2', '1', '雪花', '', '', '580ml/瓶', 'GB4927', '120529', 'JYPJF2012060004', '液态', '3.00', '7500', '9000', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('801', '雀巢果维橙味', '822', '801', '12', '1502', '4', '1768', '179', '325', '316', '2', '1', '雀巢', '', '', '400g/盒', 'Q/PAAV0010S', '20120427 21:37', 'YLGTB2012070003', '固态', '12.9', '1250', '1274', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('802', '青岛啤酒', '823', '802', '7', '1292', '4', '10926', '217', '304', '349', '2', '1', '青岛', '', '', '500ml/瓶', 'GB4927', '20120209', 'JYPJF2012060005', '液态', '4.5', '48', '120', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('803', '柠檬风味茶', '824', '803', '12', '1502', '4', '1768', '179', '317', '316', '2', '1', '立顿', '', '', '360g/盒', 'Q/TNBE1001S', '20120116H2P', 'YLGTB2012070004', '固态', '17.5', '6', '12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('804', '茅台啤酒', '825', '804', '7', '1292', '4', '10926', '217', '318', '349', '2', '1', '茅台', '优级', '', '300ml/瓶', 'GB4927', '20120418', 'JYPJF2012060006', '液态', '5.5', '70', '120', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('805', '菓珍（阳光甜橙味）', '826', '805', '12', '1502', '4', '1768', '145', '319', '316', '2', '1', '卡夫', '', '', '400g/袋', 'Q/KAFUGT0009S', '20120406 15:55C1', 'YLGTB2012070005', '固态', '11.9', '877', '924', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('806', '雪花啤酒', '827', '806', '7', '1292', '4', '10926', '217', '320', '349', '2', '1', '雪花', '', '', '330ml/瓶', 'GB4927', '20120407', 'JYPJF2012060007', '液态', '4.00', '10', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('807', '菓珍（阳光甜橙味）', '828', '807', '12', '1502', '4', '1768', '145', '319', '316', '2', '1', '卡夫', '', '', '200g/袋', 'Q/KAFUGT0009S', '20120506 19:15B4', 'YLGTB2012070006', '固态', '8', '57', '67', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('808', '金威啤酒', '829', '808', '7', '1292', '4', '10926', '217', '321', '349', '2', '1', '金威', '', '', '500ml/瓶', 'GB4927', '20120505', 'JYPJF2012060008', '液态', '3.00', '100', '1*9.200', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('809', '鲜橙多', '830', '809', '12', '1491', '4', '1768', '217', '259', '309', '3', '2', '统一', '', '', '450ml/瓶', 'GB/T21731', '20120506', 'YLGSB2012070002', '液态', '2.6', '15', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('810', '芒果多', '831', '810', '12', '1491', '4', '1768', '217', '259', '309', '3', '2', '统一', '', '', '450ml/瓶', 'Q/TYK0001S', '20120429', 'YLGSB2012070003', '液态', '2.6', '12', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('811', '高乐高果奶优', '832', '811', '12', '1501', '4', '1768', '179', '313', '316', '2', '1', '高乐高', '', '', '350g/盒', 'Q/14A0131S', '2011-11-15', 'YLGTB2012070007', '固态', '26.9', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('812', '芒果多', '833', '812', '12', '1491', '4', '1768', '217', '261', '309', '3', '2', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20120505', 'YLGSB2012070004', '液态', '4', '12', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('813', '啤酒', '834', '813', '7', '1292', '4', '10926', '217', '320', '349', '2', '1', '雪花', '优级', '', '580ml/瓶', 'GB4927', '120226', 'JYPJF2012060009', '液态', '3.00', '2*9', '20*9', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('814', '混合果蔬', '835', '814', '12', '1491', '4', '1768', '217', '261', '309', '1', '4', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20111113', 'YLGSB2012070005', '液态', '4', '12', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('815', '混合果蔬', '836', '815', '12', '1491', '4', '1768', '217', '261', '309', '3', '2', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20120429', 'YLGSB2012070006', '液态', '4', '12', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('816', '菠萝爽', '837', '816', '12', '1491', '4', '1768', '217', '269', '309', '3', '2', '利兴达', '', '', '600ml/瓶', 'Q/LXD0001S-2011', '2012/04/20', 'YLGSB2012070007', '液态', '1.5', '4500', '6000', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('817', '果粒橙', '838', '817', '12', '1491', '4', '1768', '217', '269', '309', '3', '2', '利兴达', '', '', '600ml/瓶', 'Q/LXD0001S-2011', '2012/04/21', 'YLGSB2012070008', '液态', '1.5', '4500', '6000', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('818', '蓝狮超纯啤酒', '839', '818', '7', '1292', '4', '10926', '217', '298', '349', '2', '1', '蓝狮', '', '', '330ml/瓶', 'GB4927', '20120210', 'JYPJF2012060010', '液态', '2.5', '72', '96', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('819', '葡萄汁饮品', '840', '819', '12', '1491', '4', '1768', '217', '12', '309', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'Q/WHJ0283S', '201203262114CD', 'YLGSB2012070009', '液态', '2', '675', '750', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('820', '奶茶（草莓味）', '841', '820', '12', '1502', '4', '1768', '141', '315', '316', '15', '10', '优乐美', '', '', '22g/包', 'Q/XZL0002S', '20120331阳624 13:47', 'YLGTB2012070008', '固态', '1', '50', '120', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('821', '晶莹葡萄', '842', '821', '12', '1491', '4', '1768', '217', '187', '309', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'Q/WHJ0286S', '201205181200GG', 'YLGSB2012070010', '液态', '2', '660', '750', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('822', '水蜜桃汁饮品', '843', '822', '12', '1491', '4', '1768', '217', '12', '309', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'Q/WHJ0287S', '201204282204CD', 'YLGSB2012070011', '液态', '2', '600', '750', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('823', '雀巢咖啡', '844', '823', '12', '1502', '4', '1768', '179', '322', '316', '2', '1', '雀巢咖啡', '', '', '143g/盒', 'Q/QC0004S', '20111107', 'YLGTB2012070009', '固态', '15', '14', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('824', '菓珍', '845', '824', '9', '1502', '4', '18578', '145', '319', '316', '2', '1', '卡夫', '', '', '450g/袋', 'Q/KAFUGT9', '20120625 13:12 D2', 'YLGTJ2012070001', '固态', '15', '2', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('825', '奶茶（巧克力味）', '846', '825', '12', '1502', '4', '1768', '179', '315', '316', '15', '10', '优乐美', '', '', '22g/盒', 'Q/XZL0002S', '20120203阳324 14:57', 'YLGTB2012070010', '固态', '1', '40', '120', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('827', '优乐美奶茶（原味）', '848', '827', '12', '1502', '4', '1768', '263', '315', '316', '4', '3', '优乐美', '', '', '80g/杯', 'Q/XZL0002S', '20120315阳', 'YLGTB2012070011', '固态', '3', '35', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('828', '冰工厂', '849', '828', '4', '1534', '4', '3', '239', '266', '426', '2', '1', '伊利', '', '', '23克×4支', 'SB/T10016', '20120505', 'LYQTA2012070004', '固体', '2.00', '5', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('829', '菓珍（蜜桃味）', '850', '829', '12', '1502', '4', '1768', '145', '319', '316', '2', '1', '卡夫', '', '', '200g/袋', 'Q/KAFUGT0008S', '20120318 04:57C4', 'YLGTB2012070012', '固态', '9', '10', '20', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('830', '菓珍（阳光甜橙味）', '851', '830', '12', '1502', '4', '1768', '145', '319', '316', '2', '1', '卡夫', '', '', '200g/袋', 'Q/KAFUGT0009S', '20120503 04 :14A4', 'YLGTB2012070013', '固态', '9', '8', '20', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('831', '千层雪棒', '852', '831', '4', '1532', '4', '3', '239', '324', '426', '2', '1', '和路雪', '', '', '62克', 'SB/T10013', '20120306', 'LYQTA2012070005', '固体', '3.50', '5', '15', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('832', '雀巢果维', '853', '832', '9', '1502', '4', '18578', '145', '325', '316', '2', '1', 'Nestle\'', '', '', '500g/袋', 'Q/OAEF7', '2011122316:10', 'YLGTJ2012070002', '固态', '14.5', '7', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('833', '开心果园果味饮料', '854', '833', '7', '1491', '4', '10926', '217', '323', '307', '2', '1', '飞龙峡', '', '', '600ml/瓶', 'Q/71304491-9.01', '20120410A', 'YLGSF2012060001', '液态', '2.50', '3', '15', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('834', '香飘飘奶茶（香芋味）', '855', '834', '12', '1502', '4', '1768', '263', '327', '316', '5', '2', '香飘飘', '', '', '80g/杯', 'Q/XPP0001S', '20120329CD107543', 'YLGTB2012070014', '固态', '3.7', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('835', '新桂圆八宝粥', '856', '835', '8', '219', '4', '7363', '217', '328', '393', '7', '1', '景威', '', '', '310克/瓶', 'Q/JWB0006S', '20120416', 'GTQTD2012070009', '粘稠液', '3.0', '1*4', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('836', '苹果汁饮品', '857', '836', '7', '1491', '4', '10926', '217', '326', '307', '2', '1', '康师傅', '', '', '450g/瓶', 'Q/14A0239S', '20120418', 'YLGSF2012060002', '液态', '2.5', '3', '15', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('837', '优乐美奶茶（香芋味） ）', '858', '837', '12', '1502', '4', '1768', '263', '315', '316', '5', '2', '优乐美', '', '', '80g/杯', 'Q/XZL0002S', '20120413阳', 'YLGTB2012070015', '固态', '2.9', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('838', '随便雪糕', '859', '838', '4', '1532', '4', '3', '239', '329', '426', '4', '1', '蒙牛', '', '', '70克', 'SB/T10015', '20120306', 'LYQTA2012070006', '固体', '2.50', '20', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('839', '珍珠奶茶', '860', '839', '12', '1502', '4', '1768', '263', '327', '316', '5', '2', '优乐美', '', '', '70g/杯', 'Q/XPP0001S', '20120227CD109', 'YLGTB2012070016', '固态', '3.8', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('840', '水蜜桃汁饮品', '861', '840', '12', '1491', '4', '1768', '217', '276', '309', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'Q/WHJ0933S', '201205011203FB', 'YLGSB2012070012', '液态', '3.5', '300', '500', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('841', '菓珍', '862', '841', '9', '1502', '4', '18578', '145', '319', '316', '2', '1', '卡夫', '', '', '450g/袋', 'Q/KAFUGT9', '2011071814:56B3', 'YLGTJ2012070003', '固态', '15', '13', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('842', '菓珍（阳光香橙味）', '863', '842', '12', '1502', '4', '1768', '145', '319', '316', '2', '1', '卡夫', '', '', '200g/袋', 'Q/KAFUGT0009S', '20120503', 'YLGTB2012070017', '固态', '8.9', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('843', '水蜜桃汁饮品', '864', '843', '12', '1491', '4', '1768', '217', '330', '309', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'GB/T21731', '201205161116GG', 'YLGSB2012070013', '液态', '2.0', '800', '1000', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('844', '雀巢果维', '865', '844', '12', '1502', '4', '1768', '145', '325', '316', '2', '1', '雀巢', '', '', '125g/袋', 'Q/OAEF7', '20130812A20120212', 'YLGTB2012070018', '固态', '8.9', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('845', '心多多雪糕', '866', '845', '4', '1532', '4', '3', '239', '266', '426', '4', '1', '伊利', '', '', '75克', 'SB/T10015', '20120414', 'LYQTA2012070007', '固体', '2.50', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('846', '纯果乐鲜果粒饮料', '867', '846', '9', '1491', '4', '18578', '217', '331', '307', '3', '2', '纯果乐', '', '', '420ml/瓶', '', '20120228K121:390750', 'YLGSJ2012070012', '液体', '4元', '15瓶', '60瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('847', '菓珍（欢畅柠檬味）', '868', '847', '12', '1502', '4', '1768', '145', '319', '316', '2', '1', '卡夫', '', '', '200g/袋', 'Q/KAFUGT9S', '20111209', 'YLGTB2012070019', '固态', '8.9', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('848', '芒果雪泥', '869', '848', '4', '1534', '4', '3', '239', '329', '426', '4', '1', '蒙牛', '', '', '75克', 'SB/T10014', '20120511', 'LYQTA2012070008', '固体', '2.50', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('849', '真好喝橙粒苹果饮料', '870', '849', '7', '1491', '4', '10926', '217', '275', '307', '2', '1', '澳地澳', '', '', '600g/瓶', 'Q/KAD04-2009', '2012/03/30', 'YLGSF2012060003', '液态', '2.4', '150', '200', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('850', '菓珍', '871', '850', '9', '1502', '4', '18578', '145', '319', '316', '2', '1', '卡夫', '', '', '450g/袋', 'Q/KAFUGT9', '2011041108:37A3', 'YLGTJ2012070004', '固态', '16.5', '-', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('851', '千层雪棒', '872', '851', '4', '1532', '4', '3', '239', '324', '426', '4', '1', '和路雪', '', '', '62克', 'SB/T10013', '20120504', 'LYQTA2012070009', '固体', '3.50', '20', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('852', '随便雪糕', '873', '852', '4', '1532', '4', '3', '239', '329', '426', '4', '1', '蒙牛', '', '', '75克', 'SB/T10015', '20120513', 'LYQTA2012070010', '固体', '2.50', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('853', '蓝莓绿茶', '874', '853', '12', '1491', '4', '1768', '217', '276', '309', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'GB/T21733', '20120222AH', 'YLGSB2012070014', '液态', '3.5', '60', '600', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('854', '水蜜桃汁饮品', '875', '854', '7', '1491', '4', '10926', '217', '332', '307', '2', '1', '哇哈哈', '', '', '500ml/瓶', 'Q/WHJ0287S', '20120403', 'YLGSF2012060004', '液态', '2.00', '10', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('855', '燕麦八宝粥', '876', '855', '8', '219', '4', '7363', '217', '333', '393', '7', '1', '康辉', '', '', '320g/瓶', 'Q/FJKH002', '20111228', 'GTQTD2012070010', '粘稠液', '3.2', '120', '86', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('856', '雀巢果味', '877', '856', '9', '1502', '4', '18578', '145', '325', '316', '2', '1', 'Nestle\'', '', '', '500g/袋', 'Q/OAEF7', '2012021310:00', 'YLGTJ2012070005', '固态', '14.5', '40', '48', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('857', '云南小粒咖啡', '878', '857', '12', '1502', '4', '1768', '179', '334', '316', '2', '1', '捷品', '', '', '130g/盒', 'Q/KJP0002S', '2012/05/04', 'YLGTB2012070020', '固态', '12.8', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('858', '芒果汁饮料', '879', '858', '12', '1491', '4', '1768', '217', '335', '309', '3', '2', '瑞丽江', '', '', '226ml/瓶', 'Q/KYR01', '2012/04/18', 'YLGSB2012070015', '液态', '4.0', '47', '90', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('859', '绿色心情雪糕', '880', '859', '4', '1533', '4', '3', '239', '314', '426', '4', '1', '伊利', '', '', '71克', 'Q/NYLC0007S', '20120209', 'LYQTA2012070011', '固体', '1.50', '96', '480', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('860', '菓珍', '881', '860', '9', '1502', '4', '18578', '145', '319', '316', '2', '1', '卡夫', '', '', '200克/袋', 'Q/KAFUGT0009S', '20120214', 'YLGTJ2012070006', '固态', '8.5', '10', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('861', '果粒多', '882', '861', '7', '1491', '4', '10926', '217', '336', '307', '2', '1', '果粒多', '', '', '2L/瓶', 'Q/GHY0001S-2011', '2012/02/17', 'YLGSF2012060005', '液态', '6', '6', '150', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('862', '苦咖啡雪糕', '883', '862', '4', '1532', '4', '3', '239', '314', '426', '7', '3', '伊利', '', '', '70克', 'SB/T10015', '20120503V', 'LYQTA2012070012', '固体', '2.00', '192', '480', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('863', '巧克力可米脆牛奶雪糕', '884', '863', '4', '1532', '4', '3', '239', '284', '426', '7', '3', '洞桥', '', '', '60克', 'Q/79182172-8.3', '20120513', 'LYQTA2012070013', '固体', '1.00', '200', '400', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('864', '鲜果粒', '885', '864', '12', '1491', '4', '1768', '217', '337', '309', '3', '2', 'Tropicana', '', '', '420ml/瓶', 'GB/T21731', '20111205K1', 'YLGSB2012070016', '液态', '3.9', '104', '300', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('865', '绿豆炼奶雪糕', '886', '865', '4', '1532', '4', '3', '239', '314', '426', '7', '3', '佰豆集', '', '', '70克', 'SB/T10015', '20120103V', 'LYQTA2012070014', '固体', '1.50', '144', '720', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('866', '玉米粗粮饮料', '887', '866', '12', '1491', '4', '1768', '217', '257', '309', '3', '2', '希之源', '', '', '428ml/瓶', 'Q/EMH015', '2012/03/01', 'YLGSB2012070017', '液态', '6.2', '87', '150', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('867', '克力棒雪糕', '888', '867', '4', '1532', '4', '3', '239', '314', '426', '7', '3', '伊利', '', '', '65克', 'SB/T10015', '20111217', 'LYQTA2012070015', '固体', '1.50', '144', '720', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('868', '喝的爽', '889', '868', '12', '1491', '4', '1768', '217', '338', '309', '3', '2', '健娃', '', '', '500ml/瓶', 'Q/XWT07', '2012/04/15', 'YLGSB2012070018', '液态', '2.5', '140', '228', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('869', '水果饮料', '890', '869', '7', '1491', '4', '10926', '217', '339', '307', '2', '1', '小数点', '', '', '600ml/瓶', 'Q/JLR009', '2012/04/14', 'YLGSF2012060006', '液态', '3.00', '150', '225', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('870', '冰工厂', '891', '870', '4', '1534', '4', '3', '239', '314', '426', '4', '1', '伊利', '', '', '80克', 'SB/T10016', '20120429V', 'LYQTA2012070016', '固体', '1.20', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('871', '巧乐兹雪糕', '892', '871', '4', '1532', '4', '3', '239', '314', '426', '4', '1', '伊利', '', '', '80克', 'SB/T10015', '20120510V', 'LYQTA2012070017', '固体', '2.80', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('872', '杨梅果汁饮料', '893', '872', '7', '1491', '4', '10926', '217', '340', '307', '2', '1', '扬百利', '', '', '450ml/瓶', 'Q/ZJYM2', '2012/02/16', 'YLGSF2012060007', '液态', '4.00', '15', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('873', '菠萝粒苹果饮料', '894', '873', '7', '1491', '4', '10926', '217', '275', '307', '2', '1', '澳地澳', '', '', '600g/瓶', 'Q/KAD04-2009', '2012/03/30', 'YLGSF2012060008', '液态', '4.00', '15', '75', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('874', '混合果蔬', '895', '874', '12', '1491', '4', '1768', '217', '261', '309', '3', '2', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '201205071632G6', 'YLGSB2012070019', '液态', '4.0', '65', '120', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('875', '芒果多', '896', '875', '12', '1491', '4', '1768', '217', '259', '309', '3', '2', '统一', '', '', '450ml/瓶', 'Q/TYK0001S', '20111019KM2', 'YLGSB2012070020', '液态', '3.9', '87', '150', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('876', '绿叶牌芒果汁饮料', '897', '876', '7', '1491', '4', '10926', '217', '341', '307', '2', '1', '绿叶牌', '', '', '1L/瓶', '', '20120218', 'YLGSF2012060009', '液态', '18.9', '70', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('877', '农夫果园混合果蔬', '898', '877', '7', '1491', '4', '10926', '217', '261', '309', '2', '1', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20120304', 'YLGSF2012060010', '液态', '3.7', '120', '70', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('878', '七个苹果', '899', '878', '7', '1491', '4', '10926', '217', '260', '307', '2', '1', '绿杰', '', '', '280ml/瓶', 'Q/YLJ0002S-2010', '2012/04/26', 'YLGSF2012060011', '液态', '5.8', '78', '260', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('879', '快乐宝贝', '900', '879', '7', '1491', '4', '10926', '217', '342', '307', '2', '1', '快乐王子', '', '', '338ml/瓶', 'Q/KKL0002S-2010', '2012/01/06', 'YLGSF2012060012', '液态', '3.00', '4', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('880', '糊辣椒面', '901', '880', '3', '253', '4', '3', '193', '343', '95', '400', '200', '', '', '', '散装', '', '20120620', 'PZQTA2012060001', '', '', '3斤', '8斤', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('881', '果粒橙', '902', '881', '7', '1491', '4', '10926', '217', '344', '307', '2', '1', '喜鹭', '', '', '500ml/瓶', 'Q/KLDX003-2008', '2012.04.02', 'YLGSF2012060013', '液态', '', '5', '20', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('882', '香约奶茶', '903', '882', '9', '1502', '4', '18578', '179', '345', '316', '2', '1', '香约', '', '', '64克/盒', 'Q/DHD0009S', '2012022104', 'YLGTJ2012070007', '固态', '3.00', '50', '100', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('883', '糍粑辣椒', '904', '883', '3', '253', '4', '3', '193', '346', '95', '400', '200', '', '', '', '散装', '', '20120620', 'PZQTA2012060002', '', '', '3斤', '5斤', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('884', '七个苹果', '905', '884', '7', '1491', '2', '10926', '217', '260', '307', '2', '1', '绿杰', '', '', '280ml/瓶', 'Q/YLJ0002S-2010', '2012/02/15', 'YLGSF2012060014', '液态', '6.00', '1*12', '5.00', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('885', '热带果粒', '906', '885', '7', '1491', '4', '10926', '217', '347', '307', '2', '1', '美汁源', '', '', '420ml/瓶', 'QSBDG0001S', '20120301', 'YLGSF2012060015', '液态', '3.5', '8', '1212', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('886', '风味豆豉油制辣椒', '907', '886', '3', '253', '2', '3', '217', '348', '95', '2', '1', '陶华碧', '', '', '280克/瓶', 'GB/T20293', '20120430', 'PZQTA2012060003', '', '', '5瓶', '24瓶', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('887', '蜜桃多', '908', '887', '7', '1491', '4', '10926', '217', '259', '307', '2', '1', '统一', '', '', '450ml/瓶', 'Q/TYK0001S', '20111114', 'YLGSF2012060016', '液态', '3.00', '9', '1*12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('888', '桂林风味辣椒酱', '909', '888', '3', '253', '4', '3', '217', '349', '95', '2', '1', '海天', '', '', '230克/瓶', 'Q/HT0005S', '20111020', 'PZQTA2012060004', '', '', '10瓶', '30瓶', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('889', '醇豆浆', '910', '889', '9', '1501', '4', '18578', '145', '295', '316', '2', '1', '维维', '', '', '500g/袋', 'GB/T18738', '201203220606H', 'YLGTJ2012070008', '固态', '15.8', '5', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('890', '果珍鲜', '911', '890', '7', '1491', '2', '10926', '217', '350', '307', '2', '1', '山尔', '', '', '2L/瓶', 'Q/GQS01', '201.04.14', 'YLGSF2012060017', '液态', '10.0', '1*6', '5*6', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('891', '美乐香辣酱', '912', '891', '3', '253', '4', '3', '217', '351', '95', '2', '1', '美乐', '一级', '', '350g/瓶', 'Q/20430609-5.2', '20120418', 'PZQTA2012060005', '', '', '20瓶', '24瓶', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('892', '七个苹果发酵型果醋饮料', '913', '892', '7', '1491', '4', '10926', '217', '260', '307', '2', '1', '绿杰', '', '', '280ml/瓶', 'Q/YLJ0002S-2010', '2012/05/10', 'YLGSF2012060018', '液态', '6', '600', '1400', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('893', '苹果醋', '914', '893', '7', '1491', '4', '10926', '217', '352', '307', '2', '1', '金果源', '', '', '330ml/瓶', 'Q/YCL0001S-2011', '2012/05/10', 'YLGSF2012060019', '液态', '6.00', '90', '150', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('894', '欢畅柠檬味速溶固体饮料', '915', '894', '10', '1502', '4', '9567', '145', '319', '321', '2', '1', '', '普通型', '', '400克/袋', 'Q/KAFUGT 9（果味固体饮料）', '', 'YLGTE2012060001', '粉末', '12.8', '12', '24', '6904724022420', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('895', '辣椒油', '916', '895', '3', '253', '4', '3', '217', '353', '95', '2', '1', '香如许', '一级', '', '200ml/瓶', 'Q/74033165-7.3', '20111204', 'PZQTA2012060006', '', '', '8瓶', '12瓶', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('896', '浓香咖啡', '917', '896', '9', '1502', '4', '18578', '179', '354', '316', '2', '1', '温莎威尔', '', '', '168g/盒', 'Q/STASD0005S', '2011/12/01', 'YLGTJ2012070009', '固态', '16.5', '5', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('897', '混合果蔬', '918', '897', '7', '1491', '4', '10926', '217', '261', '309', '2', '1', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20120304', 'YLGSF2012060020', '液态', '4.00', '15', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('898', '经典醇麦香旋舞奶味茶', '919', '898', '9', '1502', '4', '18578', '179', '316', '316', '2', '1', '立顿', '', '', '64g/盒', 'Q/TNBE 1010S', '20120107H2B2', 'YLGTJ2012070010', '固态', '3.5', '50', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('899', '麦斯威尔特浓咖啡', '920', '899', '10', '1502', '4', '9567', '179', '319', '321', '2', '1', '', '普通型', '', '130克/盒', 'Q/KAFUGT2', '20111113', 'YLGTE2012060002', '粉末状', '16.50', '15', '24', '6901721496230', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('900', '菓珍', '921', '900', '9', '1502', '4', '18578', '145', '319', '316', '2', '1', '卡夫', '', '', '400g/袋', 'Q/KAFUGT9', '20120105 03：49：A3', 'YLGTJ2012070011', '固态', '15.8', '10', '40', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('901', '雀巢咖啡', '922', '901', '10', '1502', '4', '9567', '179', '355', '321', '3', '2', 'NESCAFE', '普通型', '', '36克/盒', 'Q/QC0001S', '20111202', 'YLGTE2012060003', '粉末状', '17.00', '15', '48', '6917878008691', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('902', '卡夫菓珍', '923', '902', '9', '1502', '4', '18578', '145', '319', '316', '2', '1', '卡夫', '', '', '450g/袋', 'Q/KAFUGT9', '2011061203：07 A2', 'YLGTJ2012070012', '固态', '15.8', '20', '40', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('903', '小雪生雪糕', '924', '903', '7', '1532', '4', '10926', '239', '356', '428', '7', '3', '伊利', '', '', '65克/支', 'SB/T10015', '2012.5.4', 'LYQTF2012060013', '', '1.5', '5', '50', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('904', '芒果味固体饮料', '925', '904', '10', '1502', '4', '9567', '145', '325', '321', '2', '1', '雀巢果维', '普通型', '', '500克/袋', 'Q/OAEF7', '20120130', 'YLGTE2012060004', '粉末状', '14.90', '36', '48', '6917878014876', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('905', '雀巢果维', '926', '905', '9', '1502', '4', '18578', '145', '325', '316', '2', '1', 'Nestle\'', '', '', '500g/袋', 'Q/OAEF7', '20110701 09：45', 'YLGTJ2012070013', '固态', '14.5', '8', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('906', '优乐美奶茶麦香味', '927', '906', '10', '1502', '4', '9567', '145', '315', '321', '6', '3', '优乐美', '普通型', '', '22克/袋', 'Q/XZL0002S', '20120417', 'YLGTE2012060005', '粉末状', '1.0', '66', '150', '6926475204283', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('907', '心多多雪糕', '928', '907', '7', '1532', '4', '10926', '239', '357', '428', '7', '3', '佰豆集', '', '', '75克/支', 'SB/T10015', '2012.5.10', 'LYQTF2012060014', '', '2.0', '10', '50', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('908', '卡夫菓珍', '929', '908', '9', '1502', '4', '18578', '145', '319', '316', '2', '1', '卡夫', '', '', '200g/袋', 'Q/KAFUGT9', '2011061203：07 A2', 'YLGTJ2012070014', '固态', '8.5', '5', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('909', '高樂高', '930', '909', '9', '1501', '4', '18578', '145', '313', '316', '2', '1', '高樂高', '', '', '200g/袋', 'Q/14A0131S', '2012-03-0609：58：09', 'YLGTJ2012070015', '固态', '14.5', '8', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('910', '苦咖啡雪糕', '931', '910', '7', '1532', '4', '10926', '239', '357', '428', '7', '3', '伊利', '', '', '70克/支', 'SB/T10015', '2012.5.5', 'LYQTF2012060015', '', '2.0', '10', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('911', '欢畅柠檬味速溶固体饮料', '932', '911', '10', '1502', '4', '9567', '145', '319', '321', '2', '1', '', '普通型', '', '450克/袋', 'Q/KAFUGT9（果味固体饮料）', '20110508', 'YLGTE2012060006', '粉末状', '15.80', '5', '12', '6904724019727', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('912', '优乐美奶茶', '933', '912', '9', '1502', '4', '18578', '179', '315', '316', '2', '1', '喜之郎', '', '', '80g/盒', 'Q/XZL0002S', '20120331阳24403314 14：05', 'YLGTJ2012070016', '固态', '3', '35', '120', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('913', '巧乐兹雪糕', '934', '913', '7', '1532', '4', '10926', '239', '358', '428', '7', '3', '伊利', '', '', '75克/支', 'SB/T10015', '2012.5.14', 'LYQTF2012060016', '', '2.0', '4', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('914', '草莓味珍珠奶茶', '935', '914', '10', '1502', '4', '9567', '179', '315', '321', '2', '1', '优乐美', '普通型', '', '70g/盒', 'Q/XZL0002S', '20120419', 'YLGTE2012060007', '粉末状', '3.50', '60', '80', '6926475203729', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('915', '安琪儿菊花晶', '936', '915', '9', '1502', '4', '18578', '145', '359', '316', '2', '1', 'XLX', '', '', '400g/袋', 'Q/JLX006-2009', '2011108101-6', 'YLGTJ2012070017', '固态', '9.8', '8', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('916', '优乐美奶茶', '937', '916', '10', '1502', '4', '9567', '145', '315', '321', '5', '3', '优乐美', '普通型', '', '22克/袋', 'Q/XZL0002S', '20120420', 'YLGTE2012060008', '粉末状', '1.0', '100', '200', '6926475204115', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('917', '绿色情怡雪糕', '938', '917', '7', '1533', '4', '10926', '239', '357', '428', '7', '3', '伊利', '', '', '71克/支', 'Q/NYLC0007S', '2012.3.16', 'LYQTF2012060017', '', '1.10', '20', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('918', '醇品雀巢咖啡', '939', '918', '10', '1502', '4', '9567', '217', '355', '321', '2', '1', 'Rich Blend', '普通型', '', '50g/瓶', 'Q/QC0001S', '20110530', 'YLGTE2012060009', '颗粒', '22.50', '18', '25', '6903473100014', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('919', '蒙牛巧脆兹雪糕', '940', '919', '7', '1532', '4', '10926', '239', '272', '428', '7', '3', '蒙牛', '', '', '78克/支', 'SB/T10015', '2012.5.5', 'LYQTF2012060018', '', '2.0', '10', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('920', '黑加仑味固体饮料', '941', '920', '10', '1502', '4', '9567', '145', '325', '321', '2', '1', '雀巢果维', '普通型', '', '125克/袋', 'Q/OAEF7', '20120212', 'YLGTE2012060010', '粉末状', '5.4', '8', '12', '6917878022758', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('921', '优乐美奶茶', '942', '921', '9', '1502', '4', '18578', '179', '315', '316', '2', '1', '喜之郎', '', '', '80g/盒', 'Q/XZL0002S', '201203302140330412：44', 'YLGTJ2012070018', '固态', '3', '25', '180', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('922', '雀巢咖啡', '943', '922', '9', '1502', '4', '18578', '179', '355', '316', '2', '1', '雀巢咖啡', '', '', '130g/盒', 'Q/QC00045', '20111122', 'YLGTJ2012070019', '固态', '16.7', '12', '36', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('923', '经典巧恋果', '944', '923', '7', '1532', '4', '10926', '239', '357', '428', '7', '3', '伊利', '', '', '80克/支', 'SB/T10015', '2012.5.18', 'LYQTF2012060019', '', '2.5', '20', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('924', '雀巢咖啡原味', '945', '924', '10', '1502', '4', '9567', '179', '355', '321', '2', '1', '雀巢咖啡', '普通型', '', '169克/盒', 'Q/QC0004S', '20110730', 'YLGTE20120600011', '粉末状', '16.00', '6', '24', '6917878007441', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('925', '高樂高营养型固体饮料', '946', '925', '9', '1501', '4', '18578', '217', '313', '316', '2', '1', '高樂高', '', '', '350g/瓶', 'Q/14A0131S', '2011-06-0713：02：23', 'YLGTJ2012070020', '固态', '43.5', '8', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('926', '小布丁', '947', '926', '7', '1532', '4', '10926', '239', '284', '428', '7', '3', '洞桥', '', '', '48克/支', 'Q/78182172-8.3', '2012.4.9', 'LYQTF2012060020', '', '0.35', '40', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('927', '柠檬味固体饮料', '948', '927', '10', '1502', '4', '9567', '145', '325', '321', '2', '1', '雀巢果维', '普通型', '', '500克/袋', 'Q/OAEF7', '20111221', 'YLGTE2012060012', '粉末状', '16.50', '8', '24', '6917878007731', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('928', '咖啡伴侣植脂末', '949', '928', '10', '1502', '4', '9567', '217', '355', '321', '2', '1', 'Nestle', '普通型', '', '100克/瓶', 'Q/QC 0002S', '20110803', 'YLGTE20120600013', '粉末状', '12.8', '10', '', '6903473021067', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('929', '爱尚草莓', '950', '929', '7', '1532', '4', '10926', '239', '360', '428', '7', '3', '万林', '', '', '90克/盒', 'SB/T10015', '2012-2-27', 'LYQTF2012060021', '', '0.75', '1200', '7200', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('930', '桂圆莲子八宝粥', '951', '930', '7', '219', '4', '10926', '217', '361', '424', '7', '3', '好+1', '', '', '320g/瓶', 'QB/T2221', '20120404', 'GTQTF2012060001', '', '3.5', '84', '240', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('931', '桂圆莲子营养八宝粥', '952', '931', '7', '219', '4', '10926', '217', '12', '424', '7', '1', '娃哈哈', '', '', '360克/瓶', 'QB/T2221', '20120127', 'GTQTF2012060002', '', '3.5', '100', '100', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('932', '红枣桂圆八宝粥', '953', '932', '7', '219', '4', '10926', '217', '288', '424', '7', '1', '达利园', '', '', '360g/瓶', 'QB/T2221', '20111214', 'GTQTF2012060003', '', '2.7', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('933', '好粥道莲子玉米粥', '954', '933', '7', '219', '4', '10926', '217', '292', '424', '7', '1', '银鹭', '', '', '280g/瓶', 'Q/XMYL0006S', '20111111', 'GTQTF2012060004', '', '4.0', '105', '120', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('934', '桂圆莲子八宝粥', '955', '934', '7', '219', '4', '10926', '217', '263', '424', '7', '1', '银鹭', '', '', '360克/瓶', 'QB/T2221', '20120105', 'GTQTF2012060005', '', '4.0', '120', '240', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('935', '强人八宝粥', '956', '935', '7', '219', '4', '10926', '217', '362', '424', '7', '1', '强人', '', '', '360g/瓶', 'QB/T2221', '20120321', 'GTQTF2012060006', '', '3.8', '38', '80', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('936', '桂圆莲子八宝粥', '957', '936', '7', '219', '4', '10926', '217', '290', '424', '7', '1', '华雄', '', '', '320克/瓶', 'Q/SPS0002S', '2012/01/30', 'GTQTF2012060007', '', '3', '10', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('937', '桂圆八宝粥', '958', '937', '7', '219', '4', '10926', '217', '263', '424', '7', '1', '银鹭', '', '', '360g/瓶', 'QB/T2221', '20120321', 'GTQTF2012060008', '', '4', '13', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('938', '好粥道黑米粥', '959', '938', '7', '219', '4', '10926', '217', '263', '424', '7', '1', '银鹭', '', '', '280g/瓶', 'Q/XMYL0006S', '20120301', 'GTQTF2012060009', '', '4', '17', '48', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('939', '强人八宝粥', '960', '939', '7', '219', '4', '10926', '217', '362', '424', '7', '1', '强人', '', '', '360克/瓶', 'QB/T2221', '20120324', 'GTQTF2012060010', '', '3.8', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('940', '美汁源果粒橙', '961', '940', '11', '1491', '4', '14940', '217', '255', '306', '2', '1', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0001S', '20120327', 'YLGSH2012070001', '液态', '3.0', '20', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('941', '冰工厂', '962', '941', '4', '1534', '4', '3', '239', '314', '426', '4', '1', '伊利', '', '', '80克', 'SB/T10016', '20120428V', 'LYQTA2012070018', '固体', '1.20', '24', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('942', '苦咖啡雪糕', '963', '942', '4', '1532', '4', '3', '239', '314', '426', '4', '1', '伊利', '', '', '70克', 'SB/T10015', '20120427V', 'LYQTA2012070019', '固体', '2.30', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('943', '心多多雪糕', '964', '943', '4', '1532', '4', '3', '239', '314', '426', '4', '1', '伊利', '', '', '75克', 'SB/T10015', '20120505V', 'LYQTA2012070020', '固体', '2.30', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('944', '菓珍阳光甜橙味', '965', '944', '4', '1502', '4', '3', '141', '319', '289', '2', '1', '卡夫', '', '', '400克', 'Q/KAFUGT0009S', '20120213', 'YLGTA2012070001', '粉末', '15.50', '5', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('945', '雀巢咖啡', '966', '945', '4', '1501', '4', '3', '179', '355', '289', '2', '1', '雀巢咖啡', '', '', '130克', 'Q/QC004S', '20111122', 'YLGTA2012070002', '粉末', '14.00', '5', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('946', '珍珠奶茶', '967', '946', '4', '1501', '4', '3', '263', '327', '289', '2', '1', '香飘飘', '', '', '70克', 'Q/XPP0001S', '20120107', 'YLGTA2012070003', '粉末', '3.50', '15', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('947', '雀巢咖啡', '968', '947', '4', '1501', '4', '3', '179', '355', '289', '2', '1', '雀巢咖啡', '', '', '143克', 'Q/QC004S', '20120110', 'YLGTA2012070004', '粉末', '14.00', '5', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('948', '雀巢咖啡', '969', '948', '4', '1501', '4', '3', '179', '355', '289', '2', '1', '雀巢咖啡', '', '', '169克', 'Q/QC0004S', '20110320', 'YLGTA2012070005', '粉末', '15.00', '6', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('949', '橙汁', '970', '949', '8', '1491', '4', '7363', '217', '267', '307', '3', '2', '汇源', '', '', '200毫升/瓶', 'GB21731', 'B20120210', 'YLGSD2012070001', '液态', '3.5', '45瓶', '48瓶', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('951', '雀巢咖啡', '972', '951', '4', '1501', '4', '3', '179', '355', '289', '2', '1', '雀巢咖啡', '', '', '130克', 'Q/QC0004S', '20111208', 'YLGTA2012070006', '粉末', '15.60', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('953', '混合果蔬', '974', '953', '8', '1491', '2', '7363', '217', '277', '307', '3', '2', '农夫果园', '', '', '1.8L/瓶', 'Q/NFS0003S', '20110908', 'YLGSD2012070002', '液态', '8.0', '6瓶', '30瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('956', '香蕉口味营养型固体饮料', '977', '956', '4', '1501', '4', '3', '141', '313', '289', '2', '1', '高乐高', '', '', '200克', 'Q/14A0131S', '20110422', 'YLGTA2012070007', '粉末', '30.00', '5', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('957', '莲子玉米粥罐头', '978', '957', '9', '219', '4', '18578', '169', '263', '377', '7', '1', '银鹭', '', '', '280g/罐', 'Q/XMYL0006S', '20120110XM', 'GTQTJ2012070001', '', '3.8', '15', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('959', '黑米粥罐头', '980', '959', '9', '219', '4', '18578', '169', '263', '377', '7', '1', '银鹭', '', '', '280g/罐', 'Q/XMYL0006S', '20120130XM', 'GTQTJ2012070002', '', '3.8', '9', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('961', '桂圆莲子八宝粥罐头', '982', '961', '9', '219', '4', '18578', '169', '294', '377', '7', '1', '银鹭', '', '', '360g/罐', 'QB/T2221', '20110731JN', 'GTQTJ2012070003', '', '3.6', '40', '120', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('962', '蜂蜜绿茶固体饮料', '983', '962', '4', '1502', '4', '3', '179', '316', '289', '2', '1', '立顿', '', '', '100克', 'Q/TNBE 1001S', '20120411', 'YLGTA2012070008', '粉末', '17.90', '10', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('963', '旺旺包果粒多葡萄汁', '984', '963', '8', '1491', '4', '7363', '141', '365', '307', '3', '2', '果粒多', '', '', '350ml/包', 'Q/NJFW0001S', 'Y20120306', 'YLGSD2012070003', '液态', '3', '24包', '24包', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('964', '雀巢果维芒果味', '985', '964', '4', '1502', '4', '3', '141', '325', '289', '2', '1', '雀巢果维', '', '', '400克', 'Q/PAAV00001S', '20120314', 'YLGTA2012070009', '粉末', '15.90', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('965', '桂圆莲子八宝粥', '986', '965', '9', '219', '4', '18578', '169', '268', '377', '7', '1', '娃哈哈', '', '', '360g/罐', 'QB/T2221', '20120309112235CD', 'GTQTJ2012070004', '', '3.5', '12', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('967', '旺旺果粒多橙汁饮料', '988', '967', '8', '1491', '4', '7363', '145', '365', '307', '3', '2', '旺旺', '', '', '350ml/袋', 'Q/NJFW0001S', 'Y20120208', 'YLGSD2012070004', '液态', '3', '24', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('968', '低糖莲子八宝粥罐头', '989', '968', '9', '219', '4', '18578', '169', '292', '377', '7', '1', '银鹭', '', '', '360g/罐', 'Q/XMYL0011S', '20120327XG', 'GTQTJ2012070005', '', '3.5', '3', '6', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('969', '菓珍阳光甜橙味', '990', '969', '4', '1502', '4', '3', '141', '319', '289', '2', '1', '卡夫', '', '', '400克', 'Q/KAFUGT 0009S', '20120213', 'YLGTA2012070010', '粉末', '15.50', '12', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('971', '桂圆莲子八宝粥', '992', '971', '9', '219', '4', '18578', '169', '268', '377', '7', '1', '娃哈哈', '', '', '360g/罐', 'QB/T2221', '20111110212256CD', 'GTQTJ2012070006', '', '3.5', '72', '240', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('972', '桂圆莲子营养八宝粥', '993', '972', '9', '219', '4', '18578', '169', '268', '377', '7', '1', '娃哈哈', '', '', '360g/罐', 'QB/T2221', '20120330111426CD', 'GTQTJ2012070007', '', '3.5', '46', '56', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('973', '水溶C100', '994', '973', '8', '1491', '4', '7363', '217', '261', '307', '3', '2', '农夫山泉', '', '', '445ml/瓶', 'Q/NFS0008S', '20120311', 'YLGSD2012070005', '液态', '4.5', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('974', '桂圆莲子八宝粥', '995', '974', '9', '219', '4', '18578', '169', '263', '377', '7', '1', '银鹭', '', '', '360g/罐', 'QB/T2221', '20111213XM', 'GTQTJ2012070008', '', '3.5', '12', '48', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('975', '桂圆莲子八宝粥', '996', '975', '9', '219', '4', '18578', '169', '263', '377', '7', '1', '银鹭', '', '', '360g/罐', 'QB/T2221', '20111219XM', 'GTQTJ2012070009', '', '3.5', '4', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('976', '维他型豆奶粉', '997', '976', '4', '1501', '4', '3', '145', '367', '289', '2', '1', '维维', '', '', '500克', 'GB/T18738', '20120111', 'YLGTA2012070011', '粉末', '15.00', '10', '40', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('977', '珍珠奶茶', '998', '977', '7', '1502', '4', '10926', '179', '327', '316', '2', '1', '香飘飘', '', '', '70克/盒', 'Q/XPP0001S', '20120310', 'YLGTF2012060001', '固态', '3.5', '10', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('978', '真好喝菠萝粒饮料', '999', '978', '8', '1491', '4', '7363', '217', '275', '307', '3', '2', '澳地澳', '', '', '600g/瓶', 'Q/KAD04-2009', '2012/02/07', 'YLGSD2012070006', '液态', '4', '19', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('979', '桂圆莲子八宝粥', '1000', '979', '9', '219', '4', '18578', '169', '268', '377', '7', '1', '娃哈哈', '', '', '360g/罐', 'QB/T2221', '20110912210739CD', 'GTQTJ2012070010', '', '3.5', '10', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('980', '黑芝麻糊', '1001', '980', '4', '1501', '4', '3', '145', '368', '289', '2', '1', '南方', '', '', '360克', 'Q/HWL0001S', '20120305', 'YLGTA2012070012', '粉末', '11.00', '11', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('981', '奶茶', '1002', '981', '7', '1501', '4', '10926', '179', '363', '316', '2', '1', '妙恋', '', '', '80克/盒', 'Q/320124NYSW06', '20120215', 'YLGTF2012060002', '固态', '3.0', '17', '48', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('982', '菓珍', '1003', '982', '7', '1502', '4', '10926', '141', '319', '316', '2', '1', '卡夫', '', '', '200g/包', 'Q/KAFVGT9', '20110413', 'YLGTF2012060003', '固态', '7.50', '20', '8', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('983', '低糖高钙豆奶粉', '1004', '983', '4', '1501', '4', '3', '145', '369', '289', '2', '1', '伊利', '', '', '560克', 'Q/NYLB0048S', '20120316', 'YLGTA2012070013', '粉末', '18.00', '9', '30', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('984', '安琪儿菊花晶', '1005', '984', '7', '1502', '4', '10926', '145', '364', '316', '2', '1', 'XLX', '', '', '400克/袋', 'Q/JLX0060004', '2012/03/26', 'YLGTF2012060004', '固态', '10.08', '5', '5', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('985', '菓珍', '1006', '985', '7', '1502', '4', '10926', '145', '319', '316', '2', '1', '卡夫', '', '', '400克/袋', 'Q/KAFVGT9', '20111206', 'YLGTF2012060005', '固态', '16.9', '70', '100', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('986', '菓珍', '1007', '986', '7', '1502', '4', '10926', '141', '319', '316', '2', '1', '卡夫', '', '', '400克/包', 'Q/KAFVGT9', '20111208', 'YLGTF2012060006', '固态', '15.0', '15', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('987', '菓珍乐爽苹果味', '1008', '987', '4', '1502', '4', '3', '145', '319', '289', '2', '1', '卡夫', '', '', '400克', 'Q/KAFUGT9', '20111129', 'YLGTA2012070014', '粉末', '18.50', '8', '40', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('988', '纯果乐鲜果粒', '1009', '988', '8', '1491', '2', '7363', '217', '370', '307', '3', '2', '百事', '', '', '420毫升/瓶', 'Q/BSYL0018S', '20120322S', 'YLGSD2012070007', '液态', '3.5', '8', '1', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('989', '优乐美奶茶', '1010', '989', '7', '1502', '4', '10926', '179', '366', '316', '2', '1', '优乐美', '', '', '80g/盒', 'Q/XZL0002S', '20111212', 'YLGTF2012060007', '固态', '3.00', '30', '415', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('990', '蜂蜜冰糖蜜桃', '1011', '990', '8', '1491', '4', '7363', '217', '276', '307', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'Q/WHJ0933S', '201205151123FB', 'YLGSD2012070008', '液态', '4', '12', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('991', '雀巢咖啡', '1012', '991', '4', '1501', '4', '3', '179', '325', '289', '1', '1', '雀巢咖啡', '', '', '624克', 'Q/OAEF20', '20120119', 'YLGTA2012070015', '粉末', '46.00', '2', '5', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('992', '香飘飘奶茶', '1013', '992', '7', '1502', '4', '10926', '179', '327', '316', '2', '1', '香飘飘', '', '', '80g/盒', 'Q/XPP0001S', '20120305', 'YLGTF2012060008', '固态', '3.50', '10', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('993', '我滴橙果粒饮料', '1014', '993', '9', '1491', '4', '18578', '217', '371', '306', '3', '2', '我滴', '', '', '500g/瓶', 'Q/ZBG0036S', 'BG2012032218:17YE', 'YLGSJ2012070013', '液态', '5', '20', '60', '6900157527785', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('994', '优乐美奶茶', '1015', '994', '7', '1502', '4', '10926', '179', '315', '316', '2', '1', '喜之郎', '', '', '80g/盒', 'Q/XZL0002S', '20111014', 'YLGTF2012060009', '固态', '3.50', '15', '60', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('995', '菓珍速溶固体饮料', '1016', '995', '7', '1502', '4', '10926', '145', '319', '316', '2', '1', '卡夫', '', '', '450克/袋', 'Q/KAFVGT9', '20110423', 'YLGTF2012060010', '固态', '16.9', '43', '65', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('996', '纯果乐混合果汁饮料', '1017', '996', '9', '1491', '4', '18578', '217', '270', '306', '3', '2', '纯果乐', '', '', '450ml/瓶', 'Q/BSYL0013S', '20120105002112:376570', 'LYGSJ2012070014', '液态', '4', '10', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('997', '橙汁饮料', '1018', '997', '8', '1491', '4', '7363', '217', '372', '307', '3', '2', '汇源', '', '', '1.5L/瓶', 'GB/T21731', 'C20120220', 'YLGSD2012070009', '液态', '6', '3', '500', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('998', '菊花晶宝贝清火宝', '1019', '998', '4', '1502', '4', '3', '145', '373', '289', '2', '1', '美咪', '', '', '308克', 'GB7101', '20120107', 'YLGTA2012070016', '粉末', '12.00', '6', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('999', '高樂高营养型固体饮料', '1020', '999', '7', '1501', '4', '10926', '217', '313', '316', '2', '1', '高樂高', '', '', '350克/瓶', 'Q/12XJ4615', '2010-10-12', 'YLGTF2012060011', '固态', '29', '17', '30', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1000', '明珠果果粒果汁饮料', '1021', '1000', '9', '1491', '4', '18578', '217', '374', '306', '3', '2', '百万宝贝', '', '', '500ml/瓶', 'Q/ZBS16', '2012.01.01.16:01:50A', 'YLGTJ2012070015', '液态', '2.5', '5', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1001', '桂圆莲子营养八宝粥', '1022', '1001', '10', '219', '2', '9567', '169', '268', '377', '5', '1', '娃哈哈', '', '', '360克/罐', 'QB/T2221', '20120108111247CD', 'GTQTE2012060001', '固体', '3.6', '62罐', '120罐', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1002', '菓珍欢畅柠檬味', '1023', '1002', '4', '1502', '4', '3', '145', '319', '289', '2', '1', '卡夫', '', '', '200克', 'Q/KAFUGT0009S', '20120127', 'YLGTA2012070017', '粉末', '7.80', '11', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1003', '美汁源果粒橙', '1024', '1003', '9', '1491', '4', '18578', '217', '255', '306', '3', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0001S', '2012020307:45CDP1', 'LYGSJ2012070016', '液态', '3.5', '10', '24', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1004', '菓珍', '1025', '1004', '7', '1502', '4', '10926', '141', '319', '316', '2', '1', '卡夫', '', '', '200克/包', 'Q/KTVGT0009S', '20120217', 'YLGTF2012060012', '固态', '8.5', '3', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1005', '瑞丽江芝果汁饮料', '1026', '1005', '9', '1491', '4', '18578', '217', '376', '306', '3', '2', '瑞丽江', '', '', '226ml/瓶', 'K/KRY01', '2012/04/1809:15', 'YLGSJ2012070017', '液态', '4', '7', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1006', '黑牛豆浆', '1027', '1006', '4', '1501', '4', '3', '145', '377', '289', '2', '1', '黑牛', '', '', '480克', 'GB/T18738', '20120106B', 'YLGTA2012070018', '粉末', '12.50', '19', '28', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1007', '康师傅白葡萄汁饮料', '1028', '1007', '9', '1491', '4', '18578', '217', '285', '306', '3', '2', '康师傅', '', '', '450ml/瓶', 'Q/14A0239S', '2011111205:46CB7', 'YLGSJ2012070018', '液态', '2.5', '20', '100', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1008', '雀巢咖啡', '1029', '1008', '7', '1502', '4', '10926', '217', '375', '316', '2', '1', 'NESCAFE', '', '', '100克/瓶', 'Q/QC001', '20100413', 'YLGTF2012060013', '固态', '45.50', '5', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1009', '桂圆莲子八宝粥罐头', '1030', '1009', '10', '219', '4', '9567', '169', '263', '377', '5', '1', '银鹭', '', '', '360克/罐', 'QB/T2221', '20120105XM', 'GTQTE2012060002', '固体', '3.6', '400', '60', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1010', '润田玉米露', '1031', '1010', '9', '1491', '4', '18578', '217', '378', '306', '3', '2', '润田', '', '', '450ml/瓶', 'Q/ALL04', '2012/01/0216:52KLZ', 'YLGSJ2012070019', '液态', '6', '12', '60', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1011', '雀巢咖啡', '1032', '1011', '7', '1502', '4', '10926', '179', '375', '316', '2', '1', 'NESCAFE', '', '', '130克/盒', 'Q/QC0004S', '20111129', 'YLGTF2012060014', '固态', '185.00', '3', '12', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1012', '农夫果园30%混合果蔬', '1033', '1012', '9', '1491', '4', '18578', '217', '261', '306', '3', '2', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '201205052301G6', 'YLGSJ2012070020', '液态', '4', '20', '100', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1013', '木糖醇营养八宝粥', '1034', '1013', '10', '219', '4', '9567', '169', '265', '377', '5', '1', '娃哈哈', '', '', '360克/罐', 'Q/WHJ0881', '20110716', 'GTQTE2012060003', '固体', '3.6', '30', '60', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1014', 'C+橙味', '1035', '1014', '4', '1502', '4', '3', '145', '325', '289', '2', '1', '雀巢果维', '', '', '500克', 'Q/OAEF7', '20111223', 'YLGTA2012070019', '粉末', '15.90', '181', '240', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1015', '雀巢咖啡醇品', '1036', '1015', '4', '1502', '4', '3', '217', '375', '289', '2', '1', '雀巢咖啡', '', '', '50克', 'Q/QC0001S', '20110910', 'YLGTA2012070020', '粉末', '22.50', '35', '42', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1016', '低糖桂圆八宝粥罐头', '1037', '1016', '10', '219', '4', '9567', '169', '288', '377', '5', '1', '达利园', '', '', '360克/罐', 'QB/T2221', '20120204', 'GTQTE2012060004', '粘稠固体', '3.3', '20', '30', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1017', '金酸角', '1038', '1017', '8', '1491', '4', '7363', '217', '275', '307', '3', '2', '澳地澳', '', '', '600ml/瓶', 'Q/KAD0001S-2010', '2011/11/02', 'YLGSD2012070010', '液态', '4', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1018', '银耳莲子八宝粥罐头', '1039', '1018', '10', '219', '4', '9567', '169', '288', '377', '5', '1', '达利园', '', '', '360g/罐', 'QB/T2221', '20120421', 'GTQTE2012060005', '粘稠固体', '3.3', '35', '60', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1019', '菓珍', '1040', '1019', '7', '1502', '4', '10926', '145', '319', '316', '2', '1', '卡夫', '', '', '250克/袋', 'Q/KAFVGT9', '2011/12月02日', 'YLGTF2012060015', '固态', '10.5', '5', '15', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1020', '安琪儿菊花晶', '1041', '1020', '7', '1502', '4', '10926', '145', '364', '316', '2', '1', 'XLX', '', '', '400克/袋', 'Q/JLX0060004', '2012/03/26', 'YLGTF2012060016', '固态', '10.80', '2', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1021', '啤酒（勇闯天涯）', '1042', '1021', '5', '1292', '4', '3052', '252', '379', '349', '2', '1', '雪花', '优级', '', '330ml/听', 'GB4927', '20120318', 'JYPJC2012060001', '液体', '4.0元', '20听', '24听', '6949352200017', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1022', '菊花晶', '1043', '1022', '7', '1502', '4', '10926', '145', '364', '316', '2', '1', 'XLX', '', '', '400克/袋', 'Q/JLX0060009', '2012/02/05', 'YLGTF2012060017', '固态', '9.5', '6', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1023', '低糖莲子八宝粥罐头', '1044', '1023', '10', '219', '4', '9567', '169', '263', '377', '7', '1', '银鹭', '', '', '360g/罐', 'Q/WXYL 0011S', '20120216XM', 'GTQTE2012060006', '粘稠固体', '3.50', '16', '80', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1024', '优乐美奶茶', '1045', '1024', '7', '1502', '4', '10926', '179', '315', '316', '2', '1', '喜之郎', '', '', '80克/盒', 'Q/XZL0002S', '20120411', 'YLGTF2012060018', '固态', '2.7', '4', '10', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1025', '菓珍', '1046', '1025', '7', '1502', '4', '10926', '145', '319', '316', '2', '1', '卡夫', '', '', '400克/袋', 'Q/KAFVGT9', '20111207', 'YLGTF2012060019', '固态', '15.00', '13', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1026', '桂圆八宝粥罐头', '1047', '1026', '10', '219', '4', '9567', '169', '263', '377', '7', '1', '银鹭', '', '', '360g/罐', 'QB/T2221', '20120201XM', 'GTQTE2012060007', '粘稠固体', '3.5', '16', '80', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1027', '麦斯威尔特浓咖啡', '1048', '1027', '7', '1502', '4', '10926', '179', '319', '316', '2', '1', '卡夫', '', '', '130克/盒', 'Q/KAFVGT2', '20111113', 'YLGTF2012060020', '固态', '25.00', '4', '0', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1028', '莲子玉米粥罐头', '1049', '1028', '10', '219', '4', '9567', '169', '263', '377', '7', '1', '银鹭', '', '', '280g/罐', 'Q/XMYL0006S', '20120201XM', 'GTQTE2012060008', '粘稠固体', '3.5', '16', '80', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1029', '绿色情怡雪糕', '1050', '1029', '8', '1533', '4', '7363', '239', '266', '428', '5', '2', '伊利', '组合型', '', '71克/支', 'Q/NYLC0007S', '20120213V', 'LYQTD2012070001', '固体', '1.5', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1030', '牛奶蜜豆雪糕', '1051', '1030', '8', '1533', '4', '7363', '239', '266', '428', '5', '2', '伊利', '组合型', '', '75克/支', 'SB/T10015', '20111216V', 'LYQTD2012070002', '固体', '1.5', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1031', '玉米香雪糕', '1052', '1031', '8', '1534', '4', '7363', '145', '266', '428', '7', '3', '伊利', '组合型', '', '65克/袋', 'SB/T10015', '20120319V', 'LYQTD2012070003', '固体', '1.2', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1032', '冰工厂棒冰', '1053', '1032', '8', '1534', '4', '7363', '145', '266', '430', '7', '3', '伊利', '清型', '', '23克*4支/袋', 'SB/T10016', '20120521V', 'LYQTD2012070004', '固体', '1.6', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1033', '冰工厂冰片蜜桃棒冰', '1054', '1033', '8', '1534', '4', '7363', '145', '266', '432', '7', '3', '伊利', '组合型', '', '80克/袋', 'SB/T10016', '20120508V', 'LYQTD2012070005', '固体', '1.00', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1034', '妙趣牛奶味巧力棒雪糕', '1055', '1034', '8', '1532', '4', '7363', '145', '266', '428', '7', '3', '伊利', '组合型', '', '23克*3支/袋', 'SB/T10015', '20120217V', 'LYQTD2012070006', '固体', '1.6', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1035', '奶香绿豆雪糕', '1056', '1035', '8', '1532', '4', '7363', '145', '266', '428', '7', '3', '伊利', '组合型', '', '75克/袋', 'SB/T10015', '20120403V', 'LYQTD2012070007', '固体', '1.1', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1036', '红枣桂圆八宝粥罐头', '1057', '1036', '10', '219', '4', '9567', '169', '288', '377', '7', '1', '达利园', '', '', '360g/罐', 'QB/T2221', '20120101', 'GTQTE2012060009', '粘稠固体', '3.5', '16', '80', '6926892565080', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1037', '椰奶燕麦粥罐头', '1058', '1037', '10', '219', '4', '9567', '169', '263', '377', '7', '2', '银鹭', '', '', '280g/罐', 'Q/XMYL0006S', '20120214XM', 'GTQTE2012060010', '粘稠固体', '3.5', '16', '80', '6926892568089', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1038', '蓝色激情啤酒（冰纯）', '1059', '1038', '9', '1292', '4', '18578', '252', '458', '353', '2', '1', '蓝色激情', '', '', '320ml/瓶', 'GB4927-2008', '20120506', 'JYPJJ2012070001', '液体', '2.5元', '21听', '120听', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1039', '冰冰凉棒冰', '1060', '1039', '8', '1534', '4', '7363', '145', '266', '430', '7', '3', '伊利', '组合型', '', '80克/袋', 'SB/T10015', '20120525V', 'LYQTD2012070008', '固体', '0.75', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1040', '巧乐兹雪糕', '1061', '1040', '8', '1532', '2', '7363', '145', '266', '428', '7', '3', '伊利', '组合型', '', '75克/袋', 'SB/T10015', '20120214V', 'LYQTD2012070009', '', '1.6', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1041', '金威特爽啤酒', '1062', '1041', '5', '1292', '4', '3052', '217', '321', '349', '2', '1', '金威啤酒', '一级', '', '500ml/瓶', 'GB4927', '20120308', 'JYPJC2012060002', '液体', '3.0元', '54瓶', '54瓶', '6942481699066', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1042', '老冰棍棒冰', '1063', '1042', '8', '1534', '4', '7363', '145', '266', '430', '7', '3', '伊利', '组合型', '', '88克/袋', 'SB/T10015', '20120217V', 'LYQTD2012070010', '固体', '0.7', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1043', '雀巢果维黑加仑味', '1064', '1043', '10', '1502', '4', '9567', '145', '325', '321', '2', '1', '雀巢果维', '', '', '500克/袋', 'Q/OAEF7', '20120213', 'YLGTE2012060014', '粉末状固体', '16.5', '8', '24', '6917878018201', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1044', '青岛啤酒', '1065', '1044', '9', '1292', '4', '18578', '252', '311', '353', '2', '1', '青岛啤酒', '', '', '330ml', 'GB4927', '20120323', 'JYPJJ2012070002', '液体', '4元', '72听', '144听', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1045', '茅台啤酒', '1066', '1045', '5', '1292', '4', '3052', '217', '318', '349', '2', '1', '图形商标', '优级', '', '518ml/瓶', 'GB4927', '20120208', 'JYPJC2012060003', '液体', '6.8元', '0', '120瓶', '6916065700042', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1046', '金晶菊花晶', '1067', '1046', '10', '1502', '4', '9567', '145', '381', '321', '2', '1', '安亲', '', '', '380克/袋', '', '2012-1-2', 'YLGTE2012060015', '固体', '9.80', '8', '12', '6941889300475', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1047', '高原啤酒', '1068', '1047', '5', '1292', '4', '3052', '217', '318', '349', '2', '1', '高原', '优级', '', '518ml/瓶', 'GB4927', '20120503', 'JYPJC2012060004', '液体', '3.9元', '160瓶', '220瓶', '6916065900046', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1048', '双滤爽啤酒', '1069', '1048', '9', '1351', '4', '18578', '217', '382', '353', '2', '1', '漓泉', '', '', '525ml/瓶', 'GB4927', '20120422', 'JYPJJ2012070003', '液体', '3.5元', '37瓶', '45瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1049', '欢畅柠檬味速溶固体饮料', '1070', '1049', '10', '1502', '4', '9567', '141', '319', '321', '2', '1', '', '', '', '200g/包', 'Q/KAFUG79果味固体饮料', '20111106', 'YLGTE2012060016', '固体', '8.5', '10', '24', '6904724019734', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1050', '蓝贝啤酒（纯爽）', '1071', '1050', '5', '1292', '4', '3052', '252', '298', '349', '2', '1', '蓝贝啤酒', '优级', '', '500ml/听', 'GB4927', '20120417', 'JYPJC2012060005', '液体', '3.5元', '24听', '48听', '6925452501001', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1051', '妙趣菠萝味速溶固体饮料', '1072', '1051', '10', '1502', '4', '9567', '141', '319', '321', '2', '1', '', '合格', '', '400克/包', 'Q/KAFUG9果味固体饮料', '20111215', 'YLGTE2012060017', '固体', '17', '12', '24', '6904724022482', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1052', '啤酒', '1073', '1052', '5', '1292', '4', '3052', '217', '298', '349', '2', '1', '图形商标', '优级', '', '500ml/瓶', 'GB4927', '20120214', 'JYPJC2012060006', '液体', '3.5元/瓶', '7瓶', '24瓶', '6920807200059', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1053', '燕京啤酒', '1074', '1053', '9', '1351', '4', '18578', '217', '382', '353', '2', '1', '燕京', '', '', '595ml', 'GB4927', '20111230', 'JYPJJ2012070004', '液体', '4元', '19瓶', '75瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1054', '伊利牧场果酱层趣雪糕', '1075', '1054', '10', '1532', '4', '9567', '145', '266', '428', '5', '2', '伊利', '', '', '80g/袋', 'SB/T 10015(组合型）', '20120321', 'LYQTE2012060001', '', '1.6', '4', '10', '6907992820736', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1055', '燕京啤酒', '1076', '1055', '5', '1292', '4', '3052', '217', '382', '349', '2', '1', '燕京', '优级', '', '595ml/瓶', 'GB4927', '20120415', 'JYPJC2012060007', '液体', '4.0元', '132瓶', '240瓶', '6903102430086', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1056', '雀巢咖啡', '1077', '1056', '10', '1502', '4', '9567', '179', '355', '321', '2', '1', 'NESCAFE', '', '', '143g/盒', 'Q/QC 0004S', '20111107', 'YLGTE2012060018', '固体', '12.5', '10', '24', '6917898001982', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1057', '心多多雪糕', '1078', '1057', '9', '1532', '4', '18578', '145', '266', '428', '7', '3', '佰豆集', '', '', '75克/袋', 'SB/T10015（组合型）', '2012013110051241V', 'LYQTJ2012070001', '固态', '2.0', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1058', '蓝狮特制啤酒', '1079', '1058', '9', '1349', '4', '18578', '252', '298', '353', '2', '1', '蓝狮', '', '', '330ml/听', 'GB4927', 'H201220301', 'JYPJJ2012070005', '液体', '', '72', '16', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1059', '四个圈雪糕', '1080', '1059', '10', '1532', '4', '9567', '145', '266', '428', '7', '3', '伊利', '', '', '75克/袋', 'SB/T10015(组合型）', '20120507', 'LYQTE2012060002', '', '2', '7', '20', '6907992819396', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1060', '高原啤酒', '1081', '1060', '5', '1292', '4', '3052', '217', '318', '349', '2', '1', '高原', '优级', '', '518ml/瓶', 'GB4927', '20120314', 'JYPJC2012060008', '液体', '3.5元/瓶', '132瓶', '240瓶', '6916065900046', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1061', '红枣牛奶味雪糕', '1082', '1061', '9', '1532', '4', '18578', '145', '266', '428', '8', '2', '伊利', '', '', '70克/袋', 'SB/T10015（组合型）', '2012033010021141V', 'LYQTJ2012070002', '固体', '2.0', '80', '100', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1062', '绿色心情', '1083', '1062', '9', '1533', '4', '18578', '145', '383', '428', '7', '3', '景丽华', '', '', '60克/袋', 'SB/T10016-2008（清型）', '20120419', 'LYQTJ2012070003', '固体', '2.5', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1063', '燕京啤酒', '1084', '1063', '9', '1292', '4', '18578', '217', '382', '353', '2', '1', '燕京', '', '', '595ml/瓶', 'GB4927', '20120428A', 'JYPJJ2012070006', '液体', '5', '45', '54', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1064', '炫果冰', '1085', '1064', '9', '1534', '4', '18578', '145', '266', '428', '7', '3', '伊利', '', '', '80克/袋', 'SB/T10016', '2011072410031102V', 'LYQTJ2012070004', '固体', '3.0', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1065', '茅台啤酒', '1086', '1065', '5', '1292', '2', '3052', '217', '318', '349', '2', '1', '图形商标', '优级', '', '518ml/瓶', 'GB4927', '20120319', 'JYPJC2012060009', '液体', '6元', '20瓶', '120瓶', '6916065700042', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1066', '太妃口味巧克力脆皮雪糕', '1087', '1066', '9', '1532', '4', '18578', '179', '383', '428', '7', '3', '景丽华', '', '', '70克/盒', 'SB/T10015（组合型）', '20120210', 'LYQTJ2012070005', '固体', '5.0', '20', '36', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1067', '橙味固体饮料', '1088', '1067', '10', '1502', '4', '9567', '145', '325', '321', '2', '1', '雀巢果维', '', '', '500克/袋', 'Q/OAEF7', '20110605', 'YLGTE2012060019', '固体', '14.5', '10', '24', '6917878003337', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1068', '纯爽啤酒', '1089', '1068', '5', '1292', '4', '3052', '217', '298', '349', '2', '1', '蓝贝', '优级', '', '330ml/瓶', 'GB4927', '20120115', 'JYPJC2012060010', '液体', '2.5元', '6瓶', '24瓶', '6925452500752', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1069', '漓泉啤酒', '1090', '1069', '9', '1353', '4', '18578', '217', '382', '353', '2', '1', '漓泉', '', '', '595ml', 'GB4927', '20120412A', 'JYPJJ2012070007', '液体', '5', '45', '54', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1070', '草莓味雪糕', '1091', '1070', '9', '1532', '4', '18578', '237', '384', '428', '7', '3', '雀巢', '', '', '255克/桶', 'SB/T10015', '20120311.2V', 'LYQTJ2012070006', '固体', '12.0', '10', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1071', '鲜香蜜桃味速溶固体饮料', '1092', '1071', '10', '1502', '4', '9567', '141', '319', '321', '2', '1', '', '合格', '', '200克/包', 'Q/KAFUGT9（果味固体饮料）', '20111107', 'YLGTE2012060010', '固体', '8.5', '10', '24', '6904724020563', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1072', '随变旋顶杯', '1093', '1072', '10', '1532', '4', '9567', '179', '272', '433', '5', '2', '蒙牛', '', '', '120克', 'SB/T10015', '20120430', 'LYQTE2012060003', '', '3.2', '40', '120', '6923644272746', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1073', '漓泉啤酒', '1094', '1073', '9', '1354', '4', '18578', '217', '385', '353', '2', '1', '滨都', '一级', '', '600ml/瓶', 'GB4927', '20120302', 'JYPJJ2012070008', '液体', '5', '45', '54', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1074', '一口奶纯奶味冰淇淋', '1095', '1074', '9', '1532', '4', '18578', '179', '386', '427', '7', '3', '美怡乐', '', '', '20支*15g/支', 'SB/T10013（清型羊乳脂）', '201204122H', 'LYQTJ2012070007', '固体', '12.0', '10', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1075', '奶提子雪糕', '1096', '1075', '9', '1532', '4', '18578', '145', '266', '428', '7', '3', '伊利', '', '', '65克/袋', 'SB/T10015（组合型）', '2012050810052221V', 'LYQTJ2012070008', '固体', '2.0', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1076', '雪花啤酒', '1097', '1076', '9', '1353', '4', '18578', '217', '312', '353', '2', '1', '雪花', '优级', '', '580ml/瓶', 'GB4927', '20120506', 'JYPJJ2012070009', '液体', '5', '45', '54', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1077', '苦咖啡雪糕', '1098', '1077', '9', '1532', '4', '18578', '145', '266', '428', '7', '3', '伊利', '', '', '70克/袋', 'SB/T10015（组合型）', '2012030810061301V', 'LYQTJ2012070009', '固体', '3.0', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1078', '土家糟辣椒', '1099', '1078', '3', '253', '4', '3', '263', '387', '95', '2', '1', '億农', '', '', '200克/杯', 'DB52/457', '20120512', 'PZQTA2012060007', '', '', '20杯', '20杯', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1079', '小奶糕（牛奶味雪糕）', '1100', '1079', '10', '1532', '4', '9567', '145', '388', '428', '7', '3', '爱美琪', '', '', '40g', 'SB/T10015', '2012年5月8日', 'LYQTE2012060004', '', '1.4', '2', '6', '6950179720138', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1080', '绿色情怡雪糕', '1101', '1080', '9', '1532', '4', '18578', '145', '266', '428', '7', '3', '伊利', '', '', '71克/袋', 'Q/NYLC0007S（清型）', '2012042310082551V', 'LYQTJ2012070010', '固体', '1.5', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1081', '天车香辣酱', '1102', '1081', '3', '253', '4', '3', '217', '389', '95', '2', '1', '天车', '', '', '350克/瓶', 'Q/78666343-8.2', '20120530', 'PZQTA201206008', '', '', '10瓶', '12瓶', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1082', '雪花啤酒', '1103', '1082', '9', '1351', '4', '18578', '217', '312', '353', '2', '1', '雪花', '优级', '', '580ml/瓶', 'GB4927', '20120522', 'JYPJJ2012070010', '液体', '5', '40', '45', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1083', '啤酒', '1104', '1083', '10', '1292', '4', '9567', '217', '304', '353', '2', '1', '青岛啤酒', '优级', '', '500ml/瓶', 'GB4927', '20120208', 'JYPJE2012060001', '透明液体', '3.5', '24', '60', '6901035604826', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1084', '鲜糟辣椒', '1105', '1084', '3', '253', '4', '3', '217', '390', '95', '2', '1', '一万响', '', '', '480g/瓶', 'DB52/457-2004', '20111003', 'PZQTA2012060009', '', '', '10瓶', '20瓶', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1085', '纯脆草莓香草叶脆皮雪糕', '1106', '1085', '9', '1532', '4', '18578', '145', '272', '428', '7', '3', '蒙牛', '', '', '56克/袋', 'SB/T10015（组合型）', '201204252210710', 'LYQTJ2012070011', '固体', '1.5', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1086', '绿色情怡雪糕', '1107', '1086', '9', '1532', '4', '18578', '145', '266', '428', '7', '3', '伊利', '', '', '71克/袋', 'Q/NYLC0007S（清型）', '20120511V', 'LYQTJ2012070012', '固体', '1.5', '10', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1087', '奶香绿豆雪糕', '1108', '1087', '9', '1532', '4', '18578', '145', '266', '428', '7', '3', '佰豆集', '', '', '75克/袋', 'SB/T10015（组合型）', '20120329V', 'LYQTJ2012070013', '固体', '1.5', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1088', '玉米香雪糕', '1109', '1088', '9', '1532', '4', '18578', '145', '266', '428', '7', '3', '伊利', '', '', '65克/袋', 'SB/T10015（组合型）', '2012031410042281V', 'LYQTJ2012070014', '固体', '1.5', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1089', '啤酒', '1110', '1089', '10', '1292', '4', '9567', '217', '308', '353', '2', '1', '雪花', '优级', '', '580ml/瓶', 'GB4927(优级）', '20120211', 'JYPJE2012060002', '透明液体', '2.8', '24', '60', '6920252600282', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1090', '冰工厂', '1111', '1090', '9', '1534', '4', '18578', '145', '266', '428', '7', '3', '伊利', '', '', '80克/袋', 'SB/T10016', '20120411V', 'LYQTJ2012070015', '固体', '1.0', '20', '30', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1091', '郫县豆瓣', '1112', '1091', '3', '253', '4', '3', '217', '391', '95', '2', '1', '无意', '', '', '500克/瓶', 'Q/X2179666-9.2', '20120104', 'PZQTA2012060010', '', '', '15瓶', '24瓶', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1092', '香雪儿雪糕', '1113', '1092', '9', '1532', '4', '18578', '237', '266', '428', '7', '3', '伊利', '', '', '145克/桶', 'SB/T10015（组合型）', '20120426100110110V', 'LYQTJ2012070016', '固体', '4.5', '10', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1093', '纯生啤酒', '1114', '1093', '10', '1292', '4', '9567', '217', '392', '353', '2', '1', '億龍', '一级', '', '588ml/瓶', 'GB4927', '20120303', 'JYPJE2012060003', '透明液体', '3.5', '60', '100', '6941880800325', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1094', '巧克力芝麻脆皮香草味雪糕', '1115', '1094', '9', '1532', '4', '18578', '179', '393', '428', '7', '3', '雀巢', '', '', '84克/盒', 'SB/T10015（组合型）', '20120427Z2', 'LYQTJ2012070017', '固体', '8.0', '10', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1095', '奶砖雪糕', '1116', '1095', '9', '1532', '4', '18578', '179', '266', '428', '7', '3', '伊利', '', '', '75克/盒', 'SB/T10015（清型）', '2012010410022121V', 'LYQTJ2012070018', '固体', '4.0', '10', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1096', '巧克力味冰淇淋', '1117', '1096', '9', '1532', '4', '18578', '145', '393', '427', '7', '3', '雀巢', '', '', '65克/袋', 'SB/T10013', '20120317Z2', 'LYQTJ2012070019', '固体', '6.0', '10', '25', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1097', '冰工厂缤纷果园棒冰', '1118', '1097', '10', '1532', '4', '9567', '145', '266', '433', '1', '1', '伊利', '', '', '80克/袋', 'SB/T10016（组合型）', '20120418', 'LYQTE2012060005', '', '', '6', '10', '6907992819181', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1098', '金澳纯生风味啤酒', '1119', '1098', '10', '1292', '4', '9567', '217', '394', '353', '2', '1', '嘉港', '一级', '', '330ml/瓶', 'GB4927', '2012/04/18', 'JYPJE2012060004', '无色透明液体', '1.5', '120', '300', '6901035603348', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1099', '杏仁香草味冰淇淋', '1120', '1099', '9', '1532', '4', '18578', '179', '393', '427', '7', '3', '雀巢', '', '', '65克/盒', 'SB/T10013', '20120313Z2', 'LYQTJ2012070020', '固体', '6.0', '10', '20', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1100', '布丁雪糕', '1121', '1100', '10', '1532', '4', '9567', '145', '272', '428', '5', '5', '蒙牛', '', '', '45g/袋', 'SB/T10015', '20120531', 'LYQTE2012060006', '', '', '2', '5', '6923644275174', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1101', '啤酒', '1122', '1101', '10', '1292', '3', '9567', '217', '395', '353', '2', '1', '青岛啤酒', '', '', '330ml/瓶', 'Q/02QPJ028', '20111211', 'JYPJE20125060005', '无色透明液体', '2.9', '192', '300', '6901035603348', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1102', '小雪生雪糕', '1123', '1102', '5', '1532', '4', '3052', '145', '266', '428', '5', '2', '伊利', '', '', '65g/袋', 'SB/T10015', '20120429', 'LYQTC2012060001', '固体', '2元', '600袋', '800袋', '6907992816296', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1103', '绿色心情雪糕', '1124', '1103', '10', '1532', '4', '9567', '145', '329', '428', '5', '5', '蒙牛', '', '', '71克/袋', 'Q/NMRY 0044S', '20120528', 'LYQTE201206007', '', '', '5', '15', '6923644266257', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1104', '小布丁雪糕', '1125', '1104', '10', '1532', '4', '9567', '145', '369', '428', '7', '3', '伊利', '', '', '48克/袋', 'SB/T41148(清型）', '2012.5.28', 'LYQTE2012060008', '', '0.5', '60', '150', '6907992811611', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1105', '雪花啤酒', '1126', '1105', '10', '1317', '4', '9567', '217', '299', '353', '2', '1', '雪花', '一级', '', '330ml/瓶', 'GB4927(一级）', '2012/01/18', 'jypjE201206006', '无色透明液体', '3.7', '9', '10', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1106', '冰工厂', '1127', '1106', '5', '1534', '4', '3052', '239', '266', '430', '5', '2', '伊利', '', '', '75克/支', '伊利', '20120417', 'LYQTC2012060002', '固体', '1.5元', '600支', '800支', '6907992817422', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1107', '巧蔓菲蔓越莓咖啡口味', '1128', '1107', '10', '1532', '4', '9567', '145', '266', '428', '7', '3', '伊利', '', '', '75g/袋', 'SB/T10015（组合型）', '20120521', 'LYQTE2012060009', '', '2.5', '3', '6', '6907992820835', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1108', '重庆火锅底料', '1129', '1108', '5', '253', '4', '3052', '145', '396', '193', '2', '1', '周君记', '', '', '200g/袋', 'DB50/105-2006', '2012/02/16', 'PZQTC2012060001', '固态', '5.5元/袋', '7袋', '10袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1109', '雪糕', '1130', '1109', '5', '1532', '2', '3052', '145', '266', '428', '5', '2', '伊利', '', '', '80g/袋', 'SB/T10015', '20110822', 'LYQTC2012060003', '固体', '2.5元/袋', '600袋', '800袋', '6907992818375', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1110', '金崂超纯啤酒', '1131', '1110', '10', '1292', '4', '9567', '217', '306', '353', '2', '1', '', '一级', '', '345ml/瓶', 'GB 4927(一级）', '2012/04/21', 'jypjE20126007', '无色透明液体', '3.0', '3', '5', '6940624200247', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1111', '花椒', '1132', '1111', '3', '253', '2', '3', '193', '397', '193', '200', '200', '', '', '', '散装', '', '', 'PZQTA2012060011', '', '', '100斤', '300斤', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1112', '巧脆兹雪糕', '1133', '1112', '10', '1532', '4', '9567', '145', '272', '428', '7', '3', '蒙牛', '', '', '78克/袋', 'SB/T10015', '20120519', 'LYQTE2012060010', '', '2', '3', '12', '6923044274054', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1113', '老冰棍', '1134', '1113', '5', '1532', '4', '3052', '239', '272', '430', '5', '2', '蒙牛', '', '', '88克/支', 'SB/T10016', '20120511', 'LYQTC2012060004', '固体', '1元', '600支', '800支', '6923644268688', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1114', '花椒', '1135', '1114', '3', '253', '2', '3', '193', '398', '193', '200', '200', '', '', '', '散装', '', '', 'PZQTA2012060012', '', '', '200斤', '500斤', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1115', '蓝宝精制啤酒', '1136', '1115', '10', '1292', '4', '9567', '217', '306', '353', '2', '1', '艾尔', '一级', '', '350ml/瓶', 'GB4927（一级）', '2011-10-16', 'jypjE201260008', '无色透明液体', '3.0', '3', '5', '0000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1116', '冰+', '1137', '1116', '5', '1534', '4', '3052', '239', '272', '430', '5', '2', '蒙牛', '', '', '85g/支', 'SB/T10016', '20120229', 'LYQTC2012060005', '固体', '1元/支', '600支', '800支', '6923644275051', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1117', '花椒', '1138', '1117', '3', '253', '2', '3', '193', '399', '193', '200', '200', '', '', '', '散装', '', '', 'PZQTA2012060013', '', '', '', '', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1118', '花椒', '1139', '1118', '3', '253', '2', '3', '193', '400', '193', '200', '200', '', '', '', '散装', '', '', 'PZQTA2012060014', '', '', '30斤', '100斤', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1119', '大红花椒', '1140', '1119', '3', '253', '2', '3', '193', '401', '193', '200', '200', '', '', '', '散装', '', '', 'PZQTA2012060015', '', '', '300斤', '500斤', '', '', '3', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1120', '红枣牛奶味雪糕', '1141', '1120', '10', '1532', '4', '9567', '239', '266', '428', '6', '2', '伊利', '', '', '70克', 'SB/T10015', '20120508', 'LYQTE2012060011', '', '2.00', '15', '40', '6907992819150', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1121', '麻辣鱼调料', '1142', '1121', '5', '253', '4', '3052', '145', '402', '193', '2', '1', '毛哥', '', '', '180g/袋', 'Q/MG0005S', '2012/02/21', 'PZQTC2012060002', '半固体', '5元/袋', '8袋', '15袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1122', '奶香乐', '1143', '1122', '5', '1532', '4', '3052', '145', '282', '428', '5', '2', '雀巢', '', '', '63g/袋', 'SB/T10015', '20120411', 'LYQTC2012060006', '固体', '2.5元/支', '540支', '720支', '6918551810600', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1123', '花心筒', '1144', '1123', '5', '1532', '4', '3052', '239', '403', '427', '5', '2', '雀巢', '', '', '68g/支', 'Q/CYQCZ223', '20120327', 'LYQTC2012060007', '固体', '3.5元', '360支', '480支', '6918551810839', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1124', '巧乐兹雪糕', '1145', '1124', '10', '1532', '4', '9567', '239', '266', '428', '6', '2', '伊利', '', '', '75克/支', 'SB/T10015(组合型）', '20120428', 'LYQTE2012060012', '', '1.5', '20', '50', '6907992820835', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1125', '烧鸡公调料', '1146', '1125', '5', '253', '4', '3052', '145', '404', '193', '2', '2', '好人家', '', '', '160g/袋', 'Q/79783088-7.02', '2012/03/09', 'PZQTC2012060003', '半固态', '6元/袋', '5袋', '10袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1126', '纯生态啤酒', '1147', '1126', '10', '1292', '4', '9567', '217', '405', '353', '2', '1', '图标见瓶身', '一级', '', '580ml/瓶', 'GB4927-2008(一级）', '2012.04.19', 'jypjE201206009', '无色透明液体', '3.0', '36', '60', '6930098220315', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1127', '红汤火锅底料', '1148', '1127', '5', '253', '4', '3052', '145', '406', '193', '2', '1', '六六红', '', '', '150克/袋', 'Q/LLH0001S', '2012/01/05', 'PZQTC2012060004', '固态', '5.5元/袋', '3袋', '10袋', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1128', '雪花啤酒', '1149', '1128', '10', '1292', '2', '9567', '217', '308', '353', '2', '1', '雪花', '优级', '', '500ml/瓶', 'GB4927(优级）', '120221', 'JYPJE2012060010', '无色透明液体', '4', '5', '10', '00000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1129', '玉米香雪糕', '1150', '1129', '10', '1532', '4', '9567', '239', '266', '428', '6', '2', '伊利', '', '', '65克', 'SB/T10015', '20120511', 'LYQTE2012060013', '', '1.50', '25', '50', '6907992815909', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1130', '酸汤肉丸火锅底料', '1151', '1130', '5', '253', '4', '3052', '145', '407', '193', '2', '1', '刘胡子', '', '', '210克/袋', 'Q/ZYLHZ0001S', '2012年03月12日', 'PZQTC2012060005', '半固体', '5元/袋', '4袋', '10袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1131', '清爽蓝莓棒冰', '1152', '1131', '10', '1532', '4', '9567', '239', '266', '433', '6', '2', '伊利', '', '', '80克/支', 'SB/T10015（组合型）', '20120426', 'LYQTE2012060014', '', '1.5', '20', '50', '6907992818696', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1132', '苹果口味果冻冰棍', '1153', '1132', '5', '1534', '4', '3052', '239', '408', '430', '5', '2', '和路雪', '', '', '60g/支', 'SB/T10016', '20120412', 'LYQTC2012060008', '固体', '2元', '432支', '540支', '6909493800057', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1133', '可爱多', '1154', '1133', '5', '1532', '4', '3052', '239', '408', '427', '5', '2', '和路雪', '', '', '67克/支', '和路雪', '20120120', 'LYQTC2012060009', '固体', '3.5元', '120支', '192支', '6909493400103', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1134', '绿色情怡雪糕', '1155', '1134', '10', '1532', '4', '9567', '239', '266', '428', '6', '2', '伊利', '', '', '71克/支', 'Q/NYLC00075(清型）', '20120511', 'LYQTE2012060015', '', '1.50', '18', '50', '00000000000000', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1135', '绿色恋情', '1156', '1135', '5', '1533', '4', '3052', '239', '409', '430', '5', '2', '大桥道', '', '', '77克/支', 'SB/T10016', '20120503', 'LYQTC2012060010', '固体', '1.5元', '288支', '540支', '6925147001038', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1137', '绿色心情', '1158', '1137', '5', '1533', '4', '3052', '239', '272', '430', '5', '2', '蒙牛', '', '', '71克/支', 'Q/NMRY0044S', '20120323', 'LYQTC2012060011', '固体', '1.5元/支', '200支', '400支', '6923644266257', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1138', '牛奶蜜豆雪糕', '1159', '1138', '10', '1532', '4', '9567', '239', '266', '433', '6', '2', '佰豆集', '', '', '75克', 'SB/T10015', '20120504', 'LYQTE2012060017', '', '1.50', '13', '50', '6907992816036', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1139', '即溶咖啡（原味1+2）', '1160', '1139', '5', '1501', '4', '3052', '179', '375', '321', '1', '1', '雀巢咖啡', '', '', '624克/盒', 'Q/QC0004S', '20120106', 'YLGTC2012060001', '固态', '42元/盒', '0盒', '2盒', '6917878002446', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1140', '冰块', '1161', '1140', '5', '1534', '4', '3052', '145', '411', '432', '5', '2', '夏君樂', '', '', '90克/袋', 'SB/T10327', '120524', 'LYQTC2012060012', '固体', '0.5元', '400袋', '2000袋', '6940777000015', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1141', 'e.脆点雪糕', '1162', '1141', '10', '1534', '4', '9567', '239', '412', '433', '6', '2', '三代行', '', '', '65克/支', 'SB/T10015（组合型）', '20120519', 'LYQTE2012060018', '', '1.5', '20', '50', '6945512520421', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1142', '冰工厂冰片蜜桃棒冰', '1163', '1142', '10', '1534', '4', '9567', '239', '266', '433', '6', '2', '伊利', '', '', '80克', 'SB/T10016（组合型）', '20120425', 'LYQTE2012060019', '', '1.5', '20', '50', '6907992818696', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1143', '巧乐兹雪糕', '1164', '1143', '10', '1532', '4', '9567', '239', '266', '433', '6', '2', '伊利', '', '', '75克/支', 'SB/T10015(组合型）', '20120507', 'LYQTE2012060020', '', '1.5', '15', '50', '6907992819396', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1144', '奶茶（麦香乐）', '1165', '1144', '5', '1502', '4', '3052', '179', '315', '321', '6', '2', '优乐美', '', '', '80克/盒', 'Q/XZL0002S', '20111215', 'YLGTC2012060002', '固体', '3.0元', '21盒', '30盒', '6926475203156', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1145', '绿色之恋', '1166', '1145', '5', '1533', '2', '3052', '179', '413', '430', '2', '1', '龙旺', '', '', '75克/盒', 'SB/T10016', '2012.04.18', 'LYQTC2012060013', '固体', '1.5元', '108盒', '360盒', '6942663270328', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1146', '菓珍速溶固体饮料（阳光甜橙味）', '1167', '1146', '5', '1502', '4', '3052', '145', '319', '318', '2', '4', '卡夫', '', '', '400克/袋', 'Q/KAFUGT0009S', '20120328', 'YLGTC2012060003', '固态', '17.5元/袋', '6袋', '12袋', '6904724022406', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1148', '奶茶（麦香味）', '1169', '1148', '5', '1502', '4', '3052', '145', '315', '321', '2', '1', '优乐美', '', '', '22克/袋', 'Q/XZL0002S', '20120505', 'YLGTC2012060004', '固态', '1元/袋', '18袋', '30袋', '6926475204283', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1149', '即溶冲调奶茶', '1170', '1149', '5', '1502', '2', '3052', '179', '327', '321', '7', '2', '香飘飘', '', '', '64克/盒', 'Q/XPP0001S', '20120318', 'YLGTC2012060005', '固态', '3.9元/盒', '39盒', '60盒', '6938888888615', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1151', '奶茶', '1172', '1151', '5', '1502', '4', '3052', '179', '327', '321', '2', '4', '香飘飘', '', '', '80g/盒', 'Q/XPP0001S', '20120305', 'YLGTC2012060006', '固态', '3元/盒', '78盒', '168盒', '6938888888837', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1152', '冰加', '1173', '1152', '5', '1534', '4', '3052', '145', '414', '430', '5', '2', '', '', '', '65克/袋', 'SB/T10016', '20120508', 'LYQTC2012060014', '固体', '1元', '240袋', '400袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1153', '橙味固体饮料', '1174', '1153', '5', '1502', '4', '3052', '145', '325', '318', '2', '1', '雀巢果维', '', '', '400g/袋', 'Q/PAAV0010S', '20120308', 'YLGTC2012060007', '固态', '14.28元/袋', '15袋', '48袋', '6917878037578', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1154', '芒果', '1175', '1154', '5', '1534', '4', '3052', '145', '415', '430', '5', '2', '峒河', '', '', '100克/袋', 'SB/T10015', '2012年04月12', 'LYQTC2012060015', '固体', '1元', '280袋', '400袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1155', '桂园八宝粥', '1176', '1155', '5', '219', '4', '3052', '252', '416', '377', '7', '1', '银鹭', '', '', '360克/听', 'QB/T2221', '20120221', 'GTQTC2012060001', '半固体', '3.50元', '60厅', '120厅', '6926892527088', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1156', '柠檬味固体饮料', '1177', '1156', '5', '1502', '4', '3052', '145', '325', '318', '2', '1', '雀巢果维', '', '', '500g/袋', 'Q/OAEF7', '20120220', 'YLGTC2012060008', '固态', '14.28元/袋', '10袋', '24袋', '6917878037653', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1157', '红舌头', '1178', '1157', '5', '1534', '4', '3052', '145', '279', '430', '5', '2', '甜甜雪', '', '', '60克/袋', 'SB/T10016', '2012年3月13日', 'LYQTC2012060016', '固体', '1元', '200袋', '400袋', '6907720813146', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1158', '咖啡', '1179', '1158', '5', '1502', '4', '3052', '179', '355', '321', '2', '1', 'NESCAFE', '', '', '36g/盒', 'Q/QC0001S', '20120304', 'YLGTC2012060009', '固态', '14.5元/盒', '20盒', '48盒', '6917878008691', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1159', '浪漫星期天', '1180', '1159', '5', '1532', '2', '3052', '145', '417', '428', '5', '2', '美伦', '', '', '60克/袋', 'SB/T10015', '20120216', 'LYQTC2012060017', '固体', '1.5元', '40袋', '200袋', '6920612642693', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1160', '缤纷果园', '1181', '1160', '5', '1534', '4', '3052', '145', '418', '430', '5', '2', '好友', '', '', '68克/袋', 'SB/T10016', '2012年5月18日', 'LYQTC2012060018', '固体', '1.0元', '80袋', '200袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1161', '黑白两道', '1182', '1161', '5', '1534', '4', '3052', '145', '418', '430', '5', '3', '好友', '', '', '65克/袋', 'SB/T10016', '2012年5月15日', 'LYQTC2012060019', '固体', '1.0元', '40袋', '200袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1162', '娃哈哈桂园莲子', '1183', '1162', '5', '219', '4', '3052', '217', '419', '377', '7', '1', '娃哈哈', '', '', '360克/瓶', 'QB/T2221', '20120119', 'GTQTC2012060002', '半固体', '4元', '36瓶', '120瓶', '6902083880781', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1163', '功夫豆', '1184', '1163', '5', '1533', '4', '3052', '145', '420', '430', '5', '2', '祐康', '', '', '85g/袋', 'SB/T10015', '20120216', 'LYQTC2012060020', '固体', '2.0元', '40袋', '200袋', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1164', '红枣桂园八宝粥', '1185', '1164', '5', '219', '4', '3052', '217', '288', '377', '7', '1', '达利园', '', '', '360克/瓶', 'QB/T2221', '20120101', 'GTQTC2012060003', '半固体', '3.5元/瓶', '24瓶', '60瓶', '6911988013965', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1165', '咖啡伴侣', '1186', '1165', '5', '1501', '4', '3052', '217', '355', '321', '2', '1', '咖啡伴侣', '', '', '100g/瓶', 'Q/QC0002S', '20120303', 'YLGTC2012060010', '固态', '9元/瓶', '2瓶', '6瓶', '6903473021067', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1166', '桂园莲子八宝粥', '1187', '1166', '5', '219', '4', '3052', '217', '288', '377', '7', '1', '达利园', '', '', '360克/瓶', 'QB/T2221', '20120101', 'GTQTC2012060004', '半固体', '3.50元', '36瓶', '60瓶', '6911988011985', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1167', '好粥道八宝粥罐头', '1188', '1167', '5', '219', '4', '3052', '217', '263', '377', '7', '1', '银鹭', '', '', '280克/瓶', 'Q/XMYL0006S', '20120115', 'GTQTC201206005', '半固体', '4.5元', '12瓶', '60瓶', '6926892568081', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1168', '雀巢果维', '1189', '1168', '5', '1502', '4', '3052', '145', '325', '318', '2', '1', '雀巢果维', '', '', '500克/袋', 'Q/OAEF7', '20120215', 'YLGTC2012060011', '固态', '14.5元/袋', '10袋', '10袋', '6917878003337', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1169', '桂圆八宝粥', '1190', '1169', '5', '219', '4', '3052', '217', '263', '377', '7', '1', '银鹭', '', '', '360g/瓶', 'QB/T2221', '20111228', 'GTQTC2012060006', '半固体', '3050元', '20瓶', '50瓶', '6926892565080', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1170', '菓珍速溶固体饮料', '1191', '1170', '5', '1502', '4', '3052', '145', '319', '318', '2', '1', '卡夫', '', '', '400克/袋', 'Q/KAFUGT9', '20111216', 'YLGTC2012060012', '固态', '17.5元/袋', '8袋', '24袋', '6904724022444', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1171', '好粥道莲子玉米粥', '1192', '1171', '5', '219', '4', '3052', '217', '263', '377', '7', '1', '银鹭', '', '', '280g/瓶', 'Q/XMYL0006S', '20120108', 'GTQTC2012060007', '半固体', '3.80元', '20瓶', '50瓶', '6926892527088', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1172', '桂圆八宝粥', '1193', '1172', '5', '219', '4', '3052', '217', '268', '377', '7', '1', '娃哈哈', '', '', '630g/瓶', 'QB/T2221', '20120330', 'GTQTC2012060008', '半固体', '3.80元', '18瓶', '60瓶', '6902083880781', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1173', '鲜果粒爽滑桃肉', '1194', '1173', '11', '1491', '4', '14940', '217', '370', '306', '2', '1', '纯果乐', '', '', '420ml/瓶', 'Q/BSYL0018S', '20120320S', 'YLGSH2012070002', '液态', '4.0', '20', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1174', '莆田八宝粥', '1195', '1174', '5', '219', '4', '3052', '217', '421', '377', '7', '1', '真田', '', '', '360g/瓶', 'QB/T2221', '2012/03/13', 'GTQTC2012060009', '半固体', '3.5元', '17瓶', '24瓶', '6923284168058', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1175', '燕麦八宝粥', '1196', '1175', '5', '219', '4', '3052', '217', '333', '377', '11', '48', '康辉', '', '', '320g/瓶', 'Q/FJKH002', '2012/04/22', 'GTQTC2012060010', '半固体', '3元', '11瓶', '48瓶', '6905337013270', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1176', '苹果珍果汁饮料', '1197', '1176', '11', '1491', '4', '14940', '217', '422', '306', '2', '1', '益和源', '', '', '500ml/瓶', 'Q/JYY0013S', '2012-04-10', 'YLGSH2012070003', '液态', '4.0', '40', '50', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1177', '芒果+菠萝+番石榴+苹果+番茄混合果蔬汁饮料', '1198', '1177', '11', '1491', '4', '14940', '217', '261', '306', '2', '1', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20120305', 'YLGSH2012070004', '液态', '4.0', '40', '50', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1178', '冰糖雪梨', '1199', '1178', '11', '1491', '4', '14940', '217', '259', '306', '3', '2', '统一', '', '', '500ml/瓶', 'Q/TYK0001S', '20120407', 'YLGSH2012070005', '液态', '3.5', '12', '48', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1179', '银耳湘莲八宝粥罐头', '1200', '1179', '11', '219', '4', '14940', '169', '362', '377', '7', '1', '强人', '', '', '360克', 'QB/T2221', '20111209HL09', 'GTQTH2012070001', '混合', '4', '20', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1180', '中绿粗粮王红豆紫米粥罐头', '1201', '1180', '11', '219', '4', '14940', '252', '423', '377', '7', '1', '中绿', '', '', '380g', 'QB/T2221', '2012∕04∕14  19：22', 'GTQTH2012070002', '混合状', '4', '30', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1182', '桂圆莲子八宝粥', '1203', '1182', '11', '219', '4', '14940', '169', '292', '377', '7', '1', '银鹭', '', '', '360g', 'QB/T2221', '20120217 17:28', 'GTQTH2012070003', '混合状', '3.50', '24', '240', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1183', '橙汁饮品', '1204', '1183', '11', '1491', '4', '14940', '217', '297', '306', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'GB/T21731', '20120120', 'YLGSH2012070007', '液态', '3.0', '8', '15', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1184', '桂圆莲子营养八宝粥', '1205', '1184', '11', '219', '4', '14940', '169', '268', '377', '7', '1', '娃哈哈', '', '', '360克', 'QB/T2221', '20120406', 'GTQTH2012070004', '混合状', '3.00', '57', '480', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1185', '蜜桃汁饮料', '1206', '1185', '11', '1491', '4', '14940', '145', '365', '306', '3', '2', '果粒多', '', '', '350ml/袋', 'Q/NJFW0001S', 'Y20120401', 'YLGSH2012070008', '液态', '3.5', '6', '72', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1186', '桂圆莲子营养八宝粥', '1207', '1186', '11', '219', '4', '14940', '252', '268', '377', '7', '1', '娃哈哈', '', '', '360克/瓶', 'QB/T2221', '20120513 112201CD', 'GTQTH2012070005', '混合状', '3.5', '400*12', '500*12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1187', '鲜果粒饱满果肉', '1208', '1187', '11', '1491', '4', '14940', '217', '337', '306', '3', '2', '纯果乐', '', '', '420ml/瓶', 'GB/T21731', '20111203K1', 'YLGSH2012070009', '液态', '4.0', '8', '12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1188', '桂圆莲子营养八宝粥', '1209', '1188', '11', '219', '4', '14940', '169', '268', '377', '7', '1', '娃哈哈', '', '', '360克', 'QB/T2221', '20120311', 'GTQTH2012070006', '混合状', '3.5', '32', '72', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1189', '桂圆莲子', '1210', '1189', '11', '219', '4', '14940', '169', '263', '377', '7', '1', '银鹭', '', '', '360g', 'QB/T2221', '20120117 16:51', 'GTQTH2012070007', '混合状', '3.50', '36', '144', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1190', '无蔗糖新桂圆八宝粥罐头', '1211', '1190', '11', '219', '4', '14940', '252', '328', '377', '7', '1', '景威', '', '', '310g', 'Q/JWB0006S', '2012∕01∕02 20:21', 'GTQTH2012070008', '混合状', '3', '36', '120', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1191', '桂圆椰果八宝粥罐头', '1212', '1191', '11', '219', '4', '14940', '252', '263', '377', '7', '1', '银鹭', '', '', '360g', 'QB/T2221', '20120211', 'GTQTH2012070009', '混合状', '3', '49', '180', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1192', '桂圆莲子营养八宝粥', '1213', '1192', '11', '219', '4', '14940', '252', '268', '377', '7', '1', '娃哈哈', '', '', '360克', 'QB/T2221', '20120211', 'GTQTH2012070010', '混合状', '6.00', '3*12', '10*12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1193', '果粒橙', '1215', '1193', '11', '1491', '4', '14940', '217', '255', '306', '3', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0001S', '20120410', 'YLGSH2012070010', '液态', '3.5', '24', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1194', '营养果粒', '1216', '1194', '11', '1491', '4', '14940', '217', '122', '306', '3', '2', '娃哈哈', '', '', '450ml/瓶', 'GB/T21731', '20120329', 'YLGSH2012070011', '液态', '3.5', '24', '36', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1195', '鲜果粒饱满果肉', '1217', '1195', '11', '1491', '4', '14940', '217', '337', '306', '3', '2', '纯果乐', '', '', '420ml/瓶', 'GB/T21731', '20111122K1', 'YLGSH2012070012', '液态', '3.8', '24', '36', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1196', '菠萝果粒果汁饮料', '1218', '1196', '11', '1491', '4', '14940', '217', '374', '306', '3', '2', '百万宝贝', '', '', '500ml/瓶', 'Q/ZBS16', '2012.01.01', 'YLGSH2012070013', '液态', '2.8', '24', '36', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1197', '鲜果粒滑爽桃肉', '1219', '1197', '11', '1491', '4', '14940', '217', '370', '306', '3', '2', '纯果乐', '', '', '420ml/瓶', 'Q/BSYL0018S', '20120325S', 'YLGSH2012070014', '液态', '3.5', '20', '45', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1198', '鲜果粒饱满果肉', '1220', '1198', '11', '1491', '4', '14940', '217', '337', '306', '3', '2', '纯果乐', '', '', '420ml/瓶', 'GB/T21731', '20120114K1', 'YLGSH2012070015', '液态', '3.5', '12', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1199', '橙+胡萝卜+苹果+菠萝+猕猴桃混合果蔬汁饮料', '1221', '1199', '11', '1491', '4', '14940', '217', '277', '306', '3', '2', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20120226', 'YLGSH2012070016', '液态', '3.5', '21', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1200', '葡萄汁饮料', '1222', '1200', '11', '1491', '4', '14940', '217', '255', '306', '3', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0001S', '20120303', 'YLGSH2012070017', '液态', '3.5', '3', '12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1201', '统一鲜橙多（橙汁饮品）', '1223', '1201', '11', '1491', '4', '14940', '217', '259', '306', '4', '1', '统一', '', '', '450ml/瓶', 'GB/T21731', '20120507', 'YLGSH2012070019', '液态', '2.8', '2', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1202', '西瓜口味棒冰', '1224', '1202', '8', '1533', '4', '7363', '239', '329', '432', '1', '1', '蒙牛', '组合型', '', '85克/支', 'SB/T10016', '20120514', 'LYQTD2012070011', '固体', '1.5', '', '', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1203', '芒果菓珍', '1225', '1203', '5', '1502', '4', '3052', '145', '319', '318', '2', '1', '卡夫', '', '', '400g/袋', 'Q/KAFUGT9', '20111121', 'YLGTC2012060013', '固态', '17.5元/袋', '11袋', '24袋', '6904724022468', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1204', '巧脆兹雪糕', '1226', '1204', '8', '1532', '4', '7363', '145', '329', '428', '7', '3', '蒙牛', '组合型', '', '78克/袋', 'SB/T10015', '20120518', 'LYQTD2012070012', '固体', '1.5', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1205', '高乐高水果型营养固体饮料', '1227', '1205', '5', '1501', '4', '3052', '145', '313', '321', '2', '1', '高樂高', '', '', '200克/袋', 'Q/14A0131S', '2011-09-19', 'YLGTC2012060014', '固态', '17.5元/袋', '6袋', '12袋', '6923589200170', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1206', '随便问顶榛子巧克力口味雪糕', '1228', '1206', '8', '1532', '4', '7363', '145', '369', '428', '7', '3', '蒙牛', '——', '', '70克/袋', 'SB/T10015', '20120511', 'LYQTD2012070013', '固体', '1.7', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1207', '雀巢果维', '1229', '1207', '5', '1502', '4', '3052', '145', '325', '318', '2', '1', '雀巢果维', '', '', '500克/袋', 'Q/OAEF7', '20110726', 'YLGTC2012060015', '固态', '14.9元/袋', '10袋', '12袋', '6917878004709', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1208', '蓝莓酸奶口味棒冰', '1230', '1208', '8', '1532', '4', '7363', '145', '329', '430', '7', '3', '蒙牛', '组合型', '', '78克/袋', 'SB/T10016', '20120519', 'LYQTD2012070014', '固体', '0.69', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1209', '香芋味脆皮雪糕', '1231', '1209', '8', '1532', '4', '7363', '145', '329', '428', '7', '3', '蒙牛', '组合型', '', '75克/袋', 'SB/T10015', '20120512', 'LYQTD2012070015', '固体', '0.97', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1210', '珍珠奶茶', '1232', '1210', '5', '1502', '4', '3052', '179', '315', '321', '8', '2', '优乐美', '', '', '70g/盒', 'Q/XZL0002S', '20120316', 'YLGTC2012060016', '固态', '3.5元/盒', '28盒', '60盒', '6926475203712', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1211', '优乐美奶茶（麦香味）', '1233', '1211', '5', '1502', '4', '3052', '217', '315', '321', '8', '2', '优乐美', '', '', '80克/瓶', 'Q/XZL0002S', '20111225', 'YLGTC2012060017', '固态', '3元/盒', '10盒', '30盒', '6926475203156', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1212', '雀巢果维', '1234', '1212', '5', '1502', '4', '3052', '145', '325', '318', '2', '1', '雀巢果维', '', '', '500g/袋', 'Q/OAEF7', '20120225', 'YLGTC2012060018', '固态', '14.9元/袋', '5袋', '30袋', '6917878003337', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1213', '冰葡萄雪糕', '1235', '1213', '8', '1534', '4', '7363', '145', '329', '428', '7', '3', '蒙牛', '组合型', '', '75克/袋', 'SB/T10015', '20120513', 'LYQTD2012070016', '固体', '0.65', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1214', '蜂蜜冰糖雪梨', '1236', '1214', '5', '1491', '4', '3052', '217', '122', '307', '3', '1', '娃哈哈', '', '', '450ml/瓶', 'Q/WHJ0931S', '20120327', 'YLGSC2012060001', '液体', '4.00元', '', '15瓶', '6902083893170', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1215', '统一鲜橙多', '1237', '1215', '5', '1491', '4', '3052', '217', '259', '307', '3', '1', '统一', '', '', '450ml/瓶', 'GB/T21731', '20120422', 'YLGSC2012060002', '液体', '3.00元', '60瓶', '75瓶', '6925303721039', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1216', '草莓香味脆皮雪糕', '1238', '1216', '8', '1532', '4', '7363', '145', '329', '428', '7', '3', '蒙牛', '组合型', '', '56克/袋', 'SB/T10015', '20120520', 'LYQTD2012070017', '固体', '0.75', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1217', '橙汁饮品', '1239', '1217', '5', '1491', '4', '3052', '217', '187', '307', '3', '1', '娃哈哈', '', '', '500ml/瓶', 'GB/T21731', '20110922', 'YLGSC2012060003', '液体', '3.00元', '10瓶', '15瓶', '6902083883041', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1218', '白咖啡', '1240', '1218', '5', '1501', '4', '3052', '179', '325', '321', '9', '1', '雀巢咖啡', '', '', '145克/盒', 'Q/PAAV0001S', '20120416', 'YLGTC2012060019', '固态', '16.8元/盒', '18盒', '24盒', '6917878036564', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1219', '30%混合果蔬', '1241', '1219', '5', '1491', '4', '3052', '217', '261', '309', '2', '1', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20110921', 'YLGSC2012060004', '液体', '3.5元', '4瓶', '24瓶', '6921168532049', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1220', '芒果口味雪泥', '1242', '1220', '8', '1534', '2', '7363', '145', '329', '429', '7', '3', '蒙牛', '清型', '', '75克/袋', 'SB/T10014', '20120515', 'LYQTD2012070018', '固体', '1.15', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1221', '幽沫奶味茶', '1243', '1221', '5', '1502', '4', '3052', '179', '425', '321', '6', '2', '蜡笔小新', '', '', '80g/盒', 'Q/JLBX006(F)', '20111225', 'YLGTC2012060020', '固态', '3元/盒', '16盒', '50盒', '6921101210928', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1222', '鲜果粒（爽滑桃肉）', '1244', '1222', '5', '1491', '4', '3052', '217', '337', '307', '2', '1', 'Tropicana纯果乐', '', '', '420毫升/瓶', 'Q/BSYL0018S', '20120418', 'YLGSC2012060005', '液体', '3.8元', '32瓶', '48瓶', '6934024516107', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1223', '绿莎莎绿色心情雪糕', '1245', '1223', '8', '1533', '4', '7363', '145', '329', '428', '8', '2', '蒙牛', '清型', '', '71克/袋', 'Q/NMRY0044S', '20120520', 'LYQTD2012070019', '固体', '1.50', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1224', '果粒橙汁饮料', '1246', '1224', '5', '1491', '4', '3052', '217', '426', '307', '3', '2', '中沃', '', '', '2.5L/瓶', 'GB/T21731', '20110913', 'YLGSC2012060006', '液体', '8元', '7瓶', '24瓶', '6927573109005', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1225', '倾心芒果冰淇淋', '1247', '1225', '8', '1532', '4', '7363', '179', '329', '427', '8', '2', '蒙牛', '——', '', '60克/盒', 'SB/T10013', '20120507', 'LYQTD2012070020', '固体', '2.3', '', '', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1226', '西柚汁饮料', '1248', '1226', '5', '1491', '4', '3052', '217', '427', '307', '3', '2', '水溶C100', '', '', '445ml/瓶', 'Q/NFS0008S', '20120408', 'YLGSC2012060007', '液体', '3.5元', '19瓶', '48瓶', '6921168500970', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1227', '纯果乐', '1249', '1227', '5', '1491', '4', '3052', '217', '428', '307', '3', '2', 'Tropicana', '', '', '420毫升/瓶', 'Q/BSYL0018S', '20120418', 'YLGSC2012060008', '液体', '3元', '19瓶', '48瓶', '6934024516107', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1228', '橙粒苹果饮料', '1250', '1228', '5', '1491', '4', '3052', '217', '275', '307', '3', '2', '澳地澳', '', '', '600g/瓶', 'Q/KAD04-2009', '2012/02/07', 'YLGSC2012060009', '液体', '3.5元', '23瓶', '96瓶', '6945032921968', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1229', '雪花啤酒', '1251', '1229', '11', '1292', '4', '14940', '217', '379', '348', '2', '1', '雪花', '优级', '', '330ml/瓶', 'GB4927', '20100321H', 'JYPJH2012070001', '液态', '4.0', '30', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1230', '青岛啤酒', '1252', '1230', '11', '1292', '4', '14940', '217', '311', '348', '2', '1', '青岛啤酒', '', '', '330ml/瓶', 'GB4927', '20120108', 'JYPJH2012070002', '液态', '4.0', '30', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1231', '漓泉啤酒', '1253', '1231', '11', '1292', '4', '14940', '217', '382', '348', '2', '1', '漓泉', '', '', '330ml/瓶', 'GB4927', '20120428', 'JYPJH2012070003', '液态', '2.5', '270', '4800', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1232', '椰果粒饮料', '1254', '1232', '5', '1491', '4', '3052', '217', '371', '307', '3', '2', '', '', '', '500g/瓶', 'Q/ZBG0036S', '20120401', 'YLGSC2012060010', '液体', '3.5元', '30瓶', '45瓶', '6900157550851', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1233', '统一鲜橙多', '1255', '1233', '5', '1491', '4', '3052', '217', '259', '307', '3', '2', '统一', '', '', '450ml/瓶', 'GB/T21731', '20111213', 'YLGSC2012060011', '液体', '3.00元', '38瓶', '75瓶', '6925303721039', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1234', '扬百利', '1256', '1234', '5', '1491', '4', '3052', '217', '291', '307', '3', '2', '图形商标', '', '', '450ml/瓶', 'Q/ZJYM2', '2012/04/19', 'YLGSC2012060012', '液体', '4.00元', '40瓶', '45瓶', '6940870920012', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1235', '纯果乐', '1257', '1235', '5', '1491', '4', '3052', '217', '337', '307', '3', '2', '纯果乐', '', '', '420ml/瓶', 'GB/T21731', '20111112', 'YLGSC2012060013', '液体', '4.00元', '4瓶', '15瓶', '6934024515605', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1236', '杨梅果汁饮料', '1258', '1236', '5', '1491', '4', '3052', '217', '429', '307', '3', '1', '扬百利', '', '', '450ml/瓶', 'Q/YBL0005S', '2012/05/05', 'YLGSC2012060014', '液体', '4.0元', '7瓶', '15瓶', '6940870920012', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1237', '水晶葡萄饮料', '1259', '1237', '5', '1491', '4', '3052', '217', '430', '307', '3', '2', '汇源', '', '', '500ml/瓶', 'Q/MAAD0001S', '20120502', 'YLGSC2012060015', '液体', '3.00元', '13瓶', '15瓶', '6923555215597', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1238', '汇源纯果汁', '1260', '1238', '5', '1491', '4', '3052', '217', '372', '307', '1', '1', '汇源', '', '', '1升', 'Q/MAAD0001S', '20120102', 'YLGSC2012060016', '液体', '12.80元', '20瓶', '45瓶', '6923555210462', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1239', '农夫果园混合果蔬', '1261', '1239', '5', '1491', '4', '3052', '217', '277', '309', '3', '1', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20120103', 'YLGSC2012060017', '液体', '3.90元', '30瓶', '50瓶', '6921168532001', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1240', '农夫果园混合果蔬', '1262', '1240', '5', '1491', '4', '3052', '217', '277', '309', '1', '1', '农夫果园', '', '', '1.8L/瓶', 'Q/NFS0003S', '20110816', 'YLGSC2012060018', '液体', '8.90元', '12瓶', '18瓶', '', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1241', '混合果蔬', '1263', '1241', '5', '1491', '4', '3052', '217', '261', '309', '3', '1', '农夫果园', '', '', '500ml/瓶', 'Q/NFS0003S', '20110922', 'YLGSC2012060019', '液体', '3.9元', '20瓶', '36瓶', '6921168532025', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1242', '鲜橙多', '1264', '1242', '5', '1491', '4', '3052', '217', '259', '307', '1', '1', '统一', '', '', '2L/瓶', 'GB/T21731', '20120117', 'YLGSC2012060020', '液体', '7.90元', '27瓶', '60瓶', '6925303721244', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1243', '雀巢咖啡', '1265', '1243', '11', '1501', '4', '14940', '179', '355', '321', '2', '1', '雀巢咖啡', '', '', '143克', 'Q/QC0004S', '20120210', 'YLGTH2012070001', '粉末状', '16.5', '10', '20', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1244', '雀巢果维', '1266', '1244', '11', '1502', '4', '14940', '145', '431', '318', '2', '1', '雀巢果维', '', '', '125g', 'Q/OAEF7', '2012 02 26', 'YLGTH2012070002', '粉末状', '5.80', '20', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1245', '高乐高营养型固体饮料', '1267', '1245', '11', '1501', '4', '14940', '145', '313', '321', '2', '1', '高乐高', '', '', '200克', 'Q/12XJ4615', '2010-0 8-24', 'YLGTH2012070003', '粉末状', '15.50', '15', '20', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1246', '去渣豆浆', '1268', '1246', '11', '1501', '4', '14940', '145', '432', '319', '2', '1', '智仁', '', '', '218克', 'Q/GLZR1D', '2011∕06∕10_2R06', 'YLGTH2012070004', '粉末状', '10.00', '15', '50', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1247', '维他型豆奶粉', '1269', '1247', '11', '1501', '4', '14940', '145', '433', '319', '2', '1', '维维', '', '', '360克', 'GB/T18738', '201112265387A', 'YLGTH2012070005', '粉末状', '10.00', '30', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1248', '雀巢咖啡原味1+2', '1270', '1248', '11', '1501', '4', '14940', '179', '325', '321', '2', '1', '雀巢咖啡', '', '', '143克', 'Q/OAEF20', '20120218 12:18', 'YLGTH2012070006', '粉末状', '16.70', '8', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1249', '雀巢咖啡原味1+2', '1271', '1249', '11', '1501', '4', '14940', '179', '355', '321', '2', '1', '雀巢咖啡', '', '', '143克', 'Q/QC0004S', '20110128', 'YLGTH2012070007', '粉末状', '16.70', '12', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1250', '啤酒', '1272', '1250', '11', '1292', '4', '14940', '217', '312', '348', '2', '1', '雪花', '优级', '', '500ml/瓶', 'GB4927', '120413 16:41H', 'JYPJH2012070004', '液态', '3.5', '600', '600', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1251', '燕京鲜啤', '1273', '1251', '11', '1292', '4', '14940', '217', '382', '348', '2', '1', '燕京', '优级', '', '595ml/瓶', 'GB4927', '20120601 06:08G14A', 'JYPJH2012070005', '液态', '4.0', '2000', '2000', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1252', '雪花啤酒', '1274', '1252', '11', '1292', '4', '14940', '217', '312', '348', '2', '1', '雪花', '优级', '', '580ml/瓶', 'GB4927', '120423', 'JYPJH2012070006', '液态', '3.0', '300', '3000', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1253', '雪花啤酒', '1275', '1253', '11', '1292', '4', '14940', '217', '312', '348', '2', '1', '雪花', '优级', '', '580ml/瓶', 'GB4927', '120524 04:15H', 'JYPJH2012070007', '液态', '2.2', '18000', '18000', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1254', '雪花啤酒', '1276', '1254', '11', '1292', '4', '14940', '217', '312', '348', '2', '1', '雪花', '优级', '', '580ml/瓶', 'GB4927', '120426 18：39K', 'JYPJH2012070008', '液态', '2.2', '5400', '18000', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1255', '珠江啤酒', '1277', '1255', '11', '1292', '4', '14940', '217', '434', '348', '2', '1', '', '优级', '', '600ml/瓶', 'GB4927', '20120405 1832DHG', 'JYPJH2012070009', '液态', '3.0', '2', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1256', '燕京鲜啤', '1278', '1256', '11', '1292', '4', '14940', '217', '382', '348', '2', '1', '燕京', '优级', '', '595ml/瓶', 'GB4927', '20120510 16:28S24A', 'JYPJH2012070010', '液态', '4.0', '10', '120', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1257', '绿色好心情', '1279', '1257', '6', '1533', '4', '5321', '145', '272', '428', '7', '3', '蒙牛', '', '', '71g/袋', 'Q/NMRY0044S', '20120429263251W', 'LYQTG2012070001', '固体', '1.5', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1258', '雪糕', '1280', '1258', '6', '1532', '4', '5321', '145', '266', '428', '3', '1', '伊利', '', '', '80克/袋', 'SB/T10015', '2012050910062231V', 'LYQTG2012070002', '固体', '2.5', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1259', '花心筒', '1281', '1259', '6', '1532', '2', '5321', '145', '403', '428', '7', '3', '雀巢', '', '', '68克/袋', 'SB/T10015', '2012042921', 'LYQTG2012070003', '固体', '4.0', '1', '1', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1260', '蓝莓酸奶口味棒冰', '1282', '1260', '6', '1532', '2', '5321', '145', '272', '428', '7', '3', '蒙牛', '', '', '78g/袋', 'SB/T10016', '20120428132170171W', 'LYQTG2012070004', '固体', '1.5', '1', '1', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1261', '冰棒', '1283', '1261', '6', '1534', '2', '5321', '145', '314', '428', '7', '3', '伊利', '', '', '80克/袋', 'SB/T10015', '2012032910131491V', 'LYQTG2012070005', '固体', '1.5', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1262', '雪糕', '1284', '1262', '6', '1532', '2', '5321', '145', '272', '428', '7', '3', '蒙牛', '', '', '70g/袋', 'SB/T10015', '201202212311210', 'LYQTG2012070006', '固体', '3.0', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1263', '雀巢8次方', '1285', '1263', '6', '1532', '2', '5321', '145', '403', '428', '7', '3', '雀巢', '', '', '84克/袋', 'SB/T10015', '201202100Z1', 'LYQTG2012070007', '固体', '5.0', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1264', '雪糕', '1286', '1264', '6', '1532', '2', '5321', '145', '272', '232', '7', '3', '蒙牛', '', '', '56克/袋', 'SB/T10016', '20120517121091W', 'LYQTG2012070008', '固体', '1.5', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1265', '雪糕', '1287', '1265', '6', '1533', '2', '5321', '145', '272', '428', '7', '3', '蒙牛', '', '', '65克/袋', 'SB/T10015', '201201092321010', 'LYQTG2012070009', '固体', '2.5', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1266', '三色雪糕', '1288', '1266', '6', '1532', '2', '5321', '179', '272', '428', '7', '3', '蒙牛', '', '', '100克/盒', 'SB/T10015', '20120401111041W', 'LYQTG2012070010', '固体', '3.0', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1267', '冰工厂棒冰', '1289', '1267', '6', '1534', '2', '5321', '145', '266', '428', '7', '3', '伊利', '', '', '80g/袋', 'SB/T10016', '201204263209V', 'LYQTG2012070011', '固体', '1.5', '20', '85', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1268', '花生脆雪糕', '1290', '1268', '6', '1532', '2', '5321', '145', '408', '428', '7', '3', '和路雪', '', '', '74g/袋', 'SB/T10015-2008', 'T20120310', 'LYQTG2012070012', '固体', '5.0', '30', '50', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1269', '巧乐滋雪糕', '1291', '1269', '6', '1532', '2', '5321', '145', '266', '428', '7', '3', '伊利', '', '', '85克/袋', 'SB/T10015', '20120109V', 'LYQTG2012070013', '固体', '2.5', '25', '58', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1270', '老冰棍', '1292', '1270', '6', '1532', '2', '5321', '145', '272', '430', '7', '3', '蒙牛', '', '', '88g/袋', 'SB/T10016', '20120218162261W', 'LYQTG2012070014', '固态', '1.0', '20', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1271', '蒂兰圣雪冰淇林', '1293', '1271', '6', '1532', '4', '5321', '263', '272', '427', '7', '3', '蒙牛', '', '', '80克/杯', 'SB/T10013', '20120326211031E', 'LYQTG2012070015', '固态', '2.5', '28', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1272', '优牧之选牛奶雪糕', '1294', '1272', '6', '1532', '4', '5321', '263', '272', '428', '7', '3', '蒙牛', '', '', '70克/杯', 'SB/T10015', '20120310173310', 'LYQTG2012070016', '固态', '2.5', '23', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1273', '三色雪糕', '1295', '1273', '6', '1532', '4', '5321', '145', '272', '428', '7', '3', '蒙牛', '', '', '110克/袋', 'SB/T10015', '2012030111105W', 'LYQTG2012070017', '固态', '3.5', '35', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1274', '蒂兰圣雪冰淇林', '1296', '1274', '6', '1532', '4', '5321', '179', '272', '427', '7', '3', '蒙牛', '', '', '60克/盒', 'SB/T10013', '20120312E', 'LYQTG2012070018', '固态', '4.0', '52', '80', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1275', '红枣牛奶雪糕', '1297', '1275', '6', '1532', '4', '5321', '145', '266', '428', '7', '3', '伊利', '', '', '70克/袋', 'SB/T10015', '2012050710021121V', 'LYQTG2012070019', '固态', '2.0', '18', '50', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1276', '芒果味雪泥', '1298', '1276', '6', '1534', '4', '5321', '145', '272', '429', '7', '3', '蒙牛', '', '', '75克/袋', 'SB/T10014', '20120304222101W', 'LYQTG2012070020', '固态', '2.0', '28', '65', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1277', '营养麦片', '1299', '1277', '6', '1501', '4', '5321', '145', '433', '316', '2', '1', '维维', '', '', '560克/袋', 'QB/T2762', '2012052087A', 'YLGTG2012070003', '固态', '18.5', '10', '12', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1278', '燕麦片', '1300', '1278', '6', '1501', '4', '5321', '145', '435', '316', '2', '1', '黑牛', '', '', '680克/袋', 'Q/HNSP0003S', '20120201B', 'YLGTG2012070004', '固态', '17.5', '8', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1279', '雀巢咖啡', '1301', '1279', '6', '1502', '4', '5321', '179', '375', '316', '2', '1', '雀巢', '', '', '169克/盒', 'Q/QC0004S', '20110722DC', 'YLGTG2012070005', '固态', '14.5', '12', '20', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1280', '豆奶粉', '1302', '1280', '6', '1501', '4', '5321', '145', '369', '316', '2', '1', '伊利', '', '', '560克/袋', 'Q/NYLB0048S', '83183111G', 'YLGTG2012070006', '固态', '16.8', '50', '75', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1281', '高钙型豆奶粉', '1303', '1281', '6', '1501', '4', '5321', '145', '436', '316', '3', '1', '雅芙', '', '', '530克/袋', 'GB/T18738', '20120101C/09', 'YLGTG2012070007', '固态', '13.5', '60', '75', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1282', '橙味固体饮料', '1304', '1282', '6', '1502', '4', '5321', '145', '325', '316', '3', '1', '雀巢', '', '', '500克/袋', 'Q/OAEF7', '20120225A', 'YLGTG2012070008', '固态', '14.8', '60', '80', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1283', '豆浆晶', '1305', '1283', '6', '1501', '4', '5321', '145', '437', '316', '3', '1', '冰泉', '', '', '300克/袋', 'GB7101', '2012年02月03日', 'YLGTG2012070009', '固态', '10.8', '30', '45', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1284', '苹果味固体饮料', '1306', '1284', '6', '1502', '4', '5321', '145', '325', '316', '3', '1', '雀巢', '', '', '500克/袋', 'Q/OAEF7', '20111216E', 'YLGTG2012070010', '固态', '14.8', '70', '100', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1285', '奶茶', '1307', '1285', '6', '1502', '4', '5321', '217', '315', '316', '2', '1', '优乐美', '', '', '80克/瓶', 'Q/XZL0002S', '20111129阳', 'YLGTG2012070011', '固态', '3.0', '56', '100', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1286', '维他型豆奶粉', '1308', '1286', '6', '1501', '4', '5321', '141', '367', '316', '2', '1', '维维', '', '', '500g/包', 'GB/T18738', '201203267798E', 'YLGTG2012070012', '固态', '12.0', '50', '80', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1287', '雀巢咖啡', '1309', '1287', '6', '1502', '4', '5321', '179', '375', '316', '2', '1', '雀巢', '', '', '169克/盒', 'Q/QC0004S', '20110813KC', 'YLGTG2012070013', '固态', '15.0', '16', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1288', '豆奶粉', '1310', '1288', '6', '1501', '4', '5321', '145', '369', '316', '2', '1', '伊利', '', '', '560g/袋', 'Q/NYLB0048S', '2012032283183111G', 'YLGTG2012070014', '固态', '', '50', '80', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1289', '豆奶粉', '1311', '1289', '6', '1501', '4', '5321', '145', '377', '316', '2', '1', '黑牛', '', '', '508克/袋', 'GB/T18738', '2011.12.22-B', 'YLGTG2012070015', '固态', '11.0', '30', '50', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1290', '珍珠奶茶', '1312', '1290', '6', '1502', '3', '5321', '263', '327', '316', '2', '1', '香飘飘', '', '', '70克/杯', 'Q/XPP0001S', '20111213CD104', 'YLGTG2012070016', '固态', '3.5', '57', '90', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1291', '雀巢咖啡', '1313', '1291', '6', '1502', '3', '5321', '179', '325', '316', '2', '1', '雀巢', '', '', '143克/盒', 'Q/OAEF20', '20120106NS', 'YLGTG2012070017', '固态', '16.0', '10', '70', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1292', '珍珠奶茶草莓味', '1314', '1292', '6', '1502', '3', '5321', '179', '315', '316', '2', '1', '优乐美', '', '', '70克/盒', 'Q/XZL0002S', '2740417阳', 'YLGTG2012070018', '固态', '3.5', '83', '120', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1293', '雀巢咖啡特浓1+2', '1315', '1293', '6', '1502', '3', '5321', '179', '375', '316', '2', '1', '雀巢', '', '', '130克/盒', 'Q/QC0004S', '20111127GLBL', 'YLGTG2012070019', '固态', '16', '7', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1294', '雀巢果维C', '1316', '1294', '6', '1502', '3', '5321', '145', '325', '316', '2', '1', '雀巢', '', '', '125克/袋', 'Q/OAEF7', '20120212', 'YLGTG2012070020', '固态', '6.0', '13', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1295', '纯果乐果缤纷', '1317', '1295', '8', '1491', '4', '7363', '217', '438', '307', '3', '2', '百事', '', '', '450毫升/瓶', 'Q/BSYL0013S', '20120319CDH', 'YLGSD2012070011', '液态', '3.5', '15', '45', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1296', '蜂蜜金桔柠檬复合果汁饮料', '1318', '1296', '8', '1491', '4', '7363', '217', '276', '307', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'Q/WHJ0932', '20120328FB', 'YLGSD2012070012', '液态', '4.0', '8', '15', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1297', '蜂蜜冰糖雪梨梨汁饮料', '1319', '1297', '8', '1491', '4', '7363', '217', '276', '307', '3', '2', '娃哈哈', '', '', '500ml/瓶', 'Q/WHJ0931S', '20120402FB', 'YLGSD2012070013', '液态', '', '10瓶', '15瓶', '', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1299', '雪花啤酒', '1321', '1299', '6', '1292', '4', '5321', '169', '299', '348', '1', '1', '雪花', '一级', '', '330ml/罐', 'GB4927', '2012/02/28', 'JYPJG2012070001', '液态', '2.5', '13', '192', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1300', '燕麦片', '1322', '1300', '11', '1501', '4', '14940', '141', '439', '321', '2', '1', '西麦', '', '', '380克/包', 'NY/T 1510', '2012.4.27', 'YLGTH2012070008', '粉末状', '9.8', '8', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1301', '燕京鲜啤', '1323', '1301', '6', '1317', '4', '5321', '169', '382', '348', '1', '1', '燕京', '', '', '330ml/罐', 'GB4927', '20110923', 'JYPJG2012070002', '液态', '3.5', '72', '144', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1302', '圣罗黑啤', '1324', '1302', '6', '1292', '4', '5321', '169', '380', '348', '1', '1', '圣罗黑啤', '', '', '320ml/罐', 'GB4927-2008', '2011/12/27', 'JYPJG2012070003', '液态', '3.5', '13', '48', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1303', '经典巧恋果', '1325', '1303', '11', '1532', '4', '14940', '145', '266', '426', '7', '3', '伊利', '', '', '80克/袋', 'SB/T 10015(组合型)', '20120517', 'LYQTH2012070001', '固态', '2.5', '60', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1304', '茅台啤酒', '1326', '1304', '6', '1292', '4', '5321', '169', '318', '348', '1', '1', '红钻', '优级', '', '330ml/罐', 'GB4927', '2012/01/12', 'JYPJG2012070004', '液态', '4.0', '134', '144', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1305', '炭烧咖啡', '1327', '1305', '11', '1501', '4', '14940', '169', '440', '321', '2', '1', '南国', '', '', '450g/罐', 'Q/HNNG0001S', '2012∕03∕23K', 'YLGTH2012070009', '粉末状', '45.00', '5', '6', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1306', '蓝狮超干啤酒', '1328', '1306', '6', '1292', '4', '5321', '169', '303', '348', '1', '1', '蓝带', '优级', '', '500ml/瓶', 'GB4927', 'CD20111017', 'JYPJG2012070002', '液态', '3.5', '74', '144', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1307', '冰+芒果口味雪泥', '1329', '1307', '11', '1534', '4', '14940', '145', '272', '426', '7', '3', '蒙牛', '', '', '75克/袋', 'SB/T 10014', '20120418', 'LYQTH2012070002', '固态', '2.0', '60', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1308', '燕京精品鲜啤', '1330', '1308', '6', '1317', '4', '5321', '217', '382', '348', '1', '1', '燕京', '优级', '', '500ml/瓶', 'GB4927', '20120420', 'JYPJG2012070006', '液态', '5.0', '2', '3', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1309', '经典巧脆棒', '1331', '1309', '11', '1532', '4', '14940', '145', '266', '426', '7', '3', '伊利', '', '', '80克/袋', 'SB/T 10015(组合型)', '20120525', 'LYQTH2012070003', '固态', '2.5', '90', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1310', '果珍', '1332', '1310', '11', '1502', '4', '14940', '141', '441', '318', '2', '1', '卡夫', '', '', '400克/包', 'Q/KAFUGT9', '2011.12.13', 'YLGTH2012070010', '粉末状', '17', '18', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1311', '维他型豆奶粉', '1333', '1311', '8', '1501', '4', '7363', '141', '367', '319', '2', '1', '维维', '', '', '500克/袋', 'GB/T18738', '20120310 4098E', 'YLGTD2012070001', '固体', '13.5', '21', '30', '6904432800372', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1312', '雪花新动啤酒', '1334', '1312', '6', '1292', '4', '5321', '217', '308', '348', '2', '1', '雪花', '优级', '', '500ml/瓶', 'GB4927', '20120518', 'JYPJG2012070007', '液态', '5.0', '4', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1313', '纯果乐鲜果粒', '1335', '1313', '8', '1491', '4', '7363', '217', '370', '470', '3', '2', '百事', '', '', '420毫升/瓶', 'Q/BSYL0018S', '20120331S', 'YLGSD2012070014', '液态', '3.5', '31', '75', '6934024516107', '', '2', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1314', '心多多', '1336', '1314', '11', '1532', '4', '14940', '145', '266', '426', '7', '3', '佰豆集', '', '', '75克/袋', 'SB/T 10015(组合型)', '20120511', 'LYQTH2012070004', '固态', '2.0', '30', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1315', '咖啡伴侣', '1337', '1315', '11', '1501', '4', '14940', '179', '355', '321', '2', '1', '雀巢', '', '', '60克', 'Q/QC0002S', '20111211', 'YLGTH2012070011', '粉末状', '7.80', '20', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1316', '雪花新动啤酒', '1338', '1316', '6', '1292', '4', '5321', '217', '308', '348', '2', '1', '雪花', '优级', '', '500ml/瓶', 'GB4927', '20120517', 'JYPJG2012070008', '液态', '5.0', '4', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1317', '卡布奇诺咖啡', '1339', '1317', '8', '1501', '4', '7363', '179', '325', '323', '2', '1', '雀巢咖啡', '', '', '65克/盒', 'Q/PAAV0001S', '20111120', 'YLGTD2012070002', '固体', '15.00', '22', '30', '6917878017891', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1318', '巧克力味冰欺凌', '1340', '1318', '11', '1532', '4', '14940', '145', '403', '426', '7', '3', '雀巢', '', '', '65克/袋', 'SB/T 10013', '20120318Z1', 'LYQTH2012070005', '固态', '6.0', '42', '21', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1319', '蓝带啤酒', '1341', '1319', '6', '1292', '4', '5321', '217', '298', '348', '2', '1', '蓝带', '优级', '', '500ml/瓶', 'GB4927', 'H2012040820:15210', 'JYPJG2012070009', '液态', '6.0', '5', '6', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1320', '雪花啤酒', '1342', '1320', '6', '1292', '4', '5321', '217', '308', '348', '2', '1', '雪花', '优级', '', '580ml/瓶', 'GB4927', '12052219:56F', 'JYPJG2012070010', '液态', '2.5', '49', '50', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1321', '雀巢果维', '1343', '1321', '11', '1502', '4', '14940', '145', '325', '318', '2', '1', '雀巢果维', '', '', '125g', 'Q/OAEF7', '20120223', 'YLGTH2012070012', '粉末状', '5', '20', '50', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1322', '草原酸奶口味雪糕', '1344', '1322', '11', '1532', '4', '14940', '145', '272', '426', '7', '3', '蒙牛', '', '', '75克/袋', 'SB/T 10015', '20120511', 'LYQTH2012070006', '固态', '2.0', '40', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1323', '问顶核桃雪糕口味雪糕', '1345', '1323', '11', '1532', '4', '14940', '145', '272', '426', '7', '3', '蒙牛', '', '', '70克/袋', 'SB/T 10015', '20120120 161341C', 'LYQTH2012070007', '固态', '3.0', '60', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1324', '中老年黑芝麻糊', '1346', '1324', '8', '1501', '4', '7363', '145', '368', '321', '2', '1', '南方', '', '', '600克/袋', 'G/HWL0001S', '20120401 GX8113301', 'YLGTD2012070003', '固体', '21.8', '', '', '6901333110722', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1325', '冰芙三明治', '1347', '1325', '11', '1532', '4', '14940', '145', '266', '426', '7', '3', '伊利', '', '', '65克/袋', 'SB/T 10015(组合型)', '20120512', 'LYQTH2012070008', '固态', '2.5', '60', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1326', '雀巢咖啡', '1348', '1326', '11', '1501', '4', '14940', '179', '325', '321', '2', '1', '雀巢咖啡', '', '', '143克', 'Q/OAEF20', '20120104', 'YLGTH2012070013', '粉末状', '13.00', '20', '40', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1327', '纯果乐鲜果粒', '1349', '1327', '8', '1491', '4', '7363', '217', '442', '310', '3', '2', '百事', '', '', '420毫升/瓶', 'GB/T21731', '20120321K1', 'YLGSD2012070015', '液态', '3.5', '18', '30', '6934024515605', '', '1', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1328', '八宝粥', '1350', '1328', '6', '219', '4', '5321', '169', '421', '374', '7', '1', '真田', '', '', '360g/罐', 'QB/T2221', '2012/03/12 12:09C', 'GTQTG2012070001', '半固体', '3.8', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1329', '卡布基诺口味冰淇淋', '1351', '1329', '11', '1532', '4', '14940', '145', '408', '426', '7', '3', '梦龙', '', '', '64克/袋', 'SB/T 10013', 'T20120310', 'LYQTH2012070009', '固态', '6.0', '2', '21', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1330', '八宝粥', '1352', '1330', '6', '219', '4', '5321', '169', '362', '374', '7', '3', '强人', '', '', '360克/罐', 'QB/T2221', '20111218HL18', 'GTQTG2012070002', '半固体', '4.0', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1331', '妙趣牛奶味克力棒', '1353', '1331', '11', '1532', '4', '14940', '145', '266', '426', '7', '3', '伊利', '', '', '69克/袋', 'SB/T 10015(组合型)', '20120329', 'LYQTH2012070010', '固态', '2.0', '30', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1332', '八宝粥', '1354', '1332', '6', '219', '4', '5321', '169', '276', '374', '7', '3', '娃哈哈', '', '', '360克/罐', 'QB/T2221', '2011122935CG', 'GTQTG2012070003', '半固体', '4.8', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1333', '雀巢咖啡速溶咖啡', '1355', '1333', '11', '1502', '4', '14940', '252', '355', '321', '2', '1', '雀巢咖啡', '', '', '500克', 'Q/QC0001S', '20111112', 'YLGTH2012070014', '粉末状', '135', '0', '6', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1334', '豆奶粉儿童AD钙', '1356', '1334', '8', '1501', '4', '7363', '141', '377', '319', '2', '1', '黑牛', '合格', '', '508g/袋', 'GB/T18738', '2012.02.20-B', 'YLGTD2012070004', '固体', '13.5', '30', '30', '69336751044448', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1335', '八宝粥', '1357', '1335', '6', '219', '4', '5321', '169', '362', '374', '7', '1', '强人', '', '', '360克/罐', 'QB/T2221', '2012031313:38:07', 'GTQTG2012070004', '半固体', '4.5', '1', '2', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1336', '菠萝+青苹果口味果汁冰棍', '1358', '1336', '11', '1534', '4', '14940', '145', '408', '426', '7', '3', '和路雪', '', '', '71克/袋', 'SB/T 10016', 'T20120223', 'LYQTH2012070011', '固态', '3.0', '30', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1337', '木糖醇八宝粥908', '1359', '1337', '6', '219', '4', '5321', '169', '276', '374', '7', '1', '娃哈哈', '', '', '360克/罐', 'QB/T2221', '20110908571142CG', 'GTQTG2012070005', '半固体', '3.5', '105', '300', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1338', '果珍欢畅柠檬味速溶固体饮料', '1360', '1338', '11', '1502', '4', '14940', '141', '319', '318', '2', '1', '卡夫', '', '', '500克', 'Q/KAFUGT9果味固体饮料', '20110126G', 'YLGTH2012070015', '粉末状', '14.5', '8', '22', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1339', '柠檬味菓珍', '1361', '1339', '8', '1502', '4', '7363', '145', '319', '321', '2', '1', '卡夫', '合格', '', '400克/袋', 'Q/KAFUGT9', '20111209 01:40.132', 'YLGTD2012070005', '固体', '17.8', '16', '24', '6904724022420', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1340', '山药芡实粥', '1362', '1340', '6', '219', '4', '5321', '169', '276', '374', '7', '3', '娃哈哈', '', '', '360克/罐', 'QB/T2221', '20111219670947CG', 'GTQTG2012070006', '半固体', '3.5', '48', '240', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1341', '雀巢果维柠檬味', '1363', '1341', '11', '1502', '4', '14940', '141', '325', '318', '2', '1', '雀巢果维', '', '', '500克', 'Q/OAEF7', '20111013', 'YLGTH2012070016', '粉末状', '14.8', '9', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1342', '草莓+水蜜桃口味果汁冰棍', '1364', '1342', '11', '1534', '4', '14940', '145', '408', '426', '7', '3', '和路雪', '', '', '71克/袋', 'SB/T 10016', '20120214', 'LYQTH2012070012', '固态', '3.0', '30', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1343', '苹果味菓珍', '1365', '1343', '8', '1502', '4', '7363', '145', '319', '321', '2', '1', '卡夫', '合格', '', '400克/袋', 'Q/KAFUGT9', '20111225 03.03R2', 'YLGTD2012070006', '固体', '17.8', '19', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1344', '摩爵杏仁香草味冰淇淋', '1366', '1344', '11', '1532', '4', '14940', '145', '403', '426', '7', '3', '雀巢', '', '', '65克/袋', 'SB/T 10013', '20120313Z2', 'LYQTH2012070013', '固态', '6.0', '1', '1', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1345', '咖啡伴侣', '1367', '1345', '11', '1501', '4', '14940', '217', '355', '321', '2', '1', '雀巢', '', '', '200g', 'Q/QC0002S', '2012-0 1-0 7', 'YLGTH2012070017', '粉末状', '21.5', '3', '10', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1346', '纯果乐果缤纷蜜桃樱桃味', '1368', '1346', '8', '1491', '4', '7363', '217', '270', '310', '3', '2', '百事', '', '', '450毫升/瓶', 'Q/BSYL0013S', '20120402CD21', 'YLGSD2012070016', '液态', '3.5', '16', '30', '6934024515902', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1347', '花生脆雪糕', '1369', '1347', '11', '1532', '4', '14940', '145', '408', '426', '7', '3', '和路雪', '', '', '74克/袋', 'SB/T 10015-2008', 'T20120310', 'LYQTH2012070014', '固态', '4.0', '27', '27', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1348', '雀巢咖啡', '1370', '1348', '11', '1501', '4', '14940', '217', '375', '321', '2', '1', '雀巢咖啡', '', '', '143克', 'Q/QC0004S', '2012-0 3-10', 'YLGTH2012070018', '粉末状', '16.50', '6', '3', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1349', '桂圆椰果八宝粥', '1371', '1349', '6', '219', '4', '5321', '169', '416', '374', '7', '1', '银鹭', '', '', '360g/罐', 'QB/T2221', '20120222G106XG', 'GTQTG2012070007', '半固体', '4.0', '60', '120', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1350', '益生元高钙菊花晶', '1372', '1350', '8', '1502', '4', '7363', '217', '443', '321', '2', '1', '瑞博', '合格', '', '460g/瓶', 'Q/JRB0001S-2010', '2012/03/02', 'YLGTD2012070007', '固体', '13.5', '3', '10', '6940131000685', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1351', '炫果冰棒冰', '1373', '1351', '11', '1534', '4', '14940', '145', '266', '426', '7', '3', '伊利', '', '', '80克/袋', 'SB/T 10016(清型)', '20120429', 'LYQTH2012070015', '固态', '2.0', '1', '1', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1352', '好粥道', '1374', '1352', '6', '219', '4', '5321', '169', '416', '374', '7', '1', '银鹭', '', '', '280g/罐', 'Q/XMYL0006S', '2012031201:03XG', 'GTQTG2012070008', '半固体', '4.0', '60', '120', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1353', '雀巢咖啡醇品', '1375', '1353', '11', '1502', '4', '14940', '217', '375', '321', '2', '1', '雀巢咖啡', '', '', '100g', 'Q/QC0001S', '2012-0 3-22', 'YLGTH2012070019', '粉末状', '45.00', '6', '3', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1354', '雀巢果维固体饮料', '1376', '1354', '8', '1502', '4', '7363', '145', '325', '323', '2', '1', '雀巢果维', '合格', '', '500克/袋', 'Q/OAEF7', '20111208', 'YLGTD2012070008', '固体', '16.00', '18', '24', '6917878018201', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1355', '1+1=？两吃', '1377', '1355', '11', '1532', '4', '14940', '239', '444', '426', '7', '3', '天淇乐', '', '', '70克', 'SB/T 10015', '20120511', 'LYQTH2012070016', '固态', '0.5', '100', '110', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1356', '桂圆莲子八宝粥', '1378', '1356', '6', '219', '4', '5321', '169', '268', '374', '2', '1', '娃哈哈', '', '', '360克/罐', 'QB/T2221', '20111206111307CD', 'GTQTG2012070009', '半固体', '4.0', '8', '15', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1357', '雀巢咖啡醇品', '1379', '1357', '11', '1502', '4', '14940', '217', '375', '321', '2', '1', '雀巢咖啡', '', '', '200g', 'Q/QC0001S', '2012-0 2-17', 'YLGTH2012070020', '粉末状', '85.00', '3', '6', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1358', '冰+蓝莓', '1380', '1358', '11', '1534', '4', '14940', '239', '444', '426', '7', '3', '天淇乐', '', '', '70克', 'SB/T 10016(清型)', '20120520', 'LYQTH2012070017', '固态', '0.5', '200', '110', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1359', '八宝粥', '1381', '1359', '6', '219', '4', '5321', '169', '263', '374', '6', '1', '银鹭', '', '', '360克/罐', 'QB/T2221', '2011112418:57XM', 'GTQTG2012070010', '半固体', '4.0', '8', '15', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1360', '巧蔓菲蔓越莓咖啡口味', '1382', '1360', '11', '1532', '4', '14940', '145', '266', '426', '7', '3', '伊利', '', '', '75克/袋', 'SB/T 10015(组合型)', '20120303 10062301v', 'LYQTH2012070018', '固态', '', '60', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1361', '绿色好好心情', '1383', '1361', '11', '1532', '4', '14940', '239', '459', '426', '7', '3', '天淇乐', '', '', '50克', 'SB/T 10016(混合型)', '20120511', 'LYQTH2012070019', '固态', '0.5', '200', '110', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1362', '营养维他豆奶粉', '1384', '1362', '8', '1501', '4', '7363', '145', '445', '319', '2', '1', '金禾', '普通型', '', '500g/袋', 'GB/T18738', '2011-12-11GY', 'YLGTD2012070009', '固体', '15.8', '1件', '2件', '6923118623203', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1363', '香芋大板', '1385', '1363', '11', '1534', '4', '14940', '239', '459', '426', '7', '3', '天淇乐', '', '', '70克', 'SB/T 10016(清型)', '20120504', 'LYQTH2012070020', '固态', '0.5', '100', '90', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1364', '中老年加钙豆奶粉', '1386', '1364', '8', '1501', '4', '7363', '145', '445', '319', '2', '1', '金禾', '', '', '500克/袋', 'GB/T18738', '2012-11-21GY', 'YLGTD2012070010', '固体', '', '', '', '6923118623210', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1365', '芒果珍果汁饮料', '1387', '1365', '8', '1491', '4', '7363', '217', '422', '310', '3', '2', '益和源', '', '', '500ml/瓶', 'Q/JYH0015S', '20111208', 'YLGSD2012070017', '液态', '4.5', '2', '15', '6926452306092', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1366', '金桔柠檬', '1388', '1366', '6', '1491', '4', '5321', '217', '122', '306', '5', '2', '娃哈哈', '', '', '450ml/瓶', 'Q/WHL0932S', '20111202', 'YLGSG2012070001', '液态', '3.5', '15', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1367', '冰糖雪梨', '1389', '1367', '6', '1491', '4', '5321', '217', '122', '306', '5', '2', '娃哈哈', '', '', '450ml/瓶', 'Q/WHJ0921S', '20111113', 'YLGSG2012070002', '液态', '3.0', '12', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1368', '酸角汁果汁饮料', '1390', '1368', '8', '1491', '4', '7363', '217', '422', '310', '3', '2', '益和源', '', '', '500ml/瓶', 'Q/JYY0008S', '20120405', 'YLGSD2012070018', '液态', '4.5', '3', '15', '6926452306283', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1369', '纯果乐', '1391', '1369', '6', '1488', '4', '5321', '217', '270', '306', '5', '2', '纯果乐', '', '', '450毫升/瓶', 'Q/BSYL0013S', '20110819CD216750', 'YLGSG2012070003', '液态', '3.0', '15', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1370', '中老年豆奶粉', '1392', '1370', '8', '291', '4', '7363', '145', '377', '319', '2', '1', '黑牛', '', '', '528g/袋', 'GB/T18738', '20120227B', 'YLGTD2012070011', '固体', '13', '6包', '12包', '6933075104936', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1371', '统一鲜橙多', '1393', '1371', '6', '1488', '4', '5321', '217', '259', '306', '5', '2', '统一', '', '', '450ml/瓶', 'GB/T21731', '20111117KM1', 'YLGSG2012070004', '液态', '2.5', '13', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1372', '甜豆浆粉', '1394', '1372', '8', '1501', '4', '7363', '145', '295', '319', '2', '1', '维维', '', '', '500克/袋', 'GB/T18738', '20120320 0602H', 'YLGTD2012070012', '固体', '18', '8包', '15包', '6904432302210', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1373', '果粒多葡萄汁饮料', '1395', '1373', '8', '1491', '4', '7363', '217', '365', '310', '3', '2', '旺旺', '', '', '350ml/瓶', 'Q/NJFW0001S', 'Y20120209', 'YLGSD2012070019', '液态', '3.5', '1', '2', '6920658284284', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1374', '美汁源C粒柠檬', '1396', '1374', '6', '1491', '4', '5321', '217', '270', '59', '5', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0006S', '20120109CD21', 'YLGSG2012070005', '液态', '3.0', '14', '24', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1375', '雀巢咖啡', '1397', '1375', '8', '1502', '4', '7363', '169', '355', '323', '2', '1', '醇品', '', '', '500g/罐', 'Q/QC001', '201009009', 'YLGTD2012070013', '固体', '120', '8', '10', '6903473015103', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1376', '雪梨汁', '1398', '1376', '8', '1491', '4', '7363', '217', '422', '310', '3', '2', '益和源', '', '', '500ml/瓶', 'Q/JYH0001S', '20120305', 'YLGSD2012070020', '液态', '4.5', '6', '15', '6926452306085', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1377', '奶茶', '1399', '1377', '8', '1501', '4', '7363', '217', '327', '321', '2', '1', '香飘飘', '', '', '80g/瓶', 'Q/XPP0001S', '20120205CD', 'YLGTD2012070014', '固体', '3.5', '6', '15', '6938888888837', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1378', '猕猴桃果汁', '1400', '1378', '6', '1488', '4', '5321', '217', '446', '306', '2', '1', '汇源', '', '', '280ml/瓶', 'Q/SJSG0015', '20120412', 'YLGSG2012070006', '液态', '4.5', '150', '200', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1379', '相约奶茶', '1401', '1379', '8', '1501', '4', '7363', '169', '345', '321', '2', '1', '香约', '合格', '', '72克/罐', 'Q/DHD0009S', '2012022403', 'YLGTD2012070015', '固体', '3', '80', '58', '6926858908050', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1380', '菊花晶', '1402', '1380', '8', '1502', '4', '7363', '145', '381', '321', '2', '1', '安亲', '', '', '380g/袋', 'Q/JAP0003S-2011', '20110802', 'YLGTD2012070016', '固体', '10', '6', '5', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1381', '雀巢咖啡', '1403', '1381', '8', '1502', '4', '7363', '179', '375', '323', '2', '1', '雀巢', '合格', '', '360克/（20*1.8）盒', 'Q/QC0001S', '20111109AG', 'YLGTD2012070017', '固体', '13.9', '22', '40', '6917878008691', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1382', '速冻豆腐花', '1404', '1382', '8', '1501', '4', '7363', '141', '437', '321', '2', '1', '冰泉', '混合型', '', '192g/包', 'GB/T23782', '2011年11月17日M', 'YLGTD2012070018', '固体', '8', '47', '60', '6901432306583', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1383', '速溶豆浆晶', '1405', '1383', '8', '1501', '4', '7363', '141', '437', '321', '2', '1', '冰泉', '', '', '160g/袋', 'Q/BQSY01', '20111116', 'YLGTD2012070019', '固体', '6.5', '9', '60', '6901432737929', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1384', '速溶女人豆浆', '1406', '1384', '8', '1501', '4', '7363', '145', '437', '321', '2', '1', '冰泉', '合格', '', '246克/袋', 'Q/BQSY01', '2011年05月13日E13013', 'YLGTD2012070020', '固体', '13.8', '1', '10', '6901432533606', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1385', '原生蓝莓汁', '1407', '1385', '6', '1491', '4', '5321', '217', '447', '306', '2', '1', '雅点', '', '', '410ml/瓶', 'Q/JLN03', '20120405', 'YLGSG2012070007', '液态', '7.5', '180', '300', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1386', '玉米浆饮料', '1408', '1386', '6', '1491', '4', '5321', '217', '378', '306', '2', '1', '康乐滋', '', '', '450ml/瓶', 'Q/ALL04', '20120217', 'YLGSG2012070008', '液态', '4.5', '106', '198', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1387', '果粒果汁饮料', '1409', '1387', '6', '1488', '4', '5321', '217', '448', '306', '2', '1', '果王', '', '', '245ml/瓶', 'Q/WADB0005S', '2012/03/12', 'YLGSG2012070009', '液态', '5.5', '78', '150', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1388', '芒果汁饮料', '1410', '1388', '6', '1491', '4', '5321', '217', '335', '306', '2', '1', '瑞丽江', '', '', '226ml/瓶', 'Q/KRY01', '2012/01/03', 'YLGSG2012070010', '液态', '3.5', '100', '150', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1389', '苹果醋', '1411', '1389', '6', '1491', '4', '5321', '217', '260', '306', '2', '1', '绿杰', '', '', '260ml/瓶', 'Q/YLJ0002S-2010', '2012/01/01ZP', 'YLGSG2012070011', '液态', '5.0', '25', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1390', '猕猴桃果汁', '1412', '1390', '6', '1488', '4', '5321', '217', '446', '306', '3', '2', '六广河', '', '', '280ml/瓶', 'Q/SJSG00015', '2011年11月08日', 'YLGSG2012070012', '液态', '4.0', '30', '70', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1391', '果粒橙', '1413', '1391', '6', '1491', '4', '5321', '217', '255', '306', '3', '2', '美汁源', '', '', '420ml/瓶', 'Q/SBDG0001S', '20120202', 'YLGSG2012070013', '液态', '3.5', '2', '4', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1392', '果缤纷', '1414', '1392', '6', '1491', '4', '5321', '217', '270', '306', '3', '2', '纯果乐', '', '', '450ml/瓶', 'Q/BSYL0013S', '20111209CD6750', 'YLGSG2012070014', '液态', '3.5', '2', '4', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1393', '果粒橙', '1415', '1393', '6', '1491', '2', '5321', '217', '449', '306', '3', '2', '栗子园', '', '', '450ml/瓶', 'GB/T21731', '2012/04/27', 'YLGSG2012070014', '液态', '3.0', '2', '4', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1394', '果汁果乐', '1416', '1394', '6', '1488', '3', '5321', '217', '450', '306', '2', '1', '汇源', '', '', '500ml/瓶', 'Q/MAAD0002S', 'NC20120427', 'YLGSG2012070016', '液态', '4.0', '27', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1395', '苹果汁', '1417', '1395', '6', '1491', '3', '5321', '179', '451', '306', '2', '1', '汇源', '', '', '200ml/盒', 'Q/MAAD0001S', 'P20120406', 'YLGSG2012070017', '液态', '3.0', '18', '30', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1396', '喝得爽', '1418', '1396', '6', '1491', '3', '5321', '217', '374', '306', '5', '2', '百万宝贝', '', '', '1.8l/瓶', 'Q/ZBS16', '20120101', 'YLGSG2012070018', '液态', '7.5', '108', '150', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1397', 'C每粒果粒橙', '1419', '1397', '6', '1488', '3', '5321', '217', '264', '306', '2', '1', '汇源', '', '', '420ml/瓶', 'GB/T21731', 'Z20120410', 'YLGSG2012070019', '液态', '3.5', '14', '15', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1398', '纯果乐', '1420', '1398', '6', '1491', '3', '5321', '217', '370', '306', '5', '2', '鲜果粒', '', '', '420毫升/瓶', 'Q/BSYC0018S', '20120320S', 'YLGSG2012070020', '液态', '4.0', '16', '60', '', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1399', '鲜肉粽', '1421', '1399', '8', '1527', '4', '7363', '145', '271', '52', '8', '2', '宏興隆', '', '', '100克/袋', 'SB/T10377-2004', '2012/06/03', 'MMQTD2012060008', '固体', '4', '', '', '6933949021758', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1400', '雪花啤酒', '1422', '1400', '8', '1292', '4', '7363', '217', '308', '348', '2', '1', '雪花', '合格（优级）', '', '580ml/瓶', 'GB4927', '120322F', 'JYPJD2012070001', '液态', '3', '4件', '20件', '6920252600282', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1401', '栗子鲜肉粽', '1423', '1401', '8', '1529', '4', '7363', '145', '271', '52', '4', '1', '宏興隆', '', '', '320克/袋', 'SB/T10377-2004', '2012.05.24', 'MMQTD2012060009', '固体', '10.5', '', '', '6933949062768', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1402', '板栗鲜肉粽', '1424', '1402', '8', '1529', '4', '7363', '145', '452', '52', '3', '2', '五芳斋', '', '', '300克/袋', 'GB19855-2005', '2012年6月2日', 'MMQTD2012060010', '固体', '9.5', '', '', '6953476510112', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1403', '鲜肉粽', '1425', '1403', '8', '1527', '4', '7363', '145', '452', '52', '3', '2', '五芳斋', '', '', '300克/袋', 'GB19855-2005', '2012年5月5日', 'MMQTD2012060011', '固体', '4', '', '', '6953476510068', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1404', '蓝色经典啤酒', '1426', '1404', '8', '1292', '4', '7363', '217', '453', '353', '2', '1', '蓝色经典', '优级（合格）', '', '486ml/瓶', 'GB4927', '20120509B', 'JYPJD2012070002', '液态', '3', '8箱', '10箱', '6934067290040', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1405', '鲜肉粽', '1427', '1405', '8', '1527', '4', '7363', '145', '249', '52', '3', '2', '五芳斋', '', '', '280克/袋', 'SB/T10377', '2012/04/19B2', 'MMQTD2012060012', '固体', '7.8', '', '', '6907619663364', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1406', '迷你鲜肉粽', '1428', '1406', '8', '1527', '4', '7363', '145', '249', '52', '3', '2', '五芳斋', '', '', '200克/袋', 'SB/T10377', '2012/05/14B5', 'MMQTD2012060013', '固体', '12.5', '', '', '69076195474667', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1407', '冰鲜啤酒', '1429', '1407', '8', '1292', '4', '7363', '217', '453', '353', '2', '1', '七箭', '', '', '486ml/瓶', 'GB4927', '20120105', 'JYPJD2012070003', '液态', '2', '12瓶', '24瓶', '6934067290309', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1408', '经典猪肉粽', '1430', '1408', '8', '1527', '4', '7363', '145', '236', '52', '1', '1', '思念', '', '', '1kg/袋', 'SB/T10412', '2012051760S', 'MMQTD2012060014', '固体', '27.9', '', '', '6921665710094', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1409', '醇香花生粽', '1431', '1409', '8', '1528', '4', '7363', '145', '236', '52', '1', '1', '思念', '', '', '500g/袋', 'SB/T10412', '20120500360S', 'MMQTD2012060015', '固体', '16.5', '', '', '6921665709777', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1410', '超纯啤酒', '1432', '1410', '8', '1292', '4', '7363', '217', '454', '353', '2', '1', '老泉水', '', '', '330ml/瓶', 'GB4927', '2012/04/26C', 'JYPJD2012070004', '液态', '4.5', '240瓶', '240瓶', '6934598902191', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1411', '香润豆沙粽', '1433', '1411', '8', '1528', '4', '7363', '145', '236', '52', '1', '1', '思念', '', '', '500g/袋', 'SB/T10412', '2012050860S', 'MMQTD2012060016', '固体', '16.5', '', '', '6921665709777', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1412', '蛋黄鲜肉粽', '1434', '1412', '8', '1527', '4', '7363', '145', '249', '52', '3', '2', '五芳斋', '', '', '280克/袋', 'SB/T10377', 'SB/T10377', 'MMQTD2012060017', '固体', '17', '', '', '6907619663388', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1413', '燕京啤酒', '1435', '1413', '8', '1292', '4', '7363', '217', '382', '353', '2', '1', '燕京', '', '', '330ml/瓶', 'GB4927', '20120421A', 'JYPJD2012070005', '液态', '4.5', '1件', '5件', '6903102430161', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1414', '迷你豆沙粽', '1436', '1414', '8', '1528', '4', '7363', '145', '249', '52', '3', '2', '五芳斋', '', '', '200克/袋', 'SB/T10377', '2012/05/31B5', 'MMQTD2012060018', '固体', '8', '', '', '6907619547367', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1415', '细沙枣泥粽', '1437', '1415', '8', '1528', '4', '7363', '145', '249', '52', '3', '2', '五芳斋', '', '', '280克/袋', 'SB/T10377', '2012/05/28B2', 'MMQTD2012060019', '固体', '11', '', '', '6907619663371', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1416', '栗子鲜肉粽', '1438', '1416', '8', '1529', '4', '7363', '145', '249', '52', '3', '2', '五芳斋', '', '', '280克/袋', 'SB/T10377', '2012/05/23B6', 'MMQTD2012060020', '固体', '', '', '', '6907619663432', '', '4', '', '0', '');
INSERT INTO `t_batch_sample` VALUES ('1417', '青岛青牌酒', '1439', '1417', '8', '1292', '4', '7363', '217', '455', '353', '2', '1', '山珍', '', '', '600ml/瓶', 'GB4927', '2012/5/24', 'JYPJD2012070006', '液态', '1.8', '', '', '6932769800041', '', '4', '', '0', '');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `display_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` smallint(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '贵州省食品与药物管理局.贵阳食品与药物管理局', 'fda', '3');
INSERT INTO `user` VALUES ('5', '贵州省分析测试研究院.贵阳市分析测试研究院', 'testlab', '4');
INSERT INTO `user` VALUES ('38', '贵州分析测试院', '贵州分析测试院', '4');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PASSWORD` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `USER_NAME` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZATION_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REGISTER_DATE` datetime DEFAULT NULL,
  `LAST_LOGIN_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_NAME` (`USER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of users
-- ----------------------------

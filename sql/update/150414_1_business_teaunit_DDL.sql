/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : fsn2

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2015-04-14 10:10:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `business_teaunit`
-- ----------------------------
DROP TABLE IF EXISTS `business_teaunit`;
CREATE TABLE `business_teaunit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(1000) DEFAULT NULL COMMENT '企业名称',
  `type` varchar(500) DEFAULT NULL COMMENT '企业类型',
  `organization` bigint(20) DEFAULT NULL COMMENT '组织机构ID',
  `businessUnitid` bigint(20) DEFAULT NULL COMMENT '企业ID',
  `enterpriteDate` datetime DEFAULT NULL COMMENT '注册时间',
  `type_tab` int(11) DEFAULT NULL COMMENT '企业类型标记 0为茶企业',
  `about` varchar(10240) DEFAULT NULL COMMENT '企业简介',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of business_teaunit
-- ----------------------------
INSERT INTO `business_teaunit` VALUES ('1', '凤冈县娄山春茶叶专业合作社', '生产企业', '1759', '40214', '2014-12-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('2', '凤冈县灵蔓茶业农民专业合作社', '生产企业', '1858', '40466', '2015-01-19 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('3', '凤冈县田坝明雨茶厂', '生产企业', '1722', '40149', '2014-12-26 00:00:00', '0', '核定使用商品(第30类)');
INSERT INTO `business_teaunit` VALUES ('4', '凤岗县绿缘春茶场', '生产企业', '1698', '40114', '2014-12-24 00:00:00', '0', '土溪镇2012年度茶叶产业发展优秀企业');
INSERT INTO `business_teaunit` VALUES ('5', '安顺市秀璞茶业农民专业合作社', '生产企业', '1946', '40654', '2015-02-02 00:00:00', '0', '请修正');
INSERT INTO `business_teaunit` VALUES ('6', '安顺御茶村茶业有限责任公司', '生产企业', '1943', '40646', '2015-01-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('7', '安顺金坝茶叶加工有限公司', '生产企业', '1971', '40931', '2015-02-05 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('8', '岑巩县和协天然野生茶厂', '生产企业', '1754', '40206', '2014-12-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('9', '晴隆县吉祥茶业有限公司', '生产企业', '1171', '39528', '2014-11-10 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('10', '晴隆县清韵茶业有限公司', '生产企业', '1890', '40548', '2015-01-26 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('11', '水城县茶叶发展有限公司', '生产企业', '1720', '40146', '2014-12-26 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('12', '湄潭县八角山茶业有限公司', '生产企业', '1868', '40492', '2015-01-21 00:00:00', '0', '八角山茶业有限公司简介\n\n\n贵州湄潭县八角山茶业有限公司成立于2010年10月21日，属私营独资企业, 位于茶乡湄潭境内万亩茶海之边。湄潭湄江两岸山川秀丽, 生态良好, 是西南名茶之乡, 制茶历史悠久, 公司制茶厂有着得天独厚的自然条件, 公司是一家集茶叶种植、生产、加工、销售一体的茶叶专营公司，生产高中档名优茶系列产品。\n    公司系列产品严格按照国卫生标准、技术指标、传统工艺加工及革新的工艺流程，六大系列各具特色，內含物质丰富，清香雅致，浓郁甘爽，色泽绿润，汤色明亮，叶衣明净展。\n     贵州湄潭县八角山茶业有限公司管理制度健全，机构设罝合理，公司将不断加强内部管理，严格过程控制，树立品牌，生产出一流的让消费者放心的产品。');
INSERT INTO `business_teaunit` VALUES ('13', '湄潭县怡和茶业有限责任公司', '生产企业', '1960', '40693', '2015-02-03 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('14', '湄潭县春江茶业有限公司', '生产企业', '1955', '40673', '2015-02-03 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('15', '湄潭县清水江茶厂', '生产企业', '1953', '32205', '2015-02-02 00:00:00', '0', '湄潭县清水江茶厂主要经营：茶叶收购、生产、销售等产品。公司尊崇“踏实、拼搏、责任”的企业精神，并以诚信、共赢、开创经营理念，创造良好的企业环境，以全新的管理模式，完善的技术，周到的服务，卓越的品质为生存根本，我们始终坚持用户至上 用心服务于客户，坚持用自己的服务去打动客户。');
INSERT INTO `business_teaunit` VALUES ('16', '湄潭县随缘茶业有限公司', '生产企业', '1869', '40497', '2015-01-21 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('17', '湄潭黔北茗居茶业有限公司', '生产企业', '1966', '40708', '2015-02-04 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('18', '纳雍县云雾有机茶有限责任公司', '生产企业', '1602', '39970', '2014-12-15 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('19', '贵州万寿茶业有限责任公司', '生产企业', '1859', '40468', '2015-01-19 00:00:00', '0', '公司主要产品“万寿茗珠”牌湄潭翠芽”,毛峰,毛尖,遵义红茶,黑茶及伏砖。');
INSERT INTO `business_teaunit` VALUES ('20', '贵州凤冈县仙人岭锌硒有机茶业有限公司', '生产企业', '1468', '39758', '2014-12-04 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('21', '贵州凤冈县盘云茶业有限公司', '生产企业', '1876', '40516', '2015-01-21 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('22', '贵州和泰春茶叶科技有限公司', '生产企业', '124', '39421', '2014-10-14 00:00:00', '0', '贵州和泰春茶叶科技有限公司，位于贵州省万山特区高楼坪老山口，成立于2008年11月18日，注册资金为叁佰万元。是一家集茶叶种植、生产、深加工、销售为一体的农业加工类企业。公司现有总资产749万元。其中固定资产320万元，流动资产219万元，建有浓缩速溶茶加工生产线一条，速溶功夫茶生产车间一座，占地26640平方米，厂房4000平方米。公司现有职工60人，其中：技术人员17人，在技术人员中高级职称3人，中级职称10人。\n2010 年公司通过了质量管理体系(ISO9001)认证，获得全国工业产品生产许可证(QS)。2010年通过了食品安全管理体系（HACCP）认证，2013年1月开始进行有机食品生产。本公司管理规范，设备先进，技术力量雄厚。');
INSERT INTO `business_teaunit` VALUES ('23', '贵州四品君茶业有限公司', '生产企业', '1871', '38453', '2015-01-21 00:00:00', '0', '本公司坐落于中国贵州省遵义市,供应红茶、绿茶,欢迎惠顾！');
INSERT INTO `business_teaunit` VALUES ('24', '贵州天灵茶叶有限责任公司', '生产企业', '1773', '40263', '2015-01-05 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('25', '贵州安顺春来茶业有限公司', '生产企业', '1934', '40629', '2015-01-29 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('26', '贵州寸心草有机茶业有限公司', '生产企业', '1762', '40219', '2014-12-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('27', '贵州怡壶春生态茶业有限公司', '生产企业', '1792', '40322', '2015-01-07 00:00:00', '0', '贵州怡壶春生态茶业有限公司，成立于2008年4月，位于中国名茶之乡—贵州•湄潭，是农业产业化经营省级龙头企业和市级扶贫龙头企业。公司以生产、加工和销售名优绿茶、红茶为一体，采用公司+农户+基地的现代农业产业化经营模式，现有无公害有机生态茶园2700余亩，拥有国内领先的红茶自动发酵烘干生产线一条及全自动清洁化绿茶生产线一条，每天可生产红茶、绿茶（干茶）两万斤。\n\n“怡壶春”绿茶、“黔门红”红茶是公司主要品牌，其品质获得业界专家认可和消费者青睐，并在国内多个大中城市建立有相对固定的销售网络，部份产品已进入海外市场，为黔茶出山作出尝试性探索。\n\n贵州茶叶商务网（www.guizhoucha.com）及www.gzyhc.cn属于公司官方网站，一天上万次的点击率增加了公司的知名度和美誉度， “怡壶春•淘宝”官方旗舰店，买家的好评和日益增长交易量是消费者对公司产品认可和信赖。');
INSERT INTO `business_teaunit` VALUES ('28', '贵州海马宫茶业有限公司', '生产企业', '1695', '40106', '2014-12-23 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('29', '贵州湄江源茶业有限公司', '生产企业', '1503', '39812', '2014-12-06 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('30', '贵州湄潭兰馨茶业有限公司', '生产企业', '25', '381', null, '0', '贵州湄潭兰馨茶业有限公司位于贵州茶都湄潭，兰馨茶叶香醇，味道醇厚。');
INSERT INTO `business_teaunit` VALUES ('31', '贵州湄潭林圣茶业有限公司', '生产企业', '1634', '40014', '2014-12-16 00:00:00', '0', '公司位于中国名茶之乡——遵义、湄潭，326国道边回龙村柯家店子，交通便宜，信息发达，距县城3公里即到。公司是一家具有种植、生产、加工、销售为一体的民营化企业，下设生产部、销售部、策划部、科研部、财务部、行政部、真情回访部，聘请贵州省茶叶研究所有关专家为技术研发指导。以公司+农户+基地的模式进行科学管理，现有生态茶园2270亩。现代化清洁厂房3678平方米，全面通过QS认证、CQC质量体系认证，有机茶园转换认证1800亩，年生产能力可达686吨，名优茶年生产15吨，大宗茶年生产320吨，红茶年生产351吨。生产系列产品有：湄潭翠芽、遵义毛峰、遵义红茶、大众高绿等等。');
INSERT INTO `business_teaunit` VALUES ('32', '贵州湄潭沁园春茶业有限公司', '生产企业', '2079', '42307', '2015-04-08 00:00:00', '0', '贵州湄潭沁园春茶业有限公司座落在山水田园城市中国·西部茶乡·遵义湄潭的贵州湄潭沁园春茶业公司，创立于2006年，是一家集开发、生产、销售为一体的现代化企业。公司遵循经典人生，健康生活的经营理念。以滴水之恩，涌泉相报为企业为宗旨。践行自律、自信、自尊的精神内涵，诚信敬业，合作创新，致力于为社会奉献安全、健康的各种茶品。\n    公司以茶叶生产为基础。同时，正在开展茶产业的深度开发和茶业与旅游产业的整合经营。公司的核心茶叶基地在海拔1250米的云贵山上，古代贡茶就出于此。茶树在云雾缭绕、云蒸霞蔚之中生长，得天独厚的条件造就了沁园春茶独特的品质、丰富的营养含量和良好的保健作用。\n    贵州湄潭沁园春茶业公司拥有核心有机茶园基地5000多亩。带动农户发展茶叶基地3万亩。设在云贵山上的专业加工厂年生产能力达50万斤以上。产品得到QS认证，HACCP认证，ISO9001认证，有机认证。公司所生产的“沁园春”牌湄潭翠芽、遵义红茶、长相守、茗花仙子、白茶等系列产品精选云贵山上的优质嫩芽。经选叶杀青→ 理条整形→磨锅提香等12道工序精心制作而成，经久耐泡、香高持久、味醇甘厚，乃茶中上品，深受人们喜爱。');
INSERT INTO `business_teaunit` VALUES ('33', '贵州湄潭盛兴茶业有限公司', '生产企业', '1510', '38899', '2014-12-08 00:00:00', '0', '企业基本情况\n\n1、 基本情况\n 企业名称：贵州省湄潭县盛兴茶业有限公司\n 法人代表：潘勇辉\n 注册资本：500万元人民币\n 企业类型：有限责任公司（国有控股）\n 企业住所：湄潭县湄江镇金花村\n 经营范围：茶叶、种植、生产、加工、销售。食品加工、销售\n2、 企业概况\n    贵州湄潭盛兴茶业有限公司是专业从事以茶叶种植、生产、加工、销售为一体的民营企业，于2007年11月9日登记注册的工商营业执照。2012年9月25日贵州盘江投资控股（集团）有限公司参与控股，现为国有控股企业。公司占地13500平方米，公司已通过质量和食品安全体系及QS认证。2010年12月公司被认定为省级扶贫龙头企业；2011年12月公司被认定为国家级农业产业化经营重点龙头企业。\n      公司现有职工75人，有茶叶专业技术人员5人，有20余年制茶经验的高级技师6人，并多年来培训了一批茶园生产管理及制茶技能熟练青工。技术实力雄厚，人才济济，是公司长足发展和“遵义红”品牌建设的中坚力量。');
INSERT INTO `business_teaunit` VALUES ('34', '贵州琦福苑茶业有限公司', '生产企业', '1496', '39802', '2014-12-05 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('35', '贵州省丹寨县添香园硒锌茶厂', '生产企业', '35', '37413', null, '0', '');
INSERT INTO `business_teaunit` VALUES ('36', '贵州省余庆县凤香苑茶业有限责任公司', '生产企业', '2086', '42314', '2015-04-09 00:00:00', '0', '贵州省余庆县风香苑茶业有限责任公司，成立于2010年12月，注册资金1000万元，是集种植、研发、生产、销售于一体的综合型企业。公司自有茶叶种植面积2400余亩，合作带动茶叶生产面积5万余亩。公司拥有3个茶叶加工厂（绿茶、红茶和小叶苦丁茶各1个）和1个产品展示接待中心，年产绿茶300吨，红茶150吨，白茶20吨，苦丁茶50吨。公司坚持以“品质求生存，诚信求发展”的理念，信守“敬天爱人”、“厚物载德”的价值观，注重“科学化种植，标准化生产，品牌化运营” 的管理模式，传承“求真、合道、得趣、养生”的黔茶文化精神，致力打造“凇烟”、“凤香苑”黔茶新品牌和国际化市场格局，保证让国人品味到安全放心、健康有益的茶。公司2012年2月被评为“余庆县农业产业化经营县级龙头企业”，“余庆县青年创业就业示范基地”、年度茶叶产业发展工作“优秀企业”，并获全国质量诚信AAA级品牌企业的荣誉称号和“恒天杯”全国名优绿茶评比银奖。');
INSERT INTO `business_teaunit` VALUES ('37', '贵州省六枝特区天香茶业有限公司', '生产企业', '85', '38090', null, '0', '');
INSERT INTO `business_teaunit` VALUES ('38', '贵州省六枝特区茶叶开发公司', '生产企业', '77', '38115', null, '0', '');
INSERT INTO `business_teaunit` VALUES ('39', '贵州省凤冈县朝阳茶业有限公司', '生产企业', '1669', '40059', '2014-12-19 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('40', '贵州省凤冈县浪竹有机茶业有限公司', '生产企业', '1477', '39773', '2014-12-04 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('41', '贵州省大方县九洞天资源开发有限责任公司茶叶分公司', '生产企业', '1753', '40203', '2014-12-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('42', '贵州省安顺市国品黔茶老落坡茶叶农民专业合作社', '生产企业', '1873', '40506', '2015-01-21 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('43', '贵州省湄潭县一丫翠片茶业有限公司', '生产企业', '1796', '40335', '2015-01-08 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('44', '贵州省湄潭县制茶厂', '生产企业', '1562', '39904', '2014-12-11 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('45', '贵州省湄潭县栗香茶业有限公司', '生产企业', '1106', '39431', '2014-10-20 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('46', '贵州省湄潭县芸香茶业有限公司', '生产企业', '1864', '40484', '2015-01-20 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('47', '贵州省湄潭县西山茶业有限公司', '生产企业', '1701', '40120', '2014-12-24 00:00:00', '0', '贵州省湄潭县西山茶业有限公司（前身为2008年9月注册的贵州省湄潭县茶润生灵茶业专业合作社）是一家集茶叶种植（合作社）、加工、销售为一体的民营企业。');
INSERT INTO `business_teaunit` VALUES ('48', '贵州省湄潭县鑫辉茶业有限公司', '生产企业', '1816', '40365', '2015-01-09 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('49', '贵州省湄潭县黔茗茶业有限责任公司', '生产企业', '2014', '42030', '2015-03-10 00:00:00', '0', '贵州省湄潭县黔茗茶业有限责任公司，成立于2008年，是集茶叶生产、加工、销售、出口、弘扬茶文化为一体的综合型现代化企业。公司拥有总资产4800万元，无公害绿色生态茶园13000亩，厂区占地面积30440㎡，有现代化办公楼、红茶生产车间、绿茶生产车间、爱心包装车间、恒温冻库、员工沐浴室和员工食堂。公司拥有名优绿茶智能化生产线一条，具有绿茶分段速冷、茶叶光波杀青、自动加压揉捻及风选回软等多项先进技术，其年加工能力达1200吨。公司还拥有红茶数控生产线一条，具有红茶自动揉捻系统、数控发酵系统及数控模拟碳焙系统等先进设备，其年加工能力1000吨。工业总产值4500万元，公司通过了ISO9001:2008质量管理体系认证和HACCP质量安全管理体系认证，并取得了出口贸易经营权。依托贵州高原得天独厚的自然地理环境，利用自然禀赋的优良茶叶资源，黔茗茶业坚持传统工艺与创新技术相结合，目前获得5项专利技术同时公司开发了“黔潭玉翠”“黔茗红”“随意泡”系列产品，其中“黔茗红”功夫红茶远销全国25个省市，全国共有80余家实体专卖店，深受客户好评。');
INSERT INTO `business_teaunit` VALUES ('50', '贵州省茶叶科学研究所试验示范基地', '生产企业', '1865', '40486', '2015-01-20 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('51', '贵州省金沙县茶叶专业合作社', '生产企业', '1694', '40105', '2014-12-23 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('52', '贵州省雷山县毛克翕茶叶发展研究所', '生产企业', '104', '39267', '2014-09-25 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('53', '贵州省雷山县绿叶香茶业有限责任公司', '生产企业', '1645', '40030', '2014-12-17 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('54', '贵州省雷山县脚尧茶业有限公司', '生产企业', '1649', '40035', '2014-12-17 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('55', '贵州省雷山县苗家春茶业有限公司', '生产企业', '1646', '40032', '2014-12-17 00:00:00', '0', '贵州省雷山县苗家春茶业有限公司成立于2004年8月，注册资金200万元，是一个集茶叶基地种植、茶叶加工、销售、茶叶技术咨询、茶艺为一体的有限责任公司。公司厂房占地面积3992.3平方米，有先进的茶叶生产设备，质量检验设备齐全配套。公司自有优质茶叶基地1500亩，其中有机茶叶面积600亩；年生产干茶5吨，其中有机茶叶2.2吨；可实现产值350万元。公司立足雷山县得天独厚的资源优势，走机制名优茶创新路线，生产的主要产品有“雷公山银球茶”、“雷公山清明茶”、 “雷公山苗家白茶”、“ 雷公山玉针茶”、“ 雷公山玉叶茶”、“ 雷公山毛峰茶”等系列产品。\n    公司在“以质量求生存，以信誉求发展，不断进取、不断拼搏、做大做强茶产业”目标指引下，建立有一支产品研发、管理、加工、基地建设等较为完善的人才队伍，高级管理人员8人，高级技师4人，专业技术9人，季节性工人120多人，公司还聘请中国农业科学院茶叶研究所教授、专家2人、省茶科所2人作为公司发展和质量管理的专门顾问。得到各部门的认可，获得诸多的荣誉，有自己的生产商标，连续获得有机认证书，取得了良好的经济效益与社会效益。');
INSERT INTO `business_teaunit` VALUES ('56', '贵州省黎平县侗乡春茶业有限公司', '生产企业', '1688', '40098', '2014-12-23 00:00:00', '0', '贵州省黎平县侗乡春茶业有限公司，坐落于“全国重点产茶县”、“中国名茶之乡”黎平县境内，是一家集种植、加工、销售、茶文化交流为一体的综合企业。公司下设6个分公司，领办1个农民茶叶专业合作社，设有生产技术指导部、质检安全管理部、市场信息营销部、行政财务部，终端专营店面3个。公司现有茶园面积6226亩，其中：龙井43号1550亩，福鼎大白茶4676亩，建有标准化清洁生产车间4座，近4000平方米，并取得了国家食品安全QS认证。生产的“侗乡春”牌“雀舌茶”、“黎香绿茶”、“翠针茶”以优质鲜叶为原料，以其“色、香、味、形”俱佳，深受各方客户喜爱，产品远销山东、浙江、江苏、湖南、湖北、四川等地。近年来，经不断探索，并成功制作出各类优质红茶，其品质可与同类名茶媲美。公司秉承以“绿色生态”为宗旨、“食品安全”为命脉的理念，精心打造了“侗乡春”牌系列茶产品，先后五次荣获“中茶杯”、“中绿杯”金奖，2004年被农业部茶叶质量监督检验测试中心认证为“无公害放心茶”和中国茶科所“定点服务企业”， 2007年跻身贵州省“农业产业化经营重点龙头企业”，2010年跻身中华全国供销合作总社“农业产业化重点龙头企业”。');
INSERT INTO `business_teaunit` VALUES ('57', '贵州省黎平县森绿茶业对外贸易有限公司', '生产企业', '1824', '40380', '2015-01-09 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('58', '贵州省黎平雀舌茶业有限公司', '生产企业', '1674', '40070', '2014-12-20 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('59', '贵州紫日茶业科技有限公司', '生产企业', '1618', '39986', '2014-12-15 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('60', '贵州贵福春茶业有限公司', '生产企业', '1961', '40694', '2015-02-03 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('61', '贵州野鹿盖茶业有限公司', '生产企业', '1707', '40127', '2014-12-25 00:00:00', '0', '生产过程符合OFDC有机认证标准的要求');
INSERT INTO `business_teaunit` VALUES ('62', '贵州阳春白雪茶业有限公司', '生产企业', '1509', '39820', '2014-12-08 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('63', '贵州雷山千里香脚尧秀文茶业有限公司', '生产企业', '1949', '40661', '2015-02-02 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('64', '贵州高山有机茶开发有限公司', '生产企业', '1765', '40235', '2014-12-31 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('65', '贵州黔韵福生态茶业有限公司', '生产企业', '1785', '40307', '2015-01-06 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('66', '贵州鼎盛茶业有限公司', '生产企业', '2016', '42067', '2015-03-11 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('67', '雷山县茗聚园茶叶专业合作社', '生产企业', '1939', '40636', '2015-01-29 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('68', '黔西南州嘉宏茶业有限责任公司', '生产企业', '1164', '39517', '2014-11-07 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('69', '凤冈县娄山春茶叶专业合作社', '生产企业', '1759', '40214', '2014-12-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('70', '凤冈县灵蔓茶业农民专业合作社', '生产企业', '1858', '40466', '2015-01-19 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('71', '凤冈县田坝明雨茶厂', '生产企业', '1722', '40149', '2014-12-26 00:00:00', '0', '核定使用商品(第30类)');
INSERT INTO `business_teaunit` VALUES ('72', '凤岗县绿缘春茶场', '生产企业', '1698', '40114', '2014-12-24 00:00:00', '0', '土溪镇2012年度茶叶产业发展优秀企业');
INSERT INTO `business_teaunit` VALUES ('73', '安顺市秀璞茶业农民专业合作社', '生产企业', '1946', '40654', '2015-02-02 00:00:00', '0', '请修正');
INSERT INTO `business_teaunit` VALUES ('74', '安顺御茶村茶业有限责任公司', '生产企业', '1943', '40646', '2015-01-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('75', '安顺金坝茶叶加工有限公司', '生产企业', '1971', '40931', '2015-02-05 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('76', '岑巩县和协天然野生茶厂', '生产企业', '1754', '40206', '2014-12-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('77', '晴隆县吉祥茶业有限公司', '生产企业', '1171', '39528', '2014-11-10 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('78', '晴隆县清韵茶业有限公司', '生产企业', '1890', '40548', '2015-01-26 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('79', '水城县茶叶发展有限公司', '生产企业', '1720', '40146', '2014-12-26 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('80', '湄潭县八角山茶业有限公司', '生产企业', '1868', '40492', '2015-01-21 00:00:00', '0', '八角山茶业有限公司简介\n\n\n贵州湄潭县八角山茶业有限公司成立于2010年10月21日，属私营独资企业, 位于茶乡湄潭境内万亩茶海之边。湄潭湄江两岸山川秀丽, 生态良好, 是西南名茶之乡, 制茶历史悠久, 公司制茶厂有着得天独厚的自然条件, 公司是一家集茶叶种植、生产、加工、销售一体的茶叶专营公司，生产高中档名优茶系列产品。\n    公司系列产品严格按照国卫生标准、技术指标、传统工艺加工及革新的工艺流程，六大系列各具特色，內含物质丰富，清香雅致，浓郁甘爽，色泽绿润，汤色明亮，叶衣明净展。\n     贵州湄潭县八角山茶业有限公司管理制度健全，机构设罝合理，公司将不断加强内部管理，严格过程控制，树立品牌，生产出一流的让消费者放心的产品。');
INSERT INTO `business_teaunit` VALUES ('81', '湄潭县怡和茶业有限责任公司', '生产企业', '1960', '40693', '2015-02-03 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('82', '湄潭县春江茶业有限公司', '生产企业', '1955', '40673', '2015-02-03 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('83', '湄潭县清水江茶厂', '生产企业', '1953', '32205', '2015-02-02 00:00:00', '0', '湄潭县清水江茶厂主要经营：茶叶收购、生产、销售等产品。公司尊崇“踏实、拼搏、责任”的企业精神，并以诚信、共赢、开创经营理念，创造良好的企业环境，以全新的管理模式，完善的技术，周到的服务，卓越的品质为生存根本，我们始终坚持用户至上 用心服务于客户，坚持用自己的服务去打动客户。');
INSERT INTO `business_teaunit` VALUES ('84', '湄潭县随缘茶业有限公司', '生产企业', '1869', '40497', '2015-01-21 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('85', '湄潭黔北茗居茶业有限公司', '生产企业', '1966', '40708', '2015-02-04 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('86', '纳雍县云雾有机茶有限责任公司', '生产企业', '1602', '39970', '2014-12-15 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('87', '贵州万寿茶业有限责任公司', '生产企业', '1859', '40468', '2015-01-19 00:00:00', '0', '公司主要产品“万寿茗珠”牌湄潭翠芽”,毛峰,毛尖,遵义红茶,黑茶及伏砖。');
INSERT INTO `business_teaunit` VALUES ('88', '贵州凤冈县仙人岭锌硒有机茶业有限公司', '生产企业', '1468', '39758', '2014-12-04 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('89', '贵州凤冈县盘云茶业有限公司', '生产企业', '1876', '40516', '2015-01-21 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('90', '贵州和泰春茶叶科技有限公司', '生产企业', '124', '39421', '2014-10-14 00:00:00', '0', '贵州和泰春茶叶科技有限公司，位于贵州省万山特区高楼坪老山口，成立于2008年11月18日，注册资金为叁佰万元。是一家集茶叶种植、生产、深加工、销售为一体的农业加工类企业。公司现有总资产749万元。其中固定资产320万元，流动资产219万元，建有浓缩速溶茶加工生产线一条，速溶功夫茶生产车间一座，占地26640平方米，厂房4000平方米。公司现有职工60人，其中：技术人员17人，在技术人员中高级职称3人，中级职称10人。\n2010 年公司通过了质量管理体系(ISO9001)认证，获得全国工业产品生产许可证(QS)。2010年通过了食品安全管理体系（HACCP）认证，2013年1月开始进行有机食品生产。本公司管理规范，设备先进，技术力量雄厚。');
INSERT INTO `business_teaunit` VALUES ('91', '贵州四品君茶业有限公司', '生产企业', '1871', '38453', '2015-01-21 00:00:00', '0', '本公司坐落于中国贵州省遵义市,供应红茶、绿茶,欢迎惠顾！');
INSERT INTO `business_teaunit` VALUES ('92', '贵州天灵茶叶有限责任公司', '生产企业', '1773', '40263', '2015-01-05 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('93', '贵州安顺春来茶业有限公司', '生产企业', '1934', '40629', '2015-01-29 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('94', '贵州寸心草有机茶业有限公司', '生产企业', '1762', '40219', '2014-12-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('95', '贵州怡壶春生态茶业有限公司', '生产企业', '1792', '40322', '2015-01-07 00:00:00', '0', '贵州怡壶春生态茶业有限公司，成立于2008年4月，位于中国名茶之乡—贵州•湄潭，是农业产业化经营省级龙头企业和市级扶贫龙头企业。公司以生产、加工和销售名优绿茶、红茶为一体，采用公司+农户+基地的现代农业产业化经营模式，现有无公害有机生态茶园2700余亩，拥有国内领先的红茶自动发酵烘干生产线一条及全自动清洁化绿茶生产线一条，每天可生产红茶、绿茶（干茶）两万斤。\n\n“怡壶春”绿茶、“黔门红”红茶是公司主要品牌，其品质获得业界专家认可和消费者青睐，并在国内多个大中城市建立有相对固定的销售网络，部份产品已进入海外市场，为黔茶出山作出尝试性探索。\n\n贵州茶叶商务网（www.guizhoucha.com）及www.gzyhc.cn属于公司官方网站，一天上万次的点击率增加了公司的知名度和美誉度， “怡壶春•淘宝”官方旗舰店，买家的好评和日益增长交易量是消费者对公司产品认可和信赖。');
INSERT INTO `business_teaunit` VALUES ('96', '贵州海马宫茶业有限公司', '生产企业', '1695', '40106', '2014-12-23 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('97', '贵州湄江源茶业有限公司', '生产企业', '1503', '39812', '2014-12-06 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('98', '贵州湄潭兰馨茶业有限公司', '生产企业', '25', '381', null, '0', '贵州湄潭兰馨茶业有限公司位于贵州茶都湄潭，兰馨茶叶香醇，味道醇厚。');
INSERT INTO `business_teaunit` VALUES ('99', '贵州湄潭林圣茶业有限公司', '生产企业', '1634', '40014', '2014-12-16 00:00:00', '0', '公司位于中国名茶之乡——遵义、湄潭，326国道边回龙村柯家店子，交通便宜，信息发达，距县城3公里即到。公司是一家具有种植、生产、加工、销售为一体的民营化企业，下设生产部、销售部、策划部、科研部、财务部、行政部、真情回访部，聘请贵州省茶叶研究所有关专家为技术研发指导。以公司+农户+基地的模式进行科学管理，现有生态茶园2270亩。现代化清洁厂房3678平方米，全面通过QS认证、CQC质量体系认证，有机茶园转换认证1800亩，年生产能力可达686吨，名优茶年生产15吨，大宗茶年生产320吨，红茶年生产351吨。生产系列产品有：湄潭翠芽、遵义毛峰、遵义红茶、大众高绿等等。');
INSERT INTO `business_teaunit` VALUES ('100', '贵州湄潭沁园春茶业有限公司', '生产企业', '2079', '42307', '2015-04-08 00:00:00', '0', '贵州湄潭沁园春茶业有限公司座落在山水田园城市中国·西部茶乡·遵义湄潭的贵州湄潭沁园春茶业公司，创立于2006年，是一家集开发、生产、销售为一体的现代化企业。公司遵循经典人生，健康生活的经营理念。以滴水之恩，涌泉相报为企业为宗旨。践行自律、自信、自尊的精神内涵，诚信敬业，合作创新，致力于为社会奉献安全、健康的各种茶品。\n    公司以茶叶生产为基础。同时，正在开展茶产业的深度开发和茶业与旅游产业的整合经营。公司的核心茶叶基地在海拔1250米的云贵山上，古代贡茶就出于此。茶树在云雾缭绕、云蒸霞蔚之中生长，得天独厚的条件造就了沁园春茶独特的品质、丰富的营养含量和良好的保健作用。\n    贵州湄潭沁园春茶业公司拥有核心有机茶园基地5000多亩。带动农户发展茶叶基地3万亩。设在云贵山上的专业加工厂年生产能力达50万斤以上。产品得到QS认证，HACCP认证，ISO9001认证，有机认证。公司所生产的“沁园春”牌湄潭翠芽、遵义红茶、长相守、茗花仙子、白茶等系列产品精选云贵山上的优质嫩芽。经选叶杀青→ 理条整形→磨锅提香等12道工序精心制作而成，经久耐泡、香高持久、味醇甘厚，乃茶中上品，深受人们喜爱。');
INSERT INTO `business_teaunit` VALUES ('101', '贵州湄潭盛兴茶业有限公司', '生产企业', '1510', '38899', '2014-12-08 00:00:00', '0', '企业基本情况\n\n1、 基本情况\n 企业名称：贵州省湄潭县盛兴茶业有限公司\n 法人代表：潘勇辉\n 注册资本：500万元人民币\n 企业类型：有限责任公司（国有控股）\n 企业住所：湄潭县湄江镇金花村\n 经营范围：茶叶、种植、生产、加工、销售。食品加工、销售\n2、 企业概况\n    贵州湄潭盛兴茶业有限公司是专业从事以茶叶种植、生产、加工、销售为一体的民营企业，于2007年11月9日登记注册的工商营业执照。2012年9月25日贵州盘江投资控股（集团）有限公司参与控股，现为国有控股企业。公司占地13500平方米，公司已通过质量和食品安全体系及QS认证。2010年12月公司被认定为省级扶贫龙头企业；2011年12月公司被认定为国家级农业产业化经营重点龙头企业。\n      公司现有职工75人，有茶叶专业技术人员5人，有20余年制茶经验的高级技师6人，并多年来培训了一批茶园生产管理及制茶技能熟练青工。技术实力雄厚，人才济济，是公司长足发展和“遵义红”品牌建设的中坚力量。');
INSERT INTO `business_teaunit` VALUES ('102', '贵州琦福苑茶业有限公司', '生产企业', '1496', '39802', '2014-12-05 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('103', '贵州省丹寨县添香园硒锌茶厂', '生产企业', '35', '37413', null, '0', '');
INSERT INTO `business_teaunit` VALUES ('104', '贵州省余庆县凤香苑茶业有限责任公司', '生产企业', '2086', '42314', '2015-04-09 00:00:00', '0', '贵州省余庆县风香苑茶业有限责任公司，成立于2010年12月，注册资金1000万元，是集种植、研发、生产、销售于一体的综合型企业。公司自有茶叶种植面积2400余亩，合作带动茶叶生产面积5万余亩。公司拥有3个茶叶加工厂（绿茶、红茶和小叶苦丁茶各1个）和1个产品展示接待中心，年产绿茶300吨，红茶150吨，白茶20吨，苦丁茶50吨。公司坚持以“品质求生存，诚信求发展”的理念，信守“敬天爱人”、“厚物载德”的价值观，注重“科学化种植，标准化生产，品牌化运营” 的管理模式，传承“求真、合道、得趣、养生”的黔茶文化精神，致力打造“凇烟”、“凤香苑”黔茶新品牌和国际化市场格局，保证让国人品味到安全放心、健康有益的茶。公司2012年2月被评为“余庆县农业产业化经营县级龙头企业”，“余庆县青年创业就业示范基地”、年度茶叶产业发展工作“优秀企业”，并获全国质量诚信AAA级品牌企业的荣誉称号和“恒天杯”全国名优绿茶评比银奖。');
INSERT INTO `business_teaunit` VALUES ('105', '贵州省六枝特区天香茶业有限公司', '生产企业', '85', '38090', null, '0', '');
INSERT INTO `business_teaunit` VALUES ('106', '贵州省六枝特区茶叶开发公司', '生产企业', '77', '38115', null, '0', '');
INSERT INTO `business_teaunit` VALUES ('107', '贵州省凤冈县朝阳茶业有限公司', '生产企业', '1669', '40059', '2014-12-19 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('108', '贵州省凤冈县浪竹有机茶业有限公司', '生产企业', '1477', '39773', '2014-12-04 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('109', '贵州省大方县九洞天资源开发有限责任公司茶叶分公司', '生产企业', '1753', '40203', '2014-12-30 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('110', '贵州省安顺市国品黔茶老落坡茶叶农民专业合作社', '生产企业', '1873', '40506', '2015-01-21 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('111', '贵州省湄潭县一丫翠片茶业有限公司', '生产企业', '1796', '40335', '2015-01-08 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('112', '贵州省湄潭县制茶厂', '生产企业', '1562', '39904', '2014-12-11 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('113', '贵州省湄潭县栗香茶业有限公司', '生产企业', '1106', '39431', '2014-10-20 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('114', '贵州省湄潭县芸香茶业有限公司', '生产企业', '1864', '40484', '2015-01-20 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('115', '贵州省湄潭县西山茶业有限公司', '生产企业', '1701', '40120', '2014-12-24 00:00:00', '0', '贵州省湄潭县西山茶业有限公司（前身为2008年9月注册的贵州省湄潭县茶润生灵茶业专业合作社）是一家集茶叶种植（合作社）、加工、销售为一体的民营企业。');
INSERT INTO `business_teaunit` VALUES ('116', '贵州省湄潭县鑫辉茶业有限公司', '生产企业', '1816', '40365', '2015-01-09 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('117', '贵州省湄潭县黔茗茶业有限责任公司', '生产企业', '2014', '42030', '2015-03-10 00:00:00', '0', '贵州省湄潭县黔茗茶业有限责任公司，成立于2008年，是集茶叶生产、加工、销售、出口、弘扬茶文化为一体的综合型现代化企业。公司拥有总资产4800万元，无公害绿色生态茶园13000亩，厂区占地面积30440㎡，有现代化办公楼、红茶生产车间、绿茶生产车间、爱心包装车间、恒温冻库、员工沐浴室和员工食堂。公司拥有名优绿茶智能化生产线一条，具有绿茶分段速冷、茶叶光波杀青、自动加压揉捻及风选回软等多项先进技术，其年加工能力达1200吨。公司还拥有红茶数控生产线一条，具有红茶自动揉捻系统、数控发酵系统及数控模拟碳焙系统等先进设备，其年加工能力1000吨。工业总产值4500万元，公司通过了ISO9001:2008质量管理体系认证和HACCP质量安全管理体系认证，并取得了出口贸易经营权。依托贵州高原得天独厚的自然地理环境，利用自然禀赋的优良茶叶资源，黔茗茶业坚持传统工艺与创新技术相结合，目前获得5项专利技术同时公司开发了“黔潭玉翠”“黔茗红”“随意泡”系列产品，其中“黔茗红”功夫红茶远销全国25个省市，全国共有80余家实体专卖店，深受客户好评。');
INSERT INTO `business_teaunit` VALUES ('118', '贵州省茶叶科学研究所试验示范基地', '生产企业', '1865', '40486', '2015-01-20 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('119', '贵州省金沙县茶叶专业合作社', '生产企业', '1694', '40105', '2014-12-23 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('120', '贵州省雷山县毛克翕茶叶发展研究所', '生产企业', '104', '39267', '2014-09-25 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('121', '贵州省雷山县绿叶香茶业有限责任公司', '生产企业', '1645', '40030', '2014-12-17 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('122', '贵州省雷山县脚尧茶业有限公司', '生产企业', '1649', '40035', '2014-12-17 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('123', '贵州省雷山县苗家春茶业有限公司', '生产企业', '1646', '40032', '2014-12-17 00:00:00', '0', '贵州省雷山县苗家春茶业有限公司成立于2004年8月，注册资金200万元，是一个集茶叶基地种植、茶叶加工、销售、茶叶技术咨询、茶艺为一体的有限责任公司。公司厂房占地面积3992.3平方米，有先进的茶叶生产设备，质量检验设备齐全配套。公司自有优质茶叶基地1500亩，其中有机茶叶面积600亩；年生产干茶5吨，其中有机茶叶2.2吨；可实现产值350万元。公司立足雷山县得天独厚的资源优势，走机制名优茶创新路线，生产的主要产品有“雷公山银球茶”、“雷公山清明茶”、 “雷公山苗家白茶”、“ 雷公山玉针茶”、“ 雷公山玉叶茶”、“ 雷公山毛峰茶”等系列产品。\n    公司在“以质量求生存，以信誉求发展，不断进取、不断拼搏、做大做强茶产业”目标指引下，建立有一支产品研发、管理、加工、基地建设等较为完善的人才队伍，高级管理人员8人，高级技师4人，专业技术9人，季节性工人120多人，公司还聘请中国农业科学院茶叶研究所教授、专家2人、省茶科所2人作为公司发展和质量管理的专门顾问。得到各部门的认可，获得诸多的荣誉，有自己的生产商标，连续获得有机认证书，取得了良好的经济效益与社会效益。');
INSERT INTO `business_teaunit` VALUES ('124', '贵州省黎平县侗乡春茶业有限公司', '生产企业', '1688', '40098', '2014-12-23 00:00:00', '0', '贵州省黎平县侗乡春茶业有限公司，坐落于“全国重点产茶县”、“中国名茶之乡”黎平县境内，是一家集种植、加工、销售、茶文化交流为一体的综合企业。公司下设6个分公司，领办1个农民茶叶专业合作社，设有生产技术指导部、质检安全管理部、市场信息营销部、行政财务部，终端专营店面3个。公司现有茶园面积6226亩，其中：龙井43号1550亩，福鼎大白茶4676亩，建有标准化清洁生产车间4座，近4000平方米，并取得了国家食品安全QS认证。生产的“侗乡春”牌“雀舌茶”、“黎香绿茶”、“翠针茶”以优质鲜叶为原料，以其“色、香、味、形”俱佳，深受各方客户喜爱，产品远销山东、浙江、江苏、湖南、湖北、四川等地。近年来，经不断探索，并成功制作出各类优质红茶，其品质可与同类名茶媲美。公司秉承以“绿色生态”为宗旨、“食品安全”为命脉的理念，精心打造了“侗乡春”牌系列茶产品，先后五次荣获“中茶杯”、“中绿杯”金奖，2004年被农业部茶叶质量监督检验测试中心认证为“无公害放心茶”和中国茶科所“定点服务企业”， 2007年跻身贵州省“农业产业化经营重点龙头企业”，2010年跻身中华全国供销合作总社“农业产业化重点龙头企业”。');
INSERT INTO `business_teaunit` VALUES ('125', '贵州省黎平县森绿茶业对外贸易有限公司', '生产企业', '1824', '40380', '2015-01-09 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('126', '贵州省黎平雀舌茶业有限公司', '生产企业', '1674', '40070', '2014-12-20 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('127', '贵州紫日茶业科技有限公司', '生产企业', '1618', '39986', '2014-12-15 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('128', '贵州贵福春茶业有限公司', '生产企业', '1961', '40694', '2015-02-03 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('129', '贵州野鹿盖茶业有限公司', '生产企业', '1707', '40127', '2014-12-25 00:00:00', '0', '生产过程符合OFDC有机认证标准的要求');
INSERT INTO `business_teaunit` VALUES ('130', '贵州阳春白雪茶业有限公司', '生产企业', '1509', '39820', '2014-12-08 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('131', '贵州雷山千里香脚尧秀文茶业有限公司', '生产企业', '1949', '40661', '2015-02-02 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('132', '贵州高山有机茶开发有限公司', '生产企业', '1765', '40235', '2014-12-31 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('133', '贵州黔韵福生态茶业有限公司', '生产企业', '1785', '40307', '2015-01-06 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('134', '贵州鼎盛茶业有限公司', '生产企业', '2016', '42067', '2015-03-11 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('135', '雷山县茗聚园茶叶专业合作社', '生产企业', '1939', '40636', '2015-01-29 00:00:00', '0', '');
INSERT INTO `business_teaunit` VALUES ('136', '黔西南州嘉宏茶业有限责任公司', '生产企业', '1164', '39517', '2014-11-07 00:00:00', '0', '');

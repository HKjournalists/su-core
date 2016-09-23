

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `test_lab`;
CREATE TABLE `test_lab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) DEFAULT NULL,
  `parent_lab_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_parent_lab` (`parent_lab_id`),
  CONSTRAINT `fk_parent_lab` FOREIGN KEY (`parent_lab_id`) REFERENCES `test_lab` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test_lab
-- ----------------------------
INSERT INTO `test_lab` VALUES ('3', null, null);
INSERT INTO `test_lab` VALUES ('5', '贵阳', null);
INSERT INTO `test_lab` VALUES ('38', '贵州贵阳', '3');
INSERT INTO `test_lab` VALUES ('39', '安顺', '38');

DROP TABLE IF EXISTS `fda`;
CREATE TABLE `fda` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) DEFAULT NULL,
  `code` varchar(200) DEFAULT NULL,
  `parent_fda_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_fda_code` (`code`),
  KEY `fk_parent_fda` (`parent_fda_id`),
  CONSTRAINT `fk_parent_fda` FOREIGN KEY (`parent_fda_id`) REFERENCES `fda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fda
-- ----------------------------
INSERT INTO `fda` VALUES ('1', '贵阳', null, null);
INSERT INTO `fda` VALUES ('2', null, null, null);
INSERT INTO `fda` VALUES ('34', '贵州贵阳', 'GZFDA', '2');
INSERT INTO `fda` VALUES ('35', '安顺', 'ASFDA', '34');
-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `display_name` varchar(200) NOT NULL,
  `type` smallint(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '贵州省食品与药物管理局.贵阳食品与药物管理局', 'fda', '3');
INSERT INTO `user` VALUES ('2', '无', '无', '0');
INSERT INTO `user` VALUES ('3', '无', '无', '0');
INSERT INTO `user` VALUES ('5', '贵州省分析测试研究院.贵阳市分析测试研究院', 'testlab', '4');
INSERT INTO `user` VALUES ('34', '贵州食品与药物管理局', '贵州食品与药物管理局', '3');
INSERT INTO `user` VALUES ('35', '贵州食品与药物管理局.安顺食品与药物管理局', '安顺食品与药物管理局', '3');
INSERT INTO `user` VALUES ('38', '贵州分析测试研究', '贵州分析测试研究', '4');
INSERT INTO `user` VALUES ('39', '贵州分析测试研究.安顺分析测试研究院', '安顺分析测试研究院', '4');

DROP TABLE IF EXISTS `test_quipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_quipment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_unit_id` int(11) DEFAULT NULL COMMENT '企业',
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '设备名称',
  `format` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '规格型号',
  `accuracy_level` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '精度等级',
  `amount` int(11) DEFAULT NULL COMMENT '数量',
  `health` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '完好状态',
  `expiration_date` datetime DEFAULT NULL COMMENT '检定有效截止日期',
  `place` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '使用场所',
  `producer` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '生产厂及国别',
  `produce_date` datetime DEFAULT NULL COMMENT '生产日期',
  `purchase_date` datetime DEFAULT NULL COMMENT '购置日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='检测设备';
/*!40101 SET character_set_client = @saved_cs_client */;

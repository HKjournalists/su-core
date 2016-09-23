--
-- Table structure for table `material`
--

DROP TABLE IF EXISTS `material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `material` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_unit_id` int(11) DEFAULT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `format` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `annual_need` int(11) DEFAULT NULL COMMENT '年需要量',
  `standard_code` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '执行标准代号',
  `producer` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `country` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国别',
  `license_no` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '生产许可证编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
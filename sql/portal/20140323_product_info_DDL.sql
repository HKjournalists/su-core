--
-- Table structure for table `product_info`
--

DROP TABLE IF EXISTS `product_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `cert_id` int(11) DEFAULT NULL COMMENT '信息类型',
  `unit` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '发证机构',
  `start` datetime DEFAULT NULL COMMENT '发证日期',
  `end` datetime DEFAULT NULL COMMENT '有效期至',
  `no` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '证书编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
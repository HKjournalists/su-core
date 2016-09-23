DROP TABLE IF EXISTS `t_sentimen_website`;
CREATE TABLE `t_sentimen_website` (
  `website_name` varchar(255) NOT NULL,
  `website_url` varchar(255) NOT NULL,
  `organizationId` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_sentiment_org_to_topic`;
CREATE TABLE `t_sentiment_org_to_topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organizationName` varchar(255) DEFAULT NULL,
  `organizationId` bigint(20) DEFAULT NULL,
  `topicId` varchar(255) DEFAULT NULL,
  `topicName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;


CREATE TABLE `product_barcode_to_qrcode` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `product_barcode` varchar(255) NOT NULL DEFAULT '' COMMENT '对应的产品条形码',
  `product_id` varchar(255) NOT NULL DEFAULT '' COMMENT '对应的产品id',
  `start_num` int(10) NOT NULL DEFAULT '0' COMMENT '二维码id的起始位置',
  `end_num` int(10) NOT NULL DEFAULT '0' COMMENT '二维码id对应的终止位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
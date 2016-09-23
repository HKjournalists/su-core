/**
 * 删除表字段
 * 
 *  author:wubiao
 * data :2016.7.20
 * 
 * **/
ALTER TABLE dishs_no  DROP COLUMN show_flag;

SET FOREIGN_KEY_CHECKS=0;

/**
 * 新增菜品展示外键表
 *author:wubiao
 * data :2016.7.20
 * 
 * **/
DROP TABLE IF EXISTS `dishs_no_show`;
CREATE TABLE `dishs_no_show` (
  `show_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `dishs_no_id` BIGINT(11) NOT NULL ,
  `show_flag` VARCHAR(2) DEFAULT NULL,
  `sample_flag` VARCHAR(2) DEFAULT NULL,
  `show_time` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`show_id`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;



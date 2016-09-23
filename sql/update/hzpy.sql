# MySQL-Front 5.0  (Build 1.0)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: 127.0.0.1    Database: myrest
# ------------------------------------------------------
# Server version 5.6.15

#
# Source for function hzpy
#

DROP FUNCTION IF EXISTS `hzpy`;
CREATE FUNCTION `hzpy`(s nvarchar(80)) RETURNS varchar(80) CHARSET utf8
BEGIN
	DECLARE i INT DEFAULT 1 ;
    DECLARE strlen INT DEFAULT LENGTH(s);
    DECLARE str1 NVARCHAR(2) DEFAULT '';
    DECLARE outstr NVARCHAR(80) DEFAULT '';
    WHILE i<=strlen DO
       SELECT py_code INTO str1 FROM basic_pinyin WHERE `CHINESE`=(SUBSTRING(s,i,1));
       SET outstr=CONCAT(outstr,str1);
       SET str1='';
       SET i=i+1;
    END WHILE;
    SET outstr= UCASE(outstr);
    RETURN outstr;
END;


/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;

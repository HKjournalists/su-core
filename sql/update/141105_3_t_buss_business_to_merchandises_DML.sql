ALTER TABLE `t_buss_business_to_merchandises`
DROP PRIMARY KEY;

ALTER TABLE `t_buss_business_to_merchandises`
ADD COLUMN `ID`  bigint(20) NOT NULL AUTO_INCREMENT FIRST ,
ADD PRIMARY KEY (`ID`);
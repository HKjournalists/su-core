DROP PROCEDURE IF EXISTS productLicenseNo_DDL;
DELIMITER //
CREATE PROCEDURE productLicenseNo_DDL()
begin
declare doend int default 0;
declare qsNo varchar(100) default null;
DECLARE listqsNo_cursor CURSOR FOR SELECT DISTINCT qs_no FROM business_unit where qs_no is not NULL;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET doend=1; 
open listqsNo_cursor;
FETCH listqsNo_cursor into qsNo;
REPEAT 
	if(qsNo!='') then 
		insert into production_license_info(qs_no) values(qsNo);
	end if;
FETCH listqsNo_cursor into qsNo;
UNTIL  doend = 1 
END REPEAT;  
CLOSE  listqsNo_cursor; 
end//
DELIMITER ;

CALL productLicenseNo_DDL();

DROP PROCEDURE IF EXISTS productLicenseNo_DDL;
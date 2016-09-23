DROP PROCEDURE IF EXISTS licenseNo_DDL;
DELIMITER //
CREATE PROCEDURE licenseNo_DDL()
begin
declare doend int default 0;
declare tlicenseNo varchar(100) default null;
DECLARE listlicenseNo_cursor CURSOR FOR SELECT DISTINCT license_no FROM business_unit where license_no is not NULL;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET doend=1; 
open listlicenseNo_cursor;
FETCH listlicenseNo_cursor into tlicenseNo;
REPEAT 
	if(tlicenseNo!='') then 
		insert into license_info(license_no) values(tlicenseNo);
	end if;
FETCH listlicenseNo_cursor into tlicenseNo;
UNTIL  doend = 1 
END REPEAT;  
CLOSE  listlicenseNo_cursor; 
end//
DELIMITER ;

CALL licenseNo_DDL();

DROP PROCEDURE IF EXISTS licenseNo_DDL;
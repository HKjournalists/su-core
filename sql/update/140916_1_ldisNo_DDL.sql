DROP PROCEDURE IF EXISTS licenseNo_DDL;
DELIMITER //
CREATE PROCEDURE licenseNo_DDL()
begin
declare doend int default 0;
declare disNo varchar(100) default null;
DECLARE listlicenseNo_cursor CURSOR FOR SELECT DISTINCT distribution_no FROM business_unit where distribution_no is not NULL;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET doend=1; 
open listlicenseNo_cursor;
FETCH listlicenseNo_cursor into disNo;
REPEAT 
	if(disNo!='') then 
		insert into circulation_permit_info(distribution_no) values(disNo);
	end if;
FETCH listlicenseNo_cursor into disNo;
UNTIL  doend = 1 
END REPEAT;  
CLOSE  listlicenseNo_cursor; 
end//
DELIMITER ;

CALL licenseNo_DDL();

DROP PROCEDURE IF EXISTS licenseNo_DDL;
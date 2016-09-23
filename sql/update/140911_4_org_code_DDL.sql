DROP PROCEDURE IF EXISTS orgCode_DDL;
DELIMITER //
CREATE PROCEDURE orgCode_DDL()
begin
declare doend,cont int default 0;
declare torgCode varchar(100) default null;
DECLARE listOrgCode_cursor CURSOR FOR SELECT org_code from business_unit WHERE org_code is not null;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET doend=1; 
open listOrgCode_cursor;
FETCH listOrgCode_cursor into torgCode;
REPEAT 
    select count(*) into cont from organizing_institution where org_code=torgCode;
	if(cont=0) then 
		insert into organizing_institution(org_code) values(torgCode);
	end if;
FETCH listOrgCode_cursor into torgCode;
UNTIL  doend = 1 
END REPEAT;  
CLOSE  listOrgCode_cursor; 
end//
DELIMITER ;

CALL orgCode_DDL();

DROP PROCEDURE IF EXISTS orgCode_DDL;
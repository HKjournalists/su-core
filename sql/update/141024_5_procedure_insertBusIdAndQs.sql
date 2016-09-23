DROP PROCEDURE IF EXISTS insertBusIdAndQs;
DELIMITER //
CREATE PROCEDURE insertBusIdAndQs()
BEGIN
	declare endFlg int default 0;
	declare busId bigint;
	declare qsNO varchar(100);
	DECLARE listBusIdAndQs_cursor CURSOR FOR  select DISTINCT business_id,QS_NO from product_to_businessunit 
																						where business_id is not null and business_id != '' 
																									and QS_NO is not null and QS_NO !='';
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	open listBusIdAndQs_cursor;
	FETCH listBusIdAndQs_cursor into busId,qsNO;
	REPEAT
	if(busId is not NULL and qsNO is not null) THEN
		insert into businessunit_to_prolicinfo(business_id,qs_no) values(busId,qsNO);
	end if;
	FETCH listBusIdAndQs_cursor into busId,qsNO;
	UNTIL endFlg = 1 
	END REPEAT;  
	CLOSE listBusIdAndQs_cursor; 
END//
DELIMITER ;
-- CALL
CALL insertBusIdAndQs();
DROP PROCEDURE insertBusIdAndQs;
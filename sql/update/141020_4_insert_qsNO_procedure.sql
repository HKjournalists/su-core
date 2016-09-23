DROP PROCEDURE IF EXISTS insert_qsNo;
DELIMITER //
CREATE PROCEDURE insert_qsNo()
BEGIN
	declare endFlg,cont int default 0;
	declare addQs varchar(100);
	DECLARE listQs_cursor CURSOR FOR  select DISTINCT QS_NO from product_to_businessunit 
																		where QS_NO is not null and QS_NO != ''
																		and QS_NO not in(select DISTINCT qs_no from production_license_info 
																												where qs_no is not null and qs_no != '');
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	open listQs_cursor;
	FETCH listQs_cursor into addQs;
	REPEAT
	if(addQs is not NULL ) THEN
			insert into production_license_info(qs_no) values(addQs);
	end if;
	FETCH listQs_cursor into addQs;
UNTIL endFlg = 1 
END REPEAT;  
CLOSE listQs_cursor; 
END//
DELIMITER ;

CALL insert_qsNo();
DROP PROCEDURE insert_qsNo;

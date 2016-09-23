DROP PROCEDURE IF EXISTS updateQsOfPro2Lic;
DELIMITER //
CREATE PROCEDURE updateQsOfPro2Lic()
begin
	DECLARE pro2LicId bigint DEFAULT 0;
	DECLARE qsno VARCHAR(50) DEFAULT '';
	DECLARE qsid bigint DEFAULT 0;
	DECLARE count int default 0;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT id,qs_no FROM product_to_businessunit ORDER BY id;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into pro2LicId,qsno;
		REPEAT 
			SELECT COUNT(*) INTO count FROM production_license_info WHERE qs_no = qsno;
			IF(count = 0) THEN
				INSERT INTO production_license_info(qs_no) VALUES(qsno);
			END IF;
			
			SELECT id into qsid from production_license_info WHERE qs_no = qsno;
			UPDATE product_to_businessunit SET qs_id = qsid WHERE id = pro2LicId;

			FETCH list_cursor into pro2LicId,qsno;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateQsOfPro2Lic();

DROP PROCEDURE IF EXISTS updateQsOfPro2Lic;
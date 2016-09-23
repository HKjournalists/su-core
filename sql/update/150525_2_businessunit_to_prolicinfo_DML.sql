DROP PROCEDURE IF EXISTS updateQsOfBus2Lic;
DELIMITER //
CREATE PROCEDURE updateQsOfBus2Lic()
begin
	DECLARE bus2LicId bigint DEFAULT 0;
	DECLARE qsno VARCHAR(50) DEFAULT '';
	DECLARE qsid bigint DEFAULT 0;
	DECLARE count int default 0;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT id,qs_no FROM businessunit_to_prolicinfo;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into bus2LicId,qsno;
		REPEAT 
			SELECT COUNT(*) INTO count FROM production_license_info WHERE qs_no = qsno;
			IF(count = 0) THEN
				INSERT INTO production_license_info(qs_no) VALUES(qsno);
			END IF;
			
			SELECT id into qsid from production_license_info WHERE qs_no = qsno;
			UPDATE businessunit_to_prolicinfo SET qs_id = qsid WHERE id = bus2LicId;

			FETCH list_cursor into bus2LicId,qsno;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateQsOfBus2Lic();

DROP PROCEDURE IF EXISTS updateQsOfPro2Lic;
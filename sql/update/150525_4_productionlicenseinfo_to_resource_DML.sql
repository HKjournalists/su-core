DROP PROCEDURE IF EXISTS updateQsOfLic2Res;
DELIMITER //
CREATE PROCEDURE updateQsOfLic2Res()
begin
	DECLARE lic2ResId bigint DEFAULT 0;
	DECLARE qsno VARCHAR(50) DEFAULT '';
	DECLARE qsid bigint DEFAULT 0;
	DECLARE count int default 0;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT id,qs_no FROM productionLicenseInfo_to_resource;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into lic2ResId,qsno;
		REPEAT 
			SELECT COUNT(*) INTO count FROM production_license_info WHERE qs_no = qsno;
			IF(count = 0) THEN
				INSERT INTO production_license_info(qs_no) VALUES(qsno);
			END IF;
			
			SELECT id into qsid from production_license_info WHERE qs_no = qsno;
			UPDATE productionLicenseInfo_to_resource SET qs_id = qsid WHERE id = lic2ResId;

			FETCH list_cursor into lic2ResId,qsno;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateQsOfLic2Res();

DROP PROCEDURE IF EXISTS updateQsOfLic2Res;
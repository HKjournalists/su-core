DROP PROCEDURE IF EXISTS updateQsOfLtPro2qs;
DELIMITER //
CREATE PROCEDURE updateQsOfLtPro2qs()
begin
	DECLARE ltPro2qsId bigint DEFAULT 0;
	DECLARE qsno VARCHAR(50) DEFAULT '';
	DECLARE qsid bigint DEFAULT 0;
	DECLARE count int default 0;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT id,qs_no FROM t_liutong_produce_qs;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into ltPro2qsId,qsno;
		REPEAT 
			SELECT COUNT(*) INTO count FROM production_license_info WHERE qs_no = qsno;
			IF(count = 0) THEN
				INSERT INTO production_license_info(qs_no) VALUES(qsno);
			END IF;
			
			SELECT id into qsid from production_license_info WHERE qs_no = qsno;
			UPDATE t_liutong_produce_qs SET qs_id = qsid WHERE id = ltPro2qsId;

			FETCH list_cursor into ltPro2qsId,qsno;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateQsOfLtPro2qs();

DROP PROCEDURE IF EXISTS updateQsOfLtPro2qs;
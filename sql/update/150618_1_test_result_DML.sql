DROP PROCEDURE IF EXISTS updateDbflagOfReport;
DELIMITER //
CREATE PROCEDURE updateDbflagOfReport()
begin
	DECLARE report_id_parm bigint DEFAULT 0;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT id FROM test_result WHERE mk_db_flag = '4';
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into report_id_parm;
		REPEAT
			UPDATE test_result SET dbflag = 'parse_pdf' WHERE id = report_id_parm;
			
			FETCH list_cursor into report_id_parm;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateDbflagOfReport();

DROP PROCEDURE IF EXISTS updateDbflagOfReport;

DROP PROCEDURE IF EXISTS updateDbflagOfReport;
DELIMITER //
CREATE PROCEDURE updateDbflagOfReport()
begin
	DECLARE report_id_parm bigint DEFAULT 0;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT id FROM test_result WHERE mk_db_flag = '1';
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into report_id_parm;
		REPEAT
			UPDATE test_result SET dbflag = 'icb' WHERE id = report_id_parm;
			
			FETCH list_cursor into report_id_parm;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateDbflagOfReport();

DROP PROCEDURE IF EXISTS updateDbflagOfReport;
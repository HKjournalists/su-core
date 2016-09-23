-- 在表test_result中添加interceptionPdfPath字段
 ALTER TABLE test_result ADD COLUMN interceptionPdfPath VARCHAR(1024) DEFAULT NULL AFTER fullPdfPath;
DROP PROCEDURE IF EXISTS pro_pdfPath;
DELIMITER //
CREATE PROCEDURE pro_pdfPath()
BEGIN
	declare endFlg int default 0;
	declare testId bigint;
	declare fullPdf,tmpPath VARCHAR(1024);
	DECLARE listBus_cursor CURSOR FOR select id,fullPdfPath from test_result;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	open listBus_cursor;
	FETCH listBus_cursor into testId,fullPdf;
	REPEAT
		if(testId is NOT NULL) THEN
				set tmpPath=CONCAT("http://fsnrec.com:8080/portal",fullPdf);
				update test_result set interceptionPdfPath = tmpPath where id = testId;
		end if;
		FETCH listBus_cursor into testId,fullPdf;
		UNTIL endFlg = 1 
	END REPEAT;  
	CLOSE listBus_cursor; 
END//
DELIMITER ;
-- 调用
 CALL pro_pdfPath();
-- 删除字段fullPdfPath
ALTER TABLE test_result DROP COLUMN fullPdfPath;
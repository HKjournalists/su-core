DROP PROCEDURE IF EXISTS deleteSample_DDL;
DELIMITER //
CREATE PROCEDURE deleteSample_DDL()
begin
declare doend int default 0;
declare sampleid varchar(100) default null;
DECLARE listSampleId_cursor CURSOR FOR SELECT DISTINCT id FROM product_instance WHERE id NOT IN (SELECT sample_id FROM test_result);
DECLARE CONTINUE HANDLER FOR NOT FOUND SET doend=1; 
open listSampleId_cursor;
FETCH listSampleId_cursor into sampleid;
REPEAT 
		DELETE FROM product_instance WHERE id = sampleid;
FETCH listSampleId_cursor into sampleid;
UNTIL  doend = 1 
END REPEAT;  
CLOSE  listSampleId_cursor; 
end//
DELIMITER ;

CALL deleteSample_DDL();

DROP PROCEDURE IF EXISTS deleteSample_DDL;
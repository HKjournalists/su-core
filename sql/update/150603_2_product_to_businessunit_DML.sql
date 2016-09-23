UPDATE product_to_businessunit SET BARCORD = NULL WHERE BARCORD = '';

DROP PROCEDURE IF EXISTS updateBindOfpro2bus;
DELIMITER //
CREATE PROCEDURE updateBindOfpro2bus()
begin
	DECLARE pro2busId bigint DEFAULT 0;
	DECLARE barc VARCHAR(50) DEFAULT NULL;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT id,BARCORD FROM product_to_businessunit WHERE BARCORD IS NOT NULL ORDER BY id;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into pro2busId,barc;
		REPEAT
			UPDATE product_to_businessunit SET bind = 1 WHERE id = pro2busId;

			FETCH list_cursor into pro2busId,barc;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateBindOfpro2bus();

DROP PROCEDURE IF EXISTS updateBindOfpro2bus;
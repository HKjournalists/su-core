DROP PROCEDURE IF EXISTS updateQsnoOfproins;
DELIMITER //
CREATE PROCEDURE updateQsnoOfproins()
begin
	DECLARE sample_id_parm bigint DEFAULT 0;
	DECLARE pro_id_parm bigint DEFAULT 0;
	DECLARE bus_id_parm bigint DEFAULT 0;
  DECLARE qs_no_parm VARCHAR(50) DEFAULT NULL;
	DECLARE count_parm int default 0;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT id,product_id,producer_id FROM product_instance;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into sample_id_parm,pro_id_parm,bus_id_parm;
		REPEAT
			SELECT COUNT(*) INTO count_parm FROM product_to_businessunit p2b 
			INNER JOIN production_license_info pli ON p2b.qs_id = pli.id
			WHERE p2b.PRODUCT_ID = pro_id_parm AND p2b.business_id = bus_id_parm;

			IF(count_parm > 0) THEN
				SELECT pli.qs_no INTO qs_no_parm FROM product_to_businessunit p2b 
				INNER JOIN production_license_info pli ON p2b.qs_id = pli.id
				WHERE p2b.PRODUCT_ID = pro_id_parm AND p2b.business_id = bus_id_parm;
				
				UPDATE product_instance SET qs_no = qs_no_parm WHERE id = sample_id_parm;
			END IF;
			
			FETCH list_cursor into sample_id_parm,pro_id_parm,bus_id_parm;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateQsnoOfproins();

DROP PROCEDURE IF EXISTS updateQsnoOfproins;
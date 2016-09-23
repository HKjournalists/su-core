
DROP PROCEDURE IF EXISTS insert_product_to_business_ProId;
DELIMITER //
CREATE PROCEDURE insert_product_to_business_ProId()
BEGIN
	declare endFlg,cont int default 0;
	declare productId,busId bigint;
	declare qsNo varchar(100);
	DECLARE listProId_cursor CURSOR FOR  select pro_inst.product_id product_id,bus_proTo.qs_no qs_no,bus_proTo.business_id from product_instance pro_inst LEFT JOIN  
		(select business_id,qs_no from product_to_businessunit 
		WHERE PRODUCT_ID is null AND BARCORD is null) bus_proTo on bus_proTo.business_id = pro_inst.producer_id
		WHERE bus_proTo.business_id is NOT NULL GROUP BY product_id,qs_no,bus_proTo.business_id;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	open listProId_cursor;
	FETCH listProId_cursor into productId,qsNo,busId;
	REPEAT
	if(productId is not NULL and busId is not null) THEN
		select count(*) into cont from product_to_businessunit where PRODUCT_ID=productId AND BUSINESS_ID=busId;
		if(cont<1) THEN
		insert into product_to_businessunit(PRODUCT_ID,QS_NO,business_id) VALUES(productId,qsNo,busId);
		end if;
	end if;
	FETCH listProId_cursor into productId,qsNo,busId;
UNTIL endFlg = 1 
END REPEAT;  
CLOSE listProId_cursor; 
END//
DELIMITER ;
 CALL insert_product_to_business_ProId();
delete from product_to_businessunit where product_id is null;
DROP PROCEDURE insert_product_to_business_ProId;

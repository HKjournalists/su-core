-- 在表product_to_businessunit中添加business_id字段
ALTER TABLE product_to_businessunit ADD COLUMN business_id  BIGINT DEFAULT NULL;
DROP PROCEDURE IF EXISTS insert_product_to_business_qs;
DELIMITER //

CREATE PROCEDURE insert_product_to_business_qs()
BEGIN
	declare endFlg,cont int default 0;
	declare busId,org bigint;
  declare tmpQsNo varchar(100);
	DECLARE listBus_cursor CURSOR FOR select id,qs_no,organization from business_unit 
																		WHERE qs_no !='' and qs_no is not NULL;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	open listBus_cursor;
	FETCH listBus_cursor into busId,tmpQsNo,org;
	REPEAT
	if(org is NULL) THEN
		insert into product_to_businessunit(QS_NO,business_id) VALUES(tmpQsNo,busId);
	ELSE
		SELECT count(*) into cont FROM product_to_businessunit where ORGANIZATION=org;
		if(cont=0) THEN
			insert into product_to_businessunit(QS_NO,business_id) VALUES(tmpQsNo,busId); 
		ELSE
			UPDATE product_to_businessunit set business_id=busId where ORGANIZATION=org;
			select count(*) into cont FROM product_to_businessunit where business_id=busId and QS_NO=tmpQsNo;
			if(cont=0) THEN
				insert into product_to_businessunit(QS_NO,business_id) VALUES(tmpQsNo,busId);
			end if;
	end if;
end if;
FETCH listBus_cursor into busId,tmpQsNo,org;
UNTIL endFlg = 1 
END REPEAT;  
CLOSE listBus_cursor; 
END//
DELIMITER ;
CALL insert_product_to_business_qs();
-- 删除表product_to_businessunit中的ORGANIZATION字段
ALTER TABLE product_to_businessunit DROP COLUMN ORGANIZATION;
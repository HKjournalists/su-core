DROP PROCEDURE IF EXISTS moveBusIdToProCert;
DELIMITER //
CREATE PROCEDURE moveBusIdToProCert()
begin
declare endFlg,cont int default 0;
declare proId,busId,orgId bigint default null;
DECLARE listProId_cursor CURSOR FOR SELECT DISTINCT product_id from product_certification;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1; 
open listProId_cursor;
FETCH listProId_cursor into proId;
REPEAT 
   if(proId is not null) then
		SELECT count(*) into cont FROM product where id=proId and organization is not NULL;
		if(cont>0) THEN
			select organization into orgId from product where id=proId;
			if(orgId=23) then
				UPDATE product_certification set business_id=10329 where product_id=proId;
			else
				if(orgId=29) then
					UPDATE product_certification set business_id=10145 where product_id=proId;
				else
					if(orgId=1) THEN
						UPDATE product_certification set business_id=38119 where product_id=proId;
					else
						SELECT id into busId FROM business_unit where organization=orgId;
						UPDATE product_certification set business_id=busId where product_id=proId;
					end if;
				end if;
			end if;
		end if;
   end if;
FETCH listProId_cursor into proId;
UNTIL  endFlg = 1 
END REPEAT;  
CLOSE  listProId_cursor; 
end//
DELIMITER ;

call moveBusIdToProCert();
DROP PROCEDURE IF EXISTS moveBusIdToProCert;
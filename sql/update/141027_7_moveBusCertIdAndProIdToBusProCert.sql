DROP PROCEDURE IF EXISTS moveBusCertIdAndProIdToBusProCert;
DELIMITER //
CREATE PROCEDURE moveBusCertIdAndProIdToBusProCert()
begin
declare endFlg,cont,starp int default 0;
declare busCertId,proId,busId,certId bigint default null;
DECLARE listProCert_cursor CURSOR FOR SELECT cert_id,product_id,business_id from product_certification 
														where business_id is not null and product_id is not null;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1; 
open listProCert_cursor;
FETCH listProCert_cursor into certId,proId,busId;
REPEAT
	select count(*) into cont from business_certification where cert_id=certId and business_id=busId;
	if(cont>0) then
		select id into busCertId from business_certification where cert_id=certId and business_id=busId;
		if(busCertId is not null) then
			select count(*) into cont from business_certification_to_product where product_id=proId and business_cert_id=busCertId;
			if(cont=0) then
				insert into business_certification_to_product(product_id,business_cert_id) values(proId,busCertId);
			end if;
		end if;
	end if;
FETCH listProCert_cursor into certId,proId,busId;
UNTIL  endFlg = 1 
END REPEAT;  
CLOSE  listProCert_cursor; 
end//
DELIMITER ;

call moveBusCertIdAndProIdToBusProCert();
DROP PROCEDURE IF EXISTS moveBusCertIdAndProIdToBusProCert;
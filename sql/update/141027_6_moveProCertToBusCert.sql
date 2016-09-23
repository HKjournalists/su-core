DROP PROCEDURE IF EXISTS moveProCertToBusCert;
DELIMITER //
CREATE PROCEDURE moveProCertToBusCert()
begin
declare endFlg,cont,starp int default 0;
declare busCertId,proId,busId,certId,resId bigint default null;
declare eDate datetime;
declare docUrl varchar(1024);
declare newFName varchar(100);
DECLARE listProCert_cursor CURSOR FOR SELECT DISTINCT id,cert_id,enddate,resource_id,business_id,document_url from product_certification where business_id is not null;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1; 
open listProCert_cursor;
FETCH listProCert_cursor into busCertId,certId,eDate,resId,busId,docUrl;
REPEAT
	select count(*) into cont from business_certification where cert_id=certId and business_id=busId;
	if(cont=0) then
		if(resId is null and docUrl is not null) then
			set starp=(LENGTH(docUrl)-17);
			set newFName=SUBSTRING(docUrl,starp);
			set newFName=REPLACE (newFName,'/','');
			insert into t_test_resource(resource_name,file_name,url,RESOURCE_TYPE_ID)
			values(newFName,newFName,docUrl,8);
			set resId=LAST_INSERT_ID();
		end if;
		if(resId is not null) then
			insert into business_certification(cert_id,enddate,resource_id,business_id) values(certId,eDate,resId,busId);
		end if;
	else
		update product_certification set resource_id=null where id=busCertId;
		delete from t_test_resource where resource_id=resId;
	end if;
FETCH listProCert_cursor into busCertId,certId,eDate,resId,busId,docUrl;
UNTIL  endFlg = 1 
END REPEAT;  
CLOSE  listProCert_cursor; 
end//
DELIMITER ;

call moveProCertToBusCert();
DROP PROCEDURE IF EXISTS moveProCertToBusCert;
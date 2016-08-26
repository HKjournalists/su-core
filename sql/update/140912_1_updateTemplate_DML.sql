DROP PROCEDURE IF EXISTS delTemplateRecode;
DELIMITER //
CREATE PROCEDURE delTemplateRecode(pbarcode varchar(100))
begin
declare endFlg,cont int default 0;
declare repId,tmpId,orgId bigint default null;
declare bcode,usname varchar(200) default null;
DECLARE listTemplate_cursor CURSOR FOR SELECT BAR_CODE,REPORT_ID,ORG_ID,USER_NAME,ID FROM t_test_template WHERE BAR_CODE=pbarcode;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1; 
open listTemplate_cursor;
FETCH listTemplate_cursor into bcode,repId,orgId,usname,tmpId;
REPEAT 
   if(repId is null) then 
	  select count(*) into cont from t_test_template where BAR_CODE=bcode and ORG_ID=orgId and USER_NAME=usname;
	  if(cont>1) then
		  delete from t_test_template where id=tmpId;
	  end if;
	else
	  select count(*) into cont from test_result where id=repId;
	  if(cont=0) then
		 delete from t_test_template where id=tmpId;
	  end if;
   end if;
FETCH listTemplate_cursor into bcode,repId,orgId,usname,tmpId;
UNTIL  endFlg = 1 
END REPEAT;  
CLOSE  listTemplate_cursor; 
end//
DELIMITER ;

DROP PROCEDURE IF EXISTS updateTemplate;
DELIMITER //
CREATE PROCEDURE updateTemplate()
begin
declare doend,cont int default 0;
declare trepId bigint default null;
declare tBarcode varchar(100) default null;
DECLARE listBarcode_cursor CURSOR FOR SELECT BAR_CODE FROM t_test_template GROUP BY BAR_CODE;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET doend=1; 
open listBarcode_cursor;
FETCH listBarcode_cursor into tBarcode;
REPEAT 
   select count(*) into cont from t_test_template where BAR_CODE=tBarcode;
   if(cont>1) then
	  CALL delTemplateRecode(tBarcode);
    else
		select REPORT_ID into trepId from t_test_template where BAR_CODE=tBarcode;
		if(trepId is not null) then
			select count(*) into cont from test_result where id=trepId;
			if(cont=0) then
				delete from t_test_template where BAR_CODE=tBarcode;
			end if;
		end if;
   end if;
FETCH listBarcode_cursor into tBarcode;
UNTIL  doend = 1 
END REPEAT;  
CLOSE  listBarcode_cursor; 
end//
DELIMITER ;

CALL updateTemplate();

DROP PROCEDURE IF EXISTS updateTemplate;
DROP PROCEDURE IF EXISTS delTemplateRecode;
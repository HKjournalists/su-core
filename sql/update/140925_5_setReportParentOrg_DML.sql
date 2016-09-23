DROP PROCEDURE IF EXISTS setReportParentOrg;
DELIMITER //
CREATE PROCEDURE setReportParentOrg()
begin
declare endflag int default 0;
declare currentOrgId,parentOrgId bigint default null;
DECLARE listRepOrgIds_cursor CURSOR FOR SELECT organization from test_result where organization is not null and edition='easy' group by organization;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET endflag=1; 
open listRepOrgIds_cursor;
FETCH listRepOrgIds_cursor into currentOrgId;
REPEAT 
	select parentOrgnization into parentOrgId from business_unit where orgnization=currentOrgId;
	if(parentOrgId=0 or parentOrgId is null) then
		update test_result set parentOrgnization=currentOrgId where organization=currentOrgId;
	else
		update test_result set parentOrgnization=parentOrgId where organization=currentOrgId;
	end if;
FETCH listRepOrgIds_cursor into currentOrgId;
UNTIL  endflag = 1 
END REPEAT;  
CLOSE  listRepOrgIds_cursor; 
end//
DELIMITER ;

CALL setReportParentOrg();

DROP PROCEDURE IF EXISTS setReportParentOrg;
DROP PROCEDURE IF EXISTS updateOrganizationOfReport;
DELIMITER //
CREATE PROCEDURE updateOrganizationOfReport()
begin
	DECLARE report_id_parm bigint DEFAULT 0;
	DECLARE org_name_of_report_parm VARCHAR(100) CHARACTER SET utf8 DEFAULT '';
	DECLARE real_org_of_report_parm bigint DEFAULT 0;

	DECLARE endFlg int default 0;
	DECLARE list_cursor CURSOR FOR SELECT test_result_id FROM test_result_handler;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1;
	
	open list_cursor;
		FETCH list_cursor into report_id_parm;
		REPEAT
			SELECT organization_name INTO org_name_of_report_parm FROM test_result WHERE id = report_id_parm;
			SELECT organization INTO real_org_of_report_parm FROM business_unit WHERE `name` = org_name_of_report_parm;

			UPDATE test_result SET organization = real_org_of_report_parm WHERE id = report_id_parm;
			
			FETCH list_cursor into report_id_parm;
			UNTIL  endFlg = 1
		END REPEAT;  
	CLOSE  list_cursor; 
end//
DELIMITER;

call updateOrganizationOfReport();

DROP PROCEDURE IF EXISTS updateOrganizationOfReport;
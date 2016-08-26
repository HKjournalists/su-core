UPDATE business_unit set sign_flag = TRUE where type = '生产企业' and organization >1;

UPDATE business_unit set sign_flag = FALSE where type = '流通企业' and organization >0;

DROP PROCEDURE IF EXISTS updateBusUnitType;
DELIMITER //
CREATE PROCEDURE updateBusUnitType(busId bigint)
begin
declare cont int default 0;
select count(*) into cont from business_unit where id = busId;
if(cont=1) then
	update business_unit set type="生产企业", sign_flag = FALSE where id = busId;
end if;
end//
DELIMITER ;

CALL updateBusUnitType(377);
CALL updateBusUnitType(30025);
CALL updateBusUnitType(12487);
CALL updateBusUnitType(37548);
CALL updateBusUnitType(38087);
CALL updateBusUnitType(37555);
CALL updateBusUnitType(380);
CALL updateBusUnitType(381);
CALL updateBusUnitType(38105);
CALL updateBusUnitType(13128);
CALL updateBusUnitType(38109);
CALL updateBusUnitType(32261);
CALL updateBusUnitType(38094);
CALL updateBusUnitType(34757);
CALL updateBusUnitType(10459);
CALL updateBusUnitType(33395);
CALL updateBusUnitType(37546);
CALL updateBusUnitType(10387);
CALL updateBusUnitType(19965);
CALL updateBusUnitType(38088);
CALL updateBusUnitType(12004);
CALL updateBusUnitType(10492);
CALL updateBusUnitType(10332);
CALL updateBusUnitType(38107);
CALL updateBusUnitType(38117);
CALL updateBusUnitType(37550);
CALL updateBusUnitType(37420);
CALL updateBusUnitType(37551);
CALL updateBusUnitType(38093);
CALL updateBusUnitType(37553);
CALL updateBusUnitType(37344);
CALL updateBusUnitType(37341);
CALL updateBusUnitType(37412);
CALL updateBusUnitType(37410);
CALL updateBusUnitType(34757);
CALL updateBusUnitType(39452);
CALL updateBusUnitType(39467);
CALL updateBusUnitType(39519);
CALL updateBusUnitType(26430);

DROP PROCEDURE IF EXISTS updateBusUnitType;
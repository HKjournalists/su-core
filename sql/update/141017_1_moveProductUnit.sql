DROP PROCEDURE IF EXISTS moveProductUnit;
DELIMITER //
CREATE PROCEDURE moveProductUnit()
begin
declare endFlg,cont,unitId int default 0;
declare proId bigint default null;
declare pUnit varchar(100) default null;
DECLARE listUnit_cursor CURSOR FOR SELECT id,unit FROM product;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET endFlg=1; 
open listUnit_cursor;
FETCH listUnit_cursor into proId,pUnit;
REPEAT
   if(pUnit is not null and pUnit!='') then
			select count(*) into cont from t_meta_unit WHERE `name`=pUnit;
			if(cont>0) THEN
				SELECT id into unitId FROM t_meta_unit where `name`=pUnit;
				UPDATE product SET unit_id=unitId where id=proId;
			ELSE
				insert into t_meta_unit(NAME) values(pUnit);
				set unitId=LAST_INSERT_ID();
				UPDATE product SET unit_id=unitId where id=proId;
			end if;
   end if;
FETCH listUnit_cursor into proId,pUnit;
UNTIL  endFlg = 1 
END REPEAT;  
CLOSE  listUnit_cursor; 
end //
DELIMITER ;

call moveProductUnit();
DROP PROCEDURE IF EXISTS moveProductUnit;
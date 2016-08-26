
set @httpUrl='http://fsnrec.com:8080/portal/enterprise/logoImg/';

DROP PROCEDURE IF EXISTS insert_bus_logo;
DELIMITER //
CREATE PROCEDURE insert_bus_logo(pbusName varchar(300),pfilName varchar(300),pNewFileName varchar(300))
BEGIN
	declare busId,resId bigint default null;
	declare url varchar(1024) default null;
	select id into busId from enterprise_registe WHERE enterpriteName = pbusName;
	if(busId is null) then
		insert into enterprise_registe(enterpriteName) values(pbusName);
		set busId = LAST_INSERT_ID();
	end if;
	set url = CONCAT(@httpUrl,pNewFileName);
	insert into t_test_resource(FILE_NAME,RESOURCE_NAME,URL,RESOURCE_TYPE_ID) values(pfilName,pfilName,url,8);
	set resId = LAST_INSERT_ID();
	if(resId is not null) then
			insert into t_business_logo_to_resource(RESOURCE_ID,ENTERPRISE_REGISTE_ID) values(resId,busId);
	end if;
END//
DELIMITER ;

CALL insert_bus_logo('内蒙古蒙牛乳业（集团）股份有限公司','内蒙古蒙牛乳业（集团）股份有限公司.jpg','1411629730076.jpg');

CALL insert_bus_logo('贵州五福坊食品有限公司','贵州五福坊食品有限公司.jpg','1411629730185.jpg');

CALL insert_bus_logo('贵州北极熊实业有限公司','贵州北极熊实业有限公司.jpg','1411629730295.jpg');

CALL insert_bus_logo('贵州国台酒业有限公司','贵州国台酒业有限公司.jpg','1411629730404.jpg');

CALL insert_bus_logo('贵州好一多乳业股份有限公司','贵州好一多乳业股份有限公司.jpg','1411629730513.jpg');

CALL insert_bus_logo('贵州湄潭兰馨茶业有限公司','贵州湄潭兰馨茶业有限公司.png','1411629730622.png');

CALL insert_bus_logo('贵州老干爹食品有限公司','贵州老干爹食品有限公司.jpg','1411629730731.jpg');

CALL insert_bus_logo('贵州雄正酒业有限公司','贵州雄正酒业有限公司.jpg','1411629730841.jpg');

CALL insert_bus_logo('贵阳三联乳业有限公司','贵阳三联乳业有限公司.gif','1411629730950.gif');

CALL insert_bus_logo('贵阳市味莼园食品股份有限公司','贵阳味莼园股份有限公司.jpg','1411629731060.jpg');

CALL insert_bus_logo('贵阳娃哈哈饮料有限公司','贵阳娃哈哈饮料有限公司.jpg','1411629731169.jpg');

CALL insert_bus_logo('贵阳统一企业有限公司','贵阳统一企业有限公司.png','1411629731278.png');

DROP PROCEDURE IF EXISTS insert_bus_logo;
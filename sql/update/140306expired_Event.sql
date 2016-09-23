CREATE EVENT `expried_event`
ON SCHEDULE EVERY 1 DAY
starts  '2014-03-01 00:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
DO
UPDATE product_certification pc set
 pc.del='0' where pc.enddate <= date_format(curdate(),'YYYY-MM-DD');



SET GLOBAL event_scheduler = ON;
SET @@global.event_scheduler = ON;
SET GLOBAL event_scheduler = 1;
SET @@global.event_scheduler = 1;
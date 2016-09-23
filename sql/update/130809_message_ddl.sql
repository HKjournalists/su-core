ALTER TABLE `message`  ADD COLUMN `receiver_id` BIGINT NULL AFTER `create_date`,  
ADD COLUMN `receiver_type` INT NULL AFTER `receiver_id`;
ALTER TABLE `message`  DROP COLUMN `type`;
ALTER TABLE product_certification
 
 ADD COLUMN enddate varchar(50)
 NULL DEFAULT '' AFTER document_url;

ALTER TABLE product_certification
 ADD COLUMN del int(10)
 NULL DEFAULT '1' AFTER enddate; 


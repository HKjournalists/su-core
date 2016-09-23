create view expired as select `pc`.`Id` AS `Id`,`c`.`name` AS `cname`,`p`.`name` AS `pname`,`pc`.`document_url` AS `document_url`,`pc`.`enddate` AS `enddate` from (`certification` `c` left join (`product` `p` join `product_certification` `pc`
 on((`pc`.`product_id` = `p`.`id`))) 
on((`c`.`id` = `pc`.`cert_id`))) 
where (pc.del=0) 

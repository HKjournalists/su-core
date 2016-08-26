/**
 * author :wubiao
 * date: 2015.12.08 15:31
 * description:旧数据product_instance表中之前没有添加过期日期，因此运行如下update语句后，跟新数据
 */
UPDATE product_instance a,product b SET a.expiration_date =DATE_ADD(a.production_date,INTERVAL b.expiration_date  DAY) 
WHERE a.product_id=b.id ;
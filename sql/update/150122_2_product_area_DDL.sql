CREATE TABLE `product_area` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`code`  varchar(10) NOT NULL ,
`display_name`  varchar(200) NOT NULL ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into product_area(code,display_name) values('01','果蔬区');
insert into product_area(code,display_name) values('02','精品蔬果区');
insert into product_area(code,display_name) values('03','散装食品');
insert into product_area(code,display_name) values('04','休闲食品');
insert into product_area(code,display_name) values('05','包点');
insert into product_area(code,display_name) values('06','熟食');
insert into product_area(code,display_name) values('07','酒');
insert into product_area(code,display_name) values('08','干货');
insert into product_area(code,display_name) values('09','鲜肉');
insert into product_area(code,display_name) values('10','活鲜（海鲜、水产品）');
insert into product_area(code,display_name) values('11','冷藏');
insert into product_area(code,display_name) values('12','面包');
insert into product_area(code,display_name) values('13','特产');
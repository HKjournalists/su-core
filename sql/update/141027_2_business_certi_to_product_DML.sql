CREATE TABLE `business_certification_to_product` (
`product_id`  bigint(20) NOT NULL COMMENT '产品id' ,
`business_cert_id`  bigint(20) NOT NULL COMMENT '企业认证信息id' ,
PRIMARY KEY (`product_id`, `business_cert_id`),
CONSTRAINT `fk_proId` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `fk_bus_cert_id` FOREIGN KEY (`business_cert_id`) REFERENCES `business_certification` (`Id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);
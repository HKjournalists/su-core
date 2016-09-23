CREATE TABLE `prolicinfo_qs_applicant_claim` (
`id`  bigint UNSIGNED NOT NULL AUTO_INCREMENT ,
`qs_id_applicant_claim_backup`  bigint UNSIGNED NOT NULL ,
`business_id`  bigint UNSIGNED NOT NULL ,
`applicant`  varchar(50) NOT NULL DEFAULT '' COMMENT '申请人' ,
`applicant_time`  datetime NOT NULL COMMENT '申请时间' ,
`handler`  varchar(50) NOT NULL DEFAULT '' COMMENT '处理人' ,
`handle_time`  datetime NULL COMMENT '处理时间' ,
`handle_result`  tinyint NOT NULL DEFAULT 1 COMMENT '处理结果 1代表待审核 2代表审核通过 4代表未通过审核 8代表该申请已经过期' ,
`note`  varchar(200) NOT NULL DEFAULT '' ,
PRIMARY KEY (`id`)
);
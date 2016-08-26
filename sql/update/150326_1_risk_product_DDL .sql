ALTER TABLE `product`
ADD COLUMN `riskIndex`  double(50,2) NULL COMMENT '风险指数' AFTER `package_flag`,
ADD COLUMN `riskIndex_Date`  date NULL COMMENT '风险指数的计算时间' AFTER `riskIndex`,
ADD COLUMN `risk_succeed`  bit(1) NULL COMMENT '风险指数的计算成功:0：失败、1：成功' AFTER `riskIndex_Date`,
ADD COLUMN `test_property_name`  varchar(1000) NULL COMMENT '风险指数计算相关的检测项目名称的字符串' AFTER `risk_succeed`,
ADD COLUMN `risk_failure`  varchar(500) NULL COMMENT '风险指数计算失败的原因'  AFTER `test_property_name`;
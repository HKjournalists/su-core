DELETE FROM `test_result`;

INSERT INTO `test_result` VALUES ('36', 'Pass', '1', '2013-08-29 15:48:24', null, '3', null, null, null, '2', '1', '抽样地点', '2013-09-22 18:35:22', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'testerb', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('37', 'Pass', '1', '2013-08-29 15:48:24', null, '3', null, null, null, '152', '12', '抽样地点2', '2013-09-23 15:37:41', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'system', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('38', 'Pass', '1', '2013-08-29 15:48:24', null, '3', null, null, null, '36', '1', '抽样地点', '2013-09-23 15:39:15', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'system', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('42', 'Pass', '1', '2013-08-29 15:48:24', null, '2', null, null, null, '196', '12', '抽样地点2', '2013-09-22 15:11:51', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'testerb', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('43', 'Pass', '1', '2013-08-29 15:48:24', null, '2', null, null, null, '65', '12', '抽样地点2', '2013-09-22 15:11:51', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'testerc', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('44', 'Pass', '1', '2013-08-29 15:48:24', null, '2', null, null, null, '195', '1', '抽样地点', '2013-09-26 14:07:20', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'system', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('45', 'Pass', '1', '2013-09-12 15:16:11', null, '4', null, null, null, '169', '1', '', '2013-09-26 14:13:04', '', '', 'SB 10336-2000 配制酱油', '合格', 'system', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('46', 'Pass', '1', '2013-08-29 15:48:24', null, '4', null, null, null, '195', '12', '抽样地点2', '2013-09-26 15:10:34', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'system', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('47', 'Pass', '1', '2013-08-29 15:48:24', null, '4', null, null, null, '123', '12', '抽样地点2', '2013-09-26 15:54:34', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'system', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('48', 'Pass', '1', '2013-08-29 15:48:24', null, '4', null, null, null, '162', '12', '抽样地点2', '2013-09-26 16:35:31', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'system', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('51', 'Pass', '1', '2013-08-29 15:48:24', null, '3', null, null, null, '195', '12', '抽样地点2', '2013-09-26 16:43:21', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'system', 'testera', 'testera', '', null);
INSERT INTO `test_result` VALUES ('52', 'Pass', '1', '2013-08-29 15:48:24', null, '3', null, null, null, '23', '12', '抽样地点2', '2013-09-26 17:10:42', '', '', 'GB 2760-2011 食品添加剂使用卫生标准', '合格', 'system', 'testera', 'testera', '', null);

COMMIT;


DELETE FROM `test_property`;

INSERT INTO `test_property` VALUES ('209', '36', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('210', '36', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('211', '37', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('212', '37', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('213', '37', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('214', '37', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('215', '38', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('216', '38', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('217', '38', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('218', '38', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('231', '42', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('232', '42', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('233', '42', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('234', '42', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('235', '43', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('236', '43', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('237', '43', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('238', '43', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('239', '44', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('240', '44', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('241', '44', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('242', '44', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('243', '45', null, '罂粟壳(粉)', 'qqq', '', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('244', '46', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('245', '46', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('246', '46', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('247', '46', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('248', '47', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('249', '47', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('250', '47', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('251', '47', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('252', '48', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('253', '48', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('254', '48', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('255', '48', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('264', '51', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('265', '51', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('266', '51', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('267', '51', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('268', '52', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('269', '52', null, '糖精钠', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('270', '52', null, '氯化钾', '456546', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');
INSERT INTO `test_property` VALUES ('271', '52', null, '苯甲酸', '3423', 'mg/kg', '', '合格', 'GB/T 2222.1 - 2011');

COMMIT;
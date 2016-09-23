DELETE FROM `t_sys_role`;

INSERT INTO `t_sys_role` VALUES (1, '客户', 'zh_CN', 'Backup. en_US: Customer');
INSERT INTO `t_sys_role` VALUES (2, '业务员', 'zh_CN', 'Backup. en_US: Sales Engnieer');
INSERT INTO `t_sys_role` VALUES (3, '业务经理', 'zh_CN', 'Backup. en_US: Sales Manager');
INSERT INTO `t_sys_role` VALUES (4, '试验场所负责人', 'zh_CN', 'Backup. en_US: TSM');
INSERT INTO `t_sys_role` VALUES (5, '项目负责人', 'zh_CN', 'Backup. en_US: SD');
INSERT INTO `t_sys_role` VALUES (6, '质量管理负责人', 'zh_CN', 'Backup. en_US: QAM');
INSERT INTO `t_sys_role` VALUES (7, '质量保证人员', 'zh_CN', 'Backup. en_US: QA');
INSERT INTO `t_sys_role` VALUES (8, '实验人员', 'zh_CN', 'Backup. en_US: Tester');

COMMIT;
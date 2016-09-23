UPDATE `t_sys_user` SET ORGANIZATION_NAME='A', REGISTER_DATE='2013-05-01 00:00:00', LAST_LOGIN_DATE=NULL WHERE USER_ID=1;


-- Not very necessary needed, waiting for the formal data from these section...
INSERT INTO `t_sys_organization` VALUES ('A', '山东', '山东A站');
INSERT INTO `t_sys_organization` VALUES ('A.B', '安徽', '安徽A.B站');
INSERT INTO `t_sys_organization` VALUES ('A.B.C', '四川', '四川A.B.C站');
INSERT INTO `t_sys_organization` VALUES ('E.F.G', '火星', '火星E.F.G站');

INSERT INTO `t_sys_permission` VALUES (1, '权限1');
INSERT INTO `t_sys_permission` VALUES (2, '权限2');
INSERT INTO `t_sys_permission` VALUES (3, '权限3');

INSERT INTO `t_sys_role` VALUES (1, '角色1');
INSERT INTO `t_sys_role` VALUES (2, '角色2');
INSERT INTO `t_sys_role` VALUES (3, '角色3');

INSERT INTO `t_sys_user_role` VALUES (1, 1);
INSERT INTO `t_sys_user_role` VALUES (1, 2);
INSERT INTO `t_sys_user_role` VALUES (2, 3);

INSERT INTO `t_sys_role_permission` VALUES (1, 1);
INSERT INTO `t_sys_role_permission` VALUES (1, 2);
INSERT INTO `t_sys_role_permission` VALUES (2, 3);
INSERT INTO `t_sys_role_permission` VALUES (1, 3);

INSERT INTO `t_sys_user` VALUES (2, 'tester', 'tester', 'A.B.C', '2013-05-14 00:00:00', NULL);

COMMIT;

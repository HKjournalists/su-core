ALTER TABLE `test_result`
ADD COLUMN `mk_publish_flag`  bit(1) NOT NULL DEFAULT b'0' COMMENT 'easy���淢����־' AFTER `receiveDate`,
ADD COLUMN `back_flag`  bit(1) NOT NULL DEFAULT b'0' COMMENT '�˻ر�־' AFTER `mk_publish_flag`,
ADD COLUMN `back_result`  varchar(255) NULL COMMENT '�˻�ԭ��' AFTER `back_flag`,
ADD COLUMN `last_modify_user`  varchar(20) NULL COMMENT '��������' AFTER `back_result`,
ADD COLUMN `last_modify_time`  datetime NULL COMMENT '������ʱ��' AFTER `last_modify_user`,
ADD COLUMN `tips`  varchar(255) NULL COMMENT '��ʾ��Ϣ' AFTER `organization`,
ADD COLUMN `back_time`  datetime NULL COMMENT '�˻�ʱ��' AFTER `back_result`,
ADD COLUMN `test_orgnization`  varchar(100) NULL COMMENT '�������' AFTER `tips`,
ADD COLUMN `mk_publish_time`  datetime NULL COMMENT 'easy����ʱ��' AFTER `mk_publish_flag`,
ADD COLUMN `auto_report_flag`  bit(1) NULL DEFAULT b'0' COMMENT '�Զ����ɱ����־' AFTER `test_orgnization`,
ADD COLUMN `upload_path`  varchar(100) NULL AFTER `auto_report_flag`,
ADD COLUMN `mk_db_flag`  char(1) NULL DEFAULT '3' COMMENT '1:���̣�2:lims 2.0 , 3:easy¼������' AFTER `upload_path`;
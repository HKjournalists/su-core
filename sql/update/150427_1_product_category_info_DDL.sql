ALTER TABLE `product_category_info`
ADD COLUMN `del`  tinyint NOT NULL DEFAULT 0 COMMENT '�Ƿ�ɾ���ı�� 0:δɾ�� 1:��ɾ��' AFTER `addition`;

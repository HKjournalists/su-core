ALTER TABLE `product_category_info`
ADD COLUMN `del`  tinyint NOT NULL DEFAULT 0 COMMENT '是否删除的标记 0:未删除 1:已删除' AFTER `addition`;

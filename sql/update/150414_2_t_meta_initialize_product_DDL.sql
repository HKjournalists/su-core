ALTER TABLE `t_meta_initialize_product`
ADD COLUMN `del`  bit(1) NOT NULL DEFAULT b'0' AFTER `organization`;
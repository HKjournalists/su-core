ALTER TABLE `t_meta_receivingnote` ADD CONSTRAINT `t_meta_receivingnote_fk_1` FOREIGN KEY (`re_provide_num`) REFERENCES `business_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;


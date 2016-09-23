ALTER TABLE `deal_problem`
ADD COLUMN `user_id`  bigint NOT NULL DEFAULT '-1' COMMENT '发送投诉的人，不必传，不传默认给-1' AFTER `s_code`;


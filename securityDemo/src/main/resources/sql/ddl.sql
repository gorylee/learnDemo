-- learn_demo.learn_user definition

CREATE TABLE `learn_user` (
      `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `email` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
      `user_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
      `password` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
      `role` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色',
      `state` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
      `sex` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '性别',
      `source` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT 'REGISTER' COMMENT '来源',
      `avatar` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '头像',
      `signature` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '个人简介',
      `ext` blob COMMENT '扩展信息',
      `is_delete` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识（0:未删除、1:已删除）',
      `create_at` datetime NOT NULL COMMENT '记录创建时间',
      `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
      PRIMARY KEY (`id`),
      UNIQUE KEY `uniq_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
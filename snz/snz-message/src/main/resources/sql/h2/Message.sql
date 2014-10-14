-- -----------------------------------------------------
-- Table `snz_messages`    消息表
-- -----------------------------------------------------
CREATE TABLE `snz_messages` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主健',
  `user_id` bigint(20) NOT NULL COMMENT '创建者',
  `content` varchar(100) NOT NULL DEFAULT '' COMMENT '内容',
  `mtype` smallint(6) NOT NULL COMMENT '类型，1话题, 2回复,...',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);
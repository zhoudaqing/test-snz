-- -----------------------------------------------------
-- Table `snz_topics`    话题信息表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_topics`;

CREATE TABLE `snz_topics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `req_id` bigint(20) NOT NULL COMMENT '需求ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(80) DEFAULT NULL COMMENT '用户名',
  `company_name` varchar(64) NOT NULL COMMENT '冗余公司名',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `content` varchar(1024) DEFAULT NULL COMMENT '内容',
  `req_status` smallint(6) NOT NULL COMMENT '状态，同需求状态',
  `scope` smallint(4) NOT NULL COMMENT '话题范围（0：公开，1：圈子内）',
  `joiners` int(11) DEFAULT NULL COMMENT '圈子人数',
  `files` varchar(255) DEFAULT NULL COMMENT '附件',
  `last_replier_id` bigint(20) DEFAULT NULL COMMENT '最后回复人的编号',
  `last_replier_name` varchar(45) DEFAULT '' COMMENT '最后回复人名称',
  `total_views` int(11) DEFAULT '0' COMMENT '冗余浏览总数',
  `total_replies` int(11) DEFAULT '0' COMMENT '冗余回复总数',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志,0未删除,1删除',
  `closed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '关闭标志,0未关闭,1关闭',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `req_name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `snz_user_topics`    用户与话题中间表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_users_topics`;

CREATE TABLE `snz_users_topics` (
  `topic_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL
);
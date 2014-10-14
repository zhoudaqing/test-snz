-- -----------------------------------------------------
-- Table `snz_replies`    话题回复信息表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_replies`;
CREATE TABLE `snz_replies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '创建用户id',
  `receiver_id` bigint(20) DEFAULT NULL COMMENT '被回复的用户id, 顶级回复时值为0',
  `receiver_name` varchar(128) null COMMENT '被回复人的名字',
  `req_id` bigint(20) DEFAULT NULL COMMENT '需求id',
  `topic_id` bigint(20) NOT NULL COMMENT '话题id',
  `tid` bigint(20) DEFAULT NULL COMMENT '顶级回复id',
  `pid` bigint(20) DEFAULT NULL COMMENT '父级回复id, 0表示一级回复',
  `company_name` varchar(64) NOT NULL COMMENT '冗余公司名',
  `content` varchar(1024) NOT NULL DEFAULT '' COMMENT '内容',
  `files` varchar(255) DEFAULT NULL COMMENT '附件',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `snz_replies` (`id`, `user_id`, `receiver_id`, `receiver_name`, `req_id`, `topic_id`, `tid`, `pid`, `company_name`, `content`, `files`, `created_at`, `updated_at`)
VALUES
	(87, 2963, NULL, NULL, 313, 79, 0, 0, 'A0004319', '具体需求', NULL, '2014-08-20 19:17:40', '2014-08-20 19:17:40'),
	(89, 2962, NULL, NULL, 313, 79, 0, 0, 'A0004319', '', '{\"name\":\"\\u6f14\\u793a\\u6587\\u7a3f2.ppt\",\"url\":\"/assets/2014/08/20/2961_bb7ee316847e3354cb8c3427eb62b43d.ppt\"}', '2014-08-20 19:18:00', '2014-08-20 19:18:00'),
	(91, 2961, NULL, NULL, 313, 79, 0, 87, 'A0004319', '', '{\"name\":\"360012F05-\\u7cfb\\u7edf\\u63a5\\u53e3\\u7528\\u6237\\u90ae\\u7bb1\\u7533\\u8bf7\\u8868-20131230.doc\",\"url\":\"/assets/2014/08/20/2961_79163b8dda72ba958bef4cdd60c7246e.doc\"}', '2014-08-20 19:19:40', '2014-08-20 19:19:40'),
	(93, 2954, NULL, NULL, 313, 79, 0, 87, '杭州端点网络科技有限公司', '我的方案', NULL, '2014-08-20 19:23:54', '2014-08-20 19:23:54'),
	(95, 2954, 2961, '小李', 313, 79, 0, 87, '杭州端点网络科技有限公司', '什么？', NULL, '2014-08-20 19:24:15', '2014-08-20 19:24:15'),
	(101, 2961, 2961, '小刚', 313, 79, 0, 87, '杭州端点网络科技有限公司', '测试0822', NULL, '2014-08-22 14:21:24', '2014-08-22 14:21:24');



-- 用户
-- -----------------------------------------------------
-- Table `snz`.`snz_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_users` ;

CREATE TABLE IF NOT EXISTS `snz_users` (
  `id`                  BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `outer_id`            BIGINT(20)    NULL                    COMMENT '外部用户id',
  `name`                VARCHAR(20)   NULL                    COMMENT '用户真实姓名',
  `nick`                VARCHAR(80)   NOT NULL                COMMENT '昵称',
  `encrypted_password`  VARCHAR(32)   NOT NULL                COMMENT '加密密码',
  `mobile`              VARCHAR(11)   NOT NULL                COMMENT '手机号',
  `phone`               VARCHAR(12)   NULL                    COMMENT '固定电话',
  `email`               VARCHAR(50)   NOT NULL                COMMENT '邮箱',
  `type`                SMALLINT      NOT NULL                COMMENT '用户类型（0:游客，1:管理员,2:供应商，3：采购商）',
  `role_str`            VARCHAR(100)  NOT NULL                COMMENT '角色（字符串形式）, 比如采购商，供应商，管理员等',
  `account_type`        SMALLINT      NULL                    COMMENT '账号类型, 1:制造商，2：渠道商',
  `status`              SMALLINT      NOT NULL                COMMENT '状态（-1:已冻结,0:正常）',
  `approve_status`      SMALLINT      NULL                    COMMENT '审核状态,-1:审核不通过，0：未触发审核，1:等待审核，2：审核通过',
  `origin`              SMALLINT      NULL                    COMMENT '用户来源,0：正常入驻，1:百卓接入，2：云平台导入',
  `refuse_status`       SMALLINT      NULL                    COMMENT '审核拒绝状态,0：没有被拒绝过，-1:被拒绝过',
  `qualify_status`      SMALLINT      NULL                    COMMENT '资质校验状态',
  `tags`                VARCHAR(512)  NULL                    COMMENT '用户标签',
  `last_login_at`       DATETIME      NULL                    COMMENT '最近登录时间',
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NULL                    COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
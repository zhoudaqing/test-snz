-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users` ;

CREATE  TABLE IF NOT EXISTS `users` (
  `id`            BIGINT        NOT NULL  AUTO_INCREMENT,
  `name`        VARCHAR(64)       NOT NULL      COMMENT '用户登陆名称',
  `password` VARCHAR(127)   NULL      COMMENT '密码',
  `status`        SMALLINT      NULL      COMMENT '用户状态 ，1：正常，，-1：禁用' ,
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `real_name`     VARCHAR(32)         NULL      COMMENT '真实姓名' ,
  `login_at`       DATETIME      NULL      COMMENT '上次登陆日期',
  `created_at`       DATETIME      NULL      COMMENT '创建日期',
  `updated_at`       DATETIME      NULL      COMMENT '更新日期',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_users_name_UNIQUE` (`name` ASC));

-- -----------------------------------------------------
-- Table `user_permissions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_permissions` ;

CREATE  TABLE IF NOT EXISTS `user_permissions` (
  `id`            BIGINT        NOT NULL  AUTO_INCREMENT,
  `user_id`        BIGINT        NOT NULL      COMMENT '用户id',
  `permissions`   VARCHAR(4096)     NULL      COMMENT '以json形式存储的授权列表' ,
  `created_at`       DATETIME      NULL      COMMENT '创建日期',
  `updated_at`       DATETIME      NULL      COMMENT '更新日期',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_ups_user_id_UNIQUE` (`user_id` ASC));
CREATE TABLE `snz_factories_organs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `factory` varchar(4) DEFAULT NULL COMMENT '工厂',
  `organ` varchar(4) DEFAULT NULL COMMENT '组织',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `snz_address_park`    园区信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_address_park`;

CREATE TABLE IF NOT EXISTS `snz_address_park` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`park_name`           VARCHAR(10) NOT NULL  COMMENT '园区名称',
`created_at`          DATETIME    NULL      COMMENT '创建时间',
`updated_at`          DATETIME    NULL      COMMENT '更改时间',
PRIMARY KEY (`id`));

-- -----------------------------------------------------
-- Table `snz_address_factory`  工厂信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_address_factory`;

CREATE TABLE IF NOT EXISTS `snz_address_factory` (
  `id`                BIGINT          NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `factory_num`       VARCHAR(10)     NOT NULL  COMMENT '工厂代码',
  `factory_name`      VARCHAR(50)     NOT NULL  COMMENT '工厂名称',
  `created_at`        DATETIME        NULL      COMMENT '创建时间',
  `updated_at`        DATETIME        NULL      COMMENT '更改时间',
  PRIMARY KEY (`id`));

-- -----------------------------------------------------
-- Table `snz_category_factory`  类目，工厂关系
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pp`;

CREATE TABLE IF NOT EXISTS `snz_category_factory` (
  `id`                BIGINT          NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `product_id`        BIGINT          NOT NULL  COMMENT '产品类型（二级类目）',
  `park_id`           BIGINT          NOT NULL  COMMENT '园区编号',
  `factory_id`        BIGINT          NOT NULL  COMMENT '工厂Id',
  `created_at`        DATETIME        NULL      COMMENT '创建时间',
  `updated_at`        DATETIME        NULL      COMMENT '更改时间',
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `snz_factory_production_directors`  工厂总监
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_factory_production_directors`;

CREATE TABLE IF NOT EXISTS `snz_factory_production_directors` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`factory_num`         VARCHAR(10)   NOT NULL COMMENT '工厂代码',
`description`         VARCHAR(50)   NOT NULL COMMENT '工厂描述',
`product_line_id`     BIGINT        NOT NULL COMMENT '产品线id',
`product_line_name`   VARCHAR(20)   NULL     COMMENT '产品线名称',
`director_id`         VARCHAR(80)   NOT NULL COMMENT '总监工号',
`director_name`       VARCHAR(20)   NOT NULL COMMENT '总监名',
`created_at`          DATETIME      NULL     COMMENT '创建时间',
`updated_at`          DATETIME      NULL     COMMENT '更改时间',
PRIMARY KEY (`id`)
);

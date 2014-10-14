-- -----------------------------------------------------
-- Table `snz_frontend_categorys`    前台类目
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_frontend_categorys`;

CREATE TABLE IF NOT EXISTS `snz_frontend_categorys` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`name`                VARCHAR(32) NOT NULL  COMMENT '类目名称',
`parent_id`           BIGINT      NOT NULL  COMMENT '上级类目id,如果是根类目, 则为0',
`level`               SMALLINT    NOT NULL  COMMENT '类目层级, 从1开始',
`has_children`        BOOLEAN     NOT NULL  COMMENT '是否有下级类目',
`created_at`          DATETIME    NULL      COMMENT '创建时间',
`updated_at`          DATETIME    NULL      COMMENT '修改时间',
PRIMARY KEY (`id`));

CREATE INDEX snz_fc_parent_id ON snz_frontend_categorys (parent_id);

-- -----------------------------------------------------
-- Table `snz_backend_categorys`    后台类目
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_backend_categorys`;

CREATE TABLE IF NOT EXISTS `snz_backend_categorys` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`name`                VARCHAR(32) NOT NULL  COMMENT '类目名称',
`parent_id`           BIGINT      NOT NULL  COMMENT '上级类目id,如果是根类目, 则为0',
`level`               SMALLINT    NOT NULL  COMMENT '类目层级, 从1开始',
`has_children`        BOOLEAN     NOT NULL  COMMENT '是否有下级类目',
`created_at`          DATETIME    NULL      COMMENT '创建时间',
`updated_at`          DATETIME    NULL      COMMENT '修改时间',
PRIMARY KEY (`id`));

CREATE INDEX snz_bc_parent_id ON snz_backend_categorys (parent_id);


-- -----------------------------------------------------
-- Table `snz_backend_category_properties`    后台类目属性
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_backend_category_properties`;

CREATE TABLE IF NOT EXISTS `snz_backend_category_properties` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `bc_id`       BIGINT       NOT NULL COMMENT '后台类目id',
  `name`        VARCHAR(128) NULL COMMENT '属性名',
  `type`        VARCHAR(128) NULL COMMENT '属性类型',
  `factory_num` VARCHAR(10)  NULL COMMENT '工厂编号',
  `value1`      VARCHAR(128) NULL COMMENT '属性值1',
  `value2`      VARCHAR(128) NULL COMMENT '属性值2',
  `value3`      VARCHAR(128) NULL COMMENT '属性值3',
  `created_at`  DATETIME     NULL COMMENT '创建时间',
  `updated_at`  DATETIME     NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_bc_properties_bc_id` ON `snz_backend_category_properties` (`bc_id`);


-- -----------------------------------------------------
-- Table `snz_category_bindings`    类目绑定
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_category_bindings`;

CREATE TABLE IF NOT EXISTS `snz_category_bindings` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`front_id`            BIGINT      NOT NULL  COMMENT '前台类目id',
`bcs`                 VARCHAR(2048)NOT NULL  COMMENT '以json存储的后台类目id和name集合',
`created_at`          DATETIME    NULL      COMMENT '创建时间',
`updated_at`          DATETIME    NULL      COMMENT '修改时间',
PRIMARY KEY (`id`));

CREATE INDEX snz_cb_front_id ON snz_category_bindings (front_id);

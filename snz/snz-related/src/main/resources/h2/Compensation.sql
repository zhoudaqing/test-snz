CREATE TABLE `snz_compensations` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `park` varchar(20) DEFAULT NULL COMMENT '园区',
  `product_line` varchar(20) DEFAULT NULL COMMENT '产品线',
  `supplier_account` varchar(20) DEFAULT NULL COMMENT '供应商账号',
  `supplier_name` varchar(35) DEFAULT NULL COMMENT '供应商名称',
  `is_internal` tinyint(1) DEFAULT NULL COMMENT '内外部供应商标志, 0内部, 1外部',
  `factory` varchar(4)  DEFAULT NULL COMMENT '工厂',
  `factory_name` varchar(30) DEFAULT NULL COMMENT '工厂方法',
  `materiel_no` varchar(18) DEFAULT NULL COMMENT '物料号',
  `materiel_desc` varchar(40) DEFAULT NULL COMMENT '物料描述',
  `purchaser_group` varchar(3) DEFAULT NULL COMMENT '采购商组',
  `purchaser_group_desc` varchar(40) DEFAULT NULL COMMENT '采购商组描述',
  `score` int(11) DEFAULT NULL COMMENT '扣分',
  `is_money` tinyint(1) DEFAULT NULL COMMENT '需要扣款标志, 0不需要, 1需要',
  `money` int(11) DEFAULT NULL COMMENT '扣除金额',
  `deducted_at` date DEFAULT NULL COMMENT '扣款日期',
  `current` date DEFAULT NULL COMMENT '当前日期',
  `status` tinyint(1) DEFAULT NULL COMMENT '申诉状态',
  `result` varchar(200)  DEFAULT NULL COMMENT '扣款结果',
  `entered_at` date DEFAULT NULL COMMENT '凭证入账日期',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `snz_compensation_details`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `snz_compensation_details`;

CREATE TABLE IF NOT EXISTS `snz_compensation_details` (
`id`                    bigint(20)        NOT NULL AUTO_INCREMENT   COMMENT '自增主键',
`list_id`               bigint(20)        DEFAULT NULL              COMMENT '列表索赔id',
`list_num`              varchar(20)       DEFAULT NULL              COMMENT '订单号',
`factory`               varchar(4)        DEFAULT NULL              COMMENT '工厂',
`num_of_proof`          varchar(20)       DEFAULT NULL              COMMENT '采购凭证号',
`num_of_param`          varchar(20)       DEFAULT NULL              COMMENT '采购凭证的项目编号',
`supplier_account`      varchar(20)       DEFAULT NULL              COMMENT '供应商标识',
`materiel_no`           varchar(18)       DEFAULT NULL              COMMENT '物料号',
`time_of_delivery`      datetime          DEFAULT NULL              COMMENT '应交货日期',
`r_time_of_delivery`    datetime          DEFAULT NULL              COMMENT '实际交货日期',
`y_time_of_delivery`    datetime          DEFAULT NULL              COMMENT '预约交货日期',
`num_of_list`           int(11)           DEFAULT NULL              COMMENT '采购订单数量',
`num_of_delivery`       int(11)           DEFAULT NULL              COMMENT '实际收货数量',
`num_of_difference`     int(11)           DEFAULT NULL              COMMENT '差异数量',
`desc_of_difference`    varchar(1024)     DEFAULT NULL              COMMENT '交易差异原因',
`current_day`           datetime          DEFAULT NULL              COMMENT '当前日期',
`current_time`          datetime          DEFAULT NULL              COMMENT '当前时间',
`created_at`            datetime          DEFAULT NULL              COMMENT '创建时间',
`updated_at`            datetime          DEFAULT NULL              COMMENT '更新时间',
PRIMARY KEY (`id`)
);
CREATE INDEX `snz_compensation_details_id_idx` ON `snz_compensation_details` (`id`);

-- -----------------------------------------------------
-- Table `snz_compensation_replies`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `snz_compensation_replies`;

CREATE TABLE IF NOT EXISTS `snz_compensation_replies` (
`id`                    bigint(20)        NOT NULL AUTO_INCREMENT   COMMENT '自增主键',
`user_id`               bigint(20)        DEFAULT NULL              COMMENT '用户id',
`list_id`               bigint(20)        DEFAULT NULL              COMMENT '索赔记录id',
`content`               varchar(4000)     DEFAULT NULL              COMMENT '会话内容',
`company_name`          varchar(64)       DEFAULT NULL              COMMENT '公司名',
`files`                 varchar(1024)     DEFAULT NULL              COMMENT '附件url列表',
`created_at`            datetime          DEFAULT NULL              COMMENT '创建时间',
`updated_at`            datetime          DEFAULT NULL              COMMENT '更新时间',
PRIMARY KEY (`id`)
);
CREATE INDEX `snz_compensation_replies_id_idx` ON `snz_compensation_replies` (`id`);
CREATE INDEX `snz_compensation_replies_did_idx` ON `snz_compensation_replies` (`list_id`);

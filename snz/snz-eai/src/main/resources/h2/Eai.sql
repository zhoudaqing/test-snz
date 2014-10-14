CREATE TABLE `mdm_configures` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `mdm_bank_views` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `code` varchar(64) DEFAULT NULL COMMENT '银行代号',
  `name` varchar(255) DEFAULT NULL COMMENT '银行名字',
  `country` varchar(64) DEFAULT NULL COMMENT '银行国家',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8;


CREATE TABLE `mw_quota_temp_infos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT ,
  `module_num` varchar(32) DEFAULT NULL COMMENT '模块专用号、物料号',
  `module_name` varchar(255) DEFAULT NULL COMMENT '物料描述',
  `factory_num` varchar(32) DEFAULT NULL COMMENT '工厂',
  `matkl` varchar(32) DEFAULT NULL COMMENT ' ',
  `supplier_id` varchar(32) NOT NULL COMMENT '供应商或债权人的帐号',
  `supplier_name` varchar(32) NOT NULL COMMENT '供应商名称',
  `quantity` int(11) DEFAULT NULL COMMENT '配额',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `mw_old_module_infos` (
  `id` bigint NOT NULL auto_increment ,
  `module_num` varchar(32) NOT NULL COMMENT '模块专用号、物料号',
  `module_name` varchar(32) NOT NULL COMMENT '模块描述',
  `series_id` int(11) DEFAULT NULL COMMENT '三级类目名',
  `unit` varchar(32) NOT NULL COMMENT '资源量单位',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `mw_price_temp_infos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT ,
  `module_num` varchar(32) DEFAULT NULL COMMENT '模块专用号、物料号',
  `module_name` varchar(255) DEFAULT NULL COMMENT '物料描述',
  `matkl` varchar(32) DEFAULT NULL COMMENT ' ',
  `supplier_id` varchar(32) NOT NULL COMMENT '供应商或债权人的帐号',
  `supplier_name` varchar(32) NOT NULL COMMENT '供应商名称',
  `purchase_org` varchar(32) DEFAULT NULL COMMENT '采购组织',
  `purchase_type` varchar(32) DEFAULT NULL COMMENT '采购信息记录分类',
  `scale` int(11) DEFAULT NULL COMMENT '价格( 条件金额或百分数 )',
  `fee_unit` int(11) DEFAULT NULL COMMENT '条件定价单位',
  `purchase_unit` varchar(32) DEFAULT NULL COMMENT '条件单位',
  `coin_type` varchar(32) DEFAULT NULL COMMENT '货币',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
);

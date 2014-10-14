## 删除snz_companies表的status字段
alter table snz_companies drop column status;

## 删除snz_service_infos表的customers字段
alter table snz_service_infos drop column customers;

## 向snz_companies表添加customers字段
 alter TABLE `snz_companies`
 add `customers` VARCHAR(100) NOT NULL COMMENT '客户群' after `participate_count`;


## 删除snz_users表的type字段
alter table snz_users drop column type;

## 向snz_users表添加role_str字段
 alter TABLE `snz_users`
 add `role_str` VARCHAR(100) NOT NULL COMMENT '角色' after `email`;

## 向snz_users表添加type字段
 alter TABLE `snz_users`
 add `type` SMALLINT NOT NULL COMMENT '用户类型' after `email`;

## 修改snz_service_infos表
 alter TABLE `snz_service_infos`
 change column inland_rank in_rank INT NULL COMMENT '国内排名';

## 向snz_users表添加certificate_status字段
 alter TABLE `snz_users`
 add `certificate_status` SMALLINT NULL COMMENT '三证审核状态' after `status`;

## 修改snz_companies表
 alter TABLE `snz_companies`
 change column business_license business_license VARCHAR(100) NULL COMMENT '营业执照图片';

 alter TABLE `snz_companies`
 change column org_cert org_cert VARCHAR(100) NULL COMMENT '组织机构证号图片';

 alter TABLE `snz_companies`
 change column tax_no tax_no VARCHAR(100) NULL COMMENT '税务登记证号图片';

 alter TABLE `snz_companies`
 change column bl_date bl_date DATETIME NULL  COMMENT '营业执照有效期';

 alter TABLE `snz_companies`
 change column oc_date oc_date DATETIME NULL COMMENT '组织机构证有效期';

 alter TABLE `snz_companies`
 change column tn_date tn_date DATETIME NULL COMMENT '税务登记证有效期';

## 修改snz_users表
 alter table `snz_users`
 change column certificate_status approve_status SMALLINT NULL COMMENT '审核状态'

## 向snz_supplier_tqrdc_infos表添加字段
 alter TABLE `snz_supplier_tqrdc_infos`
 add `tech_score_rank` INT  NULL  COMMENT '技术得分排序' after `increment`;

 alter TABLE `snz_supplier_tqrdc_infos`
 add `quality_score_rank` INT  NULL  COMMENT '质量得分排序' after `tech_score_rank`;

 alter TABLE `snz_supplier_tqrdc_infos`
 add `resp_score_rank`  INT  NULL  COMMENT '响应得分排序' after `quality_score_rank`;

 alter TABLE `snz_supplier_tqrdc_infos`
 add `delivery_score_rank` INT  NULL  COMMENT '交付得分排序' after `resp_score_rank`;

 alter TABLE `snz_supplier_tqrdc_infos`
 add `cost_score_rank`  INT  NULL  COMMENT '成本得分排序' after `delivery_score_rank`;

## 向snz_companies表添加字段
alter table `snz_companies`
add `supplier_code` varchar(20) NULL COMMENT '供应商代码' after `corp_addr`;

alter table `snz_companies`
add `acting_brand` varchar(20) NULL COMMENT '代理的品牌' after `supplier_code`;

## 修改snz_users表
 alter table `snz_users`
 change column name name varchar(20) NULL COMMENT '用户真实姓名';

 alter table `snz_users`
 change column nick nick varchar(80) NOT NULL COMMENT '昵称';

## 修改snz_companies表
  alter table `snz_companies`
  change column `desc` `desc` varchar(2048) NULL COMMENT '公司简介';

  alter table `snz_companies`
  change column `listed_region` `listed_region` varchar(100) NULL COMMENT '上市地区';

## 工厂信息只提供查询
-- -----------------------------------------------------
-- Table `snz_address_park`    园区信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_address_park`;

CREATE TABLE IF NOT EXISTS `snz_address_park` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`park_name`           VARCHAR(10) NOT NULL  COMMENT '园区名称',
PRIMARY KEY (`id`));
insert into snz_address_park (park_name) values('平度'),('重庆'),('沈阳'),('青岛'),('贵州'),('大连'),('武汉'),('章丘'),('顺德'),('郑州'),('合肥');

-- -----------------------------------------------------
-- Table `snz_address_factory`  工厂信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_address_factory`;

CREATE TABLE IF NOT EXISTS `snz_address_factory` (
  `id`                BIGINT          NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `park_id`           BIGINT          NOT NULL  COMMENT '园区Id',
  `factory_num`        VARCHAR(10)     NOT NULL  COMMENT '工厂代码',
  `factory_name`       VARCHAR(50)     NOT NULL  COMMENT '工厂名称',
  PRIMARY KEY (`id`));
insert into snz_address_factory (park_id , factory_num, factory_name) values(4,'8170','青岛家电工艺装备研究所青岛家电工艺装备厂'),(4,'8171','青岛家电工艺装备研究所成套工厂'),(4,'8172','青岛家电工艺装备研究所平度工厂'),(4,'8180','海尔集团青岛冷凝器工厂'),(2,'818A','海尔集团青岛冷凝器委外工厂 '),(4,'8190','青岛海尔工装研制工厂'),(4,'8191','青岛海尔工装研制工厂成套工厂'),(4,'8200','黄岛海昌泰塑胶工厂'),(11,'8250','合肥海尔塑胶注塑工厂'),(11,'8251','合肥海尔塑胶钣金工厂'),(11,'8252','合肥海尔塑胶工厂成套工厂'),(7,'8253','武汉钣金工厂'),(7,'8254','武汉钣金成套工厂'),(11,'825A','合肥海尔塑胶注塑代工厂'),(11,'825B','合肥海尔塑胶钣金代工厂'),(11,'825C','合肥海尔塑胶工厂成套代工厂'),(11,'825D','武汉钣金代工厂'),(11,'825E','武汉钣金成套代工厂'),(8,'8270','章丘海尔电机有限公司章丘电机工厂'),(4,'8290','青岛海尔信息塑胶研制工厂'),(4,'8291','青岛海尔信息塑胶研制工厂成套工'),(4,'8300','青岛海尔电子塑胶注塑工厂'),(4,'8301','青岛海尔电子塑胶钣金工厂'),(4,'8302','青岛海尔电子塑胶工厂成套工厂'),(4,'8310','青岛海尔智能电子有限公司青岛智能电子工厂'),(4,'8311','青岛海尔智能电子有限公司青岛智能电子成套工厂'),(4,'8312','青岛海尔智能电子有限公司智能电子成套工厂'),(4,'8313','青岛海尔智能电子有限公司武汉智能电子成套工厂'),(4,'8320','青岛海尔机器人工厂'),(4,'8380','青岛海尔新材料研发有限公司新材料(胶州)工厂'),(4,'8400','青岛海尔特种钢板研制开发工厂'),(2,'840A','青岛海尔特种钢板研制开发委外工厂 '),(11,'8410','合肥海尔特种钢板研制开发工厂'),(11,'8411','海尔集团合肥冷凝器工厂'),(11,'841A','合肥海尔特种钢板研制开发工厂 '),(11,'841B','海尔集团合肥冷凝器工厂 '),(6,'8430','大连海尔精密制品工厂'),(4,'8440','青岛美尔塑料粉末工厂'),(4,'8441','青岛美尔塑料板材工厂'),(4,'8620','青岛海尔精密制品工厂'),(4,'8621','青岛海尔精密制品工厂成套工厂'),(2,'8640','重庆零部件家用空调PL工厂'),(2,'8641','重庆零部件波轮洗衣机PL工厂'),(2,'8645','重庆零部件燃气热水器PL工厂'),(2,'8646','重庆零部件商用空调PL工厂'),(2,'8648','重庆零部件冰箱PL工厂'),(2,'8670','重庆智能电子有限公司重庆智能电子工厂'),(2,'8671','重庆智能电子有限公司重庆智能电子成套工厂'),(2,'8672','重庆智能电子冷凝器工厂'),(2,'8673','重庆智能电子海昌泰工厂'),(2,'8690','重庆海尔精密塑胶注塑工厂'),(2,'8691','重庆海尔精密塑胶钣金工厂'),(2,'8692','重庆海尔精密塑胶工厂成套工厂'),(2,'869A','重庆海尔精密塑胶注塑委外工厂'),(2,'869B','重庆海尔精密塑胶钣金委外工厂'),(2,'869C','重庆海尔精密塑胶工厂成套委外工厂'),(2,'869D','武汉钣金工厂'),(2,'869E','重庆海尔精密塑胶注塑委外工厂'),(9,'8700','佛山市顺德海尔智能电子有限公司顺德智能电子工厂'),(9,'8701','顺德部品工厂'),(4,'8710','青岛鼎新电子科技有限公司鼎新电子工厂'),(4,'8711','青岛鼎新电子科技有限公司鼎新电子工厂成套工厂'),(4,'8780','青岛海尔部品胶州注塑分厂'),(4,'8781','青岛海尔部品胶州钣金分厂'),(4,'8782','青岛海尔部品胶州模块分厂'),(4,'9010','青岛海尔股份有限公司冰箱中一工厂'),(4,'9011','青岛海尔股份有限公司冰箱中一工厂'),(1,'9020','青岛海尔电冰箱(国际)有限公司海外冰箱工厂'),(1,'9021','青岛海尔电冰箱(国际)有限公司'),(5,'9030','贵州海尔电器有限公司贵州冰箱工厂'),(4,'9040','青岛海尔电冰箱有限公司冰箱中二工厂'),(4,'9041','青岛海尔电冰箱有限公司白电研发基地'),(4,'9050','青岛海尔特种电冰箱有限公司特种冰箱工厂'),(4,'9051','青岛海尔特种电冰箱有限公司白电研发基地'),(6,'9060','大连海尔电冰箱有限公司大连冰箱外销工厂'),(4,'9090','青岛海尔特种电冰柜有限公司青岛冷柜工厂'),(4,'9091','青岛海尔特种电冰柜有限公司医疗产品工厂'),(4,'9092','青岛海尔特种电冰柜有限公司商用冷柜工厂'),(4,'9093','青岛海尔特种电冰柜有限公司白电研发基地'),(7,'9110','武汉海尔电冰柜有限公司武汉冷柜工厂'),(4,'9120','青岛海尔空调器有限总公司青岛空调工厂'),(6,'9130','大连海尔空调器有限公司大连空调外销工厂'),(6,'9140','大连保税区海尔空调器贸易有限公司大连空调内销工厂'),(11,'9160','合肥海尔空调器有限公司合肥空调工厂'),(11,'916Z','合肥海尔空调器有限公司合肥空调代工厂'),(7,'9170','武汉海尔电器股份有限公司武汉空调工厂'),(4,'9180','青岛海尔空调制冷设备有限公司胶南空调工厂'),(4,'9190','青岛海尔空调电子有限公司商用空调工厂'),(4,'9191','青岛海尔空调电子有限公司中央空调工厂'),(4,'9210','青岛海尔特种塑料研制开发有限公司特塑工厂'),(4,'9220','青岛海尔洗衣机有限公司青岛洗衣机工厂'),(4,'9230','青岛海尔（胶州）空调器有限公司胶州空调工厂'),(11,'9250','合肥海尔洗衣机有限公司合肥洗衣机工厂'),(11,'925Z','合肥海尔洗衣机有限公司合肥洗衣机代工厂'),(9,'9260','佛山市顺德海尔电器有限公司波轮洗衣机工厂'),(9,'9261','佛山市顺德海尔电器有限公司滚筒洗衣机工厂'),(4,'9270','青岛胶南海尔洗衣机有限公司胶南洗衣机工厂'),(7,'9310','武汉海尔热水器有限公司武汉电热工厂'),(4,'9330','青岛海尔电子有限公司电视产品工厂'),(4,'9331','青岛海尔电子有限公司数码产品工厂'),(11,'9350','合肥海尔信息产品有限公司电视产品工厂'),(4,'9360','青岛胶南海尔电子有限公司电视产品工厂'),(4,'9390','青岛海尔特种电器有限公司生物医疗工厂'),(4,'9393','青岛海尔特种电器有限公司生物医疗工厂'),(4,'9400','青岛经济技术开发区海尔热水器有限公司黄岛电热工厂'),(4,'9401','青岛经济技术开发区海尔热水器有限公司黄岛太阳能工厂'),(4,'9402','青岛经济技术开发区海尔热水器有限公司胶南电热工厂'),(4,'9403','青岛经济技术开发区海尔热水器有限公司胶南太阳能工厂'),(4,'9420','青岛海尔洗碗机有限公司洗碗机产品工厂'),(4,'9421','青岛海尔洗碗机有限公司燃气灶产品工厂'),(4,'9422','青岛海尔洗碗机有限公司油烟机产品工厂'),(4,'9520','模具中心青岛工厂'),(2,'9680','重庆海尔空调器有限公司重庆空调工厂'),(2,'968N','重庆海尔空调器有限公司重庆空调委外工厂 '),(4,'9690','青岛海尔中央空调工程有限公司中央空调工厂'),(2,'9700','重庆海尔洗衣机有限公司重庆洗衣机工厂'),(2,'9710','重庆海尔热水器有限公司重庆燃气工厂'),(2,'9720','重庆海尔电子有限公司电视产品工厂'),(4,'9790','海尔盈德喜（青岛）电器有限公司'),(11,'9800','合肥海尔电冰箱有限公司合肥冰箱工厂'),(11,'9808','合肥海尔电冰箱有限公司合肥冰箱二厂'),(11,'980Z','合肥海尔电冰箱有限公司合肥冰箱代工厂'),(4,'9810','青岛海尔光电有限公司'),(2,'9888','重庆海尔制冷电器有限公司重庆冰箱工厂'),(2,'988N','重庆海尔制冷电器有限公司重庆冰箱委外工厂'),(2,'9A10','重庆海尔滚筒洗衣机有限公司'),(2,'9A1N','重庆海尔洗衣机有限公司重庆洗衣机委外工厂'),(3,'9A20','沈阳海尔电冰箱有限公司沈阳工厂'),(9,'9A30','佛山海尔电冰柜有限公司佛山工厂'),(9,'9A80','佛山海尔滚筒洗衣机有限公司工厂'),(10,'9B10','郑州海尔空调器有限公司郑州空调工厂');

## 修改snz_companies表
alter table `snz_companies`
change column `nature` `nature` SMALLINT NULL COMMENT '企业性质(1：国有，2：集体，3：民营，4,私营，5：合资，6：外资，7：其他)';


-- 增加snz_topics冗余列req_name
alter TABLE snz_topics ADD COLUMN req_name VARCHAR(32);

-- 在模块的配额上增加配额的比例
alter table `snz_module_quotas`
add column `scale` INT NULL COMMENT '模块配额的占总配额的比例70,50' after quantity;

## 向snz_company_ranks表添加字段
alter table `snz_company_ranks`
add `in_rank_file_name` VARCHAR(100) NULL COMMENT '国内排名证明附件名称' after `in_rank_file`;

alter table `snz_company_ranks`
add `out_rank_file_name` VARCHAR(100) NULL COMMENT '国际排名附件名称' after `out_rank_file`;

## 修改snz_companies表
alter table `snz_companies` drop column fa_coin_type;

alter table `snz_companies`
change column `fixed_assets` `factory_fixed_assets` bigint(20)  NULL COMMENT '工厂固定资产';

alter table `snz_companies`
add `fixed_assets` bigint(20) NULL COMMENT '公司固定资产' after `init_agent`;

alter table `snz_companies`
add `fa_coin_type` SMALLINT  NULL COMMENT '公司固定资产币种' after `fixed_assets`;

## 为snz_users表的nick字段添加索引
CREATE INDEX `snz_users_nick_idx` ON `snz_users` (`nick`);

## 更改模块方案
ALTER TABLE `snz_module_solutions`
CHANGE COLUMN `cost` `cost` INT(11) NULL COMMENT '成本' ;

## 为snz_companies表添加is_complete字段
alter table `snz_companies`
add `is_complete` SMALLINT NULL COMMENT '供应商信息是否完整' after `user_id`;

## 更改需求表信息
ALTER TABLE `snz_requirements`
DROP COLUMN `over_time`;

## 向snz_users表添加origin字段
 alter TABLE `snz_users`
 add `origin` SMALLINT NULL COMMENT '用户来源' after `approve_status`;

## 为snz_companies表添加product_line字段
alter table `snz_companies`
add `product_line` SMALLINT NULL COMMENT '所属产品线编号' after `is_complete`;

## 添加产品线表
DROP TABLE IF EXISTS `snz_product_lines` ;

CREATE TABLE IF NOT EXISTS `snz_product_lines` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`name`                VARCHAR(20)   NULL                    COMMENT '产品线名称',
PRIMARY KEY (`id`)
);
insert into snz_product_lines(name) values('冰箱'),('冷柜'),('波轮'),('滚筒'),('家用空调'),('商用空调'),('洗碗机'),('热水器'),('油烟机'),('灶具'),('太阳能热水器'),('燃气热水器'),('医疗冷柜'),('电视机'),('电机-600'),('控制板-600'),('新材料-600');

## 更改资源量大小
ALTER TABLE `snz_modules`
CHANGE COLUMN `resource_num` `resource_num` VARCHAR(512) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL COMMENT '这个是设置不同工厂的不同需求量（json格式rs:[{fa:101,num:1000},{fa:102,num:2000}]）' ;

## 更改供应商质量表
ALTER TABLE `snz_company_extra_quality`
ADD `auditor_type` INT NULL COMMENT '质量人员类型（1:资质审核员, 2:试验员, 3:绿带, 4:黑带)'
AFTER `number_in_lab`;

## 更改模块配额信息
ALTER TABLE `snz_module_quotas`
ADD COLUMN `original_cost` INT(11) NULL AFTER `scale`,
ADD COLUMN `actual_cost` INT(11) NULL AFTER `original_cost`;

## 创建供应商和可供货园区的中间表
CREATE TABLE IF NOT EXISTS `snz_company_supply_park` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`company_id`          BIGINT        NOT NULL                COMMENT '对应企业id',
`user_id`             BIGINT        NOT NULL                COMMENT '用户id',
`supply_park_id`      BIGINT        NOT NULL                COMMENT '供货园区id',
`name`                VARCHAR(20)   NOT NULL                COMMENT '供货园区名称（冗余)',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_company_supply_park_company_id_idx` ON `snz_company_supply_park` (`company_id`);
CREATE INDEX `snz_company_supply_park_user_id_idx` ON `snz_company_supply_park` (`user_id`);
CREATE INDEX `snz_company_supply_park_supply_park_id_idx` ON `snz_company_supply_park` (`supply_park_id`);

## 向companies表添加字段
alter table `snz_companies`
add column `business_license_id`  VARCHAR(50) NULL  COMMENT '营业执照号' after `business_license`;

alter table `snz_companies`
add column `tax_no_id`  VARCHAR(50)  NULL  COMMENT '税务登记证号' after `tax_no`;

alter table `snz_companies`
add column `org_cert_id`  VARCHAR(50)  NULL   COMMENT '组织机构证号' after `org_cert`;

# 更改模块信息
ALTER TABLE `snz_modules`
ADD COLUMN `module_num` VARCHAR(32) NULL COMMENT '模块专用号(海尔的系统生成的编号)' AFTER `module_name`,
ADD COLUMN `series_id` INT(11) NULL COMMENT '系列编号' AFTER `module_num`,
ADD COLUMN `series_name` VARCHAR(32) NULL COMMENT '系列名称' AFTER `series_id`;

# 更改需求信息添加币种信息
ALTER TABLE `snz_requirements`
ADD COLUMN `coin_type` SMALLINT NOT NULL AFTER `head_drop`;

## 修改snz_companies表
alter table `snz_companies`
drop column industry_category;

alter table `snz_companies`
drop column delivery_address;

alter table `snz_companies`
drop column country;

alter table `snz_companies`
drop column province;

alter table `snz_companies`
drop column city;

alter table `snz_companies`
drop column employee;

alter table `snz_companies`
drop column size;

alter table `snz_companies`
drop column factory_fixed_assets;

alter table `snz_companies`
add column `factories` VARCHAR(1024)  NOT NULL  COMMENT '工厂信息(json)' after `customers`;

## 修改snz_company_finances表
alter table `snz_company_finances`
drop column recent_sold;

alter table `snz_company_finances`
drop column recent_net;

alter table `snz_company_finances`
add column  `recent_finance` varchar(2048) NOT NULL COMMENT '近三年销售额和净利润,json存储' after `coin_type`;

## 向snz_users表添加refuse_status字段
 alter TABLE `snz_users`
 add `refuse_status` SMALLINT NULL COMMENT '审核拒绝状态,0：没有被拒绝过，-1:被拒绝过' after `origin`;

## 修改snz_companies表
alter table `snz_companies`
change column `factories` `factories` VARCHAR(1024)  NULL  COMMENT '工厂信息(json)' after `customers`;

# 工厂信息
ALTER TABLE `snz_address_factory`
ADD COLUMN `product_id` BIGINT(20) NOT NULL AFTER `park_id`;

## 向snz_users表添加qualify_status字段
 alter TABLE `snz_users`
 add `qualify_status` SMALLINT NULL COMMENT '资质校验状态' after `refuse_status`;

# 需求的表单信息更改（实现一个需求对应多个后台类目）
ALTER TABLE `snz_requirements`
DROP COLUMN `series_name`,
CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ;
CHANGE COLUMN `product_id` `send_scope` BIGINT(20) NOT NULL COMMENT '供应商范围（前台二级类目，当前选取的)' ,
CHANGE COLUMN `product_name` `scope_name` VARCHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL COMMENT '供应商范围名称' ,
CHANGE COLUMN `series_id` `series_ids` VARCHAR(256) NOT NULL COMMENT '系列编号(后台二级类目){sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}' ;


# 模块报价方案添加类目编号（用于方便查询类目的属性信息）
ALTER TABLE `snz_module_quotations`
ADD COLUMN `series_id` BIGINT(20) NULL AFTER `module_name`;

#在需求中添加前台一级类目的信息
ALTER TABLE `snz_requirements`
ADD COLUMN `send_front` BIGINT(20) NULL COMMENT '供应商一级类目（前台一级类目：用于显示）' AFTER `purchaser_name`,
ADD COLUMN `front_name` VARCHAR(32) NULL COMMENT '供应商一级类目名称' AFTER `send_front`;

# 重新定义资质评价表结构
DROP TABLE IF EXISTS `snz_company_qualification`;
DROP TABLE IF EXISTS `snz_company_qualification_overall`;
DROP TABLE IF EXISTS `snz_company_qualification_prepared`;
-- -----------------------------------------------------
-- Table `snz_company_qualifications`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_qualifications`;

CREATE TABLE IF NOT EXISTS `snz_company_qualifications` (
  `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id`     BIGINT        NOT NULL COMMENT '供应商用户id',
  `subject`         INT           NOT NULL COMMENT '上线内容',
  `role_allowed`    VARCHAR(64)   NOT NULL COMMENT '所属管理角色',
  `is_mandatory`    INT           NOT NULL COMMENT '是否为否决项',
  `is_passed`       INT           NULL     COMMENT '是否通过审核',
  `attach_url`      VARCHAR(128)  NULL     COMMENT '附件地址url',
  `view`            VARCHAR(256)  NULL     COMMENT '意见',
  `created_at`      DATETIME      NULL     COMMENT '创建时间',
  `updated_at`      DATETIME      NULL     COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

# 增加需求方案的状态
ALTER TABLE `snz_requirement_solutions`
CHANGE COLUMN `solution_end` `status` SMALLINT(6) NULL DEFAULT NULL COMMENT '在方案终投的情况下（标记方案终投阶段只能投递一次：0:未承诺, 1:全部承诺, 2:部分无法承诺, 3:已提交,无法更改的提交）' ;



# 2014-07-09 db change
# 模块数据添加老品模块Id
ALTER TABLE `snz_modules`
ADD COLUMN `old_moduleId` BIGINT(20) NULL COMMENT '我们系统的老品编号(老品表)' AFTER `module_name`;


# 实现多选需求二级类目
ALTER TABLE `snz_requirements`
DROP COLUMN `scope_name`,
DROP COLUMN `send_scope`,
CHANGE COLUMN `send_front` `product_id` BIGINT(20) NULL DEFAULT NULL COMMENT '产品类型（一级类目）' ,
CHANGE COLUMN `front_name` `product_name` VARCHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL COMMENT '产品类型名称（一级类目）' ;


#添加老品表
DROP TABLE IF EXISTS `snz_old_modules`;

CREATE TABLE IF NOT EXISTS `snz_old_modules` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
`requirement_name`    VARCHAR(32) NOT NULL  COMMENT '需求名称',
`purchaser_id`        BIGINT      NOT NULL  COMMENT '采购商编号(指的是注册公司的编号)',
`purchaser_name`      VARCHAR(32) NOT NULL  COMMENT '采购商名称',
`module_id`           BIGINT      NOT NULL  COMMENT '模块编号（由海尔的系统分配的编号）',
`module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称',
`module_num`          VARCHAR(32) NULL      COMMENT '模块专用号(海尔的系统生成的编号--物料号)',
`total`               INT         NOT NULL  COMMENT '全部的资源量',
`series_id`           INT         NOT NULL  COMMENT '系列编号（对应后台的二级类目）',
`series_name`         VARCHAR(32) NOT NULL  COMMENT '系列名称',
`quota_ids`           VARCHAR(128)NOT NULL  COMMENT '配额ids 1,2,3...',
`over_time`           DATE        NOT NULL  COMMENT '反超期',
`tactics_id`          INT         NOT NULL  COMMENT '模块策略编号',
`head_drop`           VARCHAR(32) NOT NULL  COMMENT '引领点',
`resource_num`        VARCHAR(128)NOT NULL  COMMENT '这个是设置不同工厂的不同需求量（json格式rs:[{fa:101,num:1000},{fa:102,num:2000}]）',
`quality`             INT         NULL      COMMENT '质量目标（可以有创建者or团队人员填写）',
`cost`                INT         NULL      COMMENT '成本目标',
`delivery`            INT         NULL      COMMENT '产能要求',
`attestation`         VARCHAR(128)NULL      COMMENT '认证要求',
`supply_at`           DATETIME    NOT NULL  COMMENT '批量供货时间',
`resources`           INT         NULL      COMMENT '资源量',
`resource_count`      INT         NULL      COMMENT '资源计数',

`time_total`          INT         NULL      COMMENT '计时（天）',
`interaction_starts`  SMALLINT    NULL      COMMENT '交互启动',
`npi_status`          SMALLINT    NULL      COMMENT 'NPI状态',
`priority_select`     SMALLINT    NULL      COMMENT '优选',
`price`               DOUBLE      NULL      COMMENT '价格',
`created_at`          DATETIME    NULL      COMMENT '创建时间',
`updated_at`          DATETIME    NULL      COMMENT '修改时间',
PRIMARY KEY (`id`));

#创建衍生品场景差异表
DROP TABLE IF EXISTS `snz_derivative_diff`;
CREATE TABLE IF NOT EXISTS `snz_derivative_diff`(
 `id`                BIGINT          NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
 `requirement_id`    BIGINT          NOT NULL                 COMMENT '需求id',
 `module_id`         BIGINT          NOT NULL                 COMMENT  '模块id',
 `module_name`       VARCHAR(32)     NOT NULL                 COMMENT '模块名称',
 `bom_module`        SMALLINT        NOT NULL                 COMMENT  '模块BOM(模块主关件不变，0:子件增加,1:子件减少,2:子件变更)',
 `matrix`            SMALLINT        NOT NULL                 COMMENT  '模具(模块主关件符合衍生号要求，0:子件增加,1:子件减少,2:子件变更)',
 `material`          SMALLINT        NOT NULL                 COMMENT  '材料（0:相同,1:不同）',
 `surface_treatment` SMALLINT        NOT NULL                 COMMENT  '表面处理工艺（0：喷涂，1：电镀，2：喷粉，3：其他）',
 `printing`          SMALLINT        NOT NULL                 COMMENT  '印刷（0：颜色，1：商标，2：文字，3：花纹）',
 `software_param`    SMALLINT        NOT NULL                 COMMENT   '软件、参数（0：软件，1：参数）',
 `structure`         SMALLINT        NOT NULL                 COMMENT   '结构（0：同截面不同长度，1：加工细节）',
 `overseas_parts`    SMALLINT        NOT NULL                 COMMENT   '是否用于海外散件（0：否，1：是）',
 `host_change`       SMALLINT        NOT NULL                 COMMENT   '主关件参数变化（0：电压频率，1：其他）',
 `drive`             SMALLINT        NOT NULL                 COMMENT   '接口相同，传动方式或传动比不同（传动方式：0，传动比：1）',
 `created_at`        DATETIME    NULL      COMMENT '创建时间',
 `updated_at`        DATETIME    NULL      COMMENT '修改时间',

 PRIMARY KEY (`id`));




# 2014-07-10 db change
# 在需求中添加供应商范围的选取
ALTER TABLE `snz_requirements`
DROP COLUMN `company_category`,
CHANGE COLUMN `company_scope` `company_scope` VARCHAR(256) NULL DEFAULT NULL COMMENT '供应商范围定义（用于圈定供应商的范围，作为选取依据）' ;


# 供货区域改为string存json
ALTER TABLE `snz_company_extra_delivery`
    MODIFY `delivery_area` VARCHAR(256) NULL;

# 完善信息中增加附件字段
ALTER TABLE `snz_company_extra_scale_and_cost`
ADD COLUMN `market_share_attach_url` VARCHAR(128) NULL AFTER `market_share_ranking`;
ALTER TABLE `snz_company_extra_scale_and_cost`
ADD COLUMN `sales_attach_url` VARCHAR(128) NULL AFTER `sales_ranking`;
ALTER TABLE `snz_company_extra_scale_and_cost`
ADD COLUMN `annual_production_attach_url` VARCHAR(128) NULL AFTER `annual_production_ranking`;
ALTER TABLE `snz_company_extra_delivery`
ADD COLUMN `number_of_equipments_attach_url` VARCHAR(128) NULL AFTER `number_of_equipments`;
ALTER TABLE `snz_company_extra_delivery`
ADD COLUMN `module_production_capacity_attach_url` VARCHAR(128) NULL AFTER `module_production_capacity`;
ALTER TABLE `snz_company_extra_delivery`
ADD COLUMN `number_of_automation_equipment_attach_url` VARCHAR(128) NULL AFTER `number_of_automation_equipment`;
ALTER TABLE `snz_company_extra_response`
ADD COLUMN `interface_development_team_size_attach_url` VARCHAR(128) NULL AFTER `interface_development_team_size`;
ALTER TABLE `snz_company_extra_response`
ADD COLUMN `quality_interface_team_size_attach_url` VARCHAR(128) NULL AFTER `quality_interface_team_size`;
ALTER TABLE `snz_company_extra_response`
ADD COLUMN `sales_service_interface_team_size_attach_url` VARCHAR(128) NULL AFTER `sales_service_interface_team_size`;
ALTER TABLE `snz_company_extra_response`
ADD COLUMN `unified_interface_jobs_attach_url` VARCHAR(128) NULL AFTER `unified_interface_jobs`;

## 向snz_supplier_tqrdc_infos表添加字段
alter table `snz_supplier_tqrdc_infos`
add column `user_id`  bigint NOT NULL COMMENT '用户id' after `id`;

alter table `snz_supplier_tqrdc_infos`
add column `company_id` BIGINT  NOT NULL COMMENT '对应企业信息' after `user_id`;



# 2014-7-11 db change
# 完善信息中增加附件字段
ALTER TABLE `snz_company_extra_rd`
    ADD COLUMN `facility_attach_url` VARCHAR(128) NULL AFTER `assets`;
ALTER TABLE `snz_company_extra_rd`
    ADD COLUMN `lab_attach_url` VARCHAR(128) NULL AFTER `lab_level`;
ALTER TABLE `snz_company_extra_rd`
    ADD COLUMN `patents_attach_url` VARCHAR(128) NULL AFTER `number_of_patents_last_three_years`;
ALTER TABLE `snz_company_extra_rd`
    ADD COLUMN `success_stories_attach_url` VARCHAR(128) NULL AFTER `success_stories`;

ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `rohs_attach_url` VARCHAR(128) NULL AFTER `rohs_valid_date`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `iso9001_attach_url` VARCHAR(128) NULL AFTER `iso9001_valid_date`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `iso14001_attach_url` VARCHAR(128) NULL AFTER `iso14001_valid_date`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `ts16949_attach_url` VARCHAR(128) NULL AFTER `ts16949_valid_date`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `number_for_quality_attach_url` VARCHAR(128) NULL AFTER `number_in_lab`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `auditor_attach_url` VARCHAR(128) NULL AFTER `number_of_auditors`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `lab_attach_url` VARCHAR(128) NULL AFTER `lab_level`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `qualified_attach_url` VARCHAR(128) NULL AFTER `customer_satisfaction`;


## 向snz_users表添加tags字段
 alter TABLE `snz_users`
 add `tags` VARCHAR(512) NULL COMMENT '用户标签' after `qualify_status`;

CREATE TABLE `snz_compensations` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `park` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '园区',
  `product_line` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品线',
  `supplier_account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '供应商账号',
  `supplier_name` varchar(35) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '供应商名称',
  `is_internal` tinyint(1) DEFAULT NULL COMMENT '内外部供应商标志, 0内部, 1外部',
  `factory` varchar(4) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工厂',
  `factory_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工厂方法',
  `materiel_no` varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '物料号',
  `materiel_desc` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '物料描述',
  `purchaser_group` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '采购商组',
  `purchaser_group_desc` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '采购商组描述',
  `score` int(11) DEFAULT NULL COMMENT '扣分',
  `is_money` tinyint(1) DEFAULT NULL COMMENT '需要扣款标志, 0不需要, 1需要',
  `money` int(11) DEFAULT NULL COMMENT '扣除金额',
  `deducted_at` date DEFAULT NULL COMMENT '扣款日期',
  `current` date DEFAULT NULL COMMENT '当前日期',
  `status` tinyint(1) DEFAULT NULL COMMENT '申诉状态',
  `result` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '扣款结果',
  `entered_at` date DEFAULT NULL COMMENT '凭证入账日期',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='索赔信息';

# 增加资质验证状态
ALTER TABLE `snz_company_qualifications`
    ADD COLUMN `bc_id` BIGINT NOT NULL AFTER `id`;

# 重新定义保证金表结构
DROP TABLE IF EXISTS `snz_deposits`;

CREATE TABLE IF NOT EXISTS `snz_deposits` (
  `id`                BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`    BIGINT      NOT NULL  COMMENT '需求id',
  `purchaser_id`      BIGINT      NOT NULL  COMMENT '采购商id',
  `deal_id`           BIGINT      NULL      COMMENT '订单号（快捷通）',
  `deal_time`         DATETIME    NULL      COMMENT '快捷通订单交易时间',
  `deal_url`          VARCHAR(128) NULL     COMMENT '快捷通交易链接',
  `amount`            INT         NOT NULL  COMMENT '保证金金额',
  `type`              INT         NOT NULL  COMMENT '保证金类型（1: 付款，2:退款）',
  `status`            SMALLINT    NOT NULL  COMMENT '保证金状态（0：未提交，1：提交成功，-1：提交失败，2：付款成功，-2：付款失败，3：退款成功，-3：退款失败）',
  `created_at`        DATETIME    NULL      COMMENT '创建时间',
  `updated_at`        DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));

CREATE INDEX snz_dep_requirement_id ON snz_deposits (requirement_id);
CREATE INDEX snz_dep_purchaser_id ON snz_deposits (purchaser_id);
CREATE INDEX snz_dep_deal_id ON snz_deposits (deal_id);

# 需求中添加物料类别
ALTER TABLE `snz_requirements`
ADD COLUMN `materiel_type` SMALLINT(6) NOT NULL COMMENT '整体需求级别的物料类别（1:SKD，2:模块，3:连接件，4:包装印刷辅料，5:二三级物料）' AFTER `series_ids`;



#2014-7-12 db change
ALTER TABLE `snz_requirement_solutions`
ADD COLUMN `qualify_status` SMALLINT(6) NULL COMMENT '资质校验状态：-2:资质检查不通过, －1:没有提交（或提交失败), 1:已提交，等待审核, 2:资质检查通过(只有通过针对于方案的整体资质验证通过了才能去确认方案' AFTER `solution_file`;


# 重新定义资质验证表结构
DROP TABLE IF EXISTS `snz_company_qualifications`;

-- -----------------------------------------------------
-- Table `snz_supplier_qualifies`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_qualifies`;

CREATE TABLE IF NOT EXISTS `snz_supplier_qualifies` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `bc_id`       BIGINT      NOT NULL COMMENT '后台类目id',
  `supplier_id` BIGINT      NOT NULL COMMENT '供应商id',
  `status`      INT         NOT NULL COMMENT '验证状态',
  `created_at`  DATETIME    NULL COMMENT '创建时间',
  `updated_at`  DATETIME    NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX snz_supplier_qualifies_bc_id on `snz_supplier_qualifies` (`bc_id`);
CREATE INDEX snz_supplier_qualifies_supplier_id on `snz_supplier_qualifies` (`supplier_id`);

-- -----------------------------------------------------
-- Table `snz_supplier_qualify_details`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_qualify_details`;

CREATE TABLE IF NOT EXISTS `snz_supplier_qualify_details` (
  `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `bc_id`           BIGINT        NOT NULL COMMENT '后台类目id',
  `supplier_id`     BIGINT        NOT NULL COMMENT '供应商用户id',
  `subject`         INT           NOT NULL COMMENT '上线内容',
  `role_allowed`    VARCHAR(64)   NOT NULL COMMENT '所属管理角色',
  `is_mandatory`    INT           NOT NULL COMMENT '是否为否决项',
  `is_passed`       INT           NULL     COMMENT '是否通过审核',
  `attach_url`      VARCHAR(128)  NULL     COMMENT '附件地址url',
  `view`            VARCHAR(256)  NULL     COMMENT '意见',
  `created_at`      DATETIME      NULL     COMMENT '创建时间',
  `updated_at`      DATETIME      NULL     COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX snz_supplier_qualify_details_bc_id on `snz_supplier_qualify_details` (`bc_id`);
CREATE INDEX snz_supplier_qualify_details_supplier_id on `snz_supplier_qualify_details` (`supplier_id`);

alter table `snz_companies`
change column `product_line` `product_line`  VARCHAR(100)  NULL  COMMENT '所属产品线(json)';


# 2014-7-15 db change
# wtf, 某条bcs都超过4096了（对应前台类目：注塑）
alter table snz_category_bindings modify bcs VARCHAR(8192);

#添加认证信息
CREATE TABLE IF NOT EXISTS `snz_identify_name` (
  `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name`            VARCHAR(32)   NOT NULL COMMENT '认证信息',
  `created_at`      DATETIME      NULL     COMMENT '创建时间',
  `updated_at`      DATETIME      NULL     COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
insert into snz_identify_name (name, created_at, updated_at) values('UL认证', now(), now()),('ETL认证', now(), now()),('EEV认证', now(), now()),
('E-star 认证', now(), now()),('SAA认证', now(), now()),('C-UL认证', now(), now()),('C-ETL认证', now(), now()),('FCC认证', now(), now()),
('FDA认证', now(), now()),('CSA认证', now(), now()),('NSF认证', now(), now()),('VDE认证', now(), now()),('VDE/GS认证', now(), now()),
('CE-LVD,EMC认证', now(), now()),('CE认证', now(), now()),('EMC认证', now(), now()),('GS/CE认证', now(), now()),('CE-MDD认证', now(), now()),
('欧洲能耗认证', now(), now()),('solar keymark认证', now(), now()),('NF认证', now(), now()),('EN认证', now(), now()),('LMBG食品卫生认证', now(), now()),
('澳洲能耗认证', now(), now()),('SASO-安全，能耗，EMC', now(), now()),('SASO-EMC认证', now(), now()),('SASO 安全认证', now(), now()),
('SASO 能耗认证', now(), now()),('日本S认证', now(), now()),('日本JET认证', now(), now()),('韩国安全，能耗，EMC认证', now(), now()),
('韩国能耗认证', now(), now()),('韩国安全认证', now(), now()),('南非SABS认证', now(), now()),('阿根廷IRAM认证', now(), now()),('俄罗斯海关联盟认证', now(), now()),
('新加坡PSB认证', now(), now()),('尼日利亚SONCAP认证', now(), now()),('巴西INMETRO认证', now(), now()),('印度BIS认证', now(), now()),('科威特TER认证', now(), now()),
('香港能耗认证', now(), now()),('CB认证', now(), now()),('CCC认证', now(), now()),('CQC自愿认证', now(), now()),('能效标识备案', now(), now()),('型检测试', now(), now()),
('CQC节能认证', now(), now()),('CCBQ博天亚性能', now(), now()),('CECP环保认证', now(), now()),('羊毛认证', now(), now()),('ROHS', now(), now()),('REACH', now(), now()),
('LCOE认证', now(), now()),('VDE/EMC认证', now(), now()),('VDE/能耗认证', now(), now()),('VDE/抗菌', now(), now());

#moudle中添加单位信息
ALTER TABLE `snz_modules`
CHANGE COLUMN `attestation` `attestation` VARCHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL COMMENT '认证要求' ,
ADD COLUMN `attestation_id` INT NULL COMMENT '认证编号' AFTER `delivery`,
ADD COLUMN `units` VARCHAR(256) NULL COMMENT '各种单位的json字段{cost:{salesId:1, name:EA, quantityId:1, name:百个},delivery:{salesId:2, name:EA, quantityId:4, name:十个}}\'' AFTER `supply_at`;


alter table `snz_old_modules`
drop column resources;



# 2014-7-16 db change
# 保证金表修改
alter table snz_deposits change purchaser_id supplier_id bigint;
alter table snz_deposits add column bank_info TEXT after amount;

ALTER TABLE `snz_requirements`
ADD COLUMN `module_amount` BIGINT(20) NULL COMMENT '需求下所有模块的总价格（计算：n*模块*成本）' AFTER `module_type`;

# 账号类型修改
alter table snz_users
change account_type `account_type`  SMALLINT  NULL COMMENT '账号类型, 1:制造商，2：渠道商';


# 修改snz_companies表
alter table snz_companies
change column user_id `user_id` BIGINT  NOT NULL  COMMENT '对应供应商id';

alter table snz_companies
add column `include_keywords`  VARCHAR(50)  NULL  COMMENT '公司名称包含的黑名单关键字' after `is_complete`;

# 为snz_users表的nick字段添加唯一索引
# alter table snz_users drop index snz_users_nick_idx;
# CREATE UNIQUE INDEX `snz_users_nick_idx` ON `snz_users` (`nick`);


# 修改黑名单表
alter table snz_blacklist
change name `name`  VARCHAR(50) NOT NULL  COMMENT '公司名';

alter table snz_blacklist
add column `keywords` VARCHAR(30)   NULL COMMENT '关键字' after `name`;

# 为snz_blacklist表的name字段添加唯一索引
alter table snz_blacklist drop index snz_blacklist_name;
CREATE UNIQUE INDEX `snz_blacklist_name_idx` ON `snz_blacklist` (`name`);

insert into snz_blacklist(name,keywords,created_at,updated_at) values('青岛海明制冷机械有限公司','海明',now(),now()),('东莞市庆新安制冷设备配件有限公司','庆新安',now(),now()),('重庆昌健塑胶有限公司','昌健',now(),now()),('北京华航海鹰新技术开发有限责任公司','华航海英',now(),now()),('富乐（广州）粘合剂有限公司','富乐',now(),now());

#2014-07-17 db change
#增加需求的谈判文件信息
ALTER TABLE `snz_requirements`
ADD COLUMN `transact_file` VARCHAR(256) NULL COMMENT '谈判文件([{name:1.doc, url:url1},{name:2.doc, url:url2}])' AFTER `check_time`;




#2014-07-18 db change
#更改生产起始年月
ALTER TABLE `snz_module_quotations`
CHANGE COLUMN `initial_year` `initial_year` VARCHAR(16) NOT NULL COMMENT '生产起始年月';


# 2014-7-21 db change
# 保证金金额改为BIGINT
alter table snz_deposits modify amount bigint;

# 新增风险保证金表
DROP TABLE IF EXISTS `snz_risk_mortgage_payments`;

CREATE TABLE IF NOT EXISTS `snz_risk_mortgage_payments` (
  `id`                BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_code`     VARCHAR(20)   NOT NULL  COMMENT '供应商代码',
  `supplier_detail`   VARCHAR(128)  NOT NULL  COMMENT '供应商描述',
  `purchaser_id`      BIGINT        NOT NULL  COMMENT '采购商',
  `amount`            BIGINT        NOT NULL  COMMENT '风险抵押金金额',
  `created_at`        DATETIME      NULL      COMMENT '创建时间',
  `updated_at`        DATETIME      NULL      COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

alter table `snz_old_modules`
drop column price,
change column  `over_time` `over_time` DATE NULL DEFAULT NULL COMMENT '反超期',
change column  `quota_ids` `quota_ids` VARCHAR(128) NULL DEFAULT NULL COMMENT '配额ids 1,2,3...';


#2014-07-22 db change
#添加新的需求状态（谈判｜竞标）
ALTER TABLE `snz_requirements`
ADD COLUMN `transact_type` SMALLINT NULL COMMENT '当需求阶段处于方案终投阶段&场景2｜3时该阶段会变成（谈判|竞标）' AFTER `check_time`;


-- 供应商审核额外信息表
-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_approve_extra`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_supplier_approve_extra` (
  `id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`             BIGINT        NOT NULL                COMMENT '用户id',
  `enter_approved_at`   DATETIME      NULL                    COMMENT '入驻审核时间',
  `enter_approver_id`   BIGINT        NULL                    COMMENT '入驻审核人编号',
  `enter_approver_name` VARCHAR(50)   NULL                    COMMENT '入驻审核人名称',
  `last_modified_at`    DATETIME      NULL                    COMMENT '最后修改基本信息的时间',
  `last_approved_at`    DATETIME      NULL                    COMMENT '审批最后修改基本信息的时间',
  `last_approver_id`    BIGINT        NULL                    COMMENT '审批最后修改信息的审核人编号',
  `last_approver_name`  VARCHAR(50)   NULL                    COMMENT '审批最后修改信息的审核人名称',
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_supplier_approve_extra_user_id_idx` ON `snz_supplier_approve_extra` (`user_id`);

#update snz_topics
ALTER TABLE snz_topics ADD COLUMN joiners INT(11) AFTER scope COMMENT "圈子人数";

#update snz_replies
ALTER TABLE snz_replies ADD COLUMN req_id INT(11) AFTER receiver_id COMMENT "需求id";

-- 根据可供货园区统计供应商数量
-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_count_by_supply_parks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_count_by_supply_parks` ;

CREATE TABLE IF NOT EXISTS `snz_supplier_count_by_supply_parks` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`date`                DATETIME      NOT NULL                COMMENT '日期',
`supply_park_id`      BIGINT        NOT NULL                COMMENT '可供货园区编号',
`supplier_count`      BIGINT        NOT NULL                COMMENT '供应商数量',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_scbsp_supply_park_id_idx` ON `snz_supplier_count_by_supply_parks` (`supply_park_id`);
CREATE INDEX `snz_scbsp_date_idx` ON `snz_supplier_count_by_supply_parks` (`date`);

-- 根据状态维度统计供应商数量
-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_count_by_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_count_by_status` ;

CREATE TABLE IF NOT EXISTS `snz_supplier_count_by_status` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`date`                DATETIME      NOT NULL                COMMENT '日期',
`status`              SMALLINT      NOT NULL                COMMENT '状态编号',
`supplier_count`      BIGINT        NOT NULL                COMMENT '供应商数量',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_scbs_status_idx` ON `snz_supplier_count_by_status` (`status`);
CREATE INDEX `snz_scbs_date_idx` ON `snz_supplier_count_by_status` (`date`);

# 2014-07-22 db change
# 添加新的MDM供应商新增
# 供应商MDM核审对接表
    -- -----------------------------------------------------
    -- Table `snz`.`mdm_supplier_qualifications`
    -- -----------------------------------------------------
    DROP TABLE IF EXISTS `mdm_supplier_qualifications`;

    CREATE TABLE IF NOT EXISTS `mdm_supplier_qualifications` (
        `id`            bigint      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
        `supplier_id`   bigint      NOT NULL COMMENT '供应商ID',
        `v_code`        varchar(64)   DEFAULT NULL COMMENT '商家v码',
        `stage`         smallint  DEFAULT NULL COMMENT '当前进行的步骤',

        `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
        `updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
        PRIMARY KEY (`id`)
    );

    CREATE INDEX mdm_supplier_qualifications_sup_id on `mdm_supplier_qualifications`(supplier_id);


# 表 snz_supplier_tqrdc_infos 新增字段 product_line_id 用以标识产品线
ALTER TABLE snz_supplier_tqrdc_infos ADD product_line_id int NULL after module;


-- 根据供应商层次维度统计供应商数量
-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_count_by_level`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_count_by_level` ;

CREATE TABLE IF NOT EXISTS `snz_supplier_count_by_level` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`month`               VARCHAR(8)    NOT NULL                COMMENT '月份',
`level`               SMALLINT      NOT NULL                COMMENT '供应商层次',
`supplier_count`      BIGINT        NOT NULL                COMMENT '供应商数量',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_scbl_level_idx` ON `snz_supplier_count_by_level` (`level`);
CREATE INDEX `snz_scbl_month_idx` ON `snz_supplier_count_by_level` (`month`);



#2014-07-25 db change
#新增加的详细模块配额信息
-- -----------------------------------------------------
-- Table `snz_detail_quotas`    详细的供应商配额信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_detail_quotas`;

CREATE TABLE IF NOT EXISTS `snz_detail_quotas` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `requirement_name`    VARCHAR(32) NOT NULL  COMMENT '需求名称',
  `solution_id`         BIGINT      NOT NULL  COMMENT '方案编号方便查询new',
  `module_id`           BIGINT      NOT NULL  COMMENT '模块编号',
  `module_num`          VARCHAR(32) NULL      COMMENT '模块专用号（plm系统给出的）',
  `module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称',
  `purchase_org`        VARCHAR(32) NULL      COMMENT '采购组织',
  `factory_num`         VARCHAR(32) NOT NULL  COMMENT '工厂编码',
  `supplier_id`         BIGINT      NOT NULL  COMMENT '供应商编号',
  `supplierV_code`      VARCHAR(32) NULL      COMMENT '供应商V码',
  `supplier_name`       VARCHAR(32) NOT NULL  COMMENT '供应商名称',
  `purchase_type`       VARCHAR(32) NULL      COMMENT '采购类型',
  `quantity`            INT         NULL      COMMENT '配额数量',
  `scale`               INT         NULL      COMMENT '模块配额的占总配额的比例70,50',
  `original_cost`       INT         NULL      COMMENT '用户的模块报价',
  `actual_cost`         INT         NULL      COMMENT '实际模块跟标价格',
  `agency_fee`          INT         NULL      COMMENT '代理费',
  `duty`                INT         NULL      COMMENT '关税',
  `other_fee`           INT         NULL      COMMENT '杂费',
  `fee_unit`            INT         NULL      COMMENT '采购价单位',
  `purchase_unit`       VARCHAR(32) NULL      COMMENT '采购单位',
  `coin_type`           VARCHAR(32) NULL      COMMENT '采购币种',
  `purchase_day`        INT         NULL      COMMENT '采购期',
  `purchase_team`       VARCHAR(32) NULL      COMMENT '采购组',
  `basic_unit`          VARCHAR(32) NULL      COMMENT '基本单位',
  `tax_code`            VARCHAR(32) NULL      COMMENT '税码',
  `status`              INT         NULL      COMMENT '状态（0:待提交，1:已写入sap）',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_detail_quota_requirement_id ON snz_detail_quotas (requirement_id);

# 黑名单表添加法人字段
alter table snz_blacklist
add column `init_agent` VARCHAR(50)  NULL COMMENT '法人' after `keywords`;

# 删除黑名单的数据，重新插入
delete from snz_blacklist where id>0;
insert into snz_blacklist(name,keywords,init_agent,created_at,updated_at) values('青岛海明制冷机械有限公司','海明','任红梅',now(),now()),('东莞市庆新安制冷设备配件有限公司','庆新安','李姚暖',now(),now()),('重庆昌健塑胶有限公司','昌健','叶昌伟',now(),now()),('北京华航海鹰新技术开发有限责任公司','华航海英','宋家琪',now(),now()),('富乐（广州）粘合剂有限公司','富乐','JOAN ALLISON BRACA',now(),now());

# 添加新供应商MDM对接信息字段
alter table `mdm_supplier_qualifications`
add `com_code`	varchar(16) null comment '公司代码' after v_code,
add `slave`		varchar(16) null comment '统奴科目' after v_code,
add `order`		varchar(16) null comment '排序码' after v_code,
add `pay_term`	varchar(16) null comment '付款条件' after v_code,
add `pay_method`varchar(16) null comment '付款方式' after v_code,
add `bank_num`	varchar(64) null comment '银行账号' after v_code,
add `bank_nation`varchar(32) null comment '采购国家' after v_code,
add `bank_owner`varchar(64) null comment '银行户主' after v_code,
add `bank_code`	varchar(16) null comment '银行码' after v_code,
add `purch_org`	varchar(32) null comment '采购组织' after v_code,
add `currency`	varchar(16) null comment '订单货币' after v_code,
add `pi_partner`	varchar(16) null comment 'pi合作伙伴' after v_code;

# 添加默认的供应商财务组
INSERT INTO `mdm_configures` (`code`, `name`, `type`)
VALUES ('1100', '默认', 6);

# 修改老品模块信息表中得模块编号（由海尔的系统分配的编号）字段
ALTER TABLE `snz_old_modules` CHANGE COLUMN `module_id` `module_id` BIGINT(20) NULL COMMENT '模块编号（由海尔的系统分配的编号）' ;
# 添加user_name字段到snz_topics
ALTER TABLE snz_topics ADD COLUMN user_name VARCHAR(80) COMMENT '用户名' AFTER user_id;

# 添加工厂组织关系 snz_factories_organs

CREATE TABLE `snz_factories_organs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `factory` varchar(4) DEFAULT NULL COMMENT '工厂',
  `organ` varchar(4) DEFAULT NULL COMMENT '组织',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工厂组织对应关系';

ALTER TABLE `snz_old_modules` CHANGE COLUMN `module_id` `module_id` BIGINT(20) NULL COMMENT '模块编号（由海尔的系统分配的编号）' ;

# 添加user_name字段到snz_topics
ALTER TABLE snz_topics ADD COLUMN user_name VARCHAR(80) COMMENT '用户名' AFTER user_id;


#2014-07-26 db change
#在需求统计数据信息
ALTER TABLE `snz_requirements`
ADD COLUMN `send_su` INT NULL COMMENT '推送的供应商数量(各种需求的统计数据，当需求进入选定供应商阶段这些统计数据将从redis中回写到数据库)' AFTER `send_time`,
ADD COLUMN `answer_su` INT NULL COMMENT '响应的供应商数量' AFTER `send_su`,
ADD COLUMN `send_so` INT NULL COMMENT '提交方案的供应商数量' AFTER `answer_su`,
ADD COLUMN `topic_num` INT NULL COMMENT '话题数' AFTER `send_so`;

#2014-07-28 db change
ALTER TABLE `snz_detail_quotas`
ADD COLUMN `data_type` VARCHAR(32) NULL COMMENT '配额的数据类型' AFTER `tax_code`;

#修改财务表
alter table snz_company_finances
change column   `country`  `country`  int(11)       NULL        COMMENT '国家代号',
change column  `opening_bank`   `opening_bank`    varchar(64)   NULL  COMMENT '开户行',
change column   `bank_code`     `bank_code`      varchar(64)   NULL        COMMENT '银行码',
change column   `bank_account`  `bank_account`  varchar(64)   NULL        COMMENT '银行账号',
change column   `coin_type`     `coin_type`      SMALLINT      NULL        COMMENT '结算货币';

#修改公司表
alter table snz_companies
change column `corporation` `corporation` varchar(80)    NOT NULL   COMMENT '法人公司',
change column `group_name` `group_name` varchar(80)  NULL  COMMENT '集团名',
change column `init_agent` `init_agent`  varchar(80)    NOT NULL   COMMENT '法人代表';

# 2014-07-29 db change
#添加每个模块的配额人数信息
ALTER TABLE `snz_modules`
ADD COLUMN `select_num` INT NULL COMMENT '每个模块选取的供应商数量' AFTER `units`;

# 增加与海尔SAP系统同步状态位
alter table snz_deposits add column sync_status smallint after status;

# 财务信息添加开户许可证
alter table snz_company_finances
add column  `open_license`  varchar(255)  NOT NULL   COMMENT '开户许可证url' after opening_bank;

# 修改MDM配置信息表
    ALTER TABLE `mdm_supplier_qualifications`
    ADD COLUMN op_msg varchar(255) default null comment '操作返回信息' after stage,
    ADD COLUMN op_code varchar(8) default null comment '操作返回状态码' after stage;

    ALTER TABLE `mdm_supplier_qualifications`
    ADD COLUMN country varchar(8) default null comment '国家信息' after op_msg;


# 2014-07-30 db change
alter table snz_risk_mortgage_payments change purchaser_id purchaser_code bigint;

# 新增供应商核审状态
    ALTER TABLE `mdm_supplier_qualifications`
    ADD COLUMN status smallint default null comment '核审状态' after stage;

# 财务信息添加开户许可证
alter table snz_company_finances
add column  `open_license`  varchar(255)  NOT NULL   COMMENT '开户许可证url' after opening_bank;

# 主营业务表添加一级类目编号字段
alter table snz_company_main_businesses
add column `first_level_id` BIGINT  NULL COMMENT '一级类目编号' after user_id;

-- 根据行业维度（一级类目）统计供应商数量
-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_count_by_industry`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_count_by_industry` ;

CREATE TABLE IF NOT EXISTS `snz_supplier_count_by_industry` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`date`                DATETIME      NOT NULL                COMMENT '日期',
`industry`            SMALLINT      NOT NULL                COMMENT '行业（一级类目编号）',
`supplier_count`      BIGINT        NOT NULL                COMMENT '供应商数量',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_scbi_industry_idx` ON `snz_supplier_count_by_industry` (`industry`);
CREATE INDEX `snz_scbi_date_idx` ON `snz_supplier_count_by_industry` (`date`);

# 修改snz_purchaser_extras表的department字段的长度
alter table snz_purchaser_extras
change `department`  `department`  VARCHAR(100)   NOT NULL  COMMENT '所属部门';



# 2014-31-00:04 snz_users添加outer_id
ALTER TABLE snz_users ADD COLUMN outer_id bigint(20) DEFAULT NULL COMMENT '外部用户id' AFTER id;

#模块报价增加单位信息
ALTER TABLE `snz_module_quotas`
ADD COLUMN `cost_unit` VARCHAR(64) NULL COMMENT '价格单位的json字段' AFTER `actual_cost`;

ALTER TABLE `snz_module_quotations`
ADD COLUMN `cost_unit` VARCHAR(64) NULL COMMENT '价格单位的json字段' AFTER `price`;

-- -----------------------------------------------------
-- Table `snz_requirement_send`    需求的数据状态（用于标记是否对接的数据已全部传输成功）
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_send`;

CREATE TABLE IF NOT EXISTS `snz_requirement_send` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `send_PLM`            SMALLINT    NULL      COMMENT '是否已写入模块数据到plm中间表(0:待写入，1:已写入)',
  `reply_moduleNum`     SMALLINT    NULL      COMMENT '从plm中间表回写模块专用号',
  `confirm_quota`       SMALLINT    NULL      COMMENT '是否选定的供应商已全部确认配额信息',
  `send_vCode`          SMALLINT    NULL      COMMENT '向plm中间表写入供应商V码信息',
  `write_detailQuota`   SMALLINT    NULL      COMMENT '是否已写入详细的配额信息',
  `send_SAP`            SMALLINT    NULL      COMMENT '是否已写入详细配额数据到sap',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_send_requirement_id ON snz_requirement_send (requirement_id);


-- -----------------------------------------------------
-- Table `snz`.`snz_main_business_approvers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_main_business_approvers` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`member_id`           VARCHAR(20)   NOT NULL                COMMENT '成员工号',
`member_name`         VARCHAR(20)   NOT NULL                COMMENT '成员姓名',
`leader_id`           VARCHAR(20)   NOT NULL                COMMENT '组长工号',
`leader_name`         VARCHAR(20)   NOT NULL                COMMENT '组长姓名',
`main_business_id`    BIGINT        NOT NULL                COMMENT '主营业务编号',
`main_business_name`  VARCHAR(20)   NOT NULL                COMMENT '主营业务名称',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_main_business_approvers_member_id_idx` ON `snz_main_business_approvers` (`member_id`);
CREATE INDEX `snz_main_business_approvers_leader_id_idx` ON `snz_main_business_approvers` (`leader_id`);


#2014-8-1 db change
ALTER TABLE `snz_requirement_solutions`
ADD COLUMN `qualify_status` SMALLINT(6) NULL COMMENT '资质校验状态：-2:资质检查不通过, －1:没有提交（或提交失败), 1:已提交，等待审核, 2:资质检查通过(只有通过针对于方案的整体资质验证通过了才能去确认方案' AFTER `solution_file`;

ALTER TABLE `snz_modules`
ADD COLUMN `property_id` BIGINT NULL COMMENT '类目属性编号（用于判断模块资源信息）' AFTER `series_name`;

ALTER TABLE `snz_backend_category_properties`
ADD COLUMN `type` VARCHAR(128) NULL COMMENT '属性类型' AFTER `name`;


#2014-8-2 db change
ALTER TABLE `snz_module_quotations`
DROP COLUMN `remark`,
DROP COLUMN `weekly_work`,
DROP COLUMN `daily_work`,
DROP COLUMN `module_life`,
DROP COLUMN `initial_year`,
DROP COLUMN `daily_harvest`,
DROP COLUMN `year_plan`,
CHANGE COLUMN `series_id` `property_id` BIGINT(20) NULL DEFAULT NULL COMMENT '类目属性编号' ,
CHANGE COLUMN `coin_type` `coin_type` VARCHAR(32) NOT NULL COMMENT '货币(指的是货币类型RMB,$)' ;

# 删除模块报价类目属性
ALTER TABLE `snz_module_quotations`
DROP COLUMN `property_id`;


-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_credit_qualifies`
-- -----------------------------------------------------
create table `snz_supplier_credit_qualifies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商id',
  `user_id` 	bigint(20) NOT NULL COMMENT '供应商id',
  `status`		smallint default null comment '审核状态',
  `message`		varchar(1000) default null comment '错误消息',
  `created_at`  datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at`  datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
create index `snz_supplier_credit_qualify_supplier` on `snz_supplier_credit_qualifies`(supplier_id);
create index `snz_supplier_credit_qualify_user` on `snz_supplier_credit_qualifies`(user_id);

ALTER TABLE `snz_module_quotas`
ADD COLUMN `resource_num` VARCHAR(512) NULL COMMENT '这个是设置不同工厂的不同需求量（json格式rs:[{fa:101,num:1000},{fa:102,num:2000}]）' AFTER `scale`;

#2014-08-04 db change
alter table snz_deposits modify deal_id varchar(64);


# 修改 snz_supplier_tqrdc_infos
alter TABLE `snz_supplier_tqrdc_infos`
add column `program_promised_rate`    int null    comment '方案承诺率' after `resp_score`,
add column `program_selected_rate`    int null    comment '方案选中率' after `program_promised_rate`,
add column `program_adopted_rate`     int null    comment '方案落地率' after `program_selected_rate`,
add column `affect_delivery_count`    int null    comment '影响 T-1 交付次数' after `delivery_score_rank`,
add column `price_reduction_amount`   int null    comment '降价额' after `cost_score_rank`,
add column `price_reduction_range`    int null    comment '降幅' after `price_reduction_amount`;
alter TABLE `snz_supplier_tqrdc_infos`
add column `supplier_status`          int null  default 0 comment '供应商状态' after `composite_score`,
add column `product_line`             varchar(128) null comment '产品线' after `product_line_id`;


# 新品导入表 新建
CREATE TABLE `snz_new_product_imports` (
  `id`                      BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `document_name`           VARCHAR(128)  DEFAULT NULL COMMENT '文件',
  `part_number`             VARCHAR(64)   DEFAULT NULL COMMENT '？？？编号',
  `original_creation_date`  DATETIME      DEFAULT NULL COMMENT '原件创建时间',
  `prototype_send_date`     DATETIME      DEFAULT NULL COMMENT '原件发布时间',
  `purchase_confirmer`      VARCHAR(64)   DEFAULT NULL COMMENT '采购确认人',
  `purchase_confirm_date`   DATETIME      DEFAULT NULL COMMENT '采购确认时间',
  `mould_number`            VARCHAR(128)  DEFAULT NULL COMMENT '模块编号',
  `sample_finish_date`      DATETIME      DEFAULT NULL COMMENT '样机完成时间',
  `assembly_finish_date`    DATETIME      DEFAULT NULL COMMENT '装配完成时间',
  `assembly_conclusion`     VARCHAR(256)  DEFAULT NULL COMMENT '装配结论',
  `out_tester`              VARCHAR(64)   DEFAULT NULL COMMENT '外检员',
  `sample_receive_date`     DATETIME      DEFAULT NULL COMMENT '收样确认时间',
  `test_sample_receive_time`DATETIME      DEFAULT NULL COMMENT '检测收样时间',
  `test_sample_receiver`    VARCHAR(64)   DEFAULT NULL COMMENT '检测收样人',
  `test_start_date`         DATETIME      DEFAULT NULL COMMENT '检测开始时间',
  `test_planed_date`        DATETIME      DEFAULT NULL COMMENT '检测计划时间',
  `test_end_date`           DATETIME      DEFAULT NULL COMMENT '检测完成时间',
  `test_conclusion`         VARCHAR(256)  DEFAULT NULL COMMENT '检测结论',
  `final_conclusion`        VARCHAR(256)  DEFAULT NULL COMMENT '最终结论',
  `created_at`              DATETIME      DEFAULT NULL COMMENT '创建时间',
  `updated_at`              DATETIME      DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);


ALTER TABLE `snz_module_quotas`
ADD COLUMN `module_num` VARCHAR(32) NULL COMMENT '模块专用号' AFTER `module_id`;

# 2014-08-05 db change

# 新增MDM银行信息表
    CREATE TABLE `mdm_bank_views` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
      `code` varchar(64) DEFAULT NULL COMMENT '银行代号',
      `name` varchar(255) DEFAULT NULL COMMENT '银行名字',
      `country` varchar(64) DEFAULT NULL COMMENT '银行国家',
      PRIMARY KEY (`id`)
    );

# 新增核审人ID和名字到信用等级评价
    alter table `snz_supplier_credit_qualifies`
    add COLUMN `reviewer_name` varchar(64) default null comment '核审人名字' after `message`,
    add COLUMN `reviewer` bigint default null comment '核审人ID' after `message`;

#2014-08-06
#添加新的状态
ALTER TABLE `snz_requirement_send`
ADD COLUMN `business_negotiate` SMALLINT(6) NULL COMMENT '商务谈判状态（当状态为1时－》中选的供应商可以更改价格但是不能高于报价）' AFTER `reply_moduleNum`,
ADD COLUMN `supplier_sign` SMALLINT(6) NULL COMMENT '供应商跟标（当状态为1时－》供应商界面显示是否跟标信息此时供应商才能看到个人的配额信息）' AFTER `business_negotiate`,
ADD COLUMN `result_publicity` SMALLINT(6) NULL COMMENT '配额结果公示（当状态为1时－》用户在详细需求页面可以看到中标的用户的配额详细信息，供应商各自也能在本身后台查看配额信息）' AFTER `supplier_sign`;

#2014-08-07
#修改园区与工厂关系
ALTER TABLE `snz_address_factory`
DROP COLUMN `park_id`,
DROP COLUMN `product_id`;

-- -----------------------------------------------------
-- Table `snz_category_factory`  类目，工厂关系
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_category_factory`;

CREATE TABLE IF NOT EXISTS `snz_category_factory` (
  `id`                BIGINT          NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `product_id`        BIGINT          NOT NULL  COMMENT '产品类型（二级类目）',
  `park_id`           BIGINT          NOT NULL  COMMENT '园区编号',
  `factory_id`        BIGINT          NOT NULL  COMMENT '工厂Id',
  `created_at`        DATETIME        NULL      COMMENT '创建时间',
  `updated_at`        DATETIME        NULL      COMMENT '更改时间',
  PRIMARY KEY (`id`));


# Table `snz_supplier_tqrdc_infos_tmp` 供应atqrdc信息 外部导入表
CREATE TABLE IF NOT EXISTS `snz_supplier_tqrdc_infos_tmp` (
`id`                    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
`user_id`               bigint          NULL    COMMENT '用户id',
`company_id`            BIGINT          NULL    COMMENT '对应企业信息',
`special_number`        VARCHAR(20)     NULL        COMMENT '专用号',
`supplier_code`         VARCHAR(20)     NULL    COMMENT '供应商代码',
`supplier_name`         VARCHAR(20)     NULL    COMMENT '供应商名称',
`module`                VARCHAR(50)     NULL    COMMENT '模块',
`product_line_id`       SMALLINT        NULL        COMMENT '产品线ID',
`product_line`          varchar(128)    NULL        COMMENT '产品线',
`location`              VARCHAR(20)     NULL    COMMENT '区域',
`rank`                  VARCHAR(20)     NULL    COMMENT '排名',
`date`                  VARCHAR(8)      NULL    COMMENT '日期',
`composite_score`       INT             NULL    COMMENT '综合绩效',
`supplier_status`       int             NULL DEFAULT '0' COMMENT '供应商状态(0:正常|-1:警告|-2:整改|-3:停新品)',
`tech_score`            INT             NULL    COMMENT '技术得分',
`delay_days`            INT             NULL    COMMENT '拖期天数',
`new_product_pass`      INT             NULL    COMMENT '新品合格率',
`quality_score`         INT             NULL    COMMENT '质量得分',
`live_bad`              INT             NULL    COMMENT '现场不良率',
`market_bad`            INT             NULL    COMMENT '市场不良率',
`resp_score`            INT             NULL    COMMENT '响应得分',
`program_promised_rate` int             NULL        COMMENT '方案承诺率',
`program_selected_rate` int             NULL        COMMENT '方案选中率',
`program_adopted_rate`  int             NULL        COMMENT '方案落地率',
`requirement_resp`      INT             NULL    COMMENT '需求响应度',
`deliver_score`         INT             NULL    COMMENT '交付得分',
`deliver_diff`          INT             NULL    COMMENT '交付差异',
`cost_score`            INT             NULL    COMMENT '成本得分',
`increment`             INT             NULL    COMMENT '增值',
`tech_score_rank`       INT             NULL        COMMENT '技术得分排序',
`quality_score_rank`    INT             NULL        COMMENT '质量得分排序',
`resp_score_rank`       INT             NULL        COMMENT '响应得分排序',
`delivery_score_rank`   INT             NULL        COMMENT '交付得分排序',
`affect_delivery_count` INT             NULL        COMMENT '影响 T-1 交付次数',
`cost_score_rank`       INT             NULL        COMMENT '成本得分排序',
`price_reduction_amount` int            NULL        COMMENT '降价额',
`price_reduction_range`  int            NULL        COMMENT '降幅',
`created_at`            DATETIME        NULL,
`updated_at`            DATETIME        NULL,
PRIMARY KEY (`id`)
);
create index `indx_snz_supplier_tqrdc_infos_tmp_date` on snz_supplier_tqrdc_infos_tmp(`date`);



# 重建新品导入步骤明细表
CREATE TABLE `snz_new_product_steps` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `parent_id` bigint(20) NOT NULL COMMENT '新品导入流程步骤对于的导入实例ID',
  `module_num` varchar(64) DEFAULT NULL COMMENT '模块编号',
  `supplier_code` varchar(128) DEFAULT NULL COMMENT '供应商代码',
  `supplier_name` varchar(128) DEFAULT NULL COMMENT '供应商名称',
  `step` smallint(6) NOT NULL DEFAULT '0' COMMENT '流程节点(1:原件创建 | 2:原件发布 | 3:装配完成 | 4:收样确认 | 5:检测收样 | 6:检测开始 | 7:检测计划时间 | 8:检测完成)',
  `datetime` datetime DEFAULT NULL COMMENT '计划时间或日期',
  `duration` int(11) DEFAULT NULL COMMENT '周期',
  `real_datetime` datetime DEFAULT NULL COMMENT '时间进度时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态(-1:拖期 | 0:未进行 | 1:正常)',
  `in_charge` varchar(64) DEFAULT NULL COMMENT '责任人',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `snz_new_product_steps_module_num` (`module_num`),
  KEY `snz_new_product_steps_supplier_name` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16284 DEFAULT CHARSET=utf8;



# 表 snz_new_product_imports 新增字段
alter table `snz_new_product_imports`
	add column supplier_code varchar(128) null comment '供应商代码' after `id`,
	add column supplier_name varchar(128) null comment '供应商名称' after `supplier_code`;

# 2014-08-07
# 表 mdm_configures 新增字段
    alter table `mdm_configures`
    add parent varchar(64) comment 'Region的所属国家' after name;

# 添加查询筛选索引
    create index `indx_snz_modules_type` on snz_modules(`type`);

# 供应商绩效表新增 date 字段
alter table snz_supplier_tqrdc_infos add column `date` varchar(10) not null comment '日期' after `month`;


# 2014-08-08
#添加选择方案的原因信息到排名信息
ALTER TABLE `snz_requirement_ranks`
ADD COLUMN `select_reason` VARCHAR(256) NULL COMMENT '选择该方案的原因' AFTER `quota_scale`;

##删掉snz_company_extra_delivery表的prompt_delivery_rate字段
alter table snz_company_extra_delivery drop column prompt_delivery_rate;

##删掉snz_company_extra_delivery表的delivery_capacity字段
alter table snz_company_extra_delivery drop column delivery_capacity;

##删掉snz_company_extra_quality表的lab_area字段
alter table snz_company_extra_quality drop column lab_area;

##删掉snz_company_extra_quality表的number_of_reliability_testing字段
alter table numberOfReliabilityTesting drop column number_of_reliability_testing;

##删掉snz_company_extra_rd表的auth_id字段
alter table snz_company_extra_rd drop column auth_id;

##删掉snz_company_extra_rd表的auth_valid_date字段
alter table snz_company_extra_rd drop column auth_valid_date;

##删掉snz_company_extra_rd表的facility_area字段
alter table snz_company_extra_rd drop column facility_area;

##删掉snz_company_extra_rd表的number_of_reliability_testing字段
alter table snz_company_extra_rd drop column number_of_reliability_testing;

##删掉snz_company_extra_response表的interface_development_team_size_attach_url字段
alter table snz_company_extra_response drop column interface_development_team_size_attach_url;

##删掉snz_company_extra_response表的interface_development_team_size字段
alter table snz_company_extra_response drop column interface_development_team_size;

##删掉snz_company_extra_response表的quality_interface_team_size字段
alter table snz_company_extra_response drop column quality_interface_team_size；

##删掉snz_company_extra_response表的quality_interface_team_size_attach_url字段
alter table snz_company_extra_response drop column quality_interface_team_size_attach_url;

##删掉snz_company_extra_response表的sales_service_interface_team_size字段
alter table snz_company_extra_response drop column sales_service_interface_team_size;

##删掉snz_company_extra_response表的sales_service_interface_team_size_attach_url字段
alter table snz_company_extra_response drop column sales_service_interface_team_size_attach_url;

##删掉snz_company_extra_response表的unified_interface_jobs字段
alter table snz_company_extra_response drop column unified_interface_jobs;

##删掉snz_company_extra_response表的unified_interface_jobs_attach_url字段
alter table snz_company_extra_response drop column unified_interface_jobs_attach_url;

##删掉snz_company_extra_scale_and_cost表的market_share字段
alter table snz_company_extra_scale_and_cost drop column market_share;

##删掉snz_company_extra_scale_and_cost表的market_share_ranking字段
alter table snz_company_extra_scale_and_cost drop column market_share_ranking;

##删掉snz_company_extra_scale_and_cost表的market_share_attach_url字段
alter table snz_company_extra_scale_and_cost drop column market_share_attach_url;

##删掉snz_company_extra_scale_and_cost表的sales字段
alter table snz_company_extra_scale_and_cost drop column sales;

##删掉snz_company_extra_scale_and_cost表的sales_ranking字段
alter table snz_company_extra_scale_and_cost drop column sales_ranking;

##删掉snz_company_extra_scale_and_cost表的sales_attach_url字段
alter table snz_company_extra_scale_and_cost drop column sales_attach_url；

##删掉snz_company_extra_scale_and_cost表的annual_production字段
alter table snz_company_extra_scale_and_cost drop column annual_production;

##删掉snz_company_extra_scale_and_cost表的annual_production_ranking字段
alter table snz_company_extra_scale_and_cost drop column annual_production_ranking;

##删掉snz_company_extra_scale_and_cost表的annual_production_attach_url字段
alter table snz_company_extra_scale_and_cost drop column annual_production_attach_url;

##删掉snz_company_extra_scale_and_cost表的cost_ranking字段
alter table snz_company_extra_scale_and_cost drop column cost_ranking;

##在snz_company_extra_rd表中增加other_names字段
alter table snz_company_extra_rd add column other_names varchar(64);

##在snz_company_extra_rd表中增加sum_number_of_engineer字段
alter table snz_company_extra_rd add column sum_number_of_engineer int(11);

##在snz_company_extra_rd表中修改technology_ranking为other_ability_description字段
alter table snz_company_extra_rd change technology_ranking other_ability_description varchar(256);

##修改snz_company_extra_rd表中的number_of_national_technology_awards字段为national_technology_awards字段
alter table snz_company_extra_rd change number_of_national_technology_awards national_technology_awards varchar(256);

##修改snz_company_extra_rd表中的number_of_provincial_technology_awards字段为provincial_technology_awards字段
alter table snz_company_extra_rd change number_of_provincial_technology_awards provincial_technology_awards varchar(256);

##在snz_company_extra_rd表中增加award_level字段
alter table snz_company_extra_rd add column award_level int(5);

##在snz_company_extra_quality表中删除number_of_auditors字段
alter table snz_company_extra_quality drop column number_of_auditors;

##在snz_company_extra_quality表中增加exam_people字段
alter table snz_company_extra_quality add column exam_people int(11) after auditor_type;

##在snz_company_extra_quality表中增加test_people字段
alter table snz_company_extra_quality add column test_people int(11) after exam_people;

##在snz_company_extra_quality表中增加green_level字段
alter table snz_company_extra_quality add column green_level int(11) after test_people;

##在snz_company_extra_quality表中增加black_level字段
alter table snz_company_extra_quality add column black_level int(11) after green_level;

##在snz_company_extra_quality表中修改quality_ranking为other_ability_description字段
alter table snz_company_extra_quality  change quality_ranking other_ability_description varchar(256);

##在snz_company_extra_delivery表中修改number_of_equipments为list_of_equipments字段
alter table snz_company_extra_delivery change number_of_equipments list_of_equipments varchar(4096);

##在snz_company_extra_delivery表中修改number_of_automation_equipment为list_of_automation_equipment字段
alter table snz_company_extra_delivery change number_of_automation_equipment list_of_automation_equipment varchar(4096);

##在snz_company_extra_delivery表中修改production_capacity为open_rate字段
alter table snz_company_extra_delivery change production_capacity open_rate int(11);

##在snz_company_extra_delivery表中删除number_of_automation_equipment_attach_url字段
alter table snz_company_extra_delivery drop column number_of_automation_equipment_attach_url;

##在snz_company_extra_delivery表中删除number_of_equipments_attach_url字段
alter table snz_company_extra_delivery drop column number_of_equipments_attach_url;


# 2014-08-10
## 增加新的资质验证数据表
-- -----------------------------------------------------
-- Table `snz_supplier_qualify_infos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_qualify_infos`;

CREATE TABLE IF NOT EXISTS `snz_supplier_qualify_infos` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`supplier_id`         BIGINT        NOT NULL                COMMENT '供应商id',
`role_checked`        VARCHAR(128)  NULL                    COMMENT '已审核的责任位',
`status`              smallint      NOT NULL                COMMENT '审核状态',
`created_at`          datetime      DEFAULT NULL            COMMENT '创建时间',
`updated_at`          datetime      DEFAULT NULL            COMMENT '更新时间',
PRIMARY KEY (`id`)
);
CREATE INDEX `snz_supplier_qualify_infos_supplier_id_idx` ON `snz_supplier_qualify_infos`(`supplier_id`);

-- -----------------------------------------------------
-- Table `snz_supplier_qualify_logs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_qualify_logs`;

CREATE TABLE IF NOT EXISTS `snz_supplier_qualify_logs` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`supplier_id`         BIGINT        NOT NULL                COMMENT '供应商id',
`checker_id`          BIGINT        NOT NULL                COMMENT '审核员id',
`type`                INT           NOT NULL                COMMENT '0. 供应商提交申请，1. 一级审核, 2. 二级审核',
`status`              INT           NOT NULL                COMMENT '1. 通过, 2. 否决 (供应商提交申请默认为通过)',
`subject_id`          INT           NULL                    COMMENT '具体条目id，type为1是有用',
`attach_url`          VARCHAR(256)  NOT NULL                COMMENT '附件地址url',
`view`                VARCHAR(256)  NOT NULL                COMMENT '意见',
`created_at`          datetime      DEFAULT NULL            COMMENT '创建时间',
`updated_at`          datetime      DEFAULT NULL            COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_supplier_qualify_logs_supplier_id_idx` ON `snz_supplier_qualify_logs` (`supplier_id`);
CREATE INDEX `snz_supplier_qualify_logs_checker_id_idx` ON `snz_supplier_qualify_logs` (`checker_id`);

# 2014-08-11 db change
# 新建 snz_supplier_reparations 表
-- !! Drop is required! - by. zcd
drop table if exists snz_supplier_delived_data_analysis;

CREATE TABLE `snz_supplier_reparations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `identify` varchar(128) DEFAULT NULL COMMENT '退款唯一标示',
  `module_num` varchar(64) DEFAULT NULL COMMENT '物料号',
  `v_code` varchar(64) DEFAULT NULL COMMENT '供应商编码',
  `quality` int(11) DEFAULT NULL COMMENT '赔偿数量',
  `money` int(11) DEFAULT NULL COMMENT '赔偿金额，单位是分',
  `type` smallint(6) DEFAULT NULL COMMENT '1-入厂，2-现场，3-（T-1），4-市场',
  `updated_at` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
);

#更改用户编号的数据类型
ALTER TABLE `snz_requirement_teams`
CHANGE COLUMN `user_number` `user_number` VARCHAR(32) NOT NULL COMMENT '队员工号' ;


# 2014-08-12 db change
## 新建 snz_file_stacks 表
    create table if not exists `snz_file_stacks` (
        `id`			bigint 				not null primary key,
        `name`			varchar(512)		default null COMMENT '文件名',
        `size`			bigint				default null comment '可选，文件大小，单位为b',
        `extend`		varchar(32)			default null comment '文件后缀',
        `mime`			varchar(128)		default null comment 'mime type',
        `hash`			varchar(64)			default null comment '文件hash，文件名hash + timestamp',
        `owner`			bigint				default null comment '文件持有者',
        `uploaded_at`	datetime			default null comment '持有者上传时间',
        `modify_by`		bigint				default null comment '文件修改人',
        `modified_at` 	datetime			default null comment '修改时间',
        `uri`			varchar(1024)		default null comment '文件uri',
        `stack`			integer				default null comment '归属表',
        `deleted`		tinyint				default null comment '逻辑删除文件',

        `updated_at`		datetime			not null,
        `created_at` 		datetime			not null
    );

    CREATE INDEX `snz_file_stacks_file_hash` on snz_file_stacks(`hash`);

## 不能为null，默认为空字符串
alter table snz_supplier_qualify_infos modify role_checked varchar(128) not null default '';
## 资质检查日志字段修改
alter table snz_supplier_qualify_logs modify checker_id bigint null;
alter table snz_supplier_qualify_logs modify attach_url varchar(128) null;
alter table snz_supplier_qualify_logs modify view varchar(128) null;

## 增加供应商名冗余（分页查找用）
alter table snz_supplier_qualify_infos add column `supplier_name` varchar(80) not null after supplier_id;

#2014-08-13 db change

-- -----------------------------------------------------
-- Table `snz_compensation_details`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `snz_compensation_details`;

CREATE TABLE IF NOT EXISTS `snz_compensation_details` (
`id`                    bigint(20)        NOT NULL AUTO_INCREMENT   COMMENT '自增主键',
`list_id`               bigint(20)        DEFAULT NULL              COMMENT '索赔记录id',
`park`                  varchar(20)       DEFAULT NULL              COMMENT '园区',
`product_line`          varchar(20)       DEFAULT NULL              COMMENT '产业线',
`supplier_account`      varchar(20)       DEFAULT NULL              COMMENT '供应商或债权人的账号',
`supplier_name`         varchar(35)       DEFAULT NULL              COMMENT '供应商名称',
`is_internal`           tinyint(1)        DEFAULT NULL              COMMENT '内外部供应商标志, 0内部, 1外部',
`factory`               varchar(4)        DEFAULT NULL              COMMENT '工厂',
`factory_name`          varchar(30)       DEFAULT NULL              COMMENT '工厂名称',
`materiel_no`           varchar(18)       DEFAULT NULL              COMMENT '物料号',
`materiel_desc`         varchar(40)       DEFAULT NULL              COMMENT '物料描述, 短文本',
`purchaser_group`       varchar(3)        DEFAULT NULL              COMMENT '采购组',
`purchaser_group_desc`  varchar(40)       DEFAULT NULL              COMMENT '采购组描述',
`score`                 int(11)           DEFAULT NULL              COMMENT '扣分',
`is_money`              tinyint(1)        DEFAULT NULL              COMMENT '需要扣款标志, 0不需要, 1需要',
`money`                 int(11)           DEFAULT NULL              COMMENT '扣除金额',
`deducted_at`           date              DEFAULT NULL              COMMENT '扣除日期',
`current`               date              DEFAULT NULL              COMMENT '当前日期',
`status`                tinyint(1)        DEFAULT NULL              COMMENT '申诉状态',
`result`                varchar(200)      DEFAULT NULL              COMMENT '扣款结果',
`entered_at`            date              DEFAULT NULL              COMMENT '凭证入账日期',
`created_at`            datetime          DEFAULT NULL              COMMENT '创建时间',
`updated_at`            datetime          DEFAULT NULL              COMMENT '更新时间',
`major_duty`            varchar(100)      DEFAULT NULL              COMMENT '主营业务',
`grade`                 int(11)           DEFAULT NULL              COMMENT '绩效得分',
`num_of_compensation`   int(11)           DEFAULT NULL              COMMENT '近一个月索赔次数',
`loss`                  int(20)           DEFAULT NULL              COMMENT '损失钱数',
PRIMARY KEY (`id`)
);
CREATE INDEX `snz_compensation_details_id_idx` ON `snz_compensation_details` (`id`);
CREATE INDEX `snz_compensation_details_list_id_idx` ON `snz_compensation_details` (`list_id`);

-- -----------------------------------------------------
-- Table `snz_compensation_replies`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `snz_compensation_replies`;

CREATE TABLE IF NOT EXISTS `snz_compensation_replies` (
`id`                    bigint(20)        NOT NULL AUTO_INCREMENT   COMMENT '自增主键',
`user_id`               bigint(20)        DEFAULT NULL              COMMENT '用户id',
`did`                   bigint(20)        DEFAULT NULL              COMMENT '明细id',
`content`               varchar(1024)     DEFAULT NULL              COMMENT '会话内容',
`company_name`          varchar(64)       DEFAULT NULL              COMMENT '公司名',
`files`                 varchar(1024)     DEFAULT NULL              COMMENT '附件url列表',
`created_at`            datetime          DEFAULT NULL              COMMENT '创建时间',
`updated_at`            datetime          DEFAULT NULL              COMMENT '更新时间',
PRIMARY KEY (`id`)
);
CREATE INDEX `snz_compensation_replies_id_idx` ON `snz_compensation_replies` (`id`);
CREATE INDEX `snz_compensation_replies_did_idx` ON `snz_compensation_replies` (`did`);

# 2014-08-13 db change
## add refund at ( <-- 线上数据库执行到这里 )
    alter table `snz_supplier_reparations`
    add `refund_at` datetime DEFAULT NULL COMMENT '赔款日期' after `type`;

## add middle table `mw_old_module_infos`  ( <-- 线上数据库执行到这里 )
    CREATE TABLE `mw_old_module_infos` (
      `id` bigint(20) NOT NULL,
      `module_num` varchar(32) NOT NULL COMMENT '模块专用号、物料号',
      `module_name` varchar(32) NOT NULL COMMENT '模块描述',
      `series_id` int(11) DEFAULT NULL COMMENT '三级类目名',
      `unit` varchar(32) NOT NULL COMMENT '资源量单位',
      `created_at` datetime NOT NULL,
      `updated_at` datetime NOT NULL,
      PRIMARY KEY (`id`)
    );

## 后台类目属性中增加工厂编号字段 ( <-- 线上数据库执行到这里 )
alter table snz_backend_category_properties add column factory_num varchar(10) null after `type`;


# Table `snz_user_complaints` 用户抱怨信息录入表 ( <-- 线上数据库执行到这里 )
CREATE TABLE `snz_user_complaints`(
  `id`                      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`                 BIGINT       NOT NULL COMMENT '抱怨人ID',
  `user_name`               VARCHAR(64)  NULL COMMENT '抱怨人',
  `product_line_id`         INT          NULL COMMENT '产品线ID',
  `product_line_name`       VARCHAR(64)  NULL COMMENT '产品线名称:后台二级类目',
  `frontend_category_id`    BIGINT       NULL COMMENT '模块:前台一级类目',
  `frontend_category_name`  VARCHAR(64)  NULL COMMENT '模块-前台一级类目',
  `factory_num`             INT          NULL COMMENT '生产工厂编号',
  `factory_name`            VARCHAR(65)  NULL COMMENT '工厂名称',
  `product_owner_id`        BIGINT       NULL COMMENT '产品负责人ID',
  `product_owner_name`      VARCHAR(64)  NULL COMMENT '产品负责人',
  `complaint_types`         VARCHAR(128) NULL COMMENT '抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))',
  `supplier_code`           VARCHAR(64)  NULL COMMENT '供应商CODE',
  `supplier_name`           VARCHAR(128) NULL COMMENT '供应商名称',
  `module_id`               BIGINT       NULL COMMENT '模块号',
  `module_name`             VARCHAR(64)  NULL COMMENT '模块名称',
  `complaint_content`       VARCHAR(512) NULL COMMENT '抱怨回馈内容',
  `complaint_reason`        VARCHAR(128) NULL COMMENT '抱怨原因',
  `created_at`              DATETIME     NOT NULL COMMENT '创建时间',
  `updated_at`              DATETIME     NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `indx_snz_user_complaints_user_id` ON `snz_user_complaints`(`user_id`);


# snz_supplier_tqrdc_infos 表新增字段 ( <-- 线上数据库执行到这里 )
alter table `snz_supplier_tqrdc_infos`
	add column tech_location varchar(8) null comment '技术得分区位' after tech_score_rank;
alter table `snz_supplier_tqrdc_infos`
	add column quality_location varchar(8) null comment '质量得分区位' after quality_score_rank;
alter table `snz_supplier_tqrdc_infos`
	add column resp_location varchar(8) null comment '响应得分区位' after resp_score_rank;
alter table `snz_supplier_tqrdc_infos`
	add column delivery_location varchar(8) null comment '交付得分区位' after delivery_score_rank;
alter table `snz_supplier_tqrdc_infos`
	add column cost_location varchar(8) null comment '成本得分区位' after cost_score_rank;

##删除并重建snz_compensation_details表
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

##修改snz_compensation_replies表的did字段为list_id字段
alter table snz_compensation_replies change did list_id bigint(20);

# 2014-08-13 db change
## 完善公司信息表，关于万元的修改
alter table snz_company_extra_rd modify investment_this_year bigint;
alter table snz_company_extra_rd modify investment_last_year bigint;
alter table snz_company_extra_rd modify investment_before_last_year bigint;
alter table snz_company_extra_rd modify assets bigint;

alter table snz_company_extra_quality modify investment_this_year bigint;
alter table snz_company_extra_quality modify investment_last_year bigint;
alter table snz_company_extra_quality modify investment_befor_last_year bigint;

## snz_users表添加入驻审核通过时间字段
alter table snz_users
add column `enter_pass_at` DATETIME  NULL   COMMENT '入驻审核通过时间' after `approve_status`;

## 修改snz_compensation_replies表的content字段,增加长度
alter table snz_compensation_replies modify content varchar(4000);


## Table snz_product_owners 产品负责人信息表  此处需 drop 重建
CREATE TABLE `snz_product_owners`(
  `id`                BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `factory_num`       VARCHAR(64)  NOT NULL COMMENT '工厂ID',
  `factory_name`      VARCHAR(128) NULL COMMENT '工程名称',
  `product_line_id`   INT          NOT NULL COMMENT '产品线ID',
  `product_line_name` VARCHAR(128) NULL COMMENT '产品线名称',
  `owner_id`          BIGINT       NULL COMMENT '产品负责人ID',
  `owner_name`        VARCHAR(128) NULL COMMENT '产品负责人',
  `created_at`        DATETIME     NOT NULL COMMENT '创建时间',
  `updated_at`        DATETIME     NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `indx_snz_product_owners_factory_num` ON `snz_product_owners`(`factory_num`);
CREATE INDEX `indx_snz_product_owners_p_l_n` ON `snz_product_owners`(`product_line_name`);

# 2014-08-15
## 创建市场不良中间表
    create TABLE `mw_supplier_market_bad_records` (
        id				bigint not null AUTO_INCREMENT primary key,
        `report_at`     datetime default null comment '报表日期',
        `port`          varchar(256) default null comment '工贸',
        `pl`            varchar(2048) default null comment '产品大类',
        `business`      varchar(2048) default null comment '事业部',
        `branch`        varchar(256) default null comment '派工网点',
        `product_id`    varchar(2048) default null comment '产品编号',
        `product_sid`   varchar(128) default null comment '产品序列号',
        `assembled_at`  datetime default null comment '生产日期',
        `log_at`        datetime default null comment '登记时间',
        `complain`      varchar(4096) default null comment '用户抱怨',
        `maint_type`    varchar(32) default null comment '保修类型',
        `service_type`  varchar(2048) default null comment '实际服务类型',
        `describe`      varchar(2048) default null comment '故障现象',
        `cause`         varchar(2048) default null comment '故障原因',
        `object`        varchar(512) default null comment '对象',
        `action`        varchar(128) default null comment '维修措施',
        `module_num`    varchar(128) default null comment '备件专用号',
        `v_code`        varchar(128) default null comment 'v码',
        `price`         varchar(16) default null comment '备件成本',
        `fee`           varchar(16) default null comment '售后费用',
        `module_name`   varchar(512) default null comment '备件名称',
        `status`        varchar(64) default null comment '备件使用状态'
    );

## 创建市场不良表
    CREATE TABLE `snz_supplier_market_bad_records` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `report_at` datetime DEFAULT NULL COMMENT '报表日期',
      `port` varchar(256) DEFAULT NULL COMMENT '工贸',
      `pl` varchar(2048) DEFAULT NULL COMMENT '产品大类',
      `business` varchar(2048) DEFAULT NULL COMMENT '事业部',
      `branch` varchar(256) DEFAULT NULL COMMENT '派工网点',
      `product_id` varchar(2048) DEFAULT NULL COMMENT '产品编号',
      `product_sid` varchar(128) DEFAULT NULL COMMENT '产品序列号',
      `assembled_at` datetime DEFAULT NULL COMMENT '生产日期',
      `log_at` datetime DEFAULT NULL COMMENT '登记时间',
      `complain` varchar(4096) DEFAULT NULL COMMENT '用户抱怨',
      `maint_type` varchar(32) DEFAULT NULL COMMENT '保修类型',
      `service_type` varchar(2048) DEFAULT NULL COMMENT '实际服务类型',
      `describe` varchar(2048) DEFAULT NULL COMMENT '故障现象',
      `cause` varchar(2048) DEFAULT NULL COMMENT '故障原因',
      `object` varchar(512) DEFAULT NULL COMMENT '对象',
      `action` varchar(128) DEFAULT NULL COMMENT '维修措施',
      `module_num` varchar(128) DEFAULT NULL COMMENT '备件专用号',
      `v_code` varchar(128) DEFAULT NULL COMMENT 'v码',
      `price` varchar(16) DEFAULT NULL COMMENT '备件成本',
      `fee` varchar(16) DEFAULT NULL COMMENT '售后费用',
      `module_name` varchar(512) DEFAULT NULL COMMENT '备件名称',
      `status` varchar(64) DEFAULT NULL COMMENT '备件使用状态',
      PRIMARY KEY (`id`)
    );


## 新增索引 (<-- 线上数据库执行到这里)
create index `indx_snz_supplier_tqrdc_infos_user_id` on `snz_supplier_tqrdc_infos`(`user_id`);

##删除审核额外信息表，新增审核记录表
DROP TABLE IF EXISTS `snz_supplier_approve_extra` ;

-- 供应商审核记录表
-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_approve_logs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_supplier_approve_logs` (
  `id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`             BIGINT        NOT NULL                COMMENT '用户id',
  `approve_type`        SMALLINT      NULL                    COMMENT '审核类型(1:入驻审核，2：修改信息审核)',
  `approved_at`         DATETIME      NULL                    COMMENT '审核时间',
  `approver_id`         BIGINT        NULL                    COMMENT '审核人编号',
  `approver_name`       VARCHAR(50)   NULL                    COMMENT '审核人名称',
  `approve_result`      SMALLINT      NULL                    COMMENT '审核结果(-1:审核不通过，1：审核通过)',
  `description`         VARCHAR(512)  NULL                    COMMENT '审核意见',
  `reg_info_updated_at` DATETIME      NULL                    COMMENT '注册信息更新时间',
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_supplier_approve_logs_user_id_idx` ON `snz_supplier_approve_logs` (`user_id`);
CREATE INDEX `snz_supplier_approve_logs_approve_type_idx` ON `snz_supplier_approve_logs` (`approve_type`);

# 2014-08-17 db change
## 工厂总监表
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

# 2014-08-17 db change
## 增加事业部名字
    alter TABLE `snz_supplier_scene_bad_records`
    add `depart_name` varchar(64) default null comment '部门名称' after `depart`;


#新增索引
create index `indx_snz_supplier_tqrdc_infos_user_id` on `snz_supplier_tqrdc_infos`(`user_id`);

#修改 tqrdc 表某些字段可为空
alter table snz_supplier_tqrdc_infos modify  `live_bad` int(11) null default 0 COMMENT '现场不良率';

# 修改snz_supplier_scene_bad_records表结构
    drop table snz_supplier_scene_bad_records;

    CREATE TABLE `snz_supplier_scene_bad_records` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `w_id` varchar(256) DEFAULT NULL,
      `module_num` varchar(128) DEFAULT NULL,
      `v_code` varchar(128) DEFAULT NULL,
      `w_count` varchar(256) DEFAULT NULL,
      `send_at` datetime DEFAULT NULL,
      `depart` varchar(128) DEFAULT NULL,
      `depart_name` varchar(64) default null,
      `created_at` datetime DEFAULT NULL,
      `updated_at` datetime DEFAULT NULL,
      `load_batch` varchar(32) DEFAULT NULL,
      `money` varchar(256) DEFAULT NULL,
      PRIMARY KEY (`id`)
    );


# 修改表
alter table snz_user_complaints add column `claim_amount` int null comment '赔偿金额' after complaint_reason;
alter table snz_user_complaints add column `claim_doc` varchar(128) null comment '赔偿证明文件路径' after claim_amount;


# 2014-08-19 db change
# 新增需求预期时间信息
CREATE TABLE IF NOT EXISTS `snz_req_predict_times` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求模版编号',
  `type`                SMALLINT    NOT NULL  COMMENT '交互状态时间（1:需求交互,2:需求锁定,3:方案交互,4:方案综投,5:选定供应商&方案）',
  `predict_start`       DATE        NOT NULL  COMMENT '预计开始时间',
  `predict_end`         DATE        NOT NULL  COMMENT '预计结束时间',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_req_predict_times_requirement_id ON snz_req_predict_times (requirement_id);

# 2014-08-20 db change
#新增模块于工厂的对应关系数据
CREATE TABLE `snz_module_factory` (
  `id`                      BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `module_id`               BIGINT        NULL COMMENT '模块编号',
  `factory_num`             VARCHAR(32)   NULL COMMENT '工厂编号（9889）',
  `factory_name`            VARCHAR(32)   NULL COMMENT '工厂名称',
  `property_id`             BIGINT        NULL COMMENT '针对于工厂的类目属性',
  `select_num`              INT           NULL COMMENT '对应工厂选取的中标供应商人数',
  `resource_num`            INT           NULL COMMENT '工厂的资源需求数量',
  `sales_id`                INT           NULL COMMENT '工厂的资源单位编号',
  `sales_name`              VARCHAR(16)   NULL COMMENT '工厂的资源的单位名称',
  `created_at`              DATETIME      NULL COMMENT '创建时间',
  `updated_at`              DATETIME      NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX snz_module_factory_module_id ON snz_module_factory (module_id);
CREATE INDEX snz_module_factory_property_id ON snz_module_factory (property_id);

# 删除模块的工厂资源量配置
ALTER TABLE `snz_modules`
DROP COLUMN `resource_num`;

# 删除模块配额的资源量配置
ALTER TABLE `snz_module_quotas`
ADD COLUMN `module_factoryId` VARCHAR(16) NULL COMMENT '模块工厂配额编号' AFTER `module_name`;

#添加模块配额的资源量信息
ALTER TABLE `snz_module_quotas`
CHANGE COLUMN `module_factoryId` `module_factoryId` BIGINT NULL DEFAULT NULL COMMENT '模块工厂配额编号' ,
ADD COLUMN `factory_num` VARCHAR(16) NULL COMMENT '工厂编号信息' AFTER `module_factoryId`;

ALTER TABLE `snz_requirements`
CHANGE COLUMN `creator_phone` `creator_phone` VARCHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL COMMENT '需求创建人手机' ;

ALTER TABLE `snz_requirement_teams`
CHANGE COLUMN `user_number` `user_number` VARCHAR(32) NOT NULL COMMENT '队员工号' ,
CHANGE COLUMN `user_phone` `user_phone` VARCHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL COMMENT '队员联系电话' ;

ALTER TABLE `snz_module_solutions`
ADD COLUMN `units` VARCHAR(128) NULL AFTER `cost`;

## 加长user表中固话长度
alter table snz_users modify phone varchar(20);

# 2014-08-21 db change
# 增加采购商权限细则，细分到类目
CREATE TABLE IF NOT EXISTS `snz_purchaser_authorities` (
  `id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`       BIGINT      NOT NULL COMMENT '用户id',
  `type`          INT         NOT NULL COMMENT '暂定: 1. 后台类目, 2. 前台类目',
  `content`       VARCHAR(20) NOT NULL COMMENT '当权限类型都应为类目时存放类目id, 其他情况视type另定',
  `rich_content`  VARCHAR(256)    NULL COMMENT '保留字段，当需要存放更复杂信息时',
  `position`      INT         NOT NULL COMMENT '职位; 暂定: 1. 小微主, 2. 小微成员',
  `role`          VARCHAR(20) NOT NULL COMMENT '职责，@see User.JobRole',
  `created_at`    DATETIME    NOT NULL COMMENT '创建时间',
  `updated_at`    DATETIME    NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `snz_purchaser_authorities_user_id_idx` ON `snz_purchaser_authorities` (`user_id`);
CREATE INDEX `snz_purchaser_authorities_content_idx` ON `snz_purchaser_authorities` (`content`);


alter table snz_user_complaints modify complaint_types varchar(256) null comment '用户抱怨类型:(包装运输协议||{T-1:{claimAmount:2000}})';
alter table snz_user_complaints modify claim_amount bigint null comment '索赔金额';
alter table snz_user_complaints modify factory_num  varchar(64) null comment '工厂编号';

# 2014-08-23
alter table snz_user_complaints change module_id module_num varchar(64) null comment '物料编号';

create index `indx_snz_supplier_tqrdc_infos_tmp_supplier_code` on `snz_supplier_tqrdc_infos_tmp`(`supplier_code`);

# 2014-08-26 db change

ALTER TABLE `snz_modules`
DROP COLUMN `attestation_id`,
CHANGE COLUMN `attestation` `attestations` VARCHAR(512) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL COMMENT '认证要求[{id:10,name:\'EU认证\'}, {id:10,name:\'EU认证\'}]' ;

ALTER TABLE `snz_modules`
CHANGE COLUMN `quality` `quality` INT(11) NULL DEFAULT NULL COMMENT '质量目标（可以有创建者or团队人员填写）' ,
CHANGE COLUMN `cost` `cost` INT(11) NULL DEFAULT NULL COMMENT '成本目标' ,
CHANGE COLUMN `delivery` `delivery` INT(11) NULL DEFAULT NULL COMMENT '产能要求' ,
CHANGE COLUMN `attestations` `attestations` VARCHAR(512) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL COMMENT '认证要求[{id:10,name:\'EU认证\'}, {id:10,name:\'EU认证\'}]' ;

# 2014-08-27 db change
# 添加方案排名的上传文档
ALTER TABLE `snz_requirement_ranks`
ADD COLUMN `select_file` VARCHAR(128) NULL COMMENT '选择该方案的上传文档' AFTER `select_reason`;



# 2014-08-27  NPI 导入表新增字段 module_id,supplier_code,supplier_name
alter table snz_new_product_imports add `module_id` bigint null comment '模块ID' after `document_name`;
alter table snz_new_product_imports add `supplier_code` varchar(128) null comment '供应商编号' after `id`;
alter table snz_new_product_imports add `supplier_name` varchar(128) null comment '供应商名称' after `supplier_code`;
create index `snz_new_product_imports_s_n` on `snz_new_product_imports`(`supplier_name`);

alter table snz_new_product_imports change part_number module_num varchar(64) null comment '模块编号';



# 2014-08-28 创建根据模块统计tqrdc相关绩效表

CREATE TABLE IF NOT EXISTS `snz_supplier_module_count` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`module_id`           BIGINT        NOT NULL                COMMENT '模块编号',
`module_name`         VARCHAR(50)   NOT NULL                COMMENT '模块名称',
`supplier_count`      BIGINT        NULL                    COMMENT '供应商数量',
`best_count`          BIGINT        NULL                    COMMENT '优选供应商数量',
`standard_count`      BIGINT        NULL                    COMMENT '合格供应商数量',
`limited_count`       BIGINT        NULL                    COMMENT '限制供应商数量',
`bad_count`           BIGINT        NULL                    COMMENT '淘汰供应商数量',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
 PRIMARY KEY (`id`)
);

alter table snz_user_complaints add `claim_total` bigint null default 0 comment  '索赔合计'  after `claim_doc`;
alter table snz_user_complaints add `score_total` bigint null default 0 comment  '积分合计'  after `claim_total`;


# 2014-08-29 db change
# 重做的资质交互
-- -----------------------------------------------------
-- Table `snz_supplier_resource_material_infos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_supplier_resource_material_infos` (
  `id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id`         BIGINT        NOT NULL COMMENT '供应商id',
  `supplier_name`       VARCHAR(80)   NOT NULL COMMENT '供应商名称 (冗余, 分页查找用)',
  `approved_modules`    VARCHAR(256)  NOT NULL COMMENT '已通过审核的类目, "[{id:1,name:c1},{id:3,name:c3},..]"',
  `times`               BIGINT        NOT NULL COMMENT '审核的次数，标识当前是第几次审核，第一次为1',
  `last_submit_time`    DATETIME      NOT NULL COMMENT '最近提交时间（用于周期管理）',
  `status`              TINYINT       NOT NULL COMMENT '审核状态',
  `created_at`          DATETIME      NULL COMMENT '创建时间',
  `updated_at`          DATETIME      NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `snz_supplier_resource_material_infos_supplier_id_idx` ON `snz_supplier_resource_material_infos`(`supplier_id`);

-- -----------------------------------------------------
-- Table `snz_supplier_resource_material_logs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_supplier_resource_material_logs` (
  `id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id`   BIGINT      NOT NULL COMMENT '供应商id',
  `checker_id`    BIGINT      NOT NULL COMMENT '审核员id',
  `times`         BIGINT      NOT NULL COMMENT '审核次数，唯一确定一次完整的审核 @see SupplierResourceMaterialInfo.times',
  `type`          TINYINT     NOT NULL COMMENT '1. 一级审核, 2. 二级审核, 5. 供应商提交申请, 6. 系统自动提交申请, 7. 系统自动设为最终通过',
  `status`        TINYINT     NOT NULL COMMENT '0. 否决, 1. 通过 (供应商提交申请默认为通过, 系统自动提交申请亦同)',
  `content`       VARCHAR(4096) NOT NULL DEFAULT '' COMMENT '视type而定，若type为1. 一级审核，暂定存放 @see SupplierQualifyRecordDto',
  `created_at`    DATETIME    NULL COMMENT '创建时间',
  `updated_at`    DATETIME    NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `snz_supplier_resource_material_logs_supplier_id_idx` ON `snz_supplier_resource_material_logs`(`supplier_id`);
CREATE INDEX `snz_supplier_resource_material_logs_checker_id_idx` ON `snz_supplier_resource_material_logs`(`checker_id`);
CREATE INDEX `snz_supplier_resource_material_logs_times_idx` ON `snz_supplier_resource_material_logs`(`times`);

-- -----------------------------------------------------
-- Table `snz_supplier_resource_material_subjects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_supplier_resource_material_subjects` (
  `id`        BIGINT          NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name`      VARCHAR(128)    NOT NULL COMMENT '资质交互审核条目名称',
  `type`      TINYINT         NOT NULL COMMENT '0. 非否决项, 1. 否决项',
  `role`      VARCHAR(32)     NOT NULL COMMENT '检查此条目的权限位, @see User.JobRole',
  `status`    TINYINT         NOT NULL COMMENT '0. 未生效, 1. 生效',
  `version`   BIGINT          NOT NULL COMMENT '标注是第几次的修改版本，默认从1开始',
  `created_at`  DATETIME      NULL COMMENT '创建时间',
  `updated_at`  DATETIME      NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

# 2014-08-29 新增需求的流水码&需求的物料类别名称
ALTER TABLE `snz_requirements`
CHANGE COLUMN `materiel_type` `materiel_type` BIGINT(20) NOT NULL COMMENT '整体需求级别的物料类别（1:SKD，2:模块，3:连接件，4:包装印刷辅料，5:二三级物料）' ,
ADD COLUMN `flow_id` VARCHAR(32) NULL COMMENT '需求统一的流水码' AFTER `id`,
ADD COLUMN `materiel_name` VARCHAR(32) NULL COMMENT '物料类别名称' AFTER `materiel_type`;

# 模块的流水码
ALTER TABLE `snz_modules`
ADD COLUMN `module_flowNum` VARCHAR(32) NULL COMMENT '模块统一的流水码' AFTER `id`;

# 2014-08-30 新增需求的报价方案文档
ALTER TABLE `snz_requirement_solutions`
ADD COLUMN `quotation_file` VARCHAR(256) NULL COMMENT '上传的详细的报价文档信息' AFTER `solution_file`;

# 2014-09-01 新增需求方案的话题编号
ALTER TABLE `snz_requirement_solutions`
CHANGE COLUMN `qualify_status` `topic_id` BIGINT NULL DEFAULT NULL COMMENT '对应供应商的唯一的topicId为了方便交谈（完全就是一个扯淡的交互）' ;


#供应商绩效字段是否为 null 改变
alter table snz_supplier_tqrdc_infos modify user_id bigint null comment '系统注册用户ID';
alter table snz_supplier_tqrdc_infos modify company_id bigint null  comment '系统注册公司ID';

# 新增供应商资源小微对应关系中间表
    create table `snz_suppliers_resources` (
        id bigint not null primary key AUTO_INCREMENT,
        v_code varchar(64) not null comment '供应商编码',
        supplier_name varchar(255) default null comment '供应商名字',
        product varchar(32) not null comment '产品线',
        user_id bigint not null comment '资源小微id',
        user_name varchar(64) not null comment '资源小微用户名字'
    ) comment '资源小微供应商对应关系中间表';

# 新增供应商用户评价统计表
    create table `snz_supplier_reparation_sumaries` (
        id bigint not null primary key AUTO_INCREMENT,
        v_code varchar(64) not null comment '供应商编码',
        daily_count		int	default 0 comment '昨日统计',
        weekly_count	int	default 0 comment '每周统计',
        monthly_count	int	default 0 comment '每月统计',
        yearly_count 	int	default 0 comment '每月统计',

        daily_amount	bigint	default 0 comment '昨日统计',
        weekly_amount	bigint	default 0 comment '每周统计',
        monthly_amount	bigint	default 0 comment '每月统计',
        yearly_amount 	bigint	default 0 comment '每月统计',

        start_at datetime default null comment '统计数据开始累计时间',
        end_at datetime default null comment '统计数据结束累计时间',

        created_at datetime not null,
        updated_at datetime not null
    ) comment '供应商索赔损失信息统计表';

# 新增企业和主营业务中间临时表
-- 企业和主营业务中间临时表
-- -----------------------------------------------------
-- Table `snz`.`snz_company_main_businesses_tmp`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_company_main_businesses_tmp` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`company_id`          BIGINT        NOT NULL                COMMENT '对应企业id',
`user_id`             BIGINT        NOT NULL                COMMENT '用户id',
`first_level_id`      BIGINT        NULL                    COMMENT '一级类目编号',
`main_business_id`    BIGINT        NOT NULL                COMMENT '主营业务id（对应前台三级类目编号）',
`name`                VARCHAR(20)   NOT NULL                COMMENT '主营业务名称（冗余）（对应前台三级类目名称）',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_company_main_bussinesses_tmp_company_id_idx` ON `snz_company_main_businesses_tmp` (`company_id`);
CREATE INDEX `snz_company_main_bussinesses_tmp_user_id_idx` ON `snz_company_main_businesses_tmp` (`user_id`);
CREATE INDEX `snz_company_main_bussinesses_tmp_main_business_id_idx` ON `snz_company_main_businesses_tmp` (`main_business_id`);


# 2014-09-03
# 老品中间表新增三级类目名
    alter table `mw_old_module_infos`
    ADD `series_name` varchar(64) not null comment '三级类目名';


# 供应商绩效海尔管理者联系方式
CREATE TABLE `snz_tqrdc_manager_contacts`(
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主健',
  `name`          VARCHAR(64) NULL COMMENT '发送人姓名',
  `phone`         VARCHAR(16) NOT NULL COMMENT '联系方式',
  `template`      VARCHAR(512)  NULL COMMENT '短信模板',
  `remark`        VARCHAR(128) NOT NULL COMMENT '备注',
  `created_at`    DATETIME DEFAULT NULL COMMENT '创建时间',
  `updated_at`    DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
# 供应商联系方式
CREATE TABLE `snz_supplier_contacts`(
  `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主健',
  `supplier_name`   VARCHAR(64) NOT NULL COMMENT '供应商名称',
  `supplier_code`   VARCHAR(16) NOT NULL COMMENT '供应商编码',
  `phone`           VARCHAR(16) NOT NULL COMMENT '联系电话',
  `created_at`      DATETIME DEFAULT NULL COMMENT '创建时间',
  `updated_at`      DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);


create index idx_snz_replies_pid on snz_replies(pid);
alter table snz_replies add `receiver_name` varchar(128) null comment '回复给某人(@某人)' after receiver_id;


# 2014-09-03
# user表新增所处阶段字段
alter table snz_users
add `step`  SMALLINT   NULL  COMMENT '所处阶段' after `tags`;


# 2014-09-05
# 新增谈判价格到模块的报价单信息
ALTER TABLE `snz_module_quotations`
ADD COLUMN `transact_price` INT(11) NULL COMMENT '新增的谈判单价' AFTER `price`;

# 加大存储字段
alter table snz_supplier_resource_material_logs modify content text;

# 2014-09-09
# 字段改名
alter table snz_supplier_resource_material_infos change approved_modules approved_module_ids varchar(256) not null;
# 加大存储字段
alter table snz_supplier_resource_material_logs modify content text;


#2014-09-12
#增加供应商uid
alter table `snz_suppliers_resources`
add supplier_uid bigint default null comment '供应商的 user_id' after `supplier_name`;

# 添加财务字段
alter table `snz_company_finances`
add column   `main_business_profit` BIGINT  NOT NULL COMMENT '主营业务毛利' after `coin_type`,
add column   `manager_cost`  BIGINT  NOT NULL  COMMENT '管理费用' after `main_business_profit`,
add column   `pre_it_earning`  BIGINT  NOT NULL  COMMENT '息税前利润' after `manager_cost`,
add column   `pre_tax_profit`  BIGINT  NOT NULL    COMMENT '税前利润' after `pre_it_earning`;

# 删除财务字段
alter table `snz_company_finances`
drop column `main_business_profit`,
drop column `manager_cost`,
drop column `pre_it_earning`,
drop column `pre_tax_profit`;

## 2014-09-14
## 扩大公司描述
alter table snz_companies modify column `desc` text;

# 2014-09-14 新增用户抱怨交互信息表
CREATE TABLE `snz_complaint_chats`(
  `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主健',
  `parent_id`       BIGINT NOT NULL COMMENT '抱怨信息ID',
  `sender_id`       BIGINT NOT NULL COMMENT '消息发送者ID',
  `sender_name`     VARCHAR(128) NOT NULL COMMENT '消息发送者姓名或nick',
  `message`         VARCHAR(256) NOT NULL COMMENT '消息内容',
  `created_at`      DATETIME DEFAULT NULL COMMENT '创建时间',
  `updated_at`      DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `idx_snz_complaint_chats_parent_id` ON `snz_complaint_chats`(`parent_id`);

# 2014-09-11
# 修改衍生号差异表
ALTER TABLE snz_derivative_diff CHANGE matrix matrix  SMALLINT  NULL COMMENT  '模具(模块主关件符合衍生号要求，0:子件增加,1:子件减少,2:子件变更)' ;
ALTER TABLE snz_derivative_diff CHANGE material material  SMALLINT  NULL COMMENT  '材料（0:相同,1:不同）';
ALTER TABLE snz_derivative_diff CHANGE surface_treatment surface_treatment  SMALLINT  NULL COMMENT '表面处理工艺（0：喷涂，1：电镀，2：喷粉，3：其他）' ;
ALTER TABLE snz_derivative_diff CHANGE printing printing  SMALLINT  NULL COMMENT  '印刷（0：颜色，1：商标，2：文字，3：花纹）';
ALTER TABLE snz_derivative_diff CHANGE software_param software_param  SMALLINT  NULL COMMENT  '软件、参数（0：软件，1：参数）';
ALTER TABLE snz_derivative_diff CHANGE structure structure  SMALLINT  NULL COMMENT  '结构（0：同截面不同长度，1：加工细节）';
ALTER TABLE snz_derivative_diff CHANGE overseas_parts overseas_parts  SMALLINT  NULL COMMENT  '是否用于海外散件（0：否，1：是）';
ALTER TABLE snz_derivative_diff CHANGE host_change host_change  SMALLINT  NULL COMMENT  '主关件参数变化（0：电压频率，1：其他）' ;
ALTER TABLE snz_derivative_diff CHANGE drive drive  SMALLINT  NULL COMMENT  '接口相同，传动方式或传动比不同（传动方式：0，传动比：1）' ;
ALTER TABLE snz_derivative_diff ADD description  VARCHAR(256)  NULL COMMENT  '差异其他描述' ;

# 新增供应商申诉消息
    alter table `snz_supplier_credit_qualifies`
    add appeal_msg varchar(4096) default null comment '申诉消息' after message,
    modify message varchar(4096) default null comment '驳回消息';

# 市场不良建立索引
    create index snz_supplier_market_bad_records_report_at on `snz_supplier_market_bad_records`(`report_at`);
    create index mw_supplier_market_bad_records_report_at on `mw_supplier_market_bad_records`(`report_at`);

alter table snz_companies modify column `desc` text;


# 修改 snz_user_complaints 字段属性
  alter table `snz_user_complaints` MODIFY  `claim_doc`  varchar(256) null comment '索赔证明材料';

# 重要: 方案的历史版本, 发布前需要对数据进行订正
alter table snz_requirement_solutions modify column solution_file varchar(4096);

# 修数据
update snz_requirement_solutions set solution_file = concat('[',replace(solution_file,'}',','),'"version":"', DATE_FORMAT(updated_at, '%Y-%m-%d %H:%i:%s'), '"}]' ) where solution_file is not null and solution_file !='{}';

# 2014-09-16
## 扩大已通过类目id字段
alter table snz_supplier_resource_material_infos modify approved_module_ids text;

# 新增资源类型字段
alter table snz_companies
add column `resource_type`  SMALLINT   NULL    COMMENT '供应商资源类型：（0：普通资源，1：标杆企业，2：500强）' after `user_id`,
add column `competitors`  varchar(255)  NULL  COMMENT '竞争对手(对采购商而言)（多个用逗号分隔）' after `resource_type`;

# 修数据：重新生成老供应商信用等级验证表
    delete from `snz_supplier_credit_qualifies`;

    insert into `snz_supplier_credit_qualifies`
    (supplier_id, user_id, status, reviewer, reviewer_name, created_at, updated_at)
    select
    id, `user_id`, 5, 0, '海尔', now(), now()
    from `snz_companies`
    where `supplier_code` is not null;

# 修改用户平评价统计表
    alter table `snz_supplier_reparation_sumaries`
    drop column `v_code`,
    add `supplier_uid` bigint default null after id;

# 2014-09-17
## 新增未通过类目字段
alter table snz_supplier_resource_material_infos add column not_approved_module_ids text not null after approved_module_ids;

# 删除审核日志表的reg_info_updated_at字段（用created_at字段就行了）
alter table `snz_supplier_approve_logs` drop column reg_info_updated_at;

#user表新增last_submit_approval_at字段
alter table `snz_users` add column `last_submit_approval_at` DATETIME   NULL COMMENT '最近提交审核时间' after `step`;

# 2014-09-19
## 新增供应商组群关系表
CREATE TABLE `snz_supplier_group_relations` (
  `id`          BIGINT  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id` BIGINT  NOT NULL COMMENT '供应商编号',
  `group_id`    BIGINT  NOT NULL COMMENT '组群编号',
  `created_at`  DATETIME  COMMENT '创建时间',
  `updated_at`  DATETIME  COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_supplier_group_relations_supplier_id_idx` ON `snz_supplier_group_relations` (`supplier_id`);
CREATE INDEX `snz_supplier_group_relations_group_id_idx` ON `snz_supplier_group_relations` (`group_id`);

# 2014-09-22
# 增加工厂名称
alter table `snz_module_quotas`
add `factory_name` VARCHAR(128) NULL COMMENT '工厂名称' after `factory_num`;

# 增加上传最后阶段的谈判文件操作 
ALTER TABLE `snz_requirement_send`
ADD COLUMN `negotiate_file` VARCHAR(256) NULL COMMENT '商务谈判时必须要上传的文档信息' AFTER `business_negotiate`;

## 新增甲指库表
CREATE TABLE IF NOT EXISTS `snz_supplier_appoint` (
`id`                    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
`company_id`            BIGINT          NULL                    COMMENT '对应供应商公司company_id',
`requirement_id`        BIGINT          NULL                    COMMENT '需求id',
`requirement_name`      VARCHAR(80)     NULL                    COMMENT '需求name , 冗余字段，便于甲指库查询',
`series_ids`            VARCHAR(1024)   NOT NULL                COMMENT '系列编号(后台三级类目){sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}，冗余字段，便于通过供应商资质',
`creator_id`            BIGINT(20)      NOT NULL                COMMENT '创建需求的人员编号（用于索引对应的主管），冗余字段，便于像创建需求这发消息',
`corporation`           VARCHAR(80)     NULL                    COMMENT '法人公司，公司名',
`status`                INT             NOT NULL DEFAULT 0      COMMENT '审核状态，0 初始，1 已提交，2 初审通过，3 初审未通过，4 终审通过，5终审未通过',
`advice`                VARCHAR(80)     NULL                    COMMENT '审核意见',
`created_at`            DATETIME        NOT NULL,
`updated_at`            DATETIME        NOT NULL,
PRIMARY KEY (`id`)
);

# 供应商物料明细信息  snz_supplier_module_details  creation
CREATE TABLE `snz_supplier_module_details`(
  `id`              BIGINT(20)  UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主健',
  `module_num`      VARCHAR(32) NOT NULL COMMENT '物料号',
  `module_name`     VARCHAR(128)NULL COMMENT '物料名称',
  `supplier_code`   VARCHAR(16) NOT NULL COMMENT '供应商编码',
  `supplier_name`   VARCHAR(64) NOT NULL COMMENT '供应商名称',
  `purch_org`       VARCHAR(16) NULL COMMENT '采购组织',
  `purch_group`     VARCHAR(16) NULL COMMENT '采购组',
  `module_group`    VARCHAR(16) NULL COMMENT '物料组',
  `module_group_desc` VARCHAR(128)  NULL COMMENT '物料组描述',
  `tax_code`        VARCHAR(16)  NULL COMMENT '税码',
  `validity_start`  VARCHAR(16) NULL COMMENT '有效期开始',
  `validity_end`    VARCHAR(16) NULL COMMENT '有效期结束',
  PRIMARY KEY (`id`)
);
CREATE INDEX  `idx_snz_supplier_module_details_supplier_code` on `snz_supplier_module_details`(`supplier_code`);

# 增加需求问卷调查表
CREATE TABLE IF NOT EXISTS `snz_requirement_questionnaire` (
`id`                                BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`user_id`                           BIGINT        NOT NULL                COMMENT '用户id',
`company_id`                        BIGINT        NOT NULL                COMMENT '对应企业id',
`corporation`                       VARCHAR(128)  NOT NULL                COMMENT '公司名称',
`requirement_id`                    BIGINT        NOT NULL                COMMENT '需求编号',
`requirement_name`                  VARCHAR(64)   NOT NULL                COMMENT '需求名称',
`no_interface_or_package`           VARCHAR(256)  NULL                    COMMENT '无模块接口/封包标准',
`no_clear_interface_or_package`     VARCHAR(256)  NULL                    COMMENT '接口/封包标准不清晰',
`no_standard_interface_or_package`  VARCHAR(256)  NULL                    COMMENT '接口/封包非行业标准',
`less_order_count`                  VARCHAR(256)  NULL                    COMMENT '订单量较小',
`parks_not_match`                   VARCHAR(256)  NULL                    COMMENT '园区无法配套',
`short_period`                      VARCHAR(256)  NULL                    COMMENT '项目周期太短',
`other_reason`                      VARCHAR(256)  NULL                    COMMENT '其它原因',
`created_at`                        DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`                        DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_requirement_questionnaire_company_id_idx` ON `snz_requirement_questionnaire` (`company_id`);
CREATE INDEX `snz_requirement_questionnaire_user_id_idx` ON `snz_requirement_questionnaire` (`user_id`);
CREATE INDEX `snz_requirement_questionnaire_requirement_id_idx` ON `snz_requirement_questionnaire` (`requirement_id`);

# 修数据
update `snz_users`
set snz_users.tags= CONCAT(`snz_users`.tags, ',信用等级评价')
where not tags like '%信用等级评价%' and
id in (
select `user_id` from `snz_companies`
where `supplier_code` is not null
);

# snz_main_business_approvers表添加索引 (<-- test.ihaier.com 数据库执行到这里)(<-- l.ihaier.com 数据库执行到这里)
CREATE INDEX `snz_main_business_approvers_main_business_id_idx` ON `snz_main_business_approvers` (`main_business_id`);

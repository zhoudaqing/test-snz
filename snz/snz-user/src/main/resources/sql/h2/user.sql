-- 用户
-- -----------------------------------------------------
-- Table `snz`.`snz_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_users` ;

CREATE TABLE IF NOT EXISTS `snz_users` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
outer_id              bigint(20)    NULL                    COMMENT '外部用户id',
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
`enter_pass_at`       DATETIME      NULL                    COMMENT '入驻审核通过时间',
`origin`              SMALLINT      NULL                    COMMENT '用户来源,0：正常入驻，1:百卓接入，2：云平台导入',
`refuse_status`       SMALLINT      NULL                    COMMENT '审核拒绝状态,0：没有被拒绝过，-1:被拒绝过',
`qualify_status`      SMALLINT      NULL                    COMMENT '资质校验状态',
`tags`                VARCHAR(512)  NULL                    COMMENT '用户标签',
`step`                SMALLINT      NULL                    COMMENT '所处阶段',
`last_submit_approval_at` DATETIME   NULL                    COMMENT '最近提交审核时间',
`last_login_at`       DATETIME      NULL                    COMMENT '最近登录时间',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NULL                    COMMENT '更新时间',
PRIMARY KEY (`id`)
);
CREATE INDEX `snz_users_nick_idx` ON `snz_users` (`nick`);

-- -----------------------------------------------------
-- Table `snz`.`snz_purchaser_extras`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_purchaser_extras` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`user_id`             BIGINT        NOT NULL                COMMENT '对应采购商id',
`employee_id`         VARCHAR(20)   NOT NULL                COMMENT '工号',
`leader`              VARCHAR(16)   NOT NULL                COMMENT '上级',
`department`          VARCHAR(32)   NOT NULL                COMMENT '所属部门',
`position`            VARCHAR(20)   NOT NULL                COMMENT '职位',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_purchaser_extras_user_id_idx` ON `snz_purchaser_extras` (`user_id`);

-- 供应商模块处理
-- -----------------------------------------------------
-- Table `snz`.`snz_companies` 企业基本信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_companies` ;

CREATE TABLE IF NOT EXISTS `snz_companies` (
`id`                    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
`user_id`               BIGINT          NOT NULL                COMMENT '对应供应商id',
`resource_type`         SMALLINT        NULL                    COMMENT '供应商资源类型：（0：普通资源，1：标杆企业，2：500强）',
`competitors`           varchar(255)    NULL                    COMMENT '竞争对手(对采购商而言)（多个用逗号分隔）',
`is_complete`           SMALLINT        NULL                    COMMENT '供应商信息是否完整：（0：否，1：是）',
`include_keywords`      VARCHAR(50)     NULL                    COMMENT '公司名称包含的黑名单关键字',
`product_line`          VARCHAR(100)    NULL                    COMMENT '所属产品线(json)',
`corporation`           varchar(80)     NOT NULL                COMMENT '法人公司',
`corp_addr`             varchar(255)    NULL                    COMMENT '法人公司地址',
`supplier_code`         VARCHAR(20)     NULL                    COMMENT '供应商代码',
`acting_brand`          VARCHAR(20)     NULL                    COMMENT '代理品牌',
`zipcode`               VARCHAR(16)     NULL                    COMMENT '邮政编码',
`group_name`            varchar(80)     NULL                    COMMENT '集团名',
`group_addr`            varchar(255)    NULL                    COMMENT '集团地址',
`init_agent`            varchar(80)     NOT NULL                COMMENT '法人代表',
`fixed_assets`          bigint(20)      NULL                    COMMENT '公司固定资产',
`fa_coin_type`          SMALLINT        NULL                    COMMENT '公司固定资产币种',
`reg_capital`           bigint(20)      NULL                    COMMENT '注册资本',
`rc_coin_type`          SMALLINT        NULL                    COMMENT '注册资本币种',
`reg_country`           int(11)         NOT NULL                COMMENT '注册国家代号',
`reg_province`          int(11)         NULL                    COMMENT '隶属地区省份代号',
`reg_city`              int(11)         NULL                    COMMENT '隶属地区城市代号',
`world_top`             SMALLINT        NULL                    COMMENT '是否世界500强：（0：否，1：是）',
`official_website`      VARCHAR(100)    NULL                    COMMENT '公司网站',
`person_scale`          varchar(16)     NULL                    COMMENT '人数,形式为 `300-500`',
`found_at`              DATE            NULL                    COMMENT '成立时间',
`desc`                  varchar(2048)   NOT NULL                COMMENT '公司简介',
`nature`                SMALLINT        NULL                    COMMENT '企业性质(1：国有，2：集体，3：民营，4,私营，5：合资，6：外资，7：其他)',
`listed_status`         SMALLINT        NULL                    COMMENT '上市状态：（0：没上市，1：已上市）',
`listed_region`         VARCHAR(100)    NULL                    COMMENT '上市地区',
`ticker`                VARCHAR(100)    NULL                    COMMENT '股票代码',
`business_license`      VARCHAR(512)    NULL                    COMMENT '营业执照图片',
`business_license_id`   VARCHAR(50)     NULL                    COMMENT '营业执照号',
`bl_date`               DATE            NULL                    COMMENT '营业执照有效期',
`org_cert`              VARCHAR(512)    NULL                    COMMENT '组织机构证号图片',
`org_cert_id`           VARCHAR(50)     NULL                    COMMENT '组织机构证号',
`oc_date`               DATE            NULL                    COMMENT '组织机构证有效期',
`tax_no`                VARCHAR(512)    NULL                    COMMENT '税务登记证号图片',
`tax_no_id`             VARCHAR(50)     NULL                    COMMENT '税务登记证号',
`tn_date`               DATE            NULL                    COMMENT '税务登记证有效期',
`participate_count`     int             NOT NULL                COMMENT '交互参与数',
`customers`             VARCHAR(2048)   NOT NULL                COMMENT '客户群(json)',
`factories`             VARCHAR(1024)   NULL                    COMMENT '工厂信息(json)',
`created_at`            DATETIME        NOT NULL,
`updated_at`            DATETIME        NOT NULL,
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_companys_user_id_idx` ON `snz_companies` (`user_id`);


-- 企业和主营业务中间表（一对多）
-- -----------------------------------------------------
-- Table `snz`.`snz_company_main_businesses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `snz_company_main_businesses` (
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

CREATE INDEX `snz_company_main_bussinesses_company_id_idx` ON `snz_company_main_businesses` (`company_id`);
CREATE INDEX `snz_company_main_bussinesses_user_id_idx` ON `snz_company_main_businesses` (`user_id`);
CREATE INDEX `snz_company_main_bussinesses_main_business_id_idx` ON `snz_company_main_businesses` (`main_business_id`);

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
-- Table `snz`.`snz_contact_infos` 公司联系人信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_contact_infos` ;

CREATE TABLE IF NOT EXISTS `snz_contact_infos` (
`id`            BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键',
`name`          VARCHAR(20) NULL            COMMENT '名字',
`department`    VARCHAR(50) NULL            COMMENT '部门',
`duty`          VARCHAR(50) NULL            COMMENT '职务',
`mobile`        VARCHAR(30) NULL            COMMENT '手机',
`office_phone`  VARCHAR(50) NULL            COMMENT '办公电话',
`email`         VARCHAR(50) NULL            COMMENT '邮箱',
`user_id`       BIGINT      NOT NULL        COMMENT '用户id',
`company_id`    BIGINT      NOT NULL        COMMENT '对应企业id',
`created_at`    DATETIME    NOT NULL,
`updated_at`    DATETIME    NOT NULL,
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_contact_info_company_id_idx` ON `snz_contact_infos` (`company_id`);


-- -----------------------------------------------------
-- Table `snz`.`snz_company_finances` 公司财务信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_finances` ;

CREATE TABLE IF NOT EXISTS `snz_company_finances` (
  `id`                         BIGINT        NOT NULL    AUTO_INCREMENT,
  `user_id`                    bigint(20)    NOT NULL    COMMENT '用户id',
  `company_id`                 BIGINT        NOT NULL    COMMENT '公司的id',
  `country`                    int(11)       NULL        COMMENT '国家代号',
  `opening_bank`               varchar(64)   NULL        COMMENT '开户行',
  `open_license`               varchar(255)  NOT NULL    COMMENT '开户许可证url',
  `bank_code`                  varchar(64)   NULL        COMMENT '银行码',
  `bank_account`               varchar(64)   NULL        COMMENT '银行账号',
  `coin_type`                  SMALLINT      NULL        COMMENT '结算货币',
  `recent_finance`             varchar(2048) NOT NULL    COMMENT '近三年销售额和净利润,json存储',
  `created_at`                 DATETIME      NOT NULL,
  `updated_at`                 DATETIME      NOT NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `snz_company_finances_user_id_idx` ON `snz_company_finances` (`user_id`);
CREATE INDEX `snz_company_finances_company_id_idx` ON `snz_company_finances` (`company_id`);

-- ----------------------------------------------------
-- Table `snz_additional_docs` 财务鉴定资料
-- ----------------------------------------------------

DROP TABLE IF EXISTS `snz_additional_docs`;
CREATE TABLE `snz_additional_docs` (
`id`            bigint(20)      NOT NULL AUTO_INCREMENT     COMMENT '自增主键',
`user_id`       bigint(20)      NOT NULL                    COMMENT '用户id',
`finance_id`    bigint(20)      NOT NULL                    COMMENT '财务信息id',
`name`          varchar(32)     NULL                        COMMENT '材料名',
`files`         varchar(255)    NULL                        COMMENT '附件',
`comment`       varchar(64)     NULL                        COMMENT '描述',
`created_at`    datetime        NOT NULL,
`updated_at`    datetime        NOT NULL,
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_additional_docs_user_id_index` ON `snz_additional_docs` (`user_id`);
CREATE INDEX `snz_additional_docs_finance_id_index` ON `snz_additional_docs` (`finance_id`);

-- -----------------------------------------------------
-- Table `snz_company_ranks` 公司行业排名
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_ranks` ;

CREATE TABLE IF NOT EXISTS `snz_company_ranks` (
`id`                    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
`user_id`               bigint(20)      NOT NULL    COMMENT '用户id',
`company_id`            BIGINT          NOT NULL    COMMENT '对应企业信息',
`in_rank`               INT             NULL        COMMENT '国内排名',
`in_rank_org`           VARCHAR(64)     NULL        COMMENT '国内排名机构',
`in_rank_file`          VARCHAR(255)    NULL        COMMENT '国内排名证明附件',
`in_rank_file_name`     VARCHAR(100)    NULL        COMMENT '国内排名证明附件名称',
`in_rank_remark`        VARCHAR(512)    NULL        COMMENT '国内备注',
`out_rank`              INT             NULL        COMMENT '国际排名',
`out_rank_org`          VARCHAR(64)     NULL        COMMENT '国际排名机构',
`out_rank_file`         VARCHAR(255)    NULL        COMMENT '国际排名附件',
`out_rank_file_name`    VARCHAR(100)    NULL        COMMENT '国际排名附件名称',
`out_rank_remark`       VARCHAR(512)    NULL        COMMENT '国际备注',
`created_at`            DATETIME        NOT NULL,
`updated_at`            DATETIME        NOT NULL,
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_company_ranks_company_id` ON `snz_company_ranks` (`company_id` ASC);
CREATE INDEX `snz_company_ranks_user_id` ON `snz_company_ranks` (`user_id` ASC);

-- -----------------------------------------------------
-- Table `snz_addresses` 地区
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_addresses`;

CREATE TABLE `snz_addresses` (
  `id`                 int(11)          NOT NULL,
  `parent_id`          int(11)          DEFAULT NULL,
  `name`               varchar(64)      DEFAULT NULL,
  `level`              smallint(6)      DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE INDEX `snz_addresses_parent_id_index` ON `snz_addresses` (`parent_id`);


-- -----------------------------------------------------
-- Table `snz_supplier_tqrdc_infos` 供应商tqrdc信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_tqrdc_infos` ;

CREATE TABLE IF NOT EXISTS `snz_supplier_tqrdc_infos` (
`id`                    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
`user_id`               bigint          NOT NULL    COMMENT '用户id',
`company_id`            BIGINT          NOT NULL    COMMENT '对应企业信息',
`special_number`        VARCHAR(20)     NULL        COMMENT '专用号',
`supplier_code`         VARCHAR(20)     NOT NULL    COMMENT '供应商代码',
`supplier_name`         VARCHAR(20)     NOT NULL    COMMENT '供应商名称',
`module`                VARCHAR(50)     NOT NULL    COMMENT '模块',
`product_line_id`       SMALLINT        NULL        COMMENT '产品线ID',
`product_line`          varchar(128)    NULL        COMMENT '产品线',
`location`              VARCHAR(20)     NOT NULL    COMMENT '区域',
`rank`                  VARCHAR(20)     NOT NULL    COMMENT '排名',
`date`                  VARCHAR(10)     NOT NULL    COMMENT '日期',
`month`                 VARCHAR(8)      NOT NULL    COMMENT '月份',
`composite_score`       INT             NOT NULL    COMMENT '综合绩效',
`supplier_status`       int             NOT NULL DEFAULT '0' COMMENT '供应商状态(0:正常|-1:警告|-2:整改|-3:停新品)',
`tech_score`            INT             NOT NULL    COMMENT '技术得分',
`delay_days`            INT             NOT NULL    COMMENT '拖期天数',
`new_product_pass`      INT             NOT NULL    COMMENT '新品合格率',
`quality_score`         INT             NOT NULL    COMMENT '质量得分',
`live_bad`              INT             NOT NULL    COMMENT '现场不良率',
`market_bad`            INT             NOT NULL    COMMENT '市场不良率',
`resp_score`            INT             NOT NULL    COMMENT '响应得分',
`program_promised_rate` int             NULL        COMMENT '方案承诺率',
`program_selected_rate` int             NULL        COMMENT '方案选中率',
`program_adopted_rate`  int             NULL        COMMENT '方案落地率',
`requirement_resp`      INT             NOT NULL    COMMENT '需求响应度',
`deliver_score`         INT             NOT NULL    COMMENT '交付得分',
`deliver_diff`          INT             NOT NULL    COMMENT '交付差异',
`cost_score`            INT             NOT NULL    COMMENT '成本得分',
`increment`             INT             NOT NULL    COMMENT '增值',
`tech_score_rank`       INT             NULL        COMMENT '技术得分排序',
`tech_location`         VARCHAR(8)      NULL        COMMENT '技术得分区位',
`quality_score_rank`    INT             NULL        COMMENT '质量得分排序',
`quality_location`      VARCHAR(8)      NULL        COMMENT '质量得分区位',
`resp_score_rank`       INT             NULL        COMMENT '响应得分排序',
`resp_location`         VARCHAR(8)      NULL        COMMENT '响应得分区位',
`delivery_score_rank`   INT             NULL        COMMENT '交付得分排序',
`delivery_location`     VARCHAR(8)      NULL        COMMENT '交付得分区位',
`affect_delivery_count` INT             NULL        COMMENT '影响 T-1 交付次数',
`cost_score_rank`       INT             NULL        COMMENT '成本得分排序',
`cost_location`         VARCHAR(8)         NULL        COMMENT '成本得分区位',
`price_reduction_amount` int            NULL        COMMENT '降价额',
`price_reduction_range`  int            NULL        COMMENT '降幅',
`created_at`            DATETIME        NOT NULL,
`updated_at`            DATETIME        NOT NULL,
PRIMARY KEY (`id`)
);




-- ------------------------------------------------------------------
--   Table `snz_supplier_tqrdc_infos_tmp` 供应atqrdc信息 外部导入表
-- -----------------------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_tqrdc_infos_tmp`;
CREATE TABLE IF NOT EXISTS `snz_supplier_tqrdc_infos_tmp` (
  `id`                    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`               bigint          NULL    COMMENT '用户id',
  `company_id`            BIGINT          NULL    COMMENT '对应企业信息',
  `special_number`        VARCHAR(20)     NULL    COMMENT '专用号',
  `supplier_code`         VARCHAR(20)     NULL    COMMENT '供应商代码',
  `supplier_name`         VARCHAR(20)     NULL    COMMENT '供应商名称',
  `module`                VARCHAR(50)     NULL    COMMENT '模块',
  `product_line_id`       SMALLINT        NULL    COMMENT '产品线ID',
  `product_line`          varchar(128)    NULL    COMMENT '产品线',
  `location`              VARCHAR(20)     NULL    COMMENT '区域',
  `rank`                  VARCHAR(20)     NULL    COMMENT '排名',
  `date`                  VARCHAR(8)      NULL    COMMENT '日期',
  `composite_score`       INT             NULL    COMMENT '综合绩效',
  `supplier_status`       int             NULL    DEFAULT '0' COMMENT '供应商状态(0:正常|-1:警告|-2:整改|-3:停新品)',
  `tech_score`            INT             NULL    COMMENT '技术得分',
  `delay_days`            INT             NULL    COMMENT '拖期天数',
  `new_product_pass`      INT             NULL    COMMENT '新品合格率',
  `quality_score`         INT             NULL    COMMENT '质量得分',
  `live_bad`              INT             NULL    COMMENT '现场不良率',
  `market_bad`            INT             NULL    COMMENT '市场不良率',
  `resp_score`            INT             NULL    COMMENT '响应得分',
  `program_promised_rate` int             NULL    COMMENT '方案承诺率',
  `program_selected_rate` int             NULL    COMMENT '方案选中率',
  `program_adopted_rate`  int             NULL    COMMENT '方案落地率',
  `requirement_resp`      INT             NULL    COMMENT '需求响应度',
  `deliver_score`         INT             NULL    COMMENT '交付得分',
  `deliver_diff`          INT             NULL    COMMENT '交付差异',
  `cost_score`            INT             NULL    COMMENT '成本得分',
  `increment`             INT             NULL    COMMENT '增值',
  `tech_score_rank`       INT             NULL    COMMENT '技术得分排序',
  `quality_score_rank`    INT             NULL    COMMENT '质量得分排序',
  `resp_score_rank`       INT             NULL    COMMENT '响应得分排序',
  `delivery_score_rank`   INT             NULL    COMMENT '交付得分排序',
  `affect_delivery_count` INT             NULL    COMMENT '影响 T-1 交付次数',
  `cost_score_rank`       INT             NULL    COMMENT '成本得分排序',
  `price_reduction_amount` int            NULL    COMMENT '降价额',
  `price_reduction_range`  int            NULL    COMMENT '降幅',
  `created_at`            DATETIME        NULL,
  `updated_at`            DATETIME        NULL,
  PRIMARY KEY (`id`)
);
create index `indx_snz_supplier_tqrdc_infos_tmp_date` on snz_supplier_tqrdc_infos_tmp(`date`);




-- 黑名单
-- -----------------------------------------------------
-- Table `snz`.`snz_blacklist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_blacklist` ;

CREATE TABLE IF NOT EXISTS `snz_blacklist` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`name`                VARCHAR(50)   NOT NULL                COMMENT '公司名',
`keywords`            VARCHAR(30)   NULL                    COMMENT '关键字',
`init_agent`          VARCHAR(50)   NULL                    COMMENT '法人',
`created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
`updated_at`          DATETIME      NULL                    COMMENT '更新时间',
PRIMARY KEY (`id`)
);

CREATE INDEX `snz_blacklist_name` ON `snz_blacklist` (`name`);


-- -----------------------------------------------------
-- Table `snz_company_extra_rd` 公司研发信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_extra_rd`;

CREATE TABLE IF NOT EXISTS `snz_company_extra_rd` (
  `id`                                     BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`                                BIGINT        NOT NULL COMMENT '用户id',
  `award_level`                            INT           NULL COMMENT '所获证书等级',
  `number_of_people`                       INT           NULL COMMENT '研发人数',
  `number_of_intermediate_engineer`        INT           NULL COMMENT '中级工程师人数',
  `number_of_senior_engineer`              INT           NULL COMMENT '高级工程师人数',
  `other_names`                            VARCHAR(64)   NULL COMMENT '其它职称',
  `sum_number_of_engineer`                 INT           NULL COMMENT '合计人数',
  `investment_this_year`                   INT           NULL COMMENT '本年研发投入（单位分）',
  `investment_last_year`                   INT           NULL COMMENT '去年投入',
  `investment_before_last_year`            INT           NULL COMMENT '前年投入',
  `assets`                                 INT           NULL COMMENT '研发资产（单位分）',
  `lab_area`                               INT           NULL COMMENT '试验室面积（单位平方米）',
  `number_of_equipments`                   INT           NULL COMMENT '研发设备数量（台）',
  `lab_level`                              SMALLINT      NULL COMMENT '试验室级别（默认1位国家级）',
  `number_of_patents`                      INT           NULL COMMENT '专利数量',
  `number_of_patents_for_inventions`       INT           NULL COMMENT '其中发明专利的数量',
  `number_of_patents_last_three_years`     INT           NULL COMMENT '近三年专利数量',
  `national_technology_awards`             INT           NULL COMMENT '国家级奖项数',
  `provincial_technology_awards`           INT           NULL COMMENT '省级奖项数',
  `success_stories`                        VARCHAR(256)  NULL COMMENT '成功案例',
  `other_ability_description`              VARCHAR(256)  NULL COMMENT '技术行业排名（1：高，2：中）',
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NULL                    COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_com_extra_rd_user_id` ON `snz_company_extra_rd` (`user_id`);

ALTER TABLE `snz_company_extra_rd`
    ADD COLUMN `facility_attach_url` VARCHAR(128) NULL AFTER `assets`;
ALTER TABLE `snz_company_extra_rd`
    ADD COLUMN `lab_attach_url` VARCHAR(128) NULL AFTER `lab_level`;
ALTER TABLE `snz_company_extra_rd`
    ADD COLUMN `patents_attach_url` VARCHAR(128) NULL AFTER `number_of_patents_last_three_years`;
ALTER TABLE `snz_company_extra_rd`
    ADD COLUMN `success_stories_attach_url` VARCHAR(128) NULL AFTER `success_stories`;

alter table snz_company_extra_rd modify investment_this_year bigint;
alter table snz_company_extra_rd modify investment_last_year bigint;
alter table snz_company_extra_rd modify investment_before_last_year bigint;
alter table snz_company_extra_rd modify assets bigint;

-- -----------------------------------------------------
-- Table `snz_company_extra_quality` 公司质量能力信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_extra_quality`;

CREATE TABLE IF NOT EXISTS `snz_company_extra_quality` (
  `id`                            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`                       BIGINT       NULL COMMENT '用户id',
  `rohs_id`                       VARCHAR(128) NULL COMMENT 'RoHS(QC08000) 证书编号',
  `rohs_valid_date`               DATETIME     NULL COMMENT 'RoHS 证书有效期',
  `iso9001_id`                    VARCHAR(128) NULL COMMENT 'ISO9001 证书编号',
  `iso9001_valid_date`            DATETIME     NULL COMMENT 'ISO9001 证书有效期',
  `iso14001_id`                   VARCHAR(128) NULL COMMENT 'ISO14001 证书编号',
  `iso14001_valid_date`           DATETIME     NULL COMMENT 'ISO14001 证书有效期',
  `ts16949_id`                    VARCHAR(128) NULL COMMENT 'TS16949 证书编号',
  `ts16949_valid_date`            DATETIME     NULL COMMENT 'TS16949 证书有效期',
  `number_for_admission_test`     INT          NULL COMMENT '入厂检验人数',
  `number_for_inspection_process` INT          NULL COMMENT '过程巡检人数',
  `number_for_product_testing`    INT          NULL COMMENT '成品检验人数',
  `number_in_lab`                 INT          NULL COMMENT '实验室人数',
  `auditor_type`                  INT          NULL COMMENT '质量人员类型（1:资质审核员, 2:试验员, 3:绿带, 4:黑带)',
  `exam_people`                    INT          NULL COMMENT '审核人员人数',
  `test_people`                    INT          NULL COMMENT '试验人员人数',
  `green_level`                    INT          NULL COMMENT '绿带人员人数',
  `black_level`                    INT          NULL COMMENT '黑带人员人数',
  `investment_this_year`          INT          NULL COMMENT '今年投入（单位分）',
  `investment_last_year`          INT          NULL COMMENT '去年投入',
  `investment_befor_last_year`    INT          NULL COMMENT '前年投入',
  `number_of_equipments`          INT          NULL COMMENT '试验设配数量（台）',
  `lab_level`                     SMALLINT     NULL COMMENT '试验室级别（默认1为国家级）',
  `checks_qualified_rate`         INT          NULL COMMENT '现场合格率（单位万分之一）',
  `market_qualified_rate`         INT          NULL COMMENT '市场合格率',
  `customer_satisfaction`         INT          NULL COMMENT '客户满意度',
  `quality_tools`                 VARCHAR(32)  NULL COMMENT '质量工具运用（SPC, MSA, . ..）',
  `ctq_cpk`                       VARCHAR(256) NULL COMMENT 'CTQ的CPK水平',
  `other_ability_description`     VARCHAR(256) NULL COMMENT '其它能力说明',
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NULL                    COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_com_extra_quality_user_id` ON `snz_company_extra_quality` (`user_id`);

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
    ADD COLUMN `auditor_attach_url` VARCHAR(128) NULL AFTER `black_level`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `lab_attach_url` VARCHAR(128) NULL AFTER `lab_level`;
ALTER TABLE `snz_company_extra_quality`
    ADD COLUMN `qualified_attach_url` VARCHAR(128) NULL AFTER `customer_satisfaction`;

alter table snz_company_extra_quality modify investment_this_year bigint;
alter table snz_company_extra_quality modify investment_last_year bigint;
alter table snz_company_extra_quality modify investment_befor_last_year bigint;

-- -----------------------------------------------------
-- Table `snz_company_extra_response` 公司响应信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_extra_response`;

CREATE TABLE IF NOT EXISTS `snz_company_extra_response` (
  `id`                                BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`                           BIGINT       NOT NULL COMMENT '用户id',
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NULL                    COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_com_extra_response_user_id` ON `snz_company_extra_response` (`user_id`);


-- -----------------------------------------------------
-- Table `snz_company_extra_delivery` 公司交货信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_extra_delivery`;

CREATE TABLE IF NOT EXISTS `snz_company_extra_delivery` (
  `id`                             BIGINT   NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`                        BIGINT   NOT NULL COMMENT '用户id',
  `list_of_equipments`           VARCHAR(4096)      NULL COMMENT '生产设备清单 设备名称 参数 数量 附件',
  `module_production_capacity`     INT      NULL COMMENT '模块生产能力（单位：件/年）',
  `list_of_automation_equipment` VARCHAR(4096)      NULL COMMENT '自动化设备清单 设备名称 参数 数量 附件',
  `delivery_area`                  SMALLINT NULL COMMENT '供货区域',
  `delivery_cycle`                 INT      NULL COMMENT '交货周期（天）',
  `open_rate`            INT      NULL COMMENT '开机率）',
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NULL                    COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_com_extra_delivery_user_id` ON `snz_company_extra_delivery` (`user_id`);

ALTER TABLE `snz_company_extra_delivery`
    MODIFY `delivery_area` VARCHAR(256) NULL;

ALTER TABLE `snz_company_extra_delivery`
ADD COLUMN `module_production_capacity_attach_url` VARCHAR(128) NULL AFTER `module_production_capacity`;
-- -----------------------------------------------------
-- Table `snz_company_extra_scale_and_cost` 公司规模和成本信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_company_extra_scale_and_cost`;

CREATE TABLE IF NOT EXISTS `snz_company_extra_scale_and_cost` (
  `id`                        BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`                   BIGINT NOT NULL COMMENT '用户id',
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NULL                    COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_com_extra_scale_and_cost_user_id` ON `snz_company_extra_scale_and_cost` (`user_id`);

-- 产品线
-- -----------------------------------------------------
-- Table `snz`.`snz_product_lines`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_product_lines` ;

CREATE TABLE IF NOT EXISTS `snz_product_lines` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`name`                VARCHAR(20)   NULL                    COMMENT '产品线名称',
PRIMARY KEY (`id`)
);

insert into snz_product_lines(name) values('冰箱'),('冷柜'),('波轮'),('滚筒'),('家用空调'),('商用空调'),('洗碗机'),('热水器'),('油烟机'),('灶具'),('太阳能热水器'),('燃气热水器'),('医疗冷柜'),('电视机'),('电机-600'),('控制板-600'),('新材料-600');


-- 企业和可供货园区中间表（一对多）
-- -----------------------------------------------------
-- Table `snz`.`snz_company_supply_park`
-- -----------------------------------------------------
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
  `created_at`          DATETIME      NOT NULL                COMMENT '创建时间',
  `updated_at`          DATETIME      NOT NULL                COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX `snz_supplier_approve_logs_user_id_idx` ON `snz_supplier_approve_logs` (`user_id`);
CREATE INDEX `snz_supplier_approve_logs_approve_type_idx` ON `snz_supplier_approve_logs` (`approve_type`);


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

-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_credit_qualifies`
-- -----------------------------------------------------
create table `snz_supplier_credit_qualifies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商id',
  `user_id` 	bigint(20) NOT NULL COMMENT '供应商id',
  `status`		smallint default null comment '审核状态',
  `message`		varchar(1000) default null comment '错误消息',
  `appeal_msg` varchar(4096) DEFAULT NULL COMMENT '申诉消息',
  `reviewer` bigint(20) DEFAULT NULL COMMENT '核审人ID',
  `reviewer_name` varchar(64) DEFAULT NULL COMMENT '核审人名字',
  `created_at`  datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at`  datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
create index `snz_supplier_credit_qualify_supplier` on `snz_supplier_credit_qualifies`(supplier_id);
create index `snz_supplier_credit_qualify_user` on `snz_supplier_credit_qualifies`(user_id);

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
CREATE INDEX `snz_main_business_approvers_main_business_id_idx` ON `snz_main_business_approvers` (`main_business_id`);


-- --------------------------------------------------------
-- Table snz_product_owners 产品负责人信息表
-- --------------------------------------------------------
DROP TABLE IF EXISTS `snz_product_owners`;
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


-- -----------------------------------------------------
-- Table `snz_user_complaints` 用户抱怨信息录入表
-- -----------------------------------------------------
DROP  TABLE  IF EXISTS `snz_user_complaints`;
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
  `module_num`	             VARCHAR(64)  NULL COMMENT '模块编号',
  `module_name`             VARCHAR(64)  NULL COMMENT '模块名称',
  `complaint_content`       VARCHAR(512) NULL COMMENT '抱怨回馈内容',
  `complaint_reason`        VARCHAR(128) NULL COMMENT '抱怨原因',
  `claim_amount`            INT          NULL COMMENT '索赔金额',
  `claim_doc`               VARCHAR(128) NULL COMMENT '索赔证明材料',
  `claim_total`             BIGINT          NULL DEFAULT 0 COMMENT  '索赔合计',
  `score_total`             BIGINT          NULL DEFAULT 0 COMMENT  '积分合计',
  `created_at`              DATETIME     NOT NULL COMMENT '创建时间',
  `updated_at`              DATETIME     NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `indx_snz_user_complaints_user_id` ON `snz_user_complaints`(`user_id`);

-- -----------------------------------------------------
-- Table `snz_purchaser_authorities` 采购商权限细则（类目相关）
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_purchaser_authorities`;
CREATE TABLE IF NOT EXISTS `snz_purchaser_authorities` (
  `id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`       BIGINT      NOT NULL COMMENT '用户id',
  `type`          INT         NOT NULL COMMENT '暂定: 1. 后台类目, 2. 前台类目',
  `content`       VARCHAR(20) NOT NULL COMMENT '当权限类型对应为类目时存放类目id, 其他情况视type另定',
  `rich_content`  VARCHAR(256)    NULL COMMENT '保留字段，当需要存放更复杂信息时',
  `position`      INT         NOT NULL COMMENT '职位; 暂定: 1. 小微主, 2. 小微成员',
  `role`          VARCHAR(20) NOT NULL COMMENT '职责，@see User.JobRole',
  `created_at`    DATETIME    NOT NULL COMMENT '创建时间',
  `updated_at`    DATETIME    NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `snz_purchaser_authorities_user_id_idx` ON `snz_purchaser_authorities` (`user_id`);
CREATE INDEX `snz_purchaser_authorities_content_idx` ON `snz_purchaser_authorities` (`content`);


-- 创建根据模块统计tqrdc相关绩效表
-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_module_count`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_module_count` ;

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

-- -----------------------------------------------------
-- Table `snz_supplier_resource_material_infos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_resource_material_infos`;

CREATE TABLE IF NOT EXISTS `snz_supplier_resource_material_infos` (
  `id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id`         BIGINT        NOT NULL COMMENT '供应商id',
  `supplier_name`       VARCHAR(80)   NOT NULL COMMENT '供应商名称 (冗余, 分页查找用)',
  `approved_module_ids` TEXT          NOT NULL COMMENT '已通过审核的类目"1,2,3,...',
  `not_approved_module_ids` TEXT      NOT NULL COMMENT '未通过审核的类目"7,4,5,...',
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
DROP TABLE IF EXISTS `snz_supplier_resource_material_logs`;

CREATE TABLE IF NOT EXISTS `snz_supplier_resource_material_logs` (
  `id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id`   BIGINT      NOT NULL COMMENT '供应商id',
  `checker_id`    BIGINT      NOT NULL COMMENT '审核员id',
  `times`         BIGINT      NOT NULL COMMENT '审核次数，唯一确定一次完整的审核 @see SupplierResourceMaterialInfo.times',
  `type`          TINYINT     NOT NULL COMMENT '1. 一级审核, 2. 二级审核, 5. 供应商提交申请, 6. 系统自动提交申请, 7. 系统自动设为最终通过',
  `status`        TINYINT     NOT NULL COMMENT '0. 否决, 1. 通过 (供应商提交申请默认为通过, 系统自动提交申请亦同)',
  `content`       TEXT        NOT NULL DEFAULT '' COMMENT '视type而定，若type为1. 一级审核，暂定存放 @see SupplierQualifyRecordDto',
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
DROP TABLE IF EXISTS `snz_supplier_resource_material_subjects`;

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


create table `snz_suppliers_resources` (
  id bigint not null primary key AUTO_INCREMENT,
  v_code varchar(64) not null comment '供应商编码',
  supplier_name varchar(255) default null comment '供应商名字',
  product varchar(32) not null comment '产品线',
  user_id bigint not null comment '资源小微id',
  user_name varchar(64) not null comment '资源小微用户名字'
);

create table `snz_supplier_reparation_sumaries` (
    id bigint not null primary key AUTO_INCREMENT,
    supplier_uid bigint not null comment '供应商编码',
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
);

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


-- ---------------------------------------------------
--  供应绩效管理者联系方式
-- ---------------------------------------------------
CREATE TABLE `snz_tqrdc_manager_contacts`(
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主健',
  `name`          VARCHAR(64) NULL COMMENT '发送人姓名',
  `phone`         VARCHAR(16) NOT NULL COMMENT '联系方式',
  `template`      VARCHAR(512) NULL COMMENT '短信模板',
  `remark`        VARCHAR(128) NOT NULL COMMENT '备注',
  `created_at`    DATETIME DEFAULT NULL COMMENT '创建时间',
  `updated_at`    DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

--  ---------------------------------------------------------
--  供应商联系方式
--  ---------------------------------------------------------
CREATE TABLE `snz_supplier_contacts`(
  `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主健',
  `supplier_name`   VARCHAR(64) NOT NULL COMMENT '供应商名称',
  `supplier_code`   VARCHAR(16) NOT NULL COMMENT '供应商编码',
  `phone`           VARCHAR(16) NOT NULL COMMENT '联系电话',
  `created_at`      DATETIME DEFAULT NULL COMMENT '创建时间',
  `updated_at`      DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

--  ---------------------------------------------------------
--  供应商关联表
--  ---------------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_group_relations`;
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

insert into snz_supplier_credit_qualifies
(supplier_id, user_id, updated_at)
values
(33, 33, '2014-09-17 10:22:12');

-- ---------------------------------------------------------
-- 用户抱怨聊天记录
-- ---------------------------------------------------------
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

-- 甲指库
-- -----------------------------------------------------
-- Table `snz`.`snz_supplier_appoint` 甲指库
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_appoint` ;

CREATE TABLE IF NOT EXISTS `snz_supplier_appoint` (
`id`                    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
`company_id`            BIGINT          NULL                    COMMENT '对应供应商公司company_id',
`requirement_id`        BIGINT          NULL                    COMMENT '需求id',
`series_ids`            varchar(1024)   NOT NULL                COMMENT '系列编号(后台二级类目){sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}，冗余字段，便于通过供应商资质',
`creator_id`            bigint(20)      NOT NULL                COMMENT '创建需求的人员编号（用于索引对应的主管），冗余字段，便于像创建需求这发消息',
`corporation`           varchar(80)     NULL                    COMMENT '法人公司，公司名',
`status`                INT             NOT NULL DEFAULT 0      COMMENT '审核状态，0 初始，1 已提交，2 初审通过，3 初审未通过，4 终审通过，5终审未通过',
`advice`                varchar(80)     NULL                    COMMENT '审核意见',
`created_at`            DATETIME        NOT NULL,
`updated_at`            DATETIME        NOT NULL,
PRIMARY KEY (`id`)
);
insert into `snz_supplier_appoint`(id, corporation, company_id, status, advice, created_at, updated_at)
values (1, 'corporation', 1 , 0 , 'advice', '2014-08-07 08:17:09', '2014-08-07 08:17:09' );

-- -----------------------------------------------------------
-- 供应商物料明细信息  snz_supplier_module_details
-- -----------------------------------------------------------
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

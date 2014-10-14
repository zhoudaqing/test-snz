

-- 用户
-- -----------------------------------------------------
-- Table `snz`.`snz_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_users` ;

CREATE TABLE IF NOT EXISTS `snz_users` (
`id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
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
  `id`             BIGINT        NOT NULL    AUTO_INCREMENT,
  `user_id`        bigint(20)    NOT NULL    COMMENT '用户id',
  `company_id`     BIGINT        NOT NULL    COMMENT '公司的id',
  `country`        int(11)       NULL        COMMENT '国家代号',
  `opening_bank`   varchar(64)   NULL        COMMENT '开户行',
  `open_license`   varchar(255)  NOT NULL    COMMENT '开户许可证url',
  `bank_code`      varchar(64)   NULL        COMMENT '银行码',
  `bank_account`   varchar(64)   NULL        COMMENT '银行账号',
  `coin_type`      SMALLINT      NULL        COMMENT '结算货币',
  `recent_finance` varchar(2048) NOT NULL    COMMENT '近三年销售额和净利润,json存储',
  `created_at`     DATETIME      NOT NULL,
  `updated_at`     DATETIME      NOT NULL,
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
`cost_location`         VARCHAR(8)      NULL        COMMENT '成本得分区位',
`price_reduction_amount` int            NULL        COMMENT '降价额',
`price_reduction_range`  int            NULL        COMMENT '降幅',
`created_at`            DATETIME        NOT NULL,
`updated_at`            DATETIME        NOT NULL,
PRIMARY KEY (`id`)
);



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



-- 需求部分
-- -----------------------------------------------------
-- Table `snz_requirements`    需求
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirements`;

CREATE TABLE IF NOT EXISTS `snz_requirements` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `name`                VARCHAR(32) NOT NULL  COMMENT '需求模版名称',
  `purchaser_id`        BIGINT      NOT NULL  COMMENT '采购商编号(指的是注册公司的编号)',
  `purchaser_name`      VARCHAR(32) NOT NULL  COMMENT '采购商名称',
  `product_lineId`      INT         NOT NULL  COMMENT '产品线编号',
  `product_lineName`    VARCHAR(32) NOT NULL  COMMENT '产品线名称',
  `benefit_id`          INT         NOT NULL  COMMENT '利共体编号',
  `benefit_name`        VARCHAR(32) NOT NULL  COMMENT '利共体名称',
  `delivery_address`    VARCHAR(64) NOT NULL  COMMENT '配送园区&厂商（使用json保存->{ad:[{pa:101,fa:10},{pa:101,fa:20}]）',
  `describe`            VARCHAR(256)NOT NULL  COMMENT '需求描述信息',
  `accessories`         VARCHAR(256)NULL      COMMENT '上传附件内容（使用json保存->{file:[url1,url2]）',
  `check_result`        SMALLINT    NOT NULL  COMMENT '审核状态(0:待审核,1:审核通过,2:审核不通过)',
  `check_id`            BIGINT      NULL      COMMENT '审核人员编号（主管）',
  `check_name`          VARCHAR(32) NULL      COMMENT '审核人员名称（主管名称）',
  `check_time`          DATETIME    NULL      COMMENT '审核时间（作为一个发布时间）',
  `creator_id`          BIGINT      NOT NULL  COMMENT '创建需求的人员编号（用于索引对应的主管）',
  `creator_name`        VARCHAR(32) NOT NULL  COMMENT '需求创建人名称',
  `creator_phone`       VARCHAR(32) NOT NULL  COMMENT '需求创建人手机',
  `creator_email`       VARCHAR(32) NOT NULL  COMMENT '需求创建人email',
  `select_num`       	  INT      	  NULL  	  COMMENT '选中的供应商数量',
  `replace_num`         INT      	  NULL  	  COMMENT '备选方案的供应商数量',
  `over_time`           DATETIME    NULL  	  COMMENT '反超期',
  `status`              SMALLINT    NULL      COMMENT '需求状态（-1:删除状态，1:需求交互，2:需求锁定，3:方案交互，4:方案综投，5:选定供应商与方案，招标结束）',
  `solution_id`         BIGINT      NULL      COMMENT '最终方案编号，当状态为5时确认状态（这个是由需求供应商方案中选取的一个）',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirements_purchaser_id ON snz_requirements (purchaser_id);
CREATE INDEX snz_requirements_creator_id ON snz_requirements (creator_id);
CREATE INDEX snz_requirements_check_id ON snz_requirements (check_id);


-- -----------------------------------------------------
-- Table `snz_modules`    需求中的模块的需求详细信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_modules`;

CREATE TABLE IF NOT EXISTS `snz_modules` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `type`                INT         NOT NULL  COMMENT '模块类型（1:老品－>需要从后台获取先不处理，2:新品，so先不处理）',
  `spu_id`              BIGINT      NOT NULL  COMMENT '类目最终选定的一个模块编号',
  `spu_name`            VARCHAR(32) NOT NULL  COMMENT '模块名称系列名称',
  `spu_category`        VARCHAR(32) NOT NULL  COMMENT '模块类目名称',
  `tactics_id`          INT         NOT NULL  COMMENT '模块策略编号',
  `head_drop`           VARCHAR(32) NOT NULL  COMMENT '引领点',
  `resource_num`        INT         NOT NULL  COMMENT '需要这个模块的资源数量',
  `quality`             VARCHAR(128)NULL      COMMENT '质量目标（可以有创建者or团队人员填写）',
  `cost`                VARCHAR(128)NULL      COMMENT '成本目标',
  `delivery`            VARCHAR(128)NULL      COMMENT '产能要求',
  `attestation`         VARCHAR(128)NULL      COMMENT '认证要求',
  `supply_at`           DATETIME    NOT NULL  COMMENT '批量供货时间',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_modules_requirement_id ON snz_modules (requirement_id);




-- -----------------------------------------------------
-- Table `snz_requirement_teams`    交互团队成员(将这个表从模版表中分离出来的原因－》需要从交互团队人员的纬度去查询他参加的团队信息)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_teams`;

CREATE TABLE IF NOT EXISTS `snz_requirement_teams` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求模版编号',
  `requirement_name`    VARCHAR(32) NOT NULL  COMMENT '模版名称',
  `type`                SMALLINT    NOT NULL  COMMENT '绑定的节点类型（1:T技术,2:Q质量,3:R互动,4:D产能,5:C成本）',
  `user_id`             BIGINT      NOT NULL  COMMENT '队员编号',
  `user_name`           VARCHAR(32) NOT NULL  COMMENT '队员名称',
  `user_number`         INT         NOT NULL  COMMENT '队员工号',
  `user_phone`          VARCHAR(32) NOT NULL  COMMENT '队员联系电话',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_teams_requirement_id ON snz_requirement_teams (requirement_id);
CREATE INDEX snz_requirement_teams_user_id ON snz_requirement_teams (user_id);



-- -----------------------------------------------------
-- Table `snz_requirement_times`    交互时间计划
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_times`;

CREATE TABLE IF NOT EXISTS `snz_requirement_times` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求模版编号',
  `type`                SMALLINT    NOT NULL  COMMENT '交互状态时间（1:需求交互,2:需求锁定,3:方案交互,4:方案综投,5:选定供应商&方案）',
  `predict_start`       DATE        NOT NULL  COMMENT '预计开始时间',
  `predict_end`         DATE        NOT NULL  COMMENT '预计结束时间',
  `user_id`             BIGINT      NULL       COMMENT '阶段提交操作的用户编号',
  `user_name`           VARCHAR(32) NULL       COMMENT '阶段提交操作的用户姓名',
  `actual_end`          DATE        NULL       COMMENT '实际结束时间',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_times_requirement_id ON snz_requirement_times (requirement_id);



-- -----------------------------------------------------
-- Table `snz_requirement_solutions`    对应与需求的供应商提交方案
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_solutions`;

CREATE TABLE IF NOT EXISTS `snz_requirement_solutions` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
`requirement_name`    VARCHAR(32) NOT NULL  COMMENT '需求名称',
`supplier_id`         BIGINT      NOT NULL  COMMENT '供应商编号',
`supplier_name`       VARCHAR(32) NOT NULL  COMMENT '供应商名称',
`technology`          VARCHAR(32) NOT NULL  COMMENT '技术(冗余一把海尔后台供应商系统中的TQRDC数据)',
`quality`             VARCHAR(32) NOT NULL  COMMENT '质量',
`reaction`            VARCHAR(32) NOT NULL  COMMENT '互动',
`delivery`            VARCHAR(32) NOT NULL  COMMENT '产能',
`cost`                INT         NOT NULL  COMMENT '成本',
`quote`               INT         NOT NULL  COMMENT '报价',
`not_accept`          VARCHAR(256)NULL      COMMENT '无法承诺信息(NULL:承诺,填写信息表示无法承诺)',
`created_at`          DATETIME    NULL      COMMENT '创建时间',
`updated_at`          DATETIME    NULL      COMMENT '修改时间',
PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_solutions_requirement_id ON snz_requirement_solutions (requirement_id);
CREATE INDEX snz_requirement_solutions_supplier_id ON snz_requirement_solutions (supplier_id);



-- ---------------------------------
-- Table `snz_module_solutions`    对应与模块的供应商提交方案
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_module_solutions`;

CREATE TABLE IF NOT EXISTS `snz_module_solutions` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `solution_id`           BIGINT      NOT NULL  COMMENT '整体方案编号',
  `module_id`           BIGINT      NOT NULL  COMMENT '模块编号（这个是具体模块需求的编号）',
  `module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称(这个是spu的名称)',
  `technology`          VARCHAR(32) NOT NULL  COMMENT '技术',
  `quality`             VARCHAR(32) NOT NULL  COMMENT '质量',
  `reaction`            VARCHAR(32) NOT NULL  COMMENT '互动',
  `delivery`            VARCHAR(32) NOT NULL  COMMENT '产能',
  `cost`                INT         NOT NULL  COMMENT '成本',
  `quote`               INT         NOT NULL  COMMENT '报价',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_module_suppliers_solution_id ON snz_module_suppliers (solution_id);



-- -----------------------------------------------------
-- Table `snz_requirement_ranks`    最终的供应商提交方案的名次&总体配额
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_ranks`;

CREATE TABLE IF NOT EXISTS `snz_requirement_ranks` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `rank`                INT         NOT NULL  COMMENT '名次（由采购商设定名次:1,2,3...名次）',
  `type`                SMALLINT    NOT NULL  COMMENT '名次类型（1:正选供应商，2:备选供应商）',
  `supplier_id`         BIGINT      NOT NULL  COMMENT '供应商编号',
  `supplier_name`       VARCHAR(32) NOT NULL  COMMENT '供应商名称',
  `quota_scale`         INT         NULL      COMMENT '配额比例数据信息',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_ranks_requirement_id ON snz_requirement_ranks (requirement_id);
CREATE INDEX snz_requirement_ranks_supplier_id ON snz_requirement_ranks (supplier_id);



--------------------------------------------------
-- 新品导入
--------------------------------------------------
DROP TABLE IF EXISTS `snz_new_product_imports`;
CREATE TABLE `snz_new_product_imports` (
  `id`                      BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `document_name`           VARCHAR(128)  DEFAULT NULL COMMENT '文件',
  `module_id`               BIGINT        DEFAULT NULL COMMENT '模块ID内部编号',
  `module_num`              VARCHAR(64)   DEFAULT NULL COMMENT '模块编号(海尔内部使用)',
  `original_creation_date`  DATETIME      DEFAULT NULL COMMENT '原件创建时间',
  `prototype_send_date`     DATETIME      DEFAULT NULL COMMENT '原件发布时间',
  `purchase_confirmer`      VARCHAR(64)   DEFAULT NULL COMMENT '采购确认人',
  `purchase_confirm_date`   DATETIME      DEFAULT NULL COMMENT '采购确认时间',
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

-- -----------------------------------------------------
-- Table 新品导入详细步骤
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_new_product_steps`;
CREATE TABLE `snz_new_product_steps` (
  `id`                  bigint    NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `module_num`          varchar(64)   NOT NULL COMMENT '模块号(海尔内部使用)',
  `supplier_code`       varchar(128)   DEFAULT NULL COMMENT '供应商代码',
  `supplier_name`       varchar(128)   DEFAULT NULL COMMENT '供应商名称',
  `step`                smallint  NOT NULL DEFAULT '0' COMMENT '流程节点(1:原件创建 | 2:原件发布 | 3:装配完成 | 4:收样确认 | 5:检测收样 | 6:检测开始 | 7:检测计划时间 | 8:检测完成)',
  `datetime`            datetime  DEFAULT NULL COMMENT '计划时间或日期',
  `duration`            int       DEFAULT NULL COMMENT '周期',
  `real_datetime`       datetime  DEFAULT NULL COMMENT '时间进度时间',
  `status`              int       NOT NULL DEFAULT '0' COMMENT '状态(-1:拖期 | 0:未进行 | 1:正常)',
  `in_charge`           varchar(64)   null COMMENT '责任人',
  `created_at`          datetime  DEFAULT NULL COMMENT '创建时间',
  `updated_at`          datetime  DEFAULT NULL,
  PRIMARY KEY (`id`)
);
create index `indx_snz_new_product_step_module_num` on snz_new_product_steps(`module_num`);




-- -----------------------------------------------------
-- Table `snz_module_quotas`    最终每个供应商对应与模块的配额信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_module_quotas`;

CREATE TABLE IF NOT EXISTS `snz_module_quotas` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `module_id`           BIGINT      NOT NULL  COMMENT '模块编号',
  `module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称(这个是spu的名称)',
  `supplier_id`         BIGINT      NOT NULL  COMMENT '供应商编号',
  `supplier_name`       VARCHAR(32) NOT NULL  COMMENT '供应商名称',
  `quantity`            INT         NULL      COMMENT '配额数量',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_module_quotas_requirement_id ON snz_module_quotas (requirement_id);
CREATE INDEX snz_module_quotas_supplier_id ON snz_module_quotas (supplier_id);











-- 话题部分
-- -----------------------------------------------------
-- Table `snz_topics`    话题信息表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_topics`;
CREATE TABLE `snz_topics` (
`id`                bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`req_id`            bigint(20)      NOT NULL                COMMENT '需求ID',
`user_id`           bigint(20)      NOT NULL                COMMENT '用户id',
`company_name`      varchar(64)     NOT NULL                COMMENT '冗余公司名',
`title`             varchar(64)     NOT NULL                COMMENT '标题',
`content`           varchar(1024)   DEFAULT NULL            COMMENT '内容',
`req_status`        smallint(6)     NOT NULL                COMMENT '状态，同需求状态',
`scope`             smallint(4)     NOT NULL                COMMENT '话题范围（0：公开，1：圈子内）',
`files`             varchar(255)    DEFAULT NULL            COMMENT '附件',
`last_replier_id`   bigint(20)                              COMMENT '最后回复人的编号',
`last_replier_name` varchar(45)                             COMMENT '最后回复人名称',
`total_views`       integer         DEFAULT 0               COMMENT '冗余浏览总数',
`total_replies`     integer         DEFAULT 0               COMMENT '冗余回复总数',
`deleted`           tinyint(1)      NOT NULL DEFAULT '0'    COMMENT '删除标志,0未删除,1删除',
`closed`            tinyint(1)      NOT NULL DEFAULT '0'    COMMENT '关闭标志,0未关闭,1关闭',
`created_at`        datetime DEFAULT NULL                   COMMENT '创建时间',
`updated_at`        datetime DEFAULT NULL                   COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX snz_topics_user_id ON snz_topics (user_id);
CREATE INDEX snz_topics_req_id ON snz_topics (req_id);



-- -----------------------------------------------------
-- Table `snz_user_topics`    用户与话题管理表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_users_topics`;
CREATE TABLE `snz_users_topics` (
`topic_id`      bigint(20)  NOT NULL,
`user_id`       bigint(20)  NOT NULL,
`created_at`    datetime    NOT NULL,
`updated_at`    datetime    NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX snz_user_topics_user_id_idx ON snz_users_topics (user_id);
CREATE INDEX snz_user_topics_topic_id_idx ON snz_users_topics (topic_id);



-- -----------------------------------------------------
-- Table `snz_replies`    用户回复信息表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_replies`;
CREATE TABLE `snz_replies` (
`id`            bigint(20)      NOT NULL AUTO_INCREMENT,
`user_id`       bigint(20)      NOT NULL,
`receiver_id`   bigint(20)                      COMMENT '被回复的用户id, 顶级回复时值为0',
`topic_id`      bigint(20)      NOT NULL,
`tid`           bigint(20)      DEFAULT NULL    COMMENT '顶级回复id',
`pid`           BIGINT(20)      DEFAULT NULL    COMMENT '父级回复id',
`company_name`  varchar(64)     NOT NULL        COMMENT '冗余公司名',
`content`       varchar(1024)   NOT NULL        COMMENT '内容',
`files`         varchar(255)    DEFAULT NULL    COMMENT '附件',
`created_at`    datetime        NOT NULL,
`updated_at`    datetime        NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX snz_replies_user_id ON snz_replies (user_id);
CREATE INDEX snz_replies_topic_id ON snz_replies (topic_id);


-- -----------------------------------------------------
-- Table `snz_messages`    消息表
-- -----------------------------------------------------
CREATE TABLE `snz_messages` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主健',
  `user_id` bigint(20) NOT NULL COMMENT '创建者',
  `content` varchar(100) NOT NULL DEFAULT '' COMMENT '内容',
  `mtype` smallint(6) NOT NULL COMMENT '类型',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



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
  `module_num`              VARCHAR(64)  NULL COMMENT '模块编号',
  `module_name`             VARCHAR(64)  NULL COMMENT '模块名称',
  `complaint_content`       VARCHAR(512) NULL COMMENT '抱怨回馈内容',
  `complaint_reason`        VARCHAR(128) NULL COMMENT '抱怨原因',
  `created_at`              DATETIME     NOT NULL COMMENT '创建时间',
  `updated_at`              DATETIME     NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX `indx_snz_user_complaints_user_id` ON `snz_user_complaints`(`user_id`);




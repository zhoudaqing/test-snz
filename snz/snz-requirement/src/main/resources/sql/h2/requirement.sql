-- -----------------------------------------------------
-- Table `snz_requirements`    需求
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirements`;

CREATE TABLE IF NOT EXISTS `snz_requirements` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `flow_id`             VARCHAR(32) NULL  COMMENT '需求统一的流水码',
  `name`                VARCHAR(32) NOT NULL  COMMENT '采购需求名称',
  `purchaser_id`        BIGINT      NOT NULL  COMMENT '采购商编号(指的是注册公司的编号)',
  `purchaser_name`      VARCHAR(32) NOT NULL  COMMENT '采购商名称',
  `product_id`          BIGINT      NULL      COMMENT '产品类型（二级类目）',
  `product_name`        VARCHAR(32) NULL      COMMENT '产品类型名称（二级类目）',
  `series_ids`          VARCHAR(256)NOT NULL  COMMENT '系列编号(后台三级类目){sids:[{bcId:1,name:冰箱把手},{bcId:21,name:冰箱门}]}',
  `materiel_type`       LONG        NOT NULL  COMMENT '整体需求级别的物料类别（1:SKD，2:模块，3:连接件，4:包装印刷辅料，5:二三级物料）',
  `materiel_name`       VARCHAR(32) NOT NULL  COMMENT '物料类别名称',
  `module_type`         SMALLINT    NOT NULL  COMMENT '整体需求级别的模块类型（1:老品，2:新品，3:衍生号，4:甲指，5:专利）',
  `module_amount`       BIGINT      NULL      COMMENT '需求下所有模块的总价格（计算：n*模块*成本）',
  `delivery_address`    VARCHAR(64) NOT NULL  COMMENT '配送园区&厂商（使用json保存->{ad:[{pa:101,fa:10},{pa:101,fa:20}]）',
  `description`         VARCHAR(256)NOT NULL  COMMENT '需求描述信息',
  `accessories`         VARCHAR(256)NULL      COMMENT '上传附件内容（使用json保存->{file:[url1,url2]）',
  `select_num`       	  INT      	  NULL      COMMENT '选中的供应商数量(作为可选供应商的最大数量)',
  `replace_num`         INT      	  NULL      COMMENT '备选方案的供应商数量',
  `company_scope`       VARCHAR(256)NULL      COMMENT '供应商范围定义（用于圈定供应商的范围，作为选取依据。）',
  `tactics_id`          INT         NOT NULL  COMMENT '模块策略编号',
  `head_drop`           VARCHAR(32) NOT NULL  COMMENT '引领点',
  `coin_type`           SMALLINT    NOT NULL  COMMENT '需求级别的币种',
  `module_num`          INT         NOT NULL  COMMENT '用于统计采购中模块数量(这些数据都是在发布需求时确定计算出来)',
  `module_total`        INT         NOT NULL  COMMENT '用于统计需求所有的模块总数',
  `creator_id`          BIGINT      NOT NULL  COMMENT '创建需求的人员编号（用于索引对应的主管）',
  `creator_name`        VARCHAR(32) NOT NULL  COMMENT '需求创建人名称',
  `creator_phone`       VARCHAR(32) NOT NULL  COMMENT '需求创建人手机',
  `creator_email`       VARCHAR(32) NULL      COMMENT '需求创建人email',
  `check_result`        SMALLINT    NOT NULL  COMMENT '审核状态(0:未提交审核,1:待审核,2:审核通过,-1:审核不通过)',
  `check_id`            BIGINT      NULL      COMMENT '审核人员编号（主管）',
  `check_name`          VARCHAR(32) NULL      COMMENT '审核人员名称（主管名称）',
  `check_time`          DATETIME    NULL      COMMENT '审核时间',
  `transact_type`       SMALLINT    NULL      COMMENT '当需求阶段处于方案终投阶段&场景2｜3时该阶段会变成（谈判|竞标）',
  `transact_file`       VARCHAR(256)NULL      COMMENT '谈判文件([{name:1.doc, url:url1},{name:2.doc, url:url2}])',
  `send_time`           DATETIME    NULL      COMMENT '具体的发布时间（这个是从等待发布阶段跳转时记录）',
  `send_su`             INT         NULL      COMMENT '推送的供应商数量(各种需求的统计数据，当需求进入选定供应商阶段这些统计数据将从redis中回写到数据库)',
  `answer_su`           INT         NULL      COMMENT '响应的供应商数量',
  `send_so`             INT         NULL      COMMENT '提交方案的供应商数量',
  `topic_num`           INT         NULL      COMMENT '话题数',
  `status`              SMALLINT    NULL      COMMENT '需求状态（-1:删除状态，0:等待发布，1:需求交互，2:需求锁定，3:方案交互，4:方案综投，5:选定供应商与方案，6:招标结束:当所有的供应商都确认了跟标进入招标结束）',
  `solution_id`         BIGINT      NULL      COMMENT '最终方案编号，当状态为5时确认状态（这个是由需求供应商方案中选取的一个｜系统选取的最优方案）',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirements_purchaser_id ON snz_requirements (purchaser_id);
CREATE INDEX snz_requirements_creator_id ON snz_requirements (creator_id);
CREATE INDEX snz_requirements_check_id ON snz_requirements (check_id);
insert into snz_requirements (
  flow_id, name, purchaser_id, purchaser_name, product_id, product_name, series_ids, materiel_type, materiel_name, module_type, module_amount, delivery_address, description, accessories, select_num, replace_num,
  company_scope, tactics_id, head_drop, coin_type, module_num, module_total, creator_id, creator_name, creator_phone, creator_email,
  check_result, check_id, check_name, check_time, transact_type, transact_file, send_time, send_su, answer_su, send_so, topic_num, status, solution_id, created_at, updated_at
) values ('H-KT-LJ-JS-20140818000001', 'Michael', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 10000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 1, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 2, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, 0, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 2, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, 1, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 2, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, 2, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 2, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, 3, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 2, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, 4, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 2, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, 5, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', -1, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, null, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 0, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, null, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 1, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, null, null, now(), now()),
('H-KT-LJ-JS-20140818000001', 'MichaelZhao', 1, 'purchaser_name', 1, 'product_name', '{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}', 1, 'materiel_name', 1, 14000, '{ad:[{pa:101,fa:10},{pa:101,fa:20}]}', 'describe', '{file:[url1,url2]}', 3, 2
, '[{id:1,name:AT}]', 1, 'head_drop', 2, 10, 10000, 1, 'Michael', '18657327206', 'MichaelZhaoZero@gmail.com', 2, 1, 'Michael', '2014-5-10', 1, 'transact_file', '2014-5-12', 1, 1, 1, 1, null, null, now(), now());


-- -----------------------------------------------------
-- Table `snz_modules`    需求中的模块的需求详细信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_modules`;

CREATE TABLE IF NOT EXISTS `snz_modules` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `module_flowNum`      VARCHAR(32) NULL      COMMENT '模块统一的流水码',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `type`                INT         NOT NULL  COMMENT '模块类型（1:老品－>需要从后台获取先不处理，2:新品，3:已存入老品表）',
  `module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称',
  `old_moduleId`        BIGINT      NULL      COMMENT '我们系统的老品编号(老品表)',
  `module_num`          VARCHAR(32) NULL      COMMENT '模块专用号(海尔的系统生成的编号)',
  `series_id`           BIGINT      NULL      COMMENT '系列编号(模块类)',
  `series_name`         VARCHAR(32) NULL      COMMENT '系列名称',
  `property_id`         BIGINT      NULL      COMMENT '类目属性',
  `total`               INT         NOT NULL  COMMENT '全部的资源量',
  `quality`             INT         NULL      COMMENT '质量目标（可以有创建者or团队人员填写）',
  `cost`                INT         NULL      COMMENT '成本目标',
  `delivery`            INT         NULL      COMMENT '产能要求',
  `attestations`        VARCHAR(128)NULL      COMMENT '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
  `supply_at`           DATETIME    NULL      COMMENT '批量供货时间',
  `units`               VARCHAR(128)NULL      COMMENT '各种单位的json字段{cost:{salesId:1, name:EA, quantityId:1, name:百个},delivery:{salesId:2, name:EA, quantityId:4, name:十个}}',
  `select_num`          INTEGER     NULL      COMMENT '每个模块选取的供应商数量',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_modules_requirement_id ON snz_modules (requirement_id);
insert into snz_modules (module_flowNum, requirement_id, type, module_name, old_moduleId, module_num, series_id, series_name, property_id, total, quality, cost,
delivery, attestations, supply_at, units, select_num, created_at, updated_at
) values ('H-KT-LJ-20140818000001', 1, 1, '模块名称1', null, null, 1, '系列名称0', 1, 3000, 12, 12, 22, '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
now(), '{"cost":{"salesId":"1","salesName":"EA","quantityId":"1","quantityName":1},"delivery":{"salesId":"1","salesName":"EA"}}', 3, now(), now()),
('H-KT-LJ-20140818000001', 1, 1, '模块名称2', null, null, 2, '系列名称1', 1, 3000, 12, 12, 22, '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
now(), '{"cost":{"salesId":"1","salesName":"EA","quantityId":"1","quantityName":1},"delivery":{"salesId":"1","salesName":"EA"}}', 2, now(), now()),
('H-KT-LJ-20140818000001', 1, 1, '模块名称2', null, null, 2, '系列名称1', 1, 3000, 12, 12, 22, '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
now(), '{"cost":{"salesId":"1","salesName":"EA","quantityId":"1","quantityName":1},"delivery":{"salesId":"1","salesName":"EA"}}', 2, now(), now()),
('H-KT-LJ-20140818000001', 1, 1, '模块名称3', null, null, 3, '系列名称2', 1, 3000, 12, 12, 22, '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
now(), '{"cost":{"salesId":"1","salesName":"EA","quantityId":"1","quantityName":1},"delivery":{"salesId":"1","salesName":"EA"}}', 3, now(), now()),
('H-KT-LJ-20140818000001', 1, 1, '模块名称4', null, null, 4, '系列名称3', 1, 3000, 12, 12, 22, '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
now(), '{"cost":{"salesId":"1","salesName":"EA","quantityId":"1","quantityName":1},"delivery":{"salesId":"1","salesName":"EA"}}', 1, now(), now()),
('H-KT-LJ-20140818000001', 1, 3, '模块名称5', 1, null, 5, '系列名称5', 1, 3000, 12, 12, 22, '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
now(), '{"cost":{"salesId":"1","salesName":"EA","quantityId":"1","quantityName":1},"delivery":{"salesId":"1","salesName":"EA"}}', 2, now(), now()),
('H-KT-LJ-20140818000001', 1, 3, '模块名称6', 2, null, 6, '系列名称6', 1, 3000, 12, 12, 22, '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
now(), '{"cost":{"salesId":"1","salesName":"EA","quantityId":"1","quantityName":1},"delivery":{"salesId":"1","salesName":"EA"}}', 3, now(), now()),
('H-KT-LJ-20140818000001', 1, 2, '模块名称7', 2, 'A01', 6, '系列名称6', 1, 3000, 12, 12, 22, '[{id:10,name:''EU认证''}, {id:10,name:''EU认证''}]',
now(), '{"cost":{"salesId":"1","salesName":"EA","quantityId":"1","quantityName":1},"delivery":{"salesId":"1","salesName":"EA"}}', 3, now(), now());



-- -----------------------------------------------------
-- Table `snz_requirement_teams`    交互团队成员(将这个表从模版表中分离出来的原因－》需要从交互团队人员的纬度去查询他参加的团队信息)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_teams`;

CREATE TABLE IF NOT EXISTS `snz_requirement_teams` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求模版编号',
  `requirement_name`    VARCHAR(32) NOT NULL  COMMENT '需求名称',
  `type`                SMALLINT    NOT NULL  COMMENT '绑定的节点类型（1:T技术,2:Q质量,3:R互动,4:D产能,5:C成本）',
  `user_id`             BIGINT      NOT NULL  COMMENT '队员编号',
  `user_name`           VARCHAR(32) NOT NULL  COMMENT '队员名称',
  `user_number`         VARCHAR(32) NOT NULL  COMMENT '队员工号',
  `user_phone`          VARCHAR(32) NULL      COMMENT '队员联系电话',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_teams_requirement_id ON snz_requirement_teams (requirement_id);
CREATE INDEX snz_requirement_teams_user_id ON snz_requirement_teams (user_id);
insert into snz_requirement_teams (requirement_id, requirement_name, type, user_id, user_name, user_number, user_phone, created_at, updated_at)
values (1, 'requirement', 1, 1, 'michael', '1120', '18657327206', now(), now()),(1, 'requirement', 2, 1, 'michael', '1120', '18657327206', now(), now());



-- ------------------------------------------------------------------------------------
-- Table `snz_requirement_times`    交互时间(这个是实际的操作的时间（包括延期的操作后的时间）)
-- ------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_times`;

CREATE TABLE IF NOT EXISTS `snz_requirement_times` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求模版编号',
  `type`                SMALLINT    NOT NULL  COMMENT '交互状态时间（1:需求交互,2:需求锁定,3:方案交互,4:方案综投,5:选定供应商&方案）',
  `predict_start`       DATE        NOT NULL  COMMENT '预计开始时间',
  `predict_end`         DATE        NOT NULL  COMMENT '预计结束时间',
  `user_id`             BIGINT      NULL      COMMENT '阶段提交操作的用户编号(0:系统自动切换状态)',
  `user_name`           VARCHAR(32) NULL      COMMENT '阶段提交操作的用户姓名',
  `actual_start`        DATE        NULL      COMMENT '实际开始时间',
  `actual_end`          DATE        NULL      COMMENT '实际结束时间',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_times_requirement_id ON snz_requirement_times (requirement_id);
insert into snz_requirement_times (requirement_id, type, predict_start, predict_end, user_id, user_name, actual_start, actual_end, created_at, updated_at)
values (1, 1, '2014-06-01', '2014-06-08', 1, 'Michael', '2014-06-02', null, '2014-05-02', '2014-05-02'),
(1, 2, '2014-05-02', '2014-05-02', null, null, '2014-05-03', null, '2014-05-02', '2014-05-02'),
(1, 3, '2014-05-02', '2014-05-02', null, null, '2014-05-03', '2014-05-02', '2014-05-02', '2014-05-02'),
(1, 4, '2014-05-02', '2014-05-02', null, null, '2014-05-03', '2014-05-02', '2014-05-02', '2014-05-02'),
(1, 5, '2014-05-02', '2014-05-02', null, null, '2014-05-03', '2014-05-02', '2014-05-02', '2014-05-02'),
(2, 1, '2014-06-01', '2014-06-08', 1, 'Michael', '2014-06-02', null, '2014-05-02', '2014-05-02'),
(2, 2, '2014-05-02', '2014-05-02', null, null, '2014-05-03', null, '2014-05-02', '2014-05-02'),
(2, 3, '2014-05-02', '2014-05-02', null, null, '2014-05-03', '2014-05-02', '2014-05-02', '2014-05-02'),
(2, 4, '2014-05-02', '2014-05-02', null, null, '2014-05-03', '2014-05-02', '2014-05-02', '2014-05-02'),
(2, 5, '2014-05-02', '2014-05-02', null, null, '2014-05-03', '2014-05-02', '2014-05-02', '2014-05-02');


-- -----------------------------------------------------
-- Table `snz_req_predict_times`    预计的交互时间计划(主要是用于让前台查看无语)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_req_predict_times`;

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
insert into snz_req_predict_times ( requirement_id, type, predict_start, predict_end, created_at, updated_at ) values (1, 1, '2014-05-03', '2014-05-03', now(), now()),
(1, 2, '2014-05-04', '2014-05-04', now(), now()),(1, 3, '2014-05-04', '2014-05-04', now(), now()),(1, 4, '2014-05-05', '2014-05-05', now(), now());


-- -----------------------------------------------------
-- Table `snz_requirement_solutions`    对应与需求的供应商提交方案
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_solutions`;

CREATE TABLE IF NOT EXISTS `snz_requirement_solutions` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `requirement_name`    VARCHAR(32) NOT NULL  COMMENT '需求名称',
  `user_id`             BIGINT      NOT NULL  COMMENT '用户编号（用于圈定话题的参与人作用）',
  `supplier_id`         BIGINT      NOT NULL  COMMENT '供应商编号',
  `supplier_name`       VARCHAR(32) NOT NULL  COMMENT '供应商名称',
  `technology`          INT         NULL      COMMENT '由采购商对方案的技术进行评分',
  `quality`             INT         NULL      COMMENT '质量（这些数据都是后期系统自动计算出来的）',
  `reaction`            DATE        NULL      COMMENT '互动',
  `delivery`            INT         NULL      COMMENT '产能',
  `cost`                INT         NULL      COMMENT '成本（这个是通过需求模块报价计算出来的一个数据参考数据）',
  `not_accept`          VARCHAR(256)NULL      COMMENT '无法承诺信息(NULL:承诺,填写信息表示无法承诺)',
  `solution_file`       VARCHAR(4096)NULL      COMMENT '上传的详细的方案文档',
  `quotation_file`      VARCHAR(256)NULL      COMMENT '上传的详细的报价文档信息',
  `topic_id`            BIGINT      NULL      COMMENT '对应供应商的唯一的topicId为了方便交谈（完全就是一个扯淡的交互）',
  `status`              SMALLINT    NULL      COMMENT '在方案终投的情况下（标记方案终投阶段只能投递一次：0:未承诺, 1:全部承诺, 2:部分无法承诺, 3:已提交,无法更改的提交）',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_solutions_requirement_id ON snz_requirement_solutions (requirement_id);
CREATE INDEX snz_requirement_solutions_supplier_id ON snz_requirement_solutions (supplier_id);
insert into snz_requirement_solutions (requirement_id, requirement_name, user_id, supplier_id, supplier_name, technology, quality, reaction, delivery, cost,
not_accept, solution_file, quotation_file, topic_id, status, created_at, updated_at
) values (1, 'requirement0', 1, 1, 'Michael', null, null, null, null, null, null, '上传的详细的方案文档', '上传的详细的报价文档信息', 100, 3, now(), now()),
(1, 'requirement1', 2, 2, 'MichaelZhao', null, null, null, null, 1500, null, '上传的详细的方案文档', '上传的详细的报价文档信息', 100, 3, now(), now()),
(1, 'requirement1', 3, 3, 'MichaelZhao', null, null, null, null, 1200, null, '上传的详细的方案文档', '上传的详细的报价文档信息', 100, 3, now(), now()),
(2, 'requirement2', 2, 2, 'Michael', null, null, null, null, null, null, '上传的详细的方案文档', '上传的详细的报价文档信息', 100, 0, now(), now()),
(3, 'requirement2', 2, 2, 'Michael', null, null, null, null, null, null, '上传的详细的方案文档', '上传的详细的报价文档信息', 100, 1, now(), now());



-- ------------------------------------------------------
-- Table `snz_module_solutions`    对应与模块的供应商提交方案
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_module_solutions`;

CREATE TABLE IF NOT EXISTS `snz_module_solutions` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `solution_id`         BIGINT      NOT NULL  COMMENT '整体方案编号',
  `module_id`           BIGINT      NOT NULL  COMMENT '模块编号（这个是具体模块需求的编号）',
  `module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称(这个是模块的名称)',
  `technology`          VARCHAR(128)NOT NULL  COMMENT '技术',
  `quality`             INT         NOT NULL  COMMENT '质量(单位ppm)',
  `reaction`            DATE        NOT NULL  COMMENT '互动',
  `delivery`            INT         NOT NULL  COMMENT '产能',
  `cost`                INT         NULL      COMMENT '成本',
  `units`               VARCHAR(128)NULL      COMMENT '各种单位的json字段{cost:{salesId:1, name:EA, quantityId:1, name:百个},delivery:{salesId:2, name:EA, quantityId:4, name:十个}}',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_module_solutions_solution_id ON snz_module_solutions (solution_id);
insert into snz_module_solutions (solution_id, module_id, module_name, technology, quality, reaction, delivery, cost, units, created_at, updated_at
) values (1, 1, 'moduleName', 'technology', 100, '2014-06-10', 10, 10, '{cost:{}}', now(), now()),
(1, 2, 'moduleName', 'technology', 100, '2014-06-10', 10, 20, '{cost:{}}', now(), now()),
(1, 3, 'moduleName', 'technology', 100, '2014-06-10', 10, 100, '{cost:{}}', now(), now()),
(2, 4, 'moduleName', 'technology', 100, '2014-06-10', 10, 30, '{cost:{}}', now(), now()),
(2, 1, 'moduleName', 'technology', 100, '2014-06-10', 10, 30, '{cost:{}}', now(), now()),
(2, 2, 'moduleName', 'technology', 100, '2014-06-10', 10, 90, '{cost:{}}', now(), now()),
(2, 3, 'moduleName', 'technology', 100, '2014-06-10', 10, 30, '{cost:{}}', now(), now()),
(2, 4, 'moduleName', 'technology', 100, '2014-06-10', 10, 10, '{cost:{}}', now(), now()),
(3, 1, 'moduleName', 'technology', 100, '2014-06-10', 10, 23, '{cost:{}}', now(), now()),
(3, 2, 'moduleName', 'technology', 100, '2014-06-10', 10, 45, '{cost:{}}', now(), now()),
(3, 3, 'moduleName', 'technology', 100, '2014-06-10', 10, 90, '{cost:{}}', now(), now()),
(3, 4, 'moduleName', 'technology', 100, '2014-06-10', 10, 100, '{cost:{}}', now(), now());


-- ------------------------------------------------------
-- Table `snz_module_quotations`    对应与模块的报价信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_module_quotations`;

CREATE TABLE IF NOT EXISTS `snz_module_quotations` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `solution_id`         BIGINT      NOT NULL  COMMENT '整体方案编号',
  `module_id`           BIGINT      NOT NULL  COMMENT '模块编号（这个是具体模块需求的编号）',
  `module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称(这个是模块的名称)',
  `supplier_id`         BIGINT      NOT NULL  COMMENT '供应商编号(这些是为了方便查询)',
  `supplier_name`       VARCHAR(32) NOT NULL  COMMENT '供应商名称',
  `price`               INT         NOT NULL  COMMENT '计算单价',
  `transact_price`      INT         NULL      COMMENT '新增的谈判单价',
  `cost_unit`           VARCHAR(128) NULL      COMMENT '价格单位的json字段',
  `total`               INT         NOT NULL  COMMENT '全部的资源量(为了方便计算模块的C数值)',
  `coin_type`           VARCHAR(32) NULL      COMMENT '货币(指的是货币类型RMB,$)',
  `exchange_rate`       INT         NOT NULL  COMMENT '汇率',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_module_quotations ON snz_module_solutions (solution_id);
insert into snz_module_quotations (solution_id, module_id, module_name, supplier_id, supplier_name, price, transact_price, cost_unit, total, coin_type, exchange_rate, created_at, updated_at
) values (1, 1, 'moduleName', 1, '供应商名称', 1000, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 10, now(), now()),
(1, 2, 'moduleName', 1, '供应商名称', 3000, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 20, now(), now()),
(1, 3, 'moduleName', 1, '供应商名称', 1500, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 150, now(), now()),
(2, 1, 'moduleName', 2, '供应商名称', 3000, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 15, now(), now()),
(2, 2, 'moduleName', 2, '供应商名称', 1500, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 150, now(), now()),
(2, 3, 'moduleName', 2, '供应商名称', 1000, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 25, now(), now()),
(2, 4, 'moduleName', 2, '供应商名称', 3000, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 25, now(), now()),
(3, 1, 'moduleName', 3, '供应商名称', 1500, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 10, now(), now()),
(3, 2, 'moduleName', 3, '供应商名称', 1000, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 25, now(), now()),
(3, 3, 'moduleName', 3, '供应商名称', 3000, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 250, now(), now()),
(3, 4, 'moduleName', 3, '供应商名称', 1500, 890, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 18236, 'coin_type', 150, now(), now());


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
  `supplier_name`       VARCHAR(32) NOT NULL  COMMENT '供应商名称selectReason',
  `quota_scale`         INT         NULL      COMMENT '配额比例数据信息5%',
  `select_reason`       VARCHAR(256)NULL      COMMENT '选择该方案的原因',
  `select_file`         VARCHAR(128)NULL      COMMENT '选择该方案的上传文档',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_ranks_requirement_id ON snz_requirement_ranks (requirement_id);
CREATE INDEX snz_requirement_ranks_supplier_id ON snz_requirement_ranks (supplier_id);
insert into snz_requirement_ranks (requirement_id, rank, type, supplier_id, supplier_name, quota_scale, select_reason, select_file, created_at, updated_at
) values (1, 1, 1, 1, 'Michael', 30, 'selectReason', 'fileURL', now(), now());



-- -----------------------------------------------------
-- Table `snz_module_quotas`    最终每个供应商对应与模块的配额信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_module_quotas`;

CREATE TABLE IF NOT EXISTS `snz_module_quotas` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `solution_id`         BIGINT      NOT NULL  COMMENT '方案编号方便查询new',
  `module_id`           BIGINT      NOT NULL  COMMENT '模块编号',
  `module_num`          VARCHAR(32) NULL      COMMENT '模块专用号',
  `module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称',
  `module_factoryId`    BIGINT      NULL      COMMENT '模块工厂配额编号',
  `factory_num`         VARCHAR(16) NULL      COMMENT '模块工厂编号',
  `factory_name`        VARCHAR(64) NULL      COMMENT '模块工厂名称',
  `supplier_id`         BIGINT      NOT NULL  COMMENT '供应商编号',
  `supplier_name`       VARCHAR(32) NOT NULL  COMMENT '供应商名称',
  `quantity`            INT         NULL      COMMENT '配额数量',
  `scale`               INT         NULL      COMMENT '模块配额的占总配额的比例70,50',
  `original_cost`       INT         NULL      COMMENT '用户的模块报价',
  `actual_cost`         INT         NULL      COMMENT '实际模块跟标价格',
  `cost_unit`           VARCHAR(128)NULL      COMMENT '价格单位的json字段',
  `status`              SMALLINT    NULL      COMMENT '0:不同意,1:同意。是否同意配额的分配(这种状态主要是针对于C(报价来确认配额)这种情况)',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_module_quotas_requirement_id ON snz_module_quotas (requirement_id);
CREATE INDEX snz_module_quotas_solution_id ON snz_module_quotas (solution_id);
CREATE INDEX snz_module_quotas_supplier_id ON snz_module_quotas (supplier_id);
insert into snz_module_quotas (requirement_id, solution_id, module_id, module_num, module_name, module_factoryId, factory_num, factory_name, supplier_id, supplier_name, quantity, scale, original_cost, actual_cost, cost_unit, status, created_at, updated_at
) values (1, 1, 1, 'moduleNum', 'moduleName', 1, '9889', '模块工厂名称', 2, 'Michael', 100, 80, 10, 20, '{salesId:1, name:EA, quantityId:1, quantityName:1}', null, now(), now()),
(1, 1, 1, 'moduleNum', 'moduleName', 1, '9889', '模块工厂名称', 2, 'Michael', 100, 80, 10, 20, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 1, now(), now()),
(1, 1, 1, 'moduleNum', 'moduleName', 1, '9889', '模块工厂名称', 2, 'Michael', 100, 80, 10, 20, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 1, now(), now()),
(1, 1, 1, 'moduleNum', 'moduleName', 1, '9889', '模块工厂名称', 3, 'Michael', 100, 80, 10, 20, '{salesId:1, name:EA, quantityId:1, quantityName:1}', 1, now(), now());


-- -----------------------------------------------------
-- Table `snz_old_modules`    老品模块信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_old_modules`;

CREATE TABLE IF NOT EXISTS `snz_old_modules` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
`requirement_name`    VARCHAR(32) NOT NULL  COMMENT '需求名称',
`purchaser_id`        BIGINT      NOT NULL  COMMENT '采购商编号(指的是注册公司的编号)',
`purchaser_name`      VARCHAR(32) NOT NULL  COMMENT '采购商名称',
`module_id`           BIGINT      NULL  COMMENT '模块编号（由海尔的系统分配的编号）',
`module_name`         VARCHAR(32) NOT NULL  COMMENT '模块名称',
`module_num`          VARCHAR(32) NULL      COMMENT '模块专用号(海尔的系统生成的编号--物料号)',
`total`               INT         NOT NULL  COMMENT '全部的资源量',
`series_id`           BIGINT      NOT NULL  COMMENT '系列编号（对应后台的二级类目）',
`series_name`         VARCHAR(32) NOT NULL  COMMENT '系列名称',
`quota_ids`           VARCHAR(128)NULL  COMMENT '配额ids 1,2,3...',
`over_time`           DATE        NULL  COMMENT '反超期',
`tactics_id`          INT         NOT NULL  COMMENT '模块策略编号',
`head_drop`           VARCHAR(32) NOT NULL  COMMENT '引领点',
`resource_num`        VARCHAR(128)NOT NULL  COMMENT '这个是设置不同工厂的不同需求量（json格式rs:[{fa:101,num:1000},{fa:102,num:2000}]）',
`quality`             INT         NULL      COMMENT '质量目标（可以有创建者or团队人员填写）',
`cost`                INT         NULL      COMMENT '成本目标',
`delivery`            INT         NULL      COMMENT '产能要求',
`attestation`         VARCHAR(128)NULL      COMMENT '认证要求',
`supply_at`           DATETIME    NOT NULL  COMMENT '批量供货时间',
`resource_count`      INT         NULL      COMMENT '资源计数',
`time_total`          INT         NULL      COMMENT '计时（天）',
`interaction_starts`  SMALLINT    NULL      COMMENT '交互启动',
`npi_status`          SMALLINT    NULL      COMMENT 'NPI状态',
`priority_select`     SMALLINT    NULL      COMMENT '优选',
`created_at`          DATETIME    NULL      COMMENT '创建时间',
`updated_at`          DATETIME    NULL      COMMENT '修改时间',
PRIMARY KEY (`id`));
insert into snz_old_modules (purchaser_id,requirement_id,requirement_name,purchaser_name,module_id,module_name,module_num,
total,series_id,series_name,quota_ids,over_time,tactics_id,
head_drop,resource_num,quality,cost,delivery,attestation,supply_at,resource_count,
time_total,interaction_starts,npi_status,priority_select,created_at,updated_at)
values(1,1,'reqname','p',5,'moudlename','modulenum',100,1,'seriesname','1,2,3',now(),1,'headdrop','rs:[{fa:101,num:1000},{fa:102,num:2000}]',1,2,3,
'attestation',now(),2,3,4,5,6,now(),now()),(1,2,'reqname2','p2',6,'moudlename2','modulenum2',200,1,'seriesname2','1,2,3',now(),1,'headdrop2','rs:[{fa:101,num:2000},{fa:102,num:3000}]',1,2,3,
'attestation2',now(),1,1,1,1,1,now(),now()),(1,3,'reqname3','p3',1,'moudlename3','modulenum3',100,1,'seriesname3','4,5,6',now(),1,'headdrop3','rs:[{fa:100,num:1001},{fa:102,num:2000}]',1,2,3,
'attestation3',now(),2,3,3,3,3,now(),now()),(1,5,'reqname4','p4',1,'moudlename4','modulenum4',100,1,'seriesname4','1,1,1',now(),1,'headdrop4','rs:[{fa:101,num:1000},{fa:102,num:2000}]',1,2,3,
'attestation4',now(),5,3,7,5,6,now(),now());


-- -----------------------------------------------------
-- Table `snz_deposits`     保证金
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_deposits`;

CREATE TABLE IF NOT EXISTS `snz_deposits` (
  `id`                BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`    BIGINT      NOT NULL  COMMENT '需求id',
  `supplier_id`       BIGINT      NOT NULL  COMMENT '采购商id',
  `deal_id`           VARCHAR(64) NULL      COMMENT '订单号（快捷通）',
  `deal_time`         DATETIME    NULL      COMMENT '快捷通订单交易时间',
  `deal_url`          VARCHAR(128) NULL     COMMENT '快捷通交易链接',
  `amount`            BIGINT      NOT NULL  COMMENT '保证金金额',
  `bank_info`         TEXT        NULL      COMMENT '退款用银行卡号等',
  `type`              INT         NOT NULL  COMMENT '保证金类型（1: 付款，2:退款）',
  `status`            SMALLINT    NOT NULL  COMMENT '保证金状态（0：未提交，1：提交成功，-1：提交失败，2：付款成功，-2：付款失败，3：退款成功，-3：退款失败）',
  `sync_status`       SMALLINT    NOT NULL  COMMENT '与海尔SAP系统同步位（0:未同步，1:已同步）',
  `created_at`        DATETIME    NULL      COMMENT '创建时间',
  `updated_at`        DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));

CREATE INDEX snz_dep_requirement_id ON snz_deposits (requirement_id);
CREATE INDEX snz_dep_supplier_id ON snz_deposits (supplier_id);
CREATE INDEX snz_dep_deal_id ON snz_deposits (deal_id);


-- -----------------------------------------------------
-- Table `snz_address_park`    园区信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_address_park`;

CREATE TABLE IF NOT EXISTS `snz_address_park` (
`id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
`park_name`           VARCHAR(10) NOT NULL  COMMENT '园区名称',
PRIMARY KEY (`id`));
insert into snz_address_park (park_name) values('重庆'),('平度'),('沈阳'),('青岛'),('大连'),('贵州'),('武汉'),('顺德'),('郑州'),('合肥');

-- -----------------------------------------------------
-- Table `snz_address_factory`  工厂信息
-- -----------------------------------------------------

DROP TABLE IF EXISTS `snz_address_factory`;

CREATE TABLE IF NOT EXISTS `snz_address_factory` (
  `id`                BIGINT          NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `product_id`        BIGINT          NOT NULL  COMMENT '产品类型（一级类目）',
  `park_id`           BIGINT          NOT NULL  COMMENT '园区Id',
  `factory_num`       VARCHAR(10)     NOT NULL  COMMENT '工厂代码',
  `factory_name`      VARCHAR(50)     NOT NULL  COMMENT '工厂名称',
  PRIMARY KEY (`id`));

insert into snz_address_factory (product_id, park_id, factory_num, factory_name) values(1,4,'9010','青岛海尔股份有限公司冰箱中一工厂'),(1,4,'9040','青岛海尔电冰箱有限公司冰箱中二工厂'),(1,4,'9050','青岛海尔特种电冰箱有限公司特种冰箱工厂'),
(1,4,'9210','青岛海尔特种塑料研制开发有限公司特塑工厂'),(1,2,'9020','青岛海尔电冰箱(国际)有限公司海外冰箱工厂'),(1,10,'980Z','合肥海尔电冰箱有限公司合肥冰箱工厂'),(1,10,'988N','重庆海尔制冷电器有限公司重庆冰箱委外工厂'),
(1,3,'9A20','沈阳海尔电冰箱有限公司沈阳工厂'),(1,1,'9888','重庆海尔制冷电器有限公司重庆冰箱工厂'),(1,6,'9030','贵州海尔电器有限公司贵州冰箱工厂'),(1,5,'9060','大连海尔电冰箱有限公司大连冰箱外销工厂'),
(1,5,'9070.0','大连海尔电冰箱有限公司大连冰箱内销工厂'),(2,4,'9090','青岛海尔特种电冰柜有限公司青岛冷柜工厂'),(2,4,'9092','青岛海尔特种电冰柜有限公司商用冷柜工厂'),(2,7,'9110','武汉海尔电冰柜有限公司武汉冷柜工厂'),
(2,8,'9A30','佛山海尔电冰柜有限公司佛山工厂'),(3,4,'9393','青岛海尔特种电器有限公司生物医疗工厂'),(3,4,'9091','青岛海尔特种电冰柜有限公司医疗产品工厂'),(4,4,'9790','海尔盈德喜（青岛）电器有限公司'),
(4,4,'9792','海尔盈德喜（青岛）电器有限公司AQUA工厂'),(4,1,'9A10','重庆海尔滚筒洗衣机有限公司'),(5,1,'9A1N','重庆海尔洗衣机有限公司重庆洗衣机委外工厂'),(4,8,'9A80','佛山海尔滚筒洗衣机有限公司工厂'),
(4,8,'9261','佛山市顺德海尔电器有限公司滚筒洗衣机工厂'),(6,4,'9220','青岛海尔洗衣机有限公司青岛洗衣机工厂'),(6,4,'9270','青岛胶南海尔洗衣机有限公司胶南洗衣机工厂'),(6,10,'925Z','合肥海尔洗衣机有限公司合肥洗衣机工厂'),
(6,10,'9A1N','重庆海尔洗衣机有限公司重庆洗衣机委外工厂'),(6,8,'9260','佛山市顺德海尔电器有限公司波轮洗衣机工厂'),(6,1,'9700','重庆海尔洗衣机有限公司重庆洗衣机工厂'),(7,4,'9120','青岛海尔空调器有限总公司青岛空调工厂'),
(7,4,'9230','青岛海尔（胶州）空调器有限公司胶州空调工厂'),(7,10,'916Z','合肥海尔空调器有限公司合肥空调工厂'),(7,10,'968N','重庆海尔空调器有限公司重庆空调委外工厂'),(7,7,'9170','武汉海尔电器股份有限公司武汉空调工厂'),
(7,1,'9680','重庆海尔空调器有限公司重庆空调工厂'),(7,9,'9B10','郑州海尔空调器有限公司郑州空调工厂'),(7,5,'9130','大连海尔空调器有限公司大连空调外销工厂'),(8,4,'9190','青岛海尔空调电子有限公司商用空调工厂'),(8,4,'9191','青岛海尔空调电子有限公司中央空调工厂'),
(9,7,'9310','武汉海尔热水器有限公司武汉电热工厂'),(9,4,'9400','青岛经济技术开发区海尔热水器有限公司黄岛电热工厂'),(9,4,'9402','青岛经济技术开发区海尔热水器有限公司胶南电热工厂'),(10,1,'9710','重庆海尔热水器有限公司重庆燃气工厂'),
(11,4,'9403','青岛经济技术开发区海尔热水器有限公司胶南太阳能工厂'),(11,4,'9401','青岛经济技术开发区海尔热水器有限公司黄岛太阳能工厂'),(12,4,'9420','青岛海尔洗碗机有限公司洗碗机产品工厂'),(13,4,'9422','青岛海尔洗碗机有限公司油烟机产品工厂');



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
insert into snz_backend_categorys (name , parent_id, level, has_children, created_at, updated_at) values('冰箱', 0, 1, 1, now(), now()),('冷柜', 0, 1, 1, now(), now()),('医疗冷柜', 0, 1, 1, now(), now()),('滚筒洗衣机', 0, 1, 1, now(), now()),
('波轮洗衣机', 0, 1, 1, now(), now()),('双桶洗衣机/全自动洗衣机', 0, 1, 1, now(), now()),('家用空调', 0, 1, 1, now(), now()),('商用空调', 0, 1, 1, now(), now()),('电热水器', 0, 1, 1, now(), now()),('燃气热水器', 0, 1, 1, now(), now()),('太阳能热水器', 0, 1, 1, now(), now()),
('洗碗机', 0, 1, 1, now(), now()),('油烟机', 0, 1, 1, now(), now());




-- -----------------------------------------------------
-- Table `snz_derivative_diff`  衍生品信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_derivative_diff`;
CREATE TABLE `snz_derivative_diff` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id` bigint NOT NULL COMMENT '需求id',
  `module_id` bigint NOT NULL COMMENT '模块id',
  `module_name` varchar(32)  NOT NULL COMMENT '模块名称',
  `bom_module` smallint NOT NULL COMMENT '模块BOM(模块主关件不变，0:子件增加,1:子件减少,2:子件变更)',
  `matrix` smallint DEFAULT NULL COMMENT '模具(模块主关件符合衍生号要求，0:子件增加,1:子件减少,2:子件变更)',
  `material` smallint DEFAULT NULL COMMENT '材料（0:相同,1:不同）',
  `surface_treatment` smallint DEFAULT NULL COMMENT '表面处理工艺（0：喷涂，1：电镀，2：喷粉，3：其他）',
  `printing` smallint DEFAULT NULL COMMENT '印刷（0：颜色，1：商标，2：文字，3：花纹）',
  `software_param` smallint DEFAULT NULL COMMENT '软件、参数（0：软件，1：参数）',
  `structure` smallint DEFAULT NULL COMMENT '结构（0：同截面不同长度，1：加工细节）',
  `overseas_parts` smallint DEFAULT NULL COMMENT '是否用于海外散件（0：否，1：是）',
  `host_change` smallint DEFAULT NULL COMMENT '主关件参数变化（0：电压频率，1：其他）',
  `drive` smallint DEFAULT NULL COMMENT '接口相同，传动方式或传动比不同（传动方式：0，传动比：1）',
  `description` varchar(256)  DEFAULT NULL COMMENT '差异其他描述',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);
 insert into snz_derivative_diff (requirement_id,module_id,module_name,bom_module,matrix,material,surface_treatment,printing,
 software_param,structure,overseas_parts,host_change,drive,description,created_at,updated_at)
 values
   (1,1,'name' ,1,0,0,0,0,0,0,0,0,0,'description1',now(),now()),
   (1,2,'name2',1,1,1,1,1,1,1,1,1,1,'description2',now(),now()),
   (1,3,'name3',1,2,1,1,1,0,0,1,0,0,'description3',now(),now()),
   (1,4,'name4',1,0,1,0,1,0,1,0,1,0,'description4',now(),now());



-- ------------------------------------------------------
-- start 新品导入明细表
-- ------------------------------------------------------
DROP TABLE IF EXISTS `snz_income_goods`;

create table IF NOT EXISTS `snz_income_goods` (
  `id`                          bigint not null AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id`                 bigint not null COMMENT '采购商的用户ID',
  `requirement_id`              bigint not null COMMENT '冗余需求ID',
  `company_id`                  bigint not null COMMENT '冗余公司ID',
  `module_id`                   bigint not null COMMENT '模块的ID',
  `company_name`                varchar(255) not null COMMENT '冗余公司名',

  `estimate_contract`            bigint DEFAULT null COMMENT '合同签订记录',
  `estimate_design`              bigint DEFAULT null COMMENT '图纸详细设计',
  `estimate_moduling`            bigint DEFAULT null COMMENT '模具开制',
  `estimate_detection`           bigint DEFAULT null COMMENT '检验单',
  `estimate_self_test`           bigint DEFAULT null COMMENT '自检报告',
  `estimate_sample`              bigint DEFAULT null COMMENT '收样(开发)',
  `estimate_icq`                 bigint DEFAULT null COMMENT 'IQC检验',
  `estimate_form_test`           bigint DEFAULT null COMMENT '型式试验',
  `estimate_test_conclusion`     bigint DEFAULT null COMMENT '检测结论',
  `estimate_final_conclusion`    bigint DEFAULT null COMMENT '最终结论',

  `in_charge`                    varchar(64) DEFAULT null COMMENT '责任位',
  `stage`                        integer DEFAULT null COMMENT '当前进行的步骤',

  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);
create INDEX snz_income_goods_supplier         on `snz_income_goods` (supplier_id);
create INDEX snz_income_goods_requirement      on `snz_income_goods` (requirement_id);
create INDEX snz_income_goods_module           on `snz_income_goods` (module_id);

-- ------------------------------------------------------
-- end   新品导入明细表
-- ------------------------------------------------------


-- ------------------------------------------------------
-- start 新品导入明细的行记录表
-- ------------------------------------------------------
DROP TABLE IF EXISTS `snz_income_good_rows`;

create table IF NOT EXISTS `snz_income_good_rows` (
  `id`         bigint      not null AUTO_INCREMENT COMMENT '自增主键',

  `timeline`   datetime    DEFAULT NULL COMMENT '预算时间',
  `duration`   integer     DEFAULT NULL COMMENT '预算周期',
  `progress`   datetime    DEFAULT NULL COMMENT '进度',
  `status`     integer     DEFAULT NULL COMMENT '状态',
  `in_charge`  VARCHAR(64) DEFAULT NULL COMMENT '责任人',
  `stage`      smallint    DEFAULT NULL COMMENT '这条记录为什么步骤，1- 合同签订记录，2- 图纸详细设计，3- 模具开制，
  4- 检验单，5- 自检报告，6- 收样(开发)，7- IQC检验，8- 型式试验，9- 检测结论，10- 最终结论',

  `created_at` datetime    DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime    DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);

-- ------------------------------------------------------
-- end   新品导入明细的行记录表
-- ------------------------------------------------------


-- ------------------------------------------------------
-- start 新品导入明细的行记录表
-- ------------------------------------------------------
DROP TABLE IF EXISTS `snz_income_good_lists`;

create table IF NOT EXISTS `snz_income_good_lists` (
  `id`               bigint      not null AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`   bigint       DEFAULT null COMMENT '项目ID',
  `requirement_name` varchar(255) DEFAULT null COMMENT '项目名字',
  `series_id`        bigint       DEFAULT null COMMENT '模块类（后台二级类目',
  `series_name`      varchar(255) DEFAULT null COMMENT '模块类名字',

  `product_line`     varchar(64)  DEFAULT null COMMENT '所属PL',
  `module_num`       varchar(64)  DEFAULT null COMMENT '模块号',
  `module_name`      varchar(64)  DEFAULT null COMMENT '模块描述',
  `module_id`        bigint       DEFAULT null COMMENT '模块ID',
  `in_charge`        varchar(64)  DEFAULT NULL COMMENT '责任人',


  `created_at` datetime    DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime    DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX snz_income_good_lists_requirement_id on `snz_income_good_lists`(requirement_id);

-- ------------------------------------------------------
-- end   新品导入明细的行记录表
-- ------------------------------------------------------



-- -----------------------------------------------------
-- Table `snz_supplier_qualifies`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_supplier_qualifies`;

CREATE TABLE IF NOT EXISTS `snz_supplier_qualifies` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `v_code`      varchar(64) DEFAULT NULL COMMENT '商家v码',
  `pi_partner`  varchar(16) DEFAULT NULL COMMENT 'pi合作伙伴',
  `currency`    varchar(16) DEFAULT NULL COMMENT '订单货币',
  `purch_org`   varchar(32) DEFAULT NULL COMMENT '采购组织',
  `bank_code`   varchar(16) DEFAULT NULL COMMENT '银行码',
  `bank_owner`  varchar(64) DEFAULT NULL COMMENT '银行户主',
  `bank_nation` varchar(32) DEFAULT NULL COMMENT '采购国家',
  `bank_num`    varchar(64) DEFAULT NULL COMMENT '银行账号',
  `pay_method`  varchar(16) DEFAULT NULL COMMENT '付款方式',
  `pay_term`    varchar(16) DEFAULT NULL COMMENT '付款条件',
  `order`       varchar(16) DEFAULT NULL COMMENT '排序码',
  `slave`       varchar(16) DEFAULT NULL COMMENT '统奴科目',
  `com_code`    varchar(16) DEFAULT NULL COMMENT '公司代码',
  `stage`       smallint(6) DEFAULT NULL COMMENT '当前进行的步骤',
  `op_code`     varchar(8) DEFAULT NULL COMMENT '操作返回状态码',
  `op_msg`      varchar(255) DEFAULT NULL COMMENT '操作返回信息',
  `country`     varchar(8) DEFAULT NULL COMMENT '国家信息',
  `created_at`  datetime NOT NULL COMMENT '创建时间',
  `updated_at`  datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
CREATE INDEX snz_supplier_qualifies_supplier_id on `snz_supplier_qualifies` (`supplier_id`);


-- -----------------------------------------------------
-- Table `snz_identify_name`  模块认证信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_identify_name`;

CREATE TABLE IF NOT EXISTS `snz_identify_name` (
  `id`              INT           NOT NULL AUTO_INCREMENT COMMENT '自增主键',
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


-- -----------------------------------------------------
-- Table `snz_risk_mortgage_payments`  风险抵押金
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_risk_mortgage_payments`;

CREATE TABLE IF NOT EXISTS `snz_risk_mortgage_payments` (
  `id`                BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_code`     VARCHAR(20)   NOT NULL  COMMENT '供应商代码（海尔提供的代码）',
  `supplier_detail`   VARCHAR(128)  NOT NULL  COMMENT '供应商描述',
  `purchaser_code`    VARCHAR(20)   NOT NULL  COMMENT '采购商代码（海尔提供的代码）',
  `amount`            BIGINT        NOT NULL  COMMENT '风险抵押金金额',
  `created_at`        DATETIME      NULL      COMMENT '创建时间',
  `updated_at`        DATETIME      NULL      COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

-- 供应商MDM核审对接表
-- -----------------------------------------------------
-- Table `snz`.`mdm_supplier_qualifications`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mdm_supplier_qualifications`;

CREATE TABLE `mdm_supplier_qualifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `v_code` varchar(64) DEFAULT NULL COMMENT '商家v码',
  `pi_partner` varchar(16) DEFAULT NULL COMMENT 'pi合作伙伴',
  `currency` varchar(16) DEFAULT NULL COMMENT '订单货币',
  `purch_org` varchar(32) DEFAULT NULL COMMENT '采购组织',
  `bank_code` varchar(16) DEFAULT NULL COMMENT '银行码',
  `bank_owner` varchar(64) DEFAULT NULL COMMENT '银行户主',
  `bank_nation` varchar(32) DEFAULT NULL COMMENT '采购国家',
  `bank_num` varchar(64) DEFAULT NULL COMMENT '银行账号',
  `pay_method` varchar(16) DEFAULT NULL COMMENT '付款方式',
  `pay_term` varchar(16) DEFAULT NULL COMMENT '付款条件',
  `order` varchar(16) DEFAULT NULL COMMENT '排序码',
  `slave` varchar(16) DEFAULT NULL COMMENT '统奴科目',
  `com_code` varchar(16) DEFAULT NULL COMMENT '公司代码',
  `stage` smallint(6) DEFAULT NULL COMMENT '当前进行的步骤',
  `status` smallint(6) DEFAULT NULL COMMENT '核审状态',
  `op_code` varchar(8) DEFAULT NULL COMMENT '操作返回状态码',
  `op_msg` varchar(255) DEFAULT NULL COMMENT '操作返回信息',
  `country` varchar(8) DEFAULT NULL COMMENT '国家信息',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE INDEX mdm_supplier_qualifications_sup_id on `mdm_supplier_qualifications`(supplier_id);



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
  `data_type`           VARCHAR(32) NULL      COMMENT '配额的数据类型',
  `status`              INT         NULL      COMMENT '状态（0:待提交，1:已写入sap）',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_detail_quota_requirement_id ON snz_detail_quotas (requirement_id);
insert into snz_detail_quotas (requirement_id, requirement_name, solution_id, module_id, module_num, module_name, purchase_org, factory_num,
supplier_id, supplierV_code, supplier_name, purchase_type, quantity, scale, original_cost, actual_cost, agency_fee, duty, other_fee, fee_unit,
purchase_unit, coin_type, purchase_day, purchase_team, basic_unit, tax_code, data_type, status, created_at, updated_at )
values (1, '海尔集团钢板大资源需求信息', 1, 1, '00001', 'AYU91', 'ea8', '9878', 1, '00001V', '采购商1', 'zh', 10000, 10, 100, 89, 1, 0, 0, 12, 'zh', 'CNY', 1, 'e89', 'EA', 'tax', '050910', 1, now(), now()),
(1, '海尔集团钢板大资源需求信息', 1, 2, '00002', 'AZ890', 'ea8', '9878', 1, '00001V', '采购商1', 'zh', 10000, 10, 100, 89, 1, 0, 0, 12, 'zh', 'CNY',  1, 'e89', 'EA', 'tax', '050910', 1, now(), now()),
(1, '海尔集团钢板大资源需求信息', 1, 3, '00003', 'AZ780', 'ea8', '9878', 1, '00001V', '采购商1', 'zh', 10000, 10, 100, 89, 1, 0, 0, 12, 'zh', 'CNY',  1, 'e89', 'EA', 'tax', '050910', 1, now(), now());



-- -----------------------------------------------------
-- Table `snz_requirement_send`    需求的数据状态（用于标记是否对接的数据已全部传输成功）
-- -----------------------------------------------------
DROP TABLE IF EXISTS `snz_requirement_send`;

CREATE TABLE IF NOT EXISTS `snz_requirement_send` (
  `id`                  BIGINT      NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `requirement_id`      BIGINT      NOT NULL  COMMENT '需求编号',
  `send_PLM`            SMALLINT    NULL      COMMENT '是否已写入模块数据到plm中间表(0:待写入，1:已写入)',
  `reply_moduleNum`     SMALLINT    NULL      COMMENT '从plm中间表回写模块专用号',
  `business_negotiate`  SMALLINT    NULL      COMMENT '商务谈判状态（当状态为1时－》中选的供应商可以更改价格但是不能高于报价）',
  `negotiate_file`      VARCHAR(256)NULL      COMMENT '商务谈判时必须要上传的文档信息',
  `supplier_sign`       SMALLINT    NULL      COMMENT '供应商跟标（当状态为1时－》供应商界面显示是否跟标信息此时供应商才能看到个人的配额信息）',
  `result_publicity`    SMALLINT    NULL      COMMENT '配额结果公示（当状态为1时－》用户在详细需求页面可以看到中标的用户的配额详细信息，供应商各自也能在本身后台查看配额信息）',
  `confirm_quota`       SMALLINT    NULL      COMMENT '是否选定的供应商已全部确认配额信息',
  `send_vCode`          SMALLINT    NULL      COMMENT '向plm中间表写入供应商V码信息',
  `write_detailQuota`   SMALLINT    NULL      COMMENT '是否已写入详细的配额信息',
  `send_SAP`            SMALLINT    NULL      COMMENT '是否已写入详细配额数据到sap',
  `created_at`          DATETIME    NULL      COMMENT '创建时间',
  `updated_at`          DATETIME    NULL      COMMENT '修改时间',
  PRIMARY KEY (`id`));
CREATE INDEX snz_requirement_send_requirement_id ON snz_requirement_send (requirement_id);
insert into snz_requirement_send (requirement_id , send_PLM, reply_moduleNum, business_negotiate, negotiate_file, supplier_sign, result_publicity, confirm_quota, send_vCode, write_detailQuota, send_SAP, created_at, updated_at )
values (1, 0, 0, 1, 'negotiate_file', 1, 1, 0, 0, 0, 0, now(), now()),
(2, 0, 0, 1, 'negotiate_file', 1, 1, 0, 0, 0, 0, now(), now()),
(3, 0, 0, 1, 'negotiate_file', 1, 1, 0, 0, 0, 0, now(), now());



-- ----------------------------------------------------
-- ---- 新品导入
-- ----------------------------------------------------
DROP TABLE IF EXISTS `snz_new_product_imports`;
CREATE TABLE `snz_new_product_imports` (
  `id`                      BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `supplier_code`           VARCHAR(128)  NULL    NULL COMMENT '供应商代码',
  `supplier_name`           VARCHAR(128)  NULL    NULL COMMENT '供应商名称',
  `document_name`           VARCHAR(128)  DEFAULT NULL COMMENT '文件',
  `module_num`              VARCHAR(64)   DEFAULT NULL COMMENT '模块编号(海尔使用)',
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

-- ----------------------------------------------------
-- - Table `snz_module_factory` 模块对应的工厂信息（将工厂资源从模块中抽出来）
-- ----------------------------------------------------
DROP TABLE IF EXISTS `snz_module_factory`;
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
insert into snz_module_factory ( module_id, factory_num, factory_name, property_id, select_num, resource_num, sales_id, sales_name, created_at, updated_at )
values (1, '9899', 'factoryName', 1, 3, 10000, 1, 'salesName', now(), now());




DROP TABLE IF EXISTS `snz_new_product_steps`;
CREATE TABLE `snz_new_product_steps` (
  `id`                  bigint    NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `parent_id`           bigint    NOT NULL COMMENT '父ID',
  `module_num`          varchar(64)   NOT NULL COMMENT '模块号(海尔内部使用物料编号)',
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


INSERT INTO `snz_new_product_steps` (`parent_id`,`module_num`, `supplier_code`, `supplier_name`, `step`, `datetime`, `duration`, `real_datetime`, `status`,`in_charge`, `created_at`, `updated_at`)
VALUES
  (1,'40','S1','SN1',1,'2014-01-01 00:00:00',3,'2013-12-18 00:00:00',1,'小李',NULL,NULL),
  (1,'40','S1','SN1',2,'2014-03-03 00:00:00',4,'2014-03-05 00:00:00',-1,'小张',NULL,NULL),
  (1,'40','S1','SN1',3,'2014-04-04 00:00:00',5,'2014-04-03 00:00:00',1,'小王',NULL,NULL),
  (1,'40','S1','SN1',4,'2014-05-05 00:00:00',6,'2014-05-05 00:00:00',0,'小赵',NULL,NULL),
  (1,'40','S1','SN1',5,'2014-06-06 00:00:00',7,'2014-05-08 00:00:00',-1,'小李',NULL,NULL),
  (1,'40','S1','SN1',6,'2014-07-07 00:00:00',8,'2014-07-06 00:00:00',1,'小李',NULL,NULL),
  (1,'40','S1','SN1',7,'2014-08-08 00:00:00',9,'2014-08-06 00:00:00',1,'小李',NULL,NULL);

DROP TABLE IF EXISTS `snz_supplier_reparations`;
CREATE TABLE `snz_supplier_reparations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `identify` varchar(128) DEFAULT NULL COMMENT '退款唯一标示',
  `module_num` varchar(64) DEFAULT NULL COMMENT '物料号',
  `v_code` varchar(64) DEFAULT NULL COMMENT '供应商编码',
  `quality` int(11) DEFAULT NULL COMMENT '赔偿数量',
  `money` int(11) DEFAULT NULL COMMENT '赔偿金额，单位是分',
  `type` smallint DEFAULT NULL COMMENT '1-入厂，2-现场，3-（T-1），4-市场',
  `updated_at` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `snz_supplier_reparations` (`id`, `module_num`, `v_code`, `quality`, `money`, `type`, `updated_at`, `created_at`)
VALUES
  (100, '0060110867', 'V00001', 3, 1500, 2, '2014-08-01 08:17:21', '2014-08-01 08:17:21'),
  (101, '0060110867', 'V00001', 3, 1500, 2, '2014-08-02 08:17:21', '2014-08-02 08:17:21'),
  (102, '0060110867', 'V00001', 3, 1500, 2, '2014-08-03 08:17:21', '2014-08-03 08:17:21'),
  (103, '0060110867', 'V00001', 3, 1500, 2, '2014-08-04 08:17:21', '2014-08-04 08:17:21'),
  (104, '0060110867', 'V00001', 3, 1500, 2, '2014-08-05 08:17:21', '2014-08-05 08:17:21'),
  (105, '0060110867', 'V00001', 3, 1500, 2, '2014-08-06 08:17:21', '2014-08-06 08:17:21'),
  (106, '0060110867', 'V00001', 3, 1500, 2, '2014-08-07 08:17:21', '2014-08-07 08:17:21'),
  (107, '0060110867', 'V00001', 3, 1500, 2, '2014-08-08 08:17:21', '2014-08-08 08:17:21');

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

CREATE TABLE `snz_supplier_scene_bad_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `w_id` varchar(256) DEFAULT NULL,
  `module_num` varchar(128) DEFAULT NULL,
  `v_code` varchar(128) DEFAULT NULL,
  `w_count` varchar(256) DEFAULT NULL,
  `send_at` datetime DEFAULT NULL,
  `depart` varchar(128) DEFAULT NULL,
  `depart_name` varchar(64) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `load_batch` varchar(32) DEFAULT NULL,
  `money` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `mw_supplier_scene_bad_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `wasterid` varchar(256) DEFAULT NULL,
  `specialtiesno` varchar(128) DEFAULT NULL,
  `supplycode` varchar(128) DEFAULT NULL,
  `wastercount` varchar(256) DEFAULT NULL,
  `sendday` varchar(32) DEFAULT NULL,
  `careerdepart` varchar(128) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `load_batch` varchar(32) DEFAULT NULL,
  `penalty` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `snz_supplier_scene_bad_records` (`id`, `w_id`, `module_num`, `v_code`, `w_count`, `send_at`, `depart`, `depart_name`, `created_at`, `updated_at`, `load_batch`, `money`)
VALUES
  (1, '101408021247330181', '0070816358', 'V12967', '5', '2014-08-02 08:00:00', '9040', '冰箱中二', '2014-08-07 08:17:09', '2014-08-07 08:17:09', '201408070016203', '2500'),
  (2, '101408021259190151', '0060836952', 'V12970', '4', '2014-08-02 08:00:00', '9050', '特种冰箱', '2014-08-07 08:17:09', '2014-08-07 08:17:09', '201408070016203', '2000'),
  (3, '101408021259190152', '0060826686', 'V12970', '4', '2014-08-02 08:00:00', '9050', '特种冰箱', '2014-08-07 08:17:09', '2014-08-07 08:17:09', '201408070016203', '2000'),
  (4, '101408021259190153', '0060161600', 'V12970', '1', '2014-08-02 08:00:00', '9050', '特种冰箱', '2014-08-07 08:17:09', '2014-08-07 08:17:09', '201408070016203', '500'),
  (5, '101408021259190154', '0060836935', 'V12970', '2', '2014-08-02 08:00:00', '9050', '特种冰箱', '2014-08-07 08:17:09', '2014-08-07 08:17:09', '201408070016203', '1000');


  INSERT INTO `mw_supplier_scene_bad_records` (`id`, `wasterid`, `specialtiesno`, `supplycode`, `wastercount`, `sendday`, `careerdepart`, `created_date`, `last_modified_date`, `load_batch`, `penalty`)
VALUES
  (1, '1013051702301703414', '0010811904A', 'V13701', '12', '20130516', '9160', '2014-08-15 01:32:01', '2014-08-15 01:32:01', '201408150016381', '60.00'),
  (3, '101305170230173062', '0030806890B', 'V13183', '14', '20130516', '9270', '2014-08-15 01:32:01', '2014-08-15 01:32:01', '201408150016381', '420.00'),
  (5, '101305171030304913', '0020200899H', 'V13950', '9', '20130517', '9261', '2014-08-15 01:32:01', '2014-08-15 01:32:01', '201408150016381', '45.00'),
  (7, '101305171105210093', '0060830624A', 'V12970', '5', '20130517', '9010', '2014-08-15 01:32:01', '2014-08-15 01:32:01', '201408150016381', '25.00'),
  (9, '101305170230180083', '0030204981F', 'V98507', '9', '20130516', '9220', '2014-08-15 01:32:01', '2014-08-15 01:32:01', '201408150016381', '45.00'),
  (11, '101305170230170464', '0030805973', 'V12802', '1', '20130516', '9260', '2014-08-15 01:32:01', '2014-08-15 01:32:01', '201408150016381', '30.00'),
  (13, '101401031730294903', '0060115327', 'V98492', '49', '20140103', '9800', '2014-08-15 01:32:01', '2014-08-15 01:32:01', '201408150016381', '245.00');

CREATE TABLE `snz_supplier_market_bad_records` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `oid` varchar(64) DEFAULT NULL COMMENT '工单号',
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
  `price` int(11) DEFAULT NULL COMMENT '备件成本',
  `fee` int(11) DEFAULT NULL COMMENT '售后费用',
  `module_name` varchar(512) DEFAULT NULL COMMENT '备件名称',
  `status` varchar(64) DEFAULT NULL COMMENT '备件使用状态',
  PRIMARY KEY (`id`)
);

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


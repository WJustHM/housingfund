-- 新增打印委托扣划协议 权限
insert into `c_action` ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '603', '2018-03-02 13:21:33', b'0', null, '2018-03-02 13:21:36', '38105b480dc34e5bdf1c5ec8a464e1b4', 'GET', '/loan/loanContract/entrustDeduct/{DKZH}');
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUE ('00000', '603');

-- 新增异地贷款职工住房公积金缴存证明sql
INSERT INTO `c_action` (`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) VALUES ('602', '2018-02-06 09:50:02', '\0', NULL, '2018-02-06 09:50:09', 'eb2a1f741d28cc69f734c5a3ef77b40c', 'GET', '/collection/diffTerritoryLoadProvePdf/{grzh}');
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUES ('00000', '602');

---添加索引
ALTER TABLE st_housing_business_details ADD INDEX index_dkzh ( `dkzh` );
ALTER TABLE st_housing_business_details ADD INDEX index_dqqc ( `dqqc` );


-- 新增未分摊报表 权限（已运行）
INSERT INTO `c_action` ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url` ) VALUES ( '601', NOW(), FALSE, NULL, NOW(), '6699146dc733e02bf76a42b6bd4a7dff', 'GET', '/finance/report/CityZSWFT' );
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUE ('00000', '601');


-- 更新暂收汇缴为未分摊汇补缴（已运行）
UPDATE c_finance_record_unit SET ZJLY='未分摊汇补缴' WHERE ZJLY='暂收汇缴'

--  新增未分摊余额 （已运行）
ALTER TABLE c_finance_record_unit add WFTYE NUMERIC(18,2) DEFAULT 0.00 COMMENT '未分摊余额';

-- 新增*** （已经运行）
INSERT INTO `c_action` (`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) VALUES ('598', '2018-01-29 14:40:16', '\0', NULL, '2018-01-30 15:50:45', '5a7a1fd2771084cc669a996f8258512d', 'GET', '/loan/account/housingfundPlanPdf/{DKZH}');
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUES ('00000', '598');

INSERT INTO `c_action` (`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) VALUES ('599', '2018-01-30 15:50:38', '\0', NULL, '2018-01-30 15:50:45', 'fd285ac30694178d813c2555eb0d94b2', 'GET', '/other/paymentHistoryToExcel/{DKZH}');
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUES ('00000', '599');
INSERT INTO `c_permission` (`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `module_id`, `permission_code`, `permission_name`, `permission_note`) VALUES ('30311', '2018-01-30 15:56:55', '\0', NULL, NULL, '303', '30311', '导出还款记录', '1');

-- 定时任务日志表（已运行）
DROP TABLE IF EXISTS `c_time_task`;
CREATE TABLE `c_time_task` (
  `id` varchar(32) COLLATE utf8_bin NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `module` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '模块',
  `sub_module` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '子模块',
  `ZXSJ` datetime NOT NULL COMMENT '执行时间',
  `SFCG` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否成功 ''1'':成功 ''0'':失败',
  `SBYY` text COLLATE utf8_bin COMMENT '失败原因',
  PRIMARY KEY (`id`,`created_at`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='定时任务日志表';

--  楼盘添加预售许可证字段（已经运行）
ALTER TABLE c_loan_building_information_vice add YSXKZ varchar(120) DEFAULT NULL COMMENT '预售许可证';
ALTER TABLE c_loan_building_information_basic add YSXKZ varchar(120) DEFAULT NULL COMMENT '预售许可证';

-- 更新楼盘楼栋预售许可证 （已经运行）
update c_loan_building_information_vice as c INNER JOIN c_loan_eatate_project_vice as s on c.loanEatateProjectVice = s.id set c.YSXKZ = s.YSXKZH;
update c_loan_building_information_basic as c INNER JOIN c_loan_eatate_project_basic as s on c.cLoanEatateProjectBasic = s.id set c.YSXKZ = s.YSXKZH;


-- 提取列表受托银行查询按照编码查询（已运行）
UPDATE st_collection_personal_business_details d
  INNER JOIN c_collection_personal_business_details_extension de ON de.id = d.extension
SET de.JBRXM = '102'
WHERE
  d.GJHTQYWLX IN ('11', '12')
  AND d.deleted = FALSE
  AND de.JBRXM = '中国工商银行股份有限公司毕节奢香支行';

UPDATE st_collection_personal_business_details d
  INNER JOIN c_collection_personal_business_details_extension de ON de.id = d.extension
SET de.JBRXM = '301'
WHERE
  d.GJHTQYWLX IN ('11', '12')
  AND d.deleted = FALSE
  AND de.JBRXM = '交通银行毕节分行';

UPDATE st_collection_personal_business_details d
  INNER JOIN c_collection_personal_business_details_extension de ON de.id = d.extension
SET de.JBRXM = '103'
WHERE
  d.GJHTQYWLX IN ('11', '12')
  AND d.deleted = FALSE
  AND de.JBRXM = '中国农业银行股份有限公司毕节分行';

UPDATE st_collection_personal_business_details d
  INNER JOIN c_collection_personal_business_details_extension de ON de.id = d.extension
SET de.JBRXM = '104'
WHERE
  d.GJHTQYWLX IN ('11', '12')
  AND d.deleted = FALSE
  AND de.JBRXM = '中国银行股份有限公司毕节市天河支行';

UPDATE st_collection_personal_business_details d
  INNER JOIN c_collection_personal_business_details_extension de ON de.id = d.extension
SET de.JBRXM = '105'
WHERE
  d.GJHTQYWLX IN ('11', '12')
  AND d.deleted = FALSE
  AND de.JBRXM = '中国建设银行股份有限公司毕节中山支行';


-- 还款序号 已执行
ALTER TABLE c_housing_business_details_extension ADD HKXH VARCHAR(20) DEFAULT NULL COMMENT '还款序号';

-- v1.2.18
-- 新增提取总额查询相关
ALTER TABLE c_collection_withdrawl_business_extension ADD ZongE NUMERIC(18,2) DEFAULT NULL COMMENT '总额';
ALTER TABLE c_collection_withdrawl_business_extension ADD INDEX index_ZongE (ZongE);
ALTER TABLE c_collection_withdrawl_business_extension ADD INDEX index_details (details);

# --------------------------------------注意：以下SQL语句都已经执行过！！！---------------------------------------------
-- v1.2.16
insert into `c_action` ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '596', '2018-01-22 10:10:31', b'0', null, '2018-01-24 10:21:18', 'da86a88827a67e5e9bcb4224e69810eb', 'GET', '/collection/unitAccts/employee');
insert into `c_action` ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '595', '2018-01-22 10:10:31', b'0', null, '2018-01-24 10:21:18', '7bc4f4c655a2d7e48e36e367d98ddb4c', 'GET', '/other/employeeListToExcel/{DWZH}');
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUES ('00000', '595');
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUES ('00000', '596');
insert into `c_permission` ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `module_id`, `permission_code`, `permission_name`, `permission_note`) values ( '10109', '2018-01-23 12:55:51', b'0', null, null, '101', '10109', '导出职工列表', '1');

-- 新增还款账户户名
ALTER TABLE c_loan_housing_person_information_vice add HKZHHM varchar(120) DEFAULT NULL COMMENT '还款账号户名';# --（杨坤已补全历史数据）
ALTER TABLE c_loan_housing_personal_loan_extension add HKZHHM varchar(120) DEFAULT NULL COMMENT '还款账号户名';# --（杨坤已补全历史数据）

-- v1.2.15
-- 汇缴查询索引
alter table c_collection_unit_deposit_inventory_detail_vice add index index_grzh(grzh);
alter table c_collection_unit_remittance_vice add index index_qcqrdh(qcqrdh);
alter table c_collection_unit_deposit_inventory_vice add index index_qcqrdh(qcqrdh);


-- 暂收溯源
ALTER TABLE c_finance_temporary_record ADD YJZPZH VARCHAR(20) DEFAULT NULL COMMENT '源记账凭证号';

# --新增批量凭证
INSERT INTO `c_action` (`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) VALUES ('514', '2017-11-16 13:36:12', '\0', NULL, '2018-01-16 15:51:40', '2322d06e384eea3fb672a309014dfefa', 'POST', '/finance/voucher/batchVocherPdf');
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUES ('00000', '514');

INSERT INTO `c_action` (`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) VALUES ('594', '2018-01-17 14:42:20', '\0', NULL, '2018-01-17 15:17:47', 'bd3ce0429a90875ab203a5677a0f339c', 'GET', '/other/InventoryToExcel/{DWZH}/{QCNY}');
INSERT INTO `c_permission` (`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `module_id`, `permission_code`, `permission_name`, `permission_note`) VALUES ('10409', '2018-01-17 15:31:11', '\0', NULL, NULL, '104', '10409', '导出清册确认单', '1');
INSERT INTO `c_permission_action` (`permission_id`, `action_id`) VALUES ('00000', '594');

-- v1.2.9

-- 新增接口权限
insert into c_action ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '593', '2018-01-09 19:26:58', b'0', null, '2018-01-09 19:27:05', 'a0710ae1a11b2c0dfce6427c6600d905', 'GET', '/loan/account/SquareReceipt/{DKZH}');
insert into c_permission_action ( `permission_id`, `action_id`) values ( '00000', '593');

-- end



-- v1.2.8

-- 新增接口权限
insert into `c_permission` ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `module_id`, `permission_code`, `permission_name`, `permission_note`) values ( '40509', '2018-01-09 15:39:52', b'0', null, null, '405', '40509', '缴存提取分类情况表', null);
insert into `c_action` ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '592', '2018-01-09 12:39:03', b'0', null, '2018-01-09 12:39:07', 'a9d16c892454dc34325c1653c0939112', 'GET', '/finance/report/depositWithdrawlClassify');
insert into `c_permission_action` ( `permission_id`, `action_id`) values ( '00000', '592');
ALTER TABLE c_housing_business_details_extension ADD YWWD VARCHAR(32) DEFAULT NULL COMMENT '业务网点';
ALTER TABLE st_housing_business_details ADD INDEX index_jzrq (JZRQ);

UPDATE st_housing_business_details details
INNER JOIN c_housing_business_details_extension pe ON details.extenstion = pe.id
JOIN c_loan_housing_person_information_basic basic ON details.DKZH = basic.DKZH
SET pe.YWWD = basic.YWWD;

-- 业务明细表 jzrq增加索引
ALTER TABLE st_collection_personal_business_details ADD INDEX index_jzrq(JZRQ);
ALTER TABLE st_collection_unit_business_details ADD INDEX index_jzrq(JZRQ);

-- 导入年初数
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0001', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '101', 1, '住房公积金存款', '02', 528839210.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0002', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '102', 1, '增值收益存款', '02', 73618923.89, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0003', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '111', 1, '应收利息', '02', 1215737.38, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0004', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '119', 1, '其它应收款', '02', 283080730.17, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0005', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '121', 1, '委托贷款', '02', 6107118870.44, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0006', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '122', 1, '逾期贷款', '02', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0007', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '124', 1, '国家债券', '02', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0008', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '201', 1, '住房公积金', '03', 6511215408.86, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0009', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '211', 1, '应付利息', '03', 50771558.32, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0010', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '214', 1, '专项应付款', '03', 835370.74, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0011', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '21402', 2, '廉租房补充资金', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0012', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '219', 1, '其它应付款', '03', 293091272.51, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0013', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '301', 1, '贷款风险准备', '03', 52357710.93, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0014', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '32104', 2, '待分配增值收益', '03', 85602150.52, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0015', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '311', 1, '增值收益', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');

INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0024', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '401', 1, '业务收入', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0025', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '411', 1, '业务支出', '02', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0026', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '32101', 1, '提取贷款风险准备', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0027', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '32102', 1, '提取中心管理费用', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0028', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '32103', 1, '提取廉租房补充资金', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');

INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0016', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '40101', 1, '住房公积金利息收入', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0017', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '40102', 1, '增值收益利息收入', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0018', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '40103', 1, '委托贷款利息收入', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0019', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '40104', 1, '国家债券利息收入', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0020', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '40105', 1, '其他收入', '03', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0021', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '41101', 1, '住房公积金利息支出', '02', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0022', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '41102', 1, '归集手续费支出', '02', 0.00, '2c92941f5f1ef711015f1efe40830000');
INSERT INTO `c_finance_subjects_balance`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `BYJS`, `BYYE`, `BYZJ`, `KMBH`, `KMJB`, `KMMC`, `KMYEFX`, `SYYE`, `cFinanceAccountPeriod`) VALUES ('0023', '2018-01-07 15:17:17', b'0', NULL, '2018-01-07 15:17:20', 0.00, 0.00, 0.00, '41103', 1, '委托贷款手续费支出', '02', 0.00, '2c92941f5f1ef711015f1efe40830000');


-- end

-- v1.2.1
-- 新增接口权限
insert into c_action ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '590', '2017-12-28 11:31:12', b'0', null, '2017-12-28 11:31:15', '5b45a670618d8739405904625c2fd570', 'GET', '/withdrawls/tasks/{taskId}');
insert into c_action ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '589', '2017-12-28 11:31:12', b'0', null, '2017-12-28 11:31:15', 'db8d8d0c2fb5e2b26f3bbc995974341a', 'GET', '/finance/notice/new/notice');
insert into c_action ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '588', '2017-12-28 11:31:12', b'0', null, '2017-12-28 11:31:15', 'e553cf8651d745aa4a9ee6a73011a208', 'GET', '/finance/report/settlementdaybook/new/notice');
insert into c_action ( `id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `action_code`, `action_method`, `action_url`) values ( '591', '2018-01-03 10:10:13', b'0', null, '2018-01-03 10:10:16', '809e27b89633d793a58423fd3042f4b3', 'GET', '/withdrawls/printRecords');

insert into c_permission_action ( `permission_id`, `action_id`) values ( '00000', '588');
insert into c_permission_action ( `permission_id`, `action_id`) values ( '00000', '589');
insert into c_permission_action ( `permission_id`, `action_id`) values ( '00000', '590');
insert into c_permission_action ( `permission_id`, `action_id`) values ( '00000', '591');

-- end
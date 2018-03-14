/*
Navicat MySQL Data Transfer

Source Server         : 172.18.20.156_3306
Source Server Version : 50718
Source Host           : 172.18.20.156:3306
Source Database       : housingfund

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-06-26 14:23:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for st_collection_personal_account
-- ----------------------------
DROP TABLE IF EXISTS `st_collection_personal_account`;
CREATE TABLE `st_collection_personal_account` (
  `GRZH` char(20) DEFAULT NULL COMMENT '个人账号',
  `GRJCJS` decimal(18,2) DEFAULT NULL COMMENT '个人缴存基数',
  `GRZHZT` char(2) DEFAULT NULL COMMENT '个人账号状态 附A.6',
  `KHRQ` date DEFAULT NULL COMMENT '开户日期 YYYYMMDD',
  `GRZHYE` decimal(18,2) DEFAULT NULL COMMENT '个人账户余额',
  `GRZHSNJZYE` decimal(18,2) DEFAULT NULL COMMENT '个人账户上年结转余额',
  `GRZHDNGJYE` decimal(18,2) DEFAULT NULL COMMENT '个人账户当年归集余额',
  `GRYJCE` decimal(18,2) DEFAULT NULL COMMENT '个人月缴存额',
  `DWYJCE` decimal(18,2) DEFAULT NULL COMMENT '单位月缴存额',
  `XHRQ` date DEFAULT NULL COMMENT '销户日期',
  `FCHXHYY` char(2) DEFAULT NULL COMMENT '销户原因 附A.7',
  `GRCKZHHM` varchar(30) DEFAULT NULL,
  `GRCKZHKHYHMC` varchar(255) DEFAULT NULL COMMENT '个人存款账户开户银行名称',
  `GRCKZHKHYHDM` char(3) DEFAULT NULL COMMENT '个人存款账户开户银行代码。 联行号前三位',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人账户信息 表5.0.4';

-- ----------------------------
-- Table structure for st_collection_personal_business_details
-- ----------------------------
DROP TABLE IF EXISTS `st_collection_personal_business_details`;
CREATE TABLE `st_collection_personal_business_details` (
  `GRZH` char(20) DEFAULT NULL COMMENT '个人账号',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期 YYYYMMDD',
  `GJHTQYWLX` char(2) DEFAULT NULL COMMENT '归集和提取余额类型 附A.5',
  `FSE` decimal(18,2) DEFAULT NULL COMMENT '发生额',
  `DNGJFSE` decimal(18,2) DEFAULT NULL COMMENT '当年归集发生额',
  `SNJZFSE` decimal(18,2) DEFAULT NULL COMMENT '上年结转发生额',
  `FSLXE` decimal(18,2) DEFAULT NULL COMMENT '发生利息额',
  `TQYY` char(2) DEFAULT NULL COMMENT '提取原因 附A.7',
  `TQFS` char(2) DEFAULT NULL COMMENT '提取方式 附A.8',
  `DWZH` char(20) DEFAULT NULL,
  `CZBZ` char(2) DEFAULT NULL COMMENT '冲账标识 附A.9',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人业务明细信息 表5.0.5';

-- ----------------------------
-- Table structure for st_collection_unit_account
-- ----------------------------
DROP TABLE IF EXISTS `st_collection_unit_account`;
CREATE TABLE `st_collection_unit_account` (
  `DWZH` char(20) DEFAULT NULL COMMENT '单位账号',
  `DWJCBL` decimal(4,2) DEFAULT NULL COMMENT '单位缴存比例',
  `GRJCBL` decimal(4,2) DEFAULT NULL COMMENT '个人缴存比例',
  `DWJCRS` decimal(18,0) DEFAULT NULL COMMENT '单位缴存人数',
  `DWFCRS` decimal(18,0) DEFAULT NULL COMMENT '单位封存人数',
  `DWZHYE` decimal(18,2) DEFAULT NULL COMMENT '单位账号余额',
  `DWXHRQ` date DEFAULT NULL COMMENT '单位销户日期 YYYYMMDD',
  `DWXHYY` char(2) DEFAULT NULL COMMENT '单位销户原因 附A.3',
  `DWZHZT` char(2) DEFAULT NULL COMMENT '单位账户状态 附A.4',
  `JZNY` date DEFAULT NULL COMMENT '缴至年月 YYYY-MM',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单位账户信息 表5.0.2';

-- ----------------------------
-- Table structure for st_collection_unit_business_details
-- ----------------------------
DROP TABLE IF EXISTS `st_collection_unit_business_details`;
CREATE TABLE `st_collection_unit_business_details` (
  `DWZH` char(20) DEFAULT NULL COMMENT '单位账号',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期 YYYYMMDD',
  `FSE` decimal(18,2) DEFAULT NULL,
  `FSLXE` decimal(18,2) DEFAULT NULL,
  `FSRS` decimal(18,0) DEFAULT NULL,
  `YWMXLX` char(2) DEFAULT NULL COMMENT '业务明细类型 附A.5',
  `HBJNY` date DEFAULT NULL COMMENT '汇补缴年月 YYYY-MM',
  `DWZH` char(20) DEFAULT NULL COMMENT '业务流水号',
  `CZBZ` char(2) DEFAULT NULL COMMENT '冲账标识 附A.9',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单位业务明细信息 表5.0.3';

-- ----------------------------
-- Table structure for st_common_person
-- ----------------------------
DROP TABLE IF EXISTS `st_common_person`;
CREATE TABLE `st_common_person` (
  `GRZH` char(20) DEFAULT NULL COMMENT '个人账号',
  `XingMing` varchar(120) DEFAULT NULL COMMENT '姓名',
  `XMQP` varchar(255) DEFAULT NULL COMMENT '姓名全拼',
  `XingBie` char(1) DEFAULT NULL COMMENT '性别 GB/T 2261.1',
  `GDDHHM` varchar(20) DEFAULT NULL COMMENT '固定号码',
  `SJHM` char(11) DEFAULT NULL COMMENT '手机号码',
  `ZJLX` char(2) DEFAULT NULL COMMENT '证件类型 附A.1',
  `ZJHM` char(18) DEFAULT NULL COMMENT '证件号码 GB11643',
  `CSNY` date DEFAULT NULL COMMENT '出生年月 YYYY-MM',
  `HYZK` char(2) DEFAULT NULL COMMENT '婚姻状况 GB/T 2261.2',
  `ZhiYe` char(2) DEFAULT NULL COMMENT '职业 GB/T 2261.4',
  `ZhiChen` char(3) DEFAULT NULL COMMENT '职称 GB/T 8561',
  `ZhiWu` char(4) DEFAULT NULL COMMENT '职务 GB/T 12403',
  `XueLi` char(3) DEFAULT NULL COMMENT '学历 GB/T 6864',
  `YZBM` char(6) DEFAULT NULL COMMENT '邮政编码 YD/T 603',
  `JTZZ` varchar(255) DEFAULT NULL COMMENT '家庭住址',
  `JTYSR` decimal(18,2) DEFAULT NULL COMMENT '家庭月收入',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人信息 表4.0.3';

-- ----------------------------
-- Table structure for st_common_policy
-- ----------------------------
DROP TABLE IF EXISTS `st_common_policy`;
CREATE TABLE `st_common_policy` (
  `DWJCBLSX` decimal(4,2) DEFAULT NULL COMMENT '单位缴存比例上限',
  `DWJCBLXX` decimal(4,2) DEFAULT NULL COMMENT '单位缴存比例下限',
  `GRJCBLSX` decimal(4,2) DEFAULT NULL COMMENT '个人缴存比例上限',
  `GRJCBLXX` decimal(4,2) DEFAULT NULL COMMENT '个人缴存比例下限',
  `YJCESX` decimal(18,2) DEFAULT NULL COMMENT '月缴存上限',
  `YJCEXX` decimal(18,2) DEFAULT NULL COMMENT '月缴存额下限',
  `GRZFDKZCNX` tinyint(2) DEFAULT NULL COMMENT '个人住房贷款最长年限',
  `GRZFDKZGED` decimal(18,2) DEFAULT NULL COMMENT '个人住房贷款最高额度',
  `GRZFDKZGDKBL` decimal(3,2) DEFAULT NULL COMMENT '个人住房贷款最高贷款比例',
  `LLLX` char(2) DEFAULT NULL COMMENT '利率类型 附A.2',
  `ZXLL` decimal(8,7) DEFAULT NULL COMMENT '执行利率',
  `XMDKZCNX` tinyint(2) DEFAULT NULL COMMENT '项目贷款最长年限',
  `XMDKZGDKBL` decimal(4,2) DEFAULT NULL COMMENT '项目贷款最高贷款比例',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='政策信息 表4.0.4';

-- ----------------------------
-- Table structure for st_common_unit
-- ----------------------------
DROP TABLE IF EXISTS `st_common_unit`;
CREATE TABLE `st_common_unit` (
  `DWMC` varchar(255) DEFAULT NULL COMMENT '单位名称',
  `DWZH` char(20) DEFAULT NULL COMMENT '单位账号',
  `DWDZ` varchar(255) DEFAULT NULL COMMENT '单位地址',
  `DWFRDBXM` varchar(120) DEFAULT NULL COMMENT '单位法人代表姓名',
  `DWFRDBZJLX` char(2) DEFAULT '1' COMMENT '单位法人代表证件类型 01：身份证 02：军官证 03：护照 04：外国人永久居留证 99：其他',
  `DWFRDBZJHM` char(18) DEFAULT NULL COMMENT '单位法人代表证件号码',
  `DWLSGX` char(2) DEFAULT NULL COMMENT '单位隶属关系 参考GBT12404-1997',
  `DWJJLX` char(2) DEFAULT NULL COMMENT '单位经济类型 参考GB/T 12402-2000',
  `DWSSHY` char(4) DEFAULT NULL COMMENT '单位所属行业 GB/T 4754',
  `DWYB` char(6) DEFAULT NULL COMMENT '单位邮编 参照：YD/T 603',
  `DWDZXX` varchar(120) DEFAULT NULL COMMENT '单位电子信箱',
  `DWFXR` date DEFAULT NULL COMMENT '单位发薪日 GB/T 7408',
  `JBRXM` varchar(120) DEFAULT NULL COMMENT '经办人姓名',
  `JBRGDDHHM` varchar(20) DEFAULT NULL COMMENT '经办人固定电话号码',
  `JBRSJHM` char(11) DEFAULT NULL COMMENT '经办人手机号码',
  `JBRZJLX` char(2) DEFAULT NULL COMMENT '经办人证件类型 01：身份证 02：军官证 03：护照 04：外国人永久居留证 99：其他',
  `JBRZJHM` char(18) DEFAULT NULL COMMENT '经办人证件号码 GB11643',
  `ZZJGDM` char(20) DEFAULT NULL COMMENT '组织机构代码 GB11714',
  `DWSLRQ` date DEFAULT NULL COMMENT '单位设立日期 YYYYMMDD GB/T 7408',
  `DWKHRQ` date DEFAULT NULL COMMENT '单位开户日期',
  `STYHMC` varchar(255) DEFAULT NULL COMMENT '受托银行名称',
  `STYHDM` char(3) DEFAULT NULL COMMENT '受托银行代码',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='缴存单位信息 表4.0.2';

-- ----------------------------
-- Table structure for st_finance_bank_deposit_journal
-- ----------------------------
DROP TABLE IF EXISTS `st_finance_bank_deposit_journal`;
CREATE TABLE `st_finance_bank_deposit_journal` (
  `CNLSH` char(20) DEFAULT NULL COMMENT '出纳流水号',
  `YHZHHM` varchar(30) DEFAULT NULL COMMENT '银行专户号码',
  `JFFSE` decimal(18,2) DEFAULT NULL COMMENT '借方发生额',
  `DFFSE` decimal(18,2) DEFAULT NULL COMMENT '贷方发生额',
  `YuE` decimal(18,2) DEFAULT NULL COMMENT '余额',
  `YEJDFX` char(2) DEFAULT NULL COMMENT '余额借贷方向 附A.22',
  `YHJSLSH` varchar(40) DEFAULT NULL COMMENT '银行结算流水号',
  `ZhaiYao` varchar(255) DEFAULT NULL COMMENT '摘要',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期 YYYYMMDD',
  `RZRQ` date DEFAULT NULL COMMENT '入账日期 YYYYMMDD',
  `RZZT` char(2) DEFAULT NULL COMMENT '入账状态 附A.28',
  `PZSSNY` date DEFAULT NULL COMMENT '凭证所属年月 YYYY-MM',
  `CZBS` char(2) DEFAULT NULL COMMENT '冲账标识 附A.9',
  `ZJYWLX` char(2) DEFAULT NULL COMMENT '资金业务类型',
  `JZPZH` char(20) DEFAULT NULL COMMENT '记账凭证号',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行存款日记账信息 表8.0.6';

-- ----------------------------
-- Table structure for st_finance_general_ledger
-- ----------------------------
DROP TABLE IF EXISTS `st_finance_general_ledger`;
CREATE TABLE `st_finance_general_ledger` (
  `KMBH` varchar(19) DEFAULT NULL COMMENT '科目编号 附A.25',
  `KMMC` varchar(20) DEFAULT NULL COMMENT '科目名称',
  `QCYE` decimal(18,2) DEFAULT NULL COMMENT '期初余额',
  `QCYEFX` char(2) DEFAULT NULL COMMENT '期初余额方向 附A.27',
  `JFFSE` decimal(18,2) DEFAULT NULL COMMENT '借方发生额',
  `DFFSE` decimal(18,2) DEFAULT NULL COMMENT '贷方发生额',
  `QMYE` decimal(18,2) DEFAULT NULL COMMENT '期末余额',
  `QMYEFX` char(2) DEFAULT NULL COMMENT '期末余额方向 附A.27',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期 YYYYMMDD',
  `ZhaiYao` varchar(255) DEFAULT NULL COMMENT '摘要',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='总账信息 表8.0.3';

-- ----------------------------
-- Table structure for st_finance_national_debt
-- ----------------------------
DROP TABLE IF EXISTS `st_finance_national_debt`;
CREATE TABLE `st_finance_national_debt` (
  `GZBH` char(20) DEFAULT NULL,
  `GZZL` char(2) DEFAULT NULL,
  `GMQD` char(2) DEFAULT NULL,
  `GZPZH` char(20) DEFAULT NULL,
  `GZMC` varchar(255) DEFAULT NULL,
  `STYHDM` char(3) DEFAULT NULL,
  `YHZHHM` varchar(30) DEFAULT NULL,
  `LiLv` decimal(8,7) DEFAULT NULL,
  `GMJE` decimal(18,2) DEFAULT NULL,
  `MianZhi` decimal(18,2) DEFAULT NULL,
  `ShuLiang` int(8) DEFAULT NULL,
  `GMRQ` date DEFAULT NULL,
  `QXRQ` date DEFAULT NULL,
  `DQRQ` date DEFAULT NULL,
  `QiXian` decimal(2,0) DEFAULT NULL,
  `DFBJ` decimal(18,2) DEFAULT NULL,
  `DFRQ` date DEFAULT NULL,
  `LXSR` decimal(18,2) DEFAULT NULL,
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国债明细信息 表8.0.8';

-- ----------------------------
-- Table structure for st_finance_recording_voucher
-- ----------------------------
DROP TABLE IF EXISTS `st_finance_recording_voucher`;
CREATE TABLE `st_finance_recording_voucher` (
  `JZPZH` char(20) DEFAULT NULL COMMENT '记账凭证号',
  `ZhaiYao` varchar(255) DEFAULT NULL COMMENT '摘要',
  `KMBH` char(19) DEFAULT NULL COMMENT '科目编号 附A.25',
  `JFFSE` decimal(18,2) DEFAULT NULL COMMENT '借方发生额',
  `DFFSE` decimal(18,2) DEFAULT NULL COMMENT '贷方发生额',
  `FJDJS` mediumint(5) DEFAULT NULL COMMENT '附件单据数',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期 YYYYMMDD',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记账凭证信息 表8.0.5';

-- ----------------------------
-- Table structure for st_finance_subjects
-- ----------------------------
DROP TABLE IF EXISTS `st_finance_subjects`;
CREATE TABLE `st_finance_subjects` (
  `KMBH` varchar(19) DEFAULT NULL COMMENT '科目编号 附A.25',
  `KMMC` varchar(20) DEFAULT NULL COMMENT '科目名称',
  `KMJB` tinyint(1) DEFAULT NULL COMMENT '科目级别',
  `KMSX` char(2) DEFAULT NULL COMMENT '科目属性 附A.26',
  `KMYEFX` char(2) DEFAULT NULL COMMENT '科目余额方向 附A.27',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='科目信息 表8.0.2';

-- ----------------------------
-- Table structure for st_finance_subsidiary_accounts
-- ----------------------------
DROP TABLE IF EXISTS `st_finance_subsidiary_accounts`;
CREATE TABLE `st_finance_subsidiary_accounts` (
  `KMBH` varchar(19) DEFAULT NULL COMMENT '科目编号 附A.25',
  `JZPZH` char(20) DEFAULT NULL COMMENT '记账凭证号',
  `ZhaiYao` varchar(255) DEFAULT NULL COMMENT '摘要',
  `QCYE` decimal(18,2) DEFAULT NULL COMMENT '期初余额',
  `QCYEFX` char(2) DEFAULT NULL COMMENT '期初余额方向 附A.27',
  `JFFSE` decimal(18,2) DEFAULT NULL COMMENT '借方发生额',
  `DFFSE` decimal(18,2) DEFAULT NULL COMMENT '贷方发生额',
  `QMYE` decimal(18,2) DEFAULT NULL COMMENT '期末余额',
  `QMYEFX` char(2) DEFAULT NULL COMMENT '期末余额方向 附A.27',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期 YYYYMMDD',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='明细账信息 表 8.0.4';

-- ----------------------------
-- Table structure for st_finance_time_deposit
-- ----------------------------
DROP TABLE IF EXISTS `st_finance_time_deposit`;
CREATE TABLE `st_finance_time_deposit` (
  `DQCKBH` char(30) DEFAULT NULL COMMENT '定期存款编号',
  `ZHHM` varchar(30) DEFAULT NULL COMMENT '专户号码',
  `ZHMC` varchar(120) DEFAULT NULL COMMENT '账户名称',
  `KHYHMC` varchar(255) DEFAULT NULL COMMENT '开户银行名称',
  `LiLv` decimal(8,7) DEFAULT NULL COMMENT '利率',
  `BJJE` decimal(18,2) DEFAULT NULL COMMENT '本金金额',
  `CRRQ` date DEFAULT NULL COMMENT '存入日期 YYYYMMDD',
  `DQRQ` date DEFAULT NULL COMMENT '到期日期 YYYYMMDD',
  `CKQX` mediumint(5) DEFAULT NULL COMMENT '存款期限',
  `ZQQK` char(2) DEFAULT NULL COMMENT '支取情况 附A.33',
  `QKRQ` date DEFAULT NULL COMMENT '取款日期 YYYYMMDD',
  `LXSR` decimal(18,2) DEFAULT NULL COMMENT '利息收入',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定期存款明细信息 表 8.0.7';

-- ----------------------------
-- Table structure for st_housing_business_details
-- ----------------------------
DROP TABLE IF EXISTS `st_housing_business_details`;
CREATE TABLE `st_housing_business_details` (
  `DKZH` char(20) DEFAULT NULL COMMENT '贷款账号',
  `DWZH` char(20) DEFAULT NULL COMMENT '业务流水号',
  `DKYWMXLX` char(2) DEFAULT NULL COMMENT '贷款业务明细类型 附A.15',
  `YWFSRQ` date DEFAULT NULL COMMENT '业务发生日期 YYYYMMDD',
  `DKYHDM` char(3) DEFAULT NULL COMMENT '贷款银行代码',
  `FSE` decimal(18,2) DEFAULT NULL COMMENT '发生额',
  `BJJE` decimal(18,2) DEFAULT NULL COMMENT '本金金额',
  `LXJE` decimal(18,2) DEFAULT NULL COMMENT '利息金额',
  `FXJE` decimal(18,2) DEFAULT NULL COMMENT '罚息金额',
  `DQQC` smallint(4) DEFAULT NULL COMMENT '当期期次',
  `ZCZYQBJJE` decimal(18,2) DEFAULT NULL COMMENT '正常转预期本金金额',
  `YQZZCBJJE` decimal(18,2) DEFAULT NULL COMMENT '逾期转正常本金金额',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期 YYYYMMDD',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人住房贷款业务明细信息 表6.0.6';

-- ----------------------------
-- Table structure for st_housing_coborrower
-- ----------------------------
DROP TABLE IF EXISTS `st_housing_coborrower`;
CREATE TABLE `st_housing_coborrower` (
  `GTJKRGJJZH` char(20) DEFAULT NULL COMMENT '共合借款人公积金账号',
  `JKHTBH` varchar(30) DEFAULT NULL COMMENT '借款合同编号',
  `GTJKRXM` varchar(120) DEFAULT NULL COMMENT '共同借款人姓名',
  `GTJKRZJLX` char(2) DEFAULT NULL COMMENT '共同借款人证件类型',
  `GTJKRZJHM` char(18) DEFAULT NULL COMMENT '共同借款人证件号码',
  `YSR` decimal(18,2) DEFAULT NULL COMMENT '月收入',
  `CDGX` char(2) DEFAULT NULL COMMENT '参贷关系',
  `GDDHHM` varchar(20) DEFAULT NULL COMMENT '固定电话号码',
  `SJHM` char(11) DEFAULT NULL COMMENT '手机号码',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='共同借款人信息 表6.0.4';

-- ----------------------------
-- Table structure for st_housing_guarantee_contract
-- ----------------------------
DROP TABLE IF EXISTS `st_housing_guarantee_contract`;
CREATE TABLE `st_housing_guarantee_contract` (
  `DBHTBH` char(30) DEFAULT NULL COMMENT '担保合同编号',
  `JKHTBH` char(30) DEFAULT NULL COMMENT '借款合同编号',
  `DKDBLX` char(2) DEFAULT NULL COMMENT '贷款担保类型',
  `DBJGMC` varchar(255) DEFAULT NULL COMMENT '担保机构名称',
  `DYWQZH` varchar(30) DEFAULT NULL COMMENT '抵押物权证号',
  `DYWTXQZH` varchar(30) DEFAULT NULL COMMENT '抵押物他项权证号',
  `DYWFWZL` varchar(255) DEFAULT NULL COMMENT '抵押物房屋坐落',
  `DYQJLRQ` date DEFAULT NULL COMMENT '抵押权简历日期',
  `DYQJCRQ` date DEFAULT NULL COMMENT '抵押物解除日期',
  `DYWPGJZ` decimal(18,2) DEFAULT NULL COMMENT '抵押物评估价值',
  `BZHTBH` varchar(30) DEFAULT NULL COMMENT '保证合同编号',
  `BZJGMC` varchar(255) DEFAULT NULL COMMENT '保证机构名称',
  `DKBZJ` decimal(18,2) DEFAULT NULL COMMENT '贷款保证金',
  `FHBZJRQ` date DEFAULT NULL COMMENT '返还保证金日期 YYYYMMDD',
  `ZYHTBH` varchar(30) DEFAULT NULL COMMENT '质押合同编号',
  `ZYWBH` varchar(30) DEFAULT NULL COMMENT '质押物编号',
  `ZYWMC` varchar(255) DEFAULT NULL COMMENT '质押物名称',
  `ZYWJZ` decimal(18,2) DEFAULT NULL COMMENT '质押物价值',
  `ZYHTKSRQ` date DEFAULT NULL COMMENT '质押合同开始日期 YYYYMMDD',
  `ZYHTJSRQ` date DEFAULT NULL COMMENT '质押合同结束日期 YYYYMMDD',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='担保合同信息 表6.0.3';

-- ----------------------------
-- Table structure for st_housing_overdue_registration
-- ----------------------------
DROP TABLE IF EXISTS `st_housing_overdue_registration`;
CREATE TABLE `st_housing_overdue_registration` (
  `DKZH` char(30) DEFAULT NULL COMMENT '贷款账号',
  `DQQC` smallint(4) DEFAULT NULL COMMENT '逾期期次',
  `YQBJ` decimal(18,2) DEFAULT NULL COMMENT '逾期本金',
  `YQLX` decimal(18,2) DEFAULT NULL COMMENT '逾期利息',
  `YQFX` decimal(18,2) DEFAULT NULL COMMENT '逾期罚息',
  `SSRQ` date DEFAULT NULL COMMENT '实收日期',
  `HKQC` smallint(4) DEFAULT NULL COMMENT '还款期次',
  `SSYQBJJE` decimal(18,2) DEFAULT NULL COMMENT '实收逾期本金金额',
  `SSYQLXJE` decimal(18,2) DEFAULT NULL COMMENT '实收逾期利息金额',
  `SSYQFXJE` decimal(18,2) DEFAULT NULL COMMENT '实收逾期罚息金额',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for st_housing_personal_account
-- ----------------------------
DROP TABLE IF EXISTS `st_housing_personal_account`;
CREATE TABLE `st_housing_personal_account` (
  `DKZH` char(30) DEFAULT NULL COMMENT '贷款账号',
  `JKHTBH` char(30) DEFAULT NULL COMMENT '借款合同编号',
  `DKFXDJ` char(2) DEFAULT NULL COMMENT '贷款风险等级 A.14',
  `DKFFE` decimal(18,2) DEFAULT NULL COMMENT '贷款发放额',
  `DKFFRQ` date DEFAULT NULL COMMENT '贷款发放日期 YYYYMMDD',
  `DKYE` decimal(18,2) DEFAULT NULL COMMENT '贷款余额',
  `DKLL` decimal(8,7) DEFAULT NULL COMMENT '贷款利率',
  `LLFDBL` decimal(4,2) DEFAULT NULL COMMENT '利率浮动比例',
  `DKQS` smallint(4) DEFAULT NULL COMMENT '贷款期数',
  `DQJHHKJE` decimal(18,2) DEFAULT NULL COMMENT '当期计划还款金额',
  `DQJHGHBJ` decimal(18,2) DEFAULT NULL COMMENT '当期计划归还本金',
  `DQJHCHLX` decimal(18,2) DEFAULT NULL COMMENT '当期计划归还利息',
  `DQYHJE` decimal(18,2) DEFAULT NULL COMMENT '当期应还金额',
  `DQYHBJ` decimal(18,2) DEFAULT NULL COMMENT '当前应还本金',
  `DQYHLX` decimal(18,2) DEFAULT NULL COMMENT '当前应还利息',
  `DQYHFX` decimal(18,2) DEFAULT NULL COMMENT '当期应还罚息',
  `DKJQRQ` date DEFAULT NULL COMMENT '贷款结算日期 YYYYMMDD',
  `HSBJZE` decimal(18,2) DEFAULT NULL COMMENT '回收本金总额',
  `HSLXZE` decimal(18,2) DEFAULT NULL COMMENT '回收利息总额',
  `FXZE` decimal(18,2) DEFAULT NULL COMMENT '罚息总额',
  `TQGHBJZE` decimal(18,2) DEFAULT NULL COMMENT '提前归还本金总额',
  `YQBJZE` decimal(18,2) DEFAULT NULL COMMENT '逾期本金总额',
  `YQLXZE` decimal(18,2) DEFAULT NULL COMMENT '逾期利息总额',
  `LJYQQS` smallint(4) DEFAULT NULL COMMENT '累计预期期数',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人住房贷款账户信息 表6.0.5';

-- ----------------------------
-- Table structure for st_housing_personal_loan
-- ----------------------------
DROP TABLE IF EXISTS `st_housing_personal_loan`;
CREATE TABLE `st_housing_personal_loan` (
  `JKHTBH` char(30) DEFAULT NULL COMMENT '借款合同编号',
  `GFHTBH` char(30) DEFAULT NULL COMMENT '购房合同编号',
  `SWTYHMC` varchar(255) DEFAULT NULL COMMENT '受委托银行名称',
  `SWTYHDM` char(3) DEFAULT NULL COMMENT '受委托银行代码。联行号前三位',
  `DKDBLX` char(2) DEFAULT NULL COMMENT '贷款担保类型 附A.10',
  `YDFKRQ` date DEFAULT NULL COMMENT '约定放款日期 YYYYMMDD',
  `YDDQRQ` date DEFAULT NULL COMMENT '约定到期日期',
  `DKHKFS` char(2) DEFAULT NULL COMMENT '贷款还款方式 附A.11',
  `HKZH` varchar(30) DEFAULT NULL COMMENT '还款账号',
  `ZHKHYHMC` varchar(255) DEFAULT NULL COMMENT '账户开户银行名称',
  `ZHKHYHDM` char(3) DEFAULT NULL COMMENT '账户开户银行代码 联行号前三位',
  `HTDKJE` decimal(18,2) DEFAULT NULL COMMENT '合同贷款金额',
  `DKLX` char(2) DEFAULT NULL COMMENT '贷款类型 附A.12',
  `DKQS` smallint(4) DEFAULT NULL COMMENT '贷款期数',
  `FWZL` varchar(255) DEFAULT NULL COMMENT '房屋坐落',
  `FWJZMJ` decimal(18,2) DEFAULT NULL COMMENT '房屋建筑面积',
  `FWTNMJ` decimal(18,2) DEFAULT NULL,
  `FWXZ` char(2) DEFAULT NULL COMMENT '房屋性质 附A.13',
  `FWZJ` decimal(18,2) DEFAULT NULL COMMENT '房屋总价',
  `JKHTLL` decimal(8,7) DEFAULT NULL COMMENT '借款合同利率',
  `LLFDBL` decimal(4,2) DEFAULT NULL COMMENT '利率浮动比例',
  `GFSFK` decimal(18,2) DEFAULT NULL COMMENT '购房首付款',
  `JKRGJJZH` char(20) DEFAULT NULL COMMENT '借款人公积金账号',
  `JKRXM` varchar(120) DEFAULT NULL COMMENT '借款人姓名',
  `JKRZJLX` char(2) DEFAULT NULL COMMENT '借款人证件类型 附A.1',
  `JKRZJH` char(18) DEFAULT NULL COMMENT '借款人证件号',
  `SFRMC` varchar(255) DEFAULT NULL COMMENT '售房人名称',
  `SFRZHHM` varchar(30) DEFAULT NULL,
  `SFRKHYHDM` char(3) DEFAULT NULL COMMENT '售房人开户银行代码 联行号前三位',
  `SFRKHYHMC` varchar(255) DEFAULT NULL COMMENT '售房人开户银行名称',
  `JKHTQDRQ` date DEFAULT NULL COMMENT '借款合同签订日期 YYYYMMDD',
  `YDHKR` date DEFAULT NULL COMMENT '约定还款日 DD',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人住房贷款借款合同信息 表6.0.2';

-- ----------------------------
-- Table structure for st_project_closed_funds
-- ----------------------------
DROP TABLE IF EXISTS `st_project_closed_funds`;
CREATE TABLE `st_project_closed_funds` (
  `XMBH` bigint(20) unsigned DEFAULT NULL COMMENT '项目编号',
  `DWZH` char(20) DEFAULT NULL COMMENT '业务流水号',
  `ZJJGZHHM` varchar(30) DEFAULT NULL COMMENT '资金监管账户号码',
  `ZJJGZHQCYE` decimal(18,2) DEFAULT NULL COMMENT '资金监管账户期初余额',
  `QCYEFX` char(2) DEFAULT NULL COMMENT '期初余额 A.22',
  `JFFSE` decimal(18,2) DEFAULT NULL COMMENT '借方发生额',
  `DFFSE` decimal(18,2) DEFAULT NULL COMMENT '贷方发生额',
  `ZJJGZHQMYE` decimal(18,2) DEFAULT NULL COMMENT '资金监管账户期末余额',
  `QMYEFX` char(2) DEFAULT NULL COMMENT '期末余额方向 A.22',
  `ZFYT` char(2) DEFAULT NULL COMMENT '支付用途 A.23',
  `ZJLRLY` char(2) DEFAULT NULL COMMENT '资金流入来源 A.24',
  `JYDSZHMC` varchar(255) DEFAULT NULL COMMENT '交易对手账户名称',
  `JYDSZHHM` varchar(30) DEFAULT NULL COMMENT '交易对手账户号码',
  `JYDSZHYHDM` char(3) DEFAULT NULL COMMENT '交易对手账户银行代码',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期 YYYYMMDD',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for st_project_information
-- ----------------------------
DROP TABLE IF EXISTS `st_project_information`;
CREATE TABLE `st_project_information` (
  `XMBH` bigint(20) unsigned DEFAULT NULL COMMENT '项目编号',
  `XMLX` char(2) DEFAULT NULL COMMENT '项目类型 附A.16',
  `XMMC` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `SSCSDM` char(4) DEFAULT NULL COMMENT '所属城市代码 GB/T 2260',
  `TZGM` decimal(18,2) DEFAULT NULL COMMENT '投资规模',
  `DKED` decimal(18,2) DEFAULT NULL COMMENT '贷款额度',
  `JSGM` decimal(18,2) DEFAULT NULL COMMENT '建设规模',
  `XMFLJB` char(2) DEFAULT NULL COMMENT '项目分类级别 附A.17',
  `XMPC` varchar(10) DEFAULT NULL COMMENT '项目批次',
  `LXPFWH` varchar(30) DEFAULT NULL COMMENT '立项批复文号',
  `JSYDGHXKZH` varchar(30) DEFAULT NULL COMMENT '建设用地规划许可证号',
  `GYTDSYZH` varchar(30) DEFAULT NULL COMMENT '国有土地使用证号',
  `JSGCGHXKZH` varchar(30) DEFAULT NULL COMMENT '建设工程规划许可证号',
  `JZGCSGXKZH` varchar(30) DEFAULT NULL COMMENT '建筑工程施工许可证号',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for st_project_loan_account
-- ----------------------------
DROP TABLE IF EXISTS `st_project_loan_account`;
CREATE TABLE `st_project_loan_account` (
  `DKZH` char(30) DEFAULT NULL COMMENT '贷款账号',
  `JKHTBH` char(30) DEFAULT NULL COMMENT '借款合同编号',
  `DKYE` decimal(18,2) DEFAULT NULL COMMENT '贷款余额',
  `DKLL` decimal(8,7) DEFAULT NULL COMMENT '贷款利率',
  `YQFXLL` decimal(8,7) DEFAULT NULL COMMENT '逾期罚息利率',
  `NYFXLL` decimal(8,7) DEFAULT NULL COMMENT '挪用罚息利率',
  `DKFXDJ` char(2) DEFAULT NULL COMMENT '贷款风险等级 附A.14',
  `DKJQRQ` date DEFAULT NULL COMMENT '贷款结清日期 YYYYMMDD',
  `HSBJZE` decimal(18,2) DEFAULT NULL COMMENT '回收本金总额',
  `HSLXZE` decimal(18,2) DEFAULT NULL COMMENT '回收利息总额',
  `FXZE` decimal(18,2) DEFAULT NULL COMMENT '罚息总额',
  `TQHKBJZE` decimal(18,2) DEFAULT NULL COMMENT '提前还款本金总额',
  `YQBJ` decimal(18,2) DEFAULT NULL COMMENT '逾期本金',
  `YQLX` decimal(18,2) DEFAULT NULL COMMENT '逾期利息',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for st_project_loan_contract
-- ----------------------------
DROP TABLE IF EXISTS `st_project_loan_contract`;
CREATE TABLE `st_project_loan_contract` (
  `JKHTBH` char(30) DEFAULT NULL COMMENT '借款合同编号',
  `XMBH` char(20) DEFAULT NULL COMMENT '项目编号',
  `DKJE` decimal(18,2) DEFAULT NULL COMMENT '贷款金额',
  `DKQX` decimal(18,2) DEFAULT NULL COMMENT '贷款期限',
  `DKLL` decimal(8,7) DEFAULT NULL COMMENT '贷款利率',
  `YQFXLL` decimal(8,7) DEFAULT NULL COMMENT '逾期罚息利率',
  `NYFXLL` varchar(8) DEFAULT NULL COMMENT '挪用罚息利率',
  `ZJJGZHHM` varchar(30) DEFAULT NULL COMMENT '资金监管账户号码',
  `YHDM` char(3) DEFAULT NULL COMMENT '银行代码',
  `DKFFFS` char(2) DEFAULT NULL COMMENT '贷款发放方式 附A.18',
  `DKHBFS` char(2) DEFAULT NULL COMMENT '贷款还本方式 附A.19',
  `WTR` varchar(255) DEFAULT NULL COMMENT '委托人',
  `WTRQYDB` varchar(120) DEFAULT NULL COMMENT '委托人签约代表',
  `WTRQYSJ` date DEFAULT NULL COMMENT '委托人签约时间',
  `DKR` varchar(255) DEFAULT NULL COMMENT '贷款人',
  `DKRQYDB` varchar(120) DEFAULT NULL COMMENT '贷款人签约代表',
  `DKRQYSJ` date DEFAULT NULL COMMENT '贷款人签约时间',
  `JKR` varchar(255) DEFAULT NULL COMMENT '借款人',
  `JKRQYDB` varchar(120) DEFAULT NULL COMMENT '借款人签约代表',
  `JKRQYSJ` date DEFAULT NULL COMMENT '借款人签约时间 YYYYMMDD',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for st_project_loan_details
-- ----------------------------
DROP TABLE IF EXISTS `st_project_loan_details`;
CREATE TABLE `st_project_loan_details` (
  `DKZH` char(30) DEFAULT NULL COMMENT '贷款账号',
  `DWZH` char(20) DEFAULT NULL COMMENT '业务流水号',
  `YWMXLX` char(2) DEFAULT NULL COMMENT '业务明细类型 A.2',
  `FSE` decimal(18,2) DEFAULT NULL COMMENT '发生额',
  `BJJE` decimal(18,2) DEFAULT NULL COMMENT '本金金额',
  `LXJE` decimal(18,2) DEFAULT NULL COMMENT '利息金额',
  `YQFXJE` decimal(18,2) DEFAULT NULL COMMENT '逾期罚息金额',
  `NYFXJE` decimal(18,2) DEFAULT NULL COMMENT '挪用罚息金额',
  `JZRQ` date DEFAULT NULL COMMENT '记账日期',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for st_project_mortgage_contract
-- ----------------------------
DROP TABLE IF EXISTS `st_project_mortgage_contract`;
CREATE TABLE `st_project_mortgage_contract` (
  `DYHTBH` char(20) DEFAULT NULL COMMENT '抵押合同编号',
  `JKHTBH` char(30) DEFAULT NULL COMMENT '借款合同编号',
  `DYWLX` char(2) DEFAULT NULL COMMENT '抵押物类型 A.20',
  `DYWMC` varchar(255) DEFAULT NULL COMMENT '抵押物名称',
  `DYWQZH` varchar(30) DEFAULT NULL COMMENT '抵押物权证号',
  `DYWCS` varchar(255) DEFAULT NULL COMMENT '抵押物处所',
  `DYQJLRQ` date DEFAULT NULL COMMENT '抵押权建立日期 YYYYMMDD',
  `DYQJCRQ` date DEFAULT NULL COMMENT '抵押权解除日期 YYYYMMDD',
  `DYWPGJZ` decimal(18,2) DEFAULT NULL COMMENT '抵押物评估价值',
  `YDYJZ` decimal(18,2) DEFAULT NULL COMMENT '已抵押价值',
  `DYL` decimal(3,2) DEFAULT NULL COMMENT '抵押率',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for st_project_overdue_registration
-- ----------------------------
DROP TABLE IF EXISTS `st_project_overdue_registration`;
CREATE TABLE `st_project_overdue_registration` (
  `DKZH` char(30) DEFAULT NULL COMMENT '贷款账号',
  `YQBJ` decimal(18,2) DEFAULT NULL COMMENT '逾期本金',
  `YQLX` decimal(18,2) DEFAULT NULL COMMENT '逾期利息',
  `YQFX` decimal(18,2) DEFAULT NULL COMMENT '逾期罚息',
  `SSRQ` date DEFAULT NULL COMMENT '实收日期',
  `SSYQBJ` decimal(18,2) DEFAULT NULL COMMENT '实收逾期本金',
  `SSYQLX` decimal(18,2) DEFAULT NULL COMMENT '实收逾期利息',
  `SSYQFX` decimal(18,2) DEFAULT NULL COMMENT '实收逾期罚息',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for st_settlement_daybook
-- ----------------------------
DROP TABLE IF EXISTS `st_settlement_daybook`;
CREATE TABLE `st_settlement_daybook` (
  `DWZH` char(20) DEFAULT NULL COMMENT '业务流水号',
  `ZJYWLX` char(2) DEFAULT NULL COMMENT '资金业务类型',
  `YWPZHM` char(20) DEFAULT NULL COMMENT '业务凭证号码',
  `YHJSLSH` varchar(40) DEFAULT NULL COMMENT '银行结算流水号',
  `FSE` decimal(18,2) DEFAULT NULL COMMENT '发生额',
  `JSFSRQ` date DEFAULT NULL COMMENT '结算发生日期',
  `JSYHDM` char(3) DEFAULT NULL COMMENT '结算银行代码',
  `FKYHDM` char(3) DEFAULT NULL COMMENT '付款银行代码',
  `FKZHHM` varchar(30) DEFAULT NULL COMMENT '付款账号号码',
  `FKZHMC` varchar(120) DEFAULT NULL COMMENT '付款账号名称',
  `SKYHDM` char(3) DEFAULT NULL COMMENT '收款银行代码',
  `SKZHHM` varchar(30) DEFAULT NULL COMMENT '收款账户号码',
  `SKZHMC` varchar(120) DEFAULT NULL COMMENT '收款账户名称',
  `ZhaiYao` varchar(255) DEFAULT NULL COMMENT '摘要',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行结算流水信息 表9.0.2';

-- ----------------------------
-- Table structure for st_settlement_special_bank_account
-- ----------------------------
DROP TABLE IF EXISTS `st_settlement_special_bank_account`;
CREATE TABLE `st_settlement_special_bank_account` (
  `KMBH` varchar(19) DEFAULT NULL COMMENT '科目编号 附A.25',
  `YHZHHM` varchar(30) DEFAULT NULL COMMENT '银行专户号码',
  `YHZHMC` varchar(255) DEFAULT NULL COMMENT '银行专户名称',
  `YHDM` char(3) DEFAULT NULL COMMENT '银行代码',
  `YHMC` varchar(255) DEFAULT NULL COMMENT '银行名称',
  `KHRQ` date DEFAULT NULL COMMENT '开户日期',
  `ZHXZ` char(2) DEFAULT NULL COMMENT '专户性质',
  `XHRQ` date DEFAULT NULL COMMENT '销户日期 YYYYMMDD',
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

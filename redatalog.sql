/**
编号：27
修复日期：2018-03-12
修复内容：同一批次号的提取业务状态不统一；本已入账的业务点成了作废
执行人：郭大露
编写人：郭大露
 */

update st_collection_personal_business_details d
inner JOIN c_collection_personal_business_details_extension de on de.id = d.extension
set de.STEP = '新建'
where de.pch = '211803120000542';

update st_collection_personal_business_details d
inner JOIN c_collection_personal_business_details_extension de on de.id = d.extension
set de.STEP = '已入账'
where d.YWLSH = '011803120000006';



/**
编号：26
修复日期：2018-03-07
修复内容：调基业务附表增加记录业务发生时的比例情况，用于显示；同时修复老数据
执行人：
编写人：杨凡
 */
alter table c_collection_person_radix_vice add DWJCBL decimal(18,2) default 0 after FSRS;
alter table c_collection_person_radix_vice add GRJCBL decimal(18,2) default 0 after FSRS;

update c_collection_person_radix_vice a
join st_collection_unit_business_details b on a.dwywmx = b.id
join st_collection_unit_account ua on b.dwzh = ua.dwzh
set a.dwjcbl = ua.dwjcbl,a.grjcbl = ua.grjcbl;

/*
编号：25
修复日期：2018-03-01 13:28
修复内容：021802210000492
执行人：谭怡
编写人：郭大露
 */

UPDATE st_collection_unit_business_details d
INNER JOIN st_common_unit u ON u.id = d.Unit
INNER JOIN st_collection_unit_account ua ON ua.id = u.CollectionUnitAccount
set d.FSE = 1245.16,ua.DWZHYE = ua.DWZHYE - 1245.16
WHERE d.YWLSH = '021802210000492'

-- end 20

/*
编号：24
修复日期：2018-02-24 12:46
修复内容：处理威宁县农行问题
执行人：谭怡
编写人：谭怡

 */
INSERT INTO `c_finance_temporary_record`(`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `FKHM`, `FKZH`, `HRJE`, `HRSJ`, `JZPZH`, `SKHM`, `SKZH`, `state`, `ZhaiYao`, `remark`, `YJZPZH`) VALUES ('1', '2018-02-24 10:03:06', b'0', NULL, '2018-02-24 10:03:06', '中国农业银行威宁县支行', '23854001040004157', 98699.00, '2018-02-24 00:00:00', NULL, '毕节市住房公积金管理中心', '23873001040004956', b'0', '经查2017年12月18日威宁县农业银行由23854001040004157账户划交公积金，误作为合并账户划款资金处理，作对冲4956账户资金处理，凭证号1712004779#，现更正作农行缴存处理，先入暂收后划到威宁管理部，由威宁管理部核实后作入账处理。', NULL, '1802015928');

-- end 24


/*
编号：23
修复日期：2018-02-13 12:46
修复内容：贷款放款失败，更改业务流水号以便重新发送到结算平台
执行人：谭怡
编写人：练隆森

 */
SELECT
process.YWLSH
FROM c_loan_housing_business_process process
WHERE process.YWLSH in(
		'051801280000016',
		'051801230000131'
)

UPDATE
c_loan_housing_business_process process
LEFT JOIN c_loan_funds_information_basic          funds_basic          ON process.ywlsh = funds_basic.ywlsh
LEFT JOIN c_loan_housing_person_information_basic information_basic    ON process.ywlsh = information_basic.ywlsh
LEFT JOIN c_loan_housing_loan                     housing_loan         ON process.ywlsh = housing_loan.ywlsh
LEFT JOIN  c_loan_guarantee_extension              guarantee_extension  ON process.ywlsh = guarantee_extension.ywlsh
LEFT JOIN  c_loan_guarantee_pledge_extension       pledge_extension     ON process.ywlsh = pledge_extension.ywlsh
LEFT JOIN  c_loan_housing_coborrower_extension     coborrower_extension ON process.ywlsh = coborrower_extension.ywlsh
LEFT JOIN  c_loan_guarantee_mortgage_extension     mortgage_extension   ON process.ywlsh = mortgage_extension.ywlsh

SET
process.YWLSH              = CONCAT('0',process.YWLSH  + 10000),
guarantee_extension.YWLSH  = CONCAT('0',process.YWLSH  + 10000),
pledge_extension.YWLSH     = CONCAT('0',process.YWLSH  + 10000),
coborrower_extension.YWLSH = CONCAT('0',process.YWLSH  + 10000),
mortgage_extension.YWLSH   = CONCAT('0',process.YWLSH  + 10000),
funds_basic.YWLSH          = CONCAT('0',process.YWLSH  + 10000),
information_basic.YWLSH    = CONCAT('0',process.YWLSH  + 10000),
housing_loan.YWLSH         = CONCAT('0',process.YWLSH  + 10000)

WHERE process.YWLSH in(
		'051801280000016',
		'051801230000131'
)
-- end 23

/*
编号：22
修复日期：2018-02-28 13:28
修复内容：熊丽、张弩夫妻共同还贷状态不统一，修改张弩的状态为待审核
执行人：郭大露
编写人：郭大露
 */

UPDATE st_collection_personal_business_details d
INNER JOIN c_collection_personal_business_details_extension de ON de.id = d.extension
set de.STEP = '待审核'
where d.YWLSH = '011802270003981';

-- end 22

/*
编号：21
修复日期：2018-02-27 13:53
修复内容：应付成敏要求，删除凭证
执行人：谭怡
编写人：谭怡
*/

UPDATE st_finance_recording_voucher sv,
 c_finance_recording_voucher_extension cve,
 st_finance_subsidiary_accounts sa
SET sv.JFFSE = 0.00,
 sv.DFFSE = 0.00,
 cve.JFHJ = 0.00,
 cve.DFHJ = 0.00,
 sa.JFFSE = 0.00,
 sa.DFFSE = 0.00,
 sv.ZhaiYao = concat(sv.ZhaiYao, '已删除'),
 sa.ZhaiYao = concat(sa.ZhaiYao, '已删除')
WHERE
	sv.extension = cve.id
AND sv.JZPZH = sa.JZPZH
AND sv.JZPZH = '1802017904'

-- end 21

/*
编号：20
修复日期：2018-02-27 13:53
修复内容：修复黔西业务权证号和他项权证号错误
执行人：谭怡
编写人：练隆森
*/
UPDATE
c_loan_housing_business_process process
INNER JOIN c_loan_housing_guarantee_contract_vice vice on process.loanHousingGuaranteeContractVice = vice.id
INNER JOIN c_loan_housing_person_information_basic basic on basic.ywlsh = process.ywlsh
INNER JOIN st_housing_guarantee_contract st on basic.guaranteeContract = st.id
SET
vice. DYWQZH   = '黔（2017）黔西县不动产证明第0003901号' ,
st.   DYWQZH   = '黔（2017）黔西县不动产证明第0003901号' ,
vice. DYWTXQZH = '黔（2017）黔西县不动产证明第0003901号' ,
st.   DYWTXQZH = '黔（2017）黔西县不动产证明第0003901号'
WHERE
process.YWLSH = '051801090000224'

-- end 20

/*
编号：19
修复日期：2018-02-27 13:26
修复内容：刘传建销户提取提取后银行处理失败时，退回钱后个人状态应为封存
执行人：郭大露
编写人：郭大露
 */
UPDATE st_common_person p
INNER JOIN st_collection_personal_account pa ON p.PersonalAccount = pa.id
set pa.GRZHZT = '02'
WHERE p.GRZH = '9909063003913'

-- end 19

/*
编号：18
修复日期：2018-02-26 14:11
修复内容：卢婷的提取记录，本应作废，操作员失误点击了已入账
执行人：郭大露
编写人：郭大露
 */

 UPDATE st_collection_personal_business_details d
INNER JOIN c_collection_personal_business_details_extension de ON de.id = d.extension
SET de.STEP = '已作废'
WHERE
	d.YWLSH = '011802070005240';

UPDATE st_collection_unit_business_details d
INNER JOIN c_collection_unit_business_details_extension de ON de.id = d.extenstion
SET de.STEP = '已作废'
WHERE
	d.YWLSH = '021802070000491';

UPDATE st_common_person p
INNER JOIN c_common_person_extension pe ON pe.id = p.extension
INNER JOIN st_collection_personal_account pa ON pa.id = p.PersonalAccount
SET pe.XCTQRQ = NULL,pa.GRZHYE = pa.GRZHYE + 14000
WHERE
	p.GRZH = '9915010200001';

UPDATE st_common_unit u
INNER JOIN st_collection_unit_account ua ON ua.id = u.CollectionUnitAccount
set ua.DWZHYE = ua.DWZHYE + 14000
WHERE u.DWZH = '9817112001215';



UPDATE st_finance_recording_voucher sv,
 c_finance_recording_voucher_extension cve,
 st_finance_subsidiary_accounts sa
SET sv.JFFSE = 0.00,
 sv.DFFSE = 0.00,
 cve.JFHJ = 0.00,
 cve.DFHJ = 0.00,
 sa.JFFSE = 0.00,
 sa.DFFSE = 0.00,
 sv.ZhaiYao = concat(sv.ZhaiYao, '已删除'),
 sa.ZhaiYao = concat(sa.ZhaiYao, '已删除')
WHERE
	sv.extension = cve.id
AND sv.JZPZH = sa.JZPZH
AND sv.JZPZH = '1802006684'

-- end 18

/*
编号：17
修复日期：2018-02-11 19:16
修复内容：贷款历史数据问题
执行人：谭怡
编写人：谭怡

 */
UPDATE  c_loan_housing_business_process SET STEP='已入账'  WHERE STEP IS NULL

-- end 17


SELECT
	*
FROM
	c_loan_housing_person_information_basic
WHERE
	JKRZJHM IN (
		'41302719831027007X',
		'522425198806036328',
		'522425197905066616',
		'522423198911100018'
	)

/*
编号：16
修复日期：2018-02-11 19:16
修复内容：贷款历史数据问题
执行人：谭怡
编写人：练隆森

 */
UPDATE
c_loan_housing_person_information_basic basic
inner JOIN c_loan_housing_business_process process on basic.YWLSH = process.YWLSH
set basic.deleted_at = NOW(), basic.deleted = 1
WHERE process.CZNR is null

-- end 16

/*
编号：15
修复日期：2018-02-11 16:41
修复内容：贷款状态机问题
执行人：谭怡
编写人：郭大露

 */

UPDATE c_state_machine_configuration SET TransitionKind='EXTERNAL' WHERE TransitionKind<>'EXTERNAL'


-- end 15

/*
编号：14
修复日期：2018-02-11 13:01
修复内容：贷款放款失败，更改业务流水号以便重新发送到结算平台
执行人：谭怡
编写人：练隆森

 */
SELECT
process.YWLSH
FROM c_loan_housing_business_process process
WHERE process.YWLSH in(
		'051801160000054',
		'051801160000051',
		'051801160000050',
		'051801160000036'
)

UPDATE
c_loan_housing_business_process process
LEFT JOIN c_loan_funds_information_basic          funds_basic          ON process.ywlsh = funds_basic.ywlsh
LEFT JOIN c_loan_housing_person_information_basic information_basic    ON process.ywlsh = information_basic.ywlsh
LEFT JOIN c_loan_housing_loan                     housing_loan         ON process.ywlsh = housing_loan.ywlsh
LEFT JOIN  c_loan_guarantee_extension              guarantee_extension  ON process.ywlsh = guarantee_extension.ywlsh
LEFT JOIN  c_loan_guarantee_pledge_extension       pledge_extension     ON process.ywlsh = pledge_extension.ywlsh
LEFT JOIN  c_loan_housing_coborrower_extension     coborrower_extension ON process.ywlsh = coborrower_extension.ywlsh
LEFT JOIN  c_loan_guarantee_mortgage_extension     mortgage_extension   ON process.ywlsh = mortgage_extension.ywlsh

SET
process.YWLSH              = CONCAT('0',process.YWLSH  + 10000),
guarantee_extension.YWLSH  = CONCAT('0',process.YWLSH  + 10000),
pledge_extension.YWLSH     = CONCAT('0',process.YWLSH  + 10000),
coborrower_extension.YWLSH = CONCAT('0',process.YWLSH  + 10000),
mortgage_extension.YWLSH   = CONCAT('0',process.YWLSH  + 10000),
funds_basic.YWLSH          = CONCAT('0',process.YWLSH  + 10000),
information_basic.YWLSH    = CONCAT('0',process.YWLSH  + 10000),
housing_loan.YWLSH         = CONCAT('0',process.YWLSH  + 10000)

WHERE process.YWLSH in(
		'051801160000054',
		'051801160000051',
		'051801160000050',
		'051801160000036'
)
-- end 14


/*
编号：13
修复日期：2018-02-11 12:09
修复内容：贷款删除上下文
执行人：谭怡
编写人：谭怡

 */
UPDATE c_business_state_transform_context
SET deleted = 1
WHERE
	taskid IN (
		'051712260000040',
		'051801030000439',
		'051712260000038',
		'051801080000088'
	)
-- end 13


/*
编号：12
修复日期：2018-02-08 12:50
修复内容：修复个人姓名
执行人：练隆森
编写人：练隆森

 */

UPDATE
st_common_person person
INNER JOIN st_collection_personal_account account on person.PersonalAccount = account.id
SET person.XingMing = '邓怀群'
WHERE
account.XHRQ IS NOT NULL
AND
person.ZJHM = '52242219640917004X'

-- end 12

/*
编号：11
修复日期：2018-02-07 21:10
修复内容：修复贷款发放问题
执行人：谭怡
编写人：练隆森

 */
INSERT INTO c_loan_housing_loan(
id,created_at,updated_at,deleted,deleted_at,state,
CZY,YWLSH,YWWD,
DKZH,JKRZJHM,JKRZJLX,JKHTBH,JKRXM,
FKYHMC,FKZH,FKZHHM,
SKYHMC,SKZH,SKZHHM
)
VALUES(
REPLACE(UUID(),"-",""),NOW(),NOW(),0,null,0,
'李德江','051801160000075','3',
'8018012300044','522422198604016557','01','9018012300016','谢鑫',
'中国农业银行股份有限公司毕节分行','23873001040006175','毕节市住房公积金管理中心',
'中国农业银行股份有限公司毕节分行','23794001040001115','毕节兴业房地产开发有限公司'
)

INSERT INTO c_loan_housing_loan(
id,created_at,updated_at,deleted,deleted_at,state,
CZY,YWLSH,YWWD,
DKZH,JKRZJHM,JKRZJLX,JKHTBH,JKRXM,
FKYHMC,FKZH,FKZHHM,
SKYHMC,SKZH,SKZHHM
)
VALUES(
REPLACE(UUID(),"-",""),NOW(),NOW(),0,null,0,
'李德江','051801160000070','3',
'8018012300045','522401198709276476','01','9018012300022','马秀平',
'中国农业银行股份有限公司毕节分行','23873001040006175','毕节市住房公积金管理中心',
'中国农业银行股份有限公司毕节分行','23794001040001115','毕节兴业房地产开发有限公司'
)

INSERT INTO c_loan_housing_loan(
id,created_at,updated_at,deleted,deleted_at,state,
CZY,YWLSH,YWWD,
DKZH,JKRZJHM,JKRZJLX,JKHTBH,JKRXM,
FKYHMC,FKZH,FKZHHM,
SKYHMC,SKZH,SKZHHM
)
VALUES(
REPLACE(UUID(),"-",""),NOW(),NOW(),0,null,0,
'李德江','051801180000085','3',
'8018012300053','130927199002140189','01','9018012600029','王倩',
'中国建设银行股份有限公司毕节中山支行','52001694036052500058','毕节市住房公积金管理中心',
'中国建设银行股份有限公司毕节中山支行','52001694036052514622','毕节碧阳置业有限公司'
)
-- end 11

/*
编号：10
修复日期：2018-02-07 10:30
修复内容：修复贷款的老数据，状态转换失败的问题
执行人：谭怡
编写人：郭大露

 */

UPDATE c_business_state_transform_context c
SET c.context = REPLACE(
	c.context ,
	'INTERNAL' ,
	'EXTERNAL'
)
WHERE
	c.context LIKE '%INTERNAL%'

-- end 10

/*
编号：9
修复日期：2018-02-07 9:56
修复内容：更新状态机设置
执行人：谭怡
编写人：郭大露

 */

UPDATE c_state_machine_configuration
SET TransitionKind = 'EXTERNAL'
where TransitionKind = 'INTERNAL'

-- end 9

/*
编号：8
修复日期：2018-02-07 13:40
修复内容：中国人民政治协商会议贵州省赫章县委员会办公室，增加9913101600019 周雨泉、9913091600019 陆慧君的 补缴数据
执行人：杨凡
编写人：杨凡

 */
select id from st_common_unit where dwzh = '9817112009060'

# 11月数据
insert into c_collection_unit_business_details_extension
(id,beizhu,czmc,czy,jbrxm,slsj,step,ywwd,created_at,deleted)
values
(
'92416a3c0bb311e8a09d6cb3113b9aac',null,'02','周晓迪','未知经办人',NOW(),'待确认','10',NOW(),0
);
insert into st_collection_unit_business_details
(id,created_at,deleted,czbz,dwzh,fse,fslxe,fsrs,hbjny,ywmxlx,extenstion,Unit)
values(
'b20fb36a0bb411e8a09d6cb3113b9aac',now(),0,'01','9817112009060',1324,0,1,'201711','02','92416a3c0bb311e8a09d6cb3113b9aac','64c2858bd2b611e79ad8438ef89ed5c6'
);
insert into c_collection_unit_payback_vice
(id,created_at,deleted,bjfs,dwywmx,fse)
values (
'120fb36a0bb411e8a09d6cb3113b9aac',now(),0,'02','b20fb36a0bb411e8a09d6cb3113b9aac',1324
);
insert into c_collection_unit_payback_detail_vice
(id,created_at,deleted,bjyy,dwbje,grbje,grzh,bjmx)
values(
'220fb36a0bb411e8a09d6cb3113b9aac',now(),0,'转移补缴',882.67,441.33,'9913101600019','120fb36a0bb411e8a09d6cb3113b9aac'
);

# 12月数据
insert into c_collection_unit_business_details_extension
(id,beizhu,czmc,czy,jbrxm,slsj,step,ywwd,created_at,deleted)
values
(
'92416a3c0bb311e8a09d6cb3113b9a11',null,'02','周晓迪','未知经办人',NOW(),'待确认','10',NOW(),0
);
insert into st_collection_unit_business_details
(id,created_at,deleted,czbz,dwzh,fse,fslxe,fsrs,hbjny,ywmxlx,extenstion,Unit)
values(
'b20fb36a0bb411e8a09d6cb3113b9a22',now(),0,'01','9817112009060',2632,0,1,'201712','02','92416a3c0bb311e8a09d6cb3113b9a11','64c2858bd2b611e79ad8438ef89ed5c6'
);
insert into c_collection_unit_payback_vice
(id,created_at,deleted,bjfs,dwywmx,fse)
values (
'120fb36a0bb411e8a09d6cb3113b9a33',now(),0,'02','b20fb36a0bb411e8a09d6cb3113b9a22',2632
);
insert into c_collection_unit_payback_detail_vice
(id,created_at,deleted,bjyy,dwbje,grbje,grzh,bjmx)
values(
'220fb36a0bb411e8a09d6cb3113b9a44',now(),0,'转移补缴',882.67,441.33,'9913101600019','120fb36a0bb411e8a09d6cb3113b9a33'
);
insert into c_collection_unit_payback_detail_vice
(id,created_at,deleted,bjyy,dwbje,grbje,grzh,bjmx)
values(
'220fb36a0bb411e8a09d6cb3113b9a55',now(),0,'转移补缴',872,436,'9913091600019','120fb36a0bb411e8a09d6cb3113b9a33'
);
-- end 8



/*
编号：8
修复日期：2018-02-06:17:18
修复内容：贷款删除上下文
执行人：谭怡
编写人：谭怡

 */
UPDATE c_business_state_transform_context
SET deleted = 1
WHERE
	taskid IN (
		'051801210000001',
		'051801210000003',
		'051801210000002'
	)
-- end 8

/*
编号：7
修复日期：2018-02-06:14:07
修复内容：吴顺-提取：11-07该笔提取删除，金额8200，个人账户余额增加8200
执行人：杨凡
编写人：杨凡
 */

select * from st_collection_personal_account  where grzh = '9912020100493' ;
# 吴顺 9912020100493 个人账户余额 8318.27 -> 16518.27
update st_collection_personal_account
set grzhye = grzhye + 8200
where grzh = '9912020100493' ;

select * from st_collection_unit_account where dwzh = '9817112003189';
# 贵州大方煤业有限公司,9817112003189,单位账户余额 8140935.97 -> 8149135.97
update st_collection_unit_account
set dwzhye = dwzhye + 8200
where dwzh = '9817112003189';

# 提取业务撤销
select * from st_collection_personal_business_details a
where a.grzh = '9912020100493' and a.GJHTQYWLX = '11';
update st_collection_personal_business_details a
set a.deleted = 1 , a.fse = 0 ,a.jzrq = null
where a.grzh = '9912020100493' and a.GJHTQYWLX = '11';
-- end 7



/*
编号：6
修复日期：2018-02-06:14:13
修复内容：根据客户要求进行基础数据录入，老系统有，新系统未录入。
执行人：谭怡
编写人：谭怡

 */

UPDATE st_collection_unit_account sa,
c_collection_unit_account_extension ce
SET ce.ZSYE = ce.ZSYE + 9775
WHERE
	sa.extenstion = ce.id
	AND sa.DWZH = '9818010400003'

-- 	end 6

/*
编号：5
修复日期：2018-02-06:13:50
修复内容：修复状态初始化那笔数据
执行人：练隆森
编写人：练隆森

 */
UPDATE
c_loan_housing_business_process process
SET process.STEP = '新建'
WHERE process.STEP = '初始状态'
-- end 5

/*
编号：4
修复日期：2018-02-06:13:43
修复内容：删除凭证
执行人：谭怡
编写人：谭怡

 */
UPDATE st_finance_recording_voucher sv,
 c_finance_recording_voucher_extension cve,
 st_finance_subsidiary_accounts sa
SET sv.JFFSE = 0.00,
 sv.DFFSE = 0.00,
 cve.JFHJ = 0.00,
 cve.DFHJ = 0.00,
 sa.JFFSE = 0.00,
 sa.DFFSE = 0.00,
 sv.ZhaiYao = concat(sv.ZhaiYao, '已删除'),
 sa.ZhaiYao = concat(sa.ZhaiYao, '已删除')
WHERE
	sv.extension = cve.id
AND sv.JZPZH = sa.JZPZH
AND sv.JZPZH = '1802005449'
-- end 4

/*
编号：3
修复日期：2018-02-06:13:36
修复内容：贷款删除上下文
执行人：谭怡
编写人：谭怡

 */
UPDATE c_business_state_transform_context
SET deleted = 1
WHERE
	taskid IN (
		'051801170000094',
		'051801170000097',
		'051801170000087',
		'051801100000090',
		'051801100000036',
		'051801160000020',
		'051801290000027'
	)
-- end 3

/*
编号：2
修复日期：2018-02-06:13:27
修复内容：贷款放款失败，更改业务流水号以便重新发送到结算平台
执行人：谭怡
编写人：练隆森

 */
SELECT
process.YWLSH
FROM c_loan_housing_business_process process
WHERE process.YWLSH in(
'051801300010099',
'051712270010121',
'051712260010099',
'051712250010042',
'051801030010366',
'051801240010094'
)

UPDATE
c_loan_housing_business_process process
LEFT JOIN c_loan_funds_information_basic          funds_basic          ON process.ywlsh = funds_basic.ywlsh
LEFT JOIN c_loan_housing_person_information_basic information_basic    ON process.ywlsh = information_basic.ywlsh
LEFT JOIN c_loan_housing_loan                     housing_loan         ON process.ywlsh = housing_loan.ywlsh
LEFT JOIN  c_loan_guarantee_extension              guarantee_extension  ON process.ywlsh = guarantee_extension.ywlsh
LEFT JOIN  c_loan_guarantee_pledge_extension       pledge_extension     ON process.ywlsh = pledge_extension.ywlsh
LEFT JOIN  c_loan_housing_coborrower_extension     coborrower_extension ON process.ywlsh = coborrower_extension.ywlsh
LEFT JOIN  c_loan_guarantee_mortgage_extension     mortgage_extension   ON process.ywlsh = mortgage_extension.ywlsh

SET
process.YWLSH              = CONCAT('0',process.YWLSH  + 10000),
guarantee_extension.YWLSH  = CONCAT('0',process.YWLSH  + 10000),
pledge_extension.YWLSH     = CONCAT('0',process.YWLSH  + 10000),
coborrower_extension.YWLSH = CONCAT('0',process.YWLSH  + 10000),
mortgage_extension.YWLSH   = CONCAT('0',process.YWLSH  + 10000),
funds_basic.YWLSH          = CONCAT('0',process.YWLSH  + 10000),
information_basic.YWLSH    = CONCAT('0',process.YWLSH  + 10000),
housing_loan.YWLSH         = CONCAT('0',process.YWLSH  + 10000)

WHERE process.YWLSH in(
'051801300000099',
'051712270000121',
'051712260000099',
'051712250000042',
'051801030000366',
'051801240000094'
)
-- end 2


 /*
编号：1
修复日期：2018-02-06 10:10
修复内容：暂收汇缴修改为未分摊汇补缴历史数据
执行人：谭怡
编写人：谭怡

 */

 UPDATE c_finance_record_unit SET ZJLY='未分摊汇补缴' WHERE ZJLY='暂收汇缴'

-- end 1
package com.handge.housingfund.finance.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.collection.enumeration.UnitAccountStatus;
import com.handge.housingfund.common.service.finance.IFinanceBaseService;
import com.handge.housingfund.common.service.finance.IVoucherAutoService;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherAmount;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.base.General;
import com.handge.housingfund.common.service.finance.model.base.TableRow;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.finance.model.enums.WFTLY;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.CriteriaUtils;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tanyi on 2017/10/1.
 */
public class VoucherAutoService implements IVoucherAutoService {

    @Autowired
    private IVoucherManagerService iVoucherManagerService;

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;

    @Autowired
    private IStCommonPersonDAO iStCommonPersonDAO;

    @Autowired
    private IStCollectionUnitAccountDAO iStCollectionUnitAccountDAO;

    @Autowired
    ICFinanceBusinessProcessDAO financeBusinessProcessDAO;

    @Autowired
    IStCollectionPersonalAccountDAO iStCollectionPersonalAccountDAO;

    @Autowired
    ISettlementSpecialBankAccountManageService settlementSpecialBankAccountManageService;

    @Autowired
    IFinanceBaseService financeBaseService;

    @Autowired
    private ICFinanceRecordUnitDAO icFinanceRecordUnitDAO;

    public static Gson gson = new Gson();

    @Override
    public void checkBusiness(AccChangeNotice accChangeNotice) {
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            throw new ErrorException(ReturnEnumeration.Business_In_Process, "结算平台数据错误");
        }
        String zhaiyao = accChangeNoticeFile.getSummary();//摘要
        BigDecimal fse = accChangeNoticeFile.getAmt();//发生额

        //如果是支出，不做暂收
        if (fse.compareTo(BigDecimal.ZERO) < 0) {
            return;
        }

        String date = accChangeNoticeFile.getDate();//交易日期
        Date jy_date = DateUtil.safeStr2Date("yyyyMMdd", date);
        String remark = accChangeNoticeFile.getRemark();//备注
        String acct = accChangeNoticeFile.getAcct();//银行专户号码
        String no = accChangeNoticeFile.getNo();
        String opponentName = accChangeNoticeFile.getOpponentName();//交易对手户名
        String opponentAcct = accChangeNoticeFile.getOpponentAcct();//交易对手账号

        List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
        List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

        //查找单位 备注
        StCommonUnit dwxx = DAOBuilder.instance(commonUnitDAO).searchOption(SearchOption.REFINED).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.in(CriteriaUtils.addAlias(criteria, "collectionUnitAccount.dwzhzt"),
                        UnitAccountStatus.正常.getCode(), UnitAccountStatus.开户.getCode(), UnitAccountStatus.缓缴.getCode()));
                criteria.add(Restrictions.or(Restrictions.eq("dwzh", remark), Restrictions.eq("dwmc", remark)));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (dwxx == null) {
            //查找单位 摘要
            dwxx = DAOBuilder.instance(commonUnitDAO).searchOption(SearchOption.REFINED).extension(new IBaseDAO.CriteriaExtension() {

                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.in(CriteriaUtils.addAlias(criteria, "collectionUnitAccount.dwzhzt"),
                            UnitAccountStatus.正常.getCode(), UnitAccountStatus.开户.getCode(), UnitAccountStatus.缓缴.getCode()));
                    criteria.add(Restrictions.or(Restrictions.eq("dwzh", zhaiyao), Restrictions.eq("dwmc", zhaiyao)));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (dwxx == null) {
                //查找单位 交易对手户名
                dwxx = DAOBuilder.instance(commonUnitDAO).searchOption(SearchOption.REFINED).extension(new IBaseDAO.CriteriaExtension() {

                    @Override
                    public void extend(Criteria criteria) {
                        criteria.add(Restrictions.in(CriteriaUtils.addAlias(criteria, "collectionUnitAccount.dwzhzt"),
                                UnitAccountStatus.正常.getCode(), UnitAccountStatus.开户.getCode(), UnitAccountStatus.缓缴.getCode()));
                        criteria.add(Restrictions.or(Restrictions.eq("dwzh", opponentName), Restrictions.eq("dwmc", opponentName)));
                    }
                }).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                if (dwxx == null) {
                    CenterAccountInfo centerAccountInfo = settlementSpecialBankAccountManageService.getSpecialAccountByZHHM(acct);
                    String kmbh;
                    if (centerAccountInfo != null) {
                        kmbh = centerAccountInfo.getKMBH();
                    } else {
                        kmbh = "101";
                    }
                    JFSJ.add(new VoucherAmount() {{
                        this.setRemark(kmbh);
                        this.setZhaiYao("收到暂收");
                        this.setJinE(fse);
                    }});
                    DFSJ.add(new VoucherAmount() {{
                        this.setZhaiYao("收到暂收");
                        this.setJinE(fse);
                    }});
                    VoucherRes voucherRes = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动", "", "",
                            VoucherBusinessType.收到暂收.getCode(), VoucherBusinessType.收到暂收.getCode(), no,
                            JFSJ, DFSJ, String.valueOf(1), acct, null);

                    //暂收记录
                    String SKHM;
                    if (centerAccountInfo == null) {
                        SKHM = "未知专户账号";
                    } else {
                        SKHM = centerAccountInfo.getYHZHMC();
                    }
                    iVoucherManagerService.addTemporaryRecord(acct, SKHM, opponentAcct, opponentName, fse, jy_date, voucherRes.getJZPZH(), zhaiyao, remark);
                    return;
                }
            }
        }
        StCommonUnit finalDwxx = dwxx;
        JFSJ.add(new VoucherAmount() {{
            this.setZhaiYao("汇补缴" + finalDwxx.getDwmc());
            this.setJinE(fse);
        }});
        StCommonUnit finalDwxx1 = dwxx;
        DFSJ.add(new VoucherAmount() {{
            this.setZhaiYao("汇补缴" + finalDwxx1.getDwmc());
            this.setJinE(fse);
        }});
        try {
            VoucherRes voucherRes = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动", "", "",
                    VoucherBusinessType.汇补缴_有单位无流水.getCode(), VoucherBusinessType.汇补缴_有单位无流水.getCode(), no,
                    JFSJ, DFSJ, String.valueOf(1), acct, null);
            StCollectionUnitAccount account = dwxx.getCollectionUnitAccount();
            BigDecimal zsye = account.getExtension().getZsye();
            account.getExtension().setZsye(zsye.add(fse));
            DAOBuilder.instance(iStCollectionUnitAccountDAO).entity(account).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            CFinanceRecordUnit cFinanceRecordUnit = new CFinanceRecordUnit();
            cFinanceRecordUnit.setFse(fse);
            cFinanceRecordUnit.setJzpzh(voucherRes.getJZPZH());
            cFinanceRecordUnit.setRemark(remark);
            cFinanceRecordUnit.setSummary(zhaiyao);
            cFinanceRecordUnit.setZjly(WFTLY.汇补缴.getName());
            cFinanceRecordUnit.setDwzh(account.getDwzh());
            cFinanceRecordUnit.setWftye(account.getExtension().getZsye());
            icFinanceRecordUnitDAO.save(cFinanceRecordUnit);

        } catch (Exception e) {
            System.out.println("生成凭证记录：" + e.getMessage());
        }

    }

    @Override
    public void transferAccounts(AccChangeNotice accChangeNotice) {
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            throw new ErrorException(ReturnEnumeration.Business_In_Process, "结算平台数据错误");
        }
        String zhaiyao = accChangeNoticeFile.getSummary();//摘要
        String YWLSH = accChangeNoticeFile.getNo();//业务流水号
        BigDecimal fse = accChangeNoticeFile.getAmt();//发生额

        CFinanceBusinessProcess cFinanceBusinessProcess = DAOBuilder.instance(financeBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(YWLSH)) {
                this.put("ywlsh", YWLSH);
            }
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cFinanceBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务缺失");
        }
        CFinanceDailyBusinessVice vice = cFinanceBusinessProcess.getcFinanceDailyBusinessVice();
        Map content = gson.fromJson(vice.getYwsj(), Map.class);

        String zy = zhaiyao;
        if (content.containsKey("ZY")) {
            zy = (String) content.get("ZY");
        } else if (content.containsKey("BZ")) {
            zy = (String) content.get("BZ");
        }

        List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
        List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        List<Object> res = gson.fromJson(vice.getBlzl(), type);
        int djsl = 2;
        for (Object o : res) {
            Map map = gson.fromJson(o.toString(), Map.class);
            String data = map.get("data").toString();
            List<Object> os = gson.fromJson(data, type);
            djsl += os.size();
        }
        General general = FinanceBaseService.gson.fromJson(vice.getYwsj(), General.class);
        List<TableRow> tableRows = general.getTableRows();
        for (TableRow tableRow : tableRows) {
            BigDecimal df = new BigDecimal(tableRow.getDebitAmount());//借方金额
            if (df.compareTo(BigDecimal.ZERO) == 0) {
                VoucherAmount amount = new VoucherAmount();
                String credit = tableRow.getCreditAmount();
                if (NumberUtils.isNumber(credit)) {
                    amount.setJinE(new BigDecimal(credit).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    amount.setJinE(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                amount.setZhaiYao(StringUtil.notEmpty(zy) ? zy : tableRow.getRowName());
                amount.setRemark(tableRow.getRowName());
                DFSJ.add(amount);
            } else {
                VoucherAmount amount = new VoucherAmount();
                String debit = tableRow.getDebitAmount();
                if (NumberUtils.isNumber(debit)) {
                    amount.setJinE(new BigDecimal(debit).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    amount.setJinE(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                amount.setZhaiYao(StringUtil.notEmpty(zy) ? zy : tableRow.getRowName());
                amount.setRemark(tableRow.getRowName());
                JFSJ.add(amount);
            }
        }
        VoucherRes voucherRes = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", cFinanceBusinessProcess.getCzy(), cFinanceBusinessProcess.getZhclry(), "", "",
                vice.getYwmcid(), vice.getMbbh(), YWLSH,
                JFSJ, DFSJ, String.valueOf(djsl), vice.getZjywlx(), accChangeNotice, null);

        if (voucherRes.getJZPZH() != null) {
            financeBaseService.updateStatus(YWLSH, "已入账", "");
            financeBaseService.savaToBase(YWLSH, voucherRes.getJZPZH());
        } else {
            financeBaseService.updateStatus(YWLSH, "入账失败", voucherRes.getMSG());
        }

    }

    @Override
    public void transferAccountsFail(AccChangeNotice accChangeNotice) {
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            throw new ErrorException(ReturnEnumeration.Business_In_Process, "结算平台数据错误");
        }
        String YWLSH = accChangeNoticeFile.getNo();//业务流水号

        financeBaseService.updateStatus(YWLSH, "入账失败", "");
        iVoucherManagerService.rehedgeVoucher(YWLSH);
    }

    @Override
    public void addexternalTransferAccounts(AccChangeNotice accChangeNotice) {
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        if (accChangeNoticeFile == null) {
            throw new ErrorException(ReturnEnumeration.Business_In_Process, "结算平台数据错误");
        }
        String zhaiyao = accChangeNoticeFile.getSummary();//摘要，身份证号码
        BigDecimal fse = accChangeNoticeFile.getAmt();//发生额
        String remark = accChangeNoticeFile.getRemark();//备注
        String date = accChangeNoticeFile.getDate();//交易日期
        String no = accChangeNoticeFile.getNo();
        Date jy_date = DateUtil.safeStr2Date("yyyyMMdd", date);
        String opponentName = accChangeNoticeFile.getOpponentName();//交易对手户名
        String opponentAcct = accChangeNoticeFile.getOpponentAcct();//交易对手账号

        StCommonPerson person = DAOBuilder.instance(iStCommonPersonDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("zjhm", zhaiyao);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (person == null) {
            //进入暂收
            String acct = accChangeNoticeFile.getAcct();//银行专户号码
            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
            int djsl = 1;//单据数量
            CenterAccountInfo centerAccountInfo = settlementSpecialBankAccountManageService.getSpecialAccountByZHHM(acct);
            String kmbh;
            if (centerAccountInfo != null) {
                kmbh = centerAccountInfo.getKMBH();
            } else {
                kmbh = "101";
            }
            JFSJ.add(new VoucherAmount() {{
                this.setRemark(kmbh);
                this.setZhaiYao("收到暂收");
                this.setJinE(fse);
            }});
            DFSJ.add(new VoucherAmount() {{
                this.setZhaiYao("收到暂收");
                this.setJinE(fse);
            }});
            VoucherRes voucherRes = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动", "", "",
                    VoucherBusinessType.收到暂收.getCode(), VoucherBusinessType.收到暂收.getCode(), no,
                    JFSJ, DFSJ, String.valueOf(djsl), acct, null);

            //暂收记录
            String SKHM;
            if (centerAccountInfo == null) {
                SKHM = "未知专户账号";
            } else {
                SKHM = centerAccountInfo.getYHZHMC();
            }
            iVoucherManagerService.addTemporaryRecord(acct, SKHM, opponentAcct, opponentName, fse, jy_date, voucherRes.getJZPZH(), zhaiyao, remark);

        } else {
            StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();
            personalAccount.setGrzhye(personalAccount.getGrzhye().add(fse));
            DAOBuilder.instance(iStCollectionPersonalAccountDAO).entity(personalAccount).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            StCommonUnit unit = person.getUnit();
            StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
            unitAccount.setDwzhye(unitAccount.getDwzhye().add(fse));
            DAOBuilder.instance(iStCollectionUnitAccountDAO).entity(unitAccount).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

            VoucherAmount jfamount = new VoucherAmount();
            jfamount.setJinE(fse);
            jfamount.setZhaiYao(person.getXingMing() + " 外部转入");
            JFSJ.add(jfamount);

            VoucherAmount dfamount = new VoucherAmount();
            dfamount.setJinE(fse);
            dfamount.setZhaiYao("外部转入 " + unit.getDwmc());
            DFSJ.add(dfamount);

            accChangeNoticeFile.setSummary(person.getXingMing() + " 外部转入");
            accChangeNotice.setAccChangeNoticeFile(accChangeNoticeFile);
            VoucherRes voucherRes = iVoucherManagerService.addVoucher("毕节市住房公积金管理中心", "自动", "自动", "",
                    "", VoucherBusinessType.外部转入.getCode(), VoucherBusinessType.外部转入.getCode(), accChangeNoticeFile.getNo(), JFSJ, DFSJ, "1", accChangeNotice, null);

        }
    }

}

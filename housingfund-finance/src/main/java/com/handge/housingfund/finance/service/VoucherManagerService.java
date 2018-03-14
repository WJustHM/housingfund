package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.finance.IAccountBookService;
import com.handge.housingfund.common.service.finance.IBusinessClassifySetService;
import com.handge.housingfund.common.service.finance.IFinanceReportService;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.BankToSubject;
import com.handge.housingfund.finance.utils.FinanceComputeHelper;
import com.handge.housingfund.finance.utils.SubjectHelper;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tanyi on 2017/8/24.
 */
@Component
public class VoucherManagerService implements IVoucherManagerService {

    public static String format = "yyyy-MM-dd";

    @Autowired
    IStSettlementSpecialBankAccountDAO specialBankAccountDAO;

    @Autowired
    private IStFinanceRecordingVoucherDAO financeRecordingVoucherDAO;

    @Autowired
    private ICBankAccChangeNoticeDAO changeNoticeDAO;

    @Autowired
    private ICFinanceBusinessVoucherSetsDAO financeBusinessVoucherSetsDAO;

    @Autowired
    private ICFinanceAccountPeriodDAO icFinanceAccountPeriodDAO;

    @Autowired
    private IStFinanceSubjectsDAO stFinanceSubjectsDAO;

    @Autowired
    private IUpdateSFSYService<CFinanceBusinessVoucherSets, ICFinanceBusinessVoucherSetsDAO> updateSFSYService;

    @Autowired
    private ICFinanceTemporaryRecordDAO icFinanceTemporaryRecordDAO;

    @Autowired
    private IFinanceReportService iFinanceReportService;

    @Autowired
    private ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;

    @Autowired
    private IPdfService pdfService;

    @Autowired
    private IBusinessClassifySetService iBusinessClassifySetService;

    @Autowired
    private IAccountBookService iAccountBookService;

    @Autowired
    private IStFinanceSubsidiaryAccountsDAO iStFinanceSubsidiaryAccountsDAO;

    @Autowired
    private IStFinanceGeneralLedgerDAO iStFinanceGeneralLedgerDAO;

    // 结账前必须做的凭证 ：//计提公积金利息//计提个人贷款手续费//期末结转业务收入//期末结转业务支出
    List<String> list = Arrays.asList("017", "024", "029", "030");

    /**
     * 生成凭证 有到账通知，凭证模板中借方或贷方科目唯一，有模板编号
     *
     * @param HSDW            核算单位
     * @param JiZhang         记账
     * @param FuHe            复核
     * @param ChuNa           出纳
     * @param ZhiDan          制单
     * @param MBBH            模板编号
     * @param CZNR            操作内容
     * @param accChangeNotice 结算平台回执单
     * @return
     */
    @Override
    public VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan, String MBBH, String CZNR, AccChangeNotice accChangeNotice, String YHDM) {

        try {
            AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
            if (accChangeNoticeFile == null) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "账户变动详情不存在");
            }
            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
            int djsl = 2;//单据数量

            JFSJ.add(new VoucherAmount() {{
                this.setZhaiYao(accChangeNoticeFile.getSummary());
                this.setJinE(accChangeNoticeFile.getAmt().abs());
            }});
            DFSJ.add(new VoucherAmount() {{
                this.setZhaiYao(accChangeNoticeFile.getSummary());
                this.setJinE(accChangeNoticeFile.getAmt().abs());
            }});
            return addVoucher(HSDW, JiZhang, FuHe, ChuNa, ZhiDan, MBBH, CZNR, accChangeNoticeFile.getNo(),
                    JFSJ, DFSJ, String.valueOf(djsl), accChangeNotice, YHDM);
        } catch (Exception e) {
            System.out.println("生成凭证记录：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new VoucherRes(null, e.getMessage());
        }

    }

    /**
     * 生成凭证 无到账通知，凭证模板中借方或贷方科目唯一，有模板编号
     *
     * @param HSDW    核算单位
     * @param JiZhang 记账
     * @param FuHe    复核
     * @param ChuNa   出纳
     * @param ZhiDan  制单
     * @param MBBH    模板编号
     * @param CZNR    操作内容
     * @param YWLSH   业务流水号
     * @param ZhaiYao 摘要
     * @param FSE     发生额
     * @param YHZHH   没有则传null
     * @return
     */
    @Override
    public VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa,
                                 String ZhiDan, String MBBH, String CZNR, String YWLSH, String ZhaiYao, BigDecimal FSE, String YHZHH, String YHDM) {
        try {
            List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
            List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

            JFSJ.add(new VoucherAmount() {{
                this.setZhaiYao(ZhaiYao);
                this.setJinE(FSE.abs());
            }});
            DFSJ.add(new VoucherAmount() {{
                this.setZhaiYao(ZhaiYao);
                this.setJinE(FSE.abs());
            }});
            return addVoucher("毕节市住房公积金管理中心", JiZhang, FuHe, "", ZhiDan, MBBH, CZNR, YWLSH, JFSJ, DFSJ, "1", YHZHH, YHDM);
        } catch (Exception e) {
            System.out.println("生成凭证记录：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new VoucherRes(null, e.getMessage());
        }

    }

    /**
     * 生成凭证 有到账通知，凭证模板中借方或贷方科目不唯一，有科目编号
     *
     * @param HSDW            核算单位
     * @param JiZhang         记账
     * @param FuHe            复核
     * @param ChuNa           出纳
     * @param ZhiDan          制单
     * @param MBBH            模板编号
     * @param CZNR            操作内容
     * @param YWLSH           业务流水号
     * @param JFSJ            借方数据
     * @param DFSJ            贷方数据
     * @param DJSL            单据数量
     * @param accChangeNotice
     * @return
     */
    @Override
    public VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan,
                                 String MBBH, String CZNR, String YWLSH, List<VoucherAmount> JFSJ,
                                 List<VoucherAmount> DFSJ, String DJSL, AccChangeNotice accChangeNotice, String YHDM) {
        try {
            AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
            if (accChangeNoticeFile == null) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "账户变动详情不存在");
            }
            CFinanceBusinessVoucherSets cFinanceBusinessVoucherSets = DAOBuilder.instance(financeBusinessVoucherSetsDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("mbbh", MBBH);
            }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            updateSFSYService.updateSFSY(new CFinanceBusinessVoucherSets(), financeBusinessVoucherSetsDAO, cFinanceBusinessVoucherSets.getId());

            return addVoucher(HSDW, JiZhang, FuHe, ChuNa, ZhiDan, CZNR, cFinanceBusinessVoucherSets.getYwmcid(), YWLSH,
                    cFinanceBusinessVoucherSets.getJfkm(), cFinanceBusinessVoucherSets.getDfkm(),
                    JFSJ, DFSJ, DJSL, cFinanceBusinessVoucherSets.getYwmc(), accChangeNoticeFile.getAcct(), YHDM);
        } catch (Exception e) {
            System.out.println("生成凭证记录：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new VoucherRes(null, e.getMessage());
        }

    }

    @Override
    public VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan,
                                 String YWID, String CZNR, String YWLSH, List<VoucherAmount> JFSJ,
                                 List<VoucherAmount> DFSJ, String DJSL, String YWMC, AccChangeNotice accChangeNotice, String YHDM) {
        try {
            AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
            if (accChangeNoticeFile == null) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "账户变动详情不存在");
            }
            CFinanceBusinessVoucherSets cFinanceBusinessVoucherSets = DAOBuilder.instance(financeBusinessVoucherSetsDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("ywmcid", YWID);
            }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            updateSFSYService.updateSFSY(new CFinanceBusinessVoucherSets(), financeBusinessVoucherSetsDAO, cFinanceBusinessVoucherSets.getId());

            return addVoucher(HSDW, JiZhang, FuHe, ChuNa, ZhiDan, CZNR, YWID, YWLSH,
                    cFinanceBusinessVoucherSets.getJfkm(), cFinanceBusinessVoucherSets.getDfkm(),
                    JFSJ, DFSJ, DJSL, cFinanceBusinessVoucherSets.getYwmc(), accChangeNoticeFile.getAcct(), YHDM);
        } catch (Exception e) {
            System.out.println("生成凭证记录：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new VoucherRes(null, e.getMessage());
        }
    }

    /**
     * 生成凭证 无到账通知，凭证模板中借方或贷方科目不唯一，有模板编号
     *
     * @param HSDW    核算单位
     * @param JiZhang 记账
     * @param FuHe    复核
     * @param ChuNa   出纳
     * @param ZhiDan  制单
     * @param MBBH    模板编号
     * @param CZNR    操作内容
     * @param YWLSH   业务流水号
     * @param JFSJ    借方数据
     * @param DFSJ    贷方数据
     * @param DJSL    单据数量
     * @param YHZHH   业务名称
     * @return
     */
    @Override
    public VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan, String MBBH, String CZNR,
                                 String YWLSH, List<VoucherAmount> JFSJ, List<VoucherAmount> DFSJ, String DJSL, String YHZHH, String YHDM) {
        try {
            CFinanceBusinessVoucherSets cFinanceBusinessVoucherSets = DAOBuilder.instance(financeBusinessVoucherSetsDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("mbbh", MBBH);
            }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            updateSFSYService.updateSFSY(new CFinanceBusinessVoucherSets(), financeBusinessVoucherSetsDAO, cFinanceBusinessVoucherSets.getId());

            return addVoucher(HSDW, JiZhang, FuHe, ChuNa, ZhiDan, CZNR, cFinanceBusinessVoucherSets.getYwmcid(), YWLSH, cFinanceBusinessVoucherSets.getJfkm(), cFinanceBusinessVoucherSets.getDfkm(),
                    JFSJ, DFSJ, DJSL, cFinanceBusinessVoucherSets.getYwmc(), YHZHH, YHDM);
        } catch (Exception e) {
            System.out.println("生成凭证记录：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new VoucherRes(null, e.getMessage());
        }
    }


    /**
     * 生成凭证 无到账通知，凭证模板中借方或贷方科目不唯一，没有科目编号
     *
     * @param HSDW    核算单位
     * @param JiZhang 记账
     * @param FuHe    复核
     * @param ChuNa   出纳
     * @param ZhiDan  制单
     * @param CZNR    操作内容
     * @param YWLSH   业务流水号
     * @param JFKM    借方科目
     * @param DFKM    贷方科目
     * @param JFSJ    借方数据
     * @param DFSJ    贷方数据
     * @param DJSL    单据数量
     * @param YWMC    业务名称
     * @return
     */
    @Override
    public VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa,
                                 String ZhiDan, String CZNR, String YWID, String YWLSH, String JFKM,
                                 String DFKM, List<VoucherAmount> JFSJ, List<VoucherAmount> DFSJ,
                                 String DJSL, String YWMC, String YHZHH, String YHDM) {
        AccountBookModel accountBookModel = iAccountBookService.getAccountBookList();
        HSDW = "毕节市住房公积金管理中心";
        String CWZG;
        if (accountBookModel != null) {
            CWZG = accountBookModel.getKJZG();
        } else {
            CWZG = "账套未设置财务主管";
        }
        ZhiDan = CWZG;
        StFinanceRecordingVoucher stFinanceRecordingVoucher;
        if (StringUtil.notEmpty(YWLSH)) {
            stFinanceRecordingVoucher = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("cFinanceRecordingVoucherExtension.ywlsh", YWLSH);
                this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
            }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (stFinanceRecordingVoucher != null) {
//                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "生成凭证记录：业务流水号、\"" + YWLSH + "\"已生成凭证");
                HashMap<String, Object> filter = new HashMap<>();
                if (YWLSH.length() == 32) filter.put("id", YWLSH);
                else filter.put("bus_seq_no", YWLSH);
                List<CBankAccChangeNotice> cBankAccChangeNotices = DAOBuilder.instance(changeNoticeDAO).searchFilter(filter).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension();
                BigDecimal djsl = stFinanceRecordingVoucher.getFjdjs();
                if (cBankAccChangeNotices.size() > 0) {
                    djsl = djsl.add(new BigDecimal(cBankAccChangeNotices.size() - 1));
                }
                stFinanceRecordingVoucher.setFjdjs(djsl);//附件单据数
                cFinanceRecordingVoucherExtension.setcBankAccChangeNotices(cBankAccChangeNotices);

                DAOBuilder.instance(financeRecordingVoucherDAO).entity(stFinanceRecordingVoucher).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNotices) {
                    if (cBankAccChangeNotice != null) {
                        cBankAccChangeNotice.setJzpzh(stFinanceRecordingVoucher.getJzpzh());
                        cBankAccChangeNotice.setIs_make_acc("1");
                        try {
                            //写日记账和流水表
                            addBankRecord(cBankAccChangeNotice, stFinanceRecordingVoucher.getJzpzh(), YWID);
                        } catch (Exception e) {
                            System.out.println("生成凭证记录：生成日记账和流水" + e.getMessage());
                        }
                    } else {
                        cBankAccChangeNotices.remove(null);
                    }
                }
                DAOBuilder.instance(changeNoticeDAO).entities(cBankAccChangeNotices).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                return new VoucherRes(stFinanceRecordingVoucher.getJzpzh(), null);
            } else {
                stFinanceRecordingVoucher = new StFinanceRecordingVoucher();
            }
        } else {
            stFinanceRecordingVoucher = new StFinanceRecordingVoucher();
        }

        List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(stFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        List<SubjectModel> jfsubjectModels = SubjectHelper.getSubjectByJson(JFKM);
        if (jfsubjectModels == null || jfsubjectModels.size() <= 0) {
            jfsubjectModels = new ArrayList<>();
            for (VoucherAmount amount : JFSJ) {
                SubjectModel model = SubjectHelper.getSubjectByKMMC(stFinanceSubjects, amount.getRemark());
                if (model != null) {
                    jfsubjectModels.add(model);
                }
            }
            if (jfsubjectModels.size() <= 0) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务凭证模板不存在");
            }
        }

        List<SubjectModel> dfsubjectModels = SubjectHelper.getSubjectByJson(DFKM);
        if (dfsubjectModels == null || dfsubjectModels.size() <= 0) {
            dfsubjectModels = new ArrayList<>();
            for (VoucherAmount amount : DFSJ) {
                SubjectModel model = SubjectHelper.getSubjectByKMMC(stFinanceSubjects, amount.getRemark());
                if (model != null) {
                    dfsubjectModels.add(model);
                }
            }
            if (dfsubjectModels.size() <= 0) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务凭证模板不存在");
            }
        }

        CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension = new CFinanceRecordingVoucherExtension();
        stFinanceRecordingVoucher.setZhaiYao(JFSJ.get(0).getZhaiYao());//摘要

        stFinanceRecordingVoucher.setKmbh(getKMBH(jfsubjectModels.get(0).getKMBH(), YHZHH, YHDM));//科目编号
        stFinanceRecordingVoucher.setJffse(JFSJ.get(0).getJinE().abs());//借方发生额
        stFinanceRecordingVoucher.setDffse(BigDecimal.ZERO);//贷方发生额

        cFinanceRecordingVoucherExtension.setJfhj(BigDecimal.ZERO);//借方合计
        cFinanceRecordingVoucherExtension.setDfhj(BigDecimal.ZERO);//贷方合计

        //region  记账日期，某些业务需要将帐入到上个月最后一天，最后一秒
        Date JZRQ = new Date();
        Date created_at = new Date();
        if (list.contains(CZNR)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(JZRQ);
            calendar.add(Calendar.MONTH, -1);

            String KJQJUP = DateUtil.date2Str(calendar.getTime(), "yyyyMM");

            CFinanceAccountPeriod cFinanceAccountPeriodold = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("kjqj", KJQJUP);
                }
            }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cFinanceAccountPeriodold != null) {
                if (!cFinanceAccountPeriodold.isSfjs()) {
                    int MaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), MaxDay, 23, 59, 58);
                    created_at = calendar.getTime();
                    try {
                        JZRQ = DateUtil.str2Date("yyyy-MM-dd", DateUtil.date2Str(calendar.getTime(), "yyyy-MM-dd"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //endregion
        Date jzrq = JZRQ;
        try {
            jzrq = DateUtil.str2Date("yyyyMMdd", DateUtil.date2Str(jzrq, "yyyyMMdd"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        stFinanceRecordingVoucher.setJzrq(jzrq);//记账日期
        cFinanceRecordingVoucherExtension.setHsdw(HSDW);//核算单位
        cFinanceRecordingVoucherExtension.setCwzg(CWZG);//财务主管
        cFinanceRecordingVoucherExtension.setJizhang(JiZhang);//记账
        cFinanceRecordingVoucherExtension.setFuhe(FuHe);//复核
        cFinanceRecordingVoucherExtension.setChuna(ChuNa);//出纳
        cFinanceRecordingVoucherExtension.setZhidan(ZhiDan);//制单
        cFinanceRecordingVoucherExtension.setKmyefx(jfsubjectModels.get(0).getKMYEFX());
        cFinanceRecordingVoucherExtension.setCreated_at(created_at);

        HashMap<String, Object> filter = new HashMap<>();
        if (YWLSH.length() == 32) filter.put("id", YWLSH);
        else filter.put("bus_seq_no", YWLSH);
        List<CBankAccChangeNotice> cBankAccChangeNotices = DAOBuilder.instance(changeNoticeDAO).searchFilter(filter).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        BigDecimal djsl = new BigDecimal(DJSL);
        if (cBankAccChangeNotices.size() > 0) {
            djsl = djsl.add(new BigDecimal(cBankAccChangeNotices.size() - 1));
        }
        stFinanceRecordingVoucher.setFjdjs(djsl);//附件单据数
        stFinanceRecordingVoucher.setCreated_at(created_at);
        cFinanceRecordingVoucherExtension.setcBankAccChangeNotices(cBankAccChangeNotices);
        cFinanceRecordingVoucherExtension.setYwlsh(YWLSH.length() == 32 ? null : YWLSH);//业务流水号
        cFinanceRecordingVoucherExtension.setCznr(CZNR);//操作内容
        cFinanceRecordingVoucherExtension.setSfzjl(true);//是否主记录
        cFinanceRecordingVoucherExtension.setYwmc(YWMC);//业务名称
        cFinanceRecordingVoucherExtension.setSfjz(false);

        HashMap<String, String> ywlxh = iBusinessClassifySetService.getFundBusinessType(YWID);
        cFinanceRecordingVoucherExtension.setYwlx(ywlxh.get("zjywlxbm"));

        stFinanceRecordingVoucher.setcFinanceRecordingVoucherExtension(cFinanceRecordingVoucherExtension);

        stFinanceRecordingVoucher = DAOBuilder.instance(financeRecordingVoucherDAO).entity(stFinanceRecordingVoucher).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        String jzpzh = stFinanceRecordingVoucher.getJzpzh();
        List<StFinanceSubsidiaryAccounts> SubsidiaryAccounts = new ArrayList<>();

        //region 生成明细账
        StFinanceSubsidiaryAccounts stFinanceSubsidiaryAccounts = new StFinanceSubsidiaryAccounts();
        stFinanceSubsidiaryAccounts.setDffse(BigDecimal.ZERO);
        stFinanceSubsidiaryAccounts.setJffse(JFSJ.get(0).getJinE().abs());
        stFinanceSubsidiaryAccounts.setJzpzh(jzpzh);
        stFinanceSubsidiaryAccounts.setJzrq(jzrq);
        stFinanceSubsidiaryAccounts.setKmbh(getKMBH(jfsubjectModels.get(0).getKMBH(), YHZHH, YHDM));
        stFinanceSubsidiaryAccounts.setQcye(BigDecimal.ZERO);
        stFinanceSubsidiaryAccounts.setQcyefx(cFinanceRecordingVoucherExtension.getKmyefx());
        stFinanceSubsidiaryAccounts.setQmye(BigDecimal.ZERO);
        stFinanceSubsidiaryAccounts.setQmyefx(cFinanceRecordingVoucherExtension.getKmyefx());
        stFinanceSubsidiaryAccounts.setZhaiYao(stFinanceRecordingVoucher.getZhaiYao());
        stFinanceSubsidiaryAccounts.setCreated_at(created_at);
        SubsidiaryAccounts.add(stFinanceSubsidiaryAccounts);
        //endregion

        List<StFinanceRecordingVoucher> financeRecordingVouchers = new ArrayList<>();

        BigDecimal JFHJ = JFSJ.get(0).getJinE();
        BigDecimal DFHJ = new BigDecimal("0.00");

        for (int i = 1; i < JFSJ.size(); i++) {
            VoucherAmount amount = JFSJ.get(i);
            StFinanceRecordingVoucher temp = new StFinanceRecordingVoucher();
            CFinanceRecordingVoucherExtension tempe = new CFinanceRecordingVoucherExtension();
            temp.setJzpzh(jzpzh);
            temp.setZhaiYao(amount.getZhaiYao());
            temp.setKmbh(getKMBH(jfsubjectModels.get(i).getKMBH(), YHZHH, YHDM));//科目编号
            temp.setJffse(amount.getJinE().abs());//借方发生额
            temp.setDffse(BigDecimal.ZERO);//贷方发生额
            temp.setFjdjs(new BigDecimal(DJSL));//附件单据数
            temp.setJzrq(jzrq);
            tempe.setSfzjl(false);
            tempe.setFuhe(FuHe);
            tempe.setHsdw(HSDW);
            tempe.setCznr(CZNR);
            tempe.setCwzg(CWZG);
            tempe.setChuna(ChuNa);
            tempe.setYwlsh(YWLSH.length() == 32 ? null : YWLSH);
            tempe.setSfjz(false);
            tempe.setKmyefx(jfsubjectModels.get(i).getKMYEFX());
            tempe.setJizhang(JiZhang);
            tempe.setYwlx(ywlxh.get("zjywlxbm"));
            tempe.setYwmc(YWMC);
            tempe.setZhidan(ZhiDan);
            tempe.setCreated_at(created_at);
            temp.setcFinanceRecordingVoucherExtension(tempe);
            temp.setCreated_at(created_at);
            financeRecordingVouchers.add(temp);
            JFHJ = JFHJ.add(amount.getJinE().abs());

            //region 生成明细账
            StFinanceSubsidiaryAccounts accounts = new StFinanceSubsidiaryAccounts();
            accounts.setDffse(BigDecimal.ZERO);
            accounts.setJffse(amount.getJinE().abs());
            accounts.setJzpzh(jzpzh);
            accounts.setJzrq(jzrq);
            accounts.setKmbh(getKMBH(jfsubjectModels.get(i).getKMBH(), YHZHH, YHDM));
            accounts.setQcye(BigDecimal.ZERO);
            accounts.setQcyefx(jfsubjectModels.get(i).getKMYEFX());
            accounts.setQmye(BigDecimal.ZERO);
            accounts.setQmyefx(jfsubjectModels.get(i).getKMYEFX());
            accounts.setZhaiYao(amount.getZhaiYao());
            accounts.setCreated_at(created_at);
            SubsidiaryAccounts.add(accounts);
            //endregion

        }

        for (int i = 0; i < DFSJ.size(); i++) {
            VoucherAmount amount = DFSJ.get(i);
            StFinanceRecordingVoucher temp = new StFinanceRecordingVoucher();
            CFinanceRecordingVoucherExtension tempe = new CFinanceRecordingVoucherExtension();
            temp.setJzpzh(jzpzh);
            temp.setZhaiYao(amount.getZhaiYao());
            temp.setKmbh(getKMBH(dfsubjectModels.get(i).getKMBH(), YHZHH, YHDM));//科目编号
            temp.setJffse(BigDecimal.ZERO);//借方发生额
            temp.setDffse(amount.getJinE().abs());//贷方发生额
            temp.setFjdjs(new BigDecimal(DJSL));//附件单据数
            temp.setJzrq(jzrq);
            tempe.setSfzjl(false);
            tempe.setFuhe(FuHe);
            tempe.setHsdw(HSDW);
            tempe.setCznr(CZNR);
            tempe.setCwzg(CWZG);
            tempe.setChuna(ChuNa);
            tempe.setYwlsh(YWLSH.length() == 32 ? null : YWLSH);
            tempe.setSfjz(false);
            tempe.setKmyefx(dfsubjectModels.get(i).getKMYEFX());
            tempe.setJizhang(JiZhang);
            tempe.setYwlx(ywlxh.get("zjywlxbm"));
            tempe.setYwmc(YWMC);
            tempe.setZhidan(ZhiDan);
            tempe.setCreated_at(created_at);
            temp.setcFinanceRecordingVoucherExtension(tempe);
            temp.setCreated_at(created_at);
            financeRecordingVouchers.add(temp);
            DFHJ = DFHJ.add(amount.getJinE().abs());

            //region 生成明细账
            StFinanceSubsidiaryAccounts accounts = new StFinanceSubsidiaryAccounts();
            accounts.setDffse(amount.getJinE().abs());
            accounts.setJffse(BigDecimal.ZERO);
            accounts.setJzpzh(jzpzh);
            accounts.setJzrq(jzrq);
            accounts.setKmbh(getKMBH(dfsubjectModels.get(i).getKMBH(), YHZHH, YHDM));
            accounts.setQcye(BigDecimal.ZERO);
            accounts.setQcyefx(dfsubjectModels.get(i).getKMYEFX());
            accounts.setQmye(BigDecimal.ZERO);
            accounts.setQmyefx(dfsubjectModels.get(i).getKMYEFX());
            accounts.setZhaiYao(amount.getZhaiYao());
            accounts.setCreated_at(created_at);
            SubsidiaryAccounts.add(accounts);
            //endregion

        }

        CFinanceRecordingVoucherExtension extension = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension();
        JFHJ = JFHJ.setScale(2, BigDecimal.ROUND_HALF_UP);
        DFHJ = DFHJ.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (DFHJ.compareTo(JFHJ) != 0) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "借贷不平衡");
        }
        extension.setJfhj(JFHJ);
        extension.setDfhj(DFHJ);
        stFinanceRecordingVoucher.setcFinanceRecordingVoucherExtension(extension);
        financeRecordingVouchers.add(stFinanceRecordingVoucher);

        DAOBuilder.instance(financeRecordingVoucherDAO).entities(financeRecordingVouchers).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNotices) {
            if (cBankAccChangeNotice != null) {
                cBankAccChangeNotice.setJzpzh(jzpzh);
                cBankAccChangeNotice.setIs_make_acc("1");
                try {
                    //写日记账和流水表
                    addBankRecord(cBankAccChangeNotice, jzpzh, YWID);
                } catch (Exception e) {
                    System.out.println("生成凭证记录：生成日记账和流水" + e.getMessage());
                }
            } else {
                cBankAccChangeNotices.remove(null);
            }
        }
        DAOBuilder.instance(changeNoticeDAO).entities(cBankAccChangeNotices).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        DAOBuilder.instance(iStFinanceSubsidiaryAccountsDAO).entities(SubsidiaryAccounts).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        System.out.println("生成凭证记录：" + jzpzh);
        return new VoucherRes(jzpzh, null);
    }

    @Override
    public VoucherRes reVoucher(String YWLSH, List<VoucherAmount> JFSJ, List<VoucherAmount> DFSJ, String DJSL, String YHZHH, String YHDM) {
        StFinanceRecordingVoucher stFinanceRecordingVoucher = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("cFinanceRecordingVoucherExtension.ywlsh", YWLSH);
            this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new VoucherRes(stFinanceRecordingVoucher.getJzpzh(), null);
    }

    @Override
    public void delVoucher(String YWLSH) {
        if (StringUtil.notEmpty(YWLSH)) {
            List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("cFinanceRecordingVoucherExtension.ywlsh", YWLSH);
            }}).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (stFinanceRecordingVouchers != null && stFinanceRecordingVouchers.size() > 0) {
                for (StFinanceRecordingVoucher voucher : stFinanceRecordingVouchers) {
                    voucher.setDffse(BigDecimal.ZERO);
                    voucher.setJffse(BigDecimal.ZERO);
                    voucher.setZhaiYao(voucher.getZhaiYao() + "已删除");
                    CFinanceRecordingVoucherExtension extension = voucher.getcFinanceRecordingVoucherExtension();
                    extension.setDfhj(BigDecimal.ZERO);
                    extension.setJfhj(BigDecimal.ZERO);
                    voucher.setcFinanceRecordingVoucherExtension(extension);
                }
                DAOBuilder.instance(financeRecordingVoucherDAO).entities(stFinanceRecordingVouchers).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                //region 删除明细账
                List<StFinanceSubsidiaryAccounts> stFinanceSubsidiaryAccounts = DAOBuilder.instance(iStFinanceSubsidiaryAccountsDAO).searchFilter(new HashMap<String, Object>() {{
                    this.put("jzpzh", stFinanceRecordingVouchers.get(0).getJzpzh());
                }}).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                for (StFinanceSubsidiaryAccounts accounts : stFinanceSubsidiaryAccounts) {
                    accounts.setZhaiYao(accounts.getZhaiYao() + "已删除");
                    accounts.setDffse(BigDecimal.ZERO);
                    accounts.setJffse(BigDecimal.ZERO);
                }
                DAOBuilder.instance(iStFinanceSubsidiaryAccountsDAO).entities(stFinanceSubsidiaryAccounts).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                //endregion

                //region 删除到账通知关联
                HashMap<String, Object> filter = new HashMap<>();
                filter.put("jzpzh", stFinanceRecordingVouchers.get(0).getJzpzh());
                List<CBankAccChangeNotice> cBankAccChangeNotices = DAOBuilder.instance(changeNoticeDAO).searchFilter(filter).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNotices) {
                    if (cBankAccChangeNotice != null) {
                        cBankAccChangeNotice.setJzpzh(null);
                        cBankAccChangeNotice.setIs_make_acc("0");
                        cBankAccChangeNotice.setcFinanceRecordingVoucherExtension(null);
                    }
                }
                DAOBuilder.instance(changeNoticeDAO).entities(cBankAccChangeNotices).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                //endregion
            }
        } else {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务流水号为空");
        }
    }

    @Override
    public VoucherRes rehedgeVoucher(String YWLSH) {
        if (!StringUtil.notEmpty(YWLSH)) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务流水号为空");
        }
        List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("cFinanceRecordingVoucherExtension.ywlsh", YWLSH);
        }}).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stFinanceRecordingVouchers != null && stFinanceRecordingVouchers.size() > 0) {
            int jfnum = 0;
            for (StFinanceRecordingVoucher voucher : stFinanceRecordingVouchers) {
                if (voucher.getJffse().compareTo(BigDecimal.ZERO) > 0) {
                    jfnum++;
                }
            }
            String kmbh = "";
            String kmyefx = "";
            for (StFinanceRecordingVoucher voucher : stFinanceRecordingVouchers) {
                if (jfnum == 1) {
                    if (voucher.getJffse().compareTo(BigDecimal.ZERO) > 0) {
                        kmbh = voucher.getKmbh();
                        kmyefx = voucher.getcFinanceRecordingVoucherExtension().getKmyefx();
                    }
                } else {
                    if (voucher.getDffse().compareTo(BigDecimal.ZERO) > 0) {
                        kmbh = voucher.getKmbh();
                        kmyefx = voucher.getcFinanceRecordingVoucherExtension().getKmyefx();
                    }
                }
            }
            for (StFinanceRecordingVoucher voucher : stFinanceRecordingVouchers) {
                voucher.setKmbh(kmbh);
                voucher.getcFinanceRecordingVoucherExtension().setKmyefx(kmyefx);
            }
            DAOBuilder.instance(financeRecordingVoucherDAO).entities(stFinanceRecordingVouchers).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            //region 修改明细账
            List<StFinanceSubsidiaryAccounts> stFinanceSubsidiaryAccounts = DAOBuilder.instance(iStFinanceSubsidiaryAccountsDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("jzpzh", stFinanceRecordingVouchers.get(0).getJzpzh());
            }}).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (StFinanceSubsidiaryAccounts accounts : stFinanceSubsidiaryAccounts) {
                accounts.setKmbh(kmbh);
                accounts.setQmyefx(kmyefx);
                accounts.setQcyefx(kmyefx);
            }
            DAOBuilder.instance(iStFinanceSubsidiaryAccountsDAO).entities(stFinanceSubsidiaryAccounts).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            //endregion

            //region 绑定到账通知单
            StFinanceRecordingVoucher voucher = stFinanceRecordingVouchers.get(0);
            HashMap<String, Object> filter = new HashMap<>();
            if (YWLSH.length() == 32) filter.put("id", YWLSH);
            else filter.put("bus_seq_no", YWLSH);
            List<CBankAccChangeNotice> cBankAccChangeNotices = DAOBuilder.instance(changeNoticeDAO).searchFilter(filter).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension = voucher.getcFinanceRecordingVoucherExtension();
            BigDecimal djsl = voucher.getFjdjs();
            if (cBankAccChangeNotices.size() > 0) {
                djsl = djsl.add(new BigDecimal(cBankAccChangeNotices.size() - 1));
            }
            voucher.setFjdjs(djsl);//附件单据数
            cFinanceRecordingVoucherExtension.setcBankAccChangeNotices(cBankAccChangeNotices);
            voucher.setcFinanceRecordingVoucherExtension(cFinanceRecordingVoucherExtension);

            DAOBuilder.instance(financeRecordingVoucherDAO).entity(voucher).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNotices) {
                if (cBankAccChangeNotice != null) {
                    cBankAccChangeNotice.setJzpzh(voucher.getJzpzh());
                    cBankAccChangeNotice.setIs_make_acc("1");
                    try {
                        //写日记账和流水表
                        addBankRecord(cBankAccChangeNotice, voucher.getJzpzh(), null);
                    } catch (Exception e) {
                        System.out.println("生成凭证记录：生成日记账和流水" + e.getMessage());
                    }
                } else {
                    cBankAccChangeNotices.remove(null);
                }
            }
            DAOBuilder.instance(changeNoticeDAO).entities(cBankAccChangeNotices).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            //endregion
            return new VoucherRes(voucher.getJzpzh(), null);
        } else {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "凭证未找到");
        }
    }

    /**
     * 生成银行流水记录
     *
     * @param cBankAccChangeNotice
     * @param jzpzh
     * @param YWID
     */
    private void addBankRecord(CBankAccChangeNotice cBankAccChangeNotice, String jzpzh, String YWID) {

        if (cBankAccChangeNotice == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "账户变动详情不存在");
        }
        SettlementDayBook settlementDayBook = new SettlementDayBook();

        CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(cBankAccChangeNotice.getAcct());
        HashMap<String, String> ywlxh = iBusinessClassifySetService.getFundBusinessType(YWID);

        if (cBankAccChangeNotice.getAmt().intValue() < 0) {
            settlementDayBook.setFKYHDM(centerAccountInfo.getCode());
            settlementDayBook.setFKZHHM(centerAccountInfo.getYHZHHM());
            settlementDayBook.setFKZHMC(centerAccountInfo.getYHZHMC());
//            settlementDayBook.setJSYHDM("null".equals(accChangeNoticeFile.getOpponentBankNo()) ? "" : accChangeNoticeFile.getOpponentBankNo().substring(0, 3));
//            settlementDayBook.setSKYHDM("null".equals(accChangeNoticeFile.getOpponentBankNo()) ? "" : accChangeNoticeFile.getOpponentBankNo().substring(0, 3));
            settlementDayBook.setJSYHDM(centerAccountInfo.getCode());
            settlementDayBook.setSKYHDM(centerAccountInfo.getCode());
            settlementDayBook.setSKZHHM(cBankAccChangeNotice.getOpponent_acct());
            settlementDayBook.setSKZHMC(cBankAccChangeNotice.getOpponent_name());
        } else {
//            settlementDayBook.setFKYHDM("null".equals(accChangeNoticeFile.getOpponentBankNo()) ? "" : accChangeNoticeFile.getOpponentBankNo().substring(0, 3));
            settlementDayBook.setFKYHDM(centerAccountInfo.getCode());
            settlementDayBook.setFKZHHM(cBankAccChangeNotice.getOpponent_acct());
            settlementDayBook.setFKZHMC(cBankAccChangeNotice.getOpponent_name());
            settlementDayBook.setJSYHDM(centerAccountInfo.getCode());
            settlementDayBook.setSKYHDM(centerAccountInfo.getCode());
            settlementDayBook.setSKZHHM(centerAccountInfo.getYHZHHM());
            settlementDayBook.setSKZHMC(centerAccountInfo.getYHZHMC());
        }

        settlementDayBook.setFSE(String.valueOf(cBankAccChangeNotice.getAmt()));
        settlementDayBook.setJSFSRQ(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyyMMdd"));
        settlementDayBook.setYHJSLSH(cBankAccChangeNotice.getHost_seq_no());
        settlementDayBook.setYWLSH(StringUtil.notEmpty(cBankAccChangeNotice.getBus_seq_no()) ? cBankAccChangeNotice.getBus_seq_no() : null);
        settlementDayBook.setYWPZHM(jzpzh);
        settlementDayBook.setZHAIYAO(cBankAccChangeNotice.getSummary());
        settlementDayBook.setZJYWLX(ywlxh.get("zjywlxbm"));
        iFinanceReportService.addSettlementDayBook(settlementDayBook);//添加银行结算流水信息

        DepositJournal depositJournal = new DepositJournal();
        depositJournal.setCNLSH(StringUtil.notEmpty(cBankAccChangeNotice.getBus_seq_no()) ? cBankAccChangeNotice.getBus_seq_no() : null);
        depositJournal.setCZBS("01");
        if (cBankAccChangeNotice.getAmt().compareTo(BigDecimal.ZERO) > 0) {
            depositJournal.setDFFSE("0.00");
            depositJournal.setJFFSE(String.valueOf(cBankAccChangeNotice.getAmt().abs()));
        } else {
            depositJournal.setDFFSE(String.valueOf(cBankAccChangeNotice.getAmt().abs()));
            depositJournal.setJFFSE("0.00");
        }

        depositJournal.setJZPZH(jzpzh);
        depositJournal.setJZRQ(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyyMMdd"));
        depositJournal.setPZSSNY(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyyMM"));

        depositJournal.setRZRQ(DateUtil.date2Str(new Date(), "yyyyMMdd"));
        depositJournal.setRZZT("01");
        depositJournal.setYEJDFX("02");
        depositJournal.setYHJSLSH(cBankAccChangeNotice.getHost_seq_no());
        depositJournal.setYHZHHM(cBankAccChangeNotice.getAcct());
        depositJournal.setYuE(String.valueOf(cBankAccChangeNotice.getBalance()));
        depositJournal.setZHAIYAO(cBankAccChangeNotice.getSummary());
        depositJournal.setZJYWLX(ywlxh.get("zjywlxbm"));
        iFinanceReportService.addDepositJournal(depositJournal);//添加银行存款日记账
    }


    @Override
    public String addTemporaryRecord(String SKZH, String SKHM, String FKZH, String FKHM, BigDecimal HRJE, Date HRSJ, String JZPZH, String zhaiyao, String remark) {
        CFinanceTemporaryRecord record = new CFinanceTemporaryRecord();
        record.setSkzh(SKZH);
        record.setSkhm(SKHM);
        record.setFkzh(FKZH);
        record.setFkhm(FKHM);
        HRJE = HRJE.setScale(2, BigDecimal.ROUND_HALF_UP);
        record.setHrje(HRJE);
        record.setHrsj(HRSJ);
        record.setState(false);
        record.setYjzpzh(JZPZH);
        record.setJzpzh(null);
        record.setZhaiyao(zhaiyao);
        record.setRemark(remark);
        return DAOBuilder.instance(icFinanceTemporaryRecordDAO).entity(record).saveThenFetchId(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) throws ErrorException {
                throw new ErrorException(e);
            }
        });
    }


    @Override
    public String updateTemporaryRecord(String ID, Boolean state, String JZPZH) {
//        try {
        CFinanceTemporaryRecord record = DAOBuilder.instance(icFinanceTemporaryRecordDAO).UID(ID).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (record == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "该暂收不存在");
        }
        if (record.getState()) {
            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "该暂收已分摊");
        }
        record.setState(state);
        if (StringUtil.notEmpty(JZPZH)) {
            record.setJzpzh(JZPZH);
        }
        return DAOBuilder.instance(icFinanceTemporaryRecordDAO).entity(record).saveThenFetchId(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
//        } catch (Exception e) {
//            System.err.println(e.getMessage())();
//            return null;
//        }

    }

    @Override
    public PageRes<TemporaryRecord> getTemporaryRecordList(String HRHM, int state, String HRSJKS, String HRSJJS, String FSE, String JZPZH, String YHMC, String ZHHM, int pageSize, int pageNo) {
        try {
            Date hrsjks = DateUtil.str2Date(format, HRSJKS);
            Date hrsjjs = DateUtil.str2Date(format, HRSJJS);
            PageRes<TemporaryRecord> res = new PageRes<>();

            HashMap<String, Object> filter = new HashMap<>();
            if (StringUtil.notEmpty(YHMC) && !"所有".equals(YHMC)) filter.put("yhmc", YHMC);
            if (StringUtil.notEmpty(ZHHM) && !"所有".equals(ZHHM)) filter.put("yhzhhm", ZHHM);

            List<StSettlementSpecialBankAccount> specialBankAccountList = DAOBuilder.instance(specialBankAccountDAO).searchFilter(filter).searchOption(SearchOption.REFINED).
                    getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                        }
                    });

            List<String> yhzhs = new ArrayList<>();
            for (StSettlementSpecialBankAccount account : specialBankAccountList) {
                if (account != null) {
                    yhzhs.add(account.getYhzhhm());
                }
            }

            if (yhzhs.size() <= 0) {
                res.setResults(new ArrayList<>());
                return res;
            }

            List<CFinanceTemporaryRecord> financeTemporaryRecords = DAOBuilder.instance(icFinanceTemporaryRecordDAO)
                    .searchFilter(new HashMap<String, Object>() {{
                        if (StringUtil.notEmpty(HRHM)) {
                            this.put("fkhm", HRHM);
                        }
                        if (state != 0) {
                            this.put("state", state == 1);
                        }
                        if (StringUtil.notEmpty(FSE)) {
                            this.put("hrje", new BigDecimal(FSE));
                        }
                    }}).searchOption(SearchOption.FUZZY).orderOption("state", Order.ASC)
                    .extension(new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.add(Restrictions.between("hrsj", hrsjks, hrsjjs));
                            criteria.add(Restrictions.or(
                                    Restrictions.in("skzh", yhzhs),
                                    Restrictions.in("fkzh", yhzhs)
                            ));
                            if (StringUtil.notEmpty(JZPZH)) {
                                criteria.add(Restrictions.or(
                                        Restrictions.eq("jzpzh", JZPZH),
                                        Restrictions.eq("yjzpzh", JZPZH)
                                ));
                            }
                        }
                    }).pageOption(res, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });

            ArrayList<TemporaryRecord> list = new ArrayList<>();
            List<StSettlementSpecialBankAccount> stSettlementSpecialBankAccounts = DAOBuilder.instance(specialBankAccountDAO).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (CFinanceTemporaryRecord record : financeTemporaryRecords) {
                TemporaryRecord temporaryRecord = new TemporaryRecord();
                temporaryRecord.setId(record.getId());
                temporaryRecord.setFKHM(record.getFkhm());
                temporaryRecord.setFKZH(record.getFkzh());
                temporaryRecord.setSKHM(record.getSkhm());
                temporaryRecord.setSKZH(record.getSkzh());
                temporaryRecord.setHRJE(String.valueOf(record.getHrje()));
                temporaryRecord.setHRSJ(DateUtil.date2Str(record.getHrsj(), "yyyy-MM-dd"));
                temporaryRecord.setJZPZH(record.getJzpzh());
                temporaryRecord.setYJZPZH(record.getYjzpzh());
                temporaryRecord.setZhaiYao(record.getZhaiyao());
                temporaryRecord.setState(record.getState() ? "1" : "2");
                temporaryRecord.setRemark(record.getRemark());
                StSettlementSpecialBankAccount account = searchSpecialBankAccount(stSettlementSpecialBankAccounts, record.getSkzh());
                temporaryRecord.setSKYHMC(account == null ? "" : account.getYhmc());
                list.add(temporaryRecord);
            }
            res.setResults(list);
            return res;
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ErrorException(e);
        }
    }

    @Override
    public PageResNew<TemporaryRecord> getTemporaryRecordList(String HRHM, int state, String HRSJKS, String HRSJJS, String FSE, String JZPZH, String YHMC, String ZHHM, String marker, String action, int pageSize) {
        try {
            Date hrsjks = DateUtil.str2Date(format, HRSJKS);
            Date hrsjjs = DateUtil.str2Date(format, HRSJJS);
            PageResNew<TemporaryRecord> res = new PageResNew<>();

            HashMap<String, Object> filter = new HashMap<>();
            if (StringUtil.notEmpty(YHMC) && !"所有".equals(YHMC)) filter.put("yhmc", YHMC);
            if (StringUtil.notEmpty(ZHHM) && !"所有".equals(YHMC)) filter.put("yhzhhm", ZHHM);

            List<StSettlementSpecialBankAccount> specialBankAccountList = DAOBuilder.instance(specialBankAccountDAO).searchFilter(filter).searchOption(SearchOption.FUZZY).
                    getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                        }
                    });

            List<String> yhzhs = new ArrayList<>();
            for (StSettlementSpecialBankAccount account : specialBankAccountList) {
                if (account != null) {
                    yhzhs.add(account.getYhzhhm());
                }
            }

            List<CFinanceTemporaryRecord> financeTemporaryRecords = DAOBuilder.instance(icFinanceTemporaryRecordDAO)
                    .searchFilter(new HashMap<String, Object>() {{
                        if (StringUtil.notEmpty(HRHM)) {
                            this.put("fkhm", HRHM);
                        }
                        if (state != 0) {
                            this.put("state", state == 1);
                        }
                        if (StringUtil.notEmpty(FSE)) {
                            this.put("hrje", new BigDecimal(FSE));
                        }
                        if (StringUtil.notEmpty(JZPZH)) {
                            this.put("jzpzh", JZPZH);
                        }
                    }}).searchOption(SearchOption.FUZZY).orderOption("state", Order.ASC)
                    .extension(new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.add(Restrictions.between("hrsj", hrsjks, hrsjjs));
                            if (yhzhs.size() > 0) {
                                criteria.add(Restrictions.or(
                                        Restrictions.in("skzh", yhzhs),
                                        Restrictions.in("fkzh", yhzhs)
                                ));
                            }
                        }
                    }).pageOption(marker, action, pageSize).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });

            ArrayList<TemporaryRecord> list = new ArrayList<>();
            List<StSettlementSpecialBankAccount> stSettlementSpecialBankAccounts = DAOBuilder.instance(specialBankAccountDAO).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (CFinanceTemporaryRecord record : financeTemporaryRecords) {
                TemporaryRecord temporaryRecord = new TemporaryRecord();
                temporaryRecord.setId(record.getId());
                temporaryRecord.setFKHM(record.getFkhm());
                temporaryRecord.setFKZH(record.getFkzh());
                temporaryRecord.setSKHM(record.getSkhm());
                temporaryRecord.setSKZH(record.getSkzh());
                temporaryRecord.setHRJE(String.valueOf(record.getHrje()));
                temporaryRecord.setHRSJ(DateUtil.date2Str(record.getHrsj(), "yyyy-MM-dd HH:mm:ss"));
                temporaryRecord.setJZPZH(record.getJzpzh());
                temporaryRecord.setYJZPZH(record.getYjzpzh());
                temporaryRecord.setZhaiYao(record.getZhaiyao());
                temporaryRecord.setState(record.getState() ? "1" : "2");
                temporaryRecord.setRemark(record.getRemark());
                StSettlementSpecialBankAccount account = searchSpecialBankAccount(stSettlementSpecialBankAccounts, record.getSkzh());
                temporaryRecord.setSKYHMC(account == null ? "" : account.getYhmc());
                list.add(temporaryRecord);
            }
            res.setResults(action, list);
            return res;
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ErrorException(e);
        }
    }

    private StSettlementSpecialBankAccount searchSpecialBankAccount(List<StSettlementSpecialBankAccount> stSettlementSpecialBankAccounts, String YHZH) {
        if (!StringUtil.notEmpty(YHZH)) {
            return null;
        }
        for (StSettlementSpecialBankAccount s : stSettlementSpecialBankAccounts) {
            if (YHZH.equals(s.getYhzhhm())) {
                return s;
            }
        }
        return null;
    }

    @Override
    public PageRes<VoucherManagerBase> getVoucherList(String PZRQKS, String PZRQJS, String PZH, String YWLX, String YWMC, String ZhaiYao, String FSE, int pageSize, int pageNo) {
        try {
            Date pzrqks = DateUtil.str2Date("yyyy-MM-dd HH:mm", "1990-01-01 00:00");
            Date pzrqjs = new Date();
            if (StringUtil.notEmpty(PZRQKS)) {
                pzrqks = DateUtil.str2Date("yyyy-MM-dd HH:mm:ss", PZRQKS+":59");
            }
            if (StringUtil.notEmpty(PZRQJS)) {
                pzrqjs = DateUtil.str2Date("yyyy-MM-dd HH:mm:ss", PZRQJS+":59");
            }
            PageRes<VoucherManagerBase> res = new PageRes<>();
            List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO).betweenDate(pzrqks, pzrqjs).searchFilter(new HashMap<String, Object>() {{
                if (StringUtil.notEmpty(PZH)) {
                    this.put("jzpzh", PZH);
                }
                if (StringUtil.notEmpty(YWLX) && !YWLX.equals("所有")) {
                    this.put("cFinanceRecordingVoucherExtension.ywlx", YWLX);
                }
                if (StringUtil.notEmpty(YWMC)) {
                    this.put("cFinanceRecordingVoucherExtension.ywmc", YWMC);
                }
                if (StringUtil.notEmpty(FSE)) {
                    this.put("cFinanceRecordingVoucherExtension.jfhj", new BigDecimal(FSE));
                }
                if (StringUtil.notEmpty(ZhaiYao)) {
                    this.put("zhaiYao", ZhaiYao);
                }
                this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
            }}).searchOption(SearchOption.FUZZY).pageOption(res, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            ArrayList<VoucherManagerBase> reslist = new ArrayList<>();
            for (StFinanceRecordingVoucher stFinanceRecordingVoucher : stFinanceRecordingVouchers) {
                VoucherManagerBase voucherManagerBase = new VoucherManagerBase();
                voucherManagerBase.setId(stFinanceRecordingVoucher.getId());
                CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension();
                voucherManagerBase.setZhaiYao(stFinanceRecordingVoucher.getZhaiYao());
                voucherManagerBase.setJiZhang(cFinanceRecordingVoucherExtension.getJizhang());
                voucherManagerBase.setJZPZH(stFinanceRecordingVoucher.getJzpzh());
                voucherManagerBase.setJZRQ(DateUtil.date2Str(stFinanceRecordingVoucher.getJzrq(), "yyyy-MM-dd"));
                voucherManagerBase.setYWLX(cFinanceRecordingVoucherExtension.getYwlx());
                voucherManagerBase.setYWMC(cFinanceRecordingVoucherExtension.getYwmc());
                voucherManagerBase.setYWLSH(cFinanceRecordingVoucherExtension.getYwlsh());
                voucherManagerBase.setFSE(cFinanceRecordingVoucherExtension.getJfhj().abs().toString());
                reslist.add(voucherManagerBase);
            }
            res.setResults(reslist);
            return res;
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式错误");
        }
    }

    @Override
    public PageResNew<VoucherManagerBase> getVoucherList(String PZRQKS, String PZRQJS, String PZH, String YWLX, String YWMC, String ZhaiYao, String FSE, String marker, String action, int pageSize) {
        try {
            Date pzrqks = DateUtil.str2Date("yyyy-MM-dd HH:mm", "1990-01-01 00:00");
            Date pzrqjs = new Date();
            if (StringUtil.notEmpty(PZRQKS)) {
                pzrqks = DateUtil.str2Date("yyyy-MM-dd HH:mm:ss", PZRQKS+":59");
            }
            if (StringUtil.notEmpty(PZRQJS)) {
                pzrqjs = DateUtil.str2Date("yyyy-MM-dd HH:mm:ss", PZRQJS+":59");
            }
            PageResNew<VoucherManagerBase> res = new PageResNew<>();
            List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO).betweenDate(pzrqks, pzrqjs).searchFilter(new HashMap<String, Object>() {{
                if (StringUtil.notEmpty(PZH)) {
                    this.put("jzpzh", PZH);
                }
                if (StringUtil.notEmpty(YWLX) && !YWLX.equals("所有")) {
                    this.put("cFinanceRecordingVoucherExtension.ywlx", YWLX);
                }
                if (StringUtil.notEmpty(YWMC)) {
                    this.put("cFinanceRecordingVoucherExtension.ywmc", YWMC);
                }
                if (StringUtil.notEmpty(FSE)) {
                    this.put("cFinanceRecordingVoucherExtension.jfhj", new BigDecimal(FSE));
                }
                this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
            }}).searchOption(SearchOption.FUZZY).pageOption(marker, action, pageSize).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            ArrayList<VoucherManagerBase> reslist = new ArrayList<>();
            for (StFinanceRecordingVoucher stFinanceRecordingVoucher : stFinanceRecordingVouchers) {
                VoucherManagerBase voucherManagerBase = new VoucherManagerBase();
                voucherManagerBase.setId(stFinanceRecordingVoucher.getId());
                CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension();
                voucherManagerBase.setZhaiYao(stFinanceRecordingVoucher.getZhaiYao());
                voucherManagerBase.setJiZhang(cFinanceRecordingVoucherExtension.getJizhang());
                voucherManagerBase.setJZPZH(stFinanceRecordingVoucher.getJzpzh());
                voucherManagerBase.setJZRQ(DateUtil.date2Str(stFinanceRecordingVoucher.getJzrq(), "yyyy-MM-dd"));
                voucherManagerBase.setYWLX(cFinanceRecordingVoucherExtension.getYwlx());
                voucherManagerBase.setYWMC(cFinanceRecordingVoucherExtension.getYwmc());
                voucherManagerBase.setYWLSH(cFinanceRecordingVoucherExtension.getYwlsh());
                voucherManagerBase.setFSE(cFinanceRecordingVoucherExtension.getJfhj().toString());
                reslist.add(voucherManagerBase);
            }
            res.setResults(action, reslist);
            return res;
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式错误");
        }
    }

    /**
     * @param JZPZH 业务凭证号
     * @return
     */
    @Override
    public VoucherManager getDetail(String JZPZH) {
        StFinanceRecordingVoucher stFinanceRecordingVoucher = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("jzpzh", JZPZH);
            this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        VoucherManager res = new VoucherManager();
        if (stFinanceRecordingVoucher == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "凭证不存在");
        }
        List<AccChangeNoticeFile> accChangeNoticeFiles = new ArrayList<>();
        List<CBankAccChangeNotice> cBankAccChangeNotices = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension().getcBankAccChangeNotices();
        for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNotices) {
            AccChangeNoticeFile accChangeNoticeFile = new AccChangeNoticeFile();
            if (cBankAccChangeNotice != null) {
                accChangeNoticeFile.setAcct(cBankAccChangeNotice.getAcct());
                accChangeNoticeFile.setAmt(cBankAccChangeNotice.getAmt());
                accChangeNoticeFile.setAvailableAmt(cBankAccChangeNotice.getAvailable_amt());
                accChangeNoticeFile.setBalance(cBankAccChangeNotice.getBalance());
                accChangeNoticeFile.setBookListNo(cBankAccChangeNotice.getBook_list_no());
                accChangeNoticeFile.setBookNo(cBankAccChangeNotice.getBook_no());
                accChangeNoticeFile.setCurrIden(cBankAccChangeNotice.getCurr_iden());
                accChangeNoticeFile.setCurrNo(cBankAccChangeNotice.getCurr_no());
                accChangeNoticeFile.setDate(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyy-MM-dd"));
                accChangeNoticeFile.setHostSeqNo(cBankAccChangeNotice.getHost_seq_no());
                accChangeNoticeFile.setNo(cBankAccChangeNotice.getNotice_no());
                accChangeNoticeFile.setOpenBankNo("null".equals(cBankAccChangeNotice.getOpen_bank_no()) ? null : cBankAccChangeNotice.getOpen_bank_no());
                accChangeNoticeFile.setOpponentAcct(cBankAccChangeNotice.getOpponent_acct());
                accChangeNoticeFile.setOpponentBankNo("null".equals(cBankAccChangeNotice.getOpponent_bank_no()) ? null : cBankAccChangeNotice.getOpponent_bank_no());
                accChangeNoticeFile.setOpponentName(cBankAccChangeNotice.getOpponent_name());
                accChangeNoticeFile.setOverdraft(cBankAccChangeNotice.getOverdraft());
                accChangeNoticeFile.setRedo("null".equals(cBankAccChangeNotice.getRedo()) ? null : cBankAccChangeNotice.getRedo());
                accChangeNoticeFile.setRemark("null".equals(cBankAccChangeNotice.getRemark()) ? null : cBankAccChangeNotice.getRemark());
                accChangeNoticeFile.setSummary(cBankAccChangeNotice.getSummary());
                accChangeNoticeFile.setTime(cBankAccChangeNotice.getTime());
                accChangeNoticeFile.setTxCode("null".equals(cBankAccChangeNotice.getTx_code()) ? null : cBankAccChangeNotice.getTx_code());
                accChangeNoticeFile.setVoucherNo("null".equals(cBankAccChangeNotice.getVoucher_no()) ? null : cBankAccChangeNotice.getVoucher_no());
                accChangeNoticeFile.setVoucherType("null".equals(cBankAccChangeNotice.getVoucher_type()) ? null : cBankAccChangeNotice.getVoucher_type());
                accChangeNoticeFile.setSmwj(cBankAccChangeNotice.getSmwj());
                accChangeNoticeFiles.add(accChangeNoticeFile);

            }
        }
        res.setAccChangeNoticeFiles(accChangeNoticeFiles);

        CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension();
        res.setChuNa(cFinanceRecordingVoucherExtension.getChuna());
        res.setCWZG(cFinanceRecordingVoucherExtension.getCwzg());
        res.setJFHJ(String.valueOf(cFinanceRecordingVoucherExtension.getJfhj()));
        res.setDFHJ(String.valueOf(cFinanceRecordingVoucherExtension.getDfhj()));
        res.setFJSL(String.valueOf(stFinanceRecordingVoucher.getFjdjs()));
        res.setFuHe(cFinanceRecordingVoucherExtension.getFuhe());
        res.setHSDW(cFinanceRecordingVoucherExtension.getHsdw());
        res.setZhiDan(cFinanceRecordingVoucherExtension.getZhidan());
        res.setId(stFinanceRecordingVoucher.getId());
        res.setJiZhang(cFinanceRecordingVoucherExtension.getJizhang());
        res.setJZPZH(stFinanceRecordingVoucher.getJzpzh());
        res.setJZRQ(DateUtil.date2Str(stFinanceRecordingVoucher.getJzrq(), "yyyy-MM-dd"));
        res.setYWLX(cFinanceRecordingVoucherExtension.getYwlx());
        res.setYWMC(cFinanceRecordingVoucherExtension.getYwmc());
        res.setYWLSH(cFinanceRecordingVoucherExtension.getYwlsh());
        res.setCZNR(cFinanceRecordingVoucherExtension.getCznr());
        res.setKMYEFX(cFinanceRecordingVoucherExtension.getKmyefx());
        res.setFSE(String.valueOf(cFinanceRecordingVoucherExtension.getJfhj()));
        res.setZhaiYao(stFinanceRecordingVoucher.getZhaiYao());
        List<VoucherMangerList> voucherMangerLists = new ArrayList<>();
        VoucherMangerList v = new VoucherMangerList();
        v.setDFJE(String.valueOf(stFinanceRecordingVoucher.getDffse()));
        v.setJFJE(String.valueOf(stFinanceRecordingVoucher.getJffse()));
        v.setKJKM(stFinanceRecordingVoucher.getKmbh());
        List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(stFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        v.setKJKMKMMC(getKJKMMC(stFinanceSubjects, stFinanceRecordingVoucher.getKmbh()));
        v.setZhaiYao(stFinanceRecordingVoucher.getZhaiYao());
        voucherMangerLists.add(v);
        List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(JZPZH)) {
                this.put("jzpzh", JZPZH);
            }
            this.put("cFinanceRecordingVoucherExtension.sfzjl", false);
        }}).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        BigDecimal HJDFJE = new BigDecimal(0);
        BigDecimal HJJFJE = new BigDecimal(0);
        for (StFinanceRecordingVoucher s : stFinanceRecordingVouchers) {
            VoucherMangerList voucherMangerList = new VoucherMangerList();
            voucherMangerList.setDFJE(String.valueOf(s.getDffse()));
            voucherMangerList.setJFJE(String.valueOf(s.getJffse()));
            voucherMangerList.setKJKM(s.getKmbh());
            voucherMangerList.setKJKMKMMC(getKJKMMC(stFinanceSubjects, s.getKmbh()));
            voucherMangerList.setZhaiYao(s.getZhaiYao());
            voucherMangerList.setKMYEFX(s.getcFinanceRecordingVoucherExtension().getKmyefx());
            voucherMangerLists.add(voucherMangerList);
            HJDFJE = HJDFJE.add(s.getDffse());
            HJJFJE = HJJFJE.add(s.getJffse());
        }
        HJDFJE = HJDFJE.add(new BigDecimal(v.getDFJE()));
        HJJFJE = HJJFJE.add(new BigDecimal(v.getJFJE()));
        HJDFJE = HJDFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
        HJJFJE = HJJFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
        res.setHJDFJE(String.valueOf(HJDFJE));
        res.setHJJFJE(String.valueOf(HJJFJE));
        res.setVoucherMangerLists(voucherMangerLists);
        String id = pdfService.getVoucherGetDetailPdf(res);
        res.setPDFID(id);
        return res;
    }

    private String getKJKMMC(List<StFinanceSubjects> stFinanceSubjects, String KMBH) {
        StringBuilder res = new StringBuilder();
        StFinanceSubjects subjects_other;
        boolean isfirst = true;
        do {
            subjects_other = FinanceComputeHelper.searchSubjects(stFinanceSubjects, KMBH);
            if (subjects_other != null) {
                res.insert(0, subjects_other.getKmmc() + (isfirst ? "" : "-"));
                KMBH = subjects_other.getcFinanceSubjectsExtension().getSjkm();
                isfirst = false;
            }
        }
        while (subjects_other != null && StringUtil.notEmpty(subjects_other.getcFinanceSubjectsExtension().getSjkm()));

        return res.toString();
    }

    @Override
    public CommonResponses getDetailpdf(String JZPZH) {
        String id = pdfService.getVoucherGetDetailPdf(getDetail(JZPZH));
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    public CommonResponses batchVoucherPdf(VoucherBatchPdfPost voucherBatchPdfPost) {
        ArrayList<String> jzpzhs = voucherBatchPdfPost.getJZPZHS();
        if (jzpzhs.size() < 1) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "请选择需要打印的业务");
        }
        ArrayList<VoucherManager> VoucherInfoArray = new ArrayList<>();
        for (String v : jzpzhs) {
            VoucherManager voucherManager = getVoucherPartdetail(v);
            VoucherInfoArray.add(voucherManager);
        }
        String PdfId = pdfService.getMergerVoucherMorePdf(VoucherInfoArray);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(PdfId);
        commonResponses.setState("success");
        return commonResponses;
    }

    public VoucherManager getVoucherPartdetail(String JZPZH) {
        StFinanceRecordingVoucher stFinanceRecordingVoucher = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("jzpzh", JZPZH);
            this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        VoucherManager res = new VoucherManager();
        if (stFinanceRecordingVoucher == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "凭证不存在");
        }
        CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension();
        res.setChuNa(cFinanceRecordingVoucherExtension.getChuna());
        res.setCWZG(cFinanceRecordingVoucherExtension.getCwzg());
        res.setJFHJ(String.valueOf(cFinanceRecordingVoucherExtension.getJfhj()));
        res.setDFHJ(String.valueOf(cFinanceRecordingVoucherExtension.getDfhj()));
        res.setFJSL(String.valueOf(stFinanceRecordingVoucher.getFjdjs()));
        res.setFuHe(cFinanceRecordingVoucherExtension.getFuhe());
        res.setHSDW(cFinanceRecordingVoucherExtension.getHsdw());
        res.setZhiDan(cFinanceRecordingVoucherExtension.getZhidan());
        res.setId(stFinanceRecordingVoucher.getId());
        res.setJiZhang(cFinanceRecordingVoucherExtension.getJizhang());
        res.setJZPZH(stFinanceRecordingVoucher.getJzpzh());
        res.setJZRQ(DateUtil.date2Str(stFinanceRecordingVoucher.getJzrq(), "yyyy-MM-dd"));
        res.setYWLX(cFinanceRecordingVoucherExtension.getYwlx());
        res.setYWMC(cFinanceRecordingVoucherExtension.getYwmc());
        res.setYWLSH(cFinanceRecordingVoucherExtension.getYwlsh());
        res.setCZNR(cFinanceRecordingVoucherExtension.getCznr());
        res.setKMYEFX(cFinanceRecordingVoucherExtension.getKmyefx());
        res.setFSE(String.valueOf(cFinanceRecordingVoucherExtension.getJfhj()));
        res.setZhaiYao(stFinanceRecordingVoucher.getZhaiYao());
        List<VoucherMangerList> voucherMangerLists = new ArrayList<>();
        VoucherMangerList v = new VoucherMangerList();
        v.setDFJE(String.valueOf(stFinanceRecordingVoucher.getDffse()));
        v.setJFJE(String.valueOf(stFinanceRecordingVoucher.getJffse()));
        v.setKJKM(stFinanceRecordingVoucher.getKmbh());
        List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(stFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        v.setKJKMKMMC(getKJKMMC(stFinanceSubjects, stFinanceRecordingVoucher.getKmbh()));
        v.setZhaiYao(stFinanceRecordingVoucher.getZhaiYao());
        voucherMangerLists.add(v);
        List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(JZPZH)) {
                this.put("jzpzh", JZPZH);
            }
            this.put("cFinanceRecordingVoucherExtension.sfzjl", false);
        }}).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        BigDecimal HJDFJE = new BigDecimal(0);
        BigDecimal HJJFJE = new BigDecimal(0);
        for (StFinanceRecordingVoucher s : stFinanceRecordingVouchers) {
            VoucherMangerList voucherMangerList = new VoucherMangerList();
            voucherMangerList.setDFJE(String.valueOf(s.getDffse()));
            voucherMangerList.setJFJE(String.valueOf(s.getJffse()));
            voucherMangerList.setKJKM(s.getKmbh());
            voucherMangerList.setKJKMKMMC(getKJKMMC(stFinanceSubjects, s.getKmbh()));
            voucherMangerList.setZhaiYao(s.getZhaiYao());
            voucherMangerList.setKMYEFX(s.getcFinanceRecordingVoucherExtension().getKmyefx());
            voucherMangerLists.add(voucherMangerList);
            HJDFJE = HJDFJE.add(s.getDffse());
            HJJFJE = HJJFJE.add(s.getJffse());
        }
        HJDFJE = HJDFJE.add(new BigDecimal(v.getDFJE()));
        HJJFJE = HJJFJE.add(new BigDecimal(v.getJFJE()));
        HJDFJE = HJDFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
        HJJFJE = HJJFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
        res.setHJDFJE(String.valueOf(HJDFJE));
        res.setHJJFJE(String.valueOf(HJJFJE));
        res.setVoucherMangerLists(voucherMangerLists);
        return res;
    }

    @Override
    public CommonResponses checkoutVoucher(String JZR, String KJQJ) {
        try {
            CFinanceAccountPeriod cFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("kjqj", KJQJ);
                }
            }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cFinanceAccountPeriod == null) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "没有会计期间");
            }
            if (new Date().getTime() < cFinanceAccountPeriod.getJiezrq().getTime() + 86399 * 1000) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "未到截止日期不能结账");
            }
            if (cFinanceAccountPeriod.isSfjs()) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "会计期间已结账");
            }
            List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(stFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            List<String> kmbhlist = new ArrayList<>();
            for (StFinanceSubjects s : stFinanceSubjects) {
                kmbhlist.add(s.getKmbh());
            }
            Collections.sort(kmbhlist);
            List<CFinanceSubjectsBalance> cFinanceSubjectsBalances = new ArrayList<>();

            List<CFinanceSubjectsBalance> oldbalances = getOldPeriod(KJQJ, "yyyyMM");

            Date start = cFinanceAccountPeriod.getQsrq();
            Date end = new Date(cFinanceAccountPeriod.getJiezrq().getTime() + 86399 * 1000);//精确到到最后一天最后一秒
            //region 验证是否有未做账到账通知单
            List<CBankAccChangeNotice> accChangeNotices = DAOBuilder.instance(changeNoticeDAO).betweenDate(start, end).searchFilter(new HashMap<String, Object>() {{
                this.put("is_make_acc", "0");
            }}).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (accChangeNotices.size() > 0) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "有未做账到账通知单暂不能结账");
            }
            //endregion

            List<StFinanceRecordingVoucher> vs = DAOBuilder.instance(financeRecordingVoucherDAO).betweenDate(start, end)
                    .searchFilter(new HashMap<String, Object>() {{
                        this.put("cFinanceRecordingVoucherExtension.cznr", list);
                        this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
                    }})
                    .searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
            StringBuilder error = new StringBuilder();
            for (String cznr : list) {
                boolean isfind = false;
                for (StFinanceRecordingVoucher v : vs) {
                    if (cznr.equals(v.getcFinanceRecordingVoucherExtension().getCznr())) {
                        isfind = true;
                    }
                }
                if (!isfind) {
                    error.append(",未做：").append(cznr).append("凭证");
                }
            }
            if (StringUtil.notEmpty(error.toString())) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, error.toString());
            }

            List<StFinanceRecordingVoucher> vouchers = DAOBuilder.instance(financeRecordingVoucherDAO).betweenDate(start, end).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (String s : kmbhlist) {
                CFinanceSubjectsBalance cFinanceSubjectsBalance = new CFinanceSubjectsBalance();
                CFinanceSubjectsBalance balance = FinanceComputeHelper.searchSubjectsBalance(oldbalances, s);
                if (balance == null) {
                    cFinanceSubjectsBalance.setSyye(new BigDecimal("0.00"));
                } else {
                    cFinanceSubjectsBalance.setSyye(balance.getByye());
                }

                StFinanceSubjects subject = FinanceComputeHelper.searchSubjects(stFinanceSubjects, s);

                if (subject == null) {
                    continue;
                }

                List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = FinanceComputeHelper.searchRecordingVoucher(vouchers, s);

                BigDecimal JFJE = new BigDecimal("0.00");
                BigDecimal DFJE = new BigDecimal("0.00");

                for (StFinanceRecordingVoucher stFinanceRecordingVoucher : stFinanceRecordingVouchers) {
                    if ("02".equals(subject.getKmyefx())) {
                        JFJE = JFJE.add(stFinanceRecordingVoucher.getJffse());
                        DFJE = DFJE.add(stFinanceRecordingVoucher.getDffse());
                    } else {
                        JFJE = JFJE.add(stFinanceRecordingVoucher.getDffse());
                        DFJE = DFJE.add(stFinanceRecordingVoucher.getJffse());
                    }
                }
                JFJE = JFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
                DFJE = DFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
                cFinanceSubjectsBalance.setByzj(JFJE);
                cFinanceSubjectsBalance.setByjs(DFJE);

                cFinanceSubjectsBalance.setKmbh(subject.getKmbh());
                cFinanceSubjectsBalance.setKmmc(subject.getKmmc());
                cFinanceSubjectsBalance.setKmjb(subject.getKmjb());
                cFinanceSubjectsBalance.setKmyefx(subject.getKmyefx());
                cFinanceSubjectsBalance.setcFinanceAccountPeriod(cFinanceAccountPeriod);
                cFinanceSubjectsBalances.add(cFinanceSubjectsBalance);
            }
            financeRecordingVoucherDAO.updateVoucher(DateUtil.date2Str(start, "yyyy-MM-dd HH:mm:ss"), DateUtil.date2Str(end, "yyyy-MM-dd HH:mm:ss"));
            cFinanceSubjectsBalances = computBalance(cFinanceSubjectsBalances);

            //region 生成总账
            List<StFinanceGeneralLedger> ledgers = new ArrayList<>();
            for (CFinanceSubjectsBalance balance : cFinanceSubjectsBalances) {
                if (balance.getKmjb().intValue() != 1) {
                    continue;
                }
                CFinanceSubjectsBalance oldbalance = FinanceComputeHelper.searchSubjectsBalance(oldbalances, balance.getKmbh());
                StFinanceGeneralLedger stFinanceGeneralLedger = new StFinanceGeneralLedger();
                stFinanceGeneralLedger.setDffse(balance.getByjs());
                stFinanceGeneralLedger.setJffse(balance.getByzj());
                stFinanceGeneralLedger.setJzrq(new Date());
                stFinanceGeneralLedger.setKmbh(balance.getKmbh());
                stFinanceGeneralLedger.setKmmc(balance.getKmmc());
                stFinanceGeneralLedger.setQcye(oldbalance == null ? BigDecimal.ZERO : oldbalance.getByye());
                stFinanceGeneralLedger.setQcyefx(balance.getKmyefx());
                stFinanceGeneralLedger.setQmye(balance.getByye());
                stFinanceGeneralLedger.setQmyefx(balance.getKmyefx());
                stFinanceGeneralLedger.setZhaiYao(KJQJ + "总账");
                ledgers.add(stFinanceGeneralLedger);
            }
            DAOBuilder.instance(iStFinanceGeneralLedgerDAO).entities(ledgers).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            //endregion
            cFinanceAccountPeriod.setSfjs(true);
            cFinanceAccountPeriod.setJzr(JZR);
            cFinanceAccountPeriod.setJzrq(new Date());

            cFinanceAccountPeriod.setcFinanceSubjectsBalances(cFinanceSubjectsBalances);
            String id = DAOBuilder.instance(icFinanceAccountPeriodDAO).entity(cFinanceAccountPeriod).saveThenFetchId(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            CommonResponses res = new CommonResponses();
            res.setState("Success");
            res.setId(id);
            return res;
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ErrorException(e);
        }
    }


    @Override
    public List<SubjectsBalance> getSubjectsCollect(String JZRQ) {
        try {
            if (!StringUtil.notEmpty(JZRQ)) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "截止日期不能为空");
            }
            List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(stFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            List<String> kmbhlist = new ArrayList<>();
            for (StFinanceSubjects s : stFinanceSubjects) {
                kmbhlist.add(s.getKmbh());
            }
            Collections.sort(kmbhlist);

            List<CFinanceSubjectsBalance> oldbalances = getOldPeriod(JZRQ, "yyyy-MM-dd");

            List<CFinanceSubjectsBalance> cFinanceSubjectsBalances = new ArrayList<>();

            String[] jzrq = JZRQ.split("-");
            Date start = DateUtil.str2Date("yyyy-MM-dd HH:mm:ss", jzrq[0] + "-" + jzrq[1] + "-01 00:00:00");
            Date end = DateUtil.str2Date("yyyy-MM-dd HH:mm:ss", JZRQ + " 23:59:59");
            List<StFinanceRecordingVoucher> vouchers = DAOBuilder.instance(financeRecordingVoucherDAO).betweenDate(start, end).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (String s : kmbhlist) {
                CFinanceSubjectsBalance cFinanceSubjectsBalance = new CFinanceSubjectsBalance();
                CFinanceSubjectsBalance balance = FinanceComputeHelper.searchSubjectsBalance(oldbalances, s);
                if (balance == null) {
                    cFinanceSubjectsBalance.setSyye(new BigDecimal("0.00"));
                } else {
                    cFinanceSubjectsBalance.setSyye(balance.getByye());
                }

                StFinanceSubjects subject = FinanceComputeHelper.searchSubjects(stFinanceSubjects, s);
                if (subject == null) {
                    continue;
                }
                List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = FinanceComputeHelper.searchRecordingVoucher(vouchers, s);

                BigDecimal JFJE = new BigDecimal("0.00");
                BigDecimal DFJE = new BigDecimal("0.00");
                for (StFinanceRecordingVoucher stFinanceRecordingVoucher : stFinanceRecordingVouchers) {
                    CFinanceRecordingVoucherExtension extension = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension();
                    if ("02".equals(extension.getKmyefx())) {
                        JFJE = JFJE.add(stFinanceRecordingVoucher.getJffse());
                        DFJE = DFJE.add(stFinanceRecordingVoucher.getDffse());
                    } else {
                        JFJE = JFJE.add(stFinanceRecordingVoucher.getDffse());
                        DFJE = DFJE.add(stFinanceRecordingVoucher.getJffse());
                    }
                }
                JFJE = JFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
                DFJE = DFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
                cFinanceSubjectsBalance.setByzj(JFJE);
                cFinanceSubjectsBalance.setByjs(DFJE);

                cFinanceSubjectsBalance.setKmbh(subject.getKmbh());
                cFinanceSubjectsBalance.setKmmc(subject.getKmmc());
                cFinanceSubjectsBalance.setKmjb(subject.getKmjb());
                cFinanceSubjectsBalance.setKmyefx(subject.getKmyefx());
                cFinanceSubjectsBalances.add(cFinanceSubjectsBalance);
            }
            cFinanceSubjectsBalances = computBalance(cFinanceSubjectsBalances);
            List<SubjectsBalance> res = new ArrayList<>();
            for (CFinanceSubjectsBalance balance : cFinanceSubjectsBalances) {
                SubjectsBalance s = new SubjectsBalance();
                s.setKMMC(balance.getKmmc());
                s.setKMJB(balance.getKmjb().intValue());
                s.setKMBH(balance.getKmbh());
                s.setBYYE(String.valueOf(balance.getByye()));
                s.setSYYE(String.valueOf(balance.getSyye()));
                s.setBYZJ(String.valueOf(balance.getByzj()));
                s.setBYJS(String.valueOf(balance.getByjs()));
                res.add(s);
            }
            return computSubjectsBalance(res);
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
    }

    @Override
    public List<BookDetails> getBooksDetails(String KMBH, String KJQJKS, String KJQJJS, boolean BHWJZPZ) {
        try {
            List<BookDetails> res = new ArrayList<>();
            List<CFinanceAccountPeriod> cFinanceAccountPeriods = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    if (BHWJZPZ) {
                        this.put("sfjs", BHWJZPZ);
                    }
                }
            }).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.between("kjqj", KJQJKS, KJQJJS));
                }
            }).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cFinanceAccountPeriods.size() <= 0) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "会计期间不存在");
            }
            Date ksdate = cFinanceAccountPeriods.get(0).getQsrq();
            Date jsdate = new Date(cFinanceAccountPeriods.get(cFinanceAccountPeriods.size() - 1).getJiezrq().getTime() + 86399 * 1000);//精确到到最后一天最后一秒

            List<StFinanceRecordingVoucher> vouchers = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
                if (BHWJZPZ) {
                    this.put("cFinanceRecordingVoucherExtension.sfjz", BHWJZPZ);
                }
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.like("kmbh", KMBH + "%"));
                }
            }).betweenDate(ksdate, jsdate).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            List<CFinanceSubjectsBalance> oldbalances = getOldPeriod(KJQJKS, "yyyyMM");

            CFinanceSubjectsBalance KSbalance = FinanceComputeHelper.searchSubjectsBalance(oldbalances, KMBH);
            if (KSbalance == null) {
                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该科目上月未结账");
            }
            BookDetails QCYE = new BookDetails();
            BigDecimal qcdf = KSbalance.getByjs();
            BigDecimal qcjf = KSbalance.getByzj();
            QCYE.setDFFSE("");
            QCYE.setJFFSE("");
            QCYE.setZhaiYao("期初余额");
            QCYE.setYuE(String.valueOf(KSbalance.getByye()));
            QCYE.setYEFX(KSbalance.getKmyefx());
            res.add(QCYE);//期初余额

            BigDecimal JFFSEHJ = new BigDecimal("0.00");
            BigDecimal DFFSEHJ = new BigDecimal("0.00");
            BigDecimal YuEtemp = new BigDecimal(QCYE.getYuE());
            for (StFinanceRecordingVoucher voucher : vouchers) {
                JFFSEHJ = JFFSEHJ.add(voucher.getJffse());
                DFFSEHJ = DFFSEHJ.add(voucher.getDffse());
                BookDetails bookDetails = new BookDetails();
                bookDetails.setDFFSE(String.valueOf(voucher.getDffse()));
                bookDetails.setJFFSE(String.valueOf(voucher.getJffse()));
                bookDetails.setZhaiYao(voucher.getZhaiYao());
                bookDetails.setJZPZH(voucher.getJzpzh());
                bookDetails.setJZRQ(DateUtil.date2Str(voucher.getJzrq(), "yyyy-MM-dd"));
                if ("02".equals(voucher.getcFinanceRecordingVoucherExtension().getKmyefx())) {
                    YuEtemp = YuEtemp.add(voucher.getJffse()).subtract(voucher.getDffse());
                } else {
                    YuEtemp = YuEtemp.add(voucher.getDffse()).subtract(voucher.getJffse());
                }
                bookDetails.setYuE(String.valueOf(YuEtemp));
                bookDetails.setYEFX(KSbalance.getKmyefx());
                res.add(bookDetails);
            }
            BigDecimal YuEHJ;
            if ("02".equals(QCYE.getYEFX())) {
                YuEHJ = new BigDecimal(QCYE.getYuE()).add(JFFSEHJ).subtract(DFFSEHJ);
            } else {
                YuEHJ = new BigDecimal(QCYE.getYuE()).add(DFFSEHJ).subtract(JFFSEHJ);
            }
            if (YuEHJ.abs().doubleValue() != YuEtemp.abs().doubleValue()) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "数据错误，计算失败");
            }
            DFFSEHJ = DFFSEHJ.setScale(2, BigDecimal.ROUND_HALF_UP);
            JFFSEHJ = JFFSEHJ.setScale(2, BigDecimal.ROUND_HALF_UP);
            YuEHJ = YuEHJ.setScale(2, BigDecimal.ROUND_HALF_UP);

            BookDetails BQHJ = new BookDetails();
            BQHJ.setDFFSE(String.valueOf(DFFSEHJ));
            BQHJ.setJFFSE(String.valueOf(JFFSEHJ));
            BQHJ.setZhaiYao("本期合计");
            BQHJ.setYuE(String.valueOf(YuEHJ));
            BQHJ.setYEFX(KSbalance.getKmyefx());

            res.add(BQHJ);//本期合计

            BigDecimal JFBNHJ = qcjf.add(JFFSEHJ);
            BigDecimal DFBNHJ = qcdf.add(DFFSEHJ);

            JFBNHJ = JFBNHJ.setScale(2, BigDecimal.ROUND_HALF_UP);
            DFBNHJ = DFBNHJ.setScale(2, BigDecimal.ROUND_HALF_UP);

            BookDetails BNLJ = new BookDetails();
            BNLJ.setDFFSE(String.valueOf(DFBNHJ));
            BNLJ.setJFFSE(String.valueOf(JFBNHJ));
            BNLJ.setZhaiYao("本年累计");
            BNLJ.setYuE(String.valueOf(YuEHJ));
            BNLJ.setYEFX(KSbalance.getKmyefx());
            res.add(BNLJ);//本年累计

            return res;
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
    }

    @Override
    public List<BookDetails> getBooksGeneral(String KMBH, String KJQJKS, String KJQJJS, boolean BHWJZPZ) {
        try {
            List<BookDetails> res = new ArrayList<>();
            List<CFinanceAccountPeriod> cFinanceAccountPeriods = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
                {
                    if (BHWJZPZ) {
                        this.put("sfjs", BHWJZPZ);
                    }
                }
            }).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.between("kjqj", KJQJKS, KJQJJS));
                }
            }).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cFinanceAccountPeriods.size() <= 0) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "会计期间不存在");
            }

            List<CFinanceSubjectsBalance> oldbalances = getOldPeriod(KJQJKS, "yyyyMM");

            CFinanceSubjectsBalance qcbalance = FinanceComputeHelper.searchSubjectsBalance(oldbalances, KMBH);
            if (qcbalance == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "科目上月未结账");
            }
            BookDetails QCYE = new BookDetails();
            QCYE.setDFFSE("");
            QCYE.setJFFSE("");
            QCYE.setZhaiYao("期初余额");
            QCYE.setYuE(String.valueOf(qcbalance.getByye()));
            QCYE.setYEFX(qcbalance.getKmyefx());
            res.add(QCYE);//期初余额
            List<CFinanceSubjectsBalance> cFinanceSubjectsBalances = new ArrayList<>();

            BigDecimal qcdffse = qcbalance.getByjs();
            BigDecimal qcjffse = qcbalance.getByzj();
            BigDecimal YuEHJ = new BigDecimal(QCYE.getYuE());

            List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(stFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            List<String> kmbhlist = new ArrayList<>();
            for (StFinanceSubjects s : stFinanceSubjects) {
                if (s.getKmbh().indexOf(KMBH) == 0) {
                    kmbhlist.add(s.getKmbh());
                }
            }
            Collections.sort(kmbhlist);
            int curmonth = 0;
            for (int i = 0; i < cFinanceAccountPeriods.size(); i++) {

                CFinanceAccountPeriod period = cFinanceAccountPeriods.get(i);
                CFinanceSubjectsBalance balance;
                if (period.isSfjs()) {
                    List<CFinanceSubjectsBalance> balances = period.getcFinanceSubjectsBalances();
                    balance = FinanceComputeHelper.searchSubjectsBalance(balances, KMBH);
                    if (balance == null) {
                        continue;
                    }
                } else {
                    if (curmonth >= 1) {
                        continue;
                    }
                    Date ksdate = period.getQsrq();
                    Date jsdate = new Date(period.getJiezrq().getTime() + 86399 * 1000);//精确到到最后一天最后一秒

                    for (String s : kmbhlist) {
                        CFinanceSubjectsBalance cFinanceSubjectsBalance = new CFinanceSubjectsBalance();
                        cFinanceSubjectsBalance.setSyye(new BigDecimal("0.00"));

                        StFinanceSubjects subject = FinanceComputeHelper.searchSubjects(stFinanceSubjects, s);

                        if (subject == null) {
                            continue;
                        }
                        List<StFinanceRecordingVoucher> stFinanceRecordingVouchers = DAOBuilder.instance(financeRecordingVoucherDAO)
                                .betweenDate(ksdate, jsdate).searchFilter(new HashMap<String, Object>() {
                                    {
                                        this.put("kmbh", subject.getKmbh());
//                        this.put("cFinanceRecordingVoucherExtension.sfjz", false);
                                    }
                                }).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
                                    @Override
                                    public void error(Exception e) {
                                        throw new ErrorException(e);
                                    }
                                });
                        BigDecimal JFJE = new BigDecimal("0.00");
                        BigDecimal DFJE = new BigDecimal("0.00");
                        for (StFinanceRecordingVoucher stFinanceRecordingVoucher : stFinanceRecordingVouchers) {
                            JFJE = JFJE.add(stFinanceRecordingVoucher.getJffse());
                            DFJE = DFJE.add(stFinanceRecordingVoucher.getDffse());
                        }
                        JFJE = JFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
                        DFJE = DFJE.setScale(2, BigDecimal.ROUND_HALF_UP);
                        cFinanceSubjectsBalance.setByzj(JFJE);
                        cFinanceSubjectsBalance.setByjs(DFJE);

                        cFinanceSubjectsBalance.setKmbh(subject.getKmbh());
                        cFinanceSubjectsBalance.setKmmc(subject.getKmmc());
                        cFinanceSubjectsBalance.setKmjb(subject.getKmjb());
                        cFinanceSubjectsBalance.setKmyefx(subject.getKmyefx());
                        cFinanceSubjectsBalances.add(cFinanceSubjectsBalance);
                    }
                    cFinanceSubjectsBalances = computBalance(cFinanceSubjectsBalances);
                    balance = FinanceComputeHelper.searchSubjectsBalance(cFinanceSubjectsBalances, KMBH);
                    curmonth++;
                    if (balance == null) {
                        continue;
                    }
                }
                BookDetails bookDetails = new BookDetails();
                bookDetails.setDFFSE(String.valueOf(balance.getByjs()));
                bookDetails.setJFFSE(String.valueOf(balance.getByzj()));
                bookDetails.setZhaiYao("");
                BigDecimal YUE1;
                if (QCYE.getYEFX().equals("02")) {
                    YUE1 = new BigDecimal(QCYE.getYuE()).add(balance.getByzj()).subtract(balance.getByjs());
                } else {
                    YUE1 = new BigDecimal(QCYE.getYuE()).add(balance.getByjs()).subtract(balance.getByzj());
                }
                bookDetails.setYuE(String.valueOf(YUE1));
                bookDetails.setJZRQ(period.getKjqj());
                bookDetails.setYEFX(balance.getKmyefx());
                res.add(bookDetails);

                BookDetails bqhj = new BookDetails();
                bqhj.setDFFSE(String.valueOf(balance.getByjs()));
                bqhj.setJFFSE(String.valueOf(balance.getByzj()));
                bqhj.setZhaiYao("当前合计");
                bqhj.setYuE(String.valueOf(YUE1));
                res.add(bqhj);

                qcdffse = qcdffse.add(balance.getByjs());
                qcjffse = qcjffse.add(balance.getByzj());

                if (i == 0) {
                    if (QCYE.getYEFX().equals("02")) {
                        YuEHJ = YuEHJ.add(new BigDecimal(bqhj.getJFFSE())).subtract(new BigDecimal(bqhj.getDFFSE()));
                    } else {
                        YuEHJ = YuEHJ.add(new BigDecimal(bqhj.getDFFSE())).subtract(new BigDecimal(bqhj.getJFFSE()));
                    }

                } else {
                    if (balance.getKmyefx().equals("02")) {
                        YuEHJ = YuEHJ.add(new BigDecimal(bqhj.getJFFSE())).subtract(new BigDecimal(bqhj.getDFFSE()));
                    } else {
                        YuEHJ = YuEHJ.add(new BigDecimal(bqhj.getDFFSE())).subtract(new BigDecimal(bqhj.getJFFSE()));
                    }
                }

                BookDetails bqlj = new BookDetails();
                bqlj.setDFFSE(String.valueOf(qcdffse));
                bqlj.setJFFSE(String.valueOf(qcjffse));
                bqlj.setZhaiYao("本期累计");
                bqlj.setYuE(String.valueOf(YuEHJ));
                res.add(bqlj);
            }
            return res;
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
    }

    /**
     * 获取上月会计期间中的所有科目余额信息
     *
     * @param KJQJ   当前时间
     * @param Format 时间格式
     * @return
     * @throws ParseException
     */
    private List<CFinanceSubjectsBalance> getOldPeriod(String KJQJ, String Format) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.str2Date(Format, KJQJ));
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String KJQJUP = format.format(calendar.getTime());

        CFinanceAccountPeriod cFinanceAccountPeriodold = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("kjqj", KJQJUP);
            }
        }).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        List<CFinanceSubjectsBalance> oldbalances;
        if (cFinanceAccountPeriodold != null) {
            if (!cFinanceAccountPeriodold.isSfjs()) {
                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "上月未结账");
            }
            oldbalances = cFinanceAccountPeriodold.getcFinanceSubjectsBalances();
            if (oldbalances == null || oldbalances.size() <= 0) {
                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "上月未结账");
            }
        } else {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "上月会计期间不存在");
        }
        return oldbalances;
    }

    /**
     * 计算余额信息
     *
     * @param subjectsBalanceList
     * @return
     */
    private List<CFinanceSubjectsBalance> computBalance(List<CFinanceSubjectsBalance> subjectsBalanceList) {
        List<CFinanceSubjectsBalance> res = new ArrayList<>();
        for (CFinanceSubjectsBalance c : subjectsBalanceList) {
            switch (c.getKmjb().intValue()) {
                case 1:
                    List<CFinanceSubjectsBalance> balances2 = FinanceComputeHelper.filterBalance(subjectsBalanceList, c.getKmbh(), 2);
                    List<CFinanceSubjectsBalance> balances3 = FinanceComputeHelper.filterBalance(subjectsBalanceList, c.getKmbh(), 3);
                    BigDecimal SYYE = c.getSyye();
                    BigDecimal BYZJ = c.getByzj();
                    BigDecimal BYJS = c.getByjs();
                    for (CFinanceSubjectsBalance balance : balances2) {
                        BYZJ = BYZJ.add(balance.getByzj());
                        BYJS = BYJS.add(balance.getByjs());
                    }
                    for (CFinanceSubjectsBalance b : balances3) {
                        BYZJ = BYZJ.add(b.getByzj());
                        BYJS = BYJS.add(b.getByjs());
                    }
                    BigDecimal BYYE = SYYE.add(BYZJ).subtract(BYJS);
                    SYYE = SYYE.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYZJ = BYZJ.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYJS = BYJS.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYYE = BYYE.setScale(2, BigDecimal.ROUND_HALF_UP);
                    c.setSyye(SYYE);
                    c.setByzj(BYZJ);
                    c.setByjs(BYJS);
                    c.setByye(BYYE);
                    res.add(c);
                    break;
                case 2:
                    List<CFinanceSubjectsBalance> balances23 = FinanceComputeHelper.filterBalance(subjectsBalanceList, c.getKmbh(), 3);
                    BigDecimal SYYE2 = c.getSyye();
                    BigDecimal BYZJ2 = c.getByzj();
                    BigDecimal BYJS2 = c.getByjs();
                    for (CFinanceSubjectsBalance b : balances23) {
                        BYZJ2 = BYZJ2.add(b.getByzj());
                        BYJS2 = BYJS2.add(b.getByjs());
                    }
                    BigDecimal BYYE2 = SYYE2.add(BYZJ2).subtract(BYJS2);

                    SYYE2 = SYYE2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYZJ2 = BYZJ2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYJS2 = BYJS2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BYYE2 = BYYE2.setScale(2, BigDecimal.ROUND_HALF_UP);
                    c.setSyye(SYYE2);
                    c.setByzj(BYZJ2);
                    c.setByjs(BYJS2);
                    c.setByye(BYYE2);
                    res.add(c);
                    break;
                case 3:
                    BigDecimal SYYE3 = c.getSyye();
                    BigDecimal BYZJ3 = c.getByzj();
                    BigDecimal BYJS3 = c.getByjs();

                    BigDecimal BYYE3 = SYYE3.add(BYZJ3).subtract(BYJS3);

                    BYYE3 = BYYE3.setScale(2, BigDecimal.ROUND_HALF_UP);
                    c.setByye(BYYE3);
                    res.add(c);
                    break;
                default:
                    break;
            }
        }
        return res;
    }

    /**
     * 获取科目编号
     *
     * @param KMBH  原科目编号
     * @param YHZHH 银行专户号
     * @return
     */
    private String getKMBH(String KMBH, String YHZHH, String YHDM) {
        String res;
        //住房公积金存款、增值收益存款、个人委托贷款
        if (KMBH.equals("12101")) {
            if (StringUtil.notEmpty(YHDM)) {
                BankToSubject subject = BankToSubject.getSubject(YHDM);
                if (subject != null) {
                    return subject.getGrwtcode();
                } else {
                    return KMBH;
                }
            } else {
                return KMBH;
            }
        }
        if (KMBH.equals("12201")) {
            if (StringUtil.notEmpty(YHDM)) {
                BankToSubject subject = BankToSubject.getSubject(YHDM);
                if (subject != null) {
                    return subject.getGryqcode();
                } else {
                    return KMBH;
                }
            } else {
                return KMBH;
            }
        }
        if (KMBH.equals("21904")) {
            if (StringUtil.notEmpty(YHDM)) {
                BankToSubject subject = BankToSubject.getSubject(YHDM);
                if (subject != null) {
                    return subject.getGjsxf();
                } else {
                    return KMBH;
                }
            } else {
                return KMBH;
            }
        }
        if (KMBH.equals("21905")) {
            if (StringUtil.notEmpty(YHDM)) {
                BankToSubject subject = BankToSubject.getSubject(YHDM);
                if (subject != null) {
                    return subject.getWdsxf();
                } else {
                    return KMBH;
                }
            } else {
                return KMBH;
            }
        }
        if (KMBH.equals("40103")){
            return "4010301";
        }
        if (KMBH.equals("40105")){
            return "4010501";
        }
        if (KMBH.equals("101") || KMBH.equals("102")) {
            if (StringUtil.notEmpty(YHZHH)) {
                try {
                    CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(YHZHH);
                    if (centerAccountInfo != null) {
                        String k = centerAccountInfo.getKMBH();
                        if (k.indexOf(KMBH) == 0) {
                            return centerAccountInfo.getKMBH();
                        } else {
                            return KMBH;
                        }
                    } else {
                        return KMBH;
                    }
                } catch (ErrorException e) {
                    return KMBH;
                }
            } else {
                return KMBH;
            }
        } else {
            return KMBH;
        }
    }

    private List<SubjectsBalance> computSubjectsBalance(List<SubjectsBalance> subjectsBalanceList) {
        List<SubjectsBalance> res = new ArrayList<>();
        BigDecimal ZCXJ = new BigDecimal("0.00");//资产小计
        BigDecimal FZXJ = new BigDecimal("0.00");//负债小计
        BigDecimal QYXJ = new BigDecimal("0.00");//权益小计
        BigDecimal SRXJ = new BigDecimal("0.00");//收入小计
        BigDecimal FYXJ = new BigDecimal("0.00");//费用小计
        for (SubjectsBalance s : subjectsBalanceList) {
            if (s.getKMJB() == 1) {
                if (s.getKMBH().indexOf("1") == 0) {
                    ZCXJ = ZCXJ.add(new BigDecimal(s.getBYYE()));
                } else if (s.getKMBH().indexOf("2") == 0) {
                    SubjectsBalance ZCXJBalance = new SubjectsBalance();
                    ZCXJBalance.setKMBH("资产小计");
                    ZCXJBalance.setKMJB(0);
                    ZCXJBalance.setBYYE(String.valueOf(ZCXJ));
                    if (!res.contains(ZCXJBalance)) {
                        res.add(ZCXJBalance);
                    }
                    FZXJ = FZXJ.add(new BigDecimal(s.getBYYE()));
                } else if (s.getKMBH().indexOf("3") == 0) {
                    SubjectsBalance FZXJBalance = new SubjectsBalance();
                    FZXJBalance.setKMBH("负债小计");
                    FZXJBalance.setKMJB(0);
                    FZXJBalance.setBYYE(String.valueOf(FZXJ));
                    if (!res.contains(FZXJBalance)) {
                        res.add(FZXJBalance);
                    }
                    QYXJ = QYXJ.add(new BigDecimal(s.getBYYE()));
                } else if (s.getKMBH().indexOf("401") == 0) {
                    SubjectsBalance QYXJBalance = new SubjectsBalance();
                    QYXJBalance.setKMBH("权益小计");
                    QYXJBalance.setKMJB(0);
                    QYXJBalance.setBYYE(String.valueOf(QYXJ));
                    if (!res.contains(QYXJBalance)) {
                        res.add(QYXJBalance);
                    }
                    SRXJ = SRXJ.add(new BigDecimal(s.getBYYE()));
                } else if (s.getKMBH().indexOf("411") == 0) {
                    SubjectsBalance SRXJBalance = new SubjectsBalance();
                    SRXJBalance.setKMBH("收入小计");
                    SRXJBalance.setKMJB(0);
                    SRXJBalance.setBYYE(String.valueOf(SRXJ));
                    if (!res.contains(SRXJBalance)) {
                        res.add(SRXJBalance);
                    }
                    FYXJ = FYXJ.add(new BigDecimal(s.getBYYE()));
                }
            }
            res.add(s);
        }
        SubjectsBalance FYJBalance = new SubjectsBalance();
        FYJBalance.setKMBH("费用小计");
        FYJBalance.setKMJB(0);
        FYJBalance.setBYYE(String.valueOf(FYXJ));
        res.add(FYJBalance);

        SubjectsBalance ZCJFYBalance = new SubjectsBalance();
        ZCJFYBalance.setKMBH("【资产+费用】合计");
        ZCJFYBalance.setKMJB(0);
        ZCJFYBalance.setBYYE(String.valueOf(ZCXJ.add(FYXJ)));
        res.add(ZCJFYBalance);

        SubjectsBalance FZJSRJQYBalance = new SubjectsBalance();
        FZJSRJQYBalance.setKMBH("【负债+收入+权益】合计");
        FZJSRJQYBalance.setKMJB(0);
        FZJSRJQYBalance.setBYYE(String.valueOf(FZXJ.add(SRXJ).add(QYXJ)));
        res.add(FZJSRJQYBalance);

        return res;
    }


}

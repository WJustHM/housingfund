package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.bean.center.AccTransDetailQueryIn;
import com.handge.housingfund.common.service.bank.bean.center.AccTransDetailQueryOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.finance.IAccChangeNoticeService;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.model.*;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.ICBankAccChangeNoticeDAO;
import com.handge.housingfund.database.dao.ICFinanceRecordUnitDAO;
import com.handge.housingfund.database.entities.CBankAccChangeNotice;
import com.handge.housingfund.database.entities.CFinanceRecordUnit;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 账户变动通知
 * Created by Administrator on 2017/9/12.
 */
@Component
public class AccChangeNoticeService implements IAccChangeNoticeService {
    @Autowired
    private ICBankAccChangeNoticeDAO icBankAccChangeNoticeDAO;
    @Autowired
    private ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;
    @Autowired
    private IBank iBank;
    @Autowired
    private IUploadImagesService iUploadImagesService;
    @Autowired
    private ICFinanceRecordUnitDAO icFinanceRecordUnitDAO;
    @Autowired
    IFinanceTrader iFinanceTrader;

    @Override
    public PageRes<AccChangeNoticeModel> getAccChangeNotices(String zhhm, String sfzz, String summary, String begin, String end, BigDecimal FSE, String DSZH, String DSHM, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(zhhm)) filter.put("acct", zhhm);
        if (StringUtil.notEmpty(sfzz)) filter.put("is_make_acc", sfzz);
        if (StringUtil.notEmpty(DSZH)) filter.put("opponent_acct", DSZH);
        if (StringUtil.notEmpty(DSHM)) filter.put("opponent_name", DSHM);
        if (FSE != null) filter.put("amt", FSE);
        try {
            Date beginDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", begin);

            Date endDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", end);


            PageRes pageRes = new PageRes();

            List<CBankAccChangeNotice> cBankAccChangeNotices = DAOBuilder.instance(icBankAccChangeNoticeDAO).searchFilter(filter)
                    .betweenDate(beginDate, endDate).pageOption(pageRes, pageSize, pageNo)
                    .searchOption(SearchOption.FUZZY)
                    .orderOption("created_at", Order.DESC)
                    .extension(new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            if (StringUtil.notEmpty(summary)) {
                                criteria.add(Restrictions.or(Restrictions.like("summary", "%" + summary + "%"), Restrictions.like("remark", "%" + summary + "%")));
                            }
                        }
                    })
                    .getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                        }
                    });

            PageRes<AccChangeNoticeModel> accChangeNoticeModelPageRes = new PageRes<>();
            accChangeNoticeModelPageRes.setCurrentPage(pageRes.getCurrentPage());
            accChangeNoticeModelPageRes.setNextPageNo(pageRes.getNextPageNo());
            accChangeNoticeModelPageRes.setPageCount(pageRes.getPageCount());
            accChangeNoticeModelPageRes.setPageSize(pageRes.getPageSize());
            accChangeNoticeModelPageRes.setTotalCount(pageRes.getTotalCount());

            ArrayList<AccChangeNoticeModel> accChangeNoticeModels = new ArrayList<>();
            for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNotices) {
                AccChangeNoticeModel accChangeNoticeModel = new AccChangeNoticeModel();
                accChangeNoticeModel.setAcct(cBankAccChangeNotice.getAcct());
                accChangeNoticeModel.setAmt(cBankAccChangeNotice.getAmt() != null ? cBankAccChangeNotice.getAmt().toPlainString() : null);
                accChangeNoticeModel.setAvailable_amt(cBankAccChangeNotice.getAvailable_amt() != null ? cBankAccChangeNotice.getAvailable_amt().toPlainString() : null);
                accChangeNoticeModel.setBalance(cBankAccChangeNotice.getBalance() != null ? cBankAccChangeNotice.getBalance().toPlainString() : null);
                accChangeNoticeModel.setBook_list_no(cBankAccChangeNotice.getBook_list_no());
                accChangeNoticeModel.setBook_no(cBankAccChangeNotice.getBook_no());
                accChangeNoticeModel.setBus_seq_no(cBankAccChangeNotice.getBus_seq_no());
                accChangeNoticeModel.setCurr_iden(cBankAccChangeNotice.getCurr_iden());
                accChangeNoticeModel.setCurr_no(cBankAccChangeNotice.getCurr_no());
                accChangeNoticeModel.setDate(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyy-MM-dd"));
                accChangeNoticeModel.setHost_seq_no(cBankAccChangeNotice.getHost_seq_no());
                accChangeNoticeModel.setId(cBankAccChangeNotice.getId());
                accChangeNoticeModel.setIs_make_acc(cBankAccChangeNotice.getIs_make_acc());
                accChangeNoticeModel.setNotice_no(cBankAccChangeNotice.getNotice_no());
                accChangeNoticeModel.setOpen_bank_no("null".equals(cBankAccChangeNotice.getOpen_bank_no()) ? null : cBankAccChangeNotice.getOpen_bank_no());
                accChangeNoticeModel.setOpponent_acct(cBankAccChangeNotice.getOpponent_acct());
                accChangeNoticeModel.setOpponent_bank_no("null".equals(cBankAccChangeNotice.getOpponent_bank_no()) ? null : cBankAccChangeNotice.getOpponent_bank_no());
                accChangeNoticeModel.setOpponent_name(cBankAccChangeNotice.getOpponent_name());
                accChangeNoticeModel.setOverdraft(cBankAccChangeNotice.getOverdraft() != null ? cBankAccChangeNotice.getOverdraft().toPlainString() : null);
                accChangeNoticeModel.setRedo("null".equals(cBankAccChangeNotice.getRedo()) ? null : cBankAccChangeNotice.getRedo());
                accChangeNoticeModel.setRemark("null".equals(cBankAccChangeNotice.getRemark()) ? null : cBankAccChangeNotice.getRemark());
                accChangeNoticeModel.setSummary(cBankAccChangeNotice.getSummary());
                accChangeNoticeModel.setTime(cBankAccChangeNotice.getTime());
                accChangeNoticeModel.setTx_code("null".equals(cBankAccChangeNotice.getTx_code()) ? null : cBankAccChangeNotice.getTx_code());
                accChangeNoticeModel.setVoucher_no("null".equals(cBankAccChangeNotice.getVoucher_no()) ? null : cBankAccChangeNotice.getVoucher_no());
                accChangeNoticeModel.setVoucher_type("null".equals(cBankAccChangeNotice.getVoucher_type()) ? null : cBankAccChangeNotice.getVoucher_type());
                accChangeNoticeModel.setJzpzh(cBankAccChangeNotice.getJzpzh());
                accChangeNoticeModel.setSmwj(cBankAccChangeNotice.getSmwj());

                accChangeNoticeModels.add(accChangeNoticeModel);
            }

            accChangeNoticeModelPageRes.setResults(accChangeNoticeModels);
            return accChangeNoticeModelPageRes;
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
        }
    }

    @Override
    public PageResNew<AccChangeNoticeModel> getAccChangeNotices(String zhhm, String sfzz, String summary, String begin, String end, BigDecimal FSE, String DSZH, String DSHM, String marker, String action, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(zhhm)) filter.put("acct", zhhm);
        if (StringUtil.notEmpty(sfzz)) filter.put("is_make_acc", sfzz);
        if (StringUtil.notEmpty(summary)) filter.put("summary", summary);
        if (StringUtil.notEmpty(DSZH)) filter.put("opponent_acct", DSZH);
        if (StringUtil.notEmpty(DSHM)) filter.put("opponent_name", DSHM);
        if (FSE != null) filter.put("amt", FSE);

        List<CBankAccChangeNotice> cBankAccChangeNotices = DAOBuilder.instance(icBankAccChangeNoticeDAO).searchFilter(filter)
                .pageOption(marker, action, pageSize)
                .searchOption(SearchOption.FUZZY)
                .orderOption("created_at", Order.DESC)
                .extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
                        if (StringUtil.notEmpty(begin)) {
                            try {
                                Date beginDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", begin);
                                criteria.add(Restrictions.ge("date", beginDate));
                            } catch (ParseException e) {
                                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
                            }
                        }
                        if (StringUtil.notEmpty(end)) {
                            try {
                                Date endDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", end);
                                criteria.add(Restrictions.le("date", endDate));
                            } catch (ParseException e) {
                                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
                            }
                        }
                    }
                })
                .getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        PageResNew<AccChangeNoticeModel> accChangeNoticeModelPageRes = new PageResNew<>();

        ArrayList<AccChangeNoticeModel> accChangeNoticeModels = new ArrayList<>();
        for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNotices) {
            AccChangeNoticeModel accChangeNoticeModel = new AccChangeNoticeModel();
            accChangeNoticeModel.setAcct(cBankAccChangeNotice.getAcct());
            accChangeNoticeModel.setAmt(cBankAccChangeNotice.getAmt() != null ? cBankAccChangeNotice.getAmt().toPlainString() : null);
            accChangeNoticeModel.setAvailable_amt(cBankAccChangeNotice.getAvailable_amt() != null ? cBankAccChangeNotice.getAvailable_amt().toPlainString() : null);
            accChangeNoticeModel.setBalance(cBankAccChangeNotice.getBalance() != null ? cBankAccChangeNotice.getBalance().toPlainString() : null);
            accChangeNoticeModel.setBook_list_no(cBankAccChangeNotice.getBook_list_no());
            accChangeNoticeModel.setBook_no(cBankAccChangeNotice.getBook_no());
            accChangeNoticeModel.setBus_seq_no(cBankAccChangeNotice.getBus_seq_no());
            accChangeNoticeModel.setCurr_iden(cBankAccChangeNotice.getCurr_iden());
            accChangeNoticeModel.setCurr_no(cBankAccChangeNotice.getCurr_no());
            accChangeNoticeModel.setDate(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyy-MM-dd"));
            accChangeNoticeModel.setHost_seq_no(cBankAccChangeNotice.getHost_seq_no());
            accChangeNoticeModel.setId(cBankAccChangeNotice.getId());
            accChangeNoticeModel.setIs_make_acc(cBankAccChangeNotice.getIs_make_acc());
            accChangeNoticeModel.setNotice_no(cBankAccChangeNotice.getNotice_no());
            accChangeNoticeModel.setOpen_bank_no("null".equals(cBankAccChangeNotice.getOpen_bank_no()) ? null : cBankAccChangeNotice.getOpen_bank_no());
            accChangeNoticeModel.setOpponent_acct(cBankAccChangeNotice.getOpponent_acct());
            accChangeNoticeModel.setOpponent_bank_no("null".equals(cBankAccChangeNotice.getOpponent_bank_no()) ? null : cBankAccChangeNotice.getOpponent_bank_no());
            accChangeNoticeModel.setOpponent_name(cBankAccChangeNotice.getOpponent_name());
            accChangeNoticeModel.setOverdraft(cBankAccChangeNotice.getOverdraft() != null ? cBankAccChangeNotice.getOverdraft().toPlainString() : null);
            accChangeNoticeModel.setRedo("null".equals(cBankAccChangeNotice.getRedo()) ? null : cBankAccChangeNotice.getRedo());
            accChangeNoticeModel.setRemark("null".equals(cBankAccChangeNotice.getRemark()) ? null : cBankAccChangeNotice.getRemark());
            accChangeNoticeModel.setSummary(cBankAccChangeNotice.getSummary());
            accChangeNoticeModel.setTime(cBankAccChangeNotice.getTime());
            accChangeNoticeModel.setTx_code("null".equals(cBankAccChangeNotice.getTx_code()) ? null : cBankAccChangeNotice.getTx_code());
            accChangeNoticeModel.setVoucher_no("null".equals(cBankAccChangeNotice.getVoucher_no()) ? null : cBankAccChangeNotice.getVoucher_no());
            accChangeNoticeModel.setVoucher_type("null".equals(cBankAccChangeNotice.getVoucher_type()) ? null : cBankAccChangeNotice.getVoucher_type());
            accChangeNoticeModel.setJzpzh(cBankAccChangeNotice.getJzpzh());
            accChangeNoticeModel.setSmwj(cBankAccChangeNotice.getSmwj());
            accChangeNoticeModels.add(accChangeNoticeModel);
        }
        accChangeNoticeModelPageRes.setResults(action, accChangeNoticeModels);
        return accChangeNoticeModelPageRes;
    }

    @Override
    public AccChangeNoticeModel getAccChangeNotice(String id) {

        CBankAccChangeNotice cBankAccChangeNotice = DAOBuilder.instance(icBankAccChangeNoticeDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cBankAccChangeNotice == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该通知(" + id + ")的相关信息");
        }

        AccChangeNoticeModel accChangeNoticeModel = new AccChangeNoticeModel();
        accChangeNoticeModel.setAcct(cBankAccChangeNotice.getAcct());

        try {
            CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(cBankAccChangeNotice.getAcct());
            accChangeNoticeModel.setYhmc(centerAccountInfo.getBank_name());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        accChangeNoticeModel.setAmt(cBankAccChangeNotice.getAmt() != null ? cBankAccChangeNotice.getAmt().toPlainString() : null);
        accChangeNoticeModel.setAvailable_amt(cBankAccChangeNotice.getAvailable_amt() != null ? cBankAccChangeNotice.getAvailable_amt().toPlainString() : null);
        accChangeNoticeModel.setBalance(cBankAccChangeNotice.getBalance() != null ? cBankAccChangeNotice.getBalance().toPlainString() : null);
        accChangeNoticeModel.setBook_list_no(cBankAccChangeNotice.getBook_list_no());
        accChangeNoticeModel.setBook_no(cBankAccChangeNotice.getBook_no());
        accChangeNoticeModel.setBus_seq_no(cBankAccChangeNotice.getBus_seq_no());
        accChangeNoticeModel.setCurr_iden(cBankAccChangeNotice.getCurr_iden());
        accChangeNoticeModel.setCurr_no(cBankAccChangeNotice.getCurr_no());
        accChangeNoticeModel.setDate(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyy-MM-dd"));
        accChangeNoticeModel.setHost_seq_no(cBankAccChangeNotice.getHost_seq_no());
        accChangeNoticeModel.setId(cBankAccChangeNotice.getId());
        accChangeNoticeModel.setIs_make_acc(cBankAccChangeNotice.getIs_make_acc());
        accChangeNoticeModel.setNotice_no(cBankAccChangeNotice.getNotice_no());
        accChangeNoticeModel.setOpen_bank_no("null".equals(cBankAccChangeNotice.getOpen_bank_no()) ? null : cBankAccChangeNotice.getOpen_bank_no());
        accChangeNoticeModel.setOpponent_acct(cBankAccChangeNotice.getOpponent_acct());
        accChangeNoticeModel.setOpponent_bank_no("null".equals(cBankAccChangeNotice.getOpponent_bank_no()) ? null : cBankAccChangeNotice.getOpponent_bank_no());
        accChangeNoticeModel.setOpponent_name(cBankAccChangeNotice.getOpponent_name());
        accChangeNoticeModel.setOverdraft(cBankAccChangeNotice.getOverdraft() != null ? cBankAccChangeNotice.getOverdraft().toPlainString() : null);
        accChangeNoticeModel.setRedo("null".equals(cBankAccChangeNotice.getRedo()) ? null : cBankAccChangeNotice.getRedo());
        accChangeNoticeModel.setRemark("null".equals(cBankAccChangeNotice.getRemark()) ? null : cBankAccChangeNotice.getRemark());
        accChangeNoticeModel.setSummary(cBankAccChangeNotice.getSummary());
        accChangeNoticeModel.setTime(cBankAccChangeNotice.getTime());
        accChangeNoticeModel.setTx_code("null".equals(cBankAccChangeNotice.getTx_code()) ? null : cBankAccChangeNotice.getTx_code());
        accChangeNoticeModel.setVoucher_no("null".equals(cBankAccChangeNotice.getVoucher_no()) ? null : cBankAccChangeNotice.getVoucher_no());
        accChangeNoticeModel.setVoucher_type("null".equals(cBankAccChangeNotice.getVoucher_type()) ? null : cBankAccChangeNotice.getVoucher_type());
        accChangeNoticeModel.setJzpzh(cBankAccChangeNotice.getJzpzh());
        accChangeNoticeModel.setSmwj(cBankAccChangeNotice.getSmwj());

        return accChangeNoticeModel;
    }

    @Override
    public PageRes<FinanceRecordUnitModel> getAccChangeNoticesByDWZH(String DWZH,
                                                                     String ZJLY,
                                                                     BigDecimal FSE,
                                                                     String JZPZH,
                                                                     String begin,
                                                                     String end,
                                                                     int pageNo,
                                                                     int pageSize) {
        PageRes pageRes = new PageRes();
        Date beginDate;
        try {
            beginDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", begin);
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
        }
        Date endDate;
        try {
            endDate = DateUtil.str2Date("yyyy-MM-dd HH:mm", end);
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
        }

        List<CFinanceRecordUnit> models = DAOBuilder.instance(icFinanceRecordUnitDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(ZJLY)) {
                this.put("zjly", ZJLY);
            }
            if (StringUtil.notEmpty(JZPZH)) {
                this.put("jzpzh", JZPZH);
            }
            this.put("dwzh", DWZH);
        }}).searchOption(SearchOption.FUZZY).pageOption(pageRes, pageSize, pageNo)
                .orderOption("created_at", Order.DESC)
                .betweenDate(beginDate, endDate)
                .extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
                        if (FSE != null) {
                            criteria.add(Restrictions.or(Restrictions.eq("fse", FSE), Restrictions.eq("fse", FSE.negate())));
                        }
                    }
                })
                .getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        PageRes<FinanceRecordUnitModel> accChangeNoticeModelPageRes = new PageRes<>();
        accChangeNoticeModelPageRes.setCurrentPage(pageRes.getCurrentPage());
        accChangeNoticeModelPageRes.setNextPageNo(pageRes.getNextPageNo());
        accChangeNoticeModelPageRes.setPageCount(pageRes.getPageCount());
        accChangeNoticeModelPageRes.setPageSize(pageRes.getPageSize());
        accChangeNoticeModelPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<FinanceRecordUnitModel> financeRecordUnitModels = new ArrayList<>();
        for (CFinanceRecordUnit unit : models) {
            FinanceRecordUnitModel model = new FinanceRecordUnitModel();
            model.setDwzh(unit.getDwzh());
            model.setFse(unit.getFse());
            model.setFSSJ(DateUtil.date2Str(unit.getCreated_at(), "yyyy-MM-dd HH:mm"));
            model.setJzpzh(unit.getJzpzh());
            model.setRemark(unit.getRemark());
            model.setSummary(unit.getSummary());
            model.setZjly(unit.getZjly());
            model.setWftye(unit.getWftye());
            financeRecordUnitModels.add(model);
        }
        accChangeNoticeModelPageRes.setResults(financeRecordUnitModels);
        return accChangeNoticeModelPageRes;
    }

    @Override
    public AccChangeNoticeModel putAccChangeNotice(TokenContext context, String id, AccChangeNoticeSMWJ accChangeNoticeFile) {
        if (!iUploadImagesService.validateUploadFile(UploadFileModleType.财务.getCode(), UploadFileBusinessType.到账通知单.getCode(), accChangeNoticeFile.getSMWJ())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料缺失");
        }
        CBankAccChangeNotice cBankAccChangeNotice = DAOBuilder.instance(icBankAccChangeNoticeDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cBankAccChangeNotice == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该通知(" + id + ")的相关信息");
        }
        cBankAccChangeNotice.setSmwj(accChangeNoticeFile.getSMWJ());

        DAOBuilder.instance(icBankAccChangeNoticeDAO).entity(cBankAccChangeNotice).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        AccChangeNoticeModel accChangeNoticeModel = new AccChangeNoticeModel();
        accChangeNoticeModel.setAcct(cBankAccChangeNotice.getAcct());

        try {
            CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(cBankAccChangeNotice.getAcct());
            accChangeNoticeModel.setYhmc(centerAccountInfo.getBank_name());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        accChangeNoticeModel.setAmt(cBankAccChangeNotice.getAmt() != null ? cBankAccChangeNotice.getAmt().toPlainString() : null);
        accChangeNoticeModel.setAvailable_amt(cBankAccChangeNotice.getAvailable_amt() != null ? cBankAccChangeNotice.getAvailable_amt().toPlainString() : null);
        accChangeNoticeModel.setBalance(cBankAccChangeNotice.getBalance() != null ? cBankAccChangeNotice.getBalance().toPlainString() : null);
        accChangeNoticeModel.setBook_list_no(cBankAccChangeNotice.getBook_list_no());
        accChangeNoticeModel.setBook_no(cBankAccChangeNotice.getBook_no());
        accChangeNoticeModel.setBus_seq_no(cBankAccChangeNotice.getBus_seq_no());
        accChangeNoticeModel.setCurr_iden(cBankAccChangeNotice.getCurr_iden());
        accChangeNoticeModel.setCurr_no(cBankAccChangeNotice.getCurr_no());
        accChangeNoticeModel.setDate(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyy-MM-dd"));
        accChangeNoticeModel.setHost_seq_no(cBankAccChangeNotice.getHost_seq_no());
        accChangeNoticeModel.setId(cBankAccChangeNotice.getId());
        accChangeNoticeModel.setIs_make_acc(cBankAccChangeNotice.getIs_make_acc());
        accChangeNoticeModel.setNotice_no(cBankAccChangeNotice.getNotice_no());
        accChangeNoticeModel.setOpen_bank_no("null".equals(cBankAccChangeNotice.getOpen_bank_no()) ? null : cBankAccChangeNotice.getOpen_bank_no());
        accChangeNoticeModel.setOpponent_acct(cBankAccChangeNotice.getOpponent_acct());
        accChangeNoticeModel.setOpponent_bank_no("null".equals(cBankAccChangeNotice.getOpponent_bank_no()) ? null : cBankAccChangeNotice.getOpponent_bank_no());
        accChangeNoticeModel.setOpponent_name(cBankAccChangeNotice.getOpponent_name());
        accChangeNoticeModel.setOverdraft(cBankAccChangeNotice.getOverdraft() != null ? cBankAccChangeNotice.getOverdraft().toPlainString() : null);
        accChangeNoticeModel.setRedo("null".equals(cBankAccChangeNotice.getRedo()) ? null : cBankAccChangeNotice.getRedo());
        accChangeNoticeModel.setRemark("null".equals(cBankAccChangeNotice.getRemark()) ? null : cBankAccChangeNotice.getRemark());
        accChangeNoticeModel.setSummary(cBankAccChangeNotice.getSummary());
        accChangeNoticeModel.setTime(cBankAccChangeNotice.getTime());
        accChangeNoticeModel.setTx_code("null".equals(cBankAccChangeNotice.getTx_code()) ? null : cBankAccChangeNotice.getTx_code());
        accChangeNoticeModel.setVoucher_no("null".equals(cBankAccChangeNotice.getVoucher_no()) ? null : cBankAccChangeNotice.getVoucher_no());
        accChangeNoticeModel.setVoucher_type("null".equals(cBankAccChangeNotice.getVoucher_type()) ? null : cBankAccChangeNotice.getVoucher_type());
        accChangeNoticeModel.setJzpzh(cBankAccChangeNotice.getJzpzh());
        accChangeNoticeModel.setSmwj(cBankAccChangeNotice.getSmwj());

        return accChangeNoticeModel;

    }

    @Override
    public List<CompareBooks> getCompareBooks(String node, String KHBH, String YHZH, String CXRQKS, String CXRQJS) {
        try {
            Date pzrqks = new Date();
            Date pzrqjs = new Date();
            if (StringUtil.notEmpty(CXRQKS)) {
                pzrqks = DateUtil.str2Date("yyyy-MM-dd HH:mm", CXRQKS);
            }
            if (StringUtil.notEmpty(CXRQJS)) {
                pzrqjs = DateUtil.str2Date("yyyy-MM-dd HH:mm", CXRQJS);
            }

            //中心对账单
            List<CBankAccChangeNotice> cBankAccChangeNotices = getCBankAccChangeNotices(YHZH, pzrqks, pzrqjs);

            //银行对账单
            String txDateBegin = DateUtil.date2Str(pzrqks, "yyyyMMdd");
            String txDateEnd = DateUtil.date2Str(pzrqjs, "yyyyMMdd");
            List<Object> accChangeNoticeFiles = getAccTransDetail(node, KHBH, txDateBegin, txDateEnd);

            //调换顺序
            List<AccChangeNoticeFile> files = new ArrayList<>();
            for (Object file : accChangeNoticeFiles) {
                AccChangeNoticeFile accChangeNoticeFile = (AccChangeNoticeFile) file;
                files.add(0, accChangeNoticeFile);
            }

            List<CompareBooks> res = new ArrayList<>();
            boolean haszxdzd = false;
            if (cBankAccChangeNotices.size() > 0) {
                haszxdzd = true;
            }
            for (int i = 0; i < files.size(); i++) {
                AccChangeNoticeFile accChangeNoticeFile = files.get(i);

                if (accChangeNoticeFile.getAcct().equals(YHZH)) {
                    if (i == 0) {
                        CompareBooks compareBooks = new CompareBooks();
                        BookDetails details = new BookDetails();//银行对账单期初余额
                        details.setYEFX("02");
                        details.setJFFSE("");
                        details.setDFFSE("");
                        details.setJZPZH("");
                        details.setJZRQ(DateUtil.date2Str(DateUtil.str2Date("yyyyMMdd", accChangeNoticeFile.getDate()), "yyyy-MM-dd"));
                        details.setSFYC(false);
                        String YE;
                        if (accChangeNoticeFile.getAmt().compareTo(BigDecimal.ZERO) < 0) {
                            YE = String.valueOf(accChangeNoticeFile.getBalance().abs().add(accChangeNoticeFile.getAmt().abs()).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            YE = String.valueOf(accChangeNoticeFile.getBalance().abs().subtract(accChangeNoticeFile.getAmt().abs()).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        details.setYuE(YE);
                        details.setZhaiYao("期初余额");
                        compareBooks.setYHDZD(details);

                        if (haszxdzd) {
                            //中心对账单期初余额
                            String ye;
                            if (cBankAccChangeNotices.get(0).getAmt().compareTo(BigDecimal.ZERO) < 0) {
                                ye = String.valueOf(cBankAccChangeNotices.get(0).getBalance().abs().add(cBankAccChangeNotices.get(0).getAmt().abs()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else {
                                ye = String.valueOf(cBankAccChangeNotices.get(0).getBalance().abs().subtract(cBankAccChangeNotices.get(0).getAmt().abs()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                            try {
                                BookDetails bookDetails = (BookDetails) details.clone();
                                bookDetails.setYuE(ye);
                                bookDetails.setJZRQ(DateUtil.date2Str(cBankAccChangeNotices.get(0).getDate(), "yyyy-MM-dd"));
                                compareBooks.setZXDZD(bookDetails);
                                res.add(compareBooks);
                            } catch (CloneNotSupportedException e) {
                                throw new ErrorException(e);
                            }
                        } else {
                            BookDetails bookDetails = new BookDetails();
                            bookDetails.setYEFX("");
                            bookDetails.setJFFSE("");
                            bookDetails.setDFFSE("");
                            bookDetails.setJZRQ("");
                            bookDetails.setJZPZH("");
                            bookDetails.setSFYC(true);
                            bookDetails.setYuE("");
                            bookDetails.setZhaiYao("");
                            compareBooks.setZXDZD(bookDetails);
                            res.add(compareBooks);
                        }

                    }
                    CompareBooks compareBooks = new CompareBooks();
                    boolean sfyc = true;
                    boolean isfind = false;
                    BookDetails bookDetails = new BookDetails();//中心对账单
                    for (int j = 0; j < cBankAccChangeNotices.size(); j++) {
                        CBankAccChangeNotice cBankAccChangeNotice = cBankAccChangeNotices.get(j);
                        if (accChangeNoticeFile.getHostSeqNo().equals(cBankAccChangeNotice.getHost_seq_no())) {
                            isfind = true;
                            if (accChangeNoticeFile.getAmt().compareTo(cBankAccChangeNotice.getAmt()) == 0) {
                                if (accChangeNoticeFile.getBalance().compareTo(cBankAccChangeNotice.getBalance()) == 0) {
                                    sfyc = false;
                                }
                            }
                            if (cBankAccChangeNotice.getAmt().compareTo(BigDecimal.ZERO) < 0) {
                                bookDetails.setYEFX("03");
                                bookDetails.setJFFSE("0.00");
                                bookDetails.setDFFSE(String.valueOf(cBankAccChangeNotice.getAmt().abs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                            } else {
                                bookDetails.setYEFX("02");
                                bookDetails.setDFFSE("0.00");
                                bookDetails.setJFFSE(String.valueOf(cBankAccChangeNotice.getAmt().abs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                            }
                            bookDetails.setJZPZH(cBankAccChangeNotice.getJzpzh());
                            bookDetails.setJZRQ(DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyy-MM-dd"));
                            bookDetails.setSFYC(sfyc);
                            bookDetails.setYuE(String.valueOf(cBankAccChangeNotice.getBalance().abs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                            bookDetails.setZhaiYao(cBankAccChangeNotice.getSummary());
                        }
                    }
                    if (!isfind) {
                        bookDetails.setYEFX("");
                        bookDetails.setJFFSE("");
                        bookDetails.setDFFSE("");
                        bookDetails.setJZRQ("");
                        bookDetails.setJZPZH("");
                        bookDetails.setSFYC(true);
                        bookDetails.setYuE("");
                        bookDetails.setZhaiYao("");
                    }
                    compareBooks.setZXDZD(bookDetails);

                    BookDetails details = new BookDetails();//银行对账单
                    if (accChangeNoticeFile.getAmt().compareTo(BigDecimal.ZERO) < 0) {
                        details.setYEFX("03");
                        details.setJFFSE("0.00");
                        details.setDFFSE(String.valueOf(accChangeNoticeFile.getAmt().abs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                    } else {
                        details.setYEFX("02");
                        details.setDFFSE("0.00");
                        details.setJFFSE(String.valueOf(accChangeNoticeFile.getAmt().abs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                    }
                    details.setJZPZH("");
                    details.setJZRQ(DateUtil.date2Str(DateUtil.str2Date("yyyyMMdd", accChangeNoticeFile.getDate()), "yyyy-MM-dd"));
                    details.setSFYC(sfyc);
                    details.setYuE(String.valueOf(accChangeNoticeFile.getBalance().abs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                    details.setZhaiYao(accChangeNoticeFile.getSummary());
                    compareBooks.setYHDZD(details);
                    res.add(compareBooks);

                    if (i == accChangeNoticeFiles.size() - 1) {
                        if (haszxdzd && accChangeNoticeFile.getBalance().abs().compareTo(cBankAccChangeNotices.get(cBankAccChangeNotices.size() - 1).getBalance().abs()) == 0) {
                            sfyc = false;
                        }
                        CompareBooks books = new CompareBooks();
                        BookDetails d = new BookDetails();//银行对账单
                        d.setYEFX("02");
                        d.setJFFSE("");
                        d.setDFFSE("");
                        d.setJZPZH("");
                        d.setJZRQ("");
                        d.setSFYC(sfyc);
                        d.setYuE(String.valueOf(accChangeNoticeFile.getBalance().abs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        d.setZhaiYao("期末余额");
                        books.setYHDZD(d);
                        if (haszxdzd) {
                            try {
                                BookDetails d1 = (BookDetails) d.clone();
                                d1.setYuE(String.valueOf(cBankAccChangeNotices.get(cBankAccChangeNotices.size() - 1).getBalance().abs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                books.setZXDZD(d1);
                                res.add(books);
                            } catch (CloneNotSupportedException e) {
                                throw new ErrorException(e);
                            }
                        } else {
                            bookDetails = new BookDetails();
                            bookDetails.setYEFX("");
                            bookDetails.setJFFSE("");
                            bookDetails.setDFFSE("");
                            bookDetails.setJZRQ("");
                            bookDetails.setJZPZH("");
                            bookDetails.setSFYC(sfyc);
                            bookDetails.setYuE("");
                            bookDetails.setZhaiYao("");
                            compareBooks.setZXDZD(bookDetails);
                            res.add(compareBooks);
                        }
                    }
                }
            }

            //银行账户实时余额
            CompareBooks compareBooks = new CompareBooks();
            BookDetails details = new BookDetails();
            ActivedBalance balance = iFinanceTrader.getActivedBalance(YHZH);
            details.setYuE(balance.getAcctBal());
            compareBooks.setZXDZD(details);
            res.add(compareBooks);

            return res;
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ErrorException(e);
        }
    }

    private List<CBankAccChangeNotice> getCBankAccChangeNotices(String YHZH, Date pzrqks, Date pzrqjs) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("acct", YHZH);

        return DAOBuilder.instance(icBankAccChangeNoticeDAO).searchFilter(filter)
                .searchOption(SearchOption.REFINED)
                .orderOption("created_at", Order.ASC)
                .extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
                        criteria.add(Restrictions.ge("date", pzrqks));
                        criteria.add(Restrictions.le("date", pzrqjs));
                    }
                })
                .getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
                    }
                });
    }

    public ReconciliationInitBalance getReconciliationInitBalance(String acct, Date firstDay, Date lastDay) {
        ReconciliationInitBalance reconciliationInitBalance = new ReconciliationInitBalance();

        BigDecimal amt = icBankAccChangeNoticeDAO.getAmt(acct, firstDay, lastDay);
        reconciliationInitBalance.setCenterAcctBal(amt != null ? amt.toPlainString() : "-");

        return reconciliationInitBalance;
    }

    public List<Object> getAccTransDetail(String node, String khbh, String txDateBegin, String txDateEnd) {
        CenterHeadIn centerHeadIn = new CenterHeadIn();
        centerHeadIn.setReceiveNode(node);
        centerHeadIn.setOperNo("admin");
        centerHeadIn.setCustNo(khbh);

        AccTransDetailQueryIn accTransDetailQueryIn = new AccTransDetailQueryIn();
        accTransDetailQueryIn.setCenterHeadIn(centerHeadIn);
        accTransDetailQueryIn.setTxDateBegin(txDateBegin);
        accTransDetailQueryIn.setTxDateEnd(txDateEnd);

        try {
            AccTransDetailQueryOut accTransDetailQueryOut = iBank.sendMsg(accTransDetailQueryIn);
            return TransactionFileFactory.getObjFromFile(accTransDetailQueryOut.getFilelist().getDATA(), AccChangeNoticeFile.class.getName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}

package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.IFixedRecordService;
import com.handge.housingfund.common.service.finance.model.FixedRecord;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICFinanceActived2FixedDAO;
import com.handge.housingfund.database.dao.ICFinanceFixedBalanceDAO;
import com.handge.housingfund.database.entities.CFinanceActived2Fixed;
import com.handge.housingfund.database.entities.CFinanceFixedBalance;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.DepositPeriodTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */
@SuppressWarnings("Duplicates")
@Component
public class FixedRecordService implements IFixedRecordService {

    @Autowired
    ICFinanceFixedBalanceDAO icFinanceFixedBalanceDAO;
    @Autowired
    ICFinanceActived2FixedDAO icFinanceActived2FixedDAO;
    @Autowired
    ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;

    @Override
    public PageRes<FixedRecord> getFixedRecords(String khyhmc, String acctNo, String bookNo, int bookListNo, String acct_status, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(khyhmc)) filter.put("khyhmc", khyhmc);
        if (StringUtil.notEmpty(acctNo)) filter.put("acct_no", acctNo);
        if (StringUtil.notEmpty(bookNo)) filter.put("book_no", bookNo);
        if (bookListNo != -1) filter.put("book_list_no", bookListNo);
        if (StringUtil.notEmpty(acct_status) && !"所有".equals(acct_status)) filter.put("acct_status", acct_status);

        PageRes pageRes = new PageRes();

        List<CFinanceFixedBalance> cFinanceFixedBalances = DAOBuilder.instance(icFinanceFixedBalanceDAO)
                .searchFilter(filter)
                .orderOption("created_at", Order.DESC)
                .searchOption(SearchOption.FUZZY)
                .pageOption(pageRes, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        PageRes<FixedRecord> fixedRecordPageRes = new PageRes<>();
        fixedRecordPageRes.setCurrentPage(pageRes.getCurrentPage());
        fixedRecordPageRes.setNextPageNo(pageRes.getNextPageNo());
        fixedRecordPageRes.setPageCount(pageRes.getPageCount());
        fixedRecordPageRes.setPageSize(pageRes.getPageSize());
        fixedRecordPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<FixedRecord> fixedRecords = new ArrayList<>();

        for (CFinanceFixedBalance cFinanceFixedBalance : cFinanceFixedBalances) {
            FixedRecord fixedRecord = new FixedRecord();
            fixedRecord.setAcct_no(cFinanceFixedBalance.getAcct_no());

            try {
                CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(cFinanceFixedBalance.getAcct_no());
                fixedRecord.setAcct_name(centerAccountInfo.getYHZHMC());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            String status;
            if ("1".equals(cFinanceFixedBalance.getAcct_status())) status = "1";
            else if ("99".equals(cFinanceFixedBalance.getAcct_status())) status = "2";
            else status = "0";

            fixedRecord.setAcct_status(status);
            fixedRecord.setBeg_amt(cFinanceFixedBalance.getBeg_amt() != null ? cFinanceFixedBalance.getBeg_amt().toPlainString() : null);
            fixedRecord.setBook_list_no(cFinanceFixedBalance.getBook_list_no());
            fixedRecord.setBook_no(cFinanceFixedBalance.getBook_no());
            fixedRecord.setDeposit_period(cFinanceFixedBalance.getDeposit_period() != null ? DepositPeriodTransfer.daysToFlag(cFinanceFixedBalance.getDeposit_period()) : null);
            fixedRecord.setDeposit_begin_date(DateUtil.date2Str(cFinanceFixedBalance.getDeposit_begin_date(),"yyyy-MM-dd"));
            fixedRecord.setDeposit_end_date(DateUtil.date2Str(cFinanceFixedBalance.getDeposit_end_date(),"yyyy-MM-dd"));
            fixedRecord.setDraw_amt(cFinanceFixedBalance.getDraw_amt() != null ? cFinanceFixedBalance.getDraw_amt().toPlainString() : null);
            fixedRecord.setFreeze_type(cFinanceFixedBalance.getFreeze_type());
            fixedRecord.setId(cFinanceFixedBalance.getId());
            fixedRecord.setInterest(cFinanceFixedBalance.getInterest() != null ? cFinanceFixedBalance.getInterest().toPlainString() : null);
            fixedRecord.setInterest_rate(cFinanceFixedBalance.getInterest_rate() != null ? cFinanceFixedBalance.getInterest_rate().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : null);
            fixedRecord.setKhyhmc(cFinanceFixedBalance.getKhyhmc());
            fixedRecord.setLoss_flag(cFinanceFixedBalance.getLoss_flag());
            fixedRecord.setDqckbh(cFinanceFixedBalance.getDqckbh());

            fixedRecords.add(fixedRecord);
        }

        fixedRecordPageRes.setResults(fixedRecords);
        return fixedRecordPageRes;
    }

    @Override
    public FixedRecord getFixedRecord(String id) {

        CFinanceFixedBalance cFinanceFixedBalance = DAOBuilder.instance(icFinanceFixedBalanceDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceFixedBalance == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为"+ id +"的相关信息");
        }

        FixedRecord fixedRecord = new FixedRecord();

        fixedRecord.setAcct_no(cFinanceFixedBalance.getAcct_no());

        try {
            CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(cFinanceFixedBalance.getAcct_no());
            fixedRecord.setAcct_name(centerAccountInfo.getYHZHMC());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        String status;
        if ("1".equals(cFinanceFixedBalance.getAcct_status())) status = "1";
        else if ("99".equals(cFinanceFixedBalance.getAcct_status())) status = "2";
        else status = "0";

        fixedRecord.setAcct_status(status);
        fixedRecord.setBeg_amt(cFinanceFixedBalance.getBeg_amt() != null ? cFinanceFixedBalance.getBeg_amt().toPlainString() : null);
        fixedRecord.setBook_list_no(cFinanceFixedBalance.getBook_list_no());
        fixedRecord.setBook_no(cFinanceFixedBalance.getBook_no());
        fixedRecord.setDeposit_period(cFinanceFixedBalance.getDeposit_period() != null ? DepositPeriodTransfer.daysToFlag(cFinanceFixedBalance.getDeposit_period()) : null);
        fixedRecord.setDeposit_begin_date(DateUtil.date2Str(cFinanceFixedBalance.getDeposit_begin_date(),"yyyy-MM-dd"));
        fixedRecord.setDeposit_end_date(DateUtil.date2Str(cFinanceFixedBalance.getDeposit_end_date(),"yyyy-MM-dd"));
        fixedRecord.setDraw_amt(cFinanceFixedBalance.getDraw_amt() != null ? cFinanceFixedBalance.getDraw_amt().toPlainString() : null);
        fixedRecord.setFreeze_type(cFinanceFixedBalance.getFreeze_type());
        fixedRecord.setId(cFinanceFixedBalance.getId());
        fixedRecord.setInterest(cFinanceFixedBalance.getInterest() != null ? cFinanceFixedBalance.getInterest().toPlainString() : null);
        fixedRecord.setInterest_rate(cFinanceFixedBalance.getInterest_rate() != null ? cFinanceFixedBalance.getInterest_rate().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : null);
        fixedRecord.setKhyhmc(cFinanceFixedBalance.getKhyhmc());
        fixedRecord.setLoss_flag(cFinanceFixedBalance.getLoss_flag());
        fixedRecord.setDqckbh(cFinanceFixedBalance.getDqckbh());

        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).searchFilter(new HashMap<String, Object>(){{
            this.put("dqckbh", cFinanceFixedBalance.getDqckbh());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (cFinanceActived2Fixed != null) {
            fixedRecord.setCzy(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getCzy());
            fixedRecord.setSlrq(DateUtil.date2Str(cFinanceActived2Fixed.getCreated_at(), "yyyy-MM-dd"));
            fixedRecord.setBeizhu(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getBeizhu());
        }

        return fixedRecord;
    }

    @Override
    public void addFixedRecord(FixedRecord fixedRecord) {
        CFinanceFixedBalance cFinanceFixedBalance = new CFinanceFixedBalance();
        cFinanceFixedBalance.setDqckbh(fixedRecord.getDqckbh());
        cFinanceFixedBalance.setAcct_no(fixedRecord.getAcct_no());
        cFinanceFixedBalance.setBook_list_no(fixedRecord.getBook_list_no());
        cFinanceFixedBalance.setBook_no(fixedRecord.getBook_no());
        cFinanceFixedBalance.setBeg_amt(new BigDecimal(fixedRecord.getBeg_amt()));
        try {
            cFinanceFixedBalance.setDeposit_begin_date(DateUtil.str2Date("yyyyMMdd", fixedRecord.getDeposit_begin_date()));
            cFinanceFixedBalance.setDeposit_end_date(DateUtil.str2Date("yyyyMMdd", fixedRecord.getDeposit_end_date()));
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
        cFinanceFixedBalance.setDeposit_period(new BigDecimal(fixedRecord.getDeposit_period()));
        cFinanceFixedBalance.setKhyhmc(fixedRecord.getKhyhmc());
        cFinanceFixedBalance.setAcct_status("0");
        cFinanceFixedBalance.setDraw_amt(new BigDecimal(fixedRecord.getDraw_amt()));
        cFinanceFixedBalance.setFreeze_type("0");
        cFinanceFixedBalance.setInterest(null);
        cFinanceFixedBalance.setInterest_rate(new BigDecimal(2.35));
        cFinanceFixedBalance.setLoss_flag("0");

        DAOBuilder.instance(icFinanceFixedBalanceDAO).entity(cFinanceFixedBalance).save(new DAOBuilder.ErrorHandler() {
            @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
    }

    @Override
    public void updateFixedRecord(String dqckbh, String acctStatus) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("dqckbh", dqckbh);
        CFinanceFixedBalance cFinanceFixedBalance = DAOBuilder.instance(icFinanceFixedBalanceDAO)
                .searchFilter(filter)
                .getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        if (cFinanceFixedBalance != null) {
            cFinanceFixedBalance.setAcct_status(acctStatus);
            DAOBuilder.instance(icFinanceFixedBalanceDAO).entity(cFinanceFixedBalance).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
    }
}

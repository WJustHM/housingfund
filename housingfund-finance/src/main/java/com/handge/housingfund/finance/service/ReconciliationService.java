package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.finance.IAccChangeNoticeService;
import com.handge.housingfund.common.service.finance.IReconciliationService;
import com.handge.housingfund.common.service.finance.model.Reconciliation;
import com.handge.housingfund.common.service.finance.model.ReconciliationBase;
import com.handge.housingfund.common.service.finance.model.ReconciliationInitBalance;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICBankSendSeqNoDAO;
import com.handge.housingfund.database.dao.ICFinanceBankBalanceResetDAO;
import com.handge.housingfund.database.entities.CBankSendSeqNo;
import com.handge.housingfund.database.entities.CFinanceBankBalanceReset;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Created by gxy on 17-10-25.
 */
@Component
public class ReconciliationService implements IReconciliationService {

    @Autowired
    private ICFinanceBankBalanceResetDAO icFinanceBankBalanceResetDAO;

    @Autowired
    IAccChangeNoticeService iAccChangeNoticeService;

    @Autowired
    ICBankSendSeqNoDAO icBankSendSeqNoDAO;

    @Override
    public PageRes<Reconciliation> getReconciliationList(String yhmc, String zhhm, String tjny, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(yhmc)) filter.put("node", yhmc);
        if (StringUtil.notEmpty(zhhm)) filter.put("zhhm", zhhm);
        if (StringUtil.notEmpty(tjny)) filter.put("tjrq", tjny);

        PageRes pageRes = new PageRes();
        List<CFinanceBankBalanceReset> cFinanceBankBalanceResets = DAOBuilder.instance(icFinanceBankBalanceResetDAO).searchFilter(filter).searchOption(SearchOption.FUZZY).
                pageOption(pageRes, pageSize, pageNo).orderOption("created_at", Order.ASC).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        PageRes<Reconciliation> res = new PageRes<>();
        res.setCurrentPage(pageRes.getCurrentPage());
        res.setPageSize(pageRes.getPageSize());
        res.setNextPageNo(pageRes.getNextPageNo());
        res.setPageCount(pageRes.getPageCount());
        res.setTotalCount(pageRes.getTotalCount());

        ArrayList<Reconciliation> reconciliations = new ArrayList<>();
        for (CFinanceBankBalanceReset cFinanceBankBalanceReset : cFinanceBankBalanceResets) {
            Reconciliation reconciliation = new Reconciliation();
            reconciliation.setNode(cFinanceBankBalanceReset.getNode());
            reconciliation.setTJRQ(cFinanceBankBalanceReset.getTjrq());
            reconciliation.setYHDZYE(String.valueOf(cFinanceBankBalanceReset.getYhdzye()));
            reconciliation.setYHJ(String.valueOf(cFinanceBankBalanceReset.getYhj()));
            reconciliation.setYHJIA(String.valueOf(cFinanceBankBalanceReset.getYhjia()));
            reconciliation.setYHYE(String.valueOf(cFinanceBankBalanceReset.getYhye()));
            reconciliation.setZBR(cFinanceBankBalanceReset.getZbr());
            reconciliation.setZBSJ(DateUtil.date2Str(cFinanceBankBalanceReset.getZbsj(), "yyyy-MM-dd"));
            reconciliation.setZHHM(cFinanceBankBalanceReset.getZhhm());
            reconciliation.setZXCKYE(String.valueOf(cFinanceBankBalanceReset.getZxckye()));
            reconciliation.setZXJ(String.valueOf(cFinanceBankBalanceReset.getZxj()));
            reconciliation.setZXJIA(String.valueOf(cFinanceBankBalanceReset.getZxjia()));
            reconciliation.setZXYE(String.valueOf(cFinanceBankBalanceReset.getZxye()));
            reconciliation.setId(cFinanceBankBalanceReset.getId());
            reconciliation.setKHYHM(cFinanceBankBalanceReset.getKhyhm());

            reconciliations.add(reconciliation);
        }
        res.setResults(reconciliations);
        return res;
    }

    @Override
    public Reconciliation getReconciliation(String id) {
        CFinanceBankBalanceReset cFinanceBankBalanceReset = DAOBuilder.instance(icFinanceBankBalanceResetDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cFinanceBankBalanceReset == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "id为" + id + "的记录不存在");
        }
        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setNode(cFinanceBankBalanceReset.getNode());
        reconciliation.setTJRQ(cFinanceBankBalanceReset.getTjrq());
        reconciliation.setYHDZYE(String.valueOf(cFinanceBankBalanceReset.getYhdzye()));
        reconciliation.setYHJ(String.valueOf(cFinanceBankBalanceReset.getYhj()));
        reconciliation.setYHJIA(String.valueOf(cFinanceBankBalanceReset.getYhjia()));
        reconciliation.setYHYE(String.valueOf(cFinanceBankBalanceReset.getYhye()));
        reconciliation.setZBR(cFinanceBankBalanceReset.getZbr());
        reconciliation.setZBSJ(DateUtil.date2Str(cFinanceBankBalanceReset.getZbsj(), "yyyy-MM-dd"));
        reconciliation.setZHHM(cFinanceBankBalanceReset.getZhhm());
        reconciliation.setZXCKYE(String.valueOf(cFinanceBankBalanceReset.getZxckye()));
        reconciliation.setZXJ(String.valueOf(cFinanceBankBalanceReset.getZxj()));
        reconciliation.setZXJIA(String.valueOf(cFinanceBankBalanceReset.getZxjia()));
        reconciliation.setZXYE(String.valueOf(cFinanceBankBalanceReset.getZxye()));
        reconciliation.setKHYHM(cFinanceBankBalanceReset.getKhyhm());

        return reconciliation;
    }

    @Override
    public CommonResponses addReconciliation(TokenContext tokenContext, ReconciliationBase reconciliation) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("tjrq", reconciliation.getTJRQ());
        CFinanceBankBalanceReset cFinanceBankBalanceReset = DAOBuilder.instance(icFinanceBankBalanceResetDAO).searchFilter(filter).searchOption(SearchOption.REFINED).
                getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
        if (cFinanceBankBalanceReset != null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "调节年月已存在");
        }
        CFinanceBankBalanceReset reset = new CFinanceBankBalanceReset();
        reset.setNode(reconciliation.getNode());
        reset.setTjrq(reconciliation.getTJRQ());
        reset.setYhdzye(new BigDecimal(reconciliation.getYHDZYE()));
        reset.setYhj(StringUtil.notEmpty(reconciliation.getYHJ()) ? new BigDecimal(reconciliation.getYHJ()) : BigDecimal.ZERO);
        reset.setYhjia(StringUtil.notEmpty(reconciliation.getYHJIA()) ? new BigDecimal(reconciliation.getYHJIA()) : BigDecimal.ZERO);
        reset.setYhye(new BigDecimal(reconciliation.getYHYE()));
        reset.setZbr(tokenContext.getUserInfo().getCZY());
        reset.setZbsj(new Date());
        reset.setZhhm(reconciliation.getZHHM());
        reset.setZxckye(new BigDecimal(reconciliation.getZXCKYE()));
        reset.setZxj(StringUtil.notEmpty(reconciliation.getZXJ()) ? new BigDecimal(reconciliation.getZXJ()) : BigDecimal.ZERO);
        reset.setZxjia(StringUtil.notEmpty(reconciliation.getZXJIA()) ? new BigDecimal(reconciliation.getZXJIA()) : BigDecimal.ZERO);
        reset.setZxye(new BigDecimal(reconciliation.getZXYE()));
        reset.setKhyhm(reconciliation.getKHYHM());
        String id = DAOBuilder.instance(icFinanceBankBalanceResetDAO).entity(reset).saveThenFetchId(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        CommonResponses res = new CommonResponses();
        res.setState("Success");
        res.setId(id);
        return res;
    }

    @Override
    public CommonResponses updateReconciliation(TokenContext tokenContext, String id, ReconciliationBase reconciliation) {
        CFinanceBankBalanceReset reset = DAOBuilder.instance(icFinanceBankBalanceResetDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (reset == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "id为" + id + "的记录不存在");
        }
        reset.setNode(reconciliation.getNode());
        reset.setTjrq(reconciliation.getTJRQ());
        reset.setYhdzye(new BigDecimal(reconciliation.getYHDZYE()));
        reset.setYhj(StringUtil.notEmpty(reconciliation.getYHJ()) ? new BigDecimal(reconciliation.getYHJ()) : BigDecimal.ZERO);
        reset.setYhjia(StringUtil.notEmpty(reconciliation.getYHJIA()) ? new BigDecimal(reconciliation.getYHJIA()) : BigDecimal.ZERO);
        reset.setYhye(new BigDecimal(reconciliation.getYHYE()));
        reset.setZbr(tokenContext.getUserInfo().getCZY());
        reset.setZbsj(new Date());
        reset.setZhhm(reconciliation.getZHHM());
        reset.setZxckye(new BigDecimal(reconciliation.getZXCKYE()));
        reset.setZxj(StringUtil.notEmpty(reconciliation.getZXJ()) ? new BigDecimal(reconciliation.getZXJ()) : BigDecimal.ZERO);
        reset.setZxjia(StringUtil.notEmpty(reconciliation.getZXJIA()) ? new BigDecimal(reconciliation.getZXJIA()) : BigDecimal.ZERO);
        reset.setZxye(new BigDecimal(reconciliation.getZXYE()));
        reset.setKhyhm(reconciliation.getKHYHM());
        DAOBuilder.instance(icFinanceBankBalanceResetDAO).entity(reset).saveThenFetchId(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        CommonResponses res = new CommonResponses();
        res.setState("Success");
        res.setId(id);
        return res;
    }

    @Override
    public boolean delReconciliation(String id) {
        CFinanceBankBalanceReset reset = DAOBuilder.instance(icFinanceBankBalanceResetDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (reset == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "id为" + id + "的记录不存在");
        }
        DAOBuilder.instance(icFinanceBankBalanceResetDAO).entity(reset).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return false;
    }

    public Reconciliation getReconciliationInitBalance(String node, String khbh, String yhzh, String tjny) {
        CFinanceBankBalanceReset cFinanceBankBalanceReset = DAOBuilder.instance(icFinanceBankBalanceResetDAO).searchFilter(
                new HashMap<String, Object>() {{
                    this.put("zhhm", yhzh);
                    this.put("tjrq", tjny);
                }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库出错");
            }
        });

        Reconciliation reconciliation = new Reconciliation();


        Date tjnY;
        try {
            tjnY = DateUtil.str2Date("yyyy-MM", tjny);
        } catch (ParseException e) {
            throw new ErrorException("调节年月格式有误");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(tjnY);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        Date firstDay = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDay = cal.getTime();

        String txDateBegin = DateUtil.date2Str(firstDay, "yyyyMMdd");
        String txDateEnd = DateUtil.date2Str(lastDay, "yyyyMMdd");

        try {
            firstDay = DateUtil.str2Date("yyyyMMdd HH:mm:ss", txDateBegin + " 00:00:00");
            lastDay = DateUtil.str2Date("yyyyMMdd HH:mm:ss", txDateEnd + " 23:59:59");
        } catch (ParseException e) {
            throw new ErrorException("调节年月格式有误");
        }

        //中心银行存款日记账余额
        ReconciliationInitBalance reconciliationInitBal = iAccChangeNoticeService.getReconciliationInitBalance(yhzh, firstDay, lastDay);
        reconciliation.setZXCKYE(reconciliationInitBal.getCenterAcctBal());

        //银行对账单余额
//        String small = txDateBegin + " 000000";
        String lastBalance = "0.00";
        List<Object> accChangeNoticeFiles = iAccChangeNoticeService.getAccTransDetail(node, khbh, txDateBegin, txDateEnd);

        for (Object o : accChangeNoticeFiles) {
            AccChangeNoticeFile accChangeNoticeFile = (AccChangeNoticeFile) o;
            if (yhzh.equals(accChangeNoticeFile.getAcct())) {
//                if (small.compareTo(accChangeNoticeFile.getDate() + " " + accChangeNoticeFile.getTime()) <= 0) {
//                    small = accChangeNoticeFile.getDate() + " " + accChangeNoticeFile.getTime();
                lastBalance = accChangeNoticeFile.getBalance() != null ? accChangeNoticeFile.getBalance().toPlainString() : "-";
//                }
                break;
            }
        }

        reconciliation.setYHDZYE(lastBalance);

        if ("-".equals(reconciliation.getZXCKYE()) || "-".equals(reconciliation.getYHDZYE()))
            throw new ErrorException("该月未记账");

        List<CBankSendSeqNo> bankSendSeqNos = icBankSendSeqNoDAO.getUnrecorded(firstDay, lastDay);
        if (bankSendSeqNos != null) {
            BigDecimal zxj = BigDecimal.ZERO;
            BigDecimal zxjia = BigDecimal.ZERO;
            for (CBankSendSeqNo no : bankSendSeqNos) {
                if (no.getAmt() == null) {
                    continue;
                }
                if (no.getAmt().compareTo(BigDecimal.ZERO) < 0) {
                    zxj = zxj.add(no.getAmt().abs());
                } else {
                    zxjia = zxjia.add(no.getAmt().abs());
                }
            }
            reconciliation.setZXJ(zxj.toString());
            reconciliation.setZXJIA(zxjia.toString());
        }
        if (cFinanceBankBalanceReset != null) {
            reconciliation.setId(cFinanceBankBalanceReset.getId());
            reconciliation.setNode(cFinanceBankBalanceReset.getNode());
            reconciliation.setTJRQ(cFinanceBankBalanceReset.getTjrq());
            reconciliation.setYHJ(String.valueOf(cFinanceBankBalanceReset.getYhj()));
            reconciliation.setYHJIA(String.valueOf(cFinanceBankBalanceReset.getYhjia()));
            reconciliation.setYHYE(String.valueOf(cFinanceBankBalanceReset.getYhye()));
            reconciliation.setZBR(cFinanceBankBalanceReset.getZbr());
            reconciliation.setZBSJ(DateUtil.date2Str(cFinanceBankBalanceReset.getZbsj(), "yyyy-MM-dd"));
            reconciliation.setZHHM(cFinanceBankBalanceReset.getZhhm());
            reconciliation.setZXJ(String.valueOf(cFinanceBankBalanceReset.getZxj()));
            reconciliation.setZXJIA(String.valueOf(cFinanceBankBalanceReset.getZxjia()));
            reconciliation.setZXYE(String.valueOf(cFinanceBankBalanceReset.getZxye()));
            reconciliation.setKHYHM(cFinanceBankBalanceReset.getKhyhm());
        }

        return reconciliation;
    }
}

package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.finance.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */
public interface IAccChangeNoticeService {

    /**
     * 获取账户变动通知列表
     *
     * @return
     */
    PageRes<AccChangeNoticeModel> getAccChangeNotices(String zhhm, String sfzz, String summary, String begin, String end, BigDecimal FSE, String DSZH, String DSHM, int pageNo, int pageSize);

    /**
     * 获取账户变动通知列表
     *
     * @return
     */
    PageResNew<AccChangeNoticeModel> getAccChangeNotices(String zhhm, String sfzz, String summary, String begin, String end, BigDecimal FSE, String DSZH, String DSHM, String marker, String action, int pageSize);

    /**
     * 获取账户变动通知
     *
     * @return
     */
    AccChangeNoticeModel getAccChangeNotice(String id);


    /**
     * 通过单位账号获取账户变动通知
     *
     * @param DWZH
     * @return
     */
    PageRes<FinanceRecordUnitModel> getAccChangeNoticesByDWZH(final String DWZH,
                                                              String ZJLY,
                                                              BigDecimal FSE,
                                                              String JZPZH,
                                                              String begin,
                                                              String end,
                                                              int pageNo,
                                                              int pageSize);

    /**
     * 上传资料到到账通知单
     *
     * @param context
     * @param id
     * @param accChangeNoticeFile
     * @return
     */
    AccChangeNoticeModel putAccChangeNotice(TokenContext context, String id, AccChangeNoticeSMWJ accChangeNoticeFile);


    /**
     * 获取对账单
     *
     * @param node   银行节点号
     * @param KHBH   客户编号
     * @param YHZH   银行账号
     * @param CXRQKS 开始日期
     * @param CXRQJS 结束日期
     * @return
     */
    List<CompareBooks> getCompareBooks(String node, String KHBH, String YHZH, String CXRQKS, String CXRQJS);

    /**
     * 获取银行对账单
     *
     * @param node        银行节点号
     * @param khbh        客户编号
     * @param txDateBegin 开始日期
     * @param txDateEnd   结束日期
     * @return
     */
    List<Object> getAccTransDetail(String node, String khbh, String txDateBegin, String txDateEnd);

    /**
     * 获取中心银行存款日记账余额
     *
     * @param acct
     * @param firstDay
     * @param lastDay
     * @return
     */
    ReconciliationInitBalance getReconciliationInitBalance(String acct, Date firstDay, Date lastDay);
}

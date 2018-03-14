package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.AccChangeNoticeSMWJ;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/9/12.
 */
public interface IAccChangeNoticeAPI {

    /**
     * 获取账户变动通知列表
     *
     * @return
     */
    Response getAccChangeNotices(final String zhhm,
                                 final String sfzz,
                                 final String summary,
                                 final String begin,
                                 final String end,
                                 final String FSE,
                                 final String DSZH,
                                 final String DSHM,
                                 final int pageNo,
                                 final int pageSize);

    /**
     * 获取账户变动通知列表
     *
     * @return
     */
    Response getAccChangeNotices(final String zhhm,
                                 final String sfzz,
                                 final String summary,
                                 final String begin,
                                 final String end,
                                 final String FSE,
                                 final String DSZH,
                                 final String DSHM,
                                 final String marker,
                                 final String action,
                                 final String pageSize);

    /**
     * 通过单位账号查询到账通知单
     *
     * @param DWZH
     * @return
     */
    Response getAccChangeNoticesByDWZH(final String DWZH,
                                       String ZJLY,
                                       String FSE,
                                       String JZPZH,
                                       String begin,
                                       String end,
                                       int pageNo,
                                       int pageSize);

    /**
     * 获取账户变动通知
     *
     * @return
     */
    Response getAccChangeNotice(String id);

    /**
     * 获取账户变动通知
     *
     * @return
     */
    Response putAccChangeNotice(TokenContext context, String id, AccChangeNoticeSMWJ accChangeNoticeFile);

    /**
     * 获取对账单
     *
     * @param node   银行节点号
     * @param KHBH   客户编号
     * @param YHZH   银行账号
     * @param CXRQKS 开始日期 yyyy-MM-dd HH:mm
     * @param CXRQJS 结束日期 yyyy-MM-dd HH:mm
     * @return
     */
    Response getCompareBooks(String node, String KHBH, String YHZH, String CXRQKS, String CXRQJS);
}

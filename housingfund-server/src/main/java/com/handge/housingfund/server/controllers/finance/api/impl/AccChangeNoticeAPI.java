package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.finance.IAccChangeNoticeService;
import com.handge.housingfund.common.service.finance.model.AccChangeNoticeSMWJ;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.IAccChangeNoticeAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/9/12.
 */
@Component
public class AccChangeNoticeAPI implements IAccChangeNoticeAPI {
    @Autowired
    IAccChangeNoticeService iAccChangeNoticeService;

    @Override
    public Response getAccChangeNotices(String zhhm, String sfzz, String summary, String begin, String end, String FSE, String DSZH, String DSHM, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;
        BigDecimal Amt = null;
        if (StringUtil.notEmpty(FSE)) {
            try {
                Amt = new BigDecimal(FSE);
            } catch (Exception e) {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("金额非法");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        }

        try {
            return Response.status(200).entity(iAccChangeNoticeService.getAccChangeNotices(zhhm, sfzz, summary, begin, end, Amt, DSZH, DSHM, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getAccChangeNotices(String zhhm, String sfzz, String summary, String begin, String end, String FSE, String DSZH, String DSHM, String marker, String action, String pageSize) {
        try {
            int pagesize = StringUtil.notEmpty(pageSize) ? Integer.parseInt(pageSize) : 30;
            BigDecimal Amt = null;
            if (StringUtil.notEmpty(FSE)) {
                try {
                    Amt = new BigDecimal(FSE);
                } catch (Exception e) {
                    return Response.status(200).entity(new Msg() {
                        {
                            this.setMsg("金额非法");
                            this.setCode(ReturnCode.Error);
                        }
                    }).build();
                }
            }
            return Response.status(200).entity(iAccChangeNoticeService.getAccChangeNotices(zhhm, sfzz, summary, begin, end, Amt, DSZH, DSHM, marker, action, pagesize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getAccChangeNoticesByDWZH(final String DWZH,
                                              String ZJLY,
                                              String FSE,
                                              String JZPZH,
                                              String begin,
                                              String end,
                                              int pageNo,
                                              int pageSize) {
        if (!StringUtil.notEmpty(DWZH))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("DWZH不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;
        BigDecimal Amt = null;
        if (StringUtil.notEmpty(FSE)) {
            try {
                Amt = new BigDecimal(FSE);
            } catch (Exception e) {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("金额非法");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        }
        try {
            return Response.status(200).entity(iAccChangeNoticeService.getAccChangeNoticesByDWZH(DWZH, ZJLY, Amt, JZPZH, begin, end, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getAccChangeNotice(String id) {
        if (!StringUtil.notEmpty(id))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iAccChangeNoticeService.getAccChangeNotice(id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response putAccChangeNotice(TokenContext context, String id, AccChangeNoticeSMWJ accChangeNoticeFile) {
        try {
            return Response.status(200).entity(iAccChangeNoticeService.putAccChangeNotice(context, id, accChangeNoticeFile)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getCompareBooks(String node, String KHBH, String YHZH, String CXRQKS, String CXRQJS) {
        if (!StringUtil.notEmpty(node))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行节点号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(KHBH))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("客户编号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(YHZH))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行账户不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(CXRQKS))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("开始日期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(CXRQKS))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("结束日期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();


        try {
            return Response.status(200).entity(iAccChangeNoticeService.getCompareBooks(node, KHBH, YHZH, CXRQKS, CXRQJS)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

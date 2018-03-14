package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.finance.IActived2FixedService;
import com.handge.housingfund.common.service.finance.IFixedBusinessAuditService;
import com.handge.housingfund.common.service.finance.IFixedDrawService;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.IFixedBusinessAuditAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/14.
 */
@Component
public class FixedBusinessAuditAPI implements IFixedBusinessAuditAPI{
    @Autowired
    IFixedBusinessAuditService iFixedBusinessAuditService;
    @Autowired
    IActived2FixedService iActived2FixedService;
    @Autowired
    IFixedDrawService iFixedDrawService;

    @Override
    public Response getAuditRecords(String khyhmc, String acctNo, String ywlx, String audit, String begin, String end, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;
        if (!StringUtil.notEmpty(ywlx)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("业务类型不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!Arrays.asList("09", "10", "11", "12").contains(ywlx)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("业务类型不正确, 09:活期转定期 10:定期支取 11:活期转通知存款 12:通知存款支取");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(audit)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("是否已审核标志不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!Arrays.asList("0", "1", "待审核", "已审核").contains(audit)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("是否已审核标志不正确");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iFixedBusinessAuditService.getAuditRecords(khyhmc, acctNo, ywlx, audit, begin, end, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getAuditRecord(TokenContext tokenContext, String id, String ywlx) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(ywlx)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("业务类型不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            Object entity = null;
            if ("09".equals(ywlx)) {
                entity = iActived2FixedService.getActivedToFixed(tokenContext,id);
            } else if ("10".equals(ywlx)) {
                entity = iFixedDrawService.getFixedDraw(tokenContext,id);
            } else if ("11".equals(ywlx)) {

            } else if ("12".equals(ywlx)) {

            }
            return Response.status(200).entity(entity).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response auditActived2Fixed(String id, ReviewInfo reviewInfo) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (reviewInfo == null || !StringUtil.notEmpty(reviewInfo.getSHJG()) || !Arrays.asList("01", "02").contains(reviewInfo.getSHJG()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("审核内容不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            iFixedBusinessAuditService.auditActived2Fixed(id, reviewInfo);
            return Response.status(200).entity(new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("审核完成");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response auditFixedDraw(String id, ReviewInfo reviewInfo) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (reviewInfo == null || !StringUtil.notEmpty(reviewInfo.getSHJG()) || !Arrays.asList("01", "02").contains(reviewInfo.getSHJG()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("审核内容不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            iFixedBusinessAuditService.auditFixedDraw(id, reviewInfo);
            return Response.status(200).entity(new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("审核完成");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IFixedBusinessAuditAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/9/14.
 */
@Path("/finance/fixedBusinessAudit")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class FixedBusinessAuditResource {
    @Autowired
    IFixedBusinessAuditAPI iFixedBusinessAuditAPI;

    /**
     * 获取定期业务审核列表
     * @param khyhmc 开户银行名称
     * @param acctNo 账号
     * @param ywlx  业务类型 09:活期转定期 10:定期支取 11:活期转通知存款 12:通知存款支取
     * @param audit 是否已审核标志，区分待处理和已处理 0:待审核 1:已审核
     * @param begin 到达时间-开始
     * @param end 到达时间-结束
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @return
     */
    @Path("")
    @GET
    public Response getAuditRecords(final @QueryParam("khyhmc") String khyhmc,
                                    final @QueryParam("acctNo") String acctNo,
                                    final @QueryParam("ywlx") String ywlx,
                                    final @QueryParam("audit") String audit,
                                    final @QueryParam("begin") String begin,
                                    final @QueryParam("end") String end,
                                    final @QueryParam("pageNo") int pageNo,
                                    final @QueryParam("pageSize") int pageSize) {
        System.out.println("获取定期业务审核列表");
        return ResUtils.wrapEntityIfNeeded(iFixedBusinessAuditAPI.getAuditRecords(khyhmc, acctNo, ywlx, audit, begin, end, pageNo, pageSize));
    }

    @GET
    @Path("/{id}")
    public Response getAuditRecord(final @Context HttpRequest request, final @PathParam("id") String id, final @QueryParam("ywlx") String ywlx) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取定期业务详情");
        return ResUtils.wrapEntityIfNeeded(iFixedBusinessAuditAPI.getAuditRecord(tokenContext,id, ywlx));
    }

    @POST
    @Path("/auditActived2Fixed")
    public Response auditActived2Fixed(final @QueryParam("id") String id, RequestWrapper<ReviewInfo> reviewInfoRequestWrapper) {
        System.out.println("审核活期转定期");
        return ResUtils.wrapEntityIfNeeded(iFixedBusinessAuditAPI.auditActived2Fixed(id, reviewInfoRequestWrapper == null ? null : reviewInfoRequestWrapper.getReq()));
    }

    @POST
    @Path("/auditFixedDraw")
    public Response auditFixedDraw(final @QueryParam("id") String id, RequestWrapper<ReviewInfo> reviewInfoRequestWrapper) {
        System.out.println("审核活期转定期");
        return ResUtils.wrapEntityIfNeeded(iFixedBusinessAuditAPI.auditFixedDraw(id, reviewInfoRequestWrapper == null ? null : reviewInfoRequestWrapper.getReq()));
    }
}

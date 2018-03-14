package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.RepaymentApplyPrepaymentPost;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IRepaymentApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/loan/repayment")
@Controller
public class RepaymentResource {

    @Autowired
    private IRepaymentApi<Response> service;

    /**
     * 还款repaymentApplyReceipt
     * // completed 815
     *
     * @param DKZH  贷款账号
     * @param JKRXM 借款人姓名
     **/
    @Path("/repamentapply")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHousingRepaymentApplyList(final @Context HttpRequest request,
                                                 final @QueryParam("DKZH") String DKZH, final @QueryParam("JKRXM") String JKRXM
            , final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String pageNo, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ
            ,final @QueryParam("SWTYH") String YHDM,
                                                 final @QueryParam("ZJHM") String ZJHM) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("还款repaymentApplyReceipt");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingRepaymentApplyList(tokenContext, DKZH, JKRXM, pageSize, pageNo,KSSJ,JSSJ,YHDM,ZJHM));
    }


    /**
     * 还款repaymentApplyReceipt
     * // completed 815
     *
     * @param DKZH  贷款账号
     * @param JKRXM 借款人姓名
     **/
    @Path("/repamentapply/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHousingRepaymentApplyListnew(final @Context HttpRequest request,
                                                 final @QueryParam("DKZH") String DKZH, final @QueryParam("JKRXM") String JKRXM
            , final @QueryParam("pageSize") String pageSize, final @QueryParam("marker") String marker, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("action") String action
    ,final @QueryParam("SWTYH") String YHDM) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("还款repaymentApplyReceipt");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingRepaymentApplyListnew(tokenContext, DKZH, JKRXM, pageSize, marker,KSSJ,JSSJ,action,YHDM));
    }


    /**
     * 新增还款申请
     * 815
     *
     * @param action (0保存 1提交)
     **/
    @Path("/repamentapply/perpayment/{action}")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPerpayment(final @Context HttpRequest request, final @PathParam("action") String action, final RequestWrapper<RepaymentApplyPrepaymentPost> repaymentapplyprepaymentpost) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("新增还款申请");

        return ResUtils.wrapEntityIfNeeded(this.service.postRepayment(tokenContext, action, repaymentapplyprepaymentpost == null ? null : repaymentapplyprepaymentpost.getReq()));
    }


    /**
     * 修改还款申请
     * 815
     *
     * @param action (0保存 1提交)
     * @param YWLSH  业务流水号
     **/
    @Path("/repamentapply/perpaymentall/{action}/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPerpaymentall(final @Context HttpRequest request, final @PathParam("action") String action, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<RepaymentApplyPrepaymentPost> repaymentapplyprepaymentallpost) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("修改提前还款结清申请");

        return ResUtils.wrapEntityIfNeeded(this.service.putRepayment(tokenContext, action, YWLSH, repaymentapplyprepaymentallpost == null ? null : repaymentapplyprepaymentallpost.getReq()));
    }


    /**
     * 删除申请受理
     **/
    @Path("/repamentapply")
    @DELETE
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteHousingRepaymentApply(final @Context HttpRequest request, final RequestWrapper<DelList> dellist) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("删除申请受理");

        return ResUtils.wrapEntityIfNeeded(this.service.deleteHousingRepaymentApply(tokenContext, dellist == null ? null : dellist.getReq()));
    }




    /**
     * 获取还款申请详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/repamentapply/details/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getPerpaymentall(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取还款申请详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getPerpaymentDetails(tokenContext, YWLSH));
    }


    /**
     * 提交与撤回还款申请
     *
     * @param action 状态（0：提交 1：撤回 ）
     **/
    @Path("/state/{action}")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response putEstateStatusSubmit(final @Context HttpRequest request, final @PathParam("action") String action, final @QueryParam("YWLX") String ywlx,
                                          final RequestWrapper<BatchSubmission> body) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("单独修改状态（提交与撤回,删除）申请受理");

        return ResUtils.wrapEntityIfNeeded(this.service.putEstateStatusSubmit(tokenContext, body == null ? null : body.getReq(), action, ywlx));
    }



    /**
     * 打印还款申请回执单
     *
     * @param YWLSH
     * @return
     */
    @Path("/printReceipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response printReceipt(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("打印还款申请回执单");
        return ResUtils.wrapEntityIfNeeded(this.service.printRepaymentReceipt(tokenContext, YWLSH));
    }

    /**
     * 还款申请查询2
     */
    @Path("/backRepaymentInfo/{HKLX}/{DKZH}/{YDKKRQ}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response backRepaymentInfo(final @Context HttpRequest request, final @PathParam("HKLX") String HKLX, final @PathParam("DKZH") String DKZH,
                                      final @PathParam("YDKKRQ") String YDKKRQ, final @QueryParam("BCHKJE") String BCHKJE) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("提前查询");
        return ResUtils.wrapEntityIfNeeded(this.service.backRepaymentInfo(tokenContext, HKLX, DKZH, YDKKRQ, BCHKJE));
    }

    /**
     * 还款失败查询
     */
    @Path("/repaymentFailureInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response repaymentFailureInfo(final @Context HttpRequest request, final @QueryParam("JKRXM") String JKRXM, final @QueryParam("DKZH") String DKZH,
                                         final @QueryParam("stime") String stime, final @QueryParam("etime") String etime,
                                         final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String pageNo
            ,final @QueryParam("SWTYH") String YHDM,
                                         final @QueryParam("ZJHM")        String ZJHM
            ,final @QueryParam("HKYWLX")  String HKYWLX
    ,final @QueryParam("YWWD")  String YWWD) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("失败查询");

        return ResUtils.wrapEntityIfNeeded(this.service.getFailedBuinessInfo(tokenContext, JKRXM, DKZH, stime, etime, pageSize, pageNo,YHDM,ZJHM,HKYWLX,YWWD));
    }


    /**
     * 还款失败查询
     */
    @Path("/repaymentFailureInfo/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response repaymentFailureInfonew(final @Context HttpRequest request, final @QueryParam("JKRXM") String JKRXM, final @QueryParam("DKZH") String DKZH,
                                         final @QueryParam("stime") String stime, final @QueryParam("etime") String etime,
                                         final @QueryParam("pageSize") String pageSize, final @QueryParam("marker") String marker, final @QueryParam("action") String action
            ,final @QueryParam("SWTYH") String YHDM) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("失败查询");

        return ResUtils.wrapEntityIfNeeded(this.service.getFailedBuinessInfonew(tokenContext, JKRXM, DKZH, stime, etime, pageSize, marker,action,YHDM));
    }

    /**
     * 重新划扣
     */
    @Path("/repaymentFailureChange")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response putFailedBuinessSubmit(final @Context HttpRequest request, final @QueryParam("ID") String ID, final @QueryParam("CZLX") String CZLX) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("查询审核");

        return ResUtils.wrapEntityIfNeeded(this.service.putFailedBuinessSubmit(tokenContext, ID, CZLX));
    }

    /**
     * 逾期修改查询
     */
    @Path("/repaymentOverdueModification/{ywlsh}/{dkzh}/{ydkkrq}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response postOverdueModification(final @Context HttpRequest request, final @PathParam("ywlsh") String ywlsh, final @PathParam("dkzh") String dkzh
            , final @PathParam("ydkkrq") String ydkkrq) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("逾期修改查询");

        return ResUtils.wrapEntityIfNeeded(this.service.getOverdueModification(tokenContext, ywlsh, dkzh, ydkkrq));
    }

}
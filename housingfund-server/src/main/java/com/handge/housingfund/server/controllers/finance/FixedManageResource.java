package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.Actived2Fixed;
import com.handge.housingfund.common.service.finance.model.FixedDraw;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IFixedManageAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/9/13.
 * 描述
 */
@Path("/finance/fixedManage")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class FixedManageResource {
    @Autowired
    IFixedManageAPI fixedManageAPI;

    /**
     * 定期记录查询
     *
     * @param khyhmc     开户银行名
     * @param acctNo     专户号码
     * @param bookNo     册号
     * @param bookListNo 笔号
     * @param status     状态
     * @return
     */
    @GET
    @Path("/fixedRecords")
    public Response getFixedRecords(final @Context HttpRequest request, final @QueryParam("khyhmc") String khyhmc,
                                    final @QueryParam("acctNo") String acctNo,
                                    final @QueryParam("bookNo") String bookNo,
                                    final @QueryParam("bookListNo") String bookListNo,
                                    final @QueryParam("status") String status,
                                    final @QueryParam("pageNo") int pageNo,
                                    final @QueryParam("pageSize") int pageSize) {


        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("定期记录");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.getFixedRecords(tokenContext,khyhmc, acctNo, bookNo, bookListNo, status, pageNo, pageSize));
    }

    /**
     * 获取定期详情
     *
     * @param id
     * @return
     */
    @GET
    @Path("/fixedRecords/{id}")
    public Response getFixedRecord(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取定期详情");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.getFixedRecord(tokenContext,id));
    }

    /**
     * 获取活期转定期详细信息列表
     *
     * @param khyhmc        开户银行名
     * @param acctNo        专户号码
     * @param depositPeriod 存入期限
     * @param status        状态
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET
    @Path("/activedToFixed")
    public Response getActivedToFixeds(final @Context HttpRequest request, final @QueryParam("khyhmc") String khyhmc,
                                       final @QueryParam("acctNo") String acctNo,
                                       final @QueryParam("depositPeriod") String depositPeriod,
                                       final @QueryParam("status") String status,
                                       final @QueryParam("pageNo") int pageNo,
                                       final @QueryParam("pageSize") int pageSize) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("活期转定期");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.getActivedToFixeds(tokenContext,khyhmc, acctNo, depositPeriod, status, pageNo, pageSize));
    }

    /**
     * 获取活期转定期详细信息
     *
     * @param id 活期转定期id
     * @return
     */
    @GET
    @Path("/activedToFixed/{id}")
    public Response getActivedToFixed(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取活期转定期详细信息");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.getActivedToFixed(tokenContext,id));
    }

    /**
     * 新增活期转定期
     *
     * @param activedToFixed
     * @param type 操作类型 0：保存 1：提交
     * @return
     */
    @POST
    @Path("/activedToFixed")
    public Response addActivedToFixed(final @Context HttpRequest request, final RequestWrapper<Actived2Fixed> activedToFixed, final @QueryParam("type") String type) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("新增活期转定期");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.addActivedToFixed(tokenContext,activedToFixed == null ? null : activedToFixed.getReq(), type));
    }

    /**
     * 修改活期转定期
     *
     * @param activedToFixed
     * @param type 操作类型 0：保存 1：提交
     * @return
     */
    @PUT
    @Path("/activedToFixed/{id}")
    public Response modifyActivedToFixed(final @Context HttpRequest request, final @PathParam("id") String id, final RequestWrapper<Actived2Fixed> activedToFixed, final @QueryParam("type") String type) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("修改活期转定期");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.modifyActivedToFixed(tokenContext,id, activedToFixed == null ? null : activedToFixed.getReq(), type));
    }

    /**
     * 删除活期转定期
     *
     * @param id 活期转定期id
     * @return
     */
    @DELETE
    @Path("/activedToFixed/{id}")
    public Response delActivedToFixed(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("删除活期转定期");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.delActivedToFixed(tokenContext,id));
    }

    /**
     * 提交活期转定期
     *
     * @param id 活期转定期id
     * @return
     */
    @GET
    @Path("/activedToFixed/submit/{id}")
    public Response submitActivedToFixed(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("提交活期转定期");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.submitActivedToFixed(tokenContext,id));
    }

    /**
     * 撤回活期转定期
     *
     * @param id 活期转定期id
     * @return
     */
    @GET
    @Path("/activedToFixed/revoke/{id}")
    public Response revokeActivedToFixed(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("撤回活期转定期");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.revokeActivedToFixed(tokenContext,id));
    }

    /**
     *更新活期转定期业务状态
     *
     * @param request
     * @param id   活期转定期id
     * @param step 业务状态
     * @return
     */
    @GET
    @Path("/activedToFixed/manual/{id}")
    public Response updateA2FStep(final @Context HttpRequest request, final @PathParam("id") String id, final @QueryParam("step") String step) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("更新活期转定期业务状态");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.updateA2FStep(tokenContext, id, step));
    }

    /**
     * 定期支取列表
     *
     * @param khyhmc     开户银行名
     * @param acctNo     专户号码
     * @param bookNo     册号
     * @param bookListNo 笔号
     * @param status     状态
     * @return
     */
    @GET
    @Path("/fixedDraw")
    public Response getFixedDraws(final @Context HttpRequest request, final @QueryParam("khyhmc") String khyhmc,
                                  final @QueryParam("acctNo") String acctNo,
                                  final @QueryParam("bookNo") String bookNo,
                                  final @QueryParam("bookListNo") String bookListNo,
                                  final @QueryParam("status") String status,
                                  final @QueryParam("pageNo") int pageNo,
                                  final @QueryParam("pageSize") int pageSize) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("定期支取");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.getFixedDraws(tokenContext,khyhmc, acctNo, bookNo, bookListNo, status, pageNo, pageSize));
    }

    /**
     * 获取定期支取详细信息
     *
     * @param id 定期支取id
     * @return
     */
    @GET
    @Path("/fixedDraw/{id}")
    public Response getFixedDraw(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取定期支取详细信息");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.getFixedDraw(tokenContext,id));
    }

    /**
     * 新增定期支取
     *
     * @param fixedDraw
     * @param type      操作类型 0：保存 1：提交
     * @return
     */
    @POST
    @Path("/fixedDraw")
    public Response addFixedDraw(final @Context HttpRequest request, final RequestWrapper<FixedDraw> fixedDraw, final @QueryParam("type") String type) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("新增定期支取");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.addFixedDraw(tokenContext,fixedDraw == null ? null : fixedDraw.getReq(), type));
    }

    /**
     * 修改定期支取
     *
     * @param fixedDraw
     * @param type      操作类型 0：保存 1：提交
     * @return
     */
    @PUT
    @Path("/fixedDraw/{id}")
    public Response modifyFixedDraw(final @Context HttpRequest request, final @PathParam("id") String id, final RequestWrapper<FixedDraw> fixedDraw, final @QueryParam("type") String type) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("修改定期支取");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.modifyFixedDraw(tokenContext,id, fixedDraw == null ? null : fixedDraw.getReq(), type));
    }

    /**
     * 删除定期支取
     *
     * @param id 定期支取id
     * @return
     */
    @DELETE
    @Path("/fixedDraw/{id}")
    public Response delFixedDraw(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("删除定期支取");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.delFixedDraw(tokenContext,id));
    }

    /**
     * 提交定期支取
     *
     * @param id 定期支取id
     * @return
     */
    @GET
    @Path("/fixedDraw/submit/{id}")
    public Response submitFixedDraw(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("提交定期支取");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.submitFixedDraw(tokenContext,id));
    }

    /**
     * 撤回定期支取
     *
     * @param id 定期支取id
     * @return
     */
    @GET
    @Path("/fixedDraw/revoke/{id}")
    public Response revokeFixedDraw(final @Context HttpRequest request, final @PathParam("id") String id) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("撤回定期支取");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.revokeFixedDraw(tokenContext,id));
    }

    /**
     * 更新定期支取业务状态
     *
     * @param request
     * @param id    定期支取id
     * @param step  状态
     * @return
     */
    @GET
    @Path("/fixedDraw/manual/{id}")
    public Response updateF2AStep(final @Context HttpRequest request, final @PathParam("id") String id, final @QueryParam("step") String step) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("更新定期支取业务状态");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.updateF2AStep(tokenContext, id, step));
    }

    /**
     * 获取活期余额
     *
     * @param request
     * @param zhhm 活期账户
     * @return
     */
    @GET
    @Path("/actived/{zhhm}")
    public Response getActivedBalance(final @Context HttpRequest request, final @PathParam("zhhm") String zhhm) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        System.out.println("获取活期余额");
        return ResUtils.wrapEntityIfNeeded(fixedManageAPI.getActivedBalance(tokenContext,zhhm));
    }
}

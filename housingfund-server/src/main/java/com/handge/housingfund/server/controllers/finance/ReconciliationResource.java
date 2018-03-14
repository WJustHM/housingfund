package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.Reconciliation;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IReconciliationAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 余额调节
 * Created by gxy on 17-10-25.
 */
@Path("/finance/reconciliation")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class ReconciliationResource {

    @Autowired
    IReconciliationAPI iReconciliationAPI;

    /**
     * 获取余额调节列表
     *
     * @param yhmc     银行名称
     * @param zhhm     专户号码
     * @param tjny     调节年月
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Path("")
    @GET
    public Response getReconciliationList(final @QueryParam("YHMC") String yhmc,
                                          final @QueryParam("ZHHM") String zhhm,
                                          final @QueryParam("TJNY") String tjny,
                                          final @QueryParam("pageNo") int pageNo,
                                          final @QueryParam("pageSize") int pageSize) {
        System.out.println("获取余额调节列表");
        return ResUtils.wrapEntityIfNeeded(iReconciliationAPI.getReconciliationList(yhmc, zhhm, tjny, pageNo, pageSize));
    }

    /**
     * 获取余额调节详情
     *
     * @param id
     * @return
     */
    @Path("/{id}")
    @GET
    public Response getReconciliation(final @PathParam("id") String id) {
        System.out.println("获取余额调节详情");
        return ResUtils.wrapEntityIfNeeded(iReconciliationAPI.getReconciliation(id));
    }

    /**
     * 新增余额调节
     *
     * @param requestWrapper
     * @return
     */
    @Path("")
    @POST
    public Response addReconciliation(final @Context HttpRequest request, final RequestWrapper<Reconciliation> requestWrapper) {
        System.out.println("新增余额调节");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(iReconciliationAPI.addReconciliation(tokenContext, requestWrapper != null ? requestWrapper.getReq() : null));
    }

    /**
     * 修改余额调节
     *
     * @param id
     * @param requestWrapper
     * @return
     */
    @Path("/{id}")
    @PUT
    public Response updateReconciliation(final @Context HttpRequest request, final @PathParam("id") String id, final RequestWrapper<Reconciliation> requestWrapper) {
        System.out.println("修改余额调节");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(iReconciliationAPI.updateReconciliation(tokenContext, id, requestWrapper != null ? requestWrapper.getReq() : null));
    }

    /**
     * 删除余额调节
     *
     * @param id
     * @return
     */
    @Path("/{id}")
    @DELETE
    public Response delReconciliation(final @PathParam("id") String id) {
        System.out.println("删除余额调节");
        return ResUtils.wrapEntityIfNeeded(iReconciliationAPI.delReconciliation(id));
    }

    /**
     * 获取余额调节余额
     *
     * @param node 银行节点号
     * @param khbh 客户编号
     * @param yhzh 银行账号
     * @param tjny 调节年月
     * @return
     */
    @Path("/getBalance")
    @GET
    public Response getBalance(final @QueryParam("NODE") String node,
                               final @QueryParam("KHBH") String khbh,
                               final @QueryParam("YHZH") String yhzh,
                               final @QueryParam("TJNY") String tjny) {
        System.out.println("获取余额调节详情");
        return ResUtils.wrapEntityIfNeeded(iReconciliationAPI.getBalance(node, khbh, yhzh, tjny));
    }
}

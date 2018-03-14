package com.handge.housingfund.server.controllers.collection;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.CollectionDeleteInfo;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.CommonApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Liujuhao on 2017/8/29.
 */

@Controller
@Path("/collection")
public class CommonResource {

    @Autowired
    private CommonApi<Response> service;



    /**
     * 删除，归集业务
     *
     * @param
     **/
    @Path("/common/delete")
    @DELETE
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteOperation(@Context HttpRequest httpRequest, final RequestWrapper<CollectionDeleteInfo> deleteInfo, final @QueryParam("YWMK") String YWMK) {

        System.out.println("删除，归集业务");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.deleteOperation(tokenContext, deleteInfo == null ? null : deleteInfo.getReq().getYWLSHs(), YWMK));
    }

    /**
     * 撤回，归集业务
     *
     * @param YWLSH
     *            业务流水号
     **/
    @Path("/common/revoke/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response revokeOperation(@Context HttpRequest httpRequest, final @PathParam("YWLSH") String YWLSH, final @QueryParam("YWMK") String YWMK) {

        System.out.println("撤回，归集业务");

        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);

        return ResUtils.wrapEntityIfNeeded(this.service.revokeOperation(tokenContext, YWLSH, YWMK));
    }

    /**
     * 查询，账户历史记录
     * @param ZhangHao
     * @param YWMK
     * @param pageSize
     * @param pageNo
     * @return
     */
    @Path("/common/accountHistory/{ZhangHao}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecordHistory(@Context HttpRequest httpRequest,final @PathParam("ZhangHao") String ZhangHao, final @QueryParam("YWMK") String YWMK,
                                     final @QueryParam("pageSize") String pageSize, final @QueryParam("pageNo") String pageNo){

        System.out.println("查询，个人/单位账户历史记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getRecordHistory(ZhangHao, YWMK, pageSize, pageNo));
    }


    /**
     * 查询，账户历史记录
     * @param YWLSH
     * @return
     */
    @Path("/common/reviewHistory/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecordHistory(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH){

        System.out.println("查询审核历史记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getReviewInfos(YWLSH));
    }

    /**
     * 归集手动入账接口
     */
    @Path("/common/accountRecord")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response accountRecord(@Context HttpRequest httpRequest,final @QueryParam("YWLSH") String YWLSH,
                                  final @QueryParam("YWLX") String YWLX,final @QueryParam("CODE") String CODE){

        System.out.println("归集手动入账接口");

        return ResUtils.wrapEntityIfNeeded(this.service.accountRecord(YWLSH,YWLX,CODE));
    }

}

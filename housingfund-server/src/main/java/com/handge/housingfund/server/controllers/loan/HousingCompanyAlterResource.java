package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPost;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPut;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IHousingCompanyAlterApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by 向超 on 2017/8/24.
 */
@Path("/loan/housingCompanyAlterResource")
@Controller
public class HousingCompanyAlterResource {
    @Autowired
    private IHousingCompanyAlterApi<Response> service;

    /**
     * 添加变更房开信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param FKZH 房开账号
     * @param housingCompanyPost 添加变更房开公司信息
     * @return CommonResponses
     */
    @Path("/addHousingCompanyAlter")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addHousingCompanyAlter(final @Context HttpRequest request,final @QueryParam("CZLX") String CZLX, final @QueryParam("FKZH") String FKZH, final RequestWrapper<HousingCompanyPost> housingCompanyPost) {

        System.out.println("新增变更房开公司");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.addHousingCompanyAlter(tokenContext,CZLX, FKZH, housingCompanyPost == null ? null : housingCompanyPost.getReq()));
    }

    /**
     * 修改变更房开信息
     * @param YWLSH 业务流水号
     * @param CZLX 操作类型 0 保存 1 提交
     * @param housingCompanyPut 修改变更房开公司信息
     * @return CommonResponses
     */
    @Path("/reHousingCompanyAlterInfo")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reHousingCompanyAlterInfo(final @Context HttpRequest request, final @QueryParam("YWLSH") String YWLSH, final @QueryParam("CZLX") String CZLX, final RequestWrapper<HousingCompanyPut> housingCompanyPut) {

        System.out.println("修改变更房开公司");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.reHousingCompanyAlterInfo(tokenContext,YWLSH, CZLX, housingCompanyPut == null ? null : housingCompanyPut.getReq()));
    }

    /**
     * 变更房开详情
     * @param YWLSH 业务流水号
     * @return HousingIdGet
     */
    @Path("/showHousingCompanyAlterInfo/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response showHousingCompanyAlterInfo(final @PathParam("YWLSH") String YWLSH) {

        System.out.println("变更房开公司详情");

        return ResUtils.wrapEntityIfNeeded(this.service.showHousingCompanyAlterInfo(YWLSH));
    }

    /**
     * 受理变更房开列表
     * @param FKZH 房开公司账号
     * @param FKGS 房开公司名称
     * @param pageNo 页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    @Path("/getHousingCompanyAlterInfoAccept")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHousingCompanyAlterInfoAccept(final @Context HttpRequest httpRequest,final @QueryParam("FKZH") String FKZH, final @QueryParam("FKGS") String FKGS,final @QueryParam("ZHUANGTAI") String ZHUANGTAI,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo,
                                       final @QueryParam("pageSize") String pageSize) {

        System.out.println("获取审核列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingCompanyAlterInfoAccept((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),FKZH, FKGS,ZHUANGTAI,KSSJ,JSSJ, pageNo, pageSize));
    }

    /**
     * 受理变更房开列表
     * @param FKZH 房开公司账号
     * @param FKGS 房开公司名称
     * @param marker 页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    @Path("/getHousingCompanyAlterInfoAccept/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHousingCompanyAlterInfoAcceptNew(final @Context HttpRequest httpRequest,final @QueryParam("FKZH") String FKZH, final @QueryParam("FKGS") String FKGS,final @QueryParam("ZHUANGTAI") String ZHUANGTAI,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker,
                                                     final @QueryParam("pageSize") String pageSize,final @QueryParam("action") String action) {

        System.out.println("获取审核列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getHousingCompanyAlterInfoAcceptNew((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),FKZH, FKGS,ZHUANGTAI,KSSJ,JSSJ, marker, pageSize,action));
    }

    /**
     * 房开回执单
     * @param YWLSH 业务流水号
     * @return ApplyHousingCompanyReceipt
     */
    @Path("/receiptHousingCompanyAlter/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response receiptHousingCompanyAlter(final @PathParam("YWLSH") String YWLSH) {

        System.out.println("变更房开公司回执单");

        return ResUtils.wrapEntityIfNeeded(this.service.receiptHousingCompanyAlter(YWLSH));
    }

    /**
     * 批量提交操作
     * @param YWLSHS 业务流水号集合
     * @return CommonResponses
     */
    @Path("/submitHousingCompanyAlter")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response submitHousingCompanyAlter(final @Context HttpRequest httpRequest,final RequestWrapper<AlterIndiAcctSubmitPost> YWLSHS) {

        System.out.println("批量提交房开变更");

        return ResUtils.wrapEntityIfNeeded(this.service.submitHousingCompanyAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSHS.getReq()));
    }

}

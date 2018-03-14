package com.handge.housingfund.server.controllers.loan;


import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPost;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IHousingCompanyRecordsApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 1 级功能，2
 */
@Path("/loan/housingCompanyRecords")
@Controller
public class HousingCompanyRecordsResource {

    @Autowired
    private IHousingCompanyRecordsApi<Response> service;


    /**
     * 修改房开公司信息
     *@param CZLX 操作类型 0 保存 1 提交
     * @param YWLSH 业务流水号
     *  @param housingcompanyinfo model
     **/
    @Path("/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putReHousingCompanyInfomation(final @Context HttpRequest request, final @PathParam("YWLSH") String YWLSH, final @QueryParam("CZLX") String CZLX, final RequestWrapper<HousingCompanyPost> housingcompanyinfo) {

        System.out.println("修改房开公司信息");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.putReHousingCompanyInfomation(tokenContext,YWLSH, CZLX, housingcompanyinfo == null ? null : housingcompanyinfo.getReq()));
    }


    /**
     * 修改房开公司信息
     *
     * @param FKGSZH 房开公司账号
     * @param FKGSZH 是否启用 0禁用 1启用
     **/
    @Path("/putHousingComapnyState/{FKGSZH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reHousingCompanySFQY(final @Context HttpRequest request,final @PathParam("FKGSZH") String FKGSZH, final @QueryParam("SFQY") String SFQY) {

        System.out.println("修改房开公司信息");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.reHousingCompanySFQY(tokenContext,FKGSZH, SFQY));
    }


    /**
     * 房开公司详情
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getApplyHousingCompanyDetails(final @PathParam("YWLSH") String YWLSH, final @QueryParam("TYPE") String type) {

        System.out.println("房开公司详情");

        return ResUtils.wrapEntityIfNeeded(this.service.getApplyHousingCompanyDetails(YWLSH, type));
    }

    /**
     * 查询待审核房开信息
     *
     * @param FKGS      房开公司
     * @param ddsjStart 开始到达时间
     * @param ddsjEnd   结束到达时间
     **/
    // @Path("/getHCReviewListProcessing")
    // @GET
    // @Produces("application/json; charset=utf-8")
    // public Response getHCReviewListProcessing(final @QueryParam("FKGS") String FKGS, final @QueryParam("ddsjStart") String ddsjStart, final @QueryParam("ddsjEnd") String ddsjEnd, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {
    //
    //     System.out.println("查询待审核房开信息");
    //
    //     return ResUtils.wrapEntityIfNeeded(this.service.getHCReviewListProcessing(FKGS, ddsjStart, ddsjEnd, pageNo, pageSize));
    // }

    /**
     * 查询审核后房开信息
     *
     * @param FKGS      房开公司
     * @param slsjStart 开始受理时间
     * @param slsjEnd   结束受理时间
     **/
    // @Path("/getHCReviewListProcessed")
    // @GET
    // @Produces("application/json; charset=utf-8")
    // public Response getHCReviewListProcessed(final @QueryParam("FKGS") String FKGS, final @QueryParam("slsjStart") String slsjStart, final @QueryParam("slsjEnd") String slsjEnd, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {
    //
    //     System.out.println("查询审核后房开信息");
    //
    //     return ResUtils.wrapEntityIfNeeded(this.service.getHCReviewListProcessed(FKGS, slsjStart, slsjEnd, pageNo, pageSize));
    // }

    /**
     * 查询房开历史
     *
     * @param ZZJGDM   组织机构代码
     **/
    @Path("/getCompanyHistory/{ZZJGDM}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getCompanyHistory(final @PathParam("ZZJGDM") String ZZJGDM,final @QueryParam("pageNo") String pageNo,final @QueryParam("pageSize")String pageSize) {

        System.out.println("查询房开历史");

        return ResUtils.wrapEntityIfNeeded(this.service.getCompanyHistory(ZZJGDM,pageNo,pageSize));
    }

    /**
     *  根据房开公司账号查找房开信息
     * @param FKZH 房开公司账号
     * @return HousingIdGet
     */
    @Path("/getCompanyInfoByFKZH/{FKZH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getCompanyInfoByFKZH(final @PathParam("FKZH") String FKZH){

        System.out.println("根据房开公司账号查找房开信息");
        return ResUtils.wrapEntityIfNeeded(this.service.getCompanyInfoByFKZH(FKZH));

    }
}
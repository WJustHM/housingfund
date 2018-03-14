package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.model.BatchHousingCompanyInfo;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPost;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IHousingCompanyApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 部分1级，详细看原型，12
 */
@Path("/loan/housingCompany")
@Controller
public class HousingCompanyResource {

    @Autowired
    private IHousingCompanyApi<Response> service;

    @Path("/getHousingCompany")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHouingCompany(final @QueryParam("FKGS") String FKGS, final @QueryParam("FKZH") String FKZH,final @QueryParam("YWWD") String YWWD, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {
        System.out.println("根据房开公司与房开账号查询房开信息");
        return ResUtils.wrapEntityIfNeeded(this.service.getHousingCompany(FKGS, FKZH, YWWD,pageNo, pageSize));
    }

    /**
     * 查询过程中房开信息
     */
    @Path("/getHousingCompanyAccept")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHouingCompanyAccept(final @Context HttpRequest httpRequest, final @QueryParam("FKGS") String FKGS, final @QueryParam("ZHUANGTAI") String ZHUANGTAI,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {
        System.out.println("查询过程中房开信息");
        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.getHouingCompanyAccept(tokenContext, FKGS, ZHUANGTAI,KSSJ,JSSJ, pageNo, pageSize));
    }

    /**
     * 查询过程中房开信息
     */
    @Path("/getHousingCompanyAccept/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHouingCompanyAcceptNew(final @Context HttpRequest httpRequest, final @QueryParam("FKGS") String FKGS, final @QueryParam("ZHUANGTAI") String ZHUANGTAI,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize,final @QueryParam("action") String action) {
        System.out.println("查询过程中房开信息");
        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.getHouingCompanyAcceptNew(tokenContext, FKGS, ZHUANGTAI,KSSJ,JSSJ, marker, pageSize,action));
    }


    /**
     * 批量提交房开变更
     *
     * @param YWLSHS 业务流水号集合
     * @return CommonResponses
     */
    @Path("/submitHousingCompany")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response submitHousingCompany(final @Context HttpRequest httpRequest, final RequestWrapper<AlterIndiAcctSubmitPost> YWLSHS) {

        System.out.println("批量提交房开");

        return ResUtils.wrapEntityIfNeeded(this.service.submitHousingCompany((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), YWLSHS.getReq()));
    }

    /**
     * 新增变更房开公司信息
     **/
    @Path("/modify")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postModifyHousingCompanyInfomation(final @Context HttpRequest request, final RequestWrapper<HousingCompanyPost> housingcompanyinfo) {

        System.out.println("新增变更房开公司信息");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.postModifyHousingCompanyInfomation(tokenContext, housingcompanyinfo == null ? null : housingcompanyinfo.getReq()));
    }


    /**
     * 批量操作房开信息
     *
     * @param TYPE 类型(0申请，1变更)
     **/
    @Path("/batch/{TYPE}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putBatchHousingCompanyInfomation(final @PathParam("TYPE") String TYPE, final RequestWrapper<BatchHousingCompanyInfo> batchhousingcompanyinfo) {

        System.out.println("批量操作房开信息");

        return ResUtils.wrapEntityIfNeeded(this.service.putBatchHousingCompanyInfomation(TYPE, batchhousingcompanyinfo == null ? null : batchhousingcompanyinfo.getReq()));
    }


    /**
     * 删除房开公司信息，修改数据库字段状态
     *
     * @param TYPE 类型(0申请，1变更)
     **/
    @Path("/Records/{TYPE}")
    @DELETE
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteHousingCompanyInformation(final @PathParam("TYPE") String TYPE, final RequestWrapper<DelList> dellist) {

        System.out.println("删除房开公司信息，修改数据库字段状态");

        return ResUtils.wrapEntityIfNeeded(this.service.deleteHousingCompanyInformation(TYPE, dellist == null ? null : dellist.getReq()));
    }


    /**
     * 房开公司列表
     *
     * @param FKGS 房开公司
     * @param FKZH 房开账号
     **/
    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHousingCompanyList(final @Context HttpRequest httpRequest, final @QueryParam("FKGS") String FKGS, final @QueryParam("FKZH") String FKZH, final @QueryParam("SFQY") String SFQY, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        System.out.println("房开公司列表");
        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.getHousingCompanyList(tokenContext, FKGS, FKZH, SFQY, KSSJ, JSSJ, pageNo, pageSize));
    }

    /**
     * 房开公司列表
     *
     * @param FKGS 房开公司
     * @param FKZH 房开账号
     **/
    @Path("/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getHousingCompanyListNew(final @Context HttpRequest httpRequest, final @QueryParam("FKGS") String FKGS, final @QueryParam("FKZH") String FKZH, final @QueryParam("SFQY") String SFQY, final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize,final @QueryParam("action") String action) {

        System.out.println("房开公司列表");
        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.getHousingCompanyListNew(tokenContext, FKGS, FKZH, SFQY, KSSJ, JSSJ, marker, pageSize,action));
    }


    /**
     * 新增房开公司信息
     **/
    @Path("/addHousingCompany")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postHousingCompanyInfomation(final @Context HttpRequest request, final @QueryParam("CZLX") String CZLX, final RequestWrapper<HousingCompanyPost> housingcompanyinfo) {

        System.out.println("新增房开公司信息");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.postHousingCompanyInfomation(tokenContext, CZLX, housingcompanyinfo == null ? null : housingcompanyinfo.getReq()));
    }


    /**
     * 打印申请回执
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/printReceipt/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getApplyHousingCompanyReceipt(final @PathParam("YWLSH") String YWLSH) {

        System.out.println("打印申请回执");

        return ResUtils.wrapEntityIfNeeded(this.service.getApplyHousingCompanyReceipt(YWLSH));
    }


    /**
     * 停用/启用房开公司
     *
     * @param id    id
     * @param state (1：启用，2：停用)
     **/
    @Path("/{id}")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response putHousingCompanystate(final @PathParam("id") String id, final @QueryParam("state") String state) {

        System.out.println("停用/启用房开公司");

        return ResUtils.wrapEntityIfNeeded(this.service.putHousingCompanystate(id, state));
    }


}
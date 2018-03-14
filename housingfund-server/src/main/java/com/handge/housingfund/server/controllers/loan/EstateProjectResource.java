package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.model.BatchEstateProjectInfo;
import com.handge.housingfund.common.service.loan.model.EstateProjectBatchReviewInfo;
import com.handge.housingfund.common.service.loan.model.EstateProjectInfo;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 1 级功能，6
 */
@Path("/loan/estateProject")
@Controller
public class EstateProjectResource {

    @Autowired
    private IEstateProjectApi<Response> service;

    /**
     * 新增变更楼盘项目
     *
     * @param action 状态（0：保存 1：提交）
     * @param LPBH   楼盘编号
     **/
    @Path("/modify")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postModifyEstate(final @QueryParam("action") String action, final @QueryParam("LPBH") String LPBH, final RequestWrapper<EstateProjectInfo> estateprojectinfo) {

        System.out.println("新增变更楼盘项目");

        return ResUtils.wrapEntityIfNeeded(this.service.postModifyEstate(action, LPBH, estateprojectinfo == null ? null : estateprojectinfo.getReq()));
    }


    /**
     * 批量审核楼盘信息（待定）
     *
     * @param action 0审核通过，1审核不通过
     **/
    @Path("/BatchReview")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putEstateProjectBatchReview(final @QueryParam("action") String action, final RequestWrapper<EstateProjectBatchReviewInfo> estateprojectbatchreviewinfo) {

        System.out.println("批量审核楼盘信息");

        return ResUtils.wrapEntityIfNeeded(this.service.putEstateProjectBatchReview(action, estateprojectbatchreviewinfo == null ? null : estateprojectbatchreviewinfo.getReq()));
    }


    /**
     * 楼盘项目列表
     *
     * @param LPMC 楼盘名称
     * @param FKGS 房开公司
     **/
    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstateList(final @Context HttpRequest request,final @QueryParam("LPMC") String LPMC, final @QueryParam("FKGS") String FKGS,final @QueryParam("SFQY") String SFQY,final @QueryParam("YWWD") String YWWD, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        System.out.println("楼盘项目列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getEstateList((TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY),LPMC, FKGS, SFQY ,YWWD,pageNo, pageSize));
    }

    /**
     * 受理楼盘列表
     *
     * @param LPMC      楼盘名称
     * @param ZHUANGTAI 状态
     **/
    @Path("/getEstateProjectInfoAccept")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstateProjectInfoAccept(final @Context HttpRequest request,final @QueryParam("LPMC") String LPMC, final @QueryParam("ZHUANGTAI") String ZHUANGTAI,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

        System.out.println("楼盘项目列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getEstateProjectInfoAccept((TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY),LPMC, ZHUANGTAI,KSSJ,JSSJ, pageNo, pageSize));
    }

    /**
     * 受理楼盘列表
     *
     * @param LPMC      楼盘名称
     * @param ZHUANGTAI 状态
     **/
    @Path("/getEstateProjectInfoAccept/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstateProjectInfoAccept(final @Context HttpRequest request,final @QueryParam("LPMC") String LPMC, final @QueryParam("ZHUANGTAI") String ZHUANGTAI,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker, final @QueryParam("pageSize") String pageSize,final @QueryParam("action") String action) {

        System.out.println("楼盘项目列表");

        return ResUtils.wrapEntityIfNeeded(this.service.getEstateProjectInfoAcceptNew((TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY),LPMC, ZHUANGTAI,KSSJ,JSSJ, marker, pageSize,action));
    }



    @Path("/submitEstateProject")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response submitEstateProject(final @Context HttpRequest httpRequest,final RequestWrapper<AlterIndiAcctSubmitPost> YWLSHS) {

        System.out.println("批量提交楼盘");

        return ResUtils.wrapEntityIfNeeded(this.service.submitEstateProject((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSHS.getReq()));
    }
    
    /**
     * 新增楼盘项目
     *
     * @param action 状态（0：保存 1：提交）
     **/
    @Path("")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postEstate(final @Context HttpRequest request, final @QueryParam("action") String action, final RequestWrapper<EstateProjectInfo> estateprojectinfo) {

        System.out.println("新增楼盘项目");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.postEstate(tokenContext,action, estateprojectinfo == null ? null : estateprojectinfo.getReq()));
    }
    /**
     * 是否启用楼盘
     *
     * @param LPBH 楼盘编号
     * @param SFQY 是否启用 0禁用 1启用
     **/
    @Path("/putEstateProjectState/{LPBH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reEstateProjectSFQY(final @Context HttpRequest request, final @PathParam("LPBH") String LPBH,final @QueryParam("SFQY") String SFQY){
        System.out.println("是否启用楼盘");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.reEstateProjectSFQY(tokenContext,LPBH, SFQY));
    }


    /**
     * 单独修改状态（提交与撤回）
     *
     * @param LPBH   楼盘编号
     * @param status 状态（0：提交 1：撤回）
     **/
    @Path("/{LPBH}/state/{status}")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response putEstateStatusSubmits(final @PathParam("LPBH") String LPBH, final @PathParam("status") String status) {

        System.out.println("单独修改状态（提交与撤回）");

        return ResUtils.wrapEntityIfNeeded(this.service.putEstateStatusSubmits(LPBH, status));
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
    public Response putBatchEstateStatusInfos(final @PathParam("TYPE") String TYPE, final RequestWrapper<BatchEstateProjectInfo> batchestateprojectinfo) {

        System.out.println("批量操作房开信息");

        return ResUtils.wrapEntityIfNeeded(this.service.putBatchEstateStatusInfos(TYPE, batchestateprojectinfo == null ? null : batchestateprojectinfo.getReq()));
    }

//    /**
//     * 打印回执
//     * @param YWLSH 业务流水号
//     * @return
//     */
//    @Path("/")
//    public Response getEstateStatusReceipts(final @PathParam("YWLSH") String YWLSH){
//        return ResUtils.wrapEntityIfNeeded(this.service.getEstateProjectsReceipts(YWLSH));
//    }

}
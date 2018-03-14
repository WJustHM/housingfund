package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.model.EstateProjectInfo;
import com.handge.housingfund.common.service.loan.model.EstatePut;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectAlterApi;
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
@Path("/loan/estateProjectAlter")
@Controller
public class EstateProjectAlterResource {

    @Autowired
    private IEstateProjectAlterApi<Response> service;
    /**
     * 添加变更楼盘信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param LPBH 楼盘编号
     * @param estateprojectinfo 添加变更楼盘信息
     * @return
     */
    @Path("/addEstateProjectAlter")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEstateProjectAlter(final @Context HttpRequest request, final @QueryParam("CZLX") String CZLX, final @QueryParam("LPBH") String LPBH, final RequestWrapper<EstateProjectInfo> estateprojectinfo) {

        System.out.println("新增变更楼盘项目");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.addEstateProjectAlter(tokenContext,CZLX, LPBH, estateprojectinfo == null ? null : estateprojectinfo.getReq()));
    }

    /**
     * 修改变更楼盘信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param YWLSH  业务流水号
     * @param estatePut 修改变更楼盘信息
     * @return
     */
    @Path("/reEstateProjectAlterInfo")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reEstateProjectAlterInfo(final @Context HttpRequest request,final @QueryParam("CZLX") String CZLX, final @QueryParam("YWLSH") String YWLSH, final RequestWrapper<EstatePut> estatePut) {

        System.out.println("修改变更楼盘");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.reEstateProjectAlterInfo(tokenContext,CZLX, YWLSH, estatePut == null ? null : estatePut.getReq()));
    }

    /**
     * 变更楼盘详情
     * @param YWLSH 业务流水号
     * @return EstateIdGet
     */
    @Path("/showEstateProjectAlterInfo/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response showEstateProjectAlterInfo(final @PathParam("YWLSH") String YWLSH) {

        System.out.println("变更楼盘详情");

        return ResUtils.wrapEntityIfNeeded(this.service.showEstateProjectAlterInfo(YWLSH));
    }

    /**
     * 受理变更楼盘列表
     * @param LPMC 变更楼盘名称
     * @param ZHUANGTAI 业务状态
     * @param pageNo  页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    @Path("/getEstateProjectAlterInfoAccept")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstateProjectAlterInfoAccept(final @Context HttpRequest request,final @QueryParam("LPMC") String LPMC, final @QueryParam("ZHUANGTAI") String ZHUANGTAI,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("pageNo") String pageNo,
                                                     final @QueryParam("pageSize") String pageSize) {

        System.out.println("获取审核列表");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.getEstateProjectAlterInfoAccept(tokenContext,LPMC, ZHUANGTAI,KSSJ,JSSJ, pageNo, pageSize));
    }

    /**
     * 受理变更楼盘列表
     * @param LPMC 变更楼盘名称
     * @param ZHUANGTAI 业务状态
     * @param marker  页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    @Path("/getEstateProjectAlterInfoAccept/new")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstateProjectAlterInfoAcceptNew(final @Context HttpRequest request,final @QueryParam("LPMC") String LPMC, final @QueryParam("ZHUANGTAI") String ZHUANGTAI,final @QueryParam("KSSJ") String KSSJ,final @QueryParam("JSSJ") String JSSJ, final @QueryParam("marker") String marker,
                                                    final @QueryParam("pageSize") String pageSize,final @QueryParam("action") String action) {

        System.out.println("获取审核列表");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.service.getEstateProjectAlterInfoAcceptNew(tokenContext,LPMC, ZHUANGTAI,KSSJ,JSSJ, marker, pageSize,action));
    }


    /**
     * 批量提交操作
     * @param YWLSHS 业务流水号集合
     * @return
     */
    @Path("/submitEstateProjectAlter")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response submitEstateProjectAlter(final @Context HttpRequest httpRequest,final RequestWrapper<AlterIndiAcctSubmitPost> YWLSHS) {

        System.out.println("批量提交楼盘变更");

        return ResUtils.wrapEntityIfNeeded(this.service.submitEstateProjectAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSHS.getReq()));
    }
}

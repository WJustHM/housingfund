package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.EstatePut;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectsRecordApi;
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
@Path("/loan/estateProjectsRecord")
@Controller
public class EstateProjectsRecordResource {

    @Autowired
    private IEstateProjectsRecordApi<Response> estateProjectsRecordApi;

    /**
     * 修改楼盘项目信息
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putEstateId(final @Context HttpRequest request, final @QueryParam("CZLX") String CZLX, final @PathParam("YWLSH") String YWLSH, final RequestWrapper<EstatePut> estateput) {

        System.out.println("修改楼盘项目信息");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.estateProjectsRecordApi.putEstateId(tokenContext,CZLX, YWLSH, estateput == null ? null : estateput.getReq()));
    }


    /**
     * 楼盘项目详情（打印回执单）
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstateId(final @PathParam("YWLSH") String YWLSH) {

        System.out.println("楼盘项目详情（打印回执单）");

        return ResUtils.wrapEntityIfNeeded(this.estateProjectsRecordApi.getEstateId(YWLSH));
    }
    /**
     * 楼盘项目历史
     *
     * @param LPBH 楼盘编号
     **/
    @Path("/getEstateProjectHistory/{LPBH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstateProjectHistory(final @PathParam("LPBH") String LPBH,final @QueryParam("pageNo") String pageNo,final @QueryParam("pageSize") String pageSize) {

        System.out.println("楼盘项目历史");

        return ResUtils.wrapEntityIfNeeded(this.estateProjectsRecordApi.getEstateProjectHistory(LPBH,pageNo,pageSize));
    }

    /**
     *  根据楼盘编号查找楼盘信息
     * @param LPBH 楼盘编号
     * @return EstateIdGet
     */
    @Path("/getEstateProjectInfoByLPBH/{LPBH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstateProjectInfoByLPBH(final @PathParam("LPBH") String LPBH) {
        System.out.println("根据楼盘编号查找楼盘信息");
        return ResUtils.wrapEntityIfNeeded(this.estateProjectsRecordApi.getEstateProjectInfoByLPBH(LPBH));
    }

    /**
     *  根据楼盘名称查找售房人开户信息
     * @param LPMC 楼盘名称
     * @return EstateIdGet
     */
    @Path("/getSFRZHHM/{LPMC}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getSFRZHHM(final @PathParam("LPMC") String LPMC) {
        System.out.println("根据楼盘名称查找售房人开户信息");
        return ResUtils.wrapEntityIfNeeded(this.estateProjectsRecordApi.getSFRZHHM(LPMC));
    }


}
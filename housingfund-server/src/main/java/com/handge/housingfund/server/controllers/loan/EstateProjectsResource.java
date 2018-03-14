package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * 1 级功能，3
 */
@Path("/loan/estateProjects")
@Controller
public class EstateProjectsResource {

    @Autowired
    private IEstateProjectsApi<Response> service;

    /**
     * 停用/启用楼盘
     *
     * @param LPBH  楼盘编号
     * @param state (1：启用，2：停用)
     **/
    @Path("/{LPBH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response putEstateProjectsState(final @PathParam("LPBH") String LPBH, final @QueryParam("state") String state) {

        System.out.println("停用/启用楼盘");

        return ResUtils.wrapEntityIfNeeded(this.service.putEstateProjectsstate(LPBH, state));
    }


    /**
     * 删除楼盘项目受理
     *
     * @param TYPE 1：申请受理 ,2:变更受理
     **/
    @Path("/Records/{TYPE}")
    @DELETE
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEstateId(final @PathParam("TYPE") String TYPE, final RequestWrapper<DelList> dellist) {

        System.out.println("删除楼盘项目信息");

        return ResUtils.wrapEntityIfNeeded(this.service.deleteEstateId(TYPE, dellist == null ? null : dellist.getReq()));
    }


    /**
     * 受理记录
     *
     * @param TYPE      1：申请受理 ,2:变更受理
     * @param LPMC      楼盘名称
     * @param ZhuangTai 状态(00所有，01新建，02待审核，03审核不通过，04办结)
     **/
    @Path("/Records/{TYPE}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstatRecords(final @PathParam("TYPE") String TYPE, final @QueryParam("LPMC") String LPMC, final @QueryParam("ZhuangTai") String ZhuangTai) {

        System.out.println("受理记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getEstatRecords(TYPE, LPMC, ZhuangTai));
    }


}
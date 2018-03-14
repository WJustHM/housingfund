package com.handge.housingfund.server.controllers.account;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.DepartmentBody;
import com.handge.housingfund.common.service.account.model.IdListBody;
import com.handge.housingfund.common.service.account.model.NetWorkBody;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.account.api.INetworkManagementAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gxy on 17-7-15.
 */
@Path("/account/outlets")
@Controller
public class NetworkManagementController {

    @Autowired
    INetworkManagementAPI<Response> networkManagementAPI;

    @GET
    @Path("/network")
    @Produces("application/json; charset=utf-8")
    public Response getNetworkList(@QueryParam("pageNo") int pageNo, @QueryParam("pageSize") int pageSize, final @QueryParam("MingCheng") String MingCheng) {
        System.out.println("获取网点列表");
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.getNetworkList(pageNo, pageSize, MingCheng));
    }

    @GET
    @Path("/network/{id}")
    @Produces("application/json; charset=utf-8")
    public Response getNetworkDetail(final @PathParam("id") String id) {
        System.out.println("获取网点详情");
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.getNetworkDetail(id));
    }

    @POST
    @Path("/network")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putNetwork(final @Context HttpRequest request, NetWorkBody network) {
        System.out.println("新增网点");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.addNetwork(tokenContext, network.getReq()));
    }

    @PUT
    @Path("/network/{id}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateNetwork(final @Context HttpRequest request, final @PathParam("id") String id, NetWorkBody network) {
        System.out.println("修改网点信息");
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.updateNetwork(tokenContext, id, network.getReq()));
    }

    @DELETE
    @Path("/network")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteNetwork(IdListBody idListBody) {
        System.out.println(idListBody.getReq().get(0));
        System.out.println("删除网点");
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.deleteNetwork(idListBody.getReq()));
    }

    @GET
    @Path("/department")
    @Produces("application/json; charset=utf-8")
    public Response getDepartmentList(final @QueryParam("id") String id) {
        System.out.println("获取部门列表");
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.getDepartmentList(id));
    }

//    @GET
//    @Path("/department/{id}")
//    @Produces("application/json; charset=utf-8")
//    public Response getDepartmentDetail(final @PathParam("id") String id) {
//        System.out.println("获取部门详情");
//        return ResUtils.wrapEntityIfNeeded(this.service.getDepartmentDetial(id));
//    }

    @POST
    @Path("/department")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putDepartment(DepartmentBody departmentBody) {
        System.out.println("新增/修改部门");
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.addDepartment(departmentBody.getReq()));
    }
//
//    @PUT
//    @Path("/department/{id}")
//    @Produces("application/json; charset=utf-8")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response updateDepartment(final @PathParam("id") String id, Department department) {
//        System.out.println("更新部门信息");
//        return ResUtils.wrapEntityIfNeeded(this.service.updateDepartment(id, department));
//    }

    @DELETE
    @Path("/department")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(IdListBody idListBody) {
        System.out.println("删除部门");
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.deleteDepartment(idListBody.getReq()));
    }

    @GET
    @Path("/department/member")
    @Produces("application/json; charset=utf-8")
    public Response getDepartmentMemberList(@QueryParam("id") String id) {
        System.out.println("获取未分配部门成员列表");
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.getDepartmentMember(id));
    }

    @GET
    @Path("/area")
    @Produces("application/json; charset=utf-8")
    public Response getArea(@QueryParam("id") String id) {
        System.out.println("获取地区");
        return ResUtils.wrapEntityIfNeeded(this.networkManagementAPI.getArea(id));
    }

}

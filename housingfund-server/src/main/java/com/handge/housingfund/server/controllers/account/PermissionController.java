package com.handge.housingfund.server.controllers.account;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.EmployeeReq;
import com.handge.housingfund.common.service.account.model.IdListBody;
import com.handge.housingfund.common.service.account.model.RoleBody;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.account.api.IPermissionAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tanyi on 2017/7/18.
 */
@Path("/account/organization")
@Controller
public class PermissionController {

    @Autowired
    private IPermissionAPI<Response> permissiomAPI;

    @Path("/role")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getRoleList(@QueryParam("roleName") String roleName, @QueryParam("pageNo") int pageNo, @QueryParam("pageSize") int pageSize) {
        System.out.println("获取角色列表");
        System.out.println(roleName + "|" + pageNo + "|" + pageSize);
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.getRoleList(roleName, pageNo, pageSize));
    }

    @Path("/role/{id}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getRoleDetail(@PathParam("id") String id) {
        System.out.println("获取角色详情");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.getRoleDetail(id));
    }

    @Path("/role")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRole(RoleBody roleBody) {
        System.out.println("添加角色");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.addRole(roleBody != null ? roleBody.getReq() : null));
    }

    @Path("/role/{id}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRole(@PathParam("id") String id, RoleBody roleBody) {
        System.out.println("修改角色");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.putRole(id, roleBody != null ? roleBody.getReq() : null));
    }

    @Path("/role/{id}")
    @DELETE
    @Produces("application/json; charset=utf-8")
    public Response delRole(@PathParam("id") String id) {
        System.out.println("删除角色");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.delRole(id));
    }

    @Path("/roleEmployee")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getRoleEmployeeList(@QueryParam("name") String name, @QueryParam("id") String id) {
        System.out.println("成员管理");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.getRoleEmployeeList(name, id));
    }

    @Path("/roleEmployee")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRoleEmployee(@QueryParam("id") String id, IdListBody idListBody) {
        System.out.println("修改该角色下的成员");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.putRoleEmployee(id, idListBody != null ? idListBody.getReq() : null));
    }

    @Path("/classify")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getClassifyList(@QueryParam("id") String id) {
        System.out.println("获取编辑权限列表");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.getClassifyList(id));
    }

    @Path("/permission")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPermission(@QueryParam("id") String id, IdListBody idListBody) {
        System.out.println("给角色添加权限");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.addPermission(id, idListBody != null ? idListBody.getReq() : null));
    }

    @Path("/approve")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getApproveList() {
        System.out.println("获取审批流程");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.getApproveList());
    }

    @Path("/approve")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addApprove() {
        System.out.println("添加审批流程");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.addApprove());
    }


    @Path("/employee")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEmployeeList(@QueryParam("pageNo") int pageNo, @QueryParam("pageSize") int pageSize,
                                    @QueryParam("WangDian") String WangDian, @QueryParam("XingMing") String XingMing,
                                    @QueryParam("ZhangHao") String ZhangHao, @QueryParam("BuMen") String BuMen) {
        System.out.println("获取员工列表");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.getEmployeeList(pageNo, pageSize, WangDian, XingMing, ZhangHao, BuMen));
    }

    @Path("/employee/{id}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEmployeeDetail(@PathParam("id") String id) {
        System.out.println("获取员工详情");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.getEmployeeDetail(id));
    }

    @Path("/employee")
    @POST
    @Produces("application/json; charset=utf-8")
    public Response addEmployee(EmployeeReq employeeBody) {
        System.out.println("新增员工");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.addEmployee(employeeBody.getReq()));
    }

    @Path("/employee/{id}")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response putEmployee(@PathParam("id") String id, EmployeeReq employeeBody) {
        System.out.println("修改员工信息");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.putEmployee(id, employeeBody.getReq()));
    }

    @Path("/employee")
    @DELETE
    @Produces("application/json; charset=utf-8")
    public Response delEmployee(IdListBody idListBody) {
        System.out.println("删除员工");
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.delEmployee(idListBody.getReq()));
    }

    @Path("/loginPersonInfo")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getPersonInfo(@Context HttpRequest httpRequest) {
        System.out.println("获取个人详情");
        TokenContext tokenContext = (TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY);
        //用户类型（0：最高管理员，1：个人账户，2：员工账户，3：单位账户，4：房开商账号）
        return ResUtils.wrapEntityIfNeeded(this.permissiomAPI.getEmployeeInfo(tokenContext));
    }
}

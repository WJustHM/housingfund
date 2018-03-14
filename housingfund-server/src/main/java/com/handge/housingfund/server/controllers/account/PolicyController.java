package com.handge.housingfund.server.controllers.account;

import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.account.api.IPolicyAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by 向超 on 2017/9/20.
 */
@Path("/account/policy")
@Controller
public class PolicyController {
    @Autowired
    private IPolicyAPI<Response> policyAPI;

    /**
     * 获取政策信息
     *
     * @return
     */
    @Path("/getPolicy")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getPolicy() {
        System.out.println("获取政策信息");

        return ResUtils.wrapEntityIfNeeded(this.policyAPI.getPolicy());
    }

    /**
     * @param ID
     * @param XGZ  修改值
     * @param SXRQ 生效日期
     * @return
     */
    @Path("/updatePolicy/{ID}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRoleDetail(@PathParam("ID") String ID, @QueryParam("XGZ") String XGZ, @QueryParam("SXRQ") String SXRQ) {
        System.out.println("更改政策信息");
        return ResUtils.wrapEntityIfNeeded(this.policyAPI.updatePolicyInfo(ID, XGZ, SXRQ));
    }
}

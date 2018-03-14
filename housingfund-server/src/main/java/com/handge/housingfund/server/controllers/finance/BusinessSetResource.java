package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IBusinessSetAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
@Path("/finance/business")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class BusinessSetResource {

    @Autowired
    IBusinessSetAPI iBusinessSetAPI;

    @Path("")
    @GET
    public Response getBusinessList(final @QueryParam("name") String name,final @QueryParam("pageNo") int pageNo,
                                    final @QueryParam("pageSize") int pageSize) {
        System.out.println("获取业务类型列表");
        return ResUtils.wrapEntityIfNeeded(iBusinessSetAPI.getBusinessList(name, pageNo, pageSize));
    }

    @DELETE
    @Path("/{id}")
    public Response delBusiness(final @PathParam("id") String id) {
        System.out.println("删除业务类型");
        return ResUtils.wrapEntityIfNeeded(iBusinessSetAPI.delBusiness(id));
    }

    @Path("")
    @DELETE
    public Response delBusinesses(final RequestWrapper<ArrayList<String>> delList) {
        System.out.println("批量删除业务类型");
        return ResUtils.wrapEntityIfNeeded(iBusinessSetAPI.delBusinesses(delList == null ? null : delList.getReq()));
    }
}

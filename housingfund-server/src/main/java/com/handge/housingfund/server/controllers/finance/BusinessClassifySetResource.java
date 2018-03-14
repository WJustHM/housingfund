package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.service.finance.model.BusinessClassifySet;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IBusinessClassifySetAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
@Path("/finance/businessClassify")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class BusinessClassifySetResource {

    @Autowired
    IBusinessClassifySetAPI iBusinessClassifySetAPI;

    @Path("")
    @GET
    public Response getBusinessClassifys() {
        System.out.println("获取日常业务分类集合");
        return ResUtils.wrapEntityIfNeeded(iBusinessClassifySetAPI.getBusinessClassifys());
    }

    @Path("")
    @POST
    public Response updateBusinessClassify(final RequestWrapper<ArrayList<BusinessClassifySet>> businessClassifySetRequestWrapper) {
        System.out.println("更新日常业务分类");
        return ResUtils.wrapEntityIfNeeded(iBusinessClassifySetAPI.updateBusinessClassify(businessClassifySetRequestWrapper == null ? null : businessClassifySetRequestWrapper.getReq()));
    }

//    @DELETE
//    public Response delBusinessClassify(final RequestWrapper<ArrayList<String>> delListRequestWrapper) {
//        System.out.println("删除日常业务分类");
//        return ResUtils.wrapEntityIfNeeded(iBusinessClassifySetAPI.delBusinessClassify(delListRequestWrapper == null ? null : delListRequestWrapper.getReq()));
//    }

    @POST
    @Path("/business")
    public Response updateBusiness(final RequestWrapper<BusinessClassifySet> businessClassifySetRequestWrapper) {
        System.out.println("更新日常业务");
        return ResUtils.wrapEntityIfNeeded(iBusinessClassifySetAPI.updateBusiness(businessClassifySetRequestWrapper == null ? null : businessClassifySetRequestWrapper.getReq()));
    }
}

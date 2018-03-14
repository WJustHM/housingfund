package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IHistoryRecordApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * 待定
 */
@Path("/loan/historyRecord")
@Controller
public class HistoryRecordResource {

    @Autowired
    private IHistoryRecordApi<Response> service;

    /**
     * 房开历史业务记录
     *
     * @param ZZJGDM 组织机构代码
     *
     **/
    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response historyRecordList(final @QueryParam("ZZJGDM") String ZZJGDM) {

        System.out.println("历史业务记录");

        return ResUtils.wrapEntityIfNeeded(this.service.getCompanyHistory(ZZJGDM));
    }


}
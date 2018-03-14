package com.handge.housingfund.server.controllers.account;

import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.account.api.ICenterInfoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gxy on 17-12-8.
 */
@Path("/account")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class CenterInfoResource {

    @Autowired
    private ICenterInfoAPI iCenterInfoAPI;

    /**
     * 获取中心信息列表
     * @param zxmc   中心名称
     * @param zxdm   中心代码
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Path("/centerinfo")
    @GET
    public Response getBankInfoList(final @QueryParam("ZXMC") String zxmc,
                                    final @QueryParam("ZXDM") String zxdm,
                                    final @QueryParam("pageNo") int pageNo,
                                    final @QueryParam("pageSize") int pageSize) {

        System.out.println("通过公积金中心名称或代码获取中心列表");
        return ResUtils.wrapEntityIfNeeded(iCenterInfoAPI.getCenterInfoList(zxmc, zxdm, pageNo, pageSize));
    }

}

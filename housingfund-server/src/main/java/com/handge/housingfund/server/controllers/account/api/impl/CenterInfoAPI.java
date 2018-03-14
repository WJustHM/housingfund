package com.handge.housingfund.server.controllers.account.api.impl;

import com.handge.housingfund.common.service.account.ICenterInfoService;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.server.controllers.account.api.ICenterInfoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

/**
 * Created by gxy on 17-12-8.
 */
@Component
public class CenterInfoAPI implements ICenterInfoAPI {

    @Autowired
    private ICenterInfoService iCenterInfoService;

    @Override
    public Response getCenterInfoList(String zxmc, String zxdm, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(iCenterInfoService.getCenterInfoList(zxmc, zxdm, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.server.controllers.loan.api.IHistoryRecordApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class HistoryRecordApi implements IHistoryRecordApi<Response> {
    @Autowired
    private IHistoryRecordApi historyRecordApi;
    /**
     * 历史业务记录
     *
     * @param ZZJGDM 组织机构代码
     *
     **/
    public Response getCompanyHistory(final String ZZJGDM) {

        System.out.println("历史业务记录");

        return Response.status(200).entity(historyRecordApi.getCompanyHistory(ZZJGDM)).build();
    }


}
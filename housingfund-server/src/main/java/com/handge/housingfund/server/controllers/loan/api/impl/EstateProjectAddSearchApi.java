package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.loan.model.EstateProjectAddSearchGet;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectAddSearchApi;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class EstateProjectAddSearchApi implements IEstateProjectAddSearchApi<Response> {


    /**
     * 选择房开公司
     *
     * @param FKZH 房开账号
     * @param FKGS 房开公司
     **/
    public Response getEstate(final String FKZH, final String FKGS) {

        System.out.println("选择房开公司");

        return Response.status(200).entity(new EstateProjectAddSearchGet() {{


        }}).build();
    }


}
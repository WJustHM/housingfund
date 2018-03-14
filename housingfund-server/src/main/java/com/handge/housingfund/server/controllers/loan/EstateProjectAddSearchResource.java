package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectAddSearchApi;
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
@Path("/loan/estateProjectAddSearch")
@Controller
public class EstateProjectAddSearchResource {

    @Autowired
    private IEstateProjectAddSearchApi<Response> service;

    /**
     * 选择房开公司
     *
     * @param FKZH 房开账号
     * @param FKGS 房开公司
     **/
    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getEstate(final @QueryParam("FKZH") String FKZH, final @QueryParam("FKGS") String FKGS) {

        System.out.println("选择房开公司");

        return ResUtils.wrapEntityIfNeeded(this.service.getEstate(FKZH, FKGS));
    }


}
package com.handge.housingfund.server.controllers.finance;

import com.handge.housingfund.common.service.finance.model.AccountBookModel;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.IAccountBookAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by xuefei_wang on 17-8-16.
 */
@Path("/finance/accountbook")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class AccountBookResource {

    @Autowired
    IAccountBookAPI iAccountBookAPI;

    /**
     * 账套列表
     * @return
     */
    @Path("")
    @GET
    public Response listAccountBooks(){
        System.out.println("查询账套列表");
        return ResUtils.wrapEntityIfNeeded(iAccountBookAPI.getAccountBookList());
    }

    /**
     * 修改账套
     * @param accountBookModel
     * @return
     */
    @Path("")
    @PUT
    public Response updateAccountBook(final RequestWrapper<AccountBookModel> accountBookModel){
        System.out.println("修改账套");
        return ResUtils.wrapEntityIfNeeded(iAccountBookAPI.updateAccountBook(accountBookModel == null ? null : accountBookModel.getReq()));
    }

    /**
     * 获取会计期间列表
     * @param KJND 会计年度
     * @return
     */
    @GET
    @Path("/period")
    public Response getAccountPeriodList(final @QueryParam("KJND") String KJND){
        System.out.println("获取会计期间列表");
        return ResUtils.wrapEntityIfNeeded(iAccountBookAPI.getAccountPeriodList(KJND));
    }

    /**
     * 增加会计年度
     * @param KJND 会计年度
     **/
    @Path("/period/{KJND}")
    @POST
    public Response addAccountPeriod(final @PathParam("KJND") String KJND){
        System.out.println("增加会计年度");
        return ResUtils.wrapEntityIfNeeded(iAccountBookAPI.addAccountPeriod(KJND));
    }
}

package com.handge.housingfund.server.controllers.account;

import com.handge.housingfund.common.service.account.model.SettlementSpecialBankAccount;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.account.api.ISettlementSpecialBankAccountManageAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/5.
 */
@Path("/account/special")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class SettlementSpecialBankAccountManageController {

    @Autowired
    ISettlementSpecialBankAccountManageAPI accountManageAPI;

    @Path("")
    @GET
    public Response getSpecialAccountList(final @QueryParam("YHMC") String yhmc,
                                          final @QueryParam("YHZHHM") String yhzhhm,
                                          final @QueryParam("status") String status,
                                          final @QueryParam("pageNo") int pageNo,
                                          final @QueryParam("pageSize") int pageSize) {
        System.out.println("获取专户列表");
        return ResUtils.wrapEntityIfNeeded(accountManageAPI.getSpecialAccountList(yhmc, yhzhhm, status, pageNo, pageSize));
    }

    @GET
    @Path("/{id}")
    public Response getSpecialAccountById(final @PathParam("id") String id) {
        System.out.println("获取专户详情");
        return ResUtils.wrapEntityIfNeeded(accountManageAPI.getSpecialAccountById(id));
    }

    @Path("")
    @POST
    public Response addSpecialAccount(final RequestWrapper<SettlementSpecialBankAccount> requestWrapper) {
        System.out.println("添加银行专户");
        return ResUtils.wrapEntityIfNeeded(accountManageAPI.addSpecialAccount(requestWrapper == null ? null : requestWrapper.getReq()));
    }

    @PUT
    @Path("/{id}")
    public Response updateSpecialAccount(final @PathParam("id") String id, final RequestWrapper<SettlementSpecialBankAccount> requestWrapper) {
        System.out.println("修改银行专户");
        return ResUtils.wrapEntityIfNeeded(accountManageAPI.updateSpecialAccount(id, requestWrapper == null ? null : requestWrapper.getReq()));
    }

    @DELETE
    @Path("/{id}")
    public Response delSpecialAccount(final @PathParam("id") String id) {
        System.out.println("删除银行专户");
        return ResUtils.wrapEntityIfNeeded(accountManageAPI.delSpecialAccount(id));
    }

    @Path("")
    @DELETE
    public Response delSpecialAccounts(final RequestWrapper<ArrayList<String>> delList) {
        System.out.println("批量删除银行专户");
        return ResUtils.wrapEntityIfNeeded(accountManageAPI.delSpecialAccounts(delList == null ? null : delList.getReq()));
    }

    @PUT
    @Path("/cancel/{id}")
    public Response cancelSpecialAccount(final @PathParam("id") String id) {
        System.out.println("银行专户销户");
        return ResUtils.wrapEntityIfNeeded(accountManageAPI.cancelSpecialAccount(id));
    }

    @GET
    @Path("/accountinfo")
    public Response getSpecialAccount(final @QueryParam("YHMC") String yhmc) {
        System.out.println("根据对手方银行获取本公积金中心的对应的银行账户");
        return ResUtils.wrapEntityIfNeeded(accountManageAPI.getSpecialAccount(yhmc));
    }
}


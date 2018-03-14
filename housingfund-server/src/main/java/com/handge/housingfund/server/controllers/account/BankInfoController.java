package com.handge.housingfund.server.controllers.account;

import com.handge.housingfund.common.service.account.model.BankContactReq;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.account.api.IBankInfoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/9/6.
 */
@Path("/account")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class BankInfoController {

    @Autowired
    IBankInfoAPI iBankInfoAPI;

    /**
     * @param yhmc     银行名称
     * @param code     银行代码
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Path("/bankinfo")
    @GET
    public Response getBankInfoList(final @QueryParam("YHMC") String yhmc,
                                    final @QueryParam("CODE") String code,
                                    final @QueryParam("pageNo") int pageNo,
                                    final @QueryParam("pageSize") int pageSize) {

        System.out.println("通过银行名称或代码获取银行列表");
        return ResUtils.wrapEntityIfNeeded(iBankInfoAPI.getBankInfoList(yhmc, code, pageNo, pageSize));
    }

    /**
     * @param yhmc     银行名称
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Path("/bankContact")
    @GET
    public Response getBankContactList(final @QueryParam("YHMC") String yhmc,
                                       final @QueryParam("pageNo") int pageNo,
                                       final @QueryParam("pageSize") int pageSize) {

        System.out.println("通过银行名称签约银行列表");
        return ResUtils.wrapEntityIfNeeded(iBankInfoAPI.getBankContactList(yhmc, pageNo, pageSize));
    }

    /**
     * @param bankContactReq 签约银行数据
     * @return
     */
    @Path("/bankContact")
    @POST
    public Response addBankContact(final RequestWrapper<BankContactReq> bankContactReq) {
        System.out.println("添加签约银行");
        return ResUtils.wrapEntityIfNeeded(iBankInfoAPI.addBankContact(bankContactReq.getReq()));
    }

    /**
     * @param id 签约银行ID
     * @return
     */
    @Path("/bankContact/{id}")
    @GET
    public Response getBankContactDetail(final @PathParam("id") String id) {

        System.out.println("获取签约银行详情");
        return ResUtils.wrapEntityIfNeeded(iBankInfoAPI.getBankContactDetail(id));
    }

    /**
     * @param id             签约银行ID
     * @param bankContactReq 签约银行数据
     * @return
     */
    @Path("/bankContact/{id}")
    @POST
    public Response putBankContact(final @PathParam("id") String id, final RequestWrapper<BankContactReq> bankContactReq) {
        System.out.println("修改签约银行");
        return ResUtils.wrapEntityIfNeeded(iBankInfoAPI.putBankContact(id, bankContactReq.getReq()));
    }

    /**
     * @param delList 删除列表
     * @return
     */
    @Path("/bankContact")
    @DELETE
    public Response delBankContact(final RequestWrapper<DelList> delList) {
        System.out.println("删除签约银行");
        return ResUtils.wrapEntityIfNeeded(iBankInfoAPI.delBankContact(delList.getReq()));
    }

}

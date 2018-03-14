package com.handge.housingfund.server.controllers.account.api.impl;

import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.SettlementSpecialBankAccount;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.account.api.ISettlementSpecialBankAccountManageAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/5.
 */
@Component
public class SettlementSpecialBankAccountManageAPI implements ISettlementSpecialBankAccountManageAPI {
    @Autowired
    ISettlementSpecialBankAccountManageService accountManageService;

    @Override
    public Response getSpecialAccountList(String yhmc, String yhzhhm, String status, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(accountManageService.getSpecialAccountList(yhmc, yhzhhm, status, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getSpecialAccountById(String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(accountManageService.getSpecialAccountById(id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response addSpecialAccount(SettlementSpecialBankAccount settlementSpecialBankAccount) {
        if (settlementSpecialBankAccount == null) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户信息不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHZHHM())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户号码不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHZHMC())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户名称不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHMC())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户银行名称不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHDM())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户银行代码不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getKMBH())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户科目编号不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getNode())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("银行节点号不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHID())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("银行id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(accountManageService.addSpecialAccount(settlementSpecialBankAccount) ).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response updateSpecialAccount(String id, SettlementSpecialBankAccount settlementSpecialBankAccount) {
        if (!StringUtil.notEmpty(id) || settlementSpecialBankAccount == null) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户信息不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHZHHM())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户号码不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHZHMC())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户名称不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHMC())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户银行名称不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHDM())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户银行代码不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getKMBH())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户科目编号不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getNode())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("银行节点号不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(settlementSpecialBankAccount.getYHID())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("银行id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(accountManageService.updateSpecialAccount(id, settlementSpecialBankAccount) ? new Msg(){{
                this.setCode(ReturnCode.Success);
                this.setMsg("该专户已在使用，不能修改");
            }}: new Success(){{
                this.setState("修改专户信息成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delSpecialAccount(String id) {
        if (! StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("银行专户id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(accountManageService.delSpecialAccount(id) ? new Msg(){{
                this.setCode(ReturnCode.Success);
                this.setMsg("已使用专户，不能删除");
            }}: new Success(){{
                this.setState("删除专户成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delSpecialAccounts(ArrayList<String> ids) {
        if (ids == null || ids.size() <= 0)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("专户id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        for (String id : ids) {
            if (!StringUtil.notEmpty(id)) {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("专户id不能为空");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        }

        try {
            return Response.status(200).entity(accountManageService.delSpecialAccounts(ids) ? new Msg(){{
                this.setCode(ReturnCode.Success);
                this.setMsg("已使用专户，不能删除");
            }}: new Success(){{
                this.setState("删除专户成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response cancelSpecialAccount(String id) {
        if (! StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("银行专户id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            accountManageService.cancelSpecialAccount(id);
            return Response.status(200).entity(new Success(){{
                this.setState("专户销户成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getSpecialAccount(String yhmc) {
        if (! StringUtil.notEmpty(yhmc)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("银行名称不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(accountManageService.getSpecialAccount(yhmc)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

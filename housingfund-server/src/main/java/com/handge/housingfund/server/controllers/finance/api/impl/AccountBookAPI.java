package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.finance.IAccountBookService;
import com.handge.housingfund.common.service.finance.model.AccountBookModel;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.IAccountBookAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/8/23.
 */
@Component
public class AccountBookAPI implements IAccountBookAPI {

    @Autowired
    IAccountBookService iAccountBookService;

    @Override
    public Response getAccountBookList() {
        try {
            return Response.status(200).entity(iAccountBookService.getAccountBookList()).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response updateAccountBook(AccountBookModel accountBookModel) {
        if (accountBookModel == null || !StringUtil.notEmpty(accountBookModel.getZTMC()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("账套内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            iAccountBookService.updateAccountBook(accountBookModel);
            return Response.status(200).entity(new Success(){{
                this.setState("更新成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getAccountPeriodList(String KJND) {
        if (!StringUtil.notEmpty(KJND))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("会计年度不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iAccountBookService.getAccountPeriodList(KJND)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response addAccountPeriod(String KJND) {
        if (!StringUtil.notEmpty(KJND))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("会计年度不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iAccountBookService.addAccountPeriod()).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

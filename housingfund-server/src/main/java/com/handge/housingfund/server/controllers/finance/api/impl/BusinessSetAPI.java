package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.finance.IBusinessSetService;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.IBusinessSetAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
@Component
public class BusinessSetAPI implements IBusinessSetAPI {

    @Autowired
    IBusinessSetService iBusinessSetService;

    @Override
    public Response getBusinessList(String name, int pageNo, int pageSize) {

        try {
            return Response.status(200).entity(iBusinessSetService.getBusinessList(name, pageNo, pageSize)).build();
        } catch (ErrorException e ) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delBusiness(String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("业务类型id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        try {
            return Response.status(200).entity(iBusinessSetService.delBusiness(id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("默认或已使用业务类型，不能删除");
            }} : new Success(){{
                this.setState("删除业务类型成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delBusinesses(ArrayList<String> delList) {
        if (delList == null || delList.size() <= 0)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务类型id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        for (String id : delList) {
            if (!StringUtil.notEmpty(id)) {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("业务类型id不能为空");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        }

        try {
            return Response.status(200).entity(iBusinessSetService.delBusinesses(delList) ? new Msg(){{
                this.setCode(ReturnCode.Success);
                this.setMsg("业务类型为默认或已在使用，不能删除");
            }}: new Success(){{
                this.setState("删除业务类型成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

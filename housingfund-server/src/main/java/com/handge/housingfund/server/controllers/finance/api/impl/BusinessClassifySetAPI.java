package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.finance.IBusinessClassifySetService;
import com.handge.housingfund.common.service.finance.model.BusinessClassifySet;
import com.handge.housingfund.common.service.finance.model.BusinessSet;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.IBusinessClassifySetAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
@Component
public class BusinessClassifySetAPI implements IBusinessClassifySetAPI {

    @Autowired
    IBusinessClassifySetService iBusinessClassifySetService;

    @Override
    public Response getBusinessClassifys() {
        try {
            return Response.status(200).entity(iBusinessClassifySetService.getBusinessClassifys()).build();
        } catch (ErrorException e ) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response updateBusinessClassify(ArrayList<BusinessClassifySet> businessClassifySets) {
        if (businessClassifySets == null || businessClassifySets.size() <= 0 )
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务分类内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        for (BusinessClassifySet classify: businessClassifySets) {
            if (classify == null || !StringUtil.notEmpty(classify.getYWMC()))
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("业务分类或分类名称不能为空");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
        }

        try {
            return Response.status(200).entity(iBusinessClassifySetService.updateBusinessClassify(businessClassifySets) ? new Msg(){{
                this.setCode(ReturnCode.Success);
                this.setMsg("业务分类为默认分类或已在使用，不能修改或删除");
            }}: new Success(){{
                this.setState("更新业务分类成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

//    @Override
//    public Response delBusinessClassify(ArrayList<String> delList) {
//        if (delList == null || delList.size() <= 0)
//            return Response.status(200).entity(new Msg() {
//                {
//                    this.setMsg("业务分类id不能为空");
//                    this.setCode(ReturnCode.Error);
//                }
//            }).build();
//
//        for (String id : delList) {
//            if (!StringUtil.notEmpty(id))
//                return Response.status(200).entity(new Msg() {
//                    {
//                        this.setMsg("业务分类id不能为空");
//                        this.setCode(ReturnCode.Error);
//                    }
//                }).build();
//        }
//
//        try {
//            return Response.status(200).entity(iBusinessClassifySetService.delBusinessClassify(delList) ? new Msg(){{
//                this.setCode(ReturnCode.Success);
//                this.setMsg("业务分类为默认分类或已在使用，不能删除");
//            }}: new Msg(){{
//                this.setCode(ReturnCode.Success);
//                this.setMsg("删除业务分类成功");
//            }}).build();
//        } catch (ErrorException e) {
//            return Response.status(200).entity(new Error() {{
//                this.setMsg(e.getMsg());
//                this.setCode(e.getCode());
//            }}).build();
//        }
//    }

    @Override
    public Response updateBusiness(BusinessClassifySet businessClassifySet) {
        if (businessClassifySet == null ||
                !StringUtil.notEmpty(businessClassifySet.getId()) ||
                businessClassifySet.getRCYW() == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务分类或分类id或业务不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        for (BusinessSet businessSet :  businessClassifySet.getRCYW()) {
            if (businessSet == null || !StringUtil.notEmpty(businessSet.getYWMC()))
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("业务或业务名称不能为空");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
        }

        try {
            return Response.status(200).entity(iBusinessClassifySetService.updateBusiness(businessClassifySet) ? new Msg(){{
                this.setCode(ReturnCode.Success);
                this.setMsg("业务为默认或已在使用，不能修改或删除");
            }}: new Success(){{
                this.setState("更新业务成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

//    @Override
//    public Response delBusiness(ArrayList<String> delList) {
//        return null;
//    }
}

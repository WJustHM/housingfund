package com.handge.housingfund.server.controllers.account.api.impl;

import com.handge.housingfund.common.service.account.IBankInfoService;
import com.handge.housingfund.common.service.account.model.BankContactReq;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.account.api.IBankInfoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/9/6.
 */
@Component
public class BankInfoAPI implements IBankInfoAPI {

    @Autowired
    IBankInfoService iBankInfoService;

    @Autowired
    IUploadImagesService iUploadImagesService;

    @Override
    public Response getBankInfoList(String yhmc, String code, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(iBankInfoService.getBankInfoList(yhmc, code, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getBankContactList(String YHMC, int pageNo, int pageSize) {
//        if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
//                UploadFileBusinessType.个人缴存基数调整.getCode(), "[{\"name\":\"单位证书\",\"modle\":\"01\",\"business\":\"12\",\"required\":true,\"operation\":\"个人缴存基数调整\",\"data\":[]},{\"name\":\"法人或负责人身份证\",\"modle\":\"01\",\"business\":\"12\",\"required\":true,\"operation\":\"个人缴存基数调整\",\"data\":[\"123\"]},{\"name\":\"委托书\",\"modle\":\"01\",\"business\":\"12\",\"required\":true,\"operation\":\"个人缴存基数调整\",\"data\":[\"123\"]},{\"name\":\"受托人身份证\",\"modle\":\"01\",\"business\":\"12\",\"required\":true,\"operation\":\"个人缴存基数调整\",\"data\":[\"123\"]},{\"name\":\"缴存基数变更清册\",\"modle\":\"01\",\"business\":\"12\",\"required\":true,\"operation\":\"个人缴存基数调整\",\"data\":[\"123\"]}]")) {
//            return Response.status(200).entity(new Error() {{
//                this.setMsg("资料未上传完整");
//                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
//            }}).build();
//        }
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;
        try {
            return Response.status(200).entity(iBankInfoService.getBankContactList(YHMC, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response addBankContact(BankContactReq bankContactReq) {
        if (!StringUtil.notEmpty(bankContactReq.getYHMC())) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("银行名称不能为空");
                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
            }}).build();
        }
        if (!StringUtil.notEmpty(bankContactReq.getKHBH())) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("客户编号不能为空");
                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
            }}).build();
        }
        if (!StringUtil.notEmpty(bankContactReq.getXDCKJE())) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("协定存款金额不能为空");
                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
            }}).build();
        }

        try {
            return Response.status(200).entity(iBankInfoService.addBankContact(bankContactReq)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getBankContactDetail(String id) {
        if (!StringUtil.notEmpty(id)) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("id不能为空");
                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
            }}).build();
        }
        try {
            return Response.status(200).entity(iBankInfoService.getBankContactDetail(id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response putBankContact(String id, BankContactReq bankContactReq) {
        if (!StringUtil.notEmpty(id)) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("id不能为空");
                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
            }}).build();
        }
        if (!StringUtil.notEmpty(bankContactReq.getYHMC())) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("银行名称不能为空");
                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
            }}).build();
        }
        if (!StringUtil.notEmpty(bankContactReq.getKHBH())) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("客户编号不能为空");
                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
            }}).build();
        }
        if (!StringUtil.notEmpty(bankContactReq.getXDCKJE())) {
            return Response.status(200).entity(new Error() {{
                this.setMsg("协定存款金额不能为空");
                this.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
            }}).build();
        }
        try {
            return Response.status(200).entity(iBankInfoService.putBankContact(id, bankContactReq)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delBankContact(DelList delList) {
        try {
            return Response.status(200).entity(iBankInfoService.delBankContact(delList)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

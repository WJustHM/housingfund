package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.ISignContractService;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.server.controllers.loan.api.ISignContractListApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class SignContractListApi implements ISignContractListApi<Response> {

    @Autowired
    private ISignContractService signContractService;

    /**
     * 签订合同列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public Response getSignContractList(TokenContext tokenContext,String YWZT,String JE, final String JKRXM, final String JKRZJHM, final String DKYT,String YHMC,String YWWD, String pageSize, String page,String KSSJ,String JSSJ) {


        System.out.println("签订合同列表");


        try {

            return Response.status(200).entity(this.signContractService.getSignContractList(tokenContext,YWZT,JE,JKRXM, JKRZJHM, DKYT,YHMC,YWWD, pageSize, page,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }


    /**
     * 签订合同列表
     *
     * @param JKRXM   借款人姓名
     * @param JKRZJHM 借款人证件号码
     * @param DKYT    贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）
     **/
    public Response getSignContractList(TokenContext tokenContext, final String JKRXM, final String JKRZJHM, final String DKYT,String YHMC,String YWWD, String pageSize, String marker,String action,String KSSJ,String JSSJ) {


        System.out.println("签订合同列表");


        try {

            return Response.status(200).entity(this.signContractService.getSignContractList(tokenContext,JKRXM, JKRZJHM, DKYT,YHMC,YWWD, pageSize, marker,action,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }

    /**
     * 自动获取合同信息
     *
     * @param YWLSH 业务流水号
     **/
    public Response getContractAuto(TokenContext tokenContext,String YWLSH){

        System.out.println("自动获取合同信息");


        try {

            return Response.status(200).entity(this.signContractService.getContractAuto(tokenContext,YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }


    /**
     * 打印 合同pdf
     *
     * @param YWLSH 业务流水号
     **/
    public Response printContractPdf(TokenContext tokenContext,String YWLSH){

        System.out.println("打印 合同pdf");


        try {

            return Response.status(200).entity(this.signContractService.printContractPdf(tokenContext,YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }

}
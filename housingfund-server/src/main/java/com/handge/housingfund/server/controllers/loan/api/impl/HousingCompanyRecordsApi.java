package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.IHousingCompany;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPost;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IHousingCompanyRecordsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@Component
public class HousingCompanyRecordsApi implements IHousingCompanyRecordsApi<Response> {

    @Autowired
    private IHousingCompany housingCompany;

    /**
     * 修改房开公司信息
     *
     * @param YWLSH 业务流水号
     **/
    public Response putReHousingCompanyInfomation(final TokenContext tokenContext, final String YWLSH, final String CZLX, final HousingCompanyPost body) {

        System.out.println("修改房开公司信息");
        if (!StringUtil.notEmpty(YWLSH) || body == null||!StringUtil.notEmpty(CZLX)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if(!Arrays.asList("0","1").contains(CZLX)){
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH,"操作类型");
        }
        try {

            return Response.status(200).entity(housingCompany.reHousingCompanyInfo(tokenContext,YWLSH, CZLX, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }

    /**
     * 是否启用房开公司
     *
     * @param FKGSZH 房开公司账号
     * @param SFQY   是否启用 0 禁用 1 启用
     **/
    public Response reHousingCompanySFQY(TokenContext tokenContext,final String FKGSZH, final String SFQY) {

        System.out.println("修改房开公司信息");
        if (!StringUtil.notEmpty(FKGSZH) || !StringUtil.notEmpty(SFQY)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(SFQY)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(housingCompany.reHousingCompanySFQY(tokenContext, FKGSZH, SFQY)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }

    /**
     * 房开公司详情
     *
     * @param YWLSH 业务流水号
     * @param type  详情类型 1 审核通过前  2 审核通过后
     **/
    public Response getApplyHousingCompanyDetails(final String YWLSH, final String type) {

        System.out.println("房开公司详情");

        if (!StringUtil.notEmpty(YWLSH) || !StringUtil.notEmpty(type)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("1", "2").contains(type)) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(housingCompany.showHousingCompanyInfo(YWLSH, type)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }

    }

    // public Response getHCReviewListProcessing(String FKGS, String ddsjStart, String ddsjEnd, String pageNo, String pageSize) {
    //     System.out.println("查询待审核房开信息");
    //
    //     try {
    //
    //         // return Response.status(200).entity(housingCompany.getHCReviewListProcessing(FKGS, ddsjStart, ddsjEnd, pageNo, pageSize)).build();
    //
    //     } catch (ErrorException e) {
    //
    //         return Response.status(200).entity(new Error() {{
    //             this.setMsg(e.getMsg());
    //             this.setCode(e.getCode());
    //         }}).build();
    //
    //     }
    // }

    // public Response getHCReviewListProcessed(String FKGS, String slsjStart, String slsjEnd, String pageNo, String pageSize) {
    //     System.out.println("查询审核后房开信息");
    //
    //     try {
    //
    //         // return Response.status(200).entity(housingCompany.getHCReviewListProcessed(FKGS, slsjStart, slsjEnd, pageNo, pageSize)).build();
    //
    //     } catch (ErrorException e) {
    //
    //         return Response.status(200).entity(new Error() {{
    //             this.setMsg(e.getMsg());
    //             this.setCode(e.getCode());
    //         }}).build();
    //
    //     }
    // }

    public Response getCompanyHistory(String ZZJGDM,String pageNo,String pageSize){
        System.out.println("房开历史");

        if (!StringUtil.notEmpty(ZZJGDM)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        try {

            return Response.status(200).entity(housingCompany.getCompanyHistory(ZZJGDM,pageNo,pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }

    /**
     *  根据房开公司账号查找房开信息
     * @param fkzh 房开公司账号
     * @return HousingIdGet
     */
    public Response getCompanyInfoByFKZH(String fkzh){
        System.out.println("房开历史");

        if (!StringUtil.notEmpty(fkzh)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        try {

            return Response.status(200).entity(housingCompany.getCompanyInfoByFKZH(fkzh)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }

}
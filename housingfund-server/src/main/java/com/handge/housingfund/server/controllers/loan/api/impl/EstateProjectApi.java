package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.IEstateProject;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@Component
public class EstateProjectApi implements IEstateProjectApi<Response> {

    @Autowired
    private IEstateProject estateProject;

    /**
     * 新增变更楼盘项目
     *
     * @param action 状态（0：保存 1：提交）
     * @param LPBH   楼盘编号
     **/
    public Response postModifyEstate(final String action, final String LPBH, final EstateProjectInfo Estate) {

        System.out.println("新增变更楼盘项目");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 批量审核楼盘信息
     *
     * @param action 0审核通过，1审核不通过
     **/
    public Response putEstateProjectBatchReview(final String action, final EstateProjectBatchReviewInfo body) {

        System.out.println("批量审核楼盘信息");
        if (!StringUtil.notEmpty(action) || body == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(action)) {
            return ResUtils.buildParametersErrorResponse();
        }
        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 楼盘项目列表
     *
     * @param LPMC 楼盘名称
     * @param FKGS 房开公司
     **/
    public Response getEstateList(TokenContext tokenContext,final String LPMC, final String FKGS, final String SFQY,final String YWWD, final String pageNo, final String pageSize) {

        System.out.println("楼盘项目列表");

        Boolean sfqy = null;
        if(StringUtil.notEmpty(SFQY)){
            if(!Arrays.asList("0","1").contains(SFQY))
                return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH,"是否启用");
            if(SFQY.equals("0")){
                sfqy = false;
            }else if(SFQY.equals("1")){
                sfqy= true;
            }
        }
        try {

            return Response.status(200).entity(estateProject.getEstateProjectInfo(tokenContext,LPMC, FKGS,sfqy ,YWWD,pageNo, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }

    /**
     * 受理楼盘列表
     *
     * @param LPMC      楼盘名称
     * @param ZHUANGTAI 状态
     **/
    public Response getEstateProjectInfoAccept(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize) {

        try {

            return Response.status(200).entity(estateProject.getEstateProjectInfoAccept(tokenContext,LPMC, ZHUANGTAI, KSSJ, JSSJ,pageNo, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }

    /**
     * 受理楼盘列表
     *
     * @param LPMC      楼盘名称
     * @param ZHUANGTAI 状态
     **/
    public Response getEstateProjectInfoAcceptNew(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ, String marker, String pageSize,String action) {

        try {

            return Response.status(200).entity(estateProject.getEstateProjectInfoAcceptNew(tokenContext,LPMC, ZHUANGTAI, KSSJ, JSSJ,marker, pageSize,action)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }


    @Override
    public Response submitEstateProject(TokenContext tokenContext,AlterIndiAcctSubmitPost YWLSHS) {
        if(YWLSHS.getYWLSHJH().contains("")||YWLSHS.getYWLSHJH().contains(null)||YWLSHS.getYWLSHJH().size()==0){
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {
            return Response.status(200).entity(estateProject.submitEstateProject(tokenContext,YWLSHS.getYWLSHJH())).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 查询审核后房开信息
     *
     * @param LPBH    楼盘名称
     * @param SFQY    是否启用
     **/
    public Response reEstateProjectSFQY(TokenContext tokenContext,String LPBH,String SFQY){
        System.out.println("是否启用楼盘");
        if(!StringUtil.notEmpty(LPBH)||!StringUtil.notEmpty(SFQY)){
            return ResUtils.buildParametersMissErrorResponse();
        }
        if(!Arrays.asList("0","1").contains(SFQY)){
            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(estateProject.reEstateProjectSFQY(tokenContext,LPBH, SFQY)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }


    /**
     * 新增楼盘项目
     *
     * @param action 状态（0：保存 1：提交）
     **/
    public Response postEstate(TokenContext tokenContext, final String action, final EstateProjectInfo estate) {

        System.out.println("新增楼盘项目");
        if (!StringUtil.notEmpty(action) || estate == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(action)) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(estateProject.addEstateProject(tokenContext,action, estate)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }


    /**
     * 单独修改状态（提交与撤回）
     *
     * @param YWLSH  业务流水号
     * @param status 状态（0：提交 1：撤回）
     **/
    public Response putEstateStatusSubmits(final String YWLSH, final String status) {

        System.out.println("单独修改状态（提交与撤回）");
        if (!StringUtil.notEmpty(YWLSH) || !StringUtil.notEmpty(status)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(status)) {
            return ResUtils.buildParametersErrorResponse();
        }

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 批量操作楼盘信息
     *
     * @param TYPE 类型(0申请，1变更)
     **/
    public Response putBatchEstateStatusInfos(final String TYPE, final BatchEstateProjectInfo body) {

        System.out.println("批量操作楼盘信息");
        if (!StringUtil.notEmpty(TYPE) || body == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(TYPE)) {
            return ResUtils.buildParametersErrorResponse();
        }
        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }

    /**
     * 打印回执
     *
     * @param YWLSH 业务流水号
     **/
    public Response getEstateProjectsReceipts(String YWLSH) {
        System.out.println("打印回执");
        if (!StringUtil.notEmpty(YWLSH)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        return Response.status(200).entity(new EstateProjectsReceipts() {{


        }}).build();
    }


}
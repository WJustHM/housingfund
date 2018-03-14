package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.IHousingCompany;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IHousingCompanyApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@Component
public class HousingCompanyApi implements IHousingCompanyApi<Response> {
    @Autowired
    private IHousingCompany housingCompany;

    /**
     * 查询过程中房开信息
     */
    public Response getHouingCompanyAccept(TokenContext tokenContext,String FKGS, String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize) {
        System.out.println("查询过程中房开信息");

        try {
            return Response.status(200).entity(housingCompany.getHousingCompanyInfoAccept(tokenContext,FKGS, ZHUANGTAI, KSSJ, JSSJ, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 查询过程中房开信息
     */
    public Response getHouingCompanyAcceptNew(TokenContext tokenContext,String FKGS, String ZHUANGTAI,String KSSJ,String JSSJ, String marker, String pageSize,String action) {
        System.out.println("查询过程中房开信息");

        try {
            return Response.status(200).entity(housingCompany.getHousingCompanyInfoAcceptNew(tokenContext,FKGS, ZHUANGTAI, KSSJ, JSSJ, marker, pageSize,action)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }



    /**
     * 新增变更房开公司信息
     **/
    public Response postModifyHousingCompanyInfomation(TokenContext tokenContext,final HousingCompanyPost rehousing) {

        System.out.println("新增变更房开公司信息");

        if (rehousing == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }

    /**
     *
     * @param YWLSHS
     * @return
     */
    public Response submitHousingCompany(TokenContext tokenContext,AlterIndiAcctSubmitPost YWLSHS){
        System.out.println("批量提交房开");
        if(YWLSHS.getYWLSHJH().contains("")||YWLSHS.getYWLSHJH().contains(null)||YWLSHS.getYWLSHJH().size()==0){
            return ResUtils.buildParametersMissErrorResponse();
        }
        try {
            return Response.status(200).entity(housingCompany.submitHousingCompany(tokenContext,YWLSHS.getYWLSHJH())).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * 批量操作房开信息
     *
     * @param TYPE 类型(0申请，1变更)
     **/
    public Response putBatchHousingCompanyInfomation(final String TYPE, final BatchHousingCompanyInfo body) {

        System.out.println("批量操作房开信息");
        if (!StringUtil.notEmpty(TYPE) || body == null || !StringUtil.notEmpty(body.getAction())) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(TYPE) || !Arrays.asList("0", "1", "2").contains(body.getAction())) {
            return ResUtils.buildParametersErrorResponse();
        }
        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 删除房开公司信息，修改数据库字段状态
     *
     * @param TYPE 类型(0申请，1变更)
     **/
    public Response deleteHousingCompanyInformation(final String TYPE, final DelList body) {
        if (!StringUtil.notEmpty(TYPE) || body == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(TYPE)) {
            return ResUtils.buildParametersErrorResponse();
        }
        System.out.println("删除房开公司信息，修改数据库字段状态");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 审核房开信息
     *
     * @param YWLSH 业务流水号
     **/
    public Response putHousingCompanyReview(final String YWLSH, final HousingCompanyReviewInfo body) {

        System.out.println("审核房开信息");
        if (!StringUtil.notEmpty(YWLSH) || body == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 房开公司列表
     *
     * @param FKGS 房开公司
     * @param FKZH 房开账号
     * @param SFQY 是否启用 0 禁用 1 启用
     **/
    public Response getHousingCompanyList(TokenContext tokenContext,final String FKGS, final String FKZH, final String SFQY,final String kssj,final String jssj, final String pageNo, final String pageSize) {

        System.out.println("房开公司列表");
        if (StringUtil.notEmpty(pageNo) || StringUtil.notEmpty(pageSize)) {
            try {
                if (StringUtil.notEmpty(pageNo)) Integer.parseInt(pageNo);
                if (StringUtil.notEmpty(pageSize)) Integer.parseInt(pageSize);
            } catch (Exception e) {
                return ResUtils.buildParametersErrorResponse();
            }
        }
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
            return Response.status(200).entity(housingCompany.getHousingCompanyInfo(tokenContext,FKGS, FKZH, sfqy,kssj,jssj, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 房开公司列表
     *
     * @param FKGS 房开公司
     * @param FKZH 房开账号
     * @param SFQY 是否启用 0 禁用 1 启用
     **/
    public Response getHousingCompanyListNew(TokenContext tokenContext,final String FKGS, final String FKZH, final String SFQY,final String kssj,final String jssj, final String marker, final String pageSize,String action) {

        System.out.println("房开公司列表");

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
            return Response.status(200).entity(housingCompany.getHousingCompanyInfoNew(tokenContext,FKGS, FKZH, sfqy,kssj,jssj, marker, pageSize,action)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 新增房开公司信息
     **/
    public Response postHousingCompanyInfomation(TokenContext tokenContext, final String CZLX, final HousingCompanyPost housing) {
        System.out.println("新增房开公司信息");
        if (housing == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        try {
            return Response.status(200).entity(housingCompany.addHousingCompany(tokenContext,CZLX, housing)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    /**
     * 打印申请回执
     *
     * @param YWLSH 业务流水号
     **/
    public Response getApplyHousingCompanyReceipt(final String YWLSH) {
        System.out.println("打印申请回执");
        if (!StringUtil.notEmpty(YWLSH)) {
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {
            return Response.status(200).entity(housingCompany.receiptHousingCompany(YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 根据房开公司与房开账号查询房开信息
     *
     * @param FKGS 房开公司
     * @param FKZH 房开账号
     **/
    public Response getHousingCompany(String FKGS, String FKZH, String YWWD,String pageNo, String pageSize) {
        System.out.println("根据房开公司与房开账号查询房开信息");

        try {
            return Response.status(200).entity(housingCompany.getHousingCompany(FKGS, FKZH, YWWD, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    /**
     * 提交与撤回
     *
     * @param YWLSH     业务流水号
     * @param operation 状态（0：提交 1：撤回）
     **/
    public Response putHousingCompanyStatus(final String YWLSH, final String operation) {
        System.out.println("提交与撤回");
        if (!StringUtil.notEmpty(YWLSH) || !StringUtil.notEmpty(operation)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(operation)) {
            return ResUtils.buildParametersErrorResponse();
        }
        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 批量审核房开信息
     *
     * @param action 0审核通过，1审核不通过
     **/
    public Response putHousingCompanyBatchReview(final String action, final HousingCompanyBatchReviewInfo body) {
        System.out.println("批量审核房开信息");
        if (StringUtil.notEmpty(action) || body == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("1", "2").contains(action)) {
            return ResUtils.buildParametersErrorResponse();
        }

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 停用/启用房开公司
     *
     * @param id    id
     * @param state (1：启用，2：停用)
     **/
    public Response putHousingCompanystate(final String id, final String state) {
        System.out.println("停用/启用房开公司");
        if (!StringUtil.notEmpty(id) || !StringUtil.notEmpty(state)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("1", "2").contains(state)) {
            return ResUtils.buildParametersErrorResponse();
        }

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


}
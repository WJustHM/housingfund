package com.handge.housingfund.server.controllers.loan.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.IHousingCompanyAlter;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPost;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPut;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IHousingCompanyAlterApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * Created by 向超 on 2017/8/24.
 */
@Component
public class HousingCompanyAlterApi implements IHousingCompanyAlterApi<Response>{

    @Autowired
    private IHousingCompanyAlter housingCompanyAlter;
    @Override
    public Response addHousingCompanyAlter(TokenContext tokenContext,String CZLX, String FKZH, HousingCompanyPost housingCompanyInfo) {
        if(!StringUtil.notEmpty(CZLX)){
            throw new ErrorException(ReturnEnumeration.Data_MISS,"接口参数操作类型");
        }
        if(!StringUtil.notEmpty(FKZH)){
            throw new ErrorException(ReturnEnumeration.Data_MISS,"接口参数房开账号");
        }
        if(housingCompanyInfo==null){
            throw new ErrorException(ReturnEnumeration.Data_MISS,"房开信息");
        }
        if(!Arrays.asList("0","1").contains(CZLX)){
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(housingCompanyAlter.addHousingCompanyAlter(tokenContext,CZLX, FKZH, housingCompanyInfo)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response reHousingCompanyAlterInfo(TokenContext tokenContext,String YWLSH, String CZLX, HousingCompanyPut housingCompanyInfo) {
        if(!StringUtil.notEmpty(CZLX)||!StringUtil.notEmpty(YWLSH)||housingCompanyInfo==null){
            return ResUtils.buildParametersMissErrorResponse();
        }
        if(!Arrays.asList("0","1").contains(CZLX)){
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(housingCompanyAlter.reHousingCompanyAlterInfo(tokenContext,YWLSH, CZLX, housingCompanyInfo)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response showHousingCompanyAlterInfo(String YWLSH) {
        if(!StringUtil.notEmpty(YWLSH)){
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {
            return Response.status(200).entity(housingCompanyAlter.showHousingCompanyAlterInfo(YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getHousingCompanyAlterInfoAccept(TokenContext tokenContext,String FKZH, String FKGS,String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize) {

        try {
            return Response.status(200).entity(housingCompanyAlter.getHousingCompanyAlterInfoAccept(tokenContext,FKZH, FKGS,ZHUANGTAI, KSSJ, JSSJ, pageNo,pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getHousingCompanyAlterInfoAcceptNew(TokenContext tokenContext,String FKZH, String FKGS,String ZHUANGTAI,String KSSJ,String JSSJ, String marker, String pageSize,String action) {

        try {
            return Response.status(200).entity(housingCompanyAlter.getHousingCompanyAlterInfoAcceptNew(tokenContext,FKZH, FKGS,ZHUANGTAI, KSSJ, JSSJ, marker,pageSize,action)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response receiptHousingCompanyAlter(String YWLSH) {
        if(!StringUtil.notEmpty(YWLSH)){
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {
            return Response.status(200).entity(housingCompanyAlter.receiptHousingCompanyAlter(YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }



    @Override
    public Response submitHousingCompanyAlter(TokenContext tokenContext,AlterIndiAcctSubmitPost YWLSHS) {
        if(YWLSHS.getYWLSHJH().contains("")||YWLSHS.getYWLSHJH().contains(null)||YWLSHS.getYWLSHJH().size()==0){
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {
            return Response.status(200).entity(housingCompanyAlter.submitHousingCompanyAlter(tokenContext,YWLSHS.getYWLSHJH())).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

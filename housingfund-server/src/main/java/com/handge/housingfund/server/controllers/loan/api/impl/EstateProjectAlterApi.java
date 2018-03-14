package com.handge.housingfund.server.controllers.loan.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.IEstateProjectAlter;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.loan.model.EstateProjectInfo;
import com.handge.housingfund.common.service.loan.model.EstatePut;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectAlterApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * Created by 向超 on 2017/8/24.
 */
@Component
public class EstateProjectAlterApi implements IEstateProjectAlterApi<Response>{

    @Autowired
    private IEstateProjectAlter estateProjectAlter;
    @Override
    public Response addEstateProjectAlter(TokenContext tokenContext,String CZLX, String LPBH, EstateProjectInfo estateProjectInfo) {
        if(!StringUtil.notEmpty(CZLX)||!StringUtil.notEmpty(LPBH)||estateProjectInfo==null){
            return ResUtils.buildParametersMissErrorResponse();
        }
        if(!Arrays.asList("0","1").contains(CZLX)){
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(estateProjectAlter.addEstateProjectAlter(tokenContext,CZLX, LPBH, estateProjectInfo)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response reEstateProjectAlterInfo(TokenContext tokenContext, String CZLX, String YWLSH, EstatePut estatePut) {
        if(!StringUtil.notEmpty(CZLX)||!StringUtil.notEmpty(YWLSH)||estatePut==null){
            return ResUtils.buildParametersMissErrorResponse();
        }
        if(!Arrays.asList("0","1").contains(CZLX)){
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(estateProjectAlter.reEstateProjectAlterInfo(tokenContext,CZLX, YWLSH, estatePut)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response showEstateProjectAlterInfo(String YWLSH) {
        if(!StringUtil.notEmpty(YWLSH)){
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {
            return Response.status(200).entity(estateProjectAlter.showEstateProjectAlterInfo(YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getEstateProjectAlterInfoAccept(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize) {

        try {
            return Response.status(200).entity(estateProjectAlter.getEstateProjectAlterInfoAccept(tokenContext,LPMC, ZHUANGTAI,KSSJ,JSSJ, pageNo,pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getEstateProjectAlterInfoAcceptNew(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ, String marker, String pageSize,String action) {

        try {
            return Response.status(200).entity(estateProjectAlter.getEstateProjectAlterInfoAcceptNew(tokenContext,LPMC, ZHUANGTAI,KSSJ,JSSJ, marker,pageSize,action)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }



    @Override
    // TODO: 2017/8/25 检查该API是否有意义？（向超）
    public Response doEstateProjectAlter(String YWLSH) {

        if(!StringUtil.notEmpty(YWLSH)){
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {
            return Response.status(200).entity(null).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response submitEstateProjectAlter(TokenContext tokenContext,AlterIndiAcctSubmitPost YWLSHS) {
        if(YWLSHS.getYWLSHJH().contains("")||YWLSHS.getYWLSHJH().contains(null)||YWLSHS.getYWLSHJH().size()==0){
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {
            return Response.status(200).entity(estateProjectAlter.submitEstateProjectAlter(tokenContext, YWLSHS.getYWLSHJH())).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}

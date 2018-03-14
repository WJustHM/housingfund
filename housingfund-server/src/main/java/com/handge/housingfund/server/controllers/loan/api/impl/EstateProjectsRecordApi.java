package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.IEstateProject;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.loan.model.EstatePut;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IEstateProjectsRecordApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@Component
public class EstateProjectsRecordApi implements IEstateProjectsRecordApi<Response> {

    @Autowired
    private IEstateProject estateProject;

    /**
     * 修改楼盘项目信息
     *
     * @param YWLSH 业务流水号
     **/
    public Response putEstateId(TokenContext tokenContext, final String CZLX, final String YWLSH, final EstatePut estate) {

        System.out.println("修改楼盘项目信息");

        if (!StringUtil.notEmpty(YWLSH) || estate == null) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(CZLX)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(estateProject.reEstateProjectInfo(tokenContext,CZLX, YWLSH, estate)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }


    /**
     * 楼盘项目详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getEstateId(final String YWLSH) {

        System.out.println("楼盘项目详情");
        if (!StringUtil.notEmpty(YWLSH)) {
            return ResUtils.buildParametersMissErrorResponse();
        }

        try {

            return Response.status(200).entity(estateProject.showEstateProjectInfo(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }
    /**
     * 楼盘项目历史
     *
     * @param LPBH 楼盘编号
     **/
    @Override
    public Response getEstateProjectHistory(String LPBH,String pageNo,String pageSize) {
        System.out.println("楼盘项目历史");
        if (!StringUtil.notEmpty(LPBH)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        try {

            return Response.status(200).entity(estateProject.getEstateProjectHistory(LPBH,pageNo,pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }

    /**
     *  根据楼盘编号查找楼盘信息
     * @param lpbh 楼盘编号
     * @return EstateIdGet
     */
    public Response getEstateProjectInfoByLPBH(String lpbh) {
        System.out.println("根据楼盘编号查找楼盘信息");
        if (!StringUtil.notEmpty(lpbh)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        try {

            return Response.status(200).entity(estateProject.getEstateProjectInfoByLPBH(lpbh)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }
    /**
     *  根据楼盘名称查找售房人开户信息
     * @param lpmc 楼盘名称
     * @return EstateIdGet
     */
    public Response getSFRZHHM(String lpmc) {
        System.out.println("根据楼盘名称查找售房人开户信息");
        if (!StringUtil.notEmpty(lpmc)) {
            return ResUtils.buildParametersMissErrorResponse();
        }
        try {
            return Response.status(200).entity(estateProject.getSFRZHHM(lpmc)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();

        }
    }
}
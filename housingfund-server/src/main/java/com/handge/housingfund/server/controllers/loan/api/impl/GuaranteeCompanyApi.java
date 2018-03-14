package com.handge.housingfund.server.controllers.loan.api.impl;


import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.server.controllers.loan.api.IGuaranteeCompanyApi;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class GuaranteeCompanyApi implements IGuaranteeCompanyApi<Response> {


    /**
     * 担保公司列表（审核查询,申请受理都用）
     *
     * @param GSMC   公司名称
     * @param Status 担保状态
     **/
    public Response getGuarateeCompanyList(final String GSMC, final Integer Status) {

        System.out.println("担保公司列表（审核查询,申请受理都用）");

        return Response.status(200).entity(new GuaranteeCompanyGet() {{


        }}).build();
    }


    /**
     * 新增担保公司信息
     *
     * @param status 状态（0：保存 1：提交）
     **/
    public Response postGuarateeCompany(final String status, final GuaranteeCompanyPost guarantee) {

        System.out.println("新增担保公司信息");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 单独修改状态（提交与撤回）
     *
     * @param guarantee_id 担保公司id
     * @param status       状态（0：提交 1：撤回）
     **/
    public Response putGuarateeCompanySubmit(final String guarantee_id, final String status) {

        System.out.println("单独修改状态（提交与撤回）");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 删除担保公司信息（标识符删除）
     *
     * @param YWLSH 业务流水号
     **/
    public Response deleteGuarateeCompany(final String YWLSH) {

        System.out.println("删除担保公司信息（标识符删除）");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 修改担保公司信息
     *
     * @param YWLSH 业务流水号
     **/
    public Response putGuarateeCompany(final String YWLSH, final GuaranteePut guarantee) {

        System.out.println("修改担保公司信息");

        return Response.status(200).entity(new CommonResponses() {{


        }}).build();
    }


    /**
     * 担保公司详情(打印回执单)
     *
     * @param YWLSH 业务流水号
     **/
    public Response getGuarateeCompany(final String YWLSH) {

        System.out.println("担保公司详情(打印回执单)");

        return Response.status(200).entity(new GuaranteeIdGet() {{


        }}).build();
    }


}
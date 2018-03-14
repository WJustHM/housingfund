package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.loan.model.GuaranteeCompanyPost;
import com.handge.housingfund.common.service.loan.model.GuaranteePut;

public interface IGuaranteeCompanyApi<T> {


    /**
     * 担保公司列表（审核查询,申请受理都用）
     *
     * @param GSMC   公司名称
     * @param Status 担保状态
     **/
    public T getGuarateeCompanyList(final String GSMC, final Integer Status);


    /**
     * 新增担保公司信息
     *
     * @param status 状态（0：保存 1：提交）
     **/
    public T postGuarateeCompany(final String status, final GuaranteeCompanyPost guaranteecompanypost);


    /**
     * 单独修改状态（提交与撤回）
     *
     * @param guarantee_id 担保公司id
     * @param status       状态（0：提交 1：撤回）
     **/
    public T putGuarateeCompanySubmit(final String guarantee_id, final String status);


    /**
     * 删除担保公司信息（标识符删除）
     *
     * @param YWLSH 业务流水号
     **/
    public T deleteGuarateeCompany(final String YWLSH);


    /**
     * 修改担保公司信息
     *
     * @param YWLSH 业务流水号
     **/
    public T putGuarateeCompany(final String YWLSH, final GuaranteePut guaranteeput);


    /**
     * 担保公司详情(打印回执单)
     *
     * @param YWLSH 业务流水号
     **/
    public T getGuarateeCompany(final String YWLSH);


}
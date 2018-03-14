package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPost;

public interface IHousingCompanyRecordsApi<T> {


    /**
     * 修改房开公司信息
     *
     * @param YWLSH 业务流水号
     **/
    public T putReHousingCompanyInfomation(final TokenContext tokenContext, final String YWLSH, final String CZLX, final HousingCompanyPost housingcompanyinfo);

    /**
     * 是否启用房开公司
     *
     * @param FKGSZH 房开公司账号
     * @param SFQY   是否启用 0 禁用 1 启用
     **/
    public T reHousingCompanySFQY(TokenContext tokenContext,final String FKGSZH, final String SFQY);


    /**
     * 房开公司详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getApplyHousingCompanyDetails(final String YWLSH, final String type);

    //查询待审核房开信息
    // public T getHCReviewListProcessing(String FKGS, String ddsjStart, String ddsjEnd, String pageNo, String pageSize);

    //查询审核后房开信息
    // public T getHCReviewListProcessed(String FKGS, String slsjStart, String slsjEnd, String pageNo, String pageSize);

    //房开历史
    public T getCompanyHistory(String ZZJGDM,String pageNo,String pageSize);

    /**
     *  根据房开公司账号查找房开信息
     * @param fkzh 房开公司账号
     * @return HousingIdGet
     */
    public T getCompanyInfoByFKZH(String fkzh);
}
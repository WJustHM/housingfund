package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.EstatePut;

public interface IEstateProjectsRecordApi<T> {


    /**
     * 修改楼盘项目信息
     *
     * @param YWLSH 业务流水号
     **/
    public T putEstateId(final TokenContext tokenContext, final String CZLX, final String YWLSH, final EstatePut estateput);


    /**
     * 楼盘项目详情（打印回执单）
     *
     * @param YWLSH 业务流水号
     **/
    public T getEstateId(final String YWLSH);

    /**
     * 楼盘项目历史
     *
     * @param LPBH 楼盘编号
     **/
    public T getEstateProjectHistory(final String LPBH ,final String pageNo,final String pageSize);

    /**
     *  根据楼盘编号查找楼盘信息
     * @param lpbh 楼盘编号
     * @return EstateIdGet
     */
    public T getEstateProjectInfoByLPBH(String lpbh);

    /**
     *  根据楼盘名称查找售房人开户信息
     * @param lpmc 楼盘名称
     * @return EstateIdGet
     */
    public T getSFRZHHM(String lpmc);

}
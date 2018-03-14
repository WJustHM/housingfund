package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.loan.model.DelList;

public interface IEstateProjectsApi<T> {


    /**
     * 停用/启用房开公司
     *
     * @param id    id
     * @param state (1：启用，2：停用)
     **/
    public T putEstateProjectsstate(final String id, final String state);


    /**
     * 删除楼盘项目信息
     *
     * @param TYPE 1：申请受理 ,2:变更受理
     **/
    public T deleteEstateId(final String TYPE, final DelList dellist);


    /**
     * 受理记录
     *
     * @param TYPE      1：申请受理 ,2:变更受理
     * @param LPMC      楼盘名称
     * @param ZhuangTai 状态(00所有，01新建，02待审核，03审核不通过，04办结)
     **/
    public T getEstatRecords(final String TYPE, final String LPMC, final String ZhuangTai);


}
package com.handge.housingfund.server.controllers.loan.api;

public interface IEstateProjectAddSearchApi<T> {


    /**
     * 选择房开公司
     *
     * @param FKZH 房开账号
     * @param FKGS 房开公司
     **/
    public T getEstate(final String FKZH, final String FKGS);


}
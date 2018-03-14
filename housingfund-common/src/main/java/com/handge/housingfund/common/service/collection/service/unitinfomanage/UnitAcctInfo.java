package com.handge.housingfund.common.service.collection.service.unitinfomanage;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.individual.ListEmployeeRes;
import com.handge.housingfund.common.service.collection.model.unit.ListUnitAcctsResRes;
import com.handge.housingfund.common.service.collection.model.unit.UnitEmployeeExcelRes;

import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/7/17.
 */
public interface UnitAcctInfo {

    /**
     * 获取单位账号信息列表
     * @param DWMC
     * @param DWZH
     * @param DWLB
     * @param DWZHZT
     * @return
     */
    public PageRes<ListUnitAcctsResRes> getUnitAcctsInfo(TokenContext tokenContext, final String SFXH ,final String DWMC, final String DWZH, final String DWLB, final String DWZHZT,String YWWD,String startTime, String endTime, String page, String pagesize);


    public PageResNew<ListUnitAcctsResRes> getUnitAcctsInfo(TokenContext tokenContext, final String SFXH , final String DWMC, final String DWZH, final String DWLB, final String DWZHZT, String YWWD, String startTime, String endTime, String marker, String action, String pagesize);

    public PageRes<ListEmployeeRes> getEmployeeList(TokenContext tokenContext, String DWZH, String XingMing, String page, String pagesize);

    public UnitEmployeeExcelRes getEmployeeAllData(String DWZH);
}

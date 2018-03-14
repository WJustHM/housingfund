package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.IBusinessSetService;
import com.handge.housingfund.common.service.finance.model.BusinessClassifySet;
import com.handge.housingfund.common.service.finance.model.BusinessSet;
import com.handge.housingfund.common.service.others.IStateMachineService;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICFinanceDailyBusinessSetsDAO;
import com.handge.housingfund.database.dao.ICFundBusinessTypeDAO;
import com.handge.housingfund.database.entities.CFinanceDailyBusinessClassfiySets;
import com.handge.housingfund.database.entities.CFinanceDailyBusinessSets;
import com.handge.housingfund.database.entities.CFundBusinessType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 */
@Component
public class BusinessSetService implements IBusinessSetService {

    @Autowired
    private ICFinanceDailyBusinessSetsDAO icFinanceDailyBusinessSetsDAO;
    @Autowired
    private ICFundBusinessTypeDAO icFundBusinessTypeDAO;
    @Autowired
    private IStateMachineService iStateMachineService;

    @Override
    public PageRes<BusinessSet> getBusinessList(String name, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();

        if (StringUtil.notEmpty(name)) {
            filter.put("ywmc", name);
        }
        filter.put("sfzd", false);

        PageRes pageRes = new PageRes();
        List<CFinanceDailyBusinessSets> dailyBusinessSets = DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).searchFilter(filter).searchOption(SearchOption.FUZZY).pageOption(pageRes, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        PageRes<BusinessSet> businessSetPageRes = new PageRes<>();
        businessSetPageRes.setCurrentPage(pageRes.getCurrentPage());
        businessSetPageRes.setNextPageNo(pageRes.getNextPageNo());
        businessSetPageRes.setPageCount(pageRes.getPageCount());
        businessSetPageRes.setPageSize(pageRes.getPageSize());
        businessSetPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<BusinessSet> businessSetArrayList = new ArrayList<>();

        for (CFinanceDailyBusinessSets dailyBusinessSet : dailyBusinessSets) {
            BusinessSet businessSet = new BusinessSet();
            businessSet.setId(dailyBusinessSet.getId());
            businessSet.setYWMC(dailyBusinessSet.getYwmc());
            businessSet.setCJR(dailyBusinessSet.getCjr());
            businessSet.setCJSJ(DateUtil.date2Str(dailyBusinessSet.getCjsj(), "yyyy-MM-dd HH:mm"));
            businessSet.setSFMR(dailyBusinessSet.getSfmr());
            businessSet.setSFYSY(dailyBusinessSet.getSfsy());

            CFundBusinessType cFundBusinessType = DAOBuilder.instance(icFundBusinessTypeDAO).searchFilter(new HashMap<String, Object>(){{
                this.put("ywmcid", dailyBusinessSet.getId());
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            if (cFundBusinessType != null) {
                businessSet.setZJYWLXBM(cFundBusinessType.getZjywlxbm());
                businessSet.setZJYWLX(cFundBusinessType.getZjywlx());
            }

            CFinanceDailyBusinessClassfiySets dailyBusinessClassfiySet = dailyBusinessSet.getcFinanceDailyBusinessClassfiySets();
            BusinessClassifySet businessClassifySet = new BusinessClassifySet();
            businessClassifySet.setId(dailyBusinessClassfiySet.getId());
            businessClassifySet.setYWMC(dailyBusinessClassfiySet.getYwmc());
            businessClassifySet.setSFRC(dailyBusinessClassfiySet.getSfrc());
            businessClassifySet.setCJSJ(DateUtil.date2Str(dailyBusinessClassfiySet.getCjsj(), "yyyy-MM-dd HH:mm"));
            businessClassifySet.setCJR(dailyBusinessClassfiySet.getCjr());
            businessSet.setSSFL(businessClassifySet);

            businessSetArrayList.add(businessSet);
        }

        businessSetPageRes.setResults(businessSetArrayList);
        return businessSetPageRes;
    }

    @Override
    public boolean delBusiness(String id) {
        CFinanceDailyBusinessSets dailyBusinessSets = DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (dailyBusinessSets == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该业务(" + id + ")的相关信息");
        }
        if (dailyBusinessSets.getSfmr() || dailyBusinessSets.getSfsy()) return true;

        dailyBusinessSets.removeClassify();

        DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).entity(dailyBusinessSets).deleteForever(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "删除业务分类失败，请联系管理员");
            }
        });

        //删除业务流程
        iStateMachineService.deleteConfig(BusinessType.Finance, "日常" + dailyBusinessSets.getYwmc());

        return false;
    }

    @Override
    public boolean delBusinesses(ArrayList<String> delList) {
        for (String id : delList) {
            if (delBusiness(id)) return true;
        }

        return false;
    }
}

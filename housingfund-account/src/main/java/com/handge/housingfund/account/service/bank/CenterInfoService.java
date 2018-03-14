package com.handge.housingfund.account.service.bank;

import com.handge.housingfund.common.service.account.ICenterInfoService;
import com.handge.housingfund.common.service.account.model.CenterInfoModel;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICBankCenterInfoDAO;
import com.handge.housingfund.database.entities.CBankCenterInfo;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gxy on 17-12-8.
 */
@Component
public class CenterInfoService implements ICenterInfoService {

    @Autowired
    private ICBankCenterInfoDAO icBankCenterInfoDAO;

    @Override
    public PageRes<CenterInfoModel> getCenterInfoList(String zxmc, String zxdm, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(zxmc)) filter.put("unit_name", zxmc);
        if (StringUtil.notEmpty(zxdm)) filter.put("unit_no", zxdm);

        PageRes pageRes = new PageRes();

        List<CBankCenterInfo> cBankCenterInfoList = DAOBuilder.instance(icBankCenterInfoDAO).searchFilter(filter).searchOption(SearchOption.FUZZY)
                .pageOption(pageRes, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        PageRes<CenterInfoModel> centerInfoModelPageRes = new PageRes<>();
        centerInfoModelPageRes.setCurrentPage(pageRes.getCurrentPage());
        centerInfoModelPageRes.setNextPageNo(pageRes.getNextPageNo());
        centerInfoModelPageRes.setPageCount(pageRes.getPageCount());
        centerInfoModelPageRes.setPageSize(pageRes.getPageSize());
        centerInfoModelPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<CenterInfoModel> cBankCenterInfos = new ArrayList<>();

        for (CBankCenterInfo cBankCenterInfo : cBankCenterInfoList) {
            CenterInfoModel centerInfoModel = new CenterInfoModel();
            centerInfoModel.setUnit_name(cBankCenterInfo.getUnit_name());
            centerInfoModel.setUnit_no(cBankCenterInfo.getUnit_no());
            centerInfoModel.setParent_unit_no(cBankCenterInfo.getParent_unit_no());
            centerInfoModel.setId(cBankCenterInfo.getId());
            centerInfoModel.setNode(cBankCenterInfo.getNode());

            cBankCenterInfos.add(centerInfoModel);
        }

        centerInfoModelPageRes.setResults(cBankCenterInfos);
        return centerInfoModelPageRes;
    }
}

package com.handge.housingfund.common.service.collection.service.businessdetail;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.deposit.GetUnitBusnissDetailListResRes;

/**
 * Created by Liujuhao on 2017/7/5.
 */
public interface UnitBusiness{

     PageRes<GetUnitBusnissDetailListResRes> showUnitBusinessDetails(TokenContext tokenContext, String DWMC, String DWZH, String YWMXLX, String pageNumber, String pageSize, String start, String end);

     PageResNew<GetUnitBusnissDetailListResRes> showUnitBusinessDetailsnew(TokenContext tokenContext, String DWMC, String DWZH, String YWMXLX, String marker, String pageSize, String start, String end, String action);


}

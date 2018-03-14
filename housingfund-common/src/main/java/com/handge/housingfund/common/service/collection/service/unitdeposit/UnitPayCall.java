package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.deposit.AddUnitPayCallRes;
import com.handge.housingfund.common.service.collection.model.deposit.ListUnitPayCallHistoryRes;
import com.handge.housingfund.common.service.collection.model.deposit.ListUnitPayCallResRes;
import com.handge.housingfund.common.service.collection.model.deposit.UnitPayCallPost;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

/**
 * Created by 向超 on 2017/7/25.
 */
public interface UnitPayCall {

    public PageRes<ListUnitPayCallResRes> getUnitPayCallInfo(TokenContext tokenContext,String DWMC, String DWZH, String pageNo, String pageSize, String kssj, String jssj);//催缴记录

    public ListUnitPayCallHistoryRes getUnitPayCallHistoryInfo(String YWLSH);//获取催缴历史记录

    public AddUnitPayCallRes addUnitPayCall(TokenContext tokenContext, UnitPayCallPost unitPayCallPosts);//提交催缴

    public CommonResponses headUnitPayCall(TokenContext tokenContext, String YWLSH);//获取催缴详情（回执单）

    void doCreateUnitPayCall();

    PageResNew<ListUnitPayCallResRes> getUnitPayCallInfo(TokenContext tokenContext, String dwmc, String dwzh, String kssj, String jssj, String marker, String pageSize, String action);

}

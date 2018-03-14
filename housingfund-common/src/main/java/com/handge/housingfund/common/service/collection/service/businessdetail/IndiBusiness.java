package com.handge.housingfund.common.service.collection.service.businessdetail;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.deposit.GetIndiBusnissDetailListResRes;

/**
 * Created by Liujuhao on 2017/7/5.
 */
public interface IndiBusiness {

    PageRes<GetIndiBusnissDetailListResRes> showIndiBusinessDetails(TokenContext tokenContext, final String XingMing, final String GRZH, final String YWMXLX, final String pageNumber, final String pageSize,
                                                                    String start, String end,String ZJHM);

    PageResNew<GetIndiBusnissDetailListResRes> showIndiBusinessDetailsnew(TokenContext tokenContext, final String XingMing, final String GRZH, final String YWMXLX, final String marker, final String pageSize,
                                                                          String start, String end, String action);

}

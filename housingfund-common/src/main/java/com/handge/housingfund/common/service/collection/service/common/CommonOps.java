package com.handge.housingfund.common.service.collection.service.common;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.collection.model.BusCommonRetrun;
import com.handge.housingfund.common.service.collection.model.individual.DeleteOperationRes;
import com.handge.housingfund.common.service.collection.model.individual.RevokeOperationRes;
import com.handge.housingfund.common.service.loan.model.GetCommonHistory;
import com.handge.housingfund.common.service.review.model.DetailsReviewInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liujuhao on 2017/7/4.
 */
public interface CommonOps {

    public DeleteOperationRes deleteOperation(TokenContext tokenContext, List<String> YWLSH, String YWMK);

    public RevokeOperationRes revokeOperation(TokenContext tokenContext, String YWLSH, String YWMK);

    public PageRes<GetCommonHistory> recordHistory(String ZhangHao, String YWMK, String pageSize, String pageNo);

    public ArrayList<DetailsReviewInfo> getReviewInfos(String YWLSH);

    BusCommonRetrun accountRecord(String ywlsh, String ywlx, String code);
}
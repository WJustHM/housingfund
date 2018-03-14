package com.handge.housingfund.common.service.collection.service.indiacctmanage;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.List;

/**
 * Created by Liujuhao on 2017/7/1.
 */
public interface IndiAcctAlter {

    public PageRes<ListOperationAcctsResRes> getAlterInfo(TokenContext tokenContext,final String YWWD,final String DWMC, final String ZHZT, final String XingMing, final String ZJHM, final String GRZH, final String CZMC, String page, String pagesize,String KSSJ,String JSSJ);

    public PageResNew<ListOperationAcctsResRes> getAlterInfo(TokenContext tokenContext,final String YWWD, final String DWMC, final String ZHZT, final String XingMing, final String ZJHM, final String GRZH, final String CZMC, String marker, String action, String pagesize, String KSSJ, String JSSJ);

    public AddIndiAcctAlterRes addAcctAlter(TokenContext tokenContext, IndiAcctAlterPost addIndiAcctAlter);

    public ReIndiAcctAlterRes reAcctAlter(TokenContext tokenContext,String YWLSH, IndiAcctAlterPut reIndiAcctAlter);

    public GetIndiAcctAlterRes showAcctAlter(TokenContext tokenContext,String YWLSH);

    public CommonResponses headAcctAlter(TokenContext tokenContext, String YWLSH);

    // TODO: 2017/7/24 审核通过后变更操作（练隆生）
    public void doAcctAlter(TokenContext tokenContext,String YWLSH);

    public SubmitAlterIndiAcctRes submitAcctAlter(TokenContext tokenContext,List<String> YWLSHJH);

    public CommonResponses doMerge(TokenContext tokenContext,String YZJHM,String XZJHM,String XINGMING,String GRCKZHHM);
}
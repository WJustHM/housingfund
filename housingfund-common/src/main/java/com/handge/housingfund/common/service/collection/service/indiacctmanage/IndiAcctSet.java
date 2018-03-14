package com.handge.housingfund.common.service.collection.service.indiacctmanage;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.List;
import java.util.Map;

/**
 * Created by Liujuhao on 2017/7/1.
 */
public interface IndiAcctSet {

    public PageRes<ListOperationAcctsResRes> getAcctsSetInfo(TokenContext tokenContext, final String DWMC, final String ZHZT, final String XingMing, final String ZJHM, final String CZMC, String page, String pagesize,String KSSJ,String JSSJ);

    public PageResNew<ListOperationAcctsResRes> getAcctsSetInfo(TokenContext tokenContext, final String DWMC, final String ZhuangTai, final String XingMing, final String ZJHM, final String CZMC, String marker, String action, String pagesize, String KSSJ, String JSSJ);

    public AddIndiAcctSetRes addAcctSet(TokenContext tokenContext,IndiAcctSetPost addIndiAcctSet);

    // TODO: 2017/7/20 修改了接口名
    public CommonResponses headAcctSet(TokenContext tokenContext, String YWLSH);

    public ReIndiAcctSetRes reAcctSet(TokenContext tokenContext,String YWLSH, IndiAcctSetPut reIndiAcctSet);

    public GetIndiAcctSetRes showAcctSet(TokenContext tokenContext,String YWLSH);

    // TODO: 2017/7/24 审核通过后开户操作（练隆生）
    public void doAcctSet(TokenContext tokenContext,String YWLSH);

    public SubmitIndiAcctSetRes submitAcctSet(TokenContext tokenContext,List<String> YWLSHJH);

    public PersonBaseMessage getIndiAcctSetCheck(TokenContext tokenContext,String xingMing, String zjlx, String zjhm);

    public CommonResponses doInnerTransfer(TokenContext tokenContext,InnerTransferPost innerTransferPost);

    public ImportExcelRes addImportIndiAcctInfo(String id,TokenContext tokenContext, Map<Integer, Map<Integer, Object>> map);

}

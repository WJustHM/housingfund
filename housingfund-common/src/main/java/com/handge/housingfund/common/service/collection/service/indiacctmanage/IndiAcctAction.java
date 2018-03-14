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
public interface IndiAcctAction {

    public PageRes<ListOperationAcctsResRes> getAcctAcionInfo(TokenContext tokenContext,String DWMC, String ZhuangTai, String XingMing, String ZJHM, String GRZH, String CZMC, String czyy,String KSSJ,String JSSJ , String pageNo, String pageSize);

    public PageResNew<ListOperationAcctsResRes> getAcctAcionInfoNew(TokenContext tokenContext, String DWMC, String ZhuangTai, String XingMing, String ZJHM, String GRZH, String CZMC, String czyy, String KSSJ, String JSSJ , String marker,String pageSize, String action);

    public AddIndiAcctActionRes addAcctAction(TokenContext tokenContext, String GRZH, IndiAcctActionPost indiAcctActionPost);

    public ReIndiAcctActionRes reAcctAction(TokenContext tokenContext,String YWLSH, IndiAcctActionPut reIndiAcctAction);

    public GetIndiAcctActionRes showAcctAction(String YWLSH);

    public AutoIndiAcctActionRes AutoIndiAcctAction(TokenContext tokenContext,String GRZH);

    public CommonResponses headAcctAction(TokenContext tokenContext, String YWLSH);

    public void doAcctAction(TokenContext tokenContext, String YWLSH);

    public SubmitIndiAcctFreezeRes submitIndiAcctAction(TokenContext tokenContext,List<String> YWLSHJH);

    /**
     * 输入生效年月时，查看职工是否能进行封存/封存业务，并进行提示信息
     */
    public ComMessage sealCheck(String grzh, String sxny);
}

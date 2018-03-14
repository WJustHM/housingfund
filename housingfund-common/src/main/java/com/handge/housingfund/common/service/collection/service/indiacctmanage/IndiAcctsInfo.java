package com.handge.housingfund.common.service.collection.service.indiacctmanage;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/7/1.
 */
public interface IndiAcctsInfo {

    public PageRes<ListIndiAcctsResRes> getAcctsInfo(TokenContext tokenContext, final String DWMC, final String GRZH, final String XingMing, final String ZJHM, final String GRZHZT, String YWWD,String SFDJ, String startTime, String endTime,String page, String pagesize);

    public PageResNew<ListIndiAcctsResRes> getAcctsInfo(TokenContext tokenContext, final String DWMC, final String GRZH, final String XingMing, final String ZJHM, final String GRZHZT, String YWWD, String SFDJ, String startTime, String endTime, String marker, String action, String pagesize);

    public GetAcctsInfoDetailsRes getAcctsInfoDetails(TokenContext tokenContext,String GRZH);

    public PageRes<Object> getBanks(TokenContext tokenContext,final String Code , final String  Name, final  String pageNo, final String pageSize);

    public PageResNew<Object> getBanks(TokenContext tokenContext,final String Code , final String  Name, String marker,String action, final String pageSize);

    public IndiAcctDepositDetailsNew<GetIndiAcctDepositDetailsDep> getPersonDepositDetails(TokenContext tokenContext, String grzh, String pageNo, String pageSize);

    public ArrayList<PersonAccountsGet> getAccounts(TokenContext tokenContext,String zjhm,String GLXH);

    public PageRes<TransferListGet> getTransferList(TokenContext tokenContext, String Xingming, String GRZH, String ZCDWM, String ZRDWM, String ZJHM,String page, String pagesize, String KSSJ, String JSSJ);

    public PageResNew<TransferListGet> getTransferList(TokenContext tokenContext, String Xingming, String GRZH, String ZCDWM, String ZRDWM, String ZJHM,String marker, String action, String pagesize, String KSSJ, String JSSJ);

    public CommonResponses getTransferDetails(TokenContext tokenContext, String ywlsh);

    public CommonResponses getPersonDepositPdfDetails(TokenContext tokenContext,String grzh,String hjnys,String hjnye);

    public CommonResponses getDiffTerritoryLoadProvePdf(TokenContext tokenContext,String grzh);
}

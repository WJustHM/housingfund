package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.model.individual.ImportExcelRes;
import com.handge.housingfund.common.service.collection.model.individual.PersonRadixExcelRes;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.Map;

/**
 * Created by Liujuhao on 2017/7/19.
 */
// TODO: 2017/7/19 个人基数调整（杜俊杰）
public interface PersonRadix {

    public PageRes<ListPersonRadixResRes> getPersonRadix(TokenContext tokenContext, final  String DWMC, final  String DWZH, final  String ZhuangTai, final  String KSSJ, final  String JSSJ, final String pageNumber, final String pageSize);

    public CommonResponses addPersonRadix(TokenContext tokenContext,final PersonRadixPost body);

    public CommonResponses rePersonRadix(TokenContext tokenContext,final  String YWLSH,final PersonRadixPut body);

    public GetPersonRadixRes showPersonRadix(TokenContext tokenContext,final  String YWLSH);

    public GetPersonRadixBeforeRes autoPersonRadix(TokenContext tokenContext,final String DWZH);

    public CommonResponses headPersonRadix(TokenContext tokenContext, final  String YWLSH);

    public void doPersonRadix( TokenContext tokenContext,final String YWLSH);

    public CommonResponses batchSubmit(TokenContext tokenContext,final BatchSubmission body);

    /**
     * 定时任务执行接口(生效年月到了，仅根据业务更新状态)
     */
    void doRadixTask(String ywlsh);

    public PersonRadixExcelRes getPersonRadixdata(TokenContext tokenContext, String DWZH,String sxny);

    public ImportExcelRes saveImportRadix(String id,TokenContext tokenContext , Map<Integer, Map<Integer, Object>> map);

    public PageResNew<ListPersonRadixResRes> getPersonRadixnew(TokenContext tokenContext, String dwmc, String dwzh, String zhuangTai, String kssj, String jssj, String marker, String pageSize, String action);

    public GetPersonRadixBeforeRes autoPersonRadix(TokenContext tokenContext,final String DWZH,String SXNY);
}

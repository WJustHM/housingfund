package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

/**
 * Created by Liujuhao on 2017/7/19.
 */
// TODO: 2017/7/19 单位缴存比例（杜俊杰）
public interface UnitDepositRatio {

      public PageRes<ListUnitDepositRatioResRes> getDepositRatioInfo(TokenContext tokenContext, final  String DWMC, final  String DWZH, final  String ZhuangTai, final  String KSSJ, final  String JSSJ, final  String pageNumber, final  String pageSize);

      public CommonResponses addDepositRatio(TokenContext tokenContext,final PostListUnitDepositRatioPost body);

      public CommonResponses reDepositRatio(TokenContext tokenContext,final  String YWLSH,final UnitDepositRatioPut body);

      public GetUnitDepositRatioRes showDepositRatio(TokenContext tokenContext,final  String YWLSH);

      public AutoUnitDepositRatioRes autoDepositRatio(TokenContext tokenContext,final  String DWZH);

      public CommonResponses headDepositRatio(TokenContext tokenContext, final String YWLSH);

      public void doActionDepositRatio( TokenContext tokenContext,final String YWLSH);

      public CommonResponses batchSubmit(TokenContext tokenContext,final BatchSubmission body);

      /**
       * 定时任务执行接口(生效年月到了，仅根据业务更新状态)
       */
      void doRatioTask(String ywlsh);

      public PageResNew<ListUnitDepositRatioResRes> getDepositRatioInfonew(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String kssj, String jssj, String marker, String pageSize, String action);

}

package com.handge.housingfund.server.controllers.collection.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.BatchWithdrawlsInfo;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.ReWithdrawlsInfo;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.WithdrawlsDetailInfo;

import java.util.ArrayList;

/**
 * Created by xuefei_wang on 17-6-26.
 */
public interface WithdrawlApi<T> {

     T getWithdrawlDetails(TokenContext tokenContext,final String taskId) ;

     T revokeWithdrawlsTask(TokenContext tokenContext,final String task);

     T updateWithdrawlsTask( TokenContext tokenContext,final String op, final String task, final ArrayList<ReWithdrawlsInfo> infos);

     T searchWithdrawlsTasks(TokenContext tokenContext,String grxm, String dwmc, String grzh, String taskStatus,String ywwd,String yhmc, String begain, String end, String pageNo, String pageSize,String zjhm,String ZongE,String tqyy);

     T searchWithdrawlsTasksNew(TokenContext tokenContext,String grxm, String dwmc, String grzh, String taskStatus,String ywwd, String yhmc, String begain, String end, String action, String marker,String pageSize,String zjhm,String ZongE);

     T saveOrSubmitWithdrawlsTask(TokenContext tokenContext, String operation, final ArrayList<WithdrawlsDetailInfo> infos);

     T getWithdrawlsReadOnly(TokenContext tokenContext,final String zjhm,String type);

     T batchOpWithdrawls(TokenContext tokenContext,BatchWithdrawlsInfo batchWithdrawlsInfo);

     T printWithdrawlsRecords(TokenContext tokenContext,final String zjhm, String begain, String end, String pageNo, String pageSize,String grzh,String XingMing);

     T searchWithdrawlsRecords(TokenContext tokenContext, String zjhm, String begain, String end, String pageNo, String pageSize,String grzh,String XingMing);

     T getNextDate(TokenContext tokenContext,String fse, String yhke);

     T getFslxe(TokenContext tokenContext,String grzh);

     T doFailedWithdrawl(TokenContext tokenContext,String ywlsh,String operation);

}

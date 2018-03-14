package com.handge.housingfund.common.service.collection.service.withdrawl;

import com.handge.housingfund.common.LoanWithdrawl;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/22.
 * 描述
 */
public interface WithdrawlTasks {

    /**
     * 获取提取业务详情
     *
     * @param taskId
     * @return
     */
   ArrayList<WithdrawlsDetailInfo> getWithdrawlDetails(TokenContext tokenContext, String taskId);

    /**
     * 更新提取业务
     *
     * @param op
     * @param taskId
     * @param info
     * @return
     */
    CommonReturn updateWithdrawl(TokenContext tokenContext, String op, String taskId, ArrayList<ReWithdrawlsInfo> info);

    /**
     * 保存或提交提取业务
     *
     * @param op
     * @param infos
     * @return
     */
    CommonReturn saveOrSubmitWithdrawl(TokenContext tokenContext, String op, ArrayList<WithdrawlsDetailInfo> infos);


    /**
     * 打印提取回执(财务模块调用，非前端调用)
     *
     * @param taskId
     * @return
     */
    ReceiptReturn printWithdrawlReceipt(String taskId);


    /**
     * 查询提取记录
     *
     * @param xm
     * @param dwmc
     * @param grzh
     * @param ywzt
     * @param begain
     * @param end
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<Withdrawl> searchWithdrawl(TokenContext tokenContext, String xm, String dwmc, String grzh, String ywzt,String ywwd,String yhmc, String begain, String end, String pageNo, String pageSize,String zjhm,String ZongE,String tqyy);



    /**
     * 查询提取记录new
     *
     * @param xm
     * @param dwmc
     * @param grzh
     * @param ywzt
     * @param begain
     * @param end
     * @param pageSize
     * @return
     */
    PageResNew<Withdrawl> searchWithdrawlNew(TokenContext tokenContext, String xm, String dwmc, String grzh, String ywzt,String ywwd,String yhmc, String begain, String end, String action, String marker, String pageSize,String zjhm,String ZongE);

    /**
     * 获取证件号码只读部分信息
     *
     * @param zjhm
     * @return
     */
    ReadOnly getWithdrawlsReadOnly(TokenContext tokenContext, String zjhm,String type);

    /**
     * 批量操作提取
     *
     * @param batchWithdrawlsInfo
     * @return
     */
    BatchWithdrawlsReturn batchOpWithdrawls(TokenContext tokenContext, BatchWithdrawlsInfo batchWithdrawlsInfo);


    /**
     * 打印个人提取记录
     *
     * @param zjhm
     * @param begain
     * @param end
     * @param pageNo
     * @param pageSize
     * @return
     */
    CommonResponses printWithdrawlsRecords(TokenContext tokenContext, String zjhm, String begain, String end, String pageNo, String pageSize,String grzh,String XingMing);


    /**
     * 搜索个人提取记录
     *
     * @param zjhm
     * @param begain
     * @param end
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<Record> searchWithdrawlsRecords(TokenContext tokenContext, String zjhm, String begain, String end, String pageNo, String pageSize,String grzh,String XingMing);

    /**
     * 计算下次提取日期
     *
     * @param fse
     * @param yhke
     * @return
     */
    NextDate getNextDate(TokenContext tokenContext, String fse, String yhke);

    /**
     * 获取发生利息额
     *
     * @param grzh
     * @return
     */
    FSLXE getFslxe(TokenContext tokenContext, String grzh);

    /**
     * 提取审核通过后的操作
     *
     * @param YWLSH
     * @return
     */
    void doWithdrawl(String YWLSH);

    /**
     * 提取办结操作
     *
     * @param YWLSH
     */
    void doFinal(String YWLSH);

    CommonReturn addWithdrawl(LoanWithdrawl loanWithdrawl);

    /**
     * 审核（全部）通过后的操作
     * @param YWLSH
     */
    void doAfterPassed(String YWLSH,String shy);

    /**
     * 审核（全部）通过后的操作(新)
     * @param YWLSHs
     */
    String doAfterPassed(ArrayList<String> YWLSHs, String shy);

    void bulkReviewPassed(String ywlsh, String shy);

    /**
     * 提取定时任务，当一条提取记录进入待入账状态超过1个小时没有收到到账通知，判断其为入账失败
     * @param ywlsh
     */
    void withdrawlTask(String ywlsh);

    /**
     * 手动修改入账失败的提取
     * @param tokenContext
     * @param ywlsh
     * @param operation
     * @return
     */
    CommonReturn doFailedWithdrawl(TokenContext tokenContext,String ywlsh,String operation);

    /**
     * 银行处理失败时，恢复该条提取记录到入账失败
     * @param ywlsh
     */
    void doRecoverWithdrawl(String ywlsh);
}

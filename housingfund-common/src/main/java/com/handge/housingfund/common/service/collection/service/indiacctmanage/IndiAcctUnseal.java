package com.handge.housingfund.common.service.collection.service.indiacctmanage;

/**
 * Created by 凡 on 2017/9/12.
 */
public interface IndiAcctUnseal extends IndiAcctAction{
    /**
     * 定时任务执行接口(生效年月到了，仅根据业务更新状态)
     */
    void doUnsealTask(String ywlsh);

    /**
     * 直接办结一笔启封业务
     */
    void douUnSealDirect(String grzh, String sxny);
}

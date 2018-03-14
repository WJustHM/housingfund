package com.handge.housingfund.common.service.collection.service.indiacctmanage;

import com.handge.housingfund.common.service.collection.model.individual.ComMessage;

/**
 * Created by 凡 on 2017/9/12.
 */
public interface IndiAcctSeal extends IndiAcctAction{

    /**
     * 定时任务执行接口(生效年月到了，仅根据业务更新状态)
     */
    void doSealTask(String ywlsh);

    /**
     * 直接办结一笔封存业务
     */
    void doSealDirect(String grzh, String sxny);

    /**
     * ComMessage：
     * code : 01(有提示信息)/02（正常）
     * message ：提示信息
     */
    ComMessage getPersonSealMsgForTQ(String grzh);

}

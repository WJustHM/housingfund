package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.model.Actived2Fixed;

/**
 * Created by Administrator on 2017/9/13.
 */
public interface IActived2FixedService {

    /**
     * 活期转定期
     *
     * @param tokenContext
     * @param khyhmc      开户银行名称
     * @param acct_no     专户号码
     * @param deposit_period 存期
     * @param step        状态
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<Actived2Fixed> getActivedToFixeds(TokenContext tokenContext, String khyhmc, String acct_no, String deposit_period, String step, int pageNo, int pageSize);

    /**
     * 获取活期转定期详细信息
     *
     * @param tokenContext
     * @param id        活期转定期id
     * @return
     */
    Actived2Fixed getActivedToFixed(TokenContext tokenContext, String id);

    /**
     * 新增活期转定期
     *
     * @param tokenContext
     * @param actived2Fixed
     * @param type
     * @return
     */
    Actived2Fixed addActivedToFixed(TokenContext tokenContext, Actived2Fixed actived2Fixed, String type);

    /**
     * 修改活期转定期
     *
     * @param tokenContext
     * @param id         活期转定期id
     * @param actived2Fixed
     * @param type
     * @return
     */
    boolean modifyActivedToFixed(TokenContext tokenContext, String id, Actived2Fixed actived2Fixed, String type);

    /**
     * 删除活期转定期
     *
     * @param tokenContext
     * @param id          活期转定期id
     * @return
     */
    boolean delActivedToFixed(TokenContext tokenContext, String id);

    /**
     * 提交活期转定期
     * @param tokenContext
     * @param id          活期转定期id
     * @return
     */
    boolean submitActivedToFixed(TokenContext tokenContext, String id);

    /**
     * 撤回活期转定期
     * @param tokenContext
     * @param id         活期转定期id
     * @return
     */
    boolean revokeActivedToFixed(TokenContext tokenContext, String id);

    /**
     * 更新活期转定期状态
     * @param tokenContext
     * @param id         活期转定期id
     * @param step       状态
     * @return
     */
    boolean updateA2FStep(TokenContext tokenContext, String id, String step);
}

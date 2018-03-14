package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.model.FixedDraw;

/**
 * Created by Administrator on 2017/9/14.
 */
public interface IFixedDrawService {

    /**
     * 定期支取
     *
     * @param khyhmc     开户银行名
     * @param acctNo     专户号码
     * @param bookNo     册号
     * @param bookListNo 笔号
     * @param status     状态
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<FixedDraw> getFixedDraws(TokenContext tokenContext, String khyhmc, String acctNo, String bookNo, int bookListNo, String status, int pageNo, int pageSize);

    /**
     * 获取定期支取详细信息
     *
     * @param tokenContext
     * @param id           定期支取id
     * @return
     */
    FixedDraw getFixedDraw(TokenContext tokenContext, String id);

    /**
     * 新增定期支取
     *
     * @param tokenContext
     * @param fixedDraw
     * @param type         操作类型 0：保存 1：提交
     * @return
     */
    FixedDraw addFixedDraw(TokenContext tokenContext, FixedDraw fixedDraw, String type);

    /**
     * 修改定期支取
     *
     * @param tokenContext
     * @param id           定期支取id
     * @param fixedDraw
     * @param type         操作类型 0：保存 1：提交
     * @return
     */
    boolean modifyFixedDraw(TokenContext tokenContext, String id, FixedDraw fixedDraw, String type);

    /**
     * 删除定期支取
     *
     * @param tokenContext
     * @param id           定期支取id
     * @return
     */
    boolean delFixedDraw(TokenContext tokenContext, String id);

    /**
     * 提交定期支取
     * @param tokenContext
     * @param id           定期支取id
     * @return
     */
    boolean submitFixedDraw(TokenContext tokenContext, String id);

    /**
     * 撤回定期支取
     * @param id           定期支取id
     * @return
     */
    boolean revokeFixedDraw(TokenContext tokenContext, String id);

    /**
     * 更新定期支取状态
     * @param tokenContext
     * @param id           定期支取id
     * @param step         状态
     * @return
     */
    boolean updateF2AStep(TokenContext tokenContext, String id, String step);
}

package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.model.FixedRecord;

/**
 * Created by Administrator on 2017/9/13.
 */
public interface IFixedRecordService {

    /**
     * 获取定期详情列表
     *
     * @param khyhmc
     * @param acct_no
     * @param book_no
     * @param book_list_no
     * @param acct_status
     * @return
     */
     PageRes<FixedRecord> getFixedRecords(String khyhmc, String acct_no, String book_no, int book_list_no, String acct_status, int pageNo, int pageSize);

    /**
     * 获取定期详情
     *
     * @param id
     * @return
     */
     FixedRecord getFixedRecord(String id);

     void addFixedRecord(FixedRecord fixedRecord);

    /**
     * 更新定期记录账户状态
     * @param dqckbh
     * @param acctStatus
     */
     void updateFixedRecord(String dqckbh, String acctStatus);
}

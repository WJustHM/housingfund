package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CBankSendSeqNo;

import java.util.Date;
import java.util.List;

/**
 * Created by gxy on 17-8-7.
 */
public interface ICBankSendSeqNoDAO extends IBaseDAO<CBankSendSeqNo> {
    public String saveWithoutTrigger(CBankSendSeqNo bankSendSeqNo);

    /**
     * @return
     */
    List<CBankSendSeqNo> getUnrecorded(Date startTime, Date endTime);
}

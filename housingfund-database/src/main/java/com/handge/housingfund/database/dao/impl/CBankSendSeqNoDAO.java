package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.database.dao.ICBankSendSeqNoDAO;
import com.handge.housingfund.database.entities.CBankSendSeqNo;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by gxy on 17-8-7.
 */
@Repository
public class CBankSendSeqNoDAO extends BaseDAO<CBankSendSeqNo> implements ICBankSendSeqNoDAO {
    public String saveWithoutTrigger(CBankSendSeqNo bankSendSeqNo) {
        String hql = "SELECT MAX(cBankSendSeqNo.send_seq_no) FROM CBankSendSeqNo AS cBankSendSeqNo WHERE cBankSendSeqNo.send_seq_no LIKE '52240%'";
        Query query = this.getSession().createQuery(hql);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        String no = (String) query.getSingleResult();
        String date = DateUtil.date2Str(new Date(), "YYMMdd");
        String sendNo = null;
        if (no == null) {
            sendNo = "52240" + date + "000000001";
        } else {
            String oldDate = no.substring(5, 11);
            if (oldDate.equals(date)) {
                int seq = Integer.valueOf(no.substring(11, no.length())) + 1;
                sendNo = "52240" + date + String.format("%09d", seq);
            } else {
                sendNo = "52240" + date + "000000001";
            }
        }
        bankSendSeqNo.setSend_seq_no(sendNo);
        this.save(bankSendSeqNo);
        return sendNo;
    }

    @Override
    public List<CBankSendSeqNo> getUnrecorded(Date startTime, Date endTime) {
        String sql = "select cbs from CBankSendSeqNo cbs left join CBankAccChangeNotice cbacn " +
                "on (cbs.host_seq_no=cbacn.host_seq_no) " +
                "where cbs.created_at<=:endtime and cbs.created_at>=:start and cbacn.host_seq_no is null and cbs.amt is not null or cbs.host_seq_no is null";
        List<CBankSendSeqNo> list = getSession().createQuery(sql, CBankSendSeqNo.class)
                .setParameter("start", startTime)
                .setParameter("endtime", endTime)
                .list();
        return list;
    }
}

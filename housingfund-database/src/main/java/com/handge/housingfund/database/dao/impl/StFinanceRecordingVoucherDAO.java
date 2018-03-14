package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.IStFinanceRecordingVoucherDAO;
import com.handge.housingfund.database.entities.StFinanceRecordingVoucher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StFinanceRecordingVoucherDAO extends BaseDAO<StFinanceRecordingVoucher>
        implements IStFinanceRecordingVoucherDAO {

    @Override
    public List<Object[]> getJDHJ() {
        String sql = "SELECT SUM(JFFSE),SUM(DFFSE) FROM st_finance_recording_voucher";
        List<Object[]> list = getSession().createNativeQuery(sql).list();
        return list;
    }

    @Override
    public List<Object[]> getVoucherHJ(String KSSJ, String JSSJ) {
        return null;
    }

    @Override
    public void updateVoucher(String KSSJ, String JSSJ) {
        String sql = "update c_finance_recording_voucher_extension ce,st_finance_recording_voucher sv set ce.SFJZ=1 " +
                "WHERE ce.id=sv.extension and sv.created_at>=:kssj and sv.created_at<=:jssj";
        getSession().createNativeQuery(sql)
                .setParameter("kssj",KSSJ)
                .setParameter("jssj",JSSJ)
                .executeUpdate();
    }
}

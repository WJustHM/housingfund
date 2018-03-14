package com.handge.housingfund.bank.server.service;

import com.handge.housingfund.bank.server.ICenter;
import com.handge.housingfund.bank.server.IHandleAccChangeNotice;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.bank.bean.head.SysHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeOut;
import com.handge.housingfund.common.service.bank.bean.sys.InterfaceCheckIn;
import com.handge.housingfund.common.service.bank.bean.sys.InterfaceCheckOut;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.database.dao.ICBankSendSeqNoDAO;
import com.handge.housingfund.database.entities.CBankSendSeqNo;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 公积金中心接口
 * Created by gxy on 17-8-14.
 */
@Component
public class CenterImpl implements ICenter {
    private static Logger logger = LogManager.getLogger(CenterImpl.class);

    @Autowired
    private IHandleAccChangeNotice iHandleAccChangeNotice;
    @Autowired
    private ICBankSendSeqNoDAO icBankSendSeqNoDAO;

    @Override
    public InterfaceCheckIn recMsg(InterfaceCheckOut interfaceCheckOut) throws Exception {
        String[] dateTime = DateUtil.getDatetime();
        InterfaceCheckIn interfaceCheckIn = new InterfaceCheckIn();
        CBankSendSeqNo cBankSendSeqNo = new CBankSendSeqNo();
        cBankSendSeqNo.setTxCode("SYS600");
        cBankSendSeqNo.setSend_date(DateUtil.str2Date("yyyyMMdd", dateTime[0]));
        String id = icBankSendSeqNoDAO.save(cBankSendSeqNo);
        String sendSeqNo = icBankSendSeqNoDAO.get(id).getSend_seq_no();
        String sendNode = interfaceCheckOut.getSysHeadOut().getSendNode();
        SysHeadIn sysHeadIn = getSysHeadIn("0", sendSeqNo, "SYS600", sendNode);

        interfaceCheckIn.setSysHeadIn(sysHeadIn);
        logger.info("发送到住建部的数据:\n" + interfaceCheckIn);

        return interfaceCheckIn;
    }

    @Override
    public AccChangeNoticeIn recMsg(AccChangeNoticeOut accChangeNoticeOut) throws Exception {
        String[] dateTime = DateUtil.getDatetime();
        AccChangeNoticeIn accChangeNoticeIn = new AccChangeNoticeIn();
        CBankSendSeqNo cBankSendSeqNo = new CBankSendSeqNo();
        cBankSendSeqNo.setTxCode("SBDC100");
        cBankSendSeqNo.setSend_date(DateUtil.str2Date("yyyyMMdd", dateTime[0]));
        String id = icBankSendSeqNoDAO.save(cBankSendSeqNo);
        String sendSeqNo = icBankSendSeqNoDAO.get(id).getSend_seq_no();
        String sendNode = accChangeNoticeOut.getSysHeadOut().getSendNode();
        SysHeadIn sysHeadIn = getSysHeadIn("0", sendSeqNo, "SBDC100", sendNode);

        accChangeNoticeIn.setSysHeadIn(sysHeadIn);
        logger.info("发送到住建部的数据:\n" + accChangeNoticeIn);

        new Thread(() -> iHandleAccChangeNotice.handler(accChangeNoticeOut)).start();

        return accChangeNoticeIn;
    }

    public static SysHeadIn getSysHeadIn(String txStatus, String sendSeqNo, String txCode, String sendNode) {
        Configuration cfg = Configure.getInstance().getConfiguration("bank");
        String[] dateTime = DateUtil.getDatetime();
        SysHeadIn sysHeadIn = new SysHeadIn();
        sysHeadIn.setTxStatus(txStatus);
        if ("0".equals(txStatus)) {
            sysHeadIn.setRtnCode("00000");
            sysHeadIn.setRtnMessage("成功");
        } else {
            sysHeadIn.setRtnCode("10000");
            sysHeadIn.setRtnMessage("失败");
        }
        sysHeadIn.setReceiveDate(dateTime[0]);
        sysHeadIn.setReceiveTime(dateTime[1]);
        sysHeadIn.setReceiveSeqNo(sendSeqNo);
        sysHeadIn.setReceiveNode(cfg.getString("nodeNo"));
        sysHeadIn.setTxCode(txCode);
        sysHeadIn.setSendNode(sendNode);

        return sysHeadIn;
    }
}

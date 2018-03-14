package com.handge.housingfund.bank.service;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.ibank.IReSendNotice;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.ICBankAccChangeNoticeDAO;
import com.handge.housingfund.database.entities.CBankAccChangeNotice;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.tienon.util.FileFieldConv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gxy on 17-12-1.
 */
@Component
public class ReSendNotice implements IReSendNotice {
    private static Logger logger = LogManager.getLogger(ReSendNotice.class);

    @Autowired
    private ICBankAccChangeNoticeDAO icBankAccChangeNoticeDAO;

    @Override
    public void reSendNotice() {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("is_make_acc", "0");

        List<CBankAccChangeNotice> cBankAccChangeNoticeList = DAOBuilder.instance(icBankAccChangeNoticeDAO).searchFilter(filter).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        System.out.println(cBankAccChangeNoticeList.size());
        for (CBankAccChangeNotice cBankAccChangeNotice : cBankAccChangeNoticeList) {
            String date = DateUtil.date2Str(cBankAccChangeNotice.getDate(), "yyyyMMdd");
            String time = cBankAccChangeNotice.getTime();

            AccChangeNoticeFile accChangeNoticeFile = new AccChangeNoticeFile();
            accChangeNoticeFile.setNo("1");
            accChangeNoticeFile.setAcct(cBankAccChangeNotice.getAcct());
            accChangeNoticeFile.setHostSeqNo(cBankAccChangeNotice.getHost_seq_no());
            accChangeNoticeFile.setTxCode(cBankAccChangeNotice.getTx_code());
            accChangeNoticeFile.setOpponentAcct(cBankAccChangeNotice.getOpponent_acct());
            accChangeNoticeFile.setOpponentName(cBankAccChangeNotice.getOpponent_name());
            accChangeNoticeFile.setAmt(cBankAccChangeNotice.getAmt());
            accChangeNoticeFile.setDate(date);
            accChangeNoticeFile.setTime(time);
            accChangeNoticeFile.setAvailableAmt(cBankAccChangeNotice.getAvailable_amt());
            accChangeNoticeFile.setOpenBankNo(cBankAccChangeNotice.getOpen_bank_no());
            accChangeNoticeFile.setRemark(cBankAccChangeNotice.getRemark());
            accChangeNoticeFile.setCurrIden(cBankAccChangeNotice.getCurr_iden());
            accChangeNoticeFile.setCurrNo(cBankAccChangeNotice.getCurr_no());
            accChangeNoticeFile.setBalance(cBankAccChangeNotice.getBalance());
            accChangeNoticeFile.setOverdraft(cBankAccChangeNotice.getOverdraft());
            accChangeNoticeFile.setVoucherType(cBankAccChangeNotice.getVoucher_type());
            accChangeNoticeFile.setVoucherNo(cBankAccChangeNotice.getVoucher_no());
            accChangeNoticeFile.setOpponentBankNo(cBankAccChangeNotice.getOpponent_bank_no());
            accChangeNoticeFile.setSummary(cBankAccChangeNotice.getSummary());
            accChangeNoticeFile.setRedo(cBankAccChangeNotice.getRedo());
            accChangeNoticeFile.setBookNo(cBankAccChangeNotice.getBook_no());
            accChangeNoticeFile.setBookListNo(cBankAccChangeNotice.getBook_list_no());

            String data;
            try {
                data = FileFieldConv.fieldASCtoBCD(TransactionFileFactory.getFileContent(Arrays.asList(accChangeNoticeFile)),"GBK");
                String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                        "<message>\n" +
                        "<head>\n" +
                        "<field name=\"SendDate\">" + date + "</field>\n" +
                        "<field name=\"SendTime\">" + time + "</field>\n" +
                        "<field name=\"SendSeqNo\">52240"+ date +"0"+ time +"</field>\n" +
                        "<field name=\"SendNode\">D00000</field>\n" +
                        "<field name=\"TxCode\">SBDC100</field>\n" +
                        "<field name=\"ReceiveNode\">C52240</field>\n" +
                        "<field name=\"BDCDate\">" + date + "</field>\n" +
                        "<field name=\"BDCTime\">" + time + "</field>\n" +
                        "<field name=\"BDCSeqNo\">52240"+ date +"0"+ time +"</field>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<field-list name=\"FILE_LIST\">\n" +
                        "<field-list name=\"0\">\n" +
                        "<field name=\"DATA\">"+ data +"</field>\n" +
                        "<field name=\"NAME\">BDC_BAL_NTF_" + date + "_" + time +"350.act</field>\n" +
                        "</field-list>\n" +
                        "</field-list>\n" +
                        "</body>\n" +
                        "</message>";
                logger.info("发送到公积金中心的报文:\n" + JAXBUtil.formatXml(xml, "send"));
                Sender sender = new Sender();
                String respXml = sender.invoke2(xml);
                logger.info("从公积金中心收到的报文:\n" + JAXBUtil.formatXml(respXml, "receive"));

                //删除已发送的
                DAOBuilder.instance(icBankAccChangeNoticeDAO).entity(cBankAccChangeNotice).deleteForever(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

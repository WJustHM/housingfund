package com.handge.housingfund.bank.testapi.bank;

import com.handge.housingfund.bank.testapi.*;
import com.handge.housingfund.common.service.bank.bean.center.*;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.bean.transfer.*;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.bank.ibank.IReSendNotice;
import com.handge.housingfund.common.service.bank.ibank.ITransfer;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.JAXBUtil;
import com.handge.housingfund.common.service.util.Sender;
import com.handge.housingfund.common.service.util.TransactionFileFactory;
import com.tienon.util.FileFieldConv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.handge.housingfund.common.service.util.DateUtil.*;

/**
 * Created by gxy on 17-6-21.
 */
public class BankDao {
    private static Logger logger = LogManager.getLogger(BankDao.class);

    public static void send(IBank iBank) throws Exception {
        //body
        SinglePaymentIn spi = TestSinglePayment.getBOC4Common();
//        SinglePaymentIn spi = TestSinglePayment.getBOC4Private();
//        SinglePaymentIn spi = TestSinglePayment.getCCB4Common();
//        SinglePaymentIn spi = TestSinglePayment.getCCB4Private();
//        SinglePaymentIn spi = TestSinglePayment.getABC4Common();
//        SinglePaymentIn spi = TestSinglePayment.getABC4Private();
//        SinglePaymentIn spi = TestSinglePayment.getICBC4Common();
//        SinglePaymentIn spi = TestSinglePayment.getICBC4Private();
//        SinglePaymentIn spi = TestSinglePayment.getBOCOM4Common();
//        SinglePaymentIn spi = TestSinglePayment.getBOCOM4Private();
//        SinglePaymentIn spi = TestSinglePayment.getGZBC4Common();
//        SinglePaymentIn spi = TestSinglePayment.getGZBC4Private();


//        SingleCollectionIn sci = TestSingleCollection.getBOCSelf4Common();
//        SingleCollectionIn sci = TestSingleCollection.getBOCSelf4Private();
//        SingleCollectionIn sci = TestSingleCollection.getBOCOther4Common();
//        SingleCollectionIn sci = TestSingleCollection.getABCSelf4Common();
//        SingleCollectionIn sci = TestSingleCollection.getABCSelf4Private();
        SingleCollectionIn sci = TestSingleCollection.getBOCOMSelf4Common();
//        SingleCollectionIn sci = TestSingleCollection.getBOCOMSelf4Private();
//        SingleCollectionIn sci = TestSingleCollection.getICBCSelf4Common();
//        SingleCollectionIn sci = TestSingleCollection.getICBCSelf4Private();
//        SingleCollectionIn sci = TestSingleCollection.getCCBSelf4Common();
//        SingleCollectionIn sci = TestSingleCollection.getCCBSelf4Private();
//        SingleCollectionIn sci = TestSingleCollection.getGZBC4Common();
//        SingleCollectionIn sci = TestSingleCollection.getGZBC4Private();


//        BatchPaymentIn bpi = TestBatchPayment.getBOC4Common();
//        BatchPaymentIn bpi = TestBatchPayment.getBOC4Private();
//        BatchPaymentIn bpi = TestBatchPayment.getABC4Common();
//        BatchPaymentIn bpi = TestBatchPayment.getABC4Private();
//        BatchPaymentIn bpi = TestBatchPayment.getICBC4Private();
//        BatchPaymentIn bpi = TestBatchPayment.getCCB4Private();
        BatchPaymentIn bpi = TestBatchPayment.getBOCOM4Common();
//        BatchPaymentIn bpi = TestBatchPayment.getBOCOM4Private();
//        BatchPaymentIn bpi = TestBatchPayment.getGZBC4Common();
//        BatchPaymentIn bpi = TestBatchPayment.getGZBC4Private();


//        BatchCollectionIn bci = TestBatchCollection.getBOCSelf4Common();
//        BatchCollectionIn bci = TestBatchCollection.getBOCSelf4Private();
//        BatchCollectionIn bci = TestBatchCollection.getABCSelf4Common();
//        BatchCollectionIn bci = TestBatchCollection.getABCSelf4Private();
//        BatchCollectionIn bci = TestBatchCollection.getICBCSelf4Private();
//        BatchCollectionIn bci = TestBatchCollection.getCCBSelf4Private();
        BatchCollectionIn bci = TestBatchCollection.getBOCOMSelf4Common();
//        BatchCollectionIn bci = TestBatchCollection.getBOCOMSelf4Private();
//        BatchCollectionIn bci = TestBatchCollection.getGZBCSelf4Common();
//        BatchCollectionIn bci = TestBatchCollection.getGZBCSelf4Private();

//        LoanDeductionIn ldi = TestLoanDeduction.getBOC4Private();
//        LoanDeductionIn ldi = TestLoanDeduction.getABC4Private();
//        LoanDeductionIn ldi = TestLoanDeduction.getICBC4Private();
//        LoanDeductionIn ldi = TestLoanDeduction.getCCB4Private();
        LoanDeductionIn ldi = TestLoanDeduction.getBOCOM4Private();
//        LoanDeductionIn ldi = TestLoanDeduction.getGZBC4Private();

//        Actived2FixedIn a2fi = TestActived2Fixed.getBOC4Common();
//        Actived2FixedIn a2fi = TestActived2Fixed.getABC4Common();
//        Actived2FixedIn a2fi = TestActived2Fixed.getICBC4Common();
//        Actived2FixedIn a2fi = TestActived2Fixed.getCCB4Common();
        Actived2FixedIn a2fi = TestActived2Fixed.getBOCOM4Common();
//        Actived2FixedIn a2fi = TestActived2Fixed.getGZBC4Common();

//        Actived2NoticeDepositIn a2ndi = TestActived2NoticeDeposit.getBOC4Common();
//        Actived2NoticeDepositIn a2ndi = TestActived2NoticeDeposit.getABC4Common();
//        Actived2NoticeDepositIn a2ndi = TestActived2NoticeDeposit.getICBC4Common();
//        Actived2NoticeDepositIn a2ndi = TestActived2NoticeDeposit.getCCB4Common();
//        Actived2NoticeDepositIn a2ndi = TestActived2NoticeDeposit.getBOCOM4Common();
//        Actived2NoticeDepositIn a2ndi = TestActived2NoticeDeposit.getGZBC4Common();

//        FixedDrawIn fdi = TestFixedDraw.getBOC4Common();
//        FixedDrawIn fdi = TestFixedDraw.getABC4Common();
//        FixedDrawIn fdi = TestFixedDraw.getICBC4Common();
//        FixedDrawIn fdi = TestFixedDraw.getCCB4Common();
//        FixedDrawIn fdi = TestFixedDraw.getBOCOM4Common();
//        FixedDrawIn fdi = TestFixedDraw.getGZBC4Common();

//        NoticeDepositDrawIn nddi = TestNoticeDepositDraw.getBOC4Common();
//        NoticeDepositDrawIn nddi = TestNoticeDepositDraw.getICBC4Common();
//        NoticeDepositDrawIn nddi = TestNoticeDepositDraw.getCCB4Common();
//        NoticeDepositDrawIn nddi = TestNoticeDepositDraw.getBOCOM4Common();
//        NoticeDepositDrawIn nddi = TestNoticeDepositDraw.getGZBC4Common();

//        NoticeDepositDrawSetIn nddsi = TestNoticeDepositDrawSet.getBOC();
//        NoticeDepositDrawSetIn nddsi = TestNoticeDepositDrawSet.getICBC();
//        NoticeDepositDrawSetIn nddsi = TestNoticeDepositDrawSet.getCCB();
//        NoticeDepositDrawSetIn nddsi = TestNoticeDepositDrawSet.getBOCOM();
//        NoticeDepositDrawSetIn nddsi = TestNoticeDepositDrawSet.getGZBC();

//        NoticeDepositDrawCancelIn nddci = TestNoticeDepositDrawCancel.getBOC();
//        NoticeDepositDrawCancelIn nddci = TestNoticeDepositDrawCancel.getICBC();
//        NoticeDepositDrawCancelIn nddci = TestNoticeDepositDrawCancel.getCCB();
//        NoticeDepositDrawCancelIn nddci = TestNoticeDepositDrawCancel.getBOCOM();
//        NoticeDepositDrawCancelIn nddci = TestNoticeDepositDrawCancel.getGZBC();

//        NoticeDepositDrawQueryIn nddqi = TestNoticeDepositDrawQuery.getBOC();
//        NoticeDepositDrawQueryIn nddqi = TestNoticeDepositDrawQuery.getICBC();
//        NoticeDepositDrawQueryIn nddqi = TestNoticeDepositDrawQuery.getCCB();
//        NoticeDepositDrawQueryIn nddqi = TestNoticeDepositDrawQuery.getBOCOM();
//        NoticeDepositDrawQueryIn nddqi = TestNoticeDepositDrawQuery.getGZBC();

//        BatchResultQueryIn brqi = TestBatchResultQuery.getBOC("52240170822198769922");
//        BatchResultQueryIn brqi = TestBatchResultQuery.getABC("0495120170104");
//        BatchResultQueryIn brqi = TestBatchResultQuery.getICBC("52240170822598662241");
//        BatchResultQueryIn brqi = TestBatchResultQuery.getCCB("VS201707270000018337");
        BatchResultQueryIn brqi = TestBatchResultQuery.getBOCOM("52240171213030475883");
//        BatchResultQueryIn brqi = TestBatchResultQuery.getGZBC("19177");

//        BatchResultDownloadIn brdi = TestBatchResultDownload.getBOC("3","52240170821358933912");
//        BatchResultDownloadIn brdi = TestBatchResultDownload.getABC("1","0495420170104");
//        BatchResultDownloadIn brdi = TestBatchResultDownload.getICBC("1","20170820100003872479");
//        BatchResultDownloadIn brdi = TestBatchResultDownload.getCCB("1","VS201707270000018337");
        BatchResultDownloadIn brdi = TestBatchResultDownload.getBOCOM("3","52240171213030475883");
//        BatchResultDownloadIn brdi = TestBatchResultDownload.getBOCOM("1","19177");

//        TransactionResultQueryIn trqi = TestTransactionResultQuery.getBOC("20200725","52240170725435946949");
//        TransactionResultQueryIn trqi = TestTransactionResultQuery.getABC("20170725","52240170725475744133");
//        TransactionResultQueryIn trqi = TestTransactionResultQuery.getICBC("20170725","52240170725509871026");
//        TransactionResultQueryIn trqi = TestTransactionResultQuery.getCCB("20170927","52240170927849166884");
        TransactionResultQueryIn trqi = TestTransactionResultQuery.getBOCOM("20171213","051712130000006");
//        TransactionResultQueryIn trqi = TestTransactionResultQuery.getGZBC("","52240170816926992789");

//        AccTransDetailQueryIn atdqi = TestAccTransDetailQuery.getBOC("20170821","20170821");
//        AccTransDetailQueryIn atdqi = TestAccTransDetailQuery.getABC("20170730","20170730");
//        AccTransDetailQueryIn atdqi = TestAccTransDetailQuery.getICBC("20170725","20170725");
//        AccTransDetailQueryIn atdqi = TestAccTransDetailQuery.getCCB("20171226","20171228");
        AccTransDetailQueryIn atdqi = TestAccTransDetailQuery.getBOCOM("20191020","20191030");
//        AccTransDetailQueryIn atdqi = TestAccTransDetailQuery.getGZBC("20270722","20270722");

//        FixedTransferModeSetIn ftmsi = TestFixedTransferModeSet.getBOC();
//        FixedTransferModeSetIn ftmsi = TestFixedTransferModeSet.getICBC();
//        FixedTransferModeSetIn ftmsi = TestFixedTransferModeSet.getCCB();
//        FixedTransferModeSetIn ftmsi = TestFixedTransferModeSet.getBOCOM();
//        FixedTransferModeSetIn ftmsi = TestFixedTransferModeSet.getGZBC();

//        SingleTransferAccountIn stai = TestSingleTransferAccount.getBOC();
//        SingleTransferAccountIn stai = TestSingleTransferAccount.getABC();
//        SingleTransferAccountIn stai = TestSingleTransferAccount.getICBC();
//        SingleTransferAccountIn stai = TestSingleTransferAccount.getCCB();
        SingleTransferAccountIn stai = TestSingleTransferAccount.getBOCOM();
//        SingleTransferAccountIn stai = TestSingleTransferAccount.getGZBC();

        //head
        CenterHeadIn chi = CenterHead.getCCBHead("BDC123");
//        CenterInterfaceCheckIn centerInterfaceCheckIn = new CenterInterfaceCheckIn(chi);
//        LoginIn loginIn = new LoginIn(chi);
//        LogoutIn logoutIn = new LogoutIn(chi);
        ChgNoQueryIn chgNoQueryIn = new ChgNoQueryIn(chi, "313", "7094");
        FixedAccBalanceQueryIn fixedAccBalanceQueryIn = new FixedAccBalanceQueryIn(chi, "310899999600008158610", "1");
        ActivedAccBalanceQueryIn activedAccBalanceQueryIn = new ActivedAccBalanceQueryIn(chi, "156","1","52001694036050006556","1");

        IBank bankImpl = (IBank) iBank;
//        bankImpl.sendMsg(centerInterfaceCheckIn);//接口探测
//        bankImpl.sendMsg(loginIn) ;//login
//        bankImpl.sendMsg(logoutIn);//logout
//        bankImpl.sendMsg(chgNoQueryIn);//联行号查询
//        bankImpl.sendMsg(fixedAccBalanceQueryIn);//定期账户余额查询
//        bankImpl.sendMsg(activedAccBalanceQueryIn);//活期账户实时余额查询

//        bankImpl.sendMsg(spi);//单笔付款
//        bankImpl.sendMsg(sci);//单笔收款

//        List<BatchPaymentFileSelf> list = new ArrayList<>();
//        list.add(new BatchPaymentFileOther(
//                "1",
//                new BigDecimal("5.00"),
//                "182708222217",
//                "公积金中心测试账户五",
//                "104362004010",
//                "mark",
//                "10000001",
//                new BigDecimal("5.00")));
//        list.add(new BatchPaymentFileOther(
//                "2",
//                new BigDecimal("5.00"),
//                "182708222221",
//                "公积金中心测试账户六",
//                "104362004010",
//                "mark",
//                "10000002",
//                new BigDecimal("5.00")));
//        List<BatchPaymentFileSelf> list = new ArrayList<>();
//        list.add(new BatchPaymentFileSelf(
//                "1",
//                new BigDecimal("10.00"),
//                "6212262406000426368",
//                "玷鑫",
//                "ICBCbatchpayment",
//                "50000011",
//                new BigDecimal("10.00")));
//        list.add(new BatchPaymentFileSelf(
//                "2",
//                new BigDecimal("10.00"),
//                "6222022406001007134",
//                "宪隔",
//                "ICBCbatchpayment",
//                "50000012",
//                new BigDecimal("10.00")));
//        list.add(new BatchPaymentFileSelf(
//                "3",
//                new BigDecimal("10.00"),
//                "6222082406000465272",
//                "遇疆",
//                "ICBCbatchpayment",
//                "50000013",
//                new BigDecimal("10.00")));
//        bankImpl.sendMsg(bpi, list);//批量付款

//        List<BatchCollectionFileSelf> list1 = new ArrayList<>();
//        list1.add(new BatchCollectionFileOther(
//                "1",
//                new BigDecimal("2.00"),
//                "182708222217",
//                "公积金中心测试账户五",
//                "104100000004",
//                "batchcollection"
//        ));
//        list1.add(new BatchCollectionFileOther(
//                "2",
//                new BigDecimal("2.00"),
//                "184208222221",
//                "公积金中心测试账户六",
//                "104100000004",
//                "batchcollection"
//        ));
//        bankImpl.sendMsg(bci, list1);//批量收款

        List<LoanDeductionFileSelf> list2 = new ArrayList<>();
//        list2.add(new LoanDeductionFileOther(
//                "1",
//                new BigDecimal("5.00"),
//                "176708222268",
//                "王小一",
//                "104362004010",
//                "0",
//                "LoanDeduction"
//        ));
//        list2.add(new LoanDeductionFileOther(
//                "2",
//                new BigDecimal("5.00"),
//                "182708222228",
//                "王小一",
//                "104362004010",
//                "0",
//                "LoanDeduction"
//        ));
        list2.add(new LoanDeductionFileSelf(
                "1",
                new BigDecimal("5.00"),
                "310972700000300477202",
                "段丽鹏",
                "0",
                "LoanDeduction"
        ));
        list2.add(new LoanDeductionFileSelf(
                "2",
                new BigDecimal("5.00"),
                "3109720900003003382020",
                "李合宽",
                "0",
                "LoanDeduction"
        ));
//        bankImpl.sendMsg(ldi, list2);//贷款扣款

//        bankImpl.sendMsg(a2fi);//活期转定期
//        bankImpl.sendMsg(a2ndi);//活期转通知存款
//        bankImpl.sendMsg(fdi);//定期支取
//        bankImpl.sendMsg(nddi);//通知存款支取
//        bankImpl.sendMsg(brqi);//批量业务结果查询
//        bankImpl.sendMsg(brdi);//批量业务结果下载
//        bankImpl.sendMsg(trqi);//交易结果查询
//        bankImpl.sendMsg(atdqi);//账户交易明细查询
//        bankImpl.sendMsg(ftmsi);//定期转存方式设定
//        bankImpl.sendMsg(nddsi);//通知存款支取设定
//        bankImpl.sendMsg(nddci);//通知存款支取通知取消
//        bankImpl.sendMsg(nddqi);//通知存款支取通知查询
//        bankImpl.sendMsg(stai);//单笔转账

        //sys
//        String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
//                "<message>\n" +
//                "<head>\n" +
//                "<field name=\"TxCode\">SYS600</field>\n" +
//                "<field name=\"CustNo\"></field>\n" +
//                "<field name=\"SendTime\">111153</field>\n" +
//                "<field name=\"ReceiveNode\">C46010</field>\n" +
//                "<field name=\"SendDate\">20151102</field>\n" +
//                "<field name=\"SendSeqNo\"></field>\n" +
//                "<field name=\"TxUnitNo\"></field>\n" +
//                "<field name=\"SendNode\">104000</field>\n" +
//                "<field name=\"BDCTime\"></field>\n" +
//                "<field name=\"BDCDate\"></field>\n" +
//                "<field name=\"BDCSeqNo\"></field>\n" +
//                "<field name=\"OperNo\"></field>\n" +
//                "</head>\n" +
//                "<body></body>\n" +
//                "</message>";

        String[] datetime = getDatetime();
        AccChangeNoticeFile accChangeNoticeFile = new AccChangeNoticeFile();
        accChangeNoticeFile.setAcct("184208222254");
        accChangeNoticeFile.setAmt(new BigDecimal(10000));
        accChangeNoticeFile.setOpponentAcct("9817101200022");
        accChangeNoticeFile.setOpponentName("对手账户");
        accChangeNoticeFile.setDate(datetime[0]);
        accChangeNoticeFile.setTime(datetime[1]);
        accChangeNoticeFile.setNo("15115451515");
        accChangeNoticeFile.setBookNo("");
        accChangeNoticeFile.setBookListNo("");
        accChangeNoticeFile.setHostSeqNo("15115451515");
        accChangeNoticeFile.setCurrIden("1");
        accChangeNoticeFile.setCurrNo("156");
        accChangeNoticeFile.setBalance(new BigDecimal("1000000"));
        accChangeNoticeFile.setSummary("五舟汗云科技有限公司");
        accChangeNoticeFile.setRemark("备注");

        String data = FileFieldConv.fieldASCtoBCD(TransactionFileFactory.getFileContent(Arrays.asList(accChangeNoticeFile)),"GBK");

        String xml1 = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<message>\n" +
                "<head>\n" +
                "<field name=\"SendDate\">" + datetime[0] + "</field>\n" +
                "<field name=\"SendTime\">" + datetime[1] + "</field>\n" +
                "<field name=\"SendSeqNo\">52240"+ datetime[0] +"0"+ datetime[1] +"</field>\n" +
                "<field name=\"SendNode\">D00000</field>\n" +
                "<field name=\"TxCode\">SBDC100</field>\n" +
                "<field name=\"ReceiveNode\">C52240</field>\n" +
                "<field name=\"BDCDate\">" + datetime[0] + "</field>\n" +
                "<field name=\"BDCTime\">" + datetime[1] + "</field>\n" +
                "<field name=\"BDCSeqNo\">52240"+ datetime[0] +"0"+ datetime[1] +"</field>\n" +
                "</head>\n" +
                "<body>\n" +
                "<field-list name=\"FILE_LIST\">\n" +
                "<field-list name=\"0\">\n" +
                "<field name=\"DATA\">"+ data +"</field>\n" +
                "<field name=\"NAME\">BDC_BAL_NTF_"+ datetime[0] +"_111153350.act</field>\n" +
                "</field-list>\n" +
                "</field-list>\n" +
                "</body>\n" +
                "</message>";
        logger.info("发送到公积金中心的报文:\n" + JAXBUtil.formatXml(xml1, "send"));
        Sender sender = new Sender();
        String respXml = sender.invoke2(xml1);
        logger.info("从公积金中心收到的报文:\n" + JAXBUtil.formatXml(respXml, "receive"));
    }

    public static void sendTransfer(ITransfer iTransfer) throws Exception {
//        CRFCenterCodeQueryIn queryIn = TestCRFCenterCodeQuery.getCenterCode("","","D00000");
//        SingleTransInApplIn transInApplIn = TestSingleTransInAppl.getSingleTransInApplIn("C52240");
        SingleTransOutInfoIn transOutInfoIn = TestSingleTransOutInfo.getSingleTransOutInfoIn("C52240");

//        ApplScheduleQueryIn applScheduleQueryIn = new ApplScheduleQueryIn();
//        applScheduleQueryIn.setCenterHeadIn(CenterHead.getTransHead("BDC903", "C52240"));
//        applScheduleQueryIn.setOrConNum("5224001712131000");
//        applScheduleQueryIn.setOrTxFunc("1");
//        applScheduleQueryIn.setTranOutUnitNo("522700000000000");

//        TransInApplCancelIn transInApplCancelIn = new TransInApplCancelIn(
//                CenterHead.getTransHead("BDC904", "C52240"),
//                "5224001712131000",
//                "1",
//                "522700000000000",
//                "黔南布依族苗族自治州住房公积金管理中心",
//                "夜神月",
//                "99",
//                "123456789"
//        );


//        System.out.println(iTransfer.sendMsg(queryIn));
//        iTransfer.sendMsg(transInApplIn);
        iTransfer.sendMsg(transOutInfoIn);
//        iTransfer.sendMsg(applScheduleQueryIn);
//        iTransfer.sendMsg(transInApplCancelIn);
    }
    public static void reSendNotice(IReSendNotice iReSendNotice) {
        iReSendNotice.reSendNotice();
    }
}

package com.handge.housingfund.bank.testapi;

import com.handge.housingfund.common.service.bank.bean.center.*;
import com.handge.housingfund.common.service.util.TransactionFileFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成测试FileList
 * Created by gxy on 17-7-4.
 */
public class CreateFileList {
    private static String basePath = "housingfund-bank/src/main/java/com/handge/housingfund/bank/testapi/file";

    /**
     * 中行批量付款，对公
     */
    public static FileList getBOCBatchPayment4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
//        String data = readFile(basePath + "/BOCBatchPayment4Common");
        List<Object> list = new ArrayList<>();
        list.add(new BatchPaymentFileOther(
                "1",
                new BigDecimal("1.00"),
                "182708222217",
                "公积金中心测试账户五",
                "104362004010",
                "mark",
                "10000001",
                new BigDecimal("1.00")));
        list.add(new BatchPaymentFileOther(
                "2",
                new BigDecimal("2.00"),
                "182708222221",
                "公积金中心测试账户六",
                "104362004010",
                "mark",
                "10000002",
                new BigDecimal("2.00")));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }
    /**
     * 中行批量付款，对私
     */
    public static FileList getBOCBatchPayment4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/BOCBatchPayment4Private");
        return new FileList(name, data);
    }
    /**
     * 农行批量付款，对公
     */
    public static FileList getABCBatchPayment4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/ABCBatchPayment4Common");
        return new FileList(name, data);
    }
    /**
     * 农行批量付款，对私
     */
    public static FileList getABCBatchPayment4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/ABCBatchPayment4Private");
        return new FileList(name, data);
    }
    /**
     * 工行批量付款，对公
     */
//    public static FileList getICBCBatchPayment4Common(String sendSeqNo){
//        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
//        String data = readFile(basePath + "/ICBCBatchPayment4Common");
//        return new FileList(name, data);
//    }
    /**
     * 工行批量付款，对私
     */
    public static FileList getICBCBatchPayment4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
//        String data = readFile(basePath + "/ICBCBatchPayment4Private");
//        1@|$1.00@|$6212262406000426368@|$玷鑫@|$ICBCbatchpayment@|$@|$10000011@|$@|$1.00@|$@|$
//        2@|$1.00@|$6222022406001007134@|$宪隔@|$ICBCbatchpayment@|$@|$10000012@|$@|$1.00@|$@|$
//        3@|$1.00@|$6222082406000465272@|$遇疆@|$ICBCbatchpayment@|$@|$10000013@|$@|$1.00@|$@|$
        List<Object> list = new ArrayList<>();
        list.add(new BatchPaymentFileSelf(
                "1",
                new BigDecimal("10.00"),
                "6212262406000426368",
                "玷鑫",
                "ICBCbatchpayment",
                "50000011",
                new BigDecimal("10.00")));
        list.add(new BatchPaymentFileSelf(
                "2",
                new BigDecimal("10.00"),
                "6222022406001007134",
                "宪隔",
                "ICBCbatchpayment",
                "50000012",
                new BigDecimal("10.00")));
        list.add(new BatchPaymentFileSelf(
                "3",
                new BigDecimal("10.00"),
                "6222082406000465272",
                "遇疆",
                "ICBCbatchpayment",
                "50000013",
                new BigDecimal("10.00")));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }
    /**
     * 建行批量付款，对私
     */
    public static FileList getCCBBatchPayment4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/CCBBatchPayment4Private");

        return new FileList(name, data);
    }
    /**
     * 交行批量付款，对公
     */
    public static FileList getBOCOMBatchPayment4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/BOCOMBatchPayment4Common");
        return new FileList(name, data);
    }
    /**
     * 交行批量付款，对私
     */
    public static FileList getBOCOMBatchPayment4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/BOCOMBatchPayment4Private");
        return new FileList(name, data);
    }
    /**
     * 贵州银行批量付款，对公
     */
    public static FileList getGZBCBatchPayment4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        List<Object> list = new ArrayList<>();
        list.add(new BatchPaymentFileSelf(
                "1",
                new BigDecimal("10.00"),
                "0707001700000046",
                "毕节公积金测试对公账户一",
                "GZBCbatchpayment",
                "50000011",
                new BigDecimal("10.00")));
        list.add(new BatchPaymentFileSelf(
                "2",
                new BigDecimal("10.00"),
                "0707001700000047",
                "毕节公积金测试对公账户二",
                "GZBCbatchpayment",
                "50000012",
                new BigDecimal("10.00")));
        list.add(new BatchPaymentFileSelf(
                "3",
                new BigDecimal("10.00"),
                "0707001700000048",
                "毕节公积金测试对公账户三",
                "GZBCbatchpayment",
                "50000012",
                new BigDecimal("10.00")));

        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }
    /**
     * 贵州银行批量付款，对私
     */
    public static FileList getGZBCBatchPayment4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        List<Object> list = new ArrayList<>();
        list.add(new BatchPaymentFileSelf(
                "1",
                new BigDecimal("10.00"),
                "6214600180002921976",
                "余辛",
                "GZBCbatchpayment",
                "50000011",
                new BigDecimal("10.00")));
        list.add(new BatchPaymentFileSelf(
                "2",
                new BigDecimal("10.00"),
                "6214600180002921984",
                "余堃",
                "GZBCbatchpayment",
                "50000012",
                new BigDecimal("10.00")));

        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }

/*--------------------------------------------分割线-----------------------------------------------*/
    /**
     * 中行批量收款（本行），对公
     */
    public static FileList getBOCBatchCollectionSelf4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
//        String data = readFile(basePath + "/BOCBatchCollectionSelf4Common");
        List<Object> list = new ArrayList<>();
        list.add(new BatchCollectionFileOther(
                "1",
                new BigDecimal("2.00"),
                "182708222217",
                "公积金中心测试账户五",
                "104100000004",
                "batchcollection"
        ));
        list.add(new BatchCollectionFileOther(
                "2",
                new BigDecimal("2.00"),
                "184208222221",
                "公积金中心测试账户六",
                "104100000004",
                "batchcollection"
        ));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }
    /**
     * 中行批量收款（本行），对私
     */
    public static FileList getBOCBatchCollectionSelf4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/BOCBatchCollectionSelf4Private");
        return new FileList(name, data);
    }
    /**
     * 中行批量收款（他行），对公
     */
    public static FileList getBOCBatchCollectionOther4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/BOCBatchCollectionOther4Common");
        return new FileList(name, data);
    }
    /**
     * 中行批量收款（他行），对私
     */
    public static FileList getBOCBatchCollectionOther4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/BOCBatchCollectionOther4Private");
        return new FileList(name, data);
    }
    /**
     * 农行批量收款（本行），对公
     */
    public static FileList getABCBatchCollectionSelf4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
//        String data = readFile(basePath + "/ABCBatchCollectionSelf4Common");
        List<Object> list = new ArrayList<>();
        list.add(new BatchCollectionFileSelf(
                "1",
                new BigDecimal("2.00"),
                "22401001040000245",
                "四淳师盟士首踏珊军准菇承于慊官送",
                "batchcollection"
        ));
        list.add(new BatchCollectionFileSelf(
                "2",
                new BigDecimal("2.00"),
                "22401001040001821",
                "四存华守玲师郁紧寒碎",
                "batchcollection"
        ));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }
    /**
     * 农行批量收款（本行），对私
     */
    public static FileList getABCBatchCollectionSelf4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/ABCBatchCollectionSelf4Private");
        return new FileList(name, data);
    }
    /**
     * 工行批量收款（本行），对私
     */
    public static FileList getICBCBatchCollectionSelf4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/ICBCBatchCollectionSelf4Private");
        return new FileList(name, data);
    }
    /**
     * 建行批量收款（本行），对公
     */
    public static FileList getCCBBatchCollectionSelf4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/CCBBatchCollectionSelf4Common");
        return new FileList(name, data);
    }
    /**
     * 建行批量收款（本行），对私
     */
    public static FileList getCCBBatchCollectionSelf4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/CCBBatchCollectionSelf4Private");
        return new FileList(name, data);
    }
    /**
     * 交行批量收款（本行），对公
     */
    public static FileList getBOCOMBatchCollectionSelf4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/BOCOMBatchCollectionSelf4Common");
        return new FileList(name, data);
    }
    /**
     * 交行批量收款（本行），对私
     */
    public static FileList getBOCOMBatchCollectionSelf4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/BOCOMBatchCollectionSelf4Private");
        return new FileList(name, data);
    }
    /**
     * 贵州批量收款（本行），对公
     */
    public static FileList getGZBCBatchCollectionSelf4Common(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        List<Object> list = new ArrayList<>();
        list.add(new BatchCollectionFileSelf(
                "1",
                new BigDecimal("100000.00"),
                "0707001700000046",
                "毕节公积金测试对公账户一",
                "batchcollection"
        ));
        list.add(new BatchCollectionFileSelf(
                "2",
                new BigDecimal("100000.00"),
                "0707001700000047",
                "毕节公积金测试对公账户二",
                "batchcollection"
        ));
        list.add(new BatchCollectionFileSelf(
                "3",
                new BigDecimal("100000.00"),
                "0707001700000048",
                "毕节公积金测试对公账户三",
                "batchcollection"
        ));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }
    /**
     * 贵州批量收款（本行），对私
     */
    public static FileList getGZBCBatchCollectionSelf4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        List<Object> list = new ArrayList<>();
        list.add(new BatchCollectionFileSelf(
                "1",
                new BigDecimal("50000.00"),
                "6214600180002921976",
                "余辛",
                "batchcollection"
        ));
        list.add(new BatchCollectionFileSelf(
                "2",
                new BigDecimal("500000.00"),
                "6214600180002921984",
                "余堃",
                "batchcollection"
        ));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }

/*--------------------------------------------分割线-----------------------------------------------*/
    /**
     * 中行贷款扣款，对私
     */
    public static FileList getBOCLoanDeduction4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
//        String data = readFile(basePath + "/BOCLoanDeduction4Private");
        List<Object> list = new ArrayList<>();
        list.add(new LoanDeductionFileOther(
                "1",
                new BigDecimal("5.00"),
                "176708222268",
                "王小一",
                "104362004010",
                "0",
                "LoanDeduction"
        ));
        list.add(new LoanDeductionFileOther(
                "2",
                new BigDecimal("5.00"),
                "182708222228",
                "王小一",
                "104362004010",
                "0",
                "LoanDeduction"
        ));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }
    /**
     * 农行贷款扣款，对私
     */
    public static FileList getABCLoanDeduction4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/ABCLoanDeduction4Private");
        return new FileList(name, data);
    }
    /**
     * 工行贷款扣款，对私
     */
    public static FileList getICBCLoanDeduction4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/ICBCLoanDeduction4Private");
        return new FileList(name, data);
    }
    /**
     * 建行贷款扣款，对私
     */
    public static FileList getCCBLoanDeduction4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        String data = readFile(basePath + "/CCBLoanDeduction4Private");
        return new FileList(name, data);
    }
    /**
     * 交行贷款扣款，对私
     */
    public static FileList getBOCOMLoanDeduction4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
//        String data = readFile(basePath + "/BOCOMLoanDeduction4Private");
        List<Object> list = new ArrayList<>();
        list.add(new LoanDeductionFileSelf(
                "1",
                new BigDecimal("5.00"),
                "310972700000300477202",
                "段丽鹏",
                "0",
                "LoanDeduction"
        ));
        list.add(new LoanDeductionFileSelf(
                "2",
                new BigDecimal("5.00"),
                "310972090000300338202",
                "李合宽",
                "0",
                "LoanDeduction"
        ));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }
    /**
     * 贵州银行贷款扣款，对私
     */
    public static FileList getGZBCLoanDeduction4Private(String sendSeqNo){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo+ ".DAT";
        List<Object> list = new ArrayList<>();
        list.add(new LoanDeductionFileSelf(
                "1",
                new BigDecimal("5.00"),
                "6214600180002921976",
                "余辛",
                "0",
                "LoanDeduction"
        ));
        list.add(new LoanDeductionFileSelf(
                "2",
                new BigDecimal("5.00"),
                "6214600180002921984",
                "余堃",
                "0",
                "LoanDeduction"
        ));
        String data = TransactionFileFactory.getFileContent(list);
        return new FileList(name, data);
    }



    private static String readFile(String fileName){
        String encoding = "GBK";
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
}

package com.handge.housingfund.bank.testapi.consumer;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.util.JAXBUtil;
import com.handge.housingfund.common.service.util.Sender;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.common.service.util.TransactionFileFactory;
import com.tienon.util.FileFieldConv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class SendNotice {
    private static Logger logger = LogManager.getLogger(SendNotice.class);

    public static void main(String[] args) throws Exception {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String, String>> list = null;
        String cellData = null;
        String filePath = "/home/gxy/project/公积金/notice.xlsx";
//        String columns[] = {"序号", "账号", "交易对手账号", "交易对手户名", "金额", "交易日期", "备注", "摘要"};
        String columns[] = {"序号", "账号", "银行主机流水号", "交易代码", "交易对手账号", "交易对手户名", "金额", "交易日期", "交易时间", "可用金额", "开户机构号", "备注", "币种", "钞汇", "余额", "可透支余额", "凭证种类", "凭证号码", "交易对手行号", "摘要", "冲正标识", "笔号", "册号"};
        wb = readExcel(filePath);
        if (wb != null) {
            //用来存放表中数据
            list = new ArrayList<Map<String, String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            System.out.println("行数: " + rownum);
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            System.out.println("列数: " + colnum);
            for (int i = 1; i < rownum; i++) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colnum; j++) {
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        }

        sendNotice(list);
    }

    //读取excel
    private static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    private static Object getCellFormatValue(Cell cell) {
        DecimalFormat df = new DecimalFormat("0");
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = df.format(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = df.format(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    private static void sendNotice(List<Map<String, String>> list) throws Exception {
        //遍历解析出来的list
        for (Map<String, String> map : list) {
            System.out.println(map);
            String[] datetime = com.handge.housingfund.common.service.util.DateUtil.getDatetime();
            AccChangeNoticeFile accChangeNoticeFile = new AccChangeNoticeFile();
            accChangeNoticeFile.setNo(map.get("序号"));
            accChangeNoticeFile.setAcct(map.get("账号"));
            accChangeNoticeFile.setHostSeqNo("52240" + System.currentTimeMillis());
//            accChangeNoticeFile.setTxCode(map.get("交易代码"));
            accChangeNoticeFile.setOpponentAcct(map.get("交易对手账号"));
            accChangeNoticeFile.setOpponentName(map.get("交易对手户名"));
            accChangeNoticeFile.setAmt(new BigDecimal(map.get("金额")));
            accChangeNoticeFile.setDate(map.get("交易日期"));
            accChangeNoticeFile.setTime(datetime[1]);
            accChangeNoticeFile.setAvailableAmt(StringUtil.notEmpty(map.get("可用金额")) ? new BigDecimal(map.get("可用金额")) : null);
            accChangeNoticeFile.setOpenBankNo(map.get("开户机构号"));
            accChangeNoticeFile.setRemark(map.get("备注"));
            accChangeNoticeFile.setCurrIden("1");
            accChangeNoticeFile.setCurrNo("156");
            accChangeNoticeFile.setBalance(new BigDecimal(100000000));
            accChangeNoticeFile.setBalance(StringUtil.notEmpty(map.get("余额")) ? new BigDecimal(map.get("余额")) : null);
            accChangeNoticeFile.setOverdraft(StringUtil.notEmpty(map.get("可透支余额")) ? new BigDecimal(map.get("可透支余额")) : null);
            accChangeNoticeFile.setVoucherType(map.get("凭证种类"));
            accChangeNoticeFile.setVoucherNo(map.get("凭证号码"));
            accChangeNoticeFile.setOpponentBankNo(map.get("交易对手行号"));
            accChangeNoticeFile.setSummary(map.get("摘要"));
            accChangeNoticeFile.setRedo(map.get("冲正标识"));
            accChangeNoticeFile.setBookNo(map.get("笔号"));
            accChangeNoticeFile.setBookListNo(map.get("册号"));


            String data = FileFieldConv.fieldASCtoBCD(TransactionFileFactory.getFileContent(Arrays.asList(accChangeNoticeFile)), "GBK");

            String xml1 = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                    "<message>\n" +
                    "<head>\n" +
                    "<field name=\"SendDate\">" + map.get("交易日期") + "</field>\n" +
                    "<field name=\"SendTime\">" + datetime[1] + "</field>\n" +
                    "<field name=\"SendSeqNo\">52240"+ map.get("交易日期") +"0"+ datetime[1] +"</field>\n" +
                    "<field name=\"SendNode\">D00000</field>\n" +
                    "<field name=\"TxCode\">SBDC100</field>\n" +
                    "<field name=\"ReceiveNode\">C52240</field>\n" +
                    "<field name=\"BDCDate\">" + map.get("交易日期") + "</field>\n" +
                    "<field name=\"BDCTime\">" + datetime[1] + "</field>\n" +
                    "<field name=\"BDCSeqNo\">52240"+ map.get("交易日期") +"0"+ datetime[1] +"</field>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<field-list name=\"FILE_LIST\">\n" +
                    "<field-list name=\"0\">\n" +
                    "<field name=\"DATA\">" + data + "</field>\n" +
                    "<field name=\"NAME\">BDC_BAL_NTF_" + map.get("交易日期") + "_" + datetime[1] + ".act</field>\n" +
                    "</field-list>\n" +
                    "</field-list>\n" +
                    "</body>\n" +
                    "</message>";
            logger.info("发送到公积金中心的报文:\n" + JAXBUtil.formatXml(xml1, "send"));
            Sender sender = new Sender();
            String respXml = sender.invoke2(xml1);
            logger.info("从公积金中心收到的报文:\n" + JAXBUtil.formatXml(respXml, "receive"));
        }
    }
}

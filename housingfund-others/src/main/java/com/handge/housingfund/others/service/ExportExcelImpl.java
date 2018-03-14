package com.handge.housingfund.others.service;

import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.model.InventoryDetail;
import com.handge.housingfund.common.service.collection.model.deposit.ExportInventoryConfirmationInfoRes;
import com.handge.housingfund.common.service.collection.model.deposit.GetPersonRadixBeforeResJCJSTZXX;
import com.handge.housingfund.common.service.collection.model.individual.PersonRadixExcelRes;
import com.handge.housingfund.common.service.collection.model.unit.UnitEmployeeExcelRes;
import com.handge.housingfund.common.service.collection.service.unitdeposit.PersonRadix;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositInventory;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctInfo;
import com.handge.housingfund.common.service.loan.ILoanAccountService;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.PaymentHistoryDataRes;
import com.handge.housingfund.common.service.others.IExportExcelService;
import com.handge.housingfund.common.service.others.model.FileType;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.FileUtil;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.ICFileDAO;
import com.handge.housingfund.database.dao.ICRoleDAO;
import com.handge.housingfund.database.entities.CFile;
import com.handge.housingfund.database.entities.CPermission;
import com.handge.housingfund.database.entities.CRole;
import org.apache.commons.configuration2.Configuration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class ExportExcelImpl<T> implements IExportExcelService {
    @Autowired
    private PersonRadix personRadix;
    @Autowired
    private UnitDepositInventory unitDepositInventory;
    @Autowired
    private UnitAcctInfo unitAcctInfo;
    @Autowired
    ICFileDAO fileDAO;
    @Autowired
    ICRoleDAO iRoleDAO;
    @Autowired
    private ILoanAccountService iLoanAccountService;

    static Configuration config = Configure.getInstance().getConfiguration("pdf");
    String BASEPATH = config.getString("file_path");
    String TEMPLATE = BASEPATH + config.getString("pdf_template") + "/";
    String REVIEWOUTPATH = config.getString("review_out_path");

    /**
     * 读取excel模板，并复制到新文件中供写入和下载
     *
     * @return
     */
    public File createNewFile(String id,String Name) {
        // 读取模板，并赋值到新文件****
        // 文件模板路径
        String path = TEMPLATE + Name+".xlsx";
        File file = new File(path);
        // 保存文件的路径
        String realPath = BASEPATH + REVIEWOUTPATH;
        // 新的文件名
        String newFileName = null;
        if(id!=null){
             newFileName = Name + id + ".xlsx";
        }else{
             newFileName = Name  + ".xlsx";
        }
        // 判断路径是否存在
        File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 写入到新的excel
        File newFile = new File(realPath, newFileName);
        try {
            newFile.createNewFile();
            // 复制模板到新文件
            fileChannelCopy(file, newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }
    /**
     * 检查角色权限
     */
    public void checkHavePermission(TokenContext tokenContext,String permissionName){
        boolean havePermission = false;
        CRole role = iRoleDAO.get(tokenContext.getRoleList().get(0));
        if (role == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有操作权限,请分配权限");
        for (CPermission permission : role.getPermissions()) {
            if(permission.getPermission_name().equals(permissionName)){
                havePermission = true;
            }
        }
        if(havePermission==false){
            throw new ErrorException(ReturnEnumeration.User_Defined, "没有操作权限,请分配权限");
        }
    }
    /**
     * 复制文件
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */

    public void fileChannelCopy(File s, File t) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(s), 1024);
                out = new BufferedOutputStream(new FileOutputStream(t), 1024);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** */
    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            oldfile.renameTo(newfile);
        }
    }
    /**
     * 生成个人基数调整excel
     */
    public String exportPersonRadixExcel(String DWZH, String newFileName, PersonRadixExcelRes personRadixExcelRes, Integer column) {
        File newFile = createNewFile(DWZH,"personRadix");
        // 新文件写入数据****
        InputStream is = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        try {
            is = new FileInputStream(newFile);// 将excel文件转为输入流
            workbook = new XSSFWorkbook(is);// 创建个workbook，
            // 获取第一个sheet
            sheet = workbook.getSheetAt(0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //sheet.protectSheet("131415");
        if (sheet != null) {
            try {
                // 写数据
                FileOutputStream fos = new FileOutputStream(newFile);
                ArrayList<GetPersonRadixBeforeResJCJSTZXX> arrayList = personRadixExcelRes.getJCJSTZXX();
                int g = personRadixExcelRes.getJCJSTZXX().size() + 2;
                // 创建字体对象
                Font ztFont = workbook.createFont();
                Font ztFont_t = workbook.createFont();
                ztFont.setFontHeightInPoints((short) 12);
                ztFont.setFontName("宋体");
                XSSFCellStyle ztStyle = (XSSFCellStyle) workbook.createCellStyle();
                XSSFCellStyle ztStyle_t = (XSSFCellStyle) workbook.createCellStyle();
                XSSFCellStyle ztStyle_tt = workbook.createCellStyle();
                XSSFDataFormat format = workbook.createDataFormat();
                XSSFDataFormat format_tt= workbook.createDataFormat();

                ztStyle_tt.setDataFormat(format_tt.getFormat("yyyy/m"));
                ztStyle.setDataFormat(format.getFormat("@"));

                ztStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle.setBorderRight(CellStyle.BORDER_THIN);
                ztStyle_t.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle_t.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle_t.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderRight(CellStyle.BORDER_THIN);
                ztStyle_tt.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle_tt.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle_tt.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle_tt.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle_tt.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle_tt.setBorderRight(CellStyle.BORDER_THIN);

                Row ztRow = null;
                Cell ztCell = null;
                for (int m = 1; m < g; m++) {
                    if (m == 1) {
                        ztRow = sheet.createRow((short) 2);
                    } else {
                        ztRow = sheet.createRow((short) m + 2);
                    }
                    for (int i = 0; i < 9; i++) {
                        // 创建单元格样式对象
                        ztCell = ztRow.createCell(i);
                        if (m == 1) {
                            switch (i) {
                                case 1:
                                    ztCell.setCellValue(personRadixExcelRes.getDWGJXX().getDWMC());
                                    break;
                                case 5:
                                    ztCell.setCellValue(personRadixExcelRes.getDWGJXX().getDWZH());
                                    break;
                                case 8:
                                    ztCell.setCellValue(personRadixExcelRes.getSXNY());
                                    break;
                                case 0:
                                    ztCell.setCellValue("单位名称");
                                    break;
                                case 4:
                                    ztCell.setCellValue("单位账号");
                                    break;
                                case 7:
                                    ztCell.setCellValue("生效年月");
                                    break;
                            }
                            if(i==0||i==2||i==5){
                                ztFont_t.setBoldweight(Font.BOLDWEIGHT_BOLD);
                                ztStyle_t.setFont(ztFont_t); // 将字体应用到样式上面
                                ztCell.setCellStyle(ztStyle_t);
                            }else{
                                if(i==6||i==7){
                                    ztStyle_tt.setFont(ztFont);
                                    ztCell.setCellStyle(ztStyle_tt);
                                }else{
                                    ztStyle.setFont(ztFont); // 将字体应用到样式上面
                                    ztCell.setCellStyle(ztStyle);
                                }
                            }
                        } else {
                            switch (i) {
                                case 0:
                                    ztCell.setCellValue(m - 1);
                                    break;
                                case 1:
                                    ztCell.setCellValue(arrayList.get(m - 2).getGRZH());
                                    break;
                                case 2:
                                    ztCell.setCellValue(arrayList.get(m - 2).getXingMing());
                                    break;
                                case 3:
                                    ztCell.setCellValue(arrayList.get(m - 2).getZJHM());
                                    break;
                                case 4:
                                    ztCell.setCellValue(arrayList.get(m - 2).getDWYJCE());
                                    break;
                                case 5:
                                    ztCell.setCellValue(arrayList.get(m - 2).getGRYJCE());
                                    break;
                                case 6:
                                    ztCell.setCellValue(arrayList.get(m - 2).getYJCE());
                                    break;
                                case 7:
                                    ztCell.setCellValue(arrayList.get(m - 2).getTZQGRJCJS().toString());
                                    break;
                                case 8:
                                    ztCell.setCellValue("");
                                    break;
                            }
                            ztStyle.setFont(ztFont); // 将字体应用到样式上面
                            ztCell.setCellStyle(ztStyle);
                        }
                        ztRow.setHeightInPoints(15);
                    }
                }
                CFile cFile = new CFile();
                cFile.setCount(new BigDecimal(0));
                cFile.setName("毕节市住房公积金缴存基数调整表--" + DWZH);
                cFile.setPath(REVIEWOUTPATH);
                String path = BASEPATH + REVIEWOUTPATH;
                cFile.setType(FileType.XLSX);
                cFile.setSize(new BigDecimal(0));
                cFile.setSHA1(FileUtil.getCheckCode(path + "/" + newFileName, "SHA-1"));
                cFile.setSize(FileUtil.getFileSize(path + "/" + newFileName));
                String id = fileDAO.save(cFile);
                workbook.write(fos);
                fos.flush();
                fos.close();
                String oldname = "personRadix" + DWZH + ".xlsx";
                String newname = id;
                renameFile(path, oldname, newname);
                return id;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public String exportInventoryExcel(String dwzh, String newFileName, ExportInventoryConfirmationInfoRes Inventorydata,int k){
        File newFile = createNewFile(dwzh,"InventoryConfirmationTemp");
        // 新文件写入数据****
        InputStream is = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        try {
            is = new FileInputStream(newFile);// 将excel文件转为输入流
            workbook = new XSSFWorkbook(is);// 创建个workbook，
            // 获取第一个sheet
            sheet = workbook.getSheetAt(0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //sheet.protectSheet("131415");
        if (sheet != null) {
            try {
                // 写数据
                FileOutputStream fos = new FileOutputStream(newFile);
                ArrayList<InventoryDetail> arrayList = Inventorydata.getInventoryMessage().getQCXQ();
                int g = arrayList.size() +6;
                // 创建字体对象
                Font ztFont = workbook.createFont();
                Font ztFont_t = workbook.createFont();
                ztFont.setFontHeightInPoints((short) 12);
                ztFont.setFontName("宋体");
                XSSFCellStyle ztStyle = (XSSFCellStyle) workbook.createCellStyle();
                XSSFCellStyle ztStyle_t = (XSSFCellStyle) workbook.createCellStyle();
                XSSFCellStyle ztStyle_tt = workbook.createCellStyle();
                XSSFDataFormat format = workbook.createDataFormat();

                ztStyle.setDataFormat(format.getFormat("@"));
                ztStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle.setBorderRight(CellStyle.BORDER_THIN);
                ztStyle_t.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle_t.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle_t.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderRight(CellStyle.BORDER_THIN);
                Row ztRow = null;
                Cell ztCell = null;
                for (int m = 2; m < g; m++) {
                    if(m!=5){
                        ztRow = sheet.createRow((short) m);
                    }
                    for (int i = 0; i < 11; i++) {
                        ztCell = ztRow.createCell(i);
                        if (m == 2) {
                            switch (i) {
                                case 0:
                                    ztCell.setCellValue("业务网点");
                                    break;
                                case 5:
                                    ztCell.setCellValue("填制日期");
                                    break;
                                case 8:
                                    ztCell.setCellValue("验证码");
                                    break;
                                case 2:
                                    ztCell.setCellValue(Inventorydata.getYWWD());
                                    break;
                                case 6:
                                    ztCell.setCellValue(Inventorydata.getTZSJ());
                                    break;
                                case 9:
                                    UUID uuid = UUID.randomUUID();
                                    ztCell.setCellValue(uuid.toString().trim().replaceAll("-", ""));
                                    break;
                            }
                            if(i==0||i==5||i==8){
                                ztFont_t.setBoldweight(Font.BOLDWEIGHT_BOLD);
                                ztStyle_t.setFont(ztFont_t); // 将字体应用到样式上面
                                ztCell.setCellStyle(ztStyle_t);
                            }else{
                                ztStyle.setFont(ztFont); // 将字体应用到样式上面
                                ztCell.setCellStyle(ztStyle);
                            }
                        }
                        if(m == 3){
                            switch (i) {
                                case 0:
                                    ztCell.setCellValue("账户信息");
                                    break;
                                case 1:
                                    ztCell.setCellValue("单位名称");
                                    break;
                                case 5:
                                    ztCell.setCellValue("单位账号");
                                    break;
                                case 8:
                                    ztCell.setCellValue("清册年月");
                                    break;
                                case 2:
                                    ztCell.setCellValue(Inventorydata.getDWMC());
                                    break;
                                case 6:
                                    ztCell.setCellValue(Inventorydata.getDWZH());
                                    break;
                                case 9:
                                    ztCell.setCellValue(Inventorydata.getQCNY());
                                    break;
                            }
                            if(i==0||i==5||i==1||i==8){
                                ztFont_t.setBoldweight(Font.BOLDWEIGHT_BOLD);
                                ztStyle_t.setFont(ztFont_t); // 将字体应用到样式上面
                                ztCell.setCellStyle(ztStyle_t);
                            }else{
                                ztStyle.setFont(ztFont); // 将字体应用到样式上面
                                ztCell.setCellStyle(ztStyle);
                            }
                        }
                        if(m == 4){
                            switch (i) {
                                case 1:
                                    ztCell.setCellValue("发生人数");
                                    break;
                                case 5:
                                    ztCell.setCellValue("发生额");
                                    break;
                                case 2:
                                    ztCell.setCellValue(Inventorydata.getFSRS());
                                    break;
                                case 6:
                                    ztCell.setCellValue(Inventorydata.getFSE());
                                    break;
                            }
                            if(i==5||i==1){
                                ztFont_t.setBoldweight(Font.BOLDWEIGHT_BOLD);
                                ztStyle_t.setFont(ztFont_t); // 将字体应用到样式上面
                                ztCell.setCellStyle(ztStyle_t);
                            }else{
                                ztStyle.setFont(ztFont); // 将字体应用到样式上面
                                ztCell.setCellStyle(ztStyle);
                            }
                        }
                        if(m==5){break;}
                        if(m >=6){
                            switch (i) {
                                case 0:
                                    ztCell.setCellValue(m - 5);
                                    break;
                                case 1:
                                    ztCell.setCellValue(arrayList.get(m -6).getGRZH());
                                    break;
                                case 2:
                                    ztCell.setCellValue(arrayList.get(m - 6).getXingMing());
                                    break;
                                case 3:
                                    ztCell.setCellValue(arrayList.get(m - 6).getZJHM());
                                    break;
                                case 4:
                                    ztCell.setCellValue(arrayList.get(m - 6).getGRJCJS());
                                    break;
                                case 5:
                                    ztCell.setCellValue(arrayList.get(m - 6).getDWYJCE());
                                    break;
                                case 6:
                                    ztCell.setCellValue(arrayList.get(m - 6).getGRYJCE());
                                    break;
                                case 7:
                                    ztCell.setCellValue(arrayList.get(m - 6).getYJCE());
                                    break;
                                case 8:
                                    ztCell.setCellValue(arrayList.get(m - 6).getQCFSE());
                                    break;
                                case 9:
                                    if(arrayList.get(m - 6).getGRZHZT()!=null){
                                        ztCell.setCellValue(PersonAccountStatus.getNameByCode(arrayList.get(m - 6).getGRZHZT()));
                                    }
                                    break;
                                case 10:
                                    ztCell.setCellValue(arrayList.get(m - 6).getGRZHYE());
                                    break;
                            }
                            ztStyle.setFont(ztFont); // 将字体应用到样式上面
                            ztCell.setCellStyle(ztStyle);
                        }
                    }

                    ztRow.setHeightInPoints(15);

                }
                CFile cFile = new CFile();
                cFile.setCount(new BigDecimal(0));
                cFile.setName("毕节市住房公积金清册确认单" + dwzh+"年月"+Inventorydata.getQCNY());
                cFile.setPath(REVIEWOUTPATH);
                String path = BASEPATH + REVIEWOUTPATH;
                cFile.setType(FileType.XLSX);
                cFile.setSize(new BigDecimal(0));
                cFile.setSHA1(FileUtil.getCheckCode(path + "/" + newFileName, "SHA-1"));
                cFile.setSize(FileUtil.getFileSize(path + "/" + newFileName));
                String id = fileDAO.save(cFile);
                workbook.write(fos);
                fos.flush();
                fos.close();
                String oldname = "InventoryConfirmationTemp" + dwzh+ ".xlsx";
                String newname = id;
                renameFile(path, oldname, newname);
                return id;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /*
     *  生成职工列表excel
     */
    public String exportEmployeeExcel(String dwzh, String newFileName,  UnitEmployeeExcelRes employeeListData, Integer k){
        File newFile = createNewFile(dwzh,"EmployeeListTemp");
        // 新文件写入数据****
        InputStream is = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        try {
            is = new FileInputStream(newFile);// 将excel文件转为输入流
            workbook = new XSSFWorkbook(is);// 创建个workbook，
            // 获取第一个sheet
            sheet = workbook.getSheetAt(0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //sheet.protectSheet("131415");
        if (sheet != null) {
            try {
                // 写数据
                FileOutputStream fos = new FileOutputStream(newFile);
                ArrayList arrayList = new ArrayList();
                int g = employeeListData.getListEmployeeRes().size() +4;
                // 创建字体对象
                Font ztFont = workbook.createFont();
                ztFont.setFontHeightInPoints((short) 12);
                Font ztFont_t = workbook.createFont();
                ztFont_t.setFontHeightInPoints((short) 12);
                ztFont.setFontName("宋体");
                XSSFCellStyle ztStyle = (XSSFCellStyle) workbook.createCellStyle();
                XSSFCellStyle ztStyle_t = (XSSFCellStyle) workbook.createCellStyle();
                XSSFDataFormat format = workbook.createDataFormat();

                ztStyle.setDataFormat(format.getFormat("@"));
                ztStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle.setBorderRight(CellStyle.BORDER_THIN);
                ztStyle_t.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle_t.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle_t.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle_t.setBorderRight(CellStyle.BORDER_THIN);

                Row ztRow = null;
                Cell ztCell = null;
                for (int m = 2; m < g; m++) {
                    if(m==3){
                    }else {
                        ztRow = sheet.createRow((short) m);
                        for (int i = 0; i < k; i++) {
                            ztCell = ztRow.createCell(i);
                            if (m == 2) {
                                switch (i) {
                                    case 0:
                                        ztCell.setCellValue("单位名称");
                                        break;
                                    case 5:
                                        ztCell.setCellValue("单位账号");
                                        break;
                                    case 2:
                                        ztCell.setCellValue(employeeListData.getDWMC()); //单位名称的值2
                                        break;
                                    case 7:
                                        ztCell.setCellValue(employeeListData.getDWZH());//单位账号的值7
                                        break;
                                }
                                if (i == 0 || i == 1 || i == 5 || i == 6) {
                                    ztFont_t.setBoldweight(Font.BOLDWEIGHT_BOLD);
                                    ztStyle_t.setFont(ztFont_t); // 将字体应用到样式上面
                                    ztCell.setCellStyle(ztStyle_t);
                                } else {
                                    ztStyle.setFont(ztFont); // 将字体应用到样式上面
                                    ztCell.setCellStyle(ztStyle);
                                }
                            }
                            if (m > 3) {
                                switch (i) {
                                    case 0:
                                        ztCell.setCellValue(m - 3);
                                        break;
                                    case 1:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getGRZH());
                                        //个人账号
                                        break;
                                    case 2:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getXingMing());
                                        //姓名
                                        break;
                                    case 3:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getZJHM());
                                       //证件号码
                                        break;
                                    case 4:
                                        ztCell.setCellValue(PersonAccountStatus.getNameByCode(employeeListData.getListEmployeeRes().get(m-4).getGRZHZT()));
                                       //个人账户状态
                                        break;
                                    case 5:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getGRJCJS());
                                       //个人缴存基数
                                        break;
                                    case 6:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getGRYJCE());
                                       //个人月缴存额
                                        break;
                                    case 7:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getDWYJCE());
                                        //单位月缴存额
                                        break;
                                    case 8:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getYJCE());
                                        //月缴存额
                                        break;
                                    case 9:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getJZNY());
                                        //缴至年月
                                        break;
                                    case 10:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getGRZHYE());
                                         //个人账户余额
                                        break;
                                    case 11:
                                        ztCell.setCellValue(employeeListData.getListEmployeeRes().get(m-4).getSFDK());
                                         //是否贷款
                                        break;
                                }
                                ztStyle.setFont(ztFont); // 将字体应用到样式上面
                                ztCell.setCellStyle(ztStyle);
                            }
                        }
                        ztRow.setHeightInPoints(15);
                    }
                }
                CFile cFile = new CFile();
                cFile.setCount(new BigDecimal(0));
                cFile.setName("毕节市住房公积金职工列表" + dwzh);
                cFile.setPath(REVIEWOUTPATH);
                String path = BASEPATH + REVIEWOUTPATH;
                cFile.setType(FileType.XLSX);
                cFile.setSize(new BigDecimal(0));
                cFile.setSHA1(FileUtil.getCheckCode(path + "/" + newFileName, "SHA-1"));
                cFile.setSize(FileUtil.getFileSize(path + "/" + newFileName));
                String id = fileDAO.save(cFile);
                workbook.write(fos);
                fos.flush();
                fos.close();
                String oldname = "EmployeeListTemp" + dwzh+ ".xlsx";
                String newname = id;
                renameFile(path, oldname, newname);
                return id;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public String exportAnnualReport(String code, String newFileName, UnitEmployeeExcelRes employeeListData, Integer column) {
        File newFile = createNewFile(code,"AnnualReportTemp");
        // 新文件写入数据****
        InputStream is = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        try {
            is = new FileInputStream(newFile);// 将excel文件转为输入流
            workbook = new XSSFWorkbook(is);// 创建个workbook，
            sheet = workbook.getSheetAt(0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //sheet.protectSheet("131415");
        if (sheet != null) {
            try {
                // 写数据
                FileOutputStream fos = new FileOutputStream(newFile);
                ArrayList arrayList = new ArrayList();
                int g = 654;
                // 创建字体对象
                Font ztFont = workbook.createFont();
                ztFont.setFontHeightInPoints((short) 10);
                Font ztFont_t = workbook.createFont();
                ztFont_t.setFontHeightInPoints((short) 10);
                ztFont.setFontName("宋体");
                XSSFCellStyle ztStyle = (XSSFCellStyle) workbook.createCellStyle();
                XSSFDataFormat format = workbook.createDataFormat();
                ztStyle.setDataFormat(format.getFormat("@"));
                ztStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle.setBorderRight(CellStyle.BORDER_THIN);
                Cell ztCell = null;
                ArrayList codeArray = new ArrayList();
                for (int m = 9; m < g; m++) {
                    if (m == 8) {
                    } else {
                        ztCell = sheet.getRow(m).getCell(5);
                        ztCell.setCellValue("10000"); //单位名称的值2
                        ztStyle.setFont(ztFont); // 将字体应用到样式上面
                        codeArray.add(sheet.getRow(m).getCell(3).getStringCellValue());
                        ztStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                        ztStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                        ztCell.setCellStyle(ztStyle);
                        sheet.getRow(m).setHeightInPoints(15);
                        }
                }
                CFile cFile = new CFile();
                cFile.setCount(new BigDecimal(0));
                cFile.setName("毕节市住房公积金职工列表" + code);
                cFile.setPath(REVIEWOUTPATH);
                String path = BASEPATH + REVIEWOUTPATH;
                cFile.setType(FileType.XLSX);
                cFile.setSize(new BigDecimal(0));
                cFile.setSHA1(FileUtil.getCheckCode(path + "/" + newFileName, "SHA-1"));
                cFile.setSize(FileUtil.getFileSize(path + "/" + newFileName));
                String id = fileDAO.save(cFile);
                workbook.write(fos);
                fos.flush();
                fos.close();
                String oldname = "AnnualReportTemp.xlsx";
                String newname = id;
                renameFile(path, oldname, newname);
                return id;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public String exportPaymentHistoryRecord(String dkzh, String newFileName,PaymentHistoryDataRes paymentHistoryDataRes, int column) {
        File newFile = createNewFile(dkzh,"PaymentHistoryTemp");
        // 新文件写入数据****
        InputStream is = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        try {
            is = new FileInputStream(newFile);// 将excel文件转为输入流
            workbook = new XSSFWorkbook(is);// 创建个workbook，
            sheet = workbook.getSheetAt(0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //sheet.protectSheet("131415");
        if (sheet != null) {
            try {
                // 写数据
                FileOutputStream fos = new FileOutputStream(newFile);
                ArrayList arrayList = new ArrayList();
                int g = paymentHistoryDataRes.getHousingRecordListGetInformations().size()+6;
                // 创建字体对象
                Font ztFont = workbook.createFont();
                ztFont.setFontHeightInPoints((short) 10);
                ztFont.setFontName("宋体");
                XSSFCellStyle ztStyle = (XSSFCellStyle) workbook.createCellStyle();
                XSSFDataFormat format = workbook.createDataFormat();
                ztStyle.setDataFormat(format.getFormat("@"));
                ztStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                ztStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
                ztStyle.setBorderBottom(CellStyle.BORDER_THIN);
                ztStyle.setBorderTop(CellStyle.BORDER_THIN);
                ztStyle.setBorderLeft(CellStyle.BORDER_THIN);
                ztStyle.setBorderRight(CellStyle.BORDER_THIN);
                Cell ztCell = null;
                for (int m = 2; m < g; m++) {
                    if (m == 5) {
                    } else {
                        for (int i = 0; i < column; i++) {
                            if(m==2) {
                                switch (i) {
                                    case 2:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getYWWD());
                                        break;
                                    case 6:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date now = new Date();
                                        ztCell.setCellValue(sdf.format(now)); //单位名称的值2
                                        break;
                                }
                            }
                            if(m==3){
                                switch (i) {
                                    case 2:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getJKRXM());
                                        break;
                                    case 6:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getDKZH());
                                        break;
                                }
                            }
                            if(m==4){
                                switch (i) {
                                    case 2:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getJKRZJLX());
                                        break;
                                    case 6:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getJKRZJHM());
                                        break;
                                }
                            }
                            if(m>5){
                                switch (i) {
                                    case 0:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(m-5);
                                        break;
                                    case 1:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getYWLSH());
                                        break;
                                    case 2:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getDKYWMXLX());
                                        break;
                                    case 3:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getYWFSRQ());
                                        break;
                                    case 4:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getFSE());
                                        break;
                                    case 5:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getBJJE());
                                        break;
                                    case 6:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getDKYE());
                                        break;
                                    case 7:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getLXJE());
                                        break;
                                    case 8:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getFXJE());
                                        break;
                                    case 9:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getDQQC());
                                        break;
                                    case 10:
                                        ztCell = sheet.getRow(m).getCell(i);
                                        ztCell.setCellValue(paymentHistoryDataRes.getHousingRecordListGetInformations().get(m-6).getJZRQ());
                                        break;
                                }
                            }
                        }
                        ztStyle.setFont(ztFont); // 将字体应用到样式上面
                        ztCell.setCellStyle(ztStyle);
                        sheet.getRow(m).setHeightInPoints(15);
                    }
                }
                CFile cFile = new CFile();
                cFile.setCount(new BigDecimal(0));
                cFile.setName("毕节市住房公积金还款记录" + dkzh);
                cFile.setPath(REVIEWOUTPATH);
                String path = BASEPATH + REVIEWOUTPATH;
                cFile.setType(FileType.XLSX);
                cFile.setSize(new BigDecimal(0));
                cFile.setSHA1(FileUtil.getCheckCode(path + "/" + newFileName, "SHA-1"));
                cFile.setSize(FileUtil.getFileSize(path + "/" + newFileName));
                String id = fileDAO.save(cFile);
                workbook.write(fos);
                fos.flush();
                fos.close();
                String oldname = "PaymentHistoryTemp"+dkzh+".xlsx";
                String newname = id;
                renameFile(path, oldname, newname);
                return id;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public CommonResponses getExceldata(TokenContext tokenContext, String DWZH,String sxny) {
        checkHavePermission(tokenContext,"导出个人基数调整");
        String newFileName = "personRadix" + DWZH + ".xlsx";
        PersonRadixExcelRes personRadixExcelRes = personRadix.getPersonRadixdata(tokenContext, DWZH,sxny);
        String id = exportPersonRadixExcel(DWZH, newFileName, personRadixExcelRes, 7);
        CommonResponses commonResponses = new CommonResponses();
        if (id != null) {
            commonResponses.setState("Success");
            commonResponses.setId(id);
        } else {
            commonResponses.setState("Fail");
            commonResponses.setId("");
        }
        return commonResponses;
    }
    public CommonResponses getInventoryData(TokenContext tokenContext, String dwzh,String qcny) {
        checkHavePermission(tokenContext,"导出清册确认单");
        String newFileName = "InventoryConfirmationTemp" + dwzh + ".xlsx";
        ExportInventoryConfirmationInfoRes Inventorydata = unitDepositInventory.getExportInventoryConfirmationInfo(dwzh,qcny);
        String id = exportInventoryExcel(dwzh, newFileName, Inventorydata, 7);
        CommonResponses commonResponses = new CommonResponses();
        if (id != null) {
            commonResponses.setState("Success");
            commonResponses.setId(id);
        } else {
            commonResponses.setState("Fail");
            commonResponses.setId("");
        }
        return commonResponses;
    }
    public CommonResponses getemployeeListData(TokenContext tokenContext, String dwzh){
        checkHavePermission(tokenContext,"导出职工列表");
        String newFileName = "EmployeeListTemp" + dwzh + ".xlsx";
        UnitEmployeeExcelRes employeeListData = unitAcctInfo.getEmployeeAllData(dwzh);
        Integer column = null;//列
        column = 12;
        String id = exportEmployeeExcel(dwzh, newFileName, employeeListData, column);
        CommonResponses commonResponses = new CommonResponses();
        if (id != null) {
            commonResponses.setState("Success");
            commonResponses.setId(id);
        } else {
            commonResponses.setState("Fail");
            commonResponses.setId("");
        }
        return commonResponses;
    }
    public CommonResponses getAnnualReportData(TokenContext tokenContext) {
        checkHavePermission(tokenContext,"导出年报");
        String newFileName = "AnnualReportTemp" + ".xlsx";
        UnitEmployeeExcelRes employeeListData = null;
        Integer column = null;//列
        column = 5;
        String code = null;
        String id = exportAnnualReport(code, newFileName, employeeListData, column);
        CommonResponses commonResponses = new CommonResponses();
        if (id != null) {
            commonResponses.setState("Success");
            commonResponses.setId(id);
        } else {
            commonResponses.setState("Fail");
            commonResponses.setId("");
        }
        return commonResponses;
    }
    public CommonResponses getPaymentHistoryData(TokenContext tokenContext,String dkzh,String hkrqs, String hkrqe) {
        checkHavePermission(tokenContext,"导出还款记录");
        String newFileName = "PaymentHistoryTemp" + dkzh+ ".xlsx";
        PaymentHistoryDataRes paymentHistoryDataRes = iLoanAccountService.getHousingRecordData(tokenContext, dkzh, hkrqs, hkrqe);
        Integer column = 11;//列
        String id = exportPaymentHistoryRecord(dkzh, newFileName, paymentHistoryDataRes, column);
        CommonResponses commonResponses = new CommonResponses();
        if (id != null) {
            commonResponses.setState("Success");
            commonResponses.setId(id);
        } else {
            commonResponses.setState("Fail");
            commonResponses.setId("");
        }
        return commonResponses;
    }
}
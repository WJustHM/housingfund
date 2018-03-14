package com.handge.housingfund.others.service;

import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.CommonMessage;
import com.handge.housingfund.common.service.collection.model.individual.ImportExcelErrorListRes;
import com.handge.housingfund.common.service.collection.model.individual.ImportExcelRes;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctSet;
import com.handge.housingfund.common.service.collection.service.unitdeposit.PersonRadix;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctSet;
import com.handge.housingfund.common.service.others.IReadExcelUtilsService;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.ICFileDAO;
import com.handge.housingfund.database.dao.ICRoleDAO;
import com.handge.housingfund.database.entities.CFile;
import com.handge.housingfund.database.entities.CPermission;
import com.handge.housingfund.database.entities.CRole;
import org.apache.commons.configuration2.Configuration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by sjw on 2017/10/23.
 */
public class ReadExcelUtilsServiceImpl implements IReadExcelUtilsService {
    @Autowired
    private UnitAcctSet unitAcctSet;
    @Autowired
    private IndiAcctSet indiAcctSet;
    @Autowired
    private PersonRadix personRadix;
    @Autowired
    ICFileDAO fileDAO;
    @Autowired
    ICRoleDAO iRoleDAO;


    private Logger logger = LoggerFactory.getLogger(ReadExcelUtilsServiceImpl.class);
    private Workbook wb;
    private Sheet sheet;
    private Row row;
    static Configuration config = Configure.getInstance().getConfiguration("pdf");

    public void init(String filepath) throws Exception {
        if(filepath==null){
            return;
        }
        //String ext = filepath.substring(filepath.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(filepath);
//            if(".xls".equals(ext)){
//                this.wb = new HSSFWorkbook(is);
//            }else if(".xlsx".equals(ext)){
//                wb = new XSSFWorkbook(is);
//            }else{
//                this.wb=null;
//            }
            this.wb = new XSSFWorkbook(is);
        } catch (FileNotFoundException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"没找到文件资源");
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        if(this.wb==null){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"文件内容为空");
        }
    }

    /**
     * 读取Excel表格表头的内容
     * @return String 表头内容的数组
     * @author zengwendong
     */
    public String[] readExcelTitle() throws Exception{
        if(wb==null){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"文件内容为空");
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            // title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = row.getCell(i).getCellFormula();
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     * @return Map 包含单元格数据内容的Map对象
     * @author zengwendong
     */
    public Map<Integer, Map<Integer,Object>> readExcelContent() throws Exception{
        if(this.wb==null){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"文件内容为空");
        }
        Map<Integer, Map<Integer,Object>> content = new HashMap<Integer, Map<Integer,Object>>();

        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer,Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {
               Object obj = getCellFormatValue(row.getCell(j));
               cellValue.put(j, obj);
                j++;
            }
            content.put(i, cellValue);
        }
        return content;
    }

    /**
     *
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     * @author zengwendong
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        // data格式是带时分秒的：2013-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();
                        // data格式是不带带时分秒的：2013-7-10
                         Date date = cell.getDateCellValue();
                         cellvalue = date;
                    } else {// 如果是纯数字

                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    public ImportExcelRes getImportUnitAcctInfo(TokenContext tokenContext, String id) {
        if(id==null||id.equals("")){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"文件标识ID");
        }
        try {
            boolean havePermission = false;
            CRole role = iRoleDAO.get(tokenContext.getRoleList().get(0));
            if (role == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "角色不存在");
            for (CPermission permission : role.getPermissions()) {
                if(permission.getPermission_name().equals("导入单位信息")){
                    havePermission = true;
                }
            }
            if(havePermission==false){
                throw new ErrorException(ReturnEnumeration.User_Defined, "没有操作权限,请分配权限");
            }
            CFile cFile = fileDAO.get(id);
            if(cFile==null){
                throw new ErrorException(ReturnEnumeration.Parameter_MISS,"未找到指定路径的文件");
            }
            String type = cFile.getType().toString();
            if(!type.equals("XLSX")){
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"格式错误，请重新上传xlsx文件");
            }
            String Path = null;
            String filepath = config.getString("file_path");
            String cFilePath = cFile.getPath();
            Path = filepath + cFilePath+ "/" + id;
            //String filepath = "D:/ExcelTemp/毕节市住房公积金单位开户登记申请表.xlsx";
            ReadExcelUtilsServiceImpl readExcelUtils = new ReadExcelUtilsServiceImpl();
            readExcelUtils.init(Path);
            Map<Integer, Map<Integer, Object>> map = readExcelUtils.readExcelContent();
            System.out.println("获得Excel表格的内容:");
            CommonMessage result = unitAcctSet.addImportAcctInfo(tokenContext , map);
            ImportExcelRes importExcelRes = new ImportExcelRes();
            ImportExcelErrorListRes excelErrorListRes = new ImportExcelErrorListRes();
            if(result.getMessage().equals("Success")){
                importExcelRes.setSuccess_num("1");
                importExcelRes.setFail_num("0");
            }else{
                importExcelRes.setSuccess_num("0");
                importExcelRes.setFail_num("1");
                ArrayList arrayList= new ArrayList();
                excelErrorListRes.setName(String.valueOf(map.get(1).get(1)));
                excelErrorListRes.setMes(result.getMessage());
                excelErrorListRes.setStatus("fail");
                arrayList.add(excelErrorListRes);
                importExcelRes.setImportExcelErrorListRes(arrayList);

            }
            return importExcelRes;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public ImportExcelRes getImportIndiAcctInfo(TokenContext tokenContext, String id) {
        try {
            boolean havePermission = false;
            CRole role = iRoleDAO.get(tokenContext.getRoleList().get(0));
            if (role == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有操作权限,请分配权限");
            for (CPermission permission : role.getPermissions()) {
                if(permission.getPermission_name().equals("导入个人信息")){
                    havePermission = true;
                }
            }
            if(havePermission==false){
                throw new ErrorException(ReturnEnumeration.User_Defined, "没有操作权限,请分配权限");
            }
            CFile cFile = fileDAO.get(id);
            if(cFile==null){
                throw new ErrorException(ReturnEnumeration.Parameter_MISS,"未找到指定路径的文件");
            }
            String type = cFile.getType().toString();
            if(!type.equals("XLSX")){
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"格式错误，请重新上传xlsx文件");
            }
            String Path = null;
            String filepath = config.getString("file_path");
            String cFilePath = cFile.getPath();
            Path = filepath + cFilePath+ "/" + id;
//          String filepath = "D:/ExcelTemp/毕节市住房公积金个人账户设立申请表.xlsx";
            ReadExcelUtilsServiceImpl readExcelUtils = new ReadExcelUtilsServiceImpl();
            readExcelUtils.init(Path);
            Map<Integer, Map<Integer, Object>> map = readExcelUtils.readExcelContent();
            System.out.println("获得Excel表格的内容:");
            ImportExcelRes result = indiAcctSet.addImportIndiAcctInfo(id,tokenContext , map);
            return result;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public ImportExcelRes getImportPersonRadix(TokenContext tokenContext, String id) {
        try {
            boolean havePermission = false;
            CRole role = iRoleDAO.get(tokenContext.getRoleList().get(0));
            if (role == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有操作权限,请分配权限");
            for (CPermission permission : role.getPermissions()) {
                if(permission.getPermission_name().equals("导入个人基数调整")){
                    havePermission = true;
                }
            }
            if(havePermission==false){
                throw new ErrorException(ReturnEnumeration.User_Defined, "没有操作权限,请分配权限");
            }
            CFile cFile = fileDAO.get(id);
            if (cFile == null) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "未找到指定路径的文件!");
            }
            String type = cFile.getType().toString();
            if (!type.equals("XLSX")) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "文件格式不对，请重新上传xlsx文件!");
            }
            String Path = null;
            String filepath = config.getString("file_path");
            String cFilePath = cFile.getPath();
            Path = filepath + cFilePath + "/" + id;
//          String filepath = "D:/ExcelTemp/毕节市住房公积金缴存基数调整表.xlsx";
            ReadExcelUtilsServiceImpl readExcelUtils = new ReadExcelUtilsServiceImpl();
            readExcelUtils.init(Path);
            Map<Integer, Map<Integer, Object>> map = readExcelUtils.readExcelContent();
            System.out.println("获得Excel表格的内容:");
            ImportExcelRes result = personRadix.saveImportRadix(id,tokenContext, map);
            return result;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }
}


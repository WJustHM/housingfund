package com.handge.housingfund.common.service.others.model;

import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.loan.model.EstateIdGetLDXXEstateDetail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sunjianwen on 2017/9/25.
 */
@XmlRootElement(name = "FilePdfData")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilePdfData implements Serializable {

    private ArrayList<String> filePathArray;//文件路径列表

    private ArrayList<HeadRemittanceInventoryResDWHJQC> dwhjqcArrayList; //清册列表

    private ArrayList<HeadUnitPayCallReceiptResCJJL> CJJLArrayList; //催缴记录列表

    private ArrayList<HeadUnitRemittanceReceiptResDWHJQC>  dqhjxqArrayList; //汇缴列表

    private ArrayList<HeadUnitPayWrongReceiptResGZXX>  gzxxArrayList; //更正信息列表

    private ArrayList<HeadUnitPayBackReceiptResBJXX>  personDetail; //个体信息列表
    private ArrayList<EstateIdGetLDXXEstateDetail> estateIdGetLDXXEstateList;//楼栋信息

    public ArrayList<EstateIdGetLDXXEstateDetail> getEstateIdGetLDXXEstateList() {
        return estateIdGetLDXXEstateList;
    }

    public void setEstateIdGetLDXXEstateList(ArrayList<EstateIdGetLDXXEstateDetail> estateIdGetLDXXEstateList) {
        this.estateIdGetLDXXEstateList = estateIdGetLDXXEstateList;
    }
    private ArrayList<HeadPersonRadixResJCJSTZXX>  jcjstzxxArrayList; //基数调整列表信息

    public ArrayList<HeadPersonRadixResJCJSTZXX> getJcjstzxxArrayList() {
        return jcjstzxxArrayList;
    }

    public void setJcjstzxxArrayList(ArrayList<HeadPersonRadixResJCJSTZXX> jcjstzxxArrayList) {
        this.jcjstzxxArrayList = jcjstzxxArrayList;
    }

    public ArrayList<HeadUnitPayBackReceiptResBJXX> getPersonDetail() {
        return personDetail;
    }

    public void setPersonDetail(ArrayList<HeadUnitPayBackReceiptResBJXX> personDetail) {
        this.personDetail = personDetail;
    }

    public ArrayList<HeadUnitPayWrongReceiptResGZXX> getGzxxArrayList() {
        return gzxxArrayList;
    }

    public void setGzxxArrayList(ArrayList<HeadUnitPayWrongReceiptResGZXX> gzxxArrayList) {
        this.gzxxArrayList = gzxxArrayList;
    }








    public ArrayList<HeadUnitRemittanceReceiptResDWHJQC> getDqhjxqArrayList() {
        return dqhjxqArrayList;
    }

    public void setDqhjxqArrayList(ArrayList<HeadUnitRemittanceReceiptResDWHJQC> dqhjxqArrayList) {
        this.dqhjxqArrayList = dqhjxqArrayList;
    }

    public ArrayList<HeadUnitPayCallReceiptResCJJL> getCJJLArrayList() {
        return CJJLArrayList;
    }

    public void setCJJLArrayList(ArrayList<HeadUnitPayCallReceiptResCJJL> CJJLArrayList) {
        this.CJJLArrayList = CJJLArrayList;
    }

    public ArrayList<HeadRemittanceInventoryResDWHJQC> getDwhjqcArrayList() {
        return dwhjqcArrayList;
    }

    public void setDwhjqcArrayList(ArrayList<HeadRemittanceInventoryResDWHJQC> dwhjqcArrayList) {
        this.dwhjqcArrayList = dwhjqcArrayList;
    }

    public ArrayList<String> getFilePathArray() {
        return filePathArray;
    }

    public void setFilePathArray(ArrayList<String> filePathArray) {
        this.filePathArray = filePathArray;
    }

}

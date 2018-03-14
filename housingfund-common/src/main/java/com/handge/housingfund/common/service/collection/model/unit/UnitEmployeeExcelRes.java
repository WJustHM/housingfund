package com.handge.housingfund.common.service.collection.model.unit;

import com.handge.housingfund.common.service.collection.model.individual.ListEmployeeRes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

@XmlRootElement(name = "UnitEmployeeExcelRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitEmployeeExcelRes implements Serializable{

    private  String    DWMC;  //单位名称

    private  String    DWZH;  //单位账号

    private  ArrayList<ListEmployeeRes> listEmployeeRes; //员工信息

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String DWZH) {
        this.DWZH = DWZH;
    }

    public ArrayList<ListEmployeeRes> getListEmployeeRes() {
        return listEmployeeRes;
    }

    public void setListEmployeeRes(ArrayList<ListEmployeeRes> listEmployeeRes) {
        this.listEmployeeRes = listEmployeeRes;
    }
}

package com.handge.housingfund.common.service.others.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sjw on 2017/12/5.
 */

@XmlRootElement(name = "MonitorDataRes")
@XmlAccessorType(XmlAccessType.FIELD)

public class MonitorDataRes  implements Serializable {

    private String Total_num = "";//总条数

    private String Suc_num = "";//成功数

    private String Mes = "";//反馈信息

    public String getTotal_num() {
        return Total_num;
    }

    public void setTotal_num(String total_num) {
        Total_num = total_num;
    }

    public String getSuc_num() {
        return Suc_num;
    }

    public void setSuc_num(String suc_num) {
        Suc_num = suc_num;
    }

    public String getMes() {
        return Mes;
    }

    public void setMes(String mes) {
        Mes = mes;
    }
}

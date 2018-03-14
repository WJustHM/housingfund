package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sjw on 2018/1/30.
 */
@XmlRootElement(name = "PaymentHistoryDataRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentHistoryDataRes  implements Serializable {

    private ArrayList<HousingRecordListGetInformation> housingRecordListGetInformations; //还款记录

    private String YWWD; //业务网点

    private String JKRXM;   //借款人姓名

    private String JKRZJLX; //借款人证件类型

    private String JKRZJHM; //借款人证件号码

    private String DKZH;    //贷款账号

    public ArrayList<HousingRecordListGetInformation> getHousingRecordListGetInformations() {
        return housingRecordListGetInformations;
    }

    public void setHousingRecordListGetInformations(ArrayList<HousingRecordListGetInformation> housingRecordListGetInformations) {
        this.housingRecordListGetInformations = housingRecordListGetInformations;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getJKRZJLX() {
        return JKRZJLX;
    }

    public void setJKRZJLX(String JKRZJLX) {
        this.JKRZJLX = JKRZJLX;
    }

    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }
}

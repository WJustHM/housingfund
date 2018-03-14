package com.handge.housingfund.common.service.others.model;

import com.handge.housingfund.common.service.finance.model.VoucherManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@XmlRootElement(name = "SingleDictionaryDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherTransmissionData implements Serializable {

    private ArrayList savePath;

    private HashMap<String,VoucherManager> dataMap;

    public ArrayList getSavePath() {
        return savePath;
    }

    public void setSavePath(ArrayList savePath) {
        this.savePath = savePath;
    }

    public HashMap<String, VoucherManager> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<String, VoucherManager> dataMap) {
        this.dataMap = dataMap;
    }
}

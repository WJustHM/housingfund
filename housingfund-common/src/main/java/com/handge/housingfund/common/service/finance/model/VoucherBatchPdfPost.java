package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sjw on 2017/11/16.
 */
@XmlRootElement(name = "批量提交凭证号")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherBatchPdfPost implements Serializable {

    private static final long serialVersionUID = 1159192187200997639L;

    private ArrayList<String> JZPZHS;//  记账凭证号数组

    public ArrayList<String> getJZPZHS() {
        return JZPZHS;
    }

    public void setJZPZHS(ArrayList<String> JZPZHS) {
        this.JZPZHS = JZPZHS;
    }


}

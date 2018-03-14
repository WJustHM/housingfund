package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Funnyboy on 2017/8/10.
 */
@XmlRootElement(name = "RepaymentGetInfomation")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentGetInfomation  implements Serializable {
    private static final long serialVersionUID = -2715517715044705376L;
    private  String JKRXM;
   private  String YQKZJ;
   private ArrayList<RepaymentGetInfomationList> repaymentGetInfomationList;

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getYQKZJ() {
        return YQKZJ;
    }

    public void setYQKZJ(String YQKZJ) {
        this.YQKZJ = YQKZJ;
    }

    public ArrayList<RepaymentGetInfomationList> getRepaymentGetInfomationList() {
        return repaymentGetInfomationList;
    }

    public void setRepaymentGetInfomationList(ArrayList<RepaymentGetInfomationList> repaymentGetInfomationList) {
        this.repaymentGetInfomationList = repaymentGetInfomationList;
    }
}

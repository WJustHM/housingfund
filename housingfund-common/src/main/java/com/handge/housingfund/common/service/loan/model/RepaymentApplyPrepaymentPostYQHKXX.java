package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "RepaymentApplyPrepaymentPostYQHKXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentPostYQHKXX  implements Serializable {


    private ArrayList<RepaymentApplyPrepaymentPostYQHKXXYQXX> YQXX;  //

    private String YQKZJ;  //逾期款总计

    private String JKRZJHM;  //贷款账号

    private String BCHKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private String JKRXM;  //借款人姓名





    public ArrayList<RepaymentApplyPrepaymentPostYQHKXXYQXX> getYQXX() {

        return this.YQXX;

    }


    public void setYQXX(ArrayList<RepaymentApplyPrepaymentPostYQHKXXYQXX> YQXX) {

        this.YQXX = YQXX;

    }


    public String getYQKZJ() {

        return this.YQKZJ;

    }


    public void setYQKZJ(String YQKZJ) {

        this.YQKZJ = YQKZJ;

    }


    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public String getBCHKJE() {
        return BCHKJE;
    }

    public void setBCHKJE(String BCHKJE) {
        this.BCHKJE = BCHKJE;
    }

    public String getYDKKRQ() {

        return this.YDKKRQ;

    }


    public void setYDKKRQ(String YDKKRQ) {

        this.YDKKRQ = YDKKRQ;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }



}
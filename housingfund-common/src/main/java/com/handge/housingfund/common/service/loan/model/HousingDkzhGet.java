package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingDkzhGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingDkzhGet  implements Serializable {

    private String DKZHZT;  //贷款账户状态

    private String DQJHGHBJ;  //当期计划归还本金

    private String HSLXZE;  //回收利息总额

    private String DQJHGHLX;  //当期计划会还利息

    private Integer DQYQQS;  //当前逾期期数

    private String YQBJZE;  //逾期本金总额

    private Integer SYQS;  //剩余期数

    private Integer DKQS;  //贷款期数

    private String DQYHFX;  //当期应还罚息

    private String JKRZJHM;  //借款人证件号码

    private String FXZE;  //罚息总额

    private String JKHTBH;  //借款合同编号

    private String JKRXM;  //借款人姓名

    private String Status;  //状态

    private String JKRZJLX;  //借款人证件类型

    private String DKZH;  //贷款账号

    private String DKLL;  //贷款利率

    private String DKFFRQ;  //贷款发放日期

    private String DKFXDJ;  //贷款风险等级

    private String DQYHLX;  //当期应还利息

    private String YQLXZE;  //逾期利息总额

    private String LLFDBL;  //利率浮动比例

    private String DKYE;  //贷款余额

    private String DKJQRQ;  //贷款结清日期

    private String DKFFE;  //贷款发放额

    private String DQYHJE;  //当期应还金额

    private String DQJHHKJE;  //当期计划还款金额

    private String DQYHBJ;  //当前应还本金

    private String TQGHBJZE;  //提前归还本金总额

    private String HSBJZE;  //回收本金总额

    private Integer LJYQQS;  //累计逾期期数

    public String getDKZHZT() {
        return DKZHZT;
    }

    public void setDKZHZT(String DKZHZT) {
        this.DKZHZT = DKZHZT;
    }

    public String getDQJHGHBJ() {

        return this.DQJHGHBJ;

    }


    public void setDQJHGHBJ(String DQJHGHBJ) {

        this.DQJHGHBJ = DQJHGHBJ;

    }


    public String getHSLXZE() {

        return this.HSLXZE;

    }


    public void setHSLXZE(String HSLXZE) {

        this.HSLXZE = HSLXZE;

    }


    public String getDQJHGHLX() {

        return this.DQJHGHLX;

    }


    public void setDQJHGHLX(String DQJHGHLX) {

        this.DQJHGHLX = DQJHGHLX;

    }


    public Integer getDQYQQS() {

        return this.DQYQQS;

    }


    public void setDQYQQS(Integer DQYQQS) {

        this.DQYQQS = DQYQQS;

    }


    public String getYQBJZE() {

        return this.YQBJZE;

    }


    public void setYQBJZE(String YQBJZE) {

        this.YQBJZE = YQBJZE;

    }


    public Integer getSYQS() {

        return this.SYQS;

    }


    public void setSYQS(Integer SYQS) {

        this.SYQS = SYQS;

    }


    public Integer getDKQS() {

        return this.DKQS;

    }


    public void setDKQS(Integer DKQS) {

        this.DKQS = DKQS;

    }


    public String getDQYHFX() {

        return this.DQYHFX;

    }


    public void setDQYHFX(String DQYHFX) {

        this.DQYHFX = DQYHFX;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getFXZE() {

        return this.FXZE;

    }


    public void setFXZE(String FXZE) {

        this.FXZE = FXZE;

    }


    public String getJKHTBH() {

        return this.JKHTBH;

    }


    public void setJKHTBH(String JKHTBH) {

        this.JKHTBH = JKHTBH;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getStatus() {

        return this.Status;

    }


    public void setStatus(String Status) {

        this.Status = Status;

    }


    public String getJKRZJLX() {

        return this.JKRZJLX;

    }


    public void setJKRZJLX(String JKRZJLX) {

        this.JKRZJLX = JKRZJLX;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getDKLL() {

        return this.DKLL;

    }


    public void setDKLL(String DKLL) {

        this.DKLL = DKLL;

    }


    public String getDKFFRQ() {

        return this.DKFFRQ;

    }


    public void setDKFFRQ(String DKFFRQ) {

        this.DKFFRQ = DKFFRQ;

    }


    public String getDKFXDJ() {

        return this.DKFXDJ;

    }


    public void setDKFXDJ(String DKFXDJ) {

        this.DKFXDJ = DKFXDJ;

    }


    public String getDQYHLX() {

        return this.DQYHLX;

    }


    public void setDQYHLX(String DQYHLX) {

        this.DQYHLX = DQYHLX;

    }


    public String getYQLXZE() {

        return this.YQLXZE;

    }


    public void setYQLXZE(String YQLXZE) {

        this.YQLXZE = YQLXZE;

    }


    public String getLLFDBL() {

        return this.LLFDBL;

    }


    public void setLLFDBL(String LLFDBL) {

        this.LLFDBL = LLFDBL;

    }


    public String getDKYE() {

        return this.DKYE;

    }


    public void setDKYE(String DKYE) {

        this.DKYE = DKYE;

    }


    public String getDKJQRQ() {

        return this.DKJQRQ;

    }


    public void setDKJQRQ(String DKJQRQ) {

        this.DKJQRQ = DKJQRQ;

    }


    public String getDKFFE() {

        return this.DKFFE;

    }


    public void setDKFFE(String DKFFE) {

        this.DKFFE = DKFFE;

    }


    public String getDQYHJE() {

        return this.DQYHJE;

    }


    public void setDQYHJE(String DQYHJE) {

        this.DQYHJE = DQYHJE;

    }


    public String getDQJHHKJE() {

        return this.DQJHHKJE;

    }


    public void setDQJHHKJE(String DQJHHKJE) {

        this.DQJHHKJE = DQJHHKJE;

    }


    public String getDQYHBJ() {

        return this.DQYHBJ;

    }


    public void setDQYHBJ(String DQYHBJ) {

        this.DQYHBJ = DQYHBJ;

    }


    public String getTQGHBJZE() {

        return this.TQGHBJZE;

    }


    public void setTQGHBJZE(String TQGHBJZE) {

        this.TQGHBJZE = TQGHBJZE;

    }


    public String getHSBJZE() {

        return this.HSBJZE;

    }


    public void setHSBJZE(String HSBJZE) {

        this.HSBJZE = HSBJZE;

    }


    public Integer getLJYQQS() {

        return this.LJYQQS;

    }


    public void setLJYQQS(Integer LJYQQS) {

        this.LJYQQS = LJYQQS;

    }

    @Override
    public String toString() {
        return "HousingDkzhGet{" +
                "DKZHZT='" + DKZHZT + '\'' +
                ", DQJHGHBJ='" + DQJHGHBJ + '\'' +
                ", HSLXZE='" + HSLXZE + '\'' +
                ", DQJHGHLX='" + DQJHGHLX + '\'' +
                ", DQYQQS=" + DQYQQS +
                ", YQBJZE='" + YQBJZE + '\'' +
                ", SYQS=" + SYQS +
                ", DKQS=" + DKQS +
                ", DQYHFX='" + DQYHFX + '\'' +
                ", JKRZJHM='" + JKRZJHM + '\'' +
                ", FXZE='" + FXZE + '\'' +
                ", JKHTBH='" + JKHTBH + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", Status='" + Status + '\'' +
                ", JKRZJLX='" + JKRZJLX + '\'' +
                ", DKZH='" + DKZH + '\'' +
                ", DKLL='" + DKLL + '\'' +
                ", DKFFRQ='" + DKFFRQ + '\'' +
                ", DKFXDJ='" + DKFXDJ + '\'' +
                ", DQYHLX='" + DQYHLX + '\'' +
                ", YQLXZE='" + YQLXZE + '\'' +
                ", LLFDBL='" + LLFDBL + '\'' +
                ", DKYE='" + DKYE + '\'' +
                ", DKJQRQ='" + DKJQRQ + '\'' +
                ", DKFFE='" + DKFFE + '\'' +
                ", DQYHJE='" + DQYHJE + '\'' +
                ", DQJHHKJE='" + DQJHHKJE + '\'' +
                ", DQYHBJ='" + DQYHBJ + '\'' +
                ", TQGHBJZE='" + TQGHBJZE + '\'' +
                ", HSBJZE='" + HSBJZE + '\'' +
                ", LJYQQS=" + LJYQQS +
                '}';
    }
}
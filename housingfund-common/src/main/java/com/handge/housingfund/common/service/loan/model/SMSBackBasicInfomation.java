
package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.service.util.ErrorException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Funnyboy on 2017/12/15.
 */
@XmlRootElement(name = "BackBasicInfomation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SMSBackBasicInfomation implements Serializable {
    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private BigDecimal DKYE;
    private BigDecimal HSBJZE;
    private BigDecimal DKFFE;
    private BigDecimal DKQS;
    private BigDecimal DQYHBJ;
    private BigDecimal DQYHLX;
    private BigDecimal DKLL;
    private Date DKFFRQ;
    private BigDecimal DQYHJE;
    private BigDecimal LJYQQS;
    private BigDecimal YQBJZE;
    private BigDecimal YQLXZE;
    private BigDecimal HSLXZE;
    private String DKZHZT;
    private BigDecimal YHQS;
    private String DKZH;
    private String JKRXM;
    private Date DKXFFRQ;
    private BigDecimal DQQC;
    private BigDecimal DKGBJHQS;
    private BigDecimal DKGBJHYE;
    private BigDecimal DKYEZCBJ;
    private String DKHKFS;
    private BigDecimal LLFDBL;
    private String ZHKHYHDM;
    private String ZHKHYHMC;
    private String HKZH;
    private String JKRGJJZH;
    private boolean WTKHYJCE;
    private String GTJKRGJJZH;
    private String CLHPIBID;
    private String SHPAID;
    private String SHPLID;
    private String ALHPAEID;
    private String YWWD;
    private String HYZK;
    private String SJHM;

    public String getCLHPIBID() {
        return CLHPIBID;
    }

    public void setCLHPIBID(String CLHPIBID) {
        this.CLHPIBID = CLHPIBID;
    }

    public String getSHPAID() {
        return SHPAID;
    }

    public void setSHPAID(String SHPAID) {
        this.SHPAID = SHPAID;
    }

    public String getSHPLID() {
        return SHPLID;
    }

    public void setSHPLID(String SHPLID) {
        this.SHPLID = SHPLID;
    }

    public String getALHPAEID() {
        return ALHPAEID;
    }

    public void setALHPAEID(String ALHPAEID) {
        this.ALHPAEID = ALHPAEID;
    }

    public BigDecimal getDKYE() {
        return DKYE;
    }

    public void setDKYE(BigDecimal DKYE) {
        this.DKYE = DKYE;
    }


    public BigDecimal getHSBJZE() {
        return HSBJZE;
    }

    public void setHSBJZE(BigDecimal HSBJZE) {
        this.HSBJZE = HSBJZE;
    }

    public BigDecimal getDKFFE() {
        return DKFFE;
    }

    public void setDKFFE(BigDecimal DKFFE) {
        this.DKFFE = DKFFE;
    }

    public BigDecimal getDKQS() {
        return DKQS;
    }

    public void setDKQS(BigDecimal DKQS) {
        this.DKQS = DKQS;
    }

    public BigDecimal getDQYHBJ() {
        return DQYHBJ;
    }

    public void setDQYHBJ(BigDecimal DQYHBJ) {
        this.DQYHBJ = DQYHBJ;
    }

    public BigDecimal getDQYHLX() {
        return DQYHLX;
    }

    public void setDQYHLX(BigDecimal DQYHLX) {
        this.DQYHLX = DQYHLX;
    }

    public BigDecimal getDKLL() {
        return DKLL;
    }

    public void setDKLL(BigDecimal DKLL) {
        this.DKLL = DKLL;
    }

    public Date getDKFFRQ() {
        return DKFFRQ;
    }

    public void setDKFFRQ(Date DKFFRQ) {

        try {
            this.DKFFRQ = sim.parse(sim.format(DKFFRQ));
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public BigDecimal getDQYHJE() {
        return DQYHJE;
    }

    public void setDQYHJE(BigDecimal DQYHJE) {
        this.DQYHJE = DQYHJE;
    }

    public BigDecimal getLJYQQS() {
        return LJYQQS;
    }

    public void setLJYQQS(BigDecimal LJYQQS) {
        this.LJYQQS = LJYQQS;
    }

    public BigDecimal getYQBJZE() {
        return YQBJZE;
    }

    public void setYQBJZE(BigDecimal YQBJZE) {
        this.YQBJZE = YQBJZE;
    }

    public BigDecimal getYQLXZE() {
        return YQLXZE;
    }

    public void setYQLXZE(BigDecimal YQLXZE) {
        this.YQLXZE = YQLXZE;
    }

    public BigDecimal getHSLXZE() {
        return HSLXZE;
    }

    public void setHSLXZE(BigDecimal HSLXZE) {
        this.HSLXZE = HSLXZE;
    }

    public String getDKZHZT() {
        return DKZHZT;
    }

    public void setDKZHZT(String DKZHZT) {
        this.DKZHZT = DKZHZT;
    }

    public BigDecimal getYHQS() {
        return YHQS;
    }

    public void setYHQS(BigDecimal YHQS) {
        this.YHQS = YHQS;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public Date getDKXFFRQ() {
        return DKXFFRQ;
    }

    public void setDKXFFRQ(Timestamp DKXFFRQ) {
        try {
            this.DKXFFRQ = sim.parse(sim.format(DKXFFRQ));
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
    }

    public boolean isWTKHYJCE() {
        return WTKHYJCE;
    }

    public void setWTKHYJCE(boolean WTKHYJCE) {
        this.WTKHYJCE = WTKHYJCE;
    }

    public BigDecimal getDQQC() {
        return DQQC;
    }

    public void setDQQC(BigDecimal DQQC) {
        this.DQQC = DQQC;
    }

    public BigDecimal getDKGBJHQS() {
        return DKGBJHQS;
    }

    public void setDKGBJHQS(BigDecimal DKGBJHQS) {
        this.DKGBJHQS = DKGBJHQS;
    }

    public BigDecimal getDKGBJHYE() {
        return DKGBJHYE;
    }

    public void setDKGBJHYE(BigDecimal DKGBJHYE) {
        this.DKGBJHYE = DKGBJHYE;
    }

    public BigDecimal getDKYEZCBJ() {
        return DKYEZCBJ;
    }

    public void setDKYEZCBJ(BigDecimal DKYEZCBJ) {
        this.DKYEZCBJ = DKYEZCBJ;
    }

    public String getDKHKFS() {
        return DKHKFS;
    }

    public void setDKHKFS(String DKHKFS) {
        this.DKHKFS = DKHKFS;
    }

    public BigDecimal getLLFDBL() {
        return LLFDBL;
    }

    public void setLLFDBL(BigDecimal LLFDBL) {
        this.LLFDBL = LLFDBL;
    }

    public String getZHKHYHDM() {
        return ZHKHYHDM;
    }

    public void setZHKHYHDM(String ZHKHYHDM) {
        this.ZHKHYHDM = ZHKHYHDM;
    }

    public String getZHKHYHMC() {
        return ZHKHYHMC;
    }

    public void setZHKHYHMC(String ZHKHYHMC) {
        this.ZHKHYHMC = ZHKHYHMC;
    }

    public String getHKZH() {
        return HKZH;
    }

    public void setHKZH(String HKZH) {
        this.HKZH = HKZH;
    }

    public String getJKRGJJZH() {
        return JKRGJJZH;
    }

    public void setJKRGJJZH(String JKRGJJZH) {
        this.JKRGJJZH = JKRGJJZH;
    }

    public String getGTJKRGJJZH() {
        return GTJKRGJJZH;
    }

    public void setGTJKRGJJZH(String GTJKRGJJZH) {
        this.GTJKRGJJZH = GTJKRGJJZH;
    }

    public String getHYZK() {
        return HYZK;
    }

    public void setHYZK(String HYZK) {
        this.HYZK = HYZK;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    @Override
    public String toString() {
        return "BackBasicInfomation{" +
                "DKYE=" + DKYE +
                ", HSBJZE=" + HSBJZE +
                ", DKFFE=" + DKFFE +
                ", DKQS=" + DKQS +
                ", DQYHBJ=" + DQYHBJ +
                ", DQYHLX=" + DQYHLX +
                ", DKLL=" + DKLL +
                ", DKFFRQ=" + DKFFRQ +
                ", DQYHJE=" + DQYHJE +
                ", LJYQQS=" + LJYQQS +
                ", YQBJZE=" + YQBJZE +
                ", YQLXZE=" + YQLXZE +
                ", HSLXZE=" + HSLXZE +
                ", DKZHZT='" + DKZHZT + '\'' +
                ", YHQS=" + YHQS +
                ", DKZH='" + DKZH + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", DKXFFRQ=" + DKXFFRQ +
                ", DQQC=" + DQQC +
                ", DKGBJHQS=" + DKGBJHQS +
                ", DKGBJHYE=" + DKGBJHYE +
                ", DKYEZCBJ=" + DKYEZCBJ +
                ", DKHKFS='" + DKHKFS + '\'' +
                ", LLFDBL=" + LLFDBL +
                ", ZHKHYHDM='" + ZHKHYHDM + '\'' +
                ", ZHKHYHMC='" + ZHKHYHMC + '\'' +
                ", HKZH='" + HKZH + '\'' +
                ", JKRGJJZH='" + JKRGJJZH + '\'' +
                ", WTKHYJCE=" + WTKHYJCE +
                ", GTJKRGJJZH='" + GTJKRGJJZH + '\'' +
                ", CLHPIBID='" + CLHPIBID + '\'' +
                ", SHPAID='" + SHPAID + '\'' +
                ", SHPLID='" + SHPLID + '\'' +
                ", ALHPAEID='" + ALHPAEID + '\'' +
                '}';
    }
}

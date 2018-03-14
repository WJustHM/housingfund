package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesPurchaseContractInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesPurchaseContractInformation  implements Serializable {

    private String FWZL;  //房屋坐落

    private String FWZJ;  //房屋总价

    private String JKRZJHM;  //借款人证件号码

    private String DKLX;  //贷款类型 （0：公积金贷款 1：组合贷款 2：贴息贷款 3：其他）

    private String DKQS;  //贷款期数

    private String SFRKHYHMC;  //售房人开户银行名称

    private String JKHTBH;  //借款合同编号

    private String JKRXM;  //借款人姓名

    private String JKRZJLX;  //借款人证件类型

    private String SFRZHHM;  //售房人账户号码

    private String HTDKJE;  //合同贷款金额

    private String JKRGJJZH;  //借款人公积金账号

    private String SWTYHMC;  //受委托银行名称

    private String JKHTQDRQ;  //借款合同签订日期

    private String JKHTLL;  //借款合同利率

    private String DKDBLX;  //贷款担保类型 （0：抵押 1：质押 2：担保 3：其他）

    private String YDFKRQ;  //约定放款日期

    private String FWJZMJ;  //房屋建筑面积

    private String SFRKHHM;  //售房人开户户名

    private String FWTNMJ;  //房屋套内面积

    private String FWXZ;  //房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）

    private String LLFDBL;  //利率浮动比例

    private String DKYT;  //贷款用途

    private String GFSFK;  //购房首付款

    private String YDHKR;  //约定还款日

    private String YDDQRQ;  //约定到期日期

    private String ZHKHYHMC;  //账号开户银行名称

    private String SWTYHDM;  //受委托银行代码

    private String GFHTBH;  //购房合同编号

    private String HKZH;  //还款账号

    private String SFRMC;  //售房人名称

    private String DKHKFS;  //贷款还款方式（0：等额本息 1：等额本金 2：一次性还款付息 3：自由还款方式 4：其他）

    private String ZHKHYHDM;  //账号开户银行代码

    public String getFWZL() {

        return this.FWZL;

    }


    public void setFWZL(String FWZL) {

        this.FWZL = FWZL;

    }


    public String getFWZJ() {

        return this.FWZJ;

    }


    public void setFWZJ(String FWZJ) {

        this.FWZJ = FWZJ;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getDKLX() {

        return this.DKLX;

    }


    public void setDKLX(String DKLX) {

        this.DKLX = DKLX;

    }


    public String getDKQS() {

        return this.DKQS;

    }


    public void setDKQS(String DKQS) {

        this.DKQS = DKQS;

    }


    public String getSFRKHYHMC() {

        return this.SFRKHYHMC;

    }


    public void setSFRKHYHMC(String SFRKHYHMC) {

        this.SFRKHYHMC = SFRKHYHMC;

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


    public String getJKRZJLX() {

        return this.JKRZJLX;

    }


    public void setJKRZJLX(String JKRZJLX) {

        this.JKRZJLX = JKRZJLX;

    }


    public String getSFRZHHM() {

        return this.SFRZHHM;

    }


    public void setSFRZHHM(String SFRZHHM) {

        this.SFRZHHM = SFRZHHM;

    }


    public String getHTDKJE() {

        return this.HTDKJE;

    }


    public void setHTDKJE(String HTDKJE) {

        this.HTDKJE = HTDKJE;

    }


    public String getJKRGJJZH() {

        return this.JKRGJJZH;

    }


    public void setJKRGJJZH(String JKRGJJZH) {

        this.JKRGJJZH = JKRGJJZH;

    }


    public String getSWTYHMC() {

        return this.SWTYHMC;

    }


    public void setSWTYHMC(String SWTYHMC) {

        this.SWTYHMC = SWTYHMC;

    }


    public String getJKHTQDRQ() {

        return this.JKHTQDRQ;

    }


    public void setJKHTQDRQ(String JKHTQDRQ) {

        this.JKHTQDRQ = JKHTQDRQ;

    }


    public String getJKHTLL() {

        return this.JKHTLL;

    }


    public void setJKHTLL(String JKHTLL) {

        this.JKHTLL = JKHTLL;

    }


    public String getDKDBLX() {

        return this.DKDBLX;

    }


    public void setDKDBLX(String DKDBLX) {

        this.DKDBLX = DKDBLX;

    }


    public String getYDFKRQ() {

        return this.YDFKRQ;

    }


    public void setYDFKRQ(String YDFKRQ) {

        this.YDFKRQ = YDFKRQ;

    }


    public String getFWJZMJ() {

        return this.FWJZMJ;

    }


    public void setFWJZMJ(String FWJZMJ) {

        this.FWJZMJ = FWJZMJ;

    }


    public String getSFRKHHM() {

        return this.SFRKHHM;

    }


    public void setSFRKHHM(String SFRKHHM) {

        this.SFRKHHM = SFRKHHM;

    }


    public String getFWTNMJ() {

        return this.FWTNMJ;

    }


    public void setFWTNMJ(String FWTNMJ) {

        this.FWTNMJ = FWTNMJ;

    }


    public String getFWXZ() {

        return this.FWXZ;

    }


    public void setFWXZ(String FWXZ) {

        this.FWXZ = FWXZ;

    }


    public String getLLFDBL() {

        return this.LLFDBL;

    }


    public void setLLFDBL(String LLFDBL) {

        this.LLFDBL = LLFDBL;

    }


    public String getDKYT() {

        return this.DKYT;

    }


    public void setDKYT(String DKYT) {

        this.DKYT = DKYT;

    }


    public String getGFSFK() {

        return this.GFSFK;

    }


    public void setGFSFK(String GFSFK) {

        this.GFSFK = GFSFK;

    }


    public String getYDHKR() {

        return this.YDHKR;

    }


    public void setYDHKR(String YDHKR) {

        this.YDHKR = YDHKR;

    }


    public String getYDDQRQ() {

        return this.YDDQRQ;

    }


    public void setYDDQRQ(String YDDQRQ) {

        this.YDDQRQ = YDDQRQ;

    }


    public String getZHKHYHMC() {

        return this.ZHKHYHMC;

    }


    public void setZHKHYHMC(String ZHKHYHMC) {

        this.ZHKHYHMC = ZHKHYHMC;

    }


    public String getSWTYHDM() {

        return this.SWTYHDM;

    }


    public void setSWTYHDM(String SWTYHDM) {

        this.SWTYHDM = SWTYHDM;

    }


    public String getGFHTBH() {

        return this.GFHTBH;

    }


    public void setGFHTBH(String GFHTBH) {

        this.GFHTBH = GFHTBH;

    }


    public String getHKZH() {

        return this.HKZH;

    }


    public void setHKZH(String HKZH) {

        this.HKZH = HKZH;

    }


    public String getSFRMC() {

        return this.SFRMC;

    }


    public void setSFRMC(String SFRMC) {

        this.SFRMC = SFRMC;

    }


    public String getDKHKFS() {

        return this.DKHKFS;

    }


    public void setDKHKFS(String DKHKFS) {

        this.DKHKFS = DKHKFS;

    }


    public String getZHKHYHDM() {

        return this.ZHKHYHDM;

    }


    public void setZHKHYHDM(String ZHKHYHDM) {

        this.ZHKHYHDM = ZHKHYHDM;

    }


    public String toString() {

        return "GetLoanRecordDetailsResponsesPurchaseContractInformation{" +

                "FWZL='" + this.FWZL + '\'' + "," +
                "FWZJ='" + this.FWZJ + '\'' + "," +
                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "DKLX='" + this.DKLX + '\'' + "," +
                "DKQS='" + this.DKQS + '\'' + "," +
                "SFRKHYHMC='" + this.SFRKHYHMC + '\'' + "," +
                "JKHTBH='" + this.JKHTBH + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' + "," +
                "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
                "SFRZHHM='" + this.SFRZHHM + '\'' + "," +
                "HTDKJE='" + this.HTDKJE + '\'' + "," +
                "JKRGJJZH='" + this.JKRGJJZH + '\'' + "," +
                "SWTYHMC='" + this.SWTYHMC + '\'' + "," +
                "JKHTQDRQ='" + this.JKHTQDRQ + '\'' + "," +
                "JKHTLL='" + this.JKHTLL + '\'' + "," +
                "DKDBLX='" + this.DKDBLX + '\'' + "," +
                "YDFKRQ='" + this.YDFKRQ + '\'' + "," +
                "FWJZMJ='" + this.FWJZMJ + '\'' + "," +
                "SFRKHHM='" + this.SFRKHHM + '\'' + "," +
                "FWTNMJ='" + this.FWTNMJ + '\'' + "," +
                "FWXZ='" + this.FWXZ + '\'' + "," +
                "LLFDBL='" + this.LLFDBL + '\'' + "," +
                "DKYT='" + this.DKYT + '\'' + "," +
                "GFSFK='" + this.GFSFK + '\'' + "," +
                "YDHKR='" + this.YDHKR + '\'' + "," +
                "YDDQRQ='" + this.YDDQRQ + '\'' + "," +
                "ZHKHYHMC='" + this.ZHKHYHMC + '\'' + "," +
                "SWTYHDM='" + this.SWTYHDM + '\'' + "," +
                "GFHTBH='" + this.GFHTBH + '\'' + "," +
                "HKZH='" + this.HKZH + '\'' + "," +
                "SFRMC='" + this.SFRMC + '\'' + "," +
                "DKHKFS='" + this.DKHKFS + '\'' + "," +
                "ZHKHYHDM='" + this.ZHKHYHDM + '\'' +

                "}";

    }
}
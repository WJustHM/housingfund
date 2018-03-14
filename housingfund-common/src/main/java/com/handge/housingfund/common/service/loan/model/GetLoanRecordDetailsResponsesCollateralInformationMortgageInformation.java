package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation  implements Serializable {

    private String UUID;    //数据库中UUID

    private String DYWSYQRSFZHM;  //抵押物所有权人身份证号码

    private String DYFWXS;  //抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他）

    private String DYWFWZL;  //抵押物房屋坐落

    private String DYWMC;  //抵押物名称

    private String FWJG;  //房屋结构（0：框架 1：砖混 2：土木 3：其他）

    private String DYWGYQRLXDH;  //抵押物共有权人联系电话

    private String DYWGYQRXM;   //抵押物共有权人姓名

    private String DYWPGJZ;  //抵押物评估价值

    private String DYWGYQRSFZHM;  //抵押物共有权人身份证号码

    private String DYWSYQRXM;  //抵押物所有权人姓名

    private String DYWSYQRLXDH;  //抵押物所有权人联系电话

    private String FWMJ;  //房屋面积1

    private String QSZSBH;  //权属证书编号

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getDYWSYQRSFZHM() {

        return this.DYWSYQRSFZHM;

    }

    public String getDYWGYQRXM() {
        return DYWGYQRXM;
    }

    public void setDYWGYQRXM(String DYWGYQRXM) {
        this.DYWGYQRXM = DYWGYQRXM;
    }

    public void setDYWSYQRSFZHM(String DYWSYQRSFZHM) {

        this.DYWSYQRSFZHM = DYWSYQRSFZHM;

    }

    public String getDYFWXS() {

        return this.DYFWXS;

    }

    public void setDYFWXS(String DYFWXS) {

        this.DYFWXS = DYFWXS;

    }

    public String getDYWFWZL() {

        return this.DYWFWZL;

    }

    public void setDYWFWZL(String DYWFWZL) {

        this.DYWFWZL = DYWFWZL;

    }

    public String getDYWMC() {

        return this.DYWMC;

    }

    public void setDYWMC(String DYWMC) {

        this.DYWMC = DYWMC;

    }

    public String getFWJG() {

        return this.FWJG;

    }

    public void setFWJG(String FWJG) {

        this.FWJG = FWJG;

    }

    public String getDYWGYQRLXDH() {

        return this.DYWGYQRLXDH;

    }

    public void setDYWGYQRLXDH(String DYWGYQRLXDH) {

        this.DYWGYQRLXDH = DYWGYQRLXDH;

    }

    public String getDYWPGJZ() {

        return this.DYWPGJZ;

    }

    public void setDYWPGJZ(String DYWPGJZ) {

        this.DYWPGJZ = DYWPGJZ;

    }

    public String getDYWGYQRSFZHM() {

        return this.DYWGYQRSFZHM;

    }

    public void setDYWGYQRSFZHM(String DYWGYQRSFZHM) {

        this.DYWGYQRSFZHM = DYWGYQRSFZHM;

    }

    public String getDYWSYQRXM() {

        return this.DYWSYQRXM;

    }

    public void setDYWSYQRXM(String DYWSYQRXM) {

        this.DYWSYQRXM = DYWSYQRXM;

    }

    public String getDYWSYQRLXDH() {

        return this.DYWSYQRLXDH;

    }

    public void setDYWSYQRLXDH(String DYWSYQRLXDH) {

        this.DYWSYQRLXDH = DYWSYQRLXDH;

    }

    public String getFWMJ() {

        return this.FWMJ;

    }

    public void setFWMJ(String FWMJ) {

        this.FWMJ = FWMJ;

    }

    public String getQSZSBH() {

        return this.QSZSBH;

    }

    public void setQSZSBH(String QSZSBH) {

        this.QSZSBH = QSZSBH;

    }

    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation{" +
                "UUID='" + UUID + '\'' +
                ", DYWSYQRSFZHM='" + DYWSYQRSFZHM + '\'' +
                ", DYFWXS='" + DYFWXS + '\'' +
                ", DYWFWZL='" + DYWFWZL + '\'' +
                ", DYWMC='" + DYWMC + '\'' +
                ", FWJG='" + FWJG + '\'' +
                ", DYWGYQRLXDH='" + DYWGYQRLXDH + '\'' +
                ", DYWGYQRXM='" + DYWGYQRXM + '\'' +
                ", DYWPGJZ='" + DYWPGJZ + '\'' +
                ", DYWGYQRSFZHM='" + DYWGYQRSFZHM + '\'' +
                ", DYWSYQRXM='" + DYWSYQRXM + '\'' +
                ", DYWSYQRLXDH='" + DYWSYQRLXDH + '\'' +
                ", FWMJ='" + FWMJ + '\'' +
                ", QSZSBH='" + QSZSBH + '\'' +
                '}';
    }
}
package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesGuaranteeContractInformationMortgageInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesGuaranteeContractInformationMortgageInformation  implements Serializable {

    private String DYWPGZ;  //抵押物评估值

    private String DYQJCRQ;  //抵押权解除日期

    private String DYFWZL;  //抵押房屋坐落

    private String DYWQZH;  //抵押物权证号

    private String DKDBLX;  //贷款担保类型（0：抵押 1：质押 2：担保 3：其他）

    private String DYQJLRQ;  //抵押权建立日期

    private String DYWTXQZH;  //抵押物他项权证号

    public String getDYWPGZ() {

        return this.DYWPGZ;

    }


    public void setDYWPGZ(String DYWPGZ) {

        this.DYWPGZ = DYWPGZ;

    }


    public String getDYQJCRQ() {

        return this.DYQJCRQ;

    }


    public void setDYQJCRQ(String DYQJCRQ) {

        this.DYQJCRQ = DYQJCRQ;

    }


    public String getDYFWZL() {

        return this.DYFWZL;

    }


    public void setDYFWZL(String DYFWZL) {

        this.DYFWZL = DYFWZL;

    }


    public String getDYWQZH() {

        return this.DYWQZH;

    }


    public void setDYWQZH(String DYWQZH) {

        this.DYWQZH = DYWQZH;

    }


    public String getDKDBLX() {

        return this.DKDBLX;

    }


    public void setDKDBLX(String DKDBLX) {

        this.DKDBLX = DKDBLX;

    }


    public String getDYQJLRQ() {

        return this.DYQJLRQ;

    }


    public void setDYQJLRQ(String DYQJLRQ) {

        this.DYQJLRQ = DYQJLRQ;

    }


    public String getDYWTXQZH() {

        return this.DYWTXQZH;

    }


    public void setDYWTXQZH(String DYWTXQZH) {

        this.DYWTXQZH = DYWTXQZH;

    }


    public String toString() {

        return "GetLoanRecordDetailsResponsesGuaranteeContractInformationMortgageInformation{" +

                "DYWPGZ='" + this.DYWPGZ + '\'' + "," +
                "DYQJCRQ='" + this.DYQJCRQ + '\'' + "," +
                "DYFWZL='" + this.DYFWZL + '\'' + "," +
                "DYWQZH='" + this.DYWQZH + '\'' + "," +
                "DKDBLX='" + this.DKDBLX + '\'' + "," +
                "DYQJLRQ='" + this.DYQJLRQ + '\'' + "," +
                "DYWTXQZH='" + this.DYWTXQZH + '\'' +

                "}";

    }
}
package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation  implements Serializable {

    private String UUID;    //数据库中UUID

    private String ZYWSYQRSFZHM;  //质押物所有权人身份证号码

    private String ZYWSYQRXM;  //质押物所有权人姓名

    private String ZYWJZ;  //质押物价值

    private String ZYWSYQRLXDH;  //质押物所有权人联系电话

    private String ZYWMC;  //质押物名称

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getZYWSYQRSFZHM() {

        return this.ZYWSYQRSFZHM;

    }


    public void setZYWSYQRSFZHM(String ZYWSYQRSFZHM) {

        this.ZYWSYQRSFZHM = ZYWSYQRSFZHM;

    }


    public String getZYWSYQRXM() {

        return this.ZYWSYQRXM;

    }


    public void setZYWSYQRXM(String ZYWSYQRXM) {

        this.ZYWSYQRXM = ZYWSYQRXM;

    }


    public String getZYWJZ() {

        return this.ZYWJZ;

    }


    public void setZYWJZ(String ZYWJZ) {

        this.ZYWJZ = ZYWJZ;

    }


    public String getZYWSYQRLXDH() {

        return this.ZYWSYQRLXDH;

    }


    public void setZYWSYQRLXDH(String ZYWSYQRLXDH) {

        this.ZYWSYQRLXDH = ZYWSYQRLXDH;

    }


    public String getZYWMC() {

        return this.ZYWMC;

    }


    public void setZYWMC(String ZYWMC) {

        this.ZYWMC = ZYWMC;

    }

    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation{" +
                "UUID='" + UUID + '\'' +
                ", ZYWSYQRSFZHM='" + ZYWSYQRSFZHM + '\'' +
                ", ZYWSYQRXM='" + ZYWSYQRXM + '\'' +
                ", ZYWJZ='" + ZYWJZ + '\'' +
                ", ZYWSYQRLXDH='" + ZYWSYQRLXDH + '\'' +
                ", ZYWMC='" + ZYWMC + '\'' +
                '}';
    }
}
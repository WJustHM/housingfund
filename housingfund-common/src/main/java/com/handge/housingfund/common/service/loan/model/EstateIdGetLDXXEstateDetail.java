package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstateIdGetLDXXEstateDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateIdGetLDXXEstateDetail  implements Serializable {

    private String YSXKZ; //预售许可证

    private String LDMH;  //楼栋名/号

    private String JGRQ;  //竣工日期

    private String KSDYH;  //开始单元号

    private String BeiZhu;  //备注

    private String XYRQ;  //协议日期

    private String DKBL;  //贷款比例

    private String DYS;  //单元数

    public String getYSXKZ() {
        return YSXKZ;
    }

    public void setYSXKZ(String YSXKZ) {
        this.YSXKZ = YSXKZ;
    }

    public String getLDMH() {

        return this.LDMH;

    }


    public void setLDMH(String LDMH) {

        this.LDMH = LDMH;

    }


    public String getJGRQ() {

        return this.JGRQ;

    }


    public void setJGRQ(String JGRQ) {

        this.JGRQ = JGRQ;

    }


    public String getKSDYH() {

        return this.KSDYH;

    }


    public void setKSDYH(String KSDYH) {

        this.KSDYH = KSDYH;

    }


    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

    }


    public String getXYRQ() {

        return this.XYRQ;

    }


    public void setXYRQ(String XYRQ) {

        this.XYRQ = XYRQ;

    }


    public String getDKBL() {

        return this.DKBL;

    }


    public void setDKBL(String DKBL) {

        this.DKBL = DKBL;

    }


    public String getDYS() {

        return this.DYS;

    }


    public void setDYS(String DYS) {

        this.DYS = DYS;

    }


    public String toString() {

        return "EstateIdGetLDXXEstateDetail{" +

                "LDMH='" + this.LDMH + '\'' + "," +
                "JGRQ='" + this.JGRQ + '\'' + "," +
                "KSDYH='" + this.KSDYH + '\'' + "," +
                "BeiZhu='" + this.BeiZhu + '\'' + "," +
                "XYRQ='" + this.XYRQ + '\'' + "," +
                "DKBL='" + this.DKBL + '\'' + "," +
                "DYS='" + this.DYS + '\'' +

                "}";

    }
}
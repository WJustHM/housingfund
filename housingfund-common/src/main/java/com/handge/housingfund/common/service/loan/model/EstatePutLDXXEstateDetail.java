package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstatePutLDXXEstateDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstatePutLDXXEstateDetail  implements Serializable {

    private String YSXKZ; //预售许可证

    private String LDMH;  //楼栋名/号

    private String JGRQ;  //竣工日期

    @Annotation.Number(name = "开始单元号")
    private String KSDYH;  //开始单元号

    private String BeiZhu;  //备注

    private String XYRQ;  //协议日期

    @Annotation.Ratio(name = "贷款比例")
    private String DKBL;  //贷款比例

    @Annotation.Number(name = "单元数")
    private String DYS;  //单元数


    private String XuHao;  //序号

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


    public String getXuHao() {

        return this.XuHao;

    }


    public void setXuHao(String XuHao) {

        this.XuHao = XuHao;

    }


    public String toString() {

        return "EstatePutLDXXEstateDetail{" +

                "LDMH='" + this.LDMH + '\'' + "," +
                "JGRQ='" + this.JGRQ + '\'' + "," +
                "KSDYH='" + this.KSDYH + '\'' + "," +
                "BeiZhu='" + this.BeiZhu + '\'' + "," +
                "XYRQ='" + this.XYRQ + '\'' + "," +
                "DKBL='" + this.DKBL + '\'' + "," +
                "DYS='" + this.DYS + '\'' + "," +
                "XuHao='" + this.XuHao + '\'' +

                "}";

    }
}
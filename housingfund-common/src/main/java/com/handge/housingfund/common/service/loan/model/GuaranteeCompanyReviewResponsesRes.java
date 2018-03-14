package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GuaranteeCompanyReviewResponsesRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GuaranteeCompanyReviewResponsesRes  implements Serializable {

    private String XuHao;  //序号

    private String YWLSH;  //业务流水号

    private String ZhuangTai;  //状态

    private String LXR;  //联系人

    private String DDSJ;  //到达时间

    private String YWWD;  //业务网点

    private String LXDH;  //联系电话

    private String BLSJ;  //办理时间

    private String CZY;  //操作员

    private String DBGS;  //担保公司

    public String getXuHao() {

        return this.XuHao;

    }


    public void setXuHao(String XuHao) {

        this.XuHao = XuHao;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getZhuangTai() {

        return this.ZhuangTai;

    }


    public void setZhuangTai(String ZhuangTai) {

        this.ZhuangTai = ZhuangTai;

    }


    public String getLXR() {

        return this.LXR;

    }


    public void setLXR(String LXR) {

        this.LXR = LXR;

    }


    public String getDDSJ() {

        return this.DDSJ;

    }


    public void setDDSJ(String DDSJ) {

        this.DDSJ = DDSJ;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getLXDH() {

        return this.LXDH;

    }


    public void setLXDH(String LXDH) {

        this.LXDH = LXDH;

    }


    public String getBLSJ() {

        return this.BLSJ;

    }


    public void setBLSJ(String BLSJ) {

        this.BLSJ = BLSJ;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getDBGS() {

        return this.DBGS;

    }


    public void setDBGS(String DBGS) {

        this.DBGS = DBGS;

    }


    public String toString() {

        return "GuaranteeCompanyReviewResponsesRes{" +

                "XuHao='" + this.XuHao + '\'' + "," +
                "YWLSH='" + this.YWLSH + '\'' + "," +
                "ZhuangTai='" + this.ZhuangTai + '\'' + "," +
                "LXR='" + this.LXR + '\'' + "," +
                "DDSJ='" + this.DDSJ + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "LXDH='" + this.LXDH + '\'' + "," +
                "BLSJ='" + this.BLSJ + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "DBGS='" + this.DBGS + '\'' +

                "}";

    }
}
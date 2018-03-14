package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sjw on 2017/8/17.
 */
@XmlRootElement(name = "GetIndiAcctsInfoDetailsResTQXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctsInfoDetailsResTQXX  implements Serializable{

    private String TQSJ;//提取时间

    private String BCTQJE;//本次提取金额

    private String LJTQJE;//累计提取金额

    private String BLR; //办理人

    private String CZY; //操作员

    private String YWWD; //业务网点

    public String getTQSJ() {
        return TQSJ;
    }

    public void setTQSJ(String TQSJ) {
        this.TQSJ = TQSJ;
    }

    public String getBCTQJE() {
        return BCTQJE;
    }

    public void setBCTQJE(String BCTQJE) {
        this.BCTQJE = BCTQJE;
    }

    public String getLJTQJE() {
        return LJTQJE;
    }

    public void setLJTQJE(String LJTQJE) {
        this.LJTQJE = LJTQJE;
    }

    public String getBLR() {
        return BLR;
    }

    public void setBLR(String BLR) {
        this.BLR = BLR;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String toString(){

        return "GetIndiAcctsInfoDetailsResTQXX{" +

                "TQSJ='" + this.TQSJ + '\'' +
                "BCTQJE='" + this.BCTQJE + '\'' +
                "LJTQJE='" + this.LJTQJE + '\'' +
                "BLR='" + this.BLR + '\'' +
                "CZY='" + this.CZY + '\'' +
                "YWWD='" + this.YWWD + '\'' +

                "}";

    }
}

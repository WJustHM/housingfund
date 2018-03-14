package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2017/10/18.
 */

/*和前端业务详情页面中的审核信息交互*/

@XmlRootElement(name = "DetailsReviewInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailsReviewInfo implements Serializable {

    private String CaoZuo;
    private String SHJG;
    private String YYYJ;
    private String CZY;
    private String ZhiWu;
    private String YWWD;
    private String CZQD;
    private String SHSJ;
    private String BeiZhu;

    @Override
    public String toString() {
        return "DetailsReviewInfo{" +
                "CaoZuo='" + CaoZuo + '\'' +
                ", SHJG='" + SHJG + '\'' +
                ", YYYJ='" + YYYJ + '\'' +
                ", CZY='" + CZY + '\'' +
                ", ZhiWu='" + ZhiWu + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", CZQD='" + CZQD + '\'' +
                ", SHSJ='" + SHSJ + '\'' +
                ", BeiZhu='" + BeiZhu + '\'' +
                '}';
    }

    public String getBeiZhu() {
        return BeiZhu;
    }

    public void setBeiZhu(String beiZhu) {
        BeiZhu = beiZhu;
    }

    public String getCaoZuo() {
        return CaoZuo;
    }

    public void setCaoZuo(String caoZuo) {
        CaoZuo = caoZuo;
    }

    public String getSHJG() {
        return SHJG;
    }

    public void setSHJG(String SHJG) {
        this.SHJG = SHJG;
    }

    public String getYYYJ() {
        return YYYJ;
    }

    public void setYYYJ(String YYYJ) {
        this.YYYJ = YYYJ;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getZhiWu() {
        return ZhiWu;
    }

    public void setZhiWu(String zhiWu) {
        ZhiWu = zhiWu;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getCZQD() {
        return CZQD;
    }

    public void setCZQD(String CZQD) {
        this.CZQD = CZQD;
    }

    public String getSHSJ() {
        return SHSJ;
    }

    public void setSHSJ(String SHSJ) {
        this.SHSJ = SHSJ;
    }
}

package com.handge.housingfund.common.service.review.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/8/14.
 */

/**
 * 后端入库对象，对应表c_audit_history
 */
@XmlRootElement(name = "ReviewInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewInfo implements Serializable {

    private String YWLSH;    //业务流水号

    private String CaoZuo;  //操作

    private String YWLX;    //业务类型

    private String SHJG;    //审核结果（01：通过，02：不通过）EVENT

    private String YYYJ;    //原因（不通过）或意见（通过）

    private String CZY;     //审核员名字

    private String YWWD;    //业务网点名字

    private String ZhiWu;   //职务（操作员角色）

    private String CZQD;    //操作渠道

    private String BeiZhu;  //备注NOTE

    private String QZSJ;     //签证数据

    private Date DDSJ;    //业务到达（审核）的时间

    private Date SHSJ;       //业务审核完成时的时间

    private String SHYBH;   //审核员ID

    public String getYWLX() {
        return YWLX;
    }

    public void setYWLX(String YWLX) {
        this.YWLX = YWLX;
    }

    public String getCaoZuo() {
        return CaoZuo;
    }

    public void setCaoZuo(String caoZuo) {
        CaoZuo = caoZuo;
    }

    public Date getDDSJ() {
        return DDSJ;
    }

    public void setDDSJ(Date DDSJ) {
        this.DDSJ = DDSJ;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getQZSJ() {
        return QZSJ;
    }

    public void setQZSJ(String QZSJ) {
        this.QZSJ = QZSJ;
    }

    public Date getSHSJ() {
        return SHSJ;
    }

    public void setSHSJ(Date SHSJ) {
        this.SHSJ = SHSJ;
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

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getZhiWu() {
        return ZhiWu;
    }

    public void setZhiWu(String zhiWu) {
        ZhiWu = zhiWu;
    }

    public String getCZQD() {
        return CZQD;
    }

    public void setCZQD(String CZQD) {
        this.CZQD = CZQD;
    }

    public String getBeiZhu() {
        return BeiZhu;
    }

    public void setBeiZhu(String beiZhu) {
        BeiZhu = beiZhu;
    }

    public String getSHYBH() {
        return SHYBH;
    }

    public void setSHYBH(String SHYBH) {
        this.SHYBH = SHYBH;
    }

    @Override
    public String toString() {
        return "ReviewInfo{" +
                "YWLSH='" + YWLSH + '\'' +
                ", CaoZuo='" + CaoZuo + '\'' +
                ", YWLX='" + YWLX + '\'' +
                ", SHJG='" + SHJG + '\'' +
                ", YYYJ='" + YYYJ + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", ZhiWu='" + ZhiWu + '\'' +
                ", CZQD='" + CZQD + '\'' +
                ", BeiZhu='" + BeiZhu + '\'' +
                ", QZSJ='" + QZSJ + '\'' +
                ", DDSJ=" + DDSJ +
                ", SHSJ=" + SHSJ +
                ", SHYBH='" + SHYBH + '\'' +
                '}';
    }
}

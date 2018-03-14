package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by 凡 on 2017/10/7.
 */
@XmlRootElement(name = "InventoryDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryDetail implements Serializable,Comparable<InventoryDetail> {

    private String GRZH;    //个人账号

    private String XingMing;    //姓名

    private String ZJHM;    //证件号码

    private String GRJCJS;  //个人缴存基数

    private String DWYJCE;  //单位月缴存额

    private String GRYJCE;  //个人月缴存额

    private String YJCE;    //月缴存额

    private String QCFSE;   //清册发生额

    private String GRZHZT;  //个人账户状态

    private String GRZHYE;  //个人账户余额

    public InventoryDetail() {

    }

    public InventoryDetail(Object[] obj) {

        this.setGRZH(obj[0].toString());
        this.setXingMing(obj[1].toString());
        this.setZJHM(obj[2].toString());
        BigDecimal grjcjs = new BigDecimal(obj[3].toString());
        BigDecimal dwyjce = new BigDecimal(obj[4].toString());
        BigDecimal gryjce = new BigDecimal(obj[5].toString());
        String s2 = obj[6] == null ? "01" : obj[6].toString() ;
        String grzhzt = s2;
        BigDecimal grzhye = new BigDecimal(obj[7].toString());

        BigDecimal heji = dwyjce.add(gryjce);
        BigDecimal qcfse = "01".equals(grzhzt) ? heji: BigDecimal.ZERO;
        this.setGRJCJS(grjcjs.toString());
        this.setDWYJCE(dwyjce.toString());
        this.setGRYJCE(gryjce.toString());
        this.setYJCE(heji.toString());
        this.setQCFSE(qcfse.toString());
        this.setGRZHZT(grzhzt);
        this.setGRZHYE(grzhye.toString());
    }

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getGRJCJS() {
        return GRJCJS;
    }

    public void setGRJCJS(String GRJCJS) {
        this.GRJCJS = GRJCJS;
    }

    public String getDWYJCE() {
        return DWYJCE;
    }

    public void setDWYJCE(String DWYJCE) {
        this.DWYJCE = DWYJCE;
    }

    public String getGRYJCE() {
        return GRYJCE;
    }

    public void setGRYJCE(String GRYJCE) {
        this.GRYJCE = GRYJCE;
    }

    public String getYJCE() {
        return YJCE;
    }

    public void setYJCE(String YJCE) {
        this.YJCE = YJCE;
    }

    public String getQCFSE() {
        return QCFSE;
    }

    public void setQCFSE(String QCFSE) {
        this.QCFSE = QCFSE;
    }

    public String getGRZHZT() {
        return GRZHZT;
    }

    public void setGRZHZT(String GRZHZT) {
        this.GRZHZT = GRZHZT;
    }

    public String getGRZHYE() {
        return GRZHYE;
    }

    public void setGRZHYE(String GRZHYE) {
        this.GRZHYE = GRZHYE;
    }

    @Override
    public String toString() {
        return "InventoryDetail{" +
                "GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", GRJCJS=" + GRJCJS +
                ", DWYJCE=" + DWYJCE +
                ", GRYJCE=" + GRYJCE +
                ", YJCE=" + YJCE +
                ", QCFSE=" + QCFSE +
                ", GRZHZT='" + GRZHZT + '\'' +
                ", GRZHYE=" + GRZHYE +
                '}';
    }


    @Override
    public int compareTo(InventoryDetail o) {
        String grzhzt = this.getGRZHZT();
        if(grzhzt.equals(o.getGRZHZT())){
            return 0;
        }
        if("01".equals(grzhzt)){
            return -1;
        }
        if("02".equals(grzhzt)){
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof InventoryDetail)){
            return false;
        }
        return this.GRZH.equals(((InventoryDetail) obj).GRZH);
    }

    @Override
    public int hashCode() {
        return this.GRZH.hashCode();
    }


    public static class GrzhCompator implements Comparator<InventoryDetail> {
        @Override
        public int compare(InventoryDetail detail, InventoryDetail detail2) {
            return detail.getGRZH().compareTo(detail2.getGRZH());
        }
    }

}

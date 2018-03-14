package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/22.
 */
@XmlRootElement(name = "报表基础信息")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableBase implements Serializable {
    private String Sheng;//省
    private String Di;//地
    private String XZHQDM;//行政划区代码
    private String JGMC;//机关名称
    private String ShiJian;//时间
    private String BiaoHao;//表号
    private String ZDJG;//制定机关
    private String PZJG;//批准机关
    private String PZWH;//批准文号
    private String YXQZ;//有效期至
    private String TBR;//填表人
    private String BCRQ;//报出日期

    public TableBase() {
    }

    public TableBase(String sheng, String di, String XZHQDM, String JGMC, String shiJian, String biaoHao,
                     String ZDJG, String PZJG, String PZWH, String YXQZ, String TBR, String BCRQ) {
        Sheng = sheng;
        Di = di;
        this.XZHQDM = XZHQDM;
        this.JGMC = JGMC;
        ShiJian = shiJian;
        BiaoHao = biaoHao;
        this.ZDJG = ZDJG;
        this.PZJG = PZJG;
        this.PZWH = PZWH;
        this.YXQZ = YXQZ;
        this.TBR = TBR;
        this.BCRQ = BCRQ;
    }

    public String getSheng() {
        return Sheng;
    }

    public void setSheng(String sheng) {
        Sheng = sheng;
    }

    public String getDi() {
        return Di;
    }

    public void setDi(String di) {
        Di = di;
    }

    public String getXZHQDM() {
        return XZHQDM;
    }

    public void setXZHQDM(String XZHQDM) {
        this.XZHQDM = XZHQDM;
    }

    public String getJGMC() {
        return JGMC;
    }

    public void setJGMC(String JGMC) {
        this.JGMC = JGMC;
    }

    public String getShiJian() {
        return ShiJian;
    }

    public void setShiJian(String shiJian) {
        ShiJian = shiJian;
    }

    public String getBiaoHao() {
        return BiaoHao;
    }

    public void setBiaoHao(String biaoHao) {
        BiaoHao = biaoHao;
    }

    public String getZDJG() {
        return ZDJG;
    }

    public void setZDJG(String ZDJG) {
        this.ZDJG = ZDJG;
    }

    public String getPZJG() {
        return PZJG;
    }

    public void setPZJG(String PZJG) {
        this.PZJG = PZJG;
    }

    public String getPZWH() {
        return PZWH;
    }

    public void setPZWH(String PZWH) {
        this.PZWH = PZWH;
    }

    public String getYXQZ() {
        return YXQZ;
    }

    public void setYXQZ(String YXQZ) {
        this.YXQZ = YXQZ;
    }

    public String getTBR() {
        return TBR;
    }

    public void setTBR(String TBR) {
        this.TBR = TBR;
    }

    public String getBCRQ() {
        return BCRQ;
    }

    public void setBCRQ(String BCRQ) {
        this.BCRQ = BCRQ;
    }
}

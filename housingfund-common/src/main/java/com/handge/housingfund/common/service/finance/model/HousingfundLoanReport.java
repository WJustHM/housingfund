package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanyi on 2017/9/29.
 */
@XmlRootElement(name = "住房公积金存贷款报备表")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundLoanReport extends TableBase implements Serializable {

    private ArrayList<HousingfundLoanReportDetail> GRZFGJJ;//个人住房公积金

    private ArrayList<HousingfundLoanReportDetail> QT;//其他

    private ArrayList<HousingfundLoanReportDetail> YEHJ1;//余额合计(1+2)

    private ArrayList<HousingfundLoanReportDetail> ZFGJJCKZH;//住房公积金存款专户

    private ArrayList<HousingfundLoanReportDetail> ZFGJJZZSYZH;//住房公积金增值收益专户

    private ArrayList<HousingfundLoanReportDetail> GRWTDK;//个人委托贷款

    private ArrayList<HousingfundLoanReportDetail> GZTZ;//国债投资

    private ArrayList<HousingfundLoanReportDetail> QTTZ;//其他投资（项目贷款)

    private ArrayList<HousingfundLoanReportDetail> YEHJ2;//余额合计(3+4+5+6+7)

    public HousingfundLoanReport() {
    }

    public HousingfundLoanReport(String sheng, String di, String XZHQDM, String JGMC,
                                 String shiJian, String biaoHao, String ZDJG, String PZJG,
                                 String PZWH, String YXQZ, String TBR, String BCRQ,
                                 ArrayList<HousingfundLoanReportDetail> GRZFGJJ,
                                 ArrayList<HousingfundLoanReportDetail> QT,
                                 ArrayList<HousingfundLoanReportDetail> YEHJ1,
                                 ArrayList<HousingfundLoanReportDetail> ZFGJJCKZH,
                                 ArrayList<HousingfundLoanReportDetail> ZFGJJZZSYZH,
                                 ArrayList<HousingfundLoanReportDetail> GRWTDK,
                                 ArrayList<HousingfundLoanReportDetail> GZTZ,
                                 ArrayList<HousingfundLoanReportDetail> QTTZ, ArrayList<HousingfundLoanReportDetail> YEHJ2) {
        super(sheng, di, XZHQDM, JGMC, shiJian, biaoHao, ZDJG, PZJG, PZWH, YXQZ, TBR, BCRQ);
        this.GRZFGJJ = GRZFGJJ;
        this.QT = QT;
        this.YEHJ1 = YEHJ1;
        this.ZFGJJCKZH = ZFGJJCKZH;
        this.ZFGJJZZSYZH = ZFGJJZZSYZH;
        this.GRWTDK = GRWTDK;
        this.GZTZ = GZTZ;
        this.QTTZ = QTTZ;
        this.YEHJ2 = YEHJ2;
    }

    public ArrayList<HousingfundLoanReportDetail> getGRZFGJJ() {
        return GRZFGJJ;
    }

    public void setGRZFGJJ(ArrayList<HousingfundLoanReportDetail> GRZFGJJ) {
        this.GRZFGJJ = GRZFGJJ;
    }

    public ArrayList<HousingfundLoanReportDetail> getQT() {
        return QT;
    }

    public void setQT(ArrayList<HousingfundLoanReportDetail> QT) {
        this.QT = QT;
    }

    public ArrayList<HousingfundLoanReportDetail> getYEHJ1() {
        return YEHJ1;
    }

    public void setYEHJ1(ArrayList<HousingfundLoanReportDetail> YEHJ1) {
        this.YEHJ1 = YEHJ1;
    }

    public ArrayList<HousingfundLoanReportDetail> getZFGJJCKZH() {
        return ZFGJJCKZH;
    }

    public void setZFGJJCKZH(ArrayList<HousingfundLoanReportDetail> ZFGJJCKZH) {
        this.ZFGJJCKZH = ZFGJJCKZH;
    }

    public ArrayList<HousingfundLoanReportDetail> getZFGJJZZSYZH() {
        return ZFGJJZZSYZH;
    }

    public void setZFGJJZZSYZH(ArrayList<HousingfundLoanReportDetail> ZFGJJZZSYZH) {
        this.ZFGJJZZSYZH = ZFGJJZZSYZH;
    }

    public ArrayList<HousingfundLoanReportDetail> getGRWTDK() {
        return GRWTDK;
    }

    public void setGRWTDK(ArrayList<HousingfundLoanReportDetail> GRWTDK) {
        this.GRWTDK = GRWTDK;
    }

    public ArrayList<HousingfundLoanReportDetail> getGZTZ() {
        return GZTZ;
    }

    public void setGZTZ(ArrayList<HousingfundLoanReportDetail> GZTZ) {
        this.GZTZ = GZTZ;
    }

    public ArrayList<HousingfundLoanReportDetail> getQTTZ() {
        return QTTZ;
    }

    public void setQTTZ(ArrayList<HousingfundLoanReportDetail> QTTZ) {
        this.QTTZ = QTTZ;
    }

    public ArrayList<HousingfundLoanReportDetail> getYEHJ2() {
        return YEHJ2;
    }

    public void setYEHJ2(ArrayList<HousingfundLoanReportDetail> YEHJ2) {
        this.YEHJ2 = YEHJ2;
    }
}

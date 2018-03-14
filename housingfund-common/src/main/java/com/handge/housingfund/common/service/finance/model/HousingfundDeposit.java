package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanyi on 2017/9/7.
 */
@XmlRootElement(name = "住房公积金缴存使用情况表")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundDeposit extends TableBase implements Serializable {

    private ArrayList<HousingfundDepositDetail> list;//报表

    public HousingfundDeposit() {
    }

    public HousingfundDeposit(String sheng, String di, String XZHQDM, String JGMC, String shiJian,
                              String biaoHao, String ZDJG, String PZJG, String PZWH, String YXQZ,
                              String TBR, String BCRQ, ArrayList<HousingfundDepositDetail> list) {
        super(sheng, di, XZHQDM, JGMC, shiJian, biaoHao, ZDJG, PZJG, PZWH, YXQZ, TBR, BCRQ);
        this.list = list;
    }

    public ArrayList<HousingfundDepositDetail> getList() {
        return list;
    }

    public void setList(ArrayList<HousingfundDepositDetail> list) {
        this.list = list;
    }
}

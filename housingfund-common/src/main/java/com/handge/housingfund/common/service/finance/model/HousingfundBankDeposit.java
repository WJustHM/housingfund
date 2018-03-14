package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanyi on 2017/9/22.
 */
@XmlRootElement(name = "住房公积金银行存款情况表")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundBankDeposit extends TableBase implements Serializable {

    private ArrayList<HousingBankDepositDetail> list;

    public HousingfundBankDeposit() {
    }

    public HousingfundBankDeposit(String sheng, String di, String XZHQDM, String JGMC, String shiJian,
                                  String biaoHao, String ZDJG, String PZJG, String PZWH, String YXQZ,
                                  String TBR, String BCRQ, ArrayList<HousingBankDepositDetail> list) {
        super(sheng, di, XZHQDM, JGMC, shiJian, biaoHao, ZDJG, PZJG, PZWH, YXQZ, TBR, BCRQ);
        this.list = list;
    }

    public ArrayList<HousingBankDepositDetail> getList() {
        return list;
    }

    public void setList(ArrayList<HousingBankDepositDetail> list) {
        this.list = list;
    }
}
